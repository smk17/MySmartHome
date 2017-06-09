package com.smk17.mysmarthome.fragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import com.smk17.android.tools.ToolNetwork;
import com.smk17.mysmarthome.Constant;
import com.smk17.mysmarthome.MyApplication;
import com.smk17.mysmarthome.R;
import com.smk17.mysmarthome.adapter.SceneControlItemAdapter;
import com.smk17.mysmarthome.cloud.ExecToCloudSql;
import com.smk17.mysmarthome.cloud.ParseDevice;
import com.smk17.mysmarthome.db.ExecSql;
import com.smk17.mysmarthome.domain.DeviceScene;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class FragmentSceneControl extends MianFragment{

	private ArrayList<DeviceScene> list = null;
	private ListView deviceList;
	private SwipeRefreshLayout deviceRefreshable;
	private boolean IsRefreshable = false;
	private SceneControlItemAdapter adapter;
	private Timer timer = new Timer();
	private MyHandler handler = new MyHandler(this);
			
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.material_fragment_scenecontrol, container, false);  
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//			SystemBarTintManager tintManager = new SystemBarTintManager(getActivity()); 
//			SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();  
//			view.setPadding(0,0, config.getPixelInsetRight(), config.getPixelInsetBottom()); 
//		} 
		deviceList = (ListView)view.findViewById(R.id.device_list);
		list = new ArrayList<DeviceScene>();
		adapter = new SceneControlItemAdapter(list, getActivity(), SceneControlItemAdapter.TYPE_SceneControl);
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
			timer = new Timer();
			timer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					if(IsRefreshable || MyApplication.getNetworkType().equals(ToolNetwork.NETWORK_WIFI)){
						ExecToCloudSql.getAllSceneRunnable(handler);
					}
					
				}
			}, 500, Constant.TASKTIME);
			break;
		case Constant.Intranet_Mode:
			//TODO 内网模式下的初始化
			refresh(new ArrayList<DeviceScene>());
			break;
		case Constant.HotSpot_Mode:
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
	
	public void refresh(ArrayList<DeviceScene> tmpList){
		if(tmpList.size() <=0 ){
			list.clear();
			list.add(new DeviceScene(0, "添加新的情景模式", 0 ,0,1,9, "该主控暂无情景模式：点击添加",null));
			adapter.notifyDataSetChanged();
		}else{
			if(!equals(list,tmpList)){
				list.clear();
				for (DeviceScene deviceScene : tmpList) {
					list.add(deviceScene);
					adapter.notifyDataSetChanged();
				}
			}
		}
	}
	
	private static class MyHandler extends Handler {
		WeakReference<FragmentSceneControl> mFragment;
		public MyHandler(FragmentSceneControl fragment){
			mFragment = new WeakReference<FragmentSceneControl>(fragment);
		}
		@Override
		public void handleMessage(Message msg) {
			FragmentSceneControl theFragment = mFragment.get();
			ArrayList<DeviceScene> tmpList = null;
			if(msg.obj!=null){
				switch (msg.what) {
				case Constant.REVToHotSpotNet_ID:					
					theFragment.refresh(ExecSql.getScene());
					if(theFragment.IsRefreshable){
						msg = new Message();	
						msg.what = Constant.Refreshable_ID;
						msg.obj = 1;
						theFragment.IsRefreshable = false;
						theFragment.handler.sendMessage(msg);
					}
					break;
				case Constant.SCENE_ID:
					String json = String.valueOf(msg.obj);
					if(theFragment.adapter !=null){
						tmpList = ParseDevice.parseSceneControl(json);	
						theFragment.refresh(tmpList);
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

	private boolean equals(ArrayList<DeviceScene> list,
			ArrayList<DeviceScene> tmpList) {
		if(list.size() == tmpList.size()){
			for (DeviceScene deviceScene : tmpList) {
				for (DeviceScene scene : list) {
					if(scene.getID() == deviceScene.getID()){
						if(!scene.getValue().equals(deviceScene.getValue()))
							return false;
					}
				}
			}
			return true;
		}else{
			return false;
		}
	}	

}
