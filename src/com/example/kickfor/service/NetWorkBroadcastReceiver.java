package com.example.kickfor.service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.example.kickfor.ClientWrite;
import com.example.kickfor.HomePageActivity;
import com.example.kickfor.PreferenceData;
import com.example.kickfor.R;
import com.example.kickfor.SQLHelper;
import com.example.kickfor.SocketSingleton;
import com.example.kickfor.TitleFragment;
import com.example.kickfor.Tools;
import com.example.kickfor.team.TeamFragment;
import com.example.kickfor.utils.IdentificationInterface;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import cn.jpush.android.api.JPushInterface;

public class NetWorkBroadcastReceiver extends BroadcastReceiver{
	
	private HomePageActivity activity=null;
	private boolean isAct=false;
	
	public NetWorkBroadcastReceiver(HomePageActivity activity){
		this.activity=activity;
		isAct=false;
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		ConnectivityManager manager=(ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo mNetworkInfo=manager.getActiveNetworkInfo();
		if(mNetworkInfo!=null){
			if(mNetworkInfo.isConnected()){
				if(isAct==false){
					System.out.println("this is firsttttttttttt time");
					isAct=true;
				}
				else{
					SocketSingleton.close();
					activity.initReader();
					activity.setNetWorkStatus();
					PreferenceData pd=new PreferenceData(context);
					Map<String, Object> map=new HashMap<String, Object>();
					map=pd.getData(new String[]{"phone", "passwords"});
					if((!map.get("phone").toString().isEmpty()) && (!map.get("passwords").toString().isEmpty())){
						map.put("request", "user login");
						map.put("date", Tools.getDate1());
						map.put("rid", JPushInterface.getRegistrationID(activity.getApplicationContext()));
						SQLHelper helper=SQLHelper.getInstance(activity.getApplicationContext());
						Cursor cursor=helper.select("ich", new String[]{"image"}, "phone=?", new String[]{"host"}, null);
						if(cursor.moveToNext()){
							String imgPath=cursor.getString(0);
							if(imgPath!=null && Tools.isFileExist(imgPath)!=false){
								map.put("img", "1");
							}
						}
						Runnable r=new ClientWrite(Tools.JsonEncode(map));
						new Thread(r).start();
					}
					isAct=false;
					System.out.println("this is secondddddddd time");
				}
			}
			
		}
		else{
			System.out.println("�϶϶϶϶϶϶϶϶϶϶϶�");
			activity.runOnUiThread(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					FragmentManager fm=activity.fm;
					if(fm.findFragmentById(R.id.title)!=null && fm.findFragmentById(R.id.title) instanceof TitleFragment){
						TitleFragment tmp=(TitleFragment)fm.findFragmentById(R.id.title);
						tmp.setNetWorkCheckOpen(true);
					}
					
					if(activity.viewPager!=null && activity.fragmentList1!=null && activity.fragmentList1.size()>0){
						Iterator<Fragment> iter=activity.fragmentList1.iterator();
						while(iter.hasNext()){
							Fragment f=iter.next();
							if(f instanceof TeamFragment){
								TeamFragment tmp=(TeamFragment)f;
								tmp.setNetWorkCheckOpen(true);
							}
						}
					}
					
				}
				
			});
			SocketSingleton.resetInstance();
			isAct=true;
		}
	}

	
}
