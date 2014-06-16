package com.example.ficheros;

import com.example.agendauca.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.MediaController;
import android.widget.VideoView;

//Reproductor video
public class ReproducirVideo extends Activity{
	VideoView miVideo;
	String rutaVideo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reproduccion_video);
		
		Bundle datosIntent = this.getIntent().getExtras();
		rutaVideo = datosIntent.getString("ArchivoReproducir");
		
		 miVideo = (VideoView) findViewById(R.id.ReproductorVideo);
         Uri ruta = Uri.parse(rutaVideo);

		 MediaController reproductor = new MediaController(this);    
		 miVideo.setMediaController(reproductor);
		 
		 miVideo.setVideoURI(ruta);
		 miVideo.start();	
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			int pos = rutaVideo.lastIndexOf("/");
			String rutaAnterior = rutaVideo.substring(0, pos);
		    Intent cambio_actividad = new Intent();
		    cambio_actividad.putExtra("Subdirectorio", rutaAnterior);
			cambio_actividad.setClass(getApplicationContext(), ListarFicheros.class);
			startActivity(cambio_actividad);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
