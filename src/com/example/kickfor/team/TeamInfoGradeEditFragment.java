package com.example.kickfor.team;

import java.util.HashMap;
import java.util.Map;

import com.example.kickfor.ClientWrite;
import com.example.kickfor.HomePageActivity;
import com.example.kickfor.R;
import com.example.kickfor.Tools;
import com.example.kickfor.utils.IdentificationInterface;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class TeamInfoGradeEditFragment extends Fragment implements TeamInterface, IdentificationInterface{
	
	private ImageView back;
	private EditText startup;
	private EditText evolution;
	private EditText expansion;
	private EditText mature;
	private TextView confirmation;
	private TextView cancel;
	
	private String teamid=null;
	private String p1=null;
	private String p2=null;
	private String p3=null;
	private String p4=null;
	
	private void init(){
		Bundle bundle=getArguments();
		this.p1=bundle.getString("p1");
		this.p2=bundle.getString("p2");
		this.p3=bundle.getString("p3");
		this.p4=bundle.getString("p4");
		this.teamid=bundle.getString("teamid");
	}
	
	@Override
	public void setEnable(boolean enable) {
		// TODO Auto-generated method stub
		if(confirmation!=null){
			confirmation.setEnabled(enable);
			cancel.setEnabled(enable);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		init();
		View view = inflater.inflate(R.layout.fragment_edit_history, container, false);
		back = (ImageView) view.findViewById(R.id.history_edit_back);
		startup = (EditText) view.findViewById(R.id.et_startup);
		evolution = (EditText) view.findViewById(R.id.et_evolution);
		expansion = (EditText) view.findViewById(R.id.et_expansion);
		mature = (EditText) view.findViewById(R.id.et_mature);
		confirmation = (TextView) view.findViewById(R.id.history_edit_confirmation);
		cancel = (TextView) view.findViewById(R.id.history_edit_cancel);
		
		initiate();
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((HomePageActivity)getActivity()).onBackPressed();
				
			}
		});
		
		confirmation.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setEnable(false);
				p1=startup.getText().toString();
				p2=evolution.getText().toString();
				p3=expansion.getText().toString();
				p4=mature.getText().toString();
				startup.setEnabled(false);
				evolution.setEnabled(false);
				expansion.setEnabled(false);
				mature.setEnabled(false);
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("request", "change process");
				map.put("teamid", teamid);
				map.put("p1", p1);
				map.put("p2", p2);
				map.put("p3", p3);
				map.put("p4", p4);
				Runnable r=new ClientWrite(Tools.JsonEncode(map));
				new Thread(r).start();
				((HomePageActivity)getActivity()).openVague(HomePageActivity.WAIT_EDIT_TEAM_PROCESS);
			}
			
		});
		
		cancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				setEnable(false);
				((HomePageActivity)getActivity()).onBackPressed();
			}
			
		});
		
		return view;
	}

	private void initiate(){
		startup.setText(p1);
		evolution.setText(p2);
		expansion.setText(p3);
		mature.setText(p4);
	}
	
	@Override
	public int getFragmentLevel() {
		// TODO Auto-generated method stub
		return IdentificationInterface.SECOND_LEVEL;
	}

	@Override
	public String getTeamid() {
		// TODO Auto-generated method stub
		return teamid;
	}

	
	
}
