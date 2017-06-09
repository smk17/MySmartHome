package com.smk17.mysmarthome.fragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;

import com.smk17.android.tools.ToolNetwork;
import com.smk17.mysmarthome.Constant;
import com.smk17.mysmarthome.MyApplication;
import com.smk17.mysmarthome.R;
import com.smk17.mysmarthome.adapter.FragmentItemAdapter;
import com.smk17.mysmarthome.cloud.ExecToCloudSql;
import com.smk17.mysmarthome.cloud.ParseDevice;
import com.smk17.mysmarthome.db.ExecSql;
import com.smk17.mysmarthome.db.HotSpotMode;
import com.smk17.mysmarthome.domain.DeviceProperty;
import com.smk17.mysmarthome.domain.DeviceSensor;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class FragmentZoneControlPage extends BaseFragment {
	
	private ArrayList<DeviceSensor> list = null;
	private ListView deviceList;
	private SwipeRefreshLayout deviceRefreshable;
	private int zoneId;
	private Timer timer = new Timer();
	private boolean IsRefreshable = true;
	private FragmentItemAdapter adapter;	
		
	public FragmentZoneControlPage(){
		this.zoneId = 0;
	}
	
	public static final FragmentZoneControlPage newInstance(int zoneId){         
		FragmentZoneControlPage fragment = new FragmentZoneControlPage(zoneId);
		return fragment;  
	}  
	
	private FragmentZoneControlPage(int zoneId){
		this.zoneId = zoneId;
	}
	
	
	private MyHandler handler = new MyHandler(this);
		
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		 View view = inflater.inflate(R.layout.material_fragment_zonecontrol_page, container, false);  
//		 if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//				SystemBarTintManager tintManager = new SystemBarTintManager(getActivity()); 
//				SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();  
//				view.setPadding(0, 0, config.getPixelInsetRight(), config.getPixelInsetBottom()); 
//		 }  
		 deviceList = (ListView)view.findViewById(R.id.device_list);
		 deviceRefreshable = (SwipeRefreshLayout)view.findViewById(R.id.device_refreshable);
		 deviceRefreshable.setColorSchemeResources(R.color.title_background);
		 list = new ArrayList<DeviceSensor>();
		 adapter = new FragmentItemAdapter(list, getActivity(),FragmentItemAdapter.TYPE_ZoneControl );		 
		 deviceList.setAdapter(adapter);
		 switch (MyApplication.getNetMode()) {
			case Constant.Network_Mode:
				//TODO 外网模式下的初始化
				deviceRefreshable.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
					
					@Override
					public void onRefresh() {
						IsRefreshable = true;						
					}
				});
				timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					if(IsRefreshable || MyApplication.getNetworkType().equals(ToolNetwork.NETWORK_WIFI)){
						ExecToCloudSql.getTerminalDeviceRunnable(handler);
//						Log.e("timer", "IsRefreshable="+IsRefreshable);
					}
//					Log.e("timer", "--------------------");
				}
			}, 500, Constant.TASKTIME);
				break;
			case Constant.Intranet_Mode:
				//TODO 内网模式下的初始化
				break;
			case Constant.HotSpot_Mode:
				//TODO 热点模式下的初始化		
//				broadcastManager = LocalBroadcastManager.getInstance(getActivity());
				Message msg=  Message.obtain();
				msg.what = Constant.REVToHotSpotNet_ID;
				msg.obj = 1;
				handler.sendMessage(msg);
				deviceRefreshable.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
					
					@Override
					public void onRefresh() {
						IsRefreshable = true;		
						Message msg=  Message.obtain();
						msg.what = Constant.REVToHotSpotNet_ID;
						msg.obj = 1;
						handler.sendMessage(msg);
					}
				});
				timer.schedule(new TimerTask() {
					
					@Override
					public void run() {
						if(HotSpotMode.getIsQuerySql()){
							Message msg1=  Message.obtain();
							msg1.what = Constant.REVToHotSpotNet_ID;
							msg1.obj = 1;
							handler.sendMessage(msg1);
							HotSpotMode.setIsQuerySql(false);
						}
					}
				}, 500, Constant.TASKTIME);
				break;
			default:
				break;
		}
		 
		return view;
	}
	
	@Override
	public void onDestroy() {
		timer.cancel();
		switch (MyApplication.getNetMode()) {
			case Constant.Network_Mode:
				//TODO 外网模式下的初始化
				
				break;
			case Constant.Intranet_Mode:
				//TODO 内网模式下的初始化
				break;
			case Constant.HotSpot_Mode:
				//TODO 热点模式下的初始化
				break;
			default:
				break;
			}
		super.onDestroy();
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		switch (MyApplication.getNetMode()) {
		case Constant.Network_Mode:
			//TODO 外网模式下的初始化
			
			break;
		case Constant.Intranet_Mode:
			//TODO 内网模式下的初始化
			break;
		case Constant.HotSpot_Mode:
			//TODO 热点模式下的初始化
			break;
		default:
			break;
		}	
		super.onActivityCreated(savedInstanceState);
	}

	public void refresh(ArrayList<DeviceSensor> tmpList){
		if(adapter != null){
			ArrayList<DeviceSensor> sList = new ArrayList<DeviceSensor>();						
			for (DeviceSensor deviceSensor : tmpList) {
				if(deviceSensor.getZoneId()==zoneId){
					sList.add(deviceSensor);
				}
			}
			int sListSize = sList.size();
			int listSize = list.size();
			if(sListSize <=0){
				list.clear();
				ArrayList<DeviceProperty> dlist = new ArrayList<DeviceProperty>();
				dlist.add(new DeviceProperty(0, "该区域下暂无终端", "点击添加", "","", 0));
				list.add(new DeviceSensor(0, 0, 0, 1, "添加新的终端", "0000",9 , dlist));
				adapter.notifyDataSetChanged();
			}else{
				if(listSize == sListSize){//终端无增无减
					for (DeviceSensor deviceSensor : sList) {
						for(int i =0;i<listSize;i++){										
							if(list.get(i).getId() == deviceSensor.getId()){
								if(!list.get(i).equals(list.get(i).getpList(), deviceSensor.getpList()) || 
										!list.get(i).getName().equals(deviceSensor.getName())  ){
									list.remove(i);
									list.add(i,deviceSensor);
									adapter.notifyDataSetChanged();
								}											
							}			
						}
					}
					
				}else{
					list.clear();
					for (DeviceSensor deviceSensor : sList) {	
						list.add(deviceSensor);
						adapter.notifyDataSetChanged();
					}			
				}
			}			
		}
	}
	private static class MyHandler extends Handler {
		WeakReference<FragmentZoneControlPage> mFragment;
		public MyHandler(FragmentZoneControlPage fragment){
			mFragment = new WeakReference<FragmentZoneControlPage>(fragment);
		}
		@Override
		public void handleMessage(Message msg) {
			FragmentZoneControlPage theFragment = mFragment.get();
			ArrayList<DeviceSensor> tmpList = null;
			if(msg.obj != null){
				switch (msg.what) {
				case Constant.REVToHotSpotNet_ID:
					theFragment.refresh(ExecSql.getDevice());
					if(theFragment.IsRefreshable){
						msg = new Message();	
						msg.what = Constant.Refreshable_ID;
						msg.obj = 1;
						theFragment.IsRefreshable = false;
						theFragment.handler.sendMessage(msg);
					}
					break;
				case Constant.DEVICESENSOR_ID:
					try {
						tmpList = ParseDevice.parseTerminalDevice(msg.obj.toString());
//						Log.d("GetTerminalDeviceRunnable","OK");
						theFragment.refresh(tmpList);
					} catch (JSONException e) {
					}
					if(theFragment.IsRefreshable){
						msg = new Message();	
						msg.what = Constant.Refreshable_ID;
						msg.obj = 1;
						theFragment.IsRefreshable = false;
						theFragment.handler.sendMessage(msg);
					}
					break;
				case Constant.Refreshable_ID:
					theFragment.deviceRefreshable.setRefreshing(false);
					theFragment.IsRefreshable = false;
					break;
				default:
					break;
				}
			}			
			super.handleMessage(msg);
		}	
		
	}


}
