package com.example.agendauca;

import java.io.File;
import android.content.Context;
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
   
   public static File[] getDir() throws NullPointerException{
	   if(estadoLectura()){
	     Context context = null;
	     File directorioPrincipal = context.getExternalFilesDir(null);
	     File[] misCarpetas = directorioPrincipal.listFiles();
	     return misCarpetas;
	   }
	   	   
	   return new File[0];
   }
   

}
