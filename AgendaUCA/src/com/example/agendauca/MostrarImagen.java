package com.example.agendauca;

import java.io.File;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

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
		
		
		
	}
}
