package com.example.persistencia;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

public class BDAcceso {
	private static String KEY_NOMBRE = "nombre";
	private static String KEY_GCM = "gcm";
	private static String BASE_DATOS_USUARIO = "AMIGOS";
	private static String NOMBRE_BD = "Amigos";
	private static int version = 1;
	private Context context;
	private SQLiteDatabase database;
	private BDUsuarios usuarios;
	
	public BDAcceso(Context context){
		this.context = context;
	}
	
	public BDAcceso BDopen() throws SQLiteException{
		usuarios = new BDUsuarios(context, NOMBRE_BD, null, version);
		database = usuarios.getWritableDatabase();
		return this;
	}
	
	public void BDclose(){
		usuarios.close();
	}
	
	public long insertarUsuario(String nombre, String gcm){
		ContentValues datosUsuario = new ContentValues();
		datosUsuario.put(KEY_NOMBRE, nombre);
		datosUsuario.put(KEY_GCM, gcm);
		return database.insert(BASE_DATOS_USUARIO, null, datosUsuario);
	}
	
	public String getGCM(String nombre){
		String sql = "SELECT gcm FROM AMIGOS WHERE nombre='"+nombre+"'";
		Cursor cursor = database.rawQuery(sql, null);
		String resGCM = "";
		if(cursor.getCount() == 1){
			cursor.moveToFirst();
			resGCM = cursor.getString(0);
		}
		return resGCM;
	}
}
