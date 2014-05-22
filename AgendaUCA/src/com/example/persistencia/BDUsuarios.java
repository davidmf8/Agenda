package com.example.persistencia;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

//Crea la base de datos
public class BDUsuarios extends SQLiteOpenHelper{	
	String SQLiteCreate = "CREATE TABLE AMIGOS (nombre TEXT PRIMARY KEY)";
	String SQLiteMensajes = "CREATE TABLE MENSAJES (mensaje TEXT, nombre TEXT, tipo INTEGER, fecha TEXT)";

	public BDUsuarios(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase bd) {
		bd.execSQL(SQLiteCreate);
		bd.execSQL(SQLiteMensajes);
	}

	@Override
	public void onUpgrade(SQLiteDatabase bd, int versionActual, int nuevaVersion) {
		// TODO Auto-generated method stub
		
	}

}
