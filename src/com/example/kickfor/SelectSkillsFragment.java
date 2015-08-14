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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class SelectSkillsFragment extends Fragment implements HomePageInterface,IdentificationInterface{
	
	private ImageView back;
	private ListView listView;

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
			listView = (ListView) view.findViewById(R.id.skills_list);
			
			back.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					((HomePageActivity) getActivity()).onBackPressed();

				}
			});
			
			init();
				
			adapter = new SkillsAdapter(getActivity(), R.layout.my_skills_style_item1, mList);
			listView.setAdapter(adapter);
		
		return view;
	}
	
	private void init(){
		mList.clear();
		Skills item=new Skills(0,"Å£Î²°Í¹ýÈË",null,null,null,null,BitmapFactory.decodeResource(getResources(), R.drawable.skills_selected),
				BitmapFactory.decodeResource(getResources(), R.drawable.skills_unselected));
		mList.add(item);
	}


}
