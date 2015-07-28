package com.example.kickfor;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;



public class KickForListView extends ListView implements OnTouchListener, OnGestureListener{

	private GestureDetector gestureDetector;
	private OnDeleteListener listener;
	private View deleteButton;
	private ViewGroup itemLayout;
	private int selectedItem;
	private boolean isDeleteShown;
	
	private String buttonText="É¾³ý";
	
	public KickForListView(Context context, AttributeSet attrs){
		super(context, attrs);
		gestureDetector=new GestureDetector(getContext(), this);
		setOnTouchListener(this);
	}
	
	public void setOnDeleteListener(OnDeleteListener l){
		listener=l;
	}
	
	

	
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if(isDeleteShown){
			itemLayout.removeView(deleteButton);
			deleteButton=null;
			isDeleteShown=false;
			return false;
		}
		else{
			return gestureDetector.onTouchEvent(event);
		}
	}
	

	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		if(!isDeleteShown){
			selectedItem=pointToPosition((int)e.getX(), (int)e.getY());
		}
		return false;
	}
	
	public void setButtonText(String str){
		buttonText=str;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		// TODO Auto-generated method stub
		if((e1.getX()-e2.getX()>0) && !isDeleteShown && Math.abs(velocityX)>Math.abs(velocityY)){
			deleteButton=LayoutInflater.from(getContext()).inflate(R.layout.delete_button, null);
			Button button=(Button)deleteButton.findViewById(R.id.btn);
			button.setText(buttonText);
			deleteButton.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					itemLayout.removeView(deleteButton);
					deleteButton=null;
					isDeleteShown=false;
					listener.onDelete(selectedItem);
				}
				
			});
			itemLayout=(ViewGroup)getChildAt(selectedItem-getFirstVisiblePosition());
			RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			params.addRule(RelativeLayout.CENTER_VERTICAL);
			itemLayout.addView(deleteButton, params);
			isDeleteShown=true;
		}
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		System.out.println("TapUp");
		return false;
	}

	public interface OnDeleteListener{
		void onDelete(int index);
	}
	
	public interface OnListClickListener{
		void onListClickListener(int index);
	}
}

