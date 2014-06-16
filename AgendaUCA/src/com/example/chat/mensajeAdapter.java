package com.example.chat;

import java.util.ArrayList;
import java.util.Date;

import com.example.agendauca.R;
import com.example.agendauca.R.color;
import com.example.agendauca.R.drawable;
import com.example.agendauca.R.id;
import com.example.agendauca.R.layout;
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

public class mensajeAdapter extends BaseAdapter{
	private Context contextChat;
	private ArrayList<Mensaje> mensajesAMostrar;
	private Date fechaActual;
	private String compararFecha;
	
	public mensajeAdapter(Context context, ArrayList<Mensaje> mensajes){
		super();
		contextChat = context;
		mensajesAMostrar = mensajes;
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
		ViewHolder holder; 
		if(convertView == null)
		{
			holder = new ViewHolder();
			convertView = LayoutInflater.from(contextChat).inflate(R.layout.sms_row, parent, false);
			holder.mensaje = (TextView) convertView.findViewById(R.id.texto_mensaje);
			holder.hora = (TextView) convertView.findViewById(R.id.text2);
			convertView.setTag(holder);
		}
		else
			holder = (ViewHolder) convertView.getTag();
		
		String fechaMensaje = esFechaActual(mensaje.getFecha());
		
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
	
	private static class ViewHolder{
		TextView mensaje;
		TextView hora;
	}
	
	public void mostrarHistorial(String nombreAmigo){
		BDAcceso BD = new BDAcceso(contextChat);
		BD.BDopen();
		mensajesAMostrar = BD.getMensajesUsuario(nombreAmigo);
		BD.BDclose();
	}
	
	public void actualizarAdapter(String nombreAmigo){
		BDAcceso BD = new BDAcceso(contextChat);
		BD.BDopen();
		mensajesAMostrar = BD.getMensajesUsuarioFechaActual(nombreAmigo);
		BD.BDclose();
	}
}
