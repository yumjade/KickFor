package com.example.kickfor;

import java.util.ArrayList;
import java.util.List;

import com.example.kickfor.utils.IdentificationInterface;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class FileFragment extends Fragment implements HomePageInterface,IdentificationInterface {

	private ImageView back;
	private ListView fileShow;
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
		
			View view = inflater.inflate(R.layout.file_list, container, false);
			back = (ImageView) view.findViewById(R.id.file_back);
			edit = (TextView) view.findViewById(R.id.detail_edit);
			fileShow = (ListView) view.findViewById(R.id.file_list);
			init();
			adapter = new FileAdapter(getActivity(), R.layout.file_item, mList);
			fileShow.setAdapter(adapter);

			back.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					((HomePageActivity) getActivity()).onBackPressed();

				}
			});
			
			edit.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					//((HomePageActivity) getActivity()).editListFile();
				}
			});


		return view;
	}

	private void init() {
		mList.clear();
		mList.add(new File("中后卫", BitmapFactory.decodeResource(getResources(),
				R.drawable.team_default), "亚平宁之师", "2015/1/12-至今","百队杯(2009)  季军",null,null,null));
	}

}
