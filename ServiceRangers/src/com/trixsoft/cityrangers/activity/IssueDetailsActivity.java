package com.trixsoft.cityrangers.activity;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.trixsoft.cityrangers.model.IssuesModel;

public class IssueDetailsActivity extends Activity {
	
	private TextView issueId;
	private TextView issueTitle;
	private TextView issueDesc;
	private TextView issueLoc;
	private TextView issuemaploc;
	private TextView issueAllocTo;
	private TextView issueStatus;
	private TextView issueLoggedBy;
	private TextView issueAcceptedTS;
	private TextView issueResolvedTS;
	
	private GoogleMap map;
	
	static final LatLng HAMBURG = new LatLng(53.558, 9.927);
	static final LatLng KIEL = new LatLng(53.551, 9.993);
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			// TODO Auto-generated method stub
			super.onCreate(savedInstanceState);
			Log.d("IssueDetail", "Enetered issue detail");
			Log.d("IssueDetail", "Befroe setting layout");
			setContentView(R.layout.issuedetails_temp);
			
			issueId = (TextView) findViewById(R.id.issueId);
			issueTitle = (TextView) findViewById(R.id.issueTitle);
			issueDesc = (TextView) findViewById(R.id.issueDesc);
			issueLoc = (TextView) findViewById(R.id.issueLoc);
			issuemaploc = (TextView) findViewById(R.id.issuemapLoc);
			issueAllocTo = (TextView) findViewById(R.id.issueAlloc);
			issueStatus = (TextView) findViewById(R.id.issueStatus);
			issueLoggedBy = (TextView) findViewById(R.id.issueloggedby);
			issueAcceptedTS = (TextView) findViewById(R.id.issueAcceptedTS);
			issueResolvedTS = (TextView) findViewById(R.id.issueResolvedTS);
			Log.d("IssueDetailsActivity", "before setting mapfragment");		
			
			
			
			Intent i = getIntent();
			IssuesModel issue = (IssuesModel)i.getSerializableExtra("issueDetails");
			issueId.setText(issue.getIS_ID());
			issueTitle.setText(issue.getIS_TITLE());
			issueDesc.setText(issue.getIS_DESC());
			issuemaploc.setText(issue.getIS_MAP_LOC());
			issueAllocTo.setText(issue.getIS_ALLOC_TO());
			issueStatus.setText(issue.getIS_STATUS());
			issueLoggedBy.setText(issue.getIS_LOGGED_BY());
			issueAcceptedTS.setText(issue.getIS_ACCEPTED_TS());
			issueResolvedTS.setText(issue.getIS_RESOLVED_TS());
			
			issueAcceptedTS.setText("NA");
			issueResolvedTS.setText("NA");
			
			Fragment frag= getFragmentManager().findFragmentById(R.id.location_map);
			MapFragment mt=(MapFragment) frag;
			map = mt.getMap();
			
			Marker issueLocMarker = map.addMarker(new MarkerOptions().position(HAMBURG)
					.title("Hamburg"));
			Marker kiel = map.addMarker(new MarkerOptions()
					.position(KIEL)
					.title("Kiel")
					.snippet("Kiel is cool")
					.icon(BitmapDescriptorFactory
							.fromResource(R.drawable.ic_launcher)));

			// Move the camera instantly to hamburg with a zoom of 15.
			map.moveCamera(CameraUpdateFactory.newLatLngZoom(HAMBURG, 15));

			// Zoom in, animating the camera.
			map.animateCamera(CameraUpdateFactory.zoomTo(10), 2000, null);
			
			
			Log.d("IssueDetail", "Issue Descritpion is :"+issue.getIS_DESC());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	

}
