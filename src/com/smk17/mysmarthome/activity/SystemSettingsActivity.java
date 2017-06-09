package com.smk17.mysmarthome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.smk17.mysmarthome.BaseActivity;
import com.smk17.mysmarthome.R;
import com.smk17.mysmarthome.fragment.systemsettings.*;

public class SystemSettingsActivity extends BaseActivity{

	public static final int Page_About = 0xE0;	
	public static final int Page_AddInfraredDevice = 0xE1;		
	public static final int Page_AddMainDevice = 0xE2;
	public static final int Page_AddScene = 0xE3;	
	public static final int Page_AddTerminalDevice = 0xE4;	
	public static final int Page_CameraManagement = 0xE6;	
	public static final int Page_EditScene = 0xE7;	
	public static final int Page_InfraredDeviceManagement = 0xE8;	
	public static final int Page_LearnDevice = 0xE9;	
	public static final int Page_MainDeviceManagement = 0xE10;	
	public static final int Page_SceneManagement = 0xE11;	
	public static final int Page_TerminalDeviceManagement = 0xE12;	
	public static final int Page_ZoneManagement = 0xE13;
	
	private OnSystemListener systemListener = null;
	private int Page = 0;
	private ImageView mIvBack;
	private ImageView mIvMoreFunc;
	private FrameLayout mFrameLayout;
	private LinearLayout mLlContainer;
	private RelativeLayout mRlTitle;
	private View.OnClickListener mSaveClick = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			mIvBack.setImageResource(R.drawable.ic_arrow_left_white_24dp);
			mIvBack.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					finish();
				}
			});
			mIvMoreFunc.setVisibility(View.GONE);			
			transaction.replace(R.id.fragment_content, FragmentSceneManagement.newInstance());
			transaction.commit();			
			if(systemListener!=null)
				systemListener.onSystemAction();
		}
	};
	private View.OnClickListener mAddClick = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(systemListener!=null)
				systemListener.onSystemAction();
		}
	};
	private View.OnClickListener mBackClick = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {			
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			mIvBack.setImageResource(R.drawable.ic_arrow_left_white_24dp);
			mIvBack.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					finish();
				}
			});
			mIvMoreFunc.setVisibility(View.GONE);
			transaction.replace(R.id.fragment_content, FragmentSceneManagement.newInstance());
			transaction.commit();			
			finish();
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.material_activity_settings,R.layout.material_activity_settings,
				R.layout.material_activity_settings,R.layout.material_activity_settings);
		TextView mTvTitle = (TextView)findViewById(R.id.tv_title);
		mIvBack = (ImageView)findViewById(R.id.iv_back);
		mIvMoreFunc = (ImageView)findViewById(R.id.iv_morefunc);
		mFrameLayout = (FrameLayout)findViewById(R.id.fragment_content);
		mLlContainer = (LinearLayout)findViewById(R.id.content);
		mRlTitle = (RelativeLayout)findViewById(R.id.title);
		//Log.d("", "".substring(3) );
		Bundle extras = getIntent().getExtras();
		if(extras != null){
			Page = extras.getInt("Page");
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			switch (Page) {
			case Page_About:
				mTvTitle.setText(R.string.about);
				transaction.replace(R.id.fragment_content, FragmentAbout.newInstance());
				break;
			case Page_AddInfraredDevice:
				mTvTitle.setText(R.string.add_infrared_device);
				transaction.replace(R.id.fragment_content, FragmentAddInfraredDevice.newInstance());
				break;
			case Page_AddMainDevice:
				mTvTitle.setText(R.string.add_main_device);
				transaction.replace(R.id.fragment_content, FragmentAddMainDevice.newInstance());
				break;
			case Page_AddScene:
				mTvTitle.setText(R.string.add_scene_name);
				mIvBack.setImageResource(R.drawable.ic_window_close_white_24dp);
				mIvMoreFunc.setImageResource(R.drawable.ic_check_white_24dp);
				mIvMoreFunc.setVisibility(View.VISIBLE);
				mIvBack.setOnClickListener(mBackClick);
				mIvMoreFunc.setOnClickListener(mSaveClick);
				transaction.replace(R.id.fragment_content, FragmentAddScene.newInstance(extras.getInt("Id")));
				break;
			case Page_AddTerminalDevice:
				mTvTitle.setText(R.string.add_terminal_device);
				transaction.replace(R.id.fragment_content, FragmentAddTerminalDevice.newInstance());
				break;			
			case Page_CameraManagement:
				mTvTitle.setText(R.string.camera_management);
				transaction.replace(R.id.fragment_content, FragmentCameraManagement.newInstance());
				break;
			case Page_EditScene:
				mTvTitle.setText(R.string.edit_scene_name);
				mIvBack.setImageResource(R.drawable.ic_window_close_white_24dp);
				mIvMoreFunc.setImageResource(R.drawable.ic_check_white_24dp);
				mIvMoreFunc.setVisibility(View.VISIBLE);
				mIvBack.setOnClickListener(mBackClick);
				mIvMoreFunc.setOnClickListener(mSaveClick);
				transaction.replace(R.id.fragment_content, FragmentEditScene.newInstance(extras.getInt("Id")));
				break;
			case Page_InfraredDeviceManagement:
				mTvTitle.setText(R.string.infrared_device_management);
				transaction.replace(R.id.fragment_content, FragmentInfraredDeviceManagement.newInstance());
				break;
			case Page_LearnDevice:
				mTvTitle.setText(R.string.learn_device);
				transaction.replace(R.id.fragment_content, FragmentLearnDevice.newInstance());
				break;
			case Page_MainDeviceManagement:
				mTvTitle.setText(R.string.main_device_management);
				transaction.replace(R.id.fragment_content, FragmentMainDeviceManagement.newInstance());
				break;
			case Page_SceneManagement:
				mTvTitle.setText(R.string.scene_management);
				mIvMoreFunc.setVisibility(View.VISIBLE);
				mIvMoreFunc.setOnClickListener(mAddClick);
				transaction.replace(R.id.fragment_content, FragmentSceneManagement.newInstance());
				break;
			case Page_TerminalDeviceManagement:
				mTvTitle.setText(R.string.terminal_device_management);
				transaction.replace(R.id.fragment_content, FragmentTerminalDeviceManagement.newInstance());
				break;
			case Page_ZoneManagement:
				mTvTitle.setText(R.string.zone_management);
				mIvMoreFunc.setVisibility(View.VISIBLE);
				mIvMoreFunc.setOnClickListener(mAddClick);
				findViewById(R.id.iv_back).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Back();
					}
				});
				transaction.replace(R.id.fragment_content, FragmentZoneManagement.newInstance());
				break;

			default:
				break;
			}
			transaction.commit();
			setTheme();
		}
	}
	
	private void Back(){
		Intent intent = new Intent();
		intent.setClass(SystemSettingsActivity.this, MainActivity.class);
		intent.putExtra("status", 1);
		intent.putExtra("currentTabIndex", 6);
		startActivity(intent);
		finish();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK)  
        {
			if(Page == Page_ZoneManagement)
				Back();    		
			else
				finish();
        }
		return false;
	}
	
	@Override
    public void onAttachFragment(Fragment fragment) {
         try {
        	 systemListener = (OnSystemListener)fragment;
         } catch (Exception e) {
        	 e.printStackTrace();
         }
         super.onAttachFragment(fragment);
    }
	
	@Override
	protected void setTheme() {
		super.setTheme();
		setThemeType(themeId);
	}

	private void setThemeType(int id){
    	switch (id) {
		case 1:			
			mLlContainer.setBackgroundResource(R.drawable.theme_default_bg);
			mFrameLayout.setBackgroundResource(R.color.content_background);
			mRlTitle.setBackgroundResource(R.color.title_background);
			break;
		case 2:
			mLlContainer.setBackgroundResource(R.drawable.theme_nighttime);
			mFrameLayout.setBackgroundResource(android.R.color.transparent);
			mRlTitle.setBackgroundResource(android.R.color.transparent);
			break;
		case 3:
			mLlContainer.setBackgroundResource(R.drawable.theme_spring);
			mFrameLayout.setBackgroundResource(android.R.color.transparent);
			mRlTitle.setBackgroundResource(android.R.color.transparent);
			break;
		case 4:
			mLlContainer.setBackgroundResource(R.drawable.theme_summer);
			mFrameLayout.setBackgroundResource(android.R.color.transparent);
			mRlTitle.setBackgroundResource(android.R.color.transparent);
			break;
		case 5:
			mLlContainer.setBackgroundResource(R.drawable.theme_autumn);
			mFrameLayout.setBackgroundResource(android.R.color.transparent);
			mRlTitle.setBackgroundResource(android.R.color.transparent);
			break;
		case 6:
			mLlContainer.setBackgroundResource(R.drawable.theme_winter);
			mFrameLayout.setBackgroundResource(android.R.color.transparent);
			mRlTitle.setBackgroundResource(android.R.color.transparent);
			break;

		default:
			break;
		}
    }
	
	public interface OnSystemListener {
        public void onSystemAction();
    }
	
}
