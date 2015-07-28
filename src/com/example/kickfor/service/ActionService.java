package com.example.kickfor.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.example.kickfor.ClientWrite;
import com.example.kickfor.Tools;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;

public class ActionService extends Service{

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		System.out.println("Service Done");
		Bundle bundle=intent.getBundleExtra("info");
		Map<String, Object> map=new HashMap<String, Object>();
		Iterator<String> iter=bundle.keySet().iterator();
		while(iter.hasNext()){
			String key=iter.next();
			map.put(key, bundle.get(key));
		}
		Runnable r=new ClientWrite(Tools.JsonEncode(map));
		new Thread(r).start();
		return super.onStartCommand(intent, flags, startId);
	}

	
}
