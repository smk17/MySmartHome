package com.smk17.mysmarthome;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.Thread.UncaughtExceptionHandler;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.pgyersdk.crash.PgyCrashManager;
import com.smk17.android.tools.ToolAlert;
import com.smk17.mysmarthome.activity.LoginActivity;
import com.smk17.mysmarthome.activity.MainActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;

/**
 * UncaughtException������,��������Uncaught�쳣��ʱ��,�и������ӹܳ���,����¼���ʹ��󱨸�.
 * 
 * @author user
 * 
 */
public class CrashHandler implements UncaughtExceptionHandler {
	
	public static final String TAG = "CrashHandler";
	
	/**
	 * ϵͳĬ�ϵ�UncaughtException������ 
	 */
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	/**
	 * CrashHandlerʵ��
	 */
	private static CrashHandler INSTANCE = new CrashHandler();

	/**
	 * �����洢�豸��Ϣ���쳣��Ϣ
	 */
	private Map<String, String> infos = new HashMap<String, String>();

	/**
	 * ���ڸ�ʽ������,��Ϊ��־�ļ�����һ����
	 */
	private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault());
	
	/**
	 * ��ֻ֤��һ��CrashHandlerʵ��
	 */
	private CrashHandler() {
	}

	/** ��ȡCrashHandlerʵ�� ,����ģʽ */
	public static CrashHandler getInstance() {
		return INSTANCE;
	}

	/**
	 * ��ʼ��
	 * 
	 * @param context
	 */
	public void init() {
		//��ȡϵͳĬ�ϵ�UncaughtException������
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		//���ø�CrashHandlerΪ�����Ĭ�ϴ�����
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * ��UncaughtException����ʱ��ת��ú���������
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			//����û�û�д�������ϵͳĬ�ϵ��쳣������������
			mDefaultHandler.uncaughtException(thread, ex);
		} else {
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
			Intent intent = new Intent();
			if(MyApplication.getMainDeviceId()==null){
				intent.setClass(MyApplication.gainContext(), LoginActivity.class);
				intent.putExtra("Exception", true);
			}else{
				intent.setClass(MyApplication.gainContext(), MainActivity.class);
				intent.putExtra("status", 0);
				intent.putExtra("MainDeviceNumRows", MyApplication.getMainDeviceNumRows());
				intent.putExtra("UserID", MyApplication.getUserID());
				intent.putExtra("MainDeviceId", MyApplication.getMainDeviceId());
				intent.putExtra("NetMode", MyApplication.getNetMode());
			}
            PendingIntent restartIntent = PendingIntent.getActivity(    
            		MyApplication.gainContext(), 0, intent,    
                    Intent.FLAG_ACTIVITY_NEW_TASK);                                                 
            //�˳�����                                          
            AlarmManager mgr = (AlarmManager)MyApplication.gainContext().getSystemService(Context.ALARM_SERVICE);    
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100,    
                    restartIntent); // 0.1���Ӻ�����Ӧ��   
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(0);
		}
	}

	/**
	 * �Զ��������,�ռ�������Ϣ ���ʹ��󱨸�Ȳ������ڴ����.
	 * 
	 * @param ex
	 * @return true:��������˸��쳣��Ϣ;���򷵻�false.
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}
		//ʹ��Toast����ʾ�쳣��Ϣ
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				ToolAlert.toastLong("�ܱ�Ǹ,��������쳣,�����˳�.");
				Looper.loop();
			}
		}.start();
		//�ռ��豸������Ϣ 
		//collectDeviceInfo(MyApplication.gainContext());
		//������־�ļ� 
		//saveCrashInfo2File(ex);
		Throwable cause = ex.getCause();
		while (cause != null) {
			PgyCrashManager.reportCaughtException(MyApplication.gainContext(),(Exception)(cause));
			cause = cause.getCause();
		}
		
		return true;
	}
	
	/**
	 * �ռ��豸������Ϣ
	 * @param ctx
	 */
	public void collectDeviceInfo(Context ctx) {
		try {
			PackageManager pm = ctx.getPackageManager();
			PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
			if (pi != null) {
				String versionName = pi.versionName == null ? "null" : pi.versionName;
				String versionCode = pi.versionCode + "";
				infos.put("versionName", versionName);
				infos.put("versionCode", versionCode);
			}
		} catch (NameNotFoundException e) {
			Log.e(TAG, "an error occured when collect package info", e);
		}
		Field[] fields = Build.class.getDeclaredFields();
		for (Field field : fields) {
			try {
				field.setAccessible(true);
				infos.put(field.getName(), field.get(null).toString());
				Log.d(TAG, field.getName() + " : " + field.get(null));
			} catch (Exception e) {
				Log.e(TAG, "an error occured when collect crash info", e);
			}
		}
	}

	/**
	 * ���������Ϣ���ļ���
	 * 
	 * @param ex
	 * @return	�����ļ�����,���ڽ��ļ����͵�������
	 */
	public File saveCrashInfo2File(Throwable ex) {
		
		StringBuffer sb = new StringBuffer();
		for (Map.Entry<String, String> entry : infos.entrySet()) {
			String key = entry.getKey();
			String value = entry.getValue();
			sb.append(key + "=" + value + "\n");
		}
		
		Writer writer = new StringWriter();
		PrintWriter printWriter = new PrintWriter(writer);
		ex.printStackTrace(printWriter);
		Throwable cause = ex.getCause();
		while (cause != null) {
			cause.printStackTrace(printWriter);
			cause = cause.getCause();
		}
		printWriter.close();
		String result = writer.toString();
		sb.append(result);
		try {
			long timestamp = System.currentTimeMillis();
			String time = formatter.format(new Date());
			String fileName = "crash-" + time + "-" + timestamp + ".log";
			if (Environment.getExternalStorageState().equals(Environment.MEDIA_NOFS)) {
				File dir = new File(MyApplication.gainContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS),"log");
				if (!dir.exists()) {
					dir.mkdirs();
				}
				File file = new File(dir, fileName);
				FileOutputStream fos = new FileOutputStream(file);
				fos.write(sb.toString().getBytes());
				fos.close();
				return file;
			}			
		} catch (Exception e) {
			Log.e(TAG, "an error occured while writing file...", e);
		}
		return null;
	}
}
