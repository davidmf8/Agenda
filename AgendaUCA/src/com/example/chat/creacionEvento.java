package com.example.chat;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract.Events;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;


import com.example.agendauca.R;
import com.example.conexionesServidor.CrearEventoAsynTask;


//Activity con formulario para crear un evento: nombre, descripcion, lugar, fecha y hora.
public class creacionEvento extends Activity{
    EditText nombreEvento, descripcionEvento, lugarEvento, fechaEvento, horaEvento;
	String nombreAmigo, nombre, descripcion, lugar, fecha, hora;
	CrearEventoAsynTask enviarMensaje;
	DatePickerDialog.OnDateSetListener fechaPicker;
	Calendar calendarioActual;
	OnTimeSetListener horaPicker;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crear_evento);
		this.setTitle("Nuevo evento");
		
		Bundle datosAmigos = this.getIntent().getExtras();
		nombreAmigo = datosAmigos.getString("Nombre");		
		nombreEvento = (EditText)this.findViewById(R.id.nombreEvento);	
		descripcionEvento = (EditText)this.findViewById(R.id.descripcionEvento);
		lugarEvento = (EditText)this.findViewById(R.id.lugarEvento);
		fechaEvento = (EditText)this.findViewById(R.id.fechaEvento);
		horaEvento = (EditText)this.findViewById(R.id.horaEvento);
		
		calendarioActual = Calendar.getInstance();
		
		horaPicker = new TimePickerDialog.OnTimeSetListener() {
			@Override
			public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
				 /*calendarioActual.set(hourOfDay, Calendar.HOUR);
	             calendarioActual.set(minute, Calendar.MINUTE);*/
	             actualizarEditTextHora(hourOfDay, minute);
			}

			private void actualizarEditTextHora(int hourOfDay, int minute) {
				String formato = "HH:mm";
			    SimpleDateFormat formatoFinal = new SimpleDateFormat(formato);
			    horaEvento.setText(hourOfDay + ":" + minute);
			}
 
         };
         
         horaEvento.setOnClickListener(new OnClickListener() {
 	        public void onClick(View v) {
 	        	Calendar calendarioActual = Calendar.getInstance();
 				new TimePickerDialog(creacionEvento.this, horaPicker, calendarioActual.get(Calendar.HOUR_OF_DAY),
 	                    calendarioActual.get(Calendar.MINUTE), true).show();
 	        }
 	    });
		
		fechaPicker = new DatePickerDialog.OnDateSetListener() {
		    @Override
		    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
		    	calendarioActual.set(Calendar.YEAR, year);
		    	calendarioActual.set(Calendar.MONTH, monthOfYear);
		    	calendarioActual.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		    	actualizarEditTextFecha();
		    }

			private void actualizarEditTextFecha() {
				String formato = "dd/MM/yyyy";
			    SimpleDateFormat formatoFinal = new SimpleDateFormat(formato);
			    fechaEvento.setText(formatoFinal.format(calendarioActual.getTime()));
			}


		};
		
		fechaEvento.setOnClickListener(new OnClickListener() {
	        public void onClick(View v) {
	        	Calendar calendarioActual = Calendar.getInstance();
				new DatePickerDialog(creacionEvento.this, fechaPicker, calendarioActual.get(Calendar.YEAR),
						calendarioActual.get(Calendar.MONTH),
	                    calendarioActual.get(Calendar.DAY_OF_MONTH)).show();
	        }
	    });
		
	}

	
	//Boton atras
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			Intent cambio_actividad = new Intent();
			cambio_actividad.putExtra("Nombre", nombreAmigo);
			cambio_actividad.setClass(getApplicationContext(), chatAmigo.class);
		    startActivity(cambio_actividad);
		    finish();
		}
		return super.onKeyDown(keyCode, event);
	}
	
	//Se envia el evento al usuario o usuarios del chat elegido.
	public void onClick(View v){
		nombre = nombreEvento.getText().toString();
		descripcion = descripcionEvento.getText().toString();
		lugar = lugarEvento.getText().toString();
		fecha = fechaEvento.getText().toString();
		hora = horaEvento.getText().toString();
		//Ningun campo debe quedar vacio
		if(nombre.length() != 0 && descripcion.length() != 0 && lugar.length() != 0 && fecha.length() != 0 && hora.length() != 0){
			  SimpleDateFormat formateoFechaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			  formateoFechaHora.setLenient(false);
			  Date formatoFecha = null;
			  try { //se controla el formato de fecha y hora, así como si es válida.
				formatoFecha = formateoFechaHora.parse(fecha + " " + hora);
				Date fechaActual = formateoFechaHora.parse(new Date().toLocaleString());
				//Log.d("AHORA",fechaActual.toString());
				//Log.d("FECHA", formatoFecha.toString());
				if(formatoFecha.after(fechaActual)){
				    enviarMensaje = new CrearEventoAsynTask();
				    ArrayList<String> datosEvento = new ArrayList<String>();
				    datosEvento.add(nombre);
				    datosEvento.add(descripcion);
				    datosEvento.add(lugar);
				    datosEvento.add(fecha);
				    datosEvento.add(hora);
				    if(nombreAmigo.split("/").length == 1)
				        enviarMensaje.inicilizarValores(nombreAmigo, datosEvento, this, true);
				    else{
				    	enviarMensaje.inicilizarValores(nombreAmigo, datosEvento, this, false);
				    }
				    enviarMensaje.execute();
				}
				else
					Toast.makeText(this, "Fecha de evento ya pasada", Toast.LENGTH_SHORT).show();
	
			} catch (ParseException e) {
				Toast.makeText(this, "Fecha u hora incorrecta. Inserte una fecha u hora válida", Toast.LENGTH_SHORT).show();
			}
			
		}
		else
			Toast.makeText(this, "Datos de evento incompleto", Toast.LENGTH_SHORT).show();
	}

	public void actualizacionEvento() {
		//Al terrminar de enviar el evento, se actualiza el dispositivo con el nuevo evento creado
		 SimpleDateFormat formateoFechaHora = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		  Date formatoFecha = null;
		  try {
			formatoFecha = formateoFechaHora.parse(fecha + " " + hora);
			ContentResolver contentCalendario = getContentResolver();
         	ContentValues parametrosEvento = new ContentValues();
         	parametrosEvento.put(Events.TITLE, nombre);
         	parametrosEvento.put(Events.DESCRIPTION, descripcion);
         	parametrosEvento.put(Events.EVENT_LOCATION, lugar);
         	parametrosEvento.put(Events.EVENT_TIMEZONE, "GTM-1");
         	parametrosEvento.put(Events.DTSTART, formatoFecha.getTime());
         	parametrosEvento.put(Events.DTEND, formatoFecha.getTime());
         	parametrosEvento.put(Events.CALENDAR_ID, 1);
         	Uri uriEvento = contentCalendario.insert(Events.CONTENT_URI, parametrosEvento);
		}catch(ParseException e){
			Toast.makeText(this, "No se ha podido añadir evento al calendario", Toast.LENGTH_SHORT).show();
		}
		Intent cambio_actividad = new Intent();
		cambio_actividad .putExtra("Nombre", nombreAmigo);
	    cambio_actividad.setClass(getApplicationContext(), chatAmigo.class);
		startActivity(cambio_actividad);
		finish();
	}
}

