package com.example.conexionesServidor;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.utilidades.FuncionesUtiles;

import android.os.AsyncTask;
import android.util.Log;

public class EnviarMensajeAsynTask extends AsyncTask<Void,Boolean,Boolean>{

	private HttpJsonObject peticionPostServidor = new HttpJsonObject();
	private String usuario, mensaje;
	private boolean resultado;
	
	//Inicializa las variables necesarias antes de ejecutar el hilo
	public void inicilizarValores(String usuario, String mensaje){
		this.usuario = usuario;
		this.mensaje = mensaje;

	}

	//Ejecución del hilo. Se envia la petición al servidor y comprobamos, con JSONObject, si
	//se ha realizado el registro correctamente
	@Override
	protected Boolean doInBackground(Void... params) { 
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("tag","sendmessage"));
        nameValuePairs.add(new BasicNameValuePair("username", usuario));
        nameValuePairs.add(new BasicNameValuePair("message", mensaje));
        JSONObject jdata = peticionPostServidor.getserverdata(nameValuePairs, FuncionesUtiles.getIPServer());
        System.out.println(jdata);
        if (jdata != null && jdata.length() > 0){
			try {
				int comprobacion = jdata.getInt("success"); //Accedemos al valor 
				System.out.println(comprobacion);
				if(comprobacion == 1) 
					resultado = true;			
				else
					resultado = false;

			} catch (JSONException e) {
				e.printStackTrace();
			}		
        }
        System.out.println(resultado);
        
		return resultado;
	}
}
