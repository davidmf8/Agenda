package com.example.persistencia;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

//BDAcceso se encarga de eliminaciones, consultar e inserciones en la base de datos
public class BDAcceso {
	private static int ID = 1;
	private static String KEY_ID = "id";
	private static String KEY_NOMBRE = "nombre";
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
	
	public void eliminarUsuario(String nombre){ //Elimina un usuario de la base de datos
		String sql = "DELETE FROM AMIGOS WHERE nombre='"+nombre+"'";
		database.execSQL(sql);
	}
	
	public int usuarioID(String nombre){ //Obtiene el ID de un usuario de la base de datos
		String sql = "SELECT id FROM AMIGOS WHERE nombre='"+nombre+"'";
		Cursor cursor = database.rawQuery(sql, null);
		int idUsuario = 9;
		if(cursor.getCount() == 1){
			cursor.moveToFirst();
			idUsuario = cursor.getInt(0);
		}
		return idUsuario;
	}
}
