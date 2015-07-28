package com.example.kickfor.wheelview;


import com.example.kickfor.R;

import android.view.View;

public class WheelScore {

	private View view;
	private WheelView goalsWheel=null;
	private WheelView lostWheel=null;
	public int screenheight=0;
	private static int START_SCORE=0, END_SCORE=20;
	
	public View getView(){
		return view;
	}
	
	public void setView(View view){
		this.view=view;
	}
	
	public static int getSTART_SCORE(){
		return START_SCORE;
	}
	
	public static int getEND_SCORE(){
		return END_SCORE;
	}
	
	public WheelScore(View view){
		super();
		this.view=view;
	}
	
	public void initScorePicker(int goals, int lost){
		goalsWheel=(WheelView)view.findViewById(R.id.wheel_goals);
		lostWheel=(WheelView)view.findViewById(R.id.wheel_lost);
		goalsWheel.setVisibleItems(1);
		lostWheel.setVisibleItems(1);
		goalsWheel.setAdapter(new NumericWheelAdapter(START_SCORE, END_SCORE));
		lostWheel.setAdapter(new NumericWheelAdapter(START_SCORE, END_SCORE));
		goalsWheel.setCyclic(false);
		lostWheel.setCyclic(false);
		goalsWheel.setCurrentItem(goals-START_SCORE);
		lostWheel.setCurrentItem(lost-START_SCORE);
		int textSize=0;
		textSize=(screenheight/100)*7;
		goalsWheel.TEXT_SIZE=textSize;
		lostWheel.TEXT_SIZE=textSize;
	}
	
	public String getScore(){
		return goalsWheel.getCurrentItem()+" - "+lostWheel.getCurrentItem();
	}
	
	public int getGoals(){
		return goalsWheel.getCurrentItem();
	}
	
	public int getLost(){
		return lostWheel.getCurrentItem();
	}
	
}
