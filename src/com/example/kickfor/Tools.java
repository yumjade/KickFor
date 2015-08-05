package com.example.kickfor;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;

import org.json.JSONObject;
import org.json.JSONStringer;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMGroup;
import com.easemob.chat.EMGroupManager;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

@SuppressLint("SimpleDateFormat")
public class Tools {
	
	private static final int GB_SP_DIFF = 160;
    // 存放国标一级汉字不同读音的起始区位码
    private static final int[] secPosValueList = { 1601, 1637, 1833, 2078, 2274, 2302,
            2433, 2594, 2787, 3106, 3212, 3472, 3635, 3722, 3730, 3858, 4027,
            4086, 4390, 4558, 4684, 4925, 5249, 5600 };
    // 存放国标一级汉字不同读音的起始区位码对应读音
    private static final char[] firstLetter = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
            'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'w', 'x',
            'y', 'z' };
	
    public static String getSpells(String characters) {
		StringBuffer buffer = new StringBuffer();
		char ch = characters.charAt(0);

		if ((ch >> 7) == 0) {
			if ((ch >= 65 && ch <= 90) || (ch >= 97 && ch <= 122)) {
				// 判断是否为汉字，如果左移7为为0就不是汉字，否则是汉字
				buffer.append(String.valueOf(ch));
			} else {
				return "#";
			}

		} else {

			char spell = getFirstLetter(ch);
			buffer.append(String.valueOf(spell));
		}
		return buffer.toString().toUpperCase();
	}

    // 获取一个汉字的首字母
    public static Character getFirstLetter(char ch) {

        byte[] uniCode = null;
        try {
            uniCode = String.valueOf(ch).getBytes("GBK");
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        if (uniCode[0] < 128 && uniCode[0] > 0) { // 非汉字
            return null;
        } else {
            return convert(uniCode);
        }
    }

    /**
     * 获取一个汉字的拼音首字母。 GB码两个字节分别减去160，转换成10进制码组合就可以得到区位码
     * 例如汉字“你”的GB码是0xC4/0xE3，分别减去0xA0（160）就是0x24/0x43
     * 0x24转成10进制就是36，0x43是67，那么它的区位码就是3667，在对照表中读音为‘n’
     */
    static char convert(byte[] bytes) {
        char result = '-';
        int secPosValue = 0;
        int i;
        for (i = 0; i < bytes.length; i++) {
            bytes[i] -= GB_SP_DIFF;
        }
        secPosValue = bytes[0] * 100 + bytes[1];
        for (i = 0; i < 23; i++) {
            if (secPosValue >= secPosValueList[i]
                    && secPosValue < secPosValueList[i + 1]) {
                result = firstLetter[i];
                break;
            }
        }
        return result;
    }
	
	
	public static boolean hasSdcard(){
		String state=Environment.getExternalStorageState();
		if(state.equals(Environment.MEDIA_MOUNTED)){
			return true;
		}
		else{
			return false;
		}
	}
	
	public static void getGroupIdForHuan(Handler handler1, String[] teamid){
		final String[] tmp=teamid;
		final Handler handler=handler1;
		new Thread(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				Bundle bundle=new Bundle();
				for(int i=0; i<3; i++){
					if(!tmp[i].isEmpty()){
						try{
							List<EMGroup> grouplist = EMGroupManager.getInstance().getGroupsFromServer();
							Iterator<EMGroup> iter=grouplist.iterator();
							while(iter.hasNext()){
								EMGroup group=iter.next();
								if(group.getGroupName().equals(tmp[i])){
									bundle.putString(tmp[i], group.getGroupId());
									break;
								}
							}
						}catch(Exception e){
							e.printStackTrace();
						}
					}
				}
				Message msg=handler.obtainMessage();
				msg.setData(bundle);
				msg.what=HomePageActivity.GET_GROUPID;
				handler.sendMessage(msg);
			}
			
		}).start();
	}
	
	
	
	public static int JsonGetLength(byte[] bytes){
		String str=new String(bytes);
		int length=Integer.parseInt(str)-8;
		return length;		
	}
	
	
	public static String MapToJson(Map<String, Object> map){
		try{
			Iterator<String> iter=map.keySet().iterator();
			JSONStringer jsonString=new JSONStringer();
			jsonString.object();
			while(iter.hasNext()){
				String key=iter.next();
				String value=map.get(key).toString();
				jsonString.key(key);
				jsonString.value(value);
			}
			String string=jsonString.endObject().toString();
			System.out.println("JSON:"+string);
			return string;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	public static String JsonEncode(Map<String, Object>map){
		try{
			Iterator<String> iter=map.keySet().iterator();
			JSONStringer jsonString=new JSONStringer();
			jsonString.object();
			while(iter.hasNext()){
				String key=iter.next();
				String value=map.get(key).toString();
				jsonString.key(key);
				jsonString.value(value);
			}
			String string=jsonString.endObject().toString();
			int length=string.getBytes().length+8;
			String str=String.valueOf(length);
			int wordsLength=8-str.length();
			for(int i=0; i<wordsLength; i++){
				str='0'+str;
			}
			string=str+string;
			return string;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
		
	}
	
	
	public static void loginHuanXin(final String phone, final String passwords){
		EMChatManager.getInstance().login(phone, passwords,new EMCallBack() {//回调
			@Override
			public void onSuccess() {
				
				EMGroupManager.getInstance().loadAllGroups();
				EMChatManager.getInstance().loadAllConversations();
				HomePageActivity.getGroupid();	
				System.out.println("登陆聊天服务器成功！");		
			}
	

			@Override
			public void onProgress(int progress, String status) {

			}

			@Override
			public void onError(int code, String message) {
				System.out.println( "登陆聊天服务器失败！");
				//loginHuanXin(new PreferenceData(), passwords);
			}
		});
	}
	
	public static String bitmapToString(Bitmap bitmap){
		String string=null;
		if(bitmap!=null){
			ByteArrayOutputStream bStream=new ByteArrayOutputStream();
			bitmap.compress(CompressFormat.PNG, 100, bStream);
			byte[] bytes=bStream.toByteArray();
			string=Base64.encodeToString(bytes, Base64.DEFAULT);
		}
		return string;
	}
	
	public static Bitmap stringtoBitmap(String string) {
        Bitmap bitmap = null;
        if(string!=null){
        	try {
                byte[] bitmapArray;
                bitmapArray = Base64.decode(string, Base64.DEFAULT);
                bitmap = BitmapFactory.decodeByteArray(bitmapArray, 0, bitmapArray.length);
               }catch(Exception e){
               	e.printStackTrace();
               	}
        }
        return bitmap;
    }
	
	public static Map<String, Object> getMapForJson(String jsonStr){
		JSONObject jsonObject;
		try{
			jsonObject=new JSONObject(jsonStr);
			@SuppressWarnings("unchecked")
			Iterator<String> keyIter=jsonObject.keys();
			String key;
			Object value;
			Map<String, Object> valueMap=new TreeMap<String, Object>();
			while(keyIter.hasNext()){
				key=keyIter.next();
				value=jsonObject.get(key);
				valueMap.put(key, value);
			}
			return valueMap;
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	public static ContentValues getContentValuesFromMap(Map<String, Object> map, String phone){
		ContentValues cv=new ContentValues();
		Iterator<String> iter=map.keySet().iterator();
		while(iter.hasNext()){
			String key=iter.next();
			Object value=map.get(key);
			boolean boo=value instanceof Integer;
			if(boo==true){
				cv.put(key, (int)value);
			}
			else{
				cv.put(key, value.toString());
			}
		}
		if(phone!=null){
			cv.put("phone", phone);
			cv.remove("request");
		}
		return cv;
	}
	
	public static Map<String, Object> addMap(Map<String, Object> map, Map<String, Object> tmp){
		Iterator<String> iter=tmp.keySet().iterator();
		while(iter.hasNext()){
			String key=iter.next();
			Object value=tmp.get(key);
			map.put(key, value);	
		}
		return map;
	}
	
	public static void saveBitmapToFile(Bitmap bitmap, String _file)throws IOException{
		BufferedOutputStream os=null;
		try{
			File file=new File(_file);
			int end=_file.lastIndexOf(File.separator);
			String _filePath=_file.substring(0, end);
			File filePath=new File(_filePath);
			if(!filePath.exists()){
				filePath.mkdirs();
			}
			if(file.exists()){
				file.delete();
			}
			file.createNewFile();
			os=new BufferedOutputStream(new FileOutputStream(file));
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
		}finally{
			if(os!=null){
				try{
					os.close();
				}catch(IOException e){
					e.printStackTrace();
				}
			}
		}
	}
	
	
	public static String AsciiToString(String ascii){
    	String[] chars=ascii.split(" ");
    	String value="";
    	for(int i=0; i<chars.length; i++){
    		value=value+(char)Integer.parseInt(chars[i], 16);
    	}
    	return value;
    }
    
    public static String StringToAscii(String string){
    	char[] chars=string.toCharArray();
    	String value="";
    	for(int i=0; i<chars.length; i++){
    		value=value+Integer.toHexString((int)chars[i])+" ";
    	}
    	return value;
    }
	
	
	public static String getDate(){
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return format.format(new Date());
	}
	
	public static String getData(long time){
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
		return format.format(new Date(time));
	}
	
	public static String getDate(long last, long current){
		String date=null;
		if(current-last<=60*5*1000){
			return "";
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Date today = new Date(System.currentTimeMillis());
        Date otherDay = new Date(current);
        int temp = Integer.parseInt(sdf.format(today))- Integer.parseInt(sdf.format(otherDay));
        switch (temp) {
        case 0:{
        	SimpleDateFormat format=new SimpleDateFormat("HH:mm");
            date="今天 "+format.format(otherDay);
            break;
        }
        case 1:{
        	SimpleDateFormat format=new SimpleDateFormat("HH:mm");
            date="昨天 "+format.format(otherDay);
            break;
        }
        case 2:{
        	SimpleDateFormat format=new SimpleDateFormat("HH:mm");
            date="前天 "+format.format(otherDay);
            break;
        }
        default:{
        	SimpleDateFormat format=new SimpleDateFormat("MM-dd HH:mm");
            date=format.format(otherDay);
        	break;
        }
        }
        if(temp>365){
        	SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd HH:mm");
            date=format.format(otherDay);
        }
		return date;
	}
	
	public static String getDate1(){
		SimpleDateFormat format=new SimpleDateFormat("yyyy-MM-dd");
		return format.format(new Date());
	}
	
	public static boolean hasUnkickedMatch(Context context, String teamid){
		if(teamid.isEmpty()){
			return false;
		}
		else{
			SQLHelper helper=SQLHelper.getInstance(context);
			Cursor cursor=helper.select("matches", new String[]{"id"}, "status=? and teamid=?", new String[]{"u", teamid}, null);
			if(cursor.moveToNext()){
				return true;
			}
			else{
				return false;
			}
		}
		
	}
	
	public static String getDatabaseName(Context context){
		PreferenceData pd=new PreferenceData(context);
		String databaseName="MK_"+pd.getData(new String[]{"phone"}).get("phone");
		return databaseName;
	}
	
	public static int getUnHandleMsgNumber(int type, Context context){
		SQLHelper helper=SQLHelper.getInstance(context);
		Cursor cursor=helper.select("systemtable", new String[]{"i"}, "type=? and result=?", new String[]{String.valueOf(type), "u"}, null);
		return cursor.getCount();
	}
	
	public static String getIndex(Context context, String teamid, String phone){
		SQLHelper helper=SQLHelper.getInstance(context);
		Cursor cursor=helper.select("ich", new String[]{"team1", "team2", "team3"}, "phone=?", new String[]{phone}, null);
		if(cursor.moveToNext()){
			if(teamid.equals(cursor.getString(0))){
				return "1";
			}
			else if(teamid.equals(cursor.getString(1))){
				return "2";
			}
			else if(teamid.equals(cursor.getString(2))){
				return "3";
			}
			else{
				return "";
			}
		}
		else{
			return "";
		}
	}
	
	public static int setListViewHeight(ListView listview){
		BaseAdapter adapter=(BaseAdapter) listview.getAdapter();
		if(adapter==null){
			return 0;
		}
		int totalHeight=0;
		for(int i=0; i<adapter.getCount(); i++){
			View listItem=adapter.getView(i, null, listview);
			listItem.measure(0, 0);
			totalHeight+=listItem.getMeasuredHeight();
		}
		
		ViewGroup.LayoutParams params=listview.getLayoutParams();
		params.height=totalHeight+(listview.getDividerHeight()*(adapter.getCount()-1));
		listview.setLayoutParams(params);
		return params.height;
	}
	
	public static void changeListViewHeight(int height, ListView listview){
		ViewGroup.LayoutParams params=listview.getLayoutParams();
		params.height=height;
		listview.setLayoutParams(params);
	}
	
	public static List<String> explode(String str, String sign){
    	int end=str.indexOf(sign);
    	int sizeoff=sign.length();
    	List<String> mList=new ArrayList<String>();
    	while(str.length()!=0){
    		mList.add(str.substring(0, end));
    		str=str.substring(end+sizeoff);
    		end=str.indexOf(sign);
    		if(end==-1){
    			mList.add(str);
    			str="";
    		}
    	}
    	return mList;	
	}
	

	
	public static boolean compareDateAndTime(String date, String time){
		List<String> dateList=Tools.explode(date, "-");
		List<String> timeList=Tools.explode(time, ":");
		int year=Integer.parseInt(dateList.get(0));
		int month=Integer.parseInt(dateList.get(1));
		int day=Integer.parseInt(dateList.get(2));
		int hour=Integer.parseInt(timeList.get(0));
		List<String> current=Tools.explode(Tools.getDate(), " ");
		List<String> currentDate=Tools.explode(current.get(0), "-");
		List<String> currentTime=Tools.explode(current.get(1), ":");
		int cYear=Integer.parseInt(currentDate.get(0));
		int cMonth=Integer.parseInt(currentDate.get(1));
		int cDay=Integer.parseInt(currentDate.get(2));
		int cHour=Integer.parseInt(currentTime.get(0));
		System.out.println("手机时间："+cYear+"-"+cMonth+"-"+cDay+":"+cHour);
		if(cYear-year>0){
			return true;
		}
		else if((cYear==year)&&(cMonth-month>0)){
			return true;
		}
		else if((cYear==year)&&(cMonth==month)&&(cDay-day>0)){
			return true;
		}
		else if((cYear==year)&&(cMonth==month)&&(cDay==day)&&(cHour>(hour-1))){
			return true;
		}
		else{
			return false;
		}
	}
	
	public static long getLongFormatDateAndTime(String date, String time){
		long setTime=-1;
		try{
    		String str=date+" "+time;
        	SimpleDateFormat format= new SimpleDateFormat("yyyy-MM-dd HH:mm");
        	Date d=format.parse(str);
        	setTime=d.getTime();
    	}catch(Exception e){
    		e.printStackTrace();
    	}
		return setTime;
	}
	
	public static boolean isFileExist(String path){
		try{
			File f=new File(path);
			if(!f.exists()){
				return false;
			}
			else{
				return true;
			}
		}catch(Exception e){
			return false;
		}
	}
	
	public static int dip2px(Context context, float dipValue){ 
        final float scale = context.getResources().getDisplayMetrics().density; 
        return (int)(dipValue * scale + 0.5f); 
	} 

	public static int px2dip(Context context, float pxValue){ 
        final float scale = context.getResources().getDisplayMetrics().density; 
        return (int)(pxValue / scale + 0.5f); 
	}
	
	public static Bitmap convertViewToBitmap(View view){
		view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
	    view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
	    view.buildDrawingCache();
	    Bitmap bitmap = view.getDrawingCache();
	    return bitmap;
	}
	
	public static String saveMyBitmap(Bitmap mBitmap,String bitName)  {
		 String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + bitName + ".jpg";
	        File f = new File(path);
	        FileOutputStream fOut = null;
	        try {
	                fOut = new FileOutputStream(f);
	        } catch (FileNotFoundException e) {
	                e.printStackTrace();
	        }
	        mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
	        try {
	                fOut.flush();
	        } catch (IOException e) {
	                e.printStackTrace();
	        }
	        try {
	                fOut.close();
	        } catch (IOException e) {
	                e.printStackTrace();
	        }
			return path;
	}
	 
		public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
//			ByteArrayOutputStream output = new ByteArrayOutputStream();
//			bmp.compress(CompressFormat.PNG, 100, output);
//			if (needRecycle) {
//				bmp.recycle();
//			}
//			
//			byte[] result = output.toByteArray();
//			try {
//				output.close();
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//			
//			return result;
			 int i;
		        int j;
		        if (bmp.getHeight() > bmp.getWidth()) {
		            i = bmp.getWidth();
		            j = bmp.getWidth();
		        } else {
		            i = bmp.getHeight();
		            j = bmp.getHeight();
		        }
		        
		        Bitmap localBitmap = Bitmap.createBitmap(i, j, Bitmap.Config.RGB_565);
		        Canvas localCanvas = new Canvas(localBitmap);
		        
		        while (true) {
		            localCanvas.drawBitmap(bmp, new Rect(0, 0, i, j), new Rect(0, 0,i, j), null);
		            if (needRecycle)
		                bmp.recycle();
		            ByteArrayOutputStream localByteArrayOutputStream = new ByteArrayOutputStream();
		            localBitmap.compress(Bitmap.CompressFormat.JPEG, 100,
		                    localByteArrayOutputStream);
		            localBitmap.recycle();
		            byte[] arrayOfByte = localByteArrayOutputStream.toByteArray();
		            try {
		                localByteArrayOutputStream.close();
		                return arrayOfByte;
		            } catch (Exception e) {
		                //F.out(e);
		            }
		            i = bmp.getHeight();
		            j = bmp.getHeight();
		        }
		}
		
		
		public static Bundle convertMapToBundle(Map<String, Object> map){
			Bundle bundle=new Bundle();
			Iterator<String> iter=map.keySet().iterator();
			while(iter.hasNext()){
				String key=iter.next();
				bundle.putString(key, map.get(key).toString());
			}
			return bundle;
		}
		
		public static int randomNumber(int number){
			Random random = new Random();
			int v = random.nextInt();
			int value = Math.abs(v % number) + 1;
			return value;			
		}
		
		public static String dataFormat(float x){ 
			float b=(float)(Math.round(x*10))/10;
			return String.valueOf(b);
		}
		
}
