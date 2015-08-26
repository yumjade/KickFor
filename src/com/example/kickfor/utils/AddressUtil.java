package com.example.kickfor.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class AddressUtil {
	
	 //获取省的地址列表,//file-->数据库文件
    public static Map<Integer,List> getProvince(File file){
        
        String sql = "select ProSort ,ProName from T_Province ";
        SQLiteDatabase db = null;
        Cursor c = null;
        Map<Integer,List> provinceData = new HashMap<Integer,List>();
        //List provinceList = null;
        try{
                db = SQLiteDatabase.openOrCreateDatabase(file, null);
                c = db.rawQuery(sql, null);
                List provinceList1 = new ArrayList();
                List provinceList2 = new ArrayList();
                while(c.moveToNext()){
                        Map provinceMap = new HashMap();
                        provinceMap.put(c.getString(1), c.getInt(0));
                        provinceList1.add(provinceMap);
                        provinceList2.add(c.getString(1));
                }
                provinceData.put(0, provinceList1);
                provinceData.put(1, provinceList2);
        }catch(Exception e){
                Log.d("WineStock", "getProvince:"+e.getMessage());
        }finally{
                if(c!=null){
                        c.close();
                }
                if(db!=null){
                        db.close();
                }
        }
        return provinceData;
    } 
    //获取对应省下面城市的列表,//file-->数据库文件,id-->指对应省的ID
    public static Map<Integer,List> getCityByPid(int id,File file){
        String sql = "select ProID,CityName  from T_City where ProID= "+id;
        SQLiteDatabase db = null;
        Cursor c = null;
        Map<Integer,List> cityData = new HashMap<Integer,List>();
        //List cityList = null;
        try{
                db = SQLiteDatabase.openOrCreateDatabase(file, null);
                c = db.rawQuery(sql, null);
                List cityList1 = new ArrayList();
                List cityList2 = new ArrayList();
                while(c.moveToNext()){
                        Map cityMap = new HashMap();
                        cityMap.put(c.getString(1), c.getInt(0));
                        cityList1.add(cityMap);
                        cityList2.add(c.getString(1));
                }
                cityData.put(0, cityList1);
                cityData.put(1, cityList2);
                
        }catch(Exception e){
            Log.d("WineStock", "getCityByPid:"+e.getMessage());
        }finally{
                if(c!=null){
                        c.close();
                }
                if(db!=null){
                        db.close();
                }
        }
        return cityData;
    } 
    //获取对应市下面区的列表,//file-->数据库文件,id-->指对应市的ID
    public static List<String> getAreaByPid(int id,File file){
        String sql = "select ZoneName  from T_Zone where CityID= "+id;
        SQLiteDatabase db = null;
        Cursor c = null;
        List<String> areaList = null;
        try{
                db = SQLiteDatabase.openOrCreateDatabase(file, null);
                c = db.rawQuery(sql, null);
                areaList = new ArrayList<String>();
                while(c.moveToNext()){
                    areaList.add(c.getString(0));
                }
        }catch(Exception e){
            Log.d("WineStock", "getAreaByPid:"+e.getMessage());
        }finally{
                if(c!=null){
                        c.close();
                }
                if(db!=null){
                        db.close();
                }
        }
        return areaList;
    } 
}
