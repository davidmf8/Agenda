package com.example.agendauca;

import java.util.ArrayList;

import com.example.conexionesServidor.EnviarMensajeAsynTask;
import com.example.persistencia.BDAcceso;
import com.example.utilidades.Mensaje;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class chatAmigo extends ListActivity{
	private String nombreAmigo;
	private ArrayList<Mensaje> mensajesChat;
	private mensajeAdapter adapterLista;
	private EditText texto;
	private BDAcceso BD;
	private String mensaje;
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
	}
	
	public void onClick(View v){
		mensaje = texto.getText().toString();
		if(mensaje.length() > 0){
			enviarMensaje = new EnviarMensajeAsynTask();
			enviarMensaje.inicilizarValores(nombreAmigo, mensaje, this.getApplicationContext());
			enviarMensaje.execute();
		}
		texto.setText("");
	}

}
