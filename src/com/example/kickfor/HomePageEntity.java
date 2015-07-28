package com.example.kickfor;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
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
	private boolean isFriend=false;
	private List<OthersMatchEntity> list=new ArrayList<OthersMatchEntity>();

	private String againstname="��";
	private String dateAndTime="��";
	private String matchPlace="��";
	private String type="";
	
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
			dateAndTime=dateAndTime+map.get("time").toString();
		}
		if(map.containsKey("place")){
			matchPlace=map.get("place").toString();
		}
		if(map.containsKey("type") && map.get("type")!=null){
			dateAndTime=map.get("type").toString();
		}
		score=map.get("score").toString();
		name=map.get("name").toString();
		power=map.get("power").toString();
		speed=map.get("speed").toString();
		skills=map.get("skills").toString();
		stamina=map.get("stamina").toString();
		attack=map.get("attack").toString();
		defence=map.get("defence").toString();
		birth=map.get("year")+"."+map.get("month");
		place=map.get("city").toString();
		weight=map.get("weight").toString();
		height=map.get("height").toString();
		team1=map.get("team1").toString();
		position=map.get("position1").toString();
		String team2=map.get("team2").toString();
		String team3=map.get("team3").toString();
		String tmatch1=map.get("tmatch1").toString();
		String tmatch2=map.get("tmatch2").toString();
		String tmatch3=map.get("tmatch3").toString();
		String goal1=map.get("goal1").toString();
		String goal2=map.get("goal2").toString();
		String goal3=map.get("goal3").toString();
		String assist1=map.get("assist1").toString();
		String assist2=map.get("assist2").toString();
		String assist3=map.get("assist3").toString();
		matchNumber=String.valueOf(Integer.parseInt(tmatch1)+Integer.parseInt(tmatch2)+Integer.parseInt(tmatch3));
		goal=String.valueOf(Integer.parseInt(goal1)+Integer.parseInt(goal2)+Integer.parseInt(goal3));
		assist=String.valueOf(Integer.parseInt(assist1)+Integer.parseInt(assist2)+Integer.parseInt(assist3));
		double win=Integer.parseInt(map.get("win1").toString())+Integer.parseInt(map.get("win2").toString())+Integer.parseInt(map.get("win3").toString());
		double number=Integer.valueOf(matchNumber);
		winRate=convert(win/number*100)+"%";
		OthersMatchEntity item1=new OthersMatchEntity(team1, tmatch1, goal1, assist1);
		list.add(item1);
		OthersMatchEntity item2=new OthersMatchEntity(team2, tmatch2, goal2, assist2);
		list.add(item2);
		OthersMatchEntity item3=new OthersMatchEntity(team3, tmatch3, goal3, assist3);
		list.add(item3);
		
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
	
	private boolean initiateOthers(){
		Cursor cursor=helper.select("friends", new String[]{"phone"}, "phone=?", new String[]{phone}, null);
		if(cursor.moveToNext()){
			return true;
		}
		else{
			return false;
		}
	}
	
	private boolean initiateMine(){
		String[] columns=new String[]{"name", "power", "speed", "skills", "stamina", "attack", 
				                      "defence", "year", "month", "city", "weight", "height", 
				                      "team1", "position1", "tmatch1", "tmatch2", "tmatch3", 
				                      "goal1", "goal2", "goal3", "assist1", "assist2", "assist3", 
				                      "win1", "win2", "win3", "image", "team2", "team3", "addup", "score", "date"};
		Cursor cursor=helper.select("ich", columns, "phone=?", new String[]{phone}, null);
		if(cursor.moveToNext()){
			name=cursor.getString(0);
			power=cursor.getString(1);
			System.out.println("power="+power);
			speed=cursor.getString(2);
			skills=cursor.getString(3);
			stamina=cursor.getString(4);
			attack=cursor.getString(5);
			defence=cursor.getString(6);
			birth=cursor.getString(7)+"."+cursor.getString(8);
			place=cursor.getString(9);
			weight=cursor.getString(10)+"Kg";
			height=cursor.getString(11)+"cm";
			team1=cursor.getString(12);
			Cursor cursor1=helper.select("teams", new String[]{"name"}, "teamid=?", new String[]{team1}, null);
			if(cursor1.moveToNext()){
				team1=cursor1.getString(0);
			}
			position=cursor.getString(13);
			matchNumber=String.valueOf(Integer.valueOf(cursor.getString(14))+Integer.valueOf(cursor.getString(15))+Integer.valueOf(cursor.getString(16)));
			goal=String.valueOf(Integer.valueOf(cursor.getString(17))+Integer.valueOf(cursor.getString(18))+Integer.valueOf(cursor.getString(19)));
			assist=String.valueOf(Integer.valueOf(cursor.getString(20))+Integer.valueOf(cursor.getString(21))+Integer.valueOf(cursor.getString(22)));
			double win=Integer.valueOf(cursor.getString(23))+Integer.valueOf(cursor.getString(24))+Integer.valueOf(cursor.getString(25));
			double number=Integer.valueOf(matchNumber);
			winRate=convert(win/number*100)+"%";
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
			return true;
		}
		else{
			return false;
		}
		
	}
	
	private String convert(double f) {
        DecimalFormat df = new DecimalFormat("#.0");
        return String.valueOf(df.format(f));
    }
	
	

}