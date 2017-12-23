package com.example.detectionsystem;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class EditDatabase extends Activity {

	MySQLite SQL;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout ll=new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		
		Button add=new Button(this);
		add.setText("Add");
		add.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent i=new Intent(EditDatabase.this,AddData.class);
				startActivity(i);
			}});
		
		Button edit=new Button(this);
		edit.setText("Edit");
		edit.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				SQL=new MySQLite(EditDatabase.this);
				SQL.openToRead();
				String[] round=SQL.queue_Retrive_round().split("\\;");
				SQL.close();
				
				final ArrayAdapter<String> AA=new ArrayAdapter<String>
				(EditDatabase.this,android.R.layout.simple_expandable_list_item_1,round);
				AA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				final Spinner S_round=new Spinner(EditDatabase.this);
				S_round.setAdapter(AA);
				
				AlertDialog.Builder builder=new AlertDialog.Builder(EditDatabase.this);
				builder.setMessage("Please select the situation data that you want to edit:");
				builder.setView(S_round);
				
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent i=new Intent(EditDatabase.this,EditData.class);
						i.putExtra("round", S_round.getSelectedItem().toString());
						startActivity(i);
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
		
		Button delete=new Button(this);
		delete.setText("Delete");
		delete.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				SQL=new MySQLite(EditDatabase.this);
				SQL.openToRead();
				String[] round=SQL.queue_Retrive_round().split("\\;");
				SQL.close();
				
				final ArrayAdapter<String> AA=new ArrayAdapter<String>
				(EditDatabase.this,android.R.layout.simple_expandable_list_item_1,round);
				AA.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				final Spinner S_round=new Spinner(EditDatabase.this);
				S_round.setAdapter(AA);
				
				AlertDialog.Builder builder=new AlertDialog.Builder(EditDatabase.this);
				builder.setMessage("Please select the situation data that you want to delete:");
				builder.setView(S_round);
				
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						AlertDialog.Builder builder1=new AlertDialog.Builder(EditDatabase.this);
						builder1.setMessage("Are you sure want to delete:");
						

						builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {
								SQL=new MySQLite(EditDatabase.this);
								SQL.openToWrite();
								SQL.delete_Retrive(S_round.getSelectedItem().toString());
								SQL.close();
								Toast.makeText(EditDatabase.this,"Delete Successfully",Toast.LENGTH_LONG).show();
								SQL=new MySQLite(EditDatabase.this);
								SQL.openToWrite();
								for(int a=1;a<=S_round.getCount()-S_round.getSelectedItemPosition();a++)
									SQL.update_Retrive_round(Integer.toString(S_round.getSelectedItemPosition()+a),
											Integer.toString(S_round.getSelectedItemPosition()+a-1));
								SQL.close();
							}
						});
						
						builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
							
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.cancel();
							}
						});
						
						AlertDialog dialog1=builder1.create();
						dialog1.show();
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
		
		ll.addView(add);
		ll.addView(edit);
		ll.addView(delete);
		setContentView(ll);
	}
}
