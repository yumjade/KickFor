package com.example.kickfor;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class SkillsShowAdapter extends BaseAdapter{

	private List<SkillsShowEntity> mList=null;
	private LayoutInflater inflater=null;
	
	public SkillsShowAdapter(Context context, List<SkillsShowEntity> list){
		inflater=LayoutInflater.from(context);
		this.mList=list;
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
		SkillsShowEntity item=mList.get(position);
		ViewHolder viewHolder=null;
		if(convertView==null){
			convertView=inflater.inflate(R.layout.skills_show_item, null);
			viewHolder=new ViewHolder();
			viewHolder.agreeNum=(TextView)convertView.findViewById(R.id.skills_show_count);
			viewHolder.name=(TextView)convertView.findViewById(R.id.skills_show_name);
			convertView.setTag(viewHolder);
		}
		else{
			viewHolder=(ViewHolder)convertView.getTag();
		}
		viewHolder.agreeNum.setText(item.getAgreeNum());
		viewHolder.name.setText(item.getSkillsName());
		return convertView;
	}

	static class ViewHolder{
		TextView agreeNum=null;
		TextView name=null;
	}
	
}
