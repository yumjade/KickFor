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
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SkillsShowFragment extends Fragment implements HomePageInterface, IdentificationInterface, HandlerListener {
	
	private ImageView back=null;
	private ImageView photo=null;
	private TextView myName=null;
	private TextView name=null;
	private ListView listView=null;
	private LinearLayout other=null;
	private RelativeLayout add=null;
	
	private String phone=null;

	private List<SkillsShowEntity> mList=null;
	private SkillsShowAdapter adapter=null;
	
	@Override
	public int getFragmentLevel() {
		return IdentificationInterface.SECOND_LEVEL;
	}
	
	private void init(){
		mList=new ArrayList<SkillsShowEntity>();
		Bundle bundle=getArguments();
		this.phone=bundle.getString("phone");
		Map<String, Object> tmp=new HashMap<String, Object>();
		tmp.put("request", "get userskills");
		tmp.put("phone", phone);
		Runnable r=new ClientWrite(Tools.JsonEncode(tmp));
		new Thread(r).start();
	}
	
	@Override
	public void setEnable(boolean enable) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		RealTimeHandler.getInstance().regist(this);
		init();
		View view = inflater.inflate(R.layout.skills_show, container, false);
		back = (ImageView) view.findViewById(R.id.skills_back);
		myName = (TextView) view.findViewById(R.id.tv_my_name);
		photo = (ImageView) view.findViewById(R.id.iv_photo);
		name = (TextView) view.findViewById(R.id.tv_name);
		listView = (ListView) view.findViewById(R.id.skills_list);
		other = (LinearLayout) view.findViewById(R.id.ll_other);
		add = (RelativeLayout) view.findViewById(R.id.skills_add);
		
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((HomePageActivity) getActivity()).onBackPressed();
			}
		});
			
		adapter = new SkillsShowAdapter(getActivity(), mList);
		listView.setAdapter(adapter);
		Tools.setListViewHeight(listView);
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				SkillsShowEntity item=mList.get(position);
				String skillkey=item.getSkillsKey();
				((HomePageActivity) getActivity()).openSkillsDetail(skillkey);	
			}
		});
				
		add.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				((HomePageActivity) getActivity()).openSelectSkills();
				
			}
		});
		
		return view;
	}

	@Override
	public void onChange(Message msg) {
		// TODO Auto-generated method stub
		if(msg.what==HomePageActivity.GET_USERSKILLS){
			Iterator<SkillsShowEntity> iter=((List<SkillsShowEntity>)msg.obj).iterator();
			while(iter.hasNext()){
				mList.add(iter.next());
			}
			if(adapter!=null){
				adapter.notifyDataSetChanged();
				Tools.setListViewHeight(listView);
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
