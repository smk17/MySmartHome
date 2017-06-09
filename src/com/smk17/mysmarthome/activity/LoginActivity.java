package com.smk17.mysmarthome.activity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.j256.ormlite.dao.Dao;
import com.smk17.android.tools.ToolAlert;
import com.smk17.android.tools.ToolDatabase;
import com.smk17.android.tools.ToolString;
import com.smk17.mysmarthome.BaseActivity;
import com.smk17.mysmarthome.Constant;
import com.smk17.mysmarthome.MyApplication;
import com.smk17.mysmarthome.R;
import com.smk17.mysmarthome.Utils.HttpUtils;
import com.smk17.mysmarthome.Utils.MD5Util;
import com.smk17.mysmarthome.cloud.ExecToCloudSql;
import com.smk17.mysmarthome.cloud.ParseDevice;
import com.smk17.mysmarthome.domain.DeviceCategory;
import com.smk17.mysmarthome.domain.DeviceProperty;
import com.smk17.mysmarthome.domain.DeviceScene;
import com.smk17.mysmarthome.domain.DeviceSensor;
import com.smk17.mysmarthome.domain.DeviceZone;
import com.smk17.mysmarthome.domain.SceneProperty;
import com.yh.materialdesign.dialog.MaterialDialog;
import com.yh.materialdesign.edittext.MaterialAutoCompleteTextView;
import com.yh.materialdesign.edittext.MaterialEditText;
import com.yh.materialdesign.views.CheckBox;

public class LoginActivity extends BaseActivity{

	private String tmpPassword = "";
	private String getPassword = "";
	private CheckBox cbRecallPassword;
	private ViewFlipper allFlipper;
	private MaterialAutoCompleteTextView etUsername;
	private MaterialEditText etPassword ;
	private MaterialDialog mLoadDialog ;
	private MaterialDialog alert = new MaterialDialog(this);
	private MyHandler handler = new MyHandler(this);
	private Socket tcpCli = null;
	private static int count = 0;
	private static int oneRun = 0;

	private Runnable getMsgToHotSpotNet = new Runnable() {
    	@Override
    	public void run()
    	{
    		// TODO 子线程接收信息并发送到主线程   
    		Message msg = new Message();
    		Log.e("Tcp Demo", "count=" + count); 
    		try {    		
    			count = 0;
    			String data = "";//tcpService
        		byte[] rBuffer = new byte[1024];
        		
				tcpCli = new Socket(Constant.SERVER_IP, Constant.SERVER_PORT);
				InputStream inputStream = tcpCli.getInputStream();
        		DataInputStream input = new DataInputStream(inputStream);
				DataOutputStream dos=new DataOutputStream(tcpCli.getOutputStream());
//	    		rev_sb = new StringBuilder();
	    		dos.write(Constant.SERVER_CONN.getBytes(Constant.CharsetName));
	    		dos.flush();
	    		msg.what = Constant.ERROR_ID;
                msg.obj = data;
	    		while (count < 60){	 
	    			Log.e("Tcp Demo", "count=" + count); 
            		int length = input.read(rBuffer);
            		data = new String(rBuffer, 0, length, Constant.CharsetName);
            		Log.e("Tcp Demo", "message From Server:" + data);  
//            		dos.writeUTF(data);
            		if(ToolString.isNoBlankAndNoNull(data)){
            			msg.what = Constant.LOGIN_Local_ID;
                        msg.obj = data;
                        break;
//                        count = 60;
            		}        			
                    Thread.sleep(1000);
                    count++;
	    		}
	    		dos.write("close".getBytes(Constant.CharsetName));
	    		dos.close();
	    		tcpCli.close();
	    		
			} catch (Exception e) {
				Log.e("getMsgToHotSpotNet", e.toString());
//				e.printStackTrace();
				msg.what = Constant.ERROR_ID;
                msg.obj = e.toString();
			}	            
    		handler.sendMessage(msg);
    	}
	};
    
    private Runnable MainDeviceNumRowsRunnable = new Runnable() {
		
		@Override
		public void run() {
			Thread t =new Thread(){

				@Override
				public void run() {
					Message msg = new Message();	
					Map<String, String> params = new HashMap<String, String>();
					params.put("userid", String.valueOf(MyApplication.getUserID()));
					params.put("ak", Constant.REQUEST_AK);
					try {
						msg.what = Constant.MainDeviceNumRows_ID;
						msg.obj  = HttpUtils.Post(Constant.URL_GET_DEVICESENSOR, params);
						
					} catch (Exception e) {
						e.printStackTrace();
					}
					handler.sendMessage(msg);		
					super.run();
				}
				
			};
			t.start();
		}
	};
	
	private Runnable getMainDeviceRunnable = new Runnable() {
		
		@Override
		public void run() {
			Message msg = new Message();	
			Map<String, String> params = new HashMap<String, String>();
			params.put("userid", String.valueOf(MyApplication.getUserID()));
			params.put("ak", Constant.REQUEST_AK);
			try {
				msg.what = Constant.DEVICESENSOR_ID;
				msg.obj  = HttpUtils.Post(Constant.URL_GET_DEVICESENSOR, params);				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			handler.sendMessage(msg);
			
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		View view = View.inflate(this, R.layout.material_activity_login, null);	
//		AlphaAnimation animation = new AlphaAnimation(0.3f, 1.0f);
//		animation.setDuration(1500);
//		view.startAnimation(animation);     
		setContentView(R.layout.material_activity_login);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				if(oneRun == 0){
					Message msg = new Message();	
					msg.what = 1;
					msg.obj = 1;
					handler.sendMessage(msg); //给UI主线程发送消息
				}				
			}
        }, 1500); //启动等待3秒钟
		Log.d("LoginActivity", "oneRun="+oneRun);
		allFlipper = (ViewFlipper) findViewById(R.id.allFlipper);
		etUsername = (MaterialAutoCompleteTextView) findViewById(R.id.met_username);
		etPassword = (MaterialEditText) findViewById(R.id.met_password);
		cbRecallPassword = (CheckBox)findViewById(R.id.cb_recall_password);
		TextView tvRecallPassword = (TextView)findViewById(R.id.tv_recall_password);
		etUsername.setDropDownBackgroundResource(R.color.dark_gray_pressed);
		if(oneRun != 0)
			allFlipper.setDisplayedChild(1);
		setTheme();
		mLoadDialog = new MaterialDialog(LoginActivity.this).setShowProgress(true)
				.setCancelable(true)
				.setOnDismissListener(new OnDismissListener() {
					
					@Override
					public void onDismiss(DialogInterface dialog) {
						count = 60;						
					}
				});
		tvRecallPassword.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				cbRecallPassword.setChecked(!cbRecallPassword.isCheck());							
			}
		});		
		etUsername.addTextChangedListener(new TextWatcher() {
			SharedPreferences sp = getSharedPreferences(Constant.INFO_USERLOGIN ,Activity.MODE_PRIVATE);
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
				if(sp.getAll().size() > 0){
					String[] allUserName = new String[sp.getAll().size()];// sp.getAll().size()返回的是有多少个键值对
					allUserName = sp.getAll().keySet().toArray(new String[0]);
					ArrayAdapter<String> adapter = new ArrayAdapter<String>(
							LoginActivity.this,
	                        R.layout.list_item1,
	                        allUserName);
					etUsername.setAdapter(adapter);
				}
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {				
				getPassword = sp.getString(etUsername.getText().toString(), "");
				if(getPassword.equals("")){
					etPassword.setText(getPassword);
				}else{
					tmpPassword = String.valueOf((int)(10000000 + Math.random()*100000000)) ;
					etPassword.setText(tmpPassword);
					etPassword.setSelection(etPassword.getText().length());
//					Log.e("random", "tmppassword is " +tmpPassword );
				}
				
			}
		});
		etPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				/*判断是否是“DONE”键*/
				if(actionId == EditorInfo.IME_ACTION_DONE){
					/*隐藏软键盘*/
					InputMethodManager imm = (InputMethodManager) v
							.getContext().getSystemService(
									Context.INPUT_METHOD_SERVICE);
					if (imm.isActive()) {
						imm.hideSoftInputFromWindow(
								v.getApplicationWindowToken(), 0);
					}
					BtnLogin();
					
					return true;
				}
				return false;
			}
		});
		SharedPreferences sp = getSharedPreferences(Constant.INFO_USERLOGIN ,Activity.MODE_PRIVATE);
		if(sp.getAll().size() > 0){
			String[] allUserName = new String[sp.getAll().size()];// sp.getAll().size()返回的是有多少个键值对
			allUserName = sp.getAll().keySet().toArray(new String[0]);
			String logininfo = sp.getString(allUserName[sp.getAll().size()-1], null);
			etUsername.setText(allUserName[sp.getAll().size()-1]);
			etUsername.setSelection(etUsername.getText().length());			
			getPassword = logininfo;
		}
	}
		
	@Override
	protected void setTheme() {
		switch (themeId) {
		case 1:
			allFlipper.setBackgroundResource(R.color.title_background);
			break;
		case 2:
			allFlipper.setBackgroundResource(R.drawable.theme_nighttime);
			break;
		case 3:
			allFlipper.setBackgroundResource(R.drawable.theme_spring);
			break;
		case 4:
			allFlipper.setBackgroundResource(R.drawable.theme_summer);
			break;
		case 5:
			allFlipper.setBackgroundResource(R.drawable.theme_autumn);
			break;
		case 6:
			allFlipper.setBackgroundResource(R.drawable.theme_winter);
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onDestroy() {
		count = 60;			
		mLoadDialog = null;
		alert = null;
		super.onDestroy();
	}

	private void BtnLogin(){
		final String username = etUsername.getText().toString();
		final String password = etPassword.getText().toString();
		if(username.equals("")  || password.equals("")){
			ToolAlert.toast(getString(R.string.usernum_and_password_no_null));
		}else{
			final SharedPreferences sp = getSharedPreferences(Constant.INFO_USERLOGIN ,Activity.MODE_PRIVATE);
			final String logininfo = sp.getString(username, null);
			mLoadDialogShow(R.string.login_loading);
			if(cbRecallPassword.isCheck()){
				if(logininfo == null || !password.equals(tmpPassword) ){
					getPassword=MD5Util.MD5(password);
				}						
			}
			new Thread(){
				@Override
				public void run(){
					Message msg = new Message();
					msg.what = Constant.LOGIN_SERVER_ID;
					msg.obj  = 4;
					Map<String, String> params = new HashMap<String, String>();
					params.put("username", username);
					params.put("password", getPassword);
					params.put("ak", Constant.REQUEST_AK);
					String return_msg;
					try {
						return_msg = HttpUtils.Post(Constant.URL_LOGIN_SERVER, params);
						if(return_msg != null){
							Log.e("HttpUtils", return_msg );
						}
						JSONObject jsonObject = new JSONObject(return_msg);
						int userid = jsonObject.getInt("userid");
						String name = jsonObject.getString("username");
						String status = jsonObject.getString("status");
						MyApplication.setUserID(userid);
						
						if (name.equals(username)  ){
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
		}
	}
	
	private void  mLoadDialogShow(int s){
		if(mLoadDialog!=null&& !mLoadDialog.isShow())
			mLoadDialog.setTitle(s).show();				
	}
	
	private void  mLoadDialogDismiss(){
		if(mLoadDialog!=null&& mLoadDialog.isShow())
			mLoadDialog.dismiss();
	}
		
	public void onClick(View v){
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.btn_login:
			//登录按钮
			BtnLogin();
			break;
		case R.id.btn_local_login:
			//本地登录
			mLoadDialogShow(R.string.conn_loading);			
			new Thread(getMsgToHotSpotNet).start();
			break;
		case R.id.btn_register:
			//注册			
			intent.setClass(LoginActivity.this, RegisterActivity.class);
			startActivityForResult(intent,0);		
			break;	
		case R.id.btn_forgot_password:
			//忘记密码
			intent.setClass(LoginActivity.this, ForgotPasswordActivity.class);
			startActivityForResult(intent,0);
			break;

		default:
			break;
		}
	}
	
	
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Log.i("onActivityResult", "requestCode="+requestCode+"\n resultCode="+resultCode);
		switch (resultCode) {
		case Constant.LOGIN_SERVER_ID:
			if(data!=null){
				String getPhoneNumber = data.getStringExtra("PhoneNumber");
				if(getPhoneNumber != null){
					etUsername.setText(getPhoneNumber);
				}
			}			
			break;

		default:
			break;
		}
		
	}
	
	private static class MyHandler extends Handler { 
		//TODO MyHandler类定义
		private boolean isAdd = false;
		WeakReference<LoginActivity> mActivity; 
				
		public MyHandler(LoginActivity activity){
			mActivity = new WeakReference<LoginActivity>(activity);
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public void handleMessage(Message msg) {			
			final LoginActivity theActivity = mActivity.get();
			if(msg.obj!=null){
				switch (msg.what) {
				case Constant.LOGIN_Local_ID:
					try {
						JSONObject Object =new JSONObject(msg.obj.toString());
						String uid = Object.getString("deviceid");
						int MainDeviceId = 1;
						int MainDeviceNumRows = 1;
						
						ToolDatabase dbHelper = ToolDatabase.gainInstance(Constant.DB_NAME, Constant.DB_VERSION);
						
						File file = new File(theActivity.getDatabasePath(Constant.DB_NAME).getPath());
						boolean fileExists = !file.exists();
						if(fileExists){
							dbHelper.addEntity(DeviceCategory.class);
							dbHelper.addEntity(DeviceProperty.class);
							dbHelper.addEntity(DeviceScene.class);
							dbHelper.addEntity(DeviceSensor.class);
							dbHelper.addEntity(DeviceZone.class);
							dbHelper.addEntity(SceneProperty.class);
						}					
						
						Dao<DeviceCategory, String> CategoryDao = (Dao<DeviceCategory, String>)dbHelper.getDao(DeviceCategory.class);
						Dao<DeviceProperty, String> DevicePropertyDao = (Dao<DeviceProperty, String>)dbHelper.getDao(DeviceProperty.class);
						Dao<DeviceSensor, String> SensorDao = (Dao<DeviceSensor, String>)dbHelper.getDao(DeviceSensor.class);
						Dao<DeviceZone, String> ZoneDao = (Dao<DeviceZone, String>)dbHelper.getDao(DeviceZone.class);
						
//						AndroidDatabaseConnection conn = new AndroidDatabaseConnection(dbHelper.getWritableDatabase(), true);
//			            conn.setAutoCommit(true);
			            if(fileExists){
			            	CategoryDao.create(new DeviceCategory(null, theActivity.getString(R.string.type_master)));
				            CategoryDao.create(new DeviceCategory(null, theActivity.getString(R.string.type_strips)));
				            CategoryDao.create(new DeviceCategory(null, theActivity.getString(R.string.type_socket)));
				            CategoryDao.create(new DeviceCategory(null, theActivity.getString(R.string.type_dimmablelights)));
				            CategoryDao.create(new DeviceCategory(null, theActivity.getString(R.string.type_switch)));
				            
				            SensorDao.create(new DeviceSensor(null, 1, 1, 1, theActivity.getString(R.string.type_master), uid, 1, null));
				            
				            DevicePropertyDao.create(new DeviceProperty(null, "match", "0", "Integer", "", 1));
				            DevicePropertyDao.create(new DeviceProperty(null, "ssid", "0", "String", "", 1));
				            DevicePropertyDao.create(new DeviceProperty(null, "password", "0", "String", "", 1));
				            DevicePropertyDao.create(new DeviceProperty(null, "status", "0", "Boolean", "", 1));
				            
				            ZoneDao.create(new DeviceZone(null, 1, theActivity.getString(R.string.undefined)));
//				            conn.commit(null);
			            }else{
			            	SensorDao.queryRaw("delete from deviceSensor");
			            	DevicePropertyDao.queryRaw("delete from deviceProperty");
			            	ZoneDao.queryRaw("delete from deviceZone");
			            	
			            	SensorDao.create(new DeviceSensor(null, 1, 1, 1, theActivity.getString(R.string.type_master), uid, 1, null));
				            
				            DevicePropertyDao.create(new DeviceProperty(null, "match", "0", "Integer", "", 1));
				            DevicePropertyDao.create(new DeviceProperty(null, "ssid", "0", "String", "", 1));
				            DevicePropertyDao.create(new DeviceProperty(null, "password", "0", "String", "", 1));
				            DevicePropertyDao.create(new DeviceProperty(null, "status", "0", "Boolean", "", 1));
				            
				            ZoneDao.create(new DeviceZone(null, 1, theActivity.getString(R.string.undefined)));			            	
			            }
//			            List<DeviceSensor> list = SensorDao.queryBuilder().where().eq("categoryId", 1).query();
//			            MainDeviceNumRows = list.size();
			            
			            dbHelper.releaseAll();
			            theActivity.mLoadDialogDismiss();
			            MyApplication.setNetMode(Constant.HotSpot_Mode);//设置网络模式为热点模式
						MyApplication.setMainDeviceNumRows(MainDeviceNumRows);
						MyApplication.setMainDeviceId(MainDeviceId);
						MyApplication.setUserID(0);
						Intent intent = new Intent();
						intent.setClass(theActivity, MainActivity.class);
						theActivity.startActivity(intent);
						theActivity.finish();
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (JSONException e1) {
						new Thread(theActivity.getMsgToHotSpotNet).start();
						e1.printStackTrace();
					}
						
					break;
				case Constant.ERROR_ID:
					theActivity.mLoadDialogDismiss();
					if(theActivity.alert != null){
						theActivity.alert = new MaterialDialog(theActivity);
						theActivity.alert.setTitle(R.string.prompt_name)
										.setMessage(theActivity.getString(R.string.local_login_msg, Constant.WIFINamePrefix))
										.setCanceledOnTouchOutside(true)
										.setPositiveButton(R.string.relogin_name, new OnClickListener() {
											
											@Override
											public void onClick(View v) {
												theActivity.mLoadDialogShow(R.string.conn_loading);
												new Thread(theActivity.getMsgToHotSpotNet).start();
												theActivity.alert.dismiss();
											}
										}).setNegativeButton(R.string.cancel_name,new OnClickListener() {
											
											@Override
											public void onClick(View v) {
												theActivity.alert.dismiss();	
											}
										}).show();
					}					
					break;
				case Constant.LOGIN_SERVER_ID:
					int login = Integer.valueOf(msg.obj.toString()) ;
					final String username = theActivity.etUsername.getText().toString();
					final String password = theActivity.etPassword.getText().toString();
					theActivity.mLoadDialogDismiss();
					switch (login) {
					case 0:
						ToolAlert.toast(theActivity.getString(R.string.find_not_usernum));
						break;
					case 1:
						final SharedPreferences sp = theActivity.getSharedPreferences(Constant.INFO_USERLOGIN ,Activity.MODE_PRIVATE);
						final String logininfo = sp.getString(username, null);
						MyApplication.setNetMode(Constant.Network_Mode);//设置网络模式为外网模式
						
						if(theActivity.cbRecallPassword.isCheck()){
							if(logininfo == null || !password.equals(theActivity.tmpPassword) ){
								sp.edit().putString(username ,theActivity.getPassword).commit();
							}						
						}
						ExecToCloudSql.insertLoginRunnable(this, Build.MODEL);
						this.post(theActivity.MainDeviceNumRowsRunnable);			
						break;
					case 2:
						ToolAlert.toast(theActivity.getString(R.string.error_password));
						break;
					case 3:
						ToolAlert.toast(theActivity.getString(R.string.conn_not_server));
						break;
					default:
						ToolAlert.toast(theActivity.getString(R.string.error));
						break;
					}
					break;
				case Constant.DEVICESENSOR_ID:
					ArrayList<DeviceSensor> tmpList = null;
					try {
						tmpList = ParseDevice.parseMainDevice(msg.obj.toString());
						if(tmpList.size()  == 1){
							MyApplication.setMainDeviceId(tmpList.get(0).getId());
						}
						Intent intent = new Intent();
						intent.setClass(theActivity, MainActivity.class);
						theActivity.startActivity(intent);
						theActivity.finish();
					} catch (JSONException e) {
						e.printStackTrace();
					}
					
					break;
				case Constant.MainDeviceNumRows_ID:
					try {						
						int numRows = ParseDevice.parseMainDeviceNumRows(msg.obj.toString());
						Log.e("MainDeviceNumRows_ID", "numRows = "+ numRows);
						MyApplication.setMainDeviceNumRows(numRows);//设置用户关联的主控数目
						if(numRows > 0){
							new Thread(theActivity.getMainDeviceRunnable).start();						
						}else{
							theActivity.alert = new MaterialDialog(theActivity);
							theActivity.alert.setTitle(R.string.prompt_name).setMessage(R.string.addmaindevice_login_msg).setCanceledOnTouchOutside(true)
											.setPositiveButton(R.string.add_ok, new OnClickListener() {
												
												@Override
												public void onClick(View v) {
													isAdd = true;
													Intent intent = new Intent();
													intent.setClass(theActivity, SystemSettingsActivity.class);
													intent.putExtra("Page",SystemSettingsActivity.Page_AddMainDevice);
													theActivity.startActivity(intent);
//													theActivity.finish();
													theActivity.alert.dismiss();													
												}
											}).setNegativeButton(theActivity.getString(R.string.dialog_quit),Color.RED, new OnClickListener() {
												
												@Override
												public void onClick(View v) {
													theActivity.finish();
													System.exit(0);		
													theActivity.alert.dismiss();	
												}
											}).setOnDismissListener(new OnDismissListener() {
												
												@Override
												public void onDismiss(DialogInterface dialog) {
													if(!isAdd){
														theActivity.finish();
														System.exit(0);	
													}
												}
											}).show();
						}
						
					} catch (JSONException e) {
						e.printStackTrace();
					}
					break;
				case 1:
					theActivity.allFlipper.setInAnimation(AnimationUtils.loadAnimation(theActivity, R.anim.slide_up_in));
					theActivity.allFlipper.setOutAnimation(AnimationUtils.loadAnimation(theActivity, R.anim.slide_down_out));
					theActivity.allFlipper.setDisplayedChild(1);
					oneRun++;
					break;
				default:
					break;
				}
			}
		}
	};
}
