package com.example.conexionesServidor;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import jxl.Sheet;
import jxl.Workbook;

import com.example.examenes.listaGrados;
import com.example.utilidades.Asignatura;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

public class descargarExamenesAsynTask extends AsyncTask<Void, Void, Boolean>{
    private String archivoADescargar, asignatura, fecha, lugar;
    private listaGrados context;
    private ProgressDialog dialogCarga;
    private ArrayList<Asignatura> asignaturas;
	
	public void inicializarValores(String archivoADescargar, listaGrados context){
		this.archivoADescargar = archivoADescargar;
		this.context = context;
		this.asignaturas = new ArrayList<Asignatura>();
		dialogCarga = new ProgressDialog(this.context);
        dialogCarga.setMessage("Cargando...");
        dialogCarga.setIndeterminate(false);
        dialogCarga.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialogCarga.setCancelable(true);
        dialogCarga.show();
	}

	@Override
	protected Boolean doInBackground(Void... params){
		try{
		   URL urlServidor = new URL("http://prubauca.esy.es/" + archivoADescargar);
		   HttpURLConnection conexion = (HttpURLConnection) urlServidor.openConnection();
		   conexion.setRequestMethod("GET");
		   conexion.setDoOutput(true);
		   conexion.connect();

		   File file = File.createTempFile("excel", ".xls");
		   FileOutputStream outputStream = new FileOutputStream(file);
		   InputStream inputStream = conexion.getInputStream();

		   /*int tamanioFichero = conexion.getContentLength();
		   int estadoDescargaActual = 0;*/
		   byte[] buffer = new byte[1024];
		   int longitudBuffer = 0; 
		   while((longitudBuffer = inputStream.read(buffer)) > 0 ){
			   outputStream.write(buffer, 0, longitudBuffer);
			   /*estadoDescargaActual += longitudBuffer;
			   int progress=(int)(estadoDescargaActual*100/tamanioFichero);*/
		  }
		  outputStream.close();	
		  Log.d("RUTA", file.getAbsolutePath());
		  
          Workbook excel = Workbook.getWorkbook(file);

          Sheet hojaExcel = excel.getSheet(0);

          System.out.println(hojaExcel.getColumns());
          System.out.println(hojaExcel.getRows());
          
          for(int i=0; i<hojaExcel.getRows(); i++){
                asignatura = hojaExcel.getCell(0,i).getContents();
                fecha = hojaExcel.getCell(1,i).getContents();
                System.out.println(fecha);
                lugar = hojaExcel.getCell(2,i).getContents();
                Asignatura auxAsignatura = new Asignatura(asignatura, fecha, lugar);
                asignaturas.add(auxAsignatura);
          }
          file.deleteOnExit();
		  
		}catch(Exception e){
			dialogCarga.dismiss();
			return false;
		}
		dialogCarga.dismiss();
		//context.validarExcel(true);
		return true;
	}
	
	protected void onPostExecute(Boolean result){
		context.validarExcel(result, asignaturas);
	}
}
