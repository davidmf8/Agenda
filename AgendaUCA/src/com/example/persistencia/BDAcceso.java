package com.example.persistencia;

import java.util.ArrayList;
import java.util.Date;

import com.example.utilidades.Mensaje;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

//BDAcceso se encarga de eliminaciones, consultar e inserciones en la base de datos
public class BDAcceso {
	private static int ID = 1;
	private static String KEY_ID = "id";
	private static String KEY_NOMBRE = "nombre";
	private static String KEY_MENSAJE = "mensaje";
	private static String KEY_TIPO = "tipo";
	private static String KEY_FECHA = "fecha";
	private static String BASE_DATOS_MENSAJE = "MENSAJES";
	private static String BASE_DATOS_USUARIO = "AMIGOS";
	private static String NOMBRE_BD = "Amigos";
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
	
	public void insertarUsuario(String nombre){ //Inserta un nuevo usuario en la base de datos
		ContentValues datosUsuario = new ContentValues();
		datosUsuario.put(KEY_NOMBRE, nombre);
		datosUsuario.put(KEY_ID, ID);
		long resultado = database.insert(BASE_DATOS_USUARIO, null, datosUsuario);
		if(resultado != -1)
			ID++;
	}
	
	public void insertarMensaje(String mensaje, String nombre, int tipo){
		ContentValues datosMensaje = new ContentValues();
		datosMensaje.put(KEY_MENSAJE, mensaje);
		datosMensaje.put(KEY_NOMBRE, nombre);
		datosMensaje.put(KEY_TIPO, tipo);
		Date fechaActual = new Date();
		datosMensaje.put(KEY_FECHA, fechaActual.toLocaleString());
		database.insert(BASE_DATOS_MENSAJE, null, datosMensaje);
	}
	
	public ArrayList<String> getUsuarios(){ //Consulta sobre todos los usuarios en la base de datos
		ArrayList<String> usuarios = new ArrayList<String>();
		String sql = "SELECT * FROM AMIGOS";
		Cursor cursor = database.rawQuery(sql, null);
		if(cursor.moveToFirst()){
			do {
		          usuarios.add(cursor.getString(0));
		     } while(cursor.moveToNext());
		}
		return usuarios;
	}
	
	public ArrayList<Mensaje> getMensajesUsuarioFechaActual(String nombre){
		String mensaje, fecha;
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
				mensajes = new Mensaje(mensaje, fecha, tipo);
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
		String mensaje, fecha;
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
				mensajes = new Mensaje(mensaje, fecha, tipo);
		        mensajesUsuario.add(mensajes);
		    } while(cursor.moveToNext());
		}

		for(int i = 0; i < mensajesUsuario.size(); i++){
			Log.d("Hora" ,mensajesUsuario.get(i).getMensaje());
			Log.d("Hora" ,mensajesUsuario.get(i).getFecha());
		}

		return mensajesUsuario;
	}
	
	public void eliminarUsuario(String nombre){ //Elimina un usuario de la base de datos
		String sql = "DELETE FROM AMIGOS WHERE nombre='"+nombre+"'";
		database.execSQL(sql);
	}
	
	public int usuarioID(String nombre){ //Obtiene el ID de un usuario de la base de datos
		String sql = "SELECT id FROM AMIGOS WHERE nombre='"+nombre+"'";
		Cursor cursor = database.rawQuery(sql, null);
		int idUsuario = 0;
		if(cursor.getCount() == 1){
			cursor.moveToFirst();
			idUsuario = cursor.getInt(0);
		}
		return idUsuario;
	}
}
