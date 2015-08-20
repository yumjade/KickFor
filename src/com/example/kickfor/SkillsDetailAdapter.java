package com.example.kickfor;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SkillsDetailAdapter extends BaseAdapter{

	private List<SkillDetailEntity> mList=null;
	private LayoutInflater inflater=null;
	
	public SkillsDetailAdapter(Context context, List<SkillDetailEntity> mList){
		this.mList=mList;
		inflater=LayoutInflater.from(context);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mList.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		SkillDetailEntity item=mList.get(position);
		ViewHolder viewHolder=null;
		if(convertView==null){
			convertView=inflater.inflate(R.layout.skills_detail_item, null);
			viewHolder=new ViewHolder();
			viewHolder.image=(ImageView)convertView.findViewById(R.id.skill_detail_item_image);
			viewHolder.name=(TextView)convertView.findViewById(R.id.skill_detail_item_name);
			viewHolder.team=(TextView)convertView.findViewById(R.id.skill_detail_item_teamname);
			viewHolder.position=(TextView)convertView.findViewById(R.id.skill_detail_item_position);
			convertView.setTag(viewHolder);
		}
		else{
			viewHolder=(ViewHolder)convertView.getTag();
		}
		viewHolder.image.setImageBitmap(item.getImage());
		viewHolder.name.setText(item.getName());
		viewHolder.team.setText(item.getTeam());
		viewHolder.position.setText(item.getPosition());
		return convertView;
	}

	static class ViewHolder{
		ImageView image=null;
		TextView name=null;
		TextView team=null;
		TextView position=null;
	}
	
}
