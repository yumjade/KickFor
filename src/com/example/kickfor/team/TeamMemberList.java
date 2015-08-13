package com.example.kickfor.team;

import java.util.ArrayList;
import java.util.List;

import com.example.kickfor.R;
import com.example.kickfor.SQLHelper;
import com.example.kickfor.utils.IdentificationInterface;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class TeamMemberList extends Fragment implements TeamInterface, IdentificationInterface{
	
	private List<MemberInfo> mList=new ArrayList<MemberInfo>();
	private ListView mListView=null;
	private Context context=null;
	private String teamid=null;
	private TeamMemberAdapter adapter=null;
	
	@Override
	public void setEnable(boolean enable) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public int getFragmentLevel() {
		// TODO Auto-generated method stub
		return IdentificationInterface.SECOND_LEVEL;
	}
	
	@Override
	public String getTeamid() {
		return teamid;
	}



	private void init(){
		context=getActivity();
		Bundle bundle=getArguments();
		this.teamid=bundle.getString("teamid");
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		init();
		View view=inflater.inflate(R.layout.fragment_normal_list1, container, false);
		mListView=(ListView)view.findViewById(R.id.normal_list_1);
		adapter=new TeamMemberAdapter(context, mList);
		mListView.setAdapter(adapter);
		initiate();
		return view;
	}

	private void initiate(){
		mList.clear();
		String tableName="f_"+teamid;
		SQLHelper helper=SQLHelper.getInstance(context);
		helper.createTeamMateTable(tableName);
		Cursor cursor=helper.select(tableName, new String[]{"number", "name", "attendance", "totalmatch", "phone", "position1", "position2"}, null, null, "attendance desc");
		while(cursor.moveToNext()){
			MemberInfo item=new MemberInfo(cursor.getString(4), context);
			item.setName(cursor.getString(1));
			item.setNumber(cursor.getString(0));
			item.setAInfo(cursor.getString(2), cursor.getString(3));
			item.setPosition(cursor.getString(5), cursor.getString(6));
			mList.add(item);
		}
		adapter.notifyDataSetChanged();
		
	}




}
