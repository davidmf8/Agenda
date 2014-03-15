package com.example.utilidades;

import android.os.Environment;

public class FuncionesUtiles {
   private static String IPServer = "http://192.168.1.33:81/AgendaUCA/index.php";
   private static String Sender_ID = "907123173880";
   private static String preferencias = "MisPreferencias";
   private static String usuario = "usuario";
   private static String gcm = "gcm";
	
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
   
   public static String getIPServer(){
	   return IPServer;
   }
   
   public static String getSenderID(){
	   return Sender_ID;
   }

   public static String getPreferencias() {
	   return preferencias;
   }

   public static String getUsuario() {
	   return usuario;
   }

   public static String getGcm() {
	   return gcm;
   }

}
