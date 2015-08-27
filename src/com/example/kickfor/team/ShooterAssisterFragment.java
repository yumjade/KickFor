package com.example.kickfor.team;

import java.util.ArrayList;
import java.util.List;

import com.example.kickfor.R;
import com.example.kickfor.SQLHelper;
import com.example.kickfor.HomePageActivity;
import com.example.kickfor.utils.IdentificationInterface;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ShooterAssisterFragment extends Fragment implements OnClickListener, TeamInterface, IdentificationInterface{

	private List<ShooterAssister> mList=new ArrayList<ShooterAssister>();
	private String teamid=null;
	private String tableName=null;
	private Context context=null;
	private ListView mListView=null;
	private TextView buttonShooter=null;
	private TextView buttonAssister=null;
	private TextView tabShooter;
	private TextView tabAssister;
	private ImageView back=null;
	
	
	private ShooterAssisterAdapter adapter=null;
	
	private static final boolean SHOOTER_LIST=true;
	private static final boolean ASSISTER_LIST=false;
	
	private boolean select=SHOOTER_LIST;
	
	@Override
	public int getFragmentLevel() {
		return IdentificationInterface.SECOND_LEVEL;
	}
	
	@Override
	public void setEnable(boolean enable) {
		
	}
	
	private void init(){
		Bundle bundle=getArguments();
		context=getActivity();
		this.teamid=bundle.getString("teamid");
		this.tableName="f_"+this.teamid;	
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		init();
		View view=inflater.inflate(R.layout.fragment_shooter_assister, container, false);
		back=(ImageView)view.findViewById(R.id.shooter_assister_back);
		back.setOnClickListener(this);
		mListView=(ListView)view.findViewById(R.id.shooter_assister_list);
		buttonShooter=(TextView)view.findViewById(R.id.shooter_assister_shooter);
		buttonAssister=(TextView)view.findViewById(R.id.shooter_assister_assister);
		tabShooter = (TextView) view.findViewById(R.id.tab_shooter);
		tabAssister = (TextView) view.findViewById(R.id.tab_assister);
		
		buttonShooter.setOnClickListener(this);
		buttonAssister.setOnClickListener(this);
		initiate(SHOOTER_LIST);
		adapter=new ShooterAssisterAdapter(context, mList);
		mListView.setAdapter(adapter);
		return view;
	}
	
	
	public String getTeamid(){
		return teamid;
	}
	
	public void changedData(){
		initiate(select);
		adapter.notifyDataSetChanged();
	}
	
	

	@Override
	public void onClick(View view) {
		int id=view.getId();
		if(id==R.id.shooter_assister_shooter){
			select=SHOOTER_LIST;
			initiate(select);
			
			adapter.notifyDataSetChanged();
		}
		else if(id==R.id.shooter_assister_assister){
			select=ASSISTER_LIST;
			initiate(select);
			
			adapter.notifyDataSetChanged();
		}
		else if(id==R.id.shooter_assister_back){
			((HomePageActivity)getActivity()).onBackPressed();
		}
	}

	private void initiate(boolean select){
		mList.clear();
		SQLHelper helper=SQLHelper.getInstance(context);
		helper.createTeamMateTable(tableName);
		if(select==true){
			buttonShooter.setTextColor(getResources().getColor(R.color.tab_pressed));
			buttonAssister.setTextColor(getResources().getColor(R.color.tab_normal));
			tabShooter.setVisibility(View.VISIBLE);
			tabAssister.setVisibility(View.INVISIBLE);
			tabShooter.setBackgroundColor(getResources().getColor(R.color.tab_pressed));
			tabAssister.setBackgroundColor(getResources().getColor(R.color.tab_normal));
			ShooterAssister title=new ShooterAssister(true);
			mList.add(title);
			Cursor cursor=helper.select(tableName, new String[]{"name", "number", "goal", "attendance"}, null, null, "goal desc, attendance asc, assist desc");
			int index=1;
			while(cursor.moveToNext()){
				ShooterAssister item=new ShooterAssister(true, index+cursor.getString(0), cursor.getString(2), cursor.getString(3));
				mList.add(item);
				index=index+1;
			}
		}
		else{
			buttonAssister.setTextColor(getResources().getColor(R.color.tab_pressed));
			buttonShooter.setTextColor(getResources().getColor(R.color.tab_normal));
			tabShooter.setVisibility(View.INVISIBLE);
			tabAssister.setVisibility(View.VISIBLE);
			tabShooter.setBackgroundColor(getResources().getColor(R.color.tab_normal));
			tabAssister.setBackgroundColor(getResources().getColor(R.color.tab_pressed));
			ShooterAssister title=new ShooterAssister(false);
			mList.add(title);
			Cursor cursor=helper.select(tableName, new String[]{"name", "number", "assist", "attendance"}, null, null, "assist desc, attendance asc, goal desc");
			int index=1;
			while(cursor.moveToNext()){
				ShooterAssister item=new ShooterAssister(false, index+cursor.getString(0), cursor.getString(2), cursor.getString(3));
				mList.add(item);
				index=index+1;
			}
			
		}
	}
	
}
