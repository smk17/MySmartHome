package com.smk17.mysmarthome.fragment;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import org.json.JSONException;

import com.bairuitech.anychat.AnyChatBaseEvent;
import com.bairuitech.anychat.AnyChatCoreSDK;
import com.bairuitech.anychat.AnyChatDefine;
import com.nineoldandroids.view.ViewHelper;
import com.smk17.mysmarthome.Constant;
import com.smk17.mysmarthome.MyApplication;
import com.smk17.mysmarthome.R;
import com.smk17.mysmarthome.activity.VideoActivity;
import com.smk17.mysmarthome.adapter.ZoneControlPagerAdapter;
import com.smk17.mysmarthome.cloud.ExecToCloudSql;
import com.smk17.mysmarthome.cloud.ParseDevice;
import com.smk17.mysmarthome.db.ExecSql;
import com.smk17.mysmarthome.domain.DeviceZone;
import com.yh.materialdesign.views.LayoutRipple;
import com.yh.materialwidgetlibrary.TabIndicator;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class FragmentZoneControl  extends MianFragment  implements AnyChatBaseEvent {
	
	private int userID = 0;
	private boolean monitorWitch = false;
	private boolean bOtherVideoOpened = false; // 对方视频是否已打开
	private ViewPager viewPager;
	private ZoneControlPagerAdapter adapter ;
	private TabIndicator indicator;
	private RelativeLayout deviceLoad;
	private RelativeLayout rl_monitor_list;
	private LinearLayout ll_monitor;
	private SurfaceView surface;
	private LinearLayout ll_zonecontrol;
	private LayoutRipple layoutRipple;
	private FZCBtnClickListener mFBtnClickListener = null;
	@SuppressLint("HandlerLeak")
	private Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			if(msg.obj !=null){
				switch (msg.what) {			
				case Constant.DEVICEZONE_ID:
					ArrayList<DeviceZone> tmpList = null;
					try {
						tmpList = ParseDevice.parseDeviceZone(msg.obj.toString());
//						Log.d("GetTerminalDeviceRunnable","OK");
						refresh(tmpList);
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case Constant.STATUS_ID:
					closeMonitor();
					break;

				default:
					break;
				}
			}			
			super.handleMessage(msg);
		}
	};
	
	private Boolean mFirstGetVideoBitrate = false; //"第一次"获得视频码率的标致
	private Boolean mFirstGetAudioBitrate = false; //"第一次"获得音频码率的标致
	
	private final int SHOWLOGINSTATEFLAG = 1; // 显示的按钮是登陆状态的标识
	private final int SHOWWAITINGSTATEFLAG = 2; // 显示的按钮是等待状态的标识
	private final int SHOWLOGOUTSTATEFLAG = 3; // 显示的按钮是登出状态的标识
	private final int LOCALVIDEOAUTOROTATION = 1; // 本地视频自动旋转控制
	private final int UPDATEVIDEOBITDELAYMILLIS = 200; //监听音频视频的码率的间隔刷新时间（毫秒）
	public AnyChatCoreSDK anyChatSDK;
	
	public static final FragmentZoneControl newInstance(FZCBtnClickListener fBtnClickListener){         
		FragmentZoneControl fragment = new FragmentZoneControl(fBtnClickListener);
		return fragment;  
	}  
	
	public FragmentZoneControl(){
		this.mFBtnClickListener=null;	
	}
	
	private FragmentZoneControl(FZCBtnClickListener fBtnClickListener){	
		this.mFBtnClickListener=fBtnClickListener;		
	}
		
	public interface FZCBtnClickListener  
    {  
        void onHideTitleBar();  
        void onShowTitleBar();  
    }
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.material_fragment_zonecontrol, container, false);  
//		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//			SystemBarTintManager tintManager = new SystemBarTintManager(getActivity()); 
//			SystemBarTintManager.SystemBarConfig config = tintManager.getConfig();  
//			view.setPadding(0,0, config.getPixelInsetRight(), config.getPixelInsetBottom()); 
//		} 
		deviceLoad = (RelativeLayout)view.findViewById(R.id.device_load);
		viewPager = (ViewPager) view.findViewById(R.id.fragment_zonecontrol_pager);
		indicator = (TabIndicator) view.findViewById(R.id.fragment_zonecontrol_indicator);
//		TestPagerAdapter adapter = new TestPagerAdapter(()view.getSupportFragmentManager());
//		list.add(new DeviceZone(1, "加载中"));		
		
		rl_monitor_list = (RelativeLayout)view.findViewById(R.id.rl_monitor_list);
		ll_monitor = (LinearLayout)view.findViewById(R.id.ll_monitor);
		surface = (SurfaceView)view.findViewById(R.id.surface);
		ll_zonecontrol = (LinearLayout)view.findViewById(R.id.ll_zonecontrol);
		layoutRipple = (LayoutRipple) view.findViewById(R.id.monitorButtons);
		ll_monitor.setVisibility(View.GONE);
		rl_monitor_list.setVisibility(View.GONE);
		setOriginRiple(layoutRipple);
		layoutRipple.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View arg0) {
				if(mFBtnClickListener !=null){
					mFBtnClickListener.onHideTitleBar();
				}				
				layoutRipple.setVisibility(View.GONE);
				ll_monitor.setVisibility(View.VISIBLE);
				InitSDK();
				//handler.postDelayed(runnable, UPDATEVIDEOBITDELAYMILLIS);
				anyChatSDK.Connect( "demo.anychat.cn", 8906);
				anyChatSDK.Login("admin", "");
				//anyChatSDK.EnterRoom(111, "");
				int[] userIDs = anyChatSDK.GetOnlineUser();
				for (int i = 0; i < userIDs.length; i++) {
					Log.e("layoutRipple", anyChatSDK.GetUserName(userIDs[i]));
					userID = userIDs[i];
					Intent intent = new Intent();
					intent.putExtra("UserID", userID);
					intent.setClass(getActivity(), VideoActivity.class);
					getActivity().startActivity(intent);
					break;
					//if(anyChatSDK.GetUserName(userIDs[i]).equals("demo")){
						// 如果是采用Java视频显示，则需要设置Surface的CallBack
//						if (AnyChatCoreSDK
//								.GetSDKOptionInt(AnyChatDefine.BRAC_SO_VIDEOSHOW_DRIVERCTRL) == AnyChatDefine.VIDEOSHOW_DRIVER_JAVA) {
//							int index = anyChatSDK.mVideoHelper.bindVideo(surface
//									.getHolder());
//							anyChatSDK.mVideoHelper.SetVideoUser(index, userID);
//						}
						
						
//						anyChatSDK.UserCameraControl(userIDs[i], 1); 
//						anyChatSDK.UserSpeakControl(userIDs[i], 1); 
//						userID = userIDs[i];
//						Intent intent = new Intent();
//						intent.putExtra("UserID", userID);
//						intent.setClass(getActivity(), VideoActivity.class);
//						getActivity().startActivity(intent);
						//判断远程用户视频是否已打开
//						if (!bOtherVideoOpened) {
//							if (anyChatSDK.GetCameraState(userID) == 2
//							        && anyChatSDK.GetUserVideoWidth(userID) != 0) {
//								SurfaceHolder holder = surface.getHolder();   //获得SurfaceView控件
//								holder.setFormat(PixelFormat.RGB_565);          //设置显示格式
//								holder.setFixedSize(anyChatSDK.GetUserVideoWidth(userID),
//										anyChatSDK.GetUserVideoHeight(userID));    //设置视频显示宽高
//								Surface s = holder.getSurface();                //获得视频画面
//								anyChatSDK.SetVideoPos(userID, s, 0, 0, 0, 0);     //调用API显示视频画面
//								bOtherVideoOpened = true;
//							}
//						}
					//}
				}
			}
		});
		ImageView closeMonitor = (ImageView) view.findViewById(R.id.iv_monitor_title_close);
		closeMonitor.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				closeMonitor();
				anyChatSDK.LeaveRoom(-1);
				anyChatSDK.Logout();
				anyChatSDK.Release();
			}
		});
		
		ImageView settingsMonitor = (ImageView) view.findViewById(R.id.iv_monitor_title_settings);
		settingsMonitor.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(monitorWitch){
					ll_zonecontrol.setVisibility(View.VISIBLE);
					rl_monitor_list.setVisibility(View.GONE);
					monitorWitch = false;
				}else{
					rl_monitor_list.setVisibility(View.VISIBLE);
					ll_zonecontrol.setVisibility(View.GONE);
					monitorWitch = true;
				}
			}
		});	

		switch (MyApplication.getNetMode()) {
		case Constant.Network_Mode:
			//TODO 外网模式下的初始化
			ExecToCloudSql.getZoneRunnable(handler);
			break;
		case Constant.Intranet_Mode:
			//TODO 内网模式下的初始化
			break;
		case Constant.HotSpot_Mode:
			//TODO 热点模式下的初始化
			refresh(ExecSql.getZone());
			break;
		default:
			break;
		}
	    return view;
	}
	
	Runnable runnable = new Runnable() {

		@Override
		public void run() {
			try {
				int videoBitrate = anyChatSDK.QueryUserStateInt(userID,
						AnyChatDefine.BRAC_USERSTATE_VIDEOBITRATE);
				int audioBitrate = anyChatSDK.QueryUserStateInt(userID,
						AnyChatDefine.BRAC_USERSTATE_AUDIOBITRATE);
				if (videoBitrate > 0)
				{
					//handler.removeCallbacks(runnable);
					mFirstGetVideoBitrate = true;
					surface.setBackgroundColor(Color.TRANSPARENT);
				}
				
				if(audioBitrate > 0){
					mFirstGetAudioBitrate = true;
				}
				
				if (mFirstGetVideoBitrate)
				{
					if (videoBitrate <= 0){						
						Toast.makeText(getActivity(), "对方视频中断了!", Toast.LENGTH_SHORT).show();
						// 重置下，如果对方退出了，有进去了的情况
						mFirstGetVideoBitrate = false;
					}
				}
				
				if (mFirstGetAudioBitrate){
					if (audioBitrate <= 0){
						Toast.makeText(getActivity(), "对方音频中断了!", Toast.LENGTH_SHORT).show();
						// 重置下，如果对方退出了，有进去了的情况
						mFirstGetAudioBitrate = false;
					}
				}

				handler.postDelayed(runnable, UPDATEVIDEOBITDELAYMILLIS);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	
	public void refresh(ArrayList<DeviceZone> list){
		if(viewPager != null || indicator!= null){
			adapter = new ZoneControlPagerAdapter(	getChildFragmentManager(),list);
			viewPager.setAdapter(adapter);
			viewPager.setCurrentItem(0);
			viewPager.setOffscreenPageLimit(4);
			indicator.setViewPager(viewPager);
			ll_zonecontrol.setVisibility(View.VISIBLE);
			deviceLoad.setVisibility(View.GONE);
			if(list.size() <=1)
				indicator.setVisibility(View.GONE);			
		}
	}
	
	private void setOriginRiple(final LayoutRipple layoutRipple){
    	
    	layoutRipple.post(new Runnable() {
			
			@Override
			public void run() {
				View v = layoutRipple.getChildAt(0);
		    	layoutRipple.setxRippleOrigin(ViewHelper.getX(v)+v.getWidth()/2);
		    	layoutRipple.setyRippleOrigin(ViewHelper.getY(v)+v.getHeight()/2);
		    	
		    	layoutRipple.setRippleColor(Color.parseColor("#008080"));
		    	
		    	layoutRipple.setRippleSpeed(50);
			}
		});
    	
    }
	private void closeMonitor(){
		if(mFBtnClickListener !=null){
			mFBtnClickListener.onShowTitleBar();
		}				
		ll_zonecontrol.setVisibility(View.VISIBLE);
		layoutRipple.setVisibility(View.VISIBLE);
		rl_monitor_list.setVisibility(View.GONE);
		ll_monitor.setVisibility(View.GONE);
	}
	
	private void InitSDK() {
		if (anyChatSDK == null) {
			anyChatSDK = AnyChatCoreSDK.getInstance(getActivity());
			anyChatSDK.SetBaseEvent(this);
			anyChatSDK.InitSDK(android.os.Build.VERSION.SDK_INT, 0);
			AnyChatCoreSDK.SetSDKOptionInt(
					AnyChatDefine.BRAC_SO_LOCALVIDEO_AUTOROTATION,
					LOCALVIDEOAUTOROTATION);
		}
	}
		
	@Override
	public void onMainAction() {
		Message msg = new Message();	
		msg.what = Constant.STATUS_ID;
		msg.obj = 1;
		handler.sendMessage(msg);				
	}

	@Override
	public void OnAnyChatConnectMessage(boolean bSuccess) {
		// TODO Auto-generated method stub
		
	}
	
	

	@Override
	public void OnAnyChatLoginMessage(int dwUserId, int dwErrorCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnAnyChatEnterRoomMessage(int dwRoomId, int dwErrorCode) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnAnyChatOnlineUserMessage(int dwUserNum, int dwRoomId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void OnAnyChatUserAtRoomMessage(int dwUserId, boolean bEnter) {
		if (!bEnter) {
			if (dwUserId == userID) {
				Toast.makeText(getActivity(), "对方已离开！", Toast.LENGTH_SHORT).show();
				userID = 0;
				anyChatSDK.UserCameraControl(dwUserId, 0);
				anyChatSDK.UserSpeakControl(dwUserId, 0);
				bOtherVideoOpened = false;
				
			}

		} else {
			if (userID != 0)
				return;

			int index = anyChatSDK.mVideoHelper.bindVideo(surface
					.getHolder());
			anyChatSDK.mVideoHelper.SetVideoUser(index, dwUserId);

			anyChatSDK.UserCameraControl(dwUserId, 1);
			anyChatSDK.UserSpeakControl(dwUserId, 1);
			userID = dwUserId;
		}		
	}

	@Override
	public void OnAnyChatLinkCloseMessage(int dwErrorCode) {
		// 网络连接断开之后，上层需要主动关闭已经打开的音视频设备
		if (bOtherVideoOpened) {
			anyChatSDK.UserCameraControl(userID, 0);
			anyChatSDK.UserSpeakControl(userID, 0);
			bOtherVideoOpened = false;
		}		
	}

}
