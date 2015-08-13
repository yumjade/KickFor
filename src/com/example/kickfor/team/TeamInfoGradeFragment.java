package com.example.kickfor.team;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.example.kickfor.HomePageActivity;
import com.example.kickfor.R;
import com.example.kickfor.Tools;
import com.example.kickfor.utils.IdentificationInterface;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class TeamInfoGradeFragment extends Fragment implements OnClickListener, TeamInterface, IdentificationInterface{

	private EditText create=null;
	private EditText developing=null;
	private EditText expanding=null;
	private EditText developed=null;
	private TextView edit1=null;
	private String text1=null;
	private String text2=null;
	private String text3=null;
	private String text4=null;
	
	
	private String teamid=null;
	private int authority=-1;
	
	private ListView mListView=null;
	private List<HonorInfo> mList=new ArrayList<HonorInfo>();
	private TextView edit2=null;
	private TeamInfoHornorAdapter adapter=null;
	private Context context=null;
	
	@Override
	public void setEnable(boolean enable) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public int getFragmentLevel() {
		// TODO Auto-generated method stub
		return IdentificationInterface.SECOND_LEVEL;
	}
	
	private void init(){
		context=getActivity();
		Bundle bundle=getArguments();
		this.teamid=bundle.getString("teamid");
		this.authority=Integer.parseInt(bundle.getString("authority"));
	}
	
	public void setData(String text1, String text2, String text3, String text4){
		this.text1=text1;
		this.text2=text2;
		this.text3=text3;
		this.text4=text4;
		initiate1();
	}
	
	public void setList(List<HonorInfo> list){
		mList.clear();
		Iterator<HonorInfo> iter=list.iterator();
		while(iter.hasNext()){
			mList.add(iter.next());
		}
		if(adapter!=null){
			adapter.notifyDataSetChanged();
		}
		Tools.setListViewHeight(mListView);
	}
	
	
	
	
	public String getTeamid(){
		return teamid;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		init();
		View view=inflater.inflate(R.layout.fragment_team_info_history, container, false);
		create=(EditText)view.findViewById(R.id.team_history_start);
		developing=(EditText)view.findViewById(R.id.team_history_developing);
		expanding=(EditText)view.findViewById(R.id.team_history_expanding);
		developed=(EditText)view.findViewById(R.id.team_history_developed);
		edit1=(TextView)view.findViewById(R.id.team_history_edit1);
		initiate1();
		
		mListView=(ListView)view.findViewById(R.id.team_history_honor);
		edit2=(TextView)view.findViewById(R.id.team_history_edit2);
		initiate2();
		return view;
	}
	
	private void initiate1(){
		create.setText(text1);
		developing.setText(text2);
		expanding.setText(text3);
		developed.setText(text4);
		if(authority==4){
			edit1.setOnClickListener(this);
		}
		else{
			edit1.setVisibility(View.GONE);
		}
		
	}
	
	private void initiate2(){
		
		adapter=new TeamInfoHornorAdapter(context, mList, false, mListView);
		mListView.setAdapter(adapter);
		Tools.setListViewHeight(mListView);
		create.setEnabled(false);
		developing.setEnabled(false);
		expanding.setEnabled(false);
		developed.setEnabled(false);
		if(authority==4){
			edit2.setOnClickListener(this);
		}
		else{
			edit2.setVisibility(View.GONE);
		}
	}
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id=v.getId();
		switch(id){
		case R.id.team_history_edit1:{
			Bundle bundle=new Bundle();
			bundle.putString("teamid", teamid);
			bundle.putString("p1", text1);
			bundle.putString("p2", text2);
			bundle.putString("p3", text3);
			bundle.putString("p4", text4);
			((HomePageActivity)getActivity()).openEditTeamHistory(bundle);
			break;
		}
		case R.id.team_history_edit2:{
			Bundle bundle=new Bundle();
			bundle.putString("teamid", teamid);
			Iterator<HonorInfo> iter=mList.iterator();
			int i=1;
			while(iter.hasNext()){
				bundle.putSerializable(String.valueOf(i), iter.next());
				i++;
			}
			((HomePageActivity)getActivity()).openEditTeamHonor(bundle);
			break;
		}
		
		}
	}







}
