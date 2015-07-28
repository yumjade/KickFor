package com.example.kickfor.team;

import com.example.kickfor.SQLHelper;

import android.content.Context;
import android.database.Cursor;

class MemberInfo{
	private Context context=null;
	private String phone=null;
	private String number=null;
	private String name=null;
	private String position1=null;
	private String position2=null;
	private String aInfo=null;
	private String efficiency=null;
	
	public MemberInfo(String phone, Context context){
		this.phone=phone;
		this.context=context;
	}
	
	public void setName(String name){
		this.name=name;
	}
	
	public void setNumber(String number){
		this.number=number;
	}
	
	public void setPosition(String position1, String position2){
		this.position1=position1;
		this.position2=position2;
	}
	
	public void setPosition(){
		SQLHelper helper=SQLHelper.getInstance(context);
		Cursor cursor=helper.select("ich", new String[]{"position1", "position2"}, "phone=?", new String[]{phone}, null);
		if(cursor.moveToNext()){
			this.position1=cursor.getString(0);
			this.position2=cursor.getString(1);
		}
		else{
			this.position1="";
			this.position2="";
		}
	}
	
	public void setAInfo(String attendance, String totalmatch){
		int a=Integer.parseInt(attendance);
		int t=Integer.parseInt(totalmatch);
		this.aInfo=attendance+"/"+totalmatch + " ³¡";
		if(t==0){
			this.efficiency="0" + "%";
		}
		else{
			this.efficiency=String.valueOf(a*100/t)+"%";
		}
	}
	
	public String getName(){
		return name;
	}
	
	public String getNumber(){
		return number;
	}
	
	public String getposition(){
		return position1+" "+position2;
	}
	
	public String getAInfo(){
		return aInfo;
	}
	
	public String getEfficiency(){
		return efficiency;
	}
}