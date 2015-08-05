package com.example.kickfor.team;

import java.util.ArrayList;
import java.util.List;

import com.example.kickfor.HomePageActivity;
import com.example.kickfor.R;
import com.example.kickfor.Tools;
import com.example.kickfor.wheelview.ScreenInfo;
import com.example.kickfor.wheelview.WheelYear;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class TeamInfoHornorAdapter extends BaseAdapter{
	
	final String[]items ={"冠军", "亚军", "季军", "四强", "八强", "出现", "保级"};

	private LayoutInflater inflater=null;
	private List<HonorInfo> mList=null;
	private boolean isEditing=false;
	private Context context=null;
	private WheelYear wheelYear=null;
	private ListView mListView=null;
	private TeamInfoHornorAdapter adapter=null;
	
	private List<HonorInfo> dList=null;
	
	public TeamInfoHornorAdapter(Context context, List<HonorInfo> mList, boolean isEditing, ListView mListView){
		inflater=LayoutInflater.from(context);
		this.mList=mList;
		this.isEditing=isEditing;
		this.context=context;
		this.mListView=mListView;
		adapter=this;
		dList=new ArrayList<HonorInfo>();
	}
	
	public List<HonorInfo> getDeleteList(){
		return dList;
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


	@SuppressWarnings("deprecation")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder=null;
		if(convertView==null){
			viewHolder=new ViewHolder();
			convertView=inflater.inflate(R.layout.team_history_honor_item, null);
			viewHolder.year=(TextView)convertView.findViewById(R.id.honor_year);
			viewHolder.name=(EditText)convertView.findViewById(R.id.honor_name);
			viewHolder.result=(TextView)convertView.findViewById(R.id.honor_result);
			viewHolder.cancel=(ImageView)convertView.findViewById(R.id.honor_cancel);
			if(isEditing==true){
				
				viewHolder.cancel.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						int position=(Integer)v.getTag();
						HonorInfo item=mList.get(position);
						mList.remove(item);
						adapter.notifyDataSetChanged();
						Tools.setListViewHeight(mListView);
						if((!item.getUpdateName().isEmpty()) && (!item.getUpdateYear().isEmpty()) && (!item.getUpdateResult().isEmpty())){
							dList.add(item);
						}
						
					}
					
				});
				
				viewHolder.result.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						final TextView vi=(TextView)v;
						int position=(Integer)v.getTag();
						final HonorInfo item=mList.get(position);
						AlertDialog.Builder builder = new AlertDialog.Builder(context);
						builder.setTitle("");
						ListAdapter catalogsAdapter = new ArrayAdapter<String>(context, R.layout.match_type_item, items);
						builder.setAdapter(catalogsAdapter, new DialogInterface.OnClickListener(){
						    public void onClick(DialogInterface arg0, int arg1) {
						        vi.setText(items[arg1]);
						        if(!item.getResult().equals(items[arg1])){
						        	item.setUpdateResult(items[arg1]);
						        	item.setChanged();
						        }
						    }
						});
						builder.show();
					}
					
				});
				
				viewHolder.year.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						final TextView vi=(TextView)v;
						int position=(Integer)v.getTag();
						final HonorInfo item=mList.get(position);
						LayoutInflater inflater=LayoutInflater.from(context);
						final View datepickerview=inflater.inflate(R.layout.yearpicker, null);
						wheelYear=new WheelYear(datepickerview);
						HomePageActivity c=(HomePageActivity)context;
						ScreenInfo screenInfo=new ScreenInfo(c);
						wheelYear.screenheight=screenInfo.getHeight();
						wheelYear.initDatePicker(Integer.parseInt(item.getYear().isEmpty()? "2000": item.getYear()));
						new AlertDialog.Builder(context)
						.setTitle("选择日期")
						.setView(datepickerview)
						.setPositiveButton("确定",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										vi.setText(wheelYear.getYear());
										if(!item.getYear().equals(wheelYear.getYear())){
											item.setUpdateYear(wheelYear.getYear());
											item.setChanged();
										}
									}
								})
						.setNegativeButton("取消",
								new DialogInterface.OnClickListener() {
									@Override
									public void onClick(DialogInterface dialog,
											int which) {
									}
								}).show();
					}
					
				});
				
				class MyTextWatcher implements TextWatcher{
					private ViewHolder mHolder=null;
					private int status;
					protected final static int NAME=2;
					
					public MyTextWatcher(ViewHolder mHolder, int status){
						this.mHolder=mHolder;
						this.status=status;
					}
					@Override
					public void afterTextChanged(Editable arg0) {
						// TODO Auto-generated method stub
						if(!TextUtils.isEmpty(arg0)){
							if(status==NAME){
								int position=(Integer)mHolder.name.getTag();
								mList.get(position).setUpdateName(arg0.toString());
								String str=mList.get(position).getName();
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
				viewHolder.name.addTextChangedListener(new MyTextWatcher(viewHolder, MyTextWatcher.NAME));
			}
			else{
				viewHolder.cancel.setVisibility(View.GONE);
				viewHolder.name.setEnabled(false);
			}
			
			
			convertView.setTag(viewHolder);
		}
		else{
			viewHolder=(ViewHolder)convertView.getTag();
		}
		HonorInfo item=mList.get(position);
		viewHolder.cancel.setTag(position);
		viewHolder.year.setTag(position);
		viewHolder.name.setTag(position);
		viewHolder.result.setTag(position);
		viewHolder.year.setText(item.getUpdateYear());
		viewHolder.name.setText(item.getUpdateName());
		viewHolder.result.setText(item.getUpdateResult());
		if(isEditing==true){
			viewHolder.year.setBackgroundResource(R.drawable.rect_4);
			viewHolder.name.setBackgroundResource(R.drawable.rect_4);
			viewHolder.result.setBackgroundResource(R.drawable.rect_4);
		}
		else{
			viewHolder.year.setBackgroundDrawable(null);
			viewHolder.name.setBackgroundDrawable(null);
			viewHolder.result.setBackgroundDrawable(null);
		}
		return convertView;
	}

	static class ViewHolder{
		TextView year=null;
		EditText name=null;
		TextView result=null;
		ImageView cancel=null;
	}
	

	
	
}

