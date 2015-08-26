package com.example.kickfor;

import java.io.Serializable;

public class SkillsShowEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 51L;
	private String name=null;
	private String skillskey=null;
	private String userskillskey=null;
	private boolean isAgree=false;
	private String agreeNum=null;
	
	
	public SkillsShowEntity(String userskillskey, String skillskey, String name, String isAgree, String agreeNum){
		this.userskillskey=userskillskey;
		this.skillskey=skillskey;
		this.name=name;
		this.isAgree=isAgree.equals("1")? true: false;
		this.agreeNum=agreeNum;
	}
	
	public String getUserSkillskey(){
		return userskillskey;
	}
	
	public String getSkillsKey(){
		return skillskey;
	}
	
	public String getSkillsName(){
		return name;
	}
	
	public String getAgreeNum(){
		return agreeNum;
	}
	
	public boolean isAgree(){
		return isAgree;
	}
	
}
