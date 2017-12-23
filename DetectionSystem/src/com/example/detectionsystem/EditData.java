package com.example.detectionsystem;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

public class EditData extends Activity {

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
		final String round=this.getIntent().getStringExtra("round");
		
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
		
		MySQLite sql=new MySQLite(this);
		sql.openToRead();
		String[] ppm=sql.queue_Retrive_ppm(round).split("\\;");
		sql.close();
		
		final List<EditText> let=new ArrayList();
		
		for(int a=0;a<ppm.length;a++)
		{
			TableRow tr=new TableRow(this);
			TextView tv=new TextView(this);
			tv.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
			EditText et=new EditText(this);
			et.setInputType(InputType.TYPE_CLASS_NUMBER);
			tv.setText(Integer.toString(a));
			et.setText(ppm[a]);
			tr.addView(tv);
			tr.addView(et);
			let.add(et);
			Table.addView(tr);
		}
		
		Button submit=new Button(this);
		submit.setText("Submit");
		submit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				AlertDialog.Builder builder=new AlertDialog.Builder(EditData.this);
				builder.setMessage("Confirm?");
				
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						boolean flag=true;
						for(int a=0;a<let.size();a++)
						{
							EditText et=let.get(a);
							final int time=a;
							if(et.getText().toString().compareTo("")==0)
							{
								Toast.makeText(EditData.this, "Fill in the blank", Toast.LENGTH_LONG).show();
								flag=false;
								break;
							}
							MySQLite sql=new MySQLite(EditData.this);
							sql.openToWrite();
							sql.update_Retrive_ppm(round, Integer.toString(time), et.getText().toString());
							sql.close();
						}
						if(flag)
						{
							Toast.makeText(EditData.this, "Edit Successfully", Toast.LENGTH_LONG).show();
							finish();
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
		
		Button addmore=new Button(this);
		addmore.setText("Add More");
		addmore.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				TableRow trtmp=new TableRow(EditData.this);
				TextView tvtmp=new TextView(EditData.this);
				tvtmp.setText(Integer.toString(let.size()));
				tvtmp.setTypeface(Typeface.MONOSPACE,Typeface.BOLD);
				EditText ettmp=new EditText(EditData.this);
				trtmp.addView(tvtmp);
				trtmp.addView(ettmp);
				Table.addView(trtmp);
				let.add(ettmp);
				ettmp.setInputType(InputType.TYPE_CLASS_NUMBER);
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
			}	
		});
		
		llb.addView(addmore);
		llb.addView(remove);
		llb.addView(submit);
		ll.addView(llb);
	}
}
