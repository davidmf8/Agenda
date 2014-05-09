package com.example.agendauca;

import java.util.ArrayList;

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
		
		String[] hora = mensaje.getFecha().split(" ");
		holder.mensaje.setText(mensaje.getMensaje());
		holder.hora.setText(hora[1]);
		
		LayoutParams lp = (LayoutParams) holder.mensaje.getLayoutParams();
		LayoutParams lh = (LayoutParams) holder.hora.getLayoutParams();
		//check if it is a status message then remove background, and change text color.
		/*if(mensaje.isStatusMessage())
		{
			holder.message.setBackgroundDrawable(null);
			lp.gravity = Gravity.LEFT;
			holder.message.setTextColor(R.color.textFieldColor);
		}
		else
		{*/		
			//Check whether message is mine to show green background and align to right
			if(mensaje.getTipo() == 1)
			{
				holder.mensaje.setBackgroundResource(R.drawable.speech_bubble_green);
				lp.gravity = Gravity.RIGHT;
				lh.gravity = Gravity.RIGHT;
			}
			//If not mine then it is from sender to show orange background and align to left
			else
			{
				holder.mensaje.setBackgroundResource(R.drawable.speech_bubble_orange);
				lp.gravity = Gravity.LEFT;
				lh.gravity = Gravity.LEFT;
			}
			holder.mensaje.setLayoutParams(lp);
			holder.hora.setLayoutParams(lh);
			holder.mensaje.setTextColor(R.color.textColor);	
		//}
		return convertView;
	}
	private static class ViewHolder
	{
		TextView mensaje;
		TextView hora;
	}
	
	public void actualizarAdapter(String nombreAmigo){
		BDAcceso BD = new BDAcceso(contextChat);
		BD.BDopen();
		mensajesAMostrar = BD.getMensajesUsuarioFechaActual(nombreAmigo);
		BD.BDclose();
	}
}
