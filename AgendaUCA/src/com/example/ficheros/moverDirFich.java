package com.example.ficheros;

import java.io.File;

import com.example.agendauca.MenuInicial;
import com.example.agendauca.R;
import com.example.agendauca.R.id;
import com.example.agendauca.R.layout;
import com.example.utilidades.FuncionesUtiles;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class moverDirFich extends Activity{
	static int profundidadDir = 0;
	String rutaSubDirectorio;
	String rutaDirMover;
	ListView miListaDirectorios;
	String[] nombreDirectorios;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ficheros);
		miListaDirectorios = (ListView)findViewById(R.id.ListaFicheros);
		nombreDirectorios = getNombreDirectorios(); //Obtenemos los nombres de los directorios.
		if(nombreDirectorios.length  != 0){	//Si se ha podido leer los directorios, se muestran
		   miListaDirectorios.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nombreDirectorios));
		   miListaDirectorios.setOnItemClickListener(new OnItemClickListener(){ //Si hacemos click en un directorio, nos mostrará la lista de archivos dentro de él
				public void onItemClick(AdapterView<?> adapter, View view, int posicion, long id) {
					if(nombreDirectorios[posicion] == "Mover a directorio actual..."){ //Si se ha elegido crear directorio, se muestra un editText para introducir su nombre
						File dirMover = new File(rutaDirMover);
						File nuevaRuta = new File(rutaSubDirectorio);
						//if(dirMover.isDirectory()){// || rutaSubDirectorio != getExternalFilesDir(null).getAbsolutePath()){
						   dirMover.renameTo(new File(nuevaRuta, dirMover.getName()));
						//}

						profundidadDir = 0;
						Intent cambio_actividad = new Intent();
					    cambio_actividad.setClass(getApplicationContext(), ListarFicheros.class);
					    cambio_actividad.putExtra("Subdirectorio", getExternalFilesDir(null).getAbsolutePath());
					    startActivity(cambio_actividad);
					    finish();
					}
					else{
					  File[] seleccionDirectorio = getDirectorio();
					  if(seleccionDirectorio[posicion].isDirectory()){
					    Intent cambio_actividad = new Intent();
					    String ruta = seleccionDirectorio[posicion].getAbsolutePath();
					    profundidadDir++;
					    cambio_actividad.putExtra("Subdirectorio", ruta);
					    cambio_actividad.putExtra("Mover", rutaDirMover);
					    cambio_actividad.setClass(getApplicationContext(), moverDirFich.class);
					    startActivity(cambio_actividad);
					    finish();
					  }
					}
					
				}
		   });
	    }
		else{ //Si no, se podria poner que va a una activity donde pone No hay ficheros existentes
			Toast.makeText(this, "No existen fichero o carpetas", Toast.LENGTH_SHORT).show();
			Intent cambio_actividad = new Intent();
            cambio_actividad.setClass(this, MenuInicial.class);
	        startActivity(cambio_actividad);
	        finish();
		}
		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			int pos = rutaSubDirectorio.lastIndexOf("/");
			String rutaAnterior = rutaSubDirectorio.substring(0, pos);
		    if(profundidadDir == 0){
		    	Intent cambio_actividad = new Intent();
				cambio_actividad.setClass(getApplicationContext(), MenuInicial.class);
				startActivity(cambio_actividad);
				finish();
		    }
		    else{
		      profundidadDir--;
			  Intent cambio_actividad = new Intent();
			  cambio_actividad.putExtra("Subdirectorio", rutaAnterior);
			  cambio_actividad.setClass(getApplicationContext(), moverDirFich.class);
			  startActivity(cambio_actividad);
			  finish();
		    }
		    
		}
		return super.onKeyDown(keyCode, event);
	}
	
	
	public String[] getNombreDirectorios(){ //Obtiene los nombres de los directorios para mostrarlos.
		File[] dir = getDirectorio();
		if(dir.length != 0){
			String[] nombresDir = new String[dir.length+1];
			for(int i = 0; i < dir.length; i++){
				nombresDir[i] = dir[i].getName();
			}
			nombresDir[dir.length] = "Mover a directorio actual...";
			return nombresDir;
		}	
		String noExistenDir[] = new String[1];
		noExistenDir[0] =  "Mover a directorio actual...";
		return noExistenDir;
	}
	
	public File[] getDirectorio(){ //Obtiene los directorios de la raiz de la aplicación
		Bundle rutaDeDirectorio = this.getIntent().getExtras();
		if(FuncionesUtiles.estadoLectura()){
			File[] misCarpetas;
			rutaDirMover = rutaDeDirectorio.getString("Mover");
		   if(profundidadDir == 0){
			 rutaSubDirectorio = getExternalFilesDir(null).getAbsolutePath();
		     File directorioPrincipal = getExternalFilesDir(null);
		     misCarpetas = directorioPrincipal.listFiles();
		   }
		   else{
               rutaSubDirectorio = rutaDeDirectorio.getString("Subdirectorio");
               File directorioPrincipal = new File(rutaSubDirectorio);
  		       misCarpetas = directorioPrincipal.listFiles();
		   }
		   return misCarpetas;
		}
		return new File[0];
	}
}
