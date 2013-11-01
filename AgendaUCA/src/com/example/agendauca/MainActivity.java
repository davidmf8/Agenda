package com.example.agendauca;

import com.example.agendauca.R;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View; 
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class MainActivity extends Activity implements OnClickListener{
	private ImageButton gestion_archivo;
	private ImageButton eventos;
	private ImageButton notificaciones;
	private ImageButton calificaciones;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//Primera pantalla y los botones están a la escucha de una acción
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		gestion_archivo = (ImageButton)findViewById(R.id.imageButton2);
		gestion_archivo.setOnClickListener(this);

		eventos = (ImageButton)findViewById(R.id.imageButton3);
		eventos.setOnClickListener(this);
		
		notificaciones = (ImageButton)findViewById(R.id.imageButton4);
		notificaciones.setOnClickListener(this);
		
		calificaciones = (ImageButton)findViewById(R.id.imageButton5);
		calificaciones.setOnClickListener(this);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Carga el action bar, para el "menu" de la creacion de fotos videos...
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
	

	@Override
	public void onClick(View v) {
		//Dependiendo del boton que se pulse, nos llevará a una funcionalidad u otra
		Intent cambio_actividad = new Intent();
		switch(v.getId()){
		   case R.id.imageButton2:
			   cambio_actividad.setClass(this, GestionFicheros.class);
			   startActivity(cambio_actividad);
		       break;
		}
	}
	
	@Override
	 public boolean onOptionsItemSelected(MenuItem item) {
		Intent cambio_actividad = new Intent();
		//Para los botonesde crear un archivo: foto, video o grabación.
	     switch (item.getItemId()) {
	         case R.id.foto:
	        	 cambio_actividad.setClass(this, Camara.class);
				 startActivity(cambio_actividad);
	             break;
	         case R.id.Video:
	        	 cambio_actividad.setClass(this, Video.class);
				 startActivity(cambio_actividad);
	        	 break;
	         case R.id.Audio:
	        	 break;
	     } 
	  return false;
	 }

}
