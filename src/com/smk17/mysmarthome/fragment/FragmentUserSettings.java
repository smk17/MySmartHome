package com.smk17.mysmarthome.fragment;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import org.json.JSONException;

import com.alibaba.sdk.android.oss.callback.GetFileCallback;
import com.alibaba.sdk.android.oss.model.OSSException;
import com.smk17.android.tools.ToolOssService;
import com.smk17.android.tools.ToolString;
import com.smk17.mysmarthome.Constant;
import com.smk17.mysmarthome.MyApplication;
import com.smk17.mysmarthome.R;
import com.smk17.mysmarthome.activity.UserSettingsActivity;
import com.smk17.mysmarthome.cloud.ExecToCloudSql;
import com.smk17.mysmarthome.cloud.ParseDevice;
import com.smk17.mysmarthome.domain.UserInfo;
import com.yh.materialdesign.views.ButtonFloatSmall;
import com.yh.materialdesign.views.CircleImageView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class FragmentUserSettings extends MianFragment{

	private static String TAG = "FragmentUserSettings";
	private int[] id = { R.id.user_rename, R.id.user_binding, R.id.user_login, R.id.user_jurisdiction, R.id.user_theme, 
			R.id.user_advanced_settings, R.id.user_quit  };
	private TextView mTvName;
	private TextView mTvReason;
	private ButtonFloatSmall mBfsAvatar;
	private CircleImageView mCivAvatar;
	private MyHandler handler = new MyHandler(this);
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.material_fragment_usersettings, container, false);  
		mBfsAvatar = (ButtonFloatSmall)view.findViewById(R.id.btnfs_avatar);	
		mCivAvatar = (CircleImageView)view.findViewById(R.id.civ_avatar);	
		mTvName =  (TextView) view.findViewById(R.id.tv_name);			
		mTvReason = (TextView) view.findViewById(R.id.tv_reason);	
		if(MyApplication.getNetMode().equals(Constant.HotSpot_Mode)){
			mTvName.setText(R.string.local_login_btnname);
			mTvReason.setText("");
		}else{
			ExecToCloudSql.getUserInfo(handler);
		}
		final SharedPreferences sp = getActivity().getSharedPreferences(Constant.INFO_THEME ,Activity.MODE_PRIVATE);
		final int theme_id = sp.getInt(Constant.THEME_ID, 1);
		int TextColor = getActivity().getResources().getColor(R.color.white);
		switch (theme_id) {
		case 1:
			TextColor = getActivity().getResources().getColor(R.color.black);			
			break;
		case 2:case 3:case 4:case 5:case 6:
			TextColor = getActivity().getResources().getColor(R.color.white);
			break;
		default:
			break;
		}
		mTvName.setTextColor(TextColor);
		for (int i = 0; i < id.length; i++) {
			View v = view.findViewById(id[i]);
			TextView tv = (TextView) v.findViewById(R.id.tv_item_set_name);			
            tv.setText(Constant.UserSettingsTitleNames[i]);
            tv.setTextColor(TextColor);
            if(i == id.length-1){
            	if(MyApplication.getNetMode().equals(Constant.HotSpot_Mode)){
            		tv.setText(R.string.log_user_quit);
            	}
            	ImageView iv = (ImageView)v.findViewById(R.id.iv_item_image);
            	iv.setVisibility(View.GONE);
            }
		}
		return view;
	}
	private void down(String tempFileName){
		File temp = MyApplication.gainContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);//自已项目 文件夹  
        if (!temp.exists()) {  
            temp.mkdir();  
        }
        File tempFile=new File(temp,tempFileName);         
		ToolOssService.initOssService(getActivity());
		ToolOssService.download(tempFile, tempFileName,new GetFileCallback() {

            @Override
            public void onSuccess(String objectKey, String filePath) {
                Log.d(TAG, "[onSuccess] - " + objectKey + " storage path: " + filePath);
                ExecToCloudSql.getUserInfo(handler);
            }

            @Override
            public void onProgress(String objectKey, int byteCount, int totalSize) {
                Log.d(TAG, "[onProgress] - current download: " + objectKey + " bytes:" + byteCount + " in total:" + totalSize);
            }

            @Override
            public void onFailure(String objectKey, OSSException ossException) {
                Log.e(TAG, "[onFailure] - download " + objectKey + " failed!\n" + ossException.toString());
                ossException.printStackTrace();
            }
        });		
	}
	
	private static class MyHandler extends Handler {
		WeakReference<FragmentUserSettings> mFragment;
		public MyHandler(FragmentUserSettings fragment){
			mFragment = new WeakReference<FragmentUserSettings>(fragment);
		}
		@Override
		public void handleMessage(Message msg) {
			FragmentUserSettings theFragment = mFragment.get();
			if(msg.obj !=null){
				switch (msg.what) {			
				case Constant.User_ID:
					try {
						ArrayList<UserInfo> list = ParseDevice.parseUserInfo(msg.obj.toString());
						for (UserInfo userInfo : list) {
							theFragment.mTvName.setText(userInfo.getName());
							theFragment.mTvReason.setText(userInfo.getPhone());
							if(ToolString.isNoBlankAndNoNull(userInfo.getAvatar()) && !userInfo.getAvatar().equals("null")){
								File tempFile = new File(MyApplication.gainContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), 
										userInfo.getAvatar());								
								if(tempFile.exists()){
									theFragment.mBfsAvatar.setVisibility(View.GONE);
									theFragment.mCivAvatar.setVisibility(View.VISIBLE);
									Drawable drawable = Drawable.createFromPath(tempFile.getAbsolutePath());
									theFragment.mCivAvatar.setImageDrawable(drawable);									
								}else{
									theFragment.down( userInfo.getAvatar() );
								}
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
			super.handleMessage(msg);
		}	
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		ExecToCloudSql.getUserInfo(handler);
	}



	@Override
	public void onMainAction() {
		Intent intent = new Intent();
		intent.setClass(getActivity(), UserSettingsActivity.class);
		intent.putExtra("Page",UserSettingsActivity.Page_Rename);
		startActivityForResult(intent, 11);
	}
	
	
}
