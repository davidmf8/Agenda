package com.example.agendauca;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

public class GCMServicioPush extends IntentService{

	public GCMServicioPush() {
		super("GCMServicioPush");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		GoogleCloudMessaging serverGCM = GoogleCloudMessaging.getInstance(this);
		
		String messageType = serverGCM.getMessageType(intent);
        Bundle extras = intent.getExtras();

        if (!extras.isEmpty())
        {
                if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType))
                {
                    mostrarNotificacion(extras.getString("message"));
                }
        }

        GCMBroadcastReceiver.completeWakefulIntent(intent);
	}

	private void mostrarNotificacion(String mensaje) {
		 NotificationManager mNotificationManager =
	                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	 
	        NotificationCompat.Builder mBuilder =
	            new NotificationCompat.Builder(this)
	                .setSmallIcon(android.R.drawable.stat_sys_warning)
	                .setContentTitle("Notificación GCM")
	                .setContentText(mensaje);
	 
	        Intent notIntent =  new Intent(this, MenuInicial.class);
	        PendingIntent contIntent = PendingIntent.getActivity(
	                this, 0, notIntent, 0);
	 
	        mBuilder.setContentIntent(contIntent);
	 
	        mNotificationManager.notify(1, mBuilder.build());
	}

}
