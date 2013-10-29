package Agenda.funcionalidades;


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
	private static final int CAPTURE_IMAGE_ACTIVITY_REQ = 0;
		
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
		startActivityForResult(camara, CAPTURE_IMAGE_ACTIVITY_REQ );
	}
	
	//Sobreescribimos este metodo para lo que necesitamos
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		  if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQ) {
		    if (resultCode == RESULT_OK) {
		      Uri fotoUri = null;
		      if (data == null) {
		        // A known bug here! The image should have saved in fileUri
		        Toast.makeText(this, "Image saved successfully", 
		                       Toast.LENGTH_LONG).show();
		        fotoUri = fileUri;
		      } else {
		        fotoUri = data.getData();
		        Toast.makeText(this, "Image saved successfully in: " + data.getData(), 
		                       Toast.LENGTH_LONG).show();
		      }
		      mostrarFoto(fotoUri.getPath());
		    } else if (resultCode == RESULT_CANCELED) {
		      Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
		      Intent cambio_actividad = new Intent();
		      cambio_actividad.setClass(this, MainActivity.class);
			  startActivity(cambio_actividad);
		    } else {
		      Toast.makeText(this, "Callout for image capture failed!", 
		                     Toast.LENGTH_LONG).show();
		    }
		  }
	}
	
	private File ficheroFoto() {
		
		  File directory = new File(Environment.getExternalStoragePublicDirectory(
		                Environment.DIRECTORY_PICTURES), getPackageName());
		  
		  if (!directory.exists()) {
		    if (!directory.mkdirs()) {
		      return null;
		    }
		  }
		  
		  String timeStamp = new SimpleDateFormat("yyyMMdd_HHmmss", Locale.US).format(new Date());
		  
		  return new File(directory.getPath() + File.separator + "IMG_"  
		                    + timeStamp + ".jpg");
	}
	
	private void mostrarFoto(String fotoUri) {
		  File imageFile = new File (fotoUri);
		  if (imageFile.exists()){
		     Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
		     BitmapDrawable drawable = new BitmapDrawable(this.getResources(), bitmap);
		     fotoImagen.setScaleType(ImageView.ScaleType.FIT_CENTER);
		     fotoImagen.setImageDrawable(drawable);
		  }       
		  
	}
}
