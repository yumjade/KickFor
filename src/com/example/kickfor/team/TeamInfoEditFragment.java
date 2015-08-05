package com.example.kickfor.team;

import java.util.HashMap;
import java.util.Map;

import com.example.kickfor.ClientWrite;
import com.example.kickfor.HomePageActivity;
import com.example.kickfor.R;
import com.example.kickfor.SQLHelper;
import com.example.kickfor.Tools;
import com.example.kickfor.utils.IdentificationInterface;
import com.example.kickfor.wheelview.ScreenInfo;
import com.example.kickfor.wheelview.WheelDate;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.TextView;

public class TeamInfoEditFragment extends Fragment implements TeamInterface, IdentificationInterface{
	
	private ImageView back=null;
	private ImageView photo=null;
	private EditText teamName=null;
	private TextView foundTime=null;
	private EditText city=null;
	private EditText district=null;
	private EditText number=null;
	private TextView confirmation=null;
	private WheelDate wheelDate=null;
	
	private String teamid=null;
	private Context context=null;
	private boolean isNewImage=false;
	private Bitmap bitmap=null;
	private String year=null;
	private String month=null;
	private String day=null;
	
	
	@Override
	public int getFragmentLevel() {
		// TODO Auto-generated method stub
		return IdentificationInterface.SECOND_LEVEL;
	}



	@Override
	public String getTeamid() {
		// TODO Auto-generated method stub
		return teamid;
	}


	private void init(){
		Bundle bundle=getArguments();
		this.teamid=bundle.getString("teamid");
		this.context=getActivity();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		init();
		View view = inflater.inflate(R.layout.fragment_teaminfo_edit, container, false);
		back = (ImageView) view.findViewById(R.id.info_edit_back);
		photo = (ImageView) view.findViewById(R.id.iv_info_team_photo);
		teamName = (EditText) view.findViewById(R.id.info_team_name);
		foundTime = (TextView) view.findViewById(R.id.info_team_time);
		city=(EditText)view.findViewById(R.id.info_team_city);
		district = (EditText) view.findViewById(R.id.info_team_district);
		number = (EditText) view.findViewById(R.id.info_team_number);
		confirmation = (TextView) view.findViewById(R.id.info_team_confirmation);
		
		back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				((HomePageActivity)getActivity()).onBackPressed();
			}
			
		});
		
		photo.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				((HomePageActivity)getActivity()).sendImage(4);
			}
			
		});
		
		foundTime.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				LayoutInflater mInflater=LayoutInflater.from(getActivity());
				final View datepickerview=mInflater.inflate(R.layout.datepicker, null);
				wheelDate=new WheelDate(datepickerview);
				ScreenInfo screenInfo=new ScreenInfo(getActivity());
				wheelDate.screenheight=screenInfo.getHeight();
				wheelDate.initDatePicker(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
				new AlertDialog.Builder(getActivity())
				.setTitle("选择日期")
				.setView(datepickerview)
				.setPositiveButton("确定",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								year=wheelDate.getYear();
								month=wheelDate.getMonth();
								day=wheelDate.getDay();
								foundTime.setText(wheelDate.getDate());
							}
						})
				.setNegativeButton("取消",
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog,
									int which) {
							}
						}).show();
			}
			
		});
		
		confirmation.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Map<String, Object> map=new HashMap<String, Object>();
				map.put("request", "change team info");
				map.put("teamid", teamid);
				map.put("name", teamName.getText().toString());
				map.put("city", city.getText().toString());
				map.put("district", district.getText().toString());
				map.put("number", number.getText().toString());
				map.put("year", year);
				map.put("month", month);
				map.put("day", day);
				if(isNewImage==true){
					map.put("image", Tools.bitmapToString(bitmap));
				}
				Runnable r=new ClientWrite(Tools.JsonEncode(map));
				new Thread(r).start();
			}
			
		});
		
		initiate();
		return view;
	}

	private void initiate(){
		SQLHelper helper=SQLHelper.getInstance(context);
		Cursor cursor=helper.select("teams", new String[]{"name", "year", "month", "day", "city", "district", "number", "image"}, "teamid=?", new String[]{teamid}, null);
		if(cursor.moveToNext()){
			teamName.setText(cursor.getString(0));
			year=cursor.getString(1);
			month=cursor.getString(2);
			day=cursor.getString(3);
			foundTime.setText(cursor.getString(1)+"-"+cursor.getString(2)+"-"+cursor.getString(3));
			city.setText(cursor.getString(4));
			district.setText(cursor.getString(5));
			number.setText(cursor.getString(6));
			String img=cursor.getString(7);
			if(img.equals("-1")){
				photo.setImageBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.team_default));
			}
			else{
				photo.setImageBitmap(BitmapFactory.decodeFile(img));
			}
		}
	}
	
	public void setImage(Bitmap bitmap){
		this.bitmap=bitmap;
		photo.setImageBitmap(bitmap);
		isNewImage=true;
	}
	
}
