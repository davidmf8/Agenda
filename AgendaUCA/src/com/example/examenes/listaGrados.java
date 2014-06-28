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
	private static String[] grados = {"Grado Ingeniería Informática", "Grado Ingeniería Industrial", "Grado Ingeniería Aeroespacial", 
		"Grado Ingeniería en Diseño Industrial y Desarrollo de Producto", "Grado Ingeniería Mecánica", "Grado Ingeniería Eléctrica",
		"Grado Ingeniería Electrónica Industrial"};
	private static String[] descarga = {"informatica.xls", "industrial.xls", "aeroespacial.xls",
		"diseñoindustrial.xls", "mecanica.xls", "electrica.xls", "electronica.xls"};

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
