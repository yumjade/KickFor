package com.example.kickfor.team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kickfor.ClientWrite;
import com.example.kickfor.HomePageActivity;
import com.example.kickfor.R;
import com.example.kickfor.Tools;
import com.example.kickfor.utils.IdentificationInterface;

public class EditTeamGradeFragment extends Fragment implements TeamInterface, IdentificationInterface{

	private String teamid=null;
	private Context context=null;
	
	private ListView mListView=null;
	private RelativeLayout addNew=null;
	private TextView ensure=null;
	private TextView cancel=null;
	private ImageView back=null;
	
	private List<HonorInfo> mList=null;
	private TeamInfoHornorAdapter adapter=null;
	
	private void init(){
		this.mList=new ArrayList<HonorInfo>();
		this.context=getActivity();
		Bundle bundle=getArguments();
		this.teamid=bundle.getString("teamid");
		bundle.remove("teamid");
		Iterator<String> iter=bundle.keySet().iterator();
		while(iter.hasNext()){
			String key=iter.next();
			if(bundle.getSerializable(key) instanceof HonorInfo){
				HonorInfo tmp=(HonorInfo)bundle.getSerializable(key);
				this.mList.add(tmp);
			}
		}
	}
	
	@Override
	public void setEnable(boolean enable) {
		// TODO Auto-generated method stub
		if(addNew!=null){
			addNew.setEnabled(enable);
			cancel.setEnabled(enable);
			ensure.setEnabled(enable);
			for(int i=0; i<mListView.getChildCount(); i++){
				View view=mListView.getChildAt(i);
				ImageView c=(ImageView)view.findViewById(R.id.honor_cancel);
				TextView year=(TextView)view.findViewById(R.id.honor_year);
				TextView result=(TextView)view.findViewById(R.id.honor_result);
				EditText e=(EditText)view.findViewById(R.id.honor_name);
				if(c!=null && year!=null && result!=null && e!=null){
					c.setEnabled(enable);
					year.setEnabled(enable);
					result.setEnabled(enable);
					e.setEnabled(enable);
				}
			}
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		init();
		View view=inflater.inflate(R.layout.fragment_edit_grade, container, false);
		back=(ImageView)view.findViewById(R.id.edit_history_back);
		mListView=(ListView)view.findViewById(R.id.edit_history_honor);
		addNew=(RelativeLayout)view.findViewById(R.id.edit_history_new);
		cancel=(TextView)view.findViewById(R.id.edit_history_cancel);
		ensure=(TextView)view.findViewById(R.id.edit_history_ensure);
		
		adapter=new TeamInfoHornorAdapter(context, mList, true, mListView);
		mListView.setAdapter(adapter);
		Tools.setListViewHeight(mListView);
		back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((HomePageActivity)getActivity()).onBackPressed();
			}
			
		});
		
		addNew.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean noNull=true;
				Iterator<HonorInfo> iter=mList.iterator();
				while(iter.hasNext()){
					HonorInfo item=iter.next();
					if(item.getUpdateName().isEmpty() || item.getUpdateYear().isEmpty() || item.getUpdateResult().isEmpty()){
						noNull=false;
						Toast.makeText(context, "您有条目未完善", Toast.LENGTH_SHORT).show();
						break;
					}
				}
				if(noNull==true){
					HonorInfo newItem=null;
					newItem=new HonorInfo(-1);
					newItem.setName("");
					newItem.setYear("");
					newItem.setResult("");
					newItem.setUpdateName("");
					newItem.setUpdateYear("");
					newItem.setUpdateResult("");
					mList.add(newItem);
					adapter.notifyDataSetChanged();
					Tools.setListViewHeight(mListView);
				}
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
		
		ensure.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				boolean noNull=true;
				setEnable(false);
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("request", "change result");
				map.put("teamid", teamid);
				Iterator<HonorInfo> iter=mList.iterator();
				int index=1;
				Map<String, Object> temp=new HashMap<String ,Object>();
				while(iter.hasNext()){
					HonorInfo item=iter.next();
					temp.clear();
					if(item.getChanged()){
						temp.put("id", item.getId());
						temp.put("year", item.getUpdateYear());
						temp.put("name", item.getUpdateName());
						temp.put("result", item.getUpdateResult());
						map.put(String.valueOf(index), Tools.MapToJson(temp));
						item.setYear(item.getUpdateYear());
						item.setName(item.getUpdateName());
						item.setResult(item.getUpdateResult());
						index=index+1;
					}
					if(item.getUpdateName().isEmpty() || item.getUpdateYear().isEmpty() || item.getUpdateResult().isEmpty()){
						noNull=false;
						Toast.makeText(context, "您有条目未完善", Toast.LENGTH_SHORT).show();
						setEnable(true);
						break;
					}
				}
				if(noNull==true){
					
					List<HonorInfo> mList1=adapter.getDeleteList();
					Iterator<HonorInfo> iter1=mList1.iterator();
					while(iter1.hasNext()){
						temp.clear();
						HonorInfo item=iter1.next();
						temp.put("id", item.getId());
						map.put(String.valueOf(index), Tools.MapToJson(temp));
						index++;
					}
					map.put("number", mList.size()+mList1.size());
					Runnable r=new ClientWrite(Tools.JsonEncode(map));
					new Thread(r).start();
					((HomePageActivity)getActivity()).openVague(HomePageActivity.WAIT_EDIT_TEAM_GRADE);
				}
			}
			
		});
		return view;
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
