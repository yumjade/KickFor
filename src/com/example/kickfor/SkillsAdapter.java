package com.example.kickfor;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SkillsAdapter extends BaseAdapter {
	
	private Context context;
	private int resource;
	private LayoutInflater mInflater;
	private List<Skills> list;
	

	public SkillsAdapter(Context context, int resource, List<Skills> list) {
		this.context = context;
		this.resource = resource;
		mInflater = LayoutInflater.from(context);
		this.list = list;
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		Skills item = list.get(position);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(resource, null);
			viewHolder.count = (TextView) convertView.findViewById(R.id.tv_skills_count);
			viewHolder.skillsName = (TextView) convertView.findViewById(R.id.tv_skills_name);
			viewHolder.photo = (ImageView) convertView.findViewById(R.id.iv_photo);
			viewHolder.name = (TextView) convertView.findViewById(R.id.tv_name);
			viewHolder.teamName = (TextView) convertView.findViewById(R.id.tv_team_name);
			viewHolder.position = (TextView) convertView.findViewById(R.id.tv_position);
			viewHolder.selected = (ImageView) convertView.findViewById(R.id.skills_selected);
			viewHolder.unselected = (ImageView) convertView.findViewById(R.id.skills_unselected);
			
			convertView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		if (resource == R.layout.skills_item) {
			viewHolder.count.setText(String.valueOf(item.getCount()));
			viewHolder.skillsName.setText(item.getSkillsName());
		}
		if (resource == R.layout.my_skills_style_item) {
			viewHolder.count.setText(String.valueOf(item.getCount()));
			viewHolder.skillsName.setText(item.getSkillsName());
		}
		if (resource == R.layout.my_skills_style_item1) {
			viewHolder.skillsName.setText(item.getSkillsName());
			viewHolder.selected.setImageBitmap(item.getSelected());
			viewHolder.unselected.setImageBitmap(item.getUnselected());
		}
		if (resource == R.layout.my_skills_item) {
			viewHolder.photo.setImageBitmap(item.getPhoto());;
			viewHolder.name.setText(item.getName());
			viewHolder.teamName.setText(item.getTeamName());
			viewHolder.position.setText(item.getPosition());
		}
		
		
		return convertView;
	}
	
	static class ViewHolder{
		public TextView count = null;
		public TextView skillsName = null;
		public ImageView photo;
		public TextView name;
		public TextView teamName;
		public TextView position;
		public ImageView selected;
		public ImageView unselected;
	}

}
