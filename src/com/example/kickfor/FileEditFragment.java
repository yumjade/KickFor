package com.example.kickfor;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.example.kickfor.utils.IdentificationInterface;
import com.example.kickfor.wheelview.ScreenInfo;
import com.example.kickfor.wheelview.WheelDate;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
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

public class FileEditFragment extends Fragment implements HomePageInterface, IdentificationInterface, HandlerListener{
	
	private String phone=null;
	private String teamname=null;
	private String position=null;
	private String joindate=null;
	private String exitdate=null;
	private boolean inteam=false;
	
	private int userarchivekey=-1;
	
	private ImageView back=null;
	private TextView ensure=null;
	
	private EditText teamName=null;
	private TextView fieldPosition=null;
	private TextView joinTime=null;
	private TextView leaveTime=null;
	private TextView leaveTimeText=null;
	
	private ImageView inTeam=null;
	private ImageView outTeam=null;
	
	private ListView listView=null;
	private RelativeLayout add=null;
	private TextView cancel=null;
	
	
	private FileEditAdapter adapter=null;
	private List<SubFile> mList=null;


	
	private Context context=null;
	
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
		mList=new ArrayList<SubFile>();
		this.context=getActivity();
		Bundle bundle=getArguments();
		FileEntity entity=(FileEntity)bundle.getSerializable("entity");
		this.phone=entity.getPhone();
		this.teamname=entity.getTeamName();
		this.position=entity.getPosition();
		this.inteam=entity.isInTeam();
		this.joindate=entity.getJoinDate();
		this.exitdate=entity.getExitDate();
		this.userarchivekey=entity.getUserArchiveKey();
		Iterator<SubFile> iter=entity.getMatch().iterator();
		while(iter.hasNext()){
			mList.add(iter.next());
		}
	}

	

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		init();
		View view = inflater.inflate(R.layout.file_edit, container, false);
		leaveTimeText=(TextView)view.findViewById(R.id.tv_leave_time);
		cancel=(TextView)view.findViewById(R.id.file_edit_cancel);
		cancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				new AlertDialog.Builder(context)
				.setTitle("您确认删除此档案吗？")
				.setPositiveButton("确认删除",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								if(userarchivekey!=-1){
									Map<String, Object> tmp=new HashMap<String, Object>();
									tmp.put("request", "del userarchives");
									tmp.put("userarchiveskey", userarchivekey);
									Runnable r=new ClientWrite(Tools.JsonEncode(tmp));
									new Thread(r).start();
								}
								else{
									((HomePageActivity)getActivity()).onBackPressed();
								}
							}
						})
				.setNegativeButton("我点错了",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
						}).show();
			}
			
		});
		back = (ImageView) view.findViewById(R.id.file_edit_back);
		ensure = (TextView) view.findViewById(R.id.detail_edit);
		teamName = (EditText) view.findViewById(R.id.et_edit_team_name);
		fieldPosition = (TextView) view.findViewById(R.id.et_file_position);
		fieldPosition.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((HomePageActivity)getActivity()).getInPosition(fieldPosition.getText().toString(), 4);
			}
			
		});
		inTeam = (ImageView) view.findViewById(R.id.iv_switch_open);
		outTeam = (ImageView) view.findViewById(R.id.iv_switch_close);
		
		inTeam.setVisibility(View.GONE);
		outTeam.setVisibility(View.VISIBLE);
		inteam=false;
		
		inTeam.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				inTeam.setVisibility(View.GONE);
				outTeam.setVisibility(View.VISIBLE);
				inteam=false;
				leaveTime.setVisibility(View.VISIBLE);
				leaveTimeText.setVisibility(View.VISIBLE);
			}
			
		});
		
		outTeam.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				outTeam.setVisibility(View.GONE);
				inTeam.setVisibility(View.VISIBLE);
				inteam=true;
				leaveTime.setVisibility(View.GONE);
				leaveTimeText.setVisibility(View.GONE);
			}
			
		});
		
		joinTime = (TextView) view.findViewById(R.id.et_join_time);
		joinTime.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LayoutInflater inflater=LayoutInflater.from(context);
				final View datepickerview=inflater.inflate(R.layout.datepicker, null);
				final WheelDate wheelDate=new WheelDate(datepickerview);
				ScreenInfo screenInfo=new ScreenInfo(getActivity());
				wheelDate.screenheight=screenInfo.getHeight();
				wheelDate.initDatePicker(2000, 1, 1);
				new AlertDialog.Builder(context)
				.setTitle("选择日期")
				.setView(datepickerview)
				.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								joinTime.setText(wheelDate.getDate());
								joindate=wheelDate.getDate();
							}
						})
				.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
						}).show();
			}
		});
		leaveTime = (TextView) view.findViewById(R.id.et_leave_time);
		leaveTime.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LayoutInflater inflater=LayoutInflater.from(context);
				final View datepickerview=inflater.inflate(R.layout.datepicker, null);
				final WheelDate wheelDate=new WheelDate(datepickerview);
				ScreenInfo screenInfo=new ScreenInfo(getActivity());
				wheelDate.screenheight=screenInfo.getHeight();
				Calendar c=Calendar.getInstance();
				wheelDate.initDatePicker(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
				new AlertDialog.Builder(context)
				.setTitle("选择日期")
				.setView(datepickerview)
				.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								leaveTime.setText(wheelDate.getDate());
								exitdate=wheelDate.getDate();
							}
						})
				.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
						}).show();
			}
		});
		listView = (ListView) view.findViewById(R.id.lv_match_more);
		  
		
		add = (RelativeLayout) view.findViewById(R.id.btn_add);
		adapter = new FileEditAdapter(context, mList, listView);
		listView.setAdapter(adapter);
		Tools.setListViewHeight(listView);
	
		back.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((HomePageActivity)getActivity()).onBackPressed();
				
			}
		});
		
		add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				boolean canAdd=true;
				Iterator<SubFile> iter=mList.iterator();
				while(iter.hasNext()){
					if(iter.next().isEmptyExist()){
						canAdd=false;
						break;
					}
				}
				if(canAdd==true){
					SubFile item=new SubFile();
					mList.add(item);
					adapter.notifyDataSetChanged();
					Tools.setListViewHeight(listView);
				}
				else{
					Toast.makeText(context, "存在未完善条目", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		ensure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				teamname = teamName.getText().toString();
				position = fieldPosition.getText().toString();
				joindate = joinTime.getText().toString();
				exitdate = inteam? "0000-00-00": leaveTime.getText().toString();

				
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
					String info="";
					boolean canSend=true;
					Iterator<SubFile> iter=mList.iterator();
					while(iter.hasNext()){
						SubFile temp=iter.next();
						if(temp.isEmptyExist()){
							Toast.makeText(context, "存在未完善条目", Toast.LENGTH_SHORT).show();
							canSend=false;
							break;
						}
						if(iter.hasNext()){
							info=info+temp.getMatchName()+":"+temp.getYear()+":"+temp.getRanking()+",";
						}
						else{
							info=info+temp.getMatchName()+":"+temp.getYear()+":"+temp.getRanking();
						}
					}
					if(canSend==true){
						Map<String, Object> tmp=new HashMap<String, Object>();
						tmp.put("phone", phone);
						tmp.put("request", userarchivekey==-1? "add userarchives" :"edit userarchives");
						tmp.put("userarchiveskey", userarchivekey);
						tmp.put("teamname", teamname);
						tmp.put("inteam", inteam? "1": "0");
						tmp.put("position", position);
						tmp.put("joindate", joindate);
						tmp.put("exitdate", exitdate);
						tmp.put("matchstr", info);
						Runnable r=new ClientWrite(Tools.JsonEncode(tmp));
						new Thread(r).start();
						((HomePageActivity)getActivity()).onBackPressed();
					}
				}
			}
		});
		initiate();
		RealTimeHandler.getInstance().regist(this);
		return view;
	}
	
	
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		System.out.print("FileEditFragment Destroyyyyyyyyyed");
		super.onDestroy();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		RealTimeHandler.getInstance().unRegist(this);
		super.onPause();
	}

	@Override
	public void onChange(Message msg) {
		// TODO Auto-generated method stub
		if(msg.what==HomePageActivity.GET_ARCHIVES){
			((HomePageActivity)getActivity()).onBackPressed();
		}
	}

	public void setPosition(String position){
		if(fieldPosition!=null){
			fieldPosition.setText(position);
		}
	}
	
	private void initiate(){
		teamName.setText(teamname);
		fieldPosition.setText(position);
		joinTime.setText(joindate);
		leaveTime.setText(exitdate);
	}
	
}
