package com.example.conexionesMiServidor;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.agendauca.MainActivity;

import android.os.AsyncTask;

public class LoginAsynTask extends AsyncTask<Void,Void,Boolean>{
	private HttpJsonArray post = new HttpJsonArray();
	private String name, gcmcode;
	private boolean resultado;
	
	public void inicilizarValores(String name, String gcmcode){
		this.name = name;
		this.gcmcode = gcmcode;
	}

	public boolean isResultado() {
		return resultado;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		resultado = false;
		ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("tag","usersave"));
        nameValuePairs.add(new BasicNameValuePair("username", name));
        nameValuePairs.add(new BasicNameValuePair("gcmcode", "54d"));
		
        JSONArray jdata = post.getserverdata(nameValuePairs, MainActivity.getIPServer());
        
        if (jdata != null && jdata.length() > 0){

    		JSONObject json_data; //Creem un objecte JSON
			try {
				
				json_data = jdata.getJSONObject(0); //Obtenim el primer valro retornat com será l'únic podem posar directament 0
				int comprobacion = json_data.getInt("success"); //Accedem al valor 

				if(comprobacion == 1){
					resultado = true;
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}		
        }
        
		return resultado;
	}
}



