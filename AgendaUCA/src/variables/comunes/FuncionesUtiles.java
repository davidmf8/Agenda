package variables.comunes;

import android.os.Environment;

public class FuncionesUtiles {
   private static String IPServer = "http://192.168.1.33:81/AgendaUCA/index.php";
	
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
}
