package com.example.kickfor;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class FileSelectAdapter extends BaseAdapter{

	private List<FileEntity> mList=null;
	private LayoutInflater inflater=null;
	
	public FileSelectAdapter(Context context, List<FileEntity> mList){
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
		FileEntity item=mList.get(position);
		ViewHolder viewHolder=null;
		if(convertView==null){
			convertView=inflater.inflate(R.layout.file_select_item, null);
			viewHolder=new ViewHolder();
			viewHolder.position=(TextView)convertView.findViewById(R.id.file_select_position);
			viewHolder.date=(TextView)convertView.findViewById(R.id.file_select_date);
			viewHolder.teamName=(TextView)convertView.findViewById(R.id.file_select_teamname);
			convertView.setTag(viewHolder);
		}
		else{
			viewHolder=(ViewHolder)convertView.getTag();
		}
		viewHolder.position.setText(item.getPosition());
		viewHolder.date.setText(item.getJoinDate()+" - "+item.getExitDate());
		viewHolder.teamName.setText(item.getTeamName());
		return convertView;
	}

	static class ViewHolder{
		TextView position=null;
		TextView teamName=null;
		TextView date=null;
	}
	
}
