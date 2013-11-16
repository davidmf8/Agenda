package com.example.agendauca;

import com.example.agendauca.R;

import android.os.Bundle;
import android.widget.ListView;
import android.app.Activity;


public class ListarDirectorios extends Activity{
	ListView miLista;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_gestionficheros);
		miLista = (ListView)findViewById(R.id.listaDir);
	}

}