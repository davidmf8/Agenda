package com.example.agendauca;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

//Muestra la imagen seleccionada
public class MostrarImagen extends Activity{
	ImageView miImagen;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mostrar_imagen);
		
		Bundle datosIntent = this.getIntent().getExtras();
		String rutaImagen = datosIntent.getString("Imagen");
		
		miImagen = (ImageView)findViewById(R.id.ImgFoto);
		miImagen.setImageBitmap(BitmapFactory.decodeFile(rutaImagen));
		miImagen.setScaleType(ImageView.ScaleType.FIT_XY); //Imagen a pantalla completa
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT); //No gira la pantalla
		
		
	}
}
