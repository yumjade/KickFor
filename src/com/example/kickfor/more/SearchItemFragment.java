package com.example.kickfor.more;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.example.kickfor.ClientWrite;
import com.example.kickfor.HomePageActivity;
import com.example.kickfor.HomePageEntity;
import com.example.kickfor.R;
import com.example.kickfor.Tools;
import com.example.kickfor.utils.IdentificationInterface;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class SearchItemFragment extends Fragment implements IdentificationInterface{
	
	private List<SearchItemEntity> mList=new ArrayList<SearchItemEntity>();
	private ListView mListView=null;
	private Context context=null;
	private SearchItemAdapter adapter=null;
	private HomePageEntity entity=null;
	private Bitmap teamImage=null;
	
	@Override
	public int getFragmentLevel() {
		return IdentificationInterface.SECOND_LEVEL;
	}
	
	public SearchItemFragment(Context context){
		this.context=context;
	}
	
	public void changedData(List<SearchItemEntity> mList){
		this.mList.clear();
		Iterator<SearchItemEntity> iter=mList.iterator();
		while(iter.hasNext()){
			SearchItemEntity item=iter.next();
			this.mList.add(item);
		}
		adapter.notifyDataSetChanged();
	}
	
	
	public void setDataDetail(Map<String, Object> map){
		entity.isInDatabase();
		map.get("phone");
		entity.setData(map);
		((HomePageActivity)getActivity()).openOthersHomePage(entity, entity.isInDatabase());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view=inflater.inflate(R.layout.fragment_normal_list, container, false);
		mListView=(ListView)view.findViewById(R.id.normal_list);
		adapter=new SearchItemAdapter(context, mList);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				((HomePageActivity)getActivity()).openVague(HomePageActivity.WAIT_SEARCH_LIST);
				setEnable(false);
				SearchItemEntity item=mList.get(position);
				if(item.getTypeBoolean()==false){
					String phone=item.getPhone();
					entity=new HomePageEntity(context, phone);
					if(entity.isInDatabase()){
						entity.setImage(item.getImage());
						Map<String, Object> map=new HashMap<String, Object>();
						map.put("request", "seek one info");
						map.put("phone", phone);
						Runnable r=new ClientWrite(Tools.JsonEncode(map));
						new Thread(r).start();
					}
					else{
						entity.setImage(item.getImage());
						Map<String, Object> map=new HashMap<String, Object>();
						map.put("request", "seek one info");
						map.put("phone", phone);
						Runnable r=new ClientWrite(Tools.JsonEncode(map));
						new Thread(r).start();
					}
				}
				else{
					String teamid=item.getTeamid();
					Map<String, Object> map=new HashMap<String, Object>();
					map.put("request", "seek one team");
					map.put("teamid", teamid);
					Runnable r=new ClientWrite(Tools.JsonEncode(map));
					new Thread(r).start();
					teamImage=item.getImage();
				}
			}
			
		});
		return view;
	}
	
	public void setEnable(boolean enable){
		mListView.setEnabled(enable);
	}
	
	public Bitmap getTeamImage(){
		return teamImage;
	}

}
