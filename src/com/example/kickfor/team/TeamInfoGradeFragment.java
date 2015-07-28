package com.example.kickfor.team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;








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
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class TeamInfoGradeFragment extends Fragment implements OnClickListener, TeamInterface, IdentificationInterface{

	private EditText create=null;
	private EditText developing=null;
	private EditText expanding=null;
	private EditText developed=null;
	private TextView edit1=null;
	private LinearLayout item1=null;
	private TextView cancel1=null;
	private TextView ensure1=null;
	private String text1=null;
	private String text2=null;
	private String text3=null;
	private String text4=null;
	
	private String teamid=null;
	private String authority=null;
	
	private ListView mListView=null;
	private List<HonorInfo> mList=new ArrayList<HonorInfo>();
	private LinearLayout item2=null;
	private TextView edit2=null;
	private TextView ensure2=null;
	private TextView cancel2=null;
	private TeamInfoHornorAdapter adapter=null;
	private Context context=null;
	
	@Override
	public int getFragmentLevel() {
		// TODO Auto-generated method stub
		return IdentificationInterface.SECOND_LEVEL;
	}
	
	private void init(){
		context=getActivity();
		Bundle bundle=getArguments();
		this.teamid=bundle.getString("teamid");
		this.authority=bundle.getString("authority");
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
		item1=(LinearLayout)view.findViewById(R.id.team_history_item1);
		ensure1=(TextView)view.findViewById(R.id.team_history_ensure1);
		cancel1=(TextView)view.findViewById(R.id.team_history_cancel1);
		initiate1();
		
		mListView=(ListView)view.findViewById(R.id.team_history_honor);
		edit2=(TextView)view.findViewById(R.id.team_history_edit2);
		item2=(LinearLayout)view.findViewById(R.id.team_history_item2);
		ensure2=(TextView)view.findViewById(R.id.team_history_ensure2);
		cancel2=(TextView)view.findViewById(R.id.team_history_cancel2);
		initiate2();
		return view;
	}
	
	private void initiate1(){
		create.setText(text1);
		developing.setText(text2);
		expanding.setText(text3);
		developed.setText(text4);
		if(authority.equals("4")){
			edit1.setOnClickListener(this);
			ensure1.setOnClickListener(this);
			cancel1.setOnClickListener(this);
		}
		else{
			edit1.setVisibility(View.GONE);
		}
		
	}
	
	private void initiate2(){
		
		adapter=new TeamInfoHornorAdapter(context, mList);
		mListView.setAdapter(adapter);
		Tools.setListViewHeight(mListView);
		create.setEnabled(false);
		developing.setEnabled(false);
		expanding.setEnabled(false);
		developed.setEnabled(false);
		if(authority.equals("4")){
			edit2.setOnClickListener(this);
			ensure2.setOnClickListener(this);
			cancel2.setOnClickListener(this);
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
			edit1.setVisibility(View.GONE);
			item1.setVisibility(View.VISIBLE);
			create.setBackgroundResource(R.drawable.rect_4);
			developing.setBackgroundResource(R.drawable.rect_4);
			expanding.setBackgroundResource(R.drawable.rect_4);
			developed.setBackgroundResource(R.drawable.rect_4);
			create.setEnabled(true);
			developing.setEnabled(true);
			expanding.setEnabled(true);
			developed.setEnabled(true);
			break;
		}
		case R.id.team_history_cancel1:{
			edit1.setVisibility(View.VISIBLE);
			item1.setVisibility(View.GONE);
			create.setText(text1);
			create.setBackground(null);
			developing.setText(text2);
			developing.setBackground(null);
			expanding.setText(text3);
			expanding.setBackground(null);
			developed.setText(text4);
			developed.setBackground(null);
			create.setEnabled(false);
			developing.setEnabled(false);
			expanding.setEnabled(false);
			developed.setEnabled(false);
			break;
		}
		case R.id.team_history_ensure1:{
			edit1.setVisibility(View.VISIBLE);
			item1.setVisibility(View.GONE);
			text1=create.getText().toString();
			create.setBackground(null);
			text2=developing.getText().toString();
			developing.setBackground(null);
			text3=expanding.getText().toString();
			expanding.setBackground(null);
			text4=developed.getText().toString();
			developed.setBackground(null);
			create.setEnabled(false);
			developing.setEnabled(false);
			expanding.setEnabled(false);
			developed.setEnabled(false);
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("request", "change process");
			map.put("teamid", teamid);
			map.put("p1", text1);
			map.put("p2", text2);
			map.put("p3", text3);
			map.put("p4", text4);
			Runnable r=new ClientWrite(Tools.JsonEncode(map));
			new Thread(r).start();
			break;
		}
		case R.id.team_history_edit2:{
			edit2.setVisibility(View.GONE);
			item2.setVisibility(View.VISIBLE);
			HonorInfo newItem=null;
			if(mList.size()==0){
				newItem=new HonorInfo(1);
			}
			else{
				int last=mList.size()-1;
				newItem=new HonorInfo(mList.get(last).getId()+1);
			}
			newItem.setName("");
			newItem.setYear("");
			newItem.setResult("");
			newItem.setUpdateName("");
			newItem.setUpdateYear("");
			newItem.setUpdateResult("");
			mList.add(newItem);
			adapter.setEditStatus(true);
			adapter.notifyDataSetChanged();
			Tools.setListViewHeight(mListView);
			break;
		}
		case R.id.team_history_ensure2:{
			edit2.setVisibility(View.VISIBLE);
			item2.setVisibility(View.GONE);
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("request", "change result");
			map.put("teamid", teamid);
			map.put("number", mList.size());
			Iterator<HonorInfo> iter=mList.iterator();
			int index=1;
			while(iter.hasNext()){
				HonorInfo item=iter.next();
				Map<String, Object> temp=new HashMap<String ,Object>();
				if(item.getChanged()){
					temp.put("id", item.getId());
					temp.put("year", item.getUpdateYear());
					temp.put("name", item.getUpdateName());
					temp.put("result", item.getUpdateResult());
					map.put(String.valueOf(index), Tools.MapToJson(temp));
					item.setYear(item.getUpdateYear());
					item.setName(item.getUpdateName());
					item.setResult(item.getUpdateResult());
				}
				if(item.getUpdateName().equals("")){
					temp.put("id", item.getId());
					temp.put("year", "");
					temp.put("name", "");
					temp.put("result", "");
					map.put(String.valueOf(index), Tools.MapToJson(temp));
					mList.remove(index-1);
				}
				index=index+1;
			}
			adapter.setEditStatus(false);
			adapter.notifyDataSetChanged();
			Tools.setListViewHeight(mListView);
			Runnable r=new ClientWrite(Tools.JsonEncode(map));
			new Thread(r).start();
			break;
		}
		case R.id.team_history_cancel2:{
			edit2.setVisibility(View.VISIBLE);
			item2.setVisibility(View.GONE);
			int last=mList.size()-1;
			mList.remove(last);
			adapter.setEditStatus(false);
			adapter.notifyDataSetChanged();
			Tools.setListViewHeight(mListView);
			break;
		}
		}
	}







}
