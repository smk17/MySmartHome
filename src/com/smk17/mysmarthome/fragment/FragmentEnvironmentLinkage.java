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
import com.smk17.mysmarthome.domain.DeviceProperty;
import com.smk17.mysmarthome.domain.DeviceSensor;
import com.smk17.mysmarthome.domain.DeviceZone;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class FragmentEnvironmentLinkage extends MianFragment{

	private ArrayList<DeviceSensor> list = null;
	private ArrayList<DeviceZone> zList =null;
	private Timer timer = new Timer();
	private ListView deviceList;
	private SwipeRefreshLayout deviceRefreshable;
	private boolean IsRefreshable = false;
	private FragmentItemAdapter adapter;
	private Handler handler = new MyHandler(this);
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.material_fragment_environmentlinkage, container, false);
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//			SystemBarTintManager tintManager = new SystemBarTintManager(getActivity()); 
//			SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();  
//			view.setPadding(0,0, config.getPixelInsetRight(), config.getPixelInsetBottom()); 
//		} 
		list = new ArrayList<DeviceSensor>();
		adapter = new FragmentItemAdapter(list, getActivity(),FragmentItemAdapter.TYPE_EnvironmentLinkage );
		deviceList = (ListView)view.findViewById(R.id.device_list);
		deviceRefreshable = (SwipeRefreshLayout)view.findViewById(R.id.device_refreshable);
		deviceRefreshable.setColorSchemeResources(R.color.title_background);
		deviceList.setAdapter(adapter);
		deviceRefreshable.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				IsRefreshable = true;
			}
		});
		switch (MyApplication.getNetMode()) {
		case Constant.Network_Mode:
			//TODO 外网模式下的初始化
			ExecToCloudSql.getZoneRunnable(handler);
			timer = new Timer();
			timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					if(IsRefreshable || MyApplication.getNetworkType().equals(ToolNetwork.NETWORK_WIFI)){
						ExecToCloudSql.getTerminalDeviceRunnable(handler);
					}
				}
			}, 500, Constant.TASKTIME);
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
		
		return view;
	}

	@Override
	public void onDestroy() {
		timer.cancel();
		super.onDestroy();
	}
	
	private static class MyHandler extends Handler {
		WeakReference<FragmentEnvironmentLinkage> mFragment;
		public MyHandler(FragmentEnvironmentLinkage fragment){
			mFragment = new WeakReference<FragmentEnvironmentLinkage>(fragment);
		}
		@Override
		public void handleMessage(Message msg) {
			FragmentEnvironmentLinkage theFragment = mFragment.get();
			if(msg.obj!=null){
				switch (msg.what) {			
				case Constant.DEVICESENSOR_ID:
					try {
						String json = String.valueOf(msg.obj);
						ArrayList<DeviceSensor> tmpList =ParseDevice.parseTerminalDevice(json);
						theFragment.list.clear();
						if(theFragment.zList != null){
							for (DeviceSensor deviceSensor : tmpList) {
								if(deviceSensor.getCategoryId()==2 && deviceSensor.getDeviceId() == MyApplication.getMainDeviceId()){
									String zoneName = null;
									for (DeviceZone deviceZone : theFragment.zList) {
										if(deviceZone.getId() == deviceSensor.getZoneId()){
											zoneName = deviceZone.getName();
										}
									}
									if(zoneName != null){
										deviceSensor.setName(zoneName + "(" +  deviceSensor.getName()  + ")");
									}								
									theFragment.list.add(deviceSensor);
								}
							}		
							if(theFragment.list.size() <= 0){
								ArrayList<DeviceProperty> dlist = new ArrayList<DeviceProperty>();
								dlist.add(new DeviceProperty(0, "该主控下暂无温湿度传感器", "点击添加", "","", 0));
								theFragment.list.add(new DeviceSensor(0, 0, 0, 1, "添加新的温湿度传感器", "0000", 9,dlist));
							}
						}					
						if( theFragment.adapter !=null){
							theFragment.adapter.notifyDataSetChanged();
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						Log.e("JSONException", "error" );
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
				case Constant.DEVICEZONE_ID:
					try {
						theFragment.zList =  ParseDevice.parseDeviceZone(String.valueOf(msg.obj));
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				default:
					break;
				}
			}
			
			super.handleMessage(msg);
		}	
		
	}

}
