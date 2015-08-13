package com.example.kickfor.team;

import com.example.kickfor.HomePageActivity;
import com.example.kickfor.R;
import com.example.kickfor.SQLHelper;
import com.example.kickfor.utils.IdentificationInterface;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class PrematchStartFragment extends Fragment implements TeamInterface, IdentificationInterface{

	private String teamid=null;
	private int authority=-1;
	private Context context=null;
	private TextView text=null;
	private TextView submit=null;
	private TextView cancel=null;
	private String teamName=null;
	
	@Override
	public int getFragmentLevel() {
		// TODO Auto-generated method stub
		return IdentificationInterface.SECOND_LEVEL;
	}
	
	@Override
	public void setEnable(boolean enable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getTeamid() {
		// TODO Auto-generated method stub
		return teamid;
	}
	
	private void init(){
		this.context=getActivity();
		Bundle bundle=getArguments();
		this.teamid=bundle.getString("teamid");
		this.authority=bundle.getInt("authority");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		init();
		View view=inflater.inflate(R.layout.fragment_preview_start, container, false);
		text=(TextView)view.findViewById(R.id.prematch_start_text);
		submit=(TextView)view.findViewById(R.id.prematch_start_btn);
		cancel=(TextView)view.findViewById(R.id.prematch_start_no);
		if(authority>=4){
			submit.setVisibility(View.VISIBLE);
			cancel.setText("休养生息，暂不比赛");
			submit.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					((HomePageActivity)getActivity()).edit(-1, teamid, "", "", "", "", "", "", "");
				}
				
			});
		}
		else{
			submit.setVisibility(View.GONE);
			cancel.setText("返回");
		}
		cancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((HomePageActivity)getActivity()).onBackPressed();
			}
			
		});
		initiate();
		return view;
	}
	
	private void initiate(){
		SQLHelper helper=SQLHelper.getInstance(context);
		Cursor cursor=helper.select("teams", new String[]{"name"}, "teamid=?", new String[]{teamid}, null);
		if(cursor.moveToNext()){
			teamName=cursor.getString(0);
		}
		text.setText("赶紧发布预告，让"+teamName+"时刻准备着！");
	}

	
	
}
