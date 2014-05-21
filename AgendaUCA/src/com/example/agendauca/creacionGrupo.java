package com.example.agendauca;

import java.util.ArrayList;

import com.example.conexionesServidor.EnviarMensajeAsynTask;
import com.example.persistencia.BDAcceso;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class creacionGrupo extends Activity{
	private ArrayList<String> amigos, amigosSeleccionados;
	private ListView miListaAmigos;
	private EditText nuevoGrupo;
	private String nombreGrupo, miembrosGrupo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_seleccion_grupo);
		
		this.setTitle("Nuevo grupo");
		
		Bundle datosAmigos = this.getIntent().getExtras();
		amigos = datosAmigos.getStringArrayList("ListaAmigos");
		
		amigosSeleccionados = new ArrayList<String>();
		nuevoGrupo = (EditText)findViewById(R.id.nombreGrupo);
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
	
	public void onClick(View v){
		nombreGrupo = nuevoGrupo.getText().toString();
		if(nombreGrupo.length() > 0 && amigosSeleccionados.size() > 1){
			miembrosGrupo = amigosSeleccionados.get(0);
			for(int i = 1; i < amigosSeleccionados.size(); i++){
				miembrosGrupo = miembrosGrupo + "/" + amigosSeleccionados.get(i);
			}
			Log.d("USERS", miembrosGrupo);
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
