package com.example.agendauca;

import java.io.File;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

//Lista los ficheros que hay dentro de los directorios principales
public class ListarFicheros extends Activity{
	static int dirPrincipal = 0;
	ListView miListaFicheros;
	String[] datosFicheros;
	File[] ficheros;
	String rutaSubDirectorio;
	EditText et;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ficheros);
		miListaFicheros = (ListView)findViewById(R.id.ListaFicheros);
		Bundle rutaDeDirectorio = this.getIntent().getExtras();
		
        rutaSubDirectorio = rutaDeDirectorio.getString("Subdirectorio");
		File dir = new File(rutaSubDirectorio);
		
		if(FuncionesUtiles.estadoLectura()){
		  ficherosDir(dir);
		  miListaFicheros.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datosFicheros));
		  registerForContextMenu(miListaFicheros);
		  miListaFicheros.setOnItemClickListener(new OnItemClickListener(){
				public void onItemClick(AdapterView<?> adapter, View view, int posicion, long id) {
					if(datosFicheros[posicion] == "Crear directorio..."){ //Si se ha elegido crear directorio, se muestra un editText para introducir su nombre
						crearCarpeta();
					}
					else{
					  if(ficheros[posicion].isDirectory()){
						  Intent cambio_actividad = new Intent();
						  cambio_actividad.putExtra("Subdirectorio", ficheros[posicion].getAbsolutePath());
						  dirPrincipal++;
						  cambio_actividad.setClass(getApplicationContext(), ListarFicheros.class);
						  startActivity(cambio_actividad);
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

				}
		   });
		}
		else{
			Toast.makeText(this, "Memoria externa no disponible", Toast.LENGTH_SHORT).show();
			Intent cambio_actividad = new Intent();
            cambio_actividad.setClass(this, MenuInicial.class);
	        startActivity(cambio_actividad);
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			int pos = rutaSubDirectorio.lastIndexOf("/");
			String rutaAnterior = rutaSubDirectorio.substring(0, pos);
		    if(dirPrincipal == 0){
		    	Intent cambio_actividad = new Intent();
				cambio_actividad.setClass(getApplicationContext(), ListarDirectorios.class);
				startActivity(cambio_actividad);
		    }
		    else{
		      dirPrincipal--;
			  Intent cambio_actividad = new Intent();
			  cambio_actividad.putExtra("Subdirectorio", rutaAnterior);
			  cambio_actividad.setClass(getApplicationContext(), ListarFicheros.class);
			  startActivity(cambio_actividad);
		    }
		}
		return super.onKeyDown(keyCode, event);
	}
	
	private void ficherosDir(File dir) {
		ficheros = dir.listFiles();
		  datosFicheros = new String[ficheros.length+1];
		  for(int i = 0; i < ficheros.length; i++){
			  datosFicheros[i] = ficheros[i].getName();
		  }
		  datosFicheros[ficheros.length] = "Crear directorio...";
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
	        case R.id.Renombrar:
	        	renombrar(info.position);	
	        	refrescar_lista.putExtra("Subdirectorio", rutaSubDirectorio);
	        	refrescar_lista.setClass(getApplicationContext(), ListarFicheros.class);
	            return true;
	        case R.id.Eliminar:
	        	if(!ficheros[info.position].isDirectory()){ //Si no es un directorio, se borra el fichero
	        		ficheros[info.position].delete();
	        	}
	        	else{ //Si es un directorio, se borra todo su contenido
	        		borrarDirectorio(ficheros[info.position]);	
	        	}
	        	
	        	File nuevos_archivos = new File (rutaSubDirectorio);
	        	if(nuevos_archivos.listFiles().length != 0){ 
	        		refrescar_lista.putExtra("Subdirectorio", rutaSubDirectorio);
	        		refrescar_lista.setClass(getApplicationContext(), ListarFicheros.class);
	        		startActivity(refrescar_lista);
	        	}
	        	else{
	        		nuevos_archivos.delete();
	        		refrescar_lista.setClass(getApplicationContext(), ListarDirectorios.class);
	        		startActivity(refrescar_lista);
	        	}       	
	            return true;
	        case R.id.Mover: 
	        	Intent cambio_actividad = new Intent();
				cambio_actividad.putExtra("Mover", ficheros[info.position].getAbsolutePath());
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
        et = new EditText(this);
        dialogo.setTitle("Nuevo nombre");
        dialogo.setView(et);

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
        	@Override
        	public void onClick(DialogInterface dialog, int which) {
        		File renombrado = null;
        		String nombreFichero = ficheros[posicionFichero].getName();
        		if(nombreFichero.indexOf(".jpg") != -1){
        			renombrado = new File(rutaSubDirectorio, et.getText().toString() + ".jpg");
				}
				if(nombreFichero.indexOf(".mp4") != -1){
					renombrado = new File(rutaSubDirectorio, et.getText().toString() + ".mp4");
				}
				if( nombreFichero.indexOf(".3gp") != -1){
					renombrado = new File(rutaSubDirectorio, et.getText().toString() + ".3gp");
				}
				if(nombreFichero.indexOf(".txt") != -1){
					renombrado = new File(rutaSubDirectorio, et.getText().toString() + ".txt");
				}
				
				if(renombrado != null){
	        	  ficheros[posicionFichero].renameTo(renombrado);
				}
				else{
	        		renombrado = new File(rutaSubDirectorio, et.getText().toString());
		        	ficheros[posicionFichero].renameTo(renombrado);
				}
				
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
        	  File dir = new File(rutaSubDirectorio,  et.getText().toString()); 
      		  if(!dir.exists()){
      			dir.mkdir();
      		  }
			  Intent cambio_actividad = new Intent();
			  cambio_actividad.setClass(getApplicationContext(), ListarFicheros.class);
			  cambio_actividad.putExtra("Subdirectorio", rutaSubDirectorio);
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
