package com.example.kickfor;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FileAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater mInflater;
	private List<File> list;
	private int resource;

	public FileAdapter(Context context, int resource, List<File> list) {
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
		File item = list.get(position);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(resource, null);
			viewHolder.photo = (ImageView) convertView.findViewById(R.id.file_photo);
			viewHolder.position = (TextView) convertView.findViewById(R.id.file_position);
			viewHolder.teamName = (TextView) convertView.findViewById(R.id.file_team_name);
			viewHolder.date = (TextView) convertView.findViewById(R.id.file_date);

			viewHolder.match = (TextView) convertView.findViewById(R.id.file_event);

			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		switch (resource) {
		case R.layout.file_item:
			viewHolder.photo.setImageBitmap(item.getPhoto());
			viewHolder.position.setText(item.getPosition());
			viewHolder.teamName.setText(item.getTeamName());
			viewHolder.date.setText(item.getDate());
			viewHolder.match.setText(item.getMatch());
			break;

		case R.layout.file_edit_item:
			viewHolder.photo.setImageBitmap(item.getPhoto());
			viewHolder.position.setText(item.getPosition());
			viewHolder.teamName.setText(item.getTeamName());
			viewHolder.date.setText(item.getDate());
			break;
//		case R.layout.event_item:
//			viewHolder.eventName.setText(item.getEventName());
//			viewHolder.winningYear.setText(item.getWinningYear());
//			viewHolder.finalRank.setText(item.getFinalRank());
//			break;
		}

		return convertView;
	}

	static class ViewHolder {
		public ImageView photo = null;
		public TextView position = null;
		public TextView date = null;
		public TextView teamName = null;
		public TextView match = null;
//		public EditText eventName = null;
//		public EditText winningYear = null;
//		public EditText finalRank = null;
	}

}
