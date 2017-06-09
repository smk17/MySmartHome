package com.smk17.mysmarthome.domain;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.j256.ormlite.field.DatabaseField;

public class DeviceScene {
	
	@DatabaseField(id=true)
	private Integer ID;
	@DatabaseField(unique=true,defaultValue="")
	private String name;
	@DatabaseField(defaultValue="0")
	private Integer value;
	@DatabaseField(defaultValue="0")
	private Integer sensorId;
	@DatabaseField(defaultValue="0")
	private Integer Icon;
	@DatabaseField(defaultValue="0")
	private Integer Background;
	@DatabaseField(defaultValue="")
	private String remarks;
	private ArrayList<SceneProperty> pList = new ArrayList<SceneProperty>();
	
	public DeviceScene(){}
	public DeviceScene(Integer id,String name,Integer val, Integer sensorid,Integer icon,Integer background,String remarks,ArrayList<SceneProperty> list){
		this.ID = id;
		this.name = name;
		this.value = val;
		this.sensorId = sensorid;
		this.Icon = icon;
		this.Background = background;
		this.remarks = remarks;
		if(list!=null){
			pList.clear();
			for (SceneProperty sceneProperty : list) {
				if(sceneProperty.getsceneId() == ID)
					pList.add(sceneProperty);
			}
		}
	}
	
	public void setPList(ArrayList<SceneProperty> list){
		if(ID!=null&&list!=null){
			pList.clear();
			for (SceneProperty sceneProperty : list) {
				if(sceneProperty.getsceneId() == ID)
					pList.add(sceneProperty);
			}
		}
	}

	public void setID(Integer ID){
		this.ID = ID;
	}
	public Integer getID(){
		return this.ID;
	}
	public Integer getIcon(){
		return this.Icon;
	}
	public Integer getBackground(){
		return this.Background;
	}
	public Integer getSensorId(){
		return this.sensorId;
	}
	public void setIcon(int position) {
		this.Icon = position;
	}

	public void setBackground(int position) {
		this.Background = position;
	}

	public void setName(String name){
		this.name = name;
	}
	public String getName(){
		return this.name;
	}
	public void setValue(Integer value){
		this.value = value;
	}
	public Integer getValue(){
		return this.value;
	}
	public void setRemarks(String remarks){
		this.remarks = remarks;
	}
	public String getRemarks(){
		return this.remarks;
	}
	
	public ArrayList<SceneProperty> getList(){
		return this.pList;
	}
	
	public String toJSON(){
		JSONObject jsonObject = new JSONObject();
		try {
			jsonObject.put("ID", this.ID);
			jsonObject.put("name", this.name);
			jsonObject.put("value", this.value);
			jsonObject.put("sensorid", this.sensorId);
			jsonObject.put("userid", 1);
			jsonObject.put("icon", this.Icon);
			jsonObject.put("background", this.Background);
			jsonObject.put("remarks", this.remarks);
			jsonObject.put("list", pListToJSONArray());
		} catch (JSONException e) {					
			e.printStackTrace();
			return "";
		}
		return jsonObject.toString();	
	}

	public JSONArray  pListToJSONArray(){
		JSONArray jsonArray = new JSONArray();		
		int len = pList.size();
		if(len > 0){
			for (SceneProperty sceneProperty : pList) {
				JSONObject jsonObject = new JSONObject();
				try {
					jsonObject.put("ID", sceneProperty.getId());
					jsonObject.put("sceneid", sceneProperty.getsceneId());
					jsonObject.put("status", sceneProperty.getStatus());
					jsonObject.put("contentid", sceneProperty.getcontentId());
					jsonObject.put("value", sceneProperty.getValue());
					jsonArray.put(jsonObject);
				} catch (JSONException e) {					
					e.printStackTrace();
					return null;
				}
			}
			return jsonArray;
		}		
		return null;
	}

	public void setPropertyList(ArrayList<DeviceSensor> dEditList) {
		if(dEditList.size() > 0){
			pList.clear();			
			int id = 0;
			if(ID != null){
				id = ID;
			}
			for (DeviceSensor deviceSensor : dEditList) {
				ArrayList<DeviceProperty> list = deviceSensor.getpList();
				for (DeviceProperty deviceProperty : list) {
					SceneProperty sceneProperty = new SceneProperty(0, id, deviceProperty.getId(), 1, deviceProperty.getValue());
					pList.add(sceneProperty);
				}				
			}
		}
	}

	
}
