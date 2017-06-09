package com.smk17.mysmarthome.fragment.usersettings;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.smk17.android.tools.ToolAlert;
import com.smk17.android.tools.ToolSMS;
import com.smk17.android.tools.ToolString;
import com.smk17.mysmarthome.Constant;
import com.smk17.mysmarthome.MyApplication;
import com.smk17.mysmarthome.R;
import com.smk17.mysmarthome.Utils.HttpUtils;
import com.smk17.mysmarthome.cloud.ExecToCloudSql;
import com.yh.materialdesign.dialog.MaterialDialog;
import com.yh.materialdesign.edittext.MaterialEditText;
import com.yh.materialdesign.views.ButtonFlat;

public class FragmentVerification extends UserFragment{
	public static final int InfoType_Phone = 0xB0;	
	public static final int InfoType_Email = 0xB1;	
	private static final int UPDATE_TEXTVIEW = 99;
	private static final int ENABLED_TEXTVIEW = 98;
	
	private TextView mTvVerificationRecall;
	private TextView mTvVerifyCode;
	private MaterialEditText mMetVerifyCode;
	private ButtonFlat mBtnNext;
	private String phone = null;
	private BtnClickListener mBtnClickListener;
	private Timer mTimer = null;
	private TimerTask mTimerTask = null;
	private static int Count = 1;
	private static int VerifyCodeCount = 60;
	private static int delay = 1 * 1000;  //1s
	private static int period = 1 * 1000;  //1s
	private int InfoType= InfoType_Phone;
	private MaterialDialog alert = null;
	private MyHandler handler = new MyHandler(this);
	private OnClickListener mOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(alert !=null)
				alert.show();
		}
	};
		
	public static final FragmentVerification newInstance(int infotype,String phone,BtnClickListener BtnClickListener){
		FragmentVerification fragment = new FragmentVerification(infotype,phone,BtnClickListener);
		return fragment;		
	}	
	
	private FragmentVerification(int infotype,String phone,BtnClickListener BtnClickListener){
		this.phone = phone;
		mBtnClickListener = BtnClickListener;
		InfoType = infotype;
	}
	
	public interface BtnClickListener  
    {
		void onClick(Integer position);
    }  
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.material_fragment_verification, container, false);  	
		mTvVerificationRecall = (TextView)view.findViewById(R.id.tv_verification_recall);
		mTvVerifyCode = (TextView)view.findViewById(R.id.tv_verify_code);
		mMetVerifyCode = (MaterialEditText)view.findViewById(R.id.met_verify_code);
		mBtnNext = (ButtonFlat)view.findViewById(R.id.btn_next);
		Count = 1;
		final String tPhone = phone.substring(0,3)+"******"+phone.substring(phone.length()-2);
		final String Title = getString(R.string.secure_authentication_title);
		if(ToolString.isNoBlankAndNoNull(phone)){
			
			String msg = getString(R.string.secure_authentication_msg);
			String prompt = getString(R.string.secure_authentication_tips);			
			Spannable WordtoSpan = new SpannableString(String.format("%s\n\n%s\n\n%s  %s",Title, msg, prompt, tPhone ));
			WordtoSpan.setSpan(new AbsoluteSizeSpan(22,true), 0, Title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			WordtoSpan.setSpan(new AbsoluteSizeSpan(16,true), Title.length() + 1,  Title.length() + msg.length() + 2, 
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			WordtoSpan.setSpan(new AbsoluteSizeSpan(14,true),  Title.length() + msg.length() + 3, 
					WordtoSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			WordtoSpan.setSpan(new ForegroundColorSpan(Color.RED), WordtoSpan.length() - tPhone.length(), 
					WordtoSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			WordtoSpan.setSpan(new StyleSpan(Typeface.BOLD), 0, Title.length()+msg.length()+2, 
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			mTvVerificationRecall.setText(WordtoSpan);
		}
		mBtnNext.setBackgroundResource(R.drawable.btn_background);
		mBtnNext.setRippleSpeed(50);
		mBtnNext.setText(getString(R.string.send_SMS));
		final String CodeMsg = String.format(getString(R.string.secure_authentication_code), tPhone);
		final int phoneIndex = Title.length() + CodeMsg.indexOf(tPhone) + 2;
		mBtnNext.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {				
				switch (Count) {
				case 1:						
					Spannable WordtoSpan = new SpannableString(String.format("%s\n\n%s",Title, CodeMsg));
					WordtoSpan.setSpan(new AbsoluteSizeSpan(22,true), 0, Title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					WordtoSpan.setSpan(new StyleSpan(Typeface.BOLD), 0, Title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					WordtoSpan.setSpan(new ForegroundColorSpan(Color.RED), 
							phoneIndex,phoneIndex + tPhone.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE); 
			
					//注册SMSDK，可放置Application
					ToolSMS.initSDK(Constant.APPKEY, Constant.APPSECRET);		
					if(MyApplication.isNetworkReady()){
						ToolSMS.getVerificationCode(phone);
						mTvVerificationRecall.setText(WordtoSpan);
						mMetVerifyCode.setVisibility(View.VISIBLE);
						mTvVerifyCode.setVisibility(View.VISIBLE);
						mBtnClickListener.onClick(0);
						mBtnNext.setText(getString(R.string.next_name));
						startTimer();
						mTvVerifyCode.setOnClickListener(null);
						Count = 2;
					}else{
						ToolAlert.toast(getString(R.string.open_network));
					}
					break;
				case 2:
					if(mMetVerifyCode.getText().toString() != ""){
						ToolSMS.submitVerificationCode(phone,mMetVerifyCode.getText().toString(),new ToolSMS.IValidateSMSCode() {
							
							@Override
							public void onSucced() {
//								new SnackBar(RegisterActivity.this, "验证成功").show();
								//释放监听器
								ToolSMS.release();
								stopTimer();
								new Thread(new Runnable() {
									
									@Override
									public void run() {
										Message msg = new Message();
										msg.what = Constant.User_ID;
										handler.sendMessage(msg);
									}
								}).start();															
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
					final String metName = mMetVerifyCode.getText().toString();
					if(ToolString.isNoBlankAndNoNull(metName)){
						new Thread(){
							@Override
							public void run(){
								Message msg = new Message();
								msg.what = InfoType;
								msg.obj  = 4;
								Map<String, String> params = new HashMap<String, String>();
								params.put("username", metName);
								params.put("password", "123456");
								params.put("ak", Constant.REQUEST_AK);
								String return_msg;
								try {
									return_msg = HttpUtils.Post(Constant.URL_LOGIN_SERVER, params);
									if(return_msg != null){
										Log.e("HttpUtils", return_msg );
									}
									JSONObject jsonObject = new JSONObject(return_msg);
									String name = jsonObject.getString("username");
									String status = jsonObject.getString("status");
									
									if (name.equals(metName)  ){
										msg.obj  = 0;
										if(status.equals("succeeded")){
											msg.obj = 1;
										}else if(status.equals("failed")){
											msg.obj  = 2;
										}else if(status.equals("nouser")){
											msg.obj  = 3;
										}
									}
								} catch (JSONException e) {
									msg.obj  = 5;
								} catch (Exception e) {
									e.printStackTrace();
									msg.obj  = 6;
								}
								handler.sendMessage(msg);		
							}
						}.start();
						
					}
					
					break;
				case 4:
					if(mMetVerifyCode.getText().toString() != ""){
						switch (InfoType) {
						case InfoType_Phone:	
							ToolSMS.submitVerificationCode(phone,mMetVerifyCode.getText().toString(),new ToolSMS.IValidateSMSCode() {
								
								@Override
								public void onSucced() {
									//释放监听器
									ToolSMS.release();
									stopTimer();
									ExecToCloudSql.updateUserInfo(handler, "phone", phone);
								}
								
								@Override
								public void onFailed(Throwable e) {
									ToolAlert.toast(getString(R.string.error_ecurity_code, e.getMessage()));									
								}
							});
							break;
						case InfoType_Email:
							break;
						default:
							break;
						}
					}else{
						ToolAlert.toast(getString(R.string.enter_the_security_code));
					}
					
					break;
					
				default:
					break;
				}
				
			}
		});
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item2);
		arrayAdapter.add(getResources().getString(R.string.reget_verify_code));
		ListView listView = new ListView(getActivity());
		@SuppressWarnings("deprecation")
		Drawable drawable = getResources().getDrawable(R.drawable.settings_selector); 
		listView.setSelector(drawable);
		listView.setDividerHeight(0);
		listView.setVerticalScrollBarEnabled(true);
		listView.setAdapter(arrayAdapter);
		alert = new MaterialDialog(getActivity()).setContentView(listView).setCancelable(true).setCanceledOnTouchOutside(true);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				ToolSMS.getVerificationCode(phone);
				mTvVerifyCode.setTextColor(getResources().getColor(R.color.black));
				startTimer();
				alert.dismiss();
			}
		});
		return view;
	}
	
	/**
	 * 启动Timer
	 */
	private void startTimer(){
		
		stopTimer();
		mTvVerifyCode.setVisibility(View.VISIBLE);
		mTvVerifyCode.setOnClickListener(null);
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
		Message message = Message.obtain(handler, ENABLED_TEXTVIEW);     
        handler.sendMessage(message);
		
		
	}
	
	private void codeOKNext(){
		Count=3;
		switch (InfoType) {
		case InfoType_Phone:
			mBtnClickListener.onClick(1);
			mTvVerificationRecall.setText(R.string.enter_rename_phone);
			mMetVerifyCode.setMaxCharacters(11);
			mMetVerifyCode.setText("");
			mMetVerifyCode.setHint(R.string.enter_rename_phone);
			mTvVerifyCode.setVisibility(View.GONE);			
			break;
		case InfoType_Email:
			mBtnClickListener.onClick(2);
			mTvVerificationRecall.setText(R.string.enter_rename_email);
			mMetVerifyCode.setMaxCharacters(25);
			mMetVerifyCode.setText("");
			mMetVerifyCode.setHint(R.string.enter_rename_email);
			mTvVerifyCode.setVisibility(View.GONE);			
			break;
		default:
			break;
		}
	}
	
	private void newPhoneNext(){
		ToolSMS.initSDK(Constant.APPKEY, Constant.APPSECRET);
		phone = mMetVerifyCode.getText().toString().trim();
		if(MyApplication.isNetworkReady()){
			ToolSMS.getVerificationCode(phone);
			String Title = getString(R.string.rename_phone_title);
			String MsgPrefix = getString(R.string.register_send_verify_code_prefix);
			String MsgSsuffix = getString(R.string.register_send_verify_code_suffix);
			String verifyCode = getString(R.string.verify_code);		
			String Msg = MsgPrefix+ verifyCode +MsgSsuffix+phone;
			Spannable WordtoSpan = new SpannableString(String.format("%s\n\n%s", Title, Msg));
			WordtoSpan.setSpan(new AbsoluteSizeSpan(22,true), 0, Title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			WordtoSpan.setSpan(new StyleSpan(Typeface.BOLD), 0, Title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			WordtoSpan.setSpan(new StyleSpan(Typeface.BOLD), Title.length()+MsgPrefix.length()+2, 
					Title.length()+MsgPrefix.length()+2+verifyCode.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			WordtoSpan.setSpan(new ForegroundColorSpan(Color.RED), WordtoSpan.length() - phone.length(), 
					WordtoSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			startTimer();
			mBtnClickListener.onClick(1);
			mTvVerificationRecall.setText(WordtoSpan);
			mMetVerifyCode.setMaxCharacters(4);
			mMetVerifyCode.setText("");
			mMetVerifyCode.setHint(R.string.enter_the_security_code);
			mTvVerifyCode.setVisibility(View.VISIBLE);	
			mBtnNext.setText(getString(R.string.ok_name));
			Count=4;
		}else{
			ToolAlert.toast(getString(R.string.open_network));
		}		
	}
	
	private void newEmailNext(){
		String Title = getString(R.string.rename_email_title);
		String MsgPrefix = getString(R.string.register_send_verify_code_prefix);
		String MsgSsuffix = getString(R.string.email_send_verify_code_suffix);
		String verifyCode = getString(R.string.verify_code);
		String tEmail = mMetVerifyCode.getText().toString();
		String Msg = MsgPrefix+ verifyCode +MsgSsuffix+tEmail;
		Spannable WordtoSpan = new SpannableString(String.format("%s\n\n%s", Title, Msg));
		WordtoSpan.setSpan(new AbsoluteSizeSpan(22,true), 0, Title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		WordtoSpan.setSpan(new StyleSpan(Typeface.BOLD), 0, Title.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		WordtoSpan.setSpan(new StyleSpan(Typeface.BOLD), Title.length()+MsgPrefix.length()+2, 
				Title.length()+MsgPrefix.length()+2+verifyCode.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		WordtoSpan.setSpan(new ForegroundColorSpan(Color.RED), WordtoSpan.length() - tEmail.length(), 
				WordtoSpan.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		startTimer();
		mBtnClickListener.onClick(2);
		mTvVerificationRecall.setText(WordtoSpan);
		mMetVerifyCode.setMaxCharacters(4);
		mMetVerifyCode.setText("");
		mMetVerifyCode.setHint(R.string.enter_the_security_code);
		mTvVerifyCode.setVisibility(View.VISIBLE);
		mBtnNext.setText(getString(R.string.ok_name));
		Count=4;
	}
	
	private static class MyHandler extends Handler { 
		//TODO MyHandler类定义
		WeakReference<FragmentVerification> mFragment; 
		
		public MyHandler(FragmentVerification fragment){
			mFragment = new WeakReference<FragmentVerification>(fragment);
		}
		
		@Override
		public void handleMessage(Message msg) {			
			final FragmentVerification theFragment = mFragment.get();			
			int login ;
			switch (msg.what) {
			case UPDATE_TEXTVIEW:
				if(theFragment.isAdded()){
					theFragment.mTvVerifyCode.setOnClickListener(null);
					theFragment.mTvVerifyCode.setText(String.format(theFragment.getResources().getString(R.string.register_rev_verify_code),
							VerifyCodeCount) );
				}				
				break;
			case ENABLED_TEXTVIEW:
				if(theFragment.isAdded()){
					theFragment.mTvVerifyCode.setText(R.string.register_rev_no_verify_code);
					theFragment.mTvVerifyCode.setTextColor(theFragment.getResources().getColor(R.color.register_style_colors));
					theFragment.mTvVerifyCode.setOnClickListener(theFragment.mOnClickListener);
				}				
				break;
			case Constant.User_ID:
				theFragment.codeOKNext();
				break;
			case InfoType_Phone:
				login = Integer.valueOf(msg.obj.toString()) ;
				if(login==1 || login==2){
					ToolAlert.toast(theFragment.getString(R.string.error_rename_phone));
				}else{
					theFragment.newPhoneNext();
				}
				break;
			case InfoType_Email:
				login = Integer.valueOf(msg.obj.toString()) ;
				if(login==1 || login==2){
					ToolAlert.toast(theFragment.getString(R.string.error_rename_email));
				}else{
					theFragment.newEmailNext();
				}				
				break;
			case Constant.STATUS_ID:
				ToolAlert.toast(msg.obj.toString());
				//theFragment.getActivity().finish();
				break;
			default:
				break;
			}
		}
	}
}
