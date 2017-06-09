package com.smk17.mysmarthome.cloud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.smk17.mysmarthome.Constant;
import com.smk17.mysmarthome.MyApplication;
import com.smk17.mysmarthome.Utils.HttpUtils;
import com.smk17.mysmarthome.domain.DeviceProperty;
import com.smk17.mysmarthome.domain.DeviceScene;
import com.smk17.mysmarthome.domain.DeviceSensor;

/**
 * 执行操作到云数据库
 * @author Seng
 *
 */
public class ExecToCloudSql {
	
	public static void  getUserInfo(final Handler handler){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Message msg = new Message();	
				Map<String, String> params = new HashMap<String, String>();
				params.put("userid", String.valueOf(MyApplication.getUserID()));
				params.put("ak", Constant.REQUEST_AK);
				try {
					msg.what = Constant.User_ID;
					msg.obj  = HttpUtils.Post(Constant.URL_GET_USERINFO, params);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				handler.sendMessage(msg);
			}
		}).start();
	}
	
	/**
	 * 更新用户信息
	 * @param handler
	 * @param key key is name or phone or email
	 * @param value
	 */
	public static void  updateUserInfo(final Handler handler,final String key,
			final String value){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Message msg = new Message();	
				Map<String, String> params = new HashMap<String, String>();
				params.put("userid", String.valueOf(MyApplication.getUserID()));
				params.put(key, value);
				params.put("ak", Constant.REQUEST_AK);
				try {
					msg.what = Constant.STATUS_ID;
					msg.obj  = HttpUtils.Post(Constant.URL_UPDATE_USERINFO, params);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				handler.sendMessage(msg);
			}
		}).start();
	}
	
	/**
	 * 删除情景模式
	 * @param handler
	 * @param id
	 */
	public static void  DeleteSceneRunnable(final Handler handler,final int id){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Message msg = new Message();	
				Map<String, String> params = new HashMap<String, String>();
				params.put("id", String.valueOf(id));
				params.put("ak", Constant.REQUEST_AK);
				try {
					msg.what = Constant.STATUS_ID;
					msg.obj  = "正在删除...";
					HttpUtils.Post(Constant.URL_DELETE_SCENE, params);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				handler.sendMessage(msg);				
			}
		}).start();
	}
	
	public static void updateSceneRunnable(final Handler handler,int SceneID,final DeviceScene mScene){
		if(SceneID > 0){
			updateSceneRunnable(handler, mScene);
		}
	}
	
	public static void updateSceneRunnable(final Handler handler,final DeviceScene mScene){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Message msg = new Message();	
				try {
					Map<String, String> params = new HashMap<String, String>();
					params.put("scene", mScene.toJSON());
					params.put("ak", Constant.REQUEST_AK);
					msg.what = Constant.STATUS_ID;
					msg.obj  = "正在更新...";
					HttpUtils.Post(Constant.URL_UPDATE_ONESCENE, params);
//					msg.obj = mScene.toJSON();
				} catch (Exception e) {
					e.printStackTrace();
				}
				handler.sendMessage(msg);				
			}
		}).start();
	}
	
	/**
	 * 插入情景模式
	 * @param handler
	 * @param mScene
	 */
	public static void  insertSceneRunnable(final Handler handler,final DeviceScene mScene){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Message msg = new Message();	
				Map<String, String> params = new HashMap<String, String>();
				params.put("scene", mScene.toJSON());
				params.put("ak", Constant.REQUEST_AK);
				try {
					msg.what = Constant.STATUS_ID;
					msg.obj  =  "正在添加...";
					HttpUtils.Post(Constant.URL_INSERT_SCENE, params);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				handler.sendMessage(msg);
				
			}
		}).start();
	}
	
	public static void  getSceneRunnable(final Handler handler,final int SceneID){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Message msg = new Message();	
				if(SceneID > 0){
					Map<String, String> params = new HashMap<String, String>();
					params.put("id", String.valueOf(SceneID));
					params.put("ak", Constant.REQUEST_AK);
					try {
						msg.what = Constant.SCENE_ID;
						msg.obj  = HttpUtils.Post(Constant.URL_GET_ONESCENE, params);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					handler.sendMessage(msg);
				}
			}
		}).start();
	}
	
	/**
	 * 获取情景模式列表
	 * @param handler
	 */
	public static void  getAllSceneRunnable(final Handler handler){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Message msg = new Message();	
				Map<String, String> params = new HashMap<String, String>();
				params.put("userid", String.valueOf(MyApplication.getUserID()));
				params.put("ak", Constant.REQUEST_AK);
				try {
					msg.what = Constant.SCENE_ID;
					msg.obj  = HttpUtils.Post(Constant.URL_GET_ALLSCENE, params);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				handler.sendMessage(msg);
			}
		}).start();
	}
	
	/**
	 * 获取情景模式列表，有延时
	 * @param handler
	 * @param delay
	 */
	public static void  getAllSceneRunnable(final Handler handler,final long delay){
		new Thread(new Runnable() {
			
			@Override
			public void run() {					
				try {
					Thread.sleep(delay);
					getAllSceneRunnable(handler);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();			
	}
	
	/**
	 * 获取主控匹配模式的状态
	 * @param handler
	 * @param MainDeviceSelectedId
	 */
	public static void  matchDeviceRunnable(final Handler handler,final int MainDeviceSelectedId){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Message msg = new Message();	
				Map<String, String> params = new HashMap<String, String>();
				params.put("id", String.valueOf(MainDeviceSelectedId));
				params.put("ak", Constant.REQUEST_AK);
				try {
					msg.what = Constant.MATCHDEVICE_ID;
					msg.obj  = HttpUtils.Post(Constant.URL_GET_MATCHMAINDEVICE, params);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				handler.sendMessage(msg);
			}
		}).start();
	}
	
	/**
	 * 获取主控匹配模式的状态
	 * @param MainDeviceSelectedId
	 * @param value
	 */
	public static void  matchDeviceRunnable(final int MainDeviceSelectedId,final String value ){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("id", String.valueOf(MainDeviceSelectedId));
				params.put("value", value);
				params.put("ak", Constant.REQUEST_AK);
				try {					
					HttpUtils.Post(Constant.URL_UPDATE_MATCHMAINDEVICE, params);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		
			}
		}).start();
	}
	
	/**
	 * 删除主控设备	
	 * @param handler
	 * @param device
	 */
	public static void  DeleteMainDeviceRunnable(final Handler handler,final DeviceSensor device){
		new Thread(new Runnable() {
			
			@Override
			public void run() {					
				Message msg = new Message();	
				Map<String, String> params = new HashMap<String, String>();
				params.put("id", String.valueOf(device.getId()));
				params.put("userid", String.valueOf(MyApplication.getUserID()));
				params.put("ak", Constant.REQUEST_AK);
				try {
					msg.what = Constant.STATUS_ID;
					msg.obj  = "正在删除...";
					HttpUtils.Post(Constant.URL_DELETE_MAINDEVICE, params);
					handler.sendMessage(msg);						
					ExecToCloudSql.getMainDeviceRunnable(handler,Constant.TASKTIME);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
			}
		}).start();		
	}
	
	/**
	 * 删除终端设备
	 * @param handler
	 * @param device
	 */
	public static void  DeleteTerminalDeviceRunnable(final Handler handler,final DeviceSensor device){
		new Thread(new Runnable() {
			
			@Override
			public void run() {					
				Message msg = new Message();	
				Map<String, String> params = new HashMap<String, String>();
				params.put("id", String.valueOf(device.getId()));
				params.put("ak", Constant.REQUEST_AK);
				try {
					msg.what = Constant.STATUS_ID;
					msg.obj  = "正在删除...";
					HttpUtils.Post(Constant.URL_DELETE_TERMINALDEVICE, params);
					handler.sendMessage(msg);						
					ExecToCloudSql.getMainDeviceRunnable(handler,Constant.TASKTIME);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
			}
		}).start();		
	}
	
	public static void  UpdateDevicePropertyRunnable(final DeviceProperty dProperty){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Message msg = new Message();	
				Map<String, String> params = new HashMap<String, String>();
				params.put("id", String.valueOf(dProperty.getId()));
				params.put("value", dProperty.getValue());
				params.put("sensor_id", String.valueOf(dProperty.getSensorId()));
				params.put("ak", Constant.REQUEST_AK);
				try {
					msg.what = Constant.SCENE_ID;
					msg.obj  = HttpUtils.Post(Constant.URL_UPDATE_DEVICEPROPERTY, params);
					Log.e("UpdateDevicePropertyRunnable", "id:"+params.get("id")+"value:"+params.get("value")+"sensor_id:"+params.get("sensor_id") );
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
	}
		
	public static void  UpdateDeviceTagRunnable(final ArrayList<DeviceSensor> tmpList){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Map<String, String> params = null;
				try {	
					for (DeviceSensor deviceSensor : tmpList) {
						params = new HashMap<String, String>();
						params.put("id", String.valueOf(deviceSensor.getId()));
						params.put("ak", Constant.REQUEST_AK);
						HttpUtils.Post(Constant.URL_UPDATE_DEVICETAG, params);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	public static void  UpdateDeviceTagRunnable(final int id){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Map<String, String> params = new HashMap<String, String>();
				params.put("id", String.valueOf(id));
				params.put("ak", Constant.REQUEST_AK);
				try {					
					HttpUtils.Post(Constant.URL_UPDATE_DEVICETAG, params);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	/**
	 * 更新设备名字
	 * @param handler
	 * @param device
	 */
	public static void  UpdateDeviceRunnable(final Handler handler,final DeviceSensor device){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Message msg = new Message();	
				Map<String, String> params = new HashMap<String, String>();
				params.put("zoneid", String.valueOf(device.getZoneId()));
				params.put("id", String.valueOf(device.getId()));
				params.put("name", device.getName());
				params.put("ak", Constant.REQUEST_AK);
				try {
					msg.what = Constant.STATUS_ID;
					msg.obj  = "正在保存..";
					HttpUtils.Post(Constant.URL_UPDATE_TERMINALDEVICE, params);
					handler.sendMessage(msg);
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();			
	}
	/**
	 * 批量更新设备区域
	 * @param handler
	 * @param tmplist
	 */
	public static void  UpdateDeviceRunnable(final Handler handler,final ArrayList<DeviceSensor> tmplist){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Message msg = new Message();
				msg.what = Constant.STATUS_ID;
				msg.obj  = "正在保存..";
				for (DeviceSensor device : tmplist) {
					Map<String, String> params = new HashMap<String, String>();
					params.put("zoneid", String.valueOf(device.getZoneId()));
					params.put("id", String.valueOf(device.getId()));
					params.put("name", device.getName());
					params.put("ak", Constant.REQUEST_AK);
					try {
						HttpUtils.Post(Constant.URL_UPDATE_TERMINALDEVICE, params);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}								
				handler.sendMessage(msg);
			}
		}).start();			
	}
	
	/**
	 * 获取设备类型列表
	 * @param handler
	 */
	public static void getDeviceCategory(final Handler handler){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Message msg = new Message();	
				Map<String, String> params = new HashMap<String, String>();
				params.put("ak", Constant.REQUEST_AK);
				try {
					msg.what = Constant.DEVICECATEGORY_ID;
					msg.obj  = HttpUtils.Post(Constant.URL_GET_DEVICECATEGORY, params);				
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				handler.sendMessage(msg);	
			}
		}).start();
	}
	
	/**
	 * 获取终端设备列表
	 * @param handler
	 */
	public static void  getTerminalDeviceRunnable(final Handler handler){
		getDeviceRunnable(handler, Constant.DEVICESENSOR_ID);		
	}
	
	/**
	 * 获取终端设备列表，有延时
	 * @param handler
	 * @param delay
	 */
	public static void  getTerminalDeviceRunnable(final Handler handler,final long delay){
		new Thread(new Runnable() {
			
			@Override
			public void run() {					
				try {
					Thread.sleep(delay);
					getTerminalDeviceRunnable(handler);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();			
	}
	
	/**
	 * 获取主控列表
	 * @param handler
	 */
	public static void  getMainDeviceRunnable(final Handler handler){
		getDeviceRunnable(handler, Constant.MAINDEVICE_ID);		
	}
	
	/**
	 * 获取主控列表，有延时
	 * @param handler
	 * @param delay
	 */
	public static void  getMainDeviceRunnable(final Handler handler,final long delay){
		new Thread(new Runnable() {
			
			@Override
			public void run() {					
				try {
					Thread.sleep(delay);
					getMainDeviceRunnable(handler);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();			
	}
	
	/**
	 * 获取某账户下的区域列表
	 * @param handler
	 * @param what
	 */
	public static void  getDeviceRunnable(final Handler handler,final int what){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Message msg = new Message();	
				Map<String, String> params = new HashMap<String, String>();
				params.put("userid", String.valueOf(MyApplication.getUserID()));
				params.put("ak", Constant.REQUEST_AK);
				try {
					msg.what = what;
					msg.obj  = HttpUtils.Post(Constant.URL_GET_DEVICESENSOR, params);				
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				handler.sendMessage(msg);				
			}
		}).start();
	}
	
	/**
	 * 获取某账户下的区域列表
	 * @param handler
	 */
	public static void  getAllZoneRunnable(final Handler handler){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Message msg = new Message();	
				Map<String, String> params = new HashMap<String, String>();
				params.put("userid", String.valueOf(MyApplication.getUserID()));
				params.put("ak", Constant.REQUEST_AK);
				try {
					msg.what = Constant.DEVICEZONE_ID;
					msg.obj  = HttpUtils.Post(Constant.URL_GET_ALLZONE, params);					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				handler.sendMessage(msg);		
			}
		}).start();			
	}
	
	/**
	 * 获取某主控下的区域列表，有延时
	 * @param handler
	 * @param delay
	 */
	public static void  getAllZoneRunnable(final Handler handler,final long delay){
		new Thread(new Runnable() {
			
			@Override
			public void run() {					
				try {
					Thread.sleep(delay);
					getAllZoneRunnable(handler);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();			
	}
	
	/**
	 * 获取区域列表
	 * @param handler
	 */
	public static void  getZoneRunnable(final Handler handler){
		getZoneRunnable(handler, MyApplication.getMainDeviceId());
	}
	
	/**
	 * 获取区域列表
	 * @param handler
	 */
	public static void  getZoneRunnable(final Handler handler,final int MainDeviceId){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Message msg = new Message();	
				Map<String, String> params = new HashMap<String, String>();
				params.put("id", String.valueOf(MainDeviceId));
				params.put("ak", Constant.REQUEST_AK);
				try {
					msg.what = Constant.DEVICEZONE_ID;
					msg.obj  = HttpUtils.Post(Constant.URL_GET_DEVICEZONE, params);					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				handler.sendMessage(msg);		
			}
		}).start();			
	}
	
	/**
	 * 获取某主控下的区域列表，有延时
	 * @param handler
	 * @param delay
	 */
	public static void  getZoneRunnable(final Handler handler,final long delay){
		new Thread(new Runnable() {
			
			@Override
			public void run() {					
				try {
					Thread.sleep(delay);
					getZoneRunnable(handler);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();			
	}
	
	/**
	 * 添加一个区域
	 * @param sensorid
	 * @param name
	 */
	public static void AddZone(final String sensorid,final String name){
		new Thread(new Runnable() {
			
			@Override
			public void run() {	
				Map<String, String> params = new HashMap<String, String>();
				params.put("sensorid",sensorid);
				params.put("name",name );
				params.put("ak", Constant.REQUEST_AK);
				try {
					HttpUtils.Post(Constant.URL_ADD_ZONENAME, params);					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}						
			}
		}).start();		
	}
	
	/**
	 * 删除一个区域
	 * @param id
	 * @param zondid
	 */
	public static void DeleteZone(final String id,final String zondid){
		new Thread(new Runnable() {
			
			@Override
			public void run() {					
				Map<String, String> params = new HashMap<String, String>();
				params.put("id",id);
				params.put("zondid", zondid);		
				params.put("ak", Constant.REQUEST_AK);
				try {
					HttpUtils.Post(Constant.URL_DELETE_ZONE, params);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();		
	}
	
	/**
	 * 更新区域名字
	 * @param id
	 * @param name
	 */
	public static void UpdateZoneName(final String id,final String name){
		new Thread(new Runnable() {
			
			@Override
			public void run() {					
				Map<String, String> params = new HashMap<String, String>();
				params.put("id", id);
				params.put("name", name);
				params.put("ak", Constant.REQUEST_AK);
				try {
					HttpUtils.Post(Constant.URL_UPDATE_ZONENAME, params);					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();			
	}

	/**
	 * 更新情景模式
	 * @param scene
	 */
	public static void UpdateScene(final String id,final String value){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Message msg = new Message();	
				Map<String, String> params = new HashMap<String, String>();
				params.put("id", id);
				params.put("value", value);
				params.put("ak", Constant.REQUEST_AK);
				try {
					msg.what = Constant.SCENE_ID;
					msg.obj  = HttpUtils.Post(Constant.URL_UPDATE_SCENE, params);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public static void getJurisdictionRunnable(final Handler handler,final int sensor_id) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Message msg = new Message();	
				Map<String, String> params = new HashMap<String, String>();
				params.put("user_id", String.valueOf(MyApplication.getUserID()));
				params.put("sensor_id", String.valueOf(sensor_id));
				params.put("ak", Constant.REQUEST_AK);
				try {
					msg.what = Constant.Jurisdiction_ID;
					msg.obj  = HttpUtils.Post(Constant.URL_GET_JURISDICTION, params);
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public static void insertJurisdictionRunnable(final Handler handler, 
			final String Name, final String Phone, final int sensor_id) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Message msg = new Message();	
				Map<String, String> params = new HashMap<String, String>();
				params.put("user_id", String.valueOf(MyApplication.getUserID()));
				params.put("sensor_id", String.valueOf(sensor_id));
				params.put("name", Name);
				params.put("phone", Phone);
				params.put("ak", Constant.REQUEST_AK);
				try {
					msg.what = Constant.STATUS_ID;
					msg.obj  = HttpUtils.Post(Constant.URL_INSERT_JURISDICTION, params);
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public static void deleteJurisdictionRunnable(final Handler handler,final int ID) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Message msg = new Message();	
				Map<String, String> params = new HashMap<String, String>();
				params.put("ID", String.valueOf(ID));
				params.put("ak", Constant.REQUEST_AK);
				try {
					msg.what = Constant.STATUS_ID;
					msg.obj  = HttpUtils.Post(Constant.URL_DELETE_JURISDICTION, params);
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public static void getLoginRunnable(final Handler handler) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Message msg = new Message();	
				Map<String, String> params = new HashMap<String, String>();
				params.put("user_id", String.valueOf(MyApplication.getUserID()));
				params.put("ak", Constant.REQUEST_AK);
				try {
					msg.what = Constant.LOGIN_SERVER_ID;
					msg.obj  = HttpUtils.Post(Constant.URL_GET_LOGIN, params);
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public static void insertLoginRunnable(final Handler handler,final String name) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Message msg = new Message();	
				Map<String, String> params = new HashMap<String, String>();
				params.put("user_id", String.valueOf(MyApplication.getUserID()));
				params.put("name", name);
				params.put("ak", Constant.REQUEST_AK);
				try {
					msg.what = Constant.STATUS_ID;
					msg.obj  = HttpUtils.Post(Constant.URL_INSERT_LOGIN, params);
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public static void deleteLoginRunnable(final Handler handler,final int ID) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				Message msg = new Message();	
				Map<String, String> params = new HashMap<String, String>();
				params.put("ID", String.valueOf(ID));
				params.put("ak", Constant.REQUEST_AK);
				try {
					msg.what = Constant.STATUS_ID;
					msg.obj  = HttpUtils.Post(Constant.URL_DELETE_LOGIN, params);
					handler.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
}
