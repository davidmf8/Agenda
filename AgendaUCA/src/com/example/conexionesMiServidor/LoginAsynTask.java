package com.example.conexionesMiServidor;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.agendauca.MainActivity;

import variables.comunes.FuncionesUtiles;

import android.app.ProgressDialog;
import android.os.AsyncTask;

public class LoginAsynTask extends AsyncTask<Void,Boolean,Boolean>{
	private HttpJsonObject post = new HttpJsonObject();
	private String name, gcmcode;
	private boolean resultado;
	private MainActivity main;
	private ProgressDialog progDailog;
	
	public void inicilizarValores(String name, String gcmcode, MainActivity main){
		this.name = name;
		this.gcmcode = gcmcode;
		this.main = main;
		progDailog = new ProgressDialog(this.main);
        progDailog.setMessage("Loading...");
        progDailog.setIndeterminate(false);
        progDailog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progDailog.setCancelable(true);
        progDailog.show();
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("tag","usersave"));
        nameValuePairs.add(new BasicNameValuePair("username", name));
        nameValuePairs.add(new BasicNameValuePair("gcmcode", "54d"));
		
        JSONObject jdata = post.getserverdata(nameValuePairs, FuncionesUtiles.getIPServer());
        if (jdata != null && jdata.length() > 0){
			try {
				int comprobacion = jdata.getInt("success"); //Accedem al valor 
				if(comprobacion == 1){
					resultado = true;
					
				}
				else{
					resultado = false;
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}		
        }
        progDailog.dismiss();
        main.validacion(resultado);
        
		return resultado;
	}
}



