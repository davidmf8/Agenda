package com.example.conexionesServidor;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

public class subirFicheroAsyntask extends AsyncTask<Void, Void, Boolean>{
    private String archivoADescargar, usuarioDestino;
    private File ficheroAEnviar;
    private ProgressDialog dialogCarga;
	private Context context;
	
	public void inicializarValores(String archivoADescargar, Context context, String usuarioDestino){
		this.archivoADescargar = archivoADescargar;
		this.context = context;
		this.usuarioDestino = usuarioDestino;
		dialogCarga = new ProgressDialog(this.context);
        dialogCarga.setMessage("Cargando...");
        dialogCarga.setIndeterminate(false);
        dialogCarga.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialogCarga.setCancelable(false);
        dialogCarga.show();
	}

	@Override
	protected Boolean doInBackground(Void... params){
		try { 	  
			String patron1 = "\r\n";
			String patron2 = "--";
			String patron3 = "*****";
			byte[] buffer;
			int tamMaxBuffer = 1 * 1024 * 1024;
			ficheroAEnviar = new File(archivoADescargar); 
			FileInputStream ficheroStream = new FileInputStream(ficheroAEnviar);
			URL url = new URL("http://prubauca.esy.es/subir_fichero.php");
         
			HttpURLConnection conexion = (HttpURLConnection)url.openConnection();
			conexion.setDoInput(true); 
			conexion.setDoOutput(true); 
			conexion.setUseCaches(false); 
			conexion.setRequestMethod("POST");
			conexion.setRequestProperty("Connection", "Keep-Alive");
			conexion.setRequestProperty("ENCTYPE", "multipart/form-data");
			conexion.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + patron3);
			conexion.setRequestProperty("fichero", archivoADescargar);
         
			DataOutputStream streamFichero = new DataOutputStream(conexion.getOutputStream());
			streamFichero.writeBytes(patron2 + patron3 + patron1);
			streamFichero.writeBytes("Content-Disposition: form-data; name=\"fichero\";filename=\"" + archivoADescargar + "\"" + patron1);
			streamFichero.writeBytes(patron1);

			// create a buffer of  maximum size
			int tamDisponible = ficheroStream.available();

			int tamBuffer = Math.min(tamDisponible, tamMaxBuffer);
			buffer = new byte[tamBuffer];

			// read file and write it into form...
			int lecturaTam = ficheroStream.read(buffer, 0, tamBuffer); 
           
			while (lecturaTam > 0){
				streamFichero.write(buffer, 0, tamBuffer);
				tamDisponible = ficheroStream.available();
				tamBuffer = Math.min(tamDisponible, tamMaxBuffer);
				lecturaTam = ficheroStream.read(buffer, 0, tamBuffer);  
           }

			// send multipart form data necesssary after file data...
			streamFichero.writeBytes(patron1);
			streamFichero.writeBytes(patron2 + patron3 + patron2 + patron1);
         
			//close the streams //
			ficheroStream.close();
			streamFichero.flush();
			streamFichero.close(); 
			
            if(conexion.getResponseCode() != 200){
				dialogCarga.dismiss();
				return false;
			}
			
	    }catch(Exception e){
			dialogCarga.dismiss();
			return false;
		}
		dialogCarga.dismiss();
		return true;
	}
	
	protected void onPostExecute(Boolean result){
		if(result){
			String ruta = "http://prubauca.esy.es/descargas/" + ficheroAEnviar.getName();
			EnviarMensajeAsynTask enviarMensaje = new EnviarMensajeAsynTask();
			enviarMensaje.inicilizarValores(usuarioDestino, ruta, context, true);
			enviarMensaje.execute();
			Toast.makeText(context, "Fichero subido con éxito", Toast.LENGTH_SHORT).show();
		}
		else
			Toast.makeText(context, "Error al subir Fichero", Toast.LENGTH_SHORT).show();
	}
}
