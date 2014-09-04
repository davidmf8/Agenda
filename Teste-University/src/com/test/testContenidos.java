package com.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.Adapter;
import android.widget.ListView;

import com.example.agendauca.MenuInicial;
import com.example.agendauca.R;
import com.example.ficheros.*;
import com.robotium.solo.Solo;

public class testContenidos extends ActivityInstrumentationTestCase2<MenuInicial>{

	private Solo solo;

	public testContenidos() {
		super(MenuInicial.class);
	}
	
	public void setUp() throws Exception {
	    solo = new Solo(getInstrumentation(), getActivity());
	}
	
	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}
	
	public void test1CreacionCarpeta() throws Exception {
		solo.clickOnView(solo.getView(R.id.Directorios));
		solo.pressMenuItem(0);
		solo.clickOnMenuItem("Nueva carpeta");
		solo.waitForDialogToOpen();
		solo.enterText(0, "CarpetaTest");
		solo.clickOnButton("Aceptar");
		assertTrue(solo.searchText("CarpetaTest"));
	}
	
	public void test2CreacionNota() throws Exception {
		solo.clickOnView(solo.getView(R.id.Directorios));
		solo.pressMenuItem(0);
		solo.clickOnMenuItem("Nota");
		solo.waitForActivity(BlocNotas.class);
		solo.enterText(0, "Nota de prueba automatica");
		solo.clickOnButton("Guardar");
		solo.waitForActivity(ListarFicheros.class);
		solo.clickInList(2);
		solo.waitForActivity(ListarFicheros.class);
		solo.clickInList(0);
		assertTrue(solo.searchText("Nota de prueba automatica"));
	}
	
	public void test3CreacionAudio() throws Exception {
		solo.clickOnView(solo.getView(R.id.Directorios));
		solo.pressMenuItem(0);
		solo.clickOnMenuItem("Audio");
		solo.waitForActivity(Audio.class);
		solo.clickOnButton("Grabar");
		solo.sleep(10000);
		solo.clickOnButton("Detener");
		solo.goBack();
		solo.clickInList(3);
		solo.waitForActivity(ListarFicheros.class);
		solo.clickInList(0);
		solo.sleep(11000);
		solo.goBack();
		solo.assertCurrentActivity("ListarFicheros", ListarFicheros.class);
	}
	
	public void test4OpcionRenombrar() throws Exception {
		solo.clickOnView(solo.getView(R.id.Directorios));
		solo.clickLongInList(1);
		solo.clickOnMenuItem("Renombrar");
		solo.waitForDialogToOpen();
		solo.enterText(0, "CarpetaRenombrada");
		solo.clickOnButton("Aceptar");
		assertTrue(solo.searchText("CarpetaRenombrada"));
	}
	
	public void test5OpcionMover() throws Exception {
		solo.clickOnView(solo.getView(R.id.Directorios));
		solo.clickLongInList(1);
		solo.clickOnMenuItem("Mover");
		solo.clickLongInList(2);
		solo.waitForActivity(ListarFicheros.class);
		solo.clickLongInList(2);
		solo.waitForActivity(ListarFicheros.class);
		solo.clickInList(1);
		solo.waitForActivity(ListarFicheros.class);
		assertTrue(solo.searchText("AgendaNotas"));
	}
	
	public void test6OpcionEliminar() throws Exception {
		solo.clickOnView(solo.getView(R.id.Directorios));
		solo.clickLongInList(1);
		ListView listaFicheros = (ListView) solo.getView(R.id.ListaFicheros);
		Adapter adapter = listaFicheros.getAdapter();
		String eliminado = (String) adapter.getItem(0);
		solo.clickOnMenuItem("Eliminar");
		solo.waitForActivity(ListarFicheros.class);
		assertFalse(solo.searchText(eliminado));
	}

}
