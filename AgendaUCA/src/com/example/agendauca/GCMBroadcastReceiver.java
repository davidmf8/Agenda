package com.example.agendauca;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;

public class GCMBroadcastReceiver extends WakefulBroadcastReceiver{

	@Override
	public void onReceive(Context context, Intent intent) {
		 ComponentName componente = new ComponentName(context.getPackageName(),
	                GCMServicioPush.class.getName());

	     startWakefulService(context, (intent.setComponent(componente)));
	     setResultCode(Activity.RESULT_OK);
	}

}
