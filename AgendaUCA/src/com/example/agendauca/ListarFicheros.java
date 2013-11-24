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
						//Mas tarde
					}
					else{
						File ficheroSeleccionado = ficheros[posicion];
						String nombreFichero = ficheroSeleccionado.getName();
						if(nombreFichero.indexOf(".jpg") != -1){
							Intent mostrar_foto = new Intent();
							mostrar_foto.putExtra("Imagen", nombreFichero);
							mostrar_foto.setClass(getApplicationContext(), MostrarImagen.class);
							startActivity(mostrar_foto);
						}
						if(nombreFichero.indexOf(".mp4") != -1){
							//Muestra video
						}
						if(nombreFichero.indexOf(".3gp") != -1){
							//Muestra audio
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
