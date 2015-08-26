package com.example.kickfor;

import java.io.InputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;

import com.example.kickfor.lobby.LobbyTeamEntity;
import com.example.kickfor.more.SearchItemEntity;
import com.example.kickfor.team.HonorInfo;
import com.example.kickfor.team.MatchReviewEntity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class ClientReader implements Runnable{
	private Socket socket;
	private Handler handler;
	private InputStream in;
	private String message=null;
	private Context context=null;
	private boolean ing=true;
	
	private long reConnectedTime=0;
	private static Timer mTimer=null;
	
	private void setTimerTask(){
		mTimer.schedule(new TimerTask(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				SocketSingleton.close();
				HomePageActivity c=(HomePageActivity)context;
				c.initReader();
				c.setNetWorkStatus();
				SQLHelper helper=SQLHelper.getInstance(context);
				PreferenceData pd=new PreferenceData(context);
				Map<String, Object> map=pd.getData(new String[]{"phone", "passwords"});
				String phone=map.get("phone").toString();
				String passwords=map.get("passwords").toString();
				if((!phone.isEmpty()) && !(passwords.isEmpty())){
					Map<String, Object> tmp=new HashMap<String, Object>();
					tmp.put("request", "user login");
					tmp.put("date", Tools.getDate1());
					tmp.put("phone", phone);
					tmp.put("passwords", passwords);
					tmp.put("rid", JPushInterface.getRegistrationID(context));
					Cursor cursor=helper.select("ich", new String[]{"image"}, "phone=?", new String[]{"host"}, null);
					if(cursor.moveToNext()){
						String imgPath=cursor.getString(0);
						if(imgPath!=null && Tools.isFileExist(imgPath)!=false){
							tmp.put("img", "1");
						}
					}
					Runnable r=new ClientWrite(Tools.JsonEncode(tmp));
					new Thread(r).start();
					//Tools.loginHuanXin(phone, passwords);
				}
			}
			
		}, reConnectedTime, 10000);
		
	}
	
	public ClientReader(Handler handler, Context context){
		this.handler=handler;
		this.context=context;
	}
	
	
	public void run(){
		try{
			this.socket=SocketSingleton.getInstance().getSocket();
			in=socket.getInputStream();
			while(ing){
				byte[] tmp=new byte[8];
				int size=0;
				while((size=size+in.read(tmp, size, 8-size))!=8 && size!=-1){
				}
				if(size==-1){
					System.out.println("inputstream断开");
					break;
				}
				System.out.println(new String(tmp, "UTF-8"));
				int length=Tools.JsonGetLength(tmp);
				System.out.println("length="+length);
				byte[] bytes=new byte[length];
				size=0;
				while((size=size+in.read(bytes, size, length-size))!=length){
					
				}
				
				if(mTimer!=null){
					mTimer.cancel();
					mTimer=null;
				}
				mTimer=new Timer();
				reConnectedTime=System.currentTimeMillis()+20000;
				setTimerTask();
				
				message=new String(bytes, "UTF-8");
				System.out.println(message);
				Map<String, Object> map=Tools.getMapForJson(message);
				String request=map.get("request").toString();
				System.out.println(request);
				
				switch(request){
				
				case "existed_phone":{
					Message msg=handler.obtainMessage();
					msg.what=HomePageActivity.EXISTED_PHONE;
					handler.sendMessage(msg);
					break;
				}
				
				
				case "ok_phone":{
					Bundle bundle=new Bundle();
					bundle.putString("phone", map.get("phone").toString());
					bundle.putString("passwords", map.get("passwords").toString());
					bundle.putString("code", map.get("code").toString());
					Message msg=handler.obtainMessage();
					msg.setData(bundle);
					msg.what=HomePageActivity.RESPONSE_PHONE;
					handler.sendMessage(msg);
					break;
				}
				
				case "ok_code":{
					Bundle bundle=new Bundle();
					bundle.putString("phone", map.get("phone").toString());
					bundle.putString("passwords", map.get("passwords").toString());
					Message msg=handler.obtainMessage();
					msg.setData(bundle);
					msg.what=HomePageActivity.RESPONSE_CODE;
					handler.sendMessage(msg);
					break;
				}
				
				case "ok_name":{
					Bundle bundle=new Bundle();
					bundle.putString("phone", map.get("phone").toString());
					Message msg=handler.obtainMessage();
					msg.setData(bundle);
					msg.what=HomePageActivity.RESPONSE_NAME;
					handler.sendMessage(msg);
					break;
				}
				
				case "ok_position1":{
					Bundle bundle=new Bundle();
					bundle.putString("phone", map.get("phone").toString());
					Message msg=handler.obtainMessage();
					msg.setData(bundle);
					msg.what=HomePageActivity.RESPONSE_POSITION1;
					handler.sendMessage(msg);
					break;
				}
				case "ok_position2":{
					Bundle bundle=new Bundle();
					bundle.putString("phone", map.get("phone").toString());
					Message msg=handler.obtainMessage();
					msg.setData(bundle);
					msg.what=HomePageActivity.RESPONSE_POSITION2;
					handler.sendMessage(msg);
					break;
				}
				
				case "ok_city":{
					Bundle bundle=new Bundle();
					bundle.putString("phone", map.get("phone").toString());
					bundle.putString("passwords", map.get("passwords").toString());
					Message msg=handler.obtainMessage();
					msg.setData(bundle);
					msg.what=HomePageActivity.RESPONSE_CITY;
					handler.sendMessage(msg);
					break;
				}
				
				case "get_code":{
					Message msg=handler.obtainMessage();
					msg.what=HomePageActivity.GET_CODE;
					msg.obj=map;
					handler.sendMessage(msg);
					break;
				}
				
				case "set_psw":{
					Message msg=handler.obtainMessage();
					msg.what=HomePageActivity.SET_PSW;
					msg.obj=map;
					handler.sendMessage(msg);
					break;
				}
				
				case "set_new_psw":{
					map.remove("request");
					PreferenceData pd=new PreferenceData(context);
					pd.save(map);
					Message msg=handler.obtainMessage();
					msg.what=HomePageActivity.SET_NEW_PSW;
					handler.sendMessage(msg);
					break;
				}
				
				case "log_out":{
					Message msg=handler.obtainMessage();
					msg.what=HomePageActivity.LOG_OUT;
					handler.sendMessage(msg);
					break;
				}
				case "FriendList":{
					SQLHelper helper=SQLHelper.getInstance(context);
					map.remove("request");
					Set<String> keys=map.keySet();
					Iterator<String> iter=keys.iterator();
					while(iter.hasNext()){
						String key=iter.next();
						Map<String, Object>temp=Tools.getMapForJson(map.get(key).toString());
						if(temp.containsKey("image")){
							String image=temp.get("image").toString();
							if(!(image.equals("-1"))){
								Bitmap bitmap=Tools.stringtoBitmap(image);
								if(Tools.hasSdcard()){
									String fileName=Environment.getExternalStorageDirectory().getPath()+"/KICKFOR/user/"+temp.get("phone").toString()+".png";
									System.out.println(fileName);
									Tools.saveBitmapToFile(bitmap, fileName);
									temp.put("image", fileName);
									System.out.println("image saved");
								}
								else{
									Toast.makeText(context, "未检测到SD卡", Toast.LENGTH_LONG).show();
									temp.put("image", "-1");
								}
							}
						}
						PreferenceData pd=new PreferenceData(context);
						if(temp.get("phone").equals(pd.getData(new String[]{"phone"}).get("phone"))){
							temp.put("phone", "host");
							if(temp.containsKey("userarchivesArray")){
								helper.delete("archive", null, null);
								helper.delete("archivematch", null, null);
								List<String> list=Tools.jsonToList(temp.get("userarchivesArray").toString());
								Iterator<String> iter1=list.iterator();
								while(iter1.hasNext()){
									Map<String, Object> tmpp=Tools.getMapForJson(iter1.next());
									List<String> list1=Tools.jsonToList(tmpp.get("matchArray").toString());
									Iterator<String> iter2=list1.iterator();
									while(iter2.hasNext()){
										Map<String, Object> t=Tools.getMapForJson(iter2.next());
										helper.update(Tools.getContentValuesFromMap(t, null), Integer.parseInt(t.get("userarchivesmatchkey").toString()), Integer.parseInt(t.get("userarchiveskey").toString()));
									}
									tmpp.remove("matchArray");
									helper.update(Tools.getContentValuesFromMap(tmpp, null), "archive", Integer.parseInt(tmpp.get("userarchiveskey").toString()));
								}
								temp.remove("userarchivesArray");
							}
							if(temp.containsKey("userSkillArray")){
								helper.delete("skills", null, null);
								List<String> list=Tools.jsonToList(temp.get("userSkillArray").toString());
								Iterator<String> iter2=list.iterator();
								while(iter2.hasNext()){
									Map<String, Object> tempp=Tools.getMapForJson(iter2.next());
									helper.insert(Tools.getContentValuesFromMap(tempp, null), "skills");
								}
								temp.remove("userSkillArray");
							}
							helper.update(Tools.getContentValuesFromMap(temp, null), "ich", temp.get("phone").toString());
							System.out.println(temp.get("phone"));
						}
						else{
							helper.update(Tools.getContentValuesFromMap(temp, null), "friends", temp.get("phone").toString());
						}
					}
					
					
					
					break;
				}
				case "delete_friend":{
					String phone=map.get("phone").toString();
					SQLHelper helper=SQLHelper.getInstance(context);
					helper.delete("friends", "phone=?", new String[]{phone});
					Message msg=handler.obtainMessage();
					msg.what=HomePageActivity.DELETE_FRIEND;
					handler.sendMessage(msg);
					break;
				}
				case "team_info":{
					String teamid=map.get("teamid").toString();
					map.remove("request");
					SQLHelper helper=SQLHelper.getInstance(context);
					Message msg=handler.obtainMessage();
					if(map.containsKey("str")){
						String str=map.get("str").toString();
						ContentValues cv=new ContentValues();
						cv.put("team"+str, teamid);
						helper.update(cv, "ich", "host");
						msg.arg1=Integer.parseInt(str);
						msg.what=HomePageActivity.INIT_TEAM;
						map.remove("str");
						Map<String, Object> temp=new HashMap<String, Object>();
						temp.put("result", "u");
						temp.put("type", String.valueOf(ListsFragment.TYPE_TEAMS_MESSAGE));
						temp.put("message", "accept_join");
						temp.put("name", "球队消息");
						temp.put("teamid", teamid);
						temp.put("id", "host");
						Cursor cursor=helper.select("systemtable", new String[]{"max(i)"}, null, null, null);
						if(cursor.moveToNext()){
							temp.put("i", cursor.getInt(0)+1);
						}
						else{
							temp.put("i", 1);
						}
						helper.update(Tools.getContentValuesFromMap(temp, null), "systemtable", (Integer)temp.get("i"));
						Message msg0=handler.obtainMessage();
						msg0.what=HomePageActivity.NEW_MESSAGE;
						handler.sendMessage(msg0);
					}
					else{
						msg.what=HomePageActivity.TEAM_INFO;
					}
					msg.obj=teamid;
					String image=map.get("image").toString();
					if(!image.equals("-1")){
						Bitmap bitmap=Tools.stringtoBitmap(image);
						if(Tools.hasSdcard()){
							String fileName=Environment.getExternalStorageDirectory().getPath()+"/KICKFOR/teams/"+map.get("teamid").toString()+".png";
							System.out.println(fileName);
							Tools.saveBitmapToFile(bitmap, fileName);
							map.put("image", fileName);
						}
						else{
							Toast.makeText(context, "未检测到SD卡", Toast.LENGTH_LONG).show();
						}
					}
					helper.update(Tools.getContentValuesFromMap(map, null), "teams", teamid);				
					handler.sendMessage(msg);
					break;
				}
				
				case "team mate":{
					SQLHelper helper=SQLHelper.getInstance(context);
					String teamid=map.get("teamid").toString();
					String tableName="f_"+teamid;
					String phone=null;
					helper.createTeamMateTable(tableName);
					map.remove("request");
					map.remove("teamid");
					Iterator<String> iter=map.keySet().iterator();
					while(iter.hasNext()){
						String key=iter.next();
						Map<String, Object> temp=Tools.getMapForJson(map.get(key).toString());
						phone=temp.get("phone").toString();
						helper.update(Tools.getContentValuesFromMap(temp, null), tableName, phone);	
					}
					if(map.size()==1){
						Bundle bundle=new Bundle();
						bundle.putString("teamid", teamid);
						bundle.putString("phone", phone);
						Message msg=handler.obtainMessage();
						msg.what=HomePageActivity.TEAM_MATE;
						msg.setData(bundle);
						handler.sendMessage(msg);
					}
					break;
				}
				case "login_success":{
					Bundle bundle=new Bundle();
					SQLHelper helper=SQLHelper.getInstance(context);
					Cursor cursor=helper.select("ich", new String[]{"team1", "team2", "team3", "authority1", "authority2", "authority3"},  "phone=?", new String[]{"host"}, null);
					cursor.moveToNext();
					System.out.println("team1id="+cursor.getString(0));
					bundle.putString("teamid1", cursor.getString(0));
					bundle.putString("teamid2", cursor.getString(1));
					bundle.putString("teamid3", cursor.getString(2));
					bundle.putString("authority1", cursor.getString(3));
					bundle.putString("authority2", cursor.getString(4));
					bundle.putString("authority3", cursor.getString(5));
					Message msg=handler.obtainMessage();
					msg.setData(bundle);
					msg.what=HomePageActivity.LOGIN_SUCCESS;
					handler.sendMessage(msg);
					
					AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
					Intent intent=new Intent(context, com.example.kickfor.service.CheckPhotoService.class);
					PendingIntent tmpPending=PendingIntent.getService(context, 4, intent, PendingIntent.FLAG_UPDATE_CURRENT);
					
					long setTime=System.currentTimeMillis();
					
					am.set(AlarmManager.RTC_WAKEUP, setTime, tmpPending);
					break;
				}
				case "sign_success":{
					map.remove("request");
					String score=map.get("score").toString();
					String addup=map.get("addup").toString();
					SQLHelper helper=SQLHelper.getInstance(context);
					helper.update(Tools.getContentValuesFromMap(map, null), "ich", "host");
					Bundle bundle=new Bundle();
					bundle.putString("score", score);
					bundle.putString("addup", addup);
					Message msg=handler.obtainMessage();
					msg.setData(bundle);
					msg.what=HomePageActivity.SIGN_SUCCESS;
					handler.sendMessage(msg);
					break;
				}
				case "have_signed":{
					map.remove("request");
					String score=map.get("score").toString();
					String addup=map.get("addup").toString();
					SQLHelper helper=SQLHelper.getInstance(context);
					helper.update(Tools.getContentValuesFromMap(map, null), "ich", "host");
					Bundle bundle=new Bundle();
					bundle.putString("score", score);
					bundle.putString("addup", addup);
					Message msg=handler.obtainMessage();
					msg.setData(bundle);
					msg.what=HomePageActivity.SIGN_SUCCESS;
					handler.sendMessage(msg);
					break;
				}
				case "error_phone_passwords":{
					Message msg =handler.obtainMessage();
					msg.what = HomePageActivity.ERROR_LOGIN;
					handler.sendMessage(msg);
					break;
				}
				case "created_team":{
					Message msg =handler.obtainMessage();
					msg.what = HomePageActivity.TEAM_CREATED;
					handler.sendMessage(msg);
					break;
				}
				case "uploaded_photo":{
					SQLHelper helper=SQLHelper.getInstance(context);
					String image=map.get("image").toString();
					String phone=map.get("phone").toString();
					map.remove("request");
					map.remove("phone");
					if(!(image.equals("-1"))){
						Bitmap bitmap=Tools.stringtoBitmap(image);
						if(Tools.hasSdcard()){
							String fileName=Environment.getExternalStorageDirectory().getPath()+"/KICKFOR/user/"+phone+".png";
							System.out.println(fileName);
							Tools.saveBitmapToFile(bitmap, fileName);
							map.put("image", fileName);
							System.out.println("image saved");
						}
						else{
							Toast.makeText(context, "未检测到SD卡", Toast.LENGTH_LONG).show();
							map.put("image", "-1");
						}
					}
					helper.update(Tools.getContentValuesFromMap(map, null), "ich", "host");
					
					Message msg=handler.obtainMessage();
					msg.what=HomePageActivity.UPLOADED_PHOTO;
					handler.sendMessage(msg);
					break;
				}
				case "update_photo":{
					map.remove("request");
					SQLHelper helper=SQLHelper.getInstance(context);
					String phone=map.get("phone").toString();
					String image=map.get("image").toString();
					if(!(image.equals("-1"))){
						Bitmap bitmap=Tools.stringtoBitmap(image);
						if(Tools.hasSdcard()){
							String fileName=Environment.getExternalStorageDirectory().getPath()+"/KICKFOR/user/"+phone+".png";
							System.out.println(fileName);
							Tools.saveBitmapToFile(bitmap, fileName);
							map.put("image", fileName);
							System.out.println("image saved");
						}
						else{
							Toast.makeText(context, "未检测到SD卡", Toast.LENGTH_LONG).show();
							map.put("image", "-1");
						}
					}
					helper.update(Tools.getContentValuesFromMap(map, null), "friends", phone);
					break;
				}
				case "completed_information":{
					Message msg=handler.obtainMessage();
					if(map.containsKey("team1")){
						Bundle bundle=new Bundle();
						map.remove("request");
						SQLHelper helper=SQLHelper.getInstance(context);
						helper.update(Tools.getContentValuesFromMap(map, null), "ich", "host");
						Cursor cursor=helper.select("ich", new String[]{"team1", "team2", "team3", "authority1", "authority2", "authority3"},  "phone=?", new String[]{"host"}, null);
						cursor.moveToNext();
						bundle.putString("teamid1", cursor.getString(0));
						bundle.putString("teamid2", cursor.getString(1));
						bundle.putString("teamid3", cursor.getString(2));
						bundle.putString("authority1", cursor.getString(3));
						bundle.putString("authority2", cursor.getString(4));
						bundle.putString("authority3", cursor.getString(5));
						msg.setData(bundle);
						msg.arg1=1;
					}
					else{
						msg.arg1=0;
					}
					msg.what=HomePageActivity.COMPLETED_INFORMATION;
					handler.sendMessage(msg);
					break;
				}
				case "update_info":{
					map.remove("request");
					SQLHelper helper=SQLHelper.getInstance(context);
					helper.update(Tools.getContentValuesFromMap(map, null), "friends", map.get("phone").toString());
					break;
				}
				case "completed_position":{
					SQLHelper helper=SQLHelper.getInstance(context);
					if(map.get("position1")!=null){
						ContentValues cv=new ContentValues();
						cv.put("position1", map.get("position1").toString());
						helper.update(cv, "ich", "host");
					}
					if(map.get("position2")!=null){
						ContentValues cv=new ContentValues();
						cv.put("position2", map.get("position2").toString());
						helper.update(cv, "ich", "host");
					}
					Message msg=handler.obtainMessage();
					msg.what=HomePageActivity.COMPLETED_POSITION;
					handler.sendMessage(msg);
					break;
				}
				
				case "seeked":{
					List<SearchItemEntity> mList=new ArrayList<SearchItemEntity>();
					map.remove("request");
					Set<String> keys=map.keySet();
					Iterator<String> iter=keys.iterator();
					while(iter.hasNext()){
						String key=iter.next();
						Map<String, Object>temp=Tools.getMapForJson(map.get(key).toString());
						Bitmap bitmap;
						String image=temp.get("image").toString();
						if(!(image.equals("-1"))){
							bitmap=Tools.stringtoBitmap(image);	
						}
						else{
							bitmap=BitmapFactory.decodeResource(context.getResources(), R.drawable.team_default);
						}
						boolean b;
						if(temp.get("type").equals("0")){
							b=false;
							SearchItemEntity entity=new SearchItemEntity(b, temp.get("phone").toString());
							entity.setPersonData(temp.get("name").toString(), temp.get("city").toString(), temp.get("year").toString(), temp.get("team1").toString(), temp.get("team2").toString(), temp.get("team3").toString(), temp.get("position1").toString(), temp.get("position2").toString(), bitmap);
							mList.add(entity);
						}
						else{
							b=true;
							SearchItemEntity entity=new SearchItemEntity(b, temp.get("teamid").toString());
							entity.setTeamData(temp.get("name").toString(), temp.get("city").toString(), temp.get("year").toString(), temp.get("number").toString(), bitmap);
							if(temp.containsKey("captain")){
								entity.setCaptain(temp.get("captain").toString());
							}
							mList.add(entity);
						}
					}
					Message msg=handler.obtainMessage();
					msg.what=HomePageActivity.SEEKED;
					msg.obj=mList;
					handler.sendMessage(msg);
					break;
				}
				case "apply_friend":{
					SQLHelper helper=SQLHelper.getInstance(context);
					String phone=map.get("phone").toString();
					Cursor cursor0=helper.select("friends", new String[]{"phone"}, "phone=?", new String[]{phone}, null);
					if(!cursor0.moveToNext()){
						Map<String, Object> temp=new HashMap<String, Object>();
						temp.put("id", phone);
						temp.put("type", String.valueOf(ListsFragment.TYPE_FRIEND_APPLY));
						temp.put("name", map.get("name").toString());
						temp.put("date", map.get("date").toString());
						temp.put("message", "好友申请");
						temp.put("result", "u");
						Cursor cursor1=helper.select("systemtable", new String[]{"i"}, "type=? and id=?", new String[]{String.valueOf(ListsFragment.TYPE_FRIEND_APPLY), phone}, null);
						if(cursor1.moveToNext()){
							temp.put("i", cursor1.getInt(0));
						}
						else{
							Cursor cursor=helper.select("systemtable", new String[]{"max(i)"}, null, null, null);
							if(cursor.moveToNext()){
								int maxid=cursor.getInt((cursor.getColumnIndex("max(i)")));
								temp.put("i", maxid+1);
							}
							else{
								temp.put("i", 1);
							}
						}
						String image=map.get("image").toString();
						if(!(image.equals("-1"))){
							Bitmap bitmap=Tools.stringtoBitmap(image);
							if(Tools.hasSdcard()){
								String fileName=Environment.getExternalStorageDirectory().getPath()+"/KICKFOR/system/"+temp.get("id").toString()+".png";
								Tools.saveBitmapToFile(bitmap, fileName);
								temp.put("image", fileName);
							}
							else{
								Toast.makeText(context, "未检测到SD卡", Toast.LENGTH_LONG).show();
								temp.put("image", "-1");
							}
						}
						else{
							temp.put("image", "-1");
						}
						helper.update(Tools.getContentValuesFromMap(temp, null), "systemtable", (Integer)temp.get("i"));
						Message msg0=handler.obtainMessage();
						msg0.what=HomePageActivity.NEW_MESSAGE;
						handler.sendMessage(msg0);
						Message msg=handler.obtainMessage();
						msg.what=HomePageActivity.APPLY_FRIEND;
						handler.sendMessage(msg);
					}
					break;
				}
				case "friend_list_update":{
					SQLHelper helper=SQLHelper.getInstance(context);
					map.remove("request");
					if(map.containsKey("image")){
						String image=map.get("image").toString();
						map.remove("image");
						if(!(image.equals("-1"))){
							Bitmap bitmap=Tools.stringtoBitmap(image);
							if(Tools.hasSdcard()){
								String fileName=Environment.getExternalStorageDirectory().getPath()+"/KICKFOR/user/"+map.get("phone").toString()+".png";
								Tools.saveBitmapToFile(bitmap, fileName);
								map.put("image", fileName);
							}
							else{
								Toast.makeText(context, "未检测到SD卡", Toast.LENGTH_LONG).show();
								map.put("image", "-1");
							}
						}
					}
					else{
						map.put("image", "-1");
					}
					if(map.get("phone").equals(new PreferenceData(context).getData(new String[]{"phone"}).get("phone"))){
						map.put("phone", "host");
						helper.update(Tools.getContentValuesFromMap(map, null), "ich", map.get("phone").toString());
					}
					else{
						helper.update(Tools.getContentValuesFromMap(map, null), "friends", map.get("phone").toString());
						map.put("date", Tools.getDate());
						map.put("type", ListsFragment.TYPE_FRIEND_MESSAGE);
						map.put("id", map.get("phone").toString());
						map.remove("phone");
						map.put("message", "我们已经是好友了");
						map.put("result", "u");
						Cursor cursor=helper.select("systemtable", new String[]{"max(i)"}, null, null, null);
						if(cursor.moveToNext()){
							int maxid=cursor.getInt((cursor.getColumnIndex("max(i)")));
							map.put("i", maxid+1);
						}
						helper.update(Tools.getContentValuesFromMap(map, null), "systemtable", Integer.parseInt(map.get("i").toString()));
					}
					AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
					Intent intent=new Intent(context, com.example.kickfor.service.CheckPhotoService.class);
					PendingIntent tmpPending=PendingIntent.getService(context, 4, intent, PendingIntent.FLAG_UPDATE_CURRENT);
					
					long setTime=System.currentTimeMillis();
					
					am.set(AlarmManager.RTC_WAKEUP, setTime, tmpPending);
					Message msg=handler.obtainMessage();
					msg.what=HomePageActivity.UPDATE_FRIEND_LIST;
					handler.sendMessage(msg);
					break;
				}
				case "seeked_team":{
					map.remove("request");
					Iterator<String> iter=map.keySet().iterator();
					List<SearchItemEntity> mList=new ArrayList<SearchItemEntity>();
					while(iter.hasNext()){
						String key=iter.next();
						Map<String, Object> temp=Tools.getMapForJson(map.get(key).toString());
						Bitmap bitmap;
						String image=temp.get("image").toString();
						if(!(image.equals("-1"))){
							bitmap=Tools.stringtoBitmap(image);	
						}
						else{
							bitmap=BitmapFactory.decodeResource(context.getResources(), R.drawable.team_default);
						}
						SearchItemEntity entity=new SearchItemEntity(SearchItemEntity.TEAM_SEARCH, temp.get("teamid").toString());
						entity.setTeamData(temp.get("name").toString(), temp.get("city").toString(), temp.get("year").toString(), temp.get("number").toString(), bitmap);
						if(temp.containsKey("captain")){
							entity.setCaptain(temp.get("captain").toString());
						}
						mList.add(entity);
					}
					Message msg=handler.obtainMessage();
					msg.obj=mList;
					msg.what=HomePageActivity.SEEKED_TEAM;
					handler.sendMessage(msg);
					break;
				}
				case "updated_match":{
					map.remove("request");
					SQLHelper helper=SQLHelper.getInstance(context);
					String teamid=map.get("teamid").toString();
					String id=map.get("id").toString();
					boolean isExisted=helper.update(Tools.getContentValuesFromMap(map, null), "matches", Integer.parseInt(map.get("id").toString()), teamid);
					
					Map<String, Object> temp=new HashMap<String, Object>();
					temp.put("result", "u");
					temp.put("type", String.valueOf(ListsFragment.TYPE_TEAMS_MESSAGE));
					temp.put("id", id);
					temp.put("message", isExisted? "update_new_match":"publish_new_match");
					temp.put("teamid", teamid);
					temp.put("name", "比赛预告");
					Cursor cursor=helper.select("systemtable", new String[]{"i"}, "id=? and teamid=? and (message=? or message=?)", new String[]{id, teamid, "publish_new_match", "update_new_match"}, null);
					if(cursor.moveToNext()){
						temp.put("i", cursor.getInt(0));
					}
					else{
						Cursor cursor1=helper.select("systemtable", new String[]{"max(i)"}, null, null, null);
						if(cursor1.moveToNext()){
							temp.put("i", cursor1.getInt(0)+1);
						}
						else{
							temp.put("i", 1);
						}
					}
					helper.update(Tools.getContentValuesFromMap(temp, null), "systemtable", (Integer)temp.get("i"));
					Message msg0=handler.obtainMessage();
					msg0.what=HomePageActivity.NEW_MESSAGE;
					handler.sendMessage(msg0);
					
					Bundle bundle=new Bundle();
					bundle.putString("request", "process unkicked match");
					bundle.putString("teamid", teamid);
					bundle.putString("id", id);
					Intent intent=new Intent(context, com.example.kickfor.service.ActionService.class);
					intent.putExtra("info", bundle);
					PendingIntent tmpPending=PendingIntent.getService(context, Integer.parseInt(Tools.getIndex(context, teamid, "host")), intent, PendingIntent.FLAG_UPDATE_CURRENT);
					long setTime=Tools.getLongFormatDateAndTime(map.get("date").toString(), map.get("time").toString());
					AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
					am.set(AlarmManager.RTC_WAKEUP, setTime, tmpPending);	
					Message msg=handler.obtainMessage();
					msg.obj=teamid;
					msg.what=HomePageActivity.UPDATED_MATCH;
					handler.sendMessage(msg);
					break;
				}
				case "update_match_remind":{
					SQLHelper helper=SQLHelper.getInstance(context);
					String teamid=map.get("teamid").toString();
					String id=map.get("id").toString();
					boolean isExisted=(helper.select("matches", new String[]{"id"}, "id=?", new String[]{id}, null)).moveToNext();
					Map<String, Object> temp=new HashMap<String, Object>();
					temp.put("result", "u");
					temp.put("type", String.valueOf(ListsFragment.TYPE_TEAMS_MESSAGE));
					temp.put("id", id);
					temp.put("message", isExisted? "update_new_match":"publish_new_match");
					temp.put("teamid", teamid);
					temp.put("name", "比赛预告");
					Cursor cursor=helper.select("systemtable", new String[]{"i"}, "id=? and teamid=? and (message=? or message=?)", new String[]{id, teamid, "publish_new_match", "update_new_match"}, null);
					if(cursor.moveToNext()){
						temp.put("i", cursor.getInt(0));
					}
					else{
						Cursor cursor1=helper.select("systemtable", new String[]{"max(i)"}, null, null, null);
						if(cursor1.moveToNext()){
							temp.put("i", cursor1.getInt(0)+1);
						}
						else{
							temp.put("i", 1);
						}
					}
					helper.update(Tools.getContentValuesFromMap(temp, null), "systemtable", (Integer)temp.get("i"));
					Message msg0=handler.obtainMessage();
					msg0.what=HomePageActivity.NEW_MESSAGE;
					handler.sendMessage(msg0);
					break;
				}
				case "match_info":{
					map.remove("request");
					SQLHelper helper=SQLHelper.getInstance(context);
					helper.delete("matches", null, null);
					AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
					Iterator<String> iter=map.keySet().iterator();
					int index=0;
					Bundle bundle=new Bundle();
					while(iter.hasNext()){
						String key=iter.next();
						Map<String, Object> temp=Tools.getMapForJson(map.get(key).toString());
						helper.update(Tools.getContentValuesFromMap(temp, null), "matches", Integer.parseInt(temp.get("id").toString()), temp.get("teamid").toString());
						bundle.clear();
						bundle.putString("request", "process unkicked match");
						bundle.putString("teamid", temp.get("teamid").toString());
						bundle.putString("id", temp.get("id").toString());
						Intent intent=new Intent(context, com.example.kickfor.service.ActionService.class);
						intent.putExtra("info", bundle);
						PendingIntent tmpPending=PendingIntent.getService(context, index, intent, PendingIntent.FLAG_UPDATE_CURRENT);
						
						long setTime=Tools.getLongFormatDateAndTime(temp.get("date").toString(), temp.get("time").toString());
						
						am.set(AlarmManager.RTC_WAKEUP, setTime, tmpPending);
						index=index+1;
					}
					break;
				}
				case "processed_unkicked_match":{
					String teamid=map.get("teamid").toString();
					String id=map.get("id").toString();
					SQLHelper helper=SQLHelper.getInstance(context);
					helper.delete("matches", "id=? and teamid=?", new String[]{id, teamid});
					break;
				}
				case "deny_authority":{
					String teamid=map.get("teamid").toString();
					String phone=map.get("phone").toString();
					Bundle bundle=new Bundle();
					bundle.putString("phone", phone);
					bundle.putString("teamid", teamid);
					Message msg=handler.obtainMessage();
					msg.setData(bundle);
					msg.what=HomePageActivity.DENY_AUTHORITY;
					handler.sendMessage(msg);
					break;
				}
				case "decline_authority":{
					String phone=map.get("phone").toString();
					String teamid=map.get("teamid").toString();
					map.remove("request");
					map.remove("teamid");
					map.put("authority", '0');
					String table="f_"+teamid;
					SQLHelper helper=SQLHelper.getInstance(context);
					helper.update(Tools.getContentValuesFromMap(map, null), table, phone);
					Bundle bundle=new Bundle();
					bundle.putString("phone", phone);
					bundle.putString("teamid", teamid);
					Message msg=handler.obtainMessage();
					msg.setData(bundle);
					msg.what=HomePageActivity.DECLINE_AUTHORITY;
					handler.sendMessage(msg);
					break;
				}
				case "fuck_off":{
					SQLHelper helper=SQLHelper.getInstance(context);
					Bundle bundle=new Bundle();
					String teamid=map.get("teamid").toString();
					String phone=map.get("phone").toString();
					bundle.putString("teamid", teamid);
					bundle.putString("phone", phone);
					String tableName="f_"+teamid;
					helper.createTeamMateTable(tableName);
					helper.delete(tableName, "phone=?", new String[]{phone});
					String name=map.get("name").toString();
					if(!name.equals("-1")){
						Map<String, Object> temp=new HashMap<String, Object>();
						temp.put("result", "u");
						temp.put("type", String.valueOf(ListsFragment.TYPE_TEAMS_MESSAGE));
						temp.put("id", phone);
						temp.put("message", "some_one_left");
						temp.put("teamid", teamid);
						temp.put("name", name);
						Cursor cursor=helper.select("systemtable", new String[]{"max(id)"}, null, null, null);
						if(cursor.moveToNext()){
							temp.put("i", cursor.getInt(0)+1);
						}
						else{
							temp.put("i", 1);
						}
						helper.insert(Tools.getContentValuesFromMap(temp, null), "systemtable");
						Message msg0=handler.obtainMessage();
						msg0.what=HomePageActivity.NEW_MESSAGE;
						handler.sendMessage(msg0);
					}
					Message msg=handler.obtainMessage();
					msg.what=HomePageActivity.FUCK_OFF;
					msg.setData(bundle);
					handler.sendMessage(msg);
					break;
				}
				case "be_fucked":{
					Bundle bundle=new Bundle();
					String teamid=map.get("teamid").toString();
					SQLHelper helper=SQLHelper.getInstance(context);
					String index=Tools.getIndex(context, teamid, "host");
					System.out.println("team index======"+index);
					Map<String, Object> tmp1=new HashMap<String, Object>();
					tmp1.put("team"+index, "");
					tmp1.put("tmatch"+index, '0');
					tmp1.put("authority"+index, '0');
					tmp1.put("win"+index, '0');
					tmp1.put("goal"+index, '0');
					tmp1.put("assist"+index, '0');
					helper.update(Tools.getContentValuesFromMap(tmp1, null), "ich", "host");
					helper.delete("matches", "teamid=?", new String[]{teamid});
					helper.delete("teams", "teamid=?", new String[]{teamid});
					helper.dropTable("f_"+teamid);
					helper.dropTable("u_"+teamid);
					helper.delete("systemtable", "teamid=?", new String[]{teamid});
					Message msg0=handler.obtainMessage();
					msg0.what=HomePageActivity.NEW_MESSAGE;
					handler.sendMessage(msg0);
					Cursor cursor=helper.select("ich", new String[]{"team1", "team2", "team3", "authority1", "authority2", "authority3"},  "phone=?", new String[]{"host"}, null);
					if(cursor.moveToNext()){
						bundle.putString("teamid1", cursor.getString(0));
						bundle.putString("teamid2", cursor.getString(1));
						bundle.putString("teamid3", cursor.getString(2));
						bundle.putString("authority1", cursor.getString(3));
						bundle.putString("authority2", cursor.getString(4));
						bundle.putString("authority3", cursor.getString(5));
					}
					bundle.putString("current", teamid);
					Message msg=handler.obtainMessage();
					msg.setData(bundle);
					msg.what=HomePageActivity.BE_FUCKED;
					handler.sendMessage(msg);
					break;
				}
				case "get_grade":{
					System.out.println("treat");
					String teamid=map.get("teamid").toString();
					String p1=map.get("p1").toString();
					String p2=map.get("p2").toString();
					String p3=map.get("p3").toString();
					String p4=map.get("p4").toString();
					map.remove("request");
					map.remove("teamid");
					map.remove("p1");
					map.remove("p2");
					map.remove("p3");
					map.remove("p4");
					List<HonorInfo> mList=new ArrayList<HonorInfo>();
					Iterator<String> iter=map.keySet().iterator();
					while(iter.hasNext()){
						String index=iter.next();
						Map<String, Object> temp=Tools.getMapForJson(map.get(index).toString());
						HonorInfo item=new HonorInfo(Integer.parseInt(temp.get("id").toString()));
						System.out.println(index);
						item.setYear(temp.get("year").toString());
						item.setName(temp.get("name").toString());
						item.setResult(temp.get("result").toString());
						item.setUpdateYear(temp.get("year").toString());
						item.setUpdateName(temp.get("name").toString());
						item.setUpdateResult(temp.get("result").toString());
						mList.add(item);
					}
					Iterator<HonorInfo> iter1=mList.iterator();
					while(iter1.hasNext()){
						System.out.println(iter1.next().getYear());
					}
					Bundle bundle=new Bundle();
					bundle.putString("p1", p1);
					bundle.putString("p2", p2);
					bundle.putString("p3", p3);
					bundle.putString("p4", p4);
					bundle.putString("teamid", teamid);
					Message msg=handler.obtainMessage();
					msg.obj=mList;
					msg.setData(bundle);
					msg.what=HomePageActivity.GET_TEAM_HONOR;
					handler.sendMessage(msg);
					System.out.println("treateddddd");
					break;
				}
				case "evaluation_result":{
					PreferenceData pd=new PreferenceData(context);
					boolean isEvaluate=false;
					int n=0;
					Message msg=handler.obtainMessage();
					Bundle bundle=new Bundle();
					if(map.get("phone").equals(pd.getData(new String[]{"phone"}).get("phone"))){
						map.put("phone", "host");
						String phone=map.get("phone").toString();
						n=Integer.parseInt(map.get("n").toString());
						if(map.get("e").toString().equals("1")){
							isEvaluate=true;
						}
						else if(map.get("e").toString().equals("0")){
							isEvaluate=false;
						}
						bundle.putString("phone", phone);
						bundle.putBoolean("isEvaluate", isEvaluate);
						bundle.putInt("n", n);
						msg.setData(bundle);
						map.remove("n");
						map.remove("e");
						map.remove("request");
						map.remove("phone");
						SQLHelper helper=SQLHelper.getInstance(context);
						helper.update(Tools.getContentValuesFromMap(map, null), "ich", phone);
					}	
					else{
						String phone=map.get("phone").toString();
						bundle.putString("phone", phone);
						if(map.get("evaluate").toString().equals("1")){
							isEvaluate=true;
						}
						else if(map.get("evaluate").toString().equals("0")){
							isEvaluate=false;
						}
						bundle.putBoolean("isEvaluate", isEvaluate);
						bundle.putInt("power", Integer.parseInt(map.get("power").toString()));
						bundle.putInt("defence", Integer.parseInt(map.get("defence").toString()));
						bundle.putInt("speed", Integer.parseInt(map.get("speed").toString()));
						bundle.putInt("stamina", Integer.parseInt(map.get("stamina").toString()));
						bundle.putInt("attack", Integer.parseInt(map.get("attack").toString()));
						bundle.putInt("skills", Integer.parseInt(map.get("skills").toString()));
						msg.setData(bundle);	
					}
					msg.what=HomePageActivity.EVALUATION_RESULT;
					handler.sendMessage(msg);
					break;
				}
				case "get_ones_match":{
					String teamid=map.get("teamid").toString();
					int index=Integer.parseInt(map.get("index").toString());
					map.remove("request");
					map.remove("teamid");
					map.remove("index");
					Iterator<String> iter=map.keySet().iterator();
					List<MyMatchEntity> list=new ArrayList<MyMatchEntity>();
					while(iter.hasNext()){
						Map<String, Object> temp=Tools.getMapForJson(map.get(iter.next()).toString());
						MyMatchEntity entity=new MyMatchEntity(temp.get("teamid").toString(), Integer.parseInt(temp.get("id").toString()));
						entity.setData(temp.get("date").toString(), temp.get("againstname").toString(), temp.get("goals").toString(), temp.get("lost").toString(), temp.get("goal").toString(), temp.get("assist").toString());
						list.add(entity);
					}
					Message msg=handler.obtainMessage();
					msg.what=HomePageActivity.GET_ONES_MATCH;
					msg.obj=list;
					Bundle bundle=new Bundle();
					bundle.putString("teamid", teamid);
					bundle.putInt("index", index);
					msg.setData(bundle);
					handler.sendMessage(msg);
					break;
				}
				case "evaluate_info":{
					String phone=map.get("phone").toString();
					if(phone.equals(new PreferenceData(context).getData(new String[]{"phone"}).get("phone").toString())){
						phone="host";
					}
					int n=0;
					boolean e=false;
					if(map.containsKey("n")){
						n=Integer.parseInt(map.get("n").toString());
						if(map.get("e").toString().equals("0")){
							e=false;
						}
						else if(map.get("e").toString().equals("1")){
							e=true;
						}
					}
					if(map.containsKey("evaluate")){
						if(map.get("evaluate").toString().equals("0")){
							e=false;
						}
						else if(map.get("evaluate").toString().equals("1")){
							e=true;
						}
					}
					Bundle bundle=new Bundle();
					bundle.putString("phone", phone);
					bundle.putBoolean("isEvaluate", e);
					bundle.putInt("n", n);
					Message msg=handler.obtainMessage();
					msg.setData(bundle);
					msg.what=HomePageActivity.EVALUATE_INFO;
					handler.sendMessage(msg);
					break;
				}
				
				case "seeked_one_info":{
					map.remove("request");
					Message msg=handler.obtainMessage();
					msg.obj=map;
					msg.what=HomePageActivity.SEEKED_ONE_INFO;
					handler.sendMessage(msg);
					break;
				}
				case "seeked_one_team":{
					map.remove("request");
					Message msg=handler.obtainMessage();
					msg.obj=map;
					msg.what=HomePageActivity.SEEKED_ONE_TEAM;
					handler.sendMessage(msg);
					break;
				}
				case "apply_join":{
					SQLHelper helper=SQLHelper.getInstance(context);
					String teamid=map.get("teamid").toString();
					String phone=map.get("phone").toString();
					String table="f_"+teamid;
					Cursor cursor1=helper.select(table, new String[]{"phone"}, "phone=?", new String[]{phone}, null);
					if(!cursor1.moveToNext()){
						Cursor cursor2=helper.select("systemtable", new String[]{"i"}, "id=? and teamid=? and type=? and result=? and message=?", new String[]{phone, teamid, String.valueOf(ListsFragment.TYPE_TEAMS_MESSAGE), "u", "apply_join"},  null);
						if(cursor2.moveToNext()){
							Map<String, Object> temp=new HashMap<String, Object>();
							temp.put("date", map.get("date").toString());
							helper.update(Tools.getContentValuesFromMap(temp, null), "systemtable", cursor2.getInt(0));
						}
						else{
							Map<String, Object> temp=new HashMap<String, Object>();
							Cursor cursor=helper.select("systemtable", new String[]{"max(i)"}, null, null, null);
							if(cursor.moveToNext()){
								temp.put("i", cursor.getInt(0)+1);
							}
							else{
								temp.put("i", 1);
							}
							temp.put("result", "u");
							temp.put("type", String.valueOf(ListsFragment.TYPE_TEAMS_MESSAGE));
							temp.put("id", phone);
							temp.put("image", "");
							temp.put("date", map.get("date").toString());
							String message="apply_join";
							temp.put("message", message);
							temp.put("name", map.get("name").toString());
							temp.put("teamid", teamid);
							helper.insert(Tools.getContentValuesFromMap(temp, null), "systemtable");
						}
						Message msg0=handler.obtainMessage();
						msg0.what=HomePageActivity.NEW_MESSAGE;
						handler.sendMessage(msg0);
					}
					Message msg=handler.obtainMessage();
					msg.what=HomePageActivity.APPLY_JOIN;
					msg.obj=teamid;
					handler.sendMessage(msg);
					break;
				}
				case "someone_join":{
					String teamid=map.get("teamid").toString();
					String phone=map.get("phone").toString();
					Bundle bundle=new Bundle();
					bundle.putString("teamid", teamid);
					bundle.putString("phone", phone);
					SQLHelper helper=SQLHelper.getInstance(context);
					Map<String, Object> temp=new HashMap<String, Object>();
					temp.put("result", "u");
					Cursor cursor1=helper.select("teams", new String[]{"name"}, "teamid=?", new String[]{teamid}, null);
					if(cursor1.moveToNext()){
						temp.put("name", "球队消息");
						temp.put("message", "some_one_join");
						temp.put("type", String.valueOf(ListsFragment.TYPE_TEAMS_MESSAGE));
						temp.put("teamid", teamid);
						temp.put("id", phone);
						Cursor cursor=helper.select("systemtable", new String[]{"i"}, "id=? and teamid=? and type=? and message=?", new String[]{phone, teamid, String.valueOf(ListsFragment.TYPE_TEAMS_MESSAGE), "apply_join"}, null);
						if(cursor.moveToNext()){
							temp.put("i", cursor.getInt(0));
						}
						else{
							Cursor cursor2=helper.select("systemtable", new String[]{"max(i)"}, null, null, null);
							if(cursor2.moveToNext()){
								temp.put("i", cursor2.getInt(0)+1);
							}
							else{
								temp.put("i", 1);
							}
						}
						helper.update(Tools.getContentValuesFromMap(temp, null), "systemtable", (Integer)temp.get("i"));
						Message msg0=handler.obtainMessage();
						msg0.what=HomePageActivity.NEW_MESSAGE;
						handler.sendMessage(msg0);
						Message msg=handler.obtainMessage();
						msg.setData(bundle);
						msg.what=HomePageActivity.SOMEONE_JOININ;
						handler.sendMessage(msg);
					}
					break;
				}
				case "refuse_join":{
					String phone=map.get("phone").toString();
					String teamid=map.get("teamid").toString();
					SQLHelper helper=SQLHelper.getInstance(context);
					helper.delete("systemtable", "id=? and teamid=? and type=? and message=?", new String[]{phone, teamid, String.valueOf(ListsFragment.TYPE_TEAMS_MESSAGE), "apply_join"});
					Bundle bundle=new Bundle();
					bundle.putString("phone", phone);
					bundle.putString("teamid", teamid);
					Message msg=handler.obtainMessage();
					msg.what=HomePageActivity.REFUSE_JOIN;
					msg.setData(bundle);
					handler.sendMessage(msg);
					break;
				}
				case "join_match":{
					SQLHelper helper=SQLHelper.getInstance(context);
					Map<String, Object> temp=new HashMap<String, Object>();
					String teamid=map.get("teamid").toString();
					Cursor cursor1=helper.select("teams", new String[]{"name"}, "teamid=?", new String[]{teamid}, null);
					if(cursor1.moveToNext()){
						String teamname=cursor1.getString(0);
						String againstName=map.get("againstname").toString();
						String info=map.get("info").toString();
						Cursor cursor=helper.select("systemtable", new String[]{"max(i)"}, null, null, null);
						if(cursor.moveToNext()){
							temp.put("i", cursor.getInt(0)+1);
						}
						else{
							temp.put("i", 1);
						}
						temp.put("teamid", teamid);
						temp.put("type", String.valueOf(ListsFragment.TYPE_TEAMS_MESSAGE));
						temp.put("name", info+"决定参加"+teamname+"Vs"+againstName+"的比赛");
						temp.put("message", "join_new_match");
						temp.put("result", "u");
						temp.put("id", map.get("phone").toString());
						helper.insert(Tools.getContentValuesFromMap(temp, null), "systemtable");
						Message msg0=handler.obtainMessage();
						msg0.what=HomePageActivity.NEW_MESSAGE;
						handler.sendMessage(msg0);
						if(map.get("phone").equals(new PreferenceData(context).getData(new String[]{"phone"}).get("phone").toString())){
							ContentValues cv=new ContentValues();
							cv.put("ensure", "1");
							helper.update(cv, "matches", Integer.parseInt(map.get("id").toString()), map.get("teamid").toString());
						}
						map.remove("request");
						map.remove("teamname");
						map.remove("againstname");
						Message msg=handler.obtainMessage();
						msg.what=HomePageActivity.JOIN_MATCH;
						msg.obj=map;
						handler.sendMessage(msg);
					}
					break;
				}
				case "get_matches":{
					map.remove("request");
					Message msg=handler.obtainMessage();
					msg.what=HomePageActivity.GET_MATCHES;
					msg.obj=map;
					handler.sendMessage(msg);
					break;
				}
				case "get_match_detail":{
					map.remove(request);
					Message msg=handler.obtainMessage();
					msg.what=HomePageActivity.GET_MATCH_DETAIL;
					msg.obj=map;
					handler.sendMessage(msg);
					break;
				}
				case "get_maxid":{
					map.remove("request");
					Message msg=handler.obtainMessage();
					msg.what=HomePageActivity.GET_MAXID;
					msg.obj=map;
					handler.sendMessage(msg);
					break;
				}
				case "get_match_by_type":{
					map.remove("request");
					Message msg=handler.obtainMessage();
					msg.what=HomePageActivity.GET_MATCH_BY_TYPE;
					msg.obj=map;
					handler.sendMessage(msg);
					break;
				}
				case "get_updated_match":{
					map.remove("request");
					Message msg=handler.obtainMessage();
					msg.what=HomePageActivity.GET_UPDATED_MATCH;
					msg.obj=map;
					handler.sendMessage(msg);
					break;
				}
				case "get_photo":{
					map.remove("request");
					String phone=map.get("phone").toString();
					String image=map.get("image").toString();
					if(!(image.equals("-1"))){
						Bitmap bitmap=Tools.stringtoBitmap(image);
						if(Tools.hasSdcard()){
							String fileName=Environment.getExternalStorageDirectory().getPath()+"/KICKFOR/user/"+phone.toString()+".png";
							System.out.println(fileName);
							Tools.saveBitmapToFile(bitmap, fileName);
							map.put("image", fileName);
							System.out.println("image saved");
						}
						else{
							Toast.makeText(context, "未检测到SD卡", Toast.LENGTH_LONG).show();
							map.put("image", "-1");
						}
					}
					SQLHelper helper=SQLHelper.getInstance(context);
					helper.update(Tools.getContentValuesFromMap(map, null), "friends", phone);
					break;
				}
				case "update_person_info":{
					map.remove("request");
					String teamid=map.get("teamid").toString();
					map.remove("teamid");
					SQLHelper helper=SQLHelper.getInstance(context);
					if(map.containsKey("id")){
						String id=map.get("id").toString();
						map.remove("id");
						Map<String, Object> temp=new HashMap<String, Object>();
						temp.put("result", "u");
						temp.put("type", String.valueOf(ListsFragment.TYPE_TEAMS_MESSAGE));
						temp.put("id", id);
						temp.put("message", "update_review");
						temp.put("teamid", teamid);
						temp.put("name", "比赛回顾");
						Cursor cursor=helper.select("systemtable", new String[]{"i"}, "id=? and teamid=? and message=?", new String[]{id, teamid, "update_review"}, null);
						if(cursor.moveToNext()){
							temp.put("i", cursor.getInt(0));
						}
						else{
							Cursor cursor1=helper.select("systemtable", new String[]{"max(i)"}, null, null, null);
							if(cursor1.moveToNext()){
								temp.put("i", cursor1.getInt(0)+1);
							}
							else{
								temp.put("i", 1);
							}
						}
						helper.update(Tools.getContentValuesFromMap(temp, null), "systemtable", (Integer)temp.get("i"));
						Message msg0=handler.obtainMessage();
						msg0.what=HomePageActivity.NEW_MESSAGE;
						handler.sendMessage(msg0);
					}
					helper.update(Tools.getContentValuesFromMap(map, null), "ich", "host");	
					break;
				}
				case "update_review_remind":{
					String teamid=map.get("teamid").toString();
					String id=map.get("id").toString();
					SQLHelper helper=SQLHelper.getInstance(context);
					Map<String, Object> temp=new HashMap<String, Object>();
					temp.put("result", "u");
					temp.put("type", String.valueOf(ListsFragment.TYPE_TEAMS_MESSAGE));
					temp.put("id", id);
					temp.put("message", "update_review");
					temp.put("teamid", teamid);
					temp.put("name", "比赛回顾");
					Cursor cursor=helper.select("systemtable", new String[]{"i"}, "id=? and teamid=? and message=?", new String[]{id, teamid, "update_review"}, null);
					if(cursor.moveToNext()){
						temp.put("i", cursor.getInt(0));
					}
					else{
						Cursor cursor1=helper.select("systemtable", new String[]{"max(i)"}, null, null, null);
						if(cursor1.moveToNext()){
							temp.put("i", cursor1.getInt(0)+1);
						}
						else{
							temp.put("i", 1);
						}
					}
					helper.update(Tools.getContentValuesFromMap(temp, null), "systemtable", (Integer)temp.get("i"));
					Message msg0=handler.obtainMessage();
					msg0.what=HomePageActivity.NEW_MESSAGE;
					handler.sendMessage(msg0);
					break;
				}
				case "get_match_by_id":{
					Bundle bundle=new Bundle();
					SQLHelper helper=SQLHelper.getInstance(context);
					Bitmap teamImg=null;
					String teamName=null;
					Cursor cursor=helper.select("teams", new String[]{"image", "name"}, "teamid=?",  new String[]{map.get("teamid").toString()}, null);
					if(cursor.moveToNext()){
						if(!cursor.getString(0).equals("-1")){
							teamImg=BitmapFactory.decodeFile(cursor.getString(0));
						}
						else{
							teamImg=BitmapFactory.decodeResource(context.getResources(), R.drawable.team_default);
						}
						teamName=cursor.getString(1);
					}
					int id=Integer.parseInt(map.get("id").toString());
					String date=map.get("date").toString();
					String place=map.get("place").toString();
					Bitmap againstImg=null;
					String imgPath=map.get("againstimg").toString();
					if(imgPath.equals("-1")){
						againstImg=BitmapFactory.decodeResource(context.getResources(), R.drawable.team_default);
					}
					else{
						againstImg=BitmapFactory.decodeFile(imgPath);
					}
					String againstName=map.get("againstname").toString();
					int goals=Integer.parseInt(map.get("goal").toString());
					int lost=Integer.parseInt(map.get("lost").toString());
					MatchReviewEntity item=new MatchReviewEntity(id, date, place, Tools.bitmapToString(teamImg), Tools.bitmapToString(againstImg), teamName, againstName, goals, lost);
					item.setStatus(map.get("status").toString());
					item.setAgainstId(map.get("againstid").toString());
					bundle.putSerializable("entity", item);
					bundle.putString("teamid", map.get("teamid").toString());
					Message msg=handler.obtainMessage();
					msg.setData(bundle);
					msg.what=HomePageActivity.GET_MATCH_BY_ID;
					handler.sendMessage(msg);
					break;
				}
				case "get_fame":{
					map.remove("request");
					Message msg=handler.obtainMessage();
					msg.what=HomePageActivity.GET_FAME;
					if(map.containsKey("n")){
						msg.obj=map;
						msg.arg1=0;
					}
					else{
						msg.obj=map;
						msg.arg1=1;
					}
					handler.sendMessage(msg);
					break;
				}
				case "update_fame":{
					String teamid=map.get("teamid").toString();
					Message msg=handler.obtainMessage();
					msg.obj=teamid;
					msg.what=HomePageActivity.UPDATE_FAME;
					handler.sendMessage(msg);
					break;
				}
				case "delete_fame":{
					String teamid=map.get("teamid").toString();
					Message msg=handler.obtainMessage();
					msg.obj=teamid;
					msg.what=HomePageActivity.UPDATE_FAME;
					handler.sendMessage(msg);
					break;
				}
				case "delete_preview":{
					String teamid=map.get("teamid").toString();
					String id=map.get("id").toString();
					String info="";
					SQLHelper helper=SQLHelper.getInstance(context);
					Cursor cursor=helper.select("matches", new String[]{"date", "time"}, "id=? and teamid=?", new String[]{id, teamid}, null);
					if(cursor.moveToNext()){
						Cursor cursor1=helper.select("teams", new String[]{"name"}, "teamid=?", new String[]{teamid}, null);
						if(cursor1.moveToNext()){
							info=cursor1.getString(0)+"取消了原定于"+cursor.getString(0)+" "+cursor.getString(1)+"的比赛";
							Map<String, Object> temp=new HashMap<String, Object>();
							temp.put("result", "u");
							temp.put("message", "delete_new_match");
							temp.put("teamid", teamid);
							temp.put("type", String.valueOf(ListsFragment.TYPE_TEAMS_MESSAGE));
							temp.put("id", Integer.parseInt(id));
							temp.put("name", info);
							Cursor cursor2=helper.select("systemtable", new String[]{"i"}, "id=? and teamid=? and (message=? or message=?)", new String[]{id, teamid, "publish_new_match", "update_new_match"}, null);
							if(cursor2.moveToNext()){
								temp.put("i", cursor2.getInt(0));
							}
							else{
								Cursor cursor3=helper.select("systemtable", new String[]{"max(i)"}, null, null, null);
								if(cursor3.moveToNext()){
									temp.put("i", cursor3.getInt(0)+1);
								}
								else{
									temp.put("i", 1);
								}
							}
							helper.update(Tools.getContentValuesFromMap(temp, null), "systemtable", Integer.parseInt(temp.get("i").toString()));
							Message msg0=handler.obtainMessage();
							msg0.what=HomePageActivity.NEW_MESSAGE;
							handler.sendMessage(msg0);
							helper.delete("matches", "id=? and teamid=?", new String[]{id, teamid});
						}
					}
					Bundle bundle=new Bundle();
					bundle.putString("teamid", teamid);
					Message msg=handler.obtainMessage();
					msg.setData(bundle);
					msg.what=HomePageActivity.DELETE_PREVIEW;
					handler.sendMessage(msg);
					break;
				}
				case "change_team_info":{
					map.remove("request");
					String teamid=map.get("teamid").toString();
					SQLHelper helper=SQLHelper.getInstance(context);
					if(map.containsKey("image")){
						Cursor cursor=helper.select("teams", new String[]{"image"}, "teamid=?", new String[]{teamid}, null);
						if(cursor.moveToNext()){
							String fileName=cursor.getString(0);
							Bitmap bitmap=Tools.stringtoBitmap(map.get("image").toString());
							if(fileName.equals("-1")){
								fileName=Environment.getExternalStorageDirectory().getPath()+"/KICKFOR/teams/"+teamid+".png";
							}
							System.out.println("fff============="+fileName);
							Tools.saveBitmapToFile(bitmap, fileName);
							map.remove("image");
						}
						else{
							String fileName=Environment.getExternalStorageDirectory().getPath()+"/KICKFOR/teams/"+teamid+".png";
							System.out.println(fileName);
							Bitmap bitmap=Tools.stringtoBitmap(map.get("image").toString());
							Tools.saveBitmapToFile(bitmap, fileName);
							map.put("image", fileName);
						}
					}
					helper.update(Tools.getContentValuesFromMap(map, null), "teams", teamid);
					Message msg=handler.obtainMessage();
					msg.obj=teamid;
					msg.what=HomePageActivity.CHANGE_TEAM_INFO;
					handler.sendMessage(msg);
					break;
				}
				case "change_result":{
					Message msg=handler.obtainMessage();
					msg.what=HomePageActivity.CHANGE_TEAM_GRADE;
					handler.sendMessage(msg);
					break;
				}
				case "change_process":{
					Message msg=handler.obtainMessage();
					msg.what=HomePageActivity.CHANGE_TEAM_GRADE;
					handler.sendMessage(msg);
					break;
				}
				case "update_review_match":{
					Message msg=handler.obtainMessage();
					msg.what=HomePageActivity.UPDATE_REVIEW_MATCH;
					handler.sendMessage(msg);
					break;
				}
				case "ok_authority":{
					Bundle bundle=new Bundle();
					map.remove("request");
					SQLHelper helper=SQLHelper.getInstance(context);
					helper.update(Tools.getContentValuesFromMap(map, null), "ich", "host");
					Cursor cursor=helper.select("ich", new String[]{"team1", "team2", "team3", "authority1", "authority2", "authority3"}, "phone=?", new String[]{"host"}, null);
					if(cursor.moveToNext()){
						bundle.putString("teamid1", cursor.getString(0));
						bundle.putString("teamid2", cursor.getString(1));
						bundle.putString("teamid3", cursor.getString(2));
						bundle.putString("authority1", cursor.getString(3));
						bundle.putString("authority2", cursor.getString(4));
						bundle.putString("authority3", cursor.getString(5));
						Message msg=handler.obtainMessage();
						msg.what=HomePageActivity.AUTHORITY;
						msg.setData(bundle);
						handler.sendMessage(msg);
					}
					break;
				}
				case "ok_deltheme":{
					Map<String, Object> temp=new HashMap<String, Object>();
					temp.put("request", "get themelist");
					temp.put("pstart", 0);
					temp.put("pnum", 5);
					Runnable r=new ClientWrite(Tools.JsonEncode(temp));
					new Thread(r).start();
					break;
				}
				case "ok_replytheme":{
					Message msg=handler.obtainMessage();
					msg.what=HomePageActivity.OK_THEME;
					handler.sendMessage(msg);
					Map<String, Object> temp=new HashMap<String, Object>();
					temp.put("request", "get themelist");
					temp.put("pstart", 0);
					temp.put("pnum", 5);
					Runnable r=new ClientWrite(Tools.JsonEncode(temp));
					new Thread(r).start();
					break;
				}
				case "ok_addtheme":{
					Message msg=handler.obtainMessage();
					msg.what=HomePageActivity.OK_THEME;
					handler.sendMessage(msg);
					Map<String, Object> temp=new HashMap<String, Object>();
					temp.put("request", "get themelist");
					temp.put("pstart", 0);
					temp.put("pnum", 5);
					Runnable r=new ClientWrite(Tools.JsonEncode(temp));
					new Thread(r).start();
					break;
				}
				case "ok_getthemelist":{
					map.remove("request");
					int index=Integer.parseInt(map.get("index").toString());
					List<LobbyTeamEntity> mList=new ArrayList<LobbyTeamEntity>();
					List<String> list=Tools.jsonToList(map.get("themeArray").toString());
					Iterator<String> iter=list.iterator();
					while(iter.hasNext()){
						String value=iter.next();
						Map<String, Object>temp=Tools.getMapForJson(value);
						String themekey=temp.get("themekey").toString();
						String teamid=temp.get("teamid").toString();
						String type=temp.get("type").toString();
						String date=temp.get("createdate").toString();
						String content=temp.get("content").toString();
						String name=temp.get("name").toString();
						String city=temp.get("city").toString();
						String image=temp.get("image").toString();
						LobbyTeamEntity entity=new LobbyTeamEntity(themekey, teamid, type, date, content, image.equals("-1")? BitmapFactory.decodeResource(context.getResources(), R.drawable.team_default): Tools.stringtoBitmap(image), name, city);
						List<String> sublist=Tools.jsonToList(temp.get("replyArray").toString());
						Iterator<String> subiter=sublist.iterator();
						while(subiter.hasNext()){
							Map<String, Object>subtemp=Tools.getMapForJson(subiter.next());
							if(subtemp.get("isShowMemberName").toString().equals("0")){
								entity.setReplyList(subtemp.get("replythemekey").toString(), subtemp.get("name").toString(), subtemp.get("content").toString());
							}
							else{
								entity.setReplyList(subtemp.get("replythemekey").toString(), subtemp.get("ReplyName").toString(), subtemp.get("content").toString());
							}
						}
						mList.add(entity);
					}
					Message msg=handler.obtainMessage();
					msg.obj=mList;
					msg.what=HomePageActivity.LOBBY_TEAM;
					msg.arg1=index;
					handler.sendMessage(msg);
					break;
				}
				case "ok_adduserarchives":{
					Map<String, Object> temp=new HashMap<String, Object>();
					temp.put("request", "get userarchives");
					temp.put("phone", map.get("phone").toString());
					Runnable r=new ClientWrite(Tools.JsonEncode(temp));
					new Thread(r).start();
					break;
				}
				case "ok_edituserarchives":{
					Map<String, Object> temp=new HashMap<String, Object>();
					temp.put("request", "get userarchives");
					temp.put("phone", map.get("phone").toString());
					Runnable r=new ClientWrite(Tools.JsonEncode(temp));
					new Thread(r).start();
					break;
				}
				case "ok_getuserarchives":{
					map.remove("request");
					SQLHelper helper=SQLHelper.getInstance(context);
					helper.delete("archive", null, null);
					helper.delete("archivematch", null, null);
					Iterator<String> iter=map.keySet().iterator();
					while(iter.hasNext()){
						String key=iter.next();
						Map<String, Object> temp=Tools.getMapForJson(map.get(key).toString());
						List<String> list1=Tools.jsonToList(temp.get("matchArray").toString());
						Iterator<String> iter2=list1.iterator();
						while(iter2.hasNext()){
							Map<String, Object> t=Tools.getMapForJson(iter2.next());
							helper.update(Tools.getContentValuesFromMap(t, null), Integer.parseInt(t.get("userarchivesmatchkey").toString()), Integer.parseInt(t.get("userarchiveskey").toString()));
						}
						temp.remove("matchArray");
						helper.update(Tools.getContentValuesFromMap(temp, null), "archive", Integer.parseInt(temp.get("userarchiveskey").toString()));
					}
					Message msg=handler.obtainMessage();
					msg.what=HomePageActivity.GET_ARCHIVES;
					handler.sendMessage(msg);
					break;
				}
				case "ok_deluserarchives":{
					Map<String, Object> temp=new HashMap<String, Object>();
					temp.put("request", "get userarchives");
					temp.put("phone", map.get("phone").toString());
					Runnable r=new ClientWrite(Tools.JsonEncode(temp));
					new Thread(r).start();
					break;
				}
				case "ok_getuserskills":{
					Bundle bundle=new Bundle();
					SQLHelper helper=SQLHelper.getInstance(context);
					helper.delete("skills", null, null);
					List<String> list=Tools.jsonToList(map.get("userSkillArray").toString());
					List<SkillsShowEntity> mList=new ArrayList<SkillsShowEntity>();
					Iterator<String> iter=list.iterator();
					int i=0;
					Map<String, Object> tt=new HashMap<String, Object>();
					tt.put("userskillsnum", list.size());
					helper.update(Tools.getContentValuesFromMap(tt, null), "ich", "host");
					while(iter.hasNext()){
						Map<String, Object> temp=Tools.getMapForJson(iter.next());
						String userskillkey=temp.get("userskillkey").toString();
						String skillkey=temp.get("skillkey").toString();
						String skillname=temp.get("skillname").toString();
						String hasAgree=temp.get("hasAgree").toString();
						String agreeNum=temp.get("agreeNum").toString();
						SkillsShowEntity item=new SkillsShowEntity(userskillkey, skillkey, skillname, hasAgree, agreeNum);
						mList.add(item);
						if(i<2){
							Map<String, Object> ttt=new HashMap<String, Object>();
							ttt.put("skillkey", Integer.parseInt(skillkey));
							ttt.put("skillname", skillname);
							ttt.put("agreeNum", agreeNum);
							helper.insert(Tools.getContentValuesFromMap(ttt, null), "skills");
						}
						i++;
					}
					List<String> list1=Tools.jsonToList(map.get("agreeArray").toString());
					Iterator<String> iter1=list1.iterator();
					int ii=1;
					while(iter1.hasNext()){
						Map<String, Object> t1=Tools.getMapForJson(iter1.next());
						bundle.putString("image"+ii, t1.get("image").toString());
						bundle.putString("name"+ii, t1.get("name").toString());
						bundle.putString("phone"+ii, t1.get("phone").toString());
						ii++;
					}
					Message msg=handler.obtainMessage();
					msg.setData(bundle);
					msg.what=HomePageActivity.GET_USERSKILLS;
					msg.obj=mList;
					handler.sendMessage(msg);
					break;
				}
				case "ok_getskillsdetail":{
					Bundle bundle=new Bundle();
					bundle.putString("name", map.get("name").toString());
					Map<String, Object> tempp=Tools.getMapForJson(map.get("skillinfo").toString());
					bundle.putString("skillkey", tempp.get("skillkey").toString());
					bundle.putString("skillname", tempp.get("skillname").toString());
					bundle.putString("introduce", tempp.get("introduce").toString());
					List<String> list=Tools.jsonToList(map.get("agreeArray").toString());
					List<SkillDetailEntity> mList=new ArrayList<SkillDetailEntity>();
					Iterator<String> iter=list.iterator();
					while(iter.hasNext()){
						Map<String, Object> temp=Tools.getMapForJson(iter.next());
						SkillDetailEntity item=new SkillDetailEntity(temp.get("phone").toString(), temp.get("name").toString(), temp.get("image").toString(), Tools.getMapForJson(temp.get("team").toString()).get("name").toString(), temp.get("position1").toString());
						mList.add(item);
					}
					Message msg=handler.obtainMessage();
					msg.setData(bundle);
					msg.what=HomePageActivity.GET_SKILLS_DETAIL;
					msg.obj=mList;
					handler.sendMessage(msg);
					break;
				}
				case "ok_delskills":{
					Bundle bundle=new Bundle();
					bundle.putString("phone", map.get("phone").toString());
					bundle.putString("skillkey", map.get("skillkey").toString());
					Map<String, Object> tmp1=new HashMap<String, Object>();
					tmp1.put("request", "get userskills");
					tmp1.put("phone", map.get("phone").toString());
					Runnable r=new ClientWrite(Tools.JsonEncode(tmp1));
					new Thread(r).start();
					Message msg=handler.obtainMessage();
					msg.setData(bundle);
					msg.what=HomePageActivity.OK_DELSKILLS;
					handler.sendMessage(msg);
					break;
				}
				case "ok_addskills":{
					Map<String, Object> tmp1=new HashMap<String, Object>();
					tmp1.put("request", "get userskills");
					tmp1.put("phone", map.get("phone").toString());
					Runnable r=new ClientWrite(Tools.JsonEncode(tmp1));
					new Thread(r).start();
					break;
				}
				case "ok_getkills":{
					List<SkillsSelectEntity> mList=new ArrayList<SkillsSelectEntity>();
					List<String> list=Tools.jsonToList(map.get("userSkillArray").toString());
					Iterator<String> iter=list.iterator();
					while(iter.hasNext()){
						System.out.println("ssssssssssdddddddddddddddddddddddddddd");
						Map<String, Object> temp=Tools.getMapForJson(iter.next());
						SkillsSelectEntity item=new SkillsSelectEntity(temp.get("skillkey").toString(), temp.get("skillname").toString(), temp.get("hasSkill").toString());
						mList.add(item);
					}
					System.out.println("ssssssssssdddddddddddddddddddddddddddd");
					Message msg=handler.obtainMessage();
					msg.obj=mList;
					msg.what=HomePageActivity.GET_SKILLS;
					handler.sendMessage(msg);
					break;	
				}
				}
				
			}
		}catch(Exception e){e.printStackTrace();}
		
	}

}
