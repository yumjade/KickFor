package com.example.kickfor.lobby;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.kickfor.ClientWrite;
import com.example.kickfor.HandlerListener;
import com.example.kickfor.HomePageActivity;
import com.example.kickfor.PreferenceData;
import com.example.kickfor.R;
import com.example.kickfor.RealTimeHandler;
import com.example.kickfor.SQLHelper;
import com.example.kickfor.Tools;
import com.example.kickfor.utils.IdentificationInterface;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.Toast;

public class LobbyTeamReplyFragment extends Fragment implements IdentificationInterface, LobbyInterface, HandlerListener{

	private ImageView back=null;
	private ImageView image=null;
	private TextView name=null;
	private TextView iden=null;
	private EditText text=null;
	private TextView ensure=null;
	
	private String phone=null;
	private Context context=null;
	private String teamid1=null;
	private String teamid2=null;
	private String teamid3=null;
	private String name1=null;
	private String name2=null;
	private String name3=null;
	private Bitmap bitmap1=null;
	private Bitmap bitmap2=null;
	private Bitmap bitmap3=null;
	private Bitmap bitmap=null;
	private String type=null;
	private String themeKey=null;
	
	private List<String> mList=null;
	
	@Override
	public int getFragmentLevel() {
		return IdentificationInterface.SECOND_LEVEL;
	}

	@Override
	public void setEnable(boolean enable) {
		
	}

	private void init(){
		this.context=getActivity();
		this.phone=new PreferenceData(context).getData(new String[]{"phone"}).get("phone").toString();
		Bundle bundle=getArguments();
		this.type=bundle.getString("type");
		this.themeKey=bundle.getString("themeKey");
		this.mList=new ArrayList<String>();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		RealTimeHandler.getInstance().regist(this);
		init();
		View view=inflater.inflate(R.layout.fragment_lobby_reply_team, container, false);
		back=(ImageView)view.findViewById(R.id.lobby_reply_back);
		image=(ImageView)view.findViewById(R.id.lobby_reply_image);
		name=(TextView)view.findViewById(R.id.lobby_reply_name);
		text=(EditText)view.findViewById(R.id.lobby_reply_text);
		iden=(TextView)view.findViewById(R.id.lobby_reply_listshow);
		ensure=(TextView)view.findViewById(R.id.lobby_reply_submit);
		initiate();
		back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				((HomePageActivity)getActivity()).onBackPressed();
			}
			
		});
		
		iden.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				final TeamPopupWindow pop=new TeamPopupWindow(context, mList, iden.getWidth(), iden.getHeight());
				int[] location = new int[2];  
		        iden.getLocationOnScreen(location);
		        pop.showAsDropDown(iden);
				pop.setOnDismissListener(new OnDismissListener(){  
		            public void onDismiss(){
		            	String str=pop.getSelect();
		            	if(str!=null){
		            		iden.setText(str);
			            	if(str.equals(name1)){
			            		image.setImageBitmap(bitmap1);
			            	}
			            	else if(str.equals(name2)){
			            		image.setImageBitmap(bitmap2);
			            	}
			            	else if(str.equals(name3)){
			            		image.setImageBitmap(bitmap3);
			            	}
			            	else if(str.equals(name.getText().toString())){
			            		image.setImageBitmap(bitmap);
			            	}
		            	}
		            }             
		        });  
			}
			
		});
		
		ensure.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				String teamid=null;
				String str=iden.getText().toString();
				if(str.equals(name1)){
					teamid=teamid1;
				}
				else if(str.equals(name2)){
					teamid=teamid2;
				}
				else if(str.equals(name3)){
					teamid=teamid3;
				}
				else if(str.equals(name.getText().toString())){
					teamid="";
				}
				if(teamid==null){
					Toast.makeText(context, "请选择身份", Toast.LENGTH_SHORT).show();
				}
				else if(text.getText().toString().isEmpty()){
					Toast.makeText(context, "回复内容不能为空", Toast.LENGTH_SHORT).show();
				}
				else{
					Map<String, Object> map=new HashMap<String, Object>();
					map.put("request", "reply theme");
					map.put("phone", phone);
					map.put("teamid", teamid);
					map.put("content", text.getText().toString());
					map.put("type", type);
					map.put("themekey", themeKey);
					Runnable r=new ClientWrite(Tools.JsonEncode(map));
					new Thread(r).start();
				}
				
			}
			
		});
		
		return view;
	}

	
	
	@Override
	public void onChange(Message msg) {
		if(msg.what==HomePageActivity.OK_THEME){
			((HomePageActivity)getActivity()).removeVague();
			if(back!=null){
				((HomePageActivity)getActivity()).onBackPressed();
			}
		}
	}

	@Override
	public void onDestroy() {
		RealTimeHandler.getInstance().unRegist(this);
		super.onDestroy();
	}

	private void initiate(){
		SQLHelper helper=SQLHelper.getInstance(context);
		Cursor cursor=helper.select("ich", new String[]{"name", "team1", "team2", "team3", "authority1", "authority2", "authority3", "image"}, "phone=?", new String[]{"host"}, null);
		if(cursor.moveToNext()){
			name.setText(cursor.getString(0));
			System.out.println("image path==="+cursor.getString(7));
			bitmap=cursor.getString(7).equals("-1")? BitmapFactory.decodeResource(getResources(), R.drawable.team_default): BitmapFactory.decodeFile(cursor.getString(7));
			if(!(cursor.getString(4).equals("0")) && cursor.getString(1)!=null && !(cursor.getString(1).isEmpty())){
				teamid1=cursor.getString(1);
				Cursor cursor1=helper.select("teams", new String[]{"name", "image"}, "teamid=?", new String[]{teamid1}, null);
				if(cursor1.moveToNext()){
					name1=cursor1.getString(0);
					bitmap1=cursor1.getString(1).equals("-1")? BitmapFactory.decodeResource(getResources(), R.drawable.team_default): BitmapFactory.decodeFile(cursor1.getString(1));
					mList.add(name1);
				}
			}
			if(!(cursor.getString(5).equals("0")) && cursor.getString(2)!=null && !(cursor.getString(2).isEmpty())){
				teamid2=cursor.getString(2);
				Cursor cursor1=helper.select("teams", new String[]{"name", "image"}, "teamid=?", new String[]{teamid2}, null);
				if(cursor1.moveToNext()){
					name2=cursor1.getString(0);
					bitmap2=cursor1.getString(1).equals("-1")? BitmapFactory.decodeResource(getResources(), R.drawable.team_default): BitmapFactory.decodeFile(cursor1.getString(1));
					mList.add(name2);
				}
			}
			if(!(cursor.getString(6).equals("0")) && cursor.getString(3)!=null && !(cursor.getString(3).isEmpty())){
				teamid3=cursor.getString(3);
				Cursor cursor1=helper.select("teams", new String[]{"name", "image"}, "teamid=?", new String[]{teamid3}, null);
				if(cursor1.moveToNext()){
					name3=cursor1.getString(0);
					bitmap3=cursor1.getString(1).equals("-1")? BitmapFactory.decodeResource(getResources(), R.drawable.team_default): BitmapFactory.decodeFile(cursor1.getString(1));
					mList.add(name3);
				}
			}
			mList.add(name.getText().toString());
			iden.setText("请选择身份");
		}
	}
	
}
