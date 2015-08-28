package com.example.kickfor.more;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.example.kickfor.R;
import com.example.kickfor.SQLHelper;
import com.example.kickfor.Tools;
import com.example.kickfor.utils.IdentificationInterface;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FormationEditFragment extends Fragment implements MoreInterface, OnTouchListener, IdentificationInterface{
	
	private int OFFSET=30;
	
	private int offset=0;
	private int dustX=0;
	private int dustY=0;
	
	private String table=null;
	private String teamid=null;
	
	private ImageView back=null;
	private TextView formation=null;
	private TextView select=null;
	
	private RelativeLayout layout=null;
	private ViewPager viewPager=null;
	private LayoutInflater inflater=null;
	private List<View> mList=null;
	private FormationAdapter adapter=null;
	
	private RelativeLayout title=null;
	
	private Context context=null;
	private int frontNumber=0;
	private int middleNumber=0;
	private int behindNumber=0;
	
	private int screenWidth=0;  
    private int screenHeight=0;  
    private int lastX=0;  
    private int lastY=0;
  

    private void init(){
    	Bundle bundle=getArguments();
    	teamid="13366053911u";//bundle.getString("teamid");
    	context=getActivity();
    	inflater=LayoutInflater.from(context);
    	table="f_"+teamid;
    	mList=new ArrayList<View>();
    }
    
	@Override
	public int getFragmentLevel() {
		return IdentificationInterface.SECOND_LEVEL;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		init();
		View view = inflater.inflate(R.layout.fragment_formation_edit, container, false);
		title=(RelativeLayout)view.findViewById(R.id.formation_title);
		back=(ImageView)view.findViewById(R.id.formation_back);
		select=(TextView)view.findViewById(R.id.formation_select_player);
		
		select.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewPager.setVisibility(View.VISIBLE);
			}
			
		});
		
		formation = (TextView) view.findViewById(R.id.formation_text);
		formation.setText("");
		layout = (RelativeLayout) view.findViewById(R.id.formation_court);
		viewPager=(ViewPager)view.findViewById(R.id.formation_pager);
		
		layout.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				viewPager.setVisibility(View.GONE);
			}
			
		});
		
		adapter = new FormationAdapter(mList);
        
        viewPager.setAdapter(adapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            
            @Override
            public void onPageSelected(int arg0) {                                 
                // TODO Auto-generated method stub
            }
            
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void onPageScrollStateChanged(int arg0) {
                // TODO Auto-generated method stub
                
            }
        });
		
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getActivity().onBackPressed();
				
			}
		});
		
		DisplayMetrics dm = getResources().getDisplayMetrics();  
	    screenWidth = dm.widthPixels;  
	    screenHeight = dm.heightPixels- Tools.dip2px(context, OFFSET);  
		
	    initiate(); 
	    
		return view;
	}
	
	private void initiate(){
		SQLHelper helper=SQLHelper.getInstance(context);
		Cursor cursor=helper.select(table, new String[]{"phone", "name", "number", "position1", "position2"}, null, null, null);
		int i=0;
		while(cursor.moveToNext()){
			Cursor cursor1=helper.select("friends", new String[]{"image"}, "phone=?", new String[]{cursor.getString(0)}, null);
			Bitmap bitmap=null;
			if(cursor1.moveToNext()){
				bitmap=cursor1.getString(0).equals("-1")? BitmapFactory.decodeResource(getResources(), R.drawable.team_default): BitmapFactory.decodeFile(cursor1.getString(0));	
			}
			else{
				bitmap=BitmapFactory.decodeResource(getResources(), R.drawable.team_default);
			}
			View v=inflater.inflate(R.layout.formation_person_info, null);
			TextView number=(TextView)v.findViewById(R.id.formation_number);
			number.setText(cursor.getString(2));
			TextView name=(TextView)v.findViewById(R.id.formation_name);
			name.setText(cursor.getString(1));
			ImageView photo=(ImageView)v.findViewById(R.id.formation_photo);
			photo.setImageBitmap(bitmap);
			TextView position=(TextView)v.findViewById(R.id.formation_position);
			position.setText(cursor.getString(3)+"\n"+cursor.getString(4));
			
			Bitmap person=Tools.convertViewToBitmap(v);
			
			int left=i%8;
			int index=i/8;
			if(left==0){
				View view=inflater.inflate(R.layout.formation_edit_page, null);
				LinearLayout l=(LinearLayout)view.findViewById(R.id.formation_edit_line1);
				LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
				ImageView p=new ImageView(context);
				p.setImageBitmap(person);
				p.setOnTouchListener(this);
				l.addView(p, params);
				mList.add(view);
			}
			else if(left<4){
				LinearLayout l=(LinearLayout)mList.get(index).findViewById(R.id.formation_edit_line1);
				LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
				ImageView p=new ImageView(context);
				p.setOnTouchListener(this);
				p.setImageBitmap(person);
				l.addView(p, params);
			}
			else if(left>=4){
				LinearLayout l=(LinearLayout)mList.get(index).findViewById(R.id.formation_edit_line2);
				LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);
				ImageView p=new ImageView(context);
				p.setOnTouchListener(this);
				p.setImageBitmap(person);
				l.addView(p, params);
			}
			
			i++;
		}
		adapter.notifyDataSetChanged();
	}

	@SuppressLint("NewApi")
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		 int action=event.getAction();
	        switch(action){  
	        case MotionEvent.ACTION_DOWN:{
	        	System.out.println("down");
	        	lastX = (int) event.getRawX();  
	            lastY = (int) event.getRawY();  
	        	if(v.getParent() instanceof LinearLayout){
	        		ViewGroup parent=(ViewGroup)v.getParent();
	        		int whole=parent.getChildCount();
	        		int index=0;
	        		for(int i=0; i<whole; i++){
	        			if(parent.getChildAt(i).equals(v)){
	        				index=i;
	        				break;
	        			}
	        		}
	        		
	        		offset=title.getHeight();
	        		int[] position = new int[2];  
	        		v.getLocationInWindow(position);  
	        		
	        		ImageView p=new ImageView(context);
	        		ImageView tmp=(ImageView)v;
	        		Bitmap image = ((BitmapDrawable)tmp.getDrawable()).getBitmap();  
					p.setImageBitmap(image);
					p.setOnTouchListener(null);
					
					parent.removeViewAt(index);
					LinearLayout.LayoutParams params1=(LinearLayout.LayoutParams)v.getLayoutParams();
					parent.addView(p, index, params1);
					
	        		RelativeLayout.LayoutParams params=new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
	        		
		    	    params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		    	    params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		    	    params.setMargins(position[0], position[1]-offset, 0, 0);
		    	    
	        		layout.addView(v, 0, params);
	        		p.setAlpha((float)0.5);
	        	}
	        	viewPager.setVisibility(View.GONE);
	            break;
	        }
	        case MotionEvent.ACTION_MOVE:{ 
	        	if(v.getParent() instanceof RelativeLayout){
	        		System.out.println("move");
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
		            
		    				
		    	    RelativeLayout.LayoutParams params=(RelativeLayout.LayoutParams)v.getLayoutParams();
		    	    params.removeRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		    	    params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		    	    params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
		    	    params.setMargins(left, top, 0, 0);
		    	    v.requestLayout();
		            
		            
		            lastX = (int) event.getRawX();  
		            lastY = (int) event.getRawY();
	        	}
	            break;
	        }
	        case MotionEvent.ACTION_UP:{ 
	        	System.out.println("up");
	        	if(v.getParent() instanceof RelativeLayout){
	        		int[] position = new int[2];  
	        		select.getLocationInWindow(position);  
	        		dustX=position[0]-Tools.dip2px(context, 40);
	        		dustY=position[1]-Tools.dip2px(context, 40);
	        		if(lastX>=dustX && lastY>=dustY){
	        			Bitmap bitmap=((BitmapDrawable)((ImageView)v).getDrawable()).getBitmap();
		        		layout.removeView(v);
		        		Iterator<View> iter=mList.iterator();
		        		boolean isContinue=true;
		        		while(iter.hasNext() && isContinue==true){
		        			View vLayout=iter.next();
		        			LinearLayout l1=(LinearLayout)vLayout.findViewById(R.id.formation_edit_line1);
		        			int num1=l1.getChildCount();
		        			for(int i=0; i<num1; i++){
		        				ImageView iv=(ImageView)l1.getChildAt(i);
		        				Bitmap tmp=((BitmapDrawable)iv.getDrawable()).getBitmap();
		        				if(bitmap.equals(tmp)){
		        					iv.setOnTouchListener(this);
		        					iv.setAlpha((float)1);
		        					isContinue=false;
		        					break;
		        				}
		        			}
		        			LinearLayout l2=(LinearLayout)vLayout.findViewById(R.id.formation_edit_line2);
		        			int num2=l2.getChildCount();
		        			for(int i=0; i<num2; i++){
		        				ImageView iv=(ImageView)l2.getChildAt(i);
		        				Bitmap tmp=((BitmapDrawable)iv.getDrawable()).getBitmap();
		        				if(bitmap.equals(tmp)){
		        					iv.setOnTouchListener(this);
		        					iv.setAlpha((float)1);
		        					isContinue=false;
		        					break;
		        				}
		        			}
		        		}
		        	}
	        	}
	        	checkPosition();
	        	
	            break;  
	        }
	        }  
	        return true;     
	}

	@Override
	public void setEnable(boolean enable) {
		
	}
	
	private void checkPosition(){
		int num=layout.getChildCount();
		frontNumber=0;
		middleNumber=0;
		behindNumber=0;
		for(int i=0; i<num; i++){
			if(layout.getChildAt(i) instanceof ImageView){
				ImageView v=(ImageView)layout.getChildAt(i);
				int[] position = new int[2];  
        		v.getLocationInWindow(position);  
        		int y=position[1];
        		if(y<=screenHeight/3){
        			frontNumber++;
        		}
        		else if(y<=2*screenHeight/3){
        			middleNumber++;
        		}
        		else if(y<=screenHeight){
        			behindNumber++;
        		}
			}
		}
		formation.setText(behindNumber+"-"+middleNumber+"-"+frontNumber);
	}
	

}
