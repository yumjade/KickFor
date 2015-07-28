package com.example.kickfor.team;


import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import com.example.kickfor.HomePageActivity;
import com.example.kickfor.R;
import com.example.kickfor.utils.IdentificationInterface;
import com.example.kickfor.wheelview.ScreenInfo;
import com.example.kickfor.wheelview.WheelDate;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class TeamCreateFragment extends Fragment implements OnClickListener, IdentificationInterface{
	
	private int resource;
	private TextView buttonNewTeam=null;
	private TextView buttonSeekTeam=null;
	private ImageView buttonTeamImage=null;
	private EditText teamName=null;
	private TextView buttonNext=null;
	private EditText teamCity=null;
	private EditText teamDistrict=null;
	private RelativeLayout r=null;
	private TextView teamYear=null;
	private TextView teamMonth=null;
	private TextView teamDay=null;
	private EditText teamNumber=null;
	private ImageView back=null;
	
	private WheelDate wheelDate=null;
	
	private boolean isCreatedTeam;
	
	private boolean isNext=false;
	
	
	@Override
	public int getFragmentLevel() {
		// TODO Auto-generated method stub
		if(resource==R.layout.fragment_team_create1){
			return IdentificationInterface.MAIN_LEVEL;
		}
		else{
			return IdentificationInterface.SECOND_LEVEL;
		}
	}
	
	private void init(){
		Bundle bundle=getArguments();
		this.resource=bundle.getInt("resource");
		isCreatedTeam=bundle.getBoolean("isCreatedTeam");
	}
	
	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		if(resource==R.layout.fragment_team_create2 && isNext==false){
			System.out.println("dfdfdfdfd");
			((HomePageActivity)getActivity()).resetViewPager();
		}
	}




	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		init();
		View view=inflater.inflate(resource, container, false);
		switch(resource){
		case R.layout.fragment_team_create1:{
			buttonNewTeam=(TextView)view.findViewById(R.id.tv_newteam);
			buttonSeekTeam=(TextView)view.findViewById(R.id.tv_seekteam);
			buttonNewTeam.setOnClickListener(this);
			buttonSeekTeam.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					((HomePageActivity)getActivity()).fromViewPager=true;
					((HomePageActivity)getActivity()).openSearch();
				}
				
			});
			//((HomePageActivity)getActivity()).resetViewPager();
			break;
		}
		case R.layout.fragment_team_create2:{
			((HomePageActivity)getActivity()).closeViewPager();
			back=(ImageView)view.findViewById(R.id.teamcreate_title_back2);
			back.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					((HomePageActivity)getActivity()).onBackPressed();
				}
				
			});
			buttonTeamImage=(ImageView)view.findViewById(R.id.iv_team1);
			buttonTeamImage.setOnClickListener(this);
			teamName=(EditText)view.findViewById(R.id.et_newteam);
			buttonNext=(TextView)view.findViewById(R.id.team_next2);
			buttonNext.setOnClickListener(this);
			break;
		}
		case R.layout.fragment_team_create3:{
			back=(ImageView)view.findViewById(R.id.teamcreate_title_back3);
			back.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					((HomePageActivity)getActivity()).onBackPressed();
				}
				
			});
			teamCity=(EditText)view.findViewById(R.id.spinner_city);
			teamDistrict=(EditText)view.findViewById(R.id.spinner_district);
			buttonNext=(TextView)view.findViewById(R.id.team_next3);
			buttonNext.setOnClickListener(this);
			break;
		}
		case R.layout.fragment_team_create4:{
			r=(RelativeLayout)view.findViewById(R.id.team_create_r);
			back=(ImageView)view.findViewById(R.id.teamcreate_title_back4);
			back.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					((HomePageActivity)getActivity()).onBackPressed();
				}
				
			});
			teamYear=(TextView)view.findViewById(R.id.spinner_year);
			teamMonth=(TextView)view.findViewById(R.id.spinner_month);
			teamDay=(TextView)view.findViewById(R.id.spinner_day);
			
			r.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					LayoutInflater mInflater=LayoutInflater.from(getActivity());
					final View datepickerview=mInflater.inflate(R.layout.datepicker, null);
					wheelDate=new WheelDate(datepickerview);
					ScreenInfo screenInfo=new ScreenInfo(getActivity());
					wheelDate.screenheight=screenInfo.getHeight();
					int year=Calendar.getInstance().get(Calendar.YEAR);
					int month=Calendar.getInstance().get(Calendar.MONTH);
					int day=Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
					wheelDate.initDatePicker(year, month, day);
					new AlertDialog.Builder(getActivity())
					.setTitle("选择日期")
					.setView(datepickerview)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									teamYear.setText(wheelDate.getYear());
									teamMonth.setText(wheelDate.getMonth());
									teamDay.setText(wheelDate.getDay());
								}
							})
					.setNegativeButton("取消",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
								}
							}).show();
				}
				
			});
			
			
			
			buttonNext=(TextView)view.findViewById(R.id.team_next4);
			buttonNext.setOnClickListener(this);
			break;
		}
		case R.layout.fragment_team_create5:{
			back=(ImageView)view.findViewById(R.id.teamcreate_title_back5);
			back.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					((HomePageActivity)getActivity()).onBackPressed();
				}
				
			});
			teamNumber=(EditText)view.findViewById(R.id.et_team_number);
			buttonNext=(TextView)view.findViewById(R.id.team_next5);
			buttonNext.setOnClickListener(this);
			break;
		}
		default:break;
		}
		return view;
	}

	public Map<String, Object> getData(int id){
		Map<String, Object> map=new HashMap<String, Object>();
		switch(id){
		case R.id.team_next2:{
			map.put("name", teamName.getText().toString());
			break;
		}
		case R.id.team_next3:{
			map.put("city", teamCity.getText().toString());
			map.put("district", teamDistrict.getText().toString());
			break;
		}
		case R.id.team_next4:{
			map.put("year", teamYear.getText().toString());
			map.put("month", teamMonth.getText().toString());
			map.put("day", teamDay.getText().toString());
			break;
		}
		case R.id.team_next5:{
			map.put("number", teamNumber.getText().toString());
			break;
		}
		default:break;
		}
		return map;
	}
	
	@SuppressWarnings("deprecation")
	public void setImage(Bitmap bitmap){
		buttonTeamImage.setBackgroundDrawable(new BitmapDrawable(this.getResources(), bitmap));
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(resource==R.layout.fragment_team_create2){
			isNext=true;
		}
		((HomePageActivity)getActivity()).teamCreate(v, isCreatedTeam, this);
	}
	
	public int getResourceId(){
		return resource;
	}
	
	public void setEnable(int resource, boolean enable){
		switch(resource){
		case R.layout.fragment_team_create1:{
			buttonNewTeam.setEnabled(enable);
			buttonSeekTeam.setEnabled(enable);
			break;
		}
		case R.layout.fragment_team_create2:{
			back.setEnabled(enable);
			buttonTeamImage.setEnabled(enable);
			teamName.setEnabled(enable);
			buttonNext.setEnabled(enable);
			break;
		}
		case R.layout.fragment_team_create3:{
			back.setEnabled(enable);
			buttonNext.setEnabled(enable);
			teamCity.setEnabled(enable);
			teamDistrict.setEnabled(enable);
			break;
		}
		case R.layout.fragment_team_create4:{
			back.setEnabled(enable);
			teamYear.setEnabled(enable);
			teamMonth.setEnabled(enable);
			teamDay.setEnabled(enable);
			buttonNext.setEnabled(enable);
			break;
		}
		case R.layout.fragment_team_create5:{
			back.setEnabled(enable);
			teamNumber.setEnabled(enable);
			buttonNext.setEnabled(enable);
			break;
		}
		}
	}

}
