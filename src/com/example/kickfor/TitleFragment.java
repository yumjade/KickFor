package com.example.kickfor;

import java.util.HashMap;
import java.util.Map;































import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;
import com.example.kickfor.utils.IdentificationInterface;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TitleFragment extends Fragment implements IdentificationInterface{
	
	protected static final int SEARCH_TITLE=1;
	protected static final int HOMEPAGE_TITLE=5;
	protected static final int PREVIEW_MATCH=7;
	protected static final int TEAM_INFO_TITLE=9;
	protected static final int KICKFOR_LIFE_TITLE=10;
	protected static final int LOBBY_TITLE=11;
	
	private int state=0;
	
	@Override
	public void setEnable(boolean enable) {
		if(state==HOMEPAGE_TITLE){
			myHomepageButton.setEnabled(enable);
			myMessage.setEnabled(enable);
			myFriend.setEnabled(enable);
		}
	}
	
	@Override
	public int getFragmentLevel() {
		if(state==HOMEPAGE_TITLE || state==LOBBY_TITLE){
			return IdentificationInterface.MAIN_LEVEL;
		}
		else{
			return IdentificationInterface.SECOND_LEVEL;
		}
	}
	
	private EditText search=null;
	private ImageView backSearch=null;
	//TEAM_TITLE
	private TextView manageButton=null;
	private String teamid=null;
	private String authority=null;
	//HOMEPAGE_TITLE
	private LinearLayout group=null;
	private TextView myHomepageButton=null;
	private TextView myMessage=null;
	private FrameLayout rMyMessage=null;
	private TextView myFriend=null;
	private TextView msgHint=null;
	//PREVIEW_TITLE
	private ImageView previewBack=null;
	private ImageView previewNext=null;
	private boolean noNext=false;
	private int status=0;
	//TEAM_INFO_TITLE
	private ImageView back=null;
	private TextView nameTitle=null;
	private ImageView image=null;
	private TextView name=null;
	private TextView city=null;
	private TextView district=null;
	private TextView year=null;
	private TextView number=null;
	private TextView memberlist=null;
	private TextView history=null;
	private Context context=null;
	//KICKFOR_LIFE_TITLE
	private ImageView myImage=null;
	private ImageView kickForBack=null;
	private TextView myName=null;
	private TextView matchNumber=null;
	private TextView goal=null;
	private TextView assist=null;
	private TextView theWhole=null;
	private TextView value1=null;
	private TextView value2=null;
	private TextView value3=null;
	private TextView tabMemberList = null;
	private TextView tabHistory = null;
	//LOBBY_TITLE
	private FrameLayout friend=null;
	private FrameLayout team=null;
	private ImageView lsrc1=null;
	private ImageView lsrc2=null;
	private TextView friendNumber=null;
	private TextView teamNumber=null;
	private RelativeLayout addMore=null;
	private TextView friendText=null;
	private TextView teamText=null;
	
	
	private ImageView src1=null;
	private ImageView src2=null;
	private ImageView src3=null;
	private TextView checkNetWork=null;
	
	private boolean canAdd=false;
	
	private void init(){
		this.context=getActivity();
		Bundle bundle=getArguments();
		this.state=bundle.getInt("state");
		if(bundle.containsKey("teamid")){
			this.teamid=bundle.getString("teamid");
		}
		if(bundle.containsKey("authority")){
			this.authority=bundle.getString("authority");
		}
		if(bundle.containsKey("status")){
			this.status=bundle.getInt("status");
		}
		if(bundle.containsKey("nonext")){
			this.noNext=bundle.getBoolean("nonext");
		}
		if(bundle.containsKey("canAdd")){
			this.canAdd=bundle.getBoolean("canAdd");
		}
	}
	
	
	
	public void setNetWorkCheckOpen(boolean open){
		if(state==HOMEPAGE_TITLE && checkNetWork!=null){
			if(open==true){
				checkNetWork.setVisibility(View.VISIBLE);
			}
			else{	
				checkNetWork.setVisibility(View.GONE);
			}
		}
	}
	
	
	public String getTitleText(){
		return manageButton.getText().toString();
	}
	

	
	
	public int getState(){
		return state;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		init();
		View view=null;
		switch(state){
		case SEARCH_TITLE:{
			view=inflater.inflate(R.layout.fragment_title_search, container, false);
			search=(EditText)view.findViewById(R.id.et_search);
			backSearch=(ImageView)view.findViewById(R.id.search_back);
			backSearch.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					((HomePageActivity)getActivity()).onBackPressed();
				}
				
			});
			search.addTextChangedListener(new TextWatcher(){

				@Override
				public void afterTextChanged(Editable arg0) {
					Map<String, Object> tmp=new HashMap<String, Object>();
					if(((HomePageActivity)getActivity()).fromViewPager==false){
						tmp.put("request", "seek");
						tmp.put("info", search.getText().toString());
						Runnable r=new ClientWrite(Tools.JsonEncode(tmp));
						new Thread(r).start();
					}
					else{
						tmp.put("request", "seek team");
						tmp.put("info", search.getText().toString());
						Runnable r=new ClientWrite(Tools.JsonEncode(tmp));
						new Thread(r).start();
					}
				}

				@Override
				public void beforeTextChanged(CharSequence arg0, int arg1,
						int arg2, int arg3) {
					
				}

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					
				}
				
			});
			break;
		}
		
		case HOMEPAGE_TITLE:{
			view=inflater.inflate(R.layout.fragment_homepage_title,container,false);
			
			checkNetWork=(TextView)view.findViewById(R.id.title_network);
			if(Tools.isConnect(getActivity())){
				checkNetWork.setVisibility(View.GONE);
			}
			else{
				checkNetWork.setVisibility(View.VISIBLE);
			}
			checkNetWork.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					if(Tools.isConnect(getActivity())){
						checkNetWork.setVisibility(View.GONE);
					}
					else{
						((HomePageActivity)getActivity()).openCheckNetwork();
					}
				}
				
			});
			
			msgHint=(TextView)view.findViewById(R.id.msg_number_all);
			src1=(ImageView)view.findViewById(R.id.src_1);
			src2=(ImageView)view.findViewById(R.id.src_2);
			src3=(ImageView)view.findViewById(R.id.src_3);
			group=(LinearLayout)view.findViewById(R.id.linearLayout01);
			myHomepageButton=(TextView)view.findViewById(R.id.btn_myhomepage);
			rMyMessage=(FrameLayout)view.findViewById(R.id.relative_myMessage);
			myMessage=(TextView)view.findViewById(R.id.btn_myMessage);
			myFriend=(TextView)view.findViewById(R.id.btn_myFriends);
			myHomepageButton.setTextColor(Color.parseColor("#ffffff"));
			myMessage.setTextColor(Color.parseColor("#9e9e9e"));
			myFriend.setTextColor(Color.parseColor("#9e9e9e"));
			src1.setVisibility(View.VISIBLE);
			src2.setVisibility(View.INVISIBLE);
			src3.setVisibility(View.INVISIBLE);
			msgHint.setVisibility(View.GONE);
			int msgNumber=setMsgNumberChanged();
			remind(msgNumber);
			myHomepageButton.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View arg0) {
					myHomepageButton.setTextColor(Color.parseColor("#ffffff"));
					myMessage.setTextColor(Color.parseColor("#9e9e9e"));
					myFriend.setTextColor(Color.parseColor("#9e9e9e"));
					src1.setVisibility(View.VISIBLE);
					src2.setVisibility(View.INVISIBLE);
					src3.setVisibility(View.INVISIBLE);
					((HomePageActivity)getActivity()).titleCommand(myHomepageButton, null, null);
				}
				
			});
			
			myMessage.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					myHomepageButton.setTextColor(Color.parseColor("#9e9e9e"));
					myMessage.setTextColor(Color.parseColor("#ffffff"));
					myFriend.setTextColor(Color.parseColor("#9e9e9e"));
					src1.setVisibility(View.INVISIBLE);
					src2.setVisibility(View.VISIBLE);
					src3.setVisibility(View.INVISIBLE);
					((HomePageActivity)getActivity()).titleCommand(myMessage, null, null);
				}
				
			});
			
			myFriend.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					myHomepageButton.setTextColor(Color.parseColor("#9e9e9e"));
					myMessage.setTextColor(Color.parseColor("#9e9e9e"));
					myFriend.setTextColor(Color.parseColor("#ffffff"));
					src1.setVisibility(View.INVISIBLE);
					src2.setVisibility(View.INVISIBLE);
					src3.setVisibility(View.VISIBLE);
					((HomePageActivity)getActivity()).titleCommand(myFriend, null, null);
				}
				
			});
			
			
			break;
		}
		case LOBBY_TITLE:{
			view=inflater.inflate(R.layout.fragment_lobby_title, container, false);
//			friend=(FrameLayout)view.findViewById(R.id.lobby_myfriend);
//			friendText=(TextView)view.findViewById(R.id.lobby_friend_text);
//			team=(FrameLayout)view.findViewById(R.id.lobby_team);
			teamText=(TextView)view.findViewById(R.id.lobby_team_text);
//			lsrc1=(ImageView)view.findViewById(R.id.lobby_src_1);
//			lsrc2=(ImageView)view.findViewById(R.id.lobby_src_2);
//			friendNumber=(TextView)view.findViewById(R.id.lobby_friend_msg);
//			friendNumber.setVisibility(View.GONE);
//			teamNumber=(TextView)view.findViewById(R.id.lobby_team_msg);
//			teamNumber.setVisibility(View.GONE);
			checkNetWork=(TextView)view.findViewById(R.id.lobby_title_network);
			addMore=(RelativeLayout) view.findViewById(R.id.rl_publish);
			if(Tools.isConnect(getActivity())){
				checkNetWork.setVisibility(View.GONE);
			}
			else{
				checkNetWork.setVisibility(View.VISIBLE);
			}
			checkNetWork.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					if(Tools.isConnect(getActivity())){
						checkNetWork.setVisibility(View.GONE);
					}
					else{
						((HomePageActivity)getActivity()).openCheckNetwork();
					}
				}
				
			});
//			friendText.setOnClickListener(new OnClickListener(){
//
//				@Override
//				public void onClick(View v) {
//					friendText.setTextColor(Color.parseColor("#ffffff"));
//					teamText.setTextColor(Color.parseColor("#9e9e9e"));
//					lsrc1.setVisibility(View.VISIBLE);
//					lsrc2.setVisibility(View.INVISIBLE);
//					((HomePageActivity)getActivity()).titleCommand(friendText, null, null);
//					addMore.setVisibility(View.GONE);
//				}
//				
//			});
//			teamText.setOnClickListener(new OnClickListener(){
//
//				@Override
//				public void onClick(View v) {
//					friendText.setTextColor(Color.parseColor("#9e9e9e"));
//					teamText.setTextColor(Color.parseColor("#ffffff"));
//					lsrc1.setVisibility(View.INVISIBLE);
//					lsrc2.setVisibility(View.VISIBLE);
//					((HomePageActivity)getActivity()).titleCommand(teamText, null, null);
//					addMore.setVisibility(View.VISIBLE);
//				}
//				
//			});
			if(canAdd==true){
				addMore.setVisibility(View.VISIBLE);
				addMore.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						((HomePageActivity)getActivity()).titleCommand(addMore, null, null);
					}
					
				});
			}
			else{
				addMore.setVisibility(View.GONE);
			}
			break;
		}
		case PREVIEW_MATCH:{
			view=inflater.inflate(R.layout.fragment_preview_title, container, false);
			previewBack=(ImageView)view.findViewById(R.id.preview_left);
			previewNext=(ImageView)view.findViewById(R.id.preview_right);
			if(noNext==true){
				previewNext.setVisibility(View.GONE);
			}
			if(status==3){
				previewNext.setVisibility(View.GONE);
			}
			else{
				previewNext.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						((HomePageActivity)getActivity()).previewCommand("right", status+1);
					}
					
				});
			}
			previewBack.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					((HomePageActivity)getActivity()).previewCommand("left", status-1);
				}
				
			});
			break;
		}
		case TEAM_INFO_TITLE:{
			view=inflater.inflate(R.layout.fragment_team_info, container, false);
			back=(ImageView)view.findViewById(R.id.team_info_back);
			nameTitle=(TextView)view.findViewById(R.id.team_info_text);
			image=(ImageView)view.findViewById(R.id.team_info_image);
			name=(TextView)view.findViewById(R.id.team_info_name);
			city=(TextView)view.findViewById(R.id.team_info_city);
			district=(TextView)view.findViewById(R.id.team_info_district);
			year=(TextView)view.findViewById(R.id.team_info_year);
			number=(TextView)view.findViewById(R.id.team_info_number);
			memberlist=(TextView)view.findViewById(R.id.team_info_member);
			history=(TextView)view.findViewById(R.id.team_info_grade);
			memberlist.setTextColor(getResources().getColor(R.color.tab_pressed));
			history.setTextColor(getResources().getColor(R.color.tab_normal));
			tabMemberList = (TextView) view.findViewById(R.id.tab_team_info_member);
			tabHistory = (TextView) view.findViewById(R.id.tab_team_info_grade);
			tabMemberList.setBackgroundColor(getResources().getColor(R.color.tab_pressed));
			tabHistory.setBackgroundColor(getResources().getColor(R.color.tab_normal));
			tabMemberList.setVisibility(View.VISIBLE);
			tabHistory.setVisibility(View.INVISIBLE);
			SQLHelper helper=SQLHelper.getInstance(context);
			Cursor cursor=helper.select("teams", new String[]{"name", "city", "district", "year", "number", "image"}, "teamid=?", new String[]{teamid}, null);
			if(cursor.moveToNext()){
				nameTitle.setText(cursor.getString(0));
				name.setText(cursor.getString(0));
				city.setText(cursor.getString(1));
				district.setText(cursor.getString(2));
				year.setText(cursor.getString(3));
				number.setText(cursor.getString(4));
				String imgpath=cursor.getString(5);
				if(!imgpath.equals("-1")){
					Bitmap bitmap=BitmapFactory.decodeFile(imgpath);
					image.setImageBitmap(bitmap);
				}	
			}
			image.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					((HomePageActivity)getActivity()).openTeamInfoEdit(teamid, authority);
				}
				
			});
			back.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					((HomePageActivity)getActivity()).onBackPressed();
				}
				
			});
			memberlist.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					tabMemberList.setVisibility(View.VISIBLE);
					tabHistory.setVisibility(View.INVISIBLE);
					memberlist.setTextColor(getResources().getColor(R.color.tab_pressed));
					history.setTextColor(getResources().getColor(R.color.tab_normal));
					tabMemberList.setBackgroundColor(getResources().getColor(R.color.tab_pressed));
					tabHistory.setBackgroundColor(getResources().getColor(R.color.tab_normal));
					((HomePageActivity)getActivity()).titleCommand(v, teamid, null);
					
				}
				
			});
			history.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					tabMemberList.setVisibility(View.INVISIBLE);
					tabHistory.setVisibility(View.VISIBLE);
					memberlist.setTextColor(getResources().getColor(R.color.tab_normal));
					history.setTextColor(getResources().getColor(R.color.tab_pressed));
					tabMemberList.setBackgroundColor(getResources().getColor(R.color.tab_normal));
					tabHistory.setBackgroundColor(getResources().getColor(R.color.tab_pressed));
					((HomePageActivity)getActivity()).titleCommand(v, teamid, authority);
				}
				
			});
			break;
		}
		case KICKFOR_LIFE_TITLE:{
			view=inflater.inflate(R.layout.fragment_kickfor_life, container, false);
			kickForBack=(ImageView)view.findViewById(R.id.kickfor_back);
			myImage=(ImageView)view.findViewById(R.id.kickfor_image);
			myName=(TextView)view.findViewById(R.id.kickfor_name);
			matchNumber=(TextView)view.findViewById(R.id.kickfor_match_number);
			goal=(TextView)view.findViewById(R.id.kickfor_goal);
			assist=(TextView)view.findViewById(R.id.kickfor_assist);
			theWhole=(TextView)view.findViewById(R.id.kickfor_all);
			value1=(TextView)view.findViewById(R.id.kickfor_1);
			value2=(TextView)view.findViewById(R.id.kickfor_2);
			value3=(TextView)view.findViewById(R.id.kickfor_3);
			theWhole.setBackgroundColor(Color.parseColor("#1d8700"));
			value1.setBackgroundColor(Color.parseColor("#22a100"));
			value2.setBackgroundColor(Color.parseColor("#22a100"));
			value3.setBackgroundColor(Color.parseColor("#22a100"));
			SQLHelper helper=SQLHelper.getInstance(context);
			Cursor cursor=helper.select("ich", new String[]{"name", "image", "team1", "team2", "team3", "goal1", "goal2", "goal3", "assist1", "assist2", "assist3", "tmatch1", "tmatch2", "tmatch3"}, "phone=?", new String[]{"host"}, null);
			cursor.moveToNext();
			myName.setText(cursor.getString(0).isEmpty()? "Unknown": cursor.getString(0));
			String img=cursor.getString(1);
			if(!img.equals("-1")){
				Bitmap bitmap=BitmapFactory.decodeFile(img);
				myImage.setImageBitmap(bitmap);
			}
			int goal1=Integer.parseInt(cursor.getString(5));
			int goal2=Integer.parseInt(cursor.getString(6));
			int goal3=Integer.parseInt(cursor.getString(7));
			int assist1=Integer.parseInt(cursor.getString(8));
			int assist2=Integer.parseInt(cursor.getString(9));
			int assist3=Integer.parseInt(cursor.getString(10));
			int tmatch1=Integer.parseInt(cursor.getString(11));
			int tmatch2=Integer.parseInt(cursor.getString(12));
			int tmatch3=Integer.parseInt(cursor.getString(13));
			goal.setText(String.valueOf("进球\n" + (goal1+goal2+goal3) +" 粒"));
			assist.setText(String.valueOf("助攻\n" + (assist1+assist2+assist3) + " 次"));
			matchNumber.setText("踢否生涯比赛："+(tmatch1+tmatch2+tmatch3)+" 场");
			theWhole.setText("全部\n("+(tmatch1+tmatch2+tmatch3)+")");
			theWhole.setEnabled(false);
			final String team1=cursor.getString(2);
			final String team2=cursor.getString(3);
			final String team3=cursor.getString(4);
			if(team1.isEmpty()){
				value1.setVisibility(View.GONE);
			}
			else{
				String str=null;
				Cursor cursor1=helper.select("teams", new String[]{"name"}, "teamid=?", new String[]{team1}, null);
				if(cursor1.moveToNext()){
					str=cursor1.getString(0);
				}
				value1.setVisibility(View.VISIBLE);
				value1.setText(str+"\n("+tmatch1+")");
				value1.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						theWhole.setEnabled(true);
						value1.setEnabled(false);
						value2.setEnabled(true);
						value3.setEnabled(true);
						value1.setBackgroundColor(Color.parseColor("#1d8700"));
						theWhole.setBackgroundColor(Color.parseColor("#22a100"));
						value2.setBackgroundColor(Color.parseColor("#22a100"));
						value3.setBackgroundColor(Color.parseColor("#22a100"));
						((HomePageActivity)getActivity()).titleCommand(v, team1, null);
					}
					
				});
			}
			if(team2.isEmpty()){
				value2.setVisibility(View.GONE);
			}
			else{
				String str=null;
				Cursor cursor1=helper.select("teams", new String[]{"name"}, "teamid=?", new String[]{team2}, null);
				if(cursor1.moveToNext()){
					str=cursor1.getString(0);
				}
				value2.setVisibility(View.VISIBLE);
				value2.setText(str+"\n("+tmatch2+")");
				value2.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						theWhole.setEnabled(true);
						value1.setEnabled(true);
						value2.setEnabled(false);
						value3.setEnabled(true);
						value2.setBackgroundColor(Color.parseColor("#1d8700"));
						theWhole.setBackgroundColor(Color.parseColor("#22a100"));
						value1.setBackgroundColor(Color.parseColor("#22a100"));
						value3.setBackgroundColor(Color.parseColor("#22a100"));
						((HomePageActivity)getActivity()).titleCommand(v, team2, null);
					}
					
				});
			}
			if(team3.isEmpty()){
				value3.setVisibility(View.GONE);
			}
			else{
				String str=null;
				Cursor cursor1=helper.select("teams", new String[]{"name"}, "teamid=?", new String[]{team3}, null);
				if(cursor1.moveToNext()){
					str=cursor1.getString(0);
				}
				value3.setVisibility(View.VISIBLE);
				value3.setText(str+"\n("+tmatch3+")");
				value3.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						theWhole.setEnabled(true);
						value1.setEnabled(true);
						value2.setEnabled(true);
						value3.setEnabled(false);
						value3.setBackgroundColor(Color.parseColor("#1d8700"));
						theWhole.setBackgroundColor(Color.parseColor("#22a100"));
						value1.setBackgroundColor(Color.parseColor("#22a100"));
						value2.setBackgroundColor(Color.parseColor("#22a100"));
						((HomePageActivity)getActivity()).titleCommand(v, team3, null);
					}
					
				});
			}
			kickForBack.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					((Activity)context).onBackPressed();
				}
				
			});
			theWhole.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					theWhole.setEnabled(false);
					value1.setEnabled(true);
					value2.setEnabled(true);
					value3.setEnabled(true);
					theWhole.setBackgroundColor(Color.parseColor("#1d8700"));
					value1.setBackgroundColor(Color.parseColor("#22a100"));
					value2.setBackgroundColor(Color.parseColor("#22a100"));
					value3.setBackgroundColor(Color.parseColor("#22a100"));
					((HomePageActivity)getActivity()).titleCommand(v, "all", null);
				}
				
			});
			break;
		}
		}
		return view;
	}
	
	public void setStatus(int status){
		this.status=status;
	}
	
	public int setMsgNumberChanged(){
		int msgNumber=0;
		SQLHelper helper=SQLHelper.getInstance(context);
		Cursor cursor=helper.select("friends", new String[]{"phone"}, null, null, null);
		while(cursor.moveToNext()){
			String phone=cursor.getString(0);
			EMConversation conversation = EMChatManager.getInstance().getConversation(phone);
			int num=conversation.getUnreadMsgCount();
			if(num>0){
				EMMessage message=conversation.getLastMessage();
				TextMessageBody t=(TextMessageBody)message.getBody();
				Map<String, Object> temp=new HashMap<String, Object>();
				Cursor tmpCursor=helper.select("friends", new String[]{"name", "image"}, "phone=?", new String[]{phone}, null);
				if(tmpCursor.moveToNext()){
					temp.put("id", phone);
					temp.put("type", String.valueOf(ListsFragment.TYPE_FRIEND_MESSAGE));
					temp.put("name", tmpCursor.getString(0));
					temp.put("date", Tools.getData(message.getMsgTime()));
					temp.put("message", t.getMessage());
					temp.put("result", "u");
					Cursor tmpCursor1=helper.select("systemtable", new String[]{"i"}, "id=? and type=?", new String[]{phone, String.valueOf(ListsFragment.TYPE_FRIEND_MESSAGE)}, null);
					if(tmpCursor1.moveToNext()){
						temp.put("i", tmpCursor1.getInt(0));
					}
					else{
						Cursor tmpCursor2=helper.select("systemtable", new String[]{"max(i)"}, null, null, null);
						if(tmpCursor2.moveToNext()){
							int maxid=tmpCursor2.getInt(0);
							temp.put("i", maxid+1);
						}
					}
					String image=tmpCursor.getString(1).toString();
					if(!(image.equals("-1"))){
						temp.put("image", image);
					}
					else{
						temp.put("image", "-1");
					}
					helper.update(Tools.getContentValuesFromMap(temp, null), "systemtable", (Integer)temp.get("i"));
				}
			}
			msgNumber=msgNumber+num;
		}
		Cursor cursor1=helper.select("ich", new String[]{"team1", "team2", "team3"}, "phone=?", new String[]{"host"}, null);
		if(cursor1.moveToNext()){
			String teamid1=cursor1.getString(0);
			String teamid2=cursor1.getString(1);
			String teamid3=cursor1.getString(2);
			
			Map<String, Object> temp=new HashMap<String, Object>();
			PreferenceData pd=new PreferenceData(context);
			if(!teamid1.isEmpty()){
				temp.clear();
				Map<String, Object> map=pd.getData(new String[]{teamid1});
				if(!map.get(teamid1).toString().isEmpty()){
					EMConversation conversation = EMChatManager.getInstance().getConversation(map.get(teamid1).toString());
					int num=conversation.getUnreadMsgCount();
					if(num>0){
						EMMessage message=conversation.getLastMessage();
						TextMessageBody t=(TextMessageBody)message.getBody();
						Cursor tmpCursor=helper.select("friends", new String[]{"name", "image"}, "phone=?", new String[]{message.getFrom()}, null);
						if(tmpCursor.moveToNext()){
							temp.put("id", message.getFrom());
							temp.put("teamid", teamid1);
							temp.put("type", String.valueOf(ListsFragment.TYPE_TEAM1_CHANGINGROOM));
							Cursor tmpCursor0=helper.select("teams", new String[]{"name"}, "teamid=?", new String[]{teamid1}, null);
							if(tmpCursor0.moveToNext()){
								temp.put("name", tmpCursor0.getString(0));
							}
							temp.put("date", Tools.getData(message.getMsgTime()));
							temp.put("message", tmpCursor.getString(0)+": "+t.getMessage());
							temp.put("result", "u");
							Cursor tmpCursor1=helper.select("systemtable", new String[]{"i"}, "teamid=? and type=?", new String[]{teamid1, String.valueOf(ListsFragment.TYPE_TEAM1_CHANGINGROOM)}, null);
							if(tmpCursor1.moveToNext()){
								temp.put("i", tmpCursor1.getInt(0));
							}
							else{
								Cursor tmpCursor2=helper.select("systemtable", new String[]{"max(i)"}, null, null, null);
								if(tmpCursor2.moveToNext()){
									int maxid=tmpCursor2.getInt(0);
									temp.put("i", maxid+1);
								}
							}
							helper.update(Tools.getContentValuesFromMap(temp, null), "systemtable", (Integer)temp.get("i"));
						}
					}
					msgNumber=msgNumber+num;
				}
			}
			if(!teamid2.isEmpty()){
				temp.clear();
				Map<String, Object> map=pd.getData(new String[]{teamid2});
				if(!map.get(teamid2).toString().isEmpty()){
					EMConversation conversation = EMChatManager.getInstance().getConversation(map.get(teamid2).toString());
					int num=conversation.getUnreadMsgCount();
					if(num>0){
						EMMessage message=conversation.getLastMessage();
						TextMessageBody t=(TextMessageBody)message.getBody();
						Cursor tmpCursor=helper.select("friends", new String[]{"name", "image"}, "phone=?", new String[]{message.getFrom()}, null);
						if(tmpCursor.moveToNext()){
							temp.put("id", message.getFrom());
							temp.put("teamid", teamid2);
							temp.put("type", String.valueOf(ListsFragment.TYPE_TEAM2_CHANGINGROOM));
							Cursor tmpCursor0=helper.select("teams", new String[]{"name"}, "teamid=?", new String[]{teamid2}, null);
							if(tmpCursor0.moveToNext()){
								temp.put("name", tmpCursor0.getString(0));
							}
							temp.put("date", Tools.getData(message.getMsgTime()));
							temp.put("message", tmpCursor.getString(0)+": "+t.getMessage());
							temp.put("result", "u");
							Cursor tmpCursor1=helper.select("systemtable", new String[]{"i"}, "teamid=? and type=?", new String[]{teamid2, String.valueOf(ListsFragment.TYPE_TEAM1_CHANGINGROOM)}, null);
							if(tmpCursor1.moveToNext()){
								temp.put("i", tmpCursor1.getInt(0));
							}
							else{
								Cursor tmpCursor2=helper.select("systemtable", new String[]{"max(i)"}, null, null, null);
								if(tmpCursor2.moveToNext()){
									int maxid=tmpCursor2.getInt(0);
									temp.put("i", maxid+1);
								}
							}
							helper.update(Tools.getContentValuesFromMap(temp, null), "systemtable", (Integer)temp.get("i"));
						}
					}
					msgNumber=msgNumber+num;
				}
			}
			if(!teamid3.isEmpty()){
				temp.clear();
				Map<String, Object> map=pd.getData(new String[]{teamid3});
				if(!map.get(teamid3).toString().isEmpty()){
					EMConversation conversation = EMChatManager.getInstance().getConversation(map.get(teamid3).toString());
					int num=conversation.getUnreadMsgCount();
					if(num>0){
						EMMessage message=conversation.getLastMessage();
						TextMessageBody t=(TextMessageBody)message.getBody();
						Cursor tmpCursor=helper.select("friends", new String[]{"name", "image"}, "phone=?", new String[]{message.getFrom()}, null);
						if(tmpCursor.moveToNext()){
							temp.put("id", message.getFrom());
							temp.put("teamid", teamid3);
							temp.put("type", String.valueOf(ListsFragment.TYPE_TEAM3_CHANGINGROOM));
							Cursor tmpCursor0=helper.select("teams", new String[]{"name"}, "teamid=?", new String[]{teamid3}, null);
							if(tmpCursor0.moveToNext()){
								temp.put("name", tmpCursor0.getString(0));
							}
							temp.put("date", Tools.getData(message.getMsgTime()));
							temp.put("message", tmpCursor.getString(0)+": "+t.getMessage());
							temp.put("result", "u");
							Cursor tmpCursor1=helper.select("systemtable", new String[]{"i"}, "teamid=? and type=?", new String[]{teamid3, String.valueOf(ListsFragment.TYPE_TEAM1_CHANGINGROOM)}, null);
							if(tmpCursor1.moveToNext()){
								temp.put("i", tmpCursor1.getInt(0));
							}
							else{
								Cursor tmpCursor2=helper.select("systemtable", new String[]{"max(i)"}, null, null, null);
								if(tmpCursor2.moveToNext()){
									int maxid=tmpCursor2.getInt(0);
									temp.put("i", maxid+1);
								}
							}
							helper.update(Tools.getContentValuesFromMap(temp, null), "systemtable", (Integer)temp.get("i"));
						}
					}
					msgNumber=msgNumber+num;
				}
			}
		}
		
		msgNumber=msgNumber+Tools.getUnHandleMsgNumber(ListsFragment.TYPE_FRIEND_APPLY, context);
		msgNumber=msgNumber+Tools.getUnHandleMsgNumber(ListsFragment.TYPE_TEAMS_MESSAGE, context);
		msgNumber=msgNumber+Tools.getUnHandleMsgNumber(ListsFragment.TYPE_SYSTEM_MESSAGE, context);
		
		return msgNumber;
	}
	
	public void remind(int msgNumber){
		if(msgNumber>0){
			msgHint.setVisibility(View.VISIBLE);
		    msgHint.setText(msgNumber>99? "99+":""+msgNumber);
		}
		else{
			msgHint.setVisibility(View.GONE);
		}
	}
	
	public void setChecked(int checked, int resource){
		if(resource==R.layout.fragment_homepage_title){
			if(checked==R.id.btn_myMessage){
				myHomepageButton.setTextColor(Color.parseColor("#9e9e9e"));
				myMessage.setTextColor(Color.parseColor("#ffffff"));
				myFriend.setTextColor(Color.parseColor("#9e9e9e"));
				src1.setVisibility(View.INVISIBLE);
				src2.setVisibility(View.VISIBLE);
				src3.setVisibility(View.INVISIBLE);
			}
		}
	}
	

}
