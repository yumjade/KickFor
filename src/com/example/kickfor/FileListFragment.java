package com.example.kickfor;

import java.util.ArrayList;
import java.util.List;

import com.example.kickfor.utils.IdentificationInterface;

import android.graphics.BitmapFactory;
import android.os.Bundle;
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

public class FileListFragment extends Fragment implements HomePageInterface,IdentificationInterface {
	
	private ImageView back;
	private ListView fileListEdit;
	private RelativeLayout add;
	private TextView edit;

	private List<File> mList = new ArrayList<File>();
	private FileAdapter adapter;

	@Override
	public int getFragmentLevel() {
		return IdentificationInterface.SECOND_LEVEL;
	}
	
	
	
	@Override
	public void setEnable(boolean enable) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		init();
		View view = inflater.inflate(R.layout.file_edit_list, container, false);
		back = (ImageView) view.findViewById(R.id.file_back);
		edit = (TextView) view.findViewById(R.id.detail_edit);
		fileListEdit = (ListView) view.findViewById(R.id.file_list);
		add = (RelativeLayout) view.findViewById(R.id.file_add);
		
		init();
		
		adapter = new FileAdapter(getActivity(), R.layout.file_edit_item, mList);
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
				//((HomePageActivity)getActivity()).editFile();
				
			}
		});
		
		add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//((HomePageActivity)getActivity()).editFile();
				
			}
		});
		
		return view;
	}
	
	private void init() {
		mList.clear();
		mList.add(new File("中后卫", BitmapFactory.decodeResource(getResources(),
				R.drawable.team_default), "亚平宁之师", "2012/1/12-至今",null, null,null,null));
	}

	

}
