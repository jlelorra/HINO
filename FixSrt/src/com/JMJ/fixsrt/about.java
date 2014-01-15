package com.JMJ.fixsrt;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class about extends Activity {
	
	private TextView about = null;

	protected void onCreate(Bundle savedInstanceState) {
		
		//setContentView(R.layout.affich_srt_text);
		   super.onCreate(savedInstanceState);     
		   about = new TextView(this);
		   about.setText("\n\n\n\nCette application a été développé par Jason LELORRAIN.\n\n Elle a pour but d'adapter les sous titres sélectionnés.\n\n Elle liste l'ensemble des sous titres (.srt et .ass) contenu dans la carte Sd externe, le dossier de téléchargement ainsi que le dossier bluetooth.");
		   setContentView(about);
		
	}
}
