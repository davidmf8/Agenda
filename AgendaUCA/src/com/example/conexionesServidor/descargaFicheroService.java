package com.example.conexionesServidor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.example.utilidades.FuncionesUtiles;

import android.app.IntentService;
import android.content.Intent;

public class descargaFicheroService extends IntentService {

	public descargaFicheroService() {
		super("descargaFicheroService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		File fichero;
		try{
			String archivoADescargar = intent.getStringExtra("ruta");
			String tipoFichero = isNombreFichero(archivoADescargar);
			URL urlServidor = new URL(archivoADescargar);
			HttpURLConnection conexion = (HttpURLConnection) urlServidor.openConnection();
			conexion.setRequestMethod("GET");
			conexion.setDoOutput(true);
			conexion.connect();

			if(FuncionesUtiles.estadoEscritura()){
				File dir = new File(getExternalFilesDir(null).getAbsolutePath());
			
			    fichero = new File(dir, tipoFichero);

			    FileOutputStream outputStream = new FileOutputStream(fichero);
			    InputStream inputStream = conexion.getInputStream();
			    byte[] buffer = new byte[1024];
			    int longitudBuffer = 0; 
			    while((longitudBuffer = inputStream.read(buffer)) > 0)
				   outputStream.write(buffer, 0, longitudBuffer);
			    outputStream.close();
			}
		}catch(Exception e){}
	}

	private String isNombreFichero(String tipo) {
		String[] splitNombre = tipo.split("/");
		int tam = splitNombre.length;
		return splitNombre[tam-1];
		/*if(tipo.endsWith(".txt")){
			return ".txt";
		}
		else{
			if(tipo.endsWith(".jpg")){
				return ".jpg";
			}
			else{
				if(tipo.endsWith(".mp4")){
					return ".mp4";
				}
				else
					return ".3gp";
			}
		}*/
	}

}
