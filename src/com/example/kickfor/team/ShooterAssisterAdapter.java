package com.example.kickfor.team;

import java.util.List;

import com.example.kickfor.R;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ShooterAssisterAdapter extends BaseAdapter{

	private List<ShooterAssister> mList=null;
	private LayoutInflater mInflater=null;
	
	public ShooterAssisterAdapter(Context context, List<ShooterAssister> mList){
		mInflater=LayoutInflater.from(context);
		this.mList=mList;	
	}
	
	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder=null;
		if(convertView==null){
			viewHolder=new ViewHolder();
			convertView=mInflater.inflate(R.layout.shooter_assister_log, null);
			viewHolder.rankAndName=(TextView)convertView.findViewById(R.id.rank);
			viewHolder.totalGoalOrAssist=(TextView)convertView.findViewById(R.id.goalOrAssist);
			viewHolder.totalMatch=(TextView)convertView.findViewById(R.id.tv_person_match_number);
			viewHolder.totalEfficiency=(TextView)convertView.findViewById(R.id.efficiency);
			viewHolder.b=(LinearLayout)convertView.findViewById(R.id.shooter_assister_b);
			viewHolder.line=(View)convertView.findViewById(R.id.shooter_assister_line);
			convertView.setTag(viewHolder);
		}
		else{
			viewHolder=(ViewHolder)convertView.getTag();
		}
		if(position%2==1){
			viewHolder.b.setBackgroundColor(Color.parseColor("#ffffff"));
			viewHolder.line.setVisibility(View.GONE);
		}
		else{
			viewHolder.b.setBackgroundColor(Color.parseColor("#f5f5f5"));
			viewHolder.line.setVisibility(View.GONE);
		}
		if(position==0){
			viewHolder.b.setBackgroundColor(Color.parseColor("#fafafa"));
			viewHolder.line.setVisibility(View.VISIBLE);
			viewHolder.rankAndName.setTextColor(Color.parseColor("#6e6e6e"));
			viewHolder.rankAndName.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
			viewHolder.totalGoalOrAssist.setTextColor(Color.parseColor("#6e6e6e"));
			viewHolder.totalGoalOrAssist.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
			viewHolder.totalMatch.setTextColor(Color.parseColor("#6e6e6e"));
			viewHolder.totalMatch.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
			viewHolder.totalEfficiency.setTextColor(Color.parseColor("#6e6e6e"));
			viewHolder.totalEfficiency.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
		}
		ShooterAssister item=mList.get(position);
		viewHolder.rankAndName.setText(item.getName());
		viewHolder.totalGoalOrAssist.setText(item.getData());
		viewHolder.totalMatch.setText(item.getMatchNumber());
		viewHolder.totalEfficiency.setText(item.getEfficiency());
		return convertView;
	}
	
	static class ViewHolder{
		TextView rankAndName=null;
		TextView totalGoalOrAssist=null;
		TextView totalMatch=null;
		TextView totalEfficiency=null;
		LinearLayout b=null;
		View line=null;
	}

	
}
