package com.example.kickfor;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class HomePageEntity implements Serializable{
	
	private static final long serialVersionUID=111;
	
	private SQLHelper helper=null;
	private String phone=null;
	private String name=null;
	private String power=null;
	private String speed=null;
	private String skills=null;
	private String stamina=null;
	private String attack=null;
	private String defence=null;
	private String birth=null;
	private String place=null;
	private String height=null;
	private String weight=null;
	private String team1=null;
	private String position=null;
	private String matchNumber=null;
	private String winRate=null;
	private String goal=null;
	private String assist=null;
	private String addup=null;
	private String score=null;
	private String image=null;
	private String date=null;
	private int leftSkills=0;
	private boolean isFriend=false;
	private int left=0;
	private List<OthersMatchEntity> list=new ArrayList<OthersMatchEntity>();

	private String againstname="无";
	private String dateAndTime="无";
	private String matchPlace="无";
	private String type="";
	private String person="";
	
	private List<FileEntity> fList=new ArrayList<FileEntity>();
	private List<SkillsShowEntity> sList=new ArrayList<SkillsShowEntity>();
	
	public HomePageEntity(Context context, String phone){
		helper=SQLHelper.getInstance(context);
		this.phone=phone;
		if(phone.equals("host"))
			isFriend=initiateMine();
		else{
			isFriend=initiateOthers();
		}
	}
	
	public void setImage(Bitmap image){
		this.image=Tools.bitmapToString(image);
	}
	
	public String getAgainstName(){
		return againstname;
	}
	
	public String getDateAndTime(){
		return dateAndTime;
	}
	
	public String getPlace(){
		return matchPlace;
	}
	
	public String getType(){
		return type;
	}
	
	public String getPerson(){
		return person;
	}
	
	public String getTeam1(){
		return team1;
	}
	
	public void setData(Map<String, Object> map){
		if(map.containsKey("againstname")){
			againstname=map.get("againstname").toString();
		}
		if(map.containsKey("date")){
			dateAndTime=map.get("date").toString();
		}
		if(map.containsKey("time")){
			dateAndTime=dateAndTime+"  "+map.get("time").toString();
		}
		if(map.containsKey("place")){
			matchPlace=map.get("place").toString();
		}
		if(map.containsKey("type") && map.get("type")!=null){
			type=map.get("type").toString();
		}
		if(map.containsKey("person") && map.get("type")!=null){
			person=map.get("person").toString();
		}
		score=map.get("score").toString();
		name=map.get("name").toString().isEmpty()? "Unknown": map.get("name").toString();
		power=map.get("power").toString();
		speed=map.get("speed").toString();
		skills=map.get("skills").toString();
		stamina=map.get("stamina").toString();
		attack=map.get("attack").toString();
		defence=map.get("defence").toString();
		birth=(map.get("year").toString().isEmpty() || map.get("month").toString().isEmpty())? "生日": map.get("year")+"."+map.get("month");
		place=map.get("city").toString().isEmpty()? "地区": map.get("city").toString();
		weight=map.get("weight").toString().isEmpty()? "体重": map.get("weight").toString()+"kg";
		height=map.get("height").toString().isEmpty()? "身高": map.get("height").toString()+"cm";
		position=map.get("position1").toString().isEmpty()? "位置": map.get("position1").toString();
		team1=map.containsKey("team1") && !(map.get("team1").toString().isEmpty())? map.get("team1").toString(): "球队";
		String team2=map.containsKey("team2")? map.get("team2").toString(): "";
		String team3=map.containsKey("team3")? map.get("team3").toString(): "";
		String tmatch1=map.containsKey("tmatch1")? map.get("tmatch1").toString(): "0";
		String tmatch2=map.containsKey("tmatch2")? map.get("tmatch2").toString(): "0";
		String tmatch3=map.containsKey("tmatch3")? map.get("tmatch3").toString(): "0";
		String goal1=map.containsKey("goal1")? map.get("goal1").toString(): "0";
		String goal2=map.containsKey("goal2")? map.get("goal2").toString(): "0";
		String goal3=map.containsKey("goal3")? map.get("goal3").toString(): "0";
		String assist1=map.containsKey("assist1")? map.get("assist1").toString(): "0";
		String assist2=map.containsKey("assist2")? map.get("assist2").toString(): "0";
		String assist3=map.containsKey("assist3")? map.get("assist3").toString(): "0";
		String win1=map.containsKey("win1")? map.get("win1").toString(): "0";
		String win2=map.containsKey("win2")? map.get("win2").toString(): "0";
		String win3=map.containsKey("win3")? map.get("win3").toString(): "0";
		matchNumber=String.valueOf(Integer.parseInt(tmatch1)+Integer.parseInt(tmatch2)+Integer.parseInt(tmatch3));
		goal=String.valueOf(Integer.parseInt(goal1)+Integer.parseInt(goal2)+Integer.parseInt(goal3));
		assist=String.valueOf(Integer.parseInt(assist1)+Integer.parseInt(assist2)+Integer.parseInt(assist3));
		double win=Integer.parseInt(win1)+Integer.parseInt(win2)+Integer.parseInt(win3);
		double number=Integer.valueOf(matchNumber);
		winRate=(number==0? 100: Tools.dataFormat((float)(win/number*100)))+"%";
		if(!team1.isEmpty()){
			OthersMatchEntity item1=new OthersMatchEntity(team1, tmatch1, goal1, assist1);
			list.add(item1);
		}
		if(!team2.isEmpty()){
			OthersMatchEntity item2=new OthersMatchEntity(team2, tmatch2, goal2, assist2);
			list.add(item2);
		}
		if(!team3.isEmpty()){
			OthersMatchEntity item3=new OthersMatchEntity(team3, tmatch3, goal3, assist3);
			list.add(item3);
		}
		
		if(map.containsKey("userarchivesArray")){
			helper.delete("archive", null, null);
			helper.delete("archivematch", null, null);
			List<String> list=Tools.jsonToList(map.get("userarchivesArray").toString());
			Iterator<String> iter1=list.iterator();
			while(iter1.hasNext()){
				Map<String, Object> tmpp=Tools.getMapForJson(iter1.next());
				List<String> list1=Tools.jsonToList(tmpp.get("matchArray").toString());
				Iterator<String> iter2=list1.iterator();
				while(iter2.hasNext()){
					Map<String, Object> t=Tools.getMapForJson(iter2.next());
					helper.update(Tools.getContentValuesFromMap(t, null), Integer.parseInt(t.get("userarchivesmatchkey").toString()), Integer.parseInt(t.get("userarchiveskey").toString()));
				}
				tmpp.remove("matchArray");
				helper.update(Tools.getContentValuesFromMap(tmpp, null), "archive", Integer.parseInt(tmpp.get("userarchiveskey").toString()));
			}
		}
		if(map.containsKey("userSkillArray")){
			helper.delete("skills", null, null);
			List<String> list=Tools.jsonToList(map.get("userSkillArray").toString());
			Iterator<String> iter2=list.iterator();
			while(iter2.hasNext()){
				Map<String, Object> tempp=Tools.getMapForJson(iter2.next());
				helper.insert(Tools.getContentValuesFromMap(tempp, null), "skills");
			}
		}
		
	}
	
	public String getPhone(){
		return phone;
	}
	
	public String getScore(){
		return score;
	}
	
	public String getAddUp(){
		return addup;
	}
	
	public List<OthersMatchEntity> getList(){
		return list;
	}
	
	public boolean isInDatabase(){
		return isFriend;
	}
	
	
	public Bitmap getImage(){
		return Tools.stringtoBitmap(image);
	}
	
	public String getName(){
		return name;
	}
	
	public String getPower(){
		return power;
	}
	
	public String getSpeed(){
		return speed;
	}
	
	public String getSkills(){
		return skills;
	}
	
	public String getStamina(){
		return stamina;
	}
	
	public String getAttack(){
		return attack;
	}
	
	public String getDefence(){
		return defence;
	}
	
	public String getBirthAndPlace(){
		return birth+"\n"+place;
	}
	
	public String getWeightAndHeight(){
		return weight+"\n"+height;
	}
	
	public String getTeam1AndPosisiton(){
		return team1+"\n"+position;
	}
	
	public String getMatchNumber(){
		return matchNumber;
	}
	
	public String getWinRate(){
		return winRate;
	}
	
	public String getGoal(){
		return goal;
	}
	
	public String getAssist(){
		return assist;
	}
	
	public boolean isSignedToday(){
		if(date.equals(Tools.getDate1())){
			return true;
		}
		else{
			return false;
		}
	}
	
	public int getLeftNum(){
		return left;
	}
	
	public int getLeftSkillsNum(){
		return leftSkills;
	}
	
	public List<SkillsShowEntity> getSkillsList(){
		return sList;
	}
	
	private boolean initiateOthers(){
		Cursor cursor=helper.select("friends", new String[]{"phone"}, "phone=?", new String[]{phone}, null);
		if(cursor.moveToNext()){
			return true;
		}
		else{
			return false;
		}
	}
	
	public List<FileEntity> getFileList(){
		return fList;
	}
	
	private boolean initiateMine(){
		String[] columns=new String[]{"name", "power", "speed", "skills", "stamina", "attack", 
				                      "defence", "year", "month", "city", "weight", "height", 
				                      "team1", "position1", "tmatch1", "tmatch2", "tmatch3", 
				                      "goal1", "goal2", "goal3", "assist1", "assist2", "assist3", 
				                      "win1", "win2", "win3", "image", "team2", "team3", "addup", "score", "date", "userskillsnum"};
		Cursor cursor=helper.select("ich", columns, "phone=?", new String[]{phone}, null);
		if(cursor.moveToNext()){
			name=cursor.getString(0).isEmpty()? "Unknown": cursor.getString(0);
			power=cursor.getString(1);
			System.out.println("power="+power);
			speed=cursor.getString(2);
			skills=cursor.getString(3);
			stamina=cursor.getString(4);
			attack=cursor.getString(5);
			defence=cursor.getString(6);
			birth=cursor.getString(7).isEmpty() || cursor.getString(8).isEmpty()? "生日": cursor.getString(7)+"."+cursor.getString(8);
			place=cursor.getString(9).isEmpty()? "地区": cursor.getString(9);
			weight=cursor.getString(10).isEmpty()? "体重": cursor.getString(10)+"kg";
			height=cursor.getString(11).isEmpty()? "身高": cursor.getString(11)+"cm";
			team1=cursor.getString(12);
			Cursor cursor1=helper.select("teams", new String[]{"name"}, "teamid=?", new String[]{team1}, null);
			if(cursor1.moveToNext()){
				team1=cursor1.getString(0).isEmpty()? "球队": cursor1.getString(0);
			}
			else{
				team1="球队";
			}
			position=cursor.getString(13).isEmpty()? "位置": cursor.getString(13);
			matchNumber=String.valueOf(Integer.valueOf(cursor.getString(14))+Integer.valueOf(cursor.getString(15))+Integer.valueOf(cursor.getString(16)));
			goal=String.valueOf(Integer.valueOf(cursor.getString(17))+Integer.valueOf(cursor.getString(18))+Integer.valueOf(cursor.getString(19)));
			assist=String.valueOf(Integer.valueOf(cursor.getString(20))+Integer.valueOf(cursor.getString(21))+Integer.valueOf(cursor.getString(22)));
			double win=Integer.valueOf(cursor.getString(23))+Integer.valueOf(cursor.getString(24))+Integer.valueOf(cursor.getString(25));
			double number=Integer.valueOf(matchNumber);
			winRate=(number==0? 100: Tools.dataFormat((float)(win/number*100)))+"%";
			String imgPathName=cursor.getString(26);
			if(!(imgPathName.equals("-1"))){
				image=Tools.bitmapToString(BitmapFactory.decodeFile(cursor.getString(26)));
			}
			OthersMatchEntity item1=new OthersMatchEntity(cursor.getString(12), cursor.getString(14), cursor.getString(17), cursor.getString(20));
			list.add(item1);
			OthersMatchEntity item2=new OthersMatchEntity(cursor.getString(27), cursor.getString(15), cursor.getString(18), cursor.getString(21));
			list.add(item2);
			OthersMatchEntity item3=new OthersMatchEntity(cursor.getString(28), cursor.getString(16), cursor.getString(19), cursor.getString(22));
			list.add(item3);
			addup=cursor.getString(29);
			score=cursor.getString(30);
			date=cursor.getString(31);
			leftSkills=cursor.getInt(32)-2>=0? (cursor.getInt(32)-1): 0;
			initiateFile();
			initiateSkills();
			return true;
		}
		else{
			return false;
		}
		
	}
	
	private void initiateSkills(){
		Cursor cursor=helper.select("skills", new String[]{"skillname", "agreeNum"}, null, null, null);
		while(cursor.moveToNext()){
			SkillsShowEntity skills=new SkillsShowEntity("-1", "-1", cursor.getString(0), "1", cursor.getString(1));
			sList.add(skills);
		}
	}
	
	private void initiateFile(){
		Cursor cursor=helper.select("archive", new String[]{"position", "teamname", "inteam", "joindate", "exitdate"}, null, null, "userarchiveskey desc");
		left=cursor.getCount();
		int i=0;
		while(cursor.moveToNext() && i<2){
			FileEntity file=new FileEntity(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), null);
			fList.add(file);
			i=i+1;
		}
		left=left-fList.size();
	}
	

}
