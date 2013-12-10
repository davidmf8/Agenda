package com.example.agendauca;

import java.io.File;

import com.example.agendauca.R;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
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
		if(nombreDirectorios.length  != 0){	//Si se ha podido leer los directorios, se muestran
		   
		   miListaDirectorios.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nombreDirectorios));
		   registerForContextMenu(miListaDirectorios);
		   miListaDirectorios.setOnItemClickListener(new OnItemClickListener(){ //Si hacemos click en un directorio, nos mostrará la lista de archivos dentro de él
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
		else{ //Si no, se podria poner que va a una activity donde pone No hay ficheros existentes
			Toast.makeText(this, "No existen fichero o carpetas", Toast.LENGTH_SHORT).show();
			Intent cambio_actividad = new Intent();
            cambio_actividad.setClass(this, MainActivity.class);
	        startActivity(cambio_actividad);
		}
	}
	
	
	public String[] getNombreDirectorios(){ //Obtiene los nombres de los directorios para mostrarlos.
		File[] dir = getDirectorioRaiz();
		if(dir.length != 0){
			String[] nombresDir = new String[dir.length+1];
			for(int i = 0; i < dir.length; i++){
				nombresDir[i] = dir[i].getName();
			}
			nombresDir[dir.length] = "Crear directorio...";
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
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo){
	  super.onCreateContextMenu(menu, v, menuInfo);
	  MenuInflater inflater = getMenuInflater();
	  inflater.inflate(R.menu.menu_opciones_directorios, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		Intent refrescar_lista = new Intent();
		File[] directorios = getDirectorioRaiz();
	    switch (item.getItemId()) {
	        case R.id.EliminarDir:
	        	directorios[info.position].delete();
	        	if(getDirectorioRaiz().length != 0){ 
	        	  refrescar_lista.setClass(getApplicationContext(), ListarDirectorios.class);
	        	  startActivity(refrescar_lista);
	            }
	        	else{
		        	refrescar_lista.setClass(this, MainActivity.class);
		            startActivity(refrescar_lista);
	        	}
	            return true;
	        default:
	            return super.onContextItemSelected(item);
	    }
	}

}