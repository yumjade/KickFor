package com.example.kickfor;

import java.io.File;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;

import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.EMMessage.ChatType;
import com.easemob.chat.TextMessageBody;
import com.example.kickfor.lobby.LobbyInterface;
import com.example.kickfor.lobby.LobbyTeamAddFragment;
import com.example.kickfor.lobby.LobbyTeamEntity;
import com.example.kickfor.lobby.LobbyTeamFragment;
import com.example.kickfor.lobby.LobbyTeamReplyFragment;
import com.example.kickfor.more.AboutusFragment;
import com.example.kickfor.more.FeedbackFragment;
import com.example.kickfor.more.FindPasswordsFragment;
import com.example.kickfor.more.MoreFragment;
import com.example.kickfor.more.MoreInterface;
import com.example.kickfor.more.MoreProtocols;
import com.example.kickfor.more.SearchItemEntity;
import com.example.kickfor.more.SearchItemFragment;
import com.example.kickfor.more.SettingsFragment;
import com.example.kickfor.service.NetWorkBroadcastReceiver;
import com.example.kickfor.team.ChangingRoomAdapter;
import com.example.kickfor.team.ChangingRoomFragment;
import com.example.kickfor.team.EditPreviewFragment;
import com.example.kickfor.team.EditReviewFragment;
import com.example.kickfor.team.EditTeamGradeFragment;
import com.example.kickfor.team.HallOfFameFragment;
import com.example.kickfor.team.HallofFame;
import com.example.kickfor.team.HonorInfo;
import com.example.kickfor.team.MatchLogFragment;
import com.example.kickfor.team.MatchPreviewFragment;
import com.example.kickfor.team.MatchReviewDetailFragment;
import com.example.kickfor.team.MatchReviewEntity;
import com.example.kickfor.team.MatchReviewFragment;
import com.example.kickfor.team.OtherTeamFragment;
import com.example.kickfor.team.PrematchStartFragment;
import com.example.kickfor.team.ShooterAssisterFragment;
import com.example.kickfor.team.TeamCreateFragment;
import com.example.kickfor.team.TeamFragment;
import com.example.kickfor.team.TeamInfoEditFragment;
import com.example.kickfor.team.TeamInfoGradeEditFragment;
import com.example.kickfor.team.TeamInfoGradeFragment;
import com.example.kickfor.team.TeamInterface;
import com.example.kickfor.team.TeamMemberList;
import com.example.kickfor.utils.Constant;
import com.example.kickfor.utils.IdentificationInterface;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

public class HomePageActivity extends FragmentActivity implements HandlerListener{
	
	/**
	 * 
	 */
	private ClientReader reader=null;
	private BarFragment bar=null;
	private TitleFragment upTitle=null;

	private FrameLayout ftitle=null;
	private FrameLayout fcontent=null;
	private FrameLayout fbar=null;
	
	private EMEventListener listener=null;
	private PreferenceData pd=null;
	public FragmentManager fm=null;
	private static RealTimeHandler mRealTimeHandler=null;
	private RelativeLayout vague=null;
	private int selectImage;
	public boolean fromViewPager=false;
	
	private long ctime=0;
	private boolean isSocketClose=false;
	
	public ViewPager viewPager=null;
	public List<Fragment> fragmentList1=new ArrayList<Fragment>();
	private boolean isInitTeam=false;
	
	private FragmentStatePagerAdapter mAdapter=null;
	
	private Map<String, Object> teamMap=null;
	private String phone=null;
	private static String teamid1="";
	private static String teamid2="";
	private static String teamid3="";
	private String authority1=null;
	private String authority2=null;
	private String authority3=null;
	
	public Bitmap bitmap=null;
	private boolean needShow=false;
	private boolean isViewPager=false;
	private NetWorkBroadcastReceiver networkReceiver=null;
	private boolean networkHasChanged=false;
	private FrameLayout fmain=null;
	private boolean needToShowMain;
	private IWXAPI api=null;

	private Timer mTimer=null;
	
	protected static final String IMAGE_FILE_NAME="faceImage.jpg";
	protected static final int IMAGE_REQUEST_CODE=0;
	protected static final int CAMERA_REQUEST_CODE=1;
	protected static final int RESULT_REQUEST_CODE=2;
	protected static final int TEAM_CREATED=3;
	protected static final int CHAT_COME=4;
	protected static final int GROUP_CHAT_COME=5;
	protected static final int UPLOADED_PHOTO=6;
	protected static final int SEEKED=7;
	protected static final int APPLY_FRIEND=8;
	protected static final int UPDATE_FRIEND_LIST=9;
	protected static final int LOGIN_SUCCESS=10;
	protected static final int RESPONSE_PHONE=11;
	protected static final int RESPONSE_CODE=12;
	protected static final int RESPONSE_NAME=13;
	protected static final int RESPONSE_POSITION1=14;
	protected static final int RESPONSE_POSITION2=15;
	protected static final int RESPONSE_CITY=16;
	protected static final int COMPLETED_INFORMATION=17;
	protected static final int COMPLETED_POSITION=18;
	protected static final int SEEKED_TEAM=19;
	protected static final int ERROR_LOGIN=20;
	protected static final int TEAM_MATE=22;
	protected static final int FUCK_OFF=23;
	protected static final int GET_TEAM_HONOR=24;
	protected static final int GET_ONES_MATCH=25;
	protected static final int SEEKED_ONE_INFO=26;
	protected static final int SEEKED_ONE_TEAM=27;
	protected static final int JOIN_MATCH=28;
	protected static final int GET_MATCHES=29;
	protected static final int GET_MATCH_DETAIL=30;
	protected static final int GET_MAXID=31;
	protected static final int GET_MATCH_BY_TYPE=32;
	protected static final int GET_UPDATED_MATCH=33;
	
	protected static final int WAIT_LOGIN=34;
	protected static final int WAIT_LIST_FRIENDS=35;
	public static final int WAIT_REVIEW_LIST=36;
	public static final int WAIT_SEARCH_LIST=37;
	protected static final int WAIT_REGISTER_PHONE=38;
	protected static final int EXISTED_PHONE=39;
	protected static final int WAIT_REGISTER_CODING=40;
	protected static final int WAIT_NAME=41;
	protected static final int WAIT_POSITION=42;
	protected static final int WAIT_CITY_DISTRICT=43;
	protected static final int WAIT_COMPLETE=44;
	protected static final int WAIT_TEAM_CREATE=45;
	public static final int WAIT_ADD_NEW=46;
	public static final int WAIT_TEAM=47;
	public static final int WAIT_UPLOAD_MATCH=48;
	protected static final int WAIT_PREMATCH=49;
	public static final int WAIT_PROGRESSBAR=50;
	protected static final int SOMEONE_JOININ=51;
	protected static final int BE_FUCKED=52;
	protected static final int REFUSE_JOIN=53;
	protected static final int DENY_AUTHORITY=54;
	protected static final int DECLINE_AUTHORITY=55;
	protected static final int WAIT_COMPLETE_INFO=56;
	protected static final int WAIT_EVALUATE_INFO=57;
	protected static final int EVALUATE_INFO=58;
	protected static final int EVALUATION_RESULT=59;
	protected static final int WAIT_EVALUATE_RESULT=60;
	protected static final int TEAM_INFO=61;
	protected static final int WAIT_SIGNIN=62;
	protected static final int SIGN_SUCCESS=63;
	protected static final int UPDATED_MATCH=64;
	protected static final int DELETE_FRIEND=65;
	protected static final int APPLY_JOIN=66;
	protected static final int INIT_TEAM=67;
	protected static final int GET_GROUPID = 68;
	public static final int GET_MATCH_BY_ID = 69;
	public static final int WAIT_DELETE_PREVIEW=70;
	public static final int DELETE_PREVIEW = 71;
	public static final int NEW_MESSAGE = 72;
	public static final int GET_FAME = 73;
	public static final int UPDATE_FAME = 74;
	public static final int GET_CODE = 75;
	public static final int SET_PSW = 76;
	public static final int SET_NEW_PSW = 77;
	public static final int LOG_OUT = 78;
	public static final int CHANGE_TEAM_INFO = 79;
	public static final int CHANGE_TEAM_GRADE = 80;
	public static final int WAIT_EDIT_TEAM_INFO = 81;
	public static final int WAIT_EDIT_TEAM_PROCESS = 82;
	public static final int WAIT_EDIT_TEAM_GRADE = 83;
	public static final int WAIT_EDIT_FAME = 84;
	public static final int UPDATE_REVIEW_MATCH = 85;
	public static final int WAIT_UPDATE_REVIEW = 86;
	protected static final int LOBBY_TEAM = 87;
	protected static final int AUTHORITY = 88;
	protected static final int OK_THEME = 89;
	public static final int GET_ARCHIVES=90;
	public static final int GET_USERSKILLS=91;
	
	private ViewFlipper allFlipper=null;
	
	public static final int SPLASH_LENGTH = 3000;

	
	private Handler handler = new Handler(){
    	@Override
    	public void handleMessage(Message msg) {
    		// TODO Auto-generated method stub
    		switch (msg.what) {
    		case 1:
    			allFlipper.setDisplayedChild(1);
    			break;
    		}
    	}
    };

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_homepage);
		
		api = WXAPIFactory.createWXAPI(this, Constant.APP_ID, true);
		api.registerApp(Constant.APP_ID);

		
		allFlipper = (ViewFlipper) findViewById(R.id.allflipper);
		 new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					handler.sendEmptyMessage(1);
				}
	        }, SPLASH_LENGTH);

		
		networkReceiver=new NetWorkBroadcastReceiver(this);
		IntentFilter intentFilter=new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
		registerReceiver(networkReceiver, intentFilter);
		
		pd=new PreferenceData(this);
		fm=getSupportFragmentManager();
		
		listener=new EMEventListener() {
			
			@Override
			public void onEvent(EMNotifierEvent event) {

				switch (event.getEvent()) {	
				case EventNewMessage:{// 接收新消息
					System.out.println("uuuu");
					String phone=null;
					final EMMessage message = (EMMessage) event.getData();
					TextMessageBody t=(TextMessageBody)message.getBody();
					if(message.getChatType().equals(ChatType.Chat)){
						phone=message.getFrom();
						Map<String, Object> temp=new HashMap<String, Object>();
						SQLHelper helper=SQLHelper.getInstance(getApplicationContext());
						Cursor cursor1=helper.select("friends", new String[]{"name", "image"}, "phone=?", new String[]{phone}, null);
						if(cursor1.moveToNext()){
							temp.put("id", phone);
							temp.put("type", String.valueOf(ListsFragment.TYPE_FRIEND_MESSAGE));
							temp.put("name", cursor1.getString(0));
							temp.put("date", Tools.getData(message.getMsgTime()));
							temp.put("message", t.getMessage());
							temp.put("result", "u");
							Cursor cursor=helper.select("systemtable", new String[]{"i"}, "id=? and type=?", new String[]{phone, String.valueOf(ListsFragment.TYPE_FRIEND_MESSAGE)}, null);
							if(cursor.moveToNext()){
								temp.put("i", cursor.getInt(0));
							}
							else{
								Cursor cursor3=helper.select("systemtable", new String[]{"max(i)"}, null, null, null);
								if(cursor3.moveToNext()){
									int maxid=cursor3.getInt(0);
									temp.put("i", maxid+1);
								}
							}
							String image=cursor1.getString(1).toString();
							if(!(image.equals("-1"))){
								temp.put("image", image);
							}
							else{
								temp.put("image", "-1");
							}
							helper.update(Tools.getContentValuesFromMap(temp, null), "systemtable", (Integer)temp.get("i"));
						}
					}
					else if(message.getChatType().equals(ChatType.GroupChat)){
						Map<String, Object> map=pd.getData();
						Iterator<String> iter=map.keySet().iterator();
						while(iter.hasNext()){
							String key=iter.next();
							if(map.get(key) instanceof String && map.get(key).toString().equals(message.getTo())){
								phone=key;
								break;
							}
						}
						Map<String, Object> temp=new HashMap<String, Object>();
						SQLHelper helper=SQLHelper.getInstance(getApplicationContext());
						Cursor cursor1=helper.select("friends", new String[]{"name", "image"}, "phone=?", new String[]{message.getFrom()}, null);
						if(cursor1.moveToNext()){
							temp.put("id", message.getFrom());
							temp.put("teamid", phone);
							String type="";
							if(teamid1.equals(phone)){
								type=String.valueOf(ListsFragment.TYPE_TEAM1_CHANGINGROOM);
								temp.put("type", type);
							}
							else if(teamid2.equals(phone)){
								type=String.valueOf(ListsFragment.TYPE_TEAM2_CHANGINGROOM);
								temp.put("type", type);
							}
							else if(teamid3.equals(phone)){
								type=String.valueOf(ListsFragment.TYPE_TEAM3_CHANGINGROOM);
								temp.put("type", type);
							}
							Cursor cursor2=helper.select("teams", new String[]{"name"}, "teamid=?", new String[]{phone}, null);
							if(cursor2.moveToNext()){
								temp.put("name", cursor2.getString(0));
							}
							temp.put("date", Tools.getData(message.getMsgTime()));
							temp.put("message", cursor1.getString(0)+": "+t.getMessage());
							temp.put("result", "u");
							Cursor cursor=helper.select("systemtable", new String[]{"i"}, "teamid=? and type=?", new String[]{phone, type}, null);
							if(cursor.moveToNext()){
								temp.put("i", cursor.getInt(0));
							}
							else{
								Cursor cursor3=helper.select("systemtable", new String[]{"max(i)"}, null, null, null);
								if(cursor3.moveToNext()){
									int maxid=cursor3.getInt(0);
									temp.put("i", maxid+1);
								}
							}
							helper.update(Tools.getContentValuesFromMap(temp, null), "systemtable", (Integer)temp.get("i"));
						}
					}
					if(fm.findFragmentById(R.id.content) instanceof ListsFragment && ((ListsFragment)fm.findFragmentById(R.id.content)).getState()==ListsFragment.TYPE_MESSAGE_LIST){
						final ListsFragment tmp=(ListsFragment)fm.findFragmentById(R.id.content);
						runOnUiThread(new Runnable(){

							@Override
							public void run() {
								// TODO Auto-generated method stub
								tmp.changedData();
							}
							
						});
					}
					if(fm.findFragmentById(R.id.title) instanceof TitleFragment && ((TitleFragment)fm.findFragmentById(R.id.title)).getState()==TitleFragment.HOMEPAGE_TITLE){
						System.out.println("dffffffffffggggg");
						final TitleFragment tmp=(TitleFragment)fm.findFragmentById(R.id.title);
						runOnUiThread(new Runnable(){

							@Override
							public void run() {
								// TODO Auto-generated method stub
								int msgNumber=tmp.setMsgNumberChanged();
								tmp.remind(msgNumber);
								System.out.println(""+msgNumber);
							}
							
						});
						
					}
					if(fm.findFragmentById(R.id.main) instanceof ChatFragment && 
							((ChatFragment)fm.findFragmentById(R.id.main)).getPhone().equals(phone)){
						runOnUiThread(new Runnable(){

							@Override
							public void run() {
								// TODO Auto-generated method stub
								ChatFragment tmp=(ChatFragment)fm.findFragmentById(R.id.main);
								tmp.setMessage(message);
							}
							
						});						
					}
					break;
				}
				}
			}
			};
		
		
		
		mRealTimeHandler=RealTimeHandler.getInstance();
		initReader();
		if(pd.getData(new String[]{"phone"}).get("phone").toString().isEmpty() || pd.getData(new String[]{"passwords"}).get("passwords").toString().isEmpty()){
			LoginFragment login=new LoginFragment();
			FragmentTransaction tx=fm.beginTransaction();
			tx.replace(R.id.content, login);
			tx.commit();
		}
		else{
			SQLHelper helper=SQLHelper.getInstance(this);
			Map<String, Object> map=pd.getData(new String[]{"phone", "passwords"});
			String phone=map.get("phone").toString();
			String passwords=map.get("passwords").toString();
			Map<String, Object> tmp=new HashMap<String, Object>();
			//EMGroupManager.getInstance().loadAllGroups();
		    //EMChatManager.getInstance().loadAllConversations();
			tmp.put("request", "user login");
			tmp.put("date", Tools.getDate1());
			tmp.put("phone", phone);
			tmp.put("passwords", passwords);
			tmp.put("rid", JPushInterface.getRegistrationID(getApplicationContext()));
			Cursor cursor=helper.select("ich", new String[]{"image"}, "phone=?", new String[]{"host"}, null);
			if(cursor.moveToNext()){
				String imgPath=cursor.getString(0);
				if(imgPath!=null && Tools.isFileExist(imgPath)!=false){
					tmp.put("img", "1");
				}
			}
			Runnable r=new ClientWrite(Tools.JsonEncode(tmp));
			new Thread(r).start();
			Tools.loginHuanXin(phone, passwords);
		}
		
		fmain=(FrameLayout)findViewById(R.id.main);
		viewPager=(ViewPager)findViewById(R.id.viewpager);
		ftitle=(FrameLayout)findViewById(R.id.title);
		fcontent=(FrameLayout)findViewById(R.id.content);
		fbar=(FrameLayout)findViewById(R.id.bar);
		vague=(RelativeLayout)findViewById(R.id.vague_b);
		
		mTimer=new Timer();
		mTimer.schedule(new TimerTask(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("request", "heart");
				Runnable r=new ClientWrite(Tools.JsonEncode(map));
				new Thread(r).start();
			}
			
		}, 1000, 10000);
	}
	
	public void initReader(){
		reader=new ClientReader(mRealTimeHandler.getHandler(), this);
		new Thread(reader).start();
	}
	
	private void hideAll(){
		ftitle.setVisibility(View.GONE);
		fcontent.setVisibility(View.GONE);
		fbar.setVisibility(View.GONE);
	}
	
	private void showAll(){
		ftitle.setVisibility(View.VISIBLE);
		fcontent.setVisibility(View.VISIBLE);
		fbar.setVisibility(View.VISIBLE);
	}
	
	public void settings(){
		SettingsFragment settings = new SettingsFragment();
		FragmentTransaction tx = fm.beginTransaction();
		tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
		tx.replace(R.id.main, settings);
		tx.addToBackStack(null);
		tx.commit();
		
	}

	public void hideMain(){
		needToShowMain=true;
		fmain.setVisibility(View.GONE);
	}
	
	public void showTitle(){
		ftitle.setVisibility(View.VISIBLE);
	}
	
	public void hideTitle(){
		ftitle.setVisibility(View.GONE);
	}
	
	public void showMain(){
		fmain.setVisibility(View.VISIBLE);
		needToShowMain=false;
	}
	
	private void hideDown(){
		fcontent.setVisibility(View.GONE);
		fbar.setVisibility(View.GONE);
	}
	
	private void showDown(){
		fcontent.setVisibility(View.VISIBLE);
		fbar.setVisibility(View.VISIBLE);
	}
	
	public void setBar(boolean enable){
		bar.setEnable(enable);
	}
	
	public void removeVague(){
		if(vague!=null){
			vague.setVisibility(View.GONE);
		}
    }
    
    public void openVague(int what){
    	VagueFragment v=new VagueFragment(mRealTimeHandler.getHandler(), what);
    	new Thread(v).start();
    	vague.setVisibility(View.VISIBLE);
    	
		
    }
	
	public void openProgressBarWait(int what, Object v, Object b){
		Runnable r=new ProgressBarTimer(mRealTimeHandler.getHandler(), what, v, b);
		new Thread(r).start();
	}
	



	public void openOthersHomePage(HomePageEntity entity, boolean isFriend){
		HomePageFragment othersHomePage=new HomePageFragment();
		Bundle bundle=new Bundle();
		bundle.putSerializable("entity", entity);
		bundle.putBoolean("isFriend", isFriend);
		othersHomePage.setArguments(bundle);
		FragmentTransaction tx=fm.beginTransaction();
		tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
		tx.replace(R.id.main, othersHomePage);
		tx.addToBackStack(null);
		tx.commit();
	}


	public void registerCommand() {
		Bundle bundle=new Bundle();
		bundle.putInt("state", RegisterFragment.PHONE_PASSWORDS);
		RegisterFragment register=new RegisterFragment();
		register.setArguments(bundle);
		FragmentTransaction tx=fm.beginTransaction();
		tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
		tx.replace(R.id.content, register);
		tx.addToBackStack(null);
		tx.commit();	
	}


	
	 @Override
	 public void onConfigurationChanged(Configuration newConfig) {
		 
		 super.onConfigurationChanged(newConfig);
	 }




	public void edit(int id, String teamid, String againstid, String againstname,
		String place, String date, String time, String type, String person) {
		EditPreviewFragment tmp=new EditPreviewFragment();
		Bundle bundle=new Bundle();
		bundle.putInt("id", id);
		bundle.putString("teamid", teamid);
		bundle.putString("againstid", againstid);
		bundle.putString("againstname", againstname);
		bundle.putString("place", place);
		bundle.putString("date", date);
		bundle.putString("time", time);
		bundle.putString("type", type);
		bundle.putString("person", person);
		tmp.setArguments(bundle);
		FragmentTransaction tx=fm.beginTransaction();
		tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
		tx.replace(R.id.main, tmp);
		tx.addToBackStack(null);
		tx.commit();
	}

	
	public void openTeamInfoEdit(String teamid, String authority){
		int r=Integer.parseInt(authority);
		if(r>=2){
			Bundle bundle=new Bundle();
			bundle.putString("teamid", teamid);
			TeamInfoEditFragment tmp=new TeamInfoEditFragment();
			tmp.setArguments(bundle);
			FragmentTransaction tx=fm.beginTransaction();
			tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
			tx.replace(R.id.main, tmp);
			tx.addToBackStack(null);
			tx.commit();
		}
	}


	public void onBarCheck(int index) {
		// TODO Auto-generated method stub
		switch(index){
		case R.id.bar_home:{
			Bundle bundle0=new Bundle();
			bundle0.putInt("state", TitleFragment.HOMEPAGE_TITLE);
			TitleFragment homepageTitle=new TitleFragment();
			homepageTitle.setArguments(bundle0);
			HomePageFragment homepage=new HomePageFragment();
			Bundle bundle=new Bundle();
			bundle.putString("phone", "host");
			homepage.setArguments(bundle);
			FragmentTransaction tx=fm.beginTransaction();
			tx.replace(R.id.title, homepageTitle);
			tx.replace(R.id.content, homepage);
			tx.commit();
			closeViewPager();
			break;
		}
		case R.id.bar_lobby:{
			Bundle bundle0=new Bundle();
			if(authority1.equals("0") && authority2.equals("0") && authority3.equals("0")){
				bundle0.putBoolean("canAdd", false);
			}
			else{
				bundle0.putBoolean("canAdd", true);
			}
			bundle0.putInt("state", TitleFragment.LOBBY_TITLE);
			TitleFragment lobbyTitle=new TitleFragment();
			lobbyTitle.setArguments(bundle0);
			LobbyTeamFragment tmp=new LobbyTeamFragment();
			FragmentTransaction tx=fm.beginTransaction();
			tx.replace(R.id.title, lobbyTitle);
			tx.replace(R.id.content, tmp);
			tx.commit();
			closeViewPager();
			break;
		}
		case R.id.bar_team:{
			if(fm.findFragmentById(R.id.content)!=null){
				FragmentTransaction tx=fm.beginTransaction();
				tx.remove(fm.findFragmentById(R.id.content));
				tx.commit();
			}
			if(fm.findFragmentById(R.id.title)!=null){
				FragmentTransaction tx=fm.beginTransaction();
				tx.remove(fm.findFragmentById(R.id.title));
				tx.commit();
			}
			resetViewPager();
			isInitTeam=initTeam(isInitTeam);
			break;
		}
		case R.id.bar_more:{
			MoreFragment more=new MoreFragment();
			FragmentTransaction tx=fm.beginTransaction();
			if(fm.findFragmentById(R.id.title) instanceof TitleFragment){
				tx.remove(fm.findFragmentById(R.id.title));
			}
			tx.replace(R.id.content, more);
			tx.commit();
			closeViewPager();
			break;
		}
		}
	}
	
	public void setNetWorkStatus(){
		networkHasChanged=true;
	}
	
	public void openTeams(String teamid){
		back();
		showAll();
		if(fm.findFragmentById(R.id.content)!=null){
			FragmentTransaction tx=fm.beginTransaction();
			tx.remove(fm.findFragmentById(R.id.content));
			tx.commit();
		}
		if(fm.findFragmentById(R.id.title)!=null){
			FragmentTransaction tx=fm.beginTransaction();
			tx.remove(fm.findFragmentById(R.id.title));
			tx.commit();
		}
		bar.initChecked(R.id.bar_team);
		isInitTeam=initTeam(isInitTeam);
		if(teamid.equals(teamid1)){
			viewPager.setCurrentItem(0);
		}
		else if(teamid.equals(teamid2)){
			viewPager.setCurrentItem(1);
		}
		else if(teamid.equals(teamid3)){
			viewPager.setCurrentItem(2);
		}
		resetViewPager();
	}
	
	public void openSearch(){
		Bundle bundle=new Bundle();
		bundle.putInt("state", TitleFragment.SEARCH_TITLE);
		SearchItemFragment searchList=new SearchItemFragment(this);
		upTitle=new TitleFragment();
		upTitle.setArguments(bundle);
		FragmentTransaction tx=fm.beginTransaction();
		tx.replace(R.id.title, upTitle);
		tx.replace(R.id.content, searchList);
		if(fm.findFragmentById(R.id.bar) instanceof BarFragment){
			tx.hide(bar);
		}
		tx.addToBackStack(null);
		tx.commit();
		if(fromViewPager==true){
			closeViewPager();
		}
		System.out.println("开启搜索");
	}
	
	public void openProtocols(){
		MoreProtocols protocols=new MoreProtocols();
		FragmentTransaction tx=fm.beginTransaction();
		tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
		tx.replace(R.id.main, protocols);
		tx.addToBackStack(null);
		tx.commit();
	}
	
	private boolean initTeam(boolean isInitTeam){
		if(isInitTeam==false){
			fragmentList1.clear();
			boolean isCreatedTeam=false;
			if(authority1!=null && authority2!=null && authority3!=null && teamid1!=null && teamid2!=null && teamid3!=null){
				if(authority1.equals("4") || authority2.equals("4") || authority3.equals("4")){
					isCreatedTeam=true;
				}
				
				if(teamid1.isEmpty()){
					TeamCreateFragment tmp=new TeamCreateFragment();
					Bundle bundle=new Bundle();
					bundle.putBoolean("isCreatedTeam", isCreatedTeam);
					bundle.putInt("resource", R.layout.fragment_team_create1);
					tmp.setArguments(bundle);
					fragmentList1.add(tmp);
				}
				else{
					TeamFragment tmp=new TeamFragment();
					Bundle bundle=new Bundle();
					bundle.putString("teamid", teamid1);
					bundle.putString("authority", authority1);
					tmp.setArguments(bundle);
					fragmentList1.add(tmp);
				}
				
				if(teamid2.isEmpty()){
					TeamCreateFragment tmp=new TeamCreateFragment();
					Bundle bundle=new Bundle();
					bundle.putBoolean("isCreatedTeam", isCreatedTeam);
					bundle.putInt("resource", R.layout.fragment_team_create1);
					tmp.setArguments(bundle);
					fragmentList1.add(tmp);
				}
				else{
					TeamFragment tmp=new TeamFragment();
					Bundle bundle=new Bundle();
					bundle.putString("teamid", teamid2);
					bundle.putString("authority", authority2);
					tmp.setArguments(bundle);
					fragmentList1.add(tmp);
				}
				if(teamid3.isEmpty()){
					TeamCreateFragment tmp=new TeamCreateFragment();
					Bundle bundle=new Bundle();
					bundle.putBoolean("isCreatedTeam", isCreatedTeam);
					bundle.putInt("resource", R.layout.fragment_team_create1);
					tmp.setArguments(bundle);
					fragmentList1.add(tmp);
				}
				else{
					TeamFragment tmp=new TeamFragment();
					Bundle bundle=new Bundle();
					bundle.putString("teamid", teamid3);
					bundle.putString("authority", authority3);
					tmp.setArguments(bundle);
					fragmentList1.add(tmp);
				}
				
				if(mAdapter==null){
					mAdapter=new FragmentStatePagerAdapter(fm){

						@Override
						public Fragment getItem(int arg0) {
							// TODO Auto-generated method stub
							return fragmentList1.get(arg0);
						}

						@Override
						public int getCount() {
							// TODO Auto-generated method stub
							return fragmentList1.size();
						}

						@Override
						public int getItemPosition(Object object) {
							// TODO Auto-generated method stub
							return PagerAdapter.POSITION_NONE;
						}
					};
					viewPager.setAdapter(mAdapter);
				}
				else{
					mAdapter.notifyDataSetChanged();
				}
			}	
		}
		return true;
	}
	
	public void teamCommand(View v, String teamid, String authority) {
		// TODO Auto-generated method stub
		switch(v.getId()){
		case R.id.tv_changing_room:{
			Bundle bundle=new Bundle();
			bundle.putString("phone", teamid);
			bundle.putString("type", "0");
			bundle.putString("authority", authority);
			ChatFragment changingRoomChat=new ChatFragment();
			changingRoomChat.setArguments(bundle);
			FragmentTransaction tx=fm.beginTransaction();
			tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
			tx.replace(R.id.main, changingRoomChat);
			tx.addToBackStack(null);
			tx.commit();
			break;
		}
		case R.id.tv_rematch:{
			MatchReviewFragment matchReviewList=new MatchReviewFragment();
			Bundle bundle=new Bundle();
			bundle.putString("teamid", teamid);
			bundle.putString("authority", authority);
			matchReviewList.setArguments(bundle);
			FragmentTransaction tx=fm.beginTransaction();
			tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
			tx.replace(R.id.main, matchReviewList);
			tx.addToBackStack(null);
			tx.commit();
			break;
		}
		case R.id.team_info_up:{
			MatchLogFragment tmp=new MatchLogFragment();
			Bundle bundle=new Bundle();
			bundle.putString("teamid", teamid);
			tmp.setArguments(bundle);
			FragmentTransaction tx=fm.beginTransaction();
			tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
			tx.replace(R.id.main, tmp);
			tx.addToBackStack(null);
			tx.commit();
			break;
		}
		case R.id.tv_prematch:{
			Bundle bundle=new Bundle();
			bundle.putString("teamid", teamid);
			bundle.putInt("authority", Integer.parseInt(authority));
			if(Tools.hasUnkickedMatch(this, teamid)){
				MatchPreviewFragment previewMatch=new MatchPreviewFragment();
				previewMatch.setArguments(bundle);
				FragmentTransaction tx=fm.beginTransaction();
				tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
				tx.replace(R.id.main, previewMatch);
				tx.addToBackStack(null);
				tx.commit();
			}
			else{
				PrematchStartFragment tmp=new PrematchStartFragment();
				tmp.setArguments(bundle);
				FragmentTransaction tx=fm.beginTransaction();
				tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
				tx.replace(R.id.main, tmp);
				tx.addToBackStack(null);
				tx.commit();
			}
			break;
		}
		case R.id.team_info_down:{
			ShooterAssisterFragment tmp=new ShooterAssisterFragment();
			Bundle bundle=new Bundle();
			bundle.putString("teamid", teamid);
			tmp.setArguments(bundle);
			FragmentTransaction tx=fm.beginTransaction();
			tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
			tx.replace(R.id.main, tmp);
			tx.addToBackStack(null);
			tx.commit();
			break;
		}
		case R.id.iv_team_1_image:{
			Bundle bundle0=new Bundle();
			bundle0.putInt("state", TitleFragment.TEAM_INFO_TITLE);
			bundle0.putString("teamid", teamid);
			bundle0.putString("authority", authority);
			TitleFragment teamInfoTitle=new TitleFragment();
			teamInfoTitle.setArguments(bundle0);
			TeamMemberList tmp=new TeamMemberList();
			Bundle bundle=new Bundle();
			bundle.putString("teamid", teamid);
			tmp.setArguments(bundle);
			FragmentTransaction tx=fm.beginTransaction();
			tx.replace(R.id.title, teamInfoTitle, "teaminfo");
			tx.replace(R.id.down, tmp);
			tx.addToBackStack(null);
			tx.commit();
			break;
		}
		case R.id.tv_hall_fame:{
			openFame(HallOfFameFragment.FIRST_OPEN, teamid, authority, null);
			break;
		}
		}
	}



	public void teamCreate(View v, boolean isCreatedTeam, TeamCreateFragment t) {
		// TODO Auto-generated method stub
		int id=v.getId();
		switch(id){
		case R.id.tv_newteam:{
			if(isCreatedTeam==false){
				t.setEnable(id, false);
				TeamCreateFragment tmp=new TeamCreateFragment();
				Bundle bundle=new Bundle();
				bundle.putBoolean("isCreatedTeam", false);
				bundle.putInt("resource", R.layout.fragment_team_create2);
				tmp.setArguments(bundle);
				teamMap=new HashMap<String, Object>();
				FragmentTransaction tx=fm.beginTransaction();
				tx.replace(R.id.content, tmp, "teamcreate_2");
				if(bar!=null && bar.isVisible()){
					tx.hide(bar);
				}
				tx.addToBackStack(null);
				tx.commit();
			}
			else{
				Toast.makeText(this, "您已创立过球队", Toast.LENGTH_LONG).show();
			}
			break;
		}
		case R.id.iv_team1:{
			sendImage(2);
			break;
		}
		case R.id.team_next2:{
			if(!teamMap.containsKey("image"))
				teamMap.put("image", "-1");
			Map<String, Object>tmp=((TeamCreateFragment)fm.findFragmentByTag("teamcreate_2")).getData(R.id.team_next2);
			if(tmp.get("name").toString().isEmpty()){
				Toast.makeText(this, "球队名不能为空", Toast.LENGTH_LONG).show();
				break;
			}
			else{
				t.setEnable(id, false);
				teamMap=Tools.addMap(teamMap, tmp);
				TeamCreateFragment tmpp=new TeamCreateFragment();
				Bundle bundle=new Bundle();
				bundle.putBoolean("isCreatedTeam", false);
				bundle.putInt("resource", R.layout.fragment_team_create3);
				tmpp.setArguments(bundle);
				FragmentTransaction tx=fm.beginTransaction();
				tx.replace(R.id.content, tmpp, "teamcreate_3");
				tx.addToBackStack(null);
				tx.commit();
				break;
			}
		}
		case R.id.team_next3:{
			Map<String, Object>tmp=((TeamCreateFragment)fm.findFragmentByTag("teamcreate_3")).getData(R.id.team_next3);
			if(tmp.get("city").toString().isEmpty()){
				Toast.makeText(this, "活跃城市不能为空", Toast.LENGTH_LONG).show();
				break;
			}
			else{
				t.setEnable(id, false);
				teamMap=Tools.addMap(teamMap, tmp);
				TeamCreateFragment tmpp=new TeamCreateFragment();
				Bundle bundle=new Bundle();
				bundle.putBoolean("isCreatedTeam", false);
				bundle.putInt("resource", R.layout.fragment_team_create4);
				tmpp.setArguments(bundle);
				FragmentTransaction tx=fm.beginTransaction();
				tx.replace(R.id.content, tmpp, "teamcreate_4");
				tx.addToBackStack(null);
				tx.commit();
				break;
			}
		}
		case R.id.team_next4:{
			Map<String, Object>tmp=((TeamCreateFragment)fm.findFragmentByTag("teamcreate_4")).getData(R.id.team_next4);
			if(tmp.get("year").toString().isEmpty()||tmp.get("month").toString().isEmpty()||tmp.get("day").toString().isEmpty()){
				Toast.makeText(this, "建队日期不能为空", Toast.LENGTH_LONG).show();
				break;
			}
			else{
				t.setEnable(id, false);
				teamMap=Tools.addMap(teamMap, tmp);
				TeamCreateFragment tmpp=new TeamCreateFragment();
				Bundle bundle=new Bundle();
				bundle.putBoolean("isCreatedTeam", false);
				bundle.putInt("resource", R.layout.fragment_team_create5);
				tmpp.setArguments(bundle);
				FragmentTransaction tx=fm.beginTransaction();
				tx.replace(R.id.content, tmpp, "teamcreate_5");
				tx.addToBackStack(null);
				tx.commit();
				break;
			}
		}
		case R.id.team_next5:{
			Map<String, Object>tmp=((TeamCreateFragment)fm.findFragmentByTag("teamcreate_5")).getData(R.id.team_next5);
			if(tmp.get("number").toString().isEmpty()){
				Toast.makeText(this, "人数不能为空", Toast.LENGTH_LONG).show();
				break;
			}
			else{
				t.setEnable(id, false);
				Map<String, Object> tmp1=pd.getData(new String[]{"phone"});
				openVague(WAIT_TEAM_CREATE);
				tmp1.put("phone", tmp1.get("phone")+"u");
				teamMap=Tools.addMap(teamMap, tmp1);
				teamMap=Tools.addMap(teamMap, tmp);
				teamMap.put("request", "create team");
				Runnable r=new ClientWrite(Tools.JsonEncode(teamMap));
				new Thread(r).start();
				break;
			}
		}
		}
	}



	
	public void openEvaluate(String phone, Bitmap bitmap, String name, String info, String number){
		EvaluationCapacityFragment evaluateCapacity=new EvaluationCapacityFragment();
		Bundle bundle=new Bundle();
		bundle.putString("phone", phone);
		bundle.putString("name", name);
		bundle.putString("info", info);
		bundle.putString("number", number);
		bundle.putString("image", Tools.bitmapToString(bitmap));
		evaluateCapacity.setArguments(bundle);
		FragmentTransaction tx=fm.beginTransaction();
		tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
		tx.replace(R.id.main, evaluateCapacity);
		tx.addToBackStack(null);
		tx.commit();
	}
	
	
	
	public void onHomePageClick(View v, String phone) {
		// TODO Auto-generated method stub
		bar.setEnable(false);
		if(fm.findFragmentById(R.id.title) instanceof TitleFragment){
			TitleFragment tmp=(TitleFragment)fm.findFragmentById(R.id.title);
			tmp.setEnable(false);
		}
		switch(v.getId()){
		case R.id.rl_file:{
			showFile();
			break;
		}
		case R.id.rl_skills:{
			if(phone.equals("host")){
				phone=this.phone;
			}
			openShowSkills(phone);
			break;
		}
		case R.id.btn_mycapacity:{
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("request", "evaluate info");
			map.put("phone", this.phone);
			Runnable r=new ClientWrite(Tools.JsonEncode(map));
			new Thread(r).start();
			openVague(WAIT_EVALUATE_INFO);
			break;
		}
		case R.id.btn_info1:{
			CompleteInfoFragment completeInfo=new CompleteInfoFragment();
			Bundle bundle=new Bundle();
			bundle.putString("phone", phone);
			completeInfo.setArguments(bundle);
			FragmentTransaction tx=fm.beginTransaction();
			tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
			tx.replace(R.id.main, completeInfo);
			tx.addToBackStack(null);
			tx.commit();
			hideAll();
			break;
		}
		case R.id.btn_info2:{
			Bundle bundle0=new Bundle();
			bundle0.putInt("state", TitleFragment.KICKFOR_LIFE_TITLE);
			TitleFragment myKickForLifeTitle=new TitleFragment();
			myKickForLifeTitle.setArguments(bundle0);
			MyMatchFragment myMatch=new MyMatchFragment();
			Bundle bundle=new Bundle();
			bundle.putString("teamid", "all");
			bundle.putString("phone", phone);
			myMatch.setArguments(bundle);
			FragmentTransaction tx=fm.beginTransaction();
			tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
			tx.replace(R.id.title, myKickForLifeTitle);
			tx.replace(R.id.down, myMatch);
			tx.addToBackStack(null);
			tx.commit();
			hideDown();
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("request", "get ones match");
			map.put("phone", this.phone);
			map.put("index", 0);
			map.put("teamid", "all");
			Runnable r=new ClientWrite(Tools.JsonEncode(map));
			new Thread(r).start();
			break;
		}
		case R.id.btn_info3:{
			System.out.println("teamid1===="+teamid1);
			if(Tools.hasUnkickedMatch(this, teamid1)){
				Bundle bundle0=new Bundle();
				bundle0.putInt("state", TitleFragment.PREVIEW_MATCH);
				bundle0.putInt("status", 1);
				TitleFragment tmpTitle=new TitleFragment();
				tmpTitle.setArguments(bundle0);
				MatchPreviewFragment previewMatch=new MatchPreviewFragment();
				Bundle bundle=new Bundle();
				bundle.putString("teamid", teamid1);
				bundle.putInt("authority", -1);
				previewMatch.setArguments(bundle);
				FragmentTransaction tx=fm.beginTransaction();
				tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
				tx.replace(R.id.title, tmpTitle);
				tx.replace(R.id.down, previewMatch);
				tx.addToBackStack(null);
				tx.commit();
				hideDown();
			}
			else if(Tools.hasUnkickedMatch(this, teamid2)){
				System.out.println("222222222222222222222");
				Bundle bundle0=new Bundle();
				bundle0.putInt("state", TitleFragment.PREVIEW_MATCH);
				bundle0.putInt("status", 2);
				TitleFragment tmpTitle=new TitleFragment();
				tmpTitle.setArguments(bundle0);
				MatchPreviewFragment previewMatch=new MatchPreviewFragment();
				Bundle bundle=new Bundle();
				bundle.putString("teamid", teamid2);
				bundle.putInt("authority", -1);
				previewMatch.setArguments(bundle);
				FragmentTransaction tx=fm.beginTransaction();
				tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
				tx.replace(R.id.title, tmpTitle);
				tx.replace(R.id.down, previewMatch);
				tx.addToBackStack(null);
				tx.commit();
				hideDown();
			}
			else if(Tools.hasUnkickedMatch(this, teamid3)){
				Bundle bundle0=new Bundle();
				bundle0.putInt("state", TitleFragment.PREVIEW_MATCH);
				bundle0.putInt("status", 3);
				TitleFragment tmpTitle=new TitleFragment();
				tmpTitle.setArguments(bundle0);
				MatchPreviewFragment previewMatch=new MatchPreviewFragment();
				Bundle bundle=new Bundle();
				bundle.putString("teamid", teamid3);
				bundle.putInt("authority", -1);
				previewMatch.setArguments(bundle);
				FragmentTransaction tx=fm.beginTransaction();
				tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
				tx.replace(R.id.title, tmpTitle);
				tx.replace(R.id.down, previewMatch);
				tx.addToBackStack(null);
				tx.commit();
				hideDown();
			}
			else{
				Toast.makeText(this, "暂无下一场比赛预告", Toast.LENGTH_SHORT).show();
				bar.setEnable(true);
				if(fm.findFragmentById(R.id.title) instanceof TitleFragment){
					TitleFragment tmp=(TitleFragment)fm.findFragmentById(R.id.title);
					tmp.setEnable(true);
				}
				if(fm.findFragmentById(R.id.content) instanceof HomePageFragment){
					HomePageFragment tmp=(HomePageFragment)fm.findFragmentById(R.id.content);
					tmp.setEnable(true);
				}
			}
			break;
		}
		
		}
		
	}
	
	public void openPreview(String teamid){
		int status=0;
		if(teamid.equals(teamid1)){
			status=1;
		}
		else if(teamid.equals(teamid2)){
			status=2;
		}
		else if(teamid.equals(teamid3)){
			status=3;
		}
		if(Tools.hasUnkickedMatch(this, teamid)){
			Bundle bundle0=new Bundle();
			bundle0.putInt("state", TitleFragment.PREVIEW_MATCH);
			bundle0.putInt("status", status);
			bundle0.putBoolean("nonext", true);
			TitleFragment tmpTitle=new TitleFragment();
			tmpTitle.setArguments(bundle0);
			MatchPreviewFragment previewMatch=new MatchPreviewFragment();
			Bundle bundle=new Bundle();
			bundle.putString("teamid", teamid);
			bundle.putInt("authority", -1);
			previewMatch.setArguments(bundle);
			FragmentTransaction tx=fm.beginTransaction();
			tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
			tx.replace(R.id.title, tmpTitle);
			tx.replace(R.id.down, previewMatch);
			tx.addToBackStack(null);
			tx.commit();
		}
	}

	public void editFile(FileEntity entity) {
		FileEditFragment file = new FileEditFragment();
		Bundle bundle = new Bundle();
		bundle.putSerializable("entity", entity);
		file.setArguments(bundle);
		FragmentTransaction tx = fm.beginTransaction();
		tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
		tx.replace(R.id.main, file);
		tx.addToBackStack(null);
		tx.commit();
	}
	
	public void showFile() {
		FileShowFragment file = new FileShowFragment();
		FragmentTransaction tx = fm.beginTransaction();
		tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
		tx.replace(R.id.main, file);
		tx.addToBackStack(null);
		tx.commit();
	}
	
	public void selectFileList(List<FileEntity> list) {
		Bundle bundle=new Bundle();
		Iterator<FileEntity> iter=list.iterator();
		int i=1;
		while(iter.hasNext()){
			bundle.putSerializable(String.valueOf(i), iter.next());
			i++;
		}
		FileSelectFragment tmp = new FileSelectFragment();
		tmp.setArguments(bundle);
		FragmentTransaction tx = fm.beginTransaction();
		tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
		tx.replace(R.id.main, tmp);
		tx.addToBackStack(null);
		tx.commit();
	}



	
	
	public void openChat(String phone, String state){
		Bundle bundle=new Bundle();
		bundle.putString("phone", phone);
		bundle.putString("type", state);
		ChatFragment chatPage=new ChatFragment();
		chatPage.setArguments(bundle);
		FragmentTransaction tx=fm.beginTransaction();
		tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
		tx.replace(R.id.main, chatPage);
		tx.addToBackStack(null);
		tx.commit();
	}

	public void previewCommand(String act, int status) {
		if(act.equals("right")){
			while(status<=4){
				if(status==2){
					if(Tools.hasUnkickedMatch(this, teamid2)){
						MatchPreviewFragment previewMatch=new MatchPreviewFragment();
						TitleFragment tmpTitle=(TitleFragment)fm.findFragmentById(R.id.title);
						tmpTitle.setStatus(status);
						Bundle bundle=new Bundle();
						bundle.putString("teamid", teamid2);
						bundle.putInt("authority", -1);
						previewMatch.setArguments(bundle);
						FragmentTransaction tx=fm.beginTransaction();
						tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
						tx.replace(R.id.down, previewMatch);
						tx.addToBackStack(null);
						tx.commit();
						break;
					}
					else{
						status=status+1;
					}
				}
				else if(status==3){
					if(Tools.hasUnkickedMatch(this, teamid3)){
						TitleFragment tmpTitle=(TitleFragment)fm.findFragmentById(R.id.title);
						tmpTitle.setStatus(status);
						MatchPreviewFragment previewMatch=new MatchPreviewFragment();
						Bundle bundle=new Bundle();
						bundle.putString("teamid", teamid3);
						bundle.putInt("authority", -1);
						previewMatch.setArguments(bundle);
						FragmentTransaction tx=fm.beginTransaction();
						tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
						tx.replace(R.id.down, previewMatch);
						tx.addToBackStack(null);
						tx.commit();
						break;
					}
					else{
						status=status+1;
					}
				}
				else if(status==4){
					Toast.makeText(this, "已经是最后一页", Toast.LENGTH_LONG).show();
					status=status+1;
				}
			}	
		}
		else{
			while(status>=0){
				System.out.println("left:status="+status);
				if(status==2){
					if(Tools.hasUnkickedMatch(this, teamid2)){
						TitleFragment tmpTitle=(TitleFragment)fm.findFragmentById(R.id.title);
						tmpTitle.setStatus(status);
						MatchPreviewFragment previewMatch=new MatchPreviewFragment();
						Bundle bundle=new Bundle();
						bundle.putString("teamid", teamid2);
						bundle.putInt("authority", -1);
						previewMatch.setArguments(bundle);
						onBackPressed();
						break;
					}
					else{
						status=status-1;
					}
				}
				else if(status==1){
					if(Tools.hasUnkickedMatch(this, teamid3)){
						TitleFragment tmpTitle=(TitleFragment)fm.findFragmentById(R.id.title);
						tmpTitle.setStatus(status);
						MatchPreviewFragment previewMatch=new MatchPreviewFragment();
						Bundle bundle=new Bundle();
						bundle.putString("teamid", teamid3);
						bundle.putInt("authority", -1);
						previewMatch.setArguments(bundle);
						onBackPressed();
						break;
					}
					else{
						status=status-1;
					}
				}
				else if(status==0){
					onBackPressed();
					status=status-1;
				}
			}
		}
	}


	public void openEditTeamHistory(Bundle bundle){
		TeamInfoGradeEditFragment tmp=new TeamInfoGradeEditFragment();
		tmp.setArguments(bundle);
		FragmentTransaction tx=fm.beginTransaction();
		tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
		tx.replace(R.id.main, tmp);
		tx.addToBackStack(null);
		tx.commit();
	}
	
	public void openEditTeamHonor(Bundle bundle){
		EditTeamGradeFragment tmp=new EditTeamGradeFragment();
		tmp.setArguments(bundle);
		FragmentTransaction tx=fm.beginTransaction();
		tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
		tx.replace(R.id.main, tmp);
		tx.addToBackStack(null);
		tx.commit();
	}

	public void openChangingRoomManager(String teamid, String authority){
		Bundle bundle=new Bundle();
		bundle.putString("teamid", teamid);
		bundle.putString("authority", authority);
		ChangingRoomFragment tmp=new ChangingRoomFragment();
		tmp.setArguments(bundle);
		FragmentTransaction tx=fm.beginTransaction();
		tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
		tx.replace(R.id.main, tmp);
		tx.addToBackStack(null);
		tx.commit();
	}
	
	public void openEditReview(String teamid, MatchReviewEntity entity){
		Bundle bundle=new Bundle();
		bundle.putString("teamid", teamid);
		bundle.putSerializable("entity", entity);
		EditReviewFragment tmp=new EditReviewFragment();
		tmp.setArguments(bundle);
		FragmentTransaction tx=fm.beginTransaction();
		tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
		tx.replace(R.id.main, tmp);
		tx.addToBackStack(null);
		tx.commit();
	}


	public void titleCommand(View v, String extra1, String extra2) {
		int id=v.getId();
		switch(id){
		case R.id.lobby_team_add:{
			Bundle bundle=new Bundle();
			bundle.putString("phone", this.phone);
			LobbyTeamAddFragment add=new LobbyTeamAddFragment();
			add.setArguments(bundle);
			FragmentTransaction tx=fm.beginTransaction();
			tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
			tx.replace(R.id.main, add);
			tx.addToBackStack(null);
			tx.commit();
			break;
		}
		case R.id.lobby_friend_text:{
			
			break;
		}
		case R.id.lobby_team_text:{
			LobbyTeamFragment tmp=new LobbyTeamFragment();
			FragmentTransaction tx=fm.beginTransaction();
			tx.replace(R.id.content, tmp);
			tx.commit();
			break;
		}
		case R.id.btn_myhomepage:{
			HomePageFragment homepage=new HomePageFragment();
			Bundle bundle=new Bundle();
			bundle.putString("phone", "host");
			homepage.setArguments(bundle);
			FragmentTransaction tx=fm.beginTransaction();
			tx.replace(R.id.content, homepage);
			tx.commit();
			break;
		}
		case R.id.btn_myMessage:{
			Bundle bundle=new Bundle();
			bundle.putInt("state", ListsFragment.TYPE_MESSAGE_LIST);
			ListsFragment myMessages=new ListsFragment();
			myMessages.setArguments(bundle);
			FragmentTransaction tx=fm.beginTransaction();
			tx.replace(R.id.content, myMessages);
			tx.commit();
			break;
		}
		case R.id.btn_myFriends:{
			Bundle bundle=new Bundle();
			bundle.putInt("state", ListsFragment.TYPE_FRIEND_LIST);
			ListsFragment myFriends=new ListsFragment();
			myFriends.setArguments(bundle);
			FragmentTransaction tx=fm.beginTransaction();
			tx.replace(R.id.content, myFriends);
			tx.commit();
			break;
		}
		case R.id.team_info_member:{
			String teamid=extra1;
			TeamMemberList tmp=new TeamMemberList();
			Bundle bundle=new Bundle();
			bundle.putString("teamid", teamid);
			tmp.setArguments(bundle);
			FragmentTransaction tx=fm.beginTransaction();
			tx.replace(R.id.down, tmp, "team_info_member");
			tx.addToBackStack(null);
			tx.commit();
			break;
		}
		case R.id.team_info_grade:{
			String teamid=extra1;
			String authority=extra2;
			TeamInfoGradeFragment tmp=new TeamInfoGradeFragment();
			Bundle bundle=new Bundle();
			bundle.putString("teamid", teamid);
			bundle.putString("authority", authority);
			tmp.setArguments(bundle);
			FragmentTransaction tx=fm.beginTransaction();
			tx.replace(R.id.down, tmp, "team_info_grade");
			tx.addToBackStack(null);
			tx.commit();
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("request", "get grade");
			map.put("teamid", teamid);
			Runnable r=new ClientWrite(Tools.JsonEncode(map));
			new Thread(r).start();
			break;
		}
		case R.id.kickfor_all:{
			MyMatchFragment tmp=(MyMatchFragment)fm.findFragmentById(R.id.down);
			tmp.setIndex(0);
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("request", "get ones match");
			map.put("phone", this.phone);
			map.put("teamid", extra1);
			map.put("index", 0);
			Runnable r=new ClientWrite(Tools.JsonEncode(map));
			new Thread(r).start();
			break;
		}
		case R.id.kickfor_1:{
			MyMatchFragment tmp=(MyMatchFragment)fm.findFragmentById(R.id.down);
			tmp.setIndex(0);
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("request", "get ones match");
			map.put("phone", this.phone);
			map.put("teamid", extra1);
			map.put("index", 0);
			Runnable r=new ClientWrite(Tools.JsonEncode(map));
			new Thread(r).start();
			break;
		}
		case R.id.kickfor_2:{
			MyMatchFragment tmp=(MyMatchFragment)fm.findFragmentById(R.id.down);
			tmp.setIndex(0);
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("request", "get ones match");
			map.put("phone", this.phone);
			map.put("teamid", extra1);
			map.put("index", 0);
			Runnable r=new ClientWrite(Tools.JsonEncode(map));
			new Thread(r).start();
			break;
		}
		case R.id.kickfor_3:{
			MyMatchFragment tmp=(MyMatchFragment)fm.findFragmentById(R.id.down);
			tmp.setIndex(0);
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("request", "get ones match");
			map.put("phone", this.phone);
			map.put("teamid", extra1);
			map.put("index", 0);
			Runnable r=new ClientWrite(Tools.JsonEncode(map));
			new Thread(r).start();
			break;
		}
		}
	}


	public void resetViewPager(){
		if(isViewPager){
			mAdapter.notifyDataSetChanged();
			isViewPager=false;
		}
		viewPager.setVisibility(View.VISIBLE);
		
	}
	
	public void closeViewPager(){
		viewPager.setVisibility(View.GONE);
	}

	
	public void sendImage(int selectImage) {
		// TODO Auto-generated method stub
		this.selectImage=selectImage;
		MyPopupWindow popWindow=new MyPopupWindow(this);
		backgroundAlpha(0.4f);
		popWindow.showAtLocation(this.findViewById(R.id.content), Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL, 0, 0);
		popWindow.setOnDismissListener(new OnDismissListener(){  
            public void onDismiss() {  
            	backgroundAlpha(1.0f);             
            }             
        });  

	}

	public void openLobbyTeamReply(Bundle bundle){
		FragmentTransaction tx=fm.beginTransaction();
		LobbyTeamReplyFragment tmp=new LobbyTeamReplyFragment();
		tmp.setArguments(bundle);
		tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
		tx.replace(R.id.main, tmp);
		tx.addToBackStack(null);
		tx.commit();
	}
	
	public void getInPosition(String position, int state) {
		// TODO Auto-generated method stub
		Bundle bundle=new Bundle();
		bundle.putString("position", position);
		bundle.putString("phone", phone);
		bundle.putInt("state", state);
		SelectPositionFragment selectPosition=new SelectPositionFragment();
		selectPosition.setArguments(bundle);
		FragmentTransaction tx=fm.beginTransaction();
		tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
		tx.replace(R.id.main, selectPosition);
		tx.addToBackStack(null);
		tx.commit();
		System.out.println("select position");
	}




	public void openSkillsDetail(String skillskey) {
		Bundle bundle=new Bundle();
		bundle.putString("skillskey", skillskey);
		bundle.putString("phone", this.phone);
		SkillsDetailFragment skills = new SkillsDetailFragment();
		skills.setArguments(bundle);
		FragmentTransaction tx = fm.beginTransaction();
		tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
		tx.replace(R.id.main, skills);
		tx.addToBackStack(null);
		tx.commit();
	}
	
	public void openShowSkills(String phone) {
		Bundle bundle=new Bundle();
		bundle.putString("phone", phone);
		SkillsShowFragment skills = new SkillsShowFragment();
		skills.setArguments(bundle);
		FragmentTransaction tx = fm.beginTransaction();
		tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left,
				R.animator.slide_out_right);
		tx.replace(R.id.main, skills);
		tx.addToBackStack(null);
		tx.commit();
	}
	
	public void openSelectSkills() {
		SkillsSelectFragment skills = new SkillsSelectFragment();
		FragmentTransaction tx = fm.beginTransaction();
		tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left,
				R.animator.slide_out_right);
		tx.replace(R.id.main, skills);
		tx.addToBackStack(null);
		tx.commit();
	}

	
	
	/**
	 * CUT IMAGE METHOD
	 */
	public void startPhotoZoom(Uri uri){
		Intent intent=new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri,"image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX", 70);
		intent.putExtra("outputY", 70);
		intent.putExtra("return-data", true);
		startActivityForResult(intent, 2);
	}
	
	/**
	 * SAVE CUT IMAGE
	 */
	private void getImageToView(Intent data){
		Bundle extras=data.getExtras();
		if(extras!=null){
			bitmap=extras.getParcelable("data");
			if(selectImage==1){
				Map<String, Object> send=new HashMap<String, Object>();
				send.put("request", "upload photo");
				send.put("phone", phone);
				send.put("image", Tools.bitmapToString(bitmap));
				Runnable r=new ClientWrite(Tools.JsonEncode(send));
				new Thread(r).start();
			}
			else if(selectImage==2){
				if(fm.findFragmentById(R.id.content) instanceof TeamCreateFragment && ((TeamCreateFragment)fm.findFragmentById(R.id.content)).getResourceId()==R.layout.fragment_team_create2){
					((TeamCreateFragment)fm.findFragmentById(R.id.content)).setImage(bitmap);
					teamMap.put("image", Tools.bitmapToString(bitmap));
				}
			}
			else if(selectImage==3){
				if(fm.findFragmentById(R.id.main) instanceof HallOfFameFragment){
					HallOfFameFragment tmp=(HallOfFameFragment)fm.findFragmentById(R.id.main);
					tmp.setImage(bitmap);
				}
			}
			else if(selectImage==4){
				if(fm.findFragmentById(R.id.main) instanceof TeamInfoEditFragment){
					TeamInfoEditFragment tmp=(TeamInfoEditFragment)fm.findFragmentById(R.id.main);
					tmp.setImage(bitmap);
				}
			}
			
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		switch(requestCode){
		case IMAGE_REQUEST_CODE:
			if(data!=null && data.getData()!=null){
				startPhotoZoom(data.getData());
			}
			break;
		case CAMERA_REQUEST_CODE:
			if(Tools.hasSdcard()){
				File tempFile=new File(Environment.getExternalStorageDirectory()+"/"+IMAGE_FILE_NAME);
				startPhotoZoom(Uri.fromFile(tempFile));
			}
			else{
				Toast.makeText(HomePageActivity.this, "未找到存储卡，无法存储照片", Toast.LENGTH_LONG).show();
			}
			break;
		case RESULT_REQUEST_CODE:
			if(data!=null){
				getImageToView(data);
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}





	


	public void selectList(MyFriend item) {
		// TODO Auto-generated method stub
		int id=Integer.parseInt(item.getType());
		switch(id){
		case ListsFragment.TYPE_FRIEND_LIST:{
			HomePageFragment friendHomePage=new HomePageFragment();
			Bundle bundle=new Bundle();
			bundle.putString("phone", item.getPhone());
			friendHomePage.setArguments(bundle);
			FragmentTransaction tx=fm.beginTransaction();
			tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
			tx.replace(R.id.main, friendHomePage);
			tx.addToBackStack(null);
			tx.commit();
			break;
		}
		case ListsFragment.TYPE_FRIEND_APPLY_ALL:{
			Bundle bundle=new Bundle();
			bundle.putInt("state", ListsFragment.TYPE_FRIEND_APPLY);
			ListsFragment listPage=new ListsFragment();
			listPage.setArguments(bundle);
			FragmentTransaction tx=fm.beginTransaction();
			tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
			tx.replace(R.id.main, listPage);
			tx.addToBackStack(null);
			tx.commit();
			hideAll();
			break;
		}
		case ListsFragment.TYPE_SYSTEM_MESSAGE_ALL:{
			Bundle bundle=new Bundle();
			bundle.putInt("state", ListsFragment.TYPE_SYSTEM_MESSAGE);
			ListsFragment listPage=new ListsFragment();
			listPage.setArguments(bundle);
			FragmentTransaction tx=fm.beginTransaction();
			tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
			tx.replace(R.id.main, listPage);
			tx.addToBackStack(null);
			tx.commit();
			hideAll();
			break;
		}
		case ListsFragment.TYPE_TEAMS_MESSAGE_ALL:{
			Bundle bundle=new Bundle();
			bundle.putInt("state", ListsFragment.TYPE_TEAMS_MESSAGE);
			ListsFragment listPage=new ListsFragment();
			listPage.setArguments(bundle);
			FragmentTransaction tx=fm.beginTransaction();
			tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
			tx.replace(R.id.main, listPage);
			tx.addToBackStack(null);
			tx.commit();
			hideAll();
			break;
		}
		case ListsFragment.TYPE_FRIEND_MESSAGE:{
			openChat(item.getPhone(), ChatFragment.PEORSON_CHAT);
			hideAll();
			needShow=true;
			break;
		}
		case ListsFragment.TYPE_TEAM1_CHANGINGROOM:{
			String teamid=item.getPhone();
			openGroupChat(item.getPhone());
			hideAll();
			needShow=true;
			break;
		}
		case ListsFragment.TYPE_TEAM2_CHANGINGROOM:{
			openGroupChat(item.getPhone());
			hideAll();
			needShow=true;
			break;
		}
		case ListsFragment.TYPE_TEAM3_CHANGINGROOM:{
			openGroupChat(item.getPhone());
			hideAll();
			needShow=true;
			break;
		}
		}
		
	}
	
	public void openGroupChat(String teamid){
		String authority=null;
		if(teamid.equals(teamid1)){
			authority=authority1;
		}
		else if(teamid.equals(teamid2)){
			authority=authority2;
		}
		else if(teamid.equals(teamid3)){
			authority=authority3;
		}
		Bundle bundle=new Bundle();
		bundle.putString("phone", teamid);
		bundle.putString("type", "0");
		bundle.putString("authority", authority);
		ChatFragment changingRoomChat=new ChatFragment();
		changingRoomChat.setArguments(bundle);
		FragmentTransaction tx=fm.beginTransaction();
		tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
		tx.replace(R.id.main, changingRoomChat);
		tx.addToBackStack(null);
		tx.commit();
	}
	
	public void aboutus(){
		AboutusFragment aboutus = new AboutusFragment();
		FragmentTransaction tx=fm.beginTransaction();
		tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
		tx.replace(R.id.main, aboutus);
		tx.addToBackStack(null);
		tx.commit();
	}

	public void feedback(){
		FeedbackFragment feedback = new FeedbackFragment();
		FragmentTransaction tx=fm.beginTransaction();
		tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
		tx.replace(R.id.main, feedback);
		tx.addToBackStack(null);
		tx.commit();
	}

	
	public void openFame(boolean type, String teamid, String authority, HallofFame entity){
		if(type==HallOfFameFragment.FIRST_OPEN){
			openVague(WAIT_TEAM);
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("request", "get fame");
			map.put("teamid", teamid);
			Runnable r=new ClientWrite(Tools.JsonEncode(map));
			new Thread(r).start();
		}
		else{
			HallOfFameFragment tmp=new HallOfFameFragment();
			Bundle bundle=new Bundle();
			bundle.putString("teamid", teamid);
			bundle.putString("authority", authority);
			bundle.putInt("resource", R.layout.add_hall_of_fame);
			if(entity!=null){
				bundle.putSerializable("entity", entity);
			}
			tmp.setArguments(bundle);
			FragmentTransaction tx=fm.beginTransaction();
			tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
			tx.replace(R.id.main, tmp);
			tx.addToBackStack(null);
			tx.commit();
		}
	}

	public void reviewDetail(String teamid, MatchReviewEntity item, String authority) {
		// TODO Auto-generated method stub
		MatchReviewDetailFragment matchReviewDetail=new MatchReviewDetailFragment();
		Bundle bundle=new Bundle();
		bundle.putString("teamid", teamid);
		bundle.putSerializable("entity", item);
		bundle.putString("authority", authority);
		matchReviewDetail.setArguments(bundle);
		FragmentTransaction tx=fm.beginTransaction();
		tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
		tx.replace(R.id.main, matchReviewDetail);
		tx.addToBackStack(null);
		tx.commit();
	}


	public void openFindPasswords(Bundle bundle){
		FindPasswordsFragment tmp=new FindPasswordsFragment();
		tmp.setArguments(bundle);
		FragmentTransaction tx=fm.beginTransaction();
		if(fm.findFragmentById(R.id.main) instanceof SettingsFragment){
			tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
			tx.replace(R.id.main, tmp);
		}
		else{
			tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
			tx.replace(R.id.content, tmp);
		}
		tx.addToBackStack(null);
		tx.commit();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		mRealTimeHandler.regist(HomePageActivity.this);
		EMChatManager.getInstance().registerEventListener(listener);	
		JPushInterface.onResume(HomePageActivity.this);
	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		mRealTimeHandler.unRegist(HomePageActivity.this);
		EMChatManager.getInstance().unregisterEventListener(listener);
		JPushInterface.onPause(HomePageActivity.this);
	}



	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		EMChatManager.getInstance().logout();//此方法为同步方法
		System.out.println("环信已关闭");
		unregisterReceiver(networkReceiver);
		SQLHelper helper=SQLHelper.getInstance(this);
		helper.closeDb();
		if(isSocketClose==false){
			closeSocket();
			isSocketClose=true;
		}
		mTimer.cancel();
		System.out.println("on destroy on destroy");
	}
	
	
 

	
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		if(intent!=null){
			String type="";
			if(intent.hasExtra("type")){
				type=intent.getStringExtra("type");
				if(type.equals(ChatFragment.PEORSON_CHAT)){
					Bundle bundle2=new Bundle();
					bundle2.putInt("state", ListsFragment.TYPE_MESSAGE_LIST);
					ListsFragment myMessages=new ListsFragment();
					myMessages.setArguments(bundle2);
					FragmentTransaction tx=fm.beginTransaction();
					tx.replace(R.id.content, myMessages);
					tx.commit();
					String phone=intent.getStringExtra("phone");
					openChat(phone, ChatFragment.PEORSON_CHAT);
				}
				else if(type.equals(ChatFragment.GROUP_CHAT)){
					Bundle bundle2=new Bundle();
					bundle2.putInt("state", ListsFragment.TYPE_MESSAGE_LIST);
					ListsFragment myMessages=new ListsFragment();
					myMessages.setArguments(bundle2);
					FragmentTransaction tx=fm.beginTransaction();
					tx.replace(R.id.content, myMessages);
					tx.commit();
					String groupid=intent.getStringExtra("groupid");
					String teamid=null;
					Map<String, Object> map=new PreferenceData(this).getData(new String[]{teamid1, teamid2, teamid3});
					
					if(map.containsKey(teamid1) && map.get(teamid1).toString().equals(groupid)){
						teamid=teamid1;
					}
					else if(map.containsKey(teamid2) && map.get(teamid2).toString().equals(groupid)){
						teamid=teamid2;
					}
					else if(map.containsKey(teamid3) && map.get(teamid3).toString().equals(groupid)){
						teamid=teamid3;
					}
					System.out.println("teamid  groupid===="+teamid);
					openGroupChat(teamid);
				}
				else if(type.equals("update_review")){
					String teamid=intent.getStringExtra("teamid");
					String id=intent.getStringExtra("id");
					Map<String, Object> map=new HashMap<String, Object>();
					map.put("request", "get match by id");
					map.put("id", id);
					map.put("teamid", teamid);
					Runnable r=new ClientWrite(Tools.JsonEncode(map));
					new Thread(r).start();	
				}
				else if(type.equals("new_match")){
					String teamid=intent.getStringExtra("teamid");
					back();
					//bar.initChecked(R.id.bar_home);
					openPreview(teamid);
				}
				else if(type.equals("someone_join")){
					String teamid=intent.getStringExtra("teamid");
					back();
					//bar.initChecked(R.id.bar_team);
					openTeams(teamid);
					String authority="";
					if(teamid.endsWith(teamid1)){
						authority=authority1;
					}
					else if(teamid.endsWith(teamid2)){
						authority=authority2;
					}
					else if(teamid.endsWith(teamid3)){
						authority=authority3;
					}
					openChangingRoomManager(teamid, authority);
				}
			}
			
		}
		intent.removeExtra("type");
	}

	public void backgroundAlpha(float bgAlpha){ 
		WindowManager.LayoutParams lp = getWindow().getAttributes(); 
		lp.alpha = bgAlpha;
        getWindow().setAttributes(lp);
	}

	
	public static void getGroupid(){
		Tools.getGroupIdForHuan(mRealTimeHandler.getHandler(), new String[]{teamid1, teamid2, teamid3});
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void onChange(Message msg) {
		// TODO Auto-generated method stub
		switch (msg.what){
		case RESPONSE_PHONE:{
			removeVague();
			Bundle bundle=msg.getData();
			bundle.putInt("state", RegisterFragment.IDENTIFYING_CODE);
			RegisterFragment register=new RegisterFragment();
			register.setArguments(bundle);
			FragmentTransaction tx=fm.beginTransaction();
			tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
			tx.replace(R.id.content, register);
			tx.addToBackStack(null);
			tx.commit();
			break;
		}
		case RESPONSE_CODE:{
			removeVague();
			Bundle bundle=msg.getData();
			bundle.putInt("state", RegisterFragment.NAME);
			RegisterFragment register=new RegisterFragment();
			register.setArguments(bundle);
			FragmentTransaction tx=fm.beginTransaction();
			tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
			tx.replace(R.id.content, register);
			tx.addToBackStack(null);
			tx.commit();
			break;
		}
		case RESPONSE_NAME:{
			removeVague();
			Bundle bundle=msg.getData();
			Bundle bundle0=new Bundle();
			bundle0.putString("phone", bundle.getString("phone"));
			bundle0.putString("position", "");
			bundle0.putInt("state", 1);
			SelectPositionFragment selectPosition=new SelectPositionFragment();
			selectPosition.setArguments(bundle0);
			FragmentTransaction tx=fm.beginTransaction();
			tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
			tx.replace(R.id.content, selectPosition);
			tx.addToBackStack(null);
			tx.commit();
			break;
		}
		case RESPONSE_POSITION1:{
			removeVague();
			Bundle bundle=msg.getData();
			Bundle bundle0=new Bundle();
			bundle0.putString("phone", bundle.getString("phone"));
			bundle0.putString("position", "");
			bundle0.putInt("state", 2);
			SelectPositionFragment selectPosition=new SelectPositionFragment();
			selectPosition.setArguments(bundle0);
			FragmentTransaction tx=fm.beginTransaction();
			tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
			tx.replace(R.id.content, selectPosition);
			tx.addToBackStack(null);
			tx.commit();
			break;
		}
		
		case RESPONSE_POSITION2:{
			removeVague();
			Bundle bundle=msg.getData();
			bundle.putInt("state", RegisterFragment.CITY_DISTRICT);
			RegisterFragment register=new RegisterFragment();
			register.setArguments(bundle);
			FragmentTransaction tx=fm.beginTransaction();
			tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
			tx.replace(R.id.content, register);
			tx.addToBackStack(null);
			tx.commit();
			break;
		}
		case RESPONSE_CITY:{
			removeVague();
			Bundle bundle=msg.getData();
			bundle.putInt("state", RegisterFragment.COMPLETE);
			RegisterFragment register=new RegisterFragment();
			register.setArguments(bundle);
			FragmentTransaction tx=fm.beginTransaction();
			tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
			tx.replace(R.id.content, register);
			tx.addToBackStack(null);
			tx.commit();
			break;
		}
		
		case LOGIN_SUCCESS:{
			Bundle bundle=msg.getData();
			phone=pd.getData(new String[]{"phone"}).get("phone").toString();
			teamid1=bundle.getString("teamid1");
			teamid2=bundle.getString("teamid2");
			teamid3=bundle.getString("teamid3");
			Tools.getGroupIdForHuan(mRealTimeHandler.getHandler(), new String[]{teamid1, teamid2, teamid3});
			authority1=bundle.getString("authority1");
			authority2=bundle.getString("authority2");
			authority3=bundle.getString("authority3");
			Intent intent=getIntent();
			if(intent.hasExtra("type")){
				System.out.println("...................");
				String type=intent.getStringExtra("type");
				if(type.equals("update_review")){
					String teamid=intent.getStringExtra("teamid");
					String id=intent.getStringExtra("id");
					
					Map<String, Object> map=new HashMap<String, Object>();
					map.put("request", "get match by id");
					map.put("id", id);
					map.put("teamid", teamid);
					Runnable r=new ClientWrite(Tools.JsonEncode(map));
					new Thread(r).start();
					intent.removeExtra("type");
				}
				else if(type.equals("new_match")){
					String teamid=intent.getStringExtra("teamid");
					bar=new BarFragment();
					Bundle bundle0=new Bundle();
					bundle0.putInt("status", R.id.bar_home);
					bar.setArguments(bundle0);
					FragmentTransaction tx=fm.beginTransaction();
					tx.replace(R.id.bar, bar);
					tx.commit();
					isInitTeam=initTeam(isInitTeam);
					viewPager.setVisibility(View.GONE);
					openPreview(teamid);
				}
				else if(type.equals("someone_join")){
					String teamid=intent.getStringExtra("teamid");
					back();
					bar.initChecked(R.id.bar_team);
					openTeams(teamid);
					String authority="";
					if(teamid.endsWith(teamid1)){
						authority=authority1;
					}
					else if(teamid.endsWith(teamid2)){
						authority=authority2;
					}
					else if(teamid.endsWith(teamid3)){
						authority=authority3;
					}
					openChangingRoomManager(teamid, authority);
				}
			}
			else{
				if(networkHasChanged==false){
					//back();
					System.out.println("22222222222222222222222222222222222222222222222");
					bar=new BarFragment();
					Bundle bundle0=new Bundle();
					bundle0.putInt("state", TitleFragment.HOMEPAGE_TITLE);
					TitleFragment homepageTitle=new TitleFragment();
					homepageTitle.setArguments(bundle0);
					HomePageFragment homepage=new HomePageFragment();
					Bundle bundle1=new Bundle();
					bundle1.putString("phone", "host");
					homepage.setArguments(bundle1);
					isInitTeam=initTeam(isInitTeam);
					viewPager.setVisibility(View.GONE);
					FragmentTransaction transaction=fm.beginTransaction();
					transaction.replace(R.id.bar, bar);
					transaction.replace(R.id.title, homepageTitle);
					transaction.replace(R.id.content, homepage);
					transaction.commit();
					removeVague();
				}
				else{
					System.out.println("33333333333333333333333333333333333333333333");
					removeVague();
					if(viewPager!=null && fragmentList1!=null && fragmentList1.size()>0){
						Iterator<Fragment> iter=fragmentList1.iterator();
						while(iter.hasNext()){
							Fragment f=iter.next();
							if(f instanceof IdentificationInterface){
								IdentificationInterface tmp=(IdentificationInterface)f;
								tmp.setEnable(true);
							}
							if(f instanceof TeamFragment){
								TeamFragment tmp=(TeamFragment)f;
								tmp.setNetWorkCheckOpen(false);
							}
						}
					}
					
					if(fm.findFragmentById(R.id.title)!=null && fm.findFragmentById(R.id.title) instanceof IdentificationInterface){
						((IdentificationInterface)fm.findFragmentById(R.id.title)).setEnable(true);
						if(fm.findFragmentById(R.id.title) instanceof TitleFragment){
							TitleFragment tmp=(TitleFragment)fm.findFragmentById(R.id.title);
							tmp.setNetWorkCheckOpen(false);
						}
					}
					
					if(fm.findFragmentById(R.id.main)!=null && fm.findFragmentById(R.id.main) instanceof IdentificationInterface){
						((IdentificationInterface)fm.findFragmentById(R.id.main)).setEnable(true);
					}
					
					if(fm.findFragmentById(R.id.bar)!=null && fm.findFragmentById(R.id.bar) instanceof IdentificationInterface){
						((IdentificationInterface)fm.findFragmentById(R.id.bar)).setEnable(true);
					}
					
					if(fm.findFragmentById(R.id.content)!=null && fm.findFragmentById(R.id.content) instanceof IdentificationInterface){
						((IdentificationInterface)fm.findFragmentById(R.id.content)).setEnable(true);
					}
					
					if(fm.findFragmentById(R.id.down)!=null && fm.findFragmentById(R.id.down) instanceof IdentificationInterface){
						((IdentificationInterface)fm.findFragmentById(R.id.down)).setEnable(true);
					}
				}
			}
			
			break;	
		}
		case DELETE_FRIEND:{
			if(fm.findFragmentById(R.id.content) instanceof ListsFragment && ((ListsFragment)fm.findFragmentById(R.id.content)).getState()==ListsFragment.TYPE_FRIEND_LIST){
				ListsFragment tmp=(ListsFragment)fm.findFragmentById(R.id.content);
				tmp.changedData();
			}
			break;
		}
		case TEAM_CREATED:{
			try{
				Map<String, Object> tmp=new HashMap<String, Object>();
				String teamid=teamMap.get("phone").toString();
				String teamIndex=null;
				String authorityIndex=null;
				int index=0;
				if(teamid3.isEmpty()){
					index=2;
					teamIndex="team3";
					authorityIndex="authority3";
				}
				if(teamid2.isEmpty()){
					index=1;
					teamIndex="team2";
					authorityIndex="authority2";
				}
				if(teamid1.isEmpty()){
					index=0;
					teamIndex="team1";
					authorityIndex="authority1";
				}
				tmp.put(teamIndex, teamid);
				tmp.put(authorityIndex, "4");
				SQLHelper helper=SQLHelper.getInstance(this);
				helper.update(Tools.getContentValuesFromMap(tmp, null), "ich", "host");
				Cursor cursor=helper.select("ich", new String[]{"team1", "team2", "team3", "authority1", "authority2", "authority3"},  "phone=?", new String[]{"host"}, null);
				cursor.moveToNext();
				teamid1=cursor.getString(0);
				teamid2=cursor.getString(1);
				teamid3=cursor.getString(2);
				Tools.getGroupIdForHuan(mRealTimeHandler.getHandler(), new String[]{teamid1, teamid2, teamid3});
				authority1=cursor.getString(3);
				authority2=cursor.getString(4);
				authority3=cursor.getString(5);
				teamMap.remove("request");
				teamMap.put("teamid", teamMap.get("phone"));
				teamMap.remove("phone");
				teamMap.put("win", "0");
				teamMap.put("goal", "0");
				teamMap.put("assist", "0");
				teamMap.put("lost", "0");
				teamMap.put("totalmatch", "0");
				teamMap.put("honor", "000000000000000000000000000000");
				if(!(teamMap.get("image").toString().equals("-1"))){
					if(Tools.hasSdcard()){
						String fileName=Environment.getExternalStorageDirectory().getPath()+"/KICKFOR/teams/"+teamMap.get("phone")+".png";
						Tools.saveBitmapToFile(bitmap, fileName);
						teamMap.put("image", fileName);
					}
					else{
						Toast.makeText(this, "未检测到SD卡", Toast.LENGTH_LONG).show();
					}
				}
				helper.insert(Tools.getContentValuesFromMap(teamMap, null), "teams");
				//back();
				removeVague();
				FragmentTransaction tx=fm.beginTransaction();
				tx.show(bar);
				tx.commit();
				fragmentList1.remove(index);
				TeamFragment tmpp=new TeamFragment();
				Bundle bundle=new Bundle();
				bundle.putString("teamid", teamid);
				bundle.putString("authority", "4");
				tmpp.setArguments(bundle);
				fragmentList1.add(index, tmpp);
				mAdapter.notifyDataSetChanged();
				resetViewPager();
				teamMap.clear();
			}catch(Exception e){
				e.printStackTrace();
			}
			
			break;
		}
		case UPLOADED_PHOTO:{
			if(fm.findFragmentById(R.id.main) instanceof CompleteInfoFragment){
				CompleteInfoFragment tmp=(CompleteInfoFragment)fm.findFragmentById(R.id.main);
				tmp.changedData();
			}
			break;
		}
		case COMPLETED_INFORMATION:{
			if(msg.arg1==1){
				Bundle bundle=msg.getData();
				teamid1=bundle.getString("teamid1");
				teamid2=bundle.getString("teamid2");
				teamid3=bundle.getString("teamid3");
				authority1=bundle.getString("authority1");
				authority2=bundle.getString("authority2");
				authority3=bundle.getString("authority3");
				initTeam(false);
			}
			removeVague();
			if(fm.findFragmentById(R.id.main) instanceof CompleteInfoFragment){
				showAll();
				((HomePageFragment)fm.findFragmentById(R.id.content)).updateView();
				((HomePageFragment)fm.findFragmentById(R.id.content)).setEnable(true);
				if(fm.findFragmentById(R.id.title) instanceof TitleFragment){
					TitleFragment tmp=(TitleFragment)fm.findFragmentById(R.id.title);
					tmp.setEnable(true);
				}
				bar.setEnable(true);
				backPressed();
			}
			break;
		}
		case COMPLETED_POSITION:{
			if(fm.findFragmentById(R.id.main) instanceof SelectPositionFragment){
				onBackPressed();
			}
			break;
		}
		case SEEKED:{
			List<SearchItemEntity> list=(List<SearchItemEntity>)msg.obj;
			if(fm.findFragmentById(R.id.content) instanceof SearchItemFragment){
				SearchItemFragment searchList=(SearchItemFragment)fm.findFragmentById(R.id.content);
				searchList.changedData(list);
			}
			break;	
		}
		case APPLY_FRIEND:{
			if(fm.findFragmentById(R.id.content) instanceof ListsFragment && ((ListsFragment)fm.findFragmentById(R.id.content)).getState()==ListsFragment.TYPE_MESSAGE_LIST){
				ListsFragment tmp=(ListsFragment)fm.findFragmentById(R.id.content);
				tmp.changedData();
			}
			break;
		}
		case UPDATE_FRIEND_LIST:{
			if(fm.findFragmentById(R.id.content) instanceof ListsFragment && ((ListsFragment)fm.findFragmentById(R.id.content)).getState()==ListsFragment.TYPE_FRIEND_LIST){
				ListsFragment tmp=(ListsFragment)fm.findFragmentById(R.id.content);
				tmp.changedData();
			}
			break;
		}
		case SEEKED_TEAM:{
			if(fm.findFragmentById(R.id.main) instanceof EditPreviewFragment){
				EditPreviewFragment tmp=(EditPreviewFragment)fm.findFragmentById(R.id.main);
				tmp.changedData((List<SearchItemEntity>)msg.obj);
			}
			if(fm.findFragmentById(R.id.main) instanceof EditReviewFragment){
				EditReviewFragment tmp=(EditReviewFragment)fm.findFragmentById(R.id.main);
				tmp.changedData((List<SearchItemEntity>)msg.obj);
			}
			if(fm.findFragmentById(R.id.content) instanceof SearchItemFragment){
				SearchItemFragment tmp=(SearchItemFragment)fm.findFragmentById(R.id.content);
				tmp.changedData((List<SearchItemEntity>)msg.obj);
			}
			break;
		}
		case TEAM_MATE:{
			Bundle bundle=msg.getData();
			String teamid=bundle.getString("teamid");
			String phone=bundle.getString("phone");
			if(phone.equals(this.phone)){
				if(teamid.equals(teamid1)){
					authority1="2";
				}
				else if(teamid.equals(teamid2)){
					authority2="2";
				}
				else if(teamid.equals(teamid3)){
					authority3="2";
				}
			}
			if(fm.findFragmentById(R.id.main) instanceof ChangingRoomFragment && ((ChangingRoomFragment)fm.findFragmentById(R.id.main)).getTeamid().equals(teamid)){
				ChangingRoomFragment tmp=(ChangingRoomFragment)fm.findFragmentById(R.id.main);
				tmp.changedData(phone, 2, false, "2");
			}
			break;
		}
		case DENY_AUTHORITY:{
			Bundle bundle=msg.getData();
			String teamid=bundle.getString("teamid");
			String phone=bundle.getString("phone");
			if(fm.findFragmentById(R.id.main) instanceof ChangingRoomFragment && ((ChangingRoomFragment)fm.findFragmentById(R.id.main)).getTeamid().equals(teamid)){
				ChangingRoomFragment tmp=(ChangingRoomFragment)fm.findFragmentById(R.id.main);
				tmp.changedData(phone, 2, false, "0");
				Toast.makeText(this, "最多授权两人", Toast.LENGTH_SHORT).show();
			}
		}
		case DECLINE_AUTHORITY:{
			Bundle bundle=msg.getData();
			String teamid=bundle.getString("teamid");
			String phone=bundle.getString("phone");
			if(fm.findFragmentById(R.id.main) instanceof ChangingRoomFragment && ((ChangingRoomFragment)fm.findFragmentById(R.id.main)).getTeamid().equals(teamid)){
				ChangingRoomFragment tmp=(ChangingRoomFragment)fm.findFragmentById(R.id.main);
				tmp.changedData(phone, 2, false, "0");
			}
			break;
		}
		case FUCK_OFF:{
			Bundle bundle=msg.getData();
			String phone=bundle.getString("phone");
			String teamid=bundle.getString("teamid");
			if(fm.findFragmentById(R.id.main) instanceof ChangingRoomFragment && ((ChangingRoomFragment)fm.findFragmentById(R.id.main)).getTeamid().equals(teamid)){
				ChangingRoomFragment tmp=(ChangingRoomFragment)fm.findFragmentById(R.id.main);
				tmp.changedData(phone, 2, true, "");
			}
			break;
		}
		case APPLY_JOIN:{
			String teamid=msg.obj.toString();
			if(fm.findFragmentById(R.id.main) instanceof ListsFragment && ((ListsFragment)fm.findFragmentById(R.id.main)).getState()==ListsFragment.TYPE_TEAMS_MESSAGE){
				ListsFragment tmp=(ListsFragment)fm.findFragmentById(R.id.main);
				tmp.changedData();
			}
			if(fm.findFragmentById(R.id.main) instanceof ChangingRoomFragment && ((ChangingRoomFragment)fm.findFragmentById(R.id.main)).getTeamid().equals(teamid)){
				ChangingRoomFragment tmp=(ChangingRoomFragment)fm.findFragmentById(R.id.main);
				tmp.initiate1();
			}
			break;
		}
		case SOMEONE_JOININ:{
			Bundle bundle=msg.getData();
			String phone=bundle.getString("phone");
			String teamid=bundle.getString("teamid");
			if(fm.findFragmentById(R.id.main) instanceof ChangingRoomFragment && ((ChangingRoomFragment)fm.findFragmentById(R.id.main)).getTeamid().equals(teamid)){
				ChangingRoomFragment tmp=(ChangingRoomFragment)fm.findFragmentById(R.id.main);
				tmp.changedData(phone, 1, false, "");
			}
			else if(fm.findFragmentById(R.id.main) instanceof ListsFragment && ((ListsFragment)fm.findFragmentById(R.id.main)).getState()==ListsFragment.TYPE_TEAMS_MESSAGE){
				ListsFragment tmp=(ListsFragment)fm.findFragmentById(R.id.main);
				tmp.changedData();
			}
			break;
		}
		case REFUSE_JOIN:{
			Bundle bundle=msg.getData();
			String phone=bundle.getString("phone");
			String teamid=bundle.getString("teamid");
			if(fm.findFragmentById(R.id.main) instanceof ChangingRoomFragment && ((ChangingRoomFragment)fm.findFragmentById(R.id.main)).getTeamid().equals(teamid)){
				ChangingRoomFragment tmp=(ChangingRoomFragment)fm.findFragmentById(R.id.main);
				tmp.changedData(phone, 1, true, "");
			}
			break;
		}
		case GET_TEAM_HONOR:{
			Bundle bundle=msg.getData();
			if(fm.findFragmentById(R.id.down) instanceof TeamInfoGradeFragment && ((TeamInfoGradeFragment)fm.findFragmentById(R.id.down)).getTeamid().equals(bundle.getString("teamid"))){
				TeamInfoGradeFragment tmp=(TeamInfoGradeFragment)fm.findFragmentById(R.id.down);
				tmp.setList((List<HonorInfo>)msg.obj);
				tmp.setData(bundle.getString("p1"), bundle.getString("p2"), bundle.getString("p3"), bundle.getString("p4"));
			}
			else{
				System.out.println("not a shit");
			}
			break;
		}
		case GET_ONES_MATCH:{
			Bundle bundle=msg.getData();
			String teamid=bundle.getString("teamid");
			int index=bundle.getInt("index");
			if(fm.findFragmentById(R.id.down) instanceof MyMatchFragment){
				MyMatchFragment tmp=(MyMatchFragment)fm.findFragmentById(R.id.down);
				if(tmp.getIndex()==index){
					tmp.setList((List<MyMatchEntity>)msg.obj, teamid);
				}
			}
			break;
		}
		case SEEKED_ONE_INFO:{
			if(fm.findFragmentById(R.id.content) instanceof SearchItemFragment){
				SearchItemFragment tmp=(SearchItemFragment)fm.findFragmentById(R.id.content);
				tmp.setDataDetail((Map<String, Object>)msg.obj);
			}
			if(fm.findFragmentById(R.id.content) instanceof ListsFragment && ((ListsFragment)fm.findFragmentById(R.id.content)).getState()==ListsFragment.TYPE_FRIEND_LIST){
				ListsFragment tmp=(ListsFragment)fm.findFragmentById(R.id.content);
				tmp.setData((Map<String, Object>)msg.obj);
				tmp.setEnable(true);
			}
			removeVague();
			break;
		}
		case SEEKED_ONE_TEAM:{
			if(fm.findFragmentById(R.id.content) instanceof SearchItemFragment){
				SearchItemFragment searchList=(SearchItemFragment)fm.findFragmentById(R.id.content);
				Map<String, Object> map=(Map<String, Object>)msg.obj;
				String teamid=map.get("teamid").toString();
				map.remove("teamid");
				Bundle bundle=new Bundle();
				bundle.putString("teamid", teamid);
				bundle.putBundle("map", Tools.convertMapToBundle(map));
				OtherTeamFragment otherTeam=new OtherTeamFragment();
				otherTeam.setArguments(bundle);
				otherTeam.setImage(searchList.getTeamImage());
				FragmentTransaction tx=fm.beginTransaction();
				tx.hide(upTitle);
				tx.replace(R.id.content, otherTeam);
				tx.addToBackStack(null);
				tx.commit();
			}
			break;
		}
		case JOIN_MATCH:{
			if(fm.findFragmentById(R.id.down) instanceof MatchPreviewFragment){
				MatchPreviewFragment tmp=(MatchPreviewFragment)fm.findFragmentById(R.id.down);
				String info=((Map<String, Object>)msg.obj).get("info").toString();
				String teamid=((Map<String, Object>)msg.obj).get("teamid").toString();
				if(tmp.getTeamid().equals(teamid)){
					tmp.setAttendanceInfo(info);
				}
			}
			break;
		}
		case UPDATED_MATCH:{
			String teamid=msg.obj.toString();
			int authority=0;
			if(teamid.equals(teamid1)){
				authority=Integer.parseInt(authority1);
			}
			else if(teamid.equals(teamid2)){
				authority=Integer.parseInt(authority2);
			}
			else if(teamid.equals(teamid3)){
				authority=Integer.parseInt(authority3);
			}
			if(fm.findFragmentById(R.id.main) instanceof EditPreviewFragment){
				back();
				Bundle bundle=new Bundle();
				bundle.putString("teamid", teamid);
				bundle.putInt("authority", authority);
				MatchPreviewFragment previewMatch=new MatchPreviewFragment();
				previewMatch.setArguments(bundle);
				FragmentTransaction tx=fm.beginTransaction();
				tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
				tx.replace(R.id.main, previewMatch);
				tx.addToBackStack(null);
				tx.commit();
				if(fragmentList1.get(viewPager.getCurrentItem()) instanceof TeamFragment){
					TeamFragment tf=(TeamFragment)fragmentList1.get(viewPager.getCurrentItem());
					if(tf!=null && tf.isVisible()){
						tf.setEnable(false);
					}
				}
				bar.setEnable(false);
			}
			break;
		}
		case GET_MATCHES:{
			removeVague();
			Map<String, Object> map=(Map<String, Object>)msg.obj;
			String teamid=map.get("teamid").toString();
			String year=map.get("year").toString();
			String index=map.get("index").toString();
			map.remove("teamid");
			map.remove("year");
			map.remove("index");
			if(fm.findFragmentById(R.id.main) instanceof MatchReviewFragment){
				MatchReviewFragment matchReviewList=(MatchReviewFragment)fm.findFragmentById(R.id.main);
				if(matchReviewList.getTeamid().equals(teamid) && matchReviewList.getYear().equals(year) && matchReviewList.getIndex().equals(index)){
					matchReviewList.setData(map);
				}
			}
			break;
		}
		case TEAM_INFO:{
			String teamid=msg.obj.toString();
			int i=viewPager.getCurrentItem();
			if(fragmentList1.size()==3){
				switch(i){
				case 0:{
					if(fragmentList1.get(0) instanceof TeamFragment){
						TeamFragment tmp=(TeamFragment)fragmentList1.get(0);
						if(tmp.getTeamid()!=null && tmp.getTeamid().equals(teamid))
							tmp.initiate();
					}
					if(fragmentList1.get(1) instanceof TeamFragment){
						TeamFragment tmp=(TeamFragment)fragmentList1.get(1);
						if(tmp.getTeamid()!=null && tmp.getTeamid().equals(teamid))
							tmp.initiate();
					}
					break;
				}
				case 1:{
					if(fragmentList1.get(0) instanceof TeamFragment){
						TeamFragment tmp=(TeamFragment)fragmentList1.get(0);
						if(tmp.getTeamid()!=null && tmp.getTeamid().equals(teamid))
							tmp.initiate();
					}
					if(fragmentList1.get(1) instanceof TeamFragment){
						TeamFragment tmp=(TeamFragment)fragmentList1.get(1);
						if(tmp.getTeamid()!=null && tmp.getTeamid().equals(teamid))
							tmp.initiate();
					}
					if(fragmentList1.get(2) instanceof TeamFragment){
						TeamFragment tmp=(TeamFragment)fragmentList1.get(2);
						if(tmp.getTeamid()!=null && tmp.getTeamid().equals(teamid))
							tmp.initiate();
					}
					break;
				}
				case 2:{
					if(fragmentList1.get(1) instanceof TeamFragment){
						TeamFragment tmp=(TeamFragment)fragmentList1.get(1);
						if(tmp.getTeamid()!=null && tmp.getTeamid().equals(teamid))
							tmp.initiate();
					}
					if(fragmentList1.get(2) instanceof TeamFragment){
						TeamFragment tmp=(TeamFragment)fragmentList1.get(2);
						if(tmp.getTeamid()!=null && tmp.getTeamid().equals(teamid))
							tmp.initiate();
					}
					break;
				}
				}
			}
			break;
		}
		case GET_MATCH_DETAIL:{
			if(fm.findFragmentById(R.id.main) instanceof MatchReviewDetailFragment){
				MatchReviewDetailFragment tmp=(MatchReviewDetailFragment)fm.findFragmentById(R.id.main);
				Map<String, Object> map=(Map<String, Object>)msg.obj;
				String teamid=map.get("teamid").toString();
				if(tmp.getTeamid().equals(teamid)){
					tmp.setData(map);
				}
			}
			break;
		}
		case GET_MAXID:{
			Map<String, Object> map=(Map<String, Object>)msg.obj;
			String teamid=map.get("teamid").toString();
			int maxid=Integer.parseInt(map.get("maxid").toString());
			EditReviewFragment tmp=new EditReviewFragment();
			Bundle bundle=new Bundle();
			bundle.putString("teamid", teamid);
			bundle.putInt("maxid", maxid);
			tmp.setArguments(bundle);
			FragmentTransaction tx=fm.beginTransaction();
			tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
			tx.replace(R.id.main, tmp);
			tx.addToBackStack(null);
			tx.commit();
			break;
		}
		case GET_MATCH_BY_TYPE:{
			Map<String, Object> map=(Map<String, Object>)msg.obj;
			String teamid=map.get("teamid").toString();
			map.remove("teamid");
			if(fm.findFragmentById(R.id.main) instanceof MatchLogFragment && ((MatchLogFragment)fm.findFragmentById(R.id.main)).getTeamid().equals(teamid)){
				MatchLogFragment tmp=(MatchLogFragment)fm.findFragmentById(R.id.main);
				tmp.setData(map);
			}
			break;
		}
		case GET_UPDATED_MATCH:{
			Map<String, Object> map=(Map<String, Object>)msg.obj;
			String teamid=map.get("teamid").toString();
			map.remove("teamid");
			if(fm.findFragmentById(R.id.down) instanceof MatchPreviewFragment && ((MatchPreviewFragment)fm.findFragmentById(R.id.down)).getTeamid().equals(teamid)){
				removeVague();
				MatchPreviewFragment previewMatch=(MatchPreviewFragment)fm.findFragmentById(R.id.down);
				System.out.println("doing, doing");
				previewMatch.setData(map);
				System.out.println("donedone");
			}
			if(fm.findFragmentById(R.id.main) instanceof MatchPreviewFragment && ((MatchPreviewFragment)fm.findFragmentById(R.id.main)).getTeamid().equals(teamid)){
				removeVague();
				MatchPreviewFragment previewMatch=(MatchPreviewFragment)fm.findFragmentById(R.id.main);
				System.out.println("doing, doing");
				previewMatch.setData(map);
				System.out.println("donedone");
			}
			break;
		}
		case ERROR_LOGIN:{
			if(fm.findFragmentById(R.id.content) instanceof LoginFragment){
				removeVague();
				LoginFragment tmp=(LoginFragment)fm.findFragmentById(R.id.content);
				tmp.setEnable(true);
				Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
			}
			else{
				close();
				Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
			}
			break;
		}
		case WAIT_LOGIN:{
			removeVague();
			if(fm.findFragmentById(R.id.content) instanceof LoginFragment){
				LoginFragment tmp=(LoginFragment)fm.findFragmentById(R.id.content);
				tmp.setEnable(true);
				Toast.makeText(this, "网络状态不佳，操作未成功", Toast.LENGTH_SHORT).show();
			}
			break;
		}
		case WAIT_LIST_FRIENDS:{
			removeVague();
			if(fm.findFragmentById(R.id.content) instanceof ListsFragment && ((ListsFragment)fm.findFragmentById(R.id.content)).getState()==ListsFragment.TYPE_FRIEND_LIST){
				ListsFragment tmp=(ListsFragment)fm.findFragmentById(R.id.content);
				tmp.setEnable(true);
			}
			break;
		}
		case WAIT_REVIEW_LIST:{
			removeVague();
			if(fm.findFragmentById(R.id.main) instanceof MatchReviewFragment){
				MatchReviewFragment tmp=(MatchReviewFragment)fm.findFragmentById(R.id.main);
				tmp.setEnable(true);
				Toast.makeText(this, "网络状态不佳，操作未成功", Toast.LENGTH_SHORT).show();
			}
			break;
		}
		case WAIT_SEARCH_LIST:{
			removeVague();
			if(fm.findFragmentById(R.id.content) instanceof SearchItemFragment){
				SearchItemFragment tmp=(SearchItemFragment)fm.findFragmentById(R.id.content);
				tmp.setEnable(true);
				Toast.makeText(this, "网络状态不佳，操作未成功", Toast.LENGTH_SHORT).show();
			}
			break;
		}
		case WAIT_REGISTER_PHONE:{
			removeVague();
			if(fm.findFragmentById(R.id.content) instanceof RegisterFragment && ((RegisterFragment)fm.findFragmentById(R.id.content)).getState()==RegisterFragment.PHONE_PASSWORDS){
				RegisterFragment tmp=(RegisterFragment)fm.findFragmentById(R.id.content);
				tmp.setEnable(RegisterFragment.PHONE_PASSWORDS, true);
				Toast.makeText(this, "网络状态不佳，操作未成功", Toast.LENGTH_SHORT).show();
			}
			break;
		}
		case WAIT_REGISTER_CODING:{
			removeVague();
			if(fm.findFragmentById(R.id.content) instanceof RegisterFragment && ((RegisterFragment)fm.findFragmentById(R.id.content)).getState()==RegisterFragment.IDENTIFYING_CODE){
				RegisterFragment tmp=(RegisterFragment)fm.findFragmentById(R.id.content);
				tmp.setEnable(RegisterFragment.IDENTIFYING_CODE, true);
				Toast.makeText(this, "网络状态不佳，操作未成功", Toast.LENGTH_SHORT).show();
			}
			break;
		}
		case EXISTED_PHONE:{
			removeVague();
			if(fm.findFragmentById(R.id.content) instanceof RegisterFragment && ((RegisterFragment)fm.findFragmentById(R.id.content)).getState()==RegisterFragment.PHONE_PASSWORDS){
				RegisterFragment tmp=(RegisterFragment)fm.findFragmentById(R.id.content);
				tmp.setEnable(RegisterFragment.PHONE_PASSWORDS, true);
				tmp.reset(RegisterFragment.PHONE_PASSWORDS);
				Toast.makeText(this, "网络状态不佳，操作未成功", Toast.LENGTH_SHORT).show();
			}
			break;
		}
		case WAIT_NAME:{
			removeVague();
			if(fm.findFragmentById(R.id.content) instanceof RegisterFragment && ((RegisterFragment)fm.findFragmentById(R.id.content)).getState()==RegisterFragment.NAME){
				RegisterFragment tmp=(RegisterFragment)fm.findFragmentById(R.id.content);
				tmp.setEnable(RegisterFragment.NAME, true);
				Toast.makeText(this, "网络状态不佳，操作未成功", Toast.LENGTH_SHORT).show();
			}
			break;
		}
		case WAIT_POSITION:{
			removeVague();
			if(fm.findFragmentById(R.id.main) instanceof SelectPositionFragment){
				SelectPositionFragment tmp=(SelectPositionFragment)fm.findFragmentById(R.id.main);
				tmp.setEnable(true);
				Toast.makeText(this, "网络状态不佳，操作未成功", Toast.LENGTH_SHORT).show();
			}
			if(fm.findFragmentById(R.id.content) instanceof SelectPositionFragment){
				SelectPositionFragment tmp=(SelectPositionFragment)fm.findFragmentById(R.id.content);
				tmp.setEnable(true);
				Toast.makeText(this, "网络状态不佳，操作未成功", Toast.LENGTH_SHORT).show();
			}
			break;
		}
		case WAIT_CITY_DISTRICT:{
			removeVague();
			if(fm.findFragmentById(R.id.content) instanceof RegisterFragment && ((RegisterFragment)fm.findFragmentById(R.id.content)).getState()==RegisterFragment.CITY_DISTRICT){
				RegisterFragment tmp=(RegisterFragment)fm.findFragmentById(R.id.content);
				tmp.setEnable(RegisterFragment.CITY_DISTRICT, true);
				Toast.makeText(this, "网络状态不佳，操作未成功", Toast.LENGTH_SHORT).show();
			}
			break;
		}
		case WAIT_COMPLETE:{
			removeVague();
			if(fm.findFragmentById(R.id.content) instanceof RegisterFragment && ((RegisterFragment)fm.findFragmentById(R.id.content)).getState()==RegisterFragment.COMPLETE){
				RegisterFragment tmp=(RegisterFragment)fm.findFragmentById(R.id.content);
				tmp.setEnable(RegisterFragment.COMPLETE, true);
				Toast.makeText(this, "网络状态不佳，操作未成功", Toast.LENGTH_SHORT).show();
			}
			break;
		}
		case WAIT_TEAM_CREATE:{
			removeVague();
			if(fm.findFragmentById(R.id.content) instanceof TeamCreateFragment && ((TeamCreateFragment)fm.findFragmentById(R.id.content)).getResourceId()==R.layout.fragment_team_create5){
				TeamCreateFragment tmp=(TeamCreateFragment)fm.findFragmentById(R.id.content);
				tmp.setEnable(R.layout.fragment_team_create5, true);
				Toast.makeText(this, "网络状态不佳，操作未成功", Toast.LENGTH_SHORT).show();
			}
			break;
		}
		case WAIT_ADD_NEW:{
			removeVague();
			if(fm.findFragmentById(R.id.main) instanceof MatchReviewFragment){
				MatchReviewFragment tmp=(MatchReviewFragment)fm.findFragmentById(R.id.main);
				tmp.setEnable(true);
				Toast.makeText(this, "网络状态不佳，操作未成功", Toast.LENGTH_SHORT).show();
			}
			break;
		}
		case WAIT_TEAM:{
			removeVague();
			if(fragmentList1.get(viewPager.getCurrentItem()) instanceof TeamFragment && !(fm.findFragmentById(R.id.main) instanceof IdentificationInterface)){
				TeamFragment tf=(TeamFragment)fragmentList1.get(viewPager.getCurrentItem());
				if(tf!=null && tf.isVisible()){
					tf.setEnable(true);
				}
				bar.setEnable(true);
			}
			break;
		}
		case WAIT_UPLOAD_MATCH:{
			removeVague();
			if(fm.findFragmentById(R.id.main) instanceof EditReviewFragment){
				EditReviewFragment tmp=(EditReviewFragment)fm.findFragmentById(R.id.main);
				tmp.setEnable(true);
				Toast.makeText(this, "网络状态不佳，操作未成功", Toast.LENGTH_SHORT).show();
			}
			break;
		}
		case WAIT_EVALUATE_RESULT:{
			removeVague();
			if(fm.findFragmentById(R.id.main) instanceof EvaluationCapacityFragment && ((EvaluationCapacityFragment)fm.findFragmentById(R.id.main)).getPhone().equals(this.phone)){
				EvaluationCapacityFragment tmp=(EvaluationCapacityFragment)fm.findFragmentById(R.id.main);
				tmp.setEnable(true);
				Toast.makeText(this, "网络状态不佳，操作未成功", Toast.LENGTH_SHORT).show();
			}
			break;
		}
		case WAIT_EDIT_TEAM_GRADE:{
			removeVague();
			if(fm.findFragmentById(R.id.main) instanceof EditTeamGradeFragment){
				EditTeamGradeFragment tmp=(EditTeamGradeFragment)fm.findFragmentById(R.id.main);
				tmp.setEnable(true);
				Toast.makeText(this, "网络状态不佳，操作未成功", Toast.LENGTH_SHORT).show();
			}
			break;
		}
		case WAIT_EDIT_TEAM_INFO:{
			removeVague();
			if(fm.findFragmentById(R.id.main) instanceof TeamInfoEditFragment){
				TeamInfoEditFragment tmp=(TeamInfoEditFragment)fm.findFragmentById(R.id.main);
				tmp.setEnable(true);
				Toast.makeText(this, "网络状态不佳，操作未成功", Toast.LENGTH_SHORT).show();
			}
			break;
		}
		case EVALUATION_RESULT:{
			removeVague();
			onBackPressed();
			Bundle bundle=msg.getData();
			if(fm.findFragmentById(R.id.main) instanceof ShowCapacitiyFragment){
				ShowCapacitiyFragment tmp=(ShowCapacitiyFragment)fm.findFragmentById(R.id.main);
				tmp.setData(bundle.getBoolean("isEvaluate"), bundle.getInt("n"));
			}
			else if(fm.findFragmentById(R.id.main) instanceof HomePageFragment){
				HomePageFragment tmp=(HomePageFragment)fm.findFragmentById(R.id.main);
				tmp.setValue(bundle);
			}
			break;
		}
		case WAIT_PREMATCH:{
			removeVague();
			if(fm.findFragmentById(R.id.main) instanceof EditPreviewFragment){
				EditPreviewFragment tmp=(EditPreviewFragment)fm.findFragmentById(R.id.main);
				tmp.setEnable(true);
				Toast.makeText(this, "网络状态不佳，操作未成功", Toast.LENGTH_SHORT).show();
			}
			break;
		}
		case WAIT_PROGRESSBAR:{
			if(msg.obj!=null && msg.obj instanceof ChangingRoomAdapter.ViewHolder){
				ChangingRoomAdapter.ViewHolder tmp=(ChangingRoomAdapter.ViewHolder)msg.obj;
				tmp.setEnable(true);
				Toast.makeText(this, "网络状态不佳，操作未成功", Toast.LENGTH_SHORT).show();
			}
			break;
		}
		case WAIT_EDIT_TEAM_PROCESS:{
			removeVague();
			if(fm.findFragmentById(R.id.main) instanceof TeamInfoGradeEditFragment){
				TeamInfoGradeEditFragment tmp=(TeamInfoGradeEditFragment)fm.findFragmentById(R.id.main);
				tmp.setEnable(true);
				Toast.makeText(this, "网络状态不佳，操作未成功", Toast.LENGTH_SHORT).show();
			}
			break;
		}
		case WAIT_COMPLETE_INFO:{
			removeVague();
			if(fm.findFragmentById(R.id.main) instanceof CompleteInfoFragment){	
				CompleteInfoFragment tmp=(CompleteInfoFragment)fm.findFragmentById(R.id.main);
				tmp.setEnable(true);
				Toast.makeText(this, "网络状态不佳，操作未成功", Toast.LENGTH_SHORT).show();
			}
			break;
		}
		case WAIT_EDIT_FAME:{
			removeVague();
			if(fm.findFragmentById(R.id.main) instanceof HallOfFameFragment && ((HallOfFameFragment)fm.findFragmentById(R.id.main)).getResourceId()==R.layout.add_hall_of_fame){	
				HallOfFameFragment tmp=(HallOfFameFragment)fm.findFragmentById(R.id.main);
				tmp.setEnable(true);
				Toast.makeText(this, "网络状态不佳，操作未成功", Toast.LENGTH_SHORT).show();
			}
			break;
		}
		case INIT_TEAM:{
			int index=msg.arg1-1;
			String teamid=msg.obj.toString();
			fragmentList1.remove(index);
			TeamFragment tmp=new TeamFragment();
			Bundle bundle=new Bundle();
			bundle.putString("teamid", teamid);
			System.out.println("addddd========"+teamid);
			bundle.putString("authority", "0");
			tmp.setArguments(bundle);
			fragmentList1.add(index, tmp);
			System.out.println("aaaaa");
			if(viewPager.isShown()){
				mAdapter.notifyDataSetChanged();
			}
			else{
				isViewPager=true;
			}
			break;
		}
		case BE_FUCKED:{
			Bundle bundle=msg.getData();
			String teamid=bundle.getString("current");
			teamid1=bundle.getString("teamid1");
			teamid2=bundle.getString("teamid2");
			teamid3=bundle.getString("teamid3");
			Tools.getGroupIdForHuan(mRealTimeHandler.getHandler(), new String[]{teamid1, teamid2, teamid3});
			authority1=bundle.getString("authority1");
			authority2=bundle.getString("authority2");
			authority3=bundle.getString("authority3");
			if(viewPager!=null){
				initTeam(false);
			}
			break;
		}
		case WAIT_EVALUATE_INFO:{
			removeVague();
			if(fm.findFragmentById(R.id.content) instanceof HomePageFragment){
				HomePageFragment tmp=(HomePageFragment)fm.findFragmentById(R.id.content);
				tmp.setEnable(true);
				bar.setEnable(true);
			}
			break;
		}
		case EVALUATE_INFO:{
			Bundle bundle=msg.getData();
			String phone=bundle.getString("phone");
			removeVague();
			if(fm.findFragmentById(R.id.content) instanceof HomePageFragment){
				HomePageFragment h=(HomePageFragment)fm.findFragmentById(R.id.content);
				if(h.getPhone().equals(phone) && phone.equals("host")){
					FragmentTransaction tx=fm.beginTransaction();
					ShowCapacitiyFragment tmp=new ShowCapacitiyFragment();
					tmp.setArguments(bundle);
					tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
					tx.replace(R.id.main, tmp);
					tx.addToBackStack(null);
					tx.commit();
					hideAll();
					h.setEnable(true);
					bar.setEnable(true);
				}
			}
			if(fm.findFragmentById(R.id.main) instanceof HomePageFragment){
				HomePageFragment h=(HomePageFragment)fm.findFragmentById(R.id.main);
				if(h.getPhone().equals(phone) && !phone.equals("host")){
					boolean isEvaluate=bundle.getBoolean("isEvaluate");
					if(isEvaluate==true){
						h.setEnable(true);
						Toast.makeText(this, "您已对该好友评分", Toast.LENGTH_LONG).show();
					}
					else{
						h.evaluate();
					}
				}
				
			}
			break;
		}
		case WAIT_SIGNIN:{
			if(fm.findFragmentById(R.id.content) instanceof HomePageFragment && ((HomePageFragment)fm.findFragmentById(R.id.content)).getPhone().equals("host")){
				HomePageFragment tmp=(HomePageFragment)fm.findFragmentById(R.id.content);
				tmp.setR(true);
				removeVague();
			}
			break;
		}
		case UPDATE_REVIEW_MATCH:{
			removeVague();
			if(fm.findFragmentById(R.id.main) instanceof EditReviewFragment){
				onBackPressed();
			}
			break;
		}
		case WAIT_UPDATE_REVIEW:{
			removeVague();
			if(fm.findFragmentById(R.id.main) instanceof EditReviewFragment){
				EditReviewFragment tmp=(EditReviewFragment)fm.findFragmentById(R.id.main);
				tmp.setEnable(true);
				Toast.makeText(this, "网络状态不佳，操作未成功", Toast.LENGTH_SHORT).show();
			}
			break;
		}
		case SIGN_SUCCESS:{
			Bundle bundle=msg.getData();
			if(fm.findFragmentById(R.id.content) instanceof HomePageFragment && ((HomePageFragment)fm.findFragmentById(R.id.content)).getPhone().equals("host")){
				HomePageFragment tmp=(HomePageFragment)fm.findFragmentById(R.id.content);
				tmp.setR(true);
				tmp.setIsSign(true);
				tmp.initGrade(true, Integer.parseInt(bundle.getString("addup")), Integer.parseInt(bundle.getString("score")));
			}
			
			break;
		}
		case GET_GROUPID:{
			Map<String, Object> map=new HashMap<String, Object>();
			Bundle bundle=msg.getData();
			if(bundle.containsKey(teamid1)){
				map.put(teamid1, bundle.getString(teamid1));
				System.out.println("groupid1= "+bundle.getString(teamid1)+" while teamid= "+teamid1);
			}
			if(bundle.containsKey(teamid2)){
				map.put(teamid2, bundle.getString(teamid2));
				System.out.println("groupid2= "+bundle.getString(teamid2)+" while teamid= "+teamid2);
			}
			if(bundle.containsKey(teamid3)){
				map.put(teamid3, bundle.getString(teamid3));
				System.out.println("groupid3= "+bundle.getString(teamid3)+" while teamid= "+teamid3);
			}
			pd.save(map);
			if(fm.findFragmentById(R.id.title) instanceof TitleFragment && ((TitleFragment)fm.findFragmentById(R.id.title)).getState()==TitleFragment.HOMEPAGE_TITLE){
				TitleFragment tmp=(TitleFragment)fm.findFragmentById(R.id.title);
				int msgNumber=tmp.setMsgNumberChanged();
				tmp.remind(msgNumber);
			}
			System.out.println("dddddddddddddddddddddddddddddddddd");
			break;
		}
		case WAIT_DELETE_PREVIEW:{
			removeVague();
			if(fm.findFragmentById(R.id.main) instanceof EditPreviewFragment){
				EditPreviewFragment tmp=(EditPreviewFragment)fm.findFragmentById(R.id.main);
				tmp.setEnable(true);
				Toast.makeText(this, "网络状态不佳，操作未成功", Toast.LENGTH_SHORT).show();
			}
			break;
		}
		case AUTHORITY:{
			Bundle bundle=msg.getData();
			teamid1=bundle.getString("teamid1");
			teamid2=bundle.getString("teamid2");
			teamid3=bundle.getString("teamid3");
			authority1=bundle.getString("authority1");
			authority2=bundle.getString("authority2");
			authority3=bundle.getString("authority3");
			initTeam(false);
			break;
		}
		case DELETE_PREVIEW:{
			String teamid=msg.getData().getString("teamid");
			int authority=0;
			if(teamid.equals(teamid1)){
				authority=Integer.parseInt(authority1);
			}
			else if(teamid.equals(teamid2)){
				authority=Integer.parseInt(authority2);
			}
			else if(teamid.equals(teamid3)){
				authority=Integer.parseInt(authority3);
			}
			if(fm.findFragmentById(R.id.main) instanceof EditPreviewFragment && ((EditPreviewFragment)fm.findFragmentById(R.id.main)).getTeamid().equals(teamid)){
				removeVague();
				back();
				Bundle bundle=new Bundle();
				bundle.putString("teamid", teamid);
				bundle.putInt("authority", authority);
				PrematchStartFragment tmp=new PrematchStartFragment();
				tmp.setArguments(bundle);
				FragmentTransaction tx=fm.beginTransaction();
				tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
				tx.replace(R.id.main, tmp);
				tx.addToBackStack(null);
				tx.commit();
				if(fragmentList1.get(viewPager.getCurrentItem()) instanceof TeamFragment){
					TeamFragment tf=(TeamFragment)fragmentList1.get(viewPager.getCurrentItem());
					if(tf!=null && tf.isVisible()){
						tf.setEnable(false);
					}
				}
				bar.setEnable(false);
			}
			break;
		}
		case NEW_MESSAGE:{
			updateTitleAndBar();
			break;
		}
		case GET_MATCH_BY_ID:{
			Bundle bundle0=msg.getData();
			String teamid=bundle0.getString("teamid");
			String authority="-1";
			if(fm.findFragmentById(R.id.main) instanceof ListsFragment && ((ListsFragment)fm.findFragmentById(R.id.main)).getState()==ListsFragment.TYPE_TEAMS_MESSAGE){
				reviewDetail(teamid, (MatchReviewEntity)bundle0.getSerializable("entity"), authority);
			}
			else{
				back();
				isInitTeam=initTeam(isInitTeam);
				if(teamid.equals(teamid1)){
					viewPager.setCurrentItem(0);
					authority=authority1;
				}
				else if(teamid.equals(teamid2)){
					viewPager.setCurrentItem(1);
					authority=authority2;
				}
				else if(teamid.equals(teamid3)){
					viewPager.setCurrentItem(2);
					authority=authority3;
				}
				Bundle bundle1=new Bundle();
				bundle1.putInt("status", R.id.bar_team);
				bar=new BarFragment();
				bar.setArguments(bundle1);
				FragmentTransaction tx=fm.beginTransaction();
				tx.replace(R.id.bar, bar);
				System.out.println("1111111111111111");
				tx.commit();
				System.out.println("2222222222222222");
				resetViewPager();
				MatchReviewFragment matchReviewList=new MatchReviewFragment();
				Bundle bundle=new Bundle();
				bundle.putString("teamid", teamid);
				bundle.putString("authority", authority);
				matchReviewList.setArguments(bundle);
				tx=fm.beginTransaction();
				tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
				tx.replace(R.id.main, matchReviewList);
				tx.addToBackStack(null);
				tx.commit();
				if(fragmentList1.get(viewPager.getCurrentItem()) instanceof TeamFragment){
					TeamFragment tf=(TeamFragment)fragmentList1.get(viewPager.getCurrentItem());
					if(tf!=null && tf.isVisible()){
						tf.setEnable(true);
					}
				}
				reviewDetail(teamid, (MatchReviewEntity)bundle0.getSerializable("entity"), authority);
			}
			break;
		}
		case GET_FAME:{
			removeVague();
			Map<String, Object> map=(Map<String, Object>)msg.obj;
			if(msg.arg1==0){
				//开新登录页
				String teamid=map.get("teamid").toString();
				String authority=null;
				map.remove("teamid");
				HallOfFameFragment tmp=new HallOfFameFragment();
				Bundle bundle=new Bundle();
				bundle.putString("teamid", teamid);
				if(teamid.equals(teamid1)){
					authority=authority1;
				}
				else if(teamid.equals(teamid2)){
					authority=authority2;
				}
				else if(teamid.equals(teamid3)){
					authority=authority3;
				}
				bundle.putString("authority", authority);
				bundle.putInt("resource", R.layout.hall_of_fame);
				tmp.setArguments(bundle);
				FragmentTransaction tx=fm.beginTransaction();
				tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
				tx.replace(R.id.main, tmp);
				tx.addToBackStack(null);
				tx.commit();
			}
			else{
				if(viewPager.isShown()){
					String teamid=map.get("teamid").toString();
					String authority=null;
					map.remove("teamid");
					HallOfFameFragment tmp=new HallOfFameFragment();
					Bundle bundle=new Bundle();
					bundle.putString("teamid", teamid);
					if(teamid.equals(teamid1)){
						authority=authority1;
					}
					else if(teamid.equals(teamid2)){
						authority=authority2;
					}
					else if(teamid.equals(teamid3)){
						authority=authority3;
					}
					bundle.putString("authority", authority);
					bundle.putBundle("map", Tools.convertMapToBundle(map));
					bundle.putInt("resource", R.layout.hall_of_fame_list);
					tmp.setArguments(bundle);
					FragmentTransaction tx=fm.beginTransaction();
					tx.setCustomAnimations(R.animator.slide_in_right, R.animator.slide_out_left, R.animator.slide_in_left, R.animator.slide_out_right);
					tx.replace(R.id.main, tmp);
					tx.addToBackStack(null);
					tx.commit();
					
				}
			}
			break;
		}
		case UPDATE_FAME:{
			String teamid=msg.obj.toString();
			if(fm.findFragmentById(R.id.main) instanceof HallOfFameFragment && ((HallOfFameFragment)fm.findFragmentById(R.id.main)).getTeamid().equals(teamid)){
				String authority=null;
				if(teamid.equals(teamid1)){
					authority=authority1;
				}
				else if(teamid.equals(teamid2)){
					authority=authority2;
				}
				else if(teamid.equals(teamid3)){
					authority=authority3;
				}
				back();
				openFame(HallOfFameFragment.FIRST_OPEN, teamid, authority, null);
				if(fragmentList1.get(viewPager.getCurrentItem()) instanceof TeamFragment){
					TeamFragment tf=(TeamFragment)fragmentList1.get(viewPager.getCurrentItem());
					if(tf!=null && tf.isVisible()){
						tf.setEnable(false);
					}
				}
				bar.setEnable(false);
			}
			break;
		}
		case GET_CODE:{
			Map<String, Object> map=(Map<String, Object>)msg.obj;
			if(!map.containsKey("code")){
				Toast.makeText(this, "该帐号不存在", Toast.LENGTH_SHORT).show();
			}
			else{
				String phone=map.get("phone").toString();
				String code=map.get("code").toString();
				Bundle bundle=new Bundle();
				bundle.putInt("resource", R.layout.fragment_find_psw2);
				bundle.putString("phone", phone);
				bundle.putString("code", code);
				if(fm.findFragmentById(R.id.content) instanceof FindPasswordsFragment && ((FindPasswordsFragment)fm.findFragmentById(R.id.content)).getState()==R.layout.fragment_find_psw2){
					backPressed();
				}
				openFindPasswords(bundle);
			}
			break;
		}
		case SET_PSW:{
			back();
			break;
		}
		case SET_NEW_PSW:{
			if(fm.findFragmentById(R.id.main) instanceof FindPasswordsFragment){
				back();
			}
			break;
		}
		case LOG_OUT:{
			close();
			Toast.makeText(this, "您的帐号在别处登录，请检查", Toast.LENGTH_LONG).show();
			break;
		}
		case CHANGE_TEAM_INFO:{
			String teamid=msg.obj.toString();
			int current=viewPager.getCurrentItem();
			if(current>=1){
				if(fragmentList1.get(current-1) instanceof TeamFragment && ((TeamFragment)fragmentList1.get(current-1)).getTeamid()!=null && ((TeamFragment)fragmentList1.get(current-1)).getTeamid().equals(teamid)){
					TeamFragment tmp=(TeamFragment)fragmentList1.get(current-1);
					tmp.initiate();
					mAdapter.notifyDataSetChanged();
				}
			}
			if(current<=1){
				if(fragmentList1.get(current+1) instanceof TeamFragment && ((TeamFragment)fragmentList1.get(current+1)).getTeamid()!=null && ((TeamFragment)fragmentList1.get(current+1)).getTeamid().equals(teamid)){
					TeamFragment tmp=(TeamFragment)fragmentList1.get(current+1);
					tmp.initiate();
					mAdapter.notifyDataSetChanged();
				}
			}
			if(fragmentList1.get(current) instanceof TeamFragment && ((TeamFragment)fragmentList1.get(current)).getTeamid()!=null&& ((TeamFragment)fragmentList1.get(current)).getTeamid().equals(teamid)){
				TeamFragment tmp=(TeamFragment)fragmentList1.get(current);
				tmp.initiate();
				mAdapter.notifyDataSetChanged();
			}
			removeVague();
			back();
			break;
		}
		case CHANGE_TEAM_GRADE:{
			removeVague();
			if(fm.findFragmentById(R.id.main) instanceof EditTeamGradeFragment || fm.findFragmentById(R.id.main) instanceof TeamInfoGradeEditFragment){
				onBackPressed();
			}
			break;
		}
		case LOBBY_TEAM:{
			removeVague();
			if(fm.findFragmentById(R.id.content) instanceof LobbyTeamFragment){
				int index=msg.arg1;
				List<LobbyTeamEntity> list=(List<LobbyTeamEntity>)msg.obj;
				LobbyTeamFragment tmp=(LobbyTeamFragment)fm.findFragmentById(R.id.content);
				if(index==tmp.getIndex()){
					tmp.setData(list);
				}
			}
		}
		case OK_THEME:{
			removeVague();
			if(fm.findFragmentById(R.id.main) instanceof LobbyTeamAddFragment || fm.findFragmentById(R.id.main) instanceof LobbyTeamReplyFragment){
				onBackPressed();
			}
			break;
		}
		}
		
	}
	
	

	public void updateTitleAndBar() {
		int num=0;
		if(fm.findFragmentById(R.id.title) instanceof TitleFragment && ((TitleFragment)fm.findFragmentById(R.id.title)).getState()==TitleFragment.HOMEPAGE_TITLE){
			TitleFragment tmp=(TitleFragment)fm.findFragmentById(R.id.title);
			num=tmp.setMsgNumberChanged();
			tmp.remind(num);
		}
		if(fm.findFragmentById(R.id.bar) instanceof BarFragment){
			BarFragment tmp=(BarFragment)fm.findFragmentById(R.id.bar);
			tmp.setDot(num>0? true:false);
		}
	}

	public void openCheckNetwork(){
		Intent intent=null;
        if(android.os.Build.VERSION.SDK_INT>10){
            intent = new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS);
        }else{
            intent = new Intent();
            ComponentName component = new ComponentName("com.android.settings","com.android.settings.WirelessSettings");
            intent.setComponent(component);
            intent.setAction("android.intent.action.VIEW");
        }
        startActivity(intent);
	}

	@Override 
	public boolean onKeyDown(int keyCode, KeyEvent event) { 
		if ((keyCode == KeyEvent.KEYCODE_BACK)) { 
			long time=new Date().getTime();
			if(time-ctime<800){
				Toast.makeText(this, "您的操作过于频繁", Toast.LENGTH_SHORT).show();
				return true;
			}
			ctime=time;
		}
		return super.onKeyDown(keyCode, event);
	} 

	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		if(fm.findFragmentById(R.id.content) instanceof HomePageFragment && !(fm.findFragmentById(R.id.down) instanceof IdentificationInterface) && !(fm.findFragmentById(R.id.main) instanceof IdentificationInterface) && ((HomePageFragment)fm.findFragmentById(R.id.content)).getPhone().equals("host")){
			moveTaskToBack(false);  
			System.out.println("1");
		}
		else if(fm.findFragmentById(R.id.content) instanceof ListsFragment && !(fm.findFragmentById(R.id.main) instanceof IdentificationInterface) && !(fm.findFragmentById(R.id.down) instanceof IdentificationInterface)){
			moveTaskToBack(false); 
			System.out.println("2");
		}
		else if(fm.findFragmentById(R.id.content) instanceof LobbyInterface && !(fm.findFragmentById(R.id.main) instanceof IdentificationInterface) && !(fm.findFragmentById(R.id.down) instanceof IdentificationInterface)){
			moveTaskToBack(false); 
			System.out.println("2.5");
		}
		else if(fm.findFragmentById(R.id.content) instanceof MoreFragment && !(fm.findFragmentById(R.id.down) instanceof IdentificationInterface) && !(fm.findFragmentById(R.id.main) instanceof IdentificationInterface)){
			moveTaskToBack(false); 
			System.out.println("3");
		}
		else if(fm.findFragmentById(R.id.main) instanceof ShowCapacitiyFragment){
			showAll();
			((HomePageFragment)fm.findFragmentById(R.id.content)).updateView();
			backPressed();
			if(fm.findFragmentById(R.id.title) instanceof TitleFragment){
				TitleFragment tmp=(TitleFragment)fm.findFragmentById(R.id.title);
				tmp.setEnable(true);
			}
			System.out.println("4");
		}
		else if(fm.findFragmentById(R.id.down) instanceof MyMatchFragment){
			showDown();
			backPressed();
			System.out.println("5");
			bar.setEnable(true);
			if(fm.findFragmentById(R.id.content) instanceof HomePageFragment){
				HomePageFragment tmp=(HomePageFragment)fm.findFragmentById(R.id.content);
				tmp.setEnable(true);
			}
		}
		else if(fm.findFragmentById(R.id.main) instanceof FileShowFragment){
			backPressed();
			System.out.println("5.5");
			if(fm.findFragmentById(R.id.title) instanceof TitleFragment){
				TitleFragment tmp=(TitleFragment)fm.findFragmentById(R.id.title);
				tmp.setEnable(true);
			}
			if(fm.findFragmentById(R.id.content) instanceof HomePageFragment){
				HomePageFragment tmp=(HomePageFragment)fm.findFragmentById(R.id.content);
				tmp.setEnable(true);
			}
			if(fm.findFragmentById(R.id.bar) instanceof BarFragment){
				BarFragment tmp=(BarFragment)fm.findFragmentById(R.id.bar);
				tmp.setEnable(true);
			}
		}
		else if(fm.findFragmentById(R.id.down) instanceof MatchPreviewFragment && ((MatchPreviewFragment)fm.findFragmentById(R.id.down)).getAuthority()==-1){
			if(needToShowMain==true){
				showMain();
				hideTitle();
				System.out.println("5.5");
			}
			else{
				showTitle();
				showDown();
				bar.setEnable(true);
				if(fm.findFragmentById(R.id.content) instanceof HomePageFragment){
					HomePageFragment tmp=(HomePageFragment)fm.findFragmentById(R.id.content);
					tmp.setEnable(true);
				}
				System.out.println("6");
			}
			backPressed();
		}
		else if(fm.findFragmentById(R.id.main) instanceof ListsFragment){
			showAll();
			ListsFragment tmp=(ListsFragment)fm.findFragmentById(R.id.content);
			tmp.changedData();
			backPressed();
			TitleFragment t=(TitleFragment)fm.findFragmentById(R.id.title);
			t.setChecked(R.id.btn_myMessage, R.layout.fragment_homepage_title);
			updateTitleAndBar();
			System.out.println("6.5");
		}
		else if((fm.findFragmentById(R.id.content) instanceof RegisterFragment &&
				((RegisterFragment)fm.findFragmentById(R.id.content)).getState()!=RegisterFragment.NAME) || 
				fm.findFragmentById(R.id.content) instanceof SelectPositionFragment){
			System.out.println("6.7");
			backPressed();
		}
		else if(fm.findFragmentById(R.id.content) instanceof RegisterFragment && ((RegisterFragment)fm.findFragmentById(R.id.content)).getState()==RegisterFragment.NAME){
			RegisterFragment register=(RegisterFragment)fm.findFragmentById(R.id.content);
			openVague(WAIT_LOGIN);
			String phone=register.getPhone();
			String passwords=register.getPasswords();
			Map<String, Object> tmp=new HashMap<String, Object>();
			tmp.put("request", "user login");
			tmp.put("phone", phone);
			tmp.put("passwords", passwords);
			tmp.put("date", Tools.getDate1());
			tmp.put("rid", JPushInterface.getRegistrationID(getApplicationContext()));
			SQLHelper helper=SQLHelper.getInstance(this);
			Cursor cursor=helper.select("ich", new String[]{"image"}, "phone=?", new String[]{"host"}, null);
			if(cursor.moveToNext()){
				String imgPath=cursor.getString(0);
				if(imgPath!=null && Tools.isFileExist(imgPath)!=false){
					tmp.put("img", "1");
				}
			}
			Runnable r=new ClientWrite(Tools.JsonEncode(tmp));
			new Thread(r).start();
			if(tmp.containsKey("img")){
				tmp.remove("img");
			}
			tmp.remove("request");
			tmp.remove("date");
			pd.save(tmp);
			Tools.loginHuanXin(phone, passwords);
		
			System.out.println("7");
		}
		else if(fm.findFragmentById(R.id.main) instanceof TeamInfoEditFragment){
			backPressed();
			System.out.println("7.5");
		}
		else if(fm.findFragmentById(R.id.main) instanceof EditTeamGradeFragment || fm.findFragmentById(R.id.main) instanceof TeamInfoGradeEditFragment){
			backPressed();
			String teamid=((TeamInterface)fm.findFragmentById(R.id.main)).getTeamid();
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("request", "get grade");
			map.put("teamid", teamid);
			Runnable r=new ClientWrite(Tools.JsonEncode(map));
			new Thread(r).start();
			System.out.println("7.7");
		}
		else if(fm.findFragmentById(R.id.title) instanceof TitleFragment && ((TitleFragment)fm.findFragmentById(R.id.title)).getState()==TitleFragment.TEAM_INFO_TITLE){
			back();
			if(fragmentList1.get(viewPager.getCurrentItem()) instanceof TeamFragment){
				TeamFragment tf=(TeamFragment)fragmentList1.get(viewPager.getCurrentItem());
				if(tf!=null && tf.isVisible()){
					tf.setEnable(true);
				}
			}
			bar.setEnable(true);
			System.out.println("8");
		}
		else if(fm.findFragmentById(R.id.main) instanceof MatchReviewFragment){
			backPressed();
			if(fragmentList1.get(viewPager.getCurrentItem()) instanceof TeamFragment){
				TeamFragment tf=(TeamFragment)fragmentList1.get(viewPager.getCurrentItem());
				if(tf!=null && tf.isVisible()){
					tf.setEnable(true);
				}
			}
			bar.setEnable(true);
			System.out.println("9");
		}
		else if(fm.findFragmentById(R.id.main) instanceof MatchPreviewFragment && ((MatchPreviewFragment)fm.findFragmentById(R.id.main)).getAuthority()!=-1){
			backPressed();
			if(fragmentList1.get(viewPager.getCurrentItem()) instanceof TeamFragment){
				TeamFragment tf=(TeamFragment)fragmentList1.get(viewPager.getCurrentItem());
				if(tf!=null && tf.isVisible()){
					tf.setEnable(true);
				}
			}
			bar.setEnable(true);
			System.out.println("10");
		}
		else if(fm.findFragmentById(R.id.main) instanceof PrematchStartFragment){
			backPressed();
			if(fragmentList1.get(viewPager.getCurrentItem()) instanceof TeamFragment){
				TeamFragment tf=(TeamFragment)fragmentList1.get(viewPager.getCurrentItem());
				if(tf!=null && tf.isVisible()){
					tf.setEnable(true);
				}
			}
			bar.setEnable(true);
			System.out.println("10.5");
		}
		else if(fm.findFragmentById(R.id.main) instanceof EditPreviewFragment){
			EditPreviewFragment tmp=(EditPreviewFragment)fm.findFragmentById(R.id.main);
			openVague(WAIT_PREMATCH);
			tmp.setEnable(false);
			backPressed();
			System.out.println("11");
		}
		else if(fm.findFragmentById(R.id.content) instanceof SearchItemFragment && fromViewPager==true){
			System.out.println("123456");
			backPressed();
			resetViewPager();
			fromViewPager=false;
		}
		else if(fm.findFragmentById(R.id.main) instanceof ChangingRoomFragment){
			System.out.println("12");
			final ChangingRoomFragment tmp=(ChangingRoomFragment)fm.findFragmentById(R.id.main);
			new AlertDialog.Builder(this)
			.setTitle("是否保存操作")
			.setPositiveButton("保存操作",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							tmp.setEnable(false);
							tmp.saveExit();
							backPressed();
						}
					})
			.setNegativeButton("不保存",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							tmp.setEnable(false);
							backPressed();
						}
					}).show();
		}
		else if(fm.findFragmentById(R.id.main) instanceof CompleteInfoFragment){
			System.out.println("13");
			final CompleteInfoFragment tmp=(CompleteInfoFragment)fm.findFragmentById(R.id.main);
			new AlertDialog.Builder(this)
			.setTitle("是否保存操作")
			.setPositiveButton("保存操作",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							tmp.saveExit();
						}
					})
			.setNegativeButton("不保存",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog,
								int which) {
							backPressed();
							showAll();
							tmp.setEnable(true);
							bar.setEnable(true);
							if(fm.findFragmentById(R.id.title) instanceof TitleFragment){
								TitleFragment tmp=(TitleFragment)fm.findFragmentById(R.id.title);
								tmp.setEnable(true);
							}
							if(fm.findFragmentById(R.id.content) instanceof HomePageFragment){
								HomePageFragment tmp=(HomePageFragment)fm.findFragmentById(R.id.content);
								tmp.setEnable(true);
							}
						}
					}).show();
		}
		else if(fm.findFragmentById(R.id.main) instanceof ShooterAssisterFragment){
			backPressed();
			if(fragmentList1.get(viewPager.getCurrentItem()) instanceof TeamFragment){
				TeamFragment tf=(TeamFragment)fragmentList1.get(viewPager.getCurrentItem());
				if(tf!=null && tf.isVisible()){
					tf.setEnable(true);
				}
			}
			bar.setEnable(true);
			System.out.println("14");
		}
		else if(fm.findFragmentById(R.id.main) instanceof ChatFragment){
			if(needShow==true){
				showAll();
				needShow=false;
			}
			else{
				if(fragmentList1.get(viewPager.getCurrentItem()) instanceof TeamFragment){
					TeamFragment tf=(TeamFragment)fragmentList1.get(viewPager.getCurrentItem());
					if(tf!=null && tf.isVisible()){
						tf.setEnable(true);
					}
				}
				bar.setEnable(true);
			}
			backPressed();
			System.out.println("15");
		}
		else if(fm.findFragmentById(R.id.main) instanceof MatchLogFragment){
			backPressed();
			if(fragmentList1.get(viewPager.getCurrentItem()) instanceof TeamFragment){
				TeamFragment tf=(TeamFragment)fragmentList1.get(viewPager.getCurrentItem());
				if(tf!=null && tf.isVisible()){
					tf.setEnable(true);
				}
			}
			bar.setEnable(true);
			System.out.println("16");
		}
		else if(fm.findFragmentById(R.id.main) instanceof MatchReviewDetailFragment){
			backPressed();
			System.out.println("17");
		}
		else if(fm.findFragmentById(R.id.main) instanceof MatchPreviewFragment){
			backPressed();
			System.out.println("18");
		}
		else if(fm.findFragmentById(R.id.main) instanceof EditReviewFragment){
			System.out.println("19");
			while(!(fm.findFragmentById(R.id.main) instanceof MatchReviewFragment)){
				backPressed();
			}
		}
		else if(fm.findFragmentById(R.id.main) instanceof HallOfFameFragment){
			System.out.println("19.5");
			if(fragmentList1.get(viewPager.getCurrentItem()) instanceof TeamFragment){
				TeamFragment tf=(TeamFragment)fragmentList1.get(viewPager.getCurrentItem());
				if(tf!=null && tf.isVisible()){
					tf.setEnable(true);
				}
			}
			bar.setEnable(true);
			backPressed();
		}
		else if(fm.findFragmentById(R.id.main) instanceof SelectPositionFragment && ((SelectPositionFragment)fm.findFragmentById(R.id.main)).getState()==0){
			System.out.println("19.7");
			String position=((SelectPositionFragment)fm.findFragmentById(R.id.main)).getPosition();
			backPressed();
			while(!(fm.findFragmentById(R.id.main) instanceof HallOfFameFragment)){
				System.out.println("wait");
			}
			((HallOfFameFragment)fm.findFragmentById(R.id.main)).setPosition(position);
		}
		else if(fm.findFragmentById(R.id.main) instanceof SelectPositionFragment && ((SelectPositionFragment)fm.findFragmentById(R.id.main)).getState()==4){
			System.out.println("19.7");
			String position=((SelectPositionFragment)fm.findFragmentById(R.id.main)).getPosition();
			backPressed();
			while(!(fm.findFragmentById(R.id.main) instanceof FileEditFragment)){
				System.out.println("wait");
			}
			((FileEditFragment)fm.findFragmentById(R.id.main)).setPosition(position);
		}
		else if(fm.findFragmentById(R.id.main) instanceof FindPasswordsFragment || fm.findFragmentById(R.id.content) instanceof FindPasswordsFragment){
			backPressed();
			System.out.println("19.9");
		}
		else if(viewPager.isShown() && !(fm.findFragmentById(R.id.main) instanceof TeamInterface && !(fm.findFragmentById(R.id.down) instanceof TeamInterface))){
			moveTaskToBack(false);  
			System.out.println("20");
		}
		else if(fm.findFragmentById(R.id.content) instanceof MoreFragment && (fm.findFragmentById(R.id.main) instanceof MoreProtocols
				|| fm.findFragmentById(R.id.main) instanceof AboutusFragment || fm.findFragmentById(R.id.main) instanceof FeedbackFragment
				|| fm.findFragmentById(R.id.main) instanceof SettingsFragment)){
			MoreFragment tmp=(MoreFragment)fm.findFragmentById(R.id.content);
			tmp.setEnable(true);
			setBar(true);
			backPressed();
			System.out.println("22");
		}
		else if(fm.findFragmentById(R.id.content) instanceof SearchItemFragment && fm.findFragmentById(R.id.main) instanceof HomePageFragment){
			SearchItemFragment tmp=(SearchItemFragment)fm.findFragmentById(R.id.content);
			tmp.setEnable(true);
			backPressed();
			System.out.println("23");
		}
		else{
			super.onBackPressed();
		}
	}

	private void backPressed(){
		System.out.println("21");
		super.onBackPressed();
		Iterator<Fragment> iter1=fm.getFragments().iterator();
		while(iter1.hasNext()){
			Fragment f=iter1.next();
			if(f!=null){
				System.out.println(f.getClass().getName());
			}
		}
	}


	public static void closeSocket(){
		Socket socket=SocketSingleton.getInstance().getSocket();
		if(socket!=null){
			try{
				socket.shutdownInput();
				socket.shutdownOutput();
				
				socket.close();
				socket=null;
				SocketSingleton.resetInstance();
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	
	public void close(){
		if(isSocketClose==false){
			closeSocket();
			isSocketClose=true;
		}
		Map<String, Object> tmp=new HashMap<String, Object>();
		tmp.put("phone", "");
		tmp.put("passwords", "");
		pd.save(tmp);
		Intent intent=getIntent();
		finish();
		startActivity(intent);
		
	}
	
	
	private void back(){
		if(fm.getFragments()!=null){
			int n=fm.getFragments().size()-1;
			
			while(n>=0){
				if(fm.getFragments().get(n)!=null && fm.getFragments().get(n) instanceof IdentificationInterface 
						&& ((IdentificationInterface)fm.getFragments().get(n)).getFragmentLevel()!=IdentificationInterface.MAIN_LEVEL ){
					backPressed();
				}
				else if(fm.getFragments().get(n)!=null && fm.getFragments().get(n) instanceof IdentificationInterface 
						&& ((IdentificationInterface)fm.getFragments().get(n)).getFragmentLevel()==IdentificationInterface.MAIN_LEVEL ){
					break;
				}
				n--;
			}
			if(fragmentList1!=null && fragmentList1.size()>0 &&viewPager!=null && fragmentList1.get(viewPager.getCurrentItem()) instanceof TeamFragment){
				TeamFragment tf=(TeamFragment)fragmentList1.get(viewPager.getCurrentItem());
				if(tf!=null && tf.isVisible()){
					tf.setEnable(true);
				}
			}
			if(bar!=null){
				bar.setEnable(true);
			}
		}
	}
	
	
}
