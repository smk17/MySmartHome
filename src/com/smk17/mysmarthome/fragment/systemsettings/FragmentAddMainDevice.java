package com.smk17.mysmarthome.fragment.systemsettings;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import com.smk17.android.tools.ToolAlert;
import com.smk17.android.tools.ToolNetwork;
import com.smk17.android.tools.ToolString;
import com.smk17.android.tools.WifiAdmin;
import com.smk17.mysmarthome.Constant;
import com.smk17.mysmarthome.MyApplication;
import com.smk17.mysmarthome.R;
import com.smk17.mysmarthome.Utils.HttpUtils;
import com.smk17.mysmarthome.cloud.ParseDevice;
import com.smk17.mysmarthome.domain.DeviceSensor;
import com.yh.materialdesign.dialog.MaterialDialog;
import com.yh.materialdesign.edittext.MaterialEditText;
import com.yh.materialdesign.views.ButtonFloatSmall;
import com.yh.materialdesign.views.RoundProgressBar;
import com.yh.materialdesign.views.RoundProgressBar.OnLoadFinishListener;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class FragmentAddMainDevice extends SystemFragment {
	
	private ButtonFloatSmall fsAvatar;
	private LinearLayout mLiAvatarConn;
	private ButtonFloatSmall fsAvatarConn;
	private ButtonFloatSmall fsAvatarConn2;
	private ButtonFloatSmall fsAvatarOk;
	private TextView mTvName;
	private TextView mTvReason;
	private Button mBtnNext;
	private Button mBtnStillConn;
	private Button mBtnCancelConnt;
	private LinearLayout mLlWifi;
	private LinearLayout mLlBtn;
	private MaterialEditText mMetSsid;
	private MaterialEditText mMetPassword;
	private RoundProgressBar roundProgressBar;
	private WifiAdmin wifiAdmin = null;
	private List<ScanResult> wifiList = null;
	private MaterialDialog alert = null;
	private ArrayAdapter<String> arrayAdapter;
	private int countTag = MyHandler.gotojcloud;
	private int progress = 0;
	private int countCheckState = 0;
	private int foundDeviceNum = 1;	
	private String wifiSSID = "";
	private String wifiPassword = "";
	private String MainDeviceUuid = "";
	private String MainDeviceUuidBak = "";
	private static boolean issys = false;
	private MyHandler handler = new MyHandler(this);
	
	public static final FragmentAddMainDevice newInstance(){
		FragmentAddMainDevice fragment = new FragmentAddMainDevice();
		return fragment;		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.material_fragment_addmaindevice, container, false);
		mLiAvatarConn = (LinearLayout)view.findViewById(R.id.ll_avatar_conn);
		fsAvatarConn = (ButtonFloatSmall)view.findViewById(R.id.fs_avatar1);
		fsAvatarConn2 = (ButtonFloatSmall)view.findViewById(R.id.fs_avatar2);
		fsAvatarOk = (ButtonFloatSmall)view.findViewById(R.id.fs_avatar_ok);
//		searchView = (RelativeLayout)view.findViewById(R.id.search_view);
		fsAvatar = (ButtonFloatSmall)view.findViewById(R.id.fs_avatar);
		mTvName = (TextView)view.findViewById(R.id.tv_name);
		mTvReason = (TextView)view.findViewById(R.id.tv_reason);
		mBtnNext = (Button)view.findViewById(R.id.btn_next);
		mBtnCancelConnt= (Button)view.findViewById(R.id.btn_cancel_conn);
		mBtnStillConn = (Button)view.findViewById(R.id.btn_still_conn);
		mLlWifi  = (LinearLayout)view.findViewById(R.id.ll_wifi);
		mLlBtn = (LinearLayout)view.findViewById(R.id.ll_btn);
		mMetSsid = (MaterialEditText)view.findViewById(R.id.met_main_device_ssid);
		mMetPassword = (MaterialEditText)view.findViewById(R.id.met_main_device_password);
		roundProgressBar = (RoundProgressBar)view.findViewById(R.id.roundProgressBar);
		roundProgressBar.setTextColor(getResources().getColor(R.color.textColor));
		roundProgressBar.setCricleColor(getResources().getColor(R.color.roundColor));
		roundProgressBar.setCricleProgressColor(getResources().getColor(R.color.roundProgressColor));
		wifiAdmin = new WifiAdmin(getActivity());	   
		arrayAdapter = new ArrayAdapter<String>(
				getActivity(), R.layout.list_item2);

		wifiAdmin.openWifi();
		wifiAdmin.startScan(); // 扫描wifi热点，前提是wifi已经打开
		wifiList = wifiAdmin.getWifiList();
		arrayAdapter.clear();
		for (int i = 0; i < wifiList.size(); i++) {
			String ssid = wifiList.get(i).SSID;
			if(!ssid.contains(Constant.WIFINamePrefix)){
				arrayAdapter.add(ssid);
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

		alert = new MaterialDialog(getActivity()).setTitle("选择SSID").setContentView(listView);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				alert.dismiss();
				if(mMetSsid!=null) 
					mMetSsid.setText(arrayAdapter.getItem(arg2));
//				tdZone.setText(arg0.getAdapter().getItem(arg2).toString());							
			}
		});
		alert.setPositiveButton("取消", new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				alert.dismiss();
			}
		});
		
		mMetPassword.setRightClickListener(new MaterialEditText.RightClickListener() {
			
			@Override
			public void onRightClick() {
				// TODO Auto-generated method stub
				if(issys){
					mMetPassword.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}else{
					mMetPassword.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				}
				issys = !issys;
			}
		});
		roundProgressBar.setOnLoadFinishListener(new OnLoadFinishListener() {
			
			@Override
			public void onLoadFinished() {
				// TODO 进度条加载完毕触发
//				MainDeviceConnOk();
				MainDeviceConnFailure();
				/*wifiAdmin.disconnectWifi(wifiAdmin.getNetworkId());
				wifiAdmin.openWifi();
				wifiAdmin.addNetwork(wifiAdmin.CreateWifiInfo(wifiSSID, wifiPassword, 3));		*/
			}
		});
		view.findViewById(R.id.btn_selector_ssid).setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				alert.show();
			}
		});
		mBtnStillConn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch (countTag) {
				case MyHandler.phoneljrouterfailure:
					PhoneLjRouterIng();
					break;
				case MyHandler.phoneljcloudfailure:
					PhoneLjCloudIng();
					break;
				default:
					break;
				}
			}
		});
		mBtnNext.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch (countTag) {
				case MyHandler.gotojcloud:
					wifiAdmin.openWifi();
					wifiAdmin.startScan(); // 扫描wifi热点，前提是wifi已经打开
					wifiList = wifiAdmin.getWifiList();
					arrayAdapter.clear();
					for (int i = 0; i < wifiList.size(); i++) {
						String ssid = wifiList.get(i).SSID;
						if(!ssid.contains(Constant.WIFINamePrefix) && !ssid.equals("")){
							Log.e("Wifi ssid:", ssid);
							arrayAdapter.add(ssid);
							arrayAdapter.notifyDataSetChanged();
						}
					}
					GoToConnCloud();
					break;
				case MyHandler.phoneljrouter:
					wifiSSID = mMetSsid.getText().toString();
					wifiPassword = mMetPassword.getText().toString();
					if(!wifiSSID.equals("")&&!wifiPassword.equals("")){
						PhoneLjRouterIng();						
					}
					break;
				case MyHandler.phoneljrouterfailure:
					GoToConnCloud();
					break;
				case MyHandler.phoneljcloudfailure:
					GoToConnCloud();
					break;
				case MyHandler.MainDeviceConnIng:
					progress = 100;
					getActivity().finish();
					break;
				case MyHandler.MainDeviceConnok:
					getActivity().finish();
					break;
				case MyHandler.MainDeviceConnfailure:				
					countTag = MyHandler.gotojcloud;
					ImageView icon = fsAvatar.getIcon();
					icon.setBackgroundResource(R.drawable.ic_router);		
					fsAvatar.setIcon(icon);
					fsAvatar.setBackgroundColor(getResources().getColor(R.color.search_background));
					mTvName.setText(getResources().getString(R.string.search_name));
					mTvReason.setText(getResources().getString(R.string.search_reason));
					mBtnNext.setText(getResources().getString(R.string.next_name));
					mLlWifi.setVisibility(View.GONE);
					fsAvatar.setVisibility(View.VISIBLE);
					fsAvatarOk.setVisibility(View.GONE);
					mLiAvatarConn.setVisibility(View.GONE);
					roundProgressBar.setVisibility(View.GONE);
					mTvName.setVisibility(View.VISIBLE);
					mTvReason.setVisibility(View.VISIBLE);
					mBtnNext.setVisibility(View.VISIBLE);
					mBtnCancelConnt.setVisibility(View.GONE);
					mBtnStillConn.setVisibility(View.GONE);				
					break;
				default:
					break;
				}
			}
		});
		return view;
	}
	
	private Runnable checkStateRunnable = new Runnable() {
		
		@Override
		public void run() {
			//TODO 在手机与路由器进行连接的时候，获取连接状态
			Thread t = new Thread(){
				@Override
				public void run() {
					Message msg = new Message();
					msg.what = MyHandler.phoneljrouter;
					countCheckState = 0;
					while (countCheckState <=5) {
						try {
//							wifiAdmin.openWifi();
//							wifiAdmin.startScan(); // 扫描wifi热点，前提是wifi已经打开
//							int id =  wifiAdmin.getNetworkId() ;
//							msg.obj=id;
//							if(id > 0){						
//								handler.sendMessage(msg);
//								countCheckState = 6;
//							}else{
//								if(countCheckState == 5){
//									handler.sendMessage(msg);
//								}
//							}
							String NetworkType = ToolNetwork.getInstance().init(MyApplication.gainContext()).getNetworkType();
							if(NetworkType.equals(ToolNetwork.NETWORK_WIFI)){
								msg.obj = 2;
								handler.sendMessage(msg);
								countCheckState = 6;
							}else{
								if(countCheckState == 5){
									msg.obj = 0;
									handler.sendMessage(msg);
								}
								
							}
							countCheckState++;
							Thread.sleep(500);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			};
			t.start();						
		}
	};
	
	private void toast(){
		String NetworkType = ToolNetwork.getInstance().init(MyApplication.gainContext()).getNetworkType();
		if (!NetworkType.equals(ToolNetwork.NETWORK_WIFI)){
			Message message = new Message();
			message.what = 1;
			message.obj = "请连接到可以上网的WiFi.";
			handler.sendMessage(message);
		}
	}
	
	private Runnable getNewMainDevice = new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			wifiAdmin.disconnectWifi(wifiAdmin.getNetworkId());
			//wifiAdmin.addNetwork(wifiAdmin.CreateWifiInfo(wifiSSID, wifiPassword, 3));
			toast();
			Message msg = new Message();
			msg.what = Constant.DEVICESENSOR_ID;			
			Map<String, String> params = new HashMap<String, String>();
			params.put("uuid", MainDeviceUuid);
			params.put("ak", Constant.REQUEST_AK);
			try {
				while (true){					
					String NetworkType = ToolNetwork.getInstance().init(MyApplication.gainContext()).getNetworkType();
					if (NetworkType.equals(ToolNetwork.NETWORK_WIFI)){
						String ssid = wifiAdmin.getSSID();
						if(ssid.equals("\""+wifiSSID+"\"")){
							msg.obj = HttpUtils.Post(Constant.URL_GET_NEWMAINDEVICE, params);
							Log.e("getNewMainDevice", msg.obj.toString());
							handler.sendMessage(msg);
							break;
						}						
					}	/*
					if (i >= 0)	i++;
					if (i > 3){
						i=-1;
						Message message = new Message();
						message.what = 1;
						message.obj = "程序自动连接" + "\""+wifiSSID+"\"" + "失败，请自行连接";
						handler.sendMessage(message);
					}*/
					Thread.sleep(1000);
				}				
			} catch (Exception e) {
				Log.e("getNewMainDevice", e.getMessage());
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	};
		
	private Runnable sendMsg = new Runnable() {
    	@Override
    	public void run()
    	{
    		//TODO 往主控发送消息发送线程
        	try{
        		if(ToolString.isNoBlankAndNoNull(wifiSSID.trim()) && ToolString.isNoBlankAndNoNull(wifiPassword.trim()) ){
        			String data = "";//tcpService
            		byte[] rBuffer = new byte[1024];
        			Socket s = new Socket(Constant.SERVER_IP, Constant.SERVER_PORT);
            		DataOutputStream dos=new DataOutputStream(s.getOutputStream());
            		InputStream inputStream = s.getInputStream();
            		DataInputStream input = new DataInputStream(inputStream);
            		dos.write((byte)0xFD);
            		dos.flush();
            		dos.write(wifiSSID.getBytes(Constant.CharsetName));
            		dos.flush();
            		dos.write((byte)0xFF);
            		dos.flush();
            		dos.write(wifiPassword.getBytes(Constant.CharsetName));
            		dos.flush();
            		dos.write((byte)0xFE);
            		dos.flush();
            		
            		while (true){
    	    			Message msg = new Message();
                		int length = input.read(rBuffer);
                		if(length>=1){
                			data = new String(rBuffer, 0, length, Constant.CharsetName);
                    		dos.write(data.getBytes(Constant.CharsetName));
                    		dos.flush();
                    		if(ToolString.isNoBlankAndNoNull(data)){
                    			Log.e("Tcp Demo", "message From Server:" + data);                      			
                                if(data.equals("close")){
                                	wifiAdmin.disconnectWifi(wifiAdmin.getNetworkId());
                                	msg.what = Constant.REVToHotSpotNet_ID;
                                    msg.obj = data;
                                    handler.sendMessage(msg);
                                    break;
                                }
                    		}                    		
                		}                		
                        Thread.sleep(80);
    	    		}
            		dos.close();
                    dos.close();
                    s.close();
        		}
        	}catch (Exception e){
        		Log.e("sendMsg", e.toString());
        	}
    	}
		
	};
	
	private Thread th_progress =  new Thread(new Runnable() {
		
		@Override
		public void run() {
			while(progress <= 100){
				progress += 1;
				roundProgressBar.setProgress(progress);
				if( (progress%5) == 0 ){
					String NetworkType = ToolNetwork.getInstance().init(MyApplication.gainContext()).getNetworkType();
					wifiAdmin.openWifi();
					wifiAdmin.startScan(); // 扫描wifi热点，前提是wifi已经打开			
					String ssid = wifiAdmin.getSSID();
					if(NetworkType.equals(ToolNetwork.NETWORK_WIFI) && ssid.contains(Constant.WIFINamePrefix)){
						if(ssid.contains(Constant.WIFINamePrefix)){
							MainDeviceUuidBak = MainDeviceUuid;
							String tmp[] = ssid.replace("\"", "").split("_");
							MainDeviceUuid = tmp[tmp.length-1];
							Log.d("th_progress", MainDeviceUuid);
							if(!MainDeviceUuid.equals(MainDeviceUuidBak))
								new Thread(sendMsg).start();
						}else{
							wifiAdmin.openWifi();
							wifiAdmin.startScan(); // 扫描wifi热点，前提是wifi已经打开
							wifiList = wifiAdmin.getWifiList();
							for (int i = 0; i < wifiList.size(); i++) {
								ssid = wifiList.get(i).SSID;
								if(ssid.contains(Constant.WIFINamePrefix) && !MainDeviceUuid.equals(MainDeviceUuidBak)){
									Log.e("SSID", ssid);
									WifiConfiguration wgc = wifiAdmin.CreateWifiInfo(ssid, "123456789", 3);
									wifiAdmin.addNetwork(wgc);
								}
							}
						}
						
					}						
				}					
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					Log.e("InterruptedException", e.toString());
				}
			}
			
		}
	});
		
	/**
	 * 进入wifi设置界面
	 */
	@SuppressWarnings("deprecation")
	private void GoToConnCloud(){
//		fsAvatar.setIcoSize(32);
		fsAvatar.setDrawableIcon(getResources().getDrawable(R.drawable.ic_wifi_white_48dp));
		fsAvatar.setBackgroundColor(getResources().getColor(R.color.search_background));
		mBtnNext.setText(getResources().getString(R.string.next_name));
		mTvName.setText(getResources().getString(R.string.ljwifi_name));
		mTvReason.setText(getResources().getString(R.string.ljwifi_reason));
		mMetPassword.setText("");
		mMetSsid.setText("");
		countTag = MyHandler.phoneljrouter;
		mLlWifi.setVisibility(View.VISIBLE);
		fsAvatar.setVisibility(View.VISIBLE);
		fsAvatarOk.setVisibility(View.GONE);
		mLiAvatarConn.setVisibility(View.GONE);
		roundProgressBar.setVisibility(View.GONE);
		mTvName.setVisibility(View.VISIBLE);
		mTvReason.setVisibility(View.VISIBLE);
		mBtnNext.setVisibility(View.VISIBLE);
		mBtnCancelConnt.setVisibility(View.GONE);
		mBtnStillConn.setVisibility(View.GONE);
		mLlBtn.setVisibility(View.VISIBLE);
//		mBtnNext.setEnabled(false);
	}
	/**
	 * 手机连接路由器中。。。。
	 */
	private void PhoneLjRouterIng(){		
		mTvName.setText(getResources().getString(R.string.ljrouter_name));
		mTvReason.setVisibility(View.GONE);
		mBtnNext.setVisibility(View.GONE);
		fsAvatar.setVisibility(View.GONE);
		mLlWifi.setVisibility(View.GONE);
		mLiAvatarConn.setVisibility(View.VISIBLE);
		fsAvatarOk.setVisibility(View.GONE);
		roundProgressBar.setVisibility(View.GONE);
		mTvName.setVisibility(View.VISIBLE);
		mBtnCancelConnt.setVisibility(View.GONE);
		mBtnStillConn.setVisibility(View.GONE);
		mLlBtn.setVisibility(View.GONE);
//		wifiAdmin.disconnectWifi(wifiAdmin.getNetworkId());
//		wifiAdmin.openWifi();
//		wifiAdmin.addNetwork(wifiAdmin.CreateWifiInfo(wifiSSID, wifiPassword, 3));	
		toast();
		handler.postDelayed(checkStateRunnable, 3000);		
		
	}
	/**
	 * 手机连接路由器成功
	 */
	@SuppressWarnings("deprecation")
	private void PhoneLjRouterOk(){
		fsAvatarOk.setDrawableIcon(getResources().getDrawable(R.drawable.ic_check_white_24dp));
		fsAvatarOk.setBackgroundColor(getResources().getColor(R.color.search_background));
		mTvName.setText(getResources().getString(R.string.conn_ok));
		mTvReason.setText(getResources().getString(R.string.ljrouter_ok_reason));
		mBtnStillConn.setVisibility(View.GONE);
		mBtnCancelConnt.setVisibility(View.GONE);
		mTvReason.setVisibility(View.VISIBLE);
		fsAvatarOk.setVisibility(View.VISIBLE);		
		mBtnNext.setVisibility(View.GONE);
		fsAvatar.setVisibility(View.GONE);
		mLlWifi.setVisibility(View.GONE);
		mLiAvatarConn.setVisibility(View.VISIBLE);
		roundProgressBar.setVisibility(View.GONE);
		mTvName.setVisibility(View.VISIBLE);
		mLlBtn.setVisibility(View.GONE);
	}
	/**
	 * 手机连接路由器失败，重新选择wifi
	 */
	@SuppressWarnings("deprecation")
	private void PhoneLjRouterFailure(){
		wifiAdmin.closeWifi();
		fsAvatarOk.setDrawableIcon(getResources().getDrawable(R.drawable.ic_window_close_white_48dp));
		fsAvatarOk.setBackgroundColor(getResources().getColor(R.color.orangered));
		mTvName.setText(getResources().getString(R.string.conn_failure));
		mTvReason.setText(getResources().getString(R.string.ljrouter_failure_reason));
		mBtnNext.setText(getResources().getString(R.string.reconn_name));
		mBtnNext.setVisibility(View.VISIBLE);		
		mTvReason.setVisibility(View.VISIBLE);
		fsAvatarOk.setVisibility(View.VISIBLE);
		mBtnStillConn.setVisibility(View.VISIBLE);
		mBtnCancelConnt.setVisibility(View.VISIBLE);
		fsAvatar.setVisibility(View.GONE);
		mLlWifi.setVisibility(View.GONE);
		mLiAvatarConn.setVisibility(View.VISIBLE);
		roundProgressBar.setVisibility(View.GONE);
		mTvName.setVisibility(View.VISIBLE);
		mLlBtn.setVisibility(View.VISIBLE);
		countTag = MyHandler.phoneljrouterfailure;
		wifiAdmin.openWifi();
	}
	/**
	 * 手机连接云端中。。。
	 */
	private void PhoneLjCloudIng(){
		mTvName.setText(getResources().getString(R.string.ljcloud_name));
		ImageView icon = fsAvatarConn.getIcon();
		icon.setBackgroundResource(R.drawable.ic_router);		
		fsAvatarConn.setIcon(icon);
		fsAvatarConn.setBackgroundColor(getResources().getColor(R.color.router_background));		
		icon = fsAvatarConn2.getIcon();
		icon.setBackgroundResource(R.drawable.ic_cloud);			
		fsAvatarConn2.setIcon(icon);
		fsAvatarConn2.setBackgroundColor(getResources().getColor(R.color.search_background));
		mTvReason.setVisibility(View.GONE);
		mBtnStillConn.setVisibility(View.GONE);
		mBtnCancelConnt.setVisibility(View.GONE);
		fsAvatarOk.setVisibility(View.GONE);
		mBtnNext.setVisibility(View.VISIBLE);
		fsAvatar.setVisibility(View.GONE);
		mLlWifi.setVisibility(View.GONE);
		mLiAvatarConn.setVisibility(View.VISIBLE);
		roundProgressBar.setVisibility(View.GONE);
		mTvName.setVisibility(View.VISIBLE);
		mLlBtn.setVisibility(View.GONE);
	}
	/**
	 * 手机连接云端成功
	 */
	@SuppressWarnings("deprecation")
	private void PhoneLjCloudOk(){
		fsAvatarOk.setDrawableIcon(getResources().getDrawable(R.drawable.ic_check_white_24dp));
		fsAvatarOk.setBackgroundColor(getResources().getColor(R.color.search_background));
		mTvName.setText(getResources().getString(R.string.conn_ok));
		mTvReason.setText(getResources().getString(R.string.ljcloud_ok_reason));
		mTvReason.setVisibility(View.VISIBLE);
		fsAvatarOk.setVisibility(View.VISIBLE);
		mBtnStillConn.setVisibility(View.GONE);
		mBtnCancelConnt.setVisibility(View.GONE);
		mBtnNext.setVisibility(View.VISIBLE);
		fsAvatar.setVisibility(View.GONE);
		mLlWifi.setVisibility(View.GONE);
		mLiAvatarConn.setVisibility(View.VISIBLE);
		roundProgressBar.setVisibility(View.GONE);
		mTvName.setVisibility(View.VISIBLE);
		mLlBtn.setVisibility(View.GONE);
//		wifiAdmin.disconnectWifi(wifiAdmin.getNetworkId());
		wifiAdmin.openWifi();
		wifiAdmin.startScan(); // 扫描wifi热点，前提是wifi已经打开
		wifiList = wifiAdmin.getWifiList();
		for (int i = 0; i < wifiList.size(); i++) {
			String ssid = wifiList.get(i).SSID;
			if(ssid.contains(Constant.WIFINamePrefix)){
				Log.e("SSID", ssid);
				wifiAdmin.disconnectWifi(wifiAdmin.getNetworkId());
				WifiConfiguration wgc = wifiAdmin.CreateWifiInfo(ssid, "123456789", 3);
				wifiAdmin.addNetwork(wgc);
				break;
			}
		}
	}
	/**
	 * 手机连接云端失败
	 */
	@SuppressWarnings("deprecation")
	private void PhoneLjCloudFailure(){
		countTag = MyHandler.phoneljcloudfailure;
		fsAvatarOk.setDrawableIcon(getResources().getDrawable(R.drawable.ic_window_close_white_48dp));
		fsAvatarOk.setBackgroundColor(getResources().getColor(R.color.orangered));
		mTvName.setText(getResources().getString(R.string.conn_failure));
		mTvReason.setText(getResources().getString(R.string.ljcloud_failure_reason));
		mBtnNext.setText(getResources().getString(R.string.reconn_name));
		mBtnNext.setVisibility(View.VISIBLE);		
		mTvReason.setVisibility(View.VISIBLE);
		fsAvatarOk.setVisibility(View.VISIBLE);
		mBtnStillConn.setVisibility(View.VISIBLE);
		mBtnCancelConnt.setVisibility(View.VISIBLE);		
		fsAvatar.setVisibility(View.GONE);
		mLlWifi.setVisibility(View.GONE);
		mLiAvatarConn.setVisibility(View.VISIBLE);
		roundProgressBar.setVisibility(View.GONE);
		mTvName.setVisibility(View.VISIBLE);
		mLlBtn.setVisibility(View.VISIBLE);
	}
	/**
	 * 主控连接到云并与手机建立联系中
	 */
	private void MainDeviceConnIng(){
		mTvReason.setVisibility(View.VISIBLE);
		mBtnStillConn.setVisibility(View.GONE);
		mBtnCancelConnt.setVisibility(View.GONE);		
		fsAvatar.setVisibility(View.GONE);
		mLlWifi.setVisibility(View.GONE);
		mBtnNext.setVisibility(View.VISIBLE);
		mLiAvatarConn.setVisibility(View.GONE);
		roundProgressBar.setVisibility(View.VISIBLE);
		fsAvatarOk.setVisibility(View.GONE);
		mLlBtn.setVisibility(View.VISIBLE);
		mTvName.setText(getResources().getString(R.string.connection_name));
		mTvReason.setText(getResources().getString(R.string.connection_reason));
		mBtnNext.setText(getResources().getString(R.string.cancel_name));		
		countTag = MyHandler.MainDeviceConnIng;
		th_progress.start();
	}
	/**
	 * 主控与手机建立联系成功
	 */
	@SuppressWarnings("deprecation")
	private void MainDeviceConnOk(){
		progress = 0;
		fsAvatar.setDrawableIcon(getResources().getDrawable(R.drawable.ic_check_white_48dp));
		fsAvatar.setBackgroundColor(getResources().getColor(R.color.check_background));
		mBtnNext.setBackgroundResource(R.drawable.btn_ok_selector);
		mBtnNext.setText(getResources().getString(R.string.ok_name));
		roundProgressBar.setProgress(progress);
		roundProgressBar.setVisibility(View.GONE);
		fsAvatar.setVisibility(View.VISIBLE);
		mBtnNext.setVisibility(View.VISIBLE);
		mTvReason.setVisibility(View.VISIBLE);
		mBtnStillConn.setVisibility(View.GONE);
		mBtnCancelConnt.setVisibility(View.GONE);		
		mLlWifi.setVisibility(View.GONE);
		mLiAvatarConn.setVisibility(View.GONE);
		fsAvatarOk.setVisibility(View.GONE);
		mLlBtn.setVisibility(View.VISIBLE);
		countTag = MyHandler.MainDeviceConnok;
	}
	/**
	 * 主控与手机建立联系失败
	 */
	@SuppressWarnings("deprecation")
	private void MainDeviceConnFailure(){
		progress = 0;
		fsAvatar.setDrawableIcon(getResources().getDrawable(R.drawable.ic_window_close_white_48dp));
		fsAvatar.setBackgroundColor(getResources().getColor(R.color.orangered));
		mBtnNext.setText(getResources().getString(R.string.reset_name));
		roundProgressBar.setProgress(progress);
		roundProgressBar.setVisibility(View.GONE);
		fsAvatar.setVisibility(View.VISIBLE);
		mBtnNext.setVisibility(View.VISIBLE);
		mBtnCancelConnt.setVisibility(View.VISIBLE);		
		mTvReason.setVisibility(View.VISIBLE);
		mBtnStillConn.setVisibility(View.GONE);
		mLlWifi.setVisibility(View.GONE);
		mLiAvatarConn.setVisibility(View.GONE);
		fsAvatarOk.setVisibility(View.GONE);		
		mLlBtn.setVisibility(View.VISIBLE);
		countTag = MyHandler.MainDeviceConnfailure;
	}
	
	private static class MyHandler extends Handler { 
		//TODO MyHandler类定义
		public static final int gotojcloud 							= 0x01;
		public static final int phoneljrouter 						= 0x02;
		public static final int phoneljcloud 						= 0x03;
		public static final int phoneljcloudok 					= 0x04;
		public static final int phoneljcloudfailure 				= 0x05;
		public static final int MainDeviceConnIng 			= 0x06;
		public static final int MainDeviceConnok 				= 0x07;
		public static final int MainDeviceConnfailure 		= 0x08;
		public static final int phoneljrouterfailure				= 0x09;
		
		WeakReference<FragmentAddMainDevice> mActivity;
		
		public MyHandler(FragmentAddMainDevice Activity){
			mActivity = new WeakReference<FragmentAddMainDevice>(Activity);
		}
				
		@Override
		public void handleMessage(Message msg) {			
			final FragmentAddMainDevice theActivity = mActivity.get();
			if(msg.obj!=null){
				switch (msg.what) {
				case 1:
					ToolAlert.toast(msg.obj.toString());
					break;
				case phoneljrouter:
					int status = Integer.valueOf(msg.obj.toString());
					if(status > 0){//判断wifi-ssid和密码是否正确，
						theActivity.PhoneLjRouterOk();
						this.postDelayed(new Runnable() {
							
							@Override
							public void run() {
								// TODO Auto-generated method stub
								Message msg = new Message();
								msg.what = phoneljcloud;
								msg.obj = 1;
								MyHandler.this.sendMessage(msg);
							}
						}, 1000);
					}else{
						theActivity.PhoneLjRouterFailure();
					}
					break;
				case phoneljcloud://判断wifi是否可以连接到云
					theActivity.PhoneLjCloudIng();
					this.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Message msg = new Message();
							if(MyApplication.isNetworkReady()){
								msg.what = phoneljcloudok;
								msg.obj = 1;
							}else{
								msg.what = phoneljcloudfailure;
								msg.obj = 1;
							}
							MyHandler.this.sendMessage(msg);
						}
					}, 2000);
					break;
				case phoneljcloudok://路由器可以连接到云
					theActivity.PhoneLjCloudOk();
					this.postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// TODO Auto-generated method stub
							Message msg = new Message();
							msg.what = MainDeviceConnIng;
							msg.obj = 1;
							MyHandler.this.sendMessage(msg);
						}
					}, 1000);					
					break;
				case phoneljcloudfailure://不可以连接到云
					theActivity.PhoneLjCloudFailure();
					break;
				case MainDeviceConnIng://匹配主控，让主控连接到云
					theActivity.MainDeviceConnIng();
					break;		
				case MainDeviceConnok:
					theActivity.MainDeviceConnOk();
					break;
				case MainDeviceConnfailure:
					theActivity.MainDeviceConnFailure();
					break;
				case Constant.REVToHotSpotNet_ID:					
					new Thread(theActivity.getNewMainDevice).start();
					break;	
				case Constant.DEVICESENSOR_ID:
					Log.e("DEVICESENSOR_ID--uuid", theActivity.MainDeviceUuid);					
					if(theActivity.MainDeviceUuid.length() == 18 && theActivity.MainDeviceUuid.substring(0, 4).equals("5401") ){
						try {
							int numrows = ParseDevice.parseMainDeviceNumRows(msg.obj.toString());
							Log.e("DEVICESENSOR_ID--numrows", numrows+"");
							if(numrows > 0){
								ArrayList<DeviceSensor> tmpList = ParseDevice.parseNewMainDevice(msg.obj.toString());
								for (DeviceSensor deviceSensor : tmpList) {
									final int id = deviceSensor.getId();	
									if (theActivity.isAdded()){
										theActivity.mTvReason.setText("已连接"+theActivity.foundDeviceNum+"个设备");
										theActivity.MainDeviceUuidBak = theActivity.MainDeviceUuid;
										theActivity.mBtnNext.setText(theActivity.getResources().getString(R.string.ok_name));
										theActivity.countTag=MainDeviceConnok;
										theActivity.foundDeviceNum++;
										new Thread(new Runnable() {
											
											@Override
											public void run() {
												Map<String, String> params = new HashMap<String, String>();
												params.put("userid", String.valueOf(MyApplication.getUserID()));
												params.put("sensorid", String.valueOf(id));
												params.put("ak", Constant.REQUEST_AK);
												try {					
													String s = HttpUtils.Post(Constant.URL_ADD_NEWMAINDEVICE, params);
													Log.e("DEVICESENSOR_ID", s);
												} catch (Exception e) {
													e.printStackTrace();
												}										
											}
										}).start();		
									}													
								}
							}else{
								theActivity.mTvReason.setText("已连接"+theActivity.foundDeviceNum+"个设备");
								theActivity.MainDeviceUuidBak = theActivity.MainDeviceUuid;
								theActivity.mBtnNext.setText(theActivity.getResources().getString(R.string.ok_name));
								theActivity.countTag=MainDeviceConnok;
								theActivity.foundDeviceNum++;
								new Thread(new Runnable() {
									
									@Override
									public void run() {
										Map<String, String> params = new HashMap<String, String>();
										params.put("userid", String.valueOf(MyApplication.getUserID()));
										params.put("uuid", theActivity.MainDeviceUuid);
										params.put("ak", Constant.REQUEST_AK);
										try {					
											String s = HttpUtils.Post(Constant.URL_INSERT_NEWMAINDEVICE, params);
											Log.e("DEVICESENSOR_ID", s);
											new Thread(theActivity.getNewMainDevice).start();
										} catch (Exception e) {
											e.printStackTrace();
										}	
									}
								}).start();
							}
							
							
						} catch (JSONException e) {
							Log.e("DEVICESENSOR_ID", e.getMessage());
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					break;	
				default:
					break;
				}
			}		
		}
	};
	
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}
}
