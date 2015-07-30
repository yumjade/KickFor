package com.example.kickfor.team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;














import com.example.kickfor.ClientWrite;
import com.example.kickfor.HomePageActivity;
import com.example.kickfor.HomePageInterface;
import com.example.kickfor.ListsFragment;
import com.example.kickfor.R;
import com.example.kickfor.SQLHelper;
import com.example.kickfor.Tools;
import com.example.kickfor.utils.IdentificationInterface;

import android.content.Context;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MatchPreviewFragment extends Fragment implements TeamInterface, HomePageInterface, IdentificationInterface{
	
	private ImageView myTeamImage=null;
	private ImageView againstTeamImage=null;
	private TextView myTeamName=null;
	private TextView againstTeamName=null;
	private TextView date=null;
	private TextView time=null;
	private TextView place=null;
	private TextView type=null;
	private TextView person=null;
	private TextView history=null;
	private List<TextView> historyGroup=null;
	private LinearLayout attendance=null;
	private TextView edit=null;
	private TextView ensureJoin=null;
	private RelativeLayout title=null;
	private Context context=null;
	private String teamid=null;
	private String againstid=null;
	private int id=-1;
	private int authority=0;
	private String number="";
	private String info="";
	private boolean isEnsure=false;
	private ImageView back=null;
	
	@Override
	public int getFragmentLevel() {
		// TODO Auto-generated method stub
		return IdentificationInterface.SECOND_LEVEL;
	}
	
	public void setNumber(String number){
		this.number=number;
	}
	
	public void setAttendanceInfo(String info){
		this.info=this.info+info+";";
		setAttendance();
		SQLHelper helper=SQLHelper.getInstance(context);
		Cursor cursor=helper.select("matches", new String[]{"ensure"}, "id=? and teamid=?", new String[]{String.valueOf(id), teamid}, null);
		if(cursor.moveToNext()){
			if(cursor.getString(0).equals("1")){
				isEnsure=true;
			}
			else{
				isEnsure=false;
			}
		}
	}
	
	
	private void prepare(){
		context=getActivity();
		Bundle bundle=getArguments();
		if(bundle.containsKey("teamid")){
			teamid=bundle.getString("teamid");
		}
		if(bundle.containsKey("authority")){
			authority=bundle.getInt("authority");
		}
		historyGroup=new ArrayList<TextView>();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		prepare();
		View view=inflater.inflate(R.layout.fragment_pre_match, container, false);
		title=(RelativeLayout)view.findViewById(R.id.pre_title);
		back=(ImageView)view.findViewById(R.id.pre_back);
		back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				back.setEnabled(false);
				((HomePageActivity)getActivity()).onBackPressed();
			}
			
		});
		myTeamImage=(ImageView)view.findViewById(R.id.pre_my_team_image);
		myTeamName=(TextView)view.findViewById(R.id.pre_my_team_name);
		againstTeamImage=(ImageView)view.findViewById(R.id.pre_against_team_image);
		againstTeamName=(TextView)view.findViewById(R.id.pre_against_team_name);
		date=(TextView)view.findViewById(R.id.pre_date);
		time=(TextView)view.findViewById(R.id.pre_time);
		place=(TextView)view.findViewById(R.id.pre_place);
		type=(TextView)view.findViewById(R.id.pre_type);
		person=(TextView)view.findViewById(R.id.pre_person);
		history=(TextView)view.findViewById(R.id.pre_history_whole);
		historyGroup.add((TextView)view.findViewById(R.id.pre_history_item1));
		historyGroup.add((TextView)view.findViewById(R.id.pre_history_item2));
		historyGroup.add((TextView)view.findViewById(R.id.pre_history_item3));
		historyGroup.add((TextView)view.findViewById(R.id.pre_history_item4));
		historyGroup.add((TextView)view.findViewById(R.id.pre_history_item5));
		attendance=(LinearLayout)view.findViewById(R.id.pre_members);
		ensureJoin=(TextView)view.findViewById(R.id.pre_ensure_join);
		edit=(TextView)view.findViewById(R.id.pre_reedit);
		initiate();
		if(authority==-1){
			edit.setVisibility(View.GONE);
			title.setVisibility(View.GONE);
			ensureJoin.setVisibility(View.VISIBLE);
			ensureJoin.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(isEnsure==true){
						Toast.makeText(context, "您已确认参加该场比赛", Toast.LENGTH_LONG).show();
					}
					else{
						ensureJoin.setEnabled(false);
						Map<String, Object> tmp=new HashMap<String, Object>();
						SQLHelper helper=SQLHelper.getInstance(context);
						Cursor cursor=helper.select("ich", new String[]{"name"}, "phone=?", new String[]{"host"}, null);
						cursor.moveToNext();
						tmp.put("info", number+" "+cursor.getString(0));
						tmp.put("request", "join match");
						tmp.put("teamname", myTeamName.getText().toString());
						tmp.put("againstname", againstTeamName.getText().toString());
						tmp.put("teamid", teamid);
						tmp.put("id", id);
						Runnable r=new ClientWrite(Tools.JsonEncode(tmp));
						new Thread(r).start();
					}
				}
				
			});
		}
		else{
			ensureJoin.setVisibility(View.GONE);
			edit.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(authority<4){
						Toast.makeText(context, "不好意思，您不是球队的管理员", Toast.LENGTH_LONG).show();
					}
					else{
						edit.setEnabled(false);
						((HomePageActivity)getActivity()).edit(id, teamid, againstid, againstTeamName.getText().toString(), place.getText().toString(), date.getText().toString(), time.getText().toString(), type.getText().toString(), person.getText().toString());
					}
				}
				
			});
		}
		return view;
	}
	
	private void initiate(){
		SQLHelper helper=SQLHelper.getInstance(context);
		Cursor cursor=helper.select("matches", new String[]{"date", "time", "id", "ensure"}, "status=? and teamid=?", new String[]{"u", teamid}, null);
		if(cursor.moveToNext()){
			id=cursor.getInt(2);
			System.out.println("id==="+id);
			date.setText(cursor.getString(0));
			time.setText(cursor.getString(1));
			if(cursor.getString(3).equals('0')){
				isEnsure=false;
			}
		}
		Cursor cursor2=helper.select("teams", new String[]{"name", "image"},  "teamid=?", new String[]{teamid}, null);
		cursor2.moveToNext();
		String myTeamImgPath=cursor2.getString(1);
		if(!(myTeamImgPath.equals("-1"))){
			myTeamImage.setImageBitmap(BitmapFactory.decodeFile(myTeamImgPath));
		}
		myTeamName.setText(cursor2.getString(0));
		Map<String, Object> map=new HashMap<String, Object>();
		if(id==-1){
			((HomePageActivity)getActivity()).removeVague();
		}
		map.put("request", "get updated match");
		map.put("teamid", teamid);
		map.put("id", id);
		Runnable r=new ClientWrite(Tools.JsonEncode(map));
		new Thread(r).start();
	}
	
	public String getTeamid(){
		return teamid;
	}
	
	public void setData(Map<String, Object> map){
		String path=map.get("againstimg").toString();
		if(!path.equals("-1")){
			againstTeamImage.setImageBitmap(BitmapFactory.decodeFile(path));
		}
		else{
			againstTeamImage.setImageResource(R.drawable.team_default);
		}
		againstTeamName.setText(map.get("againstname").toString());
		place.setText(map.get("place").toString());
		info=map.get("info").toString();
		
		setAttendance();
		
		type.setText(map.get("type").toString());
		person.setText(map.get("person").toString());
		againstid=map.get("againstid").toString();
		map.remove("againstimg");
		map.remove("againstname");
		map.remove("place");
		map.remove("type");
		map.remove("person");
		map.remove("info");
		map.remove("againstid");
		Iterator<String> iter=map.keySet().iterator();
		int index=0;
		int win=0;
		int pull=0;
		int defeat=0;
		System.out.println("11111");
		while(iter.hasNext()){
			System.out.println("2222");
			String key=iter.next();
			Map<String, Object> temp=Tools.getMapForJson(map.get(key).toString());
			int goals=Integer.parseInt(temp.get("goals").toString());
			int lost=Integer.parseInt(temp.get("lost").toString());
			if(goals>lost){
				win=win+1;
			}
			else if(goals==lost){
				pull=pull+1;
			}
			else{
				defeat=defeat+1;
			}
			System.out.println("3333");
			historyGroup.get(index).setText(temp.get("date")+" "+goals+" - "+lost);
			System.out.println("4444");
			index=index+1;
		}
		history.setText(win+"胜"+pull+"平"+defeat+"负");
	}

	private void setAttendance(){
		attendance.removeAllViews();
		List<String> list0=explode(info, ";");
		int n=list0.size();
		int i=0;
		int j=-1;
		while(i<n){
			if(j==-1){
				j=0;
				LinearLayout ll=new LinearLayout(getActivity());
				ll.setOrientation(LinearLayout.HORIZONTAL);
				LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				ll.setPadding(10, 10, 10, 0);
				attendance.addView(ll, j, params);
			}
			TextView v=new TextView(getActivity());
			v.setText(list0.get(i));
			if((i/4)!=j){
				j=i/4;
				LinearLayout ll=new LinearLayout(getActivity());
				ll.setOrientation(LinearLayout.HORIZONTAL);
				LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				ll.setPadding(10, 30, 10, 0);
				attendance.addView(ll, j, params);
			}
			LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			params.weight=1;
			((LinearLayout)attendance.getChildAt(j)).addView(v, params);
			i++;
		}
		int lasting=n%4;
		for(int m=0; m<lasting; m++){
			TextView v=new TextView(getActivity());
			v.setText("");
			LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			params.weight=1;
			((LinearLayout)attendance.getChildAt(j)).addView(v, params);
		}
		if(j<1){
			int last=1-j;
			for(int m=1; m<=last; m++){
				LinearLayout ll=new LinearLayout(getActivity());
				ll.setOrientation(LinearLayout.HORIZONTAL);
				for(int mm=0; mm<=3; mm++){
					TextView v=new TextView(getActivity());
					v.setText("");
					LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
					params.weight=1;
					ll.addView(v, params);
				}
				attendance.addView(ll, j+m);
			}
		}
	}
	
	public int getAuthority(){
		return authority;
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		SQLHelper helper=SQLHelper.getInstance(context);
		Cursor cursor=helper.select("systemtable", new String[]{"i"}, "id=? and teamid=? and type=? and (message=? or message=? or message=? or message=?) ", new String[]{String.valueOf(id), teamid, String.valueOf(ListsFragment.TYPE_TEAMS_MESSAGE), "join_new_match", "publish_new_match", "update_new_match", "delete_new_match"}, null);
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
	
	private List<String> explode(String str, String sign){
    	int end=str.indexOf(sign);
    	int sizeoff=sign.length();
    	List<String> mList=new ArrayList<String>();
    	while(str.length()!=0){
    		mList.add(str.substring(0, end));
    		str=str.substring(end+sizeoff);
    		end=str.indexOf(sign);
    	}
    	return mList;
    }

}
