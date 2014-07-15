package com.example.conexionesServidor;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import com.example.chat.chatPrincipal;
import com.example.persistencia.BDAcceso;
import com.example.utilidades.FuncionesUtiles;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

public class EnviarMensajeAsynTask extends AsyncTask<Void,Boolean,Boolean>{
	private HttpJsonObject peticionPostServidor = new HttpJsonObject();
	private String mensaje;
	private String usuario;
	private boolean resultado, tag;
	private Context context;
	private SharedPreferences misPreferencias;
	private ProgressDialog dialogCarga;
	
	//Inicializa las variables necesarias antes de ejecutar el hilo
	public void inicilizarValores(String usuario, String mensaje, Context context, boolean tag){
		this.usuario = usuario;
		this.mensaje = mensaje;
		this.context = context;
		this.tag = tag;
		if(mensaje.equalsIgnoreCase("NuevoGrupo")){
			dialogCarga = new ProgressDialog(this.context);
	        dialogCarga.setMessage("Cargando...");
	        dialogCarga.setIndeterminate(false);
	        dialogCarga.setProgressStyle(ProgressDialog.STYLE_SPINNER);
	        dialogCarga.setCancelable(true);
	        dialogCarga.show();
		}
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
        nameValuePairs.add(new BasicNameValuePair(FuncionesUtiles.MENSAJE, mensaje));
        misPreferencias = context.getSharedPreferences(FuncionesUtiles.getPreferencias(), context.MODE_PRIVATE);
		String miUsuario = misPreferencias.getString(FuncionesUtiles.getUsuario(), "");
        nameValuePairs.add(new BasicNameValuePair(FuncionesUtiles.AUTOR, miUsuario));
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
        
		return resultado;
	}
	
	protected void onPostExecute(Boolean result){
		if(resultado){
		  if(mensaje.equalsIgnoreCase("NuevoGrupo")){
			 BDAcceso BD = new BDAcceso(context);
		     BD = BD.BDopen();
			 BD.insertarUsuario(usuario);
			 BD.BDclose();
			 dialogCarga.dismiss();
			 Toast.makeText(context, "Grupo creado correctamente", Toast.LENGTH_SHORT).show();
			 context.startActivity(new Intent(context, chatPrincipal.class));
		  }
		}
		  else
			Toast.makeText(context, "Servidor no disponible", Toast.LENGTH_SHORT).show();
	}
}
