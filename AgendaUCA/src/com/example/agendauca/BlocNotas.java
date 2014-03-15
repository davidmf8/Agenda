package com.example.agendauca;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.example.utilidades.FuncionesUtiles;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

//Clase que modela poder apuntar notas y guardarlas
public class BlocNotas extends Activity{
	EditText texto; //El texto que se escribirá en pantalal será recogido por este objeto
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bloc_notas);

		texto = (EditText)findViewById(R.id.blocNotas);
	}
	
	public void onClick(View v) { //Cuando se pulsa guardar
		guardarNota(); //Se guarda la nota y se carga el menu inicial
		Intent cambio_actividad = new Intent();
        cambio_actividad.setClass(this, MenuInicial.class);
        startActivity(cambio_actividad);
	}
	
	private void guardarNota(){
		if(FuncionesUtiles.estadoEscritura()){ //Si se puede accedera memoria, se crea el fichero y la carpeta, si es necesario
			  File dir = new File(this.getExternalFilesDir(null),  ".AgendaNotas");
			  if(!dir.exists()){
				dir.mkdir();
			  }
			
	          String horaLocal = new SimpleDateFormat("yyyMMdd_HHmmss", Locale.ROOT).format(new Date());
	          String ruta_nota = dir.getAbsolutePath() + "/" + "NOTA_" + horaLocal + ".txt";
	          File miNota = new File(ruta_nota);
	          //Con el fichero creado, vamos a guardar lo obtenido en el edittext en el archivo
	          try {
	        	  OutputStreamWriter archivo = new OutputStreamWriter(new FileOutputStream(miNota));
	              archivo.write(texto.getText().toString());
	              archivo.close();
	          } catch (IOException e) {}
		}
		else{
			Toast.makeText(this, "Memoria externa no disponible", Toast.LENGTH_SHORT).show();
		}
		    
	}

	
}
