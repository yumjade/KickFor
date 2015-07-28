package com.example.kickfor.team;

import java.text.DecimalFormat;

public class ShooterAssister {
	
	private boolean b;
	private String name=null;
	private String goal=null;
	private String assist=null;
	private String matchNumber=null;
	private String efficiency=null;
	
	public ShooterAssister(boolean b, String name, String data, String matchNumber){
		this.b=b;
		this.name=name;
		this.matchNumber=matchNumber;
		if(b==true){
			this.goal=data;
		}
		else{
			this.assist=data;
		}
		double tmp1=Integer.parseInt(data);
		double tmp2=Integer.parseInt(matchNumber);
		if(tmp2!=0){
			this.efficiency=convert(tmp1/tmp2)+"/��";
		}
		else{
			this.efficiency="-/-";
		}
	}
	
	public ShooterAssister(boolean b){
		this.b=b;
		name="����";
		matchNumber="��������";
		if(b==true){
			goal="������";
			efficiency="����Ч��";
		}
		else{
			assist="������";
			efficiency="����Ч��";
		}
	}
	
	public String getName(){
		return name;
	}
	
	public String getData(){
		if(b==true){
			System.out.println("goal="+goal);
			return goal;
		}
		else{
			System.out.println("assist="+assist);
			return assist;
		}
	}
	
	public String getMatchNumber(){
		return matchNumber;
	}
	
	public String getEfficiency(){
		return efficiency;
	}
	
	 private String convert(double f) {
	        DecimalFormat df = new DecimalFormat("#.00");
	        return String.valueOf(df.format(f));
	    }
	

}
