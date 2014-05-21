package com.example.agendauca;

import com.example.persistencia.BDAcceso;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class GCMServicioPush extends IntentService{
	private BDAcceso BD;

	public GCMServicioPush() {
		super("GCMServicioPush");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		GoogleCloudMessaging serverGCM = GoogleCloudMessaging.getInstance(this);
		
		String tipoMensaje = serverGCM.getMessageType(intent);
        Bundle extras = intent.getExtras();

        if (!extras.isEmpty()){
                if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(tipoMensaje)){
                    mostrarNotificacion(extras.getString("message"), extras.getString("user"), extras.getString("addGroup"));
                }
        }

        GCMBroadcastReceiver.completeWakefulIntent(intent);
	}

	private void mostrarNotificacion(String mensaje, String usuario, String grupo) {
		 NotificationManager notificador = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

		 if(mensaje.equalsIgnoreCase("Agregar")){
			 NotificationCompat.Builder notificacion =
			            new NotificationCompat.Builder(this)
			               .setSmallIcon(android.R.drawable.stat_sys_warning)
			               .setContentTitle(usuario+" te ha agregado. Tienes un nuevo amigo.")
			               .setVibrate(new long[] {100, 250, 100, 500})
			               .setAutoCancel(true);

			 BDAcceso BD = new BDAcceso(this);
		     BD = BD.BDopen();
			 BD.insertarUsuario(usuario);
			 BD.BDclose();
			 Intent actividadResultante =  new Intent(this, chatPrincipal.class);
		     PendingIntent contIntent = PendingIntent.getActivity(this, 0, actividadResultante, PendingIntent.FLAG_UPDATE_CURRENT);
		 
		     notificacion.setContentIntent(contIntent);
		 
		     notificador.notify(1, notificacion.build());
		 }
		 else{
			 if(mensaje.equalsIgnoreCase("NuevoGrupo")){
				 NotificationCompat.Builder notificacion =
				            new NotificationCompat.Builder(this)
				               .setSmallIcon(android.R.drawable.stat_sys_warning)
				               .setContentTitle("Has sido agregado a un nuevo grupo.")
				               .setVibrate(new long[] {100, 250, 100, 500})
				               .setAutoCancel(true);
				 
				 BDAcceso BD = new BDAcceso(this);
			     BD = BD.BDopen();
				 BD.insertarUsuario(grupo);
				 BD.BDclose();
				 
				 Intent actividadResultante =  new Intent(this, chatPrincipal.class);
			     PendingIntent contIntent = PendingIntent.getActivity(this, 0, actividadResultante, PendingIntent.FLAG_UPDATE_CURRENT);
			 
			     notificacion.setContentIntent(contIntent);
			 
			     notificador.notify(1, notificacion.build());
			 }
			 else{
				 NotificationCompat.Builder notificacion =
		    		 new NotificationCompat.Builder(this)
	               		.setSmallIcon(android.R.drawable.stat_sys_warning)
	               		.setContentTitle("Mensaje de "+usuario)
	               		.setVibrate(new long[] {100, 250, 100, 500})
	               		.setAutoCancel(true);
	     
	        	BD = new BDAcceso(this.getApplicationContext());
	        	BD.BDopen();
	        	BD.insertarMensaje(mensaje, usuario, 0);
	        	BD.BDclose();
	 
	        	Intent actividadResultante =  new Intent(this, chatAmigo.class);
	        	actividadResultante.putExtra("Nombre", usuario);
	        	PendingIntent contIntent = PendingIntent.getActivity(this, 0, actividadResultante, PendingIntent.FLAG_UPDATE_CURRENT);
	 
	        	notificacion.setContentIntent(contIntent);
	 
	        	notificador.notify(1, notificacion.build());
			 }
		 }
	     
	}

}
