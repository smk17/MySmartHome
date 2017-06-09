package com.smk17.mysmarthome.fragment.systemsettings;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.smk17.mysmarthome.Constant;
import com.smk17.mysmarthome.MyApplication;
import com.smk17.mysmarthome.R;
import com.smk17.mysmarthome.activity.SystemSettingsActivity;
import com.smk17.mysmarthome.adapter.SceneControlItemAdapter;
import com.smk17.mysmarthome.adapter.SceneControlItemAdapter.SCMIABtnClickListener;
import com.smk17.mysmarthome.cloud.ExecToCloudSql;
import com.smk17.mysmarthome.cloud.ParseDevice;
import com.smk17.mysmarthome.db.ExecSql;
import com.smk17.mysmarthome.domain.DeviceScene;
import com.smk17.mysmarthome.domain.DeviceSensor;
import com.yh.materialdesign.dialog.MaterialDialog;
import com.yh.materialdesign.views.DropdownButton;
import com.yh.materialdesign.views.DropdownItemObject;
import com.yh.materialdesign.views.DropdownListView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;

public class FragmentSceneManagement extends SystemFragment {
	
	private MyHandler handler = new MyHandler(this);
	private MaterialDialog mLoadDialog;
	private RelativeLayout mRlLoad;
	private ListView deviceList;
	private View mask;
	private DropdownButton chooseMainDevice;
	private DropdownListView dropdownMainDevice;
	private Animation dropdown_in, dropdown_out, dropdown_mask_out;
	private int MainDeviceSelectedId=Constant.ID_MAINDEVICE_ALL;
	private SceneControlItemAdapter adapter;	
	private MaterialDialog alert = null;
	private ArrayList<DeviceScene> dList =  new ArrayList<DeviceScene>();
	private ArrayList<DeviceScene> dAllList = new ArrayList<DeviceScene>();
	private ArrayList<DeviceSensor> MainList = new ArrayList<DeviceSensor>();
	private DropdownButtonsController dropdownButtonsController = new DropdownButtonsController();
	
	public static final FragmentSceneManagement newInstance(){
		FragmentSceneManagement fragment = new FragmentSceneManagement();
		return fragment;		
	}	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.material_fragment_scenemanagement, container, false);  		 
		deviceList = (ListView)view.findViewById(R.id.device_list);
		mRlLoad = (RelativeLayout)view.findViewById(R.id.device_load);
		mask = view.findViewById(R.id.mask);
        chooseMainDevice = (DropdownButton) view.findViewById(R.id.chooseMainDevice);		
        dropdownMainDevice = (DropdownListView) view.findViewById(R.id.dropdownMainDevice);
        
        dropdown_in = AnimationUtils.loadAnimation(getActivity(),R.anim.dropdown_in);
        dropdown_out = AnimationUtils.loadAnimation(getActivity(),R.anim.dropdown_out);
        dropdown_mask_out = AnimationUtils.loadAnimation(getActivity(),R.anim.dropdown_mask_out);
        
        adapter = new SceneControlItemAdapter(dList, getActivity(), SceneControlItemAdapter.TYPE_SceneManagement,scmia);
        deviceList.setAdapter(adapter);
        mLoadDialog = new MaterialDialog(getActivity()).setTitle("正在删除...").setShowProgress(true);
        mask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropdownButtonsController.hide();
            }
        });
        
		switch (MyApplication.getNetMode()) {
		case Constant.Network_Mode:
			//TODO 外网模式下的初始化
			ExecToCloudSql.getAllSceneRunnable(handler);
			ExecToCloudSql.getMainDeviceRunnable(handler);
			break;
		case Constant.Intranet_Mode:
			//TODO 内网模式下的初始化
			break;
		case Constant.HotSpot_Mode:
			//TODO 热点模式下的初始化
			ControllerInit(ExecSql.getMainDevice());
			refresh(ExecSql.getScene());
			break;
		default:
			break;
		}
		return view;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i("onActivityResult", "requestCode="+requestCode+"\n resultCode="+resultCode);
		MainDeviceSelectedId=Constant.ID_MAINDEVICE_ALL;
		switch (MyApplication.getNetMode()) {
		case Constant.Network_Mode:
			//TODO 外网模式下的初始化
			ExecToCloudSql.getMainDeviceRunnable(handler);
			ExecToCloudSql.getAllSceneRunnable(handler);
			break;
		case Constant.Intranet_Mode:
			//TODO 内网模式下的初始化
			break;
		case Constant.HotSpot_Mode:
			//TODO 热点模式下的初始化
			ControllerInit(ExecSql.getMainDevice());
			refresh(ExecSql.getScene());
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private SCMIABtnClickListener scmia = new SCMIABtnClickListener() {
		
		@Override
		public void onFDeleteBtnClick(final DeviceScene scene) {
			switch (MyApplication.getNetMode()) {
			case Constant.Network_Mode:
				//TODO 外网模式下的初始化
				ExecToCloudSql.DeleteSceneRunnable(handler, scene.getID());
				ExecToCloudSql.getAllSceneRunnable(handler,Constant.TASKTIME);
				break;
			case Constant.Intranet_Mode:
				//TODO 内网模式下的初始化
				break;
			case Constant.HotSpot_Mode:
				//TODO 热点模式下的初始化
				ExecSql.DeleteScene(scene);
				refresh(ExecSql.getScene());
				break;
			default:
				break;
			}			
		}

		@Override
		public void onFAddBtnClick(DeviceScene scene) {
			AddDeviceScene();
		}

		@Override
		public void onFEditBtnClick(DeviceScene scene) {
			Intent intent = new Intent();
			intent.setClass(getActivity(), SystemSettingsActivity.class);
			intent.putExtra("Page",SystemSettingsActivity.Page_EditScene);
			intent.putExtra("Id",scene.getID());
			startActivityForResult(intent, 0);
		}
	};
	
	private class DropdownButtonsController implements DropdownListView.Container {

		private DropdownListView currentDropdownList;
        private List<DropdownItemObject> datasetMainDevice = new ArrayList<>();
		@Override
		public void show(DropdownListView listView) {
			// TODO Auto-generated method stub
			 if (currentDropdownList != null) {
	                currentDropdownList.clearAnimation();
	                currentDropdownList.startAnimation(dropdown_out);
	                currentDropdownList.setVisibility(View.GONE);
	                currentDropdownList.setBottonChecked(false);
	            }
	            currentDropdownList = listView;
	            mask.clearAnimation();
	            mask.setVisibility(View.VISIBLE);
	            currentDropdownList.clearAnimation();
	            currentDropdownList.startAnimation(dropdown_in);
	            currentDropdownList.setVisibility(View.VISIBLE);
	            currentDropdownList.setBottonChecked(true);
		}

		@Override
		public void hide() {
			// TODO Auto-generated method stub
			 if (currentDropdownList != null) {
	                currentDropdownList.clearAnimation();
	                currentDropdownList.startAnimation(dropdown_out);
	                currentDropdownList.setBottonChecked(false);
	                mask.clearAnimation();
	                mask.startAnimation(dropdown_mask_out);
	            }
	            currentDropdownList = null;
		}

		@Override
		public void onSelectionChanged(DropdownListView view) {
			// TODO Auto-generated method stub
			switch (view.getId()) {
			case R.id.dropdownMainDevice:
				MainDeviceSelectedId = dropdownMainDevice.getSelectedItemObject().id;
				break;			
			default:
				break;
			}
			dList.clear();
			for (DeviceScene deviceScene : dAllList) {
				if(MainDeviceSelectedId == Constant.ID_MAINDEVICE_ALL ||  MainDeviceSelectedId == deviceScene.getSensorId())
				dList.add(deviceScene);
				adapter.notifyDataSetChanged();
			}
			if(dList.size() <=0 ){
				dList.add(new DeviceScene(0, "添加新的情景模式", 0 ,0,1,9, "该主控暂无情景模式：点击添加",null));
				adapter.notifyDataSetChanged();
			}
		}
		void reset() {
            chooseMainDevice.setChecked(false);

            dropdownMainDevice.setVisibility(View.GONE);
            mask.setVisibility(View.GONE);

            dropdownMainDevice.clearAnimation();
            mask.clearAnimation();
        }
		
		 void init() {
			 reset();
			 int i = 0;
			 int len = MainList.size();
			 datasetMainDevice.clear();
			 if(len>0){
				 datasetMainDevice.add(new DropdownItemObject(Constant.MAINDEVICE_ALL, Constant.ID_MAINDEVICE_ALL, null));
				 for(;i<len;i++){
					 datasetMainDevice.add(new DropdownItemObject(MainList.get(i).getName(), MainList.get(i).getId(), null));
				 }
				 dropdownMainDevice.bind(datasetMainDevice, chooseMainDevice, this, MainDeviceSelectedId);
			 }
			 dropdown_mask_out.setAnimationListener(new Animation.AnimationListener() {
	                @Override
	                public void onAnimationStart(Animation animation) {

	                }

	                @Override
	                public void onAnimationEnd(Animation animation) {
	                    if (currentDropdownList == null) {
	                        reset();
	                    }
	                }

	                @Override
	                public void onAnimationRepeat(Animation animation) {

	                }
	            });
		 }
		
	}
	
	private void AddDeviceScene(){
		if(MainList.size() > 1){
			if(alert !=null)
				alert.show();
		}else if(MainList.size() == 1){			
			Intent intent = new Intent();
			intent.setClass(getActivity(), SystemSettingsActivity.class);
			intent.putExtra("Page",SystemSettingsActivity.Page_AddScene);
			intent.putExtra("Id",MainList.get(0).getId());
			startActivityForResult(intent, 0);
		}
		
	}
	
	private void ControllerInit(ArrayList<DeviceSensor> tList){
		MainList.clear();
		MainList.addAll(tList);
		dropdownButtonsController.init();
		if(MainList.size() > 0){
			ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item2);
			for (int i = 0; i < MainList.size(); i++) {					
				arrayAdapter.add(MainList.get(i).getName());
				arrayAdapter.notifyDataSetChanged();
			}
			ListView listView = new ListView(getActivity());
			@SuppressWarnings("deprecation")
			Drawable drawable = getResources().getDrawable(R.drawable.settings_selector); 
			listView.setSelector(drawable);
			listView.setDividerHeight(0);
			listView.setVerticalScrollBarEnabled(true);
			listView.setAdapter(arrayAdapter);
			alert = new MaterialDialog(getActivity()).setTitle("选择主控").setContentView(listView);
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					alert.dismiss();			
					Intent intent = new Intent();
					intent.setClass(getActivity(), SystemSettingsActivity.class);
					intent.putExtra("Page",SystemSettingsActivity.Page_AddScene);
					intent.putExtra("Id",MainList.get(arg2).getId());
					startActivityForResult(intent, 0);					
				}
			});
			alert.setPositiveButton("取消", new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					alert.dismiss();
				}
			});				
		}
	}
	
	private void refresh(ArrayList<DeviceScene> tmpList){
		dList.clear();
		dAllList.clear();
		dAllList.addAll(tmpList);
		if(tmpList.size() <=0 ){
			dList.add(new DeviceScene(0, "添加新的情景模式", 0 ,0,1,9, "该账号暂无情景模式：点击添加",null));
			adapter.notifyDataSetChanged();
		}else{
			for (DeviceScene deviceScene : tmpList) {
				dList.add(deviceScene);
				adapter.notifyDataSetChanged();
			}
		}					
		if(mLoadDialog!=null&& mLoadDialog.isShow())
			mLoadDialog.dismiss();
		mRlLoad.setVisibility(View.GONE);
		deviceList.setVisibility(View.VISIBLE);
	}
	
	private static class MyHandler extends Handler { 
		//TODO MyHandler类定义
		WeakReference<FragmentSceneManagement> mActivity;
		
		public MyHandler(FragmentSceneManagement Activity){
			mActivity = new WeakReference<FragmentSceneManagement>(Activity);
		}
				
		@Override
		public void handleMessage(Message msg) {			
			final FragmentSceneManagement theActivity = mActivity.get();
			if(msg.obj!=null){
				switch (msg.what) {
				case Constant.MAINDEVICE_ID:
					ArrayList<DeviceSensor> tList = null;
					try {
						tList = ParseDevice.parseMainDevice(msg.obj.toString());
						theActivity.ControllerInit(tList);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case Constant.SCENE_ID:
					ArrayList<DeviceScene> tmpList = ParseDevice.parseSceneControl(msg.obj.toString());
					theActivity.refresh(tmpList);
					break;
				case  Constant.STATUS_ID:
					theActivity.mLoadDialog.show();
					ExecToCloudSql.getAllSceneRunnable(theActivity.handler);
					break;
				default:
					break;
				}
				
			}		
		}
	}

	@Override
	public void onSystemAction() {
		AddDeviceScene();
	};
}
