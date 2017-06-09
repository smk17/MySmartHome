package com.smk17.mysmarthome.domain;

import com.j256.ormlite.field.DatabaseField;

/**
 * 设备类型
 * @author Seng
 *
 */
public class DeviceCategory {
	//[start]设备类型全局常量
	/**
	 * 主控
	 */
	public static final int TYPE_Master	= 1;
	/**
	 * 插线板
	 */
	public static final int TYPE_Strips	= 2;
	/**
	 * 插座
	 */
	public static final int TYPE_Socket 	= 3;
	/**
	 * 可调光灯
	 */
	public static final int TYPE_DimmableLights  = 4;
	/**
	 * 开关
	 */
	public static final int TYPE_Switch = 5;
	//[end]
	
	@DatabaseField(id=true)
	private Integer Id = 0;
	@DatabaseField(defaultValue="")
	private String Name = "";
	
	
	public DeviceCategory(){}
	public DeviceCategory(Integer id,String name){
		this.Id = id;
		this.Name = name;
	}
	
	public int getId(){
		return this.Id;
	}
	public String getName(){
		return this.Name;
	}
}
