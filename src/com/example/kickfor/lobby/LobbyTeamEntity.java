package com.example.kickfor.lobby;

import java.util.ArrayList;
import java.util.List;

import com.example.kickfor.SQLHelper;

import android.content.Context;
import android.graphics.Bitmap;

public class LobbyTeamEntity {

	private String themeKey=null;
	private String teamid=null;
	private Bitmap image=null;
	private String name=null;
	private String city=null;
	private String date=null;
	private String type=null;
	private String text=null;
	private List<String> replyThemeKey=null;
	private List<String> fromName=null;
	private List<String> content=null;
	private String start=null;
	private String num=null;
	
	public LobbyTeamEntity(String themeKey, String teamid, String type, String date, String text, Bitmap bitmap, String name, String city){
		this.themeKey=themeKey;
		this.teamid=teamid;
		this.type=type;
		this.date=date;
		this.text=text;
		this.replyThemeKey=new ArrayList<String>();
		this.fromName=new ArrayList<String>();
		this.content=new ArrayList<String>();
		this.image=bitmap;
		this.name=name;
		this.city=city;
	}
	
	public LobbyTeamEntity(String type, String start, String num){
		this.themeKey=type;
		this.start=start;
		this.num=num;
	}
	
	
	
	public String getStart(){
		return start;
	}
	
	public String getNum(){
		return num;
	}
	
	public void setReplyList(String replyThemeKey, String fromName, String content){
		this.replyThemeKey.add(replyThemeKey);
		this.fromName.add(fromName);
		this.content.add(content);
	}
	
	public String getType(){
		if(type.equals("1")){
			return "招人";
		}
		else if(type.equals("2")){
			return "约战";
		}
		else{
			return "话题";
		}
	}
	
	public String getCity(){
		return city;
	}
	
	public String getName(){
		return name;
	}
	
	public String getDate(){
		return date;
	}
	
	public Bitmap getImage(){
		return image;
	}
	
	public String getThemeKey(){
		return themeKey;
	}
	
	public String getText(){
		return text;
	}
	
	public String getTeamid(){
		return teamid;
	}
	
	public List<String> getFromNameList(){
		return fromName;
	}
	
	public List<String> getContentList(){
		return content;
	}
	
	public List<String> getReplyThemeKeyList(){
		return replyThemeKey;
	}
}
