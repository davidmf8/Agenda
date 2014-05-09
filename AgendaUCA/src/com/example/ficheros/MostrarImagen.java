package com.example.ficheros;

import com.example.agendauca.ListarFicheros;
import com.example.agendauca.R;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;

//Muestra la imagen seleccionada
public class MostrarImagen extends Activity{
	ImageView miImagen;
	String rutaImagen;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mostrar_imagen);
		
		Bundle datosIntent = this.getIntent().getExtras();
		rutaImagen = datosIntent.getString("Imagen");
		
		miImagen = (ImageView)findViewById(R.id.ImgFoto);
		miImagen.setImageBitmap(BitmapFactory.decodeFile(rutaImagen));
		miImagen.setScaleType(ImageView.ScaleType.FIT_XY); //Imagen a pantalla completa
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //No gira la pantalla	
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			int pos = rutaImagen.lastIndexOf("/");
			String rutaAnterior = rutaImagen.substring(0, pos);
		    Intent cambio_actividad = new Intent();
		    cambio_actividad.putExtra("Subdirectorio", rutaAnterior);
			cambio_actividad.setClass(getApplicationContext(), ListarFicheros.class);
			startActivity(cambio_actividad);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
