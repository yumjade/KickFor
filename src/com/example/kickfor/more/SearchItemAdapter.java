package com.example.kickfor.more;

import java.util.List;

import com.example.kickfor.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SearchItemAdapter extends BaseAdapter{
	
	private LayoutInflater mInflater=null;
	private List<SearchItemEntity> mList=null;
	
	public SearchItemAdapter(Context context, List<SearchItemEntity> mList){
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
			convertView=mInflater.inflate(R.layout.search_list_item, null);
			viewHolder=new ViewHolder();
			viewHolder.type=(TextView)convertView.findViewById(R.id.search_type);
			viewHolder.name=(TextView)convertView.findViewById(R.id.search_name);
			viewHolder.info=(TextView)convertView.findViewById(R.id.search_description);
			viewHolder.image=(ImageView)convertView.findViewById(R.id.search_photo);
			convertView.setTag(viewHolder);
		}
		else{
			viewHolder=(ViewHolder)convertView.getTag();
		}
		SearchItemEntity item=mList.get(position);
		viewHolder.type.setText(item.getType());
		viewHolder.name.setText(item.getName());
		viewHolder.image.setImageBitmap(item.getImage());
		viewHolder.info.setText(item.getInfo());
		return convertView;
	}
	
	static class ViewHolder{
		TextView type=null;
		TextView name=null;
		TextView info=null;
		ImageView image=null;
	}
	
	

}
