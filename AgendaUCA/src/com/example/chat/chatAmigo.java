package com.example.chat;

import java.util.ArrayList;

import com.example.agendauca.R;
import com.example.conexionesServidor.EnviarMensajeAsynTask;
import com.example.persistencia.BDAcceso;
import com.example.utilidades.Mensaje;

import android.app.ListActivity;
import android.app.NotificationManager;
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
//Activity que muestra la conversaci�n con un usuario o grupo
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
		
		mensajesChat = new ArrayList<Mensaje>();
		BD = new BDAcceso(this);
		BD.BDopen();
		mensajesChat = BD.getMensajesUsuarioFechaActual(nombreAmigo);
		BD.setNuevoMensaje(false, nombreAmigo);
		BD.BDclose();
		
		if(grupo.length > 1){
			this.setTitle("Grupo: " + nombreAmigo.replace("/", ", "));
		}
		else
		    this.setTitle(nombreAmigo);	
		
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
	
	//Opciones del chat
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
	
	//Envia el mensaje al pulsar el bot�n enviar
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
				Log.d("GRUPO", nombreAmigo);
				enviarMensaje.inicilizarValores(nombreAmigo, mensaje, this, false);
			}
			enviarMensaje.execute();
		}
		texto.setText("");
	}
	
	//Actualiza el chat con el nuevo mensaje
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
	
	//Broadcast receiver para mantener actualizada la conversacion en caso de que el usuario este en ella.
	public class recibirNotificacion extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			   Bundle extras = intent.getExtras();
			   String origen =  extras.getString("user");
			   String origenGrupo = extras.getString("grupo");
			  
			   if(origenGrupo == null){
			     if(origen.equalsIgnoreCase(nombreAmigo)){// || (origenGrupo != null && origenGrupo.equalsIgnoreCase(nombreAmigo))){
	                actualizarLista();
	                NotificationManager notificacionesActuales = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
	                notificacionesActuales.cancel(1);
			     }
			  }
			   else{
				   //Log.d("GRUPO",origenGrupo);
				   //Log.d("NOMBREAMIGO",nombreAmigo);
				   if(origenGrupo.equalsIgnoreCase(nombreAmigo)){
					   actualizarLista();
					   NotificationManager notificacionesActuales = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		               notificacionesActuales.cancel(1);
					   //Log.d("CHAT ABIERTO",nombreAmigo);
				   }
			   }
		}
		
	}
}
