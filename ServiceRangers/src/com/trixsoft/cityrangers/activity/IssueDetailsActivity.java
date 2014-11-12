package com.trixsoft.cityrangers.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.trixsoft.cityrangers.model.IssuesModel;

public class IssueDetailsActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Log.d("IssueDetail", "Enetered issue detail");
		setContentView(R.layout.issuedetails);
		
		Intent i = getIntent();
		IssuesModel issue = (IssuesModel)i.getSerializableExtra("issueDetails");
		Log.d("IssueDetail", "Issue Descritpion is :"+issue.getIS_DESC());
		
	}
	
	

}
