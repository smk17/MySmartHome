package com.smk17.android.tools;

import com.smk17.mysmarthome.MyApplication;
import com.smk17.mysmarthome.R;

import android.content.Context;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

public class ToolGetVersion {
	private static final String TAG = "Config";
	
	public static int getVerCode() {
		return getVerCode(MyApplication.gainContext());
	}
	
	public static int getVerCode(Context context) {
		int verCode = -1;
		try {
			verCode = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionCode;
		} catch (NameNotFoundException e) {
			Log.e(TAG, e.getMessage());
		}
		return verCode;
	}
	public static String getVerName() {
		return getVerName(MyApplication.gainContext());
	}
	
	public static String getVerName(Context context) {
		String verName = "";
		try {
			verName = context.getPackageManager().getPackageInfo(
					context.getPackageName(), 0).versionName;
		} catch (NameNotFoundException e) {
			Log.e(TAG, e.getMessage());
		}
		return verName;	

	}
	
	public static String getAppName(Context context) {
		String verName = context.getResources()
		.getText(R.string.app_name).toString();
		return verName;
	}
}
