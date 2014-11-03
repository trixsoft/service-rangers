package com.trixsoft.cityrangers.activity;

import com.trixsoft.cityrangers.activity.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class HomePage extends Activity {

	Button checkIN;
	Button issueList;

	/*
	 * Button checkOUT;
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_page);

		checkIN = (Button) findViewById(R.id.button1);
		issueList = (Button) findViewById(R.id.issueList);
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
				Intent i = new Intent(getApplicationContext(),
						IssueList.class);
				startActivity(i);
			}
		});

	}

}
