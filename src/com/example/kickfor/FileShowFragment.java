package com.example.kickfor;

import java.util.ArrayList;
import java.util.List;

import com.example.kickfor.utils.IdentificationInterface;

import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FileShowFragment extends Fragment implements HomePageInterface,IdentificationInterface, HandlerListener {

	private ImageView back=null;
	private ListView mListView=null;
	private TextView edit=null;
	private Context context=null;

	private List<FileEntity> mList = null;
	private FileShowAdapter adapter;

	@Override
	public int getFragmentLevel() {
		return IdentificationInterface.SECOND_LEVEL;
	}

	@Override
	public void onChange(Message msg) {
		// TODO Auto-generated method stub
		if(msg.what==HomePageActivity.GET_ARCHIVES){
			this.mList.clear();
			SQLHelper helper=SQLHelper.getInstance(context);
			Cursor cursor=helper.select("archive", new String[]{"userarchiveskey", "uid", "teamname", "position", "inteam", "joindate", "exitdate"}, null, null, null);
			while(cursor.moveToNext()){
				FileEntity item=new FileEntity(context, cursor.getString(1), cursor.getInt(0));
				item.setData(cursor.getString(3), cursor.getString(2), cursor.getString(4), cursor.getString(5), cursor.getString(6));
				mList.add(item);
			}
			if(adapter!=null){
				adapter.notifyDataSetChanged();
			}
		}
	}
	
	@Override
	public void setEnable(boolean enable) {
		// TODO Auto-generated method stub
		
	}

	private void init(){
		this.context=getActivity();
		this.mList=new ArrayList<FileEntity>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		init();
		View view = inflater.inflate(R.layout.file_show_list, container, false);
		back = (ImageView) view.findViewById(R.id.file_show_back);
		edit = (TextView) view.findViewById(R.id.detail_edit);
		mListView = (ListView) view.findViewById(R.id.file_show_list);
		adapter = new FileShowAdapter(getActivity(), mList);
		mListView.setAdapter(adapter);
		back.setOnClickListener(new OnClickListener() {				
			@Override
			public void onClick(View v) {
				((HomePageActivity) getActivity()).onBackPressed();
			}
		});
			
		edit.setOnClickListener(new OnClickListener() {
				
			@Override
			public void onClick(View v) {
				((HomePageActivity) getActivity()).selectFileList(mList);
			}
		});
		initiate();
		RealTimeHandler.getInstance().regist(this);
		return view;
	}
	
	
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		RealTimeHandler.getInstance().unRegist(this);
		super.onDestroy();
	}

	public void initiate(){
		SQLHelper helper=SQLHelper.getInstance(context);
		Cursor cursor=helper.select("archive", new String[]{"userarchiveskey", "uid", "teamname", "position", "inteam", "joindate", "exitdate"}, null, null, null);
		while(cursor.moveToNext()){
			FileEntity item=new FileEntity(context, cursor.getString(1), cursor.getInt(0));
			item.setData(cursor.getString(3), cursor.getString(2), cursor.getString(4), cursor.getString(5), cursor.getString(6));
			mList.add(item);
		}
		if(adapter!=null){
			adapter.notifyDataSetChanged();
		}
	}


}
