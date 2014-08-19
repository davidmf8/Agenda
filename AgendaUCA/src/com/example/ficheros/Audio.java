package com.example.ficheros;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.example.agendauca.MenuInicial;
import com.example.agendauca.R;
import com.example.utilidades.FuncionesUtiles;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

//Clase para grabar audio
public class Audio extends Activity {
	MediaRecorder audio;
	Button grabar, parar;
    Chronometer tiempo;
    String ruta;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audio);
		
        Bundle rutaDeDirectorio = this.getIntent().getExtras();
		ruta = rutaDeDirectorio.getString("CarpetaDestino");

		grabar = (Button) findViewById(R.id.Grabar); 
		parar = (Button) findViewById(R.id.Parar);
		parar.setEnabled(false);
		//Obtenemos los botones para habilitar/deshabilitar alguno de ellos mientras el otro está en uso
	}

	public void grabar(View v) { //Grabar audio
		//Configuramos el formato del audio
		tiempo = (Chronometer) findViewById(R.id.tiempoGrabacion);
		audio = new MediaRecorder();
		audio.setAudioSource(MediaRecorder.AudioSource.MIC);
		audio.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		audio.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		
		String archivo = ficheroAudio();
		
		if(archivo == "ERROR"){ //Si la memoria no está disponible
			Toast.makeText(this, "Memoria externa no disponible", Toast.LENGTH_SHORT).show();
			Intent cambio_actividad = new Intent();
            cambio_actividad.setClass(this, MenuInicial.class);
	        startActivity(cambio_actividad);
	        finish();
		}
		else{ //Si lo está, se procede a preparar el micro del telefono y a grabar
		  audio.setOutputFile(archivo);
		  try {
			  audio.prepare();
		  } catch (IOException e) {}
		  audio.start();
		  tiempo.start();
		  grabar.setEnabled(false);
		  parar.setEnabled(true);
		}
	}

	public void detener(View v) { //Detener grabación
		audio.stop();
		tiempo.stop();
		tiempo.setBase(SystemClock.elapsedRealtime());
		audio.release();
		grabar.setEnabled(true); 
		parar.setEnabled(false);
		Toast.makeText(this, "Audio guardado correctamente", Toast.LENGTH_SHORT).show();
	}

	private String ficheroAudio() {
		 //Creamos directorio AgendaAudio si no está creado, y se crea el archivo que almacenará el audio
		if(FuncionesUtiles.estadoEscritura()){
			File dir;
			if(ruta.equalsIgnoreCase(getExternalFilesDir(null).getAbsolutePath())){
		       dir = new File(getExternalFilesDir(null).getAbsolutePath() + "/AgendaAudio");
		       if(!dir.exists())
		    	   dir.mkdir();
			}
			else
			   dir = new File(ruta);
		
            String horaLocal = new SimpleDateFormat("yyyMMdd_HHmmss", Locale.ROOT).format(new Date());
            String ruta_audio = dir.getAbsolutePath() + "/" + "AUD_" + horaLocal + ".3gp";
		    return ruta_audio;
		}
	    return ("Error");
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
		    Intent cambio_actividad = new Intent();
		    cambio_actividad.putExtra("Subdirectorio",  ruta);
            cambio_actividad.setClass(this, ListarFicheros.class);
			startActivity(cambio_actividad);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
