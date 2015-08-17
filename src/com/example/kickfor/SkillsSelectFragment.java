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
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SkillsSelectFragment extends Fragment implements HomePageInterface,IdentificationInterface{
	
	private ImageView back;
	private ListView listView;

	private List<SkillsEntity> mList = new ArrayList<SkillsEntity>();
	private SkillsAdapter adapter;
	
	private String phone=null;
	
	@Override
	public int getFragmentLevel() {
		return IdentificationInterface.SECOND_LEVEL;
	}
	
	
	
	@Override
	public void setEnable(boolean enable) {
		// TODO Auto-generated method stub
		
	}


	private void init(){
		this.phone=new PreferenceData(getActivity()).getData(new String[]{"phone"}).get("phone").toString();
		Map<String, Object> tmp=new HashMap<String, Object>();
		tmp.put("request", "get skills");
		tmp.put("phone", phone);
		Runnable r=new ClientWrite(Tools.JsonEncode(tmp));
		new Thread(r).start();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		init();
		View view = inflater.inflate(R.layout.skills_select, container, false);
		back = (ImageView) view.findViewById(R.id.skills_back);
		listView = (ListView) view.findViewById(R.id.skills_list);
			
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((HomePageActivity) getActivity()).onBackPressed();

			}
		});
			
				
		adapter = new SkillsAdapter(getActivity(), R.layout.skills_select_item, mList);
		listView.setAdapter(adapter);
		
		return view;
	}
	


}
