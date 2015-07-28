package com.example.kickfor;

public class MyMatchEntity {

	private String date=null;
	private String againstname=null;
	private String score=null;
	private String goal=null;
	private String assist=null;
	private int goals;
	private int lost;
	
	private int id=0;
	private String teamid=null;
	private String status=null;
	
	public MyMatchEntity(String teamid, int id){
		this.teamid=teamid;
		this.id=id;
	}
	
	public void setStatus(String status){
		this.status=status;
	}
	
	public String getStatus(){
		return status;
	}
	
	public void setData(String date, String againstname, String goals, String lost, String goal, String assist){
		this.date=date;
		this.againstname=againstname;
		this.score=goals+":"+lost;
		this.goals=Integer.parseInt(goals);
		this.lost=Integer.parseInt(lost);
		this.goal=goal;
		this.assist=assist;
	}
	
	public int getGoals(){
		return goals;
	}
	
	public int getLost(){
		return lost;
	}
	
	
	public String getDate(){
		return date;
	}
	
	public String getAgainstName(){
		return againstname;
	}
	
	public String getScore(){
		return score;
	}
	
	public String getGoal(){
		return "½øÇò"+goal;
	}
	
	public String getAssist(){
		return "Öú¹¥"+assist;
	}
	
	public int getId(){
		return id;
	}
	
	public String getTeamId(){
		return teamid;
	}
	
}
