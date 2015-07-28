package com.example.kickfor;

import java.util.Map;

import android.graphics.Bitmap;

public class MyFriend {

	private int index=0;
	private String result=null;
	private String type=null;
	private String phone=null;
	private String name=null;
	private Bitmap image=null;
	private String message=null;
	private String date=null;
	private String teamid=null;
	private int msgNumber=0;
	private String messageType=null;
	
	public MyFriend(Map<String, Object> map){
		if(map.containsKey("teamid")){
			teamid=map.get("teamid").toString();
		}
		if(map.containsKey("msgNumber")){
			msgNumber=(Integer)map.get("msgNumber");
		}
		if(map.containsKey("index")){
			index=(Integer)map.get("index");
		}
		if(map.containsKey("result")){
			result=map.get("result").toString();
		}
		if(map.containsKey("type")){
			type=map.get("type").toString();
		}
		if(map.containsKey("phone")){
			phone=map.get("phone").toString();
		}
		name=map.get("name").toString();
		image=(Bitmap)map.get("image");
		if(!(map.get("message")==null)){
			message="\n"+map.get("message");
		}
		else{
			message="\n";
		}
		if(!(map.get("date")==null)){
			date=map.get("date").toString();
		}
		else{
			date="";
		}
	}
	
	public String getTeamid(){
		return teamid;
	}
	
	public int getMsgNumber(){
		return msgNumber;
	}
	
	public void setMsgNumber(int msgNumber){
		this.msgNumber=msgNumber;
	}
	
	public String getType(){
		return type;
	}
	
	public void setMessageType(String messageType){
		this.messageType=messageType;
	}
	
	public String getMessageType(){
		return messageType;
	}
	
	public int getIndex(){
		return index;
	}
	
	public String getResult(){
		return result;
	}
	
	public String getDate(){
		return date;
	}
	
	public String getMessage(){
		return message;
	}
	
	public void setPhone(String phone){
		this.phone=phone;
	}
	
	public void setName(String name){
		this.name=name;
	}
	
	
	public void setImage(Bitmap image){
		this.image=image;
	}
	
	public String getPhone(){
		return phone;
	}
	
	public String getName(){
		return name;
	}
	
	public Bitmap getImage(){
		return image;
	}
	
	public String getHeader(){
		return Tools.getSpells(name);
	}
	
}
