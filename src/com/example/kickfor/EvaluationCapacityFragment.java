package com.example.kickfor;


import java.util.HashMap;
import java.util.Map;







import com.example.kickfor.utils.IdentificationInterface;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

public class EvaluationCapacityFragment extends Fragment implements OnClickListener, OnSeekBarChangeListener, HomePageInterface, IdentificationInterface{
	
	private ImageView image=null;
	private TextView name=null;
	private TextView team=null;
	private TextView number=null;
	private TextView sub1Button=null;
	private TextView sub2Button=null;
	private TextView sub3Button=null;
	private TextView sub4Button=null;
	private TextView sub5Button=null;
	private TextView sub6Button=null;
	private TextView add1Button=null;
	private TextView add2Button=null;
	private TextView add3Button=null;
	private TextView add4Button=null;
	private TextView add5Button=null;
	private TextView add6Button=null;
	private SeekBar attackBar=null;
	private SeekBar defenseBar=null;
	private SeekBar powerBar=null;
	private SeekBar speedBar=null;
	private SeekBar staminaBar=null;
	private SeekBar skillsBar=null;
	private TextView value1=null;
	private TextView value2=null;
	private TextView value3=null;
	private TextView value4=null;
	private TextView value5=null;
	private TextView value6=null;
	private TextView submitButton=null;
	private ImageView back=null;
	
	private String phone=null;
	private int tmp;
	private Bitmap bitmap=null;
	
	@Override
	public int getFragmentLevel() {
		// TODO Auto-generated method stub
		return IdentificationInterface.SECOND_LEVEL;
	}
	
	public void setEnable(boolean enable){
		back.setEnabled(enable);
		sub1Button.setEnabled(enable);
		sub2Button.setEnabled(enable);
		sub3Button.setEnabled(enable);
		sub4Button.setEnabled(enable);
		sub5Button.setEnabled(enable);
		sub6Button.setEnabled(enable);
		add1Button.setEnabled(enable);
		add2Button.setEnabled(enable);
		add3Button.setEnabled(enable);
		add4Button.setEnabled(enable);
		add5Button.setEnabled(enable);
		add6Button.setEnabled(enable);
		submitButton.setEnabled(enable);
		attackBar.setEnabled(enable);
		defenseBar.setEnabled(enable);
		staminaBar.setEnabled(enable);
		skillsBar.setEnabled(enable);
		speedBar.setEnabled(enable);
		powerBar.setEnabled(enable);
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.fragment_capacity_page,container,false);
		back=(ImageView)view.findViewById(R.id.evaluate_back);
		back.setOnClickListener(this);
		image=(ImageView)view.findViewById(R.id.iv_photo1);
		name=(TextView)view.findViewById(R.id.tv_name1);
		team=(TextView)view.findViewById(R.id.tv_team1);
		number=(TextView)view.findViewById(R.id.tv_number1);
		sub1Button=(TextView)view.findViewById(R.id.tv_sub1);
		sub2Button=(TextView)view.findViewById(R.id.tv_sub2);
		sub3Button=(TextView)view.findViewById(R.id.tv_sub3);
		sub4Button=(TextView)view.findViewById(R.id.tv_sub4);
		sub5Button=(TextView)view.findViewById(R.id.tv_sub5);
		sub6Button=(TextView)view.findViewById(R.id.tv_sub6);
		add1Button=(TextView)view.findViewById(R.id.tv_add1);
		add2Button=(TextView)view.findViewById(R.id.tv_add2);
		add3Button=(TextView)view.findViewById(R.id.tv_add3);
		add4Button=(TextView)view.findViewById(R.id.tv_add4);
		add5Button=(TextView)view.findViewById(R.id.tv_add5);
		add6Button=(TextView)view.findViewById(R.id.tv_add6);
		value1=(TextView)view.findViewById(R.id.tv_value1);
		value2=(TextView)view.findViewById(R.id.tv_value2);
		value3=(TextView)view.findViewById(R.id.tv_value3);
		value4=(TextView)view.findViewById(R.id.tv_value4);
		value5=(TextView)view.findViewById(R.id.tv_value5);
		value6=(TextView)view.findViewById(R.id.tv_value6);
		attackBar=(SeekBar)view.findViewById(R.id.pb_attack);
		defenseBar=(SeekBar)view.findViewById(R.id.pb_defence);
		powerBar=(SeekBar)view.findViewById(R.id.pb_power);
		skillsBar=(SeekBar)view.findViewById(R.id.pb_skills);
		staminaBar=(SeekBar)view.findViewById(R.id.pb_stamina);
		speedBar=(SeekBar)view.findViewById(R.id.pb_speed);
		
		
		attackBar.setTag(value5);
		defenseBar.setTag(value6);
		powerBar.setTag(value1);
		skillsBar.setTag(value3);
		staminaBar.setTag(value4);
		speedBar.setTag(value2);
		
		attackBar.setOnSeekBarChangeListener(this);
		defenseBar.setOnSeekBarChangeListener(this);
		powerBar.setOnSeekBarChangeListener(this);
		skillsBar.setOnSeekBarChangeListener(this);
		staminaBar.setOnSeekBarChangeListener(this);
		speedBar.setOnSeekBarChangeListener(this);
		
		sub1Button.setOnClickListener(this);
		sub2Button.setOnClickListener(this);
		sub3Button.setOnClickListener(this);
		sub4Button.setOnClickListener(this);
		sub5Button.setOnClickListener(this);
		sub6Button.setOnClickListener(this);
		add1Button.setOnClickListener(this);
		add2Button.setOnClickListener(this);
		add3Button.setOnClickListener(this);
		add4Button.setOnClickListener(this);
		add5Button.setOnClickListener(this);
		add6Button.setOnClickListener(this);
		
		submitButton=(TextView)view.findViewById(R.id.btn_submit);
		submitButton.setOnClickListener(this);
		
		initiate();
		
		return view;
	}
	
	
	
	private void initiate(){
		Bundle bundle=getArguments();
		phone=bundle.getString("phone");
		team.setText(bundle.getString("info"));
		name.setText(bundle.getString("name"));
		number.setText(bundle.getString("number"));
		if(bundle.containsKey("image")){
			bitmap=Tools.stringtoBitmap(bundle.getString("image"));
		}
		image.setImageBitmap(bitmap);
		
		attackBar.setProgress(1);
		defenseBar.setProgress(1);
		speedBar.setProgress(1);
		staminaBar.setProgress(1);
		powerBar.setProgress(1);
		skillsBar.setProgress(1);
		setBarColor(attackBar, 1);
		setBarColor(defenseBar, 1);
		setBarColor(speedBar, 1);
		setBarColor(staminaBar, 1);
		setBarColor(powerBar, 1);
		setBarColor(skillsBar, 1);
	}
	
	private void setBarColor(SeekBar seekBar, int progress){
		if(progress<=10){
			Drawable d=getResources().getDrawable(R.drawable.progress_bar_style_1);
			seekBar.setProgressDrawable(d);
		}
		else{
			Drawable d=getResources().getDrawable(R.drawable.progress_bar_style);
			seekBar.setProgressDrawable(d);
		}
	}


	

	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean arg2) {
		// TODO Auto-generated method stub
		if(arg2==true){
			((TextView)seekBar.getTag()).setText(String.valueOf(progress));
			setBarColor(seekBar, progress);
		}
	}



	@Override
	public void onStartTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onStopTrackingTouch(SeekBar arg0) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.evaluate_back:{
			((HomePageActivity)getActivity()).onBackPressed();
		}
		case R.id.tv_sub1:{
			if((tmp=Integer.parseInt(value1.getText().toString()))>=1){
				tmp=tmp-1;
				value1.setText(""+tmp);
				powerBar.setProgress(tmp);
				setBarColor(powerBar, tmp);
				
			}
			break;
		}
		case R.id.tv_add1:{
			if((tmp=Integer.parseInt(value1.getText().toString()))<20){
				tmp=tmp+1;
				value1.setText(""+tmp);
				powerBar.setProgress(tmp);
				setBarColor(powerBar, tmp);
			}
			break;
		}
		case R.id.tv_sub2:{
			if((tmp=Integer.parseInt(value2.getText().toString()))>=1){
				tmp=tmp-1;
				value2.setText(""+tmp);
				speedBar.setProgress(tmp);
				setBarColor(speedBar, tmp);
			}
			break;
		}
		case R.id.tv_add2:{
			if((tmp=Integer.parseInt(value2.getText().toString()))<20){
				tmp=tmp+1;
				value2.setText(""+tmp);
				speedBar.setProgress(tmp);
				setBarColor(speedBar, tmp);
			}
			break;
		}
		case R.id.tv_sub3:{
			if((tmp=Integer.parseInt(value3.getText().toString()))>=1){
				tmp=tmp-1;
				value3.setText(""+tmp);
				skillsBar.setProgress(tmp);
				setBarColor(skillsBar, tmp);
			}
			break;
		}
		case R.id.tv_add3:{
			if((tmp=Integer.parseInt(value3.getText().toString()))<20){
				tmp=tmp+1;
				value3.setText(""+tmp);
				skillsBar.setProgress(tmp);
				setBarColor(skillsBar, tmp);
			}
			break;
		}
		case R.id.tv_sub4:{
			if((tmp=Integer.parseInt(value4.getText().toString()))>=1){
				tmp=tmp-1;
				value4.setText(""+tmp);
				staminaBar.setProgress(tmp);
				setBarColor(staminaBar, tmp);
			}
			break;
		}
		case R.id.tv_add4:{
			if((tmp=Integer.parseInt(value4.getText().toString()))<20){
				tmp=tmp+1;
				value4.setText(""+tmp);
				staminaBar.setProgress(tmp);
				setBarColor(staminaBar, tmp);
			}
			break;
		}
		case R.id.tv_sub5:{
			if((tmp=Integer.parseInt(value5.getText().toString()))>=1){
				tmp=tmp-1;
				value5.setText(""+tmp);
				attackBar.setProgress(tmp);
				setBarColor(attackBar, tmp);
			}
			break;
		}
		case R.id.tv_add5:{
			if((tmp=Integer.parseInt(value5.getText().toString()))<20){
				tmp=tmp+1;
				value5.setText(""+tmp);
				attackBar.setProgress(tmp);
				setBarColor(attackBar, tmp);
			}
			break;
		}
		case R.id.tv_sub6:{
			if((tmp=Integer.parseInt(value6.getText().toString()))>=1){
				tmp=tmp-1;
				value6.setText(""+tmp);
				defenseBar.setProgress(tmp);
				setBarColor(defenseBar, tmp);
			}
			break;
		}
		case R.id.tv_add6:{
			if((tmp=Integer.parseInt(value6.getText().toString()))<20){
				tmp=tmp+1;
				value6.setText(""+tmp);
				defenseBar.setProgress(tmp);
				setBarColor(defenseBar, tmp);
			}
			break;
		}
		case R.id.btn_submit:{
			
			new AlertDialog.Builder(getActivity())
			.setTitle("提交后将无法修改")
			.setPositiveButton("确定提交",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							setEnable(false);
							Map<String, Object> map=new HashMap<String, Object>();
							map.put("request", "evaluate");
							map.put("phone", phone);
							map.put("skills", value3.getText().toString());
							map.put("stamina", value4.getText().toString());
							map.put("defense", value6.getText().toString());
							map.put("attack", value5.getText().toString());
							map.put("speed", value2.getText().toString());
							map.put("power", value1.getText().toString());
							Runnable r=new ClientWrite(Tools.JsonEncode(map));
							new Thread(r).start();
							((HomePageActivity)getActivity()).openVague(HomePageActivity.WAIT_EVALUATE_RESULT);
						}
					})
			.setNegativeButton("我再想想",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
						}
					}).show();
			break;
		}
		}
	}
	
	public String getPhone(){
		return phone;
	}

}
