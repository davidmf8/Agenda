package com.example.conexionesServidor;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.example.chat.chatAmigo;
import com.example.persistencia.BDAcceso;
import com.example.utilidades.FuncionesUtiles;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

public class CrearEventoAsynTask extends AsyncTask<Void,Boolean,Boolean>{

	private HttpJsonObject peticionPostServidor = new HttpJsonObject();
	private ArrayList<String> datosEvento;
	private String usuario, mensaje;
	private boolean resultado, tag;
	private Context context;
	private SharedPreferences misPreferencias;
	
	//Inicializa las variables necesarias antes de ejecutar el hilo
	public void inicilizarValores(String usuario, ArrayList<String> datosEvento, Context context, boolean tag){
		this.usuario = usuario;
		this.datosEvento = datosEvento;
		this.context = context;
		this.tag = tag;
		this.mensaje = datosEvento.get(0);
		for(int i = 1; i < datosEvento.size(); i++)
			mensaje = mensaje + "-" + datosEvento.get(i);
	}

	//Ejecución del hilo. Se envia la petición al servidor y comprobamos, con JSONObject, si
	//se ha realizado el registro correctamente
	@Override
	protected Boolean doInBackground(Void... params) { 
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        if(tag)
            nameValuePairs.add(new BasicNameValuePair(FuncionesUtiles.TAG,"notificacionPush"));
        else
        	nameValuePairs.add(new BasicNameValuePair(FuncionesUtiles.TAG,"notificacionPushGrupo"));
        nameValuePairs.add(new BasicNameValuePair(FuncionesUtiles.USERNAME, usuario));
        nameValuePairs.add(new BasicNameValuePair("message", mensaje));
        nameValuePairs.add(new BasicNameValuePair("usersend", "CrearEvento"));
        JSONObject jdata;
        
        do{
          jdata = peticionPostServidor.getserverdata(nameValuePairs, FuncionesUtiles.getIPServer());
        }while(!FuncionesUtiles.existeConexion(context));
        
		return resultado;
	}
	
	protected void onPostExecute(String result){
		
	}


}