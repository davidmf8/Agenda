package com.example.agendauca;

import com.example.agendauca.R;

import android.os.Bundle;
import android.app.Activity;
import android.app.TabActivity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View; 
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TabHost;

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
		/*TabHost host = getTabHost();
        //Añadimos cada tab, que al ser pulsadas abren sus respectivas Activities
        host.addTab(host.newTabSpec("tab_1").setIndicator("TAB1").setContent(new Intent(this, ListarDirectorios.class)));
        host.addTab(host.newTabSpec("tab_2").setIndicator("TAB2").setContent(new Intent(this, ListarDirectorios.class)));*/
		
		
		
		gestion_archivo = (ImageButton)findViewById(R.id.Directorios);
		gestion_archivo.setOnClickListener(this);

		eventos = (ImageButton)findViewById(R.id.Evento);
		eventos.setOnClickListener(this);
		
		notificaciones = (ImageButton)findViewById(R.id.Notificacion);
		notificaciones.setOnClickListener(this);
		
		calificaciones = (ImageButton)findViewById(R.id.Calificaciones);
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
		//Dependiendo del boton que se pulse, nos llevará a una funcionalidad u otra: ver directorios, eventos, calificaciones y notificaciones
		Intent cambio_actividad = new Intent();
		switch(v.getId()){
		   case R.id.Directorios:
			   cambio_actividad.setClass(this, ListarDirectorios.class);
			   startActivity(cambio_actividad);
		       break;
		}
	}
	
	@Override
	 public boolean onOptionsItemSelected(MenuItem item) {
		Intent cambio_actividad = new Intent();
		//Para los botones de crear un archivo: foto, video, grabación, nota.
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
	        	 cambio_actividad.setClass(this, Audio.class);
				 startActivity(cambio_actividad);
	        	 break;
	         case R.id.Notas:
	        	 cambio_actividad.setClass(this, BlocNotas.class);
				 startActivity(cambio_actividad);
	        	 break;
	     } 
	  return false;
	 }
}
