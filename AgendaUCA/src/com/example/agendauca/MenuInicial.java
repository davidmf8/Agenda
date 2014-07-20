package com.example.agendauca;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.agendauca.R;
import com.example.chat.chatPrincipal;
import com.example.examenes.listaGrados;
import com.example.ficheros.ListarFicheros;

import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View; 
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.Toast;

public class MenuInicial extends Activity implements OnClickListener{
	private ImageButton gestion_archivo;
	private ImageButton eventos;
	private ImageButton notificaciones;
	private ImageButton calificaciones;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//Carga del menu principal con las opciones principales: ficheros, chat, calendario y examenes
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Bundle datosExtras = this.getIntent().getExtras();
		try{
		  String[] datosEvento = datosExtras.getStringArray("datosEvento");
		  alertaEvento(datosEvento);
		}catch(Exception e){}		
		
		gestion_archivo = (ImageButton)findViewById(R.id.Directorios);
		gestion_archivo.setOnClickListener(this);

		eventos = (ImageButton)findViewById(R.id.Evento);
		eventos.setOnClickListener(this);
		
		notificaciones = (ImageButton)findViewById(R.id.Notificacion);
		notificaciones.setOnClickListener(this);
		
		calificaciones = (ImageButton)findViewById(R.id.Calificaciones);
		calificaciones.setOnClickListener(this);
		
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	//Si llega un evento, se abrirá el menú principal, con un dialog para aceptar o rechazar el evento.
	private void alertaEvento(final String[] datosEvento) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Deseas asistir a este nuevo evento?").setTitle("Nuevo evento:" + datosEvento[0])
	        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()  {
	               public void onClick(DialogInterface dialog, int id) {
	            	  String fechaHora = datosEvento[3] + " " + datosEvento[4];
	         		  SimpleDateFormat formateoFechaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	         		  Date formatoFecha = null;
	         		  try {
	         			formatoFecha = formateoFechaHora.parse(fechaHora);
	         			ContentResolver contentCalendario = getContentResolver();
		              	ContentValues parametrosEvento = new ContentValues();
		              	parametrosEvento.put(Events.TITLE, datosEvento[0]);
		              	parametrosEvento.put(Events.DESCRIPTION, datosEvento[1]);
		              	parametrosEvento.put(Events.EVENT_LOCATION, datosEvento[2]);
		              	parametrosEvento.put(Events.EVENT_TIMEZONE, "GTM-1");
		              	parametrosEvento.put(Events.DTSTART, formatoFecha.getTime());
		              	parametrosEvento.put(Events.DTEND, formatoFecha.getTime());
		              	parametrosEvento.put(Events.CALENDAR_ID, 1);
		              	Uri uriEvento = contentCalendario.insert(Events.CONTENT_URI, parametrosEvento);
	         			Log.d("FECHA", formatoFecha.toString());
	         		} catch (ParseException e) {
	         			//Toast.makeText(this, "Datos de evento incompleto", Toast.LENGTH_SHORT).show();
	         		}
	         		  
	         		  
	              }
	         })
	        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                   
	                   }
	               });
        builder.create();
        builder.show();
		
	}

	/*public void onStop(){
		finish();
		super.onStop();
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Carga el action bar, para el "menu" de la creacion de fotos videos...
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }*/
	

	@Override
	public void onClick(View v) {
		//Dependiendo del boton que se pulse, nos llevará a una funcionalidad u otra: ver directorios, calendario, examenes y notificaciones
		Intent cambio_actividad = new Intent();
		switch(v.getId()){
		   case R.id.Directorios:
			   cambio_actividad.setClass(this, ListarFicheros.class);
			   cambio_actividad.putExtra("Subdirectorio", getExternalFilesDir(null).getAbsolutePath());
			   startActivity(cambio_actividad);
			   finish();
		       break;
		   case R.id.Notificacion:
			   cambio_actividad.setClass(this, chatPrincipal.class);
			   startActivity(cambio_actividad);
			   finish();
			   break;
		   case R.id.Evento:
			   long tiempo = System.currentTimeMillis();
			   Uri.Builder builder = CalendarContract.CONTENT_URI.buildUpon();
			   builder.appendPath("time");
			   ContentUris.appendId(builder, tiempo);
			   Intent calendario = new Intent(Intent.ACTION_VIEW).setData(builder.build());
			   startActivity(calendario);
			   break;
		   case R.id.Calificaciones:
			   cambio_actividad.setClass(this, listaGrados.class);
			   startActivity(cambio_actividad);
			   finish();
			   break;
		}
		
	}

	/*@Override
	 public boolean onOptionsItemSelected(MenuItem item) {
		Intent cambio_actividad = new Intent();
		//Para los botones de crear un archivo: foto, video, grabación, nota.
	     switch (item.getItemId()) {
	         case R.id.foto:
	        	 cambio_actividad.setClass(this, Camara.class);
				 startActivity(cambio_actividad);
	             break;
	         case R.id.Video:
	        	 cambio_actividad.setClass(this, Video.class);
				 startActivity(cambio_actividad);
	        	 break;
	         case R.id.Audio:
	        	 cambio_actividad.setClass(this, Audio.class);
				 startActivity(cambio_actividad);
	        	 break;
	         case R.id.Notas:
	        	 cambio_actividad.setClass(this, BlocNotas.class);
				 startActivity(cambio_actividad);
	        	 break;
	     } 
	  return false;
	 }*/
	
	
	//Boton atrás
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent cierreAplicacion = new Intent(Intent.ACTION_MAIN);
			cierreAplicacion.addCategory(Intent.CATEGORY_HOME);
			cierreAplicacion.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(cierreAplicacion);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
