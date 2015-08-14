package com.example.kickfor;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class Skills {
	
	private int count;
	private String skillsName;
	private String 	name;
	private Bitmap photo;
	private String teamName;
	private String position;
	private Bitmap selected;
	private Bitmap unselected;
	
	public Skills(int count, String skillsName, String name, Bitmap photo, String teamName, String position, Bitmap selected, Bitmap unselected) {
		super();
		this.count = count;
		this.skillsName = skillsName;
		this.name = name;
		this.photo = photo;
		this.teamName = teamName;
		this.position = position;
		this.selected = selected;
		this.unselected = unselected;
	}
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getSkillsName() {
		return skillsName;
	}
	public void setSkillsName(String skillsName) {
		this.skillsName = skillsName;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getPosition() {
		return position;
	}
	public void setPosition(String position) {
		this.position = position;
	}

	public Bitmap getSelected() {
		return selected;
	}

	public void setSelected(Bitmap selected) {
		this.selected = selected;
	}

	public Bitmap getUnselected() {
		return unselected;
	}

	public void setUnselected(Bitmap unselected) {
		this.unselected = unselected;
	}
	
	

}
