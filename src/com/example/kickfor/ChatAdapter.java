package com.example.kickfor;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


public class ChatAdapter extends BaseAdapter{
	
	
	private LayoutInflater mInflater=null;
	private List<MyChat> list=null;
	
	public ChatAdapter(Context context, List<MyChat> list){
		mInflater=LayoutInflater.from(context);
		this.list=list;	
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
		// TODO Auto-generated method stub
		System.out.println("jjjjjjjjjjjjjjj");
		LeftViewHolder leftViewHolder=null;
		RightViewHolder rightViewHolder=null;
		boolean isMyMsg;
		MyChat item=list.get(position);
		isMyMsg=item.getIsMyMsg();
		if(convertView==null){
			if(isMyMsg==true){
				rightViewHolder=new RightViewHolder();
				convertView=mInflater.inflate(R.layout.message_right, null);
				rightViewHolder.date=(TextView)convertView.findViewById(R.id.tv_right_date);
				rightViewHolder.message=(TextView)convertView.findViewById(R.id.tv_right_message);
				rightViewHolder.photo=(ImageView)convertView.findViewById(R.id.iv_right_photo);
				convertView.setTag(rightViewHolder);
			}
			else{
				leftViewHolder=new LeftViewHolder();
				convertView=mInflater.inflate(R.layout.message_left, null);
				leftViewHolder.date=(TextView)convertView.findViewById(R.id.tv_left_date);
				leftViewHolder.message=(TextView)convertView.findViewById(R.id.tv_left_message);
				leftViewHolder.photo=(ImageView)convertView.findViewById(R.id.iv_left_photo);
				convertView.setTag(leftViewHolder);
			}
			
		}
		else{
			if(isMyMsg==true){
				if(convertView.getTag() instanceof LeftViewHolder){
					rightViewHolder=new RightViewHolder();
					convertView=mInflater.inflate(R.layout.message_right, null);
					rightViewHolder.date=(TextView)convertView.findViewById(R.id.tv_right_date);
					rightViewHolder.message=(TextView)convertView.findViewById(R.id.tv_right_message);
					rightViewHolder.photo=(ImageView)convertView.findViewById(R.id.iv_right_photo);
					convertView.setTag(rightViewHolder);
				}
				else{
					rightViewHolder=(RightViewHolder)convertView.getTag();
				}
			}
			else{
				if(convertView.getTag() instanceof RightViewHolder){
					leftViewHolder=new LeftViewHolder();
					convertView=mInflater.inflate(R.layout.message_left, null);
					leftViewHolder.date=(TextView)convertView.findViewById(R.id.tv_left_date);
					leftViewHolder.message=(TextView)convertView.findViewById(R.id.tv_left_message);
					leftViewHolder.photo=(ImageView)convertView.findViewById(R.id.iv_left_photo);
					convertView.setTag(leftViewHolder);
				}
				else{
					leftViewHolder=(LeftViewHolder)convertView.getTag();
				}
			}
		}
		if(isMyMsg==true){
			rightViewHolder.photo.setImageBitmap(item.getImage());
			if(item.getDate().isEmpty()){
				rightViewHolder.date.setVisibility(View.GONE);
			}
			else{
				rightViewHolder.date.setVisibility(View.VISIBLE);
				rightViewHolder.date.setText(item.getDate());
			}
			rightViewHolder.message.setText(item.getMessage());
			rightViewHolder.isMyMsg=item.getIsMyMsg();
		}
		else{
			leftViewHolder.photo.setImageBitmap(item.getImage());
			if(item.getDate().isEmpty()){
				leftViewHolder.date.setVisibility(View.GONE);
			}
			else{
				leftViewHolder.date.setVisibility(View.VISIBLE);
				leftViewHolder.date.setText(item.getDate());
			}
			leftViewHolder.message.setText(item.getMessage());
			leftViewHolder.isMyMsg=item.getIsMyMsg();
		}
		return convertView;
	}
	
	static class LeftViewHolder{
		public TextView message=null;
		public TextView date=null;
		public ImageView photo=null;
		public boolean isMyMsg;
	}
	
	static class RightViewHolder{
		public TextView message=null;
		public TextView date=null;
		public ImageView photo=null;
		public boolean isMyMsg;
	}
}
