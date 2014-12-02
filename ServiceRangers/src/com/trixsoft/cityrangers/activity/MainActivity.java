package com.trixsoft.cityrangers.activity;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.trixsoft.cityrangers.model.WorkerModel;
import com.trixsoft.cityrangers.util.IConstants;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class MainActivity extends FragmentActivity {

	WorkerModel worker = new WorkerModel();
	// Creating JSON Parser object
	JSONParser jParser = new JSONParser();

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);

		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}

		// Call to get Worker details
		getWorkerDetails();

		// 4 designates ground worker. So only check in - checkout screens
		// 1 designates supervisor. So issue assignment and other screens
		if (worker.getAGNT_ROLE().equalsIgnoreCase("4")) {
			Intent i = new Intent(getApplicationContext(), HomePage.class);
			startActivity(i);
		} else if (worker.getAGNT_ROLE().equalsIgnoreCase("1")) {
			Intent i = new Intent(getApplicationContext(), HomePage.class);
			startActivity(i);
		} else {
			Intent i = new Intent(getApplicationContext(), HomePage.class);
			startActivity(i);
		}
	}

	private void getWorkerDetails() {

		// WorkerJSON Object
		JSONObject workerjson;
		JSONArray workerJSONArray;

		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("agnt_imei", "123456789012346"));

		try {

			workerJSONArray = jParser.makeHttpRequestforArray(
					IConstants.URL_GETWORKERDETAILS, "GET", params);
			
			workerjson=workerJSONArray.getJSONObject(0);

			if (null != workerjson) {
				worker.setAGNT_ID((String) workerjson.get("AGNT_ID"));
				worker.setAGNT_IMEI((String) workerjson.get("AGNT_IMEI"));
				worker.setAGNT_FNM((String) workerjson.get("AGNT_FNM"));
				worker.setAGNT_MNM((String) workerjson.get("AGNT_MNM"));
				worker.setAGNT_LNM((String) workerjson.get("AGNT_LNM"));
				worker.setAGNT_NUM((String) workerjson.get("AGNT_NUM"));
				worker.setAGNT_MNC((String) workerjson.get("AGNT_MNC"));
				worker.setAGNT_WARD((String) workerjson.get("AGNT_WARD"));
				worker.setAGNT_DEP((String) workerjson.get("AGNT_DEP"));
				worker.setAGNT_TYPE((String) workerjson.get("AGNT_TYPE"));
				worker.setAGNT_SUPR((String) workerjson.get("AGNT_SUPR"));
				worker.setAGNT_ROLE((String) workerjson.get("AGNT_ROLE"));

			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.d("MainActivity", "Exiting getWorkerDetails" + worker);

	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

}
