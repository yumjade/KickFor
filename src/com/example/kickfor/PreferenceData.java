package com.example.kickfor;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferenceData {
	
	private Context context=null;
	
	public PreferenceData(Context context){
		this.context=context;
	}
	
	public void save(Map<String, Object> map){
		SharedPreferences sp=context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
		Editor editor=sp.edit();
		Iterator<String> iter=map.keySet().iterator();
		while(iter.hasNext()){
			String key=iter.next().toString();
			String value=map.get(key).toString();
			editor.putString(key, value);
		}
		editor.commit();
	}
	
	public  Map<String, Object> getData(){
		Map<String, Object>data=new HashMap<String, Object>();
		SharedPreferences sp=context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
		Iterator<String> iter=sp.getAll().keySet().iterator();
		while(iter.hasNext()){
			String key=iter.next();
			data.put(key, sp.getAll().get(key));
		}
		return data;
	}
	
	public Map<String, Object> getData(String[] names){
		Map<String, Object>data=new HashMap<String, Object>();
		SharedPreferences sp=context.getSharedPreferences("preferences", Context.MODE_PRIVATE);
		for(int i=0; i<names.length; i++){
			data.put(names[i], sp.getString(names[i], ""));
		}
		return data;
	}

}
