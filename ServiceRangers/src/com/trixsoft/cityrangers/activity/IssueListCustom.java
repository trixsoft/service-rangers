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
import com.trixsoft.cityrangers.util.IssueListParser;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

public class IssueListCustom extends Activity {

	public IssueListCustom customIssueListView = null;
	public ArrayList<IssuesModel> customListValuesArray = new ArrayList<IssuesModel>();
	public ListView listview;
	public IssueListAdapter issueListAdapter;

	// url to get all products list
	private static String url_all_issues = "http://nelloremayor.org/administration/api/getIssues.php?agnt_id=1212";

	// JSON Node names
	private static final String TAG_SUCCESS = "success";
	private static final String TAG_ISSUES = "issues";
	private static final String TAG_PID = "pid";
	private static final String TAG_ISSUE = "issue";

	// Issues JSONArray
	JSONArray issuesArray = null;

	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();

	IssueListParser issueListParser = new IssueListParser();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_issue_list_custom);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		Log.d("ISSUELISTCUSTOM", "Entered onCreateMethod");

		customIssueListView = this;

		/******** Take data in Arraylist ***********/
		setListData();

		Resources res = getResources();
		listview = (ListView) findViewById(R.id.list01);

		Log.d("IssueListCustom-onCreate", "Issues List Size is:"
				+ customListValuesArray.size());
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
		JSONObject json;
		JSONArray jsonArray;

		jsonArray = issueListParser.makeHttpRequest(url_all_issues, "GET",
				params);

		// THIS BELOW SNIPPET IS FOR LOCAL TESTING OF JSON PARSING
		// jsonArray = jParser.makeHttpRequestforIssues();

		IssuesModel issueModel;

		try {
			// Checking for SUCCESS TAG
			int success = 1;// json.getInt(TAG_SUCCESS);

			if (success == 1) {
				// issues Found ,Getting Array of Issues
				// issuesArray = json.getJSONArray(TAG_ISSUES);

				// looping through All issues
				for (int i = 0; i < jsonArray.length(); i++) {

					// For holding issue related details
					issueModel = new IssuesModel();

					JSONObject c = jsonArray.getJSONObject(i);

					// Storing each json item in variable
					issueModel.setIS_ID(c.getString("IS_ID"));
					issueModel.setIS_DESC(c.getString("IS_DESC"));
					issueModel.setIS_MAP_LOC(c.getString("IS_MAP_LOC"));
					issueModel.setIS_ALLOC_TO(c.getString("IS_ALLOC_TO"));
					issueModel.setIS_STATUS(c.getString("IS_STATUS"));
					issueModel.setIS_LOGGED_BY(c.getString("IS_LOGGED_BY"));
					issueModel.setIS_LOGGED_TS(c.getString("IS_LOGGED_TS"));
					issueModel.setIS_ACCEPTED_TS(c.getString("IS_ACCEPTED_TS"));
					issueModel.setIS_RESOLVED_TS(c.getString("IS_RESOLVED_TS"));
					issueModel.setIS_TITLE(c.getString("IS_TITLE"));
					issueModel.setIS_LOC(c.getString("IS_LOC"));

					// creating new HashMap
					HashMap<String, String> map = new HashMap<String, String>();

					/******** Take Model Object in ArrayList **********/
					customListValuesArray.add(issueModel);
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	/***************** This function used by adapter ****************/
	public void onItemClick(int mPosition) {
		IssuesModel issueDetail = (IssuesModel) customListValuesArray
				.get(mPosition);
		// SHOW ALERT
		/*Toast.makeText(
				customIssueListView,
				"Issue Desc:" + tempValues.getIS_DESC() + "ward Number:"
						+ tempValues.getIS_LOC() + "IssueId:"
						+ tempValues.getIS_ID(), Toast.LENGTH_LONG).show();*/
		
		Intent in = new Intent(getApplicationContext(),
                IssueDetailsActivity.class);
		in.putExtra("issueDetails", issueDetail);
		
		// starting new activity and expecting some response back
		startActivityForResult(in, 100);
		
	}
	
	
}
