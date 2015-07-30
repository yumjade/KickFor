package com.example.kickfor.wheelview;

import android.view.View;

import com.example.kickfor.R;

public class WheelTime {
	
	private View view;
	private WheelView hourWheel=null;
	private WheelView minuteWheel=null;
	public int screenheight=0;
	
	public View getView(){
		return view;
	}
	
	public void setView(View view){
		this.view=view;
	}
	
	
	public WheelTime(View view){
		super();
		this.view=view;
	}
	
	public void initTimePicker(int hour, int minute){
		hourWheel=(WheelView)view.findViewById(R.id.wheel_hour);
		minuteWheel=(WheelView)view.findViewById(R.id.wheel_min);
		hourWheel.setVisibleItems(3);
		minuteWheel.setVisibleItems(3);
		hourWheel.setAdapter(new NumericWheelAdapter(0, 23, "%02d"));
		minuteWheel.setAdapter(new NumericWheelAdapter(0, 59, "%02d"));
		hourWheel.setCyclic(true);
		minuteWheel.setCyclic(true);
		hourWheel.setCurrentItem(hour);
		minuteWheel.setCurrentItem(minute);
		int textSize=0;
		textSize=(screenheight/100)*5;
		hourWheel.TEXT_SIZE=textSize;
		minuteWheel.TEXT_SIZE=textSize;
	}
	
	public String getTime(){
		return String.format("%02d", hourWheel.getCurrentItem())+":"+String.format("%02d", minuteWheel.getCurrentItem());
	}
	
}
