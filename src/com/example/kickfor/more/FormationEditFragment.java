package com.example.kickfor.more;

import java.util.ArrayList;

import com.example.kickfor.HomePageActivity;
import com.example.kickfor.R;
import com.example.kickfor.utils.IdentificationInterface;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FormationEditFragment extends Fragment implements MoreInterface, OnTouchListener, IdentificationInterface {

	private CheckBox striker;
	private CheckBox striker1;
	private CheckBox leftwing;
	private CheckBox rightwing;
	private CheckBox midfield1;
	private CheckBox midfield2;
	private CheckBox midfield3;
	private CheckBox midfield4;
	private CheckBox midfield5;
	private CheckBox goalkeeper;
	private CheckBox cleaner;
	private CheckBox middefender;
	private CheckBox leftdefender;
	private CheckBox rightdefender;
	private ImageView back;
	private TextView formation;
	
	private RelativeLayout layout;
	private RelativeLayout front;
	private RelativeLayout middle;
	private RelativeLayout behind;

	private int screenWidth;  
    private int screenHeight;  
    private int lastX;  
    private int lastY;  

	@Override
	public int getFragmentLevel() {
		return IdentificationInterface.SECOND_LEVEL;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.formation_edit, container, false);

		back = (ImageView) view.findViewById(R.id.position_back);
		formation = (TextView) view.findViewById(R.id.formation_text);
		
		striker = (CheckBox) view.findViewById(R.id.rb_striker);
		striker1 = (CheckBox) view.findViewById(R.id.rb_striker1);
		leftwing = (CheckBox) view.findViewById(R.id.rb_leftwing);
		rightwing = (CheckBox) view.findViewById(R.id.rb_rightwing);
		midfield1 = (CheckBox) view.findViewById(R.id.rb_midfield1);
		midfield2 = (CheckBox) view.findViewById(R.id.rb_midfield2);
		midfield3 = (CheckBox) view.findViewById(R.id.rb_midfield3);
		midfield4 = (CheckBox) view.findViewById(R.id.rb_midfield4);
		midfield5 = (CheckBox) view.findViewById(R.id.rb_midfield5);
		goalkeeper = (CheckBox) view.findViewById(R.id.rb_goalkeeper);
		cleaner = (CheckBox) view.findViewById(R.id.rb_cleaner);
		middefender = (CheckBox) view.findViewById(R.id.rb_middefender);
		leftdefender = (CheckBox) view.findViewById(R.id.rb_leftdefender);
		rightdefender = (CheckBox) view.findViewById(R.id.rb_rightdefender);
		
		layout = (RelativeLayout) view.findViewById(R.id.rl_court);
		front = (RelativeLayout) view.findViewById(R.id.rl_court_front);
		middle = (RelativeLayout) view.findViewById(R.id.rl_court_middle);
		behind = (RelativeLayout) view.findViewById(R.id.rl_court_back);

		striker.setOnTouchListener(this);
		striker1.setOnTouchListener(this);
		leftwing.setOnTouchListener(this);
		rightwing.setOnTouchListener(this);
		midfield1.setOnTouchListener(this);
		midfield2.setOnTouchListener(this);
		midfield3.setOnTouchListener(this);
		midfield4.setOnTouchListener(this);
		midfield5.setOnTouchListener(this);
		goalkeeper.setOnTouchListener(this);
		cleaner.setOnTouchListener(this);
		middefender.setOnTouchListener(this);
		leftdefender.setOnTouchListener(this);
		rightdefender.setOnTouchListener(this);
		
		
		
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((HomePageActivity) getActivity()).onBackPressed();

			}
		});
		
		DisplayMetrics dm = getResources().getDisplayMetrics();  
	    screenWidth = dm.widthPixels;  
	    screenHeight = dm.heightPixels - 130; 
	    System.out.println("screenWidth = " + screenWidth + ", screenHeight = " + screenHeight);
	    
	    int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);  
	    int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);  
	    layout.measure(w, h);  
	    int height =layout.getMeasuredHeight();  
	    int width =layout.getMeasuredWidth();
	    System.out.println("height = " + height + ", width = " + width);
	    
	    
	    layout.getChildCount();
	    front.getChildCount();
	    System.out.println("getChildCount() = " + layout.getChildCount());
	    System.out.println("front.getChildCount() = " + front.getChildCount());

//	    formationEdit();
	    
		return view;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		int action = event.getAction();

		switch(action){  
        case MotionEvent.ACTION_DOWN:  
            lastX = (int) event.getRawX();  
            lastY = (int) event.getRawY();  
            break;  
        case MotionEvent.ACTION_MOVE:  
            int dx =(int)event.getRawX() - lastX;  
            int dy =(int)event.getRawY() - lastY;  
          
            int left = v.getLeft() + dx;  
            int top = v.getTop() + dy;  
            int right = v.getRight() + dx;  
            int bottom = v.getBottom() + dy;                      
            if(left < 0){  
                left = 0;  
                right = left + v.getWidth();  
            }                     
            if(right > screenWidth){  
                right = screenWidth;  
                left = right - v.getWidth();  
            }                     
            if(top < 0){  
                top = 0;  
                bottom = top + v.getHeight();  
            }                     
            if(bottom > screenHeight){  
                bottom = screenHeight;  
                top = bottom - v.getHeight();  
            } 
            
            v.layout(left, top, right, bottom); 
            
            lastX = (int) event.getRawX();  
            lastY = (int) event.getRawY();                    
            break;  
        case MotionEvent.ACTION_UP:
        	formationEdit();
            break;                
        } 
		
		return true;
	}
	
	public void formationEdit(){
		int m = 0;
		int n = 0;
		int k = 0;
		
		m = front.getChildCount();
		n = middle.getChildCount();
		k = behind.getChildCount();
		System.out.println("m = " + m +", n = " + n + ", k = " + k);
		
		formation.setText(m + " - " + n + " - " + k);
		
	}
}
