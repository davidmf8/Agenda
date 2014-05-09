package com.example.agendauca;

import java.util.ArrayList;

import com.example.conexionesServidor.EnviarMensajeAsynTask;
import com.example.persistencia.BDAcceso;
import com.example.utilidades.Mensaje;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;

public class chatAmigo extends ListActivity{
	private String nombreAmigo, mensaje;
	private ArrayList<Mensaje> mensajesChat;
	private mensajeAdapter adapterLista;
	private EditText texto;
	private BDAcceso BD;
	private EnviarMensajeAsynTask enviarMensaje;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conversacion);
		
        Bundle datosAmigos = this.getIntent().getExtras();
        nombreAmigo = datosAmigos.getString("Nombre");
		
		texto = (EditText)this.findViewById(R.id.conversacion);
		
		this.setTitle(nombreAmigo);
		
		mensajesChat = new ArrayList<Mensaje>();
		BD = new BDAcceso(this);
		BD.BDopen();
		mensajesChat = BD.getMensajesUsuarioFechaActual(nombreAmigo);
		BD.BDclose();
		
		adapterLista = new mensajeAdapter(this, mensajesChat);
		this.setListAdapter(adapterLista);
		this.setSelection(adapterLista.getCount()-1);
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
	
	/*public void onStop(){
		finish();
		super.onStop();
	}*/
	
	public void onClick(View v){
		mensaje = texto.getText().toString();
		if(mensaje.length() > 0){
			BDAcceso BD = new BDAcceso(this);
			BD = BD.BDopen();
			BD.insertarMensaje(mensaje, nombreAmigo, 1);
			BD.BDclose();
			actualizarLista();
			enviarMensaje = new EnviarMensajeAsynTask();
			enviarMensaje.inicilizarValores(nombreAmigo, mensaje, this);
			enviarMensaje.execute();
		}
		texto.setText("");
	}
	
	public void actualizarLista(){
		adapterLista.actualizarAdapter(nombreAmigo);
		adapterLista.notifyDataSetChanged();
		this.setSelection(adapterLista.getCount()-1);
	}

}
