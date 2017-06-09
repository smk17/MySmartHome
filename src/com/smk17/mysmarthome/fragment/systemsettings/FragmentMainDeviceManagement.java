package com.smk17.mysmarthome.fragment.systemsettings;

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
import com.smk17.mysmarthome.db.ExecSql;
import com.smk17.mysmarthome.domain.DeviceSensor;
import com.yh.materialdesign.dialog.MaterialDialog;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class FragmentMainDeviceManagement extends SystemFragment {
	
	private MyHandler handler = new MyHandler(this);
	private RelativeLayout mRlLoad;
	private ListView deviceList;
	private MaterialDialog mLoadDialog;
	private FragmentItemAdapter adapter;
	private ArrayList<DeviceSensor> dList = null; 
	
	public static final FragmentMainDeviceManagement newInstance(){
		FragmentMainDeviceManagement fragment = new FragmentMainDeviceManagement();
		return fragment;		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.material_fragment_maindevicemanagement, container, false);  		
		deviceList = (ListView)view.findViewById(R.id.device_list);
		mRlLoad = (RelativeLayout)view.findViewById(R.id.device_load);
		dList = new ArrayList<DeviceSensor>();
		mLoadDialog = new MaterialDialog(getActivity()).setTitle("正在保存...").setShowProgress(true);
        adapter = new FragmentItemAdapter(dList, getActivity(), FragmentItemAdapter.TYPE_MainDevice,fla);
        deviceList.setAdapter(adapter);
		switch (MyApplication.getNetMode()) {
		case Constant.Network_Mode:
			//TODO 外网模式下的初始化
			ExecToCloudSql.getMainDeviceRunnable(handler);
			break;
		case Constant.Intranet_Mode:
			//TODO 内网模式下的初始化
			break;
		case Constant.HotSpot_Mode:
			//TODO 热点模式下的初始化
			ArrayList<DeviceSensor> tmpList =ExecSql.getMainDevice();
			dList.clear();
			dList.addAll(tmpList);
			if(mLoadDialog!=null&& mLoadDialog.isShow())
				mLoadDialog.dismiss();
			mRlLoad.setVisibility(View.GONE);
			deviceList.setVisibility(View.VISIBLE);
			break;
		default:
			break;
		}
		return view;
	}
	
	private FIABtnClickListener fla = new FIABtnClickListener() {
		
		@Override
		public void onFEditBtnClick(final DeviceSensor device) {
			switch (MyApplication.getNetMode()) {
			case Constant.Network_Mode:
				//TODO 外网模式下的初始化
				ExecToCloudSql.UpdateDeviceRunnable(handler, device);
				ExecToCloudSql.getMainDeviceRunnable(handler,Constant.TASKTIME);					
				break;
			case Constant.Intranet_Mode:
				//TODO 内网模式下的初始化
				break;
			case Constant.HotSpot_Mode:
				//TODO 热点模式下的初始化
				ExecSql.updateDevice(device, false);
				ArrayList<DeviceSensor> tmpList =ExecSql.getMainDevice();
				dList.clear();
				dList.addAll(tmpList);
				if(mLoadDialog!=null&& mLoadDialog.isShow())
					mLoadDialog.dismiss();
				mRlLoad.setVisibility(View.GONE);
				deviceList.setVisibility(View.VISIBLE);
				break;
			default:
				break;
			}
				
		}
		
		@Override
		public void onFDeleteBtnClick(final DeviceSensor device) {			
			switch (MyApplication.getNetMode()) {
			case Constant.Network_Mode:
				//TODO 外网模式下的初始化
				ExecToCloudSql.DeleteMainDeviceRunnable(handler, device);
				ExecToCloudSql.getMainDeviceRunnable(handler,Constant.TASKTIME);				
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
			
		}
		
		@Override
		public void onFClick(Integer position) {
			// TODO Auto-generated method stub
			
		}
	};
	
	
	private static class MyHandler extends Handler { 
		//TODO MyHandler类定义
		WeakReference<FragmentMainDeviceManagement> mActivity;
		
		public MyHandler(FragmentMainDeviceManagement Activity){
			mActivity = new WeakReference<FragmentMainDeviceManagement>(Activity);
		}
				
		@Override
		public void handleMessage(Message msg) {			
			final FragmentMainDeviceManagement theActivity = mActivity.get();
			if(msg.obj!=null){
				switch (msg.what) {
				case Constant.MAINDEVICE_ID:
					try {
						ArrayList<DeviceSensor> tmpList = ParseDevice.parseMainDevice(msg.obj.toString());
						theActivity.dList.clear();
						for (DeviceSensor deviceSensor : tmpList) {
							theActivity.dList.add(deviceSensor);
							theActivity.adapter.notifyDataSetChanged();
						}
						if(theActivity.mLoadDialog!=null&& theActivity.mLoadDialog.isShow())
							theActivity.mLoadDialog.dismiss();
						theActivity.mRlLoad.setVisibility(View.GONE);
						theActivity.deviceList.setVisibility(View.VISIBLE);
					} catch (JSONException e) {
						Log.d("JSONException", msg.obj.toString());
					}
					break;
				case Constant.STATUS_ID:
					theActivity.mLoadDialog.setTitle(msg.obj.toString()).show();
					break;

				default:
					break;
				}
				
			}		
		}
	};
	
}
