package com.example.kickfor;

import com.example.kickfor.team.ChangingRoomEntity;

import android.os.Handler;
import android.os.Message;


public class ProgressBarTimer implements Runnable{

	private Object v=null;
	private Handler handler=null;
	private int what=-1;
	private Object b=null;
	
	public ProgressBarTimer(Handler handler, int what, Object v, Object b){
		this.handler=handler;
		this.what=what;
		this.v=v;
		this.b=b;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			Thread.sleep(10000);
		}catch(Exception e){
			e.printStackTrace();
		}
		if(b!=null && b instanceof ChangingRoomEntity){
			((ChangingRoomEntity)b).pb1=true;
			((ChangingRoomEntity)b).pb2=true;
			Message msg=handler.obtainMessage();
			msg.obj=v;
			msg.what=what;
			handler.sendMessage(msg);
		}
		else{
			Message msg=handler.obtainMessage();
			msg.obj=v;
			msg.what=what;
			handler.sendMessage(msg);
		}
	}
	
	
	
}
