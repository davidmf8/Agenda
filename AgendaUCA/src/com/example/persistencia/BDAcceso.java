package com.example.persistencia;

import java.util.ArrayList;
import java.util.Date;

import com.example.utilidades.Mensaje;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

//BDAcceso se encarga de eliminaciones, consultas e inserciones en la base de datos
public class BDAcceso {
	private static String KEY_NOMBRE = "nombre";
	private static String KEY_MENSAJE = "mensaje";
	private static String KEY_TIPO = "tipo";
	private static String KEY_FECHA = "fecha";
	private static String KEY_GRUPO = "autorgrupo";
	private static String BASE_DATOS_MENSAJE = "MENSAJES";
	private static String BASE_DATOS_USUARIO = "AMIGOS";
	private static String NOMBRE_BD = "Amigos";
	private static String KEY_NUEVO_MENSAJE = "nuevoMensaje";
	private static int version = 1;
	private Context context;
	private SQLiteDatabase database;
	private BDUsuarios usuarios;
	
	public BDAcceso(Context context){
		this.context = context;
	}
	
	public BDAcceso BDopen() throws SQLiteException{ //Devuelve un objeto para trabajar con la BD. 
		usuarios = new BDUsuarios(context, NOMBRE_BD, null, version);
		database = usuarios.getWritableDatabase();
		return this;
	}
	
	public void BDclose(){ //Cierra la base de datos
		usuarios.close();
	}
	
	public long insertarUsuario(String nombre){ //Inserta un nuevo usuario en la base de datos
		ContentValues datosUsuario = new ContentValues();
		datosUsuario.put(KEY_NOMBRE, nombre);
		datosUsuario.put(KEY_NUEVO_MENSAJE, 0);
		long resultado = database.insert(BASE_DATOS_USUARIO, null, datosUsuario);
		return resultado;
	}
	
	public boolean isNuevoMensaje(String nombre){
		String sql = "SELECT * FROM AMIGOS WHERE nombre='"+nombre+"'";
		Cursor cursor = database.rawQuery(sql, null);
		if(cursor.moveToFirst()){
			if(cursor.getInt(1) == 0)
				return false;
			else
				return true;
		}
		return false;
	}
	
	public void setNuevoMensaje(boolean nuevoMensaje, String nombre){
		int actualizarNuevoMensaje;
		if(nuevoMensaje)
			actualizarNuevoMensaje = 1;
		else
			actualizarNuevoMensaje = 0;
		
		/*ContentValues actualizarDatos = new ContentValues();
		actualizarDatos.put(KEY_NUEVO_MENSAJE, actualizarNuevoMensaje);
		database.update(BASE_DATOS_USUARIO, actualizarDatos, "nombre="+nombre, null);*/
		database.execSQL("UPDATE AMIGOS SET nuevoMensaje="+actualizarNuevoMensaje + " WHERE nombre='"+nombre+"'");
	}
	
	public void insertarMensaje(String mensaje, String nombre, int tipo, String grupo){
		ContentValues datosMensaje = new ContentValues();
		datosMensaje.put(KEY_MENSAJE, mensaje);
		datosMensaje.put(KEY_NOMBRE, nombre);
		datosMensaje.put(KEY_TIPO, tipo);
		Date fechaActual = new Date();
		datosMensaje.put(KEY_FECHA, fechaActual.toLocaleString());
		datosMensaje.put(KEY_GRUPO, grupo);
        database.insert(BASE_DATOS_MENSAJE, null, datosMensaje);
	}
	
	public ArrayList<String> getUsuarios(){ //Consulta sobre todos los usuarios en la base de datos
		ArrayList<String> usuarios = new ArrayList<String>();
		String sql = "SELECT * FROM AMIGOS";
		Cursor cursor = database.rawQuery(sql, null);
		if(cursor.moveToFirst()){
			do {
		          usuarios.add(cursor.getString(0));
		          System.out.println(cursor.getString(0) + cursor.getInt(1));
		     } while(cursor.moveToNext());
		}
		return usuarios;
	}
	
	public ArrayList<Mensaje> getMensajesUsuarioFechaActual(String nombre){
		String mensaje, fecha, autor;
		int tipo;
		Mensaje mensajes;
		ArrayList<Mensaje> mensajesUsuario = new ArrayList<Mensaje>();
		Date fechaActual = new Date();
		String fechaHoy = fechaActual.toLocaleString();
		String[] fechaYHora = fechaHoy.split(" ");
		String splitFecha = fechaYHora[0];
		//Log.d("FECHA", splitFecha);
		String sql = "SELECT * FROM MENSAJES WHERE nombre='"+nombre+"' AND fecha LIKE '"+splitFecha+"%'";
		Cursor cursor = database.rawQuery(sql, null);
		if(cursor.moveToFirst()){
			do {
				mensaje = cursor.getString(0);
				tipo = cursor.getInt(2);
				fecha = cursor.getString(3);
				autor = cursor.getString(4);
				mensajes = new Mensaje(mensaje, fecha, tipo, autor);
		        mensajesUsuario.add(mensajes);
		    } while(cursor.moveToNext());
		}
		
		/*for(int i = 0; i < mensajesUsuario.size(); i++){
			Log.d("Hora" ,mensajesUsuario.get(i).getMensaje());
			Log.d("Hora" ,mensajesUsuario.get(i).getFecha());
		}*/

		return mensajesUsuario;
	}
	
	public ArrayList<Mensaje> getMensajesUsuario (String nombre){
		String mensaje, fecha, autor;
		int tipo;
		Mensaje mensajes;
		ArrayList<Mensaje> mensajesUsuario = new ArrayList<Mensaje>();
		String sql = "SELECT * FROM MENSAJES WHERE nombre='"+nombre+"'";
		Cursor cursor = database.rawQuery(sql, null);
		if(cursor.moveToFirst()){
			do {
				mensaje = cursor.getString(0);
				tipo = cursor.getInt(2);
				fecha = cursor.getString(3);
				autor = cursor.getString(4);
				mensajes = new Mensaje(mensaje, fecha, tipo, autor);
		        mensajesUsuario.add(mensajes);
		    } while(cursor.moveToNext());
		}

		return mensajesUsuario;
	}
	
	public void eliminarUsuario(String nombre){ //Elimina un usuario de la base de datos
		String sql = "DELETE FROM AMIGOS WHERE nombre='"+nombre+"'";
		database.execSQL(sql);
	}
	
	public void eliminarMensajesUsuario(String nombre){
		String sql = "DELETE FROM MENSAJES WHERE nombre='"+nombre+"'";
		database.execSQL(sql);
	}
	
	public boolean existeUsuario(String nombre){ //Comprueba si un usuario existe en la base de datos
		String sql = "SELECT * FROM AMIGOS WHERE nombre='"+nombre+"'";
		Cursor cursor = database.rawQuery(sql, null);
		if(cursor.getCount() == 1){
			return true;
		}
		return false;
	}
}
