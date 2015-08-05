package com.example.kickfor.team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.example.kickfor.ClientWrite;
import com.example.kickfor.HomePageActivity;
import com.example.kickfor.R;
import com.example.kickfor.SQLHelper;
import com.example.kickfor.Tools;
import com.example.kickfor.utils.IdentificationInterface;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MatchReviewFragment extends Fragment implements OnClickListener, TeamInterface, IdentificationInterface{
	
	private List<MatchReviewEntity> mList=new ArrayList<MatchReviewEntity>();
	private ListView mListView=null;
	private Context context=null;
	private String teamid=null;
	private MatchReviewAdapter adapter=null;
	private TextView matchYear=null;
	private ImageView backOneYear=null;
	private ImageView headOneYear=null;
	private String year=null;
	private int index=0;
	private Bitmap teamImg=null;
	private String teamName=null;
	private ImageView reviewBack=null;
	private TextView addNew=null;
	private String authority=null;
	
	@Override
	public int getFragmentLevel() {
		// TODO Auto-generated method stub
		return IdentificationInterface.SECOND_LEVEL;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		index=0;
		context=getActivity();
		this.year=Tools.getDate().substring(0, 4);
		mList.clear();
		Bundle bundle=getArguments();
		teamid=bundle.getString("teamid");
		authority=bundle.getString("authority");
		getMyTeamImageAndName();
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("request", "get matches");
		map.put("teamid", teamid);
		map.put("year", year);
		map.put("index", String.valueOf(index));
		Runnable r=new ClientWrite(Tools.JsonEncode(map));
		new Thread(r).start();
		View view=inflater.inflate(R.layout.fragment_match_review, container, false);
		mListView=(ListView)view.findViewById(R.id.review_list);
		mListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				setEnable(false);
				if(mList.get(position).getId()!=-1){
					MatchReviewEntity item=mList.get(position);
					String status=item.getStatus();
					if(status.equals("u")){
						Toast.makeText(context, "比赛开始前1小时可以操作", Toast.LENGTH_LONG).show();
						setEnable(true);
					}
					else{
						((HomePageActivity)getActivity()).openVague(HomePageActivity.WAIT_REVIEW_LIST);
						((HomePageActivity)getActivity()).reviewDetail(teamid, item, authority);
					}
				}
				
			}
			
		});
		mListView.setOnItemLongClickListener(new OnItemLongClickListener(){

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub
				final MatchReviewEntity item=mList.get(position);
				final int pos=position;
				if(item.getId()!=-1){
					new AlertDialog.Builder(context)
					.setTitle("确认删除该场比赛记录吗")
					.setPositiveButton("确认删除",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									if(authority.equals("4")){
										Map<String, Object> map=new HashMap<String, Object>();
										map.put("request", "delete review");
										map.put("id", item.getId());
										map.put("teamid", teamid);
										Runnable r=new ClientWrite(Tools.JsonEncode(map));
										new Thread(r).start();
										mList.remove(pos);
										adapter.notifyDataSetChanged();
										
									}
									else{
										Toast.makeText(context, "您的权限不够", Toast.LENGTH_LONG).show();
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
				return true;
			}
			
		});
		matchYear=(TextView)view.findViewById(R.id.review_year);
		backOneYear=(ImageView)view.findViewById(R.id.review_left);
		headOneYear=(ImageView)view.findViewById(R.id.review_right);
		matchYear.setText(year);
		adapter=new MatchReviewAdapter(getActivity(), mList);
		mListView.setAdapter(adapter);
		reviewBack=(ImageView)view.findViewById(R.id.review_back);
		reviewBack.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getActivity().onBackPressed();
			}
			
		});
		addNew=(TextView)view.findViewById(R.id.review_add_new);
		if(Integer.parseInt(authority)<4){
			addNew.setVisibility(View.GONE);
		}
		else{
			addNew.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					setEnable(false);
					((HomePageActivity)getActivity()).openVague(HomePageActivity.WAIT_ADD_NEW);
					setEnable(false);
					Map<String, Object> map=new HashMap<String, Object>();
					map.put("request", "get maxid");
					map.put("teamid", teamid);
					Runnable r=new ClientWrite(Tools.JsonEncode(map));
					new Thread(r).start();
				}
				
			});
		}
		backOneYear.setOnClickListener(this);
		headOneYear.setOnClickListener(this);
		return view;
	}
	
	public void setEnable(boolean enable){
		addNew.setEnabled(enable);
		reviewBack.setEnabled(enable);
		backOneYear.setEnabled(enable);
		headOneYear.setEnabled(enable);
		mListView.setEnabled(enable);
	}


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id=v.getId();
		if(id==R.id.review_left){
			int year=Integer.valueOf(this.year);
			this.year=String.valueOf(year-1);
			index=0;
			matchYear.setText(this.year);
			Map<String, Object> map=new HashMap<String, Object>();
			mList.clear();
			adapter.notifyDataSetChanged();
			map.put("request", "get matches");
			map.put("year", this.year);
			map.put("teamid", teamid);
			map.put("index", "0");
			Runnable r=new ClientWrite(Tools.JsonEncode(map));
			new Thread(r).start();
		}
		else if(id==R.id.review_right){
			int year=Integer.valueOf(this.year);
			this.year=String.valueOf(year+1);
			index=0;
			matchYear.setText(this.year);
			Map<String, Object> map=new HashMap<String, Object>();
			mList.clear();
			adapter.notifyDataSetChanged();
			map.put("request", "get matches");
			map.put("year", this.year);
			map.put("teamid", teamid);
			map.put("index", "0");
			Runnable r=new ClientWrite(Tools.JsonEncode(map));
			new Thread(r).start();
		}
	}
	
	public String getTeamid(){
		return teamid;
	}
	
	public String getYear(){
		return year;
	}
	
	public String getIndex(){
		return String.valueOf(index);
	}
	
	public void setData(Map<String, Object> map){
		System.out.println(mList.size());
		if(!mList.isEmpty() && mList.get(mList.size()-1).getId()==-1){
			System.out.println("确实关了");
			mList.remove(mList.size()-1);
		}
		Iterator<String> iter=map.keySet().iterator();
		Map<String, Object>temp=new HashMap<String, Object>();
		while(iter.hasNext()){
			String key=iter.next();
			temp=Tools.getMapForJson(map.get(key).toString());
			int id=Integer.parseInt(temp.get("id").toString());
			String date=temp.get("date").toString();
			String place=temp.get("place").toString();
			Bitmap againstImg=null;
			String imgPath=temp.get("againstimg").toString();
			if(imgPath.equals("-1")){
				againstImg=BitmapFactory.decodeResource(context.getResources(), R.drawable.team_default);
			}
			else{
				againstImg=BitmapFactory.decodeFile(imgPath);
			}
			String againstName=temp.get("againstname").toString();
			int goals=Integer.parseInt(temp.get("goal").toString());
			int lost=Integer.parseInt(temp.get("lost").toString());
			MatchReviewEntity item=new MatchReviewEntity(id, date, place, Tools.bitmapToString(teamImg), Tools.bitmapToString(againstImg), teamName, againstName, goals, lost);
			item.setStatus(temp.get("status").toString());
			item.setAgainstId(temp.get("againstid").toString());
			mList.add(item);	
		}
		int number=map.keySet().size();
		System.out.println("number====="+number);
		if(number==5){
			index=mList.size();
			System.out.println("aaaaa "+index);
			mList.add(new MatchReviewEntity(-1, null, null, null, null, teamid, this.year, index, -1));
			System.out.println("确实加了");
		}
		if(adapter!=null){
			adapter.notifyDataSetChanged();
			((HomePageActivity)getActivity()).removeVague();
			setEnable(true);
		}
	}
	
	private void getMyTeamImageAndName(){
		SQLHelper helper=SQLHelper.getInstance(context);
		Cursor tmpCursor=helper.select("teams", new String[]{"image", "name"}, "teamid=?", new String[]{teamid}, null);
		if(tmpCursor.moveToNext()){
			String imgpath1=tmpCursor.getString(0);
			if(!imgpath1.equals("-1")){
				teamImg=BitmapFactory.decodeFile(tmpCursor.getString(0));
			}
			else{
				teamImg=BitmapFactory.decodeResource(this.context.getResources(), R.drawable.team_default);
			}
			teamName=tmpCursor.getString(1);
		}
	}
	
	
	
	
	
	

}
