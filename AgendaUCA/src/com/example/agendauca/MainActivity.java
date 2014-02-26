package com.example.agendauca;

import com.example.conexionesMiServidor.LoginAsynTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

//Crea un usuario nuevo la primera vez que se inicia la aplicacion. Las demas veces no hara falta logearse
//Se guardará el usuario establecido.
public class MainActivity extends Activity implements OnClickListener{
    private Button registrar;
    private static boolean error = false;
    private EditText usuario;
    private TextView mensajeInicial;
    private LoginAsynTask conexionLogin;

    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		registrar = (Button)findViewById(R.id.Login);
		registrar.setOnClickListener(this);
		
		mensajeInicial = (TextView)findViewById(R.id.Mensaje);
		usuario = (EditText)findViewById(R.id.UserName);
		
		if(error){
			mensajeInicial.setText("Usuario no válido. Inserte otro nombre de usuario");
		}
	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.Login){ //Lanzo un asyncTask con la peticion de registro.
			String name = usuario.getText().toString();
			conexionLogin = new LoginAsynTask();
			conexionLogin.inicilizarValores(name, "", this);
			conexionLogin.execute();
		}	
	}
	
	//Validar el resultado cuando el hilo termina su ejecución.
	public  void validacion(boolean result){
		Intent cambio_actividad = new Intent();
		if(result == false){
			error = true;
			cambio_actividad.setClass(this, MainActivity.class);
	        startActivity(cambio_actividad);
		}
		else{
			error = false;
			cambio_actividad.setClass(this, MenuInicial.class);
	        startActivity(cambio_actividad);
		}
	}
}
