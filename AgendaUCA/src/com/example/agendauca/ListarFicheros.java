package com.example.agendauca;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

//Lista los ficheros que hay dentro de los directorios principales
public class ListarFicheros extends Activity{
	ListView miListaFicheros;
	String[] datosFicheros;
	File[] ficheros;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ficheros);
		miListaFicheros = (ListView)findViewById(R.id.ListaFicheros);
		Bundle rutaDeDirectorio = this.getIntent().getExtras();

		File dir = new File(rutaDeDirectorio.getString("Subdirectorio"));
		
		if(FuncionesUtiles.estadoLectura()){
		  ficheros = dir.listFiles();
		  datosFicheros = new String[ficheros.length];
		  for(int i = 0; i < ficheros.length; i++){
			  datosFicheros[i] = ficheros[i].getName();
		  }
		  miListaFicheros.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datosFicheros));
		  miListaFicheros.setOnItemClickListener(new OnItemClickListener(){
				public void onItemClick(AdapterView<?> adapter, View view, int posicion, long id) {
					if(ficheros[posicion].isDirectory()){
						//Mas tarde, no se si se permitiran carpetas dentro de carpetas
					}
					else{
						File ficheroSeleccionado = ficheros[posicion];
						String nombreFichero = ficheroSeleccionado.getName();
						Intent mostrarTipoFichero = new Intent();
						//Segun el tipo de fichero, se mostrará de la mejor forma (video, audio, imagen, nota)
						if(nombreFichero.indexOf(".jpg") != -1){
							mostrarTipoFichero.putExtra("Imagen", ficheros[posicion].getAbsolutePath());
							mostrarTipoFichero.setClass(getApplicationContext(), MostrarImagen.class);
							startActivity(mostrarTipoFichero);
						}
						if(nombreFichero.indexOf(".mp4") != -1 || nombreFichero.indexOf(".3gp") != -1){
							mostrarTipoFichero.putExtra("AchivoReproducir", ficheros[posicion].getAbsolutePath());
							mostrarTipoFichero.setClass(getApplicationContext(), ReproducirVideo.class);
							startActivity(mostrarTipoFichero);
						}
						if(nombreFichero.indexOf(".txt") != -1){
							mostrarTipoFichero.putExtra("LecturaNota", ficheros[posicion].getAbsolutePath());
							mostrarTipoFichero.setClass(getApplicationContext(), MostrarNota.class);
							startActivity(mostrarTipoFichero);
						}
					}

				}
		   });
		}
		else{
			Toast.makeText(this, "Memoria externa no disponible", Toast.LENGTH_SHORT).show();
			Intent cambio_actividad = new Intent();
            cambio_actividad.setClass(this, MainActivity.class);
	        startActivity(cambio_actividad);
		}
	}
}
