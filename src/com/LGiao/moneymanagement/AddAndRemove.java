package com.LGiao.moneymanagement;

import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.style.TtsSpan.DecimalBuilder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;


public class AddAndRemove extends Activity implements OnClickListener{
	//Controls
	TextView tvDate;
	EditText edtAmount;
	Spinner spinnerType;
	EditText edtNote;
	Button btnOK;
	Button btnCancel;
	ListView recordsListView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.addandremove);
		//Controls
		tvDate=(TextView)findViewById(R.id.tvDate);
		edtAmount=(EditText)findViewById(R.id.edtAmount);
		spinnerType=(Spinner)findViewById(R.id.spinnerType);
		edtNote=(EditText)findViewById(R.id.edtNote);
		btnOK=(Button)findViewById(R.id.btnOK);
		btnCancel=(Button)findViewById(R.id.btnCancel);
		recordsListView=(ListView)findViewById(R.id.recordListView);
		//addOrRemove
		addOrRemove();		
		
		//Event
		btnOK.setOnClickListener(this);
		btnCancel.setOnClickListener(this);	
		//Set information		
		String str=getIntent().getStringExtra("type_name");
		int type=Integer.parseInt(str);
		if(type==1)
		{
			str="Expense: ";			
		}
		else
		{
			str="Deposit: ";
		}
		tvDate.setText(str+getIntent().getStringExtra("currdate"));	
		//load Records to listView
		loadRecords(type);

	}
	private void addOrRemove()
	{
		//Determine the Add or Remove Activity
		Intent myIntent=getIntent();		
		String str=getIntent().getStringExtra("type_name");
		Integer type=Integer.parseInt(str);
		//set values to Spinner
		Spinner edpList= (Spinner)findViewById(R.id.spinnerType);
		Resources res= getResources();
		ArrayAdapter<CharSequence> adapter = null;
		if (type==0)
		{
			adapter= ArrayAdapter.createFromResource(this, R.array.add_array,
				android.R.layout.simple_list_item_1);
		}
		else if (type==1)
		{
			adapter= ArrayAdapter.createFromResource(this, R.array.remove_array,
					android.R.layout.simple_list_item_1);
		}
		adapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
		edpList.setAdapter(adapter);	

	}
	@Override
	public void onClick(View arg)
	{
		switch (arg.getId()) {
		case R.id.btnOK:	
			MoneyDAO mDAO=new MoneyDAO(this);
			String var1=edtNote.getText().toString();
			Intent myIntent=getIntent();		
			String var2=getIntent().getStringExtra("currdate");			
			String var3=edtAmount.getText().toString();
			String var4 = (String)spinnerType.getSelectedItem();
			mDAO.open();
			long result= mDAO.createRecord(var1,var2,var3 ,var4 );
			
			mDAO.close();
			if (result==0)
			{
				Dialog d= new Dialog(this);
				d.setTitle("Error");
				TextView tv= new TextView(this);
				tv.setText("Cannot insert new record. Try again!");
				d.setContentView(tv);
				d.show();
				return;
			}
			finish();
	//		super.onResume();
			break;		
		case R.id.btnCancel:
			finish();
			break;
		default:
			finish();
	//		super.onResume();
			break;
		}
	}
	public void loadRecords(int type)
	{
		MoneyDAO mnDAO= new MoneyDAO(this);
		mnDAO.open();       
	    //Return data 
	    List<Money> records=mnDAO.listEntry(getIntent().getStringExtra("currdate"),4);
	    if(type==0)
	    {
	    	for(int i= records.size()-1;i>-1;i--)
	    	{
	    		String _record=records.get(i).getCategory();
	    		if(!_record.equals("Deposit")&&!_record.equals("Salary")&&!_record.equals("Gift"))
	    		{
	    			records.remove(records.get(i));
	    		}
	    	}
	    }
	    else 
	    {
	    	for(int i= records.size()-1;i>-1;i--)
	    	{
	    		String _record=records.get(i).getCategory();
	    		if(_record.equals("Deposit")||_record.equals("Salary")||_record.equals("Gift"))
	    		{
	    			records.remove(records.get(i));
	    		}
	    	}
	    	
	    }
	    recordsListView.setAdapter(new yourAdapter(this, records));
	    mnDAO.close();
	    
	}
}
