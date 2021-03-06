package com.example.chat;

import com.example.agendauca.MenuInicial;
import com.example.conexionesServidor.descargaFicheroService;
import com.example.ficheros.ListarFicheros;
import com.example.persistencia.BDAcceso;
import com.example.utilidades.FuncionesUtiles;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

//Clase que trata las notificaciones recibidas
public class GCMServicioPush extends IntentService{
	private BDAcceso BD;
	private Intent notificadorChat;
	private String miUsuario;

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

        if (!extras.isEmpty()){//Si hay mensajes
                if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(tipoMensaje)){
                	notificadorChat = new Intent("ActualizarLista");
                	//notificadorChat.putExtra("message", extras.getString("mensaje"));
                	notificadorChat.putExtra("user", extras.getString("user"));
                	notificadorChat.putExtra("grupo", extras.getString("nuevoGrupo"));
                	if(extras.getString("user").equalsIgnoreCase("CrearEvento")) 
                	  notificacionEvento(extras.getString("mensaje"));
                	else
                		if(extras.getString("mensaje") != null)
                		  if(extras.getString("mensaje").contains("http://prubauca.esy.es/descargas"))
                		    notificacionFichero(extras.getString("mensaje"));
                		  else	
                            mostrarNotificacion(extras.getString("mensaje"), extras.getString("user"), extras.getString("nuevoGrupo"));
                }
        }

        GCMBroadcastReceiver.completeWakefulIntent(intent);
	}

	//Trata la recepci�n de un fichero
	private void notificacionFichero(String mensajeDescarga) {
		 NotificationManager notificador = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		 NotificationCompat.Builder notificacion =
		            new NotificationCompat.Builder(this)
		               .setSmallIcon(android.R.drawable.ic_dialog_email)
		               .setContentTitle("Nuevo fichero recibido")
		               .setVibrate(new long[] {100, 250, 100, 500})
		               .setAutoCancel(true);
		 
		 Intent intent = new Intent(this, descargaFicheroService.class);
		 intent.putExtra("ruta", mensajeDescarga);
		 startService(intent);
		   
		 Intent actividadResultante =  new Intent(this, ListarFicheros.class);
	     actividadResultante.putExtra("Subdirectorio", getExternalFilesDir(null).getAbsolutePath());
		 PendingIntent contIntent = PendingIntent.getActivity(this, 0, actividadResultante, PendingIntent.FLAG_UPDATE_CURRENT);
		 
		 notificacion.setContentIntent(contIntent);
	 
	     notificador.notify(1, notificacion.build());
	}

	//Trata larecepci�n de un evento
	private void notificacionEvento(String datos) {
		 NotificationManager notificador = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		 NotificationCompat.Builder notificacion =
		            new NotificationCompat.Builder(this)
		               .setSmallIcon(android.R.drawable.ic_dialog_email)
		               .setContentTitle("Nuevo evento")
		               .setVibrate(new long[] {100, 250, 100, 500})
		               .setAutoCancel(true);
		  
		  String[] datosEvento = datos.split("-");
		 
		 Intent actividadResultante =  new Intent(this, MenuInicial.class);
		 actividadResultante.putExtra("datosEvento", datosEvento);
	     PendingIntent contIntent = PendingIntent.getActivity(this, 0, actividadResultante, PendingIntent.FLAG_UPDATE_CURRENT);
	 
	     notificacion.setContentIntent(contIntent);
	 
	     notificador.notify(1, notificacion.build());
		
	}

	//Trata la recepci�n de un mensaje: ha sido agregado por un amigo, a�adido a un grupo o ha recibido un mensaje de unusuario o grupo
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
		     PendingIntent contIntent = PendingIntent.getActivity(this, 0, actividadResultante, Intent.FLAG_ACTIVITY_NEW_TASK);
		 
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
			     PendingIntent contIntent = PendingIntent.getActivity(this, 0, actividadResultante, Intent.FLAG_ACTIVITY_NEW_TASK);
			 
			     notificacion.setContentIntent(contIntent);
			     
			     if(!miUsuario.equalsIgnoreCase(usuario))
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
					if(!miUsuario.equalsIgnoreCase(usuario)){
	        		    BD.insertarMensaje(mensaje, grupo, 0, usuario);
	        		    BD.setNuevoMensaje(true, grupo);
					}
	        	}
	        	else{	
	        	    BD.insertarMensaje(mensaje, usuario, 0, "");
	        	    BD.setNuevoMensaje(true, usuario);
	        	}
	        	BD.BDclose();
	 
	        	Intent actividadResultante =  new Intent(this, chatAmigo.class);
	        	//actividadResultante.putExtra("cerrarActivity", false);
	        	
	        	if(grupo != null)
	        		actividadResultante.putExtra("Nombre", grupo);
	        	else	
	        	    actividadResultante.putExtra("Nombre", usuario);   
	        	PendingIntent contIntent = PendingIntent.getActivity(this, 0, actividadResultante, Intent.FLAG_ACTIVITY_NEW_TASK);
	 
	        	notificacion.setContentIntent(contIntent);
	        	if(!miUsuario.equalsIgnoreCase(usuario))
	        	  notificador.notify(1, notificacion.build());
	        	
	        	sendBroadcast(notificadorChat);
			 }
		 }
       }
	     
	}

}
