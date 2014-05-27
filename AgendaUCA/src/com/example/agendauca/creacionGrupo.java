package com.example.agendauca;

import java.util.ArrayList;

import com.example.conexionesServidor.EnviarMensajeAsynTask;
import com.example.utilidades.FuncionesUtiles;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class creacionGrupo extends Activity{
	private ArrayList<String> amigos, amigosSeleccionados;
	private ListView miListaAmigos;
	private String miembrosGrupo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seleccion_grupo);
		
		this.setTitle("Nuevo grupo");
		
		Bundle datosAmigos = this.getIntent().getExtras();
		amigos = datosAmigos.getStringArrayList("ListaAmigos");
		
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
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent cambio_actividad = new Intent();
		    cambio_actividad.setClass(getApplicationContext(), chatPrincipal.class);
		    startActivity(cambio_actividad);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	public void onClick(View v){
		if(amigosSeleccionados.size() > 1){
			miembrosGrupo = amigosSeleccionados.get(0);
			for(int i = 1; i < amigosSeleccionados.size(); i++){
				miembrosGrupo = miembrosGrupo + "/" + amigosSeleccionados.get(i);
			}
			SharedPreferences misPreferencias = getSharedPreferences(FuncionesUtiles.getPreferencias(), MODE_PRIVATE);
			String miUsuario = misPreferencias.getString(FuncionesUtiles.getUsuario(), "");
			miembrosGrupo = miembrosGrupo + "/" + miUsuario;
			//Log.d("USERS", miembrosGrupo);
			String mensaje = "NuevoGrupo";
			EnviarMensajeAsynTask enviarMensaje = new EnviarMensajeAsynTask();
			enviarMensaje.inicilizarValores(miembrosGrupo, mensaje, this, false);
			enviarMensaje.execute();
		}
	}
	
	private boolean isSeleccionado(String nombreSeleccionado) {
		return amigosSeleccionados.contains(nombreSeleccionado);
	}
}
