package com.example.agendauca;

import com.example.conexionesMiServidor.LoginAsynTask;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Login extends Activity implements OnClickListener{
	private static String IPServer = "http://192.168.1.33:81/AgendaUCA/index.php";
    private Button logear;
    private EditText usuario;
    private EditText contrasenia;
    private LoginAsynTask conexionLogin;
    private boolean comprobacionPeticion;
    
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		logear = (Button)findViewById(R.id.Login);
		logear.setOnClickListener(this);
		
		usuario = (EditText)findViewById(R.id.UserName);
		contrasenia = (EditText)findViewById(R.id.Pass);

	}
	
	@Override
	public void onClick(View v) {
		if(v.getId() == R.id.Login){
			String name = usuario.getText().toString();
            //String password = contrasenia.getText().toString();
			conexionLogin = new LoginAsynTask();
			conexionLogin.inicilizarValores(name, "");
			conexionLogin.execute();
  
		}
		
		Intent cambio_actividad = new Intent();
		cambio_actividad.setClass(this, MainActivity.class);
		startActivity(cambio_actividad);
	}
	
	public static String getIPServer(){
		return IPServer;
	}

}
