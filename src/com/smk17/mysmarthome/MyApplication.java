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
	/**�����ṩ����Ӧ���������ڵ�Context**/
	private static Context instance;
	//private ArrayList<Activity> list = new ArrayList<Activity>();  
	
	/**
	 * �����ṩApplication Context
	 * @return
	 */
	public static Context gainContext() {
		return instance;
	}
	
	
	@Override
    public void onCreate()
    {
        super.onCreate();
        // ��ʼ��ȫ�ֱ���
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
     * ��������ģʽ�������֣�����������
     * @param NetMode
     */
    public static void setNetMode(Integer netMode)
    {
        NetMode = netMode;
    }
    /**
     * ��ȡ��ǰ����ģʽ
     * @return
     */
    public static Integer getNetMode()
    {
        return NetMode;
    }
    /**
     * �����û�������������Ŀ
     * @param numRows
     */
    public static void setMainDeviceNumRows(Integer numrows)
    {
        numRows = numrows;
    }
    /**
     * ��ȡ�û�������������Ŀ
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
	 * ��ȡ�����Ƿ�������
	 * @return
	 */
	public static boolean isNetworkReady(){
		return ToolNetwork.getInstance().init(instance).isConnected();
	}
	/**
	 * ��ȡ����������Ϣ</br> �����磺NO</br> WIFI���磺WIFI</br> WAP���磺CMWAP</br>
	 * NET���磺CMNET</br>
	 * 
	 * @return
	 */
	public static String getNetworkType(){
		return ToolNetwork.getInstance().init(instance).getNetworkType();
	}
	
//	/** 
//     * Activity�ر�ʱ��ɾ��Activity�б��е�Activity����*/  
//    public void removeActivity(Activity a){  
//        list.remove(a);  
//    }  
//      
//    /** 
//     * ��Activity�б������Activity����*/  
//    public void addActivity(Activity a){  
//        list.add(a);
//    }  
//      
//    /** 
//     * �ر�Activity�б��е�����Activity*/  
//    public void finishActivity(){  
//        for (Activity activity : list) {    
//            if (null != activity) {    
//                activity.finish();    
//            }    
//        }  
//        //ɱ����Ӧ�ý���  
//       android.os.Process.killProcess(android.os.Process.myPid());    
//    }  
}
