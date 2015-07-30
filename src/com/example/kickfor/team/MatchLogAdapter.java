package com.example.kickfor.team;

import java.util.List;

import com.easemob.chat.core.v;
import com.example.kickfor.R;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MatchLogAdapter extends BaseAdapter{

	private List<MatchLogEntity> mList=null;;
	
	private LayoutInflater mInflater=null;
	public MatchLogAdapter(Context context, List<MatchLogEntity> list){
		mInflater=LayoutInflater.from(context);
		this.mList=list;
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
		MatchLogEntity item=mList.get(position);
		if(convertView==null){
			viewHolder=new ViewHolder();
			convertView=mInflater.inflate(R.layout.match_log_item, null);
			viewHolder.type=(TextView)convertView.findViewById(R.id.match_type);
			viewHolder.number=(TextView)convertView.findViewById(R.id.match_number);
			viewHolder.result=(TextView)convertView.findViewById(R.id.match_result);
			viewHolder.totalGoal=(TextView)convertView.findViewById(R.id.match_total_goal);
			viewHolder.totalLost=(TextView)convertView.findViewById(R.id.match_total_lost);
			viewHolder.b=(LinearLayout)convertView.findViewById(R.id.match_log_b);
			viewHolder.margin=(TextView)convertView.findViewById(R.id.match_log_margin);
			convertView.setTag(viewHolder);
		}
		else{
			viewHolder=(ViewHolder)convertView.getTag();
		}
		viewHolder.type.setText(item.getMatchType());
		viewHolder.number.setText(item.getMatchNumber());
		viewHolder.result.setText(item.getMatchResult());
		viewHolder.totalGoal.setText(item.getMatchTotalGoal());
		viewHolder.totalLost.setText(item.getMatchTotalLost());
		if(position%2==1){
			
			viewHolder.b.setBackgroundColor(Color.parseColor("#ffffff"));
		}
		else if(position%2==0){
			viewHolder.b.setBackgroundColor(Color.parseColor("#ffffff"));
		}
//		viewHolder.margin.setVisibility(View.GONE);
		if(position==(getCount()-1))
		{
//			viewHolder.margin.setVisibility(View.VISIBLE);
//			viewHolder.b.setBackgroundColor(Color.parseColor("#ebf4ff"));
		}
		return convertView;
	}
	
	static class ViewHolder{
		TextView type=null;
		TextView number=null;
		TextView result=null;
		TextView totalGoal=null;
		TextView totalLost=null;
		LinearLayout b=null;
		TextView margin=null;
	}
	
	

}
