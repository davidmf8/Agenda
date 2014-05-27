package com.example.utilidades;

public class Mensaje {
    String mensaje, fecha;
    int tipo;
    
    public Mensaje(String mensaje, String fecha, int tipo){
    	this.mensaje = mensaje;
    	this.fecha = fecha;
    	this.tipo = tipo;
    }    
    
	public String getMensaje() {
		return mensaje;
	}

	public String getFecha() {
		return fecha;
	}

	public int getTipo() {
		return tipo;
	}
    
}
