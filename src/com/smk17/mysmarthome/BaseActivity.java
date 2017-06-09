package com.smk17.mysmarthome;

import com.pgyersdk.activity.FeedbackActivity;
import com.pgyersdk.feedback.PgyFeedback;
import com.pgyersdk.feedback.PgyFeedbackShakeManager;
import com.pgyersdk.views.PgyerDialog;
import com.smk17.android.tools.ToolAlert;
import com.smk17.mysmarthome.activity.LoginActivity;
import com.smk17.mysmarthome.activity.SystemSettingsActivity;
import com.smk17.mysmarthome.activity.UserSettingsActivity;
import com.yh.materialdesign.dialog.MaterialDialog;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;

public class BaseActivity extends FragmentActivity {
//	private static final int NOTIFI_ID = 11;
    protected NotificationManager notificationManager;
    protected int themeId ;
    //protected MyApplication app = null;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //app =  (MyApplication) getApplication();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        //app.addActivity(this);
        
        SharedPreferences sp = getSharedPreferences(Constant.INFO_THEME ,Activity.MODE_PRIVATE);
		themeId = sp.getInt(Constant.THEME_ID, 1);
    }
    
    @Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		//app.removeActivity(this);
	}
    
    @Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO 返回键响应 
    	if(keyCode == KeyEvent.KEYCODE_BACK)  
        {
    		finish();
        }
		return false;
	}
    /**
     * 此函数需要重构它已适配当前页面并在onCreate中调用
     * @param id 已使用的主题id
     */
    protected void setTheme(){
    	SharedPreferences sp = getSharedPreferences(Constant.INFO_THEME ,Activity.MODE_PRIVATE);
		themeId = sp.getInt(Constant.THEME_ID, 1);
    	switch (themeId) {
		case 1:
			break;
		case 2:
			break;
		case 3:
			break;
		case 4:
			break;
		case 5:
			break;
		case 6:
			break;

		default:
			break;
		}
    }
    
    
    
    @Override
	protected void onSaveInstanceState(Bundle outState) {
		//super.onSaveInstanceState(outState);
	}

	/**
     * 根据当前设定的应用风格匹配对应的layout
     * @param layoutMaterialResID 	材料风格的layoutResID
     * @param layoutIOSResID			IOS风格的layoutResID
     * @param layoutMetroResID		Metro风格的layoutResID
     * @param layoutFactoryResID		工厂风格的layoutResID
     */
    protected void setContentView(int layoutMaterialResID,int layoutIOSResID,int layoutMetroResID,int layoutFactoryResID){
		switch (MyApplication.getAPPStyle()) {
		case Constant.APPStyle_Material:
			setContentView(layoutMaterialResID);
			break;
		case Constant.APPStyle_IOS:
			setContentView(layoutIOSResID);
			break;
		case Constant.APPStyle_Metro:
			setContentView(layoutMetroResID);
			break;
		case Constant.APPStyle_Factory:
			setContentView(layoutFactoryResID);
			break;			
		default:
			setContentView(layoutMaterialResID);
			break;
		}
    }
    
    protected boolean IsHotSpotMode(){
		if(MyApplication.getNetMode().equals(Constant.HotSpot_Mode)){
			ToolAlert.toastShort(getString(R.string.dialog_local_login_msg));
			return false;
		}
		return true;		
	}
    
    public void onClick(View v) {
    	final Intent intent = new Intent();
    	String msg = "";
    	switch (v.getId()) {
    	case R.id.iv_back:case R.id.btn_cancel_conn:
			finish();
			break;    	    	
		case R.id.user_binding:
			if(IsHotSpotMode()){
				intent.setClass(BaseActivity.this, UserSettingsActivity.class);
				intent.putExtra("Page",UserSettingsActivity.Page_Binding);
				startActivity(intent);
			}
			break;
		case R.id.user_login:
			if(IsHotSpotMode()){
				intent.setClass(BaseActivity.this, UserSettingsActivity.class);
				intent.putExtra("Page",UserSettingsActivity.Page_Login);
				startActivity(intent);
			}
			break;
		case R.id.user_jurisdiction:
			if(IsHotSpotMode()){
				intent.setClass(BaseActivity.this, UserSettingsActivity.class);
				intent.putExtra("Page",UserSettingsActivity.Page_Jurisdiction);
				startActivity(intent);
			}
			break;
		case	R.id.user_theme:
			intent.setClass(BaseActivity.this, UserSettingsActivity.class);
			intent.putExtra("Page",UserSettingsActivity.Page_Theme);
			startActivity(intent);
			finish();
			break;
		case	R.id.user_advanced_settings:
			intent.setClass(BaseActivity.this, UserSettingsActivity.class);
			intent.putExtra("Page",UserSettingsActivity.Page_AdvancedSettings);
			startActivity(intent);
			break;
		case R.id.user_quit:
			final MaterialDialog alert = new MaterialDialog(this);
			if(MyApplication.getNetMode().equals(Constant.HotSpot_Mode)){
				msg = getString(R.string.dialog_local_quit_msg);
			}else{
				msg = getString(R.string.dialog_server_quit_msg);
			}
			alert.setTitle(0).setMessage(msg).setCanceledOnTouchOutside(true)
							.setPositiveButton(R.string.dialog_quit, new OnClickListener() {
								
								@Override
								public void onClick(View v) {									
									intent.setClass(MyApplication.gainContext(), LoginActivity.class);
//									PendingIntent restartIntent = PendingIntent.getActivity(    
//						            		MyApplication.gainContext(), 0, intent,    
//						                    Intent.FLAG_ACTIVITY_NEW_TASK);                                                 
//						            //退出程序                                          
//						            AlarmManager mgr = (AlarmManager)MyApplication.gainContext().getSystemService(Context.ALARM_SERVICE);    
//						            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100,    
//						                    restartIntent); // 0.1秒钟后重启应用   
//									android.os.Process.killProcess(android.os.Process.myPid());
//									System.exit(0);
									startActivity(intent);
									alert.dismiss();	
									finish();									
								}
							}).setNegativeButton(R.string.cancel_name, new OnClickListener() {
								
								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									alert.dismiss();	
								}
							}).show();			
			break;

		case R.id.item_add_terminal_device:			
			intent.setClass(BaseActivity.this, SystemSettingsActivity.class);
			intent.putExtra("Page",SystemSettingsActivity.Page_AddTerminalDevice);
			startActivity(intent);
			break;
		case R.id.item_add_main_device:
			intent.setClass(BaseActivity.this, SystemSettingsActivity.class);
			intent.putExtra("Page",SystemSettingsActivity.Page_AddMainDevice);
			startActivity(intent);
			break;
		case R.id.item_learn_device:
			intent.setClass(BaseActivity.this, SystemSettingsActivity.class);
			intent.putExtra("Page",SystemSettingsActivity.Page_LearnDevice);
			startActivity(intent);
			break;
		case R.id.item_add_infrared_device:
			intent.setClass(BaseActivity.this, SystemSettingsActivity.class);
			intent.putExtra("Page",SystemSettingsActivity.Page_AddInfraredDevice);
			startActivity(intent);			
			break;
		case R.id.item_terminal_device_management:			
			intent.setClass(BaseActivity.this, SystemSettingsActivity.class);
			intent.putExtra("Page",SystemSettingsActivity.Page_TerminalDeviceManagement);
			startActivity(intent);
			break;
		case R.id.item_main_device_management:
			intent.setClass(BaseActivity.this, SystemSettingsActivity.class);
			intent.putExtra("Page",SystemSettingsActivity.Page_MainDeviceManagement);
			startActivity(intent);
			break;
		case R.id.item_zone_management:
			intent.setClass(BaseActivity.this, SystemSettingsActivity.class);
			intent.putExtra("Page",SystemSettingsActivity.Page_ZoneManagement);
			startActivity(intent);	
			finish();
			break;
		case R.id.item_scene_management:
			intent.setClass(BaseActivity.this, SystemSettingsActivity.class);
			intent.putExtra("Page",SystemSettingsActivity.Page_SceneManagement);
			startActivity(intent);
			break;
		case R.id.item_camera_management:
			intent.setClass(BaseActivity.this, SystemSettingsActivity.class);
			intent.putExtra("Page",SystemSettingsActivity.Page_CameraManagement);
			startActivity(intent);
			break;
		case R.id.item_infrared_device_management:
			intent.setClass(BaseActivity.this, SystemSettingsActivity.class);
			intent.putExtra("Page",SystemSettingsActivity.Page_InfraredDeviceManagement);
			startActivity(intent);
			break;		
		case R.id.item_about:
			intent.setClass(BaseActivity.this, SystemSettingsActivity.class);
			intent.putExtra("Page",SystemSettingsActivity.Page_About);
			startActivity(intent);
			break;
			
		case R.id.about_item_common_problem:
			break;
		case R.id.about_item_feedback:			
			PgyerDialog.setDialogTitleBackgroundColor("#ff008b8b");
			PgyerDialog.setDialogTitleTextColor("#ffffff");			
			PgyFeedback.getInstance().showDialog(this);
			break;
		case R.id.about_tv_provision:
			break;
		default:
			break;
		}
    	overridePendingTransition(R.anim.slide_up_in,R.anim.slide_down_out);
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        
        // onresume时，取消notification显示

        // 自定义摇一摇的灵敏度，默认为950，数值越小灵敏度越高。
        PgyFeedbackShakeManager.setShakingThreshold(1000);

        // 以Activity的形式打开，这种情况下必须在AndroidManifest.xml配置FeedbackActivity
        // 打开沉浸式,默认为false
        // FeedbackActivity.setBarImmersive(true);       
        
        // 设置顶部导航栏和底部bar的颜色
        FeedbackActivity.setBarBackgroundColor("#ff008b8b");
		// 设置顶部按钮和底部按钮按下时的反馈色
		FeedbackActivity.setBarButtonPressedColor("#008080");
		// 设置颜色选择器的背景色
		FeedbackActivity.setColorPickerBackgroundColor("#ff0000");
		
        PgyFeedbackShakeManager.register(this, false);
    }
    
    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
        PgyFeedbackShakeManager.unregister();
    }
}
