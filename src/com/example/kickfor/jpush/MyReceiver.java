package com.example.kickfor.jpush;



import org.json.JSONObject;

import com.example.kickfor.HomePageActivity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import cn.jpush.android.api.JPushInterface;

public class MyReceiver extends BroadcastReceiver {
	 private static final String TAG = "MyReceiver";
     
	    @Override
	    public void onReceive(Context context, Intent intent) {
	    	 Bundle bundle = intent.getExtras();
	         Log.d(TAG, "onReceive - " + intent.getAction());

	         if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
	        	 System.out.println("JPush�û�ע��ɹ�");
	         }else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
	             System.out.println("�յ����Զ�����Ϣ����Ϣ�����ǣ�" + bundle.getString(JPushInterface.EXTRA_MESSAGE));
	             // �Զ�����Ϣ����չʾ��֪ͨ������ȫҪ������д����ȥ����
	         } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
	             System.out.println("�յ���֪ͨ");
	             // �����������Щͳ�ƣ�������Щ��������
	         } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
	             System.out.println("�û��������֪ͨ    "+bundle.getString(JPushInterface.EXTRA_MESSAGE));
	             // ����������Լ�д����ȥ�����û���������Ϊ
	             openNotification(context, bundle);
	         } else {
	             Log.d(TAG, "Unhandled intent - " + intent.getAction());
	         }
	    }
	    
	    private void openNotification(Context context, Bundle bundle){
	    	String extras=bundle.getString(JPushInterface.EXTRA_EXTRA);
	    	String type="";
	    	String teamid="";
	    	String id="";
	    	try{
	    		JSONObject extrasJson=new JSONObject(extras);
	    		type=extrasJson.optString("type");
	    		id=extrasJson.optString("id");
	    		teamid=extrasJson.optString("teamid");
	    	}catch(Exception e){
	    		e.printStackTrace();
	    	}
	    	Intent intent=new Intent(context, HomePageActivity.class);
	    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    	intent.putExtra("type", type);
	    	intent.putExtra("teamid", teamid);
	    	intent.putExtra("id", id);
	    	context.startActivity(intent);
	    }
}
