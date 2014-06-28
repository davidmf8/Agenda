package com.example.examenes;

import com.example.agendauca.R;
import com.example.conexionesServidor.descargarExamenesAsynTask;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class listaGrados extends Activity{
	private ListView listaGrados;
    private	descargarExamenesAsynTask descargarExcel;
	private static String[] grados = {"Grado Ingenier�a Inform�tica", "Grado Ingenier�a Industrial", "Grado Ingenier�a Aeroespacial", 
		"Grado Ingenier�a en Dise�o Industrial y Desarrollo de Producto", "Grado Ingenier�a Mec�nica", "Grado Ingenier�a El�ctrica",
		"Grado Ingenier�a Electr�nica Industrial"};
	private static String[] descarga = {"informatica.xls", "industrial.xls", "aeroespacial.xls",
		"dise�oindustrial.xls", "mecanica.xls", "electrica.xls", "electronica.xls"};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ficheros);
		listaGrados = (ListView)findViewById(R.id.ListaFicheros);
		listaGrados.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, grados));
		listaGrados.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> adapter, View view, int posicion, long id) {
				   descargarExcel = new descargarExamenesAsynTask();
				   //descargarExcel.inicializarValores(descarga[posicion], this);
				   descargarExcel.execute();
			}
		});
	}
}
