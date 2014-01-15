package com.JMJ.fixsrt;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class about extends Activity {
	
	private TextView about = null;
	Intent MainIntent;

	@SuppressLint("DefaultLocale")
	protected void onCreate(Bundle savedInstanceState) {
		
		//setContentView(R.layout.affich_srt_text);
		   super.onCreate(savedInstanceState);     
		   about = new TextView(this);
		   MainIntent= getIntent();
		   if(MainIntent.getStringExtra("VIEW").toLowerCase().contains("_fr"))about.setText("\n\n\n\nCette application a ete developpe par M. Jason LELORRAIN.\n\n Elle a pour but d'adapter les sous titres selectionnes par rapport à la video.\n\n Elle liste l'ensemble des sous titres (.srt et .ass) contenu dans la carte Sd externe, le dossier de telechargement ainsi que le dossier bluetooth.");
		   else about.setText("\n\n\n\nThis application was developped By Mr. Jason LELORRAIN.\n\n It adapt selected subtitles with the delay provided.\n\n It list all subtitles (.srt et .ass) contained into External SdCard, Download directory and BlueTooth directory.");		   
		   setContentView(about);
		
	}
}
