package com.example.conexionesMiServidor;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class HttpJsonObject {

	  InputStream is = null;
	  String result = "";
	  
	  public JSONObject getserverdata(ArrayList<NameValuePair> parameters, String urlwebserver ){
	
		  //conecta via http i envia un post
	  httppostconnect(parameters,urlwebserver);
		  
	  if (is != null){//Si rebem resposta
	  
		  getpostresponse();
		  
		 return getjsonarray();
	  
	  }else{
		  
	      return null;

	  }
		  
	  }
	  
	   
	  //Petició http
  private void httppostconnect(ArrayList<NameValuePair> parametres, String urlwebserver){
 	
	//
	try{
		
			/*Parametres de la conexio */
		 	final HttpParams httpParams = new BasicHttpParams();
		 	HttpConnectionParams.setConnectionTimeout(httpParams, 60000);
		 	
	        HttpClient httpclient = new DefaultHttpClient(httpParams);
	        
	        HttpPost httppost = new HttpPost(urlwebserver);
	        
	        httppost.setEntity(new UrlEncodedFormEntity(parametres));
	        
	        //Executem la petició enviant dades per el post
	        HttpResponse response = httpclient.execute(httppost); 
	        HttpEntity entity = response.getEntity();
	         is = entity.getContent();
	         
	}catch(Exception e){
		e.printStackTrace();
	        //Log.e("log_tag", "Error in http connection "+e.toString());
	}
	
}

public void getpostresponse(){

	//Converteix el Json a string
	try{
	        BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
	        StringBuilder sb = new StringBuilder();
	        String line = null;
	        while ((line = reader.readLine()) != null) {
	                sb.append(line + "\n");
	        }
	        is.close();
	 
	        result = sb.toString();
	        //Log.e("getpostresponse"," result = "+sb.toString());
	}catch(Exception e){
		e.printStackTrace();
	        //Log.e("log_tag", "Error converting result "+e.toString());
	}
}

public JSONObject getjsonarray(){
	//parse json data
	try{
		 JSONObject jArray;
		 if(result.equals("false") || result.equals("null")){
			 jArray = new JSONObject();
		 }else{
			 jArray = new JSONObject(result);
        }
		return jArray;
	}
	catch(JSONException e){
		e.printStackTrace();
	        //Log.e("log_tag", "Error parsing data "+e.toString());
	        return null;
	}
		
}
}
