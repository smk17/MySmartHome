package com.smk17.mysmarthome.domain;

public class UserInfo {

	private Integer ID = 0;
	private String Name = "";
	private String Phone = "";
	private String Email = "";
	private String Avatar = "";
	
	public UserInfo(){}
	public UserInfo(int id,String name,String phone,String email,String avatar){
		this.ID = id;
		this.Name = name;
		this.Email = email;
		this.Phone = phone;
		this.Avatar = avatar;
	}
	
	public int getID(){
		if(this.ID ==null){
			return 0;
		}
		return this.ID;
	}
	
	public String getName(){
		return this.Name;
	}
	
	public String getPhone(){
		return this.Phone;
	}
	
	public String getEmail(){
		return this.Email;
	}
	
	public String getAvatar(){
		return this.Avatar;
	}
}
