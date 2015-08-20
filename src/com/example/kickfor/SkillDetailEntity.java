package com.example.kickfor;

import java.io.Serializable;

import android.graphics.Bitmap;

public class SkillDetailEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 42L;

	private String image=null;
	private String phone=null;
	private String team=null;
	private String position=null;
	private String name=null;
	
	public SkillDetailEntity(String phone, String name, String image, String team, String position){
		this.phone=phone;
		this.image=image;
		this.team=team;
		this.position=position;
		this.name=name;
	}
	
	public String getPhone(){
		return phone;
	}
	
	public String getTeam(){
		return team;
	}
	
	public Bitmap getImage(){
		return Tools.stringtoBitmap(image);
	}
	
	public String getPosition(){
		return position;
	}
	
	public String getName(){
		return name;
	}
	
}
