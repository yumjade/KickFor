package com.example.kickfor.team;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.kickfor.Tools;

import android.graphics.Bitmap;

public class MatchReviewEntity implements Serializable{
	
	private static final long serialVersionUID = 1L;   
	
	private String againstid=null;
	private String date=null;
	private String place=null;
	private String teamImg=null;
	private String teamName=null;
	private String againstName=null;
	private String againstImg=null;
	private String reviewStatus = null;
	private int goals=0;
	private int lost=0;
	private int assists=0;
	private String time=null;
	private String info=null;
	private String format=null;
	private String type=null;
	private int id=0;
	private String status=null;
	
	public MatchReviewEntity(int id, String date, String place, String teamImg, String againstImg, String teamName, String againstName,String reviewStatus, int goals, int lost){
		this.date=date;
		this.place=place;
		this.teamImg=teamImg;
		this.againstImg=againstImg;
		this.teamName=teamName;
		this.againstName=againstName;
		this.reviewStatus = reviewStatus;
		this.goals=goals;
		this.lost=lost;
		this.id=id;
	}
	
	public MatchReviewEntity(int id, String date, String teamImg, String teamName, String againstName, String reviewStatus,int goals, int lost){
		this.date=date;
		this.id=id;
		this.teamImg=teamImg;
		this.teamName=teamName;
		this.againstName=againstName;
		this.reviewStatus = reviewStatus;
		this.goals=goals;
		this.lost=lost;
		
	}
	
	public void setDetail(String info, String type, String place, String time){
		this.info=info;
		this.type=type;
		this.place=place;
		this.time=time;
	}
	
	public void setPlace(String place){
		this.place=place;
	}
	
	public void setAssists(String assists){
		this.assists=Integer.parseInt(assists);
	}
	
	public void setStatus(String status){
		this.status=status;
	}
	
	public String getStatus(){
		return status;
	}
	
	public int getAssists(){
		return assists;
	}
	
	public int getGoals(){
		return goals;
	}
	
	public int getLost(){
		return lost;
	}
	
	public int getId(){
		return id;
	}
	
	public void setTime(String time){
		this.time=time;
	}
	
	public void setAgainstId(String againstid){
		this.againstid=againstid;
	}
	
	public void setAgainstName(String againstName){
		this.againstName=againstName;
	}
	
	public void setAgainstImg(String againstImg){
		this.againstImg=againstImg;
	}
	
	public void setDate(String date){
		this.date=date;
	}
	
	
	public void setDetails(String info, String format, String type){
		this.info=info;
		this.format=format;
		this.type=type;
	}
	
	public String getTime(){
		return time;
	}
	
	public String getDate(){
		return date;
	}
	
	public List<Map<String, Object>> getMembersInfo(){
		List<Map<String, Object>> listInfo=new ArrayList<Map<String, Object>>();
		List<String> mList=explode(info, "<END>");
		int length=mList.size();
		for(int index=0; index<length; index++){
			String str=mList.get(index);
			List<String> item=explode(str, ";");
			String number=item.get(0);
			String name=item.get(1);
			String goal=item.get(2);
			String assist=item.get(3);
			String yellowCard=item.get(4);
			String redCard=item.get(5);
			String phone=item.get(6);
			String attendance=item.get(7);
			Map<String, Object> map=new HashMap<String, Object>();
			map.put("number", number);
			map.put("name", name);
			map.put("phone", phone);
			map.put("attendance", attendance);
			map.put("goal", goal);
			map.put("assist", assist);
			map.put("yellow", yellowCard);
			map.put("red", redCard);
			listInfo.add(map);
		}
		return listInfo;
	}
	
	
	public String[] getDetails(){
		List<String> mList=explode(info, "<END>");
		int length=mList.size();
		String[] details=new String[4];
		for(int index=0; index<length; index++){
			String str=mList.get(index);
			List<String> item=explode(str, ";");
			String number=item.get(0);
			String name=item.get(1);
			String goal=item.get(2);
			String assist=item.get(3);
			String yellowCard=item.get(4);
			String redCard=item.get(5);
			String attendance=item.get(7);
			if(index==0){
				if(!(goal.equals("0"))){
					details[1]=name+"x"+goal+" ";
				}
				if(!(assist.equals("0"))){
					details[2]=name+"x"+assist+" ";
				}
				if(!(yellowCard.equals("0") && redCard.equals("0"))){
					details[3]=name+" ";
				}
				if(attendance.equals("1")){
					details[0]=number+" "+name+"   ";
				}
				
			}
			else{
				if(!(goal.equals("0"))){
					details[1]=details[1]+name+"x"+goal+" ";
				}
				if(!(assist.equals("0"))){
					details[2]=details[2]+name+"x"+assist+" ";
				}
				if(!(yellowCard.equals("0") && redCard.equals("0"))){
					details[3]=details[3]+name;
				}
				if(attendance.equals("1")){
					details[0]=details[0]+number+" "+name+"   ";
				}
			}
			
		}
		return details;
	}
	
	public Bitmap getFormat(){
		return Tools.stringtoBitmap(format);
	}
	
	public String getType(){
		return type;
	}
	
	public String getPlace(){
		return place;
	}
	
	public String getDateAndPlace(){
		return date+" "+place;
	}
	
	public String getAgainstid(){
		return againstid;
	}
	
	public Bitmap getTeamImg(){
		return Tools.stringtoBitmap(teamImg);
	}
	
	public Bitmap getAgainstImg(){
		return Tools.stringtoBitmap(againstImg);
	}
	
	public String getTeamName(){
		return teamName;
	}
	
	public String getAgainstName(){
		return againstName;
	}
	
	
	
	public String getReviewStatus() {
		return reviewStatus;
	}

	public void setReviewStatus(String reviewStatus) {
		this.reviewStatus = reviewStatus;
	}

	public String getSocre(){
		return goals+" - "+lost;
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
