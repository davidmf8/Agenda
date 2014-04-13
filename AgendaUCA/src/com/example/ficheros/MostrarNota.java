package com.example.ficheros;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import com.example.agendauca.R;
import com.example.agendauca.R.id;
import com.example.agendauca.R.layout;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

//Muestra una nota guardada
public class MostrarNota extends Activity{
	TextView miNota;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mostrar_nota);
		
		miNota = (TextView)findViewById(R.id.NotaMostrada);
		
		Bundle datosIntent = this.getIntent().getExtras();
		String rutaNota = datosIntent.getString("LecturaNota");
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
}
