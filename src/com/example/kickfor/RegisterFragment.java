package com.example.kickfor;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import com.easemob.EMCallBack;
import com.easemob.EMError;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.example.kickfor.utils.IdentificationInterface;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterFragment extends Fragment implements IdentificationInterface{
	
	protected static final int PHONE_PASSWORDS=1;
	protected static final int IDENTIFYING_CODE=2;
	protected static final int NAME=3;
	protected static final int CITY_DISTRICT=4;
	protected static final int COMPLETE=5;
	
	private String phone=null;
	private String psw=null;
	
	private EditText phoneText=null;
	private EditText passwords=null;
	private TelephonyManager telephonyManager=null;
	
	private EditText identify=null;
	private static TextView timerText=null;
	private static Timer timer=null;
	
	private EditText name=null;
	
	private EditText cityText=null;
	private EditText districtText=null;
	
	private TextView nextButton=null;
	private ImageView back=null;
	
	private int state=0;
	private String code=null;
	private Context context=null;
	
	@Override
	public int getFragmentLevel() {
		// TODO Auto-generated method stub
		return IdentificationInterface.SECOND_LEVEL;
	}
	
	
	private void init(){
		context=getActivity();
		Bundle bundle=getArguments();
		this.state=bundle.getInt("state");
		if(bundle.containsKey("phone")){
			this.phone=bundle.getString("phone");
		}
		if(bundle.containsKey("passwords")){
			this.psw=bundle.getString("passwords");
		}
		if(bundle.containsKey("code")){
			this.code=bundle.getString("code");
		}
		telephonyManager=(TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
		
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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		init();
		View view=null;
		switch(state){
		case PHONE_PASSWORDS:{
			view=inflater.inflate(R.layout.fragment_register1, container, false);
			phoneText=(EditText)view.findViewById(R.id.r_phone);
			String str=telephonyManager.getLine1Number();
			if(str!=null){
				phoneText.setText(str);
			}
			passwords=(EditText)view.findViewById(R.id.r_passwords);
			nextButton=(TextView)view.findViewById(R.id.r_next1);
			back=(ImageView)view.findViewById(R.id.register_title_back1);
			back.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					getActivity().onBackPressed();
				}
				
			});
			nextButton.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String str1=phoneText.getText().toString();
					String str2=passwords.getText().toString();
					if(str1.length()!=11){
						Toast.makeText(getActivity(), "手机号格式不正确", Toast.LENGTH_LONG).show();
					}
					else if(str2.length()<6){
						Toast.makeText(getActivity(), "密码至少为6位", Toast.LENGTH_LONG).show();
						passwords.setText("");
					}
					else{
						((HomePageActivity)getActivity()).openVague(HomePageActivity.WAIT_REGISTER_PHONE);
						setEnable(PHONE_PASSWORDS, false);
						Map<String, Object> tmp=new HashMap<String, Object>();
						tmp.put("request", "register phone");
						tmp.put("phone", str1);
						tmp.put("passwords", str2);
						Runnable r=new ClientWrite(Tools.JsonEncode(tmp));
						new Thread(r).start();
					}
					
				}
				
			});
			break;
		}
		case IDENTIFYING_CODE:{
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
									map.put("request", "register phone");
									map.put("phone", phone);
									map.put("passwords", psw);
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
			view=inflater.inflate(R.layout.fragment_register2, container, false);
			identify=(EditText)view.findViewById(R.id.r_verify);
			back=(ImageView)view.findViewById(R.id.register_title_back2);
			back.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					((HomePageActivity)getActivity()).onBackPressed();
					
				}
				
			});
			timerText=(TextView)view.findViewById(R.id.r_timer);
			nextButton=(TextView)view.findViewById(R.id.r_next2);
			setEnable(state, true);
			initTimer();
	        nextButton.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String str=identify.getText().toString();
					if(!str.isEmpty() && str.equals(code)){
						((HomePageActivity)getActivity()).openVague(HomePageActivity.WAIT_REGISTER_CODING);
						setEnable(IDENTIFYING_CODE, false);
						timer.cancel();
						Map<String, Object> tmp=new HashMap<String, Object>();
						tmp.put("request", "register code");
						tmp.put("phone", phone);
						tmp.put("passwords", psw);
						System.out.println("phone=="+phone);
						System.out.println("passwords=="+psw);
						Runnable r=new ClientWrite(Tools.JsonEncode(tmp));
						new Thread(r).start();
						
						
					}
					else{
						Toast.makeText(getActivity(), "验证码不正确", Toast.LENGTH_LONG).show();
					}
				}
	        	
	        });
	        
			break;
		}
		case NAME:{
			view=inflater.inflate(R.layout.fragment_register3, container, false);
			back=(ImageView)view.findViewById(R.id.register_title_back3);
			back.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					((HomePageActivity)getActivity()).onBackPressed();
				}
				
			});
			name=(EditText)view.findViewById(R.id.r_name);
			nextButton=(TextView)view.findViewById(R.id.r_next3);
			nextButton.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String str1=name.getText().toString();
					if(!str1.isEmpty()){
						setEnable(NAME, false);
						Map<String, Object> tmp=new HashMap<String, Object>();
						tmp.put("request", "register name");
						tmp.put("phone", phone);
						tmp.put("name", name.getText().toString());
						Runnable r=new ClientWrite(Tools.JsonEncode(tmp));
						new Thread(r).start();
						((HomePageActivity)getActivity()).openVague(HomePageActivity.WAIT_NAME);
					}
					else{
						Toast.makeText(getActivity(), "姓名不能为空", Toast.LENGTH_LONG).show();
					}
				}
			});
			break;
		}
		case CITY_DISTRICT:{
			view=inflater.inflate(R.layout.fragment_register4, container, false);
			back=(ImageView)view.findViewById(R.id.register_title_back4);
			cityText=(EditText)view.findViewById(R.id.r_city);
			districtText=(EditText)view.findViewById(R.id.r_district);
			nextButton=(TextView)view.findViewById(R.id.r_next4);
			back.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					((HomePageActivity)getActivity()).onBackPressed();
				}
				
			});
			nextButton.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					String str1=cityText.getText().toString();
					String str2=districtText.getText().toString();
					if(!str1.isEmpty() && !str2.isEmpty()){
						((HomePageActivity)getActivity()).openVague(HomePageActivity.WAIT_CITY_DISTRICT);
						setEnable(CITY_DISTRICT, false);
						Map<String, Object> tmp=new HashMap<String, Object>();
						tmp.put("request", "register city");
						tmp.put("phone", phone);
						tmp.put("city", str1);
						tmp.put("district", str2);
						Runnable r=new ClientWrite(Tools.JsonEncode(tmp));
						new Thread(r).start();
					}
					else{
						Toast.makeText(getActivity(), "城市和地区不能为空", Toast.LENGTH_LONG).show();
					}
				}
				
			});
			break;
		}
		case COMPLETE:{
			view=inflater.inflate(R.layout.fragment_register5, container, false);
			back=(ImageView)view.findViewById(R.id.register_title_back5);
			back.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					((HomePageActivity)getActivity()).onBackPressed();
				}
				
			});
			nextButton=(TextView)view.findViewById(R.id.r_next5);
			nextButton.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					((HomePageActivity)getActivity()).openVague(HomePageActivity.WAIT_COMPLETE);
					setEnable(COMPLETE, false);
					PreferenceData pd=new PreferenceData(getActivity());
					Map<String, Object> tmp=new HashMap<String, Object>();
					tmp.put("phone", phone);
					tmp.put("passwords", psw);
					pd.save(tmp);
					tmp.put("request", "user login");
					tmp.put("date", Tools.getDate1());
					SQLHelper helper=SQLHelper.getInstance(getActivity());
					Cursor cursor=helper.select("ich", new String[]{"image"}, "phone=?", new String[]{phone}, null);
					if(cursor.moveToNext()){
						String imgPath=cursor.getString(0);
						if(imgPath!=null && Tools.isFileExist(imgPath)!=false){
							tmp.put("img", "1");
						}
					}
					Runnable r=new ClientWrite(Tools.JsonEncode(tmp));
					new Thread(r).start();
					Tools.loginHuanXin(phone, psw);
				}
				
			});
			break;
		}
		}
		return view;
	}
	
	public void setEnable(int state, boolean enable){
		switch(state){
		case PHONE_PASSWORDS:{
			phoneText.setEnabled(enable);
			passwords.setEnabled(enable);
			nextButton.setEnabled(enable);
			back.setEnabled(enable);
			break;
		}
		case IDENTIFYING_CODE:{
			identify.setEnabled(enable);
			back.setEnabled(enable);
			nextButton.setEnabled(enable);
			if(enable==false){
				timerText.setVisibility(View.GONE);
			}
			else{
				timerText.setVisibility(View.VISIBLE);
			}
			break;
		}
		case NAME:{
			name.setEnabled(enable);
			back.setEnabled(enable);
			nextButton.setEnabled(enable);
			break;
		}
		case CITY_DISTRICT:{
			back.setEnabled(enable);
			cityText.setEnabled(enable);
			districtText.setEnabled(enable);
			nextButton.setEnabled(enable);
			break;
		}
		case COMPLETE:{
			back.setEnabled(enable);
			nextButton.setEnabled(enable);
			break;
		}
		}
	}
	
	public int getState(){
		return state;
	}

	public void reset(int state){
		switch(state){
		case PHONE_PASSWORDS:{
			phoneText.setText("");
			passwords.setText("");
			Toast.makeText(getActivity(), "此手机号已被注册", Toast.LENGTH_LONG).show();
			break;
		}
		}
	}
	
	public String getPhone(){
		return phone;
	}
	
	

	

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		if(state==IDENTIFYING_CODE){
			initTimer();
		}
		super.onResume();
	}
	
	

	public String getPasswords(){
		return psw;
	}
	
}
