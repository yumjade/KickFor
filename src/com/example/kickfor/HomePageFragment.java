package com.example.kickfor;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.example.kickfor.utils.IdentificationInterface;
import com.example.kickfor.utils.SexangleView3;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class HomePageFragment extends Fragment implements OnClickListener, HomePageInterface, IdentificationInterface, HandlerListener{

	private HomePageEntity entity=null;
	private String phone=null;
	private boolean isFriend=true;
	
	private ImageView imagePhoto=null;
	private RelativeLayout capacityButton=null;
	private RelativeLayout infoButton1=null;
	private RelativeLayout infoButton2=null;
	private TextView infoButton3=null;
	
	private LinearLayout fileLayout=null;
	private LinearLayout skillsLayout=null;
	private RelativeLayout noFile;
	private RelativeLayout noSkills;
	
	private TextView skillsText=null;
	
	private TextView name=null;
	private TextView valuePower=null;
	private TextView valueSpeed=null;
	private TextView valueSkills=null;
	private TextView valueStamina=null;
	private TextView valueAttack=null;
	private TextView valueDefence=null;
	private TextView birthAndPlace=null;
	private TextView weightAndHeight=null;
	private TextView teamAndPosition=null;
	private TextView matchNumber=null;
	private TextView winRate=null;
	private TextView totalGoal=null;
	private TextView totalAssist=null;
	private RelativeLayout signin=null;
	private ProgressBar pb=null;
	private ImageView iv=null;
	private TextView gradeText=null;
	private ProgressBar pbGrade=null;
	private TextView addupText=null;
	private TextView text=null;
	private TextView zhugong=null;
	
	private RelativeLayout file=null;
	private TextView fileLeft=null;
	
	private ProgressBar power=null;
	private ProgressBar speed=null;
	private ProgressBar skills=null;
	private ProgressBar stamina=null;
	private ProgressBar attack=null;
	private ProgressBar defence=null;
	private RelativeLayout skillsBoard=null;
	
	private ListView mListView=null;
	private List<OthersMatchEntity> mList=null;
	private RelativeLayout info3Detail=null;
	private TextView info3_team=null;
	private TextView info3_time=null;
	private TextView info3_place=null;
	private TextView info3_person=null;
	private TextView info3_type=null;
	private TextView button1=null;
	private TextView button2=null;
	private boolean isListVisible=false;
	private boolean isInfo3Visible=false;
	private ImageView titleBack=null;
	private TextView title=null;
	private OthersMatchAdapter adapter=null;
	
	private boolean isSign=false;
	
	private ProgressDialog progressDialog;
	private int score = 0;
	private int max=0;
	private String strNext=null;
	
	private SexangleView3 sexangle;

	@Override
	public void onChange(Message msg) {
		if(msg.what==HomePageActivity.GET_ARCHIVES){
			if(name!=null){
				updateView();
			}
		}
		else if(msg.what==HomePageActivity.GET_USERSKILLS){
			if(name!=null){
				updateView();
			}
		}
	}

	@Override
	public int getFragmentLevel() {
		if(phone.equals("host")){
			return IdentificationInterface.MAIN_LEVEL;
		}
		else{
			return IdentificationInterface.SECOND_LEVEL;
		}
	}
	
	public String getPhone(){
		return phone;
	}
	
	public void updateView(){
		if(name!=null){
			if(phone.equals("host")){
				entity=new HomePageEntity(getActivity(), "host");
				initiateMine();
				initiateFile();
				initiateSkills();
			}
		}
	}

	public void setEnable(boolean enable){
		if(phone.equals("host")){
			if(capacityButton!=null){
				capacityButton.setEnabled(enable);
				infoButton1.setEnabled(enable);
				infoButton2.setEnabled(enable);
				infoButton3.setEnabled(enable);
				file.setEnabled(enable);
			}
		}
		else{
			if(titleBack!=null){
				titleBack.setEnabled(enable);
				infoButton2.setEnabled(enable);
				infoButton3.setEnabled(enable);
				button1.setEnabled(enable);
				button2.setEnabled(enable);
			}
		}
		
	}
	
	private void init(){
		Bundle bundle=getArguments();
		if(bundle.containsKey("entity")){
			entity=(HomePageEntity)bundle.getSerializable("entity");
			phone=entity.getPhone();
			mList=new ArrayList<OthersMatchEntity>();
			isFriend=bundle.getBoolean("isFriend");
		}
		else{
			phone=bundle.getString("phone");
			entity=new HomePageEntity(getActivity(), this.phone);
			if(!phone.equals("host")){
				mList=new ArrayList<OthersMatchEntity>();
				isFriend=true;
			}
		}
		
	}

	private void initiateFile(){
		fileLayout.removeAllViews();
		LayoutInflater inflater=LayoutInflater.from(getActivity());
		List<FileEntity> list=entity.getFileList();
		if (list.size() == 0) {
			noFile.setVisibility(View.VISIBLE);
		}else {
			noFile.setVisibility(View.INVISIBLE);
		}
		if(entity.getLeftNum()==0){
			fileLeft.setText("赶紧点击添加档案吧");
		}
		else{
			fileLeft.setText("还有"+entity.getLeftNum()+"段经历");
		}
		Iterator<FileEntity> iter=list.iterator();
		int i=0;
		while(iter.hasNext() && i<2){
			FileEntity item=iter.next();
			View v=inflater.inflate(R.layout.file_homepage_item, null);
			TextView position=(TextView)v.findViewById(R.id.file_position);
			TextView name=(TextView)v.findViewById(R.id.file_team_name);
			TextView date=(TextView)v.findViewById(R.id.file_date);
			position.setText(item.getPosition());
			name.setText(item.getTeamName());
			date.setText(item.getJoinDate()+" - "+item.getExitDate());
			fileLayout.addView(v);
			i++;
		}
	}
	
	private void initiateSkills(){
		skillsLayout.removeAllViews();
		LayoutInflater inflater=LayoutInflater.from(getActivity());
		List<SkillsShowEntity> list=entity.getSkillsList();
		if (list.size() == 0) {
			noSkills.setVisibility(View.VISIBLE);
		}else {
			noSkills.setVisibility(View.INVISIBLE);
		}
		if(entity.getLeftSkillsNum()==0){
			skillsText.setText("赶紧点击添加技能吧");
		}
		else{
			skillsText.setText("还有"+entity.getLeftSkillsNum()+"个技能");
		}
		Iterator<SkillsShowEntity> iter=list.iterator();
		while(iter.hasNext()){
			SkillsShowEntity item=iter.next();
			View v=inflater.inflate(R.layout.skills_show_item, null);
			View line=(View)v.findViewById(R.id.skills_show_line);
			line.setVisibility(View.VISIBLE);
			TextView name=(TextView)v.findViewById(R.id.skills_show_name);
			name.setCompoundDrawables(null, null, null, null);
			TextView number=(TextView)v.findViewById(R.id.skills_show_count);
			name.setText(item.getSkillsName());
			name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
			name.setGravity(Gravity.CENTER);
			number.setText(item.getAgreeNum());
			skillsLayout.addView(v);
		}
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		init();
		View view=null;
		if(this.phone.equals("host")){
			view=inflater.inflate(R.layout.fragment_my_homepage, container, false);
			skillsBoard=(RelativeLayout)view.findViewById(R.id.rl_skills);
			skillsBoard.setOnClickListener(this);
			fileLeft=(TextView)view.findViewById(R.id.homepage_left);
			fileLayout=(LinearLayout)view.findViewById(R.id.homepage_file);
			noFile = (RelativeLayout) view.findViewById(R.id.rl_no_file);
			skillsLayout=(LinearLayout)view.findViewById(R.id.homepage_skills);
			skillsText=(TextView)view.findViewById(R.id.homepage_skills_text);
			noSkills = (RelativeLayout) view.findViewById(R.id.rl_no_skills);
			file=(RelativeLayout)view.findViewById(R.id.rl_file);
			file.setOnClickListener(this);
			name=(TextView)view.findViewById(R.id.tv_my_name);
			gradeText=(TextView)view.findViewById(R.id.tv_grade);
			pbGrade=(ProgressBar)view.findViewById(R.id.progress_grade);
			addupText=(TextView)view.findViewById(R.id.homepage_sign_text);
			
			valuePower=(TextView)view.findViewById(R.id.show_capacity_value4);
			valueSpeed=(TextView)view.findViewById(R.id.show_capacity_value5);
			valueSkills=(TextView)view.findViewById(R.id.show_capacity_value3);
			valueStamina=(TextView)view.findViewById(R.id.show_capacity_value6);
			valueAttack=(TextView)view.findViewById(R.id.show_capacity_value1);
			valueDefence=(TextView)view.findViewById(R.id.show_capacity_value2);
			
			sexangle = (SexangleView3) view.findViewById(R.id.iv_share_capacity_new);
			
			birthAndPlace=(TextView)view.findViewById(R.id.tv_info1);
			weightAndHeight=(TextView)view.findViewById(R.id.tv_info2);
			teamAndPosition=(TextView)view.findViewById(R.id.tv_info3);
			matchNumber=(TextView)view.findViewById(R.id.tv_match_number);
			winRate=(TextView)view.findViewById(R.id.tv_win_rate);
			totalGoal=(TextView)view.findViewById(R.id.tv_goal);
			totalAssist=(TextView)view.findViewById(R.id.tv_assist);
			imagePhoto=(ImageView)view.findViewById(R.id.iv_photo);
			capacityButton=(RelativeLayout)view.findViewById(R.id.btn_mycapacity);
			infoButton1=(RelativeLayout)view.findViewById(R.id.btn_info1);
			infoButton2=(RelativeLayout)view.findViewById(R.id.btn_info2);
			infoButton3=(TextView)view.findViewById(R.id.btn_info3);
			signin=(RelativeLayout)view.findViewById(R.id.homepage_signin);
			pb=(ProgressBar)view.findViewById(R.id.homepage_wait);
			iv=(ImageView)view.findViewById(R.id.homepage_sign_iv);
			text=(TextView)view.findViewById(R.id.homepage_addup);
			signin.setOnClickListener(this);
			capacityButton.setOnClickListener(this);
			infoButton1.setOnClickListener(this);
			infoButton2.setOnClickListener(this);
			infoButton3.setOnClickListener(this);
			initiateMine();
			initiateFile();
			initiateSkills();
			initGrade(entity.isSignedToday(), Integer.parseInt(entity.getAddUp()), Integer.parseInt(entity.getScore()));
			
			pbGrade.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					progressDialog = new ProgressDialog(getActivity());
					progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
					progressDialog.setTitle("经验值");
					progressDialog.setIcon(R.drawable.signin);
					progressDialog.setCancelable(true);
					progressDialog.setIndeterminate(false);
					progressDialog.setMax(max);
					progressDialog.setMessage("下一等级:"+strNext);
					progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "我知道了",    
					            new DialogInterface.OnClickListener() {  
					               public void onClick(DialogInterface dialog,  
					               int whichButton){  
					               }  
					          });  
					progressDialog.show();
					progressDialog.setProgress(score);
				}
			});
			
		}
		else{
			view=inflater.inflate(R.layout.fragment_other_homepage, container, false);
			
			skillsBoard=(RelativeLayout)view.findViewById(R.id.rl_other_skills);
			skillsBoard.setOnClickListener(this);
			fileLeft=(TextView)view.findViewById(R.id.homepage_other_left);
			fileLayout=(LinearLayout)view.findViewById(R.id.homepage_other_file);
			noFile = (RelativeLayout) view.findViewById(R.id.rl_other_no_file);
			skillsLayout=(LinearLayout)view.findViewById(R.id.homepage_other_skills);
			skillsText=(TextView)view.findViewById(R.id.homepage_other_skills_text);
			noSkills = (RelativeLayout) view.findViewById(R.id.rl_other_no_skills);
			file=(RelativeLayout)view.findViewById(R.id.rl_other_file);
			file.setOnClickListener(this);
			
			imagePhoto=(ImageView)view.findViewById(R.id.iv_other_photo);
			name=(TextView)view.findViewById(R.id.tv_other_name);
			title=(TextView)view.findViewById(R.id.other_homepage_text);
			titleBack=(ImageView)view.findViewById(R.id.other_homepage_back);
			zhugong=(TextView)view.findViewById(R.id.other_zhugong);
			
			valuePower=(TextView)view.findViewById(R.id.show_capacity_value4);
			valueSpeed=(TextView)view.findViewById(R.id.show_capacity_value5);
			valueSkills=(TextView)view.findViewById(R.id.show_capacity_value3);
			valueStamina=(TextView)view.findViewById(R.id.show_capacity_value6);
			valueAttack=(TextView)view.findViewById(R.id.show_capacity_value1);
			valueDefence=(TextView)view.findViewById(R.id.show_capacity_value2);
			
			sexangle = (SexangleView3) view.findViewById(R.id.iv_share_capacity_new);
			
			birthAndPlace=(TextView)view.findViewById(R.id.tv_other_info1);
			weightAndHeight=(TextView)view.findViewById(R.id.tv_other_info2);
			teamAndPosition=(TextView)view.findViewById(R.id.tv_other_info3);
			infoButton2=(RelativeLayout)view.findViewById(R.id.btn_other_info2);
			matchNumber=(TextView)view.findViewById(R.id.tv_other_match_number);
			winRate=(TextView)view.findViewById(R.id.tv_other_win_rate);
			totalGoal=(TextView)view.findViewById(R.id.tv_other_goal);
			totalAssist=(TextView)view.findViewById(R.id.tv_other_assist);
			mListView=(ListView)view.findViewById(R.id.other_kickfor_life);
			infoButton3=(TextView)view.findViewById(R.id.other_btn_info3);
			info3Detail=(RelativeLayout)view.findViewById(R.id.other_btn_info3_detail);
			info3_team=(TextView)view.findViewById(R.id.other_team);
			info3_time=(TextView)view.findViewById(R.id.other_date_time);
			info3_place=(TextView)view.findViewById(R.id.other_place);
			info3_person=(TextView)view.findViewById(R.id.other_number);
			info3_type=(TextView)view.findViewById(R.id.other_type);
			button1=(TextView)view.findViewById(R.id.other_chat);
			button2=(TextView)view.findViewById(R.id.other_evaluate);
			gradeText=(TextView)view.findViewById(R.id.tv_other_grade);
			initiateOthers();
			initiateFile();
			initiateSkills();
		}
		RealTimeHandler.getInstance().regist(this);
		return view;
	}
	
	
	private void initiateMine(){
		
		String fontPath3="fonts/PGDQH.ttf";
		Typeface other_tf=Typeface.createFromAsset(getActivity().getAssets(), fontPath3);
		
		imagePhoto.setImageBitmap(entity.getImage());
		name.setText(entity.getName());

		valueAttack.setText("" + Integer.parseInt(entity.getAttack()));
		valueAttack.setTypeface(other_tf);
		valueSpeed.setText("" + Integer.parseInt(entity.getSpeed()));
		valueSpeed.setTypeface(other_tf);
		valueStamina.setText("" + Integer.parseInt(entity.getStamina()));
		valueStamina.setTypeface(other_tf);
		valueDefence.setText("" + Integer.parseInt(entity.getDefence()));
		valueDefence.setTypeface(other_tf);
		valuePower.setText("" + Integer.parseInt(entity.getPower()));
		valuePower.setTypeface(other_tf);
		valueSkills.setText("" + Integer.parseInt(entity.getSkills()));
		valueSkills.setTypeface(other_tf);
		
		sexangle.setValue(Integer.parseInt(entity.getAttack()), Integer.parseInt(entity.getDefence()),
				Integer.parseInt(entity.getStamina()), Integer.parseInt(entity.getPower()),
				Integer.parseInt(entity.getSkills()), Integer.parseInt(entity.getSpeed()));
			
		birthAndPlace.setText(entity.getBirthAndPlace());
		weightAndHeight.setText(entity.getWeightAndHeight());
		teamAndPosition.setText(entity.getTeam1AndPosisiton());
		matchNumber.setText(entity.getMatchNumber());
		winRate.setText(entity.getWinRate());
		totalGoal.setText(entity.getGoal());
		totalAssist.setText(entity.getAssist());
	}
	
	private void initiateOthers(){
		initiateMine();
		if(!isFriend){
			button1.setText("申请好友");
		}
		title.setText(entity.getName()+"个人主页");
		mListView.setVisibility(View.GONE);
		info3Detail.setVisibility(View.GONE);
		Drawable drawable= getResources().getDrawable(R.drawable.more_down);
		info3Detail.setVisibility(View.GONE);
		infoButton3.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
		infoButton3.setText("下一场比赛对手："+entity.getAgainstName());
		initGrade(Integer.parseInt(entity.getScore()));
		info3_team.setText(entity.getTeam1());
		info3_place.setText(entity.getPlace());
		info3_time.setText(entity.getDateAndTime());
		info3_type.setText(entity.getType());
		info3_person.setText(entity.getPerson());
		titleBack.setOnClickListener(this);
		infoButton2.setOnClickListener(this);
		infoButton3.setOnClickListener(this);
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		OthersMatchEntity title=new OthersMatchEntity("代表球队", "上场", "进球", "助攻");
		mList.add(title);
		Iterator<OthersMatchEntity> iter=entity.getList().iterator();
		while(iter.hasNext()){
			mList.add(iter.next());
		}
		adapter=new OthersMatchAdapter(getActivity(), mList);
		mListView.setAdapter(adapter);
		Tools.setListViewHeight(mListView);
		
	}
	
	@SuppressWarnings("deprecation")
	public void setR(boolean enable){
		if(enable==false){
			signin.setEnabled(enable);
			pb.setVisibility(View.VISIBLE);
			iv.setAlpha(0);
		}
		else{
			if(!isSign){
				signin.setEnabled(enable);
				pb.setVisibility(View.GONE);
				iv.setAlpha(1);
			}
		}
	}
	
	public void setIsSign(boolean isSign){
		this.isSign=isSign;
	}
	
	@Override
	public void onClick(View v) {
		if(this.phone.equals("host")){
			if(v.getId()==R.id.homepage_signin){
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("request", "sign");
			map.put("phone", new PreferenceData(getActivity()).getData(new String[]{"phone"}).get("phone").toString());
			map.put("date", Tools.getDate1());
			Runnable r=new ClientWrite(Tools.JsonEncode(map));
			new Thread(r).start();
			setR(false);
			((HomePageActivity)getActivity()).openProgressBarWait(HomePageActivity.WAIT_SIGNIN, pb, null);
			}
			else{
				setEnable(false);
				((HomePageActivity)getActivity()).onHomePageClick(v, phone);
			}
		}
		else{
			int id=v.getId();
			switch(id){
			case R.id.btn_other_info2:{
				if(!isListVisible){
					mListView.setVisibility(View.VISIBLE);
					Drawable drawable= getResources().getDrawable(R.drawable.more_up);
					zhugong.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
					isListVisible=true;
				}
				else{
					mListView.setVisibility(View.GONE);
					Drawable drawable= getResources().getDrawable(R.drawable.more_down);
					zhugong.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
					isListVisible=false;
				}
				break;
			}
			case R.id.other_btn_info3:{
				if(!isInfo3Visible){
					info3Detail.setVisibility(View.VISIBLE);
					Drawable drawable= getResources().getDrawable(R.drawable.more_up);
					infoButton3.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
					isInfo3Visible=true;
				}
				else{
					info3Detail.setVisibility(View.GONE);
					Drawable drawable= getResources().getDrawable(R.drawable.more_down);
					infoButton3.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
					isInfo3Visible=false;
				}
				break;
			}
			case R.id.other_chat:{
				if(isFriend){
					((HomePageActivity)getActivity()).openChat(phone, ChatFragment.PEORSON_CHAT);
				}
				else{
					button1.setEnabled(false);
					Map<String, Object> tmp=new HashMap<String, Object>();
					tmp.put("request", "apply friend");
					tmp.put("phone", this.phone);
					tmp.put("date", Tools.getDate());
					Runnable r=new ClientWrite(Tools.JsonEncode(tmp));
					new Thread(r).start();
					Toast.makeText(getActivity(), "申请已发送至对方", Toast.LENGTH_SHORT).show();
				}
				break;
			}
			case R.id.other_evaluate:{
				setEnable(false);
				if(isFriend){
					Map<String, Object> map=new HashMap<String, Object>();
					map.put("request", "evaluate info");
					map.put("phone", this.phone);
					Runnable r=new ClientWrite(Tools.JsonEncode(map));
					new Thread(r).start();
					((HomePageActivity)getActivity()).openVague(HomePageActivity.WAIT_EVALUATE_INFO);
				}
				else{
					Toast.makeText(getActivity(), "成为好友才能评分哦", Toast.LENGTH_LONG).show();
					setEnable(true);
				}
				break;
			}
			case R.id.other_homepage_back:{
				((HomePageActivity)getActivity()).onBackPressed();
				break;
			}
			case R.id.rl_other_skills:{
				((HomePageActivity)getActivity()).openShowSkills(phone);
				break;
			}
			case R.id.rl_other_file:{
				Bundle bundle=new Bundle();
				Iterator<FileEntity> iter=entity.getFileList().iterator();
				int i=1;
				while(iter.hasNext()){
					bundle.putSerializable(""+i, iter.next());
					i++;
				}
				((HomePageActivity)getActivity()).showFile(bundle);
				break;
			}
			}
		}
		
	}	
	
	public void evaluate(){
		((HomePageActivity)getActivity()).openEvaluate(phone, entity.getImage(), entity.getName(), entity.getTeam1AndPosisiton(), "");
	}
	
	public void setValue(Bundle bundle){
		setBarColor(power, valuePower, bundle.getInt("power"));
		setBarColor(skills, valueSkills, bundle.getInt("skills"));
		setBarColor(stamina, valueStamina, bundle.getInt("stamina"));
		setBarColor(speed, valueSpeed, bundle.getInt("speed"));
		setBarColor(attack, valueAttack, bundle.getInt("attack"));
		setBarColor(defence, valueStamina, bundle.getInt("defence"));
	}
	
	private int initGrade(int score){
		String str=null;
		int grade=0;
		int max=0;
		if(score>=0 && score<80){
			strNext="预备队";
			str="新兵";
			grade=1;
			max=80;
		}
		else if(score>=80 && score<200){
			strNext="板凳";
			str="预备队";
			grade=2;
			max=200;
		}
		else if(score>=200 && score<360){
			strNext="替补";
			str="板凳";
			grade=3;
			max=360;
		}
		else if(score>=360 && score<580){
			strNext="超级替补";
			str="替补";
			grade=4;
			max=580;
		}
		else if(score>=580 && score<880){
			strNext="首发";
			str="超级替补";
			grade=5;
			max=800;
		}
		else if(score>=880 && score<1260){
			strNext="神经刀";
			str="首发";
			grade=6;
			max=1260;
		}
		else if(score>=1260 && score<1720){
			strNext="关键球员";
			str="神经刀";
			grade=7;
			max=1720;
		}
		else if(score>=1720 && score<2260){
			strNext="中流砥柱";
			str="关键球员";
			grade=8;
			max=2260;
		}
		else if(score>=2260 && score<2880){
			strNext="球霸";
			str="中流砥柱";
			grade=9;
			max=2880;
		}
		else if(score>=2880 && score<3560){
			strNext="大腿";
			str="球霸";
			grade=10;
			max=3560;
		}
		else if(score>=3560 && score<4300){
			strNext="老兵";
			str="大腿";
			grade=11;
			max=4300;
		}
		else if(score>=4300 && score<5100){
			strNext="传奇";
			str="老兵";
			grade=12;
			max=5100;
		}
		else if(score>=5100){
			strNext="无解";
			str="传奇";
			grade=13;
			max=5100;
		}
		gradeText.setText("Lv."+grade+" "+str);
		return max;
	}
	
	@SuppressWarnings("deprecation")
	public void initGrade(boolean isSigned, int addup, int score){
		this.score=score;
		if(isSigned){
			text.setVisibility(View.VISIBLE);
			text.setText("连续登陆"+addup+"天");
			signin.setEnabled(false);
			iv.setAlpha(0);
			addupText.setText("");
			
		}
		String str=null;
		int grade=0;
		if(score>=0 && score<80){
			str="新兵";
			grade=1;
			pbGrade.setMax(80);
			pbGrade.setProgress(score);
			max=80;
			strNext="预备队";
		}
		else if(score>=80 && score<200){
			str="预备队";
			grade=2;
			pbGrade.setMax(200);
			pbGrade.setProgress(score);
			max=200;
			strNext="板凳";
		}
		else if(score>=200 && score<360){
			str="板凳";
			grade=3;
			pbGrade.setMax(360);
			pbGrade.setProgress(score);
			max=360;
			strNext="替补";
		}
		else if(score>=360 && score<580){
			str="替补";
			grade=4;
			pbGrade.setMax(580);
			pbGrade.setProgress(score);
			max=580;
			strNext="超级替补";
		}
		else if(score>=580 && score<880){
			str="超级替补";
			grade=5;
			pbGrade.setMax(880);
			pbGrade.setProgress(score);
			max=880;
			strNext="首发";
		}
		else if(score>=880 && score<1260){
			str="首发";
			grade=6;
			pbGrade.setMax(1260);
			pbGrade.setProgress(score);
			max=1260;
			strNext="神经刀";
		}
		else if(score>=1260 && score<1720){
			str="神经刀";
			grade=7;
			pbGrade.setMax(1720);
			pbGrade.setProgress(score);
			max=1720;
			strNext="关键球员";
		}
		else if(score>=1720 && score<2260){
			str="关键球员";
			grade=8;
			pbGrade.setMax(2260);
			pbGrade.setProgress(score);
			max=2260;
			strNext="中流砥柱";
		}
		else if(score>=2260 && score<2880){
			str="中流砥柱";
			grade=9;
			pbGrade.setMax(2880);
			pbGrade.setProgress(score);
			max=2880;
			strNext="球霸";
		}
		else if(score>=2880 && score<3560){
			str="球霸";
			grade=10;
			pbGrade.setMax(3560);
			pbGrade.setProgress(score);
			max=3560;
			strNext="大腿";
		}
		else if(score>=3560 && score<4300){
			str="大腿";
			grade=11;
			pbGrade.setMax(4300);
			pbGrade.setProgress(score);
			max=4300;
			strNext="老兵";
		}
		else if(score>=4300 && score<5100){
			str="老兵";
			grade=12;
			pbGrade.setMax(5100);
			pbGrade.setProgress(score);
			max=5100;
			strNext="传奇";
		}
		else if(score>=5100){
			str="传奇";
			grade=13;
			pbGrade.setMax(5100);
			pbGrade.setProgress(5100);
			max=5100;
			strNext="无解";
		}
		gradeText.setText("Lv."+grade+" "+str);
	}
	
	
	private void setBarColor(ProgressBar progressBar, TextView value, int progress){
		if(progress<=10){
			Drawable d=getResources().getDrawable(R.drawable.progress_bar_style_1);
			progressBar.setProgressDrawable(d);
		}
		else{
			Drawable d=getResources().getDrawable(R.drawable.progress_bar_style);
			progressBar.setProgressDrawable(d);
		}
		progressBar.setProgress(progress);
		
		value.setText(""+progress);
	}

	@Override
	public void onDestroy() {
		RealTimeHandler.getInstance().unRegist(this);
		super.onDestroy();
	}
	
	

}
