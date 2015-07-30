package com.example.kickfor.team;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
import android.widget.LinearLayout;
import android.widget.TextView;

public class MatchReviewDetailFragment extends Fragment implements TeamInterface, IdentificationInterface{
	
	private MatchReviewEntity entity=null;
	private TextView date=null;
	private TextView place=null;
	private TextView score=null;
	private TextView type=null;
	private ImageView myTeamImg=null;
	private ImageView againstTeamImg=null;
	private TextView myTeamName=null;
	private TextView againstTeamName=null;
	private LinearLayout whoGoal=null;
	private LinearLayout whoAssist=null;
	private LinearLayout whoCard=null;
	private LinearLayout whoCome=null;
	private TextView format=null;
	private ImageView back=null;
	private TextView edit=null;
	private String teamid=null;
	private String authority=null;
	private boolean isAdd=false;
	private boolean isLoaded=false;
	private float density;
	
	private Bitmap goalimage=null;
	private Bitmap assistimage=null;
	private Bitmap yellowimage=null;
	private Bitmap redimage=null;

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
		goalimage=BitmapFactory.decodeResource(getResources(), R.drawable.goal_one);
		assistimage=BitmapFactory.decodeResource(getResources(), R.drawable.assist_one);
		yellowimage=BitmapFactory.decodeResource(getResources(), R.drawable.yellow_card);
		redimage=BitmapFactory.decodeResource(getResources(), R.drawable.red_card);
		
		density= getActivity().getResources().getDisplayMetrics().density;
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("request", "get match detail");
		map.put("id", entity.getId());
		map.put("teamid", teamid);
		Runnable r=new ClientWrite(Tools.JsonEncode(map));
		new Thread(r).start();
		View view=inflater.inflate(R.layout.fragment_match_review_detail, container, false);
		date=(TextView)view.findViewById(R.id.detail_date);
		place=(TextView)view.findViewById(R.id.detail_place);
		score=(TextView)view.findViewById(R.id.detail_match_result);
		type=(TextView)view.findViewById(R.id.detail_match_type);
		myTeamImg=(ImageView)view.findViewById(R.id.iv_detail_my_team);
		againstTeamImg=(ImageView)view.findViewById(R.id.iv_detail_against_team);
		myTeamName=(TextView)view.findViewById(R.id.tv_detail_my_team_name);
		againstTeamName=(TextView)view.findViewById(R.id.tv_detail_against_team_name);
		whoGoal=(LinearLayout)view.findViewById(R.id.detail_who_goal);
		whoAssist=(LinearLayout)view.findViewById(R.id.detail_who_assist);
		whoCome=(LinearLayout)view.findViewById(R.id.detail_who_come);
		whoCard=(LinearLayout)view.findViewById(R.id.detail_who_card);
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
		date.setText(entity.getDate());
		place.setText(entity.getPlace());
		score.setText(entity.getSocre());
		type.setText(entity.getType());
		myTeamImg.setImageBitmap(entity.getTeamImg());
		againstTeamImg.setImageBitmap(entity.getAgainstImg());
		myTeamName.setText(entity.getTeamName());
		againstTeamName.setText(entity.getAgainstName());
		String[] str=new String[4];
		str=entity.getDetails();
		
		List<String> list0=explode(str[0], ";");
		int n=list0.size();
		int i=0;
		int j=0;
		while(i<n){
			TextView v=new TextView(getActivity());
			v.setText(list0.get(i));
			if((i/4)!=j){
				j=i/4;
				LinearLayout ll=new LinearLayout(getActivity());
				ll.setOrientation(LinearLayout.HORIZONTAL);
				LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				ll.setPadding(10, 30, 10, 0);
				whoCome.addView(ll, j, params);
			}
			LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			params.weight=1;
			((LinearLayout)whoCome.getChildAt(j)).addView(v, params);
			i++;
		}
		int lasting=n%4;
		for(int m=0; m<lasting; m++){
			TextView v=new TextView(getActivity());
			v.setText("");
			LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			params.weight=1;
			((LinearLayout)whoCome.getChildAt(j)).addView(v, params);
		}
		if(j<3){
			int last=3-j;
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
				whoCome.addView(ll, j+m);
			}
		}
		
		if(str[1]!=null){
			i=0;
			List<String> list1=explode(str[1], ";");
			n=list1.size()+i;
			int dimens1 = 20;
			int dimens2=16;
			int finalDimens1 = (int)(dimens1 * density);
			int finalDimens2 = (int)(dimens2 * density);
			while(i<n){
				LinearLayout ll=new LinearLayout(getActivity());
				ll.setOrientation(LinearLayout.HORIZONTAL);
				TextView v=new TextView(getActivity());
				List<String> tmp=explode(list1.get(i),"x");
				int assist=Integer.parseInt(tmp.get(1));
				v.setText(tmp.get(0));
				ll.addView(v);
				LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(finalDimens1, finalDimens2);
				for(int m=0; m<assist; m++){
					ImageView iv=new ImageView(getActivity());
					iv.setLayoutParams(p);
					iv.setImageBitmap(goalimage);
					ll.addView(iv);
				}
				LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				ll.setPadding(0, 0, 10, 10);
				((LinearLayout)whoGoal).addView(ll, i, params);
				i++;
			}
		}
		
		if(str[2]!=null){
			i=0;
			List<String> list2=explode(str[2], ";");
			n=list2.size()+i;
			int dimens1 = 20;
			int dimens2=16;
			int finalDimens1 = (int)(dimens1 * density);
			int finalDimens2 = (int)(dimens2 * density);
			while(i<n){
				LinearLayout ll=new LinearLayout(getActivity());
				ll.setOrientation(LinearLayout.HORIZONTAL);
				TextView v=new TextView(getActivity());
				List<String> tmp=explode(list2.get(i),"x");
				int goal=Integer.parseInt(tmp.get(1));
				v.setText(tmp.get(0));
				ll.addView(v);
				LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(finalDimens1, finalDimens2);
				for(int m=0; m<goal; m++){
					ImageView iv=new ImageView(getActivity());
					iv.setLayoutParams(p);
					iv.setImageBitmap(assistimage);
					ll.addView(iv);
				}
				LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				ll.setPadding(0, 0, 10, 10);
				((LinearLayout)whoAssist).addView(ll, i, params);
				i++;
			}
		}
		
		
		if(str[4]!=null){
			List<String> list4=explode(str[4], ";");
			i=0;
			n=list4.size();
			int dimens1 = 20;
			int dimens2=16;
			int finalDimens1 = (int)(dimens1 * density);
			int finalDimens2 = (int)(dimens2 * density);
			while(i<n){
				LinearLayout ll=new LinearLayout(getActivity());
				ll.setOrientation(LinearLayout.HORIZONTAL);
				TextView v=new TextView(getActivity());
				v.setText(list4.get(i));
				LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(finalDimens1, finalDimens2);
				ImageView iv=new ImageView(getActivity());
				iv.setLayoutParams(p);
				iv.setImageBitmap(redimage);
				ll.addView(iv);
				ll.addView(v);
				LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				ll.setPadding(0, 0, 10, 10);
				((LinearLayout)whoCard).addView(ll, i, params);
				i++;
			}
		}
		
		if(str[3]!=null){
			List<String> list3=explode(str[3], ";");
			n=list3.size();
			System.out.println("n========="+str[3]);
			i=0;
			int dimens1 = 20;
			int dimens2=16;
			int finalDimens1 = (int)(dimens1 * density);
			int finalDimens2 = (int)(dimens2 * density);
			while(i<n){
				LinearLayout ll=new LinearLayout(getActivity());
				ll.setOrientation(LinearLayout.HORIZONTAL);
				TextView v=new TextView(getActivity());
				v.setText(list3.get(i));
				LinearLayout.LayoutParams p=new LinearLayout.LayoutParams(finalDimens1, finalDimens2);
				ImageView iv=new ImageView(getActivity());
				iv.setLayoutParams(p);
				iv.setImageBitmap(yellowimage);
				ll.addView(iv);
				ll.addView(v);
				LinearLayout.LayoutParams params=new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
				ll.setPadding(0, 0, 10, 10);
				((LinearLayout)whoCard).addView(ll, i, params);
				i++;
			}
		}
		
		
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
		entity.setPerson(map.get("person").toString());
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
