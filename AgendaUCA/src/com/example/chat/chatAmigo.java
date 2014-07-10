package com.example.chat;

import java.util.ArrayList;

import com.example.agendauca.R;
import com.example.conexionesServidor.EnviarMensajeAsynTask;
import com.example.persistencia.BDAcceso;
import com.example.utilidades.Mensaje;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class chatAmigo extends ListActivity{
	private String nombreAmigo, mensaje;
	private String[] grupo;
	private ArrayList<Mensaje> mensajesChat;
	private mensajeAdapter adapterLista;
	private EditText texto;
	private BDAcceso BD;
	private EnviarMensajeAsynTask enviarMensaje;
	private boolean historialActivo;
	private BroadcastReceiver broadcastReceiver;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_conversacion);
		
        Bundle datosAmigos = this.getIntent().getExtras();
        nombreAmigo = datosAmigos.getString("Nombre");
        //cerrarActivity= datosAmigos.getBoolean("cerrarActivity");
        grupo = nombreAmigo.split("/");
		
        historialActivo = false;
		texto = (EditText)this.findViewById(R.id.conversacion);
		
		if(grupo.length > 1){
			this.setTitle("Grupo: " + nombreAmigo.replace("/", ", "));
		}
		else
		    this.setTitle(nombreAmigo);
		
		mensajesChat = new ArrayList<Mensaje>();
		BD = new BDAcceso(this);
		BD.BDopen();
		mensajesChat = BD.getMensajesUsuarioFechaActual(nombreAmigo);
		BD.BDclose();
		
		adapterLista = new mensajeAdapter(this, mensajesChat, grupo.length);
		this.setListAdapter(adapterLista);
		this.setSelection(adapterLista.getCount()-1);
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.historial_conversacion, menu);
        return true;
    }
	
	
	public void onResume(){
		super.onResume();
		broadcastReceiver = new recibirNotificacion();
		this.registerReceiver(broadcastReceiver, new IntentFilter("ActualizarLista"));
		IntentFilter intentFiltro = new IntentFilter();
		intentFiltro.addAction("ActualizarLista");
		this.registerReceiver(broadcastReceiver, intentFiltro);
	}
	
	public void onPause(){
		this.unregisterReceiver(broadcastReceiver);
		super.onPause();
	}
	
	@Override
	 public boolean onOptionsItemSelected(MenuItem item) {
	     switch (item.getItemId()) {
	         case R.id.EliminarHistorial:
	    	     BD = new BDAcceso(this);
	 		     BD.BDopen();
	 		     BD.eliminarMensajesUsuario(nombreAmigo);
	 		     BD.BDclose();
	 		     actualizarLista();
                 break;
             case R.id.VerHistorial:
        	     historialActivo = true;
        	     actualizarLista();
        	     Toast.makeText(this, "Historial cargado", Toast.LENGTH_SHORT).show();
        	     break;
             case R.id.CrearEvento:
            	 Intent cambio_actividad = new Intent();
     		     cambio_actividad.setClass(getApplicationContext(), creacionEvento.class);
     		     cambio_actividad.putExtra("Nombre", nombreAmigo);
     		     startActivity(cambio_actividad);
     			 finish();
            	 break;
	     } 
	     return false;
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
		mensaje = texto.getText().toString();
		if(mensaje.length() > 0){
			BDAcceso BD = new BDAcceso(this);
			BD = BD.BDopen();
			BD.insertarMensaje(mensaje, nombreAmigo, 1, "");
			BD.BDclose();
			actualizarLista();
			enviarMensaje = new EnviarMensajeAsynTask();
			if(grupo.length == 1)
			    enviarMensaje.inicilizarValores(nombreAmigo, mensaje, this, true);
			else{
				//Grupo = nombreAmigo.replace(miUsuario, "");
				Log.d("GRUPO", nombreAmigo);
				enviarMensaje.inicilizarValores(nombreAmigo, mensaje, this, false);
			}
			enviarMensaje.execute();
		}
		texto.setText("");
	}
	
	public void actualizarLista(){
		if(historialActivo){
			adapterLista.mostrarHistorial(nombreAmigo);
   	        adapterLista.notifyDataSetChanged();
		    this.setSelection(adapterLista.getCount()-1);
		}
		else{
			adapterLista.actualizarAdapter(nombreAmigo);
		    adapterLista.notifyDataSetChanged();
		    this.setSelection(adapterLista.getCount()-1);
		}	
	}
	
	public class recibirNotificacion extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			   Bundle extras = intent.getExtras();
			   String origen =  extras.getString("user");
			   String origenGrupo = extras.getString("addGroup");
			  
			   if(origenGrupo == null){
			     if(origen.equalsIgnoreCase(nombreAmigo))// || (origenGrupo != null && origenGrupo.equalsIgnoreCase(nombreAmigo))){
	                actualizarLista();
			  }
			   else{
				   Log.d("GRUPO",origenGrupo);
				   Log.d("GRUPOMIO",nombreAmigo);
				   if(origenGrupo.equalsIgnoreCase(nombreAmigo)){
					   actualizarLista();
					   Log.d("SI ENTRA",nombreAmigo);
				   }
				   else
					   Log.d("NO ENTRA",nombreAmigo);
			   }
		}
		
	}
}
