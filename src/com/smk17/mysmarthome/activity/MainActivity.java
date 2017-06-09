package com.smk17.mysmarthome.activity;

import static android.view.Gravity.START;

import java.lang.ref.WeakReference;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smk17.mysmarthome.BaseActivity;
import com.smk17.mysmarthome.Constant;
import com.smk17.mysmarthome.MyApplication;
import com.smk17.mysmarthome.R;
import com.smk17.mysmarthome.adapter.DrawerArrowDrawable;
import com.smk17.mysmarthome.adapter.MainMenuItemAdapter;
import com.smk17.mysmarthome.db.ExecSql;
import com.smk17.mysmarthome.db.HotSpotMode;
import com.smk17.mysmarthome.fragment.*;
import com.yh.materialdesign.dialog.MaterialDialog;

public class MainActivity extends BaseActivity{
	
	private OnMainListener mainListener = null;
	private MyHandler handler = new MyHandler(this);
	//[start]菜单全局常量和变量
	private DrawerArrowDrawable drawerArrowDrawable;
	private ListView menuList;
	private LinearLayout mLlMenu;
	private DrawerLayout drawer;
	private ImageView mBtnMenu;
	private ImageView mBtnIco;
	private TextView mTvTitle;
	private RelativeLayout mRlTitle;
	private LinearLayout mLlContainer;
	private FrameLayout mFrameLayout;
	private float offset;
	private boolean flipped = false;
	private static final String[] menuListArray = {"情景控制","主控列表","区域控制","报警控制","环境联动","账户管理","系统设置","刷新数据"};
	//[end]
	
	//[start]界面全局常量和变量
	// 当前fragment的index
	private int currentTabIndex = 1;
//    private boolean isTabIndex = true;	
    private Fragment[] fragments;	
    private FragmentSceneControl mSceneControl = null;//情景控制
    private FragmenttMainDeviceList mMainDeviceList = null;//主控列表
    private FragmentZoneControl mZoneControl = null;//区域控制
    private FragmentAlarmControl mAlarmControl = null;//报警控制
	private FragmentEnvironmentLinkage mEnvironmentLinkage = null;//环境联动
	private FragmentUserSettings mUserSettings =null;//账号设置
	private FragmentSystemSettings mSystemSettings = null;//系统设置
	//[end]
	private FragmentZoneControl.FZCBtnClickListener fzc =new FragmentZoneControl.FZCBtnClickListener() {
		
		@Override
		public void onShowTitleBar() {
			mRlTitle.setVisibility(View.VISIBLE);
		}
		
		@Override
		public void onHideTitleBar() {
			mRlTitle.setVisibility(View.GONE);	
		}
	};
	private FragmenttMainDeviceList.SelectMainDeviceListener selectMainDeviceListener = 
			new FragmenttMainDeviceList.SelectMainDeviceListener() {
		
		@Override
		public void onSelectMainDevice() {
			// TODO  选择主控
			if(mZoneControl !=null){
				mZoneControl = FragmentZoneControl.newInstance(fzc);
				removeAllFragment(fragments);
				
				drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
				mBtnIco.setVisibility(View.GONE);
				mBtnMenu.setVisibility(View.VISIBLE);
				currentTabIndex = 2;
				getSupportFragmentManager().beginTransaction()
									.add(R.id.fragment_content, fragments[currentTabIndex])
									.show(fragments[currentTabIndex]).commit();
				mTvTitle.setText(menuListArray[currentTabIndex]);
			}
		}
	};
	private MainMenuItemAdapter.FMenuItemClickListener fMenuItemClickListener = 
			new MainMenuItemAdapter.FMenuItemClickListener() {
		
		@Override
		public void onMenuItemClick(Integer position) {
			//TODO 菜单点击事件
			if(!menuListArray[position].equals("刷新数据"))		mTvTitle.setText(menuListArray[position]);
			else		mTvTitle.setText(menuListArray[currentTabIndex]);
	        if(position == 7){//刷新数据操作
	        	mTvTitle.setText(menuListArray[currentTabIndex]);
	        	final MaterialDialog mLoadDialog = new MaterialDialog(MainActivity.this);
	        	mLoadDialog.setTitle("正在同步数据...").setShowProgress(true);
	        	mLoadDialog.show();
	        	Runnable runnable = new Runnable(){ 
	    			
	    			@Override 
	    			public void run() { 
//	    				refresh();
	    				mLoadDialog.dismiss();
	    				} 
	    			}; 
	    		new Handler().postDelayed(runnable, 2000);
	        }else if(currentTabIndex != 7){		        	
	        	if(currentTabIndex != position){
//	        		if(position == 2){
//	        			mZoneControl = new FragmentZoneControl(fzc);
//	        		}
	        		switchFragment(fragments[currentTabIndex], fragments[position]);	
	        	}
	        	currentTabIndex = position;
	        	Log.d("onMenuItemClick", ""+currentTabIndex);
//	        	isTabIndex = true;
	        }       
			drawer.closeDrawer(START);		
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.material_activity_main,R.layout.material_activity_main,
				R.layout.material_activity_main,R.layout.material_activity_main);
		mLlContainer = (LinearLayout)findViewById(R.id.content);
		mFrameLayout = (FrameLayout)findViewById(R.id.fragment_content);
		mRlTitle = (RelativeLayout) findViewById(R.id.rl_drawer_title);
		mTvTitle = (TextView) findViewById(R.id.drawer_title);
		drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
		mBtnIco = (ImageView) findViewById(R.id.drawer_ico);
		mBtnMenu = (ImageView) findViewById(R.id.drawer_indicator);
		menuList = (ListView)findViewById(R.id.menu_list);
		mLlMenu = (LinearLayout)findViewById(R.id.ll_menu);
		int status = 0;
		Bundle extras = getIntent().getExtras();
		if(extras != null){
			status = extras.getInt("status");
			if(status == 0){
				MyApplication.setMainDeviceId(extras.getInt("MainDeviceId"));
				MyApplication.setMainDeviceNumRows(extras.getInt("MainDeviceNumRows"));
				MyApplication.setNetMode(extras.getInt("NetMode"));
				MyApplication.setUserID(extras.getInt("UserID"));
			}else{
				currentTabIndex = extras.getInt("currentTabIndex");
				drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
				mBtnIco.setVisibility(View.GONE);
				mBtnMenu.setVisibility(View.VISIBLE);
			}
		}
		
		// TODO 菜单初始化
		final Resources resources = getResources();				
		drawerArrowDrawable = new DrawerArrowDrawable(resources);
		drawerArrowDrawable.setStrokeColor(resources	.getColor(R.color.light_gray));
		mBtnMenu.setImageDrawable(drawerArrowDrawable);
		drawer.setDrawerListener(new DrawerLayout.SimpleDrawerListener() {
			@Override
			public void onDrawerSlide(View drawerView, float slideOffset) {
				offset = slideOffset;

				// Sometimes slideOffset ends up so close to but not quite 1 or 0.
				if (slideOffset >= .995) {
					mTvTitle.setText(R.string.app_name);
					flipped = true;
					drawerArrowDrawable.setFlip(flipped);
				} else if (slideOffset <= .005) {
					mTvTitle.setText(menuListArray[currentTabIndex]);
					flipped = false;
					drawerArrowDrawable.setFlip(flipped);
				}

				drawerArrowDrawable.setParameter(offset);
			}
		});
		mBtnMenu.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (drawer.isDrawerVisible(START)) {
					mTvTitle.setText(menuListArray[currentTabIndex]);
					drawer.closeDrawer(START);			
					flipped = false;
				} else {
					drawer.openDrawer(START);
					mTvTitle.setText(R.string.app_name);
					flipped = true;
				}
			}
		});
		MainMenuItemAdapter adapter = new MainMenuItemAdapter(menuListArray, this,fMenuItemClickListener);		
		menuList.setAdapter(adapter);			
		
		// TODO 界面初始化
		if(savedInstanceState == null)  {
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			mSceneControl = new FragmentSceneControl();
			mMainDeviceList = FragmenttMainDeviceList.newInstance(selectMainDeviceListener);
			mZoneControl = FragmentZoneControl.newInstance(fzc);
			mAlarmControl = new FragmentAlarmControl();
			mEnvironmentLinkage = new FragmentEnvironmentLinkage();
			mUserSettings = new FragmentUserSettings();
			mSystemSettings = new FragmentSystemSettings();
			
			fragments = new Fragment[] { mSceneControl, mMainDeviceList , mZoneControl, mAlarmControl,
	        		mEnvironmentLinkage, mUserSettings, mSystemSettings };
			if(MyApplication.getMainDeviceNumRows() == 1){
				currentTabIndex = 2;
				drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
				mBtnIco.setVisibility(View.GONE);
				mBtnMenu.setVisibility(View.VISIBLE);
			}else if(MyApplication.getMainDeviceNumRows() > 1 && status == 0){
				mBtnMenu.setVisibility(View.GONE);
				drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
			}			
			transaction.add(R.id.fragment_content, fragments[currentTabIndex])
													.show(fragments[currentTabIndex]).commit();
			setTheme();
			switch (MyApplication.getNetMode()) {
			case Constant.Network_Mode:
				//TODO 外网模式下的初始化
				
				break;
			case Constant.Intranet_Mode:
				//TODO 内网模式下的初始化
				break;
			case Constant.HotSpot_Mode:
				//TODO 热点模式下的初始化
				HotSpotMode.RevTcpCli();				
				break;
			default:
				break;
			}
		}
	}
	
	
	@SuppressWarnings("deprecation")
	@Override
	protected void setTheme() {
		super.setTheme();
		Drawable  drawable = null; 
		int alpha = 200;//0~255透明度值
		Log.e("setThemeType", "themeId="+themeId);
		switch (themeId) {
		case 1:
			mRlTitle.setBackgroundResource(R.color.title_background);
			mLlContainer.setBackgroundResource(R.drawable.theme_default_bg);
			mFrameLayout.setBackgroundResource(R.color.white);
			mLlMenu.setBackgroundResource(R.color.menu_background);
			break;
		case 2:
			mLlContainer.setBackgroundResource(R.drawable.theme_nighttime);
			mFrameLayout.setBackgroundResource(android.R.color.transparent);
			mRlTitle.setBackgroundResource(android.R.color.transparent);
			drawable = getResources().getDrawable(R.drawable.theme_nighttime);
			drawable.setAlpha(alpha);
			mLlMenu.setBackgroundDrawable(drawable);
			break;
		case 3:
			mLlContainer.setBackgroundResource(R.drawable.theme_spring);
			mFrameLayout.setBackgroundResource(android.R.color.transparent);
			mRlTitle.setBackgroundResource(android.R.color.transparent);
			drawable = getResources().getDrawable(R.drawable.theme_spring);
			drawable.setAlpha(alpha);
			mLlMenu.setBackgroundDrawable(drawable);
		case 4:
			mLlContainer.setBackgroundResource(R.drawable.theme_summer);
			mFrameLayout.setBackgroundResource(android.R.color.transparent);
			mRlTitle.setBackgroundResource(android.R.color.transparent);
			drawable = getResources().getDrawable(R.drawable.theme_summer);
			drawable.setAlpha(alpha);
			mLlMenu.setBackgroundDrawable(drawable);
			break;
		case 5:
			mLlContainer.setBackgroundResource(R.drawable.theme_autumn);
			mFrameLayout.setBackgroundResource(android.R.color.transparent);
			mRlTitle.setBackgroundResource(android.R.color.transparent);
			drawable = getResources().getDrawable(R.drawable.theme_autumn);
			drawable.setAlpha(alpha);
			mLlMenu.setBackgroundDrawable(drawable);
			break;
		case 6:
			mLlContainer.setBackgroundResource(R.drawable.theme_winter);
			mFrameLayout.setBackgroundResource(android.R.color.transparent);
			mRlTitle.setBackgroundResource(android.R.color.transparent);
			drawable = getResources().getDrawable(R.drawable.theme_winter);
			drawable.setAlpha(alpha);
			mLlMenu.setBackgroundDrawable(drawable);
			break;

		default:
			break;
		}
	}


	@Override
	public void onClick(View v) {
		super.onClick(v);
		switch (v.getId()) {
		case R.id.user_rename:
			if(IsHotSpotMode()){
				if(mainListener!=null)
					mainListener.onMainAction();
			}
			break;
		}
	}


	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i("onActivityResult", "requestCode="+requestCode+"\n resultCode="+resultCode);
		
		switch (requestCode) {
		case Constant.DEVICEZONE_ID:
			if(mZoneControl !=null){
				HotSpotMode.CloseRevTcpCli();				
				Message msg = new Message();
				msg.what = Constant.DEVICEZONE_ID;
				msg.obj = 1;
				handler.sendMessage(msg);
			}
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
    public void onAttachFragment(Fragment fragment) {
         try {
              mainListener = (OnMainListener)fragment;
         } catch (Exception e) {
        	 e.printStackTrace();
//              throw new ClassCastException(this.toString() + " must implementOnMainListener");
         }
         super.onAttachFragment(fragment);
    }
	
	@Override
	public void onDestroy() {
		HotSpotMode.CloseRevTcpCli();
		super.onDestroy();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO 返回键响应 
    	if(keyCode == KeyEvent.KEYCODE_BACK)  
        {
    		if(MyApplication.getMainDeviceNumRows() == 1){
    			if(flipped){
	    			mTvTitle.setText(menuListArray[currentTabIndex]);
					drawer.closeDrawer(START);			
					flipped = false;
	    		}else{
	    			if(currentTabIndex == 2){
	    				if(mRlTitle.getVisibility()==View.GONE){
//	    					mZoneControl.closeMonitor();
	    					if(mainListener!=null)
	    						mainListener.onMainAction();
	    				}else{
	    					moveTaskToBack(true);
	    				}
	    			}else{
	    				currentTabIndex = 2;
	    				switchFragment(fragments[currentTabIndex], fragments[2]);
	    				mTvTitle.setText(menuListArray[currentTabIndex]);
	    			}
	    		}
			}else{
				if(currentTabIndex == 1 && !flipped){
					moveTaskToBack(true);
				}else{
					if(flipped){
		    			mTvTitle.setText(menuListArray[currentTabIndex]);
						drawer.closeDrawer(START);			
						flipped = false;
		    		}else{
		    			if(currentTabIndex == 2){
		    				if(mRlTitle.getVisibility()==View.GONE){
//		    					mZoneControl.closeMonitor();
		    					if(mainListener!=null)
		    						mainListener.onMainAction();
		    				}else{
		    					switchFragment(fragments[currentTabIndex], fragments[1]);	
		    					currentTabIndex = 1;
		    					mTvTitle.setText(menuListArray[currentTabIndex]);
		    				}
		    			}else{		    				
		    				switchFragment(fragments[currentTabIndex], fragments[2]);
		    				currentTabIndex = 2;
		    				mTvTitle.setText(menuListArray[currentTabIndex]);
		    			}
		    		}
				}
			}    		
        }  
    	return false;  
		//return super.onKeyDown(keyCode, event);
	}
	
	/**
	   * 切换页面的重载，优化了fragment的切换
	   * 
	   * @param f
	   * @param descString
	   */
	public void switchFragment(Fragment from, Fragment to) {
	    if (from == null || to == null)
	      return;
	    FragmentTransaction transaction = getSupportFragmentManager()
	        .beginTransaction().setCustomAnimations(R.anim.slide_up_in,R.anim.slide_down_out);		
	    if (!to.isAdded()) {
	        // 隐藏当前的fragment，add下一个到Activity中
	        transaction.hide(from).add(R.id.fragment_content, to).commit();
	      } else {
	        // 隐藏当前的fragment，显示下一个
	        transaction.hide(from).show(to).commit();
	      }
	  }
	
	private void removeAllFragment(Fragment from[]){
		if(from == null || from.length == 0)
			return;
		FragmentTransaction transaction = getSupportFragmentManager()
		        .beginTransaction().setCustomAnimations(R.anim.slide_up_in,R.anim.slide_down_out);				
		for(int i = 0; i< from.length;i++){
			transaction.remove(from[i]);
		}
		transaction.commit();
	}

	private static class MyHandler extends Handler { 
		//TODO MyHandler类定义
		WeakReference<MainActivity> mActivity;
		
		public MyHandler(MainActivity Activity){
			mActivity = new WeakReference<MainActivity>(Activity);
		}
				
		@Override
		public void handleMessage(Message msg) {			
			final MainActivity theActivity = mActivity.get();
				switch (msg.what) {
				case Constant.REVToHotSpotNet_ID:
					try {
						ExecSql.getDevice(msg.obj.toString());
					} catch (Exception e) {
						e.printStackTrace();
//						refresh(new ArrayList<DeviceSensor>());
					}
//					theActivity.mainListener.onMainAction(OnMainListener.STATUS_HOTSPOT_MSG,msg.obj.toString());	
					break;
				case Constant.DEVICEZONE_ID:
					theActivity.mZoneControl = FragmentZoneControl.newInstance(theActivity.fzc);
					theActivity.drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
					theActivity.mBtnIco.setVisibility(View.GONE);
					theActivity.mBtnMenu.setVisibility(View.VISIBLE);
					theActivity.currentTabIndex = 6;
					theActivity.removeAllFragment(theActivity.fragments);
					theActivity.getSupportFragmentManager().beginTransaction()
										.add(R.id.fragment_content, theActivity.fragments[theActivity.currentTabIndex])
										.show(theActivity.fragments[theActivity.currentTabIndex]).commit();
					theActivity.mTvTitle.setText(menuListArray[theActivity.currentTabIndex]);
					HotSpotMode.RevTcpCli();
					break;

				default:
					break;
				}
				
		}
	};
	
	public interface OnMainListener {
        public void onMainAction();
    }
}
