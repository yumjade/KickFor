package com.example.kickfor.team;

import java.util.HashMap;
import java.util.Map;

import com.example.kickfor.ClientWrite;
import com.example.kickfor.HomePageActivity;
import com.example.kickfor.ListsFragment;
import com.example.kickfor.R;
import com.example.kickfor.SQLHelper;
import com.example.kickfor.Tools;
import com.example.kickfor.utils.IdentificationInterface;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class MatchReviewDetailFragment extends Fragment implements TeamInterface, IdentificationInterface{
	
	private MatchReviewEntity entity=null;
	private TextView dateAndPlace=null;
	private TextView score=null;
	private TextView type=null;
	private ImageView myTeamImg=null;
	private ImageView againstTeamImg=null;
	private TextView myTeamName=null;
	private TextView againstTeamName=null;
	private TextView whoGoal=null;
	private TextView whoAssist=null;
	private TextView whoCard=null;
	private TextView whoCome=null;
	private TextView format=null;
	private ImageView back=null;
	private TextView edit=null;
	private String teamid=null;
	private String authority=null;
	private boolean isAdd=false;
	private boolean isLoaded=false;
	

	@Override
	public int getFragmentLevel() {
		// TODO Auto-generated method stub
		return IdentificationInterface.SECOND_LEVEL;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Bundle bundle=getArguments();
		this.entity=(MatchReviewEntity)bundle.getSerializable("entity");
		this.teamid=bundle.getString("teamid");
		this.authority=bundle.getString("authority");
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("request", "get match detail");
		map.put("id", entity.getId());
		map.put("teamid", teamid);
		Runnable r=new ClientWrite(Tools.JsonEncode(map));
		new Thread(r).start();
		View view=inflater.inflate(R.layout.fragment_match_review_detail, container, false);
		dateAndPlace=(TextView)view.findViewById(R.id.detail_date_place);
		score=(TextView)view.findViewById(R.id.detail_match_result);
		type=(TextView)view.findViewById(R.id.detail_match_type);
		myTeamImg=(ImageView)view.findViewById(R.id.iv_detail_my_team);
		againstTeamImg=(ImageView)view.findViewById(R.id.iv_detail_against_team);
		myTeamName=(TextView)view.findViewById(R.id.tv_detail_my_team_name);
		againstTeamName=(TextView)view.findViewById(R.id.tv_detail_against_team_name);
		whoGoal=(TextView)view.findViewById(R.id.detail_who_goal);
		whoAssist=(TextView)view.findViewById(R.id.detail_who_assist);
		whoCome=(TextView)view.findViewById(R.id.detail_who_come);
		whoCard=(TextView)view.findViewById(R.id.detail_who_card);
//		format=(TextView)view.findViewById(R.id.detail_format);
		back=(ImageView)view.findViewById(R.id.detail_back);
		edit=(TextView)view.findViewById(R.id.detail_edit);
		back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				getActivity().onBackPressed();
			}
			
		});
		if(authority.equals("4")){
			edit.setVisibility(View.VISIBLE);
			edit.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					edit.setEnabled(false);
					((HomePageActivity)getActivity()).openEditReview(teamid, entity);
				}
				
			});
		}
		else{
			edit.setVisibility(View.GONE);
		}
		
		if(isLoaded==true){
			initiate();
		}
		isAdd=true;
		return view;
	}
	
	@SuppressWarnings("deprecation")
	private void initiate(){
		dateAndPlace.setText(entity.getDateAndPlace());
		score.setText(entity.getSocre());
		type.setText(entity.getType());
		myTeamImg.setImageBitmap(entity.getTeamImg());
		againstTeamImg.setImageBitmap(entity.getAgainstImg());
		myTeamName.setText(entity.getTeamName());
		againstTeamName.setText(entity.getAgainstName());
		String[] str=new String[4];
		str=entity.getDetails();
		whoCome.setText(str[0]);
		whoGoal.setText(str[1]);
		whoAssist.setText(str[2]);
		whoCard.setText(str[3]);
		Drawable background=new BitmapDrawable(this.getResources(), entity.getFormat());
//		format.setBackgroundDrawable(background);
	}
	
	public String getTeamid(){
		return teamid;
	}
	
	public void setData(Map<String, Object> map){
		Bitmap format=null;
		if(map.get("format").equals("-1"))
		{
			format=BitmapFactory.decodeResource(getResources(), R.drawable.court);
		}
		else{
			format=Tools.stringtoBitmap(map.get("format").toString());
		}
		format=BitmapFactory.decodeResource(getResources(), R.drawable.court);
		entity.setDetails(map.get("info").toString(), Tools.bitmapToString(format), map.get("type").toString());
		entity.setAssists(map.get("assist").toString());
		entity.setTime(map.get("time").toString());
		if(map.containsKey("place")){
			entity.setPlace(map.get("place").toString());
		}
		Bitmap againstImg=null;
		if(map.containsKey("againstImg")){
			if(map.get("againstImg").equals("-1"))
			{
				againstImg=BitmapFactory.decodeResource(getResources(), R.drawable.team_default);
			}
			else{
				againstImg=Tools.stringtoBitmap(map.get("againstImg").toString());
			}
			entity.setAgainstImg(Tools.bitmapToString(againstImg));
		}
		if(isAdd==true){
			initiate();
		}
		isLoaded=true;
		((HomePageActivity)getActivity()).removeVague();
	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub
		SQLHelper helper=SQLHelper.getInstance(getActivity());
		Cursor cursor=helper.select("systemtable", new String[]{"i"}, "id=? and teamid=? and type=? and message=?", new String[]{String.valueOf(entity.getId()), teamid, String.valueOf(ListsFragment.TYPE_TEAMS_MESSAGE), "update_review"}, null);
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
