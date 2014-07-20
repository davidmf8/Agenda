package com.example.chat;

import java.util.ArrayList;
import java.util.Date;

import com.example.agendauca.R;
import com.example.persistencia.BDAcceso;
import com.example.utilidades.Mensaje;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.LinearLayout.LayoutParams;

//Adapter para la visualizacion de los mensajes: fecha hora y autor.
public class mensajeAdapter extends BaseAdapter{
	private Context contextChat;
	private ArrayList<Mensaje> mensajesAMostrar;
	private Date fechaActual;
	private String compararFecha;
	private int tipoChat;
	
	public mensajeAdapter(Context context, ArrayList<Mensaje> mensajes, int grupo){
		super();
		contextChat = context;
		mensajesAMostrar = mensajes;
		tipoChat = grupo;
	}

	@Override
	public int getCount() {
		return mensajesAMostrar.size();
	}

	@Override
	public Object getItem(int position) {
		return mensajesAMostrar.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Mensaje mensaje = mensajesAMostrar.get(position);
		ViewMensaje holder; 
		if(convertView == null)
		{
			holder = new ViewMensaje();
			convertView = LayoutInflater.from(contextChat).inflate(R.layout.sms_row, parent, false);
			holder.mensaje = (TextView) convertView.findViewById(R.id.texto_mensaje);
			holder.hora = (TextView) convertView.findViewById(R.id.text2);
			convertView.setTag(holder);
		}
		else
			holder = (ViewMensaje) convertView.getTag();
		
		String fechaMensaje = esFechaActual(mensaje.getFecha());
		if(tipoChat > 1 && !mensaje.getAutor().equalsIgnoreCase(""))
			holder.mensaje.setText(" " + mensaje.getAutor() +":" + "\n" +" " + mensaje.getMensaje() + " ");
		else
			holder.mensaje.setText(" " + mensaje.getMensaje() + " ");
		holder.hora.setText(fechaMensaje);
		
		LayoutParams lp = (LayoutParams) holder.mensaje.getLayoutParams();
		LayoutParams lh = (LayoutParams) holder.hora.getLayoutParams();

		if(mensaje.getTipo() == 1){
			holder.mensaje.setBackgroundResource(R.drawable.enviado);
			lp.gravity = Gravity.RIGHT;
			lh.gravity = Gravity.RIGHT;
		}
	    else{
			holder.mensaje.setBackgroundResource(R.drawable.recibido);
			lp.gravity = Gravity.LEFT;
			lh.gravity = Gravity.LEFT;
		}
		
		holder.mensaje.setLayoutParams(lp);
		holder.hora.setLayoutParams(lh);
		holder.mensaje.setTextColor(R.color.textColor);	
		return convertView;
	}
	
	private String esFechaActual(String fechaMensaje){
		fechaActual = new Date();
		compararFecha = fechaActual.toLocaleString();
		String[] fechaMsj = fechaMensaje.split(" ");
		String[] fechaHoy = compararFecha.split(" ");
		if(fechaMsj[0].equalsIgnoreCase(fechaHoy[0])){
			return fechaMsj[1];
		}
		else{
			return fechaMensaje;
		}
	}
	
	private static class ViewMensaje{
		TextView mensaje;
		TextView hora;
	}
	
	//Si esta activada la opcion de historial, se mostraran todos los mensajes.
	public void mostrarHistorial(String nombreAmigo){
		BDAcceso BD = new BDAcceso(contextChat);
		BD.BDopen();
		mensajesAMostrar = BD.getMensajesUsuario(nombreAmigo);
		BD.BDclose();
	}
	
	//Actualiza los mensajes con los nuevos enviados y/o recibidos
	public void actualizarAdapter(String nombreAmigo){
		BDAcceso BD = new BDAcceso(contextChat);
		BD.BDopen();
		mensajesAMostrar = BD.getMensajesUsuarioFechaActual(nombreAmigo);
		BD.BDclose();
	}
}
