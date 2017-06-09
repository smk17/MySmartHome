package com.smk17.mysmarthome.fragment.usersettings;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import org.json.JSONException;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.smk17.mysmarthome.Constant;
import com.smk17.mysmarthome.R;
import com.smk17.mysmarthome.adapter.FragmentItemAdapter;
import com.smk17.mysmarthome.adapter.FragmentItemAdapter.FIABtnClickListener;
import com.smk17.mysmarthome.cloud.ExecToCloudSql;
import com.smk17.mysmarthome.cloud.ParseDevice;
import com.smk17.mysmarthome.domain.DeviceSensor;

public class FragmentLogin extends UserFragment{
	private ArrayList<DeviceSensor> dList =  new ArrayList<DeviceSensor>();
	private ListView deviceList;	
	private SwipeRefreshLayout deviceRefreshable;
	private boolean IsRefreshable = false;
	private FragmentItemAdapter adapter;
	private MyHandler handler = new MyHandler(this);
	
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
			// TODO Auto-generated method stub
			
		}
	};
	
	public static final FragmentLogin newInstance(){
		FragmentLogin fragment = new FragmentLogin();
		return fragment;		
	}	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.material_fragment_usersettings_login, container, false);  	
		deviceList = (ListView)view.findViewById(R.id.device_list);
		deviceRefreshable = (SwipeRefreshLayout)view.findViewById(R.id.device_refreshable);
		deviceRefreshable.setColorSchemeResources(R.color.title_background);
		dList = new ArrayList<DeviceSensor>();
		adapter = new FragmentItemAdapter(dList, getActivity(),FragmentItemAdapter.TYPE_Login ,fBtnClickListener);		 
		deviceList.setAdapter(adapter);		
		deviceRefreshable.setOnRefreshListener(new OnRefreshListener() {
			
			@Override
			public void onRefresh() {
				new Thread(){
					@Override
					public void run() {
						IsRefreshable = true;
						try {
							Thread.sleep(Constant.TASKTIME);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}						
						ExecToCloudSql.getLoginRunnable(handler);						
					}
				}.start();
			}
		});
		ExecToCloudSql.getLoginRunnable(handler);		
		return view;
	}
	
	private static class MyHandler extends Handler {
		WeakReference<FragmentLogin> mFragment;
		public MyHandler(FragmentLogin fragment){
			mFragment = new WeakReference<FragmentLogin>(fragment);
		}
		
		@Override
		public void handleMessage(Message msg) {
			FragmentLogin theFragment = mFragment.get();
			if(msg.obj !=null){
				ArrayList<DeviceSensor> tList = null;
				switch (msg.what) {
				case Constant.LOGIN_SERVER_ID:
					try {
						tList= ParseDevice.parseLogin(msg.obj.toString());
						theFragment.dList.clear();
						theFragment.dList.addAll(tList);
						theFragment.adapter.notifyDataSetChanged();
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(theFragment.IsRefreshable){
						msg = new Message();	
						msg.what = Constant.Refreshable_ID;
						msg.obj = 1;
						theFragment.IsRefreshable = false;
						this.sendMessage(msg);
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
		}
	}
}
