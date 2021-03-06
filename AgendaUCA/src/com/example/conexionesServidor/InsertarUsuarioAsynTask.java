package com.example.conexionesServidor;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.widget.Toast;

import com.example.chat.chatPrincipal;
import com.example.persistencia.BDAcceso;
import com.example.utilidades.FuncionesUtiles;

//Busca un usuario en el servidor, para ser agregado
public class InsertarUsuarioAsynTask  extends AsyncTask<Void,Void,String>{
	private final static String ERROR = "Error";
	private HttpJsonObject peticionPostServidor = new HttpJsonObject();
	private String usuario;
	private String resultado;
	private chatPrincipal activity;
	private SharedPreferences misPreferencias;
	private ProgressDialog dialogCarga; //Mientras se ejecuta la peticion en el lado del servidor
	//se mostrar� un mensaje de cargando.
	
	//Inicializa las variables necesarias antes de ejecutar el hilo
	public void inicilizarValores(String name, chatPrincipal activity){
		this.usuario = name;
		this.activity = activity;
		dialogCarga = new ProgressDialog(this.activity);
        dialogCarga.setMessage("Cargando...");
        dialogCarga.setIndeterminate(false);
        dialogCarga.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialogCarga.setCancelable(false);
        dialogCarga.show();
	}

	//Ejecuci�n del hilo. Se envia la petici�n de busqueda de usuario al servidor y comprobamos, con JSONObject, si
	//se ha realizado el registro correctamente
	@Override
	protected String doInBackground(Void... params) { 
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair(FuncionesUtiles.TAG,"getUsuario"));
        nameValuePairs.add(new BasicNameValuePair(FuncionesUtiles.USERNAME, usuario));
        misPreferencias = activity.getSharedPreferences(FuncionesUtiles.getPreferencias(), activity.MODE_PRIVATE);
        String miUsuario = misPreferencias.getString(FuncionesUtiles.getUsuario(), "");
        nameValuePairs.add(new BasicNameValuePair("miUsuario", miUsuario));
        JSONObject jdata = peticionPostServidor.getserverdata(nameValuePairs, FuncionesUtiles.getIPServer());
        if (jdata != null && jdata.length() > 0){
			try {
				resultado = jdata.getString("success"); //Accedemos al valor 
			} catch (JSONException e) {
				e.printStackTrace();
			}		
        }
        
        
        dialogCarga.dismiss();   
        
		return resultado;
	}
	
	//Evalua el resultado, si se ha encontrado lo a�ade a la base de datos de la app. Si no muestra un
	//mensaje de usuario no encontrado
	protected void onPostExecute(String result){
		if(resultado == null || resultado.equalsIgnoreCase(ERROR)){
			Toast.makeText(activity, "Usuario no encontrado", Toast.LENGTH_SHORT).show();
		}
		else{
			BDAcceso BD = new BDAcceso(activity);
			BD = BD.BDopen();
			long resultado = BD.insertarUsuario(usuario);
			BD.BDclose();
			if(resultado != -1)
			    Toast.makeText(activity, "Usuario agregado", Toast.LENGTH_SHORT).show();
			else
				Toast.makeText(activity, "No se ha podido realizar la solicitud. Int�ntelo de nuevo.", Toast.LENGTH_SHORT).show();		
		}
		
		activity.actualizarLista();
	}

}
