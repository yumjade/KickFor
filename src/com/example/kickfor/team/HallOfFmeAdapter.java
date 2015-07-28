package com.example.kickfor.team;

import java.util.List;

import com.example.kickfor.team.HallofFame;
import com.example.kickfor.HomePageActivity;
import com.example.kickfor.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class HallOfFmeAdapter extends BaseAdapter {
	
	private Context context;
	private LayoutInflater mInflater;
	private List<HallofFame> list;
	private String teamid=null;
	private int authority=0;

	public HallOfFmeAdapter(Context context, List<HallofFame> list, String teamid, int authority) {
		this.context=context;
		mInflater=LayoutInflater.from(context);
		this.list=list;	
		this.teamid=teamid;
		this.authority=authority;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder=null;
		HallofFame item=list.get(position);
		if(convertView==null){
			viewHolder=new ViewHolder();	
			convertView=mInflater.inflate(R.layout.hall_of_fame_item, null);
			viewHolder.edit=(ImageView)convertView.findViewById(R.id.modifying_data);
			viewHolder.photo=(ImageView)convertView.findViewById(R.id.fame_photo);
			viewHolder.name=(TextView)convertView.findViewById(R.id.fame_name);
			viewHolder.position=(TextView)convertView.findViewById(R.id.team_position);
			viewHolder.date=(TextView)convertView.findViewById(R.id.attended_time);
			viewHolder.intruduction=(TextView)convertView.findViewById(R.id.fame_intruduction);
			viewHolder.edit.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					HallofFame item=(HallofFame)v.getTag();
					((HomePageActivity)context).openFame(HallOfFameFragment.SECOND_OPEN, teamid, String.valueOf(authority), item);
				}
				
			});
			convertView.setTag(viewHolder);
		}
		else{
			viewHolder=(ViewHolder)convertView.getTag();
		}
		viewHolder.photo.setImageBitmap(item.getPhoto());
		viewHolder.name.setText(item.getName());
		viewHolder.position.setText(item.getPosition());
		viewHolder.date.setText(item.getDate());
		viewHolder.intruduction.setText(item.getIntruduction());
		if(authority>=2){
			viewHolder.edit.setVisibility(View.VISIBLE);
			viewHolder.edit.setTag(item);
		}
		else{
			viewHolder.edit.setVisibility(View.GONE);
		}
		return convertView;
	}
	
	static class ViewHolder{
		public ImageView photo=null;
		public ImageView edit=null;
		public TextView name=null;
		public TextView position=null;
		public TextView date=null;
		public TextView intruduction=null;
	}

}
