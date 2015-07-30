package com.example.kickfor.team;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.kickfor.ClientWrite;
import com.example.kickfor.R;
import com.example.kickfor.Tools;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MatchReviewAdapter extends BaseAdapter{

	private LayoutInflater mInflater=null;
	private List<MatchReviewEntity> mList=null;
	
	public MatchReviewAdapter(Context context, List<MatchReviewEntity> mList){
		mInflater=LayoutInflater.from(context);
		this.mList=mList;
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder=null;
		WaitHolder waitHolder=null;
		if(convertView==null){
			if(mList.get(position).getId()==-1){
				waitHolder=new WaitHolder();
				convertView=mInflater.inflate(R.layout.refresh_item, null);
				waitHolder.r=(RelativeLayout)convertView.findViewById(R.id.refresh_layout);
				waitHolder.text=(TextView)convertView.findViewById(R.id.refresh_text);
				waitHolder.add=(ImageView)convertView.findViewById(R.id.refresh_click);
				waitHolder.pb=(ProgressBar)convertView.findViewById(R.id.refresh_wait);
				waitHolder.pb.setVisibility(View.GONE);
				convertView.setTag(waitHolder);
			}
			else{
				viewHolder=new ViewHolder();
				convertView=mInflater.inflate(R.layout.match_review_item, null);
				viewHolder.dateAndPlace=(TextView)convertView.findViewById(R.id.match_date_place);
				viewHolder.teamImg=(ImageView)convertView.findViewById(R.id.iv_my_team);
				viewHolder.againstImg=(ImageView)convertView.findViewById(R.id.iv_against_team);
				viewHolder.teamName=(TextView)convertView.findViewById(R.id.tv_my_team_name);
				viewHolder.againstName=(TextView)convertView.findViewById(R.id.tv_against_team_name);
				viewHolder.score=(TextView)convertView.findViewById(R.id.match_result);
				viewHolder.status =(TextView) convertView.findViewById(R.id.tv_review_status);
				convertView.setTag(viewHolder);
			}
		}
		else{
			Object tmp=convertView.getTag();
			if(tmp instanceof ViewHolder){
				viewHolder=(ViewHolder)tmp;
			}
			else{
				waitHolder=(WaitHolder)tmp;
			}
		}
		
		if(mList.get(position).getId()!=-1){
			MatchReviewEntity item=mList.get(position);
			if(viewHolder==null){
				viewHolder=new ViewHolder();
				convertView=mInflater.inflate(R.layout.match_review_item, null);
				viewHolder.dateAndPlace=(TextView)convertView.findViewById(R.id.match_date_place);
				viewHolder.teamImg=(ImageView)convertView.findViewById(R.id.iv_my_team);
				viewHolder.againstImg=(ImageView)convertView.findViewById(R.id.iv_against_team);
				viewHolder.teamName=(TextView)convertView.findViewById(R.id.tv_my_team_name);
				viewHolder.againstName=(TextView)convertView.findViewById(R.id.tv_against_team_name);
				viewHolder.score=(TextView)convertView.findViewById(R.id.match_result);
				viewHolder.status = (TextView) convertView.findViewById(R.id.tv_review_status);
				convertView.setTag(viewHolder);
			}
			viewHolder.dateAndPlace.setText(item.getDateAndPlace());
			viewHolder.teamImg.setImageBitmap(item.getTeamImg());
			viewHolder.againstImg.setImageBitmap(item.getAgainstImg());
			viewHolder.teamName.setText(item.getTeamName());
			viewHolder.againstName.setText(item.getAgainstName());
			viewHolder.score.setText(item.getSocre());
			viewHolder.status.setText(item.getReviewStatus());
			if(item.getSocre().equals("u")){
				viewHolder.score.setText(" - ");
				viewHolder.status.setText("比赛未开始");
				viewHolder.status.setTextColor(Color.parseColor("#ff3300"));
			}
			else if(item.getSocre().equals("p")){
				viewHolder.status.setText("比赛尚未编辑");
				viewHolder.status.setTextColor(Color.parseColor("#ff3300"));
			}
			else if(item.getSocre().equals("k")){
				viewHolder.score.setText(item.getSocre());
			}
		}
		else{
			MatchReviewEntity item=mList.get(position);
			if(waitHolder==null){
				waitHolder=new WaitHolder();
				convertView=mInflater.inflate(R.layout.refresh_item, null);
				waitHolder.r=(RelativeLayout)convertView.findViewById(R.id.refresh_layout);
				waitHolder.text=(TextView)convertView.findViewById(R.id.refresh_text);
				waitHolder.add=(ImageView)convertView.findViewById(R.id.refresh_click);
				waitHolder.pb=(ProgressBar)convertView.findViewById(R.id.refresh_wait);
				waitHolder.pb.setVisibility(View.GONE);
				convertView.setTag(waitHolder);
			}
			waitHolder.r.setOnClickListener(new MyOnClickListener(waitHolder, item));
		}
		return convertView;
	}
	
	
	
	static class ViewHolder{
		TextView dateAndPlace=null;
		ImageView teamImg=null;
		ImageView againstImg=null;
		TextView score=null;
		TextView teamName=null;
		TextView againstName=null;
		TextView status = null;
	}
		
	static class WaitHolder{
		TextView text=null;
		RelativeLayout r=null;
		ImageView add=null;
		ProgressBar pb=null;
		boolean isClick=false;
	}
	
	
	class MyOnClickListener implements OnClickListener{

		private WaitHolder waitHolder=null;
		private MatchReviewEntity entity=null;
		
		public MyOnClickListener(WaitHolder waitHolder, MatchReviewEntity entity){
			this.waitHolder=waitHolder;
			this.entity=entity;
		}
		
		@Override
		public void onClick(View arg0) {
			if(waitHolder.isClick==false){
				waitHolder.isClick=true;
				waitHolder.text.setText("正在加载");
				waitHolder.pb.setVisibility(View.VISIBLE);
				waitHolder.add.setVisibility(View.GONE);
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("request", "get matches");
				map.put("year", entity.getAgainstName());
				map.put("teamid", entity.getTeamName());
				map.put("index", entity.getGoals());
				Runnable r=new ClientWrite(Tools.JsonEncode(map));
				new Thread(r).start();
			}
			else{
				waitHolder.isClick=false;
				waitHolder.text.setText("加载更多");
				waitHolder.pb.setVisibility(View.GONE);
				waitHolder.add.setVisibility(View.VISIBLE);
			}
		}
		
	}
	

}
