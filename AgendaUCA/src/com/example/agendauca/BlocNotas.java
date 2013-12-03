package com.example.agendauca;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class BlocNotas extends Activity{
	EditText texto;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bloc_notas);

		texto = (EditText)findViewById(R.id.blocNotas);
	}
	
	public void onClick(View v) {
		guardarNota();
		Intent cambio_actividad = new Intent();
        cambio_actividad.setClass(this, MainActivity.class);
        startActivity(cambio_actividad);
	}
	
	private void guardarNota(){
		if(FuncionesUtiles.estadoEscritura()){
			  File dir = new File(this.getExternalFilesDir(null),  ".AgendaNotas");
			  if(!dir.exists()){
				dir.mkdir();
			  }
			
	          String horaLocal = new SimpleDateFormat("yyyMMdd_HHmmss", Locale.ROOT).format(new Date());
	          String ruta_nota = dir.getAbsolutePath() + "/" + "NOTA_" + horaLocal + ".txt";
	          File miNota = new File(ruta_nota);
	          try {
	        	  OutputStreamWriter archivo =
	        		        new OutputStreamWriter(
	        		            new FileOutputStream(miNota));
	              archivo.write(texto.getText().toString());
	              archivo.flush();
	              archivo.close();
	          } catch (IOException e) {
	          }
		}
		else{
			Toast.makeText(this, "Memoria externa no disponible", Toast.LENGTH_SHORT).show();
		}
		    
	}

	
}
