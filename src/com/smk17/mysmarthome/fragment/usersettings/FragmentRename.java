package com.smk17.mysmarthome.fragment.usersettings;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONException;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.smk17.android.tools.ToolAlert;
import com.smk17.android.tools.ToolOssService;
import com.smk17.android.tools.ToolString;
import com.smk17.mysmarthome.Constant;
import com.smk17.mysmarthome.MyApplication;
import com.smk17.mysmarthome.R;
import com.smk17.mysmarthome.activity.UserSettingsActivity;
import com.smk17.mysmarthome.cloud.ExecToCloudSql;
import com.smk17.mysmarthome.cloud.ParseDevice;
import com.smk17.mysmarthome.domain.UserInfo;
import com.yh.materialdesign.dialog.MaterialDialog;
import com.yh.materialdesign.edittext.MaterialEditText;
import com.yh.materialdesign.views.LayoutDropdownButton;

public class FragmentRename extends UserFragment{
	private LayoutDropdownButton mBtnAvatar;
	private LayoutDropdownButton mBtnName;
	private LayoutDropdownButton mBtnPhone;
	private LayoutDropdownButton mBtnEmail;
	private MaterialEditText metDeviveName;
	private File tempFile;
	private String Phone = null;
	private String tempFileName;
	private MyHandler handler = new MyHandler(this);
	
	public static final FragmentRename newInstance(){
		FragmentRename fragment = new FragmentRename();
		return fragment;		
	}	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.material_fragment_rename, container, false);  	
		mBtnAvatar = (LayoutDropdownButton)view.findViewById(R.id.rename_avatar);
		mBtnName = (LayoutDropdownButton)view.findViewById(R.id.rename_name);
		mBtnPhone = (LayoutDropdownButton)view.findViewById(R.id.rename_phone);
		mBtnEmail = (LayoutDropdownButton)view.findViewById(R.id.rename_email);
		mBtnAvatar.setText(R.string.avatar);
		mBtnAvatar.setReason(R.string.no_setting);
//		mBtnAvatar.setImageResource(R.drawable.ic_chevron_right_grey600_24dp);
		mBtnName.setText(R.string.name);
		mBtnName.setReason(R.string.no_setting);
//		mBtnName.setImageResource(R.drawable.ic_chevron_right_grey600_24dp);
		mBtnPhone.setText(R.string.binding_phone);
		mBtnPhone.setReason(R.string.no_setting);
//		mBtnPhone.setImageResource(R.drawable.ic_chevron_right_grey600_24dp);
		mBtnEmail.setText(R.string.binding_emil);
		mBtnEmail.setReason(R.string.no_setting);
//		mBtnEmail.setImageResource(R.drawable.ic_chevron_right_grey600_24dp);
		final MaterialDialog mMaterialDialog = new MaterialDialog(getActivity());		
		View v = LayoutInflater.from(getActivity()).inflate(R.layout.material_dialog_update, container,false);
		metDeviveName = (MaterialEditText)v.findViewById(R.id.met_device_name);
		metDeviveName.setHint(R.string.name);
		((TextView)v.findViewById(R.id.dialog_title)).setText(R.string.setting_name);
		((Button)v.findViewById(R.id.btn_save)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String name = metDeviveName.getText().toString();
				if(ToolString.isNoBlankAndNoNull(name)){
					ExecToCloudSql.updateUserInfo(handler, "name", name);
					mMaterialDialog.dismiss();
				}else{
					ToolAlert.toastShort(getString(R.string.name_null));
				}
				
			}
		});
		((Button)v.findViewById(R.id.btn_cancel)).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mMaterialDialog.dismiss();
			}
		});		
		mMaterialDialog.setCancelable(true).setCanceledOnTouchOutside(true).setView(v).setIsShowIMM(true);
		final MaterialDialog mPhoneDialog = new MaterialDialog(getActivity());	
		mPhoneDialog.setTitle(R.string.rename_phone_title).setMessage(R.string.rename_phone_msg)
								.setCancelable(true).setCanceledOnTouchOutside(true)
								.setPositiveButton(R.string.ok, new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										mPhoneDialog.dismiss();
										if(ToolString.isNoBlankAndNoNull(Phone)){
											Intent intent = new Intent();
											intent.setClass(getActivity(), UserSettingsActivity.class);
											intent.putExtra("phone",Phone);
											intent.putExtra("Page",UserSettingsActivity.Page_UpdateUserPhone);
											startActivityForResult(intent, 10);
										}else{
											ToolAlert.toast(getString(R.string.no_init));
										}										
									}
								}).setNegativeButton(R.string.cancel_name, new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										mPhoneDialog.dismiss();
										
									}
								});
		final MaterialDialog mEmailDialog = new MaterialDialog(getActivity());	
		mEmailDialog.setTitle(R.string.rename_email_title).setMessage(R.string.rename_email_msg)
								.setCancelable(true).setCanceledOnTouchOutside(true)
								.setPositiveButton(R.string.ok, new OnClickListener() {
			
									@Override
									public void onClick(View v) {
										mEmailDialog.dismiss();
										if(ToolString.isNoBlankAndNoNull(Phone)){
											Intent intent = new Intent();
											intent.setClass(getActivity(), UserSettingsActivity.class);
											intent.putExtra("phone",Phone);
											intent.putExtra("Page",UserSettingsActivity.Page_UpdateUserEmail);
											startActivityForResult(intent,10);
										}else{
											ToolAlert.toast(getString(R.string.no_init));
										}
									}
								}).setNegativeButton(R.string.cancel_name, new OnClickListener() {
			
									@Override
									public void onClick(View v) {
										mEmailDialog.dismiss();
				
									}
								});
		final MaterialDialog mAvatarDialog = new MaterialDialog(getActivity());	
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item2);
		arrayAdapter.add(getString(R.string.selector_dicm));
		arrayAdapter.add(getString(R.string.photograph));
		ListView listView = new ListView(getActivity());
		@SuppressWarnings("deprecation")
		Drawable drawable = getResources().getDrawable(R.drawable.settings_selector); 
		listView.setSelector(drawable);
		listView.setDividerHeight(0);
		listView.setVerticalScrollBarEnabled(true);
		listView.setAdapter(arrayAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				File temp = MyApplication.gainContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);//������Ŀ �ļ���  
				switch (arg2) {
				case 0:
					Intent innerIntent = new Intent(Intent.ACTION_GET_CONTENT);  
			        innerIntent.putExtra("crop", "true");// ���ܳ�������С���򣬲�Ȼû�м������ܣ�ֻ��ѡȡͼƬ  
			        innerIntent.putExtra("aspectX", 1); // ���ַŴ����С  
			        innerIntent.setType("image/*"); // �鿴���� ��ϸ�������� com.google.android.mms.ContentType   		        			        
			        
			        if (!temp.exists()) {  
			            temp.mkdir();  
			        }  
			        tempFileName = Calendar.getInstance().getTimeInMillis()+".jpg";// ��ʱ����Ϊ�ļ���
			        tempFile=new File(temp,tempFileName);         
			        innerIntent.putExtra("output", Uri.fromFile(tempFile));  // ר��Ŀ���ļ�     
			        innerIntent.putExtra("outputFormat", "JPEG"); //�����ļ���ʽ    
			          
			        Intent wrapperIntent = Intent.createChooser(innerIntent, getString(R.string.selector_pictures)); //��ʼ �����ñ���  
			        startActivityForResult(wrapperIntent, 1); // �践�� ��Ϊ 1  onActivityResult �е� requestCode ��Ӧ  
					break;
				case 1:
					Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
					if (!temp.exists()) {  
				           temp.mkdir();  
				    }  
				    tempFileName = Calendar.getInstance().getTimeInMillis()+".jpg";// ��ʱ����Ϊ�ļ���
				    tempFile=new File(temp,tempFileName);
				    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
				    intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
					startActivityForResult(intent, 2);// �践�� ��Ϊ2  onActivityResult �е� requestCode ��Ӧ					
					break;

				default:
					break;
				}
				mAvatarDialog.dismiss();
			}
		});
		mAvatarDialog.setTitle(R.string.replace_avatar).setContentView(listView)
								.setCancelable(true).setCanceledOnTouchOutside(true)
								.setNegativeButton(R.string.cancel_name, new OnClickListener() {
									
									@Override
									public void onClick(View v) {
										mAvatarDialog.dismiss();
									}
								});
		mBtnAvatar.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mAvatarDialog.show();
			}
		});
		mBtnName.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mMaterialDialog.show();	
			}
		});
		mBtnPhone.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mPhoneDialog.show();
			}
		});
		mBtnEmail.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mEmailDialog.show();
			}
		});
		ExecToCloudSql.getUserInfo(handler);
		return view;
	}
		
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {  
		case 1:
			if(tempFile.exists()){
				Drawable drawable = Drawable.createFromPath(tempFile.getAbsolutePath());
				mBtnAvatar.setImageReasonDrawable(drawable);
				new Thread(new Runnable() {
					
					@Override
					public void run() {
						ToolOssService.initOssService(getActivity());
						ToolOssService.upload(tempFile,tempFileName);
					}
				}).start();
				ExecToCloudSql.updateUserInfo(handler, "avatar", tempFileName);
			}			
			break;
		case 2:
			if(tempFile.exists()){
				// �ü�ͼƬ��ͼ
		        Intent intent = new Intent("com.android.camera.action.CROP");
		        intent.setDataAndType(Uri.fromFile(tempFile), "image/*");
		        intent.putExtra("crop", "true");
		        // �ü���ı�����1��1
		        intent.putExtra("aspectX", 1);
		        intent.putExtra("aspectY", 1);
		        // �ü������ͼƬ�ĳߴ��С
		        intent.putExtra("outputX", 250);
		        intent.putExtra("outputY", 250);
		        
		        File temp = MyApplication.gainContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);//������Ŀ �ļ���  
		        if (!temp.exists()) {  
		            temp.mkdir();  
		        }  
		        tempFileName = Calendar.getInstance().getTimeInMillis()+".jpg";// ��ʱ����Ϊ�ļ���
		        tempFile=new File(temp,tempFileName);         
		        intent.putExtra("output", Uri.fromFile(tempFile));  // ר��Ŀ���ļ� 
		 
		        intent.putExtra("outputFormat", "JPEG");// ͼƬ��ʽ
		        //intent.putExtra("noFaceDetection", true);// ȡ������ʶ��
		        intent.putExtra("return-data", true);
		        // ����һ�����з���ֵ��Activity��������ΪPHOTO_REQUEST_CUT
		        startActivityForResult(intent, 1);
			}
			break;
		case 10:
			ExecToCloudSql.getUserInfo(handler);
			break;
		default:
			break;
		}
	}

	private static class MyHandler extends Handler {
		WeakReference<FragmentRename> mFragment;
		public MyHandler(FragmentRename fragment){
			mFragment = new WeakReference<FragmentRename>(fragment);
		}
		@Override
		public void handleMessage(Message msg) {
			FragmentRename theFragment = mFragment.get();
			if(msg.obj !=null){
				switch (msg.what) {			
				case Constant.User_ID:
					try {
						ArrayList<UserInfo> list = ParseDevice.parseUserInfo(msg.obj.toString());
						for (UserInfo userInfo : list) {
							theFragment.Phone = userInfo.getPhone();
							theFragment.mBtnName.setReason(userInfo.getName());
							theFragment.mBtnPhone.setReason(userInfo.getPhone());
							theFragment.mBtnEmail.setReason(userInfo.getEmail());
							theFragment.metDeviveName.setText(userInfo.getName());							
							if(ToolString.isNoBlankAndNoNull(userInfo.getAvatar()) && !userInfo.getAvatar().equals("null")){
								File tempFile = new File(MyApplication.gainContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), 
										userInfo.getAvatar());
								if(tempFile.exists()){
									Drawable drawable = Drawable.createFromPath(tempFile.getAbsolutePath());
									theFragment.mBtnAvatar.setImageReasonDrawable(drawable);
								}else{
									
								}
							}	
						}
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}					
					break;
				case Constant.STATUS_ID:
					ExecToCloudSql.getUserInfo(this);
					break;
				default:
					break;
				}
			}			
			super.handleMessage(msg);
		}	
		
	}
}
