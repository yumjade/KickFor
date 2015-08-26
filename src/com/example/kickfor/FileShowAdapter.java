package com.example.kickfor;

import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class FileShowAdapter extends BaseAdapter {

	private Context context;
	private LayoutInflater mInflater;
	private List<FileEntity> list;

	public FileShowAdapter(Context context, List<FileEntity> list) {
		this.context = context;
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
		FileEntity item = list.get(position);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.file_show_item, null);
			viewHolder.photo = (ImageView) convertView.findViewById(R.id.file_show_photo);
			viewHolder.position = (TextView) convertView.findViewById(R.id.file_show_position);
			viewHolder.teamName = (TextView) convertView.findViewById(R.id.file_show_teamname);
			viewHolder.date = (TextView) convertView.findViewById(R.id.file_show_date);
			viewHolder.match = (LinearLayout) convertView.findViewById(R.id.file_show_event);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		viewHolder.match.removeAllViews();
		viewHolder.position.setText(item.getPosition());
		viewHolder.teamName.setText(item.getTeamName());
		viewHolder.date.setText(item.getJoinDate()+" - "+item.getExitDate());
		List<SubFile> mList=item.getMatch();
		if(mList.size()==0){
			TextView t1=new TextView(context);
			t1.setTextSize(12);
			t1.setText("ÔÝÎÞ");
			t1.setPadding(10, 15, 0, 0);
			viewHolder.match.addView(t1);	
		}
		Iterator<SubFile> iter=mList.iterator();
		while(iter.hasNext()){
			SubFile entity=iter.next();
			TextView t1=new TextView(context);
			t1.setTextSize(12);
				t1.setText(entity.getMatchName()+"£¨"+entity.getYear()+"£©");
			
			LinearLayout ll=new LinearLayout(context);
			ll.setOrientation(LinearLayout.HORIZONTAL);
			ll.setPadding(10, 15, 0, 0);
			LinearLayout.LayoutParams params1=new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
			params1.weight=6;
			
			ll.addView(t1, params1);
			TextView t2=new TextView(context);
			t2.setTextSize(12);
			t2.setText(entity.getRanking());
			LinearLayout.LayoutParams params2=new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT);
			params2.weight=1;
			ll.addView(t2, params2);
			viewHolder.match.addView(ll);	
		}
		return convertView;
	}

	static class ViewHolder {
		public ImageView photo = null;
		public TextView position = null;
		public TextView date = null;
		public TextView teamName = null;
		public LinearLayout match = null;
	}

}
