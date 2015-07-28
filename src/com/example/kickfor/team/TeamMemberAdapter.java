package com.example.kickfor.team;

import java.util.List;



import com.example.kickfor.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class TeamMemberAdapter extends BaseAdapter{

	private List<MemberInfo> mList=null;
	private LayoutInflater inflater=null;
	
	public TeamMemberAdapter(Context context, List<MemberInfo> mList){
		this.mList=mList;
		inflater=LayoutInflater.from(context);
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
			convertView=inflater.inflate(R.layout.team_info_item, null);
			viewHolder.name=(TextView)convertView.findViewById(R.id.item_info_name);
			viewHolder.number=(TextView)convertView.findViewById(R.id.item_info_number);
			viewHolder.position=(TextView)convertView.findViewById(R.id.item_info_position);
			viewHolder.aInfo=(TextView)convertView.findViewById(R.id.item_info_ainfo);
			viewHolder.efficiency=(TextView)convertView.findViewById(R.id.item_info_efficiency);
			convertView.setTag(viewHolder);
		}
		else{
			viewHolder=(ViewHolder)convertView.getTag();
		}
		MemberInfo item=mList.get(position);
		viewHolder.number.setText(item.getNumber());
		viewHolder.name.setText(item.getName());
		viewHolder.position.setText(item.getposition());
		viewHolder.aInfo.setText(item.getAInfo());
		viewHolder.efficiency.setText(item.getEfficiency());
		return convertView;
	}
	
	static class ViewHolder{
		TextView number=null;
		TextView name=null;
		TextView position=null;
		TextView aInfo=null;
		TextView efficiency=null;
	}

	
}
