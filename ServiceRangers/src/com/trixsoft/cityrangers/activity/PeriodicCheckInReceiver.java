package com.trixsoft.cityrangers.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

public class PeriodicCheckInReceiver extends BroadcastReceiver{

	public static int ctr=0;
	@Override
	public void onReceive(Context context, Intent intent) {
		
		// For our recurring task, we'll just display a message
        Log.d("PeriodicCheckInReceiver","Ctr increased:"+ctr);
        ctr++;

	}
	
	

}
