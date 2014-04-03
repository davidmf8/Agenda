package com.example.chat;

import java.util.ArrayList;

import com.example.agendauca.MenuInicial;
import com.example.agendauca.R;
import com.example.conexionesServidor.InsertarUsuarioAsynTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import com.example.persistencia.*;

public class chatPrincipal extends Activity{
	private TabHost  tabHostChat;
	private ArrayList<String> amigos;
	private ListView miListaAmigos;
	private EditText nuevoAmigo;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		
		tabHostChat = (TabHost) this.findViewById(android.R.id.tabhost);
		
		Resources res = getResources();
		 
		tabHostChat.setup();
		 
		TabHost.TabSpec spec=tabHostChat.newTabSpec("mitab1");
		spec.setContent(R.id.Amigos);
		spec.setIndicator("",
		    res.getDrawable(android.R.drawable.ic_menu_agenda));
		tabHostChat.addTab(spec);

		spec=tabHostChat.newTabSpec("mitab2");
		spec.setContent(R.id.Conversaciones);
		spec.setIndicator("TAB2",
		    res.getDrawable(android.R.drawable.ic_menu_send));
		tabHostChat.addTab(spec);
		 
		tabHostChat.setCurrentTab(0);
		listadoAmigos();
		miListaAmigos = (ListView)findViewById(R.id.ListaAmigos);
        miListaAmigos.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, amigos));
        
        miListaAmigos.setOnItemLongClickListener(new OnItemLongClickListener(){
			public boolean onItemLongClick(AdapterView<?> adapter, View view,
					int posicion, long id) {
				String usuarioABorrar = amigos.get(posicion);
				eliminarAmigo(usuarioABorrar, getApplicationContext());
				return false;
			}
        });
	}

	private void listadoAmigos() {
		BDAcceso BDAmigos = new BDAcceso(getApplicationContext());
		BDAmigos = BDAmigos.BDopen();
		amigos = BDAmigos.getUsuarios();
		BDAmigos.BDclose();
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent cambio_actividad = new Intent();
			cambio_actividad.setClass(this, MenuInicial.class);
			startActivity(cambio_actividad);
			finish();
		}
		return false;
	}
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Carga el action bar, para el "menu" de la creacion de fotos videos...
        getMenuInflater().inflate(R.menu.menu_amigos, menu);
        return true;
    }
	
	@Override
	 public boolean onOptionsItemSelected(MenuItem item) {
	  agregarAmigo(this); 
	  return false;
	 }


	private void agregarAmigo(final chatPrincipal activity) {
		AlertDialog.Builder dialogo = new Builder(this);
        nuevoAmigo = new EditText(this);
        dialogo.setTitle("Agregar Amigo");
        dialogo.setView(nuevoAmigo);

        dialogo.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
        	@Override
        	public void onClick(DialogInterface dialog, int which) {
				String consultaAmigo = nuevoAmigo.getText().toString();
				InsertarUsuarioAsynTask existeUsuario = new InsertarUsuarioAsynTask();
				existeUsuario.inicilizarValores(consultaAmigo, activity);
				existeUsuario.execute();
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
	
	private void eliminarAmigo(final String usuarioABorrar, final Context context) {
		 AlertDialog.Builder builder = new AlertDialog.Builder(this);
         builder.setMessage("¿Desea eliminarlo definitivamente?").setTitle("Eliminar Amigo")
	        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()  {
	               public void onClick(DialogInterface dialog, int id) {
	            	   BDAcceso BD = new BDAcceso(context);
	       			   BD = BD.BDopen();
	       			   BD.eliminarUsuario(usuarioABorrar);
	       			   BD.BDclose();
	   				   Intent cambio_actividad = new Intent();
					   cambio_actividad.setClass(getApplicationContext(), chatPrincipal.class);
					   startActivity(cambio_actividad);
					   finish();
	                   }
	               })
	        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                   
	                   }
	               });
         builder.create();
         builder.show();
	}

}
