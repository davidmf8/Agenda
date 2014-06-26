package com.example.agendauca;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.example.agendauca.R;
import com.example.chat.chatPrincipal;
import com.example.conexionesServidor.descargarExamenesAsynTask;
import com.example.ficheros.ListarFicheros;

import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View; 
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class MenuInicial extends Activity implements OnClickListener{
	private ImageButton gestion_archivo;
	private ImageButton eventos;
	private ImageButton notificaciones;
	private ImageButton calificaciones;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//Primera pantalla y los botones están a la escucha de una acción
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
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
		//Dependiendo del boton que se pulse, nos llevará a una funcionalidad u otra: ver directorios, eventos, calificaciones y notificaciones
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
			   Intent intent = new Intent(Intent.ACTION_VIEW).setData(builder.build());
			   startActivity(intent);
			   break;
		   case R.id.Calificaciones:
			   descargarExamenesAsynTask descargarExcel = new descargarExamenesAsynTask();
			   descargarExcel.inicializarValores(getExternalFilesDir(null).getAbsolutePath(), this);
			   descargarExcel.execute();
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
