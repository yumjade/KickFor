package com.example.kickfor.lobby;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.kickfor.ClientWrite;
import com.example.kickfor.HomePageActivity;
import com.example.kickfor.R;
import com.example.kickfor.SQLHelper;
import com.example.kickfor.Tools;
import com.example.kickfor.utils.IdentificationInterface;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Toast;

public class LobbyTeamAddFragment extends Fragment implements IdentificationInterface, LobbyInterface{

    private ImageView image=null;
    private TextView name=null;
    private RadioGroup type=null;
	private EditText text=null;
	private TextView submit=null;
	private ImageView back=null; 
	
	private String teamid1=null;
	private String teamid2=null;
	private String teamid3=null;
	private String name1=null;
	private String name2=null;
	private String name3=null;
	private Bitmap bitmap1=null;
	private Bitmap bitmap2=null;
	private Bitmap bitmap3=null;
	private List<String> mList=null;
	
	private Context context=null;
	
	private String teamid=null;
	private String choice=null;
	private String phone=null;
	
	private void init(){
		Bundle bundle=getArguments();
		this.phone=bundle.getString("phone");
		this.context=getActivity();
		this.mList=new ArrayList<String>();
	}
	
	@Override
	public int getFragmentLevel() {
		// TODO Auto-generated method stub
		return IdentificationInterface.SECOND_LEVEL;
	}

	@Override
	public void setEnable(boolean enable) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		init();
		View view=inflater.inflate(R.layout.fragment_lobby_publish, container, false);
		back=(ImageView)view.findViewById(R.id.lobby_add_back);
		image=(ImageView)view.findViewById(R.id.lobby_add_image);
		name=(TextView)view.findViewById(R.id.lobby_add_name);
		type=(RadioGroup)view.findViewById(R.id.lobby_add_type);
		text=(EditText)view.findViewById(R.id.lobby_add_text);
		submit=(TextView)view.findViewById(R.id.lobby_add_submit);
		choice="1";
		initiate();
		
		type.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if(checkedId==R.id.lobby_add_hire){
					choice="1";
					text.setHint("招人：建议写出位置、年龄、身高体重需求");
				}
				else if(checkedId==R.id.lobby_add_yue){
					choice="2";
					text.setHint("约战：建议写出合适的地点时间人数");
				}
				else if(checkedId==R.id.lobby_add_yue){
					choice="3";
					text.setHint("话题：写下话题");
				}
			}
			
		});
		
		
		name.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				final TeamPopupWindow pop=new TeamPopupWindow(context, mList, name.getWidth(), name.getHeight());
				int[] location = new int[2];  
		        name.getLocationOnScreen(location);
		        pop.showAsDropDown(name);
				pop.setOnDismissListener(new OnDismissListener(){  
		            public void onDismiss(){
		            	String str=pop.getSelect();
		            	name.setText(str);
		            	if(str.equals(name1)){
		            		image.setImageBitmap(bitmap1);
		            		teamid=teamid1;
		            	}
		            	else if(str.equals(name2)){
		            		image.setImageBitmap(bitmap2);
		            		teamid=teamid2;
		            	}
		            	else if(str.equals(name3)){
		            		image.setImageBitmap(bitmap3);
		            		teamid=teamid3;
		            	}
		            }             
		        });  
			}
			
		});
		
		back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				((HomePageActivity)getActivity()).onBackPressed();
			}
			
		});
		
		submit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String str=text.getText().toString();
				if(str.isEmpty()){
					Toast.makeText(context, "发布内容不能为空", Toast.LENGTH_SHORT).show();
				}
				else if(teamid==null){
					Toast.makeText(context, "请选择球队发布", Toast.LENGTH_SHORT).show();
				}
				else{
					Map<String, Object>map=new HashMap<String, Object>();
					map.put("request", "add theme");
					map.put("phone", phone);
					map.put("teamid", teamid);
					map.put("type", choice);
					map.put("content", str);
					Runnable r=new ClientWrite(Tools.JsonEncode(map));
					new Thread(r).start();
				}
			}
			
		});
		
		return view;
	}
	
	private void initiate(){
		SQLHelper helper=SQLHelper.getInstance(context);
		Cursor cursor=helper.select("ich", new String[]{"name", "team1", "team2", "team3", "authority1", "authority2", "authority3"}, "phone=?", new String[]{"host"}, null);
		if(cursor.moveToNext()){
			name.setText("点击选择球队");
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
		}
	}
}
