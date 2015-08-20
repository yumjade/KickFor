package com.example.kickfor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class SkillsSelectAdapter extends BaseAdapter{

	private List<SkillsSelectEntity> mList=null;
	private LayoutInflater inflater=null;
	private Bitmap selected=null;
	private Bitmap unselected=null;
	private String phone=null;
	
	
	public SkillsSelectAdapter(Context context, List<SkillsSelectEntity> mList){
		inflater=LayoutInflater.from(context);
		this.mList=mList;
		selected=BitmapFactory.decodeResource(context.getResources(), R.drawable.skills_selected);
		unselected=BitmapFactory.decodeResource(context.getResources(), R.drawable.skills_unselected);
		phone=new PreferenceData(context).getData(new String[]{"phone"}).get("phone").toString();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
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
		SkillsSelectEntity item=mList.get(position);
		ViewHolder viewHolder=null;
		if(convertView==null){
			viewHolder=new ViewHolder();
			convertView=inflater.inflate(R.layout.skills_select_item, null);
			viewHolder.name=(TextView)convertView.findViewById(R.id.skills_select_name);
			viewHolder.state=(ImageView)convertView.findViewById(R.id.skills_select_state);
			viewHolder.state.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					SkillsSelectEntity item=(SkillsSelectEntity)v.getTag();
					if(item.isAdd()==true){
						((ImageView)v).setImageBitmap(unselected);
						Map<String, Object> tmp=new HashMap<String, Object>();
						tmp.put("request", "del skills");
						tmp.put("phone", phone);
						tmp.put("skillkey", item.getSkillKey());
						Runnable r=new ClientWrite(Tools.JsonEncode(tmp));
						new Thread(r).start();
						item.setAddState(false);
					}
					else{
						((ImageView)v).setImageBitmap(selected);
						Map<String, Object> tmp=new HashMap<String, Object>();
						tmp.put("request", "add skills");
						tmp.put("phone", phone);
						tmp.put("skillstr", item.getSkillKey());
						Runnable r=new ClientWrite(Tools.JsonEncode(tmp));
						new Thread(r).start();
						item.setAddState(true);
					}
				}
				
			});
			convertView.setTag(viewHolder);
		}
		else{
			viewHolder=(ViewHolder)convertView.getTag();
		}
		viewHolder.name.setText(item.getSkillName());
		viewHolder.state.setImageBitmap(item.isAdd()? selected: unselected);
		viewHolder.state.setTag(item);
		return convertView;
	}
	
	
	static class ViewHolder{
		TextView name=null;
		ImageView state=null;
	}
	
}
