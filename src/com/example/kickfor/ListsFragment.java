package com.example.kickfor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.example.kickfor.KickForListView.OnDeleteListener;














import com.example.kickfor.utils.IdentificationInterface;
import com.example.kickfor.utils.Sidebar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ListsFragment extends Fragment implements HomePageInterface, IdentificationInterface{
	
	protected static final int TYPE_FRIEND_LIST=0;
	protected static final int TYPE_FRIEND_APPLY_ALL=-1;
	protected static final int TYPE_SYSTEM_MESSAGE_ALL=-2;
	protected static final int TYPE_TEAMS_MESSAGE_ALL=-3;
	protected static final int TYPE_FRIEND_APPLY=1;
	protected static final int TYPE_SYSTEM_MESSAGE=2;
	public static final int TYPE_TEAMS_MESSAGE=3;
	protected static final int TYPE_TEAM1_CHANGINGROOM=4;
	protected static final int TYPE_TEAM2_CHANGINGROOM=5;
	protected static final int TYPE_TEAM3_CHANGINGROOM=6;
	protected static final int TYPE_FRIEND_MESSAGE=7;
	
	protected static final int TYPE_MESSAGE_LIST=8;
		
	private KickForListView mListView=null;
	private List<MyFriend> mList=new ArrayList<MyFriend>();
	private RelativeLayout title=null;
	private ImageView back=null;
	private TextView titleText=null;
	private Context context=null;
	private FriendsAdapter adapter=null;
	private HomePageEntity entity=null;
	private int state=-1;
	
	
	private WindowManager mWindowManager;
	private TextView mDialogText;
	private View head;
	private MyFriend mFriend;
	private Sidebar sidebar;
	private List<MyFriend> friendList;
	private InputMethodManager inputMethodManager;
	private EditText query;
	private ImageButton clearSearch;
	private View searchbar;
	
	@Override
	public int getFragmentLevel() {
		// TODO Auto-generated method stub
		if(state==TYPE_FRIEND_LIST || state==TYPE_MESSAGE_LIST){
			return IdentificationInterface.MAIN_LEVEL;
		}
		else{
			return IdentificationInterface.SECOND_LEVEL;
		}
	}
	
	private void init(){
		Bundle bundle=getArguments();
		context=getActivity();
		this.state=bundle.getInt("state");
	}
	
	public int getState(){
		return state;
	}
	
	public void setData(Map<String, Object> map){
		entity.setData(map);
		((HomePageActivity)getActivity()).openOthersHomePage(entity, entity.isInDatabase());
	}
	
	public void changedData(){
		initiate();
		adapter.notifyDataSetChanged();
	}
	

	private void initiate(){
		mList.clear();
		SQLHelper helper=SQLHelper.getInstance(context);
		switch(state){
		case TYPE_FRIEND_LIST:{
			title.setVisibility(View.GONE);
			Cursor cursor=helper.select("friends", new String[]{"name", "image", "phone"}, "phone<>?", new String[]{"host"}, "name asc");
			Map<String, Object> tmp=new HashMap<String, Object>();
			while(cursor.moveToNext()){
				tmp.clear();
				String img=cursor.getString(1);
				Bitmap photo=null;
				if(!(img.equals("-1")))
					photo=BitmapFactory.decodeFile(img);
				else
					photo=BitmapFactory.decodeResource(getResources(), R.drawable.team_default);
				tmp.put("type", TYPE_FRIEND_LIST);
				tmp.put("name", cursor.getString(0));
				tmp.put("image", photo);
				tmp.put("phone", cursor.getString(2));
				mList.add(new MyFriend(tmp));
			}
			getFriendList();
			break;
		}
		case TYPE_MESSAGE_LIST:{
			title.setVisibility(View.GONE);
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("name", "ϵͳ��Ϣ");
			map.put("image", BitmapFactory.decodeResource(getResources(), R.drawable.system_message));
			map.put("type", String.valueOf(TYPE_SYSTEM_MESSAGE_ALL));
			map.put("msgNumber", Tools.getUnHandleMsgNumber(TYPE_SYSTEM_MESSAGE, context));
			mList.add(new MyFriend(map));
			map.clear();
			map.put("name", "�����Ϣ");
			map.put("image", BitmapFactory.decodeResource(getResources(), R.drawable.team_message));
			map.put("type", String.valueOf(TYPE_TEAMS_MESSAGE_ALL));
			map.put("msgNumber", Tools.getUnHandleMsgNumber(TYPE_TEAMS_MESSAGE, context));
			mList.add(new MyFriend(map));
			map.clear();
			map.put("name", "��������");
			map.put("image", BitmapFactory.decodeResource(getResources(), R.drawable.apply_friend));
			map.put("type", String.valueOf(TYPE_FRIEND_APPLY_ALL));
			map.put("msgNumber", Tools.getUnHandleMsgNumber(TYPE_FRIEND_APPLY, context));
			mList.add(new MyFriend(map));
			Cursor cursor=helper.select("systemtable", new String[]{"type", "name", "id", "date", "image", "message", "i", "result", "teamid"}, "result=?", new String[]{"u"}, "date desc");
			Map<String, Object> tmp=new HashMap<String, Object>();
			while(cursor.moveToNext()){
				tmp.clear();
				int type=Integer.parseInt(cursor.getString(0));
				switch(type){
				case TYPE_FRIEND_MESSAGE:{
					String img=cursor.getString(4);
					Bitmap photo=null;
					tmp.put("type", cursor.getString(0));
					tmp.put("name", cursor.getString(1));
					String phone=cursor.getString(2);
					tmp.put("phone", phone);
					tmp.put("message", cursor.getString(5));
					tmp.put("date", cursor.getString(3));
					tmp.put("index", cursor.getInt(6));
					tmp.put("result", cursor.getString(7));
					tmp.put("msgNumber", EMChatManager.getInstance().getConversation(phone).getUnreadMsgCount());
					
					if(img==null){
						Cursor cursor1=helper.select("friends", new String[]{"image"}, "phone=?", new String[]{phone}, null);
						if(cursor1.moveToNext()){
							img=cursor1.getString(0);
						}
					}
					if(!(img.equals("-1")))
						photo=BitmapFactory.decodeFile(img);
					else
						photo=BitmapFactory.decodeResource(getResources(), R.drawable.team_default);
					tmp.put("image", photo);
					mList.add(new MyFriend(tmp));
					break;
				}
				case TYPE_TEAM1_CHANGINGROOM:{
					tmp.put("type", cursor.getString(0));
					tmp.put("name", cursor.getString(1));
					tmp.put("image", BitmapFactory.decodeResource(getResources(), R.drawable.changingroom_message));
					tmp.put("message", cursor.getString(5));
					tmp.put("phone", cursor.getString(8));
					tmp.put("index", cursor.getInt(6));
					tmp.put("result", cursor.getString(7));
					tmp.put("date", cursor.getString(3));
					PreferenceData pd=new PreferenceData(context);
					String groupid=pd.getData(new String[]{cursor.getString(8)}).get(cursor.getString(8)).toString();
					tmp.put("msgNumber", EMChatManager.getInstance().getConversation(groupid).getUnreadMsgCount());
					mList.add(new MyFriend(tmp));
					break;
				}
				case TYPE_TEAM2_CHANGINGROOM:{
					tmp.put("type", cursor.getString(0));
					tmp.put("name", cursor.getString(1));
					tmp.put("image", BitmapFactory.decodeResource(getResources(), R.drawable.changingroom_message));
					tmp.put("message", cursor.getString(5));
					tmp.put("phone", cursor.getString(8));
					tmp.put("index", cursor.getInt(6));
					tmp.put("result", cursor.getString(7));
					tmp.put("date", cursor.getString(3));
					PreferenceData pd=new PreferenceData(context);
					String groupid=pd.getData(new String[]{cursor.getString(8)}).get(cursor.getString(8)).toString();
					tmp.put("msgNumber", EMChatManager.getInstance().getConversation(groupid).getUnreadMsgCount());
					mList.add(new MyFriend(tmp));
					break;
				}
				case TYPE_TEAM3_CHANGINGROOM:{
					tmp.put("type", cursor.getString(0));
					tmp.put("name", cursor.getString(1));
					tmp.put("image", BitmapFactory.decodeResource(getResources(), R.drawable.changingroom_message));
					tmp.put("message", cursor.getString(5));
					tmp.put("phone", cursor.getString(8));
					tmp.put("index", cursor.getInt(6));
					tmp.put("result", cursor.getString(7));
					tmp.put("date", cursor.getString(3));
					PreferenceData pd=new PreferenceData(context);
					String groupid=pd.getData(new String[]{cursor.getString(8)}).get(cursor.getString(8)).toString();
					tmp.put("msgNumber", EMChatManager.getInstance().getConversation(groupid).getUnreadMsgCount());
					mList.add(new MyFriend(tmp));
					break;
				}
				}
			}
			break;
		}
		case TYPE_FRIEND_APPLY:{
			title.setVisibility(View.VISIBLE);
			back.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					getActivity().onBackPressed();
				}
				
			});
			titleText.setText("��������");
			Cursor cursor=helper.select("systemtable", new String[]{"type", "name", "id", "date", "image", "message", "i", "result"}, "type=?", new String[]{String.valueOf(TYPE_FRIEND_APPLY)}, "date desc");
			Map<String, Object> tmp=new HashMap<String, Object>();
			while(cursor.moveToNext()){
				tmp.clear();
				tmp.put("index", cursor.getInt(6));
				tmp.put("result", cursor.getString(7));
				tmp.put("type", cursor.getString(0));
				tmp.put("name", cursor.getString(1));
				String img=cursor.getString(4);
				Bitmap photo=null;
				if(!(img.equals("-1")))
					photo=BitmapFactory.decodeFile(img);
				else
					photo=BitmapFactory.decodeResource(getResources(), R.drawable.team_default);
				tmp.put("image", photo);
				tmp.put("phone", cursor.getString(2));
				tmp.put("message", "�������");
				mList.add(new MyFriend(tmp));
			}
			break;
		}
		case TYPE_SYSTEM_MESSAGE:{
			title.setVisibility(View.VISIBLE);
			back.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					getActivity().onBackPressed();
				}
				
			});
			titleText.setText("ϵͳ��Ϣ");
			Cursor cursor=helper.select("systemtable", new String[]{"type", "name", "id", "date", "image", "message", "i", "result"}, "type=?", new String[]{String.valueOf(TYPE_SYSTEM_MESSAGE)}, "date desc");
			Map<String, Object> tmp=new HashMap<String, Object>();
			while(cursor.moveToNext()){
				tmp.clear();
				tmp.put("index", cursor.getInt(6));
				tmp.put("result", cursor.getString(7));
				tmp.put("type", cursor.getString(0));
				tmp.put("name", cursor.getString(1));
				tmp.put("image", BitmapFactory.decodeResource(getResources(), R.drawable.system_message));
				tmp.put("phone", cursor.getString(2));
				tmp.put("message", cursor.getString(3));
				mList.add(new MyFriend(tmp));
			}
			break;
		}
		case TYPE_TEAMS_MESSAGE:{
			title.setVisibility(View.VISIBLE);
			back.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					getActivity().onBackPressed();
				}
				
			});
			titleText.setText("�����Ϣ");
			Cursor cursor=helper.select("systemtable", new String[]{"type", "name", "id", "date", "image", "message", "i", "result", "teamid"}, "type=?", new String[]{String.valueOf(TYPE_TEAMS_MESSAGE)}, "result desc");
			Map<String, Object> tmp=new HashMap<String, Object>();
			while(cursor.moveToNext()){
				tmp.clear();
				System.out.println(cursor.getString(5));
				String teamid=cursor.getString(8);
				String teamName="";
				Cursor cursor1=helper.select("teams", new String[]{"name"}, "teamid=?", new String[]{teamid}, null);
				if(cursor1.moveToNext()){
					teamName=cursor1.getString(0);
				}
				tmp.put("teamid", teamid);
				tmp.put("index", cursor.getInt(6));
				tmp.put("result", cursor.getString(7));
				tmp.put("type", cursor.getString(0));
				tmp.put("name", cursor.getString(1));
				tmp.put("image", BitmapFactory.decodeResource(getResources(), R.drawable.team_message));
				tmp.put("phone", cursor.getString(2));
				String messageType=cursor.getString(5);
				String message="";
				switch(messageType){
				case "publish_new_match":
					message=teamName+"������һ������Ԥ��";
					break;
				case "update_new_match":
					message=teamName+"�޸���һ������Ԥ��";
					break;
				case "update_review":
					message=teamName+"������һ�������ع�";
					break;
				case "apply_join":
					message=cursor.getString(1)+"�������"+teamName;
					tmp.put("name", "�����Ϣ");
					break;
				case "some_one_join":
					String table="f_"+teamid;
					Cursor cursor2=helper.select(table, new String[]{"name"}, "phone=?", new String[]{cursor.getString(2)}, null);
					if(cursor2.moveToNext()){
						message=cursor2.getString(0)+"������"+teamName;
					}
					break;
				case "some_one_left":
					message=cursor.getString(1)+"�뿪��"+teamName;
					tmp.put("name", "�����Ϣ");
					break;
				case "delete_new_match":
					message=tmp.get("name").toString();
					tmp.put("name", "�����Ϣ");
					break;
				case "join_new_match":
					message=tmp.get("name").toString();
					tmp.put("name", "�����Ϣ");
					break;
				case "accept_join":
					message="���ѳɹ�����"+teamName;
					break;
				}
				
				tmp.put("message", message);
				tmp.put("msgNumber", cursor.getString(7).equals("u")? 1:0);
				MyFriend item=new MyFriend(tmp);
				item.setMessageType(messageType);
				mList.add(item);
			}
			break;
		}
		}	
	}

	

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		init();
		System.out.println("state=="+state);
		View view=null;
		if(state==TYPE_FRIEND_LIST){
			view=inflater.inflate(R.layout.fragment_list_sidebar, container,false);
			inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
			adapter=new FriendsAdapter(context, R.layout.vlist_side_bar, mList);
			query = (EditText) view.findViewById(R.id.query);
			clearSearch = (ImageButton) view.findViewById(R.id.search_clear);
			searchbar=view.findViewById(R.id.search_bar_view);
			sidebar=(Sidebar)view.findViewById(R.id.sidebar);
			mListView=(KickForListView)view.findViewById(R.id.kick_for_list);
			sidebar.setListView(mListView);
		}
		else{
			view=inflater.inflate(R.layout.fragment_list, container,false);
			mListView=(KickForListView)view.findViewById(R.id.kick_for_list);
			adapter=new FriendsAdapter(context, R.layout.vlist, mList);
		}
		title=(RelativeLayout)view.findViewById(R.id.list_title);
		back=(ImageView)view.findViewById(R.id.list_back);
		titleText=(TextView)view.findViewById(R.id.list_text);
		
		initiate();
		adapter.notifyDataSetChanged();
		mListView.setAdapter(adapter);
		mListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position,
					long arg3) {
				// TODO Auto-generated method stub
				MyFriend item=mList.get(position);
				switch(state){
				case TYPE_FRIEND_LIST:{
					String phone=item.getPhone();
					entity=new HomePageEntity(context, phone);
					entity.setImage(item.getImage());
					Map<String, Object> map=new HashMap<String, Object>();
					map.put("request", "seek one info");
					map.put("phone", phone);
					System.out.println(phone);
					((HomePageActivity)getActivity()).openVague(HomePageActivity.WAIT_LIST_FRIENDS);
					setEnable(false);
					Runnable r=new ClientWrite(Tools.JsonEncode(map));
					new Thread(r).start();
					break;
				}
				case TYPE_MESSAGE_LIST:{
					((HomePageActivity)getActivity()).selectList(item);
					break;
				}
				case TYPE_TEAMS_MESSAGE:{
					switch(item.getMessageType()){
					case "publish_new_match":{
						((HomePageActivity)getActivity()).hideMain();
						((HomePageActivity)getActivity()).showTitle();
						((HomePageActivity)getActivity()).openPreview(item.getTeamid());
						SQLHelper helper=SQLHelper.getInstance(context);
						Map<String, Object> map=new HashMap<String, Object>();
						map.put("result", "p");
						helper.update(Tools.getContentValuesFromMap(map, null), "systemtable", item.getIndex());
						item.setMsgNumber(0);
						int pos=mList.indexOf(item);
						mList.remove(pos);
						mList.add(pos, item);
						adapter.notifyDataSetChanged();
						break;
					}
					case "update_new_match":{
						((HomePageActivity)getActivity()).hideMain();
						((HomePageActivity)getActivity()).showTitle();
						((HomePageActivity)getActivity()).openPreview(item.getTeamid());
						SQLHelper helper=SQLHelper.getInstance(context);
						Map<String, Object> map=new HashMap<String, Object>();
						map.put("result", "p");
						helper.update(Tools.getContentValuesFromMap(map, null), "systemtable", item.getIndex());
						item.setMsgNumber(0);
						int pos=mList.indexOf(item);
						mList.remove(pos);
						mList.add(pos, item);
						adapter.notifyDataSetChanged();
						break;
					}
					case "update_review":{
						String id=item.getPhone();
						Map<String, Object> map=new HashMap<String, Object>();
						map.put("request", "get match by id");
						map.put("id", id);
						map.put("teamid", item.getTeamid());
						Runnable r=new ClientWrite(Tools.JsonEncode(map));
						new Thread(r).start();
						SQLHelper helper=SQLHelper.getInstance(context);
						Map<String, Object> tmp=new HashMap<String, Object>();
						tmp.put("result", "p");
						helper.update(Tools.getContentValuesFromMap(tmp, null), "systemtable", item.getIndex());
						item.setMsgNumber(0);
						int pos=mList.indexOf(item);
						mList.remove(pos);
						mList.add(pos, item);
						adapter.notifyDataSetChanged();
						break;
					}
					case "join_new_match":{
						//ȥ���˱���Ԥ��
						((HomePageActivity)getActivity()).hideMain();
						((HomePageActivity)getActivity()).showTitle();
						((HomePageActivity)getActivity()).openPreview(item.getTeamid());
						SQLHelper helper=SQLHelper.getInstance(context);
						Map<String, Object> map=new HashMap<String, Object>();
						map.put("result", "p");
						helper.update(Tools.getContentValuesFromMap(map, null), "systemtable", item.getIndex());
						item.setMsgNumber(0);
						int pos=mList.indexOf(item);
						mList.remove(pos);
						mList.add(pos, item);
						adapter.notifyDataSetChanged();
						break;
					}
					case "apply_join":{
						//ȥ������
						String teamid=item.getTeamid();
						String index=Tools.getIndex(context, teamid, "host");
						SQLHelper helper=SQLHelper.getInstance(context);
						Cursor cursor=helper.select("ich", new String[]{"authority"+index}, "phone=?", new String[]{"host"}, null);
						if(cursor.moveToNext()){
							String authority=cursor.getString(0);
							((HomePageActivity)getActivity()).openChangingRoomManager(teamid, authority);
						}
						initiate();
						break;
					}
					case "some_one_join":{
						//ȥ������
						String teamid=item.getTeamid();
						String index=Tools.getIndex(context, teamid, "host");
						SQLHelper helper=SQLHelper.getInstance(context);
						Cursor cursor=helper.select("ich", new String[]{"authority"+index}, "phone=?", new String[]{"host"}, null);
						if(cursor.moveToNext()){
							String authority=cursor.getString(0);
							((HomePageActivity)getActivity()).openChangingRoomManager(teamid, authority);
							Map<String, Object> map=new HashMap<String, Object>();
							map.put("result", "p");
							helper.update(Tools.getContentValuesFromMap(map, null), "systemtable", item.getIndex());
						}
						item.setMsgNumber(0);
						int pos=mList.indexOf(item);
						mList.remove(pos);
						mList.add(pos, item);
						adapter.notifyDataSetChanged();
						break;
					}
					case "accept_join":{
						//ȥ�����ҳ
						((HomePageActivity)getActivity()).openTeams(item.getTeamid());
						SQLHelper helper=SQLHelper.getInstance(context);
						Map<String, Object> map=new HashMap<String, Object>();
						map.put("result", "p");
						helper.update(Tools.getContentValuesFromMap(map, null), "systemtable", item.getIndex());
						item.setMsgNumber(0);
						int pos=mList.indexOf(item);
						mList.remove(pos);
						mList.add(pos, item);
						adapter.notifyDataSetChanged();
						break;
					}
					}
					break;
				}
			}
			}
			
		});
		
		
		mListView.setOnDeleteListener(new OnDeleteListener(){

			@Override
			public void onDelete(int index) {
				// TODO Auto-generated method stub
				//contentList.remove(index);
				SQLHelper helper=SQLHelper.getInstance(context);
				switch(state){
				case TYPE_FRIEND_LIST:{
					MyFriend item=mList.get(index);
					helper.delete("friends", "phone=?", new String[]{item.getPhone()});
					Map<String, Object> tmp=new HashMap<String, Object>();
					tmp.put("request", "delete friend");
					tmp.put("phone", item.getPhone());
					Runnable r=new ClientWrite(Tools.JsonEncode(tmp));
					new Thread(r).start();
					mList.remove(index);
					adapter.notifyDataSetChanged();
					break;
				}
				case TYPE_MESSAGE_LIST:{
					MyFriend item=mList.get(index);
					if(item.getType().equals(ListsFragment.TYPE_FRIEND_MESSAGE)){
						ContentValues cv=new ContentValues();
						cv.put("result", "p");
						helper.update(cv, "systemtable", item.getIndex());
						mList.remove(index);
						adapter.notifyDataSetChanged();
					}
					break;
				}
				case TYPE_FRIEND_APPLY:{
					MyFriend item=mList.get(index);
					helper.delete("systemtable", "i=?", new String[]{String.valueOf(item.getIndex())});
					mList.remove(index);
					adapter.notifyDataSetChanged();
					break;
				}
				case TYPE_SYSTEM_MESSAGE:{
					MyFriend item=mList.get(index);
					helper.delete("systemtable", "i=?", new String[]{String.valueOf(item.getIndex())});
					mList.remove(index);
					adapter.notifyDataSetChanged();
					break;
				}
				case TYPE_TEAMS_MESSAGE:{
					MyFriend item=mList.get(index);
					helper.delete("systemtable", "i=?", new String[]{String.valueOf(item.getIndex())});
					mList.remove(index);
					adapter.notifyDataSetChanged();
					break;
				}
				}
				
			}
			
		});
		
		if(state==TYPE_FRIEND_LIST){
			query.setHint(R.string.search);
			query.addTextChangedListener(new TextWatcher() {
				public void onTextChanged(CharSequence s, int start, int before, int count) {
					adapter.getFilter().filter(s);
					mListView.setAdapter(adapter);
					if (s.length() > 0) {
						clearSearch.setVisibility(View.VISIBLE);
					} else {
						clearSearch.setVisibility(View.INVISIBLE);
						
					}
				}

				public void beforeTextChanged(CharSequence s, int start, int count, int after) {
				}

				public void afterTextChanged(Editable s) {
				}
			});
			clearSearch.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					query.getText().clear();
					hideSoftKeyboard();
				}
			});
		}
		
		
		return view;
	}
	
	public void setEnable(boolean enable){
		mListView.setEnabled(enable);
	}
	
	/**
	 * ��ȡ��ϵ���б�������
	 */
	private void getFriendList() {
		// ����
		Collections.sort(mList, new Comparator<MyFriend>() {

			@Override
			public int compare(MyFriend lhs, MyFriend rhs) {
				
				return lhs.getHeader().compareTo(rhs.getHeader());
			}
		});
		
	}
	
	private void hideSoftKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		if(state==TYPE_TEAMS_MESSAGE){
			SQLHelper helper=SQLHelper.getInstance(context);
			Map<String, Object> map=new HashMap<String, Object>();
			Iterator<MyFriend> iter=mList.iterator();
			while(iter.hasNext()){
				MyFriend item=iter.next();
				if(item.getMsgNumber()==1){
					if(item.getMessageType().equals("accept_join") || item.getMessageType().equals("some_one_left") || item.getMessageType().equals("delete_new_match")){
						map.clear();
						map.put("result", "p");
						helper.update(Tools.getContentValuesFromMap(map, null), "systemtable", item.getIndex());
					}
				}
			}
		}
		super.onPause();
	}
	
	
	
}