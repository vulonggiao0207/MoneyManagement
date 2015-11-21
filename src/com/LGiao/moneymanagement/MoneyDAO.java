package com.LGiao.moneymanagement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

public class MoneyDAO extends Activity{
	public static final String KEY_ROWID="ID";
	public static final String KEY_NOTE="Note";
	public static final String KEY_DATE="Date";
	public static final String KEY_CATEGORY="Category";
	public static final String KEY_AMOUNT="Amount";
	
	private static final String DATABASE_NAME="Money";
	private static final String DATABASE_TABLE="Record";
	
	
	private DBHelper ourHelper;
	private final Context ourContext;
	private SQLiteDatabase ourDatabase;
	
	public MoneyDAO(Context c) {
		ourContext=c;
		// TODO Auto-generated method stub
	}
	public MoneyDAO open() throws SQLException
	{
		ourHelper=new DBHelper(ourContext);
		ourDatabase=ourHelper.getReadableDatabase();
		return this;
	}
	public void close() throws SQLException
	{
		ourHelper.close();
	}
	public long createRecord(String note, String date, String amount, String category) throws SQLException
	{
		ContentValues cv= new ContentValues();
		cv.put(KEY_NOTE, note);
		cv.put(KEY_DATE, date);
		cv.put(KEY_AMOUNT,Float.parseFloat(amount));
		cv.put(KEY_CATEGORY,category);
		return ourDatabase.insert(DATABASE_TABLE,null ,cv);		
	}
	public String dayJump(String currday, int dayCount)
    {
    	SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");    	
    	Calendar calendar = Calendar.getInstance();  
    	//get current dates
    	try{
    	calendar.setTime(format.parse(currday));
    	}catch(Exception e){}
    	calendar.add(calendar.DAY_OF_YEAR, dayCount);
    	return format.format(calendar.getTime()); 
    }
	public List<Money> listEntry(String date, int filter) throws SQLException
	  {
		  //Cursor cur= ourDatabase.query(DATABASE_TABLE,new String[]{ "Category", "SUM(Amount) AS Sum"},null,null,"Category",null,null);
		 String query="";
		 if(filter==0)//daily
			 query="SELECT Category, SUM(Amount) AS Sum FROM Record WHERE date='"+date+"' GROUP BY Category";
		 else if (filter==1)//Weekly
		 {
			 //get current day of week by number Sunday=1 ... Saturday =7
			 Calendar cal=Calendar.getInstance();
			 SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");			 
			 try{
			 cal.setTime(sdf.parse(date));			 
			 }catch (ParseException p ){}
			 int dayOfWeek=cal.get(Calendar.DAY_OF_WEEK);
			 //get the periods
			 int daysTo7=7-dayOfWeek;
			 int daysTo1=(6-daysTo7)*-1;
			 String startDate=dayJump(date,daysTo1);
			 String endDate=dayJump(date,daysTo7);
			 //create query
			 query="SELECT Category, SUM(Amount) AS Sum FROM Record "
			 		+ "WHERE (substr(date ,7,4) ||substr(date ,4,2)||substr(date ,1,2))"
					+" BETWEEN (substr('"+startDate+"' ,7,4) ||substr('"+startDate+"' ,4,2)||substr('"+startDate+"' ,1,2))"
			 		+" AND (substr('"+endDate+"',7,4) ||substr('"+endDate+"',4,2)||substr('"+endDate+"' ,1,2))"
					+" GROUP BY Category";
		 }
		 else if(filter==2)//monthly
		 {
			 query="SELECT Category, SUM(Amount) AS Sum FROM Record "
			 		+ "WHERE (substr(date ,7,4) ||substr(date ,4,2))"
					+ "=(substr('"+date+"',7,4)||substr('"+date+"',4,2))"
					+" GROUP BY Category";
		 }
		 else if(filter==3)//yearly
		 {
			 query="SELECT Category, SUM(Amount) AS Sum FROM Record "
			 		+ "WHERE (substr(date ,7,4))"
			 		+ "=(substr('"+date+"',7,4))"
					+" GROUP BY Category";
			 
		 }
		 else if(filter==4)
			 query="SELECT Category, Amount, Note AS Sum FROM Record WHERE date='"+date+"'";
		 Cursor cur=ourDatabase.rawQuery(query,null);
		  List<Money> list = new ArrayList<Money>();
		  int iRow= cur.getColumnIndex(KEY_ROWID);    	
		  
		  for(cur.moveToFirst();!cur.isAfterLast();cur.moveToNext())
		  {
			  if(filter==4)
			  {
				  Money mn=new Money(null,
					  cur.getString(2).toString(),
					  null,
					  cur.getString(0).toString(),
					  cur.getString(1).toString());
				  list.add(mn);		
			  }
			  else
			  {
				  Money mn=new Money(null,
						  null,
						  null,
						  cur.getString(0).toString(),
						  cur.getString(1).toString());
				  list.add(mn);			  
			  }
		  }
		  cur.close();
		  return list;
	  }


}
