package com.smk17.mysmarthome.fragment.systemsettings;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.json.JSONException;

import com.smk17.android.tools.ToolAlert;
import com.smk17.mysmarthome.Constant;
import com.smk17.mysmarthome.MyApplication;
import com.smk17.mysmarthome.R;
import com.smk17.mysmarthome.adapter.FragmentItemAdapter;
import com.smk17.mysmarthome.adapter.HorizontalListViewAdapter;
import com.smk17.mysmarthome.cloud.ExecToCloudSql;
import com.smk17.mysmarthome.cloud.ParseDevice;
import com.smk17.mysmarthome.db.ExecSql;
import com.smk17.mysmarthome.domain.DeviceCategory;
import com.smk17.mysmarthome.domain.DeviceScene;
import com.smk17.mysmarthome.domain.DeviceSensor;
import com.smk17.mysmarthome.domain.DeviceZone;
import com.yh.materialdesign.edittext.MaterialEditText;
import com.yh.materialdesign.views.ButtonFloatSmall;
import com.yh.materialdesign.views.CircleImageView;
import com.yh.materialdesign.views.DropdownButton;
import com.yh.materialdesign.views.DropdownItemObject;
import com.yh.materialdesign.views.DropdownListView;
import com.yh.materialdesign.views.HorizontalListView;
import com.yh.materialdesign.views.LayoutDropdownButton;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;

public class FragmentAddScene extends SystemFragment {
	
	private MyHandler handler = new MyHandler(this);
	private RelativeLayout mRlLoad;
	private MaterialEditText mMetSceneName;
	private LinearLayout mLlSceneProperty;
	private LinearLayout mLlScene;
//	private LinearLayout mLlBtn;
	private Button mBtnSelector;
	private Button mBtnAddorRemove;
	private RelativeLayout mRlSelectorIcon;
	private LayoutDropdownButton mRlSceneSelectorIcon;
	private LayoutDropdownButton mRlSceneSelector;
	private LayoutDropdownButton mRlEditSceneDevice;
	private LayoutDropdownButton mBtnSelectorIcon;
	private HorizontalListView mHListViewSelectorIcon;
	private HorizontalListView mHListViewSelectorBackground;
	private HorizontalListViewAdapter hListViewSelectorIconAdapter;
	private HorizontalListViewAdapter hListViewSelectorBackgroundAdapter;
	private ButtonFloatSmall mBSFAvatar;
	private ListView deviceList;
	private View mask;
	private DropdownButton  chooseCategory, chooseZone;
	private DropdownListView  dropdownCategory, dropdownZone;
	private int MainDeviceSelectedId=Constant.ID_MAINDEVICE_ALL, CategorySelectedId=Constant.ID_CATEGORY_ALL,  ZoneSelectedId=Constant.ID_ZONE_ALL;
	private Animation dropdown_in, dropdown_out, dropdown_mask_out;
	private boolean isShowSceneSelector=false,isShowSelectorIcon=false,isBtnSelectorAll=false,isShowEditSceneDevice=false ;
	private FragmentItemAdapter adapter;	
	private DeviceScene mScene = null;
	private ArrayList<DeviceSensor> dList =new ArrayList<DeviceSensor>();
	private ArrayList<DeviceSensor> dSelectedList =new ArrayList<DeviceSensor>();
	private ArrayList<DeviceSensor> dEditList =new ArrayList<DeviceSensor>();
	private ArrayList<DeviceSensor> dAllList =new ArrayList<DeviceSensor>();
	private ArrayList<DeviceZone> zoneList = new ArrayList<DeviceZone>();
	private ArrayList<DeviceCategory> CategoryList = new ArrayList<DeviceCategory>();
	private DropdownButtonsController dropdownButtonsController = new DropdownButtonsController();
	private FragmentItemAdapter.FIABtnClickListener fla = new FragmentItemAdapter.FIABtnClickListener() {
		
		@Override
		public void onFEditBtnClick(DeviceSensor device) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onFDeleteBtnClick(DeviceSensor device) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void onFClick(Integer position) {
			// TODO Auto-generated method stub
			
		}
	};
	
	
	public static final FragmentAddScene newInstance(int mainDeviceSelectedId){
		FragmentAddScene fragment = new FragmentAddScene(mainDeviceSelectedId);
		return fragment;		
	}
	
	private FragmentAddScene(int mainDeviceSelectedId){
		MainDeviceSelectedId = mainDeviceSelectedId;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.material_fragment_add_scene, container, false);
		mRlLoad = (RelativeLayout)view.findViewById(R.id.device_load);
		mMetSceneName = (MaterialEditText)view.findViewById(R.id.met_scene_name);
		mLlScene = (LinearLayout)view.findViewById(R.id.ll_scene);
		mLlSceneProperty = (LinearLayout)view.findViewById(R.id.ll_scene_property);
		mRlSelectorIcon = (RelativeLayout)view.findViewById(R.id.ll_selector_icon);
		mRlSceneSelectorIcon = (LayoutDropdownButton)view.findViewById(R.id.rl_scene_selector_icon);
		mRlSceneSelector = (LayoutDropdownButton)view.findViewById(R.id.rl_scene_selector);
		mRlEditSceneDevice = (LayoutDropdownButton)view.findViewById(R.id.rl_edit_scene_device);
		mBtnSelectorIcon =(LayoutDropdownButton)view.findViewById(R.id.btn_selector_icon);
		mHListViewSelectorIcon = (HorizontalListView)view.findViewById(R.id.horizontallistview_selector_icon);
		mHListViewSelectorBackground = (HorizontalListView)view.findViewById(R.id.horizontallistview_selector_background);
		mBSFAvatar = (ButtonFloatSmall)view.findViewById(R.id.btnfs_avatar);
//		mLlBtn = (LinearLayout)view.findViewById(R.id.ll_btn);
		mBtnAddorRemove = (Button)view.findViewById(R.id.btn_add);
		mBtnSelector = (Button)view.findViewById(R.id.btn_selector_all);
		deviceList = (ListView)view.findViewById(R.id.device_list);
		mask = view.findViewById(R.id.mask);
        chooseCategory = (DropdownButton) view.findViewById(R.id.chooseCategory);
        chooseZone = (DropdownButton) view.findViewById(R.id.chooseZone);
        dropdownCategory = (DropdownListView) view.findViewById(R.id.dropdownCategory);
        dropdownZone = (DropdownListView) view.findViewById(R.id.dropdownZone);
        
        dropdown_in = AnimationUtils.loadAnimation(getActivity(),R.anim.dropdown_in);
        dropdown_out = AnimationUtils.loadAnimation(getActivity(),R.anim.dropdown_out);
        dropdown_mask_out = AnimationUtils.loadAnimation(getActivity(),R.anim.dropdown_mask_out);
		MainDeviceSelectedId = MyApplication.getMainDeviceId();		
		mBSFAvatar.setIcoSize(110);
		mBSFAvatar.setEnabledAnime(false);
		mRlSceneSelectorIcon.setText("编辑图标");
		mBtnSelectorIcon.setText("选择自定义图片作为图标");
		mBtnSelectorIcon.setImageResource(R.drawable.ic_chevron_right_grey600_24dp);
		mRlSceneSelector.setText(R.string.scene_selector);
		mRlEditSceneDevice.setText(R.string.edit_scene_device);
		mScene = new DeviceScene(null, "", 1, MainDeviceSelectedId, 0, 0, "", null );
		adapter = new FragmentItemAdapter(dList, getActivity(), FragmentItemAdapter.TYPE_SelectionDevice,fla);
        deviceList.setAdapter(adapter);
        hListViewSelectorIconAdapter = new HorizontalListViewAdapter(getActivity(), Constant.SelectorIcon);
        hListViewSelectorBackgroundAdapter = new HorizontalListViewAdapter(getActivity(), Constant.SelectorBackground);
        mHListViewSelectorIcon.setAdapter(hListViewSelectorIconAdapter);
        mHListViewSelectorBackground.setAdapter(hListViewSelectorBackgroundAdapter);
        mHListViewSelectorIcon.setSelection(0);
        mHListViewSelectorBackground.setSelection(0);
        mHListViewSelectorIcon.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				ImageView icon = mBSFAvatar.getIcon();
				icon.setBackgroundResource(Constant.SelectorIcon[position]);
				mBSFAvatar.setIcon(icon);
//				if(position > 3 || position < (Constant.SelectorIcon.length-3) ){
//					mHListViewSelectorIcon.setSelectionFromLeft(position, 3);
//				}else{
					mHListViewSelectorIcon.setSelection(position);
//				}
					if(mScene != null){
						mScene.setIcon(position);
					}
				hListViewSelectorIconAdapter.setSelectIndex(position);
				hListViewSelectorIconAdapter.notifyDataSetChanged();
			}
		});
        mHListViewSelectorBackground.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// TODO Auto-generated method stub
				mBSFAvatar.setBackgroundColor(Color.parseColor( Constant.SelectorBackground[position]));
//				if(position > 3 || position < (Constant.SelectorBackground.length-3)){
//					mHListViewSelectorBackground.setSelectionFromLeft(position, 3);
//				}else{
					mHListViewSelectorBackground.setSelection(position);
//				}
				if(mScene != null){
					mScene.setBackground(position);
				}
				hListViewSelectorBackgroundAdapter.setSelectIndex(position);
				hListViewSelectorBackgroundAdapter.notifyDataSetChanged();
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
			ExecToCloudSql.getTerminalDeviceRunnable(handler);
			ExecToCloudSql.getZoneRunnable(handler,MainDeviceSelectedId);
			ExecToCloudSql.getDeviceCategory(handler);
			break;
		case Constant.Intranet_Mode:
			//TODO 内网模式下的初始化
			break;
		case Constant.HotSpot_Mode:
			//TODO 热点模式下的初始化。
			CategoryList.clear();
			zoneList.clear();
			CategoryList.addAll(ExecSql.getCategory());
			zoneList.addAll(ExecSql.getZone());
			TerminalDeviceInit(ExecSql.getDevice());
			break;
		default:
			break;
		}
		mBtnAddorRemove.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ArrayList<DeviceSensor> tList =new ArrayList<DeviceSensor>();
				for (DeviceSensor deviceSensor : dList) {
					if(deviceSensor.getCheck()){
						deviceSensor.setCheck(false);
						tList.add(deviceSensor);
					}
				}
				if(tList.size() > 0){
					if(isShowEditSceneDevice){
						dEditList.removeAll(tList);
						dSelectedList.addAll(tList);
						dList.clear();
						adapter.notifyDataSetChanged();
						for (DeviceSensor deviceSensor : dEditList) {
							if(deviceSensor.equals(MainDeviceSelectedId, CategorySelectedId, ZoneSelectedId)){
								deviceSensor.setSelectMode(false);
								dList.add(deviceSensor);								
								adapter.notifyDataSetChanged();
							}
						}
						dropdownButtonsController.init();
					}else if(isShowSceneSelector){
						dSelectedList.removeAll(tList);
						dEditList.addAll(tList);
						dList.clear();
						adapter.notifyDataSetChanged();
						for (DeviceSensor deviceSensor : dSelectedList) {
							if(deviceSensor.equals(MainDeviceSelectedId, CategorySelectedId, ZoneSelectedId)){
								deviceSensor.setSelectMode(true);
								dList.add(deviceSensor);								
								adapter.notifyDataSetChanged();
							}
						}
						dropdownButtonsController.init();
					}
				}else{
					String msg = "";
					if(isShowEditSceneDevice){
						msg = "请选择要移除的终端。";
					}else if(isShowSceneSelector){
						msg = "请选择要关联的终端。";
					}
					ToolAlert.toast(msg);
				}
				
			}
		});
		mBtnSelector.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ArrayList<DeviceSensor> tList =new ArrayList<DeviceSensor>();
				tList.addAll(dList);
				dList.clear();
				isBtnSelectorAll = !isBtnSelectorAll;
				if(isBtnSelectorAll){
					mBtnSelector.setText(R.string.selector_no_all);
					for (DeviceSensor deviceSensor : tList) {
						deviceSensor.setCheck(true);
						dList.add(deviceSensor);
						adapter.notifyDataSetChanged();
					}
				}else{
					mBtnSelector.setText(R.string.selector_all);
					for (DeviceSensor deviceSensor : tList) {
						deviceSensor.setCheck(false);
						dList.add(deviceSensor);
						adapter.notifyDataSetChanged();
					}
				}
			}
		});
		mRlSceneSelectorIcon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				isShowSelectorIcon = !isShowSelectorIcon;
				if(isShowSelectorIcon){
					mRlSelectorIcon.clearAnimation();
					mRlSelectorIcon.startAnimation(dropdown_in);
					mRlSelectorIcon.setVisibility(View.VISIBLE);
					mLlScene.clearAnimation();
					mLlScene.startAnimation(dropdown_out);
					mLlScene.setVisibility(View.GONE);
					mRlEditSceneDevice.clearAnimation();
					mRlEditSceneDevice.startAnimation(dropdown_out);
					mRlEditSceneDevice.setVisibility(View.GONE);
					mRlSceneSelector.clearAnimation();
					mRlSceneSelector.startAnimation(dropdown_out);
					mRlSceneSelector.setVisibility(View.GONE);				
				}else{
					hideSelectorIcon();
				}
				isShowSceneSelector = false;
				isShowEditSceneDevice = false;
				isBtnSelectorAll = false;
				mRlSceneSelectorIcon.setChecked(isShowSelectorIcon);
			}
		});
		mRlSceneSelector.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				isShowSceneSelector = !isShowSceneSelector;
				if(isShowSceneSelector){	
					dList.clear();
					for (DeviceSensor deviceSensor : dSelectedList) {
						if(deviceSensor.equals(MainDeviceSelectedId, CategorySelectedId, ZoneSelectedId)){
							deviceSensor.setSelectMode(true);
							dList.add(deviceSensor);
							adapter.notifyDataSetChanged();
						}
					}
					dropdownButtonsController.init();
					
//					mLlBtn.clearAnimation();
//					mLlBtn.startAnimation(dropdown_in);
//					mLlBtn.setVisibility(View.VISIBLE);
					mBtnAddorRemove.setText(R.string.selector_associate);
					mBtnSelector.setText(R.string.selector_all);
					
					mLlSceneProperty.clearAnimation();
					mLlSceneProperty.startAnimation(dropdown_in);
					mLlSceneProperty.setVisibility(View.VISIBLE);
					mLlScene.clearAnimation();
					mLlScene.startAnimation(dropdown_out);
					mLlScene.setVisibility(View.GONE);
					mRlEditSceneDevice.clearAnimation();
					mRlEditSceneDevice.startAnimation(dropdown_out);
					mRlEditSceneDevice.setVisibility(View.GONE);
					mRlSceneSelectorIcon.clearAnimation();
					mRlSceneSelectorIcon.startAnimation(dropdown_out);
					mRlSceneSelectorIcon.setVisibility(View.GONE);							
				}else{
					hideSceneSelector();					
				}
				isShowSelectorIcon = false;
				isShowEditSceneDevice = false;
				isBtnSelectorAll = false;
				mRlSceneSelector.setChecked(isShowSceneSelector);
			}
		});
		mRlEditSceneDevice.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				isShowEditSceneDevice = !isShowEditSceneDevice;
				if(isShowEditSceneDevice){
					dList.clear();
					for (DeviceSensor deviceSensor : dEditList) {
						if(deviceSensor.equals(MainDeviceSelectedId, CategorySelectedId, ZoneSelectedId)){
							deviceSensor.setSelectMode(false);
							dList.add(deviceSensor);								
							adapter.notifyDataSetChanged();
						}
					}
					dropdownButtonsController.init();				

					mBtnAddorRemove.setText(R.string.remove);
					mBtnSelector.setText(R.string.selector_all);
					
					mLlSceneProperty.clearAnimation();
					mLlSceneProperty.startAnimation(dropdown_in);
					mLlSceneProperty.setVisibility(View.VISIBLE);
					mLlScene.clearAnimation();
					mLlScene.startAnimation(dropdown_out);
					mLlScene.setVisibility(View.GONE);
					mRlSceneSelector.clearAnimation();
					mRlSceneSelector.startAnimation(dropdown_out);
					mRlSceneSelector.setVisibility(View.GONE);
					mRlSceneSelectorIcon.clearAnimation();
					mRlSceneSelectorIcon.startAnimation(dropdown_out);
					mRlSceneSelectorIcon.setVisibility(View.GONE);				
				}else{
					hideEditSceneDevice();
				}
				isShowSelectorIcon = false;
				isShowSceneSelector = false;
				isBtnSelectorAll = false;
				mRlEditSceneDevice.setChecked(isShowEditSceneDevice);
			}
		});
		//view.findViewById(R.id.iv_save)
		view.setOnKeyListener( new OnKeyListener() {
            @Override
            public boolean onKey( View v, int keyCode, KeyEvent event ) {
            	if(keyCode == KeyEvent.KEYCODE_BACK)  
                {
            		if(isShowSelectorIcon){
            			hideSelectorIcon();
            			isShowSelectorIcon = !isShowSelectorIcon;
            			mRlSceneSelectorIcon.setChecked(isShowSelectorIcon);
            		}else if(isShowEditSceneDevice){
            			hideEditSceneDevice();
            			isShowEditSceneDevice = !isShowEditSceneDevice;
            			mRlEditSceneDevice.setChecked(isShowEditSceneDevice);
            		}else if(isShowSceneSelector){
            			hideSceneSelector();
            			isShowSceneSelector = !isShowSceneSelector;
            			mRlSceneSelector.setChecked(isShowSceneSelector);
            		}else{
            			getActivity().finish();
            		}
            		
                }
        		return false;
            } 
       });
		return view;
	}
	

	private void hideSelectorIcon(){
		mRlSelectorIcon.clearAnimation();
		mRlSelectorIcon.startAnimation(dropdown_out);
		mRlSelectorIcon.setVisibility(View.GONE);
		mLlScene.clearAnimation();
		mLlScene.startAnimation(dropdown_in);
		mLlScene.setVisibility(View.VISIBLE);
		mRlEditSceneDevice.clearAnimation();
		mRlEditSceneDevice.startAnimation(dropdown_in);
		mRlEditSceneDevice.setVisibility(View.VISIBLE);
		mRlSceneSelector.clearAnimation();
		mRlSceneSelector.startAnimation(dropdown_in);
		mRlSceneSelector.setVisibility(View.VISIBLE);
	}
	private void hideEditSceneDevice(){
		dEditList.clear();
		dEditList.addAll(dList);
		
		mLlSceneProperty.clearAnimation();
		mLlSceneProperty.startAnimation(dropdown_out);
		mLlSceneProperty.setVisibility(View.GONE);
//		mLlBtn.clearAnimation();
//		mLlBtn.startAnimation(dropdown_out);
//		mLlBtn.setVisibility(View.GONE);
		mLlScene.clearAnimation();
		mLlScene.startAnimation(dropdown_in);
		mLlScene.setVisibility(View.VISIBLE);
		mRlSceneSelector.clearAnimation();
		mRlSceneSelector.startAnimation(dropdown_in);
		mRlSceneSelector.setVisibility(View.VISIBLE);
		mRlSceneSelectorIcon.clearAnimation();
		mRlSceneSelectorIcon.startAnimation(dropdown_in);
		mRlSceneSelectorIcon.setVisibility(View.VISIBLE);
	}
	private void hideSceneSelector(){
		dSelectedList.clear();
		dSelectedList.addAll(dList);
		
		mLlSceneProperty.clearAnimation();
		mLlSceneProperty.startAnimation(dropdown_out);
		mLlSceneProperty.setVisibility(View.GONE);
//		mLlBtn.clearAnimation();
//		mLlBtn.startAnimation(dropdown_out);
//		mLlBtn.setVisibility(View.GONE);
		mLlScene.clearAnimation();
		mLlScene.startAnimation(dropdown_in);
		mLlScene.setVisibility(View.VISIBLE);
		mRlEditSceneDevice.clearAnimation();
		mRlEditSceneDevice.startAnimation(dropdown_in);
		mRlEditSceneDevice.setVisibility(View.VISIBLE);
		mRlSceneSelectorIcon.clearAnimation();
		mRlSceneSelectorIcon.startAnimation(dropdown_in);
		mRlSceneSelectorIcon.setVisibility(View.VISIBLE);
	}
	
	private void TerminalDeviceInit(ArrayList<DeviceSensor> tmpList){		
		dList.clear();
		dSelectedList.clear();
		dAllList.clear();
		dAllList.addAll(tmpList);
		dSelectedList.addAll(tmpList);
		adapter.notifyDataSetChanged();
		if(dList.size() == 0){
			dList.add(new DeviceSensor(0, MainDeviceSelectedId, 0, 0, "该筛选条件下无终端", "点击添加", 9,null));
			adapter.notifyDataSetChanged();
		}
		mRlLoad.setVisibility(View.GONE);
		deviceList.setVisibility(View.VISIBLE);
		dropdownButtonsController.init();
	}
	
	private static class MyHandler extends Handler { 
		//TODO MyHandler类定义
		WeakReference<FragmentAddScene> mActivity;
		
		public MyHandler(FragmentAddScene Activity){
			mActivity = new WeakReference<FragmentAddScene>(Activity);
		}
				
		@Override
		public void handleMessage(Message msg) {			
			final FragmentAddScene theActivity = mActivity.get();
			
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
						theActivity.TerminalDeviceInit(tmpList);
					} catch (JSONException e) {
						Log.d("JSONException", msg.obj.toString());
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
						e.printStackTrace();
					}
					break;
				case Constant.STATUS_ID:
					if(theActivity.getActivity()!=null)
						theActivity.getActivity().finish();
					break;
				default:
					break;
				}
			}		
		}
	};
	
	private class DropdownButtonsController implements DropdownListView.Container {

		private DropdownListView currentDropdownList;
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
			if(isShowEditSceneDevice){
				for (DeviceSensor deviceSensor : dEditList) {
					if(deviceSensor.equals(MainDeviceSelectedId, CategorySelectedId, ZoneSelectedId)){
						deviceSensor.setSelectMode(false);
						dList.add(deviceSensor);								
						adapter.notifyDataSetChanged();
					}
				}
			}else if (isShowSceneSelector){
				for (DeviceSensor deviceSensor : dSelectedList) {
					if(deviceSensor.equals(MainDeviceSelectedId, CategorySelectedId, ZoneSelectedId)){
						deviceSensor.setSelectMode(false);
						dList.add(deviceSensor);								
						adapter.notifyDataSetChanged();
					}
				}
			}
			if(dList.size() == 0){
				dList.add(new DeviceSensor(0, MainDeviceSelectedId, 0, 0, "该筛选条件下无终端", "点击添加",9,  null));
				adapter.notifyDataSetChanged();
			}
		}
		void reset() {
            chooseCategory.setChecked(false);
            chooseZone.setChecked(false);

            dropdownCategory.setVisibility(View.GONE);
            dropdownZone.setVisibility(View.GONE);
            mask.setVisibility(View.GONE);

            dropdownCategory.clearAnimation();
            dropdownZone.clearAnimation();
            mask.clearAnimation();
        }
		
		 void init() {
			 reset();
			 int i = 0;
			 int len = CategoryList.size();
			 datasetCategory.clear();
			 datasetZone.clear();
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
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List removeDuplicateWithOrder(List list) {
        Set set = new HashSet();
        List newList = new ArrayList();
        for (Iterator iter = list.iterator(); iter.hasNext();) {
            Object element = iter.next();
            if (set.add(element))
                newList.add(element);
        }
        return newList;
    }
	
	@Override
	public void onSystemAction() {
		if(dEditList.size() > 0){
			String sceneName  = mMetSceneName.getText().toString();
			if(!sceneName.equals("")){
				mScene.setName(sceneName);
				mScene.setPropertyList(dEditList);
				switch (MyApplication.getNetMode()) {
				case Constant.Network_Mode:
					//TODO 外网模式下的初始化
					ExecToCloudSql.insertSceneRunnable(handler, mScene);
					break;
				case Constant.Intranet_Mode:
					//TODO 内网模式下的初始化
					break;
				case Constant.HotSpot_Mode:
					//TODO 热点模式下的初始化
					ExecSql.AddScene(mScene);
					break;
				default:
					break;
				}
				if(getActivity()!=null)
					getActivity().finish();
			}else{
				ToolAlert.toast("情景模式名字为空，请输入名字！");
			}
			
		}else{
			ToolAlert.toast("已关联的终端数为0，请选择要关联的终端数.");
		}		
	}
}
