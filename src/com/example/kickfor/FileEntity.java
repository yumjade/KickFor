package com.example.kickfor;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;

public class FileEntity implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 38L;
	private int userarchiveskey=-1;
	private String position=null;
	private String teamname=null;
	private String uid=null;
	private boolean inTeam=false;
	private String joindate=null;
	private String exitdate=null;
	private List<SubFile> mList=null;
	
	
	public FileEntity(Context context, String uid, int userarchiveskey){
		this.uid=uid;
		this.userarchiveskey=userarchiveskey;
		mList=new ArrayList<SubFile>();
		SQLHelper helper=SQLHelper.getInstance(context);
		initiate(helper);
	}
	
	public FileEntity(String position, String teamname, String inTeam, String joindate, String exitdate){
		this.position=position;
		this.teamname=teamname;
		this.inTeam=inTeam.equals("1")? true: false;
		this.joindate=joindate;
		this.exitdate=exitdate;
	}
	
	public void setData(String position, String teamname, String inTeam, String joindate, String exitdate){
		this.position=position;
		this.teamname=teamname;
		this.inTeam=inTeam.equals("1")? true: false;
		this.joindate=joindate;
		this.exitdate=exitdate;
	}
	
	private void initiate(SQLHelper helper){
		if(userarchiveskey!=-1){
			Cursor cursor=helper.select("archivematch", new String[]{"matchname", "year", "ranking"}, "userarchiveskey=?", new String[]{String.valueOf(userarchiveskey)}, null);
			while(cursor.moveToNext()){
				SubFile item=new SubFile();
				item.setData(cursor.getString(0), cursor.getString(1), cursor.getString(2));
				mList.add(item);
			}
		}
		else{
			this.position="";
			this.teamname="";
			this.inTeam=false;
			this.joindate="";
			this.exitdate="";
		}
	}
	
	public int getUserArchiveKey(){
		return userarchiveskey;
	}
	
	public List<SubFile> getMatch(){
		return mList;
	}
	
	public String getExitDate(){
		exitdate=exitdate.replaceAll("-", "/");
		return inTeam? "жа╫Я": exitdate.replace(" 00:00:00", "");
	}
	
	public boolean isInTeam(){
		return inTeam;
	}
	
	public String getJoinDate(){
		joindate=joindate.replaceAll("-", "/");
		return joindate.replace(" 00:00:00", "");
	}
	
	public String getPosition(){
		return position;
	}
	
	public String getTeamName(){
		return teamname;
	}
	
	public String getPhone(){
		return uid;
	}
	
}
