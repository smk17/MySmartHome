package com.smk17.mysmarthome.domain;

import java.util.ArrayList;

import com.j256.ormlite.field.DatabaseField;
import com.smk17.mysmarthome.Constant;


/**
 * 设备主体
 * @author Seng
 *
 */
public class DeviceSensor {
	
	@DatabaseField(id=true)
	private Integer id = 0;
	@DatabaseField(defaultValue="0")
	private int deviceId = 0;
	@DatabaseField(defaultValue="0")
	private int categoryId = 0;
	@DatabaseField(defaultValue="0")
	private int zoneId = 0;
	@DatabaseField(defaultValue="0")
	private int Background = 0;
	private boolean isCheck = false;
	private boolean isSelectMode = false;
	private boolean IsRefreshable = false;
	private ArrayList<DeviceProperty> pList = new ArrayList<DeviceProperty>();
	@DatabaseField(defaultValue="0")
	private String Name = "";
	@DatabaseField(defaultValue="0",unique=true)
	private String Uid = "";
	private long timeMillis = 0;
	public int count = 0;
	
	public DeviceSensor(){}
	public DeviceSensor(Integer id,int deviceid,int categoryid, int zoneid,String name,String uid,int background,ArrayList<DeviceProperty> list){
		pList = new ArrayList<DeviceProperty>();
		this.id=id;
		this.categoryId=categoryid;
		this.deviceId=deviceid;
		this.zoneId=zoneid;
		this.Name=name;
		this.Uid=uid;
		this.Background = background;
		if(list !=null&&id!=null){
			for (DeviceProperty deviceProperty : list) {
				if(deviceProperty.getSensorId() == id){
					pList.add(deviceProperty);
				}
			}
		}
	}
	
	public boolean getIsRefreshable(){
		return this.IsRefreshable;
	}
	public void setIsRefreshable(boolean IsRefreshable){
		this.IsRefreshable = IsRefreshable;
	}
	
	public long getTimeMillis(){
		return this.timeMillis;
	}
	public void setTimeMillis(){
		this.timeMillis = System.currentTimeMillis();
	}
	public void setTimeMillis(long timeMillis){
		this.timeMillis = timeMillis;
	}
	
	public ArrayList<DeviceProperty> getpList(){
		return this.pList;
	}
//	public void setPList( ArrayList<DeviceProperty> list){
//		this.pList = list ;
//	}
	public boolean setPropertyList( ArrayList<SceneProperty> list){
		ArrayList<DeviceProperty> tplist = new ArrayList<DeviceProperty>();
		if(list.size() > 0){
			for (DeviceProperty deviceProperty : pList) {
				for (SceneProperty sceneProperty : list) {
					if(deviceProperty.getId() == sceneProperty.getcontentId()){
						deviceProperty.setValue(sceneProperty.getValue());
						tplist.add(deviceProperty);
					}						
				}				
			}
			if(tplist.size() > 0){
				return true;
			}
		}		
		return false;
	}
	public boolean getSelectMode(){
		return this.isSelectMode;
	}
	public void setSelectMode(boolean i){
		this.isSelectMode = i;
	}

	public boolean getCheck(){
		return this.isCheck;
	}
	public void setCheck(boolean i){
		this.isCheck = i;
	}
	public Integer getId(){
		return this.id;
	}
	public String getName(){
		return this.Name;
	}
	public void setName(String name){
		this.Name=name;
	}
	public String getUid(){
		return this.Uid;
	}
	public int getCategoryId(){
		return this.categoryId;
	}
	public int getDeviceId(){
		return this.deviceId;
	}
	public int getZoneId(){
		return this.zoneId;
	}
	public void setZoneId(int zoneid){
		this.zoneId = zoneid;
	}
	
	public void setDeviceId(int deviceId){
		this.deviceId = deviceId;
	}
	
	public boolean presence(int contentId){
		if(pList.size() > 0){
			for (DeviceProperty deviceProperty : pList) {
				if(deviceProperty.getId() == contentId)
					return true;
			}
		}
		return false;
	}
	
	public boolean equals(int deviceid,int categoryid,int zoneid){
		if(deviceid == Constant.ID_MAINDEVICE_ALL || deviceid == this.deviceId){
			if(categoryid == Constant.ID_CATEGORY_ALL || categoryid == this.categoryId){
				if(zoneid == Constant.ID_ZONE_ALL || zoneid == this.zoneId ){
					return true;
				}
			}
		}
		return false;		
	}
	
	public String  pListToString(){
		int i = 0;
		String reason = "";
		String more = "";
		int len = this.getpList().size();
		if(len > 3){
			len = 3;
			more = "...";
		}
		for (i=0;i<len;i++){			
			reason += this.getpList().get(i).toString();
			if(i!=len-1) reason += ";";
		}
		return reason + more ;
	}
	public boolean equals(ArrayList<DeviceProperty> pList,
			ArrayList<DeviceProperty> getpList) {
		for (DeviceProperty deviceProperty : getpList) {
			for (DeviceProperty dProperty : pList) {
				if(dProperty.getId() == deviceProperty.getId()){
					if(!dProperty.getValue().equals(deviceProperty.getValue())){
						return false;
					}
				}
			}
		}
		return true;
	}
	public int getBackground() {
		return this.Background;
	}
	public void setList(ArrayList<DeviceProperty> list) {
		pList.clear();
		if(list !=null&&id!=null){
			for (DeviceProperty deviceProperty : list) {
				if(deviceProperty.getSensorId() == id){
					pList.add(deviceProperty);
				}
			}
		}
	}
	public void setAllPropertyValue(Integer value) {
		if(pList!=null&&id!=null&& pList.size()>0){
			ArrayList<DeviceProperty> list = new ArrayList<DeviceProperty>();
			list.addAll(pList);
			pList.clear();
			for (DeviceProperty deviceProperty : list) {
				if(deviceProperty.getType().equals("Boolean"))
					deviceProperty.setValue(String.valueOf(value));
				pList.add(deviceProperty);
			}
		}
	}

}
