package com.example.ficheros;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.agendauca.R;
import com.example.conexionesServidor.subirFicheroAsyntask;
import com.example.persistencia.BDAcceso;

//Clase para compartir un fichero. Elige a un usuario y se enviará el fichero a dicho usuario
public class compartirFicheroAmigo extends Activity{
	private ArrayList<String> amigos;
	private ListView miListaAmigos;
	private String rutaSubDirectorio, nombreFichero;
	private Context contexto;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		
		this.setTitle("Compartir fichero");
		
		Bundle rutaDeDirectorio = this.getIntent().getExtras();
		rutaSubDirectorio = rutaDeDirectorio.getString("Subdirectorio");
		nombreFichero = rutaDeDirectorio.getString("Fichero");
		
		listadoAmigos();
		miListaAmigos = (ListView)findViewById(R.id.ListaAmigos);
		miListaAmigos.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, amigos));

        contexto = this;
        miListaAmigos.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> adapter, View view, int posicion, long id) {
				subirFicheroAsyntask subirFichero = new subirFicheroAsyntask();
                subirFichero.inicializarValores(nombreFichero, contexto, amigos.get(posicion));
                subirFichero.execute();
			}
	   });
        
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			  Intent cambio_actividad = new Intent();
			  cambio_actividad.putExtra("Subdirectorio", rutaSubDirectorio);
			  cambio_actividad.setClass(getApplicationContext(), ListarFicheros.class);
			  startActivity(cambio_actividad);
			  finish();
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	//Recupera los usuarios agregados de la base de datos, eliminando los grupos
	private void listadoAmigos() {
		BDAcceso BDAmigos = new BDAcceso(getApplicationContext());
		BDAmigos = BDAmigos.BDopen();
		amigos = BDAmigos.getUsuarios();
		BDAmigos.BDclose();
		String amigoActual;
		for(int i = 0; i < amigos.size(); i++){
			amigoActual = amigos.get(i);
			if(amigoActual.split("/").length > 1){
				amigos.remove(i);
				i--;
			}
		}
	}
}
