package com.LGiao.moneymanagement;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBHelper extends SQLiteOpenHelper{

	public static final String KEY_ROWID="ID";
	public static final String KEY_NOTE="Note";
	public static final String KEY_DATE="Date";
	public static final String KEY_AMOUNT="Amount";
	public static final String KEY_CATEGORY="Category";
	
	private static final String DATABASE_NAME="Money";
	private static final String DATABASE_TABLE="Record";
	private static final int DATABASE_VERSION=1;
	public DBHelper(Context context) {
		super(context,DATABASE_NAME,null,DATABASE_VERSION);
		// TODO Auto-generated constructor stub
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
	
		String createQuery=" CREATE TABLE Record(ID INTEGER PRIMARY KEY AUTOINCREMENT,"
				+ "Note TEXT,"
				+ "Date TEXT NOT NULL,"
				+ "Amount REAL NOT NULL,"
				+ "Category TEXT NOT NULL)";
		db.execSQL(createQuery);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		Log.w(DBHelper.class.getName(), "Upgrading database from old to new version...");
		db.execSQL("DROP TABLE IF EXIST "+DATABASE_TABLE);
		onCreate(db);
		
	}


}
