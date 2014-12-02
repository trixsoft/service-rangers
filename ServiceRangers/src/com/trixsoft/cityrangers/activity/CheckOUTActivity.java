package com.trixsoft.cityrangers.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class CheckOUTActivity extends FragmentActivity {

	private TextView mLatLng;
	private TextView mAddress;
	private TextView mLastUp;
	private TextView mDeviceID;

	private LocationManager mLocationMgr;
	private Handler mHandler;

	private Location mLastLocation;

	private static final int UPDATE_LASTLATLNG = 4;
	private static final int LAST_UP = 3;
	private static final int UPDATE_LATLNG = 2;
	private static final int UPDATE_ADDRESS = 1;
	private boolean mGeocoderAvailable;

	private static final int SECONDS_TO_UP = 10000;
	private static final int METERS_TO_UP = 5;
	private static final int MINUTES_TO_STALE = 1000 * 60 * 2;

	private PendingIntent pendingIntent;
	private AlarmManager alarmManager;
	
	public void stopAlarm() {
		Log.d("TESTING ALARM ACTIVATION", "STOP ALARM");
		alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(pendingIntent);
		
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_check_in);

		Intent alarmIntent = new Intent(this, PeriodicCheckInReceiver.class);
		pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
		stopAlarm();

		mLatLng = (TextView) findViewById(R.id.latlng);
		mLastUp = (TextView) findViewById(R.id.lastup);
		mAddress = (TextView) findViewById(R.id.address);
		mDeviceID = (TextView) findViewById(R.id.deviceID);

		mDeviceID.setText(getDeviceID());

		mHandler = new Handler() {
			public void handleMessage(Message msg) {
				switch (msg.what) {
				case UPDATE_ADDRESS:
					mAddress.setText((String) msg.obj);
					break;
				case UPDATE_LATLNG:
					mLatLng.setText((String) msg.obj);
					break;
				case LAST_UP:
					mLastUp.setText((String) msg.obj);
					break;
				}
			}

		};

		mGeocoderAvailable = Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD
				&& Geocoder.isPresent();
		mLocationMgr = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);

	}

	private String getDeviceID() {

		TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		return telephonyManager.getDeviceId();

	}

	public void onStart() {
		super.onStart();
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		final boolean gpsEnabled = locationManager
				.isProviderEnabled(LocationManager.GPS_PROVIDER);

		if (!gpsEnabled) {
			new EnableGpsDialogFragment().show(getSupportFragmentManager(),
					"enableGpsDialog");
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		setup();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	}

	private void setup() {
		Location newLocation = null;
		mLocationMgr.removeUpdates(listener);
		mLatLng.setText(R.string.unknown);
		newLocation = requestUpdatesFromProvider(LocationManager.GPS_PROVIDER,
				R.string.no_gps_support);

		// If gps location doesn't work, try network location
		if (newLocation == null) {
			newLocation = requestUpdatesFromProvider(
					LocationManager.NETWORK_PROVIDER,
					R.string.no_network_support);
		}

		if (newLocation != null) {
			updateUILocation(getBestLocation(newLocation, mLastLocation));
		}

		// Code added to send the location and device details to a certain URL
		sendLocationDetails();

	}

	private void sendLocationDetails() {
		// TODO Auto-generated method stub

	}

	/**
	 * This code is based on this code:
	 * http://developer.android.com/guide/topics
	 * /location/obtaining-user-location.html
	 * 
	 * @param newLocation
	 * @param currentBestLocation
	 * @return
	 */
	protected Location getBestLocation(Location newLocation,
			Location currentBestLocation) {
		if (currentBestLocation == null) {
			return newLocation;
		}

		// Check whether the new location fix is newer or older
		long timeDelta = newLocation.getTime() - currentBestLocation.getTime();
		boolean isNewerThanStale = timeDelta > MINUTES_TO_STALE;
		boolean isOlderThanStale = timeDelta < -MINUTES_TO_STALE;
		boolean isNewer = timeDelta > 0;

		if (isNewerThanStale) {
			return newLocation;
		} else if (isOlderThanStale) {
			return currentBestLocation;
		}

		int accuracyDelta = (int) (newLocation.getAccuracy() - currentBestLocation
				.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(newLocation.getProvider(),
				currentBestLocation.getProvider());

		// Determine location quality using a combination of timeliness and
		// accuracy
		if (isMoreAccurate) {
			return newLocation;
		} else if (isNewer && !isLessAccurate) {
			return newLocation;
		} else if (isNewer && !isSignificantlyLessAccurate
				&& isFromSameProvider) {
			return newLocation;
		}
		return currentBestLocation;
	}

	/** Checks whether two providers are the same */
	private boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}

	private Location requestUpdatesFromProvider(final String provider,
			final int errorResId) {
		Location location = null;
		if (mLocationMgr.isProviderEnabled(provider)) {
			mLocationMgr.requestLocationUpdates(provider, SECONDS_TO_UP,
					METERS_TO_UP, listener);
			location = mLocationMgr.getLastKnownLocation(provider);
		} else {
			Toast.makeText(this, errorResId, Toast.LENGTH_LONG).show();
		}
		return location;
	}

	private void enableLocationSettings() {
		Intent settingsIntent = new Intent(
				Settings.ACTION_LOCATION_SOURCE_SETTINGS);
		startActivity(settingsIntent);
	}

	private void doReverseGeocoding(Location location) {
		(new ReverseGeocode(this)).execute(new Location[] { location });
	}

	private void updateUILocation(Location location) {
		Message.obtain(mHandler, UPDATE_LATLNG,
				location.getLatitude() + ", " + location.getLongitude())
				.sendToTarget();
		if (mLastLocation != null) {
			Message.obtain(
					mHandler,
					UPDATE_LASTLATLNG,
					mLastLocation.getLatitude() + ", "
							+ mLastLocation.getLongitude()).sendToTarget();
		}
		mLastLocation = location;
		Date now = new Date();
		Message.obtain(mHandler, LAST_UP, now.toString()).sendToTarget();

		if (mGeocoderAvailable)
			doReverseGeocoding(location);
	}

	private final LocationListener listener = new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {
			updateUILocation(location);
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	/**
	 * Dialog to prompt users to enable GPS on the device.
	 */

	public class EnableGpsDialogFragment extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return new AlertDialog.Builder(getActivity())
					.setTitle(R.string.enable_gps)
					.setMessage(R.string.enable_gps_dialog)
					.setPositiveButton(R.string.enable_gps,
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									enableLocationSettings();
								}
							}).create();
		}
	}

	private class ReverseGeocode extends AsyncTask<Location, Void, Void> {
		Context mContext;

		public ReverseGeocode(Context context) {
			super();
			mContext = context;
		}

		@Override
		protected Void doInBackground(Location... params) {
			Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());

			Location loc = params[0];
			List<Address> addresses = null;
			try {
				addresses = geocoder.getFromLocation(loc.getLatitude(),
						loc.getLongitude(), 1);
			} catch (IOException e) {
				e.printStackTrace();
				// Update address field with the exception.
				Message.obtain(mHandler, UPDATE_ADDRESS, e.toString())
						.sendToTarget();
			}
			if (addresses != null && addresses.size() > 0) {
				Address address = addresses.get(0);
				// Format the first line of address (if available), city, and
				// country name.
				String addressText = String.format(
						"%s, %s, %s",
						address.getMaxAddressLineIndex() > 0 ? address
								.getAddressLine(0) : "", address.getLocality(),
						address.getCountryName());
				// Update address field on UI.
				Message.obtain(mHandler, UPDATE_ADDRESS, addressText)
						.sendToTarget();
			}

			// Send Location and Device details to Server
			HttpClient httpclient = new DefaultHttpClient();
			String url = "http://nelloremayor.org/administration/api/trackLocation.php";

			// This below code is used for HTTP GET

			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
			nameValuePairs.add(new BasicNameValuePair("agnt_loc", loc
					.getLatitude() + "," + loc.getLongitude()));
			nameValuePairs.add(new BasicNameValuePair("agnt_imei",
					getDeviceID()));

			nameValuePairs.add(new BasicNameValuePair("agnt_id", "1212"
					.toString()));
			nameValuePairs.add(new BasicNameValuePair("agnt_pos", "checkin"
					.toString()));
			HttpClient httpClient = new DefaultHttpClient();
			String paramsString = URLEncodedUtils.format(nameValuePairs,
					"UTF-8");
			HttpGet httpGet = new HttpGet(url + "?" + paramsString);
			Log.d("INFO_CHECKIN _URL", url + "?" + paramsString);
			try {
				HttpResponse response = httpclient.execute(httpGet);
				// Get the response
				BufferedReader rd = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));
				String line;
				while ((line = rd.readLine()) != null) {
					Log.d("DEBUG_RESP_CHECKIN", line);
				}
				rd.close();
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// This below code is using HTTP POST
			/*
			 * HttpPost httppost = new HttpPost(urltoPost);
			 * 
			 * try { List<NameValuePair> nameValuePairs = new
			 * ArrayList<NameValuePair>( 2); nameValuePairs.add(new
			 * BasicNameValuePair("agnt_loc", mLatLng.toString()));
			 * nameValuePairs.add(new BasicNameValuePair("agnt_imei",
			 * mDeviceID.toString())); httppost.setEntity(new
			 * UrlEncodedFormEntity(nameValuePairs));
			 * httpclient.execute(httppost); Log.d(ACTIVITY_SERVICE,
			 * "http Post Done");
			 * 
			 * } catch (ClientProtocolException e) { e.printStackTrace(); }
			 * catch (IOException e) { e.printStackTrace(); }
			 */
			return null;
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		this.mLocationMgr.removeUpdates(listener);
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		this.mLocationMgr.removeUpdates(listener);
	}

}
