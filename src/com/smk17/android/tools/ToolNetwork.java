package com.smk17.android.tools;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;
/**
 * ���ھ�̬�ڲ���ʵ�ֵĵ�������֤�̰߳�ȫ��������Ϣ������ <per> ʹ�øù�����֮ǰ���ǵ���AndroidManifest.xml���Ȩ����� <xmp>
 * <uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
 * </xmp> </per>
 * 
 * ��׿�ж�����״̬��ֻ��Ҫ����Ӧ��Activity����ط�����onCreat/onResum������һ�д��뼴��
 * NetWorkUtils.getInstance(getActivity()).validateNetWork();
 * 
 * 
 * 
 * @author ������
 * @version 1.0
 */
public class ToolNetwork {

	
	public final static String NETWORK_CMNET = "CMNET";
	public final static String NETWORK_CMWAP = "CMWAP";
	public final static String NETWORK_WIFI = "WIFI";
	public final static String TAG = "ToolNetwork";
	private static NetworkInfo networkInfo = null;
	private Context mContext = null;

	private ToolNetwork() {
	}

	public static ToolNetwork getInstance() {
		return SingletonHolder.instance;
	}

	public ToolNetwork init(Context context){
		this.mContext = context;
		return this;
	}
	
	/**
	 * �ж������Ƿ����
	 * 
	 * @return ��/��
	 */
	public boolean isAvailable() {
		ConnectivityManager manager = (ConnectivityManager) mContext
				.getApplicationContext().getSystemService(
						Context.CONNECTIVITY_SERVICE);
		if (null == manager) {
			return false;
		}
		networkInfo = manager.getActiveNetworkInfo();
		if (null == networkInfo || !networkInfo.isAvailable()) {
			return false;
		}
		return true;
	}

	/**
	 * �ж������Ƿ�������
	 * 
	 * @return ��/��
	 */
	public boolean isConnected() {
		if (!isAvailable()) {
			return false;
		}
		if (!networkInfo.isConnected()) {
			return false;
		}
		return true;
	}

	/**
	 * ��鵱ǰ���������Ƿ���ã���������ת�������������,����������ǿ�ƹرյ�ǰActivity
	 */
	public void validateNetWork() {

		if (!isConnected()) {
			Builder dialogBuilder = new AlertDialog.Builder(mContext);
			dialogBuilder.setTitle("��������");
			dialogBuilder.setMessage("���粻���ã��Ƿ������������磿");
			dialogBuilder.setPositiveButton(android.R.string.ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							((Activity) mContext).startActivityForResult(
									new Intent(
											Settings.ACTION_SETTINGS),
									which);
						}
					});
			dialogBuilder.setNegativeButton(android.R.string.cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
						}
					});
			dialogBuilder.create();
			dialogBuilder.show();
		}
	}

	/**
	 * ��ȡ����������Ϣ</br> �����磺NO</br> WIFI���磺WIFI</br> WAP���磺CMWAP</br>
	 * NET���磺CMNET</br>
	 * 
	 * @return
	 */
	public String getNetworkType() {
		if (isConnected()) {
			int type = networkInfo.getType();
			if (ConnectivityManager.TYPE_MOBILE == type) {
//				Log.i(TAG,
//						"networkInfo.getExtraInfo()-->"
//								+ networkInfo.getExtraInfo());
				if (NETWORK_CMWAP.equals(networkInfo.getExtraInfo()
						.toLowerCase())) {
					return NETWORK_CMWAP;
				} else {
					return NETWORK_CMNET;
				}
			} else if (ConnectivityManager.TYPE_WIFI == type) {
				return NETWORK_WIFI;
			}
		}

		return "NO";
	}

	private static class SingletonHolder {

		private static ToolNetwork instance = new ToolNetwork();
	}
}
