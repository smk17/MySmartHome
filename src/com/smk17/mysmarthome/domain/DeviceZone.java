package com.smk17.mysmarthome.domain;

import com.j256.ormlite.field.DatabaseField;

/**
 * 设备区域控制类
 * @author Seng
 *
 */
public class DeviceZone {
	
	@DatabaseField(id=true)
	private Integer id = 0;
	@DatabaseField(defaultValue="0")
	private int sensorId = 0;
	@DatabaseField(defaultValue="")
	private String Name = "";
	
	public DeviceZone(){}
	public DeviceZone(Integer id,int sensorid,String name){
		this.id = id;
		this.sensorId = sensorid;
		this.Name = name;
	}
	
	public int getId(){
		return this.id;
	}
	public int getSensorId(){
		return this.sensorId;
	}
	public String getName(){
		return this.Name;
	}
	public void setName(String name){
		this.Name = name;
	}
	public void setSensorId(int sensorid){
		this.sensorId = sensorid;
	}
}
