package com.example.agendauca;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.example.agendauca.R;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Audio extends Activity {
	MediaRecorder audio;
	Button grabar, parar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audio);

		grabar = (Button) findViewById(R.id.Grabar);
		parar = (Button) findViewById(R.id.Parar);
	}

	public void grabar(View v) {
		audio = new MediaRecorder();
		audio.setAudioSource(MediaRecorder.AudioSource.MIC);
		audio.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		audio.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		
		String archivo = ficheroAudio();
		
		if(archivo == "ERROR"){
			Toast.makeText(this, "Memoria externa no disponible", Toast.LENGTH_SHORT).show();
			Intent cambio_actividad = new Intent();
            cambio_actividad.setClass(this, MainActivity.class);
	        startActivity(cambio_actividad);
		}
		else{
		  audio.setOutputFile(archivo);
		
		  try {
			  audio.prepare();
		  } catch (IOException e) {}
		  audio.start();
		  grabar.setEnabled(false);
		  parar.setEnabled(true);
		}
	}

	public void detener(View v) {
		audio.stop();
		audio.release();

		grabar.setEnabled(true);
		parar.setEnabled(false);
	}

	private String ficheroAudio() {
		  //Creamos directorio de musica
		if(FuncionesUtiles.estadoEscritura()){
		  File dir = new File(this.getExternalFilesDir(Environment.DIRECTORY_MUSIC),  "AgendaAudio");
		  if(!dir.exists()){
			dir.mkdir();
		  }
		
          String horaLocal = new SimpleDateFormat("yyyMMdd_HHmmss", Locale.ROOT).format(new Date());
          String ruta_audio = dir.getAbsolutePath() + "/" + "AUD_" + horaLocal + ".3gp";
		  return ruta_audio;
		}
	    return ("Error");
	}

}
