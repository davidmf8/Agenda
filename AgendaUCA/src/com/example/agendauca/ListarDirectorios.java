package com.example.agendauca;

import java.io.File;

import com.example.agendauca.R;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;


public class ListarDirectorios extends Activity{
	ListView miLista;
	String[] datos;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gestionficheros);
		miLista = (ListView)findViewById(R.id.listaDir);
		datos = getNombreDirectorios();
		if(datos.length  != 0)
		  miLista.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datos));
		else{
			Toast.makeText(this, "Memoria externa no disponible", Toast.LENGTH_SHORT).show();
			Intent cambio_actividad = new Intent();
            cambio_actividad.setClass(this, MainActivity.class);
	        startActivity(cambio_actividad);
		}
	}
	
	public String[] getNombreDirectorios(){
		File[] dir = getDirectorioRaiz();
		if(dir.length != 0){
			String[] nombresDir = new String[dir.length];
			for(int i = 0; i < dir.length; i++){
				nombresDir[i] = dir[i].getName();
			}
			return nombresDir;
		}	
		return new String[0];
	}
	
	public File[] getDirectorioRaiz(){
		if(FuncionesUtiles.estadoLectura()){
		   File directorioPrincipal = getExternalFilesDir(null);
		   File[] misCarpetas = directorioPrincipal.listFiles();
		   return misCarpetas;
		}
		return new File[0];
	}
}