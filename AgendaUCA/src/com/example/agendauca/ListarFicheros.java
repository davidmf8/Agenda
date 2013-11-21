package com.example.agendauca;

import java.io.File;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

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
		}
		else{
			Toast.makeText(this, "Memoria externa no disponible", Toast.LENGTH_SHORT).show();
			Intent cambio_actividad = new Intent();
            cambio_actividad.setClass(this, MainActivity.class);
	        startActivity(cambio_actividad);
		}
	}
}
