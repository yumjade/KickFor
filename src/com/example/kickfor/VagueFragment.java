package com.example.kickfor;

import android.os.Handler;
import android.os.Message;

public class VagueFragment implements Runnable{
	
	private static Handler handler=null;
	private static int what=-1;
	
	public VagueFragment(Handler handler, int what){
		VagueFragment.handler=handler;
		VagueFragment.what=what;
	}
	
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try{
			Thread.sleep(3000);
		}catch(Exception e){
			e.printStackTrace();
		}
		Message msg=handler.obtainMessage();
		msg.what=what;
		handler.sendMessage(msg);
	}

}
