package com.trixsoft.cityrangers.activity;

import com.trixsoft.cityrangers.activity.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class HomePage extends Activity {

	Button checkIN;
	Button issueList;
	Button issueListCustom;

	/*
	 * Button checkOUT;
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_page);

		checkIN = (Button) findViewById(R.id.button1);
		issueList = (Button) findViewById(R.id.issueList);
		issueListCustom = (Button) findViewById(R.id.issueListCustom);
		/*
		 * checkOUT=(Button) findViewById(R.id.checkOUT);
		 */

		// Check IN Button click event
		checkIN.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// Launching CheckIN Activity
				Intent i = new Intent(getApplicationContext(),
						CheckINActivity.class);
				startActivity(i);
			}
		});

		// IssueList Button click event
		issueList.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// Launching IssueList Activity
				Intent i = new Intent(getApplicationContext(), IssueList.class);
				startActivity(i);
			}
		});

		// IssueListCustom Button click event
		issueListCustom.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// Launching IssueList Activity
				Log.d("HomePage", "Entered Onclick for IssueListCustom");
				Intent i = new Intent(getApplicationContext(), IssueListCustom.class);
				startActivity(i);
			}
		});

	}

}
