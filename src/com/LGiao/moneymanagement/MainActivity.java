package com.LGiao.moneymanagement;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Date;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import android.R.string;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.media.browse.MediaBrowser.SubscriptionCallback;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnHoverListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.SlidingDrawer;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements OnClickListener, OnCheckedChangeListener {
	private static ToggleButton btnSlider;
	private static Button btnAdd;
	private static Button btnRemove;
	private static Button btnPrevious;
	private static Button btnNext;
	private static TextView tvDate;
	private static TextView tvBalance;
	private static TextView tvDeposit;
	private static TextView tvExpense;
	private static FrameLayout flChart;  
	private static TableLayout topLayout;
	private static SlidingDrawer sdOption;
	private static TableLayout buttonLayout;
	public static RadioGroup rdGroup;
	public static RadioButton rdDay;
	public static RadioButton rdWeek;
	public static RadioButton rdMonth;
	public static RadioButton rdYear;
	public static int width;
	public static int height;
	public static int top;
	public static String currdate;
	public static String currmonth;
	public static String curryear;
	public static int daycount;
	public static int monthcount;
	public static int yearcount;
	public static int filter=0;
	private static boolean RUN_ONCE = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Sliding Drawer
        btnSlider=(ToggleButton)findViewById(R.id.btnSliding);
        btnSlider.setOnClickListener(this);
        sdOption=(SlidingDrawer)findViewById(R.id.sdOption);
 
        //Add and Remove button
        btnAdd=(Button)findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(this);
        btnRemove=(Button)findViewById(R.id.btnRemove);
        btnRemove.setOnClickListener(this);
        btnPrevious=(Button)findViewById(R.id.btnPrevious);
        btnPrevious.setOnClickListener(this);
        btnNext=(Button)findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);
        //TextView
        tvDate=(TextView)findViewById(R.id.tvDate);
        tvBalance=(TextView)findViewById(R.id.tvBalance);
        tvDeposit=(TextView)findViewById(R.id.tvDeposit);
        tvExpense=(TextView)findViewById(R.id.tvExpense);
        tvBalance=(TextView)findViewById(R.id.tvBalance);
        //TableLayout top contain Toggle button
        topLayout=(TableLayout)findViewById(R.id.topLayout);
        ViewTreeObserver vto1 = topLayout.getViewTreeObserver(); 
        vto1.addOnGlobalLayoutListener(new OnGlobalLayoutListener() { 
            @Override 
            public void onGlobalLayout() {                 
               top=topLayout.getMeasuredHeight();
            } 
        });     
        //FrameLayout
        flChart=(FrameLayout)findViewById(R.id.frameChart);  
        buttonLayout=(TableLayout)findViewById(R.id.buttonLayout);
       
        //set current datetime
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        //get current date time with Date()
        Date date = new Date();  
        if(RUN_ONCE==true)
        {
        	tvDate.setText(dateFormat.format(date));
        	currdate=dateFormat.format(date).toString();
        	currmonth=dateFormat.format(date).toString().substring(3,10);
        	curryear=dateFormat.format(date).toString().substring(6,10);
        }
        else{
        if(filter==0){tvDate.setText(currdate);}
        else if (filter==1){reloadWeek();}
        else if (filter==2){reloadMonth();}
        else if (filter==3){reloadYear();}
        }
        //Draw the graph
        //Get size of the contained Layout
        ViewTreeObserver vto = flChart.getViewTreeObserver(); 
        
        vto.addOnGlobalLayoutListener(new OnGlobalLayoutListener() { 
            @Override 
            public void onGlobalLayout() {        
            	
	                width= flChart.getMeasuredWidth();
	                height= flChart.getMeasuredHeight()+20; 
	                showGraph(width,height);
            } 
        });     
        btnSlider.setChecked(true);
        //RadioButton daily, weekly, monthly, yearly
        rdGroup=(RadioGroup)findViewById(R.id.rdGroup);
        rdDay=(RadioButton)rdGroup.findViewById(R.id.rdDay);
        rdWeek=(RadioButton)rdGroup.findViewById(R.id.rdWeek);
        rdMonth=(RadioButton)rdGroup.findViewById(R.id.rdMonth);
        rdYear=(RadioButton)rdGroup.findViewById(R.id.rdYear);
        rdGroup.setOnCheckedChangeListener( this);
       
        
        
   }
    @Override
    public void onResume() {
        super.onResume();
        this.onCreate(null);
        if(RUN_ONCE==true)
        {
        	RUN_ONCE=false;
        	daycount=monthcount=yearcount=0;
       // 	showGraph(width,height);
        }
      
   //     showGraph(width,height);
    }
    public void reloadWeek()
    {
    	MoneyDAO mn= new MoneyDAO(this);
    	 //get current day of week by number Sunday=1 ... Saturday =7
		 Calendar cal=Calendar.getInstance();
		 SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");			 
		 try{
		 cal.setTime(sdf.parse(currdate));			 
		 }catch (ParseException p ){}
		 int dayOfWeek=cal.get(Calendar.DAY_OF_WEEK);
		 //get the periods
		 int daysTo7=7-dayOfWeek;
		 int daysTo1=(6-daysTo7)*-1;
		 String startDate=mn.dayJump(currdate,daysTo1);
		 String endDate=mn.dayJump(currdate,daysTo7);
		 currdate=dayJump(daycount);
		 tvDate.setText(startDate+"-"+endDate);
    }
    public void reloadMonth()
    {
    	 Calendar cal=Calendar.getInstance();
		 SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");			 
		 try{
		 cal.setTime(sdf.parse(currmonth));			 
		 }catch (ParseException p ){}
		 cal.add(cal.MONTH,monthcount);
		 String currMonth=sdf.format(cal.getTime());
		 currmonth=currMonth;
		 currMonth=currMonth.substring(3,10);
		 tvDate.setText(currMonth);
		 
    }
    public void reloadYear()
    {
    	 Calendar cal=Calendar.getInstance();
		 SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");			 
		 try{
		 cal.setTime(sdf.parse(curryear));			 
		 }catch (ParseException p ){}
		 cal.add(cal.YEAR,yearcount);
		 String currYear=sdf.format(cal.getTime());
		 curryear=currYear;
		 currYear=currYear.substring(6,10);
		 tvDate.setText(currYear);
		 
    }
    @Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		// TODO Auto-generated method stub
    	if(rdDay.isChecked()==true)
        {
     	   tvDate.setText(currdate);
     	   filter=0;
        }
        else if (rdWeek.isChecked()==true)
        {
     	   reloadWeek();
     	   filter=1;
        }
        else if (rdMonth.isChecked()==true)
        {
     	   reloadMonth();
     	   
   	//	 tvDate.setText(currmonth.substring(3,10));
     	   filter=2;
        }
        else if (rdYear.isChecked()==true)
        {
     	   reloadYear();
   //  	   tvDate.setText(curryear.substring(6,10));
     	   filter=3;    	 
        }
		
	}
    @Override
    public void onClick(View arg)
    {
    	
    	switch (arg.getId()) {
		case R.id.btnAdd:
			openAddAndRemove(this,0);
			break;
		case R.id.btnRemove:
			openAddAndRemove(this,1);
			break;
		case R.id.btnNext:			
			if(filter==0)
			{
				daycount++;
				tvDate.setText(dayJump(daycount));
				currdate=dayJump(daycount);
			}
			else if(filter==1)
			{
				daycount+=7;
				reloadWeek();
				currdate=dayJump(daycount);
				
			}
			else if (filter==2)
			{
				monthcount=1;
				reloadMonth();
				monthcount=0;
			}
			else if (filter==3)
			{
				yearcount=1;
				reloadYear();
				yearcount=0;
			}			
			showGraph(width,height);			
			break;
		case R.id.btnPrevious:
			if(filter==0)
			{
				daycount--;
				tvDate.setText(dayJump(daycount));
				currdate=dayJump(daycount);
			}
			else if(filter==1)
			{
				daycount-=7;
				reloadWeek();   
				currdate=dayJump(daycount);
			}
			else if (filter==2)
			{
				monthcount=-1;
			//	tvDate.setText(currmonth);
				reloadMonth();
				monthcount=0;
			}
			else if (filter==3)
			{
				yearcount=-1;
				reloadYear();
				yearcount=0;
			}		
			showGraph(width,height);
			
			break;
		case R.id.btnSliding:
			if(btnSlider.isChecked()!=true)
			{  //Set margin to SlidingDrawer
		        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(sdOption.getLayoutParams());
		        lp.setMargins(0, top, 0, 0);
		        sdOption.setLayoutParams(lp);
				sdOption.animateOpen();
				btnPrevious.setEnabled(false);
				btnNext.setEnabled(false);
				btnAdd.setEnabled(false);
				btnRemove.setEnabled(false);
				   if(filter==0){rdDay.setChecked(true);}
			        else if (filter==1){rdWeek.setChecked(true);}
			        else if (filter==2){rdMonth.setChecked(true);}
			        else if (filter==3){rdYear.setChecked(true);}
			
			}
			else
			{
				sdOption.animateClose();
				btnPrevious.setEnabled(true);
				btnNext.setEnabled(true);
				btnAdd.setEnabled(true);
				btnRemove.setEnabled(true);		
				//reload date label
				if(filter==0)
	        		tvDate.setText(currdate);
	        	else if (filter==1)
	        		reloadWeek();
	        	else if (filter==2)
	        		reloadMonth();
	        	else if (filter==3)
	        		reloadYear();			}
			
			break;
		default:
			break;
		}
    }
    public void showGraph(int x, int y)
   {
	   
	   //get the real measurement of the frame
	   width=x;
	   height=y;
       //Get list of expenses
       MoneyDAO mnDAO= new MoneyDAO(this);
       //Get all data from Database
       mnDAO.open();       
       //Show graph base on the period (Day, Month, Year)   	   
       String _date=currdate;    
       if(filter==0 || filter==1)
    	   _date=currdate;
       else if(filter==2)
    	   _date=currmonth;
       else if(filter==3)
    	   _date=curryear;
       //Return data 
       List<Money> records=mnDAO.listEntry(_date,filter);
       mnDAO.close();
       //Drawing graph
       showData(records);
      
   }  
    
    public static void openAddAndRemove(Context context, int type)
    {   
    	//pass value currdate to AddandRemove
    	Intent AARIntent = new Intent("android.intent.action.AddAndRemove");
    	Bundle extras= new Bundle();
    	extras.putString("type_name", String.valueOf(type));
    	extras.putString("currdate", currdate);
    	AARIntent.putExtras(extras);
    	context.startActivity(AARIntent);
//		context.startActivity(AARIntent);
    }
    //function used to return the number of increment and reduction days
    public String dayJump(int dayCount)
    {
    	SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");    	
    	Calendar calendar = Calendar.getInstance();    	
    	calendar.add(Calendar.DAY_OF_YEAR, dayCount);
    	return format.format(calendar.getTime()); 
    }
    
    public void showData(List<Money> records)
    {
    	//Deposit, expenses, left
    	float Tdeposit=0;
    	float Texpenses=0;
    	float TBalance=0;
 	   //create colors of columns in graph
 	   String []color={"#333377","#3df249","#6E6E6E","#ff0000","#32419a","#ff853d","#60b436","#3c3c3c","#cd392f","#00e0ff"};
 	   String []expense={  "Phone", "Shopping","Transport","Car","Medical","Care","Drink","Food","House","Tuition"};
 	   String []deposit={"Salary","Deposit","Gift"};
    	 //Create bitmap - the graph  
        Bitmap bg = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bg); 
    	 //Calculate the total of Deposit        
        String ctg="";
        for(int j=records.size()-1;j>-1;j--)
        { 
     	   ctg=records.get(j).getCategory();
     	   if(Arrays.asList(deposit).contains(ctg)==true)
     	   {
     		  Tdeposit=Tdeposit+Float.parseFloat(records.get(j).getAmount());
     		  records.remove(records.get(j));
     	   }
        }
        
        //Calculate the graph size and columns
        
        float wi=(width-10)/10;      
        float bottom=height;
        float right=0;
        float left=0;
        float top=height;
        for(int i=records.size()-1;i>-1;i--)
        {
        	ctg=records.get(i).getCategory();	  
     	   if(Arrays.asList(expense).contains(ctg)==true)
     	   {
 	    	   right=wi*(1+i);
 	    	   left=i*wi+5;
 	    	   float percent=Float.parseFloat(records.get(i).getAmount())/Tdeposit;
 	    	   top=height-((height-30)*percent);
 	    	   Paint paint = new Paint();
 	    	   paint.setTextSize(12);
 	    	   paint.setColor(Color.parseColor(color[i]));       
 	           canvas.drawRect(left, top,right,bottom, paint); 
 	           canvas.drawText(ctg, left+2, top-17, paint);
 	           canvas.drawText((double)Math.round(percent*1000)/10+"%", left+1,top-4, paint);
 	           Texpenses+=Float.parseFloat(records.get(i).getAmount());
     	   }
        }         
        //Show information
        TBalance=Tdeposit-Texpenses;
        tvDeposit.setText("$"+Tdeposit);
        tvExpense.setText("$"+Texpenses);
        tvBalance.setText("$"+TBalance);
        
      //  flChart.setBackgroundDrawable(new BitmapDrawable(bg)); 
        flChart.setBackground(new BitmapDrawable(bg));
    }
	



   
}
