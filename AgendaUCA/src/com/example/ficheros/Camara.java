package com.example.ficheros;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.example.agendauca.MenuInicial;
import com.example.utilidades.FuncionesUtiles;



import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Toast;

//Clase para realizar fotos
public class Camara extends Activity{
	private static final int IMAGEN_CAPTURADA = 1;
		
	private Uri fileUri = null;
	private File miFoto;
	private String ruta;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        Bundle rutaDeDirectorio = this.getIntent().getExtras();
		
        ruta = rutaDeDirectorio.getString("CarpetaDestino");
		//Intent camara. Se le indica que la accion del intent será capturar una imagen
		Intent camara = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		miFoto = ficheroFoto(ruta);
		if(miFoto.getName() == "Error"){ //El fichero no se ha creado por problemas de memoria. Se regresa a la activity principal
			Toast.makeText(this, "Memoria externa no disponible", Toast.LENGTH_SHORT).show();
			Intent cambio_actividad = new Intent();
			cambio_actividad.putExtra("Subdirectorio",  ruta);
            cambio_actividad.setClass(this, ListarFicheros.class);
	        startActivity(cambio_actividad);
	        finish();
		}
		else{ //Se ha creado correctamente el fichero
			fileUri = Uri.fromFile(miFoto);
			//Idicamos al intent de captura donde se guardará la foto en caso de confirmarlo
		  	camara.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
		  	startActivityForResult(camara, IMAGEN_CAPTURADA );
		}
	}
	
	//Sobreescribimos este metodo para lo que necesitamos, que es saber el resultado de la captura
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		  Intent cambio_actividad = new Intent();
		  if (requestCode == IMAGEN_CAPTURADA) {
		    if (resultCode == RESULT_OK) {
		        Toast.makeText(this, "Foto guardada con éxito", Toast.LENGTH_SHORT).show();
		        cambio_actividad.setClass(this, Camara.class);
		        cambio_actividad.putExtra("CarpetaDestino",  ruta);
			    startActivity(cambio_actividad);
			    finish();
		    } 
		    else{
		      Toast.makeText(this, "Cancelado", Toast.LENGTH_SHORT).show();
		  	  cambio_actividad.putExtra("Subdirectorio",  ruta);
              cambio_actividad.setClass(this, ListarFicheros.class);
		      startActivity(cambio_actividad);
		      finish();
		    } 
		  }
	}
	
	private File ficheroFoto(String ruta) {
		  //Creamos directorio Agenda foto si no está creado, y se crea el archivo que se necesita para almacenar la foto
		if(FuncionesUtiles.estadoEscritura()){
		  File dir = new File(ruta); 
		  
          String horaLocal = new SimpleDateFormat("yyyMMdd_HHmmss", Locale.ROOT).format(new Date());
          File foto = new File(dir, "IMG_" + horaLocal + ".jpg");
		  return foto;
		}
	    return new File("Error");
	}
}
