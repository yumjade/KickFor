package com.example.kickfor.team;

import java.io.Serializable;

public class HonorInfo implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 33L;
	private String year="";
	private String name="";
	private String result="";
	private int id=0;
	
	private String updateYear="";
	private String updateName="";
	private String updateResult="";
	private boolean isChanged=false;
	
	public HonorInfo(int id){
		this.id=id;
	}
	
	public void setChanged(){
		isChanged=true;
	}
	
	public boolean getChanged(){
		return isChanged;
	}
	
	public void setUpdateYear(String updateYear){
		this.updateYear=updateYear;
	}
	
	public void setUpdateName(String updateName){
		this.updateName=updateName;
	}
	
	public void setUpdateResult(String updateResult){
		this.updateResult=updateResult;
	}
	
	public String getUpdateName(){
		return updateName;
	}
	
	public String getUpdateYear(){
		return updateYear;
	}
	
	public String getUpdateResult(){
		return updateResult;
	}
	public void setYear(String year){
		this.year=year;
	}
	
	public void setName(String name){
		this.name=name;
	}
	
	public void setResult(String result){
		this.result=result;
	}
	
	public String getYear(){
		return year;
	}
	
	public String getName(){
		return name;
	}
	
	public int getId(){
		return id;
	}
	
	public String getResult(){
		return result;
	}
}