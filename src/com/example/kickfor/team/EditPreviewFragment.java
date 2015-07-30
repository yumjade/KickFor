package com.example.kickfor.team;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.example.kickfor.HomePageActivity;
import com.example.kickfor.ClientWrite;
import com.example.kickfor.R;
import com.example.kickfor.Tools;
import com.example.kickfor.more.SearchItemAdapter;
import com.example.kickfor.more.SearchItemEntity;
import com.example.kickfor.utils.IdentificationInterface;
import com.example.kickfor.wheelview.ScreenInfo;
import com.example.kickfor.wheelview.WheelDate;
import com.example.kickfor.wheelview.WheelTime;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class EditPreviewFragment extends Fragment implements TeamInterface, IdentificationInterface{
	
	final String[]items ={"联赛", "杯赛", "热身赛"};
	final String[]items1 ={"5人制", "7人制", "8人制", "9人制", "11人制", "其他"};
	
	private EditText nameText=null;
	private EditText placeText=null;
	private TextView dateText=null;
	private TextView timeText=null;
	private TextView ensure=null;
	private TextView cancel=null;
	private TextView delete=null;
	private ListView mListView=null;
	private TextView typeText=null;
	private TextView personText=null;
	
	private int id=-1;
	private String teamid=null;
	private String againstid=null;
	private String name=null;
	private String place=null;
	private String date=null;
	private String time=null;
	private String type=null;
	private String person=null;
	private Bitmap againstimg=null;
	
	private ImageView back=null;
	
	private WheelTime wheelTime=null;
	private int minute=0;
	private int hour=0;
		
	private WheelDate wheelDate=null;
	private int year=0;
	private int month=0;
	private int day=0;
	
	private Context context=null;
	private SearchItemAdapter adapter=null;
	private List<SearchItemEntity> mList=null;
	
	@Override
	public int getFragmentLevel() {
		// TODO Auto-generated method stub
		return IdentificationInterface.SECOND_LEVEL;
	}
	
	private void init(){
		Bundle bundle=getArguments();
		this.context=getActivity();
		this.id=bundle.getInt("id");
		this.teamid=bundle.getString("teamid");
		this.againstid=bundle.getString("againstid");
		this.name=bundle.getString("againstname");
		this.place=bundle.getString("place");
		this.date=bundle.getString("date");
		this.time=bundle.getString("time");
		if(bundle.containsKey("type")){
			this.type=bundle.getString("type");
		}
		if(bundle.containsKey("person")){
			this.person=bundle.getString("person");
		}
		mList=new ArrayList<SearchItemEntity>();
	}

	public void setEnable(boolean enable){
		delete.setEnabled(enable);
		nameText.setEnabled(enable);
		placeText.setEnabled(enable);
		dateText.setEnabled(enable);
		timeText.setEnabled(enable);
		ensure.setEnabled(enable);
		cancel.setEnabled(enable);
		mListView.setEnabled(enable);
		back.setEnabled(enable);
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		init();
		View view=inflater.inflate(R.layout.fragment_edit_prematch, container, false);
		typeText=(TextView)view.findViewById(R.id.match_character_2);
		typeText.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("比赛类型");
				ListAdapter catalogsAdapter = new ArrayAdapter<String>(context, R.layout.match_type_item, items);
				builder.setAdapter(catalogsAdapter, new DialogInterface.OnClickListener(){
				    public void onClick(DialogInterface arg0, int arg1) {
				        typeText.setText(items[arg1]);
				    }
				});
				builder.show();
				
			}
		});
		personText=(TextView)view.findViewById(R.id.match_format_2);
		personText.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("赛制");
				ListAdapter catalogsAdapter = new ArrayAdapter<String>(context, R.layout.match_type_item, items1);
				builder.setAdapter(catalogsAdapter, new DialogInterface.OnClickListener(){
				    public void onClick(DialogInterface arg0, int arg1) {
				        personText.setText(items1[arg1]);
				    }
				});
				builder.show();
			}
			
		});
		delete=(TextView)view.findViewById(R.id.edit_pre_delete);
		if(id==-1){
			delete.setVisibility(View.GONE);
		}
		else{
			delete.setVisibility(View.VISIBLE);
		}
		delete.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("request", "delete preview");
				map.put("id", id);
				map.put("teamid", teamid);
				Runnable r=new ClientWrite(Tools.JsonEncode(map));
				new Thread(r).start();
				setEnable(false);
				((HomePageActivity)getActivity()).openVague(HomePageActivity.WAIT_DELETE_PREVIEW);
			}
			
		});
		back=(ImageView)view.findViewById(R.id.edit_pre_back);
		back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				((HomePageActivity)getActivity()).onBackPressed();
			}
			
		});
		nameText=(EditText)view.findViewById(R.id.edit_pre_against_name);
		placeText=(EditText)view.findViewById(R.id.edit_pre_place);
		dateText=(TextView)view.findViewById(R.id.edit_pre_date);
		timeText=(TextView)view.findViewById(R.id.edit_pre_time);
		
		dateText.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LayoutInflater inflater=LayoutInflater.from(context);
				final View datepickerview=inflater.inflate(R.layout.datepicker, null);
				wheelDate=new WheelDate(datepickerview);
				ScreenInfo screenInfo=new ScreenInfo(getActivity());
				wheelDate.screenheight=screenInfo.getHeight();
				wheelDate.initDatePicker(year, month, day);
				new AlertDialog.Builder(context)
				.setTitle("选择日期")
				.setView(datepickerview)
				.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dateText.setText(wheelDate.getDate());
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
		
		timeText.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LayoutInflater inflater=LayoutInflater.from(context);
				final View timepickerview=inflater.inflate(R.layout.timepicker, null);
				wheelTime=new WheelTime(timepickerview);
				ScreenInfo screenInfo=new ScreenInfo(getActivity());
				wheelTime.screenheight=screenInfo.getHeight();
				wheelTime.initTimePicker(hour, minute);
				new AlertDialog.Builder(context)
				.setTitle("选择时间")
				.setView(timepickerview)
				.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								timeText.setText(wheelTime.getTime());
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
		
		ensure=(TextView)view.findViewById(R.id.edit_pre_ensure);
		cancel=(TextView)view.findViewById(R.id.edit_pre_cancel);
		mListView=(ListView)view.findViewById(R.id.edit_pre_team);
		mListView.setVisibility(View.GONE);
		
		
		
		initiate();
		adapter=new SearchItemAdapter(context, mList);
		mListView.setAdapter(adapter);
		ensure.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				name=nameText.getText().toString();
				place=placeText.getText().toString();
				date=dateText.getText().toString();
				time=timeText.getText().toString();
				type=typeText.getText().toString();
				person=personText.getText().toString();
				if(name.isEmpty()){
					Toast.makeText(context, "对手姓名不能为空", Toast.LENGTH_SHORT).show();
				}
				else if(place.isEmpty()){
					Toast.makeText(context, "场地不能为空", Toast.LENGTH_SHORT).show();
				}
				else if(date.isEmpty()){
					Toast.makeText(context, "比赛日期不能为空", Toast.LENGTH_SHORT).show();
				}
				else if(time.isEmpty()){
					Toast.makeText(context, "比赛时间不能为空", Toast.LENGTH_SHORT).show();
				}
				else if(Tools.compareDateAndTime(date, time)){
					Toast.makeText(context, "您无法穿越踢比赛", Toast.LENGTH_SHORT).show();
					dateText.setText("");
					timeText.setText("");
				}
				else if(type.isEmpty()){
					Toast.makeText(context, "比赛性质不能为空", Toast.LENGTH_SHORT).show();
				}
				else if(person.isEmpty()){
					Toast.makeText(context, "赛制不能为空", Toast.LENGTH_SHORT).show();
				}
				else{
					setEnable(false);
					Map<String, Object> tmp=new HashMap<String, Object>();
					tmp.put("againstid", againstid);
					tmp.put("againstname", name);
					tmp.put("place", place);
					tmp.put("date", date);
					tmp.put("time", time);
					tmp.put("status", "u");
					tmp.put("type", type);
					tmp.put("person", person);
					if(id==-1){
						tmp.put("id", id);
					}
					else{
						tmp.put("id", id);
					}
					tmp.put("info", "");
					tmp.put("teamid", teamid);
					tmp.put("request", "update match");
					Runnable r=new ClientWrite(Tools.JsonEncode(tmp));
					new Thread(r).start();
					//((HomePageActivity)getActivity()).onBackPressed();
				}
			}
			
		});
		cancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((HomePageActivity)getActivity()).onBackPressed();
			}
			
		});
		return view;
	}
	
	private void initiate(){
		if(!date.isEmpty()){
			List<String> dateList=Tools.explode(date, "-");
			year=Integer.parseInt(dateList.get(0));
			month=Integer.parseInt(dateList.get(1));
			day=Integer.parseInt(dateList.get(2));
		}
		else{
			year=Calendar.getInstance().get(Calendar.YEAR);
			month=Calendar.getInstance().get(Calendar.MONTH);
			day=Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		}
		if(!time.isEmpty()){
			List<String> timeList=Tools.explode(time, ":");
			hour=Integer.parseInt(timeList.get(0));
			minute=Integer.parseInt(timeList.get(1));
		}
		else{
			hour=Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
			minute=Calendar.getInstance().get(Calendar.MINUTE);
		}
		nameText.setText(name);
		placeText.setText(place);
		dateText.setText(date);
		timeText.setText(time);
		typeText.setText(type);
		personText.setText(person);
		nameText.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				Map<String, Object> tmp=new HashMap<String, Object>();
				tmp.put("request", "seek team");
				tmp.put("info", nameText.getText().toString());
				againstid="";
				Runnable r=new ClientWrite(Tools.JsonEncode(tmp));
				new Thread(r).start();
			}

			@Override
			public void beforeTextChanged(CharSequence arg0, int arg1,
					int arg2, int arg3) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				
			}
			
		});
	}
	
	public void changedData(List<SearchItemEntity> list){
		mList.clear();
		Iterator<SearchItemEntity> iter=list.iterator();
		while(iter.hasNext()){
			SearchItemEntity item=iter.next();
			mList.add(item);
		}
		mListView.setVisibility(View.VISIBLE);
		mListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				SearchItemEntity entity=mList.get(arg2);
				againstid=entity.getTeamid();
				againstimg=entity.getImage();
				mListView.setVisibility(View.GONE);
			}
			
		});
		adapter.notifyDataSetChanged();
	}
	
	@Override
	public String getTeamid(){
		return teamid;
	}
	
	

}
