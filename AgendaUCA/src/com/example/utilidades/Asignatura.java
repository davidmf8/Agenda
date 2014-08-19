package com.example.utilidades;

import java.io.Serializable;

//Clase asignatura que almacenará los datos de una asignatura extraidos de un fichero excel. Hereda
//de serializable para que pueda ser enviado entre activities
public class Asignatura implements Serializable{
	private String asignatura, fecha, lugar;
    
    public Asignatura(String asignatura, String fecha, String lugar){
    	this.asignatura = asignatura;
    	this.fecha = fecha;
    	this.lugar = lugar;
    }

	public String getAsignatura() {
		return asignatura;
	}

	public String getFecha() {
		return fecha;
	}

	public String getLugar() {
		return lugar;
	}
}
