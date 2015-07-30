package com.example.kickfor;

import android.graphics.Bitmap;

public class MyChat {

	private long time=0;
	private String message=null;
	private String date=null;
	private Bitmap bitmap=null;
	private String phone=null;//use for group chat
	private boolean isMyMsg;// if true, I sent, else, I received.
	private String msgId=null;

	public void setData(String message, String date, boolean isMyMsg, String phone, long time){
		this.message=message;
		this.date=date;
		this.isMyMsg=isMyMsg;
		this.phone=phone;
		this.time=time;
	}
	
	public void setMsgId(String msgId){
		this.msgId=msgId;
	}
	
	public String getMsgId(){
		return msgId;
	}
	
	public long getTime(){
		return time;
	}
	
	public void setImage(Bitmap bitmap){
		this.bitmap=bitmap;
	}
	
	public Bitmap getImage(){
		return bitmap;
	}
	
	public String getMessage(){
		return message;
	}
	
	public String getDate(){
		return date;
	}
	
	public boolean getIsMyMsg(){
		return isMyMsg;
	}
	
	public String getPhone(){
		return phone;
	}
}
