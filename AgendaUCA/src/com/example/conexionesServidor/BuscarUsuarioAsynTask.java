package com.example.conexionesServidor;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.widget.Toast;
import com.example.persistencia.BDAcceso;
import com.example.utilidades.FuncionesUtiles;

public class BuscarUsuarioAsynTask  extends AsyncTask<Void,Void,String>{
	private final static String ERROR = "Error";
	private HttpJsonObject peticionPostServidor = new HttpJsonObject();
	private String usuario;
	private String resultado;
	private Activity activity;
	private ProgressDialog dialogCarga; //Mientras seejecuta la peticion en el lado del servidor
	//se mostrará un mensaje de cargando.
	
	//Inicializa las variables necesarias antes de ejecutar el hilo
	public void inicilizarValores(String name, Activity activity){
		this.usuario = name;
		this.activity = activity;
		dialogCarga = new ProgressDialog(this.activity);
        dialogCarga.setMessage("Loading...");
        dialogCarga.setIndeterminate(false);
        dialogCarga.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialogCarga.setCancelable(true);
        dialogCarga.show();
	}

	//Ejecución del hilo. Se envia la petición al servidor y comprobamos, con JSONObject, si
	//se ha realizado el registro correctamente
	@Override
	protected String doInBackground(Void... params) { 
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair(FuncionesUtiles.TAG,"getuser"));
        nameValuePairs.add(new BasicNameValuePair(FuncionesUtiles.USERNAME, usuario));
        JSONObject jdata = peticionPostServidor.getserverdata(nameValuePairs, FuncionesUtiles.getIPServer());
        if (jdata != null && jdata.length() > 0){
			try {
				resultado = jdata.getString("success"); //Accedemos al valor 
				System.out.println(resultado);

			} catch (JSONException e) {
				e.printStackTrace();
			}		
        }
        
        
        dialogCarga.dismiss();
        
		return resultado;
	}
	
	protected void onPostExecute(String result){
		if(resultado == ERROR){
			Toast.makeText(activity, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
		}
		else{
			BDAcceso BD = new BDAcceso(activity);
			BD = BD.BDopen();
			BD.insertarUsuario(usuario, result);
			Toast.makeText(activity, "Usuario agregado", Toast.LENGTH_SHORT).show();
		}
	}

}
