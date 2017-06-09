package com.smk17.mysmarthome.domain;

import com.j256.ormlite.field.DatabaseField;

/**
 * �豸����
 * @author Seng
 *
 */
public class DeviceCategory {
	//[start]�豸����ȫ�ֳ���
	/**
	 * ����
	 */
	public static final int TYPE_Master	= 1;
	/**
	 * ���߰�
	 */
	public static final int TYPE_Strips	= 2;
	/**
	 * ����
	 */
	public static final int TYPE_Socket 	= 3;
	/**
	 * �ɵ����
	 */
	public static final int TYPE_DimmableLights  = 4;
	/**
	 * ����
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
