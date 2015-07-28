package com.example.kickfor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.easemob.util.EMLog;

import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.SectionIndexer;
import android.widget.TextView;

public class FriendsAdapter extends ArrayAdapter<MyFriend> implements
		SectionIndexer {

	private static final String TAG = "ContactAdapter";

	private LayoutInflater mInflater = null;
	private List<MyFriend> list = null;
	private List<String> mlist;
	private Context context = null;
	private int res;
	private List<MyFriend> copyFriendList;
	private SparseIntArray positionOfSection;
	private SparseIntArray sectionOfPosition;

	private MyFilter myFilter;

	private boolean notiyfyByFilter;

	// public FriendsAdapter(Context context, List<MyFriend> list){
	// this.context=context;
	// mInflater=LayoutInflater.from(context);
	// this.list=list;
	// }

	public FriendsAdapter(Context context, int resource, List<MyFriend> objects) {
		super(context, resource, objects);
		this.res = resource;
		this.list = objects;
		copyFriendList = new ArrayList<MyFriend>();
		copyFriendList.addAll(objects);
		mInflater = LayoutInflater.from(context);
		
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
		
	}

	@Override
	public MyFriend getItem(int position) {
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
		
		ViewHolder viewHolder = null;
		MyFriend item = list.get(position);
		if (convertView == null) {
			viewHolder = new ViewHolder();
			convertView = mInflater.inflate(res, null);
			viewHolder.name = (TextView) convertView.findViewById(R.id.list_name);
			viewHolder.photo = (ImageView) convertView.findViewById(R.id.list_photo);
			viewHolder.date = (TextView) convertView.findViewById(R.id.list_date);
			viewHolder.message = (TextView) convertView.findViewById(R.id.list_message);
			viewHolder.agree = (TextView) convertView.findViewById(R.id.list_agree);
			viewHolder.disagree = (TextView) convertView.findViewById(R.id.list_disagree);
			if(res==R.layout.vlist_side_bar){
				viewHolder.tvHeader = (TextView) convertView.findViewById(R.id.header);
			}
			else{
				viewHolder.number=(TextView)convertView.findViewById(R.id.list_msg_number);
			}
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		
		if(res==R.layout.vlist_side_bar){
			String header = item.getHeader();
			if (position == 0 || header != null && !header.equals(getItem(position - 1).getHeader())) {
				if (TextUtils.isEmpty(header)) {
					viewHolder.tvHeader.setVisibility(View.GONE);
				} else {
					viewHolder.tvHeader.setVisibility(View.VISIBLE);
					viewHolder.tvHeader.setText(header);
				}
			} else {
				viewHolder.tvHeader.setVisibility(View.GONE);
			}
		}

		viewHolder.name.setText(item.getName());
		viewHolder.message.setText(item.getMessage());
		viewHolder.photo.setImageBitmap(item.getImage());
		viewHolder.date.setText(item.getDate());
		viewHolder.agree.setVisibility(View.GONE);
		viewHolder.disagree.setVisibility(View.GONE);
		if (item.getType().equals("1")) {
			if (item.getResult().equals("u")) {
				System.out.println("u");
				viewHolder.date.setVisibility(View.GONE);
				viewHolder.agree.setVisibility(View.VISIBLE);
				viewHolder.disagree.setVisibility(View.VISIBLE);
				viewHolder.agree.setTag(item);
				viewHolder.disagree.setTag(item);
				viewHolder.agree.setOnClickListener(new MyOnClickListener(
						viewHolder));

				viewHolder.disagree.setOnClickListener(new MyOnClickListener(
						viewHolder));
			} else if (item.getResult().equals("a")) {
				System.out.println("a");
				viewHolder.agree.setVisibility(View.GONE);
				viewHolder.disagree.setVisibility(View.GONE);
				viewHolder.date.setVisibility(View.VISIBLE);
				viewHolder.date.setText("已接受");
			} else if (item.getResult().equals("r")) {
				viewHolder.agree.setVisibility(View.GONE);
				viewHolder.disagree.setVisibility(View.GONE);
				viewHolder.date.setVisibility(View.VISIBLE);
				viewHolder.date.setText("已拒绝");
			}
		}
		if(res==R.layout.vlist){
			if(item.getMsgNumber()>0){
				viewHolder.number.setVisibility(View.VISIBLE);
				viewHolder.number.setText(item.getMsgNumber()>99? "99+": ""+item.getMsgNumber());
			}
			else{
				viewHolder.number.setVisibility(View.GONE);
			}
		}
		return convertView;
	}

	static class ViewHolder {
		public TextView name = null;
		public TextView message = null;
		public ImageView photo = null;
		public TextView date = null;
		public TextView agree = null;
		public TextView disagree = null;
		public TextView tvHeader;
		public TextView number=null;
	}

	class MyOnClickListener implements OnClickListener {

		private ViewHolder viewHolder = null;

		public MyOnClickListener(ViewHolder viewHolder) {
			this.viewHolder = viewHolder;
			
		}
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int id = v.getId();
			if (id == R.id.list_agree) {
				Map<String, Object> map = new HashMap<String, Object>();
				MyFriend entity = ((MyFriend) v.getTag());
				map.put("request", "accept friend");
				map.put("phone", entity.getPhone());
				Runnable r = new ClientWrite(Tools.JsonEncode(map));
				new Thread(r).start();
				SQLHelper helper = SQLHelper.getInstance(context);
				ContentValues cv = new ContentValues();
				cv.put("i", entity.getIndex());
				cv.put("result", "a");
				helper.update(cv, "systemtable", entity.getIndex());
				viewHolder.agree.setVisibility(View.GONE);
				viewHolder.disagree.setVisibility(View.GONE);
				viewHolder.date.setVisibility(View.VISIBLE);
				viewHolder.date.setText("已接受");
			} else if (id == R.id.list_disagree) {
				MyFriend entity = ((MyFriend) v.getTag());
				SQLHelper helper = SQLHelper.getInstance(context);
				ContentValues cv = new ContentValues();
				cv.put("i", entity.getIndex());
				cv.put("result", "r");
				helper.update(cv, "systemtable", entity.getIndex());

				viewHolder.agree.setVisibility(View.GONE);
				viewHolder.disagree.setVisibility(View.GONE);
				viewHolder.date.setVisibility(View.VISIBLE);
				viewHolder.date.setText("已拒绝");
			}
		}

	}

	@Override
	public Object[] getSections() {
		if(res==R.layout.vlist_side_bar){
			positionOfSection = new SparseIntArray();
			sectionOfPosition = new SparseIntArray();
			int count = getCount();
			mlist = new ArrayList<String>();
			mlist.add(getContext().getString(R.string.search_header));
			positionOfSection.put(0, 0);
			sectionOfPosition.put(0, 0);
			
			System.out.println("count= "+count);
			for (int i = 0; i < count; i++) {

				String letter = getItem(i).getHeader();
				EMLog.d(TAG, "contactadapter getsection getHeader:" + letter
						+ " name:" + getItem(i).getPhone());
				
				int section = mlist.size() - 1;
				//System.out.println("section= "+section);
				//System.out.println("mlist's size= "+mlist.size());
				
				if (mlist.get(section) != null && !mlist.get(section).equals(letter)) {
					mlist.add(letter);
					section++;
					positionOfSection.put(section, i);
				}
				sectionOfPosition.put(i, section);
			}
			return mlist.toArray(new String[mlist.size()]);
		}
		else{
			System.out.println("bbbbbbbbbbbbbbbbbbbbbb");
			return null;
		}
	}

	@Override
	public int getPositionForSection(int sectionIndex) {
		if(res==R.layout.vlist_side_bar){
			return positionOfSection.get(sectionIndex);
		}
		else{
			return 0;
		}
	}
	
	
	
	
	@Override
	public int getSectionForPosition(int position) {
		// TODO Auto-generated method stub
		if(res==R.layout.vlist_side_bar){
			return sectionOfPosition.get(position);
		}
		else{
			return 0;
		}
	}

	@Override
	public Filter getFilter() {
		if (myFilter == null) {
			myFilter = new MyFilter(list);
		}
		return myFilter;
	}

	private class MyFilter extends Filter {
		List<MyFriend> mOriginalList = null;

		public MyFilter(List<MyFriend> myList) {
			this.mOriginalList = myList;
		}

		@Override
		protected synchronized FilterResults performFiltering(
				CharSequence constraint) {
			FilterResults results = new FilterResults();
			if (mOriginalList == null) {
				mOriginalList = new ArrayList<MyFriend>();
			}
			EMLog.d(TAG, "contacts original size: " + mOriginalList.size());
			EMLog.d(TAG, "contacts copy size: " + copyFriendList.size());

			if (constraint == null || constraint.length() == 0) {
				results.values = copyFriendList;
				results.count = copyFriendList.size();
			} else {
				String constraintString = constraint.toString();
				final int count = mOriginalList.size();
				final ArrayList<MyFriend> newValues = new ArrayList<MyFriend>();
				for (int i = 0; i < count; i++) {
					final MyFriend myFriend = mOriginalList.get(i);
					String phone = myFriend.getPhone();

					if (phone.startsWith(constraintString)) {
						newValues.add(myFriend);
					} else {
						final String[] words = phone.split(" ");
						final int wordCount = words.length;

						// Start at index 0, in case valueText starts with
						// space(s)
						for (int k = 0; k < wordCount; k++) {
							if (words[k].startsWith(constraintString)) {
								newValues.add(myFriend);
								break;
							}
						}
					}
				}
				results.values = newValues;
				results.count = newValues.size();
			}
			EMLog.d(TAG, "contacts filter results size: " + results.count);
			return results;
		}

		@Override
		protected synchronized void publishResults(CharSequence constraint,
				FilterResults results) {
			list.clear();
			list.addAll((List<MyFriend>) results.values);
			EMLog.d(TAG, "publish contacts filter results size: "
					+ results.count);
			if (results.count > 0) {
				notiyfyByFilter = true;
				notifyDataSetChanged();
				notiyfyByFilter = false;
			} else {
				notifyDataSetInvalidated();
			}

		}
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
		if (!notiyfyByFilter) {
			copyFriendList.clear();
			copyFriendList.addAll(list);
		}
	}

}
