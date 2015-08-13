package com.example.kickfor;



import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.kickfor.utils.IdentificationInterface;
import com.example.kickfor.wheelview.ScreenInfo;
import com.example.kickfor.wheelview.WheelDate;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class CompleteInfoFragment extends Fragment implements OnClickListener, HomePageInterface, IdentificationInterface{
	
	private String teamid1=null;
	private String teamid2=null;
	private String teamid3=null;
	
	private String name1=null;
	private String name2=null;
	private String name3=null;
	
	private String teamid=null;
	
	private List<String> items=null;
	private List<String> items1=null;
	
	private RelativeLayout photoButton=null;
	private TextView ensureButton=null;
	private ImageView photo=null;
	private EditText name=null;
	private TextView team1=null;
	private RadioGroup sex=null;
	private RadioButton male=null;
	private RadioButton female=null;
	private EditText weight=null;
	private EditText height=null;
	private TextView birth=null;
	private String year=null;
	private String month=null;
	private String day=null;
	private TextView number=null;
	private EditText city=null;
	private EditText district=null;
	private TextView position1=null;
	private TextView position2=null;
	private String bit=null;
	private WheelDate wheelDate=null;
	private ImageView back=null;
	
	private String phone=null;
	private String position1Text=null;
	private String position2Text=null;
	
	private Context context=null;
	
	private ScrollView completeInfo = null;
	
	
	@Override
	public int getFragmentLevel() {
		// TODO Auto-generated method stub
		return IdentificationInterface.SECOND_LEVEL;
	}



	public void setEnable(boolean enable){
		photoButton.setEnabled(enable);
		ensureButton.setEnabled(enable);
		name.setEnabled(enable);
		sex.setEnabled(enable);
		male.setEnabled(enable);
		female.setEnabled(enable);
		weight.setEnabled(enable);
		height.setEnabled(enable);
		birth.setEnabled(enable);
		city.setEnabled(enable);
		district.setEnabled(enable);
		position1.setEnabled(enable);
		position2.setEnabled(enable);
		back.setEnabled(enable);
	}
	
	

	private void init(){
		context=getActivity();
		Bundle bundle=getArguments();
		phone=bundle.getString("phone");
		items=new ArrayList<String>();
		items1=new ArrayList<String>();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		init();
		View view=inflater.inflate(R.layout.fragment_complete_info, container, false);
		back=(ImageView)view.findViewById(R.id.complete_back);
		back.setOnClickListener(this);
		name=(EditText)view.findViewById(R.id.et_name);
		team1=(TextView)view.findViewById(R.id.et_team1);
		height=(EditText)view.findViewById(R.id.et_height);
		weight=(EditText)view.findViewById(R.id.et_weight);
		number=(TextView)view.findViewById(R.id.et_number);
		city=(EditText)view.findViewById(R.id.et_city);
		district=(EditText)view.findViewById(R.id.et_district);
		birth=(TextView)view.findViewById(R.id.complete_birth);
		birth.setOnClickListener(this);
		sex=(RadioGroup)view.findViewById(R.id.rg_sex);
		photoButton=(RelativeLayout)view.findViewById(R.id.btn_photo);
		photoButton.setOnClickListener(this);
		ensureButton=(TextView)view.findViewById(R.id.btn_ensure);
		ensureButton.setOnClickListener(this);
		position1=(TextView)view.findViewById(R.id.tv_position1);
		position1.setOnClickListener(this);
		position2=(TextView)view.findViewById(R.id.tv_position2);
		position2.setOnClickListener(this);
		male=(RadioButton)view.findViewById(R.id.rb_male);
		female=(RadioButton)view.findViewById(R.id.rb_female);
		photo=(ImageView)view.findViewById(R.id.iv_photo1);
		completeInfo = (ScrollView) view.findViewById(R.id.sv_complete_info);
		completeInfo.setFocusableInTouchMode(true);
		
		initiate();
		initiateImage();
		
		sex.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				// TODO Auto-generated method stub
				if(checkedId==R.id.rb_male){
					bit="m";
				}
				else if(checkedId==R.id.rb_female){
					bit="f";
				}
			}
			
		});
		
		team1.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(items.size()>0 && items1.size()>0){
					AlertDialog.Builder builder = new AlertDialog.Builder(context);
					builder.setTitle("选择母队");
					ListAdapter catalogsAdapter = new ArrayAdapter<String>(context, R.layout.match_type_item, items);
					builder.setAdapter(catalogsAdapter, new DialogInterface.OnClickListener(){
					    public void onClick(DialogInterface arg0, int arg1) {
					        team1.setText(items.get(arg1));
					        teamid=items1.get(arg1);
					        PreferenceData pd=new PreferenceData(context);
					        String phone=pd.getData(new String[]{"phone"}).get("phone").toString();
					        SQLHelper helper=SQLHelper.getInstance(context);
					        Cursor cursor=helper.select("f_"+teamid, new String[]{"number"}, "phone=?", new String[]{phone}, null);
					        if(cursor.moveToNext()){
					        	number.setText(cursor.getString(0));
					        }
					    }
					});
					builder.show();
				}
				else{
					Toast.makeText(context, "您未加入或创建球队", Toast.LENGTH_SHORT).show();
				}
			}
			
		});
		
		return view;
	}
	
	public void changedData(){
		initiateImage();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		int id=v.getId();
		if(id==R.id.btn_ensure){
			saveExit();
		}
		else if(id==R.id.complete_back){
			((HomePageActivity)getActivity()).onBackPressed();
		}
		else if(id==R.id.complete_birth){
			LayoutInflater inflater=LayoutInflater.from(context);
			final View datepickerview=inflater.inflate(R.layout.datepicker, null);
			wheelDate=new WheelDate(datepickerview);
			ScreenInfo screenInfo=new ScreenInfo(getActivity());
			wheelDate.screenheight=screenInfo.getHeight();
			wheelDate.initDatePicker(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day));
			new AlertDialog.Builder(context)
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
							birth.setText(year+"  年  "+month+"  月  "+day+"  日  ");
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
		else if(id==R.id.tv_position1){
			setEnable(false);
			((HomePageActivity)getActivity()).getInPosition(position1Text, 1);
		}
		else if(id==R.id.tv_position2){
			setEnable(false);
			((HomePageActivity)getActivity()).getInPosition(position2Text, 2);
		}
		else if(id==R.id.btn_photo){
			completeInfo.clearFocus();
			completeInfo.setFocusable(false);
			((HomePageActivity)getActivity()).sendImage(1);
		}
		
	}
	
	public void saveExit(){
		setEnable(false);
		Map<String, Object> tmp=getData();
		Runnable r=new ClientWrite(Tools.JsonEncode(tmp));
		new Thread(r).start();
		tmp.remove("request");
		tmp.remove("phone");
		SQLHelper helper=SQLHelper.getInstance(context);
		helper.update(Tools.getContentValuesFromMap(tmp, null), "ich", "host");
		((HomePageActivity)getActivity()).openVague(HomePageActivity.WAIT_COMPLETE_INFO);
	}
	
	private Map<String, Object> getData(){
		Map<String, Object>map=new HashMap<String, Object>();
		map.put("request", "complete information");
		map.put("phone", new PreferenceData(context).getData(new String[]{"phone"}).get("phone").toString());
		map.put("name", name.getText());
		map.put("height", height.getText());
		map.put("weight", weight.getText());
		map.put("city", city.getText());
		if(teamid!=null){
			map.put("team1", teamid);
		}
		map.put("district", district.getText());
		map.put("year", year);
		map.put("month", month);
		map.put("day", day);
		map.put("sex", bit);
		return map;
	}
	
	private void initiate(){
		SQLHelper helper=SQLHelper.getInstance(context);
		String[] names=new String[]{"name", "team1", "sex", "weight", "height", "year", "month", "day", "number1", "city", "district", "position1", "position2", "team2", "team3"};
		Cursor cursor=helper.select("ich", names, "phone=?", new String[]{"host"}, null);
		if(cursor.moveToNext()){
			teamid1=cursor.getString(1);
			teamid2=cursor.getString(13);
			teamid3=cursor.getString(14);
			Cursor tcursor1=helper.select("teams", new String[]{"name"}, "teamid=?", new String[]{teamid1}, null);
			if(tcursor1.moveToNext()){
				name1=tcursor1.getString(0);
				items.add(name1);
				items1.add(teamid1);
			}
			Cursor tcursor2=helper.select("teams", new String[]{"name"}, "teamid=?", new String[]{teamid2}, null);
			if(tcursor2.moveToNext()){
				name2=tcursor2.getString(0);
				items.add(name2);
				items1.add(teamid2);
			}
			Cursor tcursor3=helper.select("teams", new String[]{"name"}, "teamid=?", new String[]{teamid3}, null);
			if(tcursor3.moveToNext()){
				name3=tcursor3.getString(0);
				items.add(name3);
				items1.add(teamid3);
			}
			name.setText(cursor.getString(0));
			String str=cursor.getString(1);
			Cursor cursor1=helper.select("teams", new String[]{"name"}, "teamid=?", new String[]{str}, null);
			if(cursor1.moveToNext()){
				str=cursor1.getString(0);
			}
			team1.setText(str.isEmpty()? "选择母队": str);
			bit=cursor.getString(2);
			if(bit.equals("m")){
				female.setChecked(false);
				male.setChecked(true);
			}
			else if(bit.equals("f")){
				male.setChecked(false);
				female.setChecked(true);
			}
			weight.setText(cursor.getString(3));
			height.setText(cursor.getString(4));
			year=cursor.getString(5);
			month=cursor.getString(6);
			day=cursor.getString(7);
			if(year.isEmpty() || month.isEmpty() || day.isEmpty()){
				year=String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
				month=String.valueOf(Calendar.getInstance().get(Calendar.MONTH));
				day=String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
			}
			birth.setText(year+"  年  "+month+"  月  "+day+"  日  ");
			PreferenceData pd=new PreferenceData(context);
	        String phone=pd.getData(new String[]{"phone"}).get("phone").toString();
	        number.setText("");
	        if(!teamid1.isEmpty()){
	        	Cursor cursor2=helper.select("f_"+teamid1, new String[]{"number"}, "phone=?", new String[]{phone}, null);
		        if(cursor2.moveToNext()){
		        	number.setText(cursor2.getString(0));
		        }
	        }
			city.setText(cursor.getString(9));
			district.setText(cursor.getString(10));
			position1Text="擅长踢："+cursor.getString(11);
			position1.setText(position1Text);
			position2Text="勉强踢："+cursor.getString(12);
			position2.setText(position2Text);
		}	
	}
	
	private void initiateImage(){
		SQLHelper helper=SQLHelper.getInstance(context);
		Cursor cursor=helper.select("ich", new String[]{"image"}, "phone=?", new String[]{"host"}, null);
		if(cursor.moveToNext()){
			String img=cursor.getString(0);
			if(!img.equals("-1")){
				photo.setImageBitmap(BitmapFactory.decodeFile(cursor.getString(0)));
				
			}
		}
	}
	

	
	

}
