package com.example.agendauca;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.Toast;

public class Camara extends Activity{
	private static final int IMAGEN_CAPTURADA = 1;
		
	Uri fileUri = null;
	ImageView fotoImagen = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Intent camara. Se le indica que la accion del intent será capturar una imagen
		Intent camara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		//Devuleve el fichero donde se guardará
		fileUri = Uri.fromFile(ficheroFoto());
		//Idicamos al intent de captura donde se guardará la foto en caso de confirmarlo
		camara.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
		//Espera el resultado de capturar la imagen
		startActivityForResult(camara, IMAGEN_CAPTURADA );
	}
	
	//Sobreescribimos este metodo para lo que necesitamos
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		   Intent cambio_actividad = new Intent();
		  if (requestCode == IMAGEN_CAPTURADA) {
		    if (resultCode == RESULT_OK) {
		        Toast.makeText(this, "Foto guardada con éxito", 
		                       Toast.LENGTH_LONG).show();
		        cambio_actividad.setClass(this, Camara.class);
			    startActivity(cambio_actividad);
		    } 
		    else if (resultCode == RESULT_CANCELED) {
		      Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show();
		      cambio_actividad.setClass(this, MainActivity.class);
		      startActivity(cambio_actividad);
		    } 
		  }
		  
		  
	      
	}
	
	private File ficheroFoto() {
		  //Creamos directorio foto
		  File directorioFoto = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "Fotos");

		  //SI este directorio no estáen memoria externa, lo creamos
		  if (!directorioFoto.exists()) {
		     directorioFoto.mkdir();
		  }
		  
		  String horaLocal = new SimpleDateFormat("yyyMMdd_HHmmss", Locale.ROOT).format(new Date());
		  
		  return new File(directorioFoto.getPath() + File.separator + "IMG_"  
		                    + horaLocal + ".jpg");
	}

}
