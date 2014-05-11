package com.example.conexionesServidor;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.agendauca.chatAmigo;
import com.example.persistencia.BDAcceso;
import com.example.utilidades.FuncionesUtiles;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

public class EnviarMensajeAsynTask extends AsyncTask<Void,Boolean,Boolean>{

	private HttpJsonObject peticionPostServidor = new HttpJsonObject();
	private String usuario, mensaje;
	private boolean resultado;
	private chatAmigo context;
	private SharedPreferences misPreferencias;
	
	//Inicializa las variables necesarias antes de ejecutar el hilo
	public void inicilizarValores(String usuario, String mensaje, chatAmigo context){
		this.usuario = usuario;
		this.mensaje = mensaje;
		this.context = context;

	}

	//Ejecución del hilo. Se envia la petición al servidor y comprobamos, con JSONObject, si
	//se ha realizado el registro correctamente
	@Override
	protected Boolean doInBackground(Void... params) { 
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair(FuncionesUtiles.TAG,"notificacionPush"));
        nameValuePairs.add(new BasicNameValuePair(FuncionesUtiles.USERNAME, usuario));
        nameValuePairs.add(new BasicNameValuePair("message", mensaje));
        misPreferencias = context.getSharedPreferences(FuncionesUtiles.getPreferencias(), context.MODE_PRIVATE);
		String user = misPreferencias.getString(FuncionesUtiles.getUsuario(), "");
        nameValuePairs.add(new BasicNameValuePair("usersend", user));
        JSONObject jdata;
        
        do{
          jdata = peticionPostServidor.getserverdata(nameValuePairs, FuncionesUtiles.getIPServer());
        }while(!FuncionesUtiles.existeConexion(context));
		
		//context.actualizarLista();
        
		return resultado;
	}


}
