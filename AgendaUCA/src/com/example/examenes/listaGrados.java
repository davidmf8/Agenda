package com.example.examenes;

import java.util.ArrayList;

import com.example.agendauca.MenuInicial;
import com.example.agendauca.R;
import com.example.conexionesServidor.descargarExamenesAsynTask;
import com.example.utilidades.Asignatura;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class listaGrados extends Activity{
	private ListView listaGrados;
	private listaGrados referencia;
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
		referencia = this;
		listaGrados.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> adapter, View view, int posicion, long id) {
				   descargarExcel = new descargarExamenesAsynTask();
				   descargarExcel.inicializarValores(descarga[posicion], referencia);
				   descargarExcel.execute();
			}
		});
	}
	
	public void validarExcel(boolean resultado, ArrayList<Asignatura> asignaturas){
		if(resultado){
		  Intent cambio_actividad = new Intent();
		  cambio_actividad.setClass(this, listaExamenes.class);
		  cambio_actividad.putExtra("asignaturas", asignaturas);
		  startActivity(cambio_actividad);
		  finish();
		}
		else
			Toast.makeText(this, "Excel no válido. Inténtelo más tarde", Toast.LENGTH_SHORT).show();
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent cambio_actividad = new Intent();
		    cambio_actividad.setClass(getApplicationContext(), MenuInicial.class);
		    startActivity(cambio_actividad);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
