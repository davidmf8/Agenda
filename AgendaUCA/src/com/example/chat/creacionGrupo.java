package com.example.chat;

import java.util.ArrayList;

import com.example.agendauca.R;
import com.example.conexionesServidor.EnviarMensajeAsynTask;
import com.example.utilidades.FuncionesUtiles;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class creacionGrupo extends Activity{
	private ArrayList<String> amigos, amigosSeleccionados;
	private ListView miListaAmigos;
	private String miembrosGrupo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		//Activity que muestra una lista con los usuarios que se pueden seleccionar
		//para crear un grupo de personas
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seleccion_grupo);
		
		this.setTitle("Nuevo grupo");
		
		Bundle datosAmigos = this.getIntent().getExtras();
		amigos = datosAmigos.getStringArrayList("ListaAmigos");
		
		eliminarGrupos();
		
		amigosSeleccionados = new ArrayList<String>();
		miListaAmigos = (ListView)findViewById(R.id.ListaSeleccionable);
        miListaAmigos.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, amigos));
        miListaAmigos.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        miListaAmigos.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> adapter, View view, int posicion, long id) {
				if(isSeleccionado(amigos.get(posicion))){
					miListaAmigos.setItemChecked(posicion, false);
					amigosSeleccionados.remove(amigos.get(posicion));
				}
				else{
					miListaAmigos.setItemChecked(posicion, true);
					amigosSeleccionados.add(amigos.get(posicion));
				}
			}
	   });
	}
	
	//Se descartan los grupos, para que no puedan ser seleccionados
	private void eliminarGrupos() {
		String amigoActual;
		for(int i = 0; i < amigos.size(); i++){
			amigoActual = amigos.get(i);
			if(amigoActual.startsWith("Grupo:")){
				amigos.remove(i);
				i--;
				//Log.d("ENTRA", amigoActual);
			}
		}
	}

	//Boton atras
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent cambio_actividad = new Intent();
		    cambio_actividad.setClass(getApplicationContext(), chatPrincipal.class);
		    startActivity(cambio_actividad);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	//Envia a los demás usuario  que se ha añadido a un nuevo grupo
	public void onClick(View v){
		if(amigosSeleccionados.size() > 1){
			miembrosGrupo = amigosSeleccionados.get(0);
			for(int i = 1; i < amigosSeleccionados.size(); i++){
				miembrosGrupo = miembrosGrupo + "/" + amigosSeleccionados.get(i);
			}
			SharedPreferences misPreferencias = getSharedPreferences(FuncionesUtiles.getPreferencias(), MODE_PRIVATE);
			String miUsuario = misPreferencias.getString(FuncionesUtiles.getUsuario(), "");
			miembrosGrupo = miembrosGrupo + "/" + miUsuario; //Añadimos nuestro propio usuario para completar el grupo
			//Log.d("Usuarios", miembrosGrupo);
			String mensaje = "NuevoGrupo";
			EnviarMensajeAsynTask enviarMensaje = new EnviarMensajeAsynTask();
			enviarMensaje.inicilizarValores(miembrosGrupo, mensaje, this, false);
			enviarMensaje.execute();
		}
		else
			Toast.makeText(this, "Seleccione 2 o más usuarios", Toast.LENGTH_SHORT).show();
	}
	
	//Devuelve si un elemento de la listadeamigos ha sido seleccionado o no
	private boolean isSeleccionado(String nombreSeleccionado) {
		return amigosSeleccionados.contains(nombreSeleccionado);
	}
}
