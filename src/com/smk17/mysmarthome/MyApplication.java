package com.smk17.mysmarthome;

import com.pgyersdk.crash.PgyCrashManager;
import com.smk17.android.tools.ToolNetwork;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application{
	private static Integer NetMode;
	private static Integer numRows;
	private static Integer MainDeviceId;
	private static Integer UserID;
	private static Integer APPStyle;
	/**对外提供整个应用生命周期的Context**/
	private static Context instance;
	//private ArrayList<Activity> list = new ArrayList<Activity>();  
	
	/**
	 * 对外提供Application Context
	 * @return
	 */
	public static Context gainContext() {
		return instance;
	}
	
	
	@Override
    public void onCreate()
    {
        super.onCreate();
        // 初始化全局变量
        instance = this;
        PgyCrashManager.register(this);
//        CrashHandler crashHandler = CrashHandler.getInstance();  
//        crashHandler.init();
        setNetMode(Constant.Network_Mode); 
        setMainDeviceNumRows(0);
        setUserID(0);
        setAPPStyle(Constant.APPStyle_Material);
        setMainDeviceId(null);
    }
	
	public static void setAPPStyle(Integer appStyle)
    {
		APPStyle = appStyle;
    }
	
	public static Integer getAPPStyle()
    {
        return APPStyle;
    }
	
	public static void setMainDeviceId(Integer DeviceId)
    {
        MainDeviceId = DeviceId;
    }
	public static Integer getMainDeviceId()
    {
        return MainDeviceId;
    }
    /**
     * 设置网络模式，有两种：内网和外网
     * @param NetMode
     */
    public static void setNetMode(Integer netMode)
    {
        NetMode = netMode;
    }
    /**
     * 获取当前网络模式
     * @return
     */
    public static Integer getNetMode()
    {
        return NetMode;
    }
    /**
     * 设置用户关联的主控数目
     * @param numRows
     */
    public static void setMainDeviceNumRows(Integer numrows)
    {
        numRows = numrows;
    }
    /**
     * 获取用户关联的主控数目
     * @return
     */
    public static Integer getMainDeviceNumRows()
    {
        return numRows;
    }
    public static Integer getUserID() {
		return UserID;		
	}
    public static void setUserID(int userid) {
		UserID = userid;		
	}
    
    /**
	 * 获取网络是否已连接
	 * @return
	 */
	public static boolean isNetworkReady(){
		return ToolNetwork.getInstance().init(instance).isConnected();
	}
	/**
	 * 获取网络连接信息</br> 无网络：NO</br> WIFI网络：WIFI</br> WAP网络：CMWAP</br>
	 * NET网络：CMNET</br>
	 * 
	 * @return
	 */
	public static String getNetworkType(){
		return ToolNetwork.getInstance().init(instance).getNetworkType();
	}
	
//	/** 
//     * Activity关闭时，删除Activity列表中的Activity对象*/  
//    public void removeActivity(Activity a){  
//        list.remove(a);  
//    }  
//      
//    /** 
//     * 向Activity列表中添加Activity对象*/  
//    public void addActivity(Activity a){  
//        list.add(a);
//    }  
//      
//    /** 
//     * 关闭Activity列表中的所有Activity*/  
//    public void finishActivity(){  
//        for (Activity activity : list) {    
//            if (null != activity) {    
//                activity.finish();    
//            }    
//        }  
//        //杀死该应用进程  
//       android.os.Process.killProcess(android.os.Process.myPid());    
//    }  
}
