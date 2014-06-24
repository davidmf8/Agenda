package com.example.conexionesServidor;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.example.chat.creacionEvento;
import com.example.utilidades.FuncionesUtiles;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;

public class CrearEventoAsynTask extends AsyncTask<Void,Void,Boolean>{

	private HttpJsonObject peticionPostServidor = new HttpJsonObject();
	private ArrayList<String> datosEvento;
	private String usuario, mensaje;
	private boolean resultado, tag;
	private creacionEvento context;
	private ProgressDialog dialogCarga;
	
	@Override
	protected void onPostExecute(Boolean result) {
		if(resultado){
			Toast.makeText(context, "Evento creado correctamente", Toast.LENGTH_SHORT).show();
			context.actualizacionEvento();
		}
	    else
		   Toast.makeText(context, "No se ha podido crear el evento. Inténtelo más tarde", Toast.LENGTH_SHORT).show();
	}

	//Inicializa las variables necesarias antes de ejecutar el hilo
	public void inicilizarValores(String usuario, ArrayList<String> datosEvento, creacionEvento context, boolean tag){
		this.usuario = usuario;
		this.datosEvento = datosEvento;
		this.context = context;
		this.tag = tag;
		this.mensaje = this.datosEvento.get(0);
		for(int i = 1; i < this.datosEvento.size(); i++)
			mensaje = mensaje + "-" + this.datosEvento.get(i);
		dialogCarga = new ProgressDialog(this.context);
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
        
        try {
			if(jdata.getInt("success") == 200)
				resultado = true;
			else
				resultado = false;
		} catch (Exception e) {}
        dialogCarga.dismiss();
		//context.actualizacionEvento(resultado);
		return resultado;
	}
}