package com.example.persistencia;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

//Crea la base de datos
public class BDUsuarios extends SQLiteOpenHelper{
	
	String SQLiteCreate = "CREATE TABLE AMIGOS (nombre TEXT, gcm TEXT)";

	public BDUsuarios(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase bd) {
		// TODO Auto-generated method stub
		bd.execSQL(SQLiteCreate);
	}

	@Override
	public void onUpgrade(SQLiteDatabase bd, int versionActual, int nuevaVersion) {
		// TODO Auto-generated method stub
		
	}

}
