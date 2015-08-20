package com.example.kickfor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.example.kickfor.utils.IdentificationInterface;

import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class SkillsDetailFragment extends Fragment implements HomePageInterface, IdentificationInterface, HandlerListener{
	
	private ImageView back=null;
	private ImageView delete=null;
	private TextView text=null;
	private TextView skillName=null;
	private ListView listView=null;
	private TextView agreeNum=null;
	private List<SkillDetailEntity> mList=null;
	private SkillsDetailAdapter adapter;
	private TextView skillIntroduce=null;
	
	private String phone=null;
	private String name=null;
	private String skillskey=null;
	private String introduce=null;
	private String skillname=null;

	private void init(){
		Bundle bundle=getArguments();
		this.phone=bundle.getString("phone");
		this.skillskey=bundle.getString("skillskey");
		mList=new ArrayList<SkillDetailEntity>();
		Map<String, Object> tmp=new HashMap<String, Object>();
		tmp.put("request", "get skillsdetail");
		tmp.put("skillkey", skillskey);
		tmp.put("phone", phone);
		Runnable r=new ClientWrite(Tools.JsonEncode(tmp));
		new Thread(r).start();
	}
	
	@Override
	public void setEnable(boolean enable) {
		// TODO Auto-generated method stub
		
	}

	
	@Override
	public int getFragmentLevel() {
		return IdentificationInterface.SECOND_LEVEL;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		RealTimeHandler.getInstance().regist(this);
		init();
		View view = inflater.inflate(R.layout.skills_detail, container, false);
		agreeNum=(TextView)view.findViewById(R.id.skills_detail_count);
		back = (ImageView) view.findViewById(R.id.skills_detail_back);
		text=(TextView)view.findViewById(R.id.skills_detail_text);
		skillIntroduce=(TextView)view.findViewById(R.id.skills_detail_description);
		delete = (ImageView) view.findViewById(R.id.skills_detail_delete);
		skillName = (TextView) view.findViewById(R.id.skills_detail_name);
		listView = (ListView) view.findViewById(R.id.skills_detail_list);
			
		delete.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Map<String, Object> tmp=new HashMap<String, Object>();
				tmp.put("request", "del skills");
				tmp.put("phone", phone);
				tmp.put("skillkey", skillskey);
				Runnable r=new ClientWrite(Tools.JsonEncode(tmp));
				new Thread(r).start();
			}
			
		});	
			
		adapter = new SkillsDetailAdapter(getActivity(), mList);
		listView.setAdapter(adapter);
			
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				((HomePageActivity) getActivity()).onBackPressed();
			}
		});	
		return view;
	}

	@Override
	public void onChange(Message msg) {
		// TODO Auto-generated method stub
		if(msg.what==HomePageActivity.GET_SKILLS_DETAIL){
			Bundle bundle=msg.getData();
			introduce=bundle.getString("introduce");
			skillname=bundle.getString("skillname");
			name=bundle.getString("name");
			@SuppressWarnings("unchecked")
			List<SkillDetailEntity> tmp=(List<SkillDetailEntity>)msg.obj;
			Iterator<SkillDetailEntity> iter=tmp.iterator();
			while(iter.hasNext()){
				mList.add(iter.next());
			}
			if(adapter!=null){
				adapter.notifyDataSetChanged();
				skillName.setText(skillname);
				skillIntroduce.setText(introduce);
				agreeNum.setText(""+tmp.size());
				text.setText("谁认可了"+name+"的足球技能");
			}
		}
		else if(msg.what==HomePageActivity.OK_DELSKILLS){
			Bundle bundle=msg.getData();
			if(phone.equals(bundle.getString("phone")) && skillskey.equals(bundle.getString("skillkey"))){
				((HomePageActivity)getActivity()).onBackPressed();
			}
		}
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		RealTimeHandler.getInstance().unRegist(this);
		super.onDestroy();
	}
	

	
	
	
}
