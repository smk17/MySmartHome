package com.smk17.mysmarthome.fragment.systemsettings;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.smk17.android.tools.ToolAlert;
import com.smk17.mysmarthome.Constant;
import com.smk17.mysmarthome.MyApplication;
import com.smk17.mysmarthome.R;
import com.smk17.mysmarthome.adapter.ZoneManagementItemAdapter;
import com.smk17.mysmarthome.adapter.ZoneManagementItemAdapter.ZMIABtnClickListener;
import com.smk17.mysmarthome.cloud.ExecToCloudSql;
import com.smk17.mysmarthome.cloud.ParseDevice;
import com.smk17.mysmarthome.db.ExecSql;
import com.smk17.mysmarthome.domain.DeviceSensor;
import com.smk17.mysmarthome.domain.DeviceZone;
import com.yh.materialdesign.dialog.MaterialDialog;
import com.yh.materialdesign.edittext.MaterialEditText;
import com.yh.materialdesign.views.ButtonFlat;
import com.yh.materialdesign.views.DropdownButton;
import com.yh.materialdesign.views.DropdownItemObject;
import com.yh.materialdesign.views.DropdownListView;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class FragmentZoneManagement extends SystemFragment {
	
	private MyHandler handler = new MyHandler(this);
	private RelativeLayout mRlLoad;
	private ListView deviceList;
	private View mask;
	private DropdownButton chooseMainDevice;
	private DropdownListView dropdownMainDevice;
	private Animation dropdown_in, dropdown_out, dropdown_mask_out;
	private int MainDeviceSelectedId=Constant.ID_MAINDEVICE_ALL;
	private ZoneManagementItemAdapter adapter;
	private ViewGroup parent;
	private MaterialDialog alert = null;
	private ArrayAdapter<String> arrayAdapter = null;
	private ArrayList<DeviceZone> dList = new ArrayList<DeviceZone>();
	private ArrayList<DeviceZone> dAllList = new ArrayList<DeviceZone>();
	private ArrayList<DeviceSensor> MainList = new ArrayList<DeviceSensor>();
	private DropdownButtonsController dropdownButtonsController = new DropdownButtonsController();
	
	public static final FragmentZoneManagement newInstance(){
		FragmentZoneManagement fragment = new FragmentZoneManagement();
		return fragment;		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.material_fragment_zonemanagement, container, false);  	
		parent = (ViewGroup) view.findViewById(R.id.container);
		deviceList = (ListView)view.findViewById(R.id.device_list);
		mRlLoad = (RelativeLayout)view.findViewById(R.id.device_load);
		mask = view.findViewById(R.id.mask);
        chooseMainDevice = (DropdownButton) view.findViewById(R.id.chooseMainDevice);		
        dropdownMainDevice = (DropdownListView) view.findViewById(R.id.dropdownMainDevice);
        
        dropdown_in = AnimationUtils.loadAnimation(getActivity(),R.anim.dropdown_in);
        dropdown_out = AnimationUtils.loadAnimation(getActivity(),R.anim.dropdown_out);
        dropdown_mask_out = AnimationUtils.loadAnimation(getActivity(),R.anim.dropdown_mask_out);
        
        arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item2);
        adapter = new ZoneManagementItemAdapter(dList, getActivity(), zmia);
        deviceList.setAdapter(adapter);
		switch (MyApplication.getNetMode()) {
		case Constant.Network_Mode:
			//TODO 外网模式下的初始化
//			new Thread(getZoneRunnable).start();
			ExecToCloudSql.getAllZoneRunnable(handler);
			ExecToCloudSql.getMainDeviceRunnable(handler);
			break;
		case Constant.Intranet_Mode:
			//TODO 内网模式下的初始化
			break;
		case Constant.HotSpot_Mode:
			//TODO 热点模式下的初始化
			ArrayList<DeviceZone> tmpList = ExecSql.getZone();
			dList.clear();
			dAllList.clear();
			dAllList.addAll(tmpList);
			dList.addAll(tmpList);
			adapter.notifyDataSetChanged();
			mRlLoad.setVisibility(View.GONE);
			deviceList.setVisibility(View.VISIBLE);
			MainList.addAll(ExecSql.getMainDevice());
			dropdownButtonsController.init();
			break;
		default:
			break;
		}
		return view;
	}
	
	private ZMIABtnClickListener zmia = new ZMIABtnClickListener() {
		
		@Override
		public void onFEditBtnClick(final DeviceZone device) {
			
			switch (MyApplication.getNetMode()) {
			case Constant.Network_Mode:				
				//TODO 外网模式下的初始化
				ExecToCloudSql.UpdateZoneName(String.valueOf(device.getId()), device.getName());
				ExecToCloudSql.getAllZoneRunnable(handler,Constant.TASKTIME);
				break;
			case Constant.Intranet_Mode:
				//TODO 内网模式下的初始化
				break;
			case Constant.HotSpot_Mode:
				//TODO 热点模式下的初始化
				ExecSql.UpdateZoneName(device);
				ArrayList<DeviceZone> tmpList = ExecSql.getZone();
				dList.clear();
				dAllList.clear();
				dAllList.addAll(tmpList);
				dList.addAll(tmpList);
				adapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
					
		}
		
		@Override
		public void onFDeleteBtnClick(final DeviceZone device,final int position) {
			switch (MyApplication.getNetMode()) {
			case Constant.Network_Mode:				
				//TODO 外网模式下的初始化				
				if(position > 0){
					ExecToCloudSql.DeleteZone(String.valueOf(device.getId()),String.valueOf(dList.get(0).getId()));
				}else{
					ExecToCloudSql.DeleteZone(String.valueOf(device.getId()),String.valueOf(dList.get(1).getId()));
				}
				ExecToCloudSql.getAllZoneRunnable(handler,Constant.TASKTIME);
			case Constant.Intranet_Mode:
				//TODO 内网模式下的初始化
				break;
			case Constant.HotSpot_Mode:
				//TODO 热点模式下的初始化
				if(position > 0){
					ExecSql.DeleteZone(device,dList.get(0).getId());
				}else{
					ExecSql.DeleteZone(device,dList.get(1).getId());
				}
				
				ArrayList<DeviceZone> tmpList = ExecSql.getZone();
				dList.clear();
				dAllList.clear();
				dAllList.addAll(tmpList);
				dList.addAll(tmpList);
				adapter.notifyDataSetChanged();
				break;
			default:
				break;
			}
		}
	};
		
	private static class MyHandler extends Handler { 
		//TODO MyHandler类定义
		WeakReference<FragmentZoneManagement> mActivity;
		
		public MyHandler(FragmentZoneManagement Activity){
			mActivity = new WeakReference<FragmentZoneManagement>(Activity);
		}
				
		@Override
		public void handleMessage(Message msg) {			
			final FragmentZoneManagement theActivity = mActivity.get();
			if(msg.obj!=null){
				switch (msg.what) {
				case Constant.MAINDEVICE_ID:
					ArrayList<DeviceSensor> tmpList = null;
					try {
						tmpList = ParseDevice.parseMainDevice(msg.obj.toString());
						theActivity.MainList.clear();
						theActivity.MainList.addAll(tmpList);
						theActivity.dropdownButtonsController.init();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case Constant.DEVICEZONE_ID:
					try {
						ArrayList<DeviceZone> tmpList1 = ParseDevice.parseDeviceZone(msg.obj.toString());
						theActivity.dList.clear();
						theActivity.dAllList.clear();
						theActivity.dAllList.addAll(tmpList1);
						for (DeviceZone deviceZone : tmpList1) {
							theActivity.dList.add(deviceZone);
							theActivity.adapter.notifyDataSetChanged();
						}
						theActivity.mRlLoad.setVisibility(View.GONE);
						theActivity.deviceList.setVisibility(View.VISIBLE);
					} catch (JSONException e) {
						Log.d("JSONException", msg.obj.toString());
					}
					break;
				default:
					break;
				}				
			}		
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
			for (DeviceZone deviceZone : dAllList) {
				if(MainDeviceSelectedId == Constant.ID_MAINDEVICE_ALL ||  MainDeviceSelectedId == deviceZone.getSensorId())
				dList.add(deviceZone);
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

	@Override
	public void onSystemAction() {
		final DeviceZone dZone = new DeviceZone(null,0, "");		
		final MaterialDialog mMaterialDialog = new MaterialDialog(getActivity()).setIsShowIMM(true);
		View view = LayoutInflater.from(getActivity()).inflate(R.layout.material_dialog_update, parent,false);
		((TextView)view.findViewById(R.id.dialog_title)).setText(getResources().getString(R.string.new_zone_name));
		final MaterialEditText metDeviveName = (MaterialEditText)view.findViewById(R.id.met_device_name);
		final MaterialEditText metMianDeviveName = (MaterialEditText)view.findViewById(R.id.met_zone);
		metMianDeviveName.setHint("选择主控");
		metDeviveName.setHint("区域名字");
		((LinearLayout)view.findViewById(R.id.ll_edit)).setVisibility(View.VISIBLE);
		//[start]区域选择框
		arrayAdapter.clear();
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
				metMianDeviveName.setText(arrayAdapter.getItem(arg2));
				dZone.setSensorId(MainList.get(arg2).getId());
			}
		});
		alert.setPositiveButton("取消", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alert.dismiss();
			}
		});				
		//[end]
		
		((ButtonFlat)view.findViewById(R.id.btn_selector_zone)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(alert!=null){
					alert.show();
				}						
			}
		});
		((Button)view.findViewById(R.id.btn_save)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!metDeviveName.getText().toString().equals("")&&!metDeviveName.getText().toString().equals("")){
					dZone.setName(metDeviveName.getText().toString());
					switch (MyApplication.getNetMode()) {
					case Constant.Network_Mode:				
						//TODO 外网模式下的初始化
						ExecToCloudSql.AddZone(String.valueOf(dZone.getSensorId()),dZone.getName());
						ExecToCloudSql.getAllZoneRunnable(handler,Constant.TASKTIME);
						break;
					case Constant.Intranet_Mode:
						//TODO 内网模式下的初始化
						break;
					case Constant.HotSpot_Mode:
						//TODO 热点模式下的初始化
						ExecSql.AddZone(dZone);
						ArrayList<DeviceZone> tmpList = ExecSql.getZone();
						dList.clear();
						dAllList.clear();
						dAllList.addAll(tmpList);
						dList.addAll(tmpList);
						adapter.notifyDataSetChanged();
						break;
					default:
						break;						
					}
					mMaterialDialog.dismiss();
				}else{
					ToolAlert.toast("选择主控和名字不能为空！");
				}
				
			}
		});
		((Button)view.findViewById(R.id.btn_cancel)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mMaterialDialog.dismiss();						
			}
		});
		mMaterialDialog.setView(view).setCanceledOnTouchOutside(true).show();	
	}
	
	
}
