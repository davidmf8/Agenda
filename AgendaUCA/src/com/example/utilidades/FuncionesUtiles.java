package com.example.utilidades;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;

public class FuncionesUtiles {
   private static String IPServer = "http://192.168.1.33:81/AgendaUCA/index.php";
   private static String Sender_ID = "907123173880";
   private static String preferencias = "MisPreferencias";
   private static String usuario = "usuario";
   private static String gcm = "gcm";
   public static final String TAG = "tag";
   public static final String USERNAME = "username";
   public static final String GCM = "gcm";
	
   public static boolean estadoEscritura(){ //Funcion que devuelve si la memoria está preparada para escritura
	   String estado = Environment.getExternalStorageState();
	   if(Environment.MEDIA_MOUNTED.equals(estado)){
		   return true;
	   }
	   return false;
   }
   
   public static boolean estadoLectura(){ //Funcion que devuelve si la memoria está preparada para lectura
	   String estado = Environment.getExternalStorageState();
	   if(Environment.MEDIA_MOUNTED_READ_ONLY.equals(estado) || Environment.MEDIA_MOUNTED.equals(estado)){
		   return true;
	   }
	   return false;
   }
   
   public static String getIPServer(){ //Devuelve la IP del servidor de usuarios registrados en la app
	   return IPServer;
   }
   
   public static String getSenderID(){ //Devuelve el sender_id, ID del servidor de google
	   return Sender_ID;
   }

   public static String getPreferencias() { //Devuelve el nombre de las preferencias de un usuario
	   return preferencias;
   }

   public static String getUsuario() { //Devuelve el tag de usuario
	   return usuario;
   }

   public static String getGcm() { //devuelve el tag de gcm
	   return gcm;
   }

}
