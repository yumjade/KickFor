package com.example.kickfor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;






import com.example.kickfor.team.MatchReviewEntity;
import com.example.kickfor.utils.IdentificationInterface;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class MyMatchFragment extends Fragment implements HomePageInterface, IdentificationInterface{
	
	private List<MyMatchEntity> mList=new ArrayList<MyMatchEntity>();
	private ListView mListView=null;
	private MyMatchAdapter adapter=null;
	private Context context=null;
	private String teamid=null;
	private String phone=null;
	private int index=0;
	
	@Override
	public int getFragmentLevel() {
		// TODO Auto-generated method stub
		return IdentificationInterface.SECOND_LEVEL;
	}
	
	private void init(){
		context=getActivity();
		Bundle bundle=getArguments();
		teamid=bundle.getString("teamid");
		phone=new PreferenceData(context).getData(new String[]{"phone"}).get("phone").toString();
	}
	
	public int getIndex(){
		return index;
	}
	
	public void setIndex(int index){
		this.index=index;
	}
	
	public void setList(List<MyMatchEntity> list, String teamid){
		System.out.println(teamid);
		System.out.println(this.teamid);
		if(!list.isEmpty()){
			if(this.teamid.equals(teamid)){
				if(mList.size()==0){
					Iterator<MyMatchEntity> iter=list.iterator();
					while(iter.hasNext()){
						mList.add(iter.next());
					}
					index=mList.size();
					MyMatchEntity item=new MyMatchEntity(teamid, -1);
					item.setData(phone, null, String.valueOf(index), "0", "0", null);
					mList.add(item);
					if(adapter!=null){
						adapter.notifyDataSetChanged();
						
					}
				}
				else{
					mList.remove(mList.size()-1);
					Iterator<MyMatchEntity> iter=list.iterator();
					while(iter.hasNext()){
						mList.add(iter.next());
					}
					index=mList.size();
					MyMatchEntity item=new MyMatchEntity(teamid, -1);
					item.setData(phone, null, String.valueOf(index), "0", "0", null);
					mList.add(item);
					if(adapter!=null){
						adapter.notifyDataSetChanged();
						
					}
				}
			}
			else{
				this.teamid=teamid;
				mList.clear();
				Iterator<MyMatchEntity> iter=list.iterator();
				while(iter.hasNext()){
					mList.add(iter.next());
				}
				index=mList.size();
				MyMatchEntity item=new MyMatchEntity(teamid, -1);
				item.setData(phone, null, String.valueOf(index), "0", "0", null);
				mList.add(item);
				if(adapter!=null){
					adapter.notifyDataSetChanged();
				}
			}
		}
		else{
			if(mList.size()>0){
				mList.remove(mList.size()-1);
				index=mList.size();
				MyMatchEntity item=new MyMatchEntity(teamid, -1);
				item.setData(phone, null, String.valueOf(index), "0", "0", null);
				mList.add(item);
				if(adapter!=null){
					adapter.notifyDataSetChanged();
				}
			}
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		init();
		View view=inflater.inflate(R.layout.fragment_normal_list1, container, false);
		mListView=(ListView)view.findViewById(R.id.normal_list_1);
		adapter=new MyMatchAdapter(context, mList);
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener(){


			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				if(position<mList.size()-1){
					MyMatchEntity item=mList.get(position);
					int id=item.getId();
					String againstName=item.getAgainstName();
					String date=item.getDate();
					int goals=item.getGoals();
					int lost=item.getLost();
					String teamName=null;
					SQLHelper helper=SQLHelper.getInstance(context);
					Cursor tmpCursor=helper.select("teams", new String[]{"image", "name"}, "teamid=?", new String[]{item.getTeamId()}, null);
					Bitmap teamImg=null;
					if(tmpCursor.moveToNext()){
						teamName=tmpCursor.getString(1);
						String imgpath1=tmpCursor.getString(0);
						if(!imgpath1.equals("-1")){
							teamImg=BitmapFactory.decodeFile(imgpath1);
						}
						else{
							teamImg=BitmapFactory.decodeResource(getResources(), R.drawable.team_default);
						}
					}
					Map<String, Object> map=new HashMap<String, Object>();
					MatchReviewEntity entity=new MatchReviewEntity(id, date, Tools.bitmapToString(teamImg), teamName, againstName, goals, lost);
					((HomePageActivity)getActivity()).reviewDetail(item.getTeamId(), entity, "-1");
					map.put("request", "get ones match detail");
					map.put("id", id);
					map.put("teamid", item.getTeamId());
					Runnable r=new ClientWrite(Tools.JsonEncode(map));
					new Thread(r).start();
				}
			}
			
		});
		return view;
	}


	
	
	
	
	
	

}
