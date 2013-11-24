package com.example.agendauca;

import java.io.File;

import com.example.agendauca.R;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;

//Lista los directorios principales de la aplicación
public class ListarDirectorios extends Activity{
	ListView miListaDirectorios;
	String[] nombreDirectorios;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gestionficheros);
		miListaDirectorios = (ListView)findViewById(R.id.listaDir);
		nombreDirectorios = getNombreDirectorios(); //Obtenemos los nombres de los directorios.
		if(nombreDirectorios.length  != 0){	//Si se ha podido leer los directorios
		   miListaDirectorios.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nombreDirectorios));
		   miListaDirectorios.setOnItemClickListener(new OnItemClickListener(){
				public void onItemClick(AdapterView<?> adapter, View view, int posicion, long id) {
					File[] seleccionDirectorio = getDirectorioRaiz();
					Intent cambio_actividad = new Intent();
					String ruta = seleccionDirectorio[posicion].getAbsolutePath();
					cambio_actividad.putExtra("Subdirectorio", ruta);
					cambio_actividad.setClass(getApplicationContext(), ListarFicheros.class);
					startActivity(cambio_actividad);	
				}
		   });
	    }
		else{ //Si no
			Toast.makeText(this, "Memoria externa no disponible", Toast.LENGTH_SHORT).show();
			Intent cambio_actividad = new Intent();
            cambio_actividad.setClass(this, MainActivity.class);
	        startActivity(cambio_actividad);
		}
	}
	
	
	public String[] getNombreDirectorios(){ //Obtiene los nombres de los directorios.
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
	
	public File[] getDirectorioRaiz(){ //Obtiene los directorios de la raiz de la aplicación
		if(FuncionesUtiles.estadoLectura()){
		   File directorioPrincipal = getExternalFilesDir(null);
		   File[] misCarpetas = directorioPrincipal.listFiles();
		   return misCarpetas;
		}
		return new File[0];
	}

}