package com.example.kickfor.team;

import java.io.Serializable;

import com.example.kickfor.Tools;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class HallofFame implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 20L;
	private int _id = 0;
	private String name = null;
	private String position = null;
	private String number = null;
	private String photo = null;
	private String date = null;
	private String intruduction = null;

	public HallofFame(int _id, String name, String number, String position,
			String photo, String date, String intruduction) {
		super();
		this._id = _id;
		this.name = name;
		this.position = position;
		this.photo = photo;
		this.date = date;
		this.number = number;
		this.intruduction = intruduction;
	}

	public int getId() {
		return _id;
	}

	public String getName() {
		return name;
	}

	public String getPosition() {
		return position;
	}

	public String getNumber() {
		return number;
	}

	public Bitmap getPhoto() {
		return Tools.stringtoBitmap(photo);
	}

	public String getDate() {
		return date;
	}

	public String getIntruduction() {
		return intruduction;
	}

}
