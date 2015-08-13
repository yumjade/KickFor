package com.example.kickfor.team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.example.kickfor.HomePageActivity;
import com.example.kickfor.ClientWrite;
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
import android.widget.ImageView;
import android.widget.ListView;

public class MatchLogFragment extends Fragment implements TeamInterface, IdentificationInterface{
	
	private List<MatchLogEntity> mList=new ArrayList<MatchLogEntity>();
	private ListView mListView=null;
	private Context context=null;
	private MatchLogAdapter adapter=null;
	private String teamid=null;
	private ImageView back=null;
	
	@Override
	public int getFragmentLevel() {
		// TODO Auto-generated method stub
		return IdentificationInterface.SECOND_LEVEL;
	}
	
	private void init(){
		Bundle bundle=getArguments();
		context=getActivity();
		this.teamid=bundle.getString("teamid");
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("request", "get match by type");
		map.put("teamid", teamid);
		Runnable r=new ClientWrite(Tools.JsonEncode(map));
		new Thread(r).start();
	}
	
	@Override
	public void setEnable(boolean enable) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		init();
		View view=inflater.inflate(R.layout.fragment_match_log, container, false);
		mListView=(ListView)view.findViewById(R.id.match_log_list);
		back=(ImageView)view.findViewById(R.id.match_log_back);
		back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((HomePageActivity)getActivity()).onBackPressed();
			}
			
		});
		adapter=new MatchLogAdapter(this.context, mList);
		mListView.setAdapter(adapter);
		return view;
	}
	
	
	
	public void setData(Map<String, Object> map){
		Iterator<String> iter=map.keySet().iterator();
		int totalMatch=0;
		int win=0;
		int pull=0;
		int defeat=0;
		int totalGoal=0;
		int totalLost=0;
		while(iter.hasNext()){
			String key=iter.next();
			Map<String, Object> temp=Tools.getMapForJson(map.get(key).toString());
			win=win+Integer.parseInt(temp.get("win").toString());
			pull=pull+Integer.parseInt(temp.get("pull").toString());
			defeat=defeat+Integer.parseInt(temp.get("defeat").toString());
			totalGoal=totalGoal+Integer.parseInt(temp.get("goals").toString());
			totalLost=totalLost+Integer.parseInt(temp.get("lost").toString());
			totalMatch=totalMatch+Integer.parseInt(temp.get("number").toString());
			MatchLogEntity item=new MatchLogEntity(temp.get("type").toString(), temp.get("number").toString(), temp.get("result").toString(), temp.get("goals").toString(), temp.get("lost").toString());
			mList.add(item);
		}
		MatchLogEntity last=new MatchLogEntity("×Ü¼Æ", String.valueOf(totalMatch), win+"/"+pull+"/"+defeat, String.valueOf(totalGoal), String.valueOf(totalLost));
		mList.add(last);
		if(adapter!=null)
			adapter.notifyDataSetChanged();
	}
	
	public String getTeamid(){
		return teamid;
	}
}
