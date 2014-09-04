package com.test;

import android.test.ActivityInstrumentationTestCase2;

import com.example.agendauca.MenuInicial;
import com.example.examenes.*;
import com.example.agendauca.R;
import com.robotium.solo.Solo;

public class testExamenes extends ActivityInstrumentationTestCase2<MenuInicial>{

	private Solo solo;

	public testExamenes() {
		super(MenuInicial.class);
	}
	
	public void setUp() throws Exception {
	    solo = new Solo(getInstrumentation(), getActivity());
	}
	
	public void tearDown() throws Exception {
		
	}
	
	public void test1ConsultarActas() throws Exception {
		solo.clickOnView(solo.getView(R.id.Calificaciones));
		solo.pressMenuItem(0);
		//solo.clickOnMenuItem("Consulta actas");
	}
	
	public void test2ExamenesGrado() throws Exception {
		solo.clickOnView(solo.getView(R.id.Calificaciones));
		solo.clickInList(0);
		solo.waitForActivity(listaExamenes.class);
		solo.sleep(5000);
		solo.clickInList(2);
		solo.clickOnButton("Aceptar");
		solo.goBack();
		//solo.clickOnMenuItem("Consulta actas");
	}
}
