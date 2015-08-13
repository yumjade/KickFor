package com.example.kickfor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.example.kickfor.utils.IdentificationInterface;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class FileEditFragment extends Fragment implements HomePageInterface, IdentificationInterface{
	
	private String phone;
	private String teamname;
	private String position;
	private String joindate;
	private String exitdate;
	private String inteam;
	private String matchstr;
	
	private String eventname;
	private String winningyear;
	private String finalrank;
	
	private ImageView back;
	private TextView edit;
	
	private EditText teamName;
	private EditText fieldPosition;
	private EditText joinTime;
	private EditText leaveTime;
	
	private ImageView inTeam;
	private ImageView outTeam;
	
	private ListView listView;
	private RelativeLayout add;
	private EditText eventName;
	private EditText winningYear;
	private EditText finalRank;
	
	private Handler handler = new Handler();
	
	private FileEditAdapter adapter;
	private List<File> mList=new ArrayList<File>();


	
	private Context context=null;
	
	private Boolean isOpen = true;

	public String getPhone(){
		return phone;
	}
	
	@Override
	public int getFragmentLevel() {
		return IdentificationInterface.SECOND_LEVEL;
	}
	
	
	
	@Override
	public void setEnable(boolean enable) {
		// TODO Auto-generated method stub
		
	}

	private void init(){
		Bundle bundle=getArguments();
		this.context=getActivity();
		this.phone=bundle.getString("phone");
		this.teamname=bundle.getString("teamname");
		this.position=bundle.getString("position");
		this.inteam=bundle.getString("inteam");
		this.joindate=bundle.getString("joindate");
		this.exitdate=bundle.getString("exitdate");
		this.matchstr=bundle.getString("matchstr");
//		this.eventname=bundle.getString("eventname");
//		this.winningyear=bundle.getString("winningyear");
//		this.finalrank=bundle.getString("finalrank");
	}

	
//	public void setEnable(boolean enable) {
//		back.setEnabled(enable);
//		add.setEnabled(enable);
//		for(int i=0; i<listView.getChildCount(); i++){
//			View view = listView.getChildAt(i);
//			eventName = (EditText)view.findViewById(R.id.et_event_name);
//			winningYear = (EditText)view.findViewById(R.id.et_winning_year);
//			finalRank = (EditText)view.findViewById(R.id.et_final_rank);
//			eventName.setEnabled(enable);
//			winningYear.setEnabled(enable);
//			finalRank.setEnabled(enable);
//		}
//		listView.setEnabled(enable);
//	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
//		init();
		View view = inflater.inflate(R.layout.file_edit, container, false);
		back = (ImageView) view.findViewById(R.id.file_edit_back);
		edit = (TextView) view.findViewById(R.id.detail_edit);
		teamName = (EditText) view.findViewById(R.id.et_edit_team_name);
		fieldPosition = (EditText) view.findViewById(R.id.et_file_position);
		
		inTeam = (ImageView) view.findViewById(R.id.iv_switch_open);
		outTeam = (ImageView) view.findViewById(R.id.iv_switch_close);
		
		joinTime = (EditText) view.findViewById(R.id.et_join_time);
		leaveTime = (EditText) view.findViewById(R.id.et_leave_time);
		listView = (ListView) view.findViewById(R.id.lv_match_more);
		for (int i = 0; i < listView.getChildCount(); i++) {
		     RelativeLayout layout = (RelativeLayout)listView.getChildAt(i);
//		     eventName = (EditText) layout.findViewById(R.id.et_event_name);
		     eventName = (EditText) listView.getAdapter().getItem(i);
		     winningYear = (EditText) layout.findViewById(R.id.et_winning_year);
		     finalRank = (EditText) layout.findViewById(R.id.et_final_rank);
		}    
		
//		eventName = (EditText)view.findViewById(R.id.et_event_name);
//		winningYear = (EditText)view.findViewById(R.id.et_winning_year);
//		finalRank = (EditText)view.findViewById(R.id.et_final_rank);
		
		
		
		add = (RelativeLayout) view.findViewById(R.id.btn_add);
		
		adapter = new FileEditAdapter(getActivity(), R.layout.event_item, mList);
		
		listView.setAdapter(adapter);
		
		Tools.setListViewHeight(listView);
		
		setList();
//		if (isOpen) {
//			isOpen = false;
//			inTeam.setVisibility(View.INVISIBLE);
//			outTeam.setVisibility(View.VISIBLE);
//		} else {
//			isOpen = true;
//			inTeam.setVisibility(View.VISIBLE);
//			outTeam.setVisibility(View.INVISIBLE);
//		}
		
		
		
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((HomePageActivity)getActivity()).onBackPressed();
				
			}
		});
		
		add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				handler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						setList();
					}
				}, 100);
				
			}
		});
		
		edit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				teamname = teamName.getText().toString();
				position = fieldPosition.getText().toString();
				joindate = joinTime.getText().toString();
				exitdate = leaveTime.getText().toString();
				System.out.println("exitdate = " + exitdate);
//				System.out.println(eventName.getText().toString());
//				System.out.println(winningYear.getText().toString());
//				System.out.println(finalRank.getText().toString());
				matchstr = eventName.getText().toString(); 
//						+ winningYear.getText().toString() + finalRank.getText().toString();
//				StringBuilder sb = new StringBuilder();
//				matchstr = sb.append(eventName.getText().toString())
//				.append(winningYear.getText().toString())
//				.append(finalRank.getText().toString())
//				.toString();
				
				if(teamname.isEmpty()){
					Toast.makeText(getActivity(), "球队名称不能为空", Toast.LENGTH_SHORT).show();
				}
				else if(position.isEmpty()){
					Toast.makeText(getActivity(), "场上位置不能为空", Toast.LENGTH_SHORT).show();
				}
				else if(joindate.isEmpty()){
					Toast.makeText(getActivity(), "加入时间不能为空", Toast.LENGTH_SHORT).show();
				}
				else if(exitdate.isEmpty()){
					Toast.makeText(getActivity(), "离开时间不能为空", Toast.LENGTH_SHORT).show();
				}else{
					Map<String, Object> tmp=new HashMap<String, Object>();
					tmp.put("phone", new PreferenceData(getActivity()).getData(new String[]{"phone"}).get("phone").toString());
					tmp.put("request", "add userarchives");
					tmp.put("teamname", teamname);
					tmp.put("position", position);
					tmp.put("joindate", joindate);
					tmp.put("exitdate", exitdate);
					tmp.put("mathstr", matchstr);
		
					Runnable r=new ClientWrite(Tools.JsonEncode(tmp));
					new Thread(r).start();
					((HomePageActivity)getActivity()).onBackPressed();
					
				}
			}
		});
		
		return view;
	}
	
	public void loadData(){

			Map<String, Object> tmp = new HashMap<String, Object>();
			Iterator<File> iter = mList.iterator();
			while (iter.hasNext()) {
				File item = iter.next();
			
				if (item.isChanged()) {
					tmp.put("request", "add userarchives");
					tmp.put("eventname", item.getEventName());
					tmp.put("winningyear", item.getWinningYear());
					tmp.put("finalrank", item.getFinalRank());
					Runnable r = new ClientWrite(Tools.JsonEncode(tmp));
					new Thread(r).start();
				}
			}
		
	}
	
	private void setList(){
//		mList.clear();
		File item=new File( null, null, null,null, null,"", "", "");
//		Map<String, Object> tmp=new HashMap<String, Object>();
//		tmp.put("list_item_inputvalue", item);
//		mCheckItemList.add(tmp);
		mList.add(item);
		adapter.notifyDataSetChanged();
		Tools.setListViewHeight(listView);
	}

}
