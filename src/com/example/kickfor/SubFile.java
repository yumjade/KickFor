package com.example.kickfor;

import java.io.Serializable;

public class SubFile implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 39L;
	
	private int userarchivesmatchkey=-1;
	private int userarchiveskey=-1;
	private String uid=null;
	private String matchname="";
	private String year="";
	private String ranking="";
	
	

	public void setData(String matchname, String year, String ranking){
		this.matchname=matchname;
		this.year=year;
		this.ranking=ranking;
	}
	
	public String getMatchName(){
		return matchname;
	}
	
	public String getYear(){
		return year;
	}
	
	public String getRanking(){
		return ranking;
	}
	
	public int getMatchKey(){
		return userarchivesmatchkey;
	}
	
	public int getKey(){
		return userarchiveskey;
	}
	
	public String getPhone(){
		return uid;
	}
	
	public void setMatchName(String matchname){
		this.matchname=matchname;
	}
	
	public void setRanking(String ranking){
		this.ranking=ranking;
	}
	
	public void setYear(String year){
		this.year=year;
	}
	
	public boolean isEmptyExist(){
		if(matchname.isEmpty() || year.isEmpty() || ranking.isEmpty()){
			return true;
		}
		else{
			return false;
		}
	}
	
}
