package com.example.kickfor.team;

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
import com.example.kickfor.wheelview.ScreenInfo;
import com.example.kickfor.wheelview.WheelDate;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class HallOfFameFragment extends Fragment implements TeamInterface, IdentificationInterface{

	public static final boolean FIRST_OPEN=true;
	public static final boolean SECOND_OPEN=false;
	
	private TextView hallOfFameStart = null;
	private ImageView fameBack = null;
	private Context context = null;
	private int authority = 0;
	private String teamid = null;
	private int resource;
	
	private TextView delete=null;
	private Bitmap image=null;
	private TextView confirmation = null;
	private TextView cancel = null;
	private ImageView famePhoto = null;
	private EditText editName = null;
	private EditText editNumber=null;
	private TextView editPosition=null;
	private TextView editTime=null;
	private EditText editText=null;
	private WheelDate wheelDate=null;
	private WheelDate wheelDate1=null;
	private int year=0;
	private int month=0;
	private int day=0;
	private int id=-1;
	
	private TextView addNew = null;
	

	private ListView fameList = null;
	
	private HallOfFmeAdapter adapter=null;
	private HallofFame entity = null;
	private List<HallofFame> mList=new ArrayList<HallofFame>();
	private TextView editTimeEnd=null;
	
	public int getResourceId() {
		return resource;
	}

	private void init() {
		context=getActivity();
		Bundle bundle = getArguments();
		this.resource = bundle.getInt("resource");
		this.authority=Integer.parseInt(bundle.getString("authority"));
		this.teamid=bundle.getString("teamid");
		if(bundle.containsKey("map")){
			setData(bundle.getBundle("map"));
		}
		if(bundle.containsKey("entity")){
			this.entity=(HallofFame)bundle.getSerializable("entity");
		}
		
	}
	
	@Override
	public void setEnable(boolean enable) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		init();
		View view = inflater.inflate(resource, container, false);
		switch (resource) {
		case R.layout.hall_of_fame: {
			hallOfFameStart = (TextView) view.findViewById(R.id.tv_fame_start);
			hallOfFameStart.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(authority>=2){
						((HomePageActivity)getActivity()).openFame(SECOND_OPEN, teamid, String.valueOf(authority), null);
					}
					else{
						Toast.makeText(context, "您的权限不够", Toast.LENGTH_SHORT).show();
					}
				}
				
			});

			fameBack = (ImageView) view.findViewById(R.id.fame_back);
			fameBack.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					((HomePageActivity) getActivity()).onBackPressed();
				}

			});
			break;
		}
		case R.layout.add_hall_of_fame:{
			editName=(EditText)view.findViewById(R.id.et_fame_name);
			editPosition=(TextView)view.findViewById(R.id.et_fame_position);
			editNumber=(EditText)view.findViewById(R.id.et_fame_number);
			editTime=(TextView)view.findViewById(R.id.et_fame_time);
			editTimeEnd=(TextView)view.findViewById(R.id.et_fame_time_end);
			famePhoto=(ImageView)view.findViewById(R.id.fame_add_photo);
			editText=(EditText)view.findViewById(R.id.et_fame_text);
			delete=(TextView)view.findViewById(R.id.hall_delete);
			
			
			famePhoto.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					((HomePageActivity)getActivity()).sendImage(3);
				}
				
			});
			
			editPosition.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					((HomePageActivity)getActivity()).getInPosition(editPosition.getText().toString(), 0);
				}
				
			});
			
			editTime.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					LayoutInflater inflater=LayoutInflater.from(context);
					final View datepickerview=inflater.inflate(R.layout.datepicker, null);
					wheelDate=new WheelDate(datepickerview);
					ScreenInfo screenInfo=new ScreenInfo(getActivity());
					wheelDate.screenheight=screenInfo.getHeight();
					wheelDate.initDatePicker(year, month, day);
					new AlertDialog.Builder(context)
					.setTitle("选择开始效力时间")
					.setView(datepickerview)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									editTime.setText(wheelDate.getDate1());
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
			
			editTimeEnd.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					LayoutInflater inflater=LayoutInflater.from(context);
					final View datepickerview=inflater.inflate(R.layout.datepicker, null);
					wheelDate1=new WheelDate(datepickerview);
					ScreenInfo screenInfo=new ScreenInfo(getActivity());
					wheelDate1.screenheight=screenInfo.getHeight();
					wheelDate1.initDatePicker(year, month, day);
					new AlertDialog.Builder(context)
					.setTitle("选择结束效力时间")
					.setView(datepickerview)
					.setPositiveButton("确定",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									editTimeEnd.setText(wheelDate1.getDate1());
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
			
			fameBack=(ImageView)view.findViewById(R.id.fame_back);
			fameBack.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					fameBack.setEnabled(false);
					((HomePageActivity)getActivity()).onBackPressed();
				}
				
			});
			confirmation = (TextView)view.findViewById(R.id.hall_confirmation);
			confirmation.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Map<String, Object> map=new HashMap<String, Object>();
					if(editName.getText().toString().isEmpty()){
						Toast.makeText(context, "姓名不能为空", Toast.LENGTH_SHORT).show();
					}
					else if(editNumber.getText().toString().isEmpty()){
						Toast.makeText(context, "号码不能为空", Toast.LENGTH_SHORT).show();
					}
					else if(editPosition.getText().toString().isEmpty()){
						Toast.makeText(context, "位置不能为空", Toast.LENGTH_SHORT).show();
					}
					else if(editTime.getText().toString().isEmpty() || editTimeEnd.getText().toString().isEmpty()){
						Toast.makeText(context, "效力时间不能为空", Toast.LENGTH_SHORT).show();
					}
					else if(editText.getText().toString().isEmpty()){
						Toast.makeText(context, "荣光简介不能为空", Toast.LENGTH_SHORT).show();
					}
					else if(image==null){
						Toast.makeText(context, "照片不能为空", Toast.LENGTH_SHORT).show();
					}
					else{
						setEnable(false);
						((HomePageActivity)getActivity()).openVague(HomePageActivity.WAIT_EDIT_FAME);
						map.put("request", "update fame");
						map.put("name", editName.getText().toString());
						map.put("position", editPosition.getText().toString());
						map.put("number", editNumber.getText().toString());
						map.put("date", editTime.getText().toString()+" - "+editTimeEnd.getText().toString());
						if(image==null){
							map.put("image", "-1");
						}
						else{
							map.put("image", Tools.bitmapToString(image));
						}
						map.put("text", editText.getText().toString());
						map.put("teamid", teamid);
						map.put("id", String.valueOf(id));
						Runnable r=new ClientWrite(Tools.JsonEncode(map));
						new Thread(r).start();
					}
				}
				
			});
			cancel = (TextView)view.findViewById(R.id.hall_cancel);		
			cancel.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					((HomePageActivity)getActivity()).onBackPressed();
				}
				
			});
			initiate();
			if(id==-1){
				delete.setVisibility(View.GONE);
			}
			else{
				delete.setVisibility(View.VISIBLE);
				delete.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View arg0) {
						// TODO Auto-generated method stub
						Map<String, Object> map=new HashMap<String, Object>();
						map.put("request", "delete fame");
						map.put("teamid", teamid);
						map.put("id", String.valueOf(id));
						Runnable r=new ClientWrite(Tools.JsonEncode(map));
						new Thread(r).start();
					}
					
				});
			}
			break;
		}
		case R.layout.hall_of_fame_list:{
			fameBack=(ImageView)view.findViewById(R.id.fame_back);
			addNew=(TextView)view.findViewById(R.id.fame_add_new);
			if(authority>=2){
				addNew.setVisibility(View.VISIBLE);
				addNew.setOnClickListener(new OnClickListener(){

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						if(mList.size()<3){
							((HomePageActivity)getActivity()).openFame(SECOND_OPEN, teamid, String.valueOf(authority), null);
						}
						else{
							Toast.makeText(context, "名人堂只有三个席位", Toast.LENGTH_SHORT).show();
						}
					}
					
				});
			}
			else{
				addNew.setVisibility(View.GONE);
			}
			fameBack.setOnClickListener(new OnClickListener(){

				@Override
				public void onClick(View v) {
					fameBack.setEnabled(false);
					((HomePageActivity)getActivity()).onBackPressed();
				}
				
			});
			adapter = new HallOfFmeAdapter(context, mList, teamid, authority);
			fameList = (ListView) view.findViewById(R.id.hall_of_fame_list);
			
			fameList.setAdapter(adapter);
			fameList.setItemsCanFocus(false); 
		}
		}
		return view;
	}
	

	public void setEnable(int resource, boolean enable) {
		switch (resource) {
		case R.layout.hall_of_fame: {
			hallOfFameStart.setEnabled(enable);
			break;
		}
		case R.layout.add_hall_of_fame: {
			confirmation.setEnabled(enable);
			cancel.setEnabled(enable);
			break;
		}
		case R.layout.hall_of_fame_list: {
			addNew.setEnabled(enable);
			break;
		}
		}
	}

	private void initiate(){
		if(entity!=null){
			image=entity.getPhoto();
			famePhoto.setImageBitmap(image);
			editName.setText(entity.getName());
			editNumber.setText(entity.getNumber());
			editPosition.setText(entity.getPosition());
			List<String> list=explode(entity.getDate()+" - ", " - ");
			editTime.setText(list.get(0));
			editTimeEnd.setText(list.get(1));
			editText.setText(entity.getIntruduction());
			id=entity.getId();
		}
	}
	
	private void setData(Bundle map){
		mList.clear();
		Map<String, Object> temp=new HashMap<String, Object>();
		Iterator<String> iter=map.keySet().iterator();
		while(iter.hasNext()){
			temp.clear();
			String key=iter.next();
			temp=Tools.getMapForJson(map.get(key).toString());
			String img=temp.get("image").toString();
			if(img.equals("-1")){
				img=Tools.bitmapToString(BitmapFactory.decodeResource(getResources(), R.drawable.team_default));
			}
			HallofFame item=new HallofFame(Integer.parseInt(temp.get("id").toString()), temp.get("name").toString(), temp.get("number").toString(), temp.get("position").toString(), img, temp.get("date").toString(), temp.get("text").toString());
			mList.add(item);
		}
	}
	
	public void setImage(Bitmap bitmap){
		this.image=bitmap;
		famePhoto.setImageBitmap(bitmap);
	}
	
	@Override
	public String getTeamid() {
		// TODO Auto-generated method stub
		return teamid;
	}

	@Override
	public int getFragmentLevel() {
		// TODO Auto-generated method stub
		return IdentificationInterface.SECOND_LEVEL;
	}
	
	public void setPosition(String position){
		editPosition.setText(position);
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
