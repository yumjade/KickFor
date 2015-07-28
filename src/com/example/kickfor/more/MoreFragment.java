package com.example.kickfor.more;

import java.util.ArrayList;
import java.util.List;

import com.example.kickfor.R;
import com.example.kickfor.HomePageActivity;
import com.example.kickfor.utils.IdentificationInterface;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MoreFragment extends Fragment implements IdentificationInterface{

	private ListView mListView=null;
	private List<MoreItem> mList=new ArrayList<MoreItem>();
    private MoreAdapter adapter=null;
	
    @Override
	public int getFragmentLevel() {
		// TODO Auto-generated method stub
		return IdentificationInterface.MAIN_LEVEL;
	}
    
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.fragment_more, container, false);
		mListView=(ListView)view.findViewById(R.id.more_list);
		initiate();
		adapter=new MoreAdapter(mList, getActivity());
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				int index=mList.get(arg2).getNumber();
				switch(index){
				case 1:
					((HomePageActivity)getActivity()).fromViewPager=false;
					((HomePageActivity)getActivity()).openSearch();
					break;
				case 2:
					break;
				case 3:
					((HomePageActivity)getActivity()).openProtocols();
					break;
				case 4:
					break;
				case 5:
					((HomePageActivity)getActivity()).settings();
					break;

				}
			}
			
		});
		return view;
	}
	

	
	private void initiate(){
		mList.clear();
		mList.add(new MoreItem("搜索", BitmapFactory.decodeResource(getResources(), R.drawable.more_item_search), 1));
		mList.add(new MoreItem("关于我们",BitmapFactory.decodeResource(getResources(), R.drawable.more_item_aboutus), 2));
		mList.add(new MoreItem("服务协议",BitmapFactory.decodeResource(getResources(), R.drawable.more_item_service), 3));
		mList.add(new MoreItem("意见反馈",BitmapFactory.decodeResource(getResources(), R.drawable.more_item_suggest), 4));
		mList.add(new MoreItem("设置",BitmapFactory.decodeResource(getResources(), R.drawable.more_item_control), 5));

	}
	
	static class MoreAdapter extends BaseAdapter{
		
		private List<MoreItem> mList=null;
		private LayoutInflater mInflater=null;

		public MoreAdapter(List<MoreItem> mList, Context context){
			this.mList=mList;
			mInflater=LayoutInflater.from(context);
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

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ViewHolder viewHolder=null;
			if(convertView==null){
				viewHolder=new ViewHolder();
				convertView=(View)mInflater.inflate(R.layout.more_item, null);
				viewHolder.image=(ImageView)convertView.findViewById(R.id.more_item_iv);
				viewHolder.text=(TextView)convertView.findViewById(R.id.more_item_text);
				convertView.setTag(viewHolder);
			}
			else{
				viewHolder=(ViewHolder)convertView.getTag();
			}
			MoreItem item=mList.get(position);
			viewHolder.image.setImageBitmap(item.getImage());
			viewHolder.text.setText(item.getName());
			return convertView;
		}
		
		static class ViewHolder{
			ImageView image=null;
			TextView text=null;
		}
		
	}
	
	static class MoreItem{
		private String name=null;
		private Bitmap image=null;
		private int number;
		
		public MoreItem(String name, Bitmap image, int number){
			this.name=name;
			this.image=image;
			this.number=number;
		}
		
		public int getNumber(){
			return number;
		}
		
		public String getName(){
			return name;
		}
		
		public Bitmap getImage(){
			return image;
		}
	}
}
