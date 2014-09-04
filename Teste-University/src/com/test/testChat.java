package com.test;

import android.test.ActivityInstrumentationTestCase2;

import com.example.agendauca.MenuInicial;
import com.example.agendauca.R;
import com.example.chat.chatAmigo;
import com.robotium.solo.Solo;

public class testChat extends ActivityInstrumentationTestCase2<MenuInicial>{

	private Solo solo;

	public testChat() {
		super(MenuInicial.class);
	}
	
	public void setUp() throws Exception {
	    solo = new Solo(getInstrumentation(), getActivity());
	}
	
	public void test1AgregarAmigo() throws Exception {
		solo.clickOnView(solo.getView(R.id.Notificacion));
		solo.pressMenuItem(0);
		solo.enterText(0, "david"); //Cambiar por el usuario de prueba destino
		solo.clickOnButton("Aceptar");
		while(!solo.waitForDialogToClose()){}
		assertTrue(solo.searchText("david")); //Cambiar por el usuario de prueba destino
	}
	
	public void test2EnviarMensaje() throws Exception {
		solo.clickOnView(solo.getView(R.id.Notificacion));
		solo.clickInList(1);
		solo.enterText(0, "Hola");
		solo.clickOnButton("Enviar");
		solo.sleep(5000);
		assertTrue(solo.searchText("Hola"));
	}
	
	public void test3EliminarHistorial() throws Exception {
		solo.clickOnView(solo.getView(R.id.Notificacion));
		solo.clickInList(1);
		solo.clickOnMenuItem("Eliminar historial");
		solo.sleep(5000);
		assertTrue(solo.searchText("No existen mensajes hoy"));
	}
	
	public void test4CrearEvento() throws Exception {
		solo.clickOnView(solo.getView(R.id.Notificacion));
		solo.clickInList(1);
		solo.waitForActivity(chatAmigo.class);
		//solo.pressMenuItem(0);
		solo.clickOnMenuItem("Crear evento");
		solo.enterText(0, "Evento prueba");
		solo.enterText(1, "Evento generado automaticamente");
		solo.enterText(2, "Dispositivo de prueba");
		solo.enterText(3, "12/11/2014");
		solo.clickOnButton("Cancelar");
		solo.enterText(4, "18:00");
		solo.clickOnButton("Cancelar");
		solo.clickOnButton("Crear evento");
		solo.sleep(10000);
	}
	
	public void test5EliminarAmigo() throws Exception {
		solo.clickOnView(solo.getView(R.id.Notificacion));
		solo.clickLongInList(1);
		solo.clickOnButton("Aceptar");
		assertFalse(solo.searchText("david")); //Cambiar por el usuario de prueba destino
	}
}
