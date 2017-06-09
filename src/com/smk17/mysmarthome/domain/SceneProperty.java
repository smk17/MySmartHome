package com.smk17.mysmarthome.domain;

import com.j256.ormlite.field.DatabaseField;
/**
 * …Ë±∏ Ù–‘
 * @author Seng
 *
 */
public class SceneProperty {

	@DatabaseField(id=true)
	private Integer Id = 0;
	@DatabaseField(defaultValue="0")
	private int sceneId = 0;
	@DatabaseField(defaultValue="0")
	private int contentId = 0;
	@DatabaseField(defaultValue="0")
	private int Status = 0;
	@DatabaseField(defaultValue="")
	private String Value = "";
	
	public SceneProperty(){}
	
	public SceneProperty(int id,int sceneId,int contentId,int Status,String value){
		this.Id=id;
		this.sceneId=sceneId;
		this.Value=value;
		this.contentId=contentId;
		this.Status=Status;
	}
	
	public int getId(){
		return this.Id;
	}	
	public void setId(Integer id){
		this.Id = id;
	}	
	public int getsceneId(){
		return this.sceneId;
	}	
	public void setsceneId(int id){
		this.sceneId = id;
	}	
	public int getcontentId(){
		return this.contentId;
	}	
	public int getStatus(){
		return this.Status;
	}	
	public String getValue(){
		return this.Value;
	}
	public void setValue(String value){
		this.Value=value;
	}
	
}
