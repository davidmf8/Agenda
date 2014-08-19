package com.example.utilidades;

//Clase que representa un mensaje del chat
public class Mensaje {
    String mensaje, fecha, autor;
    int tipo;
    
    public Mensaje(String mensaje, String fecha, int tipo, String autor){
    	this.mensaje = mensaje;
    	this.fecha = fecha;
    	this.tipo = tipo;
    	this.autor = autor;
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
	
	public String getAutor() {
		return autor;
	}
    
}
