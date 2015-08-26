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

public class SkillsShowOthersAdapter extends BaseAdapter{

	private Bitmap selected=null;
	private Bitmap unselected=null;
	private List<SkillsShowEntity> mList=null;
	private LayoutInflater inflater=null;
	private String host=null;
	private String phone=null;
	
	
	public SkillsShowOthersAdapter(Context context, List<SkillsShowEntity> mList, String host, String phone){
		inflater=LayoutInflater.from(context);
		this.mList=mList;
		selected=BitmapFactory.decodeResource(context.getResources(), R.drawable.skills_selected);
		unselected=BitmapFactory.decodeResource(context.getResources(), R.drawable.skills_unselected);
		this.host=host;
		this.phone=phone;
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
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		SkillsShowEntity item=mList.get(position);
		ViewHolder viewHolder=null;
		if(convertView==null){
			viewHolder=new ViewHolder();
			convertView=inflater.inflate(R.layout.skills_show_others_item, null);
			viewHolder.count=(TextView)convertView.findViewById(R.id.skills_other_show_count);
			viewHolder.name=(TextView)convertView.findViewById(R.id.skills_other_show_name);
			viewHolder.state=(TextView)convertView.findViewById(R.id.skills_other_show_ensured);
			viewHolder.stateImg=(ImageView)convertView.findViewById(R.id.skills_other_show_state);
			viewHolder.stateImg.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ViewHolder viewHolder=(ViewHolder)((View)v.getParent().getParent()).getTag();
					SkillsShowEntity item=(SkillsShowEntity)v.getTag();
					if(item.isAgree()==false){
						Map<String, Object> tmp=new HashMap<String, Object>();
						tmp.put("request", "agree skills");
						tmp.put("skillkey", item.getSkillsKey());
						tmp.put("skilluserphone", phone);
						tmp.put("phone", host);
						Runnable r=new ClientWrite(Tools.JsonEncode(tmp));
						new Thread(r).start();
						((ImageView)v).setImageBitmap(selected);
						viewHolder.count.setBackgroundResource(R.drawable.skills_number);
					}
					else{
						
					}
				}
				
			});
			convertView.setTag(viewHolder);
		}
		else{
			viewHolder=(ViewHolder)convertView.getTag();
		}
		viewHolder.count.setText(item.getAgreeNum());
		viewHolder.name.setText(item.getSkillsName());
		if(item.isAgree()==true){
			viewHolder.state.setVisibility(View.VISIBLE);
			viewHolder.stateImg.setImageBitmap(selected);
			viewHolder.count.setBackgroundResource(R.drawable.skills_number);
			
		}
		else{
			viewHolder.state.setVisibility(View.INVISIBLE);
			viewHolder.stateImg.setImageBitmap(unselected);
			viewHolder.count.setBackgroundResource(R.drawable.skills_unverified);
		}
		viewHolder.stateImg.setTag(item);
		return convertView;
	}

	static class ViewHolder{
		TextView count=null;
		TextView name=null;
		TextView state=null;
		ImageView stateImg=null;
	}
	
	
}
