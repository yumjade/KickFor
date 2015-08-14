package com.example.kickfor;

import java.io.Serializable;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLHelper extends SQLiteOpenHelper implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 10;
	
	private final static int DATABASE_VERSION=4;
	private final static String TABLE_NAME1="ich";
	private final static String TABLE_NAME2="teams";
	private final static String PHONE="phone";
	private final static String NAME="name";
	private final static String SEX="sex";
	private final static String HEIGHT="height";
	private final static String WEIGHT="weight";
	private final static String CITY="city";
	private final static String DISTRICT="district";
	private final static String YEAR="year";
	private final static String MONTH="month";
	private final static String DAY="day";
	private final static String POWER="power";
	private final static String SKILLS="skills";
	private final static String SPEED="speed";
	private final static String ATTACK="attack";
	private final static String DEFENCE="defence";
	private final static String STAMINA="stamina";
	private final static String POSITION1="position1";
	private final static String POSITION2="position2";
	private final static String TEAM1="team1";
	private final static String TEAM2="team2";
	private final static String TEAM3="team3";
	private final static String NUMBER1="number1";
	private final static String NUMBER2="number2";
	private final static String NUMBER3="number3";
	private final static String GOAL1="goal1";
	private final static String GOAL2="goal2";
	private final static String GOAL3="goal3";
	private final static String ASSIST1="assist1";
	private final static String ASSIST2="assist2";
	private final static String ASSIST3="assist3";
	private final static String IMAGE="image";
	private final static String GRADES="grades";
	private final static String MESSAGE="message";
	
	private static SQLHelper instance;
	
	
	public static SQLHelper getInstance(Context context){
		if(instance==null){
			instance=new SQLHelper(context);
		}
		return instance;
	}
	
	private SQLHelper(Context context){
		super(context, Tools.getDatabaseName(context), null, DATABASE_VERSION);
	}
	

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		System.out.println("create table ich and teams");
		String sql1="CREATE TABLE IF NOT EXISTS "+TABLE_NAME1+"("+PHONE+" varchar(11),"+NAME+" text,"+HEIGHT+" text,"+WEIGHT+" text,"+POSITION1+" text,"+POSITION2+" text,"+CITY+" text,"+DISTRICT+" text,"+YEAR+" text,"+MONTH+" text,"+DAY+" text,"+SEX+" text,"+POWER+" text,"+SKILLS+" text,"+DEFENCE+" text,"+ATTACK+" text,"+STAMINA+" text,"+SPEED+" text,"+TEAM1+" text, teamid1 varchar(12), authority1 varchar(1), authority2 varchar(1), authority3 varchar(1), teamid2 varchar(12), teamid3 varchar(12), tmatch1 varchar(5), tmatch2 varchar(5), tmatch3 varchar(5), win1 varchar(5), win2 varchar(5), win3 varchar(5), "+TEAM2+" text,"+TEAM3+" text,"+GOAL1+" text,"+ASSIST1+" text,"+GOAL2+" text,"+ASSIST2+" text,"+GOAL3+" text,"+NUMBER1+" text,"+NUMBER2+" text,"+NUMBER3+" text,"+ASSIST3+" text,"+GRADES+" varchar(22),"+MESSAGE+" text,"+IMAGE+" text, n varchar(8), score varchar(8), addup varchar(8), date varchar(10));";
		db.execSQL(sql1);
		String sql2="CREATE TABLE IF NOT EXISTS "+TABLE_NAME2+"(teamid varchar(12), "+NAME+" text,"+YEAR+" varchar(4),"+MONTH+" varchar(2),"+DAY+" varchar(2),"+CITY+" text,"+DISTRICT+" text, number varchar(2),"+IMAGE+" text, honor varchar(30), hall text, win varchar(10), goal varchar(10), assist varchar(10), lost varchar(10), totalmatch varchar(10), captain varchar(11), bestshooter text, bestassister text);";
		db.execSQL(sql2);
		String sql3="CREATE TABLE IF NOT EXISTS matches(id integer, teamid varchar(12), date text, status varchar(1), time text, ensure varchar(1));";
	    db.execSQL(sql3);
		String sql4="CREATE TABLE IF NOT EXISTS systemtable(i integer, result varchar(1), type text, id varchar(12), image text, date text, message text, name text, teamid varchar(12));";
	    db.execSQL(sql4);
	    String sql5="CREATE TABLE IF NOT EXISTS friends(phone varchar(11), name text, image text)";
	    db.execSQL(sql5);
	    String sql6="create table if not exists archive(userarchiveskey integer, uid varchar(11), teamname text, position text, inteam integer, joindate varchar(20), exitdate varchar(20));";
	    db.execSQL(sql6);
	    String sql7="create table if not exists archivematch(userarchivesmatchkey integer, uid varchar(11), userarchiveskey integer, matchname text, year varchar(4), ranking text);";
	    db.execSQL(sql7);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME1);
		db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME2);
		db.execSQL("DROP TABLE IF EXISTS matches");
		db.execSQL("DROP TABLE IF EXISTS systemtable");
		db.execSQL("DROP TABLE IF EXISTS friends");
		db.execSQL("DROP TABLE IF EXISTS archive");
		db.execSQL("DROP TABLE IF EXISTS archivematch");
		onCreate(db);
	}
	
	
	public void createFriendTable(String tableName){
		SQLiteDatabase db=getWritableDatabase();
		String sql="CREATE TABLE IF NOT EXISTS "+tableName+"(id integer, date text, who varchar(1), message text);";
		db.execSQL(sql);
	}
	
	public void createGroupTable(String tableName){
		SQLiteDatabase db=getWritableDatabase();
		String sql="CREATE TABLE IF NOT EXISTS "+tableName+"(id integer, date text, who varchar(1), phone varchar(12), name text, message text);";
		db.execSQL(sql);
	}
	
	public void createTeamMateTable(String tableName){
		SQLiteDatabase db=getWritableDatabase();
		String sql="CREATE TABLE IF NOT EXISTS "+tableName+"(phone varchar(11), name text, number varchar(2), authority varchar(1), goal integer, assist integer, attendance varchar(10), totalmatch varchar(10), position1 text, position2 text);";
		db.execSQL(sql);
	}
	
	
	
	public void dropTable(String tableName){
		SQLiteDatabase db=getWritableDatabase();
		String sql="drop table if exists "+tableName;
		db.execSQL(sql);
	}
	
	
    public void insert(ContentValues values, String tableName){  
        SQLiteDatabase db = getWritableDatabase();
        db.insert(tableName, null, values);    
    } 
    
    public void delete(String tableName, String clause, String[] whereArgs){
    	SQLiteDatabase db = getWritableDatabase();
    	db.delete(tableName, clause, whereArgs);
    }
    
    public boolean update(ContentValues values, String tableName, String phone){
    	SQLiteDatabase db=getWritableDatabase();
    	if(tableName.equals("ich")){
    		Cursor cursor=db.query(TABLE_NAME1, new String[]{PHONE}, "phone=?", new String[]{phone}, null, null, null);
        	if(cursor.moveToNext()){
        		db.update(TABLE_NAME1, values, "phone=?", new String[]{phone});
        		return true;
        	}
        	else{
        		db.insert(TABLE_NAME1, null, values);
        		return false;
        	}
    	}
    	else if(tableName.equals("teams")){
    		Cursor cursor=db.query(TABLE_NAME2, new String[]{"teamid"}, "teamid=?", new String[]{phone}, null, null, null);
        	if(cursor.moveToNext()){
        		db.update(TABLE_NAME2, values, "teamid=?", new String[]{phone});
        		return true;
        	}
        	else{
        		db.insert(TABLE_NAME2, null, values);
        		return false;
        	}
    	}
    	else if(tableName.equals("systemtable")){
    		Cursor cursor=db.query(tableName, new String[]{"id"}, "id=?", new String[]{phone}, null, null, null);
        	if(cursor.moveToNext()){
        		db.update(tableName, values, "id=?", new String[]{phone});
        		return true;
        	}
        	else{
        		db.insert(tableName, null, values);
        		return false;
        	}
    	}
    	else if(tableName.equals("friends")){
    		Cursor cursor=db.query(tableName, new String[]{"phone"}, "phone=?", new String[]{phone}, null, null, null);
        	if(cursor.moveToNext()){
        		db.update(tableName, values, "phone=?", new String[]{phone});
        		return true;
        	}
        	else{
        		if(!values.containsKey("image")){
        			values.put("image", "-1");
        		}
        		db.insert(tableName, null, values);
        		return false;
        	}
    	}
    	else{
    		Cursor cursor=db.query(tableName, new String[]{"phone"}, "phone=?", new String[]{phone}, null, null, null);
        	if(cursor.moveToNext()){
        		db.update(tableName, values, "phone=?", new String[]{phone});
        		return true;
        	}
        	else{
        		db.insert(tableName, null, values);
        		return false;
        	}
    	}
    }
    
    public boolean update(ContentValues values, String tableName, int id){
    	SQLiteDatabase db=getWritableDatabase();
    	String idd=String.valueOf(id);
    	if(tableName.equals("systemtable")){
    		Cursor cursor=db.query(tableName, new String[]{"i"}, "i=?", new String[]{idd}, null, null, null);
        	if(cursor.moveToNext()){
        		db.update(tableName, values, "i=?", new String[]{idd});
        		return true;
        	}
        	else{
        		db.insert(tableName, null, values);
        		return false;
        	}
    	}
    	else if(tableName.equals("archive")){
    		Cursor cursor=db.query(tableName, new String[]{"userarchiveskey"}, "userarchiveskey=?", new String[]{idd}, null, null, null);
        	if(cursor.moveToNext()){
        		db.update(tableName, values, "userarchiveskey=?", new String[]{idd});
        		return true;
        	}
        	else{
        		db.insert(tableName, null, values);
        		return false;
        	}
    	}
    	else{
    		Cursor cursor=db.query(tableName, new String[]{"id"}, "id=?", new String[]{idd}, null, null, null);
        	if(cursor.moveToNext()){
        		db.update(tableName, values, "id=?", new String[]{idd});
        		return true;
        	}
        	else{
        		db.insert(tableName, null, values);
        		return false;
        	}
    	}
    }
    
    public boolean update(ContentValues values, int userarchivesmatchkey, int userarchiveskey){
    	SQLiteDatabase db=getWritableDatabase();
    	Cursor cursor=db.query("archivematch", new String[]{"userarchivesmatchkey"}, "userarchivesmatchkey=? and userarchiveskey=?", new String[]{String.valueOf(userarchivesmatchkey), String.valueOf(userarchiveskey)}, null, null, null);
    	if(cursor.moveToNext()){
    		db.update("archivematch", values, "userarchivesmatchkey=? and userarchiveskey=?", new String[]{String.valueOf(userarchivesmatchkey), String.valueOf(userarchiveskey)});
    		return true;
    	}
    	else{
    		db.insert("archivematch", null, values);
    		return false;
    	}
    }
    
    public boolean update(ContentValues values, String tableName, int id, String teamid){
    	SQLiteDatabase db=getWritableDatabase();
    	String idd=String.valueOf(id);
    	Cursor cursor=db.query(tableName, new String[]{"id"}, "id=? and teamid=?", new String[]{idd, teamid}, null, null, null);
    	if(cursor.moveToNext()){
    		db.update(tableName, values, "id=? and teamid=?", new String[]{idd, teamid});
    		return true;
    	}
    	else{
    		db.insert(tableName, null, values);
    		return false;
    	}
    }
    
    
    public Cursor select(String tablename, String[] columns, String selection, String[] selectionArgs, String orderBy){
    	SQLiteDatabase db=getWritableDatabase();
    	Cursor cursor=db.query(tablename, columns, selection, selectionArgs, null, null, orderBy);
    	return cursor;
    }
    
    public void closeDb(){  
        if(instance != null){
        	try {
	            SQLiteDatabase db = instance.getWritableDatabase();
	            db.close();
	        } catch (Exception e) {
	            e.printStackTrace();
	        }
	        instance = null;
        }
    }  

}
