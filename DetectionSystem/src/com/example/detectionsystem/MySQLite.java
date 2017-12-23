//to perform the database
package com.example.detectionsystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MySQLite 
{
	public static final String DATABASE="Gas_Detection_System1";
	public static final String TABLE_HISTORY="History_Record";
	public static final String TABLE_RETRIVE="Retrive_Data";
	public static final int VERSION=2;
	
	public static final String H_TIME="Time";
	public static final String H_DATE="Date";
	public static final String H_PPM="ppm";
	public static final String H_LAT="latitude";
	public static final String H_LNG="longitude";
	
	public static final String R_round="Round";
	public static final String R_time="Time";
	public static final String R_ppm="ppm";
			
	private static final String SCRIPT1=
			"CREATE TABLE "+TABLE_HISTORY
			+"(ID INTEGER PRIMARY KEY AUTOINCREMENT,"
			+H_TIME+" TEXT,"
			+H_DATE+" TEXT,"
			+H_PPM+" NUMBER,"
			+H_LAT+" NUMBER,"
			+H_LNG+" NUMBER);";
	
	private static final String SCRIPT2=
			"CREATE TABLE "+TABLE_RETRIVE
			+"("+R_round+" NUMBER,"
			+R_time+" TEXT,"
			+R_ppm+" NUMBER);";
	
	private SQLiteHelper helper;
	private SQLiteDatabase database;
	private Context context;
	
	public MySQLite(Context c)
	{
		context=c;
		
		/*openToWrite();
		insert_Retrive("1","0","0");
		insert_Retrive("1","1","12");
		insert_Retrive("1","2","25");
		insert_Retrive("1","3","35");
		insert_Retrive("1","4","43");
		insert_Retrive("1","5","45");
		insert_Retrive("1","6","46");
		insert_Retrive("1","7","47");
		insert_Retrive("1","8","48");
		insert_Retrive("1","9","48");
		insert_Retrive("1","10","49");
		insert_Retrive("1","11","49");
		insert_Retrive("1","12","48");
		insert_Retrive("1","13","47");
		insert_Retrive("1","14","47");
		insert_Retrive("1","15","48");
		insert_Retrive("1","16","48");
		insert_Retrive("1","17","48");
		insert_Retrive("1","18","47");
		insert_Retrive("1","19","47");
		insert_Retrive("1","20","47");
		insert_Retrive("1","21","47");
		insert_Retrive("1","22","48");
		insert_Retrive("1","23","48");
		insert_Retrive("1","24","48");
		insert_Retrive("1","25","48");
		insert_Retrive("1","26","49");
		insert_Retrive("1","27","50");
		insert_Retrive("1","28","51");
		insert_Retrive("1","29","50");
		insert_Retrive("1","30","49");
		insert_Retrive("1","31","50");
		insert_Retrive("1","32","50");
		insert_Retrive("1","33","49");
		insert_Retrive("1","34","48");
		insert_Retrive("1","35","48");
		insert_Retrive("1","36","48");
		insert_Retrive("1","37","48");
		insert_Retrive("1","38","48");
		insert_Retrive("1","39","48");
		insert_Retrive("1","40","49");
		insert_Retrive("1","41","52");
		insert_Retrive("1","42","55");
		insert_Retrive("1","43","56");
		insert_Retrive("1","44","58");
		insert_Retrive("1","45","58");
		insert_Retrive("1","46","57");
		insert_Retrive("1","47","57");
		insert_Retrive("1","48","59");
		insert_Retrive("1","49","62");
		insert_Retrive("1","50","63");
		insert_Retrive("1","51","67");
		insert_Retrive("1","52","67");
		insert_Retrive("1","53","69");
		insert_Retrive("1","54","71");
		insert_Retrive("1","55","74");
		insert_Retrive("1","56","77");
		insert_Retrive("1","57","78");
		insert_Retrive("1","58","79");
		insert_Retrive("1","59","80");	
		insert_Retrive("1","60","81");
		
		insert_Retrive("2","0","0");
		insert_Retrive("2","1","0");
		insert_Retrive("2","2","1");
		insert_Retrive("2","3","1");
		insert_Retrive("2","4","1");
		insert_Retrive("2","5","1");
		insert_Retrive("2","6","1");
		insert_Retrive("2","7","1");
		insert_Retrive("2","8","1");
		insert_Retrive("2","9","1");
		insert_Retrive("2","10","1");
		insert_Retrive("2","11","1");
		insert_Retrive("2","12","1");
		insert_Retrive("2","13","1");
		insert_Retrive("2","14","1");
		insert_Retrive("2","15","2");
		insert_Retrive("2","16","3");
		insert_Retrive("2","17","2");
		insert_Retrive("2","18","1");
		insert_Retrive("2","19","1");
		insert_Retrive("2","20","1");
		insert_Retrive("2","21","1");
		insert_Retrive("2","22","1");
		insert_Retrive("2","23","1");
		insert_Retrive("2","24","1");
		insert_Retrive("2","25","1");
		insert_Retrive("2","26","1");
		insert_Retrive("2","27","1");
		insert_Retrive("2","28","1");
		insert_Retrive("2","29","1");
		insert_Retrive("2","30","1");
		insert_Retrive("2","31","1");
		insert_Retrive("2","32","1");
		insert_Retrive("2","33","1");
		insert_Retrive("2","34","1");
		insert_Retrive("2","35","1");
		insert_Retrive("2","36","1");
		insert_Retrive("2","37","1");
		insert_Retrive("2","38","1");
		insert_Retrive("2","39","1");
		insert_Retrive("2","40","1");
		insert_Retrive("2","41","1");
		insert_Retrive("2","42","1");
		insert_Retrive("2","43","1");
		insert_Retrive("2","44","1");
		insert_Retrive("2","45","1");
		insert_Retrive("2","46","2");
		insert_Retrive("2","47","1");
		insert_Retrive("2","48","1");
		insert_Retrive("2","49","1");
		insert_Retrive("2","50","1");
		insert_Retrive("2","51","1");
		insert_Retrive("2","52","1");
		insert_Retrive("2","53","1");
		insert_Retrive("2","54","1");
		insert_Retrive("2","55","1");
		insert_Retrive("2","56","1");
		insert_Retrive("2","57","1");
		insert_Retrive("2","58","1");
		insert_Retrive("2","59","1");	
		insert_Retrive("2","60","1");
		
		insert_Retrive("3","0","0");
		insert_Retrive("3","1","1");
		insert_Retrive("3","2","1");
		insert_Retrive("3","3","1");
		insert_Retrive("3","4","2");
		insert_Retrive("3","5","2");
		insert_Retrive("3","6","3");
		insert_Retrive("3","7","4");
		insert_Retrive("3","8","5");
		insert_Retrive("3","9","6");
		insert_Retrive("3","10","7");
		insert_Retrive("3","11","7");
		insert_Retrive("3","12","8");
		insert_Retrive("3","13","9");
		insert_Retrive("3","14","9");
		insert_Retrive("3","15","9");
		insert_Retrive("3","16","10");
		insert_Retrive("3","17","10");
		insert_Retrive("3","18","11");
		insert_Retrive("3","19","12");
		insert_Retrive("3","20","12");
		insert_Retrive("3","21","12");
		insert_Retrive("3","22","13");
		insert_Retrive("3","23","13");
		insert_Retrive("3","24","14");
		insert_Retrive("3","25","14");
		insert_Retrive("3","26","15");
		insert_Retrive("3","27","15");
		insert_Retrive("3","28","15");
		insert_Retrive("3","29","14");
		insert_Retrive("3","30","13");
		insert_Retrive("3","31","13");
		insert_Retrive("3","32","13");
		insert_Retrive("3","33","13");
		insert_Retrive("3","34","13");
		insert_Retrive("3","35","13");
		insert_Retrive("3","36","13");
		insert_Retrive("3","37","12");
		insert_Retrive("3","38","12");
		insert_Retrive("3","39","12");
		insert_Retrive("3","40","13");
		insert_Retrive("3","41","13");
		insert_Retrive("3","42","13");
		insert_Retrive("3","43","13");
		insert_Retrive("3","44","13");
		insert_Retrive("3","45","13");
		insert_Retrive("3","46","13");
		insert_Retrive("3","47","13");
		insert_Retrive("3","48","12");
		insert_Retrive("3","49","10");
		insert_Retrive("3","50","10");
		insert_Retrive("3","51","10");
		insert_Retrive("3","52","10");
		insert_Retrive("3","53","10");
		insert_Retrive("3","54","10");
		insert_Retrive("3","55","10");
		insert_Retrive("3","56","10");
		insert_Retrive("3","57","10");
		insert_Retrive("3","58","10");
		insert_Retrive("3","59","10");	
		insert_Retrive("3","60","10");
		close();*/
	}
	
	public MySQLite openToRead() throws SQLException
	{
		helper=new SQLiteHelper(context, DATABASE, null, VERSION);
		database=helper.getReadableDatabase();
		return this;
	}
	
	public MySQLite openToWrite() throws SQLException
	{
		helper=new SQLiteHelper(context, DATABASE, null, VERSION);
		database=helper.getWritableDatabase();
		return this;
	}
	
	public void close()
	{
		database.close();
	}
	
	public int deleteAll_History()
	{
		return database.delete(TABLE_HISTORY, null, null);
	}
	
	public int deleteAll_Retrive()
	{
		return database.delete(TABLE_RETRIVE, null, null);
	}
	
	public int delete_Retrive(String round)
	{
		return database.delete(TABLE_RETRIVE, R_round+"="+round, null);
	}
	
	public long insert_History(String t, String d, String ppm, String lat, String lng)
	{
		ContentValues cv=new ContentValues();
		cv.put(H_TIME,t);
		cv.put(H_DATE,d);
		cv.put(H_PPM,ppm);
		cv.put(H_LAT, lat);
		cv.put(H_LNG,lng);
		return database.insert(TABLE_HISTORY, null, cv);
	}
	
	public long insert_Retrive(String round, String time, String ppm)
	{
		ContentValues cv=new ContentValues();
		cv.put(R_round,round);
		cv.put(R_time,time);
		cv.put(R_ppm,ppm);
		return database.insert(TABLE_RETRIVE, null, cv);
	}
	
	public long update_Retrive_ppm(String round, String time, String ppm)
	{
		ContentValues cv=new ContentValues();
		cv.put(R_ppm,ppm);
		return database.update(TABLE_RETRIVE, cv, R_round+"="+round+" AND "+R_time+"="+time, null);
	}
	
	public long update_Retrive_round(String oldround, String newround)
	{
		ContentValues cv=new ContentValues();
		cv.put(R_round,newround);
		return database.update(TABLE_RETRIVE, cv, R_round+"="+oldround, null);
	}
	
	//when the record is more than 100
	public String queue_History200()
	{
		String[] columns={"ID",H_TIME,H_DATE,H_PPM,H_LAT,H_LNG};
		Cursor cursor=database.query(TABLE_HISTORY, columns, null, null, null, null, "ID DESC");
		String result=H_TIME+","+H_DATE+","+H_PPM+","+H_LAT+","+H_LNG+";";
		int[] index={cursor.getColumnIndex("ID"),cursor.getColumnIndex(H_TIME),cursor.getColumnIndex(H_DATE),cursor.getColumnIndex(H_PPM),cursor.getColumnIndex(H_LAT),cursor.getColumnIndex(H_LNG)};
		
		for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
		{
			result=result+cursor.getString(index[1])+","+cursor.getString(index[2])+","+cursor.getString(index[3])+","+cursor.getString(index[4])+","+cursor.getString(index[5])+";";
		}
		
		return result;
	}
	
	//when the record is less than 100
	public String queue_History100()
	{
		String[] columns={"ID",H_TIME,H_DATE,H_PPM,H_LAT,H_LNG};
		Cursor cursor=database.query(TABLE_HISTORY, columns, null, null, "ID", "ID>=(SELECT count(*) FROM History_Record)-100", "ID DESC");
		String result=H_TIME+","+H_DATE+","+H_PPM+","+H_LAT+","+H_LNG+";";
		int[] index={cursor.getColumnIndex("ID"),cursor.getColumnIndex(H_TIME),cursor.getColumnIndex(H_DATE),cursor.getColumnIndex(H_PPM),cursor.getColumnIndex(H_LAT),cursor.getColumnIndex(H_LNG)};
		
		for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
		{
			result=result+cursor.getString(index[1])+","+cursor.getString(index[2])+","+cursor.getString(index[3])+","+cursor.getString(index[4])+","+cursor.getString(index[5])+";";
		}
		
		return result;
	}
	
	public String queue_Retrive_ppm(String round)
	{
		String[] columns={R_ppm};
		Cursor cursor=database.query(TABLE_RETRIVE, columns, R_round+"="+round, null, null, null, null);
		String result="";
		int[] index={cursor.getColumnIndex(R_ppm)};
		
		for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
		{
			result=result+cursor.getString(index[0])+";";
		}
		
		return result;
	}
	
	public String queue_Retrive_round()
	{
		String[] columns={"DISTINCT " + R_round};
		Cursor cursor=database.query(TABLE_RETRIVE, columns, null, null, null, null, null);
		String result="";
		int[] index={cursor.getColumnIndex(R_round)};
		
		for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
		{
			result=result+cursor.getString(index[0])+";";
		}
		
		return result;
	}
	
	public String queue_Retrive_total_round()
	{
		String[] columns={"DISTINCT " + R_round};
		Cursor cursor=database.query(TABLE_RETRIVE, columns, null, null, null, null, null);
		String result="";
		int[] index={cursor.getColumnIndex(R_round)};
		
		for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext())
		{
			result=cursor.getString(index[0]);
		}
		
		return result;
	}
	
	public class SQLiteHelper extends SQLiteOpenHelper
	{

		public SQLiteHelper(Context context, String name, CursorFactory factory,
				int version) 
		{
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) 
		{
			db.execSQL(SCRIPT1);
			db.execSQL(SCRIPT2);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) 
		{
			onCreate(db);
		}
		
	}
}
