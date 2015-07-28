package com.example.kickfor.team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.example.kickfor.ClientWrite;
import com.example.kickfor.ListsFragment;
import com.example.kickfor.R;
import com.example.kickfor.SQLHelper;
import com.example.kickfor.Tools;
import com.example.kickfor.HomePageActivity;
import com.example.kickfor.utils.IdentificationInterface;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ChangingRoomFragment extends Fragment implements TeamInterface, IdentificationInterface{
	
	private ListView mListView1=null;
	private ListView mListView2=null;
	private ImageView addNew=null;
	private ImageView back=null;
	private TextView cleanChat=null;
	private TextView noBother=null;
	private List<ChangingRoomEntity> mList1=new ArrayList<ChangingRoomEntity>();
	private List<ChangingRoomEntity> mList2=new ArrayList<ChangingRoomEntity>();
	private ChangingRoomAdapter adapter1=null;
	private ChangingRoomAdapter adapter2=null;
	private Context context=null;
	private String teamid=null;
	private int authority=0;
	
	@Override
	public int getFragmentLevel() {
		// TODO Auto-generated method stub
		return IdentificationInterface.SECOND_LEVEL;
	}
	
	public void setEnable(boolean enable){
		back.setEnabled(enable);
		addNew.setEnabled(enable);
		cleanChat.setEnabled(enable);
		noBother.setEnabled(enable);
		for(int i=0; i<mListView1.getChildCount(); i++){
			View view=mListView1.getChildAt(i);
			EditText e1=(EditText)view.findViewById(R.id.manage_number);
			EditText e2=(EditText)view.findViewById(R.id.manage_name);
			TextView t1=(TextView)view.findViewById(R.id.manage_kick);
			TextView t2=(TextView)view.findViewById(R.id.manage_authority);	
			e1.setEnabled(enable);
			e2.setEnabled(enable);
			t1.setEnabled(enable);
			t2.setEnabled(enable);
		}
		mListView1.setEnabled(enable);
		for(int i=0; i<mListView2.getChildCount(); i++){
			View view=mListView2.getChildAt(i);
			EditText e1=(EditText)view.findViewById(R.id.manage_number);
			EditText e2=(EditText)view.findViewById(R.id.manage_name);
			TextView t1=(TextView)view.findViewById(R.id.manage_kick);
			TextView t2=(TextView)view.findViewById(R.id.manage_authority);	
			e1.setEnabled(enable);
			e2.setEnabled(enable);
			t1.setEnabled(enable);
			t2.setEnabled(enable);
		}
		mListView2.setEnabled(enable);
	}
	
	private void init(){
		this.context=getActivity();
		Bundle bundle=getArguments();
		this.teamid=bundle.getString("teamid");
		this.authority=Integer.parseInt(bundle.getString("authority"));
	}
	
	
	public void saveExit(){
		Map<String, Object> whole=new HashMap<String, Object>();
		Map<String, Object> map=new HashMap<String, Object>();
		Iterator<ChangingRoomEntity> iter=mList2.iterator();
		int i=0;
		while(iter.hasNext()){
			map.clear();
			ChangingRoomEntity item=iter.next();
			if(item.isChangedData() && !item.getName().isEmpty()){
				i=i+1;
				map.put("phone", item.getPhone());
				map.put("name", item.getName());
				map.put("number", item.getNumber());
				String str=Tools.MapToJson(map);
				whole.put(String.valueOf(i), str);
			}
		}
		if(!whole.isEmpty()){
			whole.put("request", "update list");
			whole.put("n", String.valueOf(i));
			whole.put("teamid", teamid);
			Runnable r=new ClientWrite(Tools.JsonEncode(whole));
			new Thread(r).start();
		}
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		init();
		View view=inflater.inflate(R.layout.changing_room_manage_list, container, false);
		back=(ImageView)view.findViewById(R.id.manage_back);
		back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((HomePageActivity)getActivity()).onBackPressed();
			}
			
		});
		cleanChat=(TextView)view.findViewById(R.id.manage_clean);
		noBother=(TextView)view.findViewById(R.id.manage_no_bother);
		mListView1=(ListView)view.findViewById(R.id.joinin);
		mListView2=(ListView)view.findViewById(R.id.autority_or_kick);
		addNew=(ImageView)view.findViewById(R.id.manage_add);
		if(authority<4){
			addNew.setVisibility(View.GONE);
		}
		else{
			addNew.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					ChangingRoomEntity entity=new ChangingRoomEntity(teamid);
					entity.setAuthority("0");
					entity.changedData();
					mList2.add(entity);
					adapter2.notifyDataSetChanged();
					Tools.setListViewHeight(mListView2);
					mListView2.setSelection(mListView2.getCount()-1);		
				}
				
			});
		}
		initiate1();
		initiate2();
		adapter1=new ChangingRoomAdapter(context, mList1, ChangingRoomAdapter.JOIN_IN, authority, mListView1);
		adapter2=new ChangingRoomAdapter(context, mList2, ChangingRoomAdapter.MEMBER, authority, mListView2);
		mListView1.setAdapter(adapter1);
		Tools.setListViewHeight(mListView1);
		mListView2.setAdapter(adapter2);
		Tools.setListViewHeight(mListView2);
		
		
		
		
		return view;
	}
	
	public void changedData(String phone, int table, boolean isRemove, String authority){
		if(table==2 && isRemove==true){
			Iterator<ChangingRoomEntity> iter=mList2.iterator();
			while(iter.hasNext()){
				ChangingRoomEntity tmp=iter.next();
				if(tmp.getPhone().equals(phone)){
					mList2.remove(tmp);
					adapter2.notifyDataSetChanged();
					Tools.setListViewHeight(mListView2);
					break;
				}
			}
		}
		else if(table==2 && isRemove==false){
			Iterator<ChangingRoomEntity> iter=mList2.iterator();
			while(iter.hasNext()){
				ChangingRoomEntity tmp=iter.next();
				if(tmp.getPhone().equals(phone) && authority.equals("2")){
					tmp.pb1=false;
					tmp.pb2=false;
					tmp.setAuthority(authority);
					mList2.remove(tmp);
					mList2.add(1, tmp);
					adapter2.notifyDataSetChanged();
					Tools.setListViewHeight(mListView2);
					break;
				}
				else if(tmp.getPhone().equals(phone) && authority.equals("0")){
					int position=mList2.indexOf(tmp);
					tmp.pb1=false;
					tmp.pb2=false;
					int beforeAuthority=Integer.parseInt(tmp.getAuthority());
					tmp.setAuthority(authority);
					mList2.remove(tmp);
					if(beforeAuthority<=0){
						if(position>=mList2.size()){
							mList2.add(mList2.size(), tmp);
							adapter2.notifyDataSetChanged();
						}
						else{
							mList2.add(position, tmp);
							adapter2.notifyDataSetChanged();
						}
					}
					else{
						mList2.add(mList2.size(), tmp);
						adapter2.notifyDataSetChanged();
					}
					Tools.setListViewHeight(mListView2);
					break;
				}
			}
		}
		else if(table==1 && isRemove==false){
			Iterator<ChangingRoomEntity> iter=mList1.iterator();
			while(iter.hasNext()){
				ChangingRoomEntity tmp=iter.next();
				if(tmp.getPhone().equals(phone)){
					mList1.remove(tmp);
					adapter1.notifyDataSetChanged();
					Tools.setListViewHeight(mListView1);
					break;
				}
			}
			SQLHelper helper=SQLHelper.getInstance(context);
			String tableName="f_"+teamid;
			Cursor cursor=helper.select(tableName, new String[]{"phone", "name", "number", "authority"}, "phone=?", new String[]{phone}, "authority desc, name");
			if(cursor.moveToNext()){
				ChangingRoomEntity entity=new ChangingRoomEntity(cursor.getString(0), cursor.getString(2), cursor.getString(1), teamid);
				entity.setAuthority(cursor.getString(3));
				mList2.add(entity);
				adapter2.notifyDataSetChanged();
				Tools.setListViewHeight(mListView2);
			}
		}
		else if(table==1 && isRemove==true){
			Iterator<ChangingRoomEntity> iter=mList1.iterator();
			while(iter.hasNext()){
				ChangingRoomEntity tmp=iter.next();
				if(tmp.getPhone().equals(phone)){
					mList1.remove(tmp);
					adapter1.notifyDataSetChanged();
					Tools.setListViewHeight(mListView1);
					break;
				}
			}
		}
	}
	
	public void initiate1(){
		mList1.clear();
		SQLHelper helper=SQLHelper.getInstance(context);
		Cursor cursor=helper.select("systemtable", new String[]{"id", "name"}, "type=? and teamid=? and result=? and message=?", new String[]{String.valueOf(ListsFragment.TYPE_TEAMS_MESSAGE), teamid, "u", "apply_join"}, "date desc");
		while(cursor.moveToNext()){
			ChangingRoomEntity entity=new ChangingRoomEntity(cursor.getString(0), "", cursor.getString(1)+"…Í«Îº”»Î", teamid);
			mList1.add(entity);
		}
	}
	
	private void initiate2(){
		mList2.clear();
		SQLHelper helper=SQLHelper.getInstance(context);
		String tableName="f_"+teamid;
		Cursor cursor=helper.select(tableName, new String[]{"phone", "name", "number", "authority"}, null, null, "authority desc, name");
		while(cursor.moveToNext()){
			ChangingRoomEntity entity=new ChangingRoomEntity(cursor.getString(0), cursor.getString(2), cursor.getString(1), teamid);
			entity.setAuthority(cursor.getString(3));
			mList2.add(entity);
		}
	}
	
	public void sendData(){
		Map<String, Object> tmp=new HashMap<String, Object>();
		Iterator<ChangingRoomEntity> iter=mList2.iterator();
		while(iter.hasNext()){
			ChangingRoomEntity item=iter.next();
			if(item.isChangedData()){
				tmp.put("request", "update list");
				tmp.put("phone", item.getPhone());
				tmp.put("number", item.getNumber());
				tmp.put("name", item.getName());
				tmp.put("teamid", item.getTeamid());
				Runnable r=new ClientWrite(Tools.JsonEncode(tmp));
				new Thread(r).start();
			}
		}
	}
	
	@Override
	public String getTeamid(){
		return teamid;
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		SQLHelper helper=SQLHelper.getInstance(context);
		Cursor cursor=helper.select("systemtable", new String[]{"i"}, "teamid=? and type=? and (message=? or message=?) ", new String[]{teamid, String.valueOf(ListsFragment.TYPE_TEAMS_MESSAGE), "apply_join", "some_one_join"}, null);
		Map<String, Object> map=new HashMap<String, Object>();
		while(cursor.moveToNext()){
			map.clear();
			map.put("i", cursor.getInt(0));
			map.put("result", "p");
			helper.update(Tools.getContentValuesFromMap(map, null), "systemtable", cursor.getInt(0));
		}
		((HomePageActivity)getActivity()).updateTitleAndBar();
		super.onPause();
	}
	
	
	
	

}
