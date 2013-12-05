package com.example.agendauca;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

//Reproductor video
public class ReproducirVideo extends Activity{

	VideoView miVideo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reproduccion_video);
		
		Bundle datosIntent = this.getIntent().getExtras();
		String rutaVideo = datosIntent.getString("AchivoReproducir");
		
		 miVideo = (VideoView) findViewById(R.id.ReproductorVideo);
         Uri ruta = Uri.parse(rutaVideo);

		 MediaController reproductor = new MediaController(this);    
		 miVideo.setMediaController(reproductor);
		 
		 miVideo.setVideoURI(ruta);
		 miVideo.start();
		
		
	}
}
