package com.smk17.mysmarthome.activity;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.smk17.android.tools.ToolAlert;
import com.smk17.android.tools.ToolSMS;
import com.smk17.android.tools.ToolString;
import com.smk17.mysmarthome.BaseActivity;
import com.smk17.mysmarthome.Constant;
import com.smk17.mysmarthome.MyApplication;
import com.smk17.mysmarthome.R;
import com.smk17.mysmarthome.Utils.BitmapUtil;
import com.smk17.mysmarthome.Utils.HttpUtils;
import com.smk17.mysmarthome.Utils.MD5Util;
import com.yh.materialdesign.dialog.MaterialDialog;
import com.yh.materialdesign.edittext.MaterialEditText;
import com.yh.materialdesign.views.ButtonFlat;

public class RegisterActivity extends BaseActivity {

	private String getPhoneNumber = "";
	private String password = "";
	private MaterialEditText etPhone ;
	protected MaterialEditText etPassword ;
	protected TextView tvRecall ;
	protected TextView tvTitle ;
	private TextView tvVerifyCode ;
	private LinearLayout mLlContainer;
	private static int Count = 1;
	private static int delay = 1 * 1000;  //1s
	private static int period = 1 * 1000;  //1s
	private static final int UPDATE_TEXTVIEW = 99;
	private static final int ENABLED_TEXTVIEW = 98;
	private static int VerifyCodeCount = 60;
	private ButtonFlat btnRegister;
	private String sendVerifyCode;
	private int verifyCodePosition,sendVerifyCodeLen,verifyCodeLen;
	protected static boolean isForgotPassword = false;
	private static boolean issys = false;
	private MyHandler handler = new MyHandler(this);
	private Timer mTimer = null;
	private TimerTask mTimerTask = null;
	private MaterialDialog alert = null;
	private OnClickListener mOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(alert !=null)
				alert.show();
		}
	};
	private Runnable RegisterRunnable = new Runnable() {
		
		@Override
		public void run() {
			Message msg = new Message();
			msg.what = Constant.STATUS_ID;
			Map<String, String> params = new HashMap<String, String>();
			params.put("phone", getPhoneNumber);
			params.put("password", password);
			params.put("ak", Constant.REQUEST_AK);
			try {
				msg.obj = HttpUtils.Post(Constant.URL_REGISTER_SERVER, params);
			} catch (Exception e) {
				e.printStackTrace();
			}
			handler.sendMessage(msg);
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.material_activity_register,R.layout.material_activity_register,
				R.layout.material_activity_register,R.layout.material_activity_register);
		mLlContainer = (LinearLayout)findViewById(R.id.content);
		tvTitle = (TextView)findViewById(R.id.register_title);
		etPhone = (MaterialEditText)findViewById(R.id.met_phone);
		etPassword = (MaterialEditText)findViewById(R.id.met_password);
		tvRecall = (TextView)findViewById(R.id.tv_register_recall);
		tvVerifyCode = (TextView)findViewById(R.id.tv_register_verify_code);
		btnRegister = (ButtonFlat)findViewById(R.id.btn_register);
		btnRegister.setBackgroundResource(R.drawable.btn_background);
		btnRegister.setRippleSpeed(50);
		setTheme();
		etPassword.setRightClickListener(new MaterialEditText.RightClickListener() {
			
			@Override
			public void onRightClick() {
				// TODO Auto-generated method stub
				if(issys){
					etPassword.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_PASSWORD);
				}else{
					etPassword.setInputType(InputType.TYPE_CLASS_TEXT |InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
				}
				issys = !issys;
			}
		});		
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.list_item2);
		arrayAdapter.add(getResources().getString(R.string.reget_verify_code));
		ListView listView = new ListView(this);
		@SuppressWarnings("deprecation")
		Drawable drawable = getResources().getDrawable(R.drawable.settings_selector); 
		listView.setSelector(drawable);
		listView.setDividerHeight(0);
		listView.setVerticalScrollBarEnabled(true);
		listView.setAdapter(arrayAdapter);
		alert = new MaterialDialog(this).setContentView(listView).setCancelable(true).setCanceledOnTouchOutside(true);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				ToolSMS.getVerificationCode(getPhoneNumber);
				tvVerifyCode.setTextColor(getResources().getColor(R.color.white));
				startTimer();
				alert.dismiss();
			}
		});
		Count = 1;
		isForgotPassword =false;
		String verifyCode = getResources().getString(R.string.verify_code);
		sendVerifyCode = getResources().getString(R.string.register_send_verify_code_prefix)+
				verifyCode+getResources().getString(R.string.register_send_verify_code_suffix);
		verifyCodePosition = getResources().getString(R.string.register_send_verify_code_prefix).length();
		verifyCodeLen = verifyCode.length();
		sendVerifyCodeLen = sendVerifyCode.length();
		btnRegister.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch (Count) {
				case 1:					
					getPhoneNumber = etPhone.getText().toString().trim();
					if(ToolString.isNoBlankAndNoNull(getPhoneNumber) &&  getPhoneNumber.length()==11 ){
						new Thread(){
							@Override
							public void run(){
								Message msg = new Message();
								msg.what = Constant.User_ID;
								Map<String, String> params = new HashMap<String, String>();
								params.put("userphone", getPhoneNumber);
								params.put("ak", Constant.REQUEST_AK);
								try {
									String return_msg = HttpUtils.Post(Constant.URL_EXISTS_USERPHONE, params);
									if(return_msg != null){
										Log.e("HttpUtils", return_msg );
									}
									JSONObject jsonObject = new JSONObject(return_msg);
									String userphone = jsonObject.getString("userphone");
									String status = jsonObject.getString("status");
									if (userphone.equals(getPhoneNumber)  ){
										msg.obj  = 0;
										if(status.equals("succeeded")){
											msg.obj = 1;
										}else if(status.equals("failed")){
											msg.obj  = 2;
										}
									}
								} catch (JSONException e) {
									msg.obj  = 5;
								} catch (Exception e) {
									e.printStackTrace();
									msg.obj  = 3;
								}
								handler.sendMessage(msg);
							}
						}.start();
					}else{
						ToolAlert.toast(getString(R.string.enter_the_phone_number));
					}										
					break;
				case 2:
					if(etPhone.getText().toString() != ""){
						ToolSMS.submitVerificationCode(getPhoneNumber,etPhone.getText().toString(),new ToolSMS.IValidateSMSCode() {
							
							@Override
							public void onSucced() {
//								new SnackBar(RegisterActivity.this, "验证成功").show();
								//释放监听器
								ToolSMS.release();
								if(isForgotPassword){
									tvRecall.setText(R.string.enter_the_password);
									etPhone.setVisibility(View.GONE);
									etPassword.setText("");
									etPassword.setVisibility(View.VISIBLE);
									btnRegister.setText(getResources().getString(R.string.ok_name));
									stopTimer();
									tvVerifyCode.setVisibility(View.GONE);
									tvVerifyCode.setOnClickListener(null);
									Count=3;
								}else{
									new Thread(RegisterRunnable).start();		
								}														
							}
							
							@Override
							public void onFailed(Throwable e) {
								ToolAlert.toast(getString(R.string.error_ecurity_code, e.getMessage()));
							}
						});
					}else{
						ToolAlert.toast(getString(R.string.enter_the_security_code));
					}
					break;
				case 3:
					password = MD5Util.MD5(etPassword.getText().toString().trim());
					new Thread(){
						@Override
						public void run(){
							if(ToolString.isNoBlankAndNoNull(password)){
								Message msg = new Message();
								msg.what = Constant.STATUS_ID;
								Map<String, String> params = new HashMap<String, String>();
								params.put("phone", getPhoneNumber);
								params.put("password", password);
								params.put("ak", Constant.REQUEST_AK);
								try {
									msg.obj = HttpUtils.Post(Constant.URL_RESET_PASSWORD, params);
								} catch (Exception e) {
									e.printStackTrace();
								}
								handler.sendMessage(msg);
							}							
						}
					}.start();
				default:
					break;
				}
				
			}
		});
	
	}
	
	@Override
	protected void setTheme() {
		switch (themeId) {
		case 1:
			mLlContainer.setBackgroundResource(R.color.title_background);
			break;
		case 2:
			mLlContainer.setBackgroundResource(R.drawable.theme_nighttime);
			break;
		case 3:
			mLlContainer.setBackgroundResource(R.drawable.theme_spring);
			break;
		case 4:
			mLlContainer.setBackgroundResource(R.drawable.theme_summer);
			break;
		case 5:
			mLlContainer.setBackgroundResource(R.drawable.theme_autumn);
			break;
		case 6:
			mLlContainer.setBackgroundResource(R.drawable.theme_winter);
			break;

		default:
			break;
		}
	}
	
	public void onClick(View v){
		switch (v.getId()) {
		case R.id.register_back:
			if(Count == 2){
				backtrack();
			}else{
				finish();
			}
			break;

		default:
			break;
		}
	}
	
	/**
	 * 启动Timer
	 */
	private void startTimer(){
		
		stopTimer();
		tvVerifyCode.setVisibility(View.VISIBLE);
		tvVerifyCode.setOnClickListener(null);
		if (mTimer == null) {
			mTimer = new Timer();
		}
		if (mTimerTask == null) {
			mTimerTask = new TimerTask() {
				@Override
				public void run() {
					if(VerifyCodeCount >0){
						Message message = Message.obtain(handler, UPDATE_TEXTVIEW);     
			            handler.sendMessage(message);   
			            VerifyCodeCount --;
					}else{
						Message message = Message.obtain(handler, ENABLED_TEXTVIEW);     
			            handler.sendMessage(message);
			            
					}
					
				}
			};
		}

		if(mTimer != null && mTimerTask != null )
			mTimer.schedule(mTimerTask, delay, period);
	}
	
	/**
	 * 停止Timer
	 */
	private void stopTimer(){
				
		if (mTimer != null) {
			mTimer.cancel();
			mTimer = null;
		}

		if (mTimerTask != null) {
			mTimerTask.cancel();
			mTimerTask = null;
		}	
		
		VerifyCodeCount = 60;			
		tvVerifyCode.setText(String.format(getResources().getString(R.string.register_rev_verify_code),60) );
		
	}
	
	private void initSDK(){
		//注册SMSDK，可放置Application
		ToolSMS.initSDK(Constant.APPKEY, Constant.APPSECRET);		
		password = MD5Util.MD5(etPassword.getText().toString().trim());
		try{
			String tmpPhoneNumber = "+86 " + getPhoneNumber.substring(0, 3) + " " + getPhoneNumber.substring(3, 7) +
					" " + getPhoneNumber.substring(7, 11) ;					
			Spannable WordtoSpan = new SpannableString(String.format("%s\n\n%s",sendVerifyCode, tmpPhoneNumber)); 							 
			WordtoSpan.setSpan(new AbsoluteSizeSpan( BitmapUtil.dip(RegisterActivity.this,14)), 0, sendVerifyCodeLen, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
			WordtoSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#a0ffffff")), 0, sendVerifyCodeLen, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
			WordtoSpan.setSpan(new AbsoluteSizeSpan(BitmapUtil.dip(RegisterActivity.this,26)), sendVerifyCodeLen+1, WordtoSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
			WordtoSpan.setSpan(new StyleSpan(Typeface.BOLD), sendVerifyCodeLen+1, WordtoSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);  
			WordtoSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#3FB838")), verifyCodePosition,verifyCodePosition+verifyCodeLen, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); 
			
			if(MyApplication.isNetworkReady()){
				ToolSMS.getVerificationCode(getPhoneNumber);
				tvRecall.setText(WordtoSpan);
				etPhone.setText("");
				etPassword.setVisibility(View.GONE);
				etPhone.setHint(R.string.register_verify_code);
				if(!isForgotPassword){
					btnRegister.setText(getResources().getString(R.string.ok_name));
				}								
				tvTitle.setText(R.string.register_verify_code_title);
				startTimer();
				tvVerifyCode.setOnClickListener(null);
				etPhone.setMaxCharacters(4);
				Count=2;
			}else{
				ToolAlert.toast(getString(R.string.open_network));
			}
		}catch(Exception e){
			e.printStackTrace();
		}		
	}
	
	protected void backtrack(){
		tvRecall.setText(R.string.register_recall);
		etPhone.setHint(R.string.register_phone);
		etPassword.setVisibility(View.VISIBLE);
		etPassword.setText("");
		etPhone.setText("");
		tvTitle.setText(R.string.register_name);
		etPhone.setMaxCharacters(11);
		btnRegister.setText(getResources().getString(R.string.next_name));
		stopTimer();
		tvVerifyCode.setVisibility(View.GONE);
		tvVerifyCode.setOnClickListener(null);
		//etPhone.setMaxCharacters(4);
		Count = 1;
	}
	
	private static class MyHandler extends Handler { 
		//TODO MyHandler类定义
		WeakReference<RegisterActivity> mActivity; 
				
		public MyHandler(RegisterActivity activity){
			mActivity = new WeakReference<RegisterActivity>(activity);
		}
		@Override
		public void handleMessage(Message msg) {			
			final RegisterActivity theActivity = mActivity.get();
			switch (msg.what) {
			case UPDATE_TEXTVIEW:
				theActivity.tvVerifyCode.setText(String.format(theActivity.getResources().getString(R.string.register_rev_verify_code),
						VerifyCodeCount) );
				break;
			case ENABLED_TEXTVIEW:
				theActivity.tvVerifyCode.setText(R.string.register_rev_no_verify_code);
				theActivity.tvVerifyCode.setTextColor(theActivity.getResources().getColor(R.color.register_style_colors));
				theActivity.tvVerifyCode.setOnClickListener(theActivity.mOnClickListener);
				break;
			case Constant.User_ID:
				int login = Integer.valueOf(msg.obj.toString()) ;
				switch (login) {
				case 1://存在
					if(isForgotPassword){//忘记密码
						theActivity.initSDK();
					}else{
						final MaterialDialog alert = new MaterialDialog(theActivity);
						alert.setTitle(R.string.prompt_name).setMessage(R.string.exist_phone).setCanceledOnTouchOutside(true)
								.setNegativeButton(R.string.no, new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										alert.dismiss();
									}
								}).setPositiveButton(R.string.yes, new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										Intent intent=new Intent();
										intent.putExtra("PhoneNumber", theActivity.getPhoneNumber);
										theActivity.setResult(Constant.LOGIN_SERVER_ID, intent);
										theActivity.finish();
										alert.dismiss();
									}
								}).show();
					}
					break;
				case 2://不存在
					if(isForgotPassword){//忘记密码
						final MaterialDialog alert = new MaterialDialog(theActivity);
						alert.setTitle(R.string.prompt_name).setMessage(R.string.not_exist_phone).setCanceledOnTouchOutside(true)
								.setNegativeButton(R.string.no, new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										alert.dismiss();
									}
								}).setPositiveButton(R.string.yes, new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										isForgotPassword = false;
										theActivity.initSDK();
										alert.dismiss();
									}
								}).show();
					}else{
						theActivity.initSDK();
					}
					break;
				case 3:
					ToolAlert.toast(theActivity.getString(R.string.conn_not_server));
					break;
				default:
					ToolAlert.toast(theActivity.getString(R.string.error));
					break;
				}				
				break;
			case  Constant.STATUS_ID:
				Log.e("STATUS_ID", msg.obj.toString());
				try {
					JSONObject jsonObject =new JSONObject(msg.obj.toString());
					String Status = jsonObject.getString("status");
					Intent intent=new Intent();
					intent.putExtra("PhoneNumber", theActivity.getPhoneNumber);
					theActivity.setResult(Constant.LOGIN_SERVER_ID, intent);
					theActivity.finish();
					if(Status.equals("succeeded")){						
						if(isForgotPassword){
							ToolAlert.toast(theActivity.getString(R.string.reset_password_ok));
						}else{
							ToolAlert.toast(theActivity.getString(R.string.register_ok));
						}
					}else{
						if(isForgotPassword){
							ToolAlert.toast(theActivity.getString(R.string.reset_password_ok));
						}else{
							ToolAlert.toast(theActivity.getString(R.string.register_ok));
						}
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				break;
			default:
				break;
			}
			
		}
	};	
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO 返回键响应 
    	if(keyCode == KeyEvent.KEYCODE_BACK)  
        {
    		if(Count == 2){
    			backtrack();
			}else{
				finish();
			}
        }
		return false;
	}

}
