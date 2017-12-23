//to show the history record
package com.example.detectionsystem;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class HistoryTable extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		
		ScrollView sv=new ScrollView(this);
		
		LinearLayout ll1=new LinearLayout(this);
		ll1.setOrientation(LinearLayout.VERTICAL);
		Button back=new Button(this);
		
		back.setText("BACK");
		back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				Intent i=new Intent(HistoryTable.this,MainActivity.class);
				startActivity(i);
				finish();
			}});
		
		TableLayout Table=new TableLayout(this);
		Table.setLayoutParams(new TableRow.LayoutParams
				(TableRow.LayoutParams.MATCH_PARENT,
						TableRow.LayoutParams.MATCH_PARENT));
		Table.setStretchAllColumns(true);
		sv.addView(Table);
		
		ll1.addView(back);
		ll1.addView(sv);

		MySQLite sql=new MySQLite(this);
		
		sql.openToRead();
		
		String[] tmpA=sql.queue_History100().split("\\;");
		
		if(tmpA==null)tmpA=sql.queue_History200().split("\\;");
		
		String[][] result=new String[tmpA.length][5];
		
		TextView[][] tv=new TextView[tmpA.length][5];
		TableRow[] tr=new TableRow[tmpA.length];
		
		for(int a=0;a<tmpA.length;a++)
		{
			tr[a]=new TableRow(this);
			result[a]=tmpA[a].split("\\,");
			for(int b=0;b<5;b++)
			{
				tv[a][b]=new TextView(this);
				tv[a][b].setGravity(Gravity.CENTER);
				tv[a][b].setText(result[a][b]);
				tr[a].addView(tv[a][b]);
			}
			Table.addView(tr[a]);
		}
		
		sql.close();
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(ll1);
	}	     
}
