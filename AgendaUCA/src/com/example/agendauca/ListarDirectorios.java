package com.example.agendauca;

import java.io.File;

import variables.comunes.FuncionesUtiles;

import com.example.agendauca.R;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
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
					if(nombreDirectorios[posicion] == "Crear directorio..."){ //Si se ha elegido crear directorio, se muestra un editText para introducir su nombre
						 crearCarpeta();
					}
					else{ //Si haelegido un directorio pincipal
					  File[] seleccionDirectorio = getDirectorioRaiz();
					  Intent cambio_actividad = new Intent();
					  String ruta = seleccionDirectorio[posicion].getAbsolutePath();
					  cambio_actividad.putExtra("Subdirectorio", ruta);
					  cambio_actividad.setClass(getApplicationContext(), ListarFicheros.class);
					  startActivity(cambio_actividad);
					  finish();
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
			Intent cambio_actividad = new Intent();
			cambio_actividad.setClass(getApplicationContext(), MenuInicial.class);
			startActivity(cambio_actividad);
			finish();
		}
		return super.onKeyDown(keyCode, event);
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
		String noExistenDir[] = new String[1];
		noExistenDir[0] =  "Crear directorio...";
		return noExistenDir;
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
	  inflater.inflate(R.menu.menu_opciones_listas, menu);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		Intent refrescar_lista = new Intent();
	    switch (item.getItemId()) {
	        case R.id.Eliminar:
	        	File[] directorios = getDirectorioRaiz();
	        	borrarDirectorio(directorios[info.position]);
	        	if(getDirectorioRaiz().length != 0){ 
	        	  refrescar_lista.setClass(getApplicationContext(), ListarDirectorios.class);
	        	  startActivity(refrescar_lista);
	            }
	        	else{
		        	refrescar_lista.setClass(this, MenuInicial.class);
		            startActivity(refrescar_lista);
	        	}
	            return true;
	        case R.id.Renombrar:
	        	renombrar(info.position);
	        	return true;
	        case R.id.Mover:
	        	File[] raiz = getDirectorioRaiz(); 
	        	Intent cambio_actividad = new Intent();
				cambio_actividad.putExtra("Mover", raiz[info.position].getAbsolutePath());
				cambio_actividad.setClass(getApplicationContext(), moverDirFich.class);
				startActivity(cambio_actividad);
	        	return true;
	        default:
	            return super.onContextItemSelected(item);
	    }
	}
	
	private void borrarDirectorio(File directorioABorrar) {
		File[] ficherosDir = directorioABorrar.listFiles();
		for(int i = 0; i < ficherosDir.length; i++){
			if(ficherosDir[i].isDirectory())
				borrarDirectorio(ficherosDir[i]);
			else
				ficherosDir[i].delete();		
		}
		
		directorioABorrar.delete();
	}
	
	private void renombrar(final int posicionFichero){
        AlertDialog.Builder dialogo = new Builder(this);
        final EditText et = new EditText(this);
        dialogo.setTitle("Nuevo nombre");
        dialogo.setView(et);

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
        	@Override
        	public void onClick(DialogInterface dialog, int which) {
        		File[] directorios = getDirectorioRaiz();
        		File renombrado = null;
        		renombrado = new File(getExternalFilesDir(null) + et.getText().toString());
	        	directorios[posicionFichero].renameTo(renombrado);
				
        	}
        });
        dialogo.setNegativeButton("Cancelar",new DialogInterface.OnClickListener() {
        	@Override
        	public void onClick(DialogInterface dialog, int which) {
                     // 5.2. Accion boton Cancelar
        	}
        });


        dialogo.create();
        dialogo.show();

	}
	
	private void crearCarpeta(){
        AlertDialog.Builder dialogo = new Builder(this);
        final EditText et = new EditText(this);
        dialogo.setTitle("Nueva Carpeta");
        dialogo.setView(et);

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
        	@Override
        	public void onClick(DialogInterface dialog, int which) {
        	  File dir = new File(getExternalFilesDir(null),  et.getText().toString()); 
      		  if(!dir.exists()){
      			dir.mkdir();
      		  }
			  Intent cambio_actividad = new Intent();
			  cambio_actividad.setClass(getApplicationContext(), ListarDirectorios.class);
			  startActivity(cambio_actividad);

        	}
        });
        dialogo.setNegativeButton("Cancelar",new DialogInterface.OnClickListener() {
        	@Override
        	public void onClick(DialogInterface dialog, int which) {
                     // 5.2. Accion boton Cancelar
        	}
        });


        dialogo.create();
        dialogo.show();
	}

}