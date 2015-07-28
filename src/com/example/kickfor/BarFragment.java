package com.example.kickfor;





import com.example.kickfor.utils.IdentificationInterface;

import android.annotation.TargetApi;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

public class BarFragment extends Fragment implements OnClickListener, IdentificationInterface{
	
	/**
	 * 
	 */
	
	private LinearLayout bar=null;
	private FrameLayout myKickFor=null;
	private TextView myKickForText=null;
	private TextView dot=null;
	private TextView team=null;
//	private RadioButton lobby=null;
	private TextView more=null;
	
	

	@Override
	public int getFragmentLevel() {
		// TODO Auto-generated method stub
		return IdentificationInterface.MAIN_LEVEL;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.fragment_bar, container, false);
		myKickForText=(TextView)view.findViewById(R.id.bar_home);
		dot=(TextView)view.findViewById(R.id.msg_bar_home);
		bar=(LinearLayout)view.findViewById(R.id.action_bar);
		myKickFor=(FrameLayout)view.findViewById(R.id.bar_home_f);
		team=(TextView)view.findViewById(R.id.bar_team);
//		lobby=(RadioButton)view.findViewById(R.id.bar_lobby);
		more=(TextView)view.findViewById(R.id.bar_more);
		myKickForText.setOnClickListener(this);
		team.setOnClickListener(this);
		more.setOnClickListener(this);
		initChecked(-1);
		setDot(false);
		return view;
	}

	public void setDot(boolean show){
		if(show==true){
			dot.setVisibility(View.VISIBLE);
		}
		else{
			dot.setVisibility(View.GONE);
		}
	}
	
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		int arg1=arg0.getId();
		setChecked(arg1);
		((HomePageActivity)getActivity()).onBarCheck(arg1);
	}

	
	
	public void initChecked(int arg1){
		if(arg1==-1){
			Bundle bundle=getArguments();
			arg1=0;
			if(bundle!=null && bundle.containsKey("status")){
				arg1=bundle.getInt("status");
			}
		}
		switch(arg1){
		case 0:
			setChecked(R.id.bar_home);
			break;
		case R.id.bar_home:
			setChecked(R.id.bar_home);
			break;
		case R.id.bar_team:
			setChecked(R.id.bar_team);
			break;
//		case R.id.bar_lobby:
//			lobby.setChecked(true);
//			setChecked(R.id.bar_lobby);
//			break;
		case R.id.bar_more:
			setChecked(R.id.bar_more);
			break;
		}
	}
	
	
	@SuppressWarnings("deprecation")
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	private void setChecked(int index){
		switch(index){
		case R.id.bar_home:{
			myKickForText.setTextColor(Color.parseColor("#22a100"));
			myKickForText.setBackgroundColor(Color.parseColor("#000000"));
			Drawable drawable= getResources().getDrawable(R.drawable.mykickfor_selected);
			myKickForText.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
			team.setTextColor(Color.parseColor("#ffffff"));
			team.setBackgroundDrawable(null);
			Drawable drawable1= getResources().getDrawable(R.drawable.team_unselected);
			team.setCompoundDrawablesWithIntrinsicBounds(null, drawable1, null, null);
//			lobby.setTextColor(Color.parseColor("#ffffff"));
//			lobby.setBackgroundDrawable(null);
			more.setTextColor(Color.parseColor("#ffffff"));
			more.setBackgroundDrawable(null);
			Drawable drawable2= getResources().getDrawable(R.drawable.more_unselected);
			more.setCompoundDrawablesWithIntrinsicBounds(null, drawable2, null, null);
			break;
		}
		case R.id.bar_team:{
			myKickForText.setTextColor(Color.parseColor("#ffffff"));
			myKickForText.setBackgroundDrawable(null);
			Drawable drawable= getResources().getDrawable(R.drawable.mykickfor_unselected);
			myKickForText.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
			team.setTextColor(Color.parseColor("#22a100"));
			team.setBackgroundColor(Color.parseColor("#000000"));
			Drawable drawable1= getResources().getDrawable(R.drawable.team_selected);
			team.setCompoundDrawablesWithIntrinsicBounds(null, drawable1, null, null);
//			lobby.setTextColor(Color.parseColor("#ffffff"));
//			lobby.setBackgroundDrawable(null);
			more.setTextColor(Color.parseColor("#ffffff"));
			more.setBackgroundDrawable(null);
			Drawable drawable2= getResources().getDrawable(R.drawable.more_unselected);
			more.setCompoundDrawablesWithIntrinsicBounds(null, drawable2, null, null);
			break;
		}
//		case R.id.bar_lobby:{
//			myKickFor.setTextColor(Color.parseColor("#ffffff"));
//			myKickFor.setBackgroundDrawable(null);
//			team.setTextColor(Color.parseColor("#ffffff"));
//			team.setBackgroundDrawable(null);
//			lobby.setTextColor(Color.parseColor("#22a100"));
//			lobby.setBackgroundColor(Color.parseColor("#000000"));
//			more.setTextColor(Color.parseColor("#ffffff"));
//			more.setBackgroundDrawable(null);
//			break;
//		}
		case R.id.bar_more:{
			myKickForText.setTextColor(Color.parseColor("#ffffff"));
			myKickForText.setBackgroundDrawable(null);
			Drawable drawable= getResources().getDrawable(R.drawable.mykickfor_unselected);
			myKickForText.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
			team.setTextColor(Color.parseColor("#ffffff"));
			team.setBackgroundDrawable(null);
			Drawable drawable1= getResources().getDrawable(R.drawable.team_unselected);
			team.setCompoundDrawablesWithIntrinsicBounds(null, drawable1, null, null);
//			lobby.setTextColor(Color.parseColor("#ffffff"));
//			lobby.setBackgroundDrawable(null);
			more.setTextColor(Color.parseColor("#22a100"));
			more.setBackgroundColor(Color.parseColor("#000000"));
			Drawable drawable2= getResources().getDrawable(R.drawable.more_selected);
			more.setCompoundDrawablesWithIntrinsicBounds(null, drawable2, null, null);
			break;
		}
		}
	}
	
	
	

}
