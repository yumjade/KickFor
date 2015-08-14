package com.example.kickfor;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.example.kickfor.utils.IdentificationInterface;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;


public class SelectPositionFragment extends Fragment implements OnClickListener, HomePageInterface, IdentificationInterface{
	
	private CheckBox striker=null;
	private CheckBox striker1=null;
	private CheckBox leftwing=null;
	private CheckBox rightwing=null;
	private CheckBox midfield1=null;
	private CheckBox midfield2=null;
	private CheckBox midfield3=null;
	private CheckBox midfield4=null;
	private CheckBox midfield5=null;
	private CheckBox goalkeeper=null;
	private CheckBox cleaner=null;
	private CheckBox middefender=null;
	private CheckBox leftdefender=null;
	private CheckBox rightdefender=null;
	private TextView ensureButton=null;
	private TextView positionText=null;
	private ImageView back=null;
	private TextView title=null;
	
	
	private List<CheckBox> mList=new ArrayList<CheckBox>();
	
	private String phone=null;
	private int state=0;
	private String position=null;
	
	@Override
	public int getFragmentLevel() {
		// TODO Auto-generated method stub
		return IdentificationInterface.SECOND_LEVEL;
	}
	
	public int getState(){
		return state;
	}
	
	public String getPosition(){
		return position;
	}
	
	private void init(){
		Bundle bundle=getArguments();
		this.phone=bundle.getString("phone");
		this.state=bundle.getInt("state");
		this.position=bundle.getString("position");
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		init();
		View view=inflater.inflate(R.layout.fragment_position, container, false);
		positionText=(TextView)view.findViewById(R.id.position_);
		title=(TextView)view.findViewById(R.id.position_text);
		if(state!=0 && state!=4){
			title.setText("选择您的第"+state+"场上位置");
		}
		else{
			title.setText("请选择场上位置");
		}
		back=(ImageView)view.findViewById(R.id.position_back);
		back.setOnClickListener(this);
		striker=(CheckBox)view.findViewById(R.id.rb_striker);
		striker1=(CheckBox)view.findViewById(R.id.rb_striker1);
		leftwing=(CheckBox)view.findViewById(R.id.rb_leftwing);
		rightwing=(CheckBox)view.findViewById(R.id.rb_rightwing);
		midfield1=(CheckBox)view.findViewById(R.id.rb_midfield1);
		midfield2=(CheckBox)view.findViewById(R.id.rb_midfield2);
		midfield3=(CheckBox)view.findViewById(R.id.rb_midfield3);
		midfield4=(CheckBox)view.findViewById(R.id.rb_midfield4);
		midfield5=(CheckBox)view.findViewById(R.id.rb_midfield5);
		goalkeeper=(CheckBox)view.findViewById(R.id.rb_goalkeeper);
		cleaner=(CheckBox)view.findViewById(R.id.rb_cleaner);
		middefender=(CheckBox)view.findViewById(R.id.rb_middefender);
		leftdefender=(CheckBox)view.findViewById(R.id.rb_leftdefender);
		rightdefender=(CheckBox)view.findViewById(R.id.rb_rightdefender);
		
		ensureButton=(TextView)view.findViewById(R.id.btn_ensure1);
		ensureButton.setOnClickListener(this);
		
		striker.setOnClickListener(this);
		mList.add(striker);
		striker1.setOnClickListener(this);
		mList.add(striker1);
		leftwing.setOnClickListener(this);
		mList.add(leftwing);
		rightwing.setOnClickListener(this);
		mList.add(rightwing);
		midfield1.setOnClickListener(this);
		mList.add(midfield1);
		midfield2.setOnClickListener(this);
		mList.add(midfield2);
		midfield3.setOnClickListener(this);
		mList.add(midfield3);
		midfield4.setOnClickListener(this);
		mList.add(midfield4);
		midfield5.setOnClickListener(this);
		mList.add(midfield5);
		goalkeeper.setOnClickListener(this);
		mList.add(goalkeeper);
		cleaner.setOnClickListener(this);
		mList.add(cleaner);
		middefender.setOnClickListener(this);
		mList.add(middefender);
		leftdefender.setOnClickListener(this);
		mList.add(leftdefender);
		rightdefender.setOnClickListener(this);
		mList.add(rightdefender);
		initiate();
		return view;
	}
	
	private void initiate(){
		positionText.setText(position);
		Iterator<CheckBox> iter=mList.iterator();
		while(iter.hasNext()){
			CheckBox item=iter.next();
			if(item.getText().equals(position)){
				initState1();
				item.setChecked(true);
				break;
			}
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id=v.getId();
		if(id==R.id.btn_ensure1){
			setEnable(false);
			if(state==0 || state==4){
				((HomePageActivity)getActivity()).onBackPressed();
			}
			else{
				((HomePageActivity)getActivity()).openVague(HomePageActivity.WAIT_POSITION);
				Map<String, Object> tmp=new HashMap<String, Object>();
				tmp.put("request", "update position");
				tmp.put("phone", phone);
				tmp.put("position", position);
				if(state==1){
					tmp.put("type", "1");
				}
				else if(state==2){
					tmp.put("type", "2");
				}
				Runnable r=new ClientWrite(Tools.JsonEncode(tmp));
				new Thread(r).start();
			}
		}
		else if(id==R.id.position_back){
			((HomePageActivity)getActivity()).onBackPressed();
		}
		else{
			initState1();
			CheckBox tmp=(CheckBox)v;
			tmp.setChecked(true);
			position=tmp.getText().toString();
			positionText.setText(position);
		}
	}
	
	private void initState1(){
		striker.setChecked(false);
		striker1.setChecked(false);
		leftwing.setChecked(false);
		rightwing.setChecked(false);
		midfield1.setChecked(false);
		midfield2.setChecked(false);
		midfield3.setChecked(false);
		midfield4.setChecked(false);
		midfield5.setChecked(false);
		goalkeeper.setChecked(false);
		cleaner.setChecked(false);
		middefender.setChecked(false);
		leftdefender.setChecked(false);
		rightdefender.setChecked(false);
	}
	
	public void setEnable(boolean enable){
		striker.setEnabled(enable);
		striker1.setEnabled(enable);
		leftwing.setEnabled(enable);
		rightwing.setEnabled(enable);
		midfield1.setEnabled(enable);
		midfield2.setEnabled(enable);
		midfield3.setEnabled(enable);
		midfield4.setEnabled(enable);
		midfield5.setEnabled(enable);
		goalkeeper.setEnabled(enable);
		cleaner.setEnabled(enable);
		middefender.setEnabled(enable);
		leftdefender.setEnabled(enable);
		rightdefender.setEnabled(enable);
		back.setEnabled(enable);
		ensureButton.setEnabled(enable);
	}
}
