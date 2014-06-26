package com.example.conexionesServidor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.example.agendauca.MenuInicial;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class descargarExamenesAsynTask extends AsyncTask<Void, Void, Boolean>{
    private String ruta;
    private MenuInicial context;
	
	public void inicializarValores(String ruta, MenuInicial context){
		this.ruta = ruta;
		this.context = context;
	}

	@Override
	protected Boolean doInBackground(Void... params){
		try{
		   URL urlServidor = new URL("http://prubauca.esy.es/prueba.xls");
		   HttpURLConnection conexion = (HttpURLConnection) urlServidor.openConnection();
		   conexion.setRequestMethod("GET");
		   conexion.setDoOutput(true);
		   conexion.connect();

		   File file = new File(ruta,"excel.xls");
		   FileOutputStream outputStream = new FileOutputStream(file);
		   InputStream inputStream = conexion.getInputStream();

		  // int tamanioFichero = conexion.getContentLength();
		   //int estadoDescargaActual = 0;
		   byte[] buffer = new byte[1024];
		   int longitudBuffer = 0; 
		   while ( (longitudBuffer = inputStream.read(buffer)) > 0 ) {
			   outputStream.write(buffer, 0, longitudBuffer);
			   //estadoDescargaActual += longitudBuffer;
			   //int progress=(int)(estadoDescargaActual*100/tamanioFichero);
		  }
		  outputStream.close();	
		  Log.d("RUTA", file.getAbsolutePath());
		}catch(Exception e){ return false;}
		  
		  return true;
	}
	
	protected void onPostExecute(boolean result){
		if(result)
			Toast.makeText(context, "Error en la descarga", Toast.LENGTH_SHORT).show();
		else
			Toast.makeText(context, "Descarga completa", Toast.LENGTH_SHORT).show();
	}
}
