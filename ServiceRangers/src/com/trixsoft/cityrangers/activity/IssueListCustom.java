package com.trixsoft.cityrangers.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.trixsoft.cityrangers.adapters.IssueListAdapter;
import com.trixsoft.cityrangers.model.IssuesModel;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

public class IssueListCustom extends Activity {

	public IssueListCustom customIssueListView = null;
	public ArrayList<IssuesModel> customListValuesArray = new ArrayList<IssuesModel>();
	public ListView listview;
	public IssueListAdapter issueListAdapter;

	// url to get all products list
	private static String url_all_issues = "ACTUAL SITE URL WILL BE CONFIGURED HERE";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_ISSUES = "issues";
	private static final String TAG_PID = "pid";
	private static final String TAG_ISSUE = "issue";

	// Issues JSONArray
	JSONArray issuesArray = null;

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();


	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_issue_list_custom);
		
		Log.d("ISSUELISTCUSTOM", "Entered onCreateMethod");

		customIssueListView = this;

		/******** Take data in Arraylist ***********/
		setListData();

		Resources res = getResources();
		listview = (ListView) findViewById(R.id.list); 														

		
		Log.d("IssueListCustom-onCreate", "Issues List Size is:"+customListValuesArray.size());
		/**************** Create Custom Adapter *********/
		issueListAdapter = new IssueListAdapter(customIssueListView,
				customListValuesArray, res);
		listview.setAdapter(issueListAdapter);

	}

	private void setListData() {
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		
		Log.d("ISSUELISTCUSTOM", "Entered SetListData");
		// getting JSON string from URL
		JSONObject json; // jParser.makeHttpRequest(url_all_issues, "GET",
							// params);
		json = jParser.makeHttpRequestforIssues();
		

		// Check your log cat for JSON reponse
		Log.d("All Products: ", json.toString());
		IssuesModel issueModel;

		try {
			// Checking for SUCCESS TAG
			int success = json.getInt(TAG_SUCCESS);

			if (success == 1) {
				// issues Found ,Getting Array of Issues
				issuesArray = json.getJSONArray(TAG_ISSUES);

				// looping through All issues
				for (int i = 0; i < issuesArray.length(); i++) {

					// For holding issue related details
					issueModel = new IssuesModel();

					JSONObject c = issuesArray.getJSONObject(i);

					// Storing each json item in variable
					issueModel.setIssueId(c.getString(TAG_PID));
					issueModel.setIssueDesc(c.getString(TAG_ISSUE));
					issueModel.setWardNumber(c.getString("place"));

					// creating new HashMap
					HashMap<String, String> map = new HashMap<String, String>();
					
					

					/******** Take Model Object in ArrayList **********/
					customListValuesArray.add(issueModel);					
				}				
			} else {

			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/***************** This function used by adapter ****************/
	public void onItemClick(int mPosition) {
		IssuesModel tempValues = (IssuesModel) customListValuesArray
				.get(mPosition);
		// SHOW ALERT
		Toast.makeText(
				customIssueListView,
				"Issue Desc:" + tempValues.getIssueDesc() + "ward Number:"
						+ tempValues.getWardNumber() + "IssueId:"
						+ tempValues.getIssueId(), Toast.LENGTH_LONG).show();
	}
}
