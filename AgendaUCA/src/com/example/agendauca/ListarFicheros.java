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
		  ficheros = dir.listFiles();
		  datosFicheros = new String[ficheros.length];
		  for(int i = 0; i < ficheros.length; i++){
			  datosFicheros[i] = ficheros[i].getName();
		  }
		  miListaFicheros.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, datosFicheros));
		  registerForContextMenu(miListaFicheros);
		  miListaFicheros.setOnItemClickListener(new OnItemClickListener(){
				public void onItemClick(AdapterView<?> adapter, View view, int posicion, long id) {
					if(ficheros[posicion].isDirectory()){
						//Mas tarde, no se si se permitiran carpetas dentro de carpetas
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
	        	mostrarEditText(info.position);	
	        	refrescar_lista.putExtra("Subdirectorio", rutaSubDirectorio);
	        	refrescar_lista.setClass(getApplicationContext(), ListarFicheros.class);
	            return true;
	        case R.id.Eliminar:
	        	ficheros[info.position].delete();
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
	        	return true;
	        default:
	            return super.onContextItemSelected(item);
	    }
	}
	
	
	
	private void mostrarEditText(final int posicionFichero){
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
