package com.example.kickfor;

import java.io.Serializable;

public class SkillsSelectEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 45L;
	
	private String skillkey=null;
	private String skillname=null;
	private boolean isAdd=false;
	
	public SkillsSelectEntity(String skillkey, String skillname, String isAdd){
		this.skillkey=skillkey;
		this.skillname=skillname;
		this.isAdd=isAdd.equals("1")? true: false;
	}
	
	public boolean isAdd(){
		return isAdd;
	}

	public String getSkillKey(){
		return skillkey;
	}
	
	public String getSkillName(){
		return skillname;
	}
	
	public void setAddState(boolean state){
		isAdd=state;
	}
	
}
