package com.example.agendauca;

import java.io.IOException;

import variables.comunes.FuncionesUtiles;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.*;
import com.example.conexionesMiServidor.LoginAsynTask;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

//Crea un usuario nuevo la primera vez que se inicia la aplicacion. Las demas veces no hara falta logearse
//Se guardar� el usuario establecido.
public class MainActivity extends Activity implements OnClickListener{
	private SharedPreferences misPreferencias;
    private Button registrar;
    private static boolean error = false;
    private EditText usuario;
    private TextView mensajeInicial;
    private LoginAsynTask conexionLogin;
    private String name, gcm;
    private Context context;
    private GoogleCloudMessaging serverGCM;

    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		comprobarPreferencias();
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		registrar = (Button)findViewById(R.id.Login);
		registrar.setOnClickListener(this);
		
		mensajeInicial = (TextView)findViewById(R.id.Mensaje);
		usuario = (EditText)findViewById(R.id.UserName);
		
		if(error){
			mensajeInicial.setText("Usuario no v�lido. Inserte otro nombre de usuario");
		}
	}
	
	private void comprobarPreferencias() {
		misPreferencias = getSharedPreferences(FuncionesUtiles.getPreferencias(), MODE_PRIVATE);
		String user = misPreferencias.getString(FuncionesUtiles.getUsuario(), "");
		if(user != ""){
			Intent cambio_actividad = new Intent();
			cambio_actividad.setClass(this, MenuInicial.class);
		    startActivity(cambio_actividad);
		    finish();
		}
	}

	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.Login){ //Lanzo un asyncTask con la peticion de registro.
			name = usuario.getText().toString();
			if(comprobarServiciosGoogle()){
			  context = getApplicationContext();
			  serverGCM = GoogleCloudMessaging.getInstance(context);
			  try {
				gcm = serverGCM.register(FuncionesUtiles.getSenderID());
			  } catch (IOException e) {}
			  
			  conexionLogin = new LoginAsynTask();
			  conexionLogin.inicilizarValores(name, gcm, this);
			  conexionLogin.execute();
			}
		}	
	}
	
	private boolean comprobarServiciosGoogle(){
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        9000).show();
            } 
            else 
                finish();
            return false;
        }
        return true;
		
	}
	
	//Validar el resultado cuando el hilo termina su ejecuci�n.
	public  void validacion(boolean result){
		Intent cambio_actividad = new Intent();
		if(result == false){
			error = true;
			cambio_actividad.setClass(this, MainActivity.class);
	        startActivity(cambio_actividad);
		}
		else{
			SharedPreferences.Editor misPreferenciasModificadas = misPreferencias.edit();
			misPreferenciasModificadas.putString(FuncionesUtiles.getUsuario(), name);
			misPreferenciasModificadas.commit();
			error = false;
			cambio_actividad.setClass(this, MenuInicial.class);
	        startActivity(cambio_actividad);
		}
		finish();
	}
}
