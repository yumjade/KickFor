package com.example.kickfor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.kickfor.FileAdapter.ViewHolder;
import com.example.kickfor.team.ChangingRoomEntity;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class FileEditAdapter extends BaseAdapter {
	
	private LayoutInflater mInflater;
	private List<File> mList=null;
    private List<Map<String, Object>> mData;// ´æ´¢µÄEditTextÖµ
    public Map<String, String> editorValue = new HashMap<String, String>();
	private Context context;
	private int resource;
    
    public FileEditAdapter(Context context, int resource, List<File> list) {
    	this.context = context;
		this.resource = resource;
		mInflater = LayoutInflater.from(context);
		this.mList = list;
//        init();
    }

	private void init() {
		editorValue.clear();
		
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
	
	private Integer index = -1;

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		File item=mList.get(position);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.event_item, null);
			viewHolder.eventName = (EditText) convertView.findViewById(R.id.et_event_name);
			viewHolder.winningYear = (EditText) convertView.findViewById(R.id.et_winning_year);
			viewHolder.finalRank = (EditText) convertView.findViewById(R.id.et_final_rank);
			
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
							String str=mList.get(position).getTeamName();
							
								mList.get(position).setTeamName(s.toString());
							
					}

				}
			}
			viewHolder.eventName.addTextChangedListener(new MyTextWatcher(viewHolder));
			convertView.setTag(viewHolder);
		}else{
			 viewHolder = (ViewHolder) convertView.getTag();
	        
		}
		 viewHolder.eventName.setTag(position);
         viewHolder.eventName.setText(item.getEventName());

		return convertView;
	}
	
	static class ViewHolder {
		public EditText eventName = null;
		public EditText winningYear = null;
		public EditText finalRank = null;
	}

}
