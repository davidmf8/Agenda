package com.example.ficheros;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import com.example.agendauca.ListarFicheros;
import com.example.agendauca.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.TextView;

//Muestra una nota guardada
public class MostrarNota extends Activity{
	TextView miNota;
	String rutaNota;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mostrar_nota);
		
		miNota = (TextView)findViewById(R.id.NotaMostrada);
		
		Bundle datosIntent = this.getIntent().getExtras();
		rutaNota = datosIntent.getString("LecturaNota");
		File nota = new File(rutaNota);
		try{//Se lee el fichero, se vuelva a un vector, que se transforma a String para mostrarlo por un textView
			InputStreamReader fin = new InputStreamReader(new FileInputStream(nota));
			char[] texto = new char[1000];
			fin.read(texto);
			String textoFinal = new String(texto);
		    miNota.setText(textoFinal);
		    fin.close();
		} catch(Exception e){}		
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
			int pos = rutaNota.lastIndexOf("/");
			String rutaAnterior = rutaNota.substring(0, pos);
		    Intent cambio_actividad = new Intent();
		    cambio_actividad.putExtra("Subdirectorio", rutaAnterior);
			cambio_actividad.setClass(getApplicationContext(), ListarFicheros.class);
			startActivity(cambio_actividad);
			finish();
		}
		return super.onKeyDown(keyCode, event);
	}
}
