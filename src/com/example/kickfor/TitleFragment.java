package com.example.kickfor;

import java.util.HashMap;
import java.util.Map;

























import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
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
	
	private int state=0;
	
	@Override
	public int getFragmentLevel() {
		// TODO Auto-generated method stub
		if(state==HOMEPAGE_TITLE){
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
	
	private ImageView src1=null;
	private ImageView src2=null;
	private ImageView src3=null;
	
	
	
	private void init(){
		this.context=getActivity();
		Bundle bundle=getArguments();
		if(bundle.containsKey("state")){
			this.state=bundle.getInt("state");
		}
		if(bundle.containsKey("teamid")){
			this.teamid=bundle.getString("teamid");
		}
		if(bundle.containsKey("authority")){
			this.authority=bundle.getString("authority");
		}
		if(bundle.containsKey("status")){
			this.status=bundle.getInt("status");
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
		// TODO Auto-generated method stub
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
					// TODO Auto-generated method stub
					((HomePageActivity)getActivity()).onBackPressed();
				}
				
			});
			search.addTextChangedListener(new TextWatcher(){

				@Override
				public void afterTextChanged(Editable arg0) {
					// TODO Auto-generated method stub
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
					// TODO Auto-generated method stub
					
				}

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					// TODO Auto-generated method stub
					
				}
				
			});
			break;
		}
		
		case HOMEPAGE_TITLE:{
			view=inflater.inflate(R.layout.fragment_homepage_title,container,false);
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
					// TODO Auto-generated method stub
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
					// TODO Auto-generated method stub
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
					// TODO Auto-generated method stub
					myHomepageButton.setTextColor(Color.parseColor("#9e9e9e"));
					myMessage.setTextColor(Color.parseColor("#9e9e9e"));
					myFriend.setTextColor(Color.parseColor("#ffffff"));
					src1.setVisibility(View.INVISIBLE);
					src2.setVisibility(View.INVISIBLE);
					src3.setVisibility(View.VISIBLE);
					((HomePageActivity)getActivity()).titleCommand(myFriend, null, null);
				}
				
			});
			
			/*group.setOnCheckedChangeListener(new OnCheckedChangeListener(){

				@Override
				public void onCheckedChanged(RadioGroup group, int checkedId) {
					// TODO Auto-generated method stub
					switch(checkedId){
					case R.id.btn_myhomepage:{
						myHomepageButton.setTextColor(Color.parseColor("#ffffff"));
						myMessage.setTextColor(Color.parseColor("#9e9e9e"));
						myFriend.setTextColor(Color.parseColor("#9e9e9e"));
						src1.setVisibility(View.VISIBLE);
						src2.setVisibility(View.INVISIBLE);
						src3.setVisibility(View.INVISIBLE);
						((HomePageActivity)getActivity()).titleCommand(myHomepageButton, null, null);
						break;
					}
					case R.id.relative_myMessage:{
						myHomepageButton.setTextColor(Color.parseColor("#9e9e9e"));
						myMessage.setTextColor(Color.parseColor("#ffffff"));
						myFriend.setTextColor(Color.parseColor("#9e9e9e"));
						src1.setVisibility(View.INVISIBLE);
						src2.setVisibility(View.VISIBLE);
						src3.setVisibility(View.INVISIBLE);
						((HomePageActivity)getActivity()).titleCommand(myMessage, null, null);
						break;
					}
					case R.id.btn_myFriends:{
						myHomepageButton.setTextColor(Color.parseColor("#9e9e9e"));
						myMessage.setTextColor(Color.parseColor("#9e9e9e"));
						myFriend.setTextColor(Color.parseColor("#ffffff"));
						src1.setVisibility(View.INVISIBLE);
						src2.setVisibility(View.INVISIBLE);
						src3.setVisibility(View.VISIBLE);
						((HomePageActivity)getActivity()).titleCommand(myFriend, null, null);
						break;
					}
					}
				}
				
			});*/
			
			break;
		}
		case PREVIEW_MATCH:{
			view=inflater.inflate(R.layout.fragment_preview_title, container, false);
			previewBack=(ImageView)view.findViewById(R.id.preview_left);
			previewNext=(ImageView)view.findViewById(R.id.preview_right);
			if(status==3){
				previewNext.setVisibility(View.GONE);
			}
			else{
				previewNext.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						((HomePageActivity)getActivity()).previewCommand("right", status+1);
					}
					
				});
			}
			previewBack.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
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
			back.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
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
			SQLHelper helper=SQLHelper.getInstance(context);
			Cursor cursor=helper.select("ich", new String[]{"name", "image", "team1", "team2", "team3", "goal1", "goal2", "goal3", "assist1", "assist2", "assist3", "tmatch1", "tmatch2", "tmatch3"}, "phone=?", new String[]{"host"}, null);
			cursor.moveToNext();
			myName.setText(cursor.getString(0));
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
						// TODO Auto-generated method stub
						theWhole.setEnabled(true);
						value1.setEnabled(false);
						value2.setEnabled(true);
						value3.setEnabled(true);
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
						// TODO Auto-generated method stub
						theWhole.setEnabled(true);
						value1.setEnabled(true);
						value2.setEnabled(false);
						value3.setEnabled(true);
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
						// TODO Auto-generated method stub
						theWhole.setEnabled(true);
						value1.setEnabled(true);
						value2.setEnabled(true);
						value3.setEnabled(false);
						((HomePageActivity)getActivity()).titleCommand(v, team3, null);
					}
					
				});
			}
			kickForBack.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					((Activity)context).onBackPressed();
				}
				
			});
			theWhole.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					theWhole.setEnabled(false);
					value1.setEnabled(true);
					value2.setEnabled(true);
					value3.setEnabled(true);
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
			msgNumber=msgNumber+conversation.getUnreadMsgCount();
		}
		Cursor cursor1=helper.select("ich", new String[]{"team1", "team2", "team3"}, "phone=?", new String[]{"host"}, null);
		if(cursor1.moveToNext()){
			String teamid1=cursor1.getString(0);
			String teamid2=cursor1.getString(1);
			String teamid3=cursor1.getString(2);
			PreferenceData pd=new PreferenceData(context);
			if(!teamid1.isEmpty()){
				Map<String, Object> map=pd.getData(new String[]{teamid1});
				if(!map.get(teamid1).toString().isEmpty()){
					EMConversation conversation = EMChatManager.getInstance().getConversation(map.get(teamid1).toString());
					msgNumber=msgNumber+conversation.getUnreadMsgCount();
				}
			}
			if(!teamid2.isEmpty()){
				Map<String, Object> map=pd.getData(new String[]{teamid2});
				if(!map.get(teamid2).toString().isEmpty()){
					EMConversation conversation = EMChatManager.getInstance().getConversation(map.get(teamid2).toString());
					msgNumber=msgNumber+conversation.getUnreadMsgCount();
				}
			}
			if(!teamid3.isEmpty()){
				Map<String, Object> map=pd.getData(new String[]{teamid3});
				if(!map.get(teamid3).toString().isEmpty()){
					EMConversation conversation = EMChatManager.getInstance().getConversation(map.get(teamid3).toString());
					msgNumber=msgNumber+conversation.getUnreadMsgCount();
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
