package com.example.kickfor;

import java.util.ArrayList;
import java.util.List;

import com.example.kickfor.utils.IdentificationInterface;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MySkillsFragment extends Fragment implements HomePageInterface,IdentificationInterface{
	
	private ImageView back;
	private ImageView delete;
	private TextView myName;
	private TextView name;
	private ListView listView;
	

	
	@Override
	public void setEnable(boolean enable) {
		// TODO Auto-generated method stub
		
	}

	private List<Skills> mList = new ArrayList<Skills>();
	private SkillsAdapter adapter;
	
	@Override
	public int getFragmentLevel() {
		return IdentificationInterface.SECOND_LEVEL;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
			View view = inflater.inflate(R.layout.my_skills, container, false);
			back = (ImageView) view.findViewById(R.id.skills_back);
			myName = (TextView) view.findViewById(R.id.tv_my_name);
			delete = (ImageView) view.findViewById(R.id.iv_skills_delete);
			name = (TextView) view.findViewById(R.id.tv_name);
			listView = (ListView) view.findViewById(R.id.skills_list);
			
			init();
			
			
			adapter = new SkillsAdapter(getActivity(), R.layout.my_skills_item, mList);
			listView.setAdapter(adapter);
			
			back.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					((HomePageActivity) getActivity()).onBackPressed();

				}
			});
	
		
		
		return view;
	}
	
	private void init(){
		mList.clear();
		Skills item = new Skills(0,null,"大灰狼",BitmapFactory.decodeResource(getResources(), R.drawable.team_default),"爱心联盟","中后卫",null,null);
		mList.add(item);
	}

}
