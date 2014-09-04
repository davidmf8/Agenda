package com.test;

import android.test.ActivityInstrumentationTestCase2;

import com.example.agendauca.MenuInicial;
import com.example.agendauca.R;
import com.robotium.solo.Solo;

public class testCalendario extends ActivityInstrumentationTestCase2<MenuInicial>{

	private Solo solo;

	public testCalendario() {
		super(MenuInicial.class);
	}
	
	public void setUp() throws Exception {
	    solo = new Solo(getInstrumentation(), getActivity());
	}
	
	public void tearDown() throws Exception {
		solo.finishOpenedActivities();
	}
	
	public void test1Calendario() throws Exception {
		solo.clickOnView(solo.getView(R.id.Evento));
	}
}
