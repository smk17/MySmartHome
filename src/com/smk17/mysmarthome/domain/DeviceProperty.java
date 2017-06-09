package com.smk17.mysmarthome.domain;

import com.j256.ormlite.field.DatabaseField;


/**
 * 设备属性
 * @author Seng
 *
 */
public class DeviceProperty {

	@DatabaseField(id=true)
	private Integer Id = 0;
	@DatabaseField(defaultValue="0")
	private int sensorId = 0;
	@DatabaseField(defaultValue="")
	private String Name = "";
	@DatabaseField(defaultValue="")
	private String Value = "";
	@DatabaseField(defaultValue="")
	private String Type = "";
	@DatabaseField(defaultValue="")
	private String Unit = "";
	
	
	public DeviceProperty(){}
	public DeviceProperty(Integer id,String name,String value,String type,String unit, int sensorId){
		this.Id=id;
		this.Name=name;
		this.Value=value;
		this.Type=type;
		this.Unit=unit;
		this.sensorId=sensorId;
	}
	
	public int getId(){
//		if(this.Id == null){
//			return 0;
//		}
		return this.Id;
	}
	
	public String getName(){
		return this.Name;
	}
	public String getValue(){
		return this.Value;
	}
	public void setValue(String value){
		this.Value=value;
	}
	public String getUnit(){
		return this.Unit;
	}
	public int getSensorId(){
		return this.sensorId;
	}
	public String getType(){
		return this.Type;
	}
	
	@Override
	public String toString() {		
		if(Type.equals("Boolean")){
			if(Value.equals("1")){
				return getName()+":"+"开"+getUnit();
			}else if(Value.equals("0")){
				return getName()+":"+"关"+getUnit();
			}
		}
		return getName()+":"+getValue()+getUnit();
	}
}
