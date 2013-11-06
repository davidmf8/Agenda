package com.example.agendauca;

import android.os.Environment;

public class FuncionesUtiles {
	
   public static boolean estadoEscritura(){
	   String estado = Environment.getExternalStorageState();
	   if(Environment.MEDIA_MOUNTED.equals(estado)){
		   return true;
	   }
	   return false;
   }
   
   public static boolean estadoLectura(){
	   String estado = Environment.getExternalStorageState();
	   if(Environment.MEDIA_MOUNTED_READ_ONLY.equals(estado)){
		   return true;
	   }
	   return false;
   }
   

}
