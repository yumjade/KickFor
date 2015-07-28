package com.example.kickfor;

import java.io.File;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.TextView;

public class MyPopupWindow extends PopupWindow{
	
	private TextView takePhoto=null;
	private TextView fromFile=null;
	private TextView cancel=null;
	private View view=null;
	
	public MyPopupWindow(Activity context){
		super(context);
		final Activity c=context;
		LayoutInflater inflater=LayoutInflater.from(context);
		view=inflater.inflate(R.layout.fragment_bar_select_photo, null);
		takePhoto=(TextView)view.findViewById(R.id.takePhoto);
		fromFile=(TextView)view.findViewById(R.id.fromFile);
		cancel=(TextView)view.findViewById(R.id.cancel_1);
		
		cancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				dismiss();
			}
			
		});
		
		takePhoto.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intentFromCapture=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if(Tools.hasSdcard()){
                	intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(Environment.getExternalStorageDirectory(), HomePageActivity.IMAGE_FILE_NAME)));
                }
                c.startActivityForResult(intentFromCapture, HomePageActivity.CAMERA_REQUEST_CODE);
			}
			
		});
		
		fromFile.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intentFromGallery=new Intent();
				intentFromGallery.setType("image/*");
				intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
				c.startActivityForResult(intentFromGallery,HomePageActivity.IMAGE_REQUEST_CODE);
			}
			
		});
		
		setContentView(view);
		setWidth(LayoutParams.MATCH_PARENT);
		setHeight(LayoutParams.WRAP_CONTENT);
		setFocusable(true);
		
		view.setOnTouchListener(new OnTouchListener(){

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				int height=view.findViewById(R.id.btns).getTop();
				int y=(int)event.getY();
				if(event.getAction()==MotionEvent.ACTION_UP){
					if(y<height){
						dismiss();
					}
				}
				return true;
			}
			
		});
	}

}
