package com.example.kickfor.team;

public class MatchLogEntity {
	
	private String matchType=null;
	private String matchNumber=null;
	private String matchResult=null;
	private String matchTotalGoal=null;
	private String matchTotalLost=null;
	
	public MatchLogEntity(String matchType, String matchNumber, String matchResult, String matchTotalGoal, String matchTotalLost){
		this.matchType=matchType;
		this.matchNumber=matchNumber;
		this.matchResult=matchResult;
		this.matchTotalGoal=matchTotalGoal;
		this.matchTotalLost=matchTotalLost;	
	}
	
	public String getMatchType(){
		return matchType;
	}
	
	public String getMatchNumber(){
		return matchNumber;
	}
	
	public String getMatchResult(){
		return matchResult;
	}
	
	public String getMatchTotalGoal(){
		return matchTotalGoal;
	}
	
	public String getMatchTotalLost(){
		return matchTotalLost;
	}

}
