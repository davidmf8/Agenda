package com.example.agendauca;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;


public class MostrarNota extends Activity{
	TextView miNota;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mostrar_nota);
		
		miNota = (TextView)findViewById(R.id.NotaMostrada);
		
		Bundle datosIntent = this.getIntent().getExtras();
		String rutaNota = datosIntent.getString("LecturaNota");
		
		try{
			BufferedReader fin = new BufferedReader(new InputStreamReader(openFileInput(rutaNota)));
		    miNota.setText(fin.readLine());
		    fin.close();
		} catch(Exception e){}
			
	}
}
