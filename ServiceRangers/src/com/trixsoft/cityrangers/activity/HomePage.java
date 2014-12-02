package com.trixsoft.cityrangers.activity;

import com.trixsoft.cityrangers.activity.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

public class HomePage extends Activity {

	ImageButton checkIn;
	ImageButton checkOut;
	ImageButton issues;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_home_page);

		checkIn = (ImageButton) findViewById(R.id.checkIn);
		checkOut = (ImageButton) findViewById(R.id.checkOut);
		issues = (ImageButton) findViewById(R.id.issues);

		// Check IN ImageButton click event
		checkIn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// Launching CheckIN Activity
				Intent i = new Intent(getApplicationContext(),
						CheckINActivity.class);
				startActivity(i);
			}
		});

		// Check Out ImageButton click event
		checkOut.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View view) {
				// Launching CheckIN Activity
				Intent i = new Intent(getApplicationContext(),
						CheckOUTActivity.class);
				startActivity(i);
			}
		});
		
		// IssueListCustom ImageButton click event
		issues.setOnClickListener(new View.OnClickListener() {

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
