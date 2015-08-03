package com.example.kickfor.more;

import java.util.ArrayList;
import java.util.List;

import com.example.kickfor.HomePageActivity;
import com.example.kickfor.R;
import com.example.kickfor.utils.IdentificationInterface;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
	
	private ViewGroup formationEdit;
	private ViewGroup front;
	private RelativeLayout layout;
//	private RelativeLayout front;
	private RelativeLayout middle;
	private RelativeLayout behind;

	private int screenWidth;  
    private int screenHeight;  
    private int lastX;  
    private int lastY;
    private SharedPreferences sp;
	private int _xDelta;
	private int _yDelta;
	private ViewGroup view;   

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
		
		formationEdit =  (ViewGroup) view.findViewById(R.id.rl_formation);
		layout = (RelativeLayout) view.findViewById(R.id.rl_court);
		front = (ViewGroup) view.findViewById(R.id.rl_court_front);
		middle = (RelativeLayout) view.findViewById(R.id.rl_court_middle);
		behind = (RelativeLayout) view.findViewById(R.id.rl_court_back);

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
		
//		RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(  
//                150, 50);  
//        layoutParams.leftMargin = 50;  
//        layoutParams.topMargin = 50;  
//        layoutParams.bottomMargin = -250;  
//        layoutParams.rightMargin = -250; 
//        
//        striker.setLayoutParams(layoutParams);
//        striker1.setLayoutParams(layoutParams);
//        leftwing.setLayoutParams(layoutParams);
//        rightwing.setLayoutParams(layoutParams);
//        midfield1.setLayoutParams(layoutParams);
//        midfield2.setLayoutParams(layoutParams);
//        midfield3.setLayoutParams(layoutParams);
//        midfield4.setLayoutParams(layoutParams);
//        midfield5.setLayoutParams(layoutParams);
//        goalkeeper.setLayoutParams(layoutParams);
//        cleaner.setLayoutParams(layoutParams);
//        middefender.setLayoutParams(layoutParams);
//        leftdefender.setLayoutParams(layoutParams);
//        rightdefender.setLayoutParams(layoutParams);
        
        
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
        
        
        
	    
	    int w = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);  
	    int h = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED);  
//	    layout.measure(w, h);  
	    int height =front.getMeasuredHeight();  
	    int width =front.getMeasuredWidth();
	    System.out.println("height = " + height + ", width = " + width);
	    
//	    View view2 = formationEdit.getChildAt(1);
//	    System.out.println("view2 = " + view2);
	    
	   
	    
//	    layout.getChildCount();
	    front.getChildCount();
//	    System.out.println("getChildCount() = " + layout.getChildCount());
	    System.out.println("front.getChildCount() = " + front.getChildCount());

	    formationEdit();
	    
		return view;
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {

		int action = event.getAction();

		switch(action){ 
		//记录拖动的初始位置
        case MotionEvent.ACTION_DOWN:  
            lastX = (int) event.getRawX();  
            lastY = (int) event.getRawY();  
            break; 
        //记录拖动的最新位置
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
        //更新控件的位置
        case MotionEvent.ACTION_UP:
        	int lasty = v.getTop();  
            int lastx = v.getLeft();  
//            Editor editor = sp.edit();  
//            editor.putInt("lasty", lasty);  
//            editor.putInt("lastx", lastx);  
//            editor.commit(); 
            System.out.println("lasty = " + lasty);
            front.getChildCount();
            formationEdit.getChildCount();
//            ViewGroup vp = (ViewGroup) v;
//            View viewchild = vp.getChildAt(1);
            
            List<View> allchildren = new ArrayList<View>();
            
            	for (int i = 0; i < formationEdit.getChildCount(); i++) {
            		View viewchild = front.getChildAt(i);
            		allchildren.add(viewchild);
            		allchildren.size();
            		System.out.println("allchildren.size() =  " + allchildren.size());
            	}
            
    	   
            
            System.out.println("front.getChildCount() = " + front.getChildCount());
//            System.out.println("viewchild = " + viewchild);
            break;                
        } 
		
//		final int X = (int) event.getRawX();  
//        final int Y = (int) event.getRawY();  
//        switch (event.getAction() & MotionEvent.ACTION_MASK) {  
//        case MotionEvent.ACTION_DOWN:  
//            RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) v 
//                    .getLayoutParams();  
//            _xDelta = X - lParams.leftMargin;  
//            _yDelta = Y - lParams.topMargin;  
//            break;  
//        case MotionEvent.ACTION_UP:  
//            break;  
//        case MotionEvent.ACTION_POINTER_DOWN:  
//            break;  
//        case MotionEvent.ACTION_POINTER_UP: 
//        	layout.invalidate(); 
//            break;  
//        case MotionEvent.ACTION_MOVE:  
//            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v  
//                    .getLayoutParams();  
//            layoutParams.leftMargin = X - _xDelta;  
//            layoutParams.topMargin = Y - _yDelta;  
//            layoutParams.rightMargin = -250;  
//            layoutParams.bottomMargin = -250;  
//            v.setLayoutParams(layoutParams);  
//            break;  
//        }  
//        
//        layout.invalidate();
        
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
