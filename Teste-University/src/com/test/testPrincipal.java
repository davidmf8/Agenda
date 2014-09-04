package com.test;

import android.content.SharedPreferences;
import android.test.ActivityInstrumentationTestCase2;

import com.example.agendauca.*;
import com.example.utilidades.FuncionesUtiles;
import com.robotium.solo.Solo;

public class testPrincipal extends ActivityInstrumentationTestCase2<MainActivity>{

	private Solo solo;

	public testPrincipal() {
		super(MainActivity.class);
	}
	
	public void setUp() throws Exception {
	    solo = new Solo(getInstrumentation(), getActivity());
	}
	
	public void tearDown() throws Exception {
		//solo.finishOpenedActivities();
	}
	
	public void testRegistro() throws Exception {
		solo.unlockScreen();
		solo.enterText(0, "UsuarioTest");
		solo.clickOnButton("Enviar");  
		solo.waitForActivity(MenuInicial.class, 30000);
		SharedPreferences misPreferencias = getActivity().getSharedPreferences(FuncionesUtiles.getPreferencias(), 0);
		String miUsuario = misPreferencias.getString(FuncionesUtiles.getUsuario(), "");
		assertEquals("UsuarioTest", miUsuario); 
	}

}
