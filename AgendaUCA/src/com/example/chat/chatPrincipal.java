package com.example.chat;

import com.example.agendauca.R;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TextView;
import com.example.persistencia.*;

public class chatPrincipal extends Activity{
	private TabHost  tabHostChat;
	private String prueba;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		
		tabHostChat = (TabHost) this.findViewById(android.R.id.tabhost);
		
		Resources res = getResources();
		 
		tabHostChat.setup();
		 
		TabHost.TabSpec spec=tabHostChat.newTabSpec("mitab1");
		spec.setContent(R.id.ListaAmigos);
		spec.setIndicator("",
		    res.getDrawable(android.R.drawable.ic_menu_agenda));
		tabHostChat.addTab(spec);

		spec=tabHostChat.newTabSpec("mitab2");
		spec.setContent(R.id.ListaChats);
		spec.setIndicator("TAB2",
		    res.getDrawable(android.R.drawable.ic_menu_send));
		tabHostChat.addTab(spec);
		 
		tabHostChat.setCurrentTab(0);
		prueba();
		TextView tab1 = (TextView)this.findViewById(R.id.textView1);
		tab1.setText(prueba);
	}

	private void prueba() {
		// TODO Auto-generated method stub
		BDAcceso pr = new BDAcceso(getApplicationContext());
		pr = pr.BDopen();
		pr.insertarUsuario("prueba1","OK");
		prueba = pr.getGCM("prueba1");
		pr.BDclose();
	}

}
