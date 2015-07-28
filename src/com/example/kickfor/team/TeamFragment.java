package com.example.kickfor.team;


import com.example.kickfor.HomePageActivity;
import com.example.kickfor.R;
import com.example.kickfor.SQLHelper;
import com.example.kickfor.utils.IdentificationInterface;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TeamFragment extends Fragment implements OnClickListener, TeamInterface, IdentificationInterface{
	
	
	private ImageView teamImage=null;
	private TextView teamName=null;
//	private RelativeLayout teamHornor=null;
	private RelativeLayout teamInfoUp=null;
	private RelativeLayout teamInfoDown=null;
	private TextView teamMatchNumber=null;
	private TextView teamWinRate=null;
	private TextView teamTotalGoal=null;
	private TextView teamTotalLost=null;
	private TextView teamBestShooter=null;
	private TextView teamBestAssister=null;
	private TextView teamChangingRoom=null;
	private TextView teamPreMatch=null;
	private TextView teamReMatch=null;
	private TextView teamHallOfFame=null;
	
	
	private String teamid=null;
	private String authority=null;
	private Bitmap bitmap=null;
	
	
	@Override
	public int getFragmentLevel() {
		// TODO Auto-generated method stub
		return IdentificationInterface.MAIN_LEVEL;
	}
	

	private void init(){
		Bundle bundle=getArguments();
		this.teamid=bundle.getString("teamid");
		this.authority=bundle.getString("authority");
	}




	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		init();
		View view=inflater.inflate(R.layout.fragment_team1, container, false);
		
			teamImage=(ImageView)view.findViewById(R.id.iv_team_1_image);
			teamName=(TextView)view.findViewById(R.id.tv_team_name);
//			teamHornor=(RelativeLayout)view.findViewById(R.id.team_honor);
			teamInfoUp=(RelativeLayout)view.findViewById(R.id.team_info_up);
			teamInfoDown=(RelativeLayout)view.findViewById(R.id.team_info_down);
			teamMatchNumber=(TextView)view.findViewById(R.id.team_match_number);
			teamWinRate=(TextView)view.findViewById(R.id.team_win_rate);
			teamTotalGoal=(TextView)view.findViewById(R.id.team_goal_number);
			teamTotalLost=(TextView)view.findViewById(R.id.team_lost_number);
			teamBestShooter=(TextView)view.findViewById(R.id.team_best_shooter);
			teamBestAssister=(TextView)view.findViewById(R.id.team_best_assist);
			teamChangingRoom=(TextView)view.findViewById(R.id.tv_changing_room);
			teamPreMatch=(TextView)view.findViewById(R.id.tv_prematch);
			teamReMatch=(TextView)view.findViewById(R.id.tv_rematch);
			teamHallOfFame=(TextView)view.findViewById(R.id.tv_hall_fame);
			teamImage.setOnClickListener(this);
//			teamHornor.setOnClickListener(this);
			teamInfoUp.setOnClickListener(this);
			teamInfoDown.setOnClickListener(this);
			
			teamChangingRoom.setOnClickListener(this);
			teamPreMatch.setOnClickListener(this);
			teamReMatch.setOnClickListener(this);
			teamHallOfFame.setOnClickListener(this);
			initiate();
			
		
		
		
		return view;
	}
	
	public void initiate(){
		SQLHelper helper=SQLHelper.getInstance(this.getActivity());
		System.out.println("teamidddd="+teamid);
		String[] names=new String[]{"image", "name", "totalmatch", "lost", "assist", "goal", "win", "honor", "bestshooter", "bestassister"};
		Cursor cursor=helper.select("teams", names, "teamid=?", new String[]{teamid}, null);
		if(cursor.moveToNext()){
			String imgpath=cursor.getString(0);
			if(!imgpath.equals("-1")){
				bitmap=BitmapFactory.decodeFile(cursor.getString(0));
				teamImage.setImageBitmap(bitmap);
			}
			teamName.setText(cursor.getString(1));
			teamMatchNumber.setText(cursor.getString(2));
			teamWinRate.setText("100%");
			teamBestShooter.setText(cursor.getString(8));
			teamBestAssister.setText(cursor.getString(9));
			if(!cursor.getString(2).equals("0")){
				int win=Integer.parseInt(cursor.getString(6));
				int totalmatch=Integer.parseInt(cursor.getString(2));
				teamWinRate.setText(win*100/totalmatch+"%");
			}
			teamTotalGoal.setText(cursor.getString(5));
			teamTotalLost.setText(cursor.getString(3));
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		setEnable(false);
		((HomePageActivity)getActivity()).teamCommand(v, teamid, authority);
	}
	
	public String getTeamid(){
		return teamid;
	}

	
	
	public void setEnable(boolean enable){
		teamImage.setEnabled(enable);
//		teamHornor.setEnabled(enable);
		teamInfoUp.setEnabled(enable);
		teamInfoDown.setEnabled(enable);
		teamChangingRoom.setEnabled(enable);
		teamPreMatch.setEnabled(enable);
		teamReMatch.setEnabled(enable);
		teamHallOfFame.setEnabled(enable);
	}

}
