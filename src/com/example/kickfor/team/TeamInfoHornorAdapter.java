package com.example.kickfor.team;

import java.util.List;




import com.example.kickfor.R;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

public class TeamInfoHornorAdapter extends BaseAdapter{

	private LayoutInflater inflater=null;
	private List<HonorInfo> mList=null;
	private boolean isEdit=false;
	
	public TeamInfoHornorAdapter(Context context, List<HonorInfo> mList){
		inflater=LayoutInflater.from(context);
		this.mList=mList;
	}
	
	public void setEditStatus(boolean isEdit){
		this.isEdit=isEdit;
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
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder=null;
		if(convertView==null){
			viewHolder=new ViewHolder();
			convertView=inflater.inflate(R.layout.team_history_honor_item, null);
			viewHolder.year=(EditText)convertView.findViewById(R.id.honor_year);
			viewHolder.name=(EditText)convertView.findViewById(R.id.honor_name);
			viewHolder.result=(EditText)convertView.findViewById(R.id.honor_result);
			
			class MyTextWatcher implements TextWatcher{
				private ViewHolder mHolder=null;
				private int status;
				protected final static int YEAR=1;
				protected final static int NAME=2;
				protected final static int RESULT=3;
				
				public MyTextWatcher(ViewHolder mHolder, int status){
					this.mHolder=mHolder;
					this.status=status;
				}
				@Override
				public void afterTextChanged(Editable arg0) {
					// TODO Auto-generated method stub
					if(!TextUtils.isEmpty(arg0)){
						if(status==YEAR){
							int position=(Integer)mHolder.year.getTag();
							mList.get(position).setUpdateYear(arg0.toString());
							String str=mList.get(position).getYear();
							if(!str.equals(arg0)){
								mList.get(position).setChanged();
							}
						}
						else if(status==NAME){
							int position=(Integer)mHolder.name.getTag();
							mList.get(position).setUpdateName(arg0.toString());
							String str=mList.get(position).getName();
							if(!str.equals(arg0)){
								mList.get(position).setChanged();
							}
						}
						else if(status==RESULT){
							int position=(Integer)mHolder.result.getTag();
							mList.get(position).setUpdateResult(arg0.toString());
							String str=mList.get(position).getResult();
							if(!str.equals(arg0)){
								mList.get(position).setChanged();
							}
						}
					}
				}
				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1,
						int arg2, int arg3) {
					// TODO Auto-generated method stub
					
				}
				@Override
				public void onTextChanged(CharSequence arg0, int arg1,
						int arg2, int arg3) {
					// TODO Auto-generated method stub
					
				}
			}
			viewHolder.year.addTextChangedListener(new MyTextWatcher(viewHolder, MyTextWatcher.YEAR));
			viewHolder.name.addTextChangedListener(new MyTextWatcher(viewHolder, MyTextWatcher.NAME));
			viewHolder.result.addTextChangedListener(new MyTextWatcher(viewHolder, MyTextWatcher.RESULT));
			convertView.setTag(viewHolder);
		}
		else{
			viewHolder=(ViewHolder)convertView.getTag();
		}
		HonorInfo item=mList.get(position);
		viewHolder.year.setTag(position);
		viewHolder.name.setTag(position);
		viewHolder.result.setTag(position);
		viewHolder.year.setText(item.getYear());
		viewHolder.name.setText(item.getName());
		viewHolder.result.setText(item.getResult());
		if(isEdit==true){
			viewHolder.year.setBackgroundResource(R.drawable.rect_4);
			viewHolder.name.setBackgroundResource(R.drawable.rect_4);
			viewHolder.result.setBackgroundResource(R.drawable.rect_4);
		}
		else{
			viewHolder.year.setBackground(null);
			viewHolder.name.setBackground(null);
			viewHolder.result.setBackground(null);
		}
		return convertView;
	}

	static class ViewHolder{
		EditText year=null;
		EditText name=null;
		EditText result=null;
	}
	

	
	
}

