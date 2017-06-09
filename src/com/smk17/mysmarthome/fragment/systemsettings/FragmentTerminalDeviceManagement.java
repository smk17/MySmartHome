package com.smk17.mysmarthome.fragment.systemsettings;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.smk17.mysmarthome.Constant;
import com.smk17.mysmarthome.MyApplication;
import com.smk17.mysmarthome.R;
import com.smk17.mysmarthome.adapter.FragmentItemAdapter;
import com.smk17.mysmarthome.adapter.FragmentItemAdapter.FIABtnClickListener;
import com.smk17.mysmarthome.cloud.ExecToCloudSql;
import com.smk17.mysmarthome.cloud.ParseDevice;
import com.smk17.mysmarthome.db.ExecSql;
import com.smk17.mysmarthome.domain.DeviceCategory;
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

public class FragmentTerminalDeviceManagement extends SystemFragment {
	
	private MyHandler handler = new MyHandler(this);
	private RelativeLayout mRlLoad;
	private ListView deviceList;
	private View mask;
	private DropdownButton chooseMainDevice, chooseCategory, chooseZone;
	private DropdownListView dropdownMainDevice, dropdownCategory, dropdownZone;
	private int MainDeviceSelectedId=Constant.ID_MAINDEVICE_ALL, CategorySelectedId=Constant.ID_CATEGORY_ALL,  ZoneSelectedId=Constant.ID_ZONE_ALL;
	private Animation dropdown_in, dropdown_out, dropdown_mask_out;
	private LinearLayout mLlBtn;
	private MaterialDialog alert = null;
	private ArrayAdapter<String> arrayAdapter;
	private ViewGroup parent;
	private FragmentItemAdapter adapter;	
	private ArrayList<DeviceSensor> dList =new ArrayList<DeviceSensor>();
	private ArrayList<DeviceSensor> dAllList =new ArrayList<DeviceSensor>();
	private ArrayList<DeviceZone> zoneList = new ArrayList<DeviceZone>();
	private ArrayList<DeviceSensor> MainList = new ArrayList<DeviceSensor>();
	private ArrayList<DeviceCategory> CategoryList = new ArrayList<DeviceCategory>();
	private DropdownButtonsController dropdownButtonsController = new DropdownButtonsController();
	
	public static final FragmentTerminalDeviceManagement newInstance(){
		FragmentTerminalDeviceManagement fragment = new FragmentTerminalDeviceManagement();
		return fragment;		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.material_fragment_terminaldevicemanagement, container, false);  	
		parent = (ViewGroup) view.findViewById(R.id.container);
		deviceList = (ListView)view.findViewById(R.id.device_list);
		mLlBtn = (LinearLayout)view.findViewById(R.id.ll_btn);
		mRlLoad = (RelativeLayout)view.findViewById(R.id.device_load);
		mask = view.findViewById(R.id.mask);
        chooseMainDevice = (DropdownButton) view.findViewById(R.id.chooseMainDevice);
        chooseCategory = (DropdownButton) view.findViewById(R.id.chooseCategory);
        chooseZone = (DropdownButton) view.findViewById(R.id.chooseZone);
        dropdownMainDevice = (DropdownListView) view.findViewById(R.id.dropdownMainDevice);
        dropdownCategory = (DropdownListView) view.findViewById(R.id.dropdownCategory);
        dropdownZone = (DropdownListView) view.findViewById(R.id.dropdownZone);

        dropdown_in = AnimationUtils.loadAnimation(getActivity(),R.anim.dropdown_in);
        dropdown_out = AnimationUtils.loadAnimation(getActivity(),R.anim.dropdown_out);
        dropdown_mask_out = AnimationUtils.loadAnimation(getActivity(),R.anim.dropdown_mask_out);        
		arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item2);
        adapter = new FragmentItemAdapter(dList, getActivity(), FragmentItemAdapter.TYPE_TerminalDevice,fla);
        deviceList.setAdapter(adapter);
        view.findViewById(R.id.btn_cancel_select).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int len = dList.size();
				mLlBtn.setVisibility(View.GONE);
				for(int i = 0; i <len;i++){
					dList.get(i).setSelectMode(false);
					adapter.notifyDataSetChanged();
				}
			}
		});
        view.findViewById(R.id.btn_batch_edit).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				int len = dList.size();
				final ArrayList<DeviceSensor> tmplist = new ArrayList<DeviceSensor>();
				for(int i = 0 ; i<len;i++){
					if(dList.get(i) != null){
						if(dList.get(i).getCheck()){
							tmplist.add(dList.get(i));
							Log.d("btn_batch_edit", dList.get(i).getName());
						}
					}				
				}
				Log.d("btn_batch_edit", "------------------------------");
				if(zoneList!=null&&zoneList.size()!=0){					
					final MaterialDialog mMaterialDialog = new MaterialDialog(getActivity()).setIsShowIMM(true);
					View view = LayoutInflater.from(getActivity()).inflate(R.layout.material_dialog_update, parent,false);
					((MaterialEditText)view.findViewById(R.id.met_device_name)).setVisibility(View.GONE);
					final MaterialEditText metZoneName = (MaterialEditText)view.findViewById(R.id.met_zone);
					((TextView)view.findViewById(R.id.dialog_title)).setText(getResources().getString(R.string.edit_name));
					((LinearLayout)view.findViewById(R.id.ll_edit)).setVisibility(View.VISIBLE);
					//[start]区域选择框
					arrayAdapter.clear();
					for (int i = 0; i < zoneList.size(); i++) {					
						arrayAdapter.add(zoneList.get(i).getName());
						arrayAdapter.notifyDataSetChanged();
					}

					ListView listView = new ListView(getActivity());

					@SuppressWarnings("deprecation")
					Drawable drawable = getResources().getDrawable(R.drawable.settings_selector); 
					listView.setSelector(drawable);
					listView.setDividerHeight(0);
					listView.setVerticalScrollBarEnabled(true);
					listView.setAdapter(arrayAdapter);

					alert = new MaterialDialog(getActivity()).setTitle("选择区域").setContentView(listView);
					
					listView.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> arg0, View arg1,
								int arg2, long arg3) {
							alert.dismiss();						
							metZoneName.setText(arrayAdapter.getItem(arg2));
							for(int i = 0; i < tmplist.size(); i++){
								tmplist.get(i).setZoneId(zoneList.get(arg2).getId());
							}
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
							switch (MyApplication.getNetMode()) {
							case Constant.Network_Mode:
								//TODO 外网模式下的初始化
								ExecToCloudSql.UpdateDeviceRunnable(handler, tmplist);
								ExecToCloudSql.getTerminalDeviceRunnable(handler,Constant.TASKTIME);
								break;
							case Constant.Intranet_Mode:
								//TODO 内网模式下的初始化
								break;
							case Constant.HotSpot_Mode:
								//TODO 热点模式下的初始化
								ExecSql.updateDevice(tmplist, false);
								refresh(ExecSql.getDevice());
								break;
							default:
								break;
							}						
							mMaterialDialog.dismiss();
						}
					});
					((Button)view.findViewById(R.id.btn_cancel)).setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							mMaterialDialog.dismiss();
						}
					});
					mMaterialDialog.setView(view).setCanceledOnTouchOutside(false);
					mMaterialDialog.show();
				}				
			}
		});
		mask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dropdownButtonsController.hide();
            }
        });
		
		switch (MyApplication.getNetMode()) {
		case Constant.Network_Mode:
			//TODO 外网模式下的初始化
			ExecToCloudSql.getAllZoneRunnable(handler);
			ExecToCloudSql.getMainDeviceRunnable(handler);
			ExecToCloudSql.getTerminalDeviceRunnable(handler);
			ExecToCloudSql.getDeviceCategory(handler);
			break;
		case Constant.Intranet_Mode:
			//TODO 内网模式下的初始化
			break;
		case Constant.HotSpot_Mode:
			//TODO 热点模式下的初始化
			zoneList.clear();
			MainList.clear();
			CategoryList.clear();
			zoneList.addAll(ExecSql.getZone());
			MainList.addAll(ExecSql.getMainDevice());
			CategoryList.addAll(ExecSql.getCategory());
			refresh(ExecSql.getDevice());
			break;
		default:
			break;
		}
		return view;
	}
	
	FIABtnClickListener fla = new FIABtnClickListener() {
		
		@Override
		public void onFEditBtnClick(final DeviceSensor device) {
			if(zoneList!=null&&zoneList.size()!=0){
				final MaterialDialog mMaterialDialog = new MaterialDialog(getActivity()).setIsShowIMM(true);
				View view = LayoutInflater.from(getActivity()).inflate(R.layout.material_dialog_update, parent,false);
				final MaterialEditText metDeviveName = (MaterialEditText)view.findViewById(R.id.met_device_name);
				final MaterialEditText metZoneName = (MaterialEditText)view.findViewById(R.id.met_zone);
				metDeviveName.setText(device.getName());
				
				//[start]区域选择框
				arrayAdapter.clear();
				for (int i = 0; i < zoneList.size(); i++) {
					if(device.getZoneId() == zoneList.get(i).getId()){
						metZoneName.setText(zoneList.get(i).getName());
					}
					if(device.getDeviceId() == zoneList.get(i).getSensorId()){
						arrayAdapter.add(zoneList.get(i).getName());
						arrayAdapter.notifyDataSetChanged();
					}					
				}

				ListView listView = new ListView(getActivity());

				@SuppressWarnings("deprecation")
				Drawable drawable = getResources().getDrawable(R.drawable.settings_selector); 
				listView.setSelector(drawable);
				listView.setDividerHeight(0);
				listView.setVerticalScrollBarEnabled(true);
				listView.setAdapter(arrayAdapter);

				alert = new MaterialDialog(getActivity()).setTitle("选择区域").setContentView(listView);
				
				listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						alert.dismiss();						
						metZoneName.setText(arrayAdapter.getItem(arg2));
						device.setZoneId(zoneList.get(arg2).getId());
					}
				});
				alert.setPositiveButton("取消", new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						alert.dismiss();
					}
				});
				//[end]
				((TextView)view.findViewById(R.id.dialog_title)).setText(getResources().getString(R.string.edit_name));
				((LinearLayout)view.findViewById(R.id.ll_edit)).setVisibility(View.VISIBLE);
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
						device.setName(metDeviveName.getText().toString());
						switch (MyApplication.getNetMode()) {
						case Constant.Network_Mode:
							//TODO 外网模式下的初始化
							ExecToCloudSql.UpdateDeviceRunnable(handler, device);
							ExecToCloudSql.getTerminalDeviceRunnable(handler,Constant.TASKTIME);
							break;
						case Constant.Intranet_Mode:
							//TODO 内网模式下的初始化
							break;
						case Constant.HotSpot_Mode:
							//TODO 热点模式下的初始化
							ExecSql.updateDevice(device, false);
							refresh(ExecSql.getDevice());
							break;
						default:
							break;
						}
						mMaterialDialog.dismiss();
					}
				});
				((Button)view.findViewById(R.id.btn_cancel)).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						mMaterialDialog.dismiss();
					}
				});
				mMaterialDialog.setView(view).setCanceledOnTouchOutside(false);
				mMaterialDialog.show();	
			}
			
		}
		
		@Override
		public void onFDeleteBtnClick(DeviceSensor device) {
			// TODO Auto-generated method stub
			switch (MyApplication.getNetMode()) {
			case Constant.Network_Mode:
				//TODO 外网模式下的初始化
				ExecToCloudSql.DeleteTerminalDeviceRunnable (handler, device);
				ExecToCloudSql.getTerminalDeviceRunnable(handler,Constant.TASKTIME);
				break;
			case Constant.Intranet_Mode:
				//TODO 内网模式下的初始化
				break;
			case Constant.HotSpot_Mode:
				//TODO 热点模式下的初始化
				ExecSql.deleteDevice(device,true);
				refresh(ExecSql.getDevice());
				break;
			default:
				break;
			}
		}
		
		@Override
		public void onFClick(Integer position) {
			// TODO Auto-generated method stub
			mLlBtn.setVisibility(View.VISIBLE);
		}
	};
	
	public void refresh(ArrayList<DeviceSensor> tmpList){
		dList.clear();
		dAllList.clear();
		dAllList.addAll(tmpList);
		for (DeviceSensor deviceSensor : tmpList) {
			if(deviceSensor.equals(MainDeviceSelectedId, CategorySelectedId, ZoneSelectedId)){
				deviceSensor.setSelectMode(false);
				dList.add(deviceSensor);								
				adapter.notifyDataSetChanged();
			}						
		}
		
		if(dList.size() == 0){
			dList.add(new DeviceSensor(0, MainDeviceSelectedId, 0, 0, "该筛选条件下无终端", "点击添加", 9,null));
			adapter.notifyDataSetChanged();
		}
		mLlBtn.setVisibility(View.GONE);
		mRlLoad.setVisibility(View.GONE);
		deviceList.setVisibility(View.VISIBLE);
		dropdownButtonsController.init();
	}
		
	private static class MyHandler extends Handler { 
		//TODO MyHandler类定义
		WeakReference<FragmentTerminalDeviceManagement> mActivity;
		
		public MyHandler(FragmentTerminalDeviceManagement Activity){
			mActivity = new WeakReference<FragmentTerminalDeviceManagement>(Activity);
		}
				
		@Override
		public void handleMessage(Message msg) {			
			final FragmentTerminalDeviceManagement theActivity = mActivity.get();
			
			if(msg.obj!=null ){
				switch (msg.what) {
				case Constant.DEVICEZONE_ID:
					ArrayList<DeviceZone> tList = null;
					try {
						theActivity.zoneList.clear();
						tList = ParseDevice.parseDeviceZone(msg.obj.toString());
						theActivity.zoneList.addAll(tList);
						theActivity.dropdownButtonsController.init();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case Constant.DEVICESENSOR_ID:
					try {
						ArrayList<DeviceSensor> tmpList = ParseDevice.parseTerminalDevice(msg.obj.toString());
						theActivity.refresh(tmpList);
					} catch (JSONException e) {
						Log.d("JSONException", msg.obj.toString());
					}
					break;
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
				case Constant.DEVICECATEGORY_ID:
					ArrayList<DeviceCategory>  List = null;
					try {
						List = ParseDevice.parseDeviceCategory(msg.obj.toString());
						theActivity.CategoryList.clear();
						for (DeviceCategory deviceCategory : List) {
							if(deviceCategory.getId()!=1)
								theActivity.CategoryList.add(deviceCategory);
						}						
						theActivity.dropdownButtonsController.init();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
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
        private List<DropdownItemObject> datasetMainDevice = new ArrayList<>();//全部讨论
        private List<DropdownItemObject> datasetCategory = new ArrayList<>();//全部标签
        private List<DropdownItemObject> datasetZone = new ArrayList<>();//评论排序
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
				ZoneSelectedId = Constant.ID_ZONE_ALL;
				MainDeviceSelectedId = dropdownMainDevice.getSelectedItemObject().id;
				int len = zoneList.size();
				datasetZone.clear();
				if(len >0){
					 datasetZone.add(new DropdownItemObject(Constant.ZONE_ALL, Constant.ID_ZONE_ALL, null));
					 for(int i=0;i<len;i++){
						 if(zoneList.get(i).getSensorId() == MainDeviceSelectedId || MainDeviceSelectedId == Constant.ID_ZONE_ALL)
							 datasetZone.add(new DropdownItemObject(zoneList.get(i).getName(), zoneList.get(i).getId(), null));
					 }
					 dropdownZone.bind(datasetZone, chooseZone, this, ZoneSelectedId);
				 }
				break;
			case R.id.dropdownCategory:
				CategorySelectedId = dropdownCategory.getSelectedItemObject().id;
				break;
			case R.id.dropdownZone:
				ZoneSelectedId = dropdownZone.getSelectedItemObject().id;
				break;
			default:
				break;
			}
			dList.clear();
			for (DeviceSensor deviceSensor : dAllList) {
				if(deviceSensor.equals(MainDeviceSelectedId, CategorySelectedId, ZoneSelectedId)){
					deviceSensor.setSelectMode(false);
					dList.add(deviceSensor);								
					adapter.notifyDataSetChanged();
				}						
			}
			if(dList.size() == 0){
				dList.add(new DeviceSensor(0, MainDeviceSelectedId, 0, 0, "该筛选条件下无终端", "点击添加",9, null));
				adapter.notifyDataSetChanged();
			}
		}
		void reset() {
            chooseMainDevice.setChecked(false);
            chooseCategory.setChecked(false);
            chooseZone.setChecked(false);

            dropdownMainDevice.setVisibility(View.GONE);
            dropdownCategory.setVisibility(View.GONE);
            dropdownZone.setVisibility(View.GONE);
            mask.setVisibility(View.GONE);

            dropdownMainDevice.clearAnimation();
            dropdownCategory.clearAnimation();
            dropdownZone.clearAnimation();
            mask.clearAnimation();
        }
		
		 void init() {
			 reset();
			 int i = 0;
			 int len = MainList.size();
			 datasetMainDevice.clear();
			 datasetCategory.clear();
			 datasetZone.clear();
			 if(len>0){
				 datasetMainDevice.add(new DropdownItemObject(Constant.MAINDEVICE_ALL, Constant.ID_MAINDEVICE_ALL, null));
				 for(;i<len;i++){
					 datasetMainDevice.add(new DropdownItemObject(MainList.get(i).getName(), MainList.get(i).getId(), null));
				 }
				 dropdownMainDevice.bind(datasetMainDevice, chooseMainDevice, this, MainDeviceSelectedId);
			 }
			 
			 len = CategoryList.size();
			 if(len >0){
				 datasetCategory.add(new DropdownItemObject(Constant.CATEGORY_ALL, Constant.ID_CATEGORY_ALL, null));
				 for(i=0;i<len;i++){
					 datasetCategory.add(new DropdownItemObject(CategoryList.get(i).getName(), CategoryList.get(i).getId(), null));
				 }
				 dropdownCategory.bind(datasetCategory, chooseCategory, this, CategorySelectedId);
			 }
			 
			 len = zoneList.size();
			 if(len >0){
				 datasetZone.add(new DropdownItemObject(Constant.ZONE_ALL, Constant.ID_ZONE_ALL, null));
				 for(i=0;i<len;i++){
					 datasetZone.add(new DropdownItemObject(zoneList.get(i).getName(), zoneList.get(i).getId(), null));
				 }
				 dropdownZone.bind(datasetZone, chooseZone, this, ZoneSelectedId);
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
	
}
