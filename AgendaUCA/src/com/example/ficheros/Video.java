package com.example.ficheros;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.example.agendauca.ListarFicheros;
import com.example.agendauca.MenuInicial;
import com.example.utilidades.FuncionesUtiles;



import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

//Clase para grabar video
public class Video extends Activity{
	private static final int CAPTURA_VIDEO = 200;
    String ruta;
	Uri fileUri = null;
	File miVideo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Bundle rutaDeDirectorio = this.getIntent().getExtras();
		ruta = rutaDeDirectorio.getString("CarpetaDestino");
		Intent video = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		miVideo = ficheroVideo();
		if(miVideo.getName() == "Error"){ //Si no se ha creado el archivo por memoria
			Toast.makeText(this, "Memoria externa no disponible", Toast.LENGTH_SHORT).show();
			Intent cambio_actividad = new Intent();
            cambio_actividad.setClass(this, MenuInicial.class);
	        startActivity(cambio_actividad);
	        finish();
		}
		else{ //Se graba
		  fileUri = Uri.fromFile(miVideo);
		  video.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
		  video.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 0);
		  startActivityForResult(video, CAPTURA_VIDEO);
		}
	}	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) { //Para verificar que se ha grabado bien
		   Intent cambio_actividad = new Intent();
		   if (resultCode == RESULT_OK) {
		        Toast.makeText(this, "Video guardado con éxito", Toast.LENGTH_SHORT).show();
		        cambio_actividad.setClass(this, Video.class);
		        cambio_actividad.putExtra("CarpetaDestino",  ruta);
			    startActivity(cambio_actividad);
			    finish();
		    } 
		    else if (resultCode == RESULT_CANCELED) {
		    	Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show();
		    	cambio_actividad.putExtra("Subdirectorio",  ruta);
		    	cambio_actividad.setClass(this, ListarFicheros.class);
		    	startActivity(cambio_actividad);
		    	finish();
		    } 
    }
	
	private File ficheroVideo() { //Te crea el fichero de video con respondiente para grabar
		if(FuncionesUtiles.estadoEscritura()){
		  File dir = new File(ruta);
		
          String horaLocal = new SimpleDateFormat("yyyMMdd_HHmmss", Locale.ROOT).format(new Date());
          File video = new File(dir, "VID_" + horaLocal + ".mp4");
		  return video;
		}
		
		return new File("Error");
	}
}
