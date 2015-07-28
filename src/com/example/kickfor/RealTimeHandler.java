package com.example.kickfor;

import java.util.ArrayList;
import java.util.List;

import android.os.Handler;
import android.os.Message;

public class RealTimeHandler {
	private List<HandlerListener>listeners=new ArrayList<HandlerListener>();
	private static Handler mHandler;
	private static RealTimeHandler mRealTimeHandler;
	
	private RealTimeHandler(){
		mHandler=new Handler(){
			public void handleMessage(Message msg){
				for(HandlerListener listener:listeners){
					listener.onChange(msg);
				}
			}
		};
	}
	
	public synchronized static RealTimeHandler getInstance(){
		if(mRealTimeHandler==null){
			mRealTimeHandler=new RealTimeHandler();
		}
		return mRealTimeHandler;
	}
	
	public void regist(HandlerListener listener){
		if(!listeners.contains(listener))
			listeners.add(listener);
	}
	
	public void unRegist(HandlerListener listener){
		if(listeners.contains(listener))
			listeners.remove(listener);
	}
	
	public Handler getHandler(){
		return mHandler;
	}

}
