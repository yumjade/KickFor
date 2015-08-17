package com.example.kickfor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

public class SkillsDetailFragment extends Fragment implements HomePageInterface,IdentificationInterface{
	
	private ImageView back;
	private ImageView delete;
	private TextView myName;
	private TextView name;
	private ListView listView;
	
	private String phone=null;
	private String skillskey=null;

	private void init(){
		Bundle bundle=getArguments();
		this.phone=bundle.getString("phone");
		this.skillskey=bundle.getString("skillskey");
		Map<String, Object> tmp=new HashMap<String, Object>();
		tmp.put("request", "get skillsdetail");
		tmp.put("skillkey", skillskey);
		tmp.put("phone", phone);
		Runnable r=new ClientWrite(Tools.JsonEncode(tmp));
		new Thread(r).start();
	}
	
	@Override
	public void setEnable(boolean enable) {
		// TODO Auto-generated method stub
		
	}

	private List<SkillsEntity> mList = new ArrayList<SkillsEntity>();
	private SkillsAdapter adapter;
	
	@Override
	public int getFragmentLevel() {
		return IdentificationInterface.SECOND_LEVEL;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		init();
		View view = inflater.inflate(R.layout.skills_detail, container, false);
		back = (ImageView) view.findViewById(R.id.skills_back);
		myName = (TextView) view.findViewById(R.id.tv_my_name);
		delete = (ImageView) view.findViewById(R.id.iv_skills_delete);
		name = (TextView) view.findViewById(R.id.tv_name);
		listView = (ListView) view.findViewById(R.id.skills_list);
			
			
			
		adapter = new SkillsAdapter(getActivity(), R.layout.skills_detail_item, mList);
		listView.setAdapter(adapter);
			
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((HomePageActivity) getActivity()).onBackPressed();
			}
		});
	
		
		
		return view;
	}
	

}
