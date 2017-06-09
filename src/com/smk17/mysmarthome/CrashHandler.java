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
 * UncaughtException处理类,当程序发生Uncaught异常的时候,有该类来接管程序,并记录发送错误报告.
 * 
 * @author user
 * 
 */
public class CrashHandler implements UncaughtExceptionHandler {
	
	public static final String TAG = "CrashHandler";
	
	/**
	 * 系统默认的UncaughtException处理类 
	 */
	private Thread.UncaughtExceptionHandler mDefaultHandler;
	/**
	 * CrashHandler实例
	 */
	private static CrashHandler INSTANCE = new CrashHandler();

	/**
	 * 用来存储设备信息和异常信息
	 */
	private Map<String, String> infos = new HashMap<String, String>();

	/**
	 * 用于格式化日期,作为日志文件名的一部分
	 */
	private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss", Locale.getDefault());
	
	/**
	 * 保证只有一个CrashHandler实例
	 */
	private CrashHandler() {
	}

	/** 获取CrashHandler实例 ,单例模式 */
	public static CrashHandler getInstance() {
		return INSTANCE;
	}

	/**
	 * 初始化
	 * 
	 * @param context
	 */
	public void init() {
		//获取系统默认的UncaughtException处理器
		mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
		//设置该CrashHandler为程序的默认处理器
		Thread.setDefaultUncaughtExceptionHandler(this);
	}

	/**
	 * 当UncaughtException发生时会转入该函数来处理
	 */
	@Override
	public void uncaughtException(Thread thread, Throwable ex) {
		if (!handleException(ex) && mDefaultHandler != null) {
			//如果用户没有处理则让系统默认的异常处理器来处理
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
            //退出程序                                          
            AlarmManager mgr = (AlarmManager)MyApplication.gainContext().getSystemService(Context.ALARM_SERVICE);    
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100,    
                    restartIntent); // 0.1秒钟后重启应用   
			android.os.Process.killProcess(android.os.Process.myPid());
			System.exit(0);
		}
	}

	/**
	 * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成.
	 * 
	 * @param ex
	 * @return true:如果处理了该异常信息;否则返回false.
	 */
	private boolean handleException(Throwable ex) {
		if (ex == null) {
			return false;
		}
		//使用Toast来显示异常信息
		new Thread() {
			@Override
			public void run() {
				Looper.prepare();
				ToolAlert.toastLong("很抱歉,程序出现异常,即将退出.");
				Looper.loop();
			}
		}.start();
		//收集设备参数信息 
		//collectDeviceInfo(MyApplication.gainContext());
		//保存日志文件 
		//saveCrashInfo2File(ex);
		Throwable cause = ex.getCause();
		while (cause != null) {
			PgyCrashManager.reportCaughtException(MyApplication.gainContext(),(Exception)(cause));
			cause = cause.getCause();
		}
		
		return true;
	}
	
	/**
	 * 收集设备参数信息
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
	 * 保存错误信息到文件中
	 * 
	 * @param ex
	 * @return	返回文件名称,便于将文件传送到服务器
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
