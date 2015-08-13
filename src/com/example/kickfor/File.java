package com.example.kickfor;

import java.io.Serializable;

import android.graphics.Bitmap;

public class File implements Serializable{
	
	private int _id=0;
	private String position=null;
	private Bitmap photo=null;
	private String teamName = null;
	private String date=null;
	private String match = null;
	private String eventName = null;
	private String winningYear = null;
	private String finalRank = null;
	
	private boolean isChanged=false;
	


	public File(String position, Bitmap photo, String teamName,
			 String date, String match, String eventName, String winningYear, String finalRank) {
		super();
		this.position = position;
		this.photo = photo;
		this.teamName = teamName;
		this.date = date;
		this.match = match;
		this.eventName = eventName;
		this.winningYear = winningYear;
		this.finalRank = finalRank;
		
	}

	public int get_id() {
		return _id;
	}

	public void set_id(int _id) {
		this._id = _id;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}
	
	public Bitmap getPhoto() {
		return photo;
	}

	public void setPhoto(Bitmap photo) {
		this.photo = photo;
	}

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}
	
	

	public String getMatch() {
		return match;
	}

	public void setMatch(String match) {
		this.match = match;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getWinningYear() {
		return winningYear;
	}

	public void setWinningYear(String winningYear) {
		this.winningYear = winningYear;
	}

	public String getFinalRank() {
		return finalRank;
	}

	public void setFinalRank(String finalRank) {
		this.finalRank = finalRank;
	}

	public boolean isChanged() {
		return isChanged;
	}

	public void setChanged(boolean isChanged) {
		this.isChanged = isChanged;
	}

	
	
	
}
