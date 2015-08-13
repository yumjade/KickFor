package com.example.kickfor.more;


import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.example.kickfor.ClientWrite;
import com.example.kickfor.HomePageActivity;
import com.example.kickfor.PreferenceData;
import com.example.kickfor.R;
import com.example.kickfor.Tools;
import com.example.kickfor.utils.IdentificationInterface;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class FindPasswordsFragment extends Fragment implements MoreInterface, IdentificationInterface{
	
	private int resource=0;
	private Context context=null;
	private ImageView back=null;
	private EditText phoneText=null;
	
	private EditText codeText=null;
	private TextView timerText=null;
	private TextView rPhone=null;
	
	private EditText psw1Text=null;
	private EditText psw2Text=null;
	private EditText psw3Text=null;
	
	private TextView ensure=null;
	
	private String phone=null;
	private String psw=null;
	private String code=null;
	
	private Timer timer=null;
	
	@Override
	public void setEnable(boolean enable) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public int getFragmentLevel() {
		// TODO Auto-generated method stub
		return IdentificationInterface.SECOND_LEVEL;
	}

	private Handler handler=null;
		
		private void initTimer(){
			if(timer==null){
				timer=new Timer();
		    	TimerTask task=new TimerTask(){
		    		int recLen=180;  
		            public void run() {  
		                recLen--;
		                Message msg=new Message();
		                msg.obj=recLen;
		                msg.what=1;
		                handler.sendMessage(msg);
		                if(recLen<=0){  
		                    timer.cancel();
		                    timer=null;
		                }  
		            }  
		        }; 
		        timer.schedule(task, 1000, 1000);
			}
		}
	
	private void init(){
		context=getActivity();
		Bundle bundle=getArguments();
		resource=bundle.getInt("resource");
		if(bundle.containsKey("phone")){
			this.phone=bundle.getString("phone");
		}
		if(bundle.containsKey("passwords")){
			this.psw=bundle.getString("passwords");
		}
		if(bundle.containsKey("code")){
			this.code=bundle.getString("code");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		init();
		View view=inflater.inflate(resource, container, false);
		switch(resource){
		case R.layout.fragment_find_psw1:{
			phoneText=(EditText)view.findViewById(R.id.find_psd_phone);
			back=(ImageView)view.findViewById(R.id.find_psd_back);
			ensure=(TextView)view.findViewById(R.id.find_psd_next);
			back.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					((HomePageActivity)getActivity()).onBackPressed();
				}
				
			});
			ensure.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					phone=phoneText.getText().toString();
					if(!phone.isEmpty()){
						Map<String, Object> map=new HashMap<String, Object>();
						map.put("request", "new psw");
						map.put("phone", phone);
						Runnable r=new ClientWrite(Tools.JsonEncode(map));
						new Thread(r).start();
					}
					else{
						Toast.makeText(context, "手机号不能为空", Toast.LENGTH_SHORT).show();
					}
				}
				
			});
			break;
		}
		case R.layout.fragment_find_psw2:{
			handler=new Handler(){

				@Override
				public void handleMessage(Message msg) {
					// TODO Auto-generated method stub
					super.handleMessage(msg);
					int msgId=msg.what;
					if(msgId==1){
						timerText.setText(""+msg.obj+"秒后重发");
						if((int)msg.obj==0){
							timerText.setClickable(true);
							code="";
							timerText.setText("点击重新获取");
							timerText.setOnClickListener(new OnClickListener(){

								@Override
								public void onClick(View v) {
									// TODO Auto-generated method stub
									Map<String, Object> map=new HashMap<String, Object>();
									map.put("request", "new psw");
									map.put("phone", phone);
									Runnable r=new ClientWrite(Tools.JsonEncode(map));
									new Thread(r).start();
									initTimer();
									timerText.setClickable(false);
								}
								
							});
						}
					}
				}
				
			};
			
			rPhone=(TextView)view.findViewById(R.id.yanzhengmayifa);
			String str="";
			int n=phone.length();
			for(int i=0; i<n; i++){
				if(i<3 || i>7){
					str=str+String.valueOf(phone.charAt(i));
				}
				else{
					str=str+"*";
				}
			}
			rPhone.setText("验证码已发送至您的手机号"+str+"上，3分钟内输入有效。");
			codeText=(EditText)view.findViewById(R.id.find_psd_code);
			timerText=(TextView)view.findViewById(R.id.find_psd_re);
			back=(ImageView)view.findViewById(R.id.find_psd_back1);
			ensure=(TextView)view.findViewById(R.id.find_psd_next1);
			initTimer();
			back.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					((HomePageActivity)getActivity()).onBackPressed();
				}
				
			});
			
			ensure.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(codeText.getText().toString().equals(code)){
						Bundle bundle=new Bundle();
						bundle.putString("phone", phone);
						bundle.putInt("resource", R.layout.fragment_find_psw3);
						((HomePageActivity)getActivity()).openFindPasswords(bundle);
					}
				}
				
			});
			
			break;
		}
		case R.layout.fragment_find_psw3:{
			back=(ImageView)view.findViewById(R.id.find_psd_back2);
			ensure=(TextView)view.findViewById(R.id.find_psd_next2);
			psw1Text=(EditText)view.findViewById(R.id.find_psd_repsd);
			psw2Text=(EditText)view.findViewById(R.id.find_psd_repsd1);
			back.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					((HomePageActivity)getActivity()).onBackPressed();
				}
				
			});
			ensure.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String str1=psw1Text.getText().toString();
					String str2=psw2Text.getText().toString();
					if(str1.length()>=6 && str1.equals(str2)){
						Map<String, Object> map=new HashMap<String, Object>();
						map.put("request", "set psw");
						map.put("phone", phone);
						map.put("passwords", str1);
						Runnable r=new ClientWrite(Tools.JsonEncode(map));
						new Thread(r).start();
					}
					else{
						Toast.makeText(context, "请注意密码格式并再次确认", Toast.LENGTH_LONG).show();
					}
				}
				
			});
			break;
		}
		case R.layout.fragment_reset_psw:{
			back=(ImageView)view.findViewById(R.id.reset_psd_back);
			psw3Text=(EditText)view.findViewById(R.id.reset_psd_old);
			psw1Text=(EditText)view.findViewById(R.id.reset_psd_new);
			psw2Text=(EditText)view.findViewById(R.id.reset_psd_new1);
			ensure=(TextView)view.findViewById(R.id.reset_psd_next);
			back.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					((HomePageActivity)getActivity()).onBackPressed();
				}
				
			});
			ensure.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					PreferenceData pd=new PreferenceData(context);
					String str=psw3Text.getText().toString();
					String str1=pd.getData(new String[]{"passwords"}).get("passwords").toString();
					String str2=psw1Text.getText().toString();
					String str3=psw2Text.getText().toString();
					if(!str1.equals(str)){
						Toast.makeText(context, "原密码错误", Toast.LENGTH_LONG).show();
					}
					else if(!str2.equals(str3)){
						Toast.makeText(context, "再次输入与新密码不符", Toast.LENGTH_LONG).show();
					}
					else{
						Map<String, Object> map=new HashMap<String, Object>();
						map.put("request", "set new psw");
						map.put("passwords", str2);
						map.put("phone", phone);
						Runnable r=new ClientWrite(Tools.JsonEncode(map));
						new Thread(r).start();
					}
				}
				
			});
			break;
		}
		}
		
		return view;
	}
	
	public int getState(){
		return resource;
	}
	
	
}
