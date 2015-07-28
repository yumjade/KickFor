package com.example.kickfor.team;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;








import com.example.kickfor.ClientWrite;
import com.example.kickfor.HomePageActivity;
import com.example.kickfor.R;
import com.example.kickfor.Tools;
import com.example.kickfor.utils.IdentificationInterface;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class OtherTeamFragment extends Fragment implements OnClickListener, IdentificationInterface{
	
	private ImageView back=null;
	private TextView titleText=null;
	private ImageView image=null;
	private TextView name=null;
	private TextView showMember=null;
	private ListView mListView1=null;
	private RelativeLayout showInfo1=null;
	private TextView matchNumber=null;
	private TextView winRate=null;
	private TextView goal=null;
	private TextView lost=null;
	private ListView mListView2=null;
	private RelativeLayout showInfo2=null;
	private RelativeLayout info2Detail=null;
	private TextView bestShooter=null;
	private TextView bestAssister=null;
	private TextView selectShooter=null;
	private TextView selectAssister=null;
	private ListView mListView3=null;
	private TextView info3=null;
	private TextView joinButton=null;
	private TextView subscribeButton=null;
	private TeamMemberAdapter adapter1=null;
	private MatchLogAdapter adapter2=null;
	private ShooterAssisterAdapter adapter3=null;
	private ShooterAssisterAdapter adapter4=null;
	private LinearLayout logInfo = null;
	
	private TextView tmp=null;
	
	private Context context=null;
	private String teamid=null;
	private Map<String, Object>map=null;
	private Bitmap teamImage=null;
	
	private List<MemberInfo> mList1=new ArrayList<MemberInfo>();
	private List<MatchLogEntity> mList2=new ArrayList<MatchLogEntity>();
	private List<ShooterAssister> mList3=new ArrayList<ShooterAssister>(); 
	private List<ShooterAssister> mList4=new ArrayList<ShooterAssister>();
	
	private boolean isList1Visible=false;
	private boolean isList2Visible=false;
	private boolean isList3Visible=false;
	
	@Override
	public int getFragmentLevel() {
		// TODO Auto-generated method stub
		return IdentificationInterface.SECOND_LEVEL;
	}
	
	public OtherTeamFragment(Context context, String teamid, Map<String, Object> map){
		this.context=context;
		this.teamid=teamid;
		this.map=map;
	}
	
	public void setImage(Bitmap image){
		teamImage=image;
	}

	private void setList1(Map<String, Object> map){
		mList1.clear();
		Iterator<String> iter=map.keySet().iterator();
		while(iter.hasNext()){
			String key=iter.next();
			Map<String, Object> temp=Tools.getMapForJson(map.get(key).toString());
			MemberInfo item=new MemberInfo(temp.get("phone").toString(), context);
			item.setName(temp.get("name").toString());
			item.setNumber(temp.get("number").toString());
			item.setPosition(temp.get("position1").toString(), temp.get("position2").toString());
			item.setAInfo(temp.get("attendance").toString(), temp.get("totalmatch").toString());
			mList1.add(item);
		}
		adapter1.notifyDataSetChanged();
		Tools.setListViewHeight(mListView1);
	}
	
	private void setList2(Map<String, Object> map){
		mList2.clear();
		Iterator<String> iter=map.keySet().iterator();
		while(iter.hasNext()){
			String key=iter.next();
			Map<String, Object> temp=Tools.getMapForJson(map.get(key).toString());
			MatchLogEntity item=new MatchLogEntity(temp.get("type").toString(), temp.get("number").toString(), temp.get("result").toString(), temp.get("goals").toString(), temp.get("lost").toString());
			mList2.add(item);
		}
		adapter2.notifyDataSetChanged();
		Tools.setListViewHeight(mListView2);
	}
	
	private void setList3(Map<String, Object> map){
		mList3.clear();
		ShooterAssister title=new ShooterAssister(true);
		mList3.add(title);
		Iterator<String> iter=map.keySet().iterator();
		while(iter.hasNext()){
			String key=iter.next();
			Map<String, Object> temp=Tools.getMapForJson(map.get(key).toString());
			ShooterAssister item=new ShooterAssister(true, temp.get("name").toString(), temp.get("goal").toString(), temp.get("totalmatch").toString());
			mList3.add(item);
		}
		adapter3.notifyDataSetChanged();
		Tools.setListViewHeight(mListView3);
	}
	
	private void setList4(Map<String, Object> map){
		mList4.clear();
		ShooterAssister title=new ShooterAssister(false);
		mList4.add(title);
		Iterator<String> iter=map.keySet().iterator();
		while(iter.hasNext()){
			String key=iter.next();
			Map<String, Object> temp=Tools.getMapForJson(map.get(key).toString());
			ShooterAssister item=new ShooterAssister(false, temp.get("name").toString(), temp.get("assist").toString(), temp.get("totalmatch").toString());
			mList4.add(item);
		}
		adapter4.notifyDataSetChanged();
		Tools.setListViewHeight(mListView3);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		((HomePageActivity)getActivity()).removeVague();
		View view=inflater.inflate(R.layout.fragment_other_team, container, false);
		tmp=(TextView)view.findViewById(R.id.other_team_shiqiu);
		back=(ImageView)view.findViewById(R.id.other_team_back);
		titleText=(TextView)view.findViewById(R.id.other_team_text);
		image=(ImageView)view.findViewById(R.id.other_team_image);
		name=(TextView)view.findViewById(R.id.other_team_name);
		showMember=(TextView)view.findViewById(R.id.other_team_player);
		mListView1=(ListView)view.findViewById(R.id.other_team_player_list);
		showInfo1=(RelativeLayout)view.findViewById(R.id.other_team_info_up);
		matchNumber=(TextView)view.findViewById(R.id.other_team_match_number);
		winRate=(TextView)view.findViewById(R.id.other_team_win_rate);
		goal=(TextView)view.findViewById(R.id.other_team_goal_number);
		lost=(TextView)view.findViewById(R.id.other_team_lost_number);
		mListView2=(ListView)view.findViewById(R.id.other_team_match_list);
		logInfo = (LinearLayout) view.findViewById(R.id.ll_other_team_log);
		showInfo2=(RelativeLayout)view.findViewById(R.id.other_team_info_down);
		bestShooter=(TextView)view.findViewById(R.id.other_team_best_shooter);
		bestAssister=(TextView)view.findViewById(R.id.other_team_best_assist);
		selectShooter=(TextView)view.findViewById(R.id.other_team_shooter);
		selectAssister=(TextView)view.findViewById(R.id.other_team_assister);
		info2Detail=(RelativeLayout)view.findViewById(R.id.other_team_shooter_assister_detail);
		mListView3=(ListView)view.findViewById(R.id.other_team_shooter_assister_list);
		info3=(TextView)view.findViewById(R.id.other_team_next_match);
		joinButton=(TextView)view.findViewById(R.id.other_team_joinin);
		subscribeButton=(TextView)view.findViewById(R.id.other_team_focus);
		adapter1=new TeamMemberAdapter(context, mList1);
		adapter2=new MatchLogAdapter(context, mList2);
		adapter3=new ShooterAssisterAdapter(context, mList3);
		adapter4=new ShooterAssisterAdapter(context, mList4);
		mListView1.setAdapter(adapter1);
		mListView2.setAdapter(adapter2);
		mListView3.setAdapter(adapter3);
		initiate(Tools.getMapForJson(map.get("team_info").toString()));
		setList1(Tools.getMapForJson(map.get("team_mate").toString()));
		if(map.containsKey("team_match")){
			setList2(Tools.getMapForJson(map.get("team_match").toString()));
		}
		if(map.containsKey("team_shooter")){
			setList3(Tools.getMapForJson(map.get("team_shooter").toString()));
		}
		if(map.containsKey("team_assister")){
			setList4(Tools.getMapForJson(map.get("team_assister").toString()));
		}
		return view;
	}
	
	private void initiate(Map<String, Object> map){
		String teamName=map.get("name").toString();
		titleText.setText(teamName);
		name.setText(teamName);
		Drawable drawable= getResources().getDrawable(R.drawable.more_down);
		showMember.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
		mListView1.setVisibility(View.GONE);
		map.get("totalmatch");
		matchNumber.setText(map.get("totalmatch").toString());
		double win=Integer.parseInt(map.get("win").toString());
		double totalMatch=Integer.parseInt(map.get("totalmatch").toString());
		winRate.setText(convert(win/totalMatch*100)+"%");
		goal.setText(map.get("goal").toString());
		lost.setText(map.get("lost").toString());
		tmp.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
		logInfo.setVisibility(View.GONE);
		mListView2.setVisibility(View.GONE);
		bestShooter.setText(map.get("bestshooter").toString());
		bestAssister.setText(map.get("bestassister").toString());
		selectShooter.setTextColor(getResources().getColor(R.color.tab_pressed));
		selectAssister.setTextColor(getResources().getColor(R.color.tab_normal));
		bestAssister.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
		info2Detail.setVisibility(View.GONE);
		info3.setText(map.get("next match").toString()+" "+map.get("next time").toString());
		image.setImageBitmap(teamImage);
		subscribeButton.setVisibility(View.GONE);
		back.setOnClickListener(this);
		showMember.setOnClickListener(this);
		showInfo1.setOnClickListener(this);
		showInfo2.setOnClickListener(this);
		selectShooter.setOnClickListener(this);
		selectAssister.setOnClickListener(this);
		joinButton.setOnClickListener(this);
		subscribeButton.setOnClickListener(this);
	}
	
	
	
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id=v.getId();
		switch(id){
		case R.id.other_team_back:{
			((HomePageActivity)getActivity()).onBackPressed();
			break;
		}
		case R.id.other_team_player:{
			if(!isList1Visible){
				mListView1.setVisibility(View.VISIBLE);
				Drawable drawable= getResources().getDrawable(R.drawable.more_up);
				showMember.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
				Tools.setListViewHeight(mListView1);
				isList1Visible=true;
			}
			else{
				mListView1.setVisibility(View.GONE);
				Drawable drawable= getResources().getDrawable(R.drawable.more_down);
				showMember.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
				isList1Visible=false;
			}
			break;
		}
		case R.id.other_team_info_up:{
			if(!isList2Visible){
				logInfo.setVisibility(View.VISIBLE);
//				mListView2.setAdapter(adapter2);
				mListView2.setVisibility(View.VISIBLE);
				Drawable drawable= getResources().getDrawable(R.drawable.more_up);
				tmp.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
				Tools.setListViewHeight(mListView2);
				isList2Visible=true;
			}
			else{
				logInfo.setVisibility(View.GONE);
//				mListView2.setAdapter(adapter2);
				mListView2.setVisibility(View.GONE);
				Drawable drawable= getResources().getDrawable(R.drawable.more_down);
				tmp.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
				isList2Visible=false;
			}
			break;
		}
		case R.id.other_team_info_down:{
			if(!isList3Visible){
				info2Detail.setVisibility(View.VISIBLE);
				Drawable drawable= getResources().getDrawable(R.drawable.more_up);
				bestAssister.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
				Tools.setListViewHeight(mListView3);
				isList3Visible=true;
			}
			else{
				info2Detail.setVisibility(View.GONE);
				Drawable drawable= getResources().getDrawable(R.drawable.more_down);
				bestAssister.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
				isList3Visible=false;
			}
			break;
		}
		case R.id.other_team_shooter:{
			selectShooter.setTextColor(getResources().getColor(R.color.tab_pressed));
			selectAssister.setTextColor(getResources().getColor(R.color.tab_normal));
			mListView3.setAdapter(adapter3);
			Tools.setListViewHeight(mListView3);
			break;
		}
		case R.id.other_team_assister:{
			selectAssister.setTextColor(getResources().getColor(R.color.tab_pressed));
			selectShooter.setTextColor(getResources().getColor(R.color.tab_normal));
			mListView3.setAdapter(adapter4);
			Tools.setListViewHeight(mListView3);			
			break;
		}
		case R.id.other_team_joinin:{
			Toast.makeText(getActivity(), "ƒ˙“—æ≠…Í«Î£¨«Îµ»¥˝…Û∫À£°", Toast.LENGTH_SHORT).show();
			Map<String, Object> tmp=new HashMap<String, Object>();
			tmp.put("request", "apply join");
			tmp.put("teamid", teamid);
			tmp.put("date", Tools.getDate());
			Runnable r=new ClientWrite(Tools.JsonEncode(tmp));
			new Thread(r).start();
			break;
		}
		case R.id.other_team_focus:{
			
			break;
		}
		}
	}

	private String convert(double f) {
        DecimalFormat df = new DecimalFormat("#.0");
        return String.valueOf(df.format(f));
    }
	
	
	
}
