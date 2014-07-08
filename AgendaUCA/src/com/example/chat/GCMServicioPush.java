package com.example.chat;

import java.util.List;

import com.example.agendauca.MenuInicial;
import com.example.persistencia.BDAcceso;
import com.example.utilidades.FuncionesUtiles;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


public class GCMServicioPush extends IntentService{
	private BDAcceso BD;
	private Intent notificadorChat;
	String miUsuario;

	public GCMServicioPush() {
		super("GCMServicioPush");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		GoogleCloudMessaging serverGCM = GoogleCloudMessaging.getInstance(this);
		SharedPreferences misPreferencias = this.getSharedPreferences(FuncionesUtiles.getPreferencias(), 0);
		miUsuario = misPreferencias.getString(FuncionesUtiles.getUsuario(), "");
		
		String tipoMensaje = serverGCM.getMessageType(intent);
        Bundle extras = intent.getExtras();

        if (!extras.isEmpty()){
                if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(tipoMensaje)){
                	notificadorChat = new Intent("ActualizarLista");
                	notificadorChat.putExtra("message", extras.getString("message"));
                	notificadorChat.putExtra("user", extras.getString("user"));
                	notificadorChat.putExtra("addGroup", extras.getString("addGroup"));
                	if(extras.getString("user").equalsIgnoreCase("CrearEvento"))
                	  notificacionEvento(extras.getString("message"));
                	else
                      mostrarNotificacion(extras.getString("message"), extras.getString("user"), extras.getString("addGroup"));
                }
        }

        GCMBroadcastReceiver.completeWakefulIntent(intent);
	}

	private void notificacionEvento(String datos) {
		 NotificationManager notificador = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		 NotificationCompat.Builder notificacion =
		            new NotificationCompat.Builder(this)
		               .setSmallIcon(android.R.drawable.ic_dialog_email)
		               .setContentTitle("Nuevo evento")
		               .setVibrate(new long[] {100, 250, 100, 500})
		               .setAutoCancel(true);
		  
		  String[] datosEvento = datos.split("-");
		  /*String fechaHora = datosEvento[3] + " " + datosEvento[4];
		  SimpleDateFormat formateoFechaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		  Date formatoFecha = null;
		  try {
			formatoFecha = formateoFechaHora.parse(fechaHora);
			Log.d("FECHA", formatoFecha.toString());
		} catch (ParseException e) {
			e.printStackTrace();
		}
		  
		  ContentResolver contentCalendario = getContentResolver();
     	  ContentValues parametrosEvento = new ContentValues();
     	  parametrosEvento.put(Events.TITLE, datosEvento[0]);
     	  parametrosEvento.put(Events.DESCRIPTION, datosEvento[1]);
     	  parametrosEvento.put(Events.EVENT_LOCATION, datosEvento[2]);
     	  parametrosEvento.put(Events.EVENT_TIMEZONE, "GTM-1");
     	  parametrosEvento.put(Events.DTSTART, formatoFecha.getTime());
     	  parametrosEvento.put(Events.DTEND, formatoFecha.getTime());
     	  parametrosEvento.put(Events.CALENDAR_ID, 1);
     	  Uri uriEvento = contentCalendario.insert(Events.CONTENT_URI, parametrosEvento);*/	
		 
		 Intent actividadResultante =  new Intent(this, MenuInicial.class);
		 actividadResultante.putExtra("datosEvento", datosEvento);
	     PendingIntent contIntent = PendingIntent.getActivity(this, 0, actividadResultante, PendingIntent.FLAG_UPDATE_CURRENT);
	 
	     notificacion.setContentIntent(contIntent);
	 
	     notificador.notify(1, notificacion.build());
		
	}

	private void mostrarNotificacion(String mensaje, String usuario, String grupo) {
	   NotificationManager notificador = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
       if(mensaje != null){
		 if(mensaje.equalsIgnoreCase("Agregar")){
			 NotificationCompat.Builder notificacion =
			            new NotificationCompat.Builder(this)
			               .setSmallIcon(android.R.drawable.ic_dialog_email)
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
				               .setSmallIcon(android.R.drawable.ic_dialog_email)
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
	               		.setSmallIcon(android.R.drawable.ic_dialog_email)
	               		.setContentTitle("Mensaje de "+usuario)
	               		.setVibrate(new long[] {100, 250, 100, 500})
	               		.setAutoCancel(true);
	     
	        	BD = new BDAcceso(this.getApplicationContext());
	        	BD.BDopen();
	        	if(grupo != null){
					if(!miUsuario.equalsIgnoreCase(usuario))
	        		    BD.insertarMensaje(mensaje, grupo, 0, usuario);
	        	}
	        	else	
	        	    BD.insertarMensaje(mensaje, usuario, 0, "");
	        	BD.BDclose();
	 
	        	Intent actividadResultante =  new Intent(this, chatAmigo.class);
	        	ActivityManager am = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
	        	List<RunningTaskInfo> taskInfo = am.getRunningTasks(1);
	            if(taskInfo.get(0).topActivity.getClassName().equalsIgnoreCase("com.example.chat.chatAmigo"));
	        	  actividadResultante.putExtra("cerrarActivity", false);
	        	
	        	if(grupo != null)
	        		actividadResultante.putExtra("Nombre", grupo);
	        	else	
	        	    actividadResultante.putExtra("Nombre", usuario);   
	        	PendingIntent contIntent = PendingIntent.getActivity(this, 0, actividadResultante, PendingIntent.FLAG_UPDATE_CURRENT);
	 
	        	notificacion.setContentIntent(contIntent);
	        	//if(!miUsuario.equalsIgnoreCase(usuario))
	        	notificador.notify(1, notificacion.build());
	        	
	        	sendBroadcast(notificadorChat);
			 }
		 }
       }
	     
	}

}
