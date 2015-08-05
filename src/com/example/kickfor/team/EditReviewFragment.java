package com.example.kickfor.team;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;






import java.util.Map;

import com.example.kickfor.ClientWrite;
import com.example.kickfor.HomePageActivity;
import com.example.kickfor.R;
import com.example.kickfor.SQLHelper;
import com.example.kickfor.Tools;
import com.example.kickfor.more.SearchItemAdapter;
import com.example.kickfor.more.SearchItemEntity;
import com.example.kickfor.utils.IdentificationInterface;
import com.example.kickfor.wheelview.ScreenInfo;
import com.example.kickfor.wheelview.WheelDate;
import com.example.kickfor.wheelview.WheelScore;
import com.example.kickfor.wheelview.WheelTime;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class EditReviewFragment extends Fragment implements TeamInterface, IdentificationInterface{
	
	final String[]items ={"联赛", "杯赛", "热身赛"};
	final String[]items1 ={"5人制", "7人制", "8人制", "9人制", "11人制", "其他"};
	
	private ImageView myTeamImage=null;
	private ImageView againstTeamImage=null;
	private EditText againstNameEdit=null;
	private EditText place=null;
	private TextView time=null;
	private TextView date=null;
	private TextView typeEdit=null;
	private TextView myTeamName=null;
	private TextView againstTeamName=null;
	private TextView type=null;
	private TextView score=null;
	private ListView teammateList=null;
	private List<Info> teammate=new ArrayList<Info>();
	private TextView ensure=null;
	private List<SearchItemEntity> mList=new ArrayList<SearchItemEntity>();
	private ListView mListView=null;
	private TextView listShow=null;
	private int goals=-1;
	private int lost=-1;
	private int year=0;
	private int month=0;
	private int day=0;
	private int hour=0;
	private int minute=0;
	private EditReviewAdapter adapter=null;
	private SearchItemAdapter adapter1=null;
	private Context context=null;
	private String teamid=null;
	private MatchReviewEntity entity=null;
	private String againstid="";
	private Bitmap myImage=null;
	private Bitmap againstImg=null;
	private int id=0;
	private WheelScore wheelScore=null;
	private WheelDate wheelDate=null;
	private WheelTime wheelTime=null;
	private TextView cancel=null;
	private ImageView back=null;
	private TextView person=null;
	
	@Override
	public int getFragmentLevel() {
		// TODO Auto-generated method stub
		return IdentificationInterface.SECOND_LEVEL;
	}
	
	
	private void init(){
		context=getActivity();
		Bundle bundle=getArguments();
		this.teamid=bundle.getString("teamid");
		if(bundle.containsKey("maxid")){
			this.id=bundle.getInt("maxid");
		}
		if(bundle.containsKey("entity")){
			this.entity=(MatchReviewEntity)bundle.getSerializable("entity");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		((HomePageActivity)getActivity()).removeVague();
		init();
		View view=inflater.inflate(R.layout.fragment_edit_review, container, false);
		person=(TextView)view.findViewById(R.id.tv_play_format);
		person.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("赛制");
				ListAdapter catalogsAdapter = new ArrayAdapter<String>(context, R.layout.match_type_item, items1);
				builder.setAdapter(catalogsAdapter, new DialogInterface.OnClickListener(){
				    public void onClick(DialogInterface arg0, int arg1) {
				        person.setText(items1[arg1]);
				    }
				});
				builder.show();
			}
			
		});
		myTeamImage=(ImageView)view.findViewById(R.id.iv_editre_my_team);
		againstTeamImage=(ImageView)view.findViewById(R.id.iv_editre_against_team);
		myTeamName=(TextView)view.findViewById(R.id.tv_editre_my_team_name);
		againstTeamName=(TextView)view.findViewById(R.id.tv_editre_against_team_name);
		type=(TextView)view.findViewById(R.id.editre_match_type);
		score=(TextView)view.findViewById(R.id.editre_match_result);
		listShow=(TextView)view.findViewById(R.id.editre_show_list);
		againstNameEdit=(EditText)view.findViewById(R.id.editre_against_name);
		cancel=(TextView)view.findViewById(R.id.editre_cancel);
		back=(ImageView)view.findViewById(R.id.editre_back);
		place=(EditText)view.findViewById(R.id.editre_place);
		time=(TextView)view.findViewById(R.id.editre_time);
		date=(TextView)view.findViewById(R.id.editre_date);
		typeEdit=(TextView)view.findViewById(R.id.editre_type);
		mListView=(ListView)view.findViewById(R.id.editre_team_list);
		teammateList=(ListView)view.findViewById(R.id.editre_list);
		ensure=(TextView)view.findViewById(R.id.editre_submit);
		adapter=new EditReviewAdapter(context, teammate, teammateList);
		teammateList.setAdapter(adapter);
		teammateList.setTag(true);
		adapter1=new SearchItemAdapter(context, mList);
		mListView.setAdapter(adapter1);
		
		
		listShow.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if((boolean)teammateList.getTag()==false){
					teammateList.setVisibility(View.VISIBLE);
					teammateList.setTag(true);
					Drawable drawable= getResources().getDrawable(R.drawable.more_up);
					listShow.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
				}
				else{
					teammateList.setVisibility(View.GONE);
					teammateList.setTag(false);
					Drawable drawable= getResources().getDrawable(R.drawable.more_down);
					listShow.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
				}
			}
			
		});
		
		
		back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((HomePageActivity)getActivity()).onBackPressed();
			}
			
		});
		
		againstNameEdit.addTextChangedListener(new TextWatcher(){

			@Override
			public void afterTextChanged(Editable arg0) {
				// TODO Auto-generated method stub
				Map<String, Object> tmp=new HashMap<String, Object>();
				tmp.put("request", "seek team");
				tmp.put("info", againstNameEdit.getText().toString());
				Runnable r=new ClientWrite(Tools.JsonEncode(tmp));
				new Thread(r).start();
				againstTeamName.setText(arg0);
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				
			}
			
		});
		
		cancel.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				((HomePageActivity)getActivity()).onBackPressed();
			}
			
		});
		
		
		againstNameEdit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mListView.setVisibility(View.GONE);
			}
			
		});
		place.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mListView.setVisibility(View.GONE);
			}
			
		});
		date.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mListView.setVisibility(View.GONE);
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
								date.setText(wheelDate.getDate());
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
		time.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mListView.setVisibility(View.GONE);
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
								time.setText(wheelTime.getTime());
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
		
		
		
		score.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				mListView.setVisibility(View.GONE);
				LayoutInflater inflater=LayoutInflater.from(context);
				final View scorepickerview=inflater.inflate(R.layout.scorepicker, null);
				wheelScore=new WheelScore(scorepickerview);
				ScreenInfo screenInfo=new ScreenInfo(getActivity());
				wheelScore.screenheight=screenInfo.getHeight();
				wheelScore.initScorePicker(goals==-1? 0: goals, lost==-1? 0:lost);
				new AlertDialog.Builder(context)
				.setTitle("选择比分")
				.setView(scorepickerview)
				.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								score.setText(wheelScore.getScore());
								goals=wheelScore.getGoals();
								lost=wheelScore.getLost();
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
		
		
		typeEdit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mListView.setVisibility(View.GONE);
				AlertDialog.Builder builder = new AlertDialog.Builder(context);
				builder.setTitle("比赛类型");
				ListAdapter catalogsAdapter = new ArrayAdapter<String>(context, R.layout.match_type_item, items);
				builder.setAdapter(catalogsAdapter, new DialogInterface.OnClickListener(){
				    public void onClick(DialogInterface arg0, int arg1) {
				        typeEdit.setText(items[arg1]);
				        type.setText(items[arg1]);
				    }
				});
				builder.show();
			}
			
		});
		
		
		initiateList();
		initiate();
		
		ensure.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(!Tools.compareDateAndTime(date.getText().toString(), time.getText().toString())){
					Toast.makeText(context, "本场比赛尚未开始，不支持上传数据", Toast.LENGTH_LONG).show();
				}
				else if(againstTeamName.getText().toString().isEmpty()){
					Toast.makeText(context, "对手队名不能为空", Toast.LENGTH_LONG).show();
				}
				else if(score.getText().toString().isEmpty()){
					Toast.makeText(context, "比分不能为空", Toast.LENGTH_LONG).show();
				}
				else if(goals==-1 || lost==-1){
					Toast.makeText(context, "您未上传比分", Toast.LENGTH_LONG).show();
				}
				else if(type.getText().toString().isEmpty()){
					Toast.makeText(context, "您未编辑比赛性质", Toast.LENGTH_LONG).show();
				}
				else if(person.getText().toString().isEmpty()){
					Toast.makeText(context, "您未编辑赛制", Toast.LENGTH_LONG).show();
				}
				else{
					setEnable(false);
					((HomePageActivity)getActivity()).openVague(HomePageActivity.WAIT_UPLOAD_MATCH);
					Iterator<Info> iter=teammate.iterator();
					int index=1;
					Map<String, Object> map=new HashMap<String, Object>();
					int assists=0;
					while(iter.hasNext()){
						Info tmp=iter.next();
						String str=Tools.MapToJson(tmp.getInfo());
						assists=assists+Integer.parseInt(tmp.getAssist());
						map.put(String.valueOf(index), str);
						index=index+1;
					}
					map.put("member", String.valueOf(index-1));
					map.put("request", "update review match");
					map.put("againstid", againstid);
					map.put("againstname", againstNameEdit.getText().toString());
					map.put("goals", String.valueOf(goals));
					map.put("assists", String.valueOf(assists));
					map.put("lost", String.valueOf(lost));
					map.put("place", place.getText().toString());
					map.put("time", time.getText().toString());
					map.put("type", typeEdit.getText().toString());
					map.put("date", date.getText().toString());
					map.put("teamid", teamid);
					map.put("id", id);
					map.put("person", person.getText().toString());
					Runnable r=new ClientWrite(Tools.JsonEncode(map));
					new Thread(r).start();
					((HomePageActivity)getActivity()).onBackPressed();
				}
			}
			
		});
		return view;
	}
	
	private void initiateList(){
		teammate.clear();
		SQLHelper helper=SQLHelper.getInstance(context);
		String tableName="f_"+teamid;
		helper.createTeamMateTable(tableName);
		if(entity==null){
			Cursor cursor=helper.select(tableName, new String[]{"phone", "name", "number"}, null, null, "name");
			while(cursor.moveToNext()){
				String phone=cursor.getString(0);
				String name=cursor.getString(1);
				String number=cursor.getString(2);
				Info item=new Info(phone, name, number);
				teammate.add(item);
			}
		}
		else{
			List<Map<String, Object>> listInfo=entity.getMembersInfo();
			Iterator<Map<String, Object>> iter=listInfo.iterator();
			while(iter.hasNext()){
				Map<String, Object> map=iter.next();
				String phone=map.get("phone").toString();
				String attendance=map.get("attendance").toString();
				String goal=map.get("goal").toString();
				String assist=map.get("assist").toString();
				String redCard=map.get("red").toString();
				String yellowCard=map.get("yellow").toString();
				String name=map.get("name").toString();
				String number=map.get("number").toString();
				Info item=new Info(phone, name, number);
				item.setInfo(goal, assist, redCard, yellowCard, attendance);
				teammate.add(item);
			}
		}
		adapter.notifyDataSetChanged();
		int height=Tools.setListViewHeight(teammateList);
		adapter.setCurrentHeight(height);
	}
	
	private void initiate(){
		if(entity!=null){
			againstid=entity.getAgainstid();
			myTeamImage.setImageBitmap(entity.getTeamImg());
			againstTeamImage.setImageBitmap(entity.getAgainstImg());
			myTeamName.setText(entity.getTeamName());
			againstTeamName.setText(entity.getAgainstName());
			againstNameEdit.setText(entity.getAgainstName());
			goals=entity.getGoals();
			lost=entity.getLost();
			score.setText(entity.getSocre());
			place.setText(entity.getPlace());
			time.setText(entity.getTime());
			date.setText(entity.getDate());
			type.setText(entity.getType());
			person.setText(entity.getPerson());
			typeEdit.setText(entity.getType());
			id=entity.getId();
			List<String> dateList=Tools.explode(entity.getDate(), "-");
			year=Integer.parseInt(dateList.get(0));
			month=Integer.parseInt(dateList.get(1));
			day=Integer.parseInt(dateList.get(2));
			List<String> timeList=Tools.explode(entity.getTime(), ":");
			hour=Integer.parseInt(timeList.get(0));
			minute=Integer.parseInt(timeList.get(1));
		}
		else{
			System.out.println("原来没有");
			SQLHelper helper=SQLHelper.getInstance(context);
			Cursor cursor=helper.select("teams", new String[]{"name", "image"}, "teamid=?", new String[]{teamid}, null);
			cursor.moveToNext();
			String imgpath=cursor.getString(1);
			if(!imgpath.equals("-1")){
				myImage=BitmapFactory.decodeFile(imgpath);
				myTeamImage.setImageBitmap(myImage);
			}
			myTeamName.setText(cursor.getString(0));
			Calendar calendar=Calendar.getInstance();
			year=calendar.get(Calendar.YEAR);
			month=calendar.get(Calendar.MONTH);
			day=calendar.get(Calendar.DAY_OF_MONTH);
			hour=calendar.get(Calendar.HOUR_OF_DAY);
			minute=calendar.get(Calendar.MINUTE);
		}
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
				SearchItemEntity entity1=mList.get(arg2);
				String againstid=entity1.getTeamid();
				String againstName=entity1.getName();
				Bitmap againstImage=entity1.getImage();
				if(entity==null){
					entity=new MatchReviewEntity(id, "", "", Tools.bitmapToString(myImage), Tools.bitmapToString(againstImage), myTeamName.getText().toString(), againstName, 0, 0);
					entity.setAgainstId(againstid);
				}
				else{
					entity.setAgainstId(againstid);
					entity.setAgainstImg(Tools.bitmapToString(againstImage));
					entity.setAgainstName(againstName);
				}
				againstTeamName.setText(againstName);
				againstTeamImage.setImageBitmap(againstImage);
				mListView.setVisibility(View.GONE);
			}
			
		});
		adapter1.notifyDataSetChanged();
		Tools.setListViewHeight(mListView);
	}
	
	
	public void setEnable(boolean enable){
		listShow.setEnabled(enable);
		againstNameEdit.setEnabled(enable);
		place.setEnabled(enable);
		date.setEnabled(enable);
		time.setEnabled(enable);
		score.setEnabled(enable);
		typeEdit.setEnabled(enable);
		ensure.setEnabled(enable);
		person.setEnabled(enable);
		mListView.setEnabled(enable);
		mListView.setEnabled(enable);
		back.setEnabled(enable);
		cancel.setEnabled(enable);
		if(enable==false){
			teammateList.setVisibility(View.GONE);
			teammateList.setTag(false);
			Drawable drawable= getResources().getDrawable(R.drawable.more_down);
			listShow.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
		}
		else{
			teammateList.setVisibility(View.VISIBLE);
			teammateList.setTag(true);
			Drawable drawable= getResources().getDrawable(R.drawable.more_up);
			listShow.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
		}
	}

	@Override
	public String getTeamid() {
		// TODO Auto-generated method stub
		return teamid;
	}
	

	
}
