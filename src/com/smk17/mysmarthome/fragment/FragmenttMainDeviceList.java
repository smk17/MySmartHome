package com.smk17.mysmarthome.fragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import org.json.JSONException;

import com.smk17.mysmarthome.Constant;
import com.smk17.mysmarthome.MyApplication;
import com.smk17.mysmarthome.R;
import com.smk17.mysmarthome.adapter.FragmentItemAdapter;
import com.smk17.mysmarthome.adapter.FragmentItemAdapter.FIABtnClickListener;
import com.smk17.mysmarthome.cloud.ExecToCloudSql;
import com.smk17.mysmarthome.cloud.ParseDevice;
import com.smk17.mysmarthome.domain.DeviceSensor;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class FragmenttMainDeviceList extends MianFragment {

	private ArrayList<DeviceSensor> list = null;
	private ListView deviceList;	
	private SwipeRefreshLayout deviceRefreshable;
	private boolean IsRefreshable = false;
	private FragmentItemAdapter adapter;
	private SelectMainDeviceListener mSelectMainDeviceListener = null;
	private MyHandler handler = new MyHandler(this);
	
	public static final FragmenttMainDeviceList newInstance(SelectMainDeviceListener selectMainDeviceListener){         
		FragmenttMainDeviceList fragment = new FragmenttMainDeviceList(selectMainDeviceListener);
		return fragment;  
	} 
	public FragmenttMainDeviceList(){
		this.mSelectMainDeviceListener = null;
	}
	
	private FragmenttMainDeviceList(SelectMainDeviceListener selectMainDeviceListener){
		this.mSelectMainDeviceListener = selectMainDeviceListener;
	}
	
	public interface SelectMainDeviceListener  
    {
		void onSelectMainDevice();
    }  
	
	private FIABtnClickListener fBtnClickListener = new FIABtnClickListener() {
		
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
			if(mSelectMainDeviceListener!=null)
			mSelectMainDeviceListener.onSelectMainDevice();
		}
	};
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.material_fragment_maindevicelist, container, false);  
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//			SystemBarTintManager tintManager = new SystemBarTintManager(getActivity()); 
//			SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();  
//			view.setPadding(0,0, config.getPixelInsetRight(), config.getPixelInsetBottom()); 
//		} 
		deviceList = (ListView)view.findViewById(R.id.device_list);
		deviceRefreshable = (SwipeRefreshLayout)view.findViewById(R.id.device_refreshable);
		deviceRefreshable.setColorSchemeResources(R.color.title_background);
		list = new ArrayList<DeviceSensor>();
		adapter = new FragmentItemAdapter(list, getActivity(),FragmentItemAdapter.TYPE_MainDeviceList ,fBtnClickListener);		 
		deviceList.setAdapter(adapter);
		
		switch (MyApplication.getNetMode()) {
		case Constant.Network_Mode:
			//TODO 外网模式下的初始化
			deviceRefreshable.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
				
				@Override
				public void onRefresh() {
					new Thread(){
						@Override
						public void run() {
							try {
								Thread.sleep(Constant.TASKTIME);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							ExecToCloudSql.getMainDeviceRunnable(handler);
							IsRefreshable = true;
						}
					}.start();
				}
			});
			ExecToCloudSql.getMainDeviceRunnable(handler);
			break;
		case Constant.Intranet_Mode:
			//TODO 内网模式下的初始化
			break;
		case Constant.HotSpot_Mode:
			//TODO 热点模式下的初始化
			deviceRefreshable.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
				
				@Override
				public void onRefresh() {
					deviceRefreshable.setRefreshing(false);						
				}
			});
			break;
		default:
			break;
		}
		
		return view;
	}
	
	public void refresh(ArrayList<DeviceSensor> tmpList){
		if(adapter!=null){
			list.clear();
			for (DeviceSensor deviceSensor : tmpList) {
				list.add(deviceSensor);
				adapter.notifyDataSetChanged();
			}		
			if(MyApplication.getMainDeviceId()==null){
				MyApplication.setMainDeviceId(tmpList.get(0).getId());
			}
			
		}	
	}
	
	private static class MyHandler extends Handler {
		WeakReference<FragmenttMainDeviceList> mFragment;
		public MyHandler(FragmenttMainDeviceList fragment){
			mFragment = new WeakReference<FragmenttMainDeviceList>(fragment);
		}
		@Override
		public void handleMessage(Message msg) {
			FragmenttMainDeviceList theFragment = mFragment.get();
			if(msg.obj !=null){
				switch (msg.what) {			
				case Constant.MAINDEVICE_ID:
					ArrayList<DeviceSensor> tmpList = null;
					try {
						tmpList = ParseDevice.parseMainDevice(msg.obj.toString());
//						Log.d("GetTerminalDeviceRunnable","OK");
						theFragment.refresh(tmpList);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
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
