package com.LGiao.moneymanagement;

public class Money {
	private String ID;
	private String Note;
	private String Date;
	private String Category;
	private String Amount;
	public Money(String _ID,String _Note,String _Date,String _Category,String _Amount)
	{
		ID=_ID;
		Note=_Note;
		Date=_Date;
		Category=_Category;
		Amount=_Amount;
	}	
	public String getID()
	{
		return ID;
	}
	public String getNote()
	{
		return Note;
	}
	public String getDate()
	{
		return Date;
	}
	public String getCategory()
	{
		return Category;
	}
	public String getAmount()
	{
		return Amount;
	}
}
