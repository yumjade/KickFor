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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MySkillsStyleFragment extends Fragment implements HomePageInterface,IdentificationInterface {
	
	private ImageView back;
	private ImageView photo;
	private TextView myName;
	private TextView name;
	private ListView listView;
	private LinearLayout other;
	private RelativeLayout add;

	private List<Skills> mList = new ArrayList<Skills>();
	private SkillsAdapter adapter;
	
	@Override
	public int getFragmentLevel() {
		return IdentificationInterface.SECOND_LEVEL;
	}
	
	
	
	@Override
	public void setEnable(boolean enable) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
			View view = inflater.inflate(R.layout.my_skills_style, container, false);
			back = (ImageView) view.findViewById(R.id.skills_back);
			myName = (TextView) view.findViewById(R.id.tv_my_name);
			photo = (ImageView) view.findViewById(R.id.iv_photo);
			name = (TextView) view.findViewById(R.id.tv_name);
			listView = (ListView) view.findViewById(R.id.skills_list);
			other = (LinearLayout) view.findViewById(R.id.ll_other);
			add = (RelativeLayout) view.findViewById(R.id.skills_add);
			
			back.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					((HomePageActivity) getActivity()).onBackPressed();

				}
			});
			
				init();
				myName.setVisibility(View.VISIBLE);
				other.setVisibility(View.VISIBLE);
				add.setVisibility(View.VISIBLE);
				
				adapter = new SkillsAdapter(getActivity(), R.layout.my_skills_style_item, mList);
				listView.setAdapter(adapter);
		
				listView.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						((HomePageActivity) getActivity()).mySkills();
						
					}
				});
				
				add.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						((HomePageActivity) getActivity()).selectSkills();
						
					}
				});
		
		return view;
	}
	
	private void init(){
		mList.clear();
		Skills item=new Skills(12,"Å£Î²°Í¹ýÈË",null,null,null,null,null,null);
		mList.add(item);
	}

}
