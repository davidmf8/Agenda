package com.example.chat;

import java.util.ArrayList;
import com.example.agendauca.R;
import com.example.conexionesServidor.BuscarUsuarioAsynTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
		spec.setContent(R.id.ListaAmigos);
		spec.setIndicator("",
		    res.getDrawable(android.R.drawable.ic_menu_agenda));
		tabHostChat.addTab(spec);

		spec=tabHostChat.newTabSpec("mitab2");
		spec.setContent(R.id.ListaChats);
		spec.setIndicator("TAB2",
		    res.getDrawable(android.R.drawable.ic_menu_send));
		tabHostChat.addTab(spec);
		 
		tabHostChat.setCurrentTab(0);
		listadoAmigos();
		miListaAmigos = (ListView)findViewById(R.id.Amigos);
        miListaAmigos.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, amigos));
	}

	private void listadoAmigos() {
		BDAcceso BDAmigos = new BDAcceso(getApplicationContext());
		BDAmigos = BDAmigos.BDopen();
		amigos = BDAmigos.getUsuarios();
		BDAmigos.BDclose();
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
				BuscarUsuarioAsynTask existeUsuario = new BuscarUsuarioAsynTask();
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
	
	private void eliminarAmigo() {
		
	}

}
