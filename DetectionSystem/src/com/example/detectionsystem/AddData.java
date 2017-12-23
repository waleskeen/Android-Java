package com.example.detectionsystem;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class AddData extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		LinearLayout ll=new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		LinearLayout llb=new LinearLayout(this);
		llb.setOrientation(LinearLayout.VERTICAL);
		final TableLayout Table=new TableLayout(this);
		Table.setLayoutParams(new TableRow.LayoutParams
				(TableRow.LayoutParams.MATCH_PARENT,
						TableRow.LayoutParams.MATCH_PARENT));
		Table.setStretchAllColumns(true);
		ScrollView sv=new ScrollView(this);
		ll.addView(Table);
		sv.addView(ll);
		setContentView(sv);
		
		TableRow RHeader=new TableRow(this);
		TextView[] Header=new TextView[2];
		Header[0]=new TextView(this);
		Header[0].setText("Minute");
		Header[0].setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
		Header[0].setTextSize(20);
		Header[1]=new TextView(this);
		Header[1].setText("ppm");
		Header[1].setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
		Header[1].setTextSize(20);
		RHeader.addView(Header[0]);
		RHeader.addView(Header[1]);
		Table.addView(RHeader);
		
		TableRow tr=new TableRow(this);
		TextView tv=new TextView(this);
		tv.setText("0");
		tv.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
		EditText et=new EditText(this);
		tr.addView(tv);
		tr.addView(et);
		Table.addView(tr);
		et.setInputType(InputType.TYPE_CLASS_NUMBER);
		
		final List<EditText> let=new ArrayList();
		let.add(et);
		
		Button addmore=new Button(this);
		addmore.setText("Add More");
		addmore.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				TableRow trtmp=new TableRow(AddData.this);
				TextView tvtmp=new TextView(AddData.this);
				tvtmp.setText(Integer.toString(let.size()));
				tvtmp.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
				EditText ettmp=new EditText(AddData.this);
				trtmp.addView(tvtmp);
				trtmp.addView(ettmp);
				Table.addView(trtmp);
				let.add(ettmp);
				ettmp.setInputType(InputType.TYPE_CLASS_NUMBER);
			}});
		
		Button submit=new Button(this);
		submit.setText("Submit");
		submit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				int round;
				MySQLite sqlR=new MySQLite(AddData.this);
				sqlR.openToRead();
				try{
					round=Integer.parseInt(sqlR.queue_Retrive_total_round())+1;
					sqlR.close();
				}
				catch(Exception e)
				{
					round=1;
					sqlR.close();
				}
				for(int a=0;a<let.size();a++)
				{
					if(let.get(a).getText().toString().compareTo("")==0)
					{
						let.remove(a);
						a--;
					}
					else if(let.get(a).getText().toString().compareTo("")!=0)
					{
						MySQLite sqlW=new MySQLite(AddData.this);
						sqlW.openToWrite();
						sqlW.insert_Retrive(Integer.toString(round), Integer.toString(a), 
								let.get(a).getText().toString());
						sqlW.close();
					}
				}
				Toast.makeText(AddData.this, "Add Successfully", Toast.LENGTH_LONG).show();
				finish();
			}
		});
		
		Button remove=new Button(this);
		remove.setText("Remove");
		remove.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(let.size()!=0)
				{
					Table.removeViewAt(let.size());
					let.remove(let.size()-1);
				}
			}});
		
		llb.addView(addmore);
		llb.addView(remove);
		llb.addView(submit);
		ll.addView(llb);
	}

}
