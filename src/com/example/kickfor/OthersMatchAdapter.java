package com.example.kickfor;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class OthersMatchAdapter extends BaseAdapter{

	
	private List<OthersMatchEntity> mList=null;
	private LayoutInflater inflater=null;
	
	public OthersMatchAdapter(Context context, List<OthersMatchEntity> mList){
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
		ViewHolder viewHolder=null;
		if(convertView==null){
			viewHolder=new ViewHolder();
			convertView=inflater.inflate(R.layout.others_match_item, null);
			viewHolder.name=(TextView)convertView.findViewById(R.id.others_person_name);
			viewHolder.number=(TextView)convertView.findViewById(R.id.others_person_number);
			viewHolder.goal=(TextView)convertView.findViewById(R.id.others_person_goal);
			viewHolder.assist=(TextView)convertView.findViewById(R.id.others_person_assist);
			convertView.setTag(viewHolder);
			
		}
		else{
			viewHolder=(ViewHolder)convertView.getTag();
		}
		OthersMatchEntity item=mList.get(position);
		if (position == 0) {
			convertView.setBackgroundResource(R.color.other_match);
		}else{
			convertView.setBackgroundResource(R.color.other_list);
		}
		viewHolder.name.setText(item.getName());
		viewHolder.number.setText(item.getNumber());
		viewHolder.goal.setText(item.getGoal());
		viewHolder.assist.setText(item.getAssist());
		return convertView;
	}
	
	static class ViewHolder{
		TextView name=null;
		TextView number=null;
		TextView goal=null;
		TextView assist=null;
	}
}
