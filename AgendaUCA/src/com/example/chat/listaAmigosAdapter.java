package com.example.chat;

import java.util.ArrayList;

import com.example.agendauca.R;
import com.example.persistencia.BDAcceso;

import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class listaAmigosAdapter extends BaseAdapter{
	private Context context;
	private ArrayList<String> amigos;
	private BDAcceso BDAmigos;
    
	public listaAmigosAdapter(Context context){
		super();
		this.context = context;
		BDAmigos = new BDAcceso(context);
		BDAmigos = BDAmigos.BDopen();
		amigos = BDAmigos.getUsuarios();
		BDAmigos.BDclose();
		/*String listarGrupo;
		for(int i = 0; i < amigos.size(); i++){
			if(amigos.get(i).contains("/")){
				listarGrupo = "Grupo: " +amigos.get(i).replace("/", ", ");
				amigos.set(i, listarGrupo);
			}
		}*/
	}
	
	@Override
	public int getCount() {
		return amigos.size();
	}

	@Override
	public Object getItem(int position) {
		return amigos.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewAmigos view; 
		if(convertView == null)
		{
			view = new ViewAmigos();
			convertView = LayoutInflater.from(context).inflate(R.layout.sms_new, parent, false);
			view.nombreAmigo = (TextView) convertView.findViewById(R.id.mensajeNuevo);
			convertView.setTag(view);
		}
		else
			view = (ViewAmigos) convertView.getTag();
		
		
		BDAmigos.BDopen();
		boolean resultado = BDAmigos.isNuevoMensaje(amigos.get(position));
		BDAmigos.BDclose();
		
		if(amigos.get(position).contains("/")){
			String listarGrupo = "Grupo: " +amigos.get(position).replace("/", ", ");
			amigos.set(position, listarGrupo);
		}
		
		view.nombreAmigo.setText(amigos.get(position));
		view.nombreAmigo.setGravity(Gravity.CENTER_VERTICAL);
		
		if(resultado)
			view.nombreAmigo.setTypeface(null, Typeface.BOLD);
		
		return convertView;
	}
	
	private static class ViewAmigos{
		TextView nombreAmigo;
	}

}
