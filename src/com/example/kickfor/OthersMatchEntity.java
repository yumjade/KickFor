package com.example.kickfor;

import java.io.Serializable;

public class OthersMatchEntity implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3L;
	private String name=null;
	private String number=null;
	private String goal=null;
	private String assist=null;
	
	public OthersMatchEntity(String name, String number, String goal, String assist){
		this.name=name;
		this.number=number;
		this.goal=goal;
		this.assist=assist;
	}
	
	public String getName(){
		return name;
	}
	
	public String getGoal(){
		return goal;
	}
	
	public String getAssist(){
		return assist;
	}
	
	public String getNumber(){
		return number;
	}
}
