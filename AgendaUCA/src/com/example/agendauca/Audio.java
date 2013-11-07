package com.example.agendauca;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;

public class Audio extends Activity {
	MediaRecorder recorder;
	File archivo;
	Button b1, b2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_audio);

		b1 = (Button) findViewById(R.id.button1);
		b2 = (Button) findViewById(R.id.button2);
	}

	public void grabar(View v) {
		recorder = new MediaRecorder();
		recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
		recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		
		archivo = ficheroAudio();

		recorder.setOutputFile(archivo.getAbsolutePath());
		try {
			recorder.prepare();
		} catch (IOException e) {}
		recorder.start();
		b1.setEnabled(false);
		b2.setEnabled(true);
	}

	public void detener(View v) {
		recorder.stop();
		recorder.release();

		b1.setEnabled(true);
		b2.setEnabled(false);
	}

	private File ficheroAudio() {
		  //Creamos directorio de musica
		if(FuncionesUtiles.estadoEscritura()){
		  File dir = new File(this.getExternalFilesDir(Environment.DIRECTORY_MUSIC),  "AgendaAudio");
		  if(!dir.exists()){
			dir.mkdir();
		  }
		
        String horaLocal = new SimpleDateFormat("yyyMMdd_HHmmss", Locale.ROOT).format(new Date());
        File foto = new File(dir, "AUD_" + horaLocal + ".3gp");
		  return foto;
		}
	    return new File("Error");
	}

}
