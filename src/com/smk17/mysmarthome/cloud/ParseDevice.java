package com.smk17.mysmarthome.cloud;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.smk17.mysmarthome.domain.DeviceCategory;
import com.smk17.mysmarthome.domain.DeviceProperty;
import com.smk17.mysmarthome.domain.DeviceSensor;
import com.smk17.mysmarthome.domain.DeviceZone;
import com.smk17.mysmarthome.domain.DeviceScene;
import com.smk17.mysmarthome.domain.SceneProperty;
import com.smk17.mysmarthome.domain.UserInfo;

public class ParseDevice {
	
	public static ArrayList<SceneProperty> parseSceneProperty(String data) throws JSONException {
		ArrayList<SceneProperty> list =new ArrayList<SceneProperty>();
		JSONArray jsonArray = new JSONObject(data).getJSONArray("property");
		for(int i = 0; i < jsonArray.length() ; i++){
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			Integer id = jsonObject.getInt("ID");
			Integer sceneId = jsonObject.getInt("sceneid");
			Integer contentId = jsonObject.getInt("contentid");
			Integer Status = jsonObject.getInt("status");
			String value = jsonObject.getString("value");
			list.add(new SceneProperty(id, sceneId, contentId, Status, value));
//			Log.e("DeviceProperty", jsonObject.toString() );
		}
		
		return list;
	}
	
	public static ArrayList<UserInfo> parseUserInfo(String data) throws JSONException {
		ArrayList<UserInfo> list =new ArrayList<UserInfo>();
		JSONArray jsonArray = new JSONObject(data).getJSONArray("user");
		for(int i = 0; i < jsonArray.length() ; i++){
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			Integer id = jsonObject.getInt("ID");
			String Name = jsonObject.getString("name");
			String Phone = jsonObject.getString("phone");
			String Email = jsonObject.getString("email");
			String Avatar = jsonObject.getString("avatar");
			list.add(new UserInfo(id, Name, Phone, Email,Avatar));
		}		
		return list;
	}
	
	public static ArrayList<DeviceScene> parseSceneControl(String data) {
		ArrayList<DeviceScene> list =new ArrayList<DeviceScene>();
		ArrayList<SceneProperty> pList;
		try {
			pList = parseSceneProperty(data);
			JSONArray jsonArray = new JSONObject(data).getJSONArray("scene");
			for(int i = 0; i < jsonArray.length() ; i++){
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				Integer id = jsonObject.getInt("ID");
				Integer val = jsonObject.getInt("value");
				String name = jsonObject.getString("name");
				Integer sensorid = jsonObject.getInt("sensorid");
				Integer icon = jsonObject.getInt("icon");
				Integer background = jsonObject.getInt("background");
				String remarks = jsonObject.getString("remarks");
				list.add(new DeviceScene(id, name, val, sensorid, icon, background, remarks, pList));
			}
		} catch (JSONException e) {
			
		}		
		return list;
	}
	
	public static ArrayList<DeviceCategory> parseDeviceCategory(String data) throws JSONException {
		ArrayList<DeviceCategory> list =new ArrayList<DeviceCategory>();
		JSONArray jsonArray = new JSONArray(data);
		for(int i = 0; i < jsonArray.length() ; i++){
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			Integer id = jsonObject.getInt("ID");
			String name = jsonObject.getString("name");
			list.add(new DeviceCategory(id, name));
		}
		return list;
	}
	
	
	public static ArrayList<DeviceZone> parseDeviceZone(String data) throws JSONException {
		ArrayList<DeviceZone> list =new ArrayList<DeviceZone>();
		JSONArray jsonArray = new JSONArray(data);
		for(int i = 0; i < jsonArray.length() ; i++){
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			Integer id = jsonObject.getInt("ID");
			Integer sensorid = jsonObject.getInt("sensorid");
			String name = jsonObject.getString("name");
			list.add(new DeviceZone(id, sensorid, name));
		}
		return list;
	}
	
	public static ArrayList<DeviceProperty> parseDeviceProperty(String data) throws JSONException {
		ArrayList<DeviceProperty> list =new ArrayList<DeviceProperty>();
		JSONArray jsonArray = new JSONObject(data).getJSONArray("property");
		for(int i = 0; i < jsonArray.length() ; i++){
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			Integer id = jsonObject.getInt("ID");
			Integer sensorid = jsonObject.getInt("sensorid");
			String name = jsonObject.getString("name");
			String value = jsonObject.getString("value");
			String type = jsonObject.getString("type");
			String unit = jsonObject.getString("unit");
			list.add(new DeviceProperty(id, name, value, type,unit, sensorid));
//			Log.e("DeviceProperty", jsonObject.toString() );
		}
		
		return list;
	}
	
	public static ArrayList<DeviceSensor> parseMainDevice(String data) throws JSONException {
		JSONObject Object =new JSONObject(data);
		JSONArray jsonArray =  Object.getJSONArray("device");
		return parseDeviceSensor(data,jsonArray);
	}
	
	public static ArrayList<DeviceSensor> parseTerminalDevice(String data) throws JSONException {
		JSONObject Object =new JSONObject(data);
		JSONArray jsonArray = null;
		jsonArray = Object.getJSONArray("sensor");
		return parseDeviceSensor(data,jsonArray);
	}
		
	public static ArrayList<DeviceSensor> parseMatchDevice(String data) throws JSONException {
		ArrayList<DeviceSensor> list =new ArrayList<DeviceSensor>();
		JSONObject Object =new JSONObject(data);
		JSONArray jsonArray = null;
		jsonArray = Object.getJSONArray("sensor");
		int len = jsonArray.length();
		Log.e("parseMatchDevice", "len:"+len);
		for(int i = 0; i < len ; i++){
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			Integer id = jsonObject.getInt("ID");
			Integer deviceid = jsonObject.getInt("device_id");
			Integer categoryid = jsonObject.getInt("category_id");
			Integer background = jsonObject.getInt("background");
			Integer zoneid = jsonObject.getInt("zone_id");
			String name = jsonObject.getString("name");
			String uid = jsonObject.getString("uuid");
			Log.e("parseMatchDevice", "add:ok");
			list.add(new DeviceSensor(id, deviceid, categoryid, zoneid, name, uid, background,null));
		}
		return list;
	}
	
	public static ArrayList<DeviceSensor> parseNewMainDevice(String data) throws JSONException {
		ArrayList<DeviceSensor> list =new ArrayList<DeviceSensor>();
		JSONObject Object =new JSONObject(data);
		JSONArray jsonArray = null;
		jsonArray = Object.getJSONArray("device");
		for(int i = 0; i < jsonArray.length() ; i++){
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			Integer id = jsonObject.getInt("ID");
			Integer deviceid = jsonObject.getInt("device_id");
			Integer categoryid = jsonObject.getInt("category_id");
			Integer background = jsonObject.getInt("background");
			Integer zoneid = jsonObject.getInt("zone_id");
			String name = jsonObject.getString("name");
			String uid = jsonObject.getString("uuid");
			list.add(new DeviceSensor(id, deviceid, categoryid, zoneid, name, uid,background,null));
		}
		return list;
	}
	
	public static int parseMainDeviceNumRows(String data) throws JSONException {
		JSONObject Object =new JSONObject(data);
		return Object.getInt("num_rows");
	}
	
	private static ArrayList<DeviceSensor> parseDeviceSensor(String data,JSONArray jsonArray) throws JSONException {
		ArrayList<DeviceSensor> list =new ArrayList<DeviceSensor>();
		ArrayList<DeviceProperty> pList = parseDeviceProperty(data);
		for(int i = 0; i < jsonArray.length() ; i++){
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			Integer id = jsonObject.getInt("ID");
			Integer deviceid = jsonObject.getInt("device_id");
			Integer categoryid = jsonObject.getInt("category_id");
			Integer background = jsonObject.getInt("background");
			Integer zoneid = jsonObject.getInt("zone_id");
			String name = jsonObject.getString("name");
			String uid = jsonObject.getString("uuid");
			list.add(new DeviceSensor(id, deviceid, categoryid, zoneid, name, uid, background,pList));
		}
		return list;
	}
	
	public static ArrayList<DeviceSensor> parseJurisdiction(String data) throws JSONException {
		ArrayList<DeviceSensor> list =new ArrayList<DeviceSensor>();
		JSONObject Object =new JSONObject(data);
		JSONArray jsonArray = Object.getJSONArray("jurisdiction");
		for(int i = 0; i < jsonArray.length() ; i++){
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			Integer id = jsonObject.getInt("ID");
			String name = jsonObject.getString("name");
			String uid = jsonObject.getString("phone");
			Integer deviceid = jsonObject.getInt("sensorid");
			list.add(new DeviceSensor(id, deviceid, 0, 0, name, uid, 0,null));
		}
		return list;
	}
	
	public static ArrayList<DeviceSensor> parseLogin(String data) throws JSONException {
		ArrayList<DeviceSensor> list =new ArrayList<DeviceSensor>();
		JSONObject Object =new JSONObject(data);
		JSONArray jsonArray = Object.getJSONArray("login");
		for(int i = 0; i < jsonArray.length() ; i++){
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			Integer id = jsonObject.getInt("ID");
			String name = jsonObject.getString("name");
			String uid = jsonObject.getString("time");
			list.add(new DeviceSensor(id, 0, 0, 0, name, uid, 0,null));
		}
		return list;
	}
}
