package com.example.kickfor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.kickfor.FileShowAdapter.ViewHolder;
import com.example.kickfor.team.ChangingRoomEntity;
import com.example.kickfor.team.HonorInfo;
import com.example.kickfor.wheelview.ScreenInfo;
import com.example.kickfor.wheelview.WheelYear;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class FileEditAdapter extends BaseAdapter {
	
	final String[]items ={"冠军", "亚军", "季军", "四强", "八强", "出现", "保级"};
	
	private LayoutInflater mInflater=null;
	private List<SubFile> mList=null;
	private Context context=null;
	private FileEditAdapter adapter=null;
	private ListView mListView=null;
	private WheelYear wheelYear=null;
    
    public FileEditAdapter(Context context, List<SubFile> list, ListView mListView) {
    	this.context = context;
		mInflater = LayoutInflater.from(context);
		this.mList = list;
		adapter=this;
		this.mListView=mListView;
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
		ViewHolder viewHolder = null;
		SubFile item=mList.get(position);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.event_item, null);
			viewHolder.eventName = (EditText) convertView.findViewById(R.id.et_event_name);
			viewHolder.winningYear = (TextView) convertView.findViewById(R.id.et_winning_year);
			viewHolder.finalRank = (TextView) convertView.findViewById(R.id.et_final_rank);
			viewHolder.cancel=(ImageView)convertView.findViewById(R.id.event_cancel);
			
			viewHolder.cancel.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					final View vi=v;
					int position=(Integer)vi.getTag();
					mList.remove(position);
					adapter.notifyDataSetChanged();
					Tools.setListViewHeight(mListView);
				}
				
			});
			
			viewHolder.finalRank.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					final TextView vi=(TextView)v;
					int position=(Integer)v.getTag();
					final SubFile item=mList.get(position);
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					builder.setTitle("");
					ListAdapter catalogsAdapter = new ArrayAdapter<String>(context, R.layout.match_type_item, items);
					builder.setAdapter(catalogsAdapter, new DialogInterface.OnClickListener(){
					    public void onClick(DialogInterface arg0, int arg1) {
					        vi.setText(items[arg1]);
					        item.setRanking(items[arg1]);
					    }
					});
					builder.show();
				}	
			});
			
			
			viewHolder.winningYear.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					final TextView vi=(TextView)v;
					int position=(Integer)v.getTag();
					final SubFile item=mList.get(position);
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
									item.setYear(wheelYear.getYear());
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
			
			class MyTextWatcher implements TextWatcher {
				
				private ViewHolder mHolder;
				
				public MyTextWatcher(ViewHolder holder) {
                    mHolder = holder;
                }

				@Override
				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
					
				}

				@Override
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					
				}

				@Override
				public void afterTextChanged(Editable s) {
					if(!TextUtils.isEmpty(s)){
						int position=(Integer)mHolder.eventName.getTag();
						mList.get(position).setMatchName(s.toString());	
					}

				}
			}
			viewHolder.eventName.addTextChangedListener(new MyTextWatcher(viewHolder));
			convertView.setTag(viewHolder);
		}else{
			 viewHolder = (ViewHolder) convertView.getTag(); 
		}
		viewHolder.eventName.setTag(position);
        viewHolder.eventName.setText(item.getMatchName());
        viewHolder.winningYear.setTag(position);
        viewHolder.winningYear.setText(item.getYear());
        viewHolder.finalRank.setTag(position);
        viewHolder.finalRank.setText(item.getRanking());
        viewHolder.cancel.setTag(position);
		return convertView;
	}
	
	static class ViewHolder {
		public EditText eventName = null;
		public TextView winningYear = null;
		public TextView finalRank = null;
		public ImageView cancel=null;
	}

}
