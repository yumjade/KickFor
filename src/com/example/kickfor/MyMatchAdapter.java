package com.example.kickfor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MyMatchAdapter extends BaseAdapter{

	private List<MyMatchEntity> mList=null;
	private LayoutInflater inflater=null;
	
	public MyMatchAdapter(Context context, List<MyMatchEntity> mList){
		inflater=LayoutInflater.from(context);
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
		MyMatchEntity item=mList.get(position);
		ViewHolder viewHolder=null;
		WaitHolder waitHolder=null;
		if(convertView==null){
			if(item.getId()==-1){
				waitHolder=new WaitHolder();
				convertView=inflater.inflate(R.layout.refresh_item, null);
				waitHolder.text=(TextView)convertView.findViewById(R.id.refresh_text);
				waitHolder.click=(ImageView)convertView.findViewById(R.id.refresh_click);
				waitHolder.wait=(ProgressBar)convertView.findViewById(R.id.refresh_wait);
				waitHolder.r=(RelativeLayout)convertView.findViewById(R.id.refresh_layout);
				convertView.setTag(waitHolder);
			}
			else{
				viewHolder=new ViewHolder();
				convertView=inflater.inflate(R.layout.my_match_item, null);
				viewHolder.info=(TextView)convertView.findViewById(R.id.my_match_info);
				viewHolder.score=(TextView)convertView.findViewById(R.id.my_match_score);
				viewHolder.result=(TextView)convertView.findViewById(R.id.my_match_result);
				convertView.setTag(viewHolder);
			}
		}
		else{
			Object obj=convertView.getTag();
			if(obj instanceof ViewHolder){
				viewHolder=(ViewHolder)obj;
			}
			else if(obj instanceof WaitHolder){
				waitHolder=(WaitHolder)obj;
			}
		}
		
		if(item.getId()==-1){
			if(waitHolder==null){
				waitHolder=new WaitHolder();
				convertView=inflater.inflate(R.layout.refresh_item, null);
				waitHolder.text=(TextView)convertView.findViewById(R.id.refresh_text);
				waitHolder.click=(ImageView)convertView.findViewById(R.id.refresh_click);
				waitHolder.wait=(ProgressBar)convertView.findViewById(R.id.refresh_wait);
				waitHolder.r=(RelativeLayout)convertView.findViewById(R.id.refresh_layout);
				convertView.setTag(waitHolder);
			}
			waitHolder.r.setOnClickListener(new MyOnClickListener(waitHolder, item));
		}
		else{
			if(viewHolder==null){
				viewHolder=new ViewHolder();
				convertView=inflater.inflate(R.layout.my_match_item, null);
				viewHolder.info=(TextView)convertView.findViewById(R.id.my_match_info);
				viewHolder.score=(TextView)convertView.findViewById(R.id.my_match_score);
				viewHolder.result=(TextView)convertView.findViewById(R.id.my_match_result);
				convertView.setTag(viewHolder);
			}
			viewHolder.info.setText(item.getDate()+"vs"+item.getAgainstName());
			viewHolder.score.setText(item.getScore());
			viewHolder.result.setText(item.getGoal()+" "+item.getAssist());
		}
		return convertView;
	}
	
	static class ViewHolder{
		TextView info=null;
		TextView score=null;
		TextView result=null;
	}
	
	static class WaitHolder{
		TextView text=null;
		ImageView click=null;
		ProgressBar wait=null;
		RelativeLayout r=null;
		boolean isClick=false;
	}
	
	class MyOnClickListener implements OnClickListener{

		private WaitHolder waitHolder=null;
		private MyMatchEntity entity=null;
		
		public MyOnClickListener(WaitHolder waitHolder, MyMatchEntity entity){
			this.waitHolder=waitHolder;
			this.entity=entity;
		}
		
		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			if(waitHolder.isClick==false){
				waitHolder.isClick=true;
				waitHolder.text.setText("正在加载");
				waitHolder.wait.setVisibility(View.VISIBLE);
				waitHolder.click.setVisibility(View.GONE);
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("request", "get ones match");
				map.put("phone", entity.getDate());
				map.put("teamid", entity.getTeamId());
				map.put("index", entity.getGoals());
				System.out.println(entity.getGoal()+"::"+entity.getTeamId()+"::+"+entity.getGoals());
				Runnable r=new ClientWrite(Tools.JsonEncode(map));
				new Thread(r).start();
			}
			else{
				waitHolder.isClick=false;
				waitHolder.text.setText("加载更多");
				waitHolder.wait.setVisibility(View.GONE);
				waitHolder.click.setVisibility(View.VISIBLE);
			}
		}
		
	}
	
	
	
}
