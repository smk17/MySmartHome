package com.smk17.mysmarthome.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.smk17.mysmarthome.BaseActivity;
import com.smk17.mysmarthome.R;
import com.smk17.mysmarthome.fragment.usersettings.*;
import com.smk17.mysmarthome.fragment.usersettings.FragmentVerification.BtnClickListener;

public class UserSettingsActivity extends BaseActivity{
	
	public static final int Page_Rename = 0xE0;	
	public static final int Page_Binding = 0xE1;	
	public static final int Page_Login = 0xE2;	
	public static final int Page_Jurisdiction = 0xE3;	
	public static final int Page_UpdateUserPhone = 0xE4;	
	public static final int Page_UpdateUserEmail = 0xE5;	
	public static final int Page_AdvancedSettings = 0xE6;	
	public static final int Page_Theme = 0xE7;	

	private OnUserListener userListener = null;
	private int Page = 0;
//	private ImageView mIvBack;
	private ImageView mIvMoreFunc;
	private TextView mTvTitle;
	private FrameLayout mFrameLayout;
	private LinearLayout mLlContainer;
	private RelativeLayout mRlTitle;
	private View.OnClickListener mSaveClick = new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(userListener!=null)
				userListener.onUserAction();
		}
	};
	private BtnClickListener mBtnClickListener = new BtnClickListener() {
		
		@Override
		public void onClick(Integer position) {
			switch (position) {
			case 0:
				mTvTitle.setText(R.string.register_verify_code_title);
				break;
			case 1:
				mTvTitle.setText(R.string.rename_phone_title);
				break;
			case 2:
				mTvTitle.setText(R.string.rename_email_title);
				break;
			default:
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.material_activity_settings,R.layout.material_activity_settings,
				R.layout.material_activity_settings,R.layout.material_activity_settings);
		mTvTitle = (TextView)findViewById(R.id.tv_title);
		mIvMoreFunc = (ImageView)findViewById(R.id.iv_morefunc);
		mFrameLayout = (FrameLayout)findViewById(R.id.fragment_content);
		mLlContainer = (LinearLayout)findViewById(R.id.content);
		mRlTitle = (RelativeLayout)findViewById(R.id.title);
		Bundle extras = getIntent().getExtras();
		if(extras != null){
			Page = extras.getInt("Page");
			String phone = null;
			FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
			switch (Page) {
			case Page_Rename:
				mTvTitle.setText(R.string.user_rename);
//				mIvMoreFunc.setImageResource(R.drawable.ic_check_white_24dp);
//				mIvMoreFunc.setVisibility(View.VISIBLE);
//				mIvMoreFunc.setOnClickListener(mSaveClick);
				transaction.replace(R.id.fragment_content, FragmentRename.newInstance());
				break;
			case Page_Binding:
				mTvTitle.setText(R.string.user_binding);
				transaction.replace(R.id.fragment_content, FragmentBinding.newInstance());
				break;
			case Page_Login:
				mTvTitle.setText(R.string.user_login);
				transaction.replace(R.id.fragment_content, FragmentLogin.newInstance());
				break;
			case Page_Jurisdiction:
				mTvTitle.setText(R.string.user_jurisdiction);
				mIvMoreFunc.setVisibility(View.VISIBLE);
				mIvMoreFunc.setOnClickListener(mSaveClick);
				transaction.replace(R.id.fragment_content, FragmentJurisdiction.newInstance());
				break;
			case Page_UpdateUserPhone:
				phone = extras.getString("phone");
				mTvTitle.setText(R.string.user_authentication_title);
				transaction.replace(R.id.fragment_content, FragmentVerification.newInstance(FragmentVerification.InfoType_Phone, 
						phone, mBtnClickListener));
				break;
			case Page_UpdateUserEmail:
				phone = extras.getString("phone");
				mTvTitle.setText(R.string.user_authentication_title);
				transaction.replace(R.id.fragment_content, FragmentVerification.newInstance(FragmentVerification.InfoType_Phone, 
						phone, mBtnClickListener));
				break;
			case Page_AdvancedSettings:
				mTvTitle.setText(R.string.advanced_settings);
				transaction.replace(R.id.fragment_content, FragmentAdvancedSettings.newInstance());
				break;
			case Page_Theme:
				mTvTitle.setText(R.string.theme);
				transaction.replace(R.id.fragment_content, FragmentTheme.newInstance(new FragmentTheme.BtnClickListener() {
					
					@Override
					public void onBtnClick(Integer id) {
						setThemeType(id);
					}
				}));
				findViewById(R.id.iv_back).setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Back();
					}
				});
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
		intent.setClass(UserSettingsActivity.this, MainActivity.class);
		intent.putExtra("status", 1);
		intent.putExtra("currentTabIndex", 5);
		startActivity(intent);
		finish();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK)  
        {
			if(Page == Page_Theme)
				Back();    		
			else
				finish();
        }
		return false;
	}

	@Override
    public void onAttachFragment(Fragment fragment) {
         try {
        	 userListener = (OnUserListener)fragment;
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
		Log.e("setThemeType", "id="+id);
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
	
	public interface OnUserListener {
        public void onUserAction();
    }
}
