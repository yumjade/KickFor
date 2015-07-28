package com.example.kickfor;

import java.util.HashMap;
import java.util.Map;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroupManager;
import com.example.kickfor.utils.IdentificationInterface;

import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.jpush.android.api.JPushInterface;

public class LoginFragment extends Fragment implements OnClickListener, IdentificationInterface{
	
	private EditText phone=null;
	private EditText passwords=null;
	private Button loginButton=null;
	private TextView registerButton=null;
	private TextView findPasswords=null;
	
	@Override
	public int getFragmentLevel() {
		// TODO Auto-generated method stub
		return IdentificationInterface.MAIN_LEVEL;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.fragment_login, container, false);
		phone=(EditText)view.findViewById(R.id.et_phone);
		passwords=(EditText)view.findViewById(R.id.et_passwords);
		loginButton=(Button)view.findViewById(R.id.bt_login);
		registerButton=(TextView)view.findViewById(R.id.tv_register);
		findPasswords=(TextView)view.findViewById(R.id.tv_find_passwords);
		loginButton.setOnClickListener(this);
		registerButton.setOnClickListener(this);
		findPasswords.setOnClickListener(this);
		return view;
	}
	

	@Override
	public void onClick(View v) {
		int id=v.getId();
		switch(id){
		case R.id.bt_login:{
			((HomePageActivity)getActivity()).openVague(HomePageActivity.WAIT_LOGIN);
			setEnable(false);
			Map<String, Object> tmp=new HashMap<String, Object>();
			tmp.put("request", "user login");
			tmp.put("phone", phone.getText().toString());
			tmp.put("passwords", passwords.getText().toString());
			tmp.put("date", Tools.getDate1());
			tmp.put("rid", JPushInterface.getRegistrationID(getActivity()));
			SQLHelper helper=SQLHelper.getInstance(getActivity());
			Cursor cursor=helper.select("ich", new String[]{"image"}, "phone=?", new String[]{phone.getText().toString()}, null);
			if(cursor.moveToNext()){
				String imgPath=cursor.getString(0);
				if(imgPath!=null && Tools.isFileExist(imgPath)!=false){
					tmp.put("img", "1");
				}
			}
			Runnable r=new ClientWrite(Tools.JsonEncode(tmp));
			new Thread(r).start();
			PreferenceData pd=new PreferenceData(getActivity());
			if(tmp.containsKey("img")){
				tmp.remove("img");
			}
			tmp.remove("request");
			tmp.remove("date");
			pd.save(tmp);
			
			Tools.loginHuanXin(phone.getText().toString(), passwords.getText().toString());
			
			break;
		}
		case R.id.tv_register:{
			((HomePageActivity)getActivity()).registerCommand();
			setEnable(false);
			break;
		}
		case R.id.tv_find_passwords:{
			Bundle bundle=new Bundle();
			bundle.putInt("resource", R.layout.fragment_find_psw1);
			((HomePageActivity)getActivity()).openFindPasswords(bundle);
			setEnable(false);
			break;
		}
		}
	}
	
	public void setEnable(boolean enable){
		loginButton.setEnabled(enable);
		registerButton.setEnabled(enable);
		phone.setEnabled(enable);
		passwords.setEnabled(enable);
	}
	
	

}
