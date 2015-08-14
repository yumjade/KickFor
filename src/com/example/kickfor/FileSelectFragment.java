package com.example.kickfor;

import java.util.ArrayList;
import java.util.Iterator;
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
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class FileSelectFragment extends Fragment implements HomePageInterface,IdentificationInterface, HandlerListener {
	
	private ImageView back=null;
	private ListView fileListEdit=null;
	private RelativeLayout add=null;
	private Context context=null;

	private List<FileEntity> mList=null;
	private FileSelectAdapter adapter=null;

	@Override
	public int getFragmentLevel() {
		return IdentificationInterface.SECOND_LEVEL;
	}
	
	
	
	@Override
	public void setEnable(boolean enable) {
		// TODO Auto-generated method stub
		
	}


	private void init(){
		System.out.println("beginnnnnnnnnnnnnn");
		this.context=getActivity();
		mList= new ArrayList<FileEntity>();
		SQLHelper helper=SQLHelper.getInstance(context);
		Cursor cursor=helper.select("archive", new String[]{"userarchiveskey", "uid", "teamname", "position", "inteam", "joindate", "exitdate"}, null, null, null);
		while(cursor.moveToNext()){
			FileEntity item=new FileEntity(context, cursor.getString(1), cursor.getInt(0));
			item.setData(cursor.getString(3), cursor.getString(2), cursor.getString(4), cursor.getString(5), cursor.getString(6));
			mList.add(item);
		}
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		init();
		View view = inflater.inflate(R.layout.file_select_list, container, false);
		back = (ImageView) view.findViewById(R.id.file_select_back);
		fileListEdit = (ListView) view.findViewById(R.id.file_select_list);
		add = (RelativeLayout) view.findViewById(R.id.file_add);
		
		
		adapter = new FileSelectAdapter(context, mList);
		fileListEdit.setAdapter(adapter);

		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((HomePageActivity) getActivity()).onBackPressed();

			}
		});
		
		fileListEdit.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				FileEntity item=mList.get(position);
				((HomePageActivity)getActivity()).editFile(item);
				
			}
		});
		
		add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String phone=new PreferenceData(context).getData(new String[]{"phone"}).get("phone").toString();
				FileEntity item=new FileEntity(context, phone, -1);
				((HomePageActivity)getActivity()).editFile(item);
			}
		});
		RealTimeHandler.getInstance().regist(this);
		return view;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		System.out.println("FileSelectFragment destroyed");
		RealTimeHandler.getInstance().unRegist(this);
		super.onDestroy();
	}



	@Override
	public void onChange(Message msg) {
		// TODO Auto-generated method stub
		if(msg.what==HomePageActivity.GET_ARCHIVES){
			System.out.println("ddddddddddddddddddelete");
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
				System.out.println("ddddddddddddddddddelete");
			}
		}
	}
	
	

}
