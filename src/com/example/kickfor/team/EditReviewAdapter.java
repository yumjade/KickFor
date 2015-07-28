package com.example.kickfor.team;

import java.util.List;

import com.example.kickfor.R;
import com.example.kickfor.Tools;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class EditReviewAdapter extends BaseAdapter implements OnClickListener, OnLongClickListener{
	
	private List<Info> mList=null;
	private LayoutInflater mInflater=null;
	private ListView mListView=null;
	private int height=0;
	
	public EditReviewAdapter(Context context, List<Info> mList, ListView mListView){
		this.mList=mList;
		mInflater=LayoutInflater.from(context);
		this.mListView=mListView;
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
	
	public void setCurrentHeight(int height){
		this.height=height;
	}


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		Info entity=mList.get(position);
		ViewHolder viewHolder=null;
		if(convertView==null){
			convertView=(View)mInflater.inflate(R.layout.edit_review_item, null);
			viewHolder=new ViewHolder();
			viewHolder.number=(TextView)convertView.findViewById(R.id.editre_number);
			viewHolder.name=(TextView)convertView.findViewById(R.id.editre_name);
			viewHolder.ensure=(TextView)convertView.findViewById(R.id.editre_ensure);
			viewHolder.ensure.setOnClickListener(this);
			viewHolder.name.setOnClickListener(this);
			viewHolder.number.setOnClickListener(this);
			viewHolder.r=(LinearLayout)convertView.findViewById(R.id.editre_info);
			viewHolder.goal=(ImageView)convertView.findViewById(R.id.editre_goal);
			viewHolder.assist=(ImageView)convertView.findViewById(R.id.editre_assist);
			viewHolder.card=(ImageView)convertView.findViewById(R.id.editre_card);
			viewHolder.goal.setOnClickListener(this);
			viewHolder.assist.setOnClickListener(this);
			viewHolder.card.setOnClickListener(this);
			viewHolder.goal.setOnLongClickListener(this);
			viewHolder.assist.setOnLongClickListener(this);
			viewHolder.card.setOnLongClickListener(this);
			viewHolder.goalText=(TextView)convertView.findViewById(R.id.editre_value1);
			viewHolder.assistText=(TextView)convertView.findViewById(R.id.editre_value2);
			viewHolder.cardText=(TextView)convertView.findViewById(R.id.editre_value3);
			convertView.setTag(viewHolder);
		}
		else{
			viewHolder=(ViewHolder)convertView.getTag();
		}
		viewHolder.number.setText(entity.getNumber());
		viewHolder.name.setText(entity.getName());
		viewHolder.ensure.setTag(position);
		viewHolder.name.setTag(position);
		viewHolder.number.setTag(position);
		viewHolder.goal.setTag(position);
		viewHolder.assist.setTag(position);
		viewHolder.card.setTag(position);
		if(entity.getAttendance()){
			viewHolder.ensure.setText("取消确认");
			viewHolder.ensure.setBackgroundColor(Color.parseColor("#ff3300"));
		}
		viewHolder.goalText.setText(entity.getGoal());
		viewHolder.assistText.setText(entity.getAssist());
		if(entity.getCard()==1){
			viewHolder.card.setImageResource(R.drawable.yellow_card);
			viewHolder.cardText.setText("X 1");
		}
		else if(entity.getCard()==2){
			viewHolder.card.setImageResource(R.drawable.red_card);
			viewHolder.cardText.setText("X 1");
		}
		return convertView;
	}
	
	static class ViewHolder{
		TextView number=null;
		TextView name=null;
		TextView ensure=null;
		LinearLayout r=null;
		ImageView goal=null;
		ImageView assist=null;
		ImageView card=null;
		TextView goalText=null;
		TextView assistText=null;
		TextView cardText=null;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id=v.getId();
		switch(id){
		case R.id.editre_ensure:{
			int position=(Integer)v.getTag();
			Info item=mList.get(position);
			ViewHolder viewHolder=(ViewHolder)((View)v.getParent().getParent()).getTag();
			item.setAttendance();
			if(item.getAttendance()){
				((TextView)v).setText("取消确认");
				((TextView)v).setBackgroundColor(Color.parseColor("#ff3300"));
			}
			else{
				((TextView)v).setText("确认到场");
				((TextView)v).setBackgroundColor(Color.parseColor("#22a100"));
				item.resetGoal();
				item.resetAssist();
				item.resetCard();
				if(viewHolder.r.isShown()){
					viewHolder.r.measure(0, 0);
					height=height-viewHolder.r.getMeasuredHeight();
					viewHolder.r.setVisibility(View.GONE);
					Tools.changeListViewHeight(height, mListView);
				}
				
			}
			break;
		}
		case R.id.editre_name:{
			int position=(Integer)v.getTag();
			Info item=mList.get(position);
			ViewHolder viewHolder=(ViewHolder)((View)v.getParent().getParent()).getTag();
			if(!item.getPoped()){
				viewHolder.r.setVisibility(View.VISIBLE);
				viewHolder.r.measure(0, 0);
				height=height+viewHolder.r.getMeasuredHeight();
				Tools.changeListViewHeight(height, mListView);
				item.setPoped();
			}
			else{
				viewHolder.r.measure(0, 0);
				height=height-viewHolder.r.getMeasuredHeight();
				viewHolder.r.setVisibility(View.GONE);
				Tools.changeListViewHeight(height, mListView);
				item.setPoped();
			}
			break;
		}
		case R.id.editre_number:{
			int position=(Integer)v.getTag();
			Info item=mList.get(position);
			ViewHolder viewHolder=(ViewHolder)((View)v.getParent().getParent()).getTag();
			if(!item.getPoped()){
				viewHolder.r.setVisibility(View.VISIBLE);
				viewHolder.r.measure(0, 0);
				height=height+viewHolder.r.getMeasuredHeight();
				Tools.changeListViewHeight(height, mListView);
				item.setPoped();
			}
			else{
				viewHolder.r.measure(0, 0);
				height=height-viewHolder.r.getMeasuredHeight();
				viewHolder.r.setVisibility(View.GONE);
				Tools.changeListViewHeight(height, mListView);
				item.setPoped();
			}
			break;
		}
		case R.id.editre_goal:{
			int position=(Integer)v.getTag();
			Info item=mList.get(position);
			ViewHolder viewHolder=(ViewHolder)((View)v.getParent().getParent()).getTag();
			item.setGoal();
			viewHolder.goalText.setText("X "+item.getGoal());
			if(!item.getAttendance()){
				item.setAttendance();
				viewHolder.ensure.setText("取消确认");
				viewHolder.ensure.setBackgroundColor(Color.parseColor("#ff3300"));
			}
			break;
		}
		case R.id.editre_assist:{
			int position=(Integer)v.getTag();
			Info item=mList.get(position);
			ViewHolder viewHolder=(ViewHolder)((View)v.getParent().getParent()).getTag();
			item.setAssist();
			viewHolder.assistText.setText("X "+item.getAssist());
			if(!item.getAttendance()){
				item.setAttendance();
				viewHolder.ensure.setText("取消确认");
				viewHolder.ensure.setBackgroundColor(Color.parseColor("#ff3300"));
			}
			break;
		}
		case R.id.editre_card:{
			int position=(Integer)v.getTag();
			Info item=mList.get(position);
			ViewHolder viewHolder=(ViewHolder)((View)v.getParent().getParent()).getTag();
			item.setCard();
			if(item.getCard()==1){
				viewHolder.card.setImageResource(R.drawable.yellow_card);
				viewHolder.cardText.setText("X 1");
			}
			else if(item.getCard()==2){
				viewHolder.card.setImageResource(R.drawable.red_card);
				viewHolder.cardText.setText("X 1");
			}
			else{
				viewHolder.card.setImageResource(R.drawable.yellow_card);
				viewHolder.cardText.setText("X 0");
			}
			if(!item.getAttendance()){
				item.setAttendance();
				viewHolder.ensure.setText("取消确认");
				viewHolder.ensure.setBackgroundColor(Color.parseColor("#ff3300"));
			}
			break;
		}
		}
	}


	@Override
	public boolean onLongClick(View v) {
		// TODO Auto-generated method stub
		int id=v.getId();
		switch(id){
		case R.id.editre_goal:{
			int position=(Integer)v.getTag();
			Info item=mList.get(position);
			ViewHolder viewHolder=(ViewHolder)((View)v.getParent().getParent()).getTag();
			item.resetGoal();
			viewHolder.goalText.setText("X "+item.getGoal());
			break;
		}
		case R.id.editre_assist:{
			int position=(Integer)v.getTag();
			Info item=mList.get(position);
			ViewHolder viewHolder=(ViewHolder)((View)v.getParent().getParent()).getTag();
			item.resetAssist();
			viewHolder.assistText.setText("X "+item.getAssist());
			break;
		}
		case R.id.editre_card:{
			int position=(Integer)v.getTag();
			Info item=mList.get(position);
			ViewHolder viewHolder=(ViewHolder)((View)v.getParent().getParent()).getTag();
			item.resetCard();
			viewHolder.card.setImageResource(R.drawable.yellow_card);
			viewHolder.cardText.setText("X 0");
			break;
		}
		}
		return true;
	}
	
	
	
		
}

