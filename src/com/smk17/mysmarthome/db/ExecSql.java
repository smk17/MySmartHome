package com.smk17.mysmarthome.db;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.j256.ormlite.dao.Dao;
import com.smk17.android.tools.ToolDatabase;
import com.smk17.android.tools.ToolString;
import com.smk17.mysmarthome.Constant;
import com.smk17.mysmarthome.MyApplication;
import com.smk17.mysmarthome.domain.DeviceCategory;
import com.smk17.mysmarthome.domain.DeviceProperty;
import com.smk17.mysmarthome.domain.DeviceScene;
import com.smk17.mysmarthome.domain.DeviceSensor;
import com.smk17.mysmarthome.domain.DeviceZone;
import com.smk17.mysmarthome.domain.SceneProperty;

@SuppressWarnings("unchecked")
public class ExecSql {

	private static Socket tcpCli = null;
	private static DataOutputStream dos = null;
	
	/**
	 * 更新设备信息，默认通知通知主控
	 * @param deviceSensor 待更新的设备类
	 */
	public static void updateDevice(final DeviceSensor deviceSensor) {
		updateDevice(deviceSensor, true);
	}
	
	/**
	 * 更新设备信息
	 * @param deviceSensor 待更新的设备类
	 * @param IsSendToTcp 是否通知主控，true 为通知
	 */
	public static void updateDevice(final DeviceSensor deviceSensor,boolean IsSendToTcp) {
		if(IsSendToTcp){
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					// TODO 发送信息到主控
					try {
						String uid = deviceSensor.getUid();
						String value = "DCF1DB";
						for (int i=0;i<deviceSensor.getpList().size();i++){
							if(deviceSensor.getpList().get(i).getName().equals("开关")){
								if(deviceSensor.getpList().get(i).getValue().equals("0")){
									value = "DCF0DB";
								}
							}
						}
						byte[] sBuffer = ToolString.hexStr2Bytes("DF01DE"+uid.substring(0, 16)+"DD"+uid.substring(16, 18)+value);
						if(tcpCli==null || !tcpCli.isConnected()){
							tcpCli = new Socket(Constant.SERVER_IP, Constant.SERVER_PORT);
						}
		        		if(dos==null){
		        			dos=new DataOutputStream(tcpCli.getOutputStream());
		        		}					
						dos.write(sBuffer);
						dos.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}				
		try {
			ToolDatabase dbHelper = ToolDatabase.gainInstance(Constant.DB_NAME, Constant.DB_VERSION);
			Dao<DeviceProperty, String> DevicePropertyDao = (Dao<DeviceProperty, String>)dbHelper.getDao(DeviceProperty.class);
			Dao<DeviceSensor, String> SensorDao = (Dao<DeviceSensor, String>)dbHelper.getDao(DeviceSensor.class);	
			SensorDao.update(deviceSensor);
			ArrayList<DeviceProperty> tmplist = new ArrayList<DeviceProperty>();
			tmplist.addAll(DevicePropertyDao.queryBuilder().where().eq("sensorId", deviceSensor.getId()).query());
			for (DeviceProperty deviceProperty : tmplist) {
				DevicePropertyDao.update(deviceProperty);
			}
			dbHelper.releaseAll();	
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}					
	}
	
	/**
	 * 更新设备信息
	 * @param tmplist 待更新的设备类列表
	 * @param IsSendToTcp 是否通知主控，true 为通知
	 */
	public static void updateDevice(final ArrayList<DeviceSensor> tmplist,final boolean IsSendToTcp) {
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					if(IsSendToTcp){
						// TODO 发送信息到主控
						if(tcpCli==null || !tcpCli.isConnected()){
							tcpCli = new Socket(Constant.SERVER_IP, Constant.SERVER_PORT);
						}
		        		if(dos==null){
		        			dos=new DataOutputStream(tcpCli.getOutputStream());
		        		}
		        		for (DeviceSensor deviceSensor : tmplist) {
		        			String uid = deviceSensor.getUid();
							String value = "DCF1DB";
							for (int i=0;i<deviceSensor.getpList().size();i++){
								if(deviceSensor.getpList().get(i).getName().equals("开关")){
									if(deviceSensor.getpList().get(i).getValue().equals("0")){
										value = "DCF0DB";
									}
								}
							}
							byte[] sBuffer = ToolString.hexStr2Bytes("DF01DE"+uid.substring(0, 16)+"DD"+uid.substring(16, 18)+value);
							Thread.sleep(Constant.TASKTIME);
							dos.write(sBuffer);
							dos.flush();
						}
					}						
	        		ToolDatabase dbHelper = ToolDatabase.gainInstance(Constant.DB_NAME, Constant.DB_VERSION);
	    			Dao<DeviceProperty, String> DevicePropertyDao = (Dao<DeviceProperty, String>)dbHelper.getDao(DeviceProperty.class);
	    			Dao<DeviceSensor, String> SensorDao = (Dao<DeviceSensor, String>)dbHelper.getDao(DeviceSensor.class);	
	    			ArrayList<DeviceProperty> tlist = new ArrayList<DeviceProperty>();
	    			for (DeviceSensor deviceSensor : tmplist) {
	    				SensorDao.update(deviceSensor);
	    				tlist.clear();
	    				tlist.addAll(DevicePropertyDao.queryBuilder().where().eq("sensorId", deviceSensor.getId()).query());
	    				for (DeviceProperty deviceProperty : tlist) {
	    					DevicePropertyDao.update(deviceProperty);
	    				}
	    			}			
	    			dbHelper.releaseAll();	
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();				
	}
	
	/**
	 * 删除设备
	 * @param deviceSensor 待删除的设备类
	 */
	public static void deleteDevice(final DeviceSensor deviceSensor,final boolean IsSendToTcp) {		
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					if(IsSendToTcp){
						// TODO 发送信息到主控
						if(tcpCli==null || !tcpCli.isConnected()){
							tcpCli = new Socket(Constant.SERVER_IP, Constant.SERVER_PORT);
						}
		        		if(dos==null){
		        			dos=new DataOutputStream(tcpCli.getOutputStream());
		        		}
		        		String uid = deviceSensor.getUid();
						byte[] sBuffer = ToolString.hexStr2Bytes("CF01CE"+uid.substring(0, 16)+"CD");
						Thread.sleep(Constant.TASKTIME);
						dos.write(sBuffer);
						dos.flush();
					}						
					ToolDatabase dbHelper = ToolDatabase.gainInstance(Constant.DB_NAME, Constant.DB_VERSION);
					Dao<DeviceProperty, String> DevicePropertyDao = (Dao<DeviceProperty, String>)dbHelper.getDao(DeviceProperty.class);
					Dao<DeviceSensor, String> SensorDao = (Dao<DeviceSensor, String>)dbHelper.getDao(DeviceSensor.class);	
					ArrayList<DeviceProperty> tmplist = new ArrayList<DeviceProperty>();
					tmplist.addAll(DevicePropertyDao.queryBuilder().where().eq("sensorId", deviceSensor.getId()).query());
					DevicePropertyDao.delete(tmplist);
					
					SensorDao.delete(deviceSensor);
					dbHelper.releaseAll();	
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();					
	}
	
	/**
	 * 关闭tcp
	 */
	public static void CloseSocket(){
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					if(tcpCli.isConnected()){
						dos.close();
						tcpCli.close();
					}					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}		
			}
		}).start();
		
	}
	
	/**
	 * 从数据库中获取区域列表
	 * @return
	 */
	public static ArrayList<DeviceZone> getZone(){
		ArrayList<DeviceZone> arrayList = new ArrayList<DeviceZone>();
		try {
			ToolDatabase dbHelper = ToolDatabase.gainInstance(Constant.DB_NAME, Constant.DB_VERSION);
			Dao<DeviceZone, String> ZoneDao = (Dao<DeviceZone, String>)dbHelper.getDao(DeviceZone.class);           
            List<DeviceZone> list = ZoneDao.queryBuilder().where().eq("sensorId", MyApplication.getMainDeviceId()).query();            
            arrayList.addAll(list);
            dbHelper.releaseAll();
		} catch (SQLException e) {
			arrayList.add(new DeviceZone(0, 0, ""));
		}
		return arrayList;
	}
	
	/**
	 * 更新情景模式
	 * @param scene
	 * @param IsSendToTcp 此次更新需要向主控发送吗？true为需要
	 */
	public static void UpdateScene(final DeviceScene scene,boolean IsSendToTcp){
		if(IsSendToTcp){
			new Thread(new Runnable() {
				
				@Override
				public void run() {
					
				}
			}).start();
		}
		try {	
			ArrayList<SceneProperty> tmpList = new ArrayList<SceneProperty>();
			ToolDatabase dbHelper = ToolDatabase.gainInstance(Constant.DB_NAME, Constant.DB_VERSION);
			Dao<DeviceScene, String> SceneDao = (Dao<DeviceScene, String>)dbHelper.getDao(DeviceScene.class);
			Dao<SceneProperty, String> ScenePropertyDao = (Dao<SceneProperty, String>)dbHelper.getDao(SceneProperty.class);
			SceneDao.update(scene);
			tmpList.addAll(ScenePropertyDao.queryBuilder().where().eq("ID", scene.getID()).query());
			for (SceneProperty sceneProperty : tmpList) {
				ScenePropertyDao.update(sceneProperty);
			}
			dbHelper.releaseAll();		
		} catch (SQLException e) {
		}
	}
	
	/**
	 * 从数据库中获取情景模式列表
	 * @return
	 */
	public static ArrayList<DeviceScene> getScene() {
		ArrayList<DeviceScene> tmpList = new ArrayList<DeviceScene>();
		try {			
			ToolDatabase dbHelper = ToolDatabase.gainInstance(Constant.DB_NAME, Constant.DB_VERSION);
			Dao<DeviceScene, String> SceneDao = (Dao<DeviceScene, String>)dbHelper.getDao(DeviceScene.class);
			Dao<SceneProperty, String> ScenePropertyDao = (Dao<SceneProperty, String>)dbHelper.getDao(SceneProperty.class);
			tmpList.addAll(SceneDao.queryBuilder().where().eq("sensorId", MyApplication.getMainDeviceId()).query());
			int len = tmpList.size();
            if(len>0){
            	ArrayList<SceneProperty> plist = new ArrayList<SceneProperty>();			            	
            	for(int i = 0; i< len ; i++){
            		plist.clear();
            		plist.addAll(ScenePropertyDao.queryBuilder().where().eq("sceneId", tmpList.get(i).getID()).query());
            		tmpList.get(i).setPList(plist);
            	}
            }
			dbHelper.releaseAll();
		} catch (SQLException e) {
		}
		return tmpList;
	}
	
	/**
	 * 从数据库中获取某一情景模式
	 * @param id 要获取的情景模式的ID
	 * @return
	 */
	public static ArrayList<DeviceScene> getScene(int id) {
		ArrayList<DeviceScene> tmpList = new ArrayList<DeviceScene>();
		try {			
			ToolDatabase dbHelper = ToolDatabase.gainInstance(Constant.DB_NAME, Constant.DB_VERSION);
			Dao<DeviceScene, String> SceneDao = (Dao<DeviceScene, String>)dbHelper.getDao(DeviceScene.class);
			Dao<SceneProperty, String> ScenePropertyDao = (Dao<SceneProperty, String>)dbHelper.getDao(SceneProperty.class);
			tmpList.addAll(SceneDao.queryBuilder().where().eq("ID", id).query());
			int len = tmpList.size();
            if(len>0){
            	ArrayList<SceneProperty> plist = new ArrayList<SceneProperty>();			            	
            	for(int i = 0; i< len ; i++){
            		plist.clear();
            		plist.addAll(ScenePropertyDao.queryBuilder().where().eq("sceneId", tmpList.get(i).getID()).query());
            		tmpList.get(i).setPList(plist);
            	}
            }
			dbHelper.releaseAll();
		} catch (SQLException e) {
		}
		return tmpList;
	}
	
	/**
	 * 新建一个情景模式
	 * @param mScene 要新建的情景模式类
	 */
	public static void AddScene(DeviceScene mScene) {
		try {
			ToolDatabase dbHelper = ToolDatabase.gainInstance(Constant.DB_NAME, Constant.DB_VERSION);
			Dao<DeviceScene, String> SceneDao = (Dao<DeviceScene, String>)dbHelper.getDao(DeviceScene.class);
			Dao<SceneProperty, String> ScenePropertyDao = (Dao<SceneProperty, String>)dbHelper.getDao(SceneProperty.class);
			ArrayList<DeviceScene> slist = new ArrayList<DeviceScene>();
			ArrayList<SceneProperty> list = mScene.getList();
			SceneDao.create(mScene);
			slist.addAll(SceneDao.queryForMatchingArgs(mScene));
			if(slist.size()>0){
				int id = slist.get(0).getID();
				for (SceneProperty sceneProperty : list) {
					sceneProperty.setId(null);
					sceneProperty.setsceneId(id);
					ScenePropertyDao.create(sceneProperty);
				}
			}			
			dbHelper.releaseAll();
		} catch (SQLException e) {
		}		
	}

	/**
	 * 更新某一情景模式
	 * @param mScene
	 */
	public static void UpdateScene(DeviceScene mScene) {
		try {
			ToolDatabase dbHelper = ToolDatabase.gainInstance(Constant.DB_NAME, Constant.DB_VERSION);
			Dao<DeviceScene, String> SceneDao = (Dao<DeviceScene, String>)dbHelper.getDao(DeviceScene.class);
			Dao<SceneProperty, String> ScenePropertyDao = (Dao<SceneProperty, String>)dbHelper.getDao(SceneProperty.class);
			ArrayList<SceneProperty> list = new ArrayList<SceneProperty>();
			list.addAll(ScenePropertyDao.queryBuilder().where().eq("sceneId", mScene.getID()).query());
			for (SceneProperty sceneProperty : list) {
				ScenePropertyDao.update(sceneProperty);
			}
			SceneDao.update(mScene);
			dbHelper.releaseAll();
		} catch (SQLException e) {
		}				
	}
	
	/**
	 * 删除情景模式
	 * @param scene
	 */
	public static void DeleteScene(final DeviceScene scene) {
		try {			
			ToolDatabase dbHelper = ToolDatabase.gainInstance(Constant.DB_NAME, Constant.DB_VERSION);
			Dao<DeviceScene, String> SceneDao = (Dao<DeviceScene, String>)dbHelper.getDao(DeviceScene.class);
			Dao<SceneProperty, String> ScenePropertyDao = (Dao<SceneProperty, String>)dbHelper.getDao(SceneProperty.class);
						
			ArrayList<SceneProperty> plist = new ArrayList<SceneProperty>();
    		plist.addAll(ScenePropertyDao.queryBuilder().where().eq("sceneId",scene.getID()).query());
    		ScenePropertyDao.delete(plist);    		
            SceneDao.delete(scene);
			dbHelper.releaseAll();
		} catch (SQLException e) {
		}
	}
	
	/**
	 * 从数据库中获取主控设备列表
	 * @return
	 */
	public static ArrayList<DeviceSensor> getMainDevice() {
		ArrayList<DeviceSensor> tmpList =new ArrayList<DeviceSensor>();
		try {
			ToolDatabase dbHelper = ToolDatabase.gainInstance(Constant.DB_NAME, Constant.DB_VERSION);
			Dao<DeviceProperty, String> DevicePropertyDao = (Dao<DeviceProperty, String>)dbHelper.getDao(DeviceProperty.class);
			Dao<DeviceSensor, String> SensorDao = (Dao<DeviceSensor, String>)dbHelper.getDao(DeviceSensor.class);
            
            ArrayList<DeviceProperty>plist = new ArrayList<DeviceProperty>();
            plist.addAll(DevicePropertyDao.queryForAll());
			List<DeviceSensor> list = SensorDao.queryBuilder().where().eq("categoryId", 1)
					.and().eq("deviceId", MyApplication.getMainDeviceId()).query();
			for (DeviceSensor deviceSensor : list) {
				deviceSensor.setList(plist);
				tmpList.add(deviceSensor);
			}
			dbHelper.releaseAll();
		} catch (SQLException e) {
		}
		return tmpList;
	}
	
	/**
	 * 从数据库中获取终端设备列表
	 * @return
	 */
	public static ArrayList<DeviceSensor> getDevice() {
		ArrayList<DeviceSensor> tmpList =new ArrayList<DeviceSensor>();
		try {
			ToolDatabase dbHelper = ToolDatabase.gainInstance(Constant.DB_NAME, Constant.DB_VERSION);
			Dao<DeviceProperty, String> DevicePropertyDao = (Dao<DeviceProperty, String>)dbHelper.getDao(DeviceProperty.class);
			Dao<DeviceSensor, String> SensorDao = (Dao<DeviceSensor, String>)dbHelper.getDao(DeviceSensor.class);
            
            ArrayList<DeviceProperty>plist = new ArrayList<DeviceProperty>();
            plist.addAll(DevicePropertyDao.queryForAll());
			List<DeviceSensor> list = SensorDao.queryBuilder().where().ne("categoryId", 1)
					.and().eq("deviceId", MyApplication.getMainDeviceId()).query();
			for (DeviceSensor deviceSensor : list) {
				deviceSensor.setList(plist);
				tmpList.add(deviceSensor);
			}
			dbHelper.releaseAll();
		} catch (SQLException e) {
		}
		return tmpList;
	}
	
	/**
	 * 解析主控发送的信息并更新到数据库
	 * @param data 主控发送的信息
	 * @param deviceid 当前主控id
	 * @return 一个完整的设备列表
	 * @throws JSONException SQLException
	 */	
	public static ArrayList<DeviceSensor> getDevice(String data) throws Exception {
		ToolDatabase dbHelper = ToolDatabase.gainInstance(Constant.DB_NAME, Constant.DB_VERSION);
		Dao<DeviceProperty, String> DevicePropertyDao = (Dao<DeviceProperty, String>)dbHelper.getDao(DeviceProperty.class);
		Dao<DeviceSensor, String> SensorDao = (Dao<DeviceSensor, String>)dbHelper.getDao(DeviceSensor.class);	
		Dao<DeviceZone, String> ZoneDao = (Dao<DeviceZone, String>)dbHelper.getDao(DeviceZone.class);
		ArrayList<DeviceSensor> list =new ArrayList<DeviceSensor>();
		ArrayList<DeviceProperty>plist = new ArrayList<DeviceProperty>();
        plist.addAll(DevicePropertyDao.queryForAll());
		JSONObject Object =new JSONObject(data);
		String type = Object.getString("type");
		if(type.equals("info")){
			JSONArray jsonArray = Object.getJSONArray("msg");
			int len = jsonArray.length();
			for(int i = 0; i < len ; i++){
				JSONObject jsonObject = jsonArray.getJSONObject(i);
//				Integer id = jsonObject.getInt("id");
				String uuid = jsonObject.getString("uuid");
				Integer value = jsonObject.getInt("value");
				DeviceSensor deviceSensor = new DeviceSensor(null, MyApplication.getMainDeviceId(), Integer.valueOf(uuid.substring(3, 4)), 1, "未命名", uuid, 0, plist);
				List<DeviceSensor> dlist = SensorDao.queryBuilder().where().eq("Uid", uuid).query();
				if(dlist.size() == 1){
					deviceSensor = dlist.get(0);
					deviceSensor.setList(plist);
					deviceSensor.setAllPropertyValue(value);
					SensorDao.update(deviceSensor);
					ArrayList<DeviceProperty> tplist = deviceSensor.getpList();
					for (DeviceProperty deviceProperty : tplist) {
						DevicePropertyDao.update(deviceProperty);
					}
				}else if(dlist.size() == 0){
					List<DeviceZone> zlist = ZoneDao.queryBuilder().where().eq("sensorId", MyApplication.getMainDeviceId()).query();
					if(zlist.size()>1){
						deviceSensor.setZoneId(zlist.get(0).getId());
					}
					SensorDao.create(deviceSensor);
					dlist = SensorDao.queryBuilder().where().eq("Uid", uuid).query();
					deviceSensor = dlist.get(0);
					switch (deviceSensor.getCategoryId()) {
					case 5:
						DevicePropertyDao.create(new DeviceProperty(null, "开关", String.valueOf(value), "Boolean", "", deviceSensor.getId()));
						break;

					default:
						break;
					}
				}				
				list.add(deviceSensor);
			}
		}
		dbHelper.releaseAll();
		return list;
	}
	
	/**
	 * 获取某一设备信息
	 * @param uid 该设备信息的UUID
	 * @return
	 */
	public static ArrayList<DeviceSensor> getContainsDevice(String uid) {
		ArrayList<DeviceSensor> tmpList =new ArrayList<DeviceSensor>();
		try {
			ToolDatabase dbHelper = ToolDatabase.gainInstance(Constant.DB_NAME, Constant.DB_VERSION);
			Dao<DeviceProperty, String> DevicePropertyDao = (Dao<DeviceProperty, String>)dbHelper.getDao(DeviceProperty.class);
			Dao<DeviceSensor, String> SensorDao = (Dao<DeviceSensor, String>)dbHelper.getDao(DeviceSensor.class);
            
			List<DeviceSensor> list = SensorDao.queryBuilder().where().like("Uid", "%"+uid+"%").query();
			if(list.size() > 0){
				for (DeviceSensor deviceSensor : list) {
					deviceSensor.setDeviceId(MyApplication.getMainDeviceId());
					SensorDao.update(deviceSensor);
				}
			}else{
				 int CategoryId = Integer.valueOf(uid.substring(3, 4));
				 int LargeOnes = Integer.valueOf(uid.substring(uid.length() - 1));
				 switch (CategoryId) {
				case 5:
					for(int i = 1; i <= LargeOnes ;i++){
						String uuid =  uid+"0"+i;
						DeviceSensor deviceSensor = new DeviceSensor(null, MyApplication.getMainDeviceId(), CategoryId, 1, 
								LargeOnes+"位开关"+i+"路",uuid, 0, null);
						SensorDao.create(deviceSensor);
						
						List<DeviceSensor> dlist = SensorDao.queryBuilder().where().eq("Uid", uuid).query();
						if(dlist.size() > 0){
							DevicePropertyDao.create(new DeviceProperty(null, "开关", "0", "Boolean", "", dlist.get(0).getId()));
						}						
					}
					break;

				default:
					break;
				}
			}
			
			dbHelper.releaseAll();
		} catch (SQLException e) {
		}
		return tmpList;
	}
	
	/**
	 * 添加一个区域
	 * @param dZone
	 */
	public static void AddZone(DeviceZone dZone) {		
		try {
			ToolDatabase dbHelper = ToolDatabase.gainInstance(Constant.DB_NAME, Constant.DB_VERSION);			
			Dao<DeviceZone, String> ZoneDao = (Dao<DeviceZone, String>)dbHelper.getDao(DeviceZone.class);			
			ZoneDao.create(dZone);
			dbHelper.releaseAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}           
        
	}

	/**
	 * 更新某一区域
	 * @param device
	 */
	public static void UpdateZoneName(DeviceZone device) {
		try {
			ToolDatabase dbHelper = ToolDatabase.gainInstance(Constant.DB_NAME, Constant.DB_VERSION);
			Dao<DeviceZone, String> ZoneDao = (Dao<DeviceZone, String>)dbHelper.getDao(DeviceZone.class);
			ZoneDao.update(device);
			dbHelper.releaseAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}           
	}

	/**
	 * 删除某一区域
	 * @param device 要删除的区域类
	 * @param zoneid 该区域下的设备将要移动到的区域id
	 */
	public static void DeleteZone(DeviceZone device, int zoneid) {
		try {			
			ToolDatabase dbHelper = ToolDatabase.gainInstance(Constant.DB_NAME, Constant.DB_VERSION);
			Dao<DeviceSensor, String> SensorDao = (Dao<DeviceSensor, String>)dbHelper.getDao(DeviceSensor.class);
			Dao<DeviceZone, String> ZoneDao = (Dao<DeviceZone, String>)dbHelper.getDao(DeviceZone.class);
			List<DeviceSensor> list = SensorDao.queryBuilder().where().eq("zoneId", device.getId()).query();
			for (DeviceSensor deviceSensor : list) {
				deviceSensor.setZoneId(zoneid);
				SensorDao.update(deviceSensor);
			}
			ZoneDao.delete(device);
			dbHelper.releaseAll();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}        
	}

	/**
	 * 从数据库中获取设备类型列表
	 * @return
	 */
	public static ArrayList<DeviceCategory> getCategory() {
		ArrayList<DeviceCategory> arrayList = new ArrayList<DeviceCategory>();
		try {
			ToolDatabase dbHelper = ToolDatabase.gainInstance(Constant.DB_NAME, Constant.DB_VERSION);
			Dao<DeviceCategory, String> CategoryDao = (Dao<DeviceCategory, String>)dbHelper.getDao(DeviceCategory.class);           
            List<DeviceCategory> list = CategoryDao.queryBuilder().where().ne("Id", "1").query();
            arrayList.addAll(list);
            dbHelper.releaseAll();
		} catch (SQLException e) {
		}
		return arrayList;
	}
}
