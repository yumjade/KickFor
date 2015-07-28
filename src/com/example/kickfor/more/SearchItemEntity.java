package com.example.kickfor.more;

import android.graphics.Bitmap;

public class SearchItemEntity {
	
	public static final boolean TEAM_SEARCH=true;
	protected static final boolean PERSON_SEARCH=false;
	
	private String phone=null;
	private String teamid=null;
	private boolean type;
	private String name=null;
	private String city=null;
	private String year=null;
	private String number=null;
	private String team=null;
	private String position=null;
	private Bitmap image=null;
	private String captain=null;
	
	public SearchItemEntity(boolean type, String id){
		this.type=type;
		if(type==TEAM_SEARCH){
			this.teamid=id;
		}
		else{
			this.phone=id;
		}
	}
	
	public String getCaptain(){
		if(captain!=null){
			return captain;
		}
		else{
			return "";
		}
	}
	
	public void setCaptain(String captain){
		this.captain=captain;
	}
	
	public void setTeamData(String name, String city, String year, String number, Bitmap image){
		this.name=name;
		this.city=city;
		this.year=year;
		this.number=number;
		this.image=image;
	}
	
	public void setPersonData(String name, String city, String year, String team1, String team2, String team3, String position1, String position2, Bitmap image){
		this.name=name;
		this.city=city;
		this.year=year;
		this.team=team1+" "+team2+" "+team3;
		this.position=position1+" "+position2;
		this.image=image;
	}
	
	public Bitmap getImage(){
		return image;
	}
	
	public String getName(){
		return name;
	}
	
	public String getTeamid(){
		return teamid;
	}
	
	public String getPhone(){
		return phone;
	}
	
	
	public String getInfo(){
		if(type==PERSON_SEARCH){
			String str=city+",生于"+year+"年，\n效力于"+team+"\n可踢"+position;
			return str;
		}
		else{
			String str=city+"草根球队，成立于"+year+"，\n目前球队有"+number+"人，队长是"+getCaptain();
			return str;
		}
	}
	
	public String getType(){
		if(type==TEAM_SEARCH){
			return "球队";
		}
		else{
			return "球员";
		}
	}
	
	public boolean getTypeBoolean(){
		return type;
	}
	
	public boolean isTeamOrPerson(){
		return type;
	}

}
