package com.example.agendauca;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

public class Video extends Activity{
	private static final int CAPTURA_VIDEO = 200;

	Uri fileUri = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Intent video = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
		fileUri = Uri.fromFile(ficheroVideo());
		video.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
		video.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
		startActivityForResult(video, CAPTURA_VIDEO);
	}
	
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		   Intent cambio_actividad = new Intent();
		   if (resultCode == RESULT_OK) {
		        Toast.makeText(this, "Video guardado con éxito", 
		                       Toast.LENGTH_SHORT).show();
		        cambio_actividad.setClass(this, Video.class);
			    startActivity(cambio_actividad);
		    } 
		    else if (resultCode == RESULT_CANCELED) {
		      Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show();
		      cambio_actividad.setClass(this, MainActivity.class);
		      startActivity(cambio_actividad);
		    } 
    }
	
	private File ficheroVideo() {
		File dir = new File(this.getExternalFilesDir(Environment.DIRECTORY_DCIM),  "AgendaVideos");
		if(!dir.exists()){
			dir.mkdir();
		}
		
        String horaLocal = new SimpleDateFormat("yyyMMdd_HHmmss", Locale.ROOT).format(new Date());
        File video = new File(dir, "VID_" + horaLocal + ".mp4");
		return video;
	}
}
