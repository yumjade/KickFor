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
	private ImageView photo1=null;
	private ImageView photo2=null;
	private ImageView photo3=null;
	private TextView myName=null;
	private TextView name1=null;
	private TextView name2=null;
	private TextView name3=null;
	private ListView listView=null;
	private RelativeLayout add=null;
	private TextView title=null;
	
	private String phone=null;
	private String host=null;
	private String name=null;

	private List<SkillsShowEntity> mList=null;
	private SkillsShowAdapter adapter=null;
	private SkillsShowOthersAdapter adapterOther=null;
	
	@Override
	public int getFragmentLevel() {
		return IdentificationInterface.SECOND_LEVEL;
	}
	
	private void init(){
		host=new PreferenceData(getActivity()).getData(new String[]{"phone"}).get("phone").toString();
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
		title=(TextView)view.findViewById(R.id.skills_show_text);
		back = (ImageView) view.findViewById(R.id.skills_show_back);
		myName = (TextView) view.findViewById(R.id.skills_show_myname);
		photo1 = (ImageView) view.findViewById(R.id.skills_show_photo1);
		photo2 = (ImageView) view.findViewById(R.id.skills_show_photo2);
		photo3 = (ImageView) view.findViewById(R.id.skills_show_photo3);
		name1 = (TextView) view.findViewById(R.id.skills_show_name1);
		name2 = (TextView) view.findViewById(R.id.skills_show_name2);
		name3 = (TextView) view.findViewById(R.id.skills_show_name3);
		listView = (ListView) view.findViewById(R.id.skills_list);
		add = (RelativeLayout) view.findViewById(R.id.skills_add);
		
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((HomePageActivity) getActivity()).onBackPressed();
			}
		});
			
		if(phone.equals(host)){
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
			add.setVisibility(View.VISIBLE);
		}
		else{
			add.setVisibility(View.GONE);
			adapterOther=new SkillsShowOthersAdapter(getActivity(), mList, host, phone);
			listView.setAdapter(adapterOther);
			listView.setDividerHeight(0);
			Tools.setListViewHeight(listView);
		}
		return view;
	}

	@Override
	public void onChange(Message msg) {
		// TODO Auto-generated method stub
		if(msg.what==HomePageActivity.GET_USERSKILLS){
			mList.clear();
			@SuppressWarnings("unchecked")
			Iterator<SkillsShowEntity> iter=((List<SkillsShowEntity>)msg.obj).iterator();
			while(iter.hasNext()){
				mList.add(iter.next());
			}
			if(host.equals(phone)){
				if(adapter!=null){
					adapter.notifyDataSetChanged();
					Tools.setListViewHeight(listView);
				}
			}
			else{
				if(adapterOther!=null){
					adapterOther.notifyDataSetChanged();
					Tools.setListViewHeight(listView);
				}
			}
			
			Bundle bundle=msg.getData();
			
			if(bundle.containsKey("name")){
				title.setText(host.equals(phone)? "我的技能风格": bundle.getString("name")+"的技能风格");
				myName.setText("谁认可了"+bundle.getString("name")+"的技能");
			}
			
			if(bundle.containsKey("image1") && photo1!=null){
				photo1.setVisibility(View.VISIBLE);
				name1.setVisibility(View.VISIBLE);
				photo1.setImageBitmap(bundle.getString("image1").equals("-1")? BitmapFactory.decodeResource(getResources(), R.drawable.team_default): Tools.stringtoBitmap(bundle.getString("image1")));
				name1.setText(bundle.getString("name1"));
			}
			else{
				photo1.setVisibility(View.GONE);
				name1.setVisibility(View.GONE);
			}
			
			if(bundle.containsKey("image2") && photo2!=null){
				photo2.setVisibility(View.VISIBLE);
				name2.setVisibility(View.VISIBLE);
				photo2.setImageBitmap(bundle.getString("image2").equals("-1")? BitmapFactory.decodeResource(getResources(), R.drawable.team_default): Tools.stringtoBitmap(bundle.getString("image2")));
				name2.setText(bundle.getString("name2"));
			}
			else{
				photo2.setVisibility(View.GONE);
				name2.setVisibility(View.GONE);
			}
			
			if(bundle.containsKey("image3") && photo3!=null){
				photo3.setVisibility(View.VISIBLE);
				name3.setVisibility(View.VISIBLE);
				photo3.setImageBitmap(bundle.getString("image3").equals("-1")? BitmapFactory.decodeResource(getResources(), R.drawable.team_default): Tools.stringtoBitmap(bundle.getString("image3")));
				name3.setText(bundle.getString("name3"));
			}
			else{
				photo3.setVisibility(View.GONE);
				name3.setVisibility(View.GONE);
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
