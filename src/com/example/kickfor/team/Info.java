package com.example.kickfor.team;

import java.util.HashMap;
import java.util.Map;

public class Info{
	
	private String phone=null;
	private String name=null;
	private String number=null;
	private boolean attendance=false;
	private int goal=0;
	private int assist=0;
	private int card=0;
	
	private boolean attendance1;
	private int goal1=0;
	private int assist1=0;
	private int card1=0;
	
	private boolean isNew=true;
	
	private boolean isPoped=false;
	
	public Info(String phone, String name, String number){
		this.phone=phone;
		this.number=number;
		this.name=name;
	}
	
	public void setInfo(String goal, String assist, String redCard, String yellowCard, String attendance){
		this.goal=Integer.parseInt(goal);
		this.goal1=this.goal;
		this.assist=Integer.parseInt(assist);
		this.assist1=this.assist;
		if(attendance.equals("1")){
			this.attendance=true;
		}
		else{
			this.attendance=false;
		}
		this.attendance1=this.attendance;
		
		if(redCard.equals("1")){
			card=2;
		}
		else if(yellowCard.equals("1")){
			card=1;
		}
		else{
			card=0;
		}
		this.card1=card;
		isNew=false;
	}
	
	public void setAttendance(){
		attendance=!attendance;
	}
	
	public void setPoped(){
		isPoped=!isPoped;
	}
	
	public boolean getPoped(){
		return isPoped;
	}
	
	public void resetGoal(){
		goal=0;
	}
	
	public void setGoal(){
		goal=goal+1;
	}
	
	public void resetAssist(){
		assist=0;
	}
	
	public void setAssist(){
		assist=assist+1;
	}
	
	public void resetCard(){
		card=0;
	}
	
	public void setCard(){
		if(card<2){
			card=card+1;
		}
	}
	
	public String getGoal(){
		return String.valueOf(goal);
	}
	
	public String getAssist(){
		return String.valueOf(assist);
	}
	
	public int getCard(){
		return card;
	}
	
	public String getNumber(){
		return String.valueOf(number);
	}
	
	public String getName(){
		return name;
	}
	
	public boolean getAttendance(){
		return attendance;
	}
	
	public Map<String, Object> getInfo(){
		Map<String, Object> map=new HashMap<String, Object>();
		if(isNew==true){
			map.put("phone", phone);
			map.put("number", number);
			map.put("name", name);
			map.put("totalmatch", "1");
			if(attendance==true){
				map.put("attendance", "1");
				map.put("goal", String.valueOf(goal));
				map.put("assist", String.valueOf(assist));
				map.put("card", String.valueOf(card));
			}
			else{
				map.put("attendance", "0");
				map.put("goal", "0");
				map.put("assist", "0");
				map.put("card", "0");
			}
		}
		else{
			map.put("phone", phone);
			map.put("number", number);
			map.put("name", name);
			map.put("totalmatch", "0");
			if(attendance==true && attendance1==true){
				map.put("attendance", "0");
				map.put("goal", String.valueOf(goal-goal1));
				map.put("assist", String.valueOf(assist-assist1));
				map.put("card", String.valueOf(card-card1));
			}
			else if(attendance==true && attendance1==false){
				map.put("attendance", "1");
				map.put("goal", String.valueOf(goal-goal1));
				map.put("assist", String.valueOf(assist-assist1));
				map.put("card", String.valueOf(card));
			}
			else if(attendance==false && attendance1==true){
				map.put("attendance", "-1");
				map.put("goal", String.valueOf(0-goal1));
				map.put("assist", String.valueOf(0-assist1));
				map.put("card", String.valueOf(0-card1));
			}
			else{
				map.put("attendance", "0");
				map.put("goal", "0");
				map.put("assist", "0");
				map.put("card", "0");
			}
		}
		return map;
	}
	
}
