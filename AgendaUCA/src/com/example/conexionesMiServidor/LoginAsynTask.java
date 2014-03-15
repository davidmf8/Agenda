package com.example.conexionesMiServidor;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.agendauca.MainActivity;
import com.example.utilidades.FuncionesUtiles;
import com.google.android.gms.gcm.GoogleCloudMessaging;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

//Clase encargada de realizar el registro de usuario en segundo plano.
public class LoginAsynTask extends AsyncTask<Void,Boolean,Boolean>{
	private HttpJsonObject peticionPostServidor = new HttpJsonObject();
	private String usuario, gcmcode;
	private boolean resultado;
	private MainActivity mainActivity;
	private ProgressDialog dialogCarga; //Mientras seejecuta la peticion en el lado del servidor
	//se mostrará un mensaje de cargando.
	
	//Inicializa las variables necesarias antes de ejecutar el hilo
	public void inicilizarValores(String name, String gcmcode, MainActivity main){
		this.usuario = name;
		//this.gcmcode = gcmcode;
		this.mainActivity = main;
		dialogCarga = new ProgressDialog(this.mainActivity);
        dialogCarga.setMessage("Loading...");
        dialogCarga.setIndeterminate(false);
        dialogCarga.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialogCarga.setCancelable(true);
        dialogCarga.show();
	}

	//Ejecución del hilo. Se envia la petición al servidor y comprobamos, con JSONObject, si
	//se ha realizado el registro correctamente
	@Override
	protected Boolean doInBackground(Void... params) { 
        if(mainActivity.comprobarServiciosGoogle()){
			  Context context = mainActivity.getApplicationContext();
			  GoogleCloudMessaging serverGCM = GoogleCloudMessaging.getInstance(context);
			  try {
				gcmcode = serverGCM.register(FuncionesUtiles.getSenderID());
			  } catch (IOException e) {}
        }
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("tag","usersave"));
        nameValuePairs.add(new BasicNameValuePair("username", usuario));
        nameValuePairs.add(new BasicNameValuePair("gcmcode", gcmcode));
        JSONObject jdata = peticionPostServidor.getserverdata(nameValuePairs, FuncionesUtiles.getIPServer());
        if (jdata != null && jdata.length() > 0){
			try {
				int comprobacion = jdata.getInt("success"); //Accedemos al valor 
				if(comprobacion == 1)
					resultado = true;			
				else
					resultado = false;

			} catch (JSONException e) {
				e.printStackTrace();
			}		
        }
        dialogCarga.dismiss();
        mainActivity.validacion(resultado, gcmcode);
        
		return resultado;
	}
	
	
}



