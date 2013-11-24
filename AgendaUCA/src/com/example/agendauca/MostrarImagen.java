package com.example.agendauca;

import android.app.Activity;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

public class MostrarImagen extends Activity{
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//Primera pantalla y los botones est�n a la escucha de una acci�n
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mostrar_imagen);
		Bundle datosIntent = this.getIntent().getExtras();
		String rutaImagen = datosIntent.getString("Imagen");
		
		ImageView miImagen = (ImageView)findViewById(R.id.ImgFoto);
		miImagen.setImageBitmap(BitmapFactory.decodeFile(rutaImagen));
		
	}
}
