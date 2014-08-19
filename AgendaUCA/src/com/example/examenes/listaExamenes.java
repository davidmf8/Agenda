package com.example.examenes;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract.Events;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

import com.example.agendauca.R;
import com.example.utilidades.Asignatura;

//Muestra los exaémenes de un grado
public class listaExamenes extends Activity{
	private ListView listaExamenes;
	private String[] examenes;
	private ArrayList<Asignatura> asignaturas;
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_ficheros);
		listaExamenes = (ListView)findViewById(R.id.ListaFicheros);
		Bundle extra = this.getIntent().getExtras();
		asignaturas = (ArrayList<Asignatura>)extra.getSerializable("asignaturas");
		getAsignaturas();
		listaExamenes.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, examenes));
		listaExamenes.setOnItemClickListener(new OnItemClickListener(){
			public void onItemClick(AdapterView<?> adapter, View view, int posicion, long id) {
				   agregarExamen(posicion, getApplicationContext());
			}
		});
	}

	private void getAsignaturas() {
		examenes = new String[asignaturas.size()];
		for(int i = 0; i < asignaturas.size(); i++){
			examenes[i] = asignaturas.get(i).getAsignatura();
		}
	}
	
	//Agrega un examen al calendario
	private void agregarExamen(final int posicion, final Context context) {
		 AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Desea agregar este examen a tu calendario?").setTitle("Agregar examen")
	        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()  {
	               public void onClick(DialogInterface dialog, int id) {
	            	   SimpleDateFormat formateoFechaHora = new SimpleDateFormat("dd MM yyyy HH:mm:ss");
	         		  Date formatoFecha = null;
	         		  try {
	         			formatoFecha = formateoFechaHora.parse(asignaturas.get(posicion).getFecha());
	         			Log.d("FECHA", formatoFecha.toString());
	         		} catch (ParseException e) {
	         			e.printStackTrace();
	         		}
	         		  
	         		  ContentResolver contentCalendario = getContentResolver();
	              	  ContentValues parametrosEvento = new ContentValues();
	              	  parametrosEvento.put(Events.TITLE, "Examen de " + asignaturas.get(posicion).getAsignatura());
	              	  parametrosEvento.put(Events.DESCRIPTION, "Examen");
	              	  parametrosEvento.put(Events.EVENT_LOCATION, "UCA. Aula " + asignaturas.get(posicion).getLugar());
	              	  parametrosEvento.put(Events.EVENT_TIMEZONE, "GTM-1");
	              	  parametrosEvento.put(Events.DTSTART, formatoFecha.getTime());
	              	  parametrosEvento.put(Events.DTEND, formatoFecha.getTime());
	              	  parametrosEvento.put(Events.CALENDAR_ID, 1);
	              	  Uri uriEvento = contentCalendario.insert(Events.CONTENT_URI, parametrosEvento);
	              	  Toast.makeText(context, "Examen agregado al calendario", Toast.LENGTH_SHORT).show();
	              }
	         })
	        .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
	               public void onClick(DialogInterface dialog, int id) {
	                   
	                   }
	               });
        builder.create();
        builder.show();
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent cambio_actividad = new Intent();
		    cambio_actividad.setClass(getApplicationContext(), listaGrados.class);
		    startActivity(cambio_actividad);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
