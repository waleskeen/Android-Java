package com.example.detectionsystem;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.afree.data.xy.XYSeries;
import org.afree.graphics.SolidColor;
import org.afree.graphics.geom.Font;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Gravity;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity{
	
	MediaPlayer mediaPlayer;
	ChartView chart;
	XYSeries series;
	int cnt=61;
	LinearLayout ll;
	TextView desc;
	TextView Tppm;
	Handler mhandler;
	String[][] S_INFO;
	String[][] S_suggestion;
	TextView info;
	TextView suggestion;
	volatile boolean flag[]=new boolean[2];
    volatile boolean flag2=true;
    MySQLite sql;
    GPSTracker GPS;
    SendThread ST;
    boolean httpflag;
    ReceiveThread RT;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		httpflag=true;
		GPS=new GPSTracker(this);
		
		setData();
		
		mhandler=new Handler();
		ll=new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		ll.setBackgroundColor(Color.GREEN);
		
		desc=new TextView(this);
		desc.setText("SAFE");
		desc.setGravity(Gravity.CENTER);
		desc.setTextSize(30);
		
		info=new TextView(this);
		info.setText("GOOD CONDITION");
		info.setGravity(Gravity.CENTER);
		info.setTextSize(15);
		
		suggestion=new TextView(this);
		suggestion.setText("It is safe.");
		suggestion.setGravity(Gravity.CENTER);
		suggestion.setTextSize(15);
		
		Tppm=new TextView(this);
		Tppm.setText("0 ppm");
		Tppm.setGravity(Gravity.CENTER);
		Tppm.setTextSize(30);
		Tppm.setId(1);
		this.registerForContextMenu(Tppm);
		
		Button history=new Button(this);
		history.setText("History Record");
		history.setHeight(this.getWindowManager().getDefaultDisplay().getHeight()/10);
		
		chart=new ChartView(this);
		chart.getChart().setBackgroundPaintType(new SolidColor(Color.GREEN));
        chart.getChart().getPlot().setBackgroundPaintType(new SolidColor(Color.GREEN));
        chart.getChart().getXYPlot().getRangeAxis().setTickLabelFont(new Font("",Typeface.NORMAL,this.getWindowManager().getDefaultDisplay().getWidth()/30));
        chart.getChart().getTitle().setFont(new Font("",Typeface.NORMAL,this.getWindowManager().getDefaultDisplay().getWidth()/20));
        chart.getChart().getXYPlot().getRangeAxis().setLabelFont(new Font("",Typeface.NORMAL,this.getWindowManager().getDefaultDisplay().getWidth()/25));
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		
		//to exit the application
		Button exit=new Button(this);
		exit.setText("EXIT");
		exit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
				
				builder.setMessage("Do you want to exit?");
				
				builder.setPositiveButton("YES", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface dialog, int which) {
						mediaPlayer.stop();
						mhandler.removeCallbacksAndMessages(null);
						httpflag=false;						
						MainActivity.this.finish();
					}});
				
				builder.setNegativeButton("NO", new DialogInterface.OnClickListener(){
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();						
					}});
				
				AlertDialog dialog=builder.create();
				dialog.show();
			}});
		
		//to set the symptoms
		S_INFO=new String[][]
		{{"35", "Headache and dizziness within 6-8hours of constant exposure"},
		{"100", "Slightly headache in 2-3 hours"},
		{"200", "Slightly headache within 2-3 hours; and loss of judgment"},
		{"400", "Frontal headache within 1-2 hours"},
		{"800", "Dizziness, nausea, and convulsion within 45 minutes; and insensible within 2hours"},
		{"1600", "Headache, tachycardia, dizziness, and nausea within 20 minutes; and death less than 2 hours"},
		{"3200", "Headache, dizziness, and nausea in 5-10 minutes; and death within 30 minutes"},
		{"6400", "Headache, and dizziness in 1-2 minutes; and convulsions, respiratory arrest, and death in less than 20 minutes"},
		{"12800", "Unconsciousness after 2-3 breaths, and death in less than 3 minutes"}};
		
		Button editdata=new Button(this);
		
		editdata.setText("Edit Data");
		editdata.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent i=new Intent(MainActivity.this,EditDatabase.class);
				startActivity(i);
			}});
		
		Button rundata=new Button(this);
		rundata.setText("Run Simulation");
		rundata.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				MySQLite SQL=new MySQLite(MainActivity.this);
				SQL.openToRead();
				String[] round=SQL.queue_Retrive_round().split("\\;");
				SQL.close();
				
				final ArrayAdapter<String> AA=new ArrayAdapter<String>
				(MainActivity.this,android.R.layout.simple_expandable_list_item_1,round);
				AA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				final Spinner S_round=new Spinner(MainActivity.this);
				S_round.setAdapter(AA);
				
				AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
				builder.setMessage("Please select the situation that you want to run:");
				builder.setView(S_round);
				
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						MySQLite SQL2=new MySQLite(MainActivity.this);
						SQL2.openToRead();
						final String[] Rppm=SQL2.queue_Retrive_ppm(S_round.getSelectedItem().toString()).split("\\;");
						SQL2.close();
						
						mhandler.removeCallbacksAndMessages(null);
						httpflag=false;
						for(int a=0;a<Rppm.length;a++)
						{
							final int cnt1=a;
							mhandler.postDelayed(new Runnable(){

								@Override
								public void run() {
									detect(Double.parseDouble(Rppm[cnt1]));
								}},cnt1*1000);
						}
					}
				});
				
				builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();						
					}
				});
				
				AlertDialog dialog=builder.create();
				dialog.show();
			}});
		
		ll.addView(exit);
		ll.addView(editdata);
		ll.addView(rundata);
		ll.addView(desc);
		ll.addView(info);
		ll.addView(suggestion);
		ll.addView(Tppm);
		ll.addView(history);
		ll.addView(chart);
		setContentView(ll);
		
		//to check the history record
		history.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent i=new Intent(MainActivity.this,HistoryTable.class);
				httpflag=false;
				mhandler.removeCallbacksAndMessages(null);
				mediaPlayer.stop();
				startActivity(i);
				ChartView.createDataset();
				finish();
			}});
		
		 mediaPlayer=MediaPlayer.create(MainActivity.this, R.raw.factory_alarm_sound);

		 //to get data from another computer
		 ST=new SendThread();
		 ST.start();
	}
	
	//to perform the detection
	public void detect(double ppm)
	{
		final double chartppm=ppm;
		runOnUiThread(new Runnable()
    	{
			@Override
			public void run() {
				series=chart.getSeries();
				series.remove(0);
		        series.add(cnt,chartppm);
		        cnt++;
			}
    	});
        
		//if the ppm is more then 800
        if(ppm>=Double.parseDouble(S_INFO[4][0]))
        {        	
        	flag[1]=false;
        	flag[0]=true;
        	
        	runOnUiThread(new Runnable()
        	{
        		//turn to red color
				@Override
				public void run() {
					ll.setBackgroundColor(Color.RED);
		            chart.setBackgroundColor(Color.RED);
		            chart.getChart().setBackgroundPaintType(new SolidColor(Color.RED));
		            chart.getChart().getPlot().setBackgroundPaintType(new SolidColor(Color.RED));
				}
        	});
        	
        	Thread t1=new Thread(new Runnable()
            {
        		//perform colour blinking
                @Override
                public void run()
                {
                    while (flag[0])
                    {
                        try
                        {
                            runOnUiThread(new Runnable() // start actions in UI thread
                            {

                                @Override
                                public void run()
                                {
                                    if(flag2)
                                    {
                                    	desc.setTextColor(Color.BLACK);
                                    	flag2=false;
                                    }
                                    else
                                    {
                                    	desc.setTextColor(Color.WHITE);
                                    	flag2=true;
                                    }
                                }
                            });
                            Thread.sleep(1000);
                        }
                        catch (InterruptedException e)
                        {
                        	
                        }
                    }
                }
            });
        	t1.start();
        	runOnUiThread(new Runnable()
       	 	{

				@Override
				public void run() {
					desc.setText("DANGEROUS");
				}
       	 	});
            
        	//to show the symptoms
            if(ppm>=Double.parseDouble(S_INFO[8][0]))
            {
            	runOnUiThread(new Runnable()
           	 	{

    				@Override
    				public void run() {
    					info.setText(S_INFO[8][1]);
    				}
           	 	});
            }
            
            else if(ppm>=Double.parseDouble(S_INFO[7][0]))
            {
            	runOnUiThread(new Runnable()
           	 	{

    				@Override
    				public void run() {
    					info.setText(S_INFO[7][1]);
    				}
           	 	});
            }
            
            else if(ppm>=Double.parseDouble(S_INFO[6][0]))
            {
            	runOnUiThread(new Runnable()
           	 	{

    				@Override
    				public void run() {
    					info.setText(S_INFO[6][1]);
    				}
           	 	});
            }
            
            else if(ppm>=Double.parseDouble(S_INFO[6][0]))
            {
            	runOnUiThread(new Runnable()
           	 	{

    				@Override
    				public void run() {
    					info.setText(S_INFO[6][1]);
    				}
           	 	});
            }
            
            else if(ppm>=Double.parseDouble(S_INFO[5][0]))
            {
            	runOnUiThread(new Runnable()
           	 	{

    				@Override
    				public void run() {
    					info.setText(S_INFO[5][1]);
    				}
           	 	});
            }
            
            else
            {
            	runOnUiThread(new Runnable()
           	 	{

    				@Override
    				public void run() {
    					info.setText(S_INFO[4][1]);
    				}
           	 	});
            }
            
            runOnUiThread(new Runnable()
       	 	{

				@Override
				public void run() {
					suggestion.setText("You need to quit from the place as fast as possible.");
				}
       	 	});

            mediaPlayer.stop();
            mediaPlayer=MediaPlayer.create(MainActivity.this, R.raw.factory_alarm_sound);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
        
        //if the ppm is more then 35
        else if(ppm>=Double.parseDouble(S_INFO[0][0]))
        {
        	flag[0]=false;
        	flag[1]=true;
        	
        	 runOnUiThread(new Runnable()
        	 {
        		//change to yellow colour
				@Override
				public void run() {
					ll.setBackgroundColor(Color.YELLOW);
		            chart.setBackgroundColor(Color.YELLOW);
		            chart.getChart().setBackgroundPaintType(new SolidColor(Color.YELLOW));
		            chart.getChart().getPlot().setBackgroundPaintType(new SolidColor(Color.YELLOW));
				}
        		 
        	 });
        	
        	Thread t1=new Thread(new Runnable()
            {
        		//perform colour blinking
                @Override
                public void run()
                {
                    while (flag[1])
                    {
                        try
                        {
                            runOnUiThread(new Runnable() // start actions in UI thread
                            {

                                @Override
                                public void run()
                                {
                                    if(flag2)
                                    {
                                    	desc.setTextColor(Color.BLACK);
                                    	flag2=false;
                                    }
                                    else
                                    {
                                    	desc.setTextColor(Color.WHITE);
                                    	flag2=true;
                                    }
                                }
                            });
                            Thread.sleep(1000);
                        }
                        catch (InterruptedException e)
                        {

                        }
                    }
                }
            });
        	t1.start();
        	
        	runOnUiThread(new Runnable()
       	 	{
				@Override
				public void run() {
		            desc.setText("CAUTION");
				}
       	 	});
		        
        	//to show the symptoms
		    if(ppm>=Double.parseDouble(S_INFO[3][0]))
		    {
		    	runOnUiThread(new Runnable()
	       	 	{
					@Override
					public void run() {
						info.setText(S_INFO[3][1]);
					}
	       	 	});
		    }
		    
		    else if(ppm>=Double.parseDouble(S_INFO[2][0]))
		    {
		    	runOnUiThread(new Runnable()
	       	 	{
					@Override
					public void run() {
						info.setText(S_INFO[2][1]);					
					}
	       	 	});
		    }
		            
		    else if(ppm>=Double.parseDouble(S_INFO[1][0]))
		    {
		    	runOnUiThread(new Runnable()
	       	 	{
					@Override
					public void run() {
						info.setText(S_INFO[1][1]);
					}
	       	 	});
		    }
		            
		    else
		    {
		    	runOnUiThread(new Runnable()
	       	 	{
					@Override
					public void run() {
						info.setText(S_INFO[0][1]);
					}
	       	 	});
		    }
	        runOnUiThread(new Runnable()
	       	{
				@Override
				public void run() {
					suggestion.setText("You need to quit from the place as fast as possible.");
				}
       	 	});
            mediaPlayer.stop();
        }
        
        //if the ppm is less than 35
        else if(ppm>=0)
        {
        	flag[0]=false;
        	flag[1]=false;
        	
        	runOnUiThread(new Runnable()
        	{
        		//change to green colour
				@Override
				public void run() {
					ll.setBackgroundColor(Color.GREEN);
		        	chart.setBackgroundColor(Color.GREEN);
		        	chart.getChart().setBackgroundPaintType(new SolidColor(Color.GREEN));
		            chart.getChart().getPlot().setBackgroundPaintType(new SolidColor(Color.GREEN));
				}
        		
        	});
            
            Thread t1=new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    try
                    {
                    	Thread.sleep(1000);
                      	runOnUiThread(new Runnable() // start actions in UI thread
                        {
                            @Override
                            public void run()
                            {
                            	desc.setTextColor(Color.BLACK);
                            }
                        });
                    }
                    catch (InterruptedException e)
                    {
                    	
                    }
                }
            });
            t1.start();
            runOnUiThread(new Runnable()
       	 	{

				@Override
				public void run() {
		            desc.setText("SAFE");
		            info.setText("GOOD CONDITION");
		            suggestion.setText("It is safe.");
				}
       	 	});
            mediaPlayer.stop();
        }
        
        //to show the ppm
        if(ppm>=0)
        {
        	final double GUIppm=ppm;
        	runOnUiThread(new Runnable()
       	 	{
				@Override
				public void run() {
					if(Double.parseDouble(String.valueOf(GUIppm))==Double.parseDouble(String.valueOf(GUIppm).split("\\.")[0]))
		                Tppm.setText(String.valueOf(GUIppm).split("\\.")[0]+" ppm");
		            else
		                Tppm.setText(String.valueOf(GUIppm)+" ppm"); 
				}
       	 	});
        	
        	//to store data into history record
            String timeStamp = new SimpleDateFormat("yyyy/MM/dd,HH:mm:ss").format(Calendar.getInstance().getTime());
            try
            {
                sql=new MySQLite(this);
                sql.openToWrite();
                sql.insert_History(timeStamp.split("\\,")[1],timeStamp.split("\\,")[0],String.valueOf(ppm),Double.toString(GPS.getLatitude()),Double.toString(GPS.getLongitude()));
                sql.close();
                if(ppm>=800)
                {
	                RT=new ReceiveThread(timeStamp.split("\\,")[1],timeStamp.split("\\,")[0],String.valueOf(ppm),Double.toString(GPS.getLatitude()),Double.toString(GPS.getLongitude()));
	                RT.start();
                }
            }
            catch(Exception ex)
            {
                
            }
        }
        //when ppm is less than 0
        else
        {
            ppm=Double.parseDouble(Tppm.getText().toString().replace(" ppm", ""));
        }
	}
	
	//key in the ppm manually
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo){
		super.onCreateContextMenu(menu,v,menuInfo);
		if(v.getId() == 1)
		{
			final EditText ppm=new EditText(MainActivity.this);
			ppm.setInputType(InputType.TYPE_CLASS_NUMBER);
					
			AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
			builder.setMessage("Please type in the ppm:");
			builder.setView(ppm);
					
			builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
						
				@Override
				public void onClick(DialogInterface dialog, int which) {
					try
					{
						mhandler.removeCallbacksAndMessages(null);
						httpflag=false;
						detect(Double.parseDouble(ppm.getText().toString()));
					}
					catch(NumberFormatException e)
					{
						Toast.makeText(MainActivity.this, "key in number only", Toast.LENGTH_LONG).show();
						dialog.cancel();
					}
				}
			});
					
			builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
						
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();						
				}
			});

			AlertDialog dialog=builder.create();
			dialog.show();
		}
		if(v.getId() == 2)
		{
			Toast.makeText(MainActivity.this, "LOL", Toast.LENGTH_LONG).show();
		}
	}
	
	//to receive data from another computer
	private class SendThread extends Thread
	{		
		public void run()
		{
			do
			{
				try{
					URL url=new URL("http://192.168.137.1:8080/Serlvet/SendData");
					HttpURLConnection hc=(HttpURLConnection)url.openConnection();
							
					int length=hc.getContentLength();
						
					InputStream input = hc.getInputStream();
					byte[] binput = new byte[length];
					int readSize = input.read(binput);
								
					String htmlppm=new String(binput).trim();
								
					mhandler.removeCallbacksAndMessages(null);
					detect(Double.parseDouble(htmlppm));
					input.close();
								
					Thread.sleep(1000);
				}
				catch(Exception e)
				{
					Log.e("Net","Error",e);
				}
			}while(httpflag);
		}
	}
	
	//to send data to the server to let them know the dangerous situation
	private class ReceiveThread extends Thread
	{		
		String Time;
		String Date;
		String httpppm;
		String Latitude;
		String Longtitude;
		
		public ReceiveThread(String t, String d, String p, String lat, String lng)
		{
			Time=t;
			Date=d;
			httpppm=p;
			Latitude=lat;
			Longtitude=lng;
		}
		public void run()
		{
			try{
				String parameters ="Time="+Time+"&Date="+Date+"&ppm="+httpppm+
						"&Latitude="+Latitude+"&Longtitude="+Longtitude;
				URL url=new URL("http://192.168.137.1:8080/Serlvet/ReceiveData");
				HttpURLConnection hc=(HttpURLConnection)url.openConnection();
				hc.setDoOutput(true);
					
				OutputStream output = hc.getOutputStream();
				output.write(parameters.getBytes());
				output.flush();
				
				int length=hc.getContentLength();
						
				InputStream input = hc.getInputStream();
				byte[] binput = new byte[length];
				int readSize = input.read(binput);
								
				input.close();
			}
			catch(Exception e)
			{
				Log.e("Net","Error",e);
			}
		}
	}
	
	public void setData()
	{
		sql=new MySQLite(this);
		sql.openToRead();
		
		if(sql.queue_Retrive_total_round().compareTo("")==0)
		{
			sql.close();
			sql=new MySQLite(this);
			sql.openToWrite();
			sql.insert_Retrive("1","0","0");
			sql.insert_Retrive("1","1","12");
			sql.insert_Retrive("1","2","25");
			sql.insert_Retrive("1","3","35");
			sql.insert_Retrive("1","4","43");
			sql.insert_Retrive("1","5","45");
			sql.insert_Retrive("1","6","46");
			sql.insert_Retrive("1","7","47");
			sql.insert_Retrive("1","8","48");
			sql.insert_Retrive("1","9","48");
			sql.insert_Retrive("1","10","49");
			sql.insert_Retrive("1","11","49");
			sql.insert_Retrive("1","12","48");
			sql.insert_Retrive("1","13","47");
			sql.insert_Retrive("1","14","47");
			sql.insert_Retrive("1","15","48");
			sql.insert_Retrive("1","16","48");
			sql.insert_Retrive("1","17","48");
			sql.insert_Retrive("1","18","47");
			sql.insert_Retrive("1","19","47");
			sql.insert_Retrive("1","20","47");
			sql.insert_Retrive("1","21","47");
			sql.insert_Retrive("1","22","48");
			sql.insert_Retrive("1","23","48");
			sql.insert_Retrive("1","24","48");
			sql.insert_Retrive("1","25","48");
			sql.insert_Retrive("1","26","49");
			sql.insert_Retrive("1","27","50");
			sql.insert_Retrive("1","28","51");
			sql.insert_Retrive("1","29","50");
			sql.insert_Retrive("1","30","49");
			sql.insert_Retrive("1","31","50");
			sql.insert_Retrive("1","32","50");
			sql.insert_Retrive("1","33","49");
			sql.insert_Retrive("1","34","48");
			sql.insert_Retrive("1","35","48");
			sql.insert_Retrive("1","36","48");
			sql.insert_Retrive("1","37","48");
			sql.insert_Retrive("1","38","48");
			sql.insert_Retrive("1","39","48");
			sql.insert_Retrive("1","40","49");
			sql.insert_Retrive("1","41","52");
			sql.insert_Retrive("1","42","55");
			sql.insert_Retrive("1","43","56");
			sql.insert_Retrive("1","44","58");
			sql.insert_Retrive("1","45","58");
			sql.insert_Retrive("1","46","57");
			sql.insert_Retrive("1","47","57");
			sql.insert_Retrive("1","48","59");
			sql.insert_Retrive("1","49","62");
			sql.insert_Retrive("1","50","63");
			sql.insert_Retrive("1","51","67");
			sql.insert_Retrive("1","52","67");
			sql.insert_Retrive("1","53","69");
			sql.insert_Retrive("1","54","71");
			sql.insert_Retrive("1","55","74");
			sql.insert_Retrive("1","56","77");
			sql.insert_Retrive("1","57","78");
			sql.insert_Retrive("1","58","79");
			sql.insert_Retrive("1","59","80");	
			sql.insert_Retrive("1","60","81");
			
			sql.insert_Retrive("2","0","0");
			sql.insert_Retrive("2","1","0");
			sql.insert_Retrive("2","2","1");
			sql.insert_Retrive("2","3","1");
			sql.insert_Retrive("2","4","1");
			sql.insert_Retrive("2","5","1");
			sql.insert_Retrive("2","6","1");
			sql.insert_Retrive("2","7","1");
			sql.insert_Retrive("2","8","1");
			sql.insert_Retrive("2","9","1");
			sql.insert_Retrive("2","10","1");
			sql.insert_Retrive("2","11","1");
			sql.insert_Retrive("2","12","1");
			sql.insert_Retrive("2","13","1");
			sql.insert_Retrive("2","14","1");
			sql.insert_Retrive("2","15","2");
			sql.insert_Retrive("2","16","3");
			sql.insert_Retrive("2","17","2");
			sql.insert_Retrive("2","18","1");
			sql.insert_Retrive("2","19","1");
			sql.insert_Retrive("2","20","1");
			sql.insert_Retrive("2","21","1");
			sql.insert_Retrive("2","22","1");
			sql.insert_Retrive("2","23","1");
			sql.insert_Retrive("2","24","1");
			sql.insert_Retrive("2","25","1");
			sql.insert_Retrive("2","26","1");
			sql.insert_Retrive("2","27","1");
			sql.insert_Retrive("2","28","1");
			sql.insert_Retrive("2","29","1");
			sql.insert_Retrive("2","30","1");
			sql.insert_Retrive("2","31","1");
			sql.insert_Retrive("2","32","1");
			sql.insert_Retrive("2","33","1");
			sql.insert_Retrive("2","34","1");
			sql.insert_Retrive("2","35","1");
			sql.insert_Retrive("2","36","1");
			sql.insert_Retrive("2","37","1");
			sql.insert_Retrive("2","38","1");
			sql.insert_Retrive("2","39","1");
			sql.insert_Retrive("2","40","1");
			sql.insert_Retrive("2","41","1");
			sql.insert_Retrive("2","42","1");
			sql.insert_Retrive("2","43","1");
			sql.insert_Retrive("2","44","1");
			sql.insert_Retrive("2","45","1");
			sql.insert_Retrive("2","46","2");
			sql.insert_Retrive("2","47","1");
			sql.insert_Retrive("2","48","1");
			sql.insert_Retrive("2","49","1");
			sql.insert_Retrive("2","50","1");
			sql.insert_Retrive("2","51","1");
			sql.insert_Retrive("2","52","1");
			sql.insert_Retrive("2","53","1");
			sql.insert_Retrive("2","54","1");
			sql.insert_Retrive("2","55","1");
			sql.insert_Retrive("2","56","1");
			sql.insert_Retrive("2","57","1");
			sql.insert_Retrive("2","58","1");
			sql.insert_Retrive("2","59","1");	
			sql.insert_Retrive("2","60","1");
			
			sql.insert_Retrive("3","0","0");
			sql.insert_Retrive("3","1","1");
			sql.insert_Retrive("3","2","1");
			sql.insert_Retrive("3","3","1");
			sql.insert_Retrive("3","4","2");
			sql.insert_Retrive("3","5","2");
			sql.insert_Retrive("3","6","3");
			sql.insert_Retrive("3","7","4");
			sql.insert_Retrive("3","8","5");
			sql.insert_Retrive("3","9","6");
			sql.insert_Retrive("3","10","7");
			sql.insert_Retrive("3","11","7");
			sql.insert_Retrive("3","12","8");
			sql.insert_Retrive("3","13","9");
			sql.insert_Retrive("3","14","9");
			sql.insert_Retrive("3","15","9");
			sql.insert_Retrive("3","16","10");
			sql.insert_Retrive("3","17","10");
			sql.insert_Retrive("3","18","11");
			sql.insert_Retrive("3","19","12");
			sql.insert_Retrive("3","20","12");
			sql.insert_Retrive("3","21","12");
			sql.insert_Retrive("3","22","13");
			sql.insert_Retrive("3","23","13");
			sql.insert_Retrive("3","24","14");
			sql.insert_Retrive("3","25","14");
			sql.insert_Retrive("3","26","15");
			sql.insert_Retrive("3","27","15");
			sql.insert_Retrive("3","28","15");
			sql.insert_Retrive("3","29","14");
			sql.insert_Retrive("3","30","13");
			sql.insert_Retrive("3","31","13");
			sql.insert_Retrive("3","32","13");
			sql.insert_Retrive("3","33","13");
			sql.insert_Retrive("3","34","13");
			sql.insert_Retrive("3","35","13");
			sql.insert_Retrive("3","36","13");
			sql.insert_Retrive("3","37","12");
			sql.insert_Retrive("3","38","12");
			sql.insert_Retrive("3","39","12");
			sql.insert_Retrive("3","40","13");
			sql.insert_Retrive("3","41","13");
			sql.insert_Retrive("3","42","13");
			sql.insert_Retrive("3","43","13");
			sql.insert_Retrive("3","44","13");
			sql.insert_Retrive("3","45","13");
			sql.insert_Retrive("3","46","13");
			sql.insert_Retrive("3","47","13");
			sql.insert_Retrive("3","48","12");
			sql.insert_Retrive("3","49","10");
			sql.insert_Retrive("3","50","10");
			sql.insert_Retrive("3","51","10");
			sql.insert_Retrive("3","52","10");
			sql.insert_Retrive("3","53","10");
			sql.insert_Retrive("3","54","10");
			sql.insert_Retrive("3","55","10");
			sql.insert_Retrive("3","56","10");
			sql.insert_Retrive("3","57","10");
			sql.insert_Retrive("3","58","10");
			sql.insert_Retrive("3","59","10");	
			sql.insert_Retrive("3","60","10");
		}
		sql.close();
	}
}
