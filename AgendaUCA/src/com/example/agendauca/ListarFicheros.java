package com.example.agendauca;

import java.io.File;

import com.example.ficheros.Audio;
import com.example.ficheros.BlocNotas;
import com.example.ficheros.Camara;
import com.example.ficheros.MostrarImagen;
import com.example.ficheros.MostrarNota;
import com.example.ficheros.ReproducirVideo;
import com.example.ficheros.Video;
import com.example.utilidades.FuncionesUtiles;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.KeyEvent;
import android.view.Menu;
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
	EditText renombrar;
	
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
						  finish();
					  }
					  else{
						File ficheroSeleccionado = ficheros[posicion];
						String nombreFichero = ficheroSeleccionado.getName();
						Intent mostrarTipoFichero = new Intent();
						//Segun el tipo de fichero, se mostrar� de la mejor forma (video, audio, imagen, nota)
						if(nombreFichero.indexOf(".jpg") != -1){
							mostrarTipoFichero.putExtra("Imagen", ficheros[posicion].getAbsolutePath());
							mostrarTipoFichero.setClass(getApplicationContext(), MostrarImagen.class);
							startActivity(mostrarTipoFichero);
						}
						if(nombreFichero.indexOf(".mp4") != -1 || nombreFichero.indexOf(".3gp") != -1){
							mostrarTipoFichero.putExtra("ArchivoReproducir", ficheros[posicion].getAbsolutePath());
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
	        finish();
		}
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Carga el action bar, para el "menu" de la creacion de fotos videos...
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
	
	@Override
	 public boolean onOptionsItemSelected(MenuItem item) {
		Intent cambio_actividad = new Intent();
		//Para los botones de crear un archivo: foto, video, grabaci�n, nota.
	     switch (item.getItemId()) {
	         case R.id.foto:
	        	 cambio_actividad.setClass(this, Camara.class);
	        	 cambio_actividad.putExtra("CarpetaDestino",  rutaSubDirectorio);
				 startActivity(cambio_actividad);
	             break;
	         case R.id.Video:
	        	 cambio_actividad.setClass(this, Video.class);
	        	 cambio_actividad.putExtra("CarpetaDestino",  rutaSubDirectorio);
				 startActivity(cambio_actividad);
	        	 break;
	         case R.id.Audio:
	        	 cambio_actividad.setClass(this, Audio.class);
	        	 cambio_actividad.putExtra("CarpetaDestino",  rutaSubDirectorio);
				 startActivity(cambio_actividad);
	        	 break;
	         case R.id.Notas:
	        	 cambio_actividad.setClass(this, BlocNotas.class);
	        	 cambio_actividad.putExtra("CarpetaDestino",  rutaSubDirectorio);
				 startActivity(cambio_actividad);
	        	 break;
	     } 
	  return false;
	 }
	
	/*public void onStop(){
        //finish();
		super.onStop();
	}*/
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			int pos = rutaSubDirectorio.lastIndexOf("/");
			String rutaAnterior = rutaSubDirectorio.substring(0, pos);
		    if(dirPrincipal == 0){
		    	Intent cambio_actividad = new Intent();
				cambio_actividad.setClass(getApplicationContext(), MenuInicial.class);
				startActivity(cambio_actividad);
				finish();
		    }
		    else{
		      dirPrincipal--;
			  Intent cambio_actividad = new Intent();
			  cambio_actividad.putExtra("Subdirectorio", rutaAnterior);
			  cambio_actividad.setClass(getApplicationContext(), ListarFicheros.class);
			  startActivity(cambio_actividad);
			  finish();
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
	        		finish();
	        	}
	        	else{
	        		refrescar_lista.putExtra("Subdirectorio", rutaSubDirectorio);
	        		refrescar_lista.setClass(getApplicationContext(), ListarFicheros.class);
	        		startActivity(refrescar_lista);
	        		finish();
	        	}       	
	            return true;
	        case R.id.Mover: 
	        	dirPrincipal = 0;
	        	Intent cambio_actividad = new Intent();
				cambio_actividad.putExtra("Mover", ficheros[info.position].getAbsolutePath());
				cambio_actividad.setClass(getApplicationContext(), moverDirFich.class);
				startActivity(cambio_actividad);
				finish();
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
        renombrar = new EditText(this);
        dialogo.setTitle("Nuevo nombre");
        dialogo.setView(renombrar);

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
        	@Override
        	public void onClick(DialogInterface dialog, int which) {
        		File renombrado = null;
        		String nombreFichero = ficheros[posicionFichero].getName();
        		if(nombreFichero.indexOf(".jpg") != -1){
        			renombrado = new File(rutaSubDirectorio, renombrar.getText().toString() + ".jpg");
				}
				if(nombreFichero.indexOf(".mp4") != -1){
					renombrado = new File(rutaSubDirectorio, renombrar.getText().toString() + ".mp4");
				}
				if( nombreFichero.indexOf(".3gp") != -1){
					renombrado = new File(rutaSubDirectorio, renombrar.getText().toString() + ".3gp");
				}
				if(nombreFichero.indexOf(".txt") != -1){
					renombrado = new File(rutaSubDirectorio, renombrar.getText().toString() + ".txt");
				}
				
				if(renombrado != null){
	        	  ficheros[posicionFichero].renameTo(renombrado);
				}
				else{
	        		renombrado = new File(rutaSubDirectorio, renombrar.getText().toString());
		        	ficheros[posicionFichero].renameTo(renombrado);
				}
				
				Intent refrescar_lista = new Intent();
				refrescar_lista .putExtra("Subdirectorio", rutaSubDirectorio);
        		refrescar_lista.setClass(getApplicationContext(), ListarFicheros.class);
        		startActivity(refrescar_lista);
        		finish();
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
			  finish();
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
