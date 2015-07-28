package com.example.kickfor.service;

import java.util.HashMap;
import java.util.Map;

import com.example.kickfor.ClientWrite;
import com.example.kickfor.SQLHelper;
import com.example.kickfor.Tools;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;

public class CheckPhotoService extends Service{

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		SQLHelper helper=SQLHelper.getInstance(this);
		Cursor cursor=helper.select("friends", new String[]{"phone", "image"}, null, null, null);
		Map<String, Object> map=new HashMap<String, Object>();
		while(cursor.moveToNext()){
			map.clear();
			String phone=cursor.getString(0);
			String imgPath=cursor.getString(1);
			if(imgPath==null || Tools.isFileExist(imgPath)==false){
				map.put("request", "get photo");
				map.put("phone", phone);
				Runnable r=new ClientWrite(Tools.JsonEncode(map));
				new Thread(r).start();
			}
		}
		return super.onStartCommand(intent, flags, startId);
	}

	
}
