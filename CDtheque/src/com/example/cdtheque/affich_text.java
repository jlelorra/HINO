package com.example.cdtheque;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class affich_text extends Activity {
	LinearLayout layout;
	private TextView textchamp;
	String artist;
	String album;
	String year;
	float bar;
	CDBDD cdBdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.affich_text);
        Intent intent = getIntent();
        Log.d("CONTENT", intent.getStringExtra("ALBUM"));
        //Pour vérifier que l'on a bien créé notre livre dans la BDD
        //on extrait le livre de la BDD grâce au titre du livre que l'on a créé précédemment
        cdBdd = new CDBDD(this);
        cdBdd.open();
        CD cdFromBdd = cdBdd.getCDWithAlbum(intent.getStringExtra("ALBUM"));
        //Si un livre est retourné (donc si le livre à bien été ajouté à la BDD)
        if(cdFromBdd != null){
        	//On affiche les infos du livre dans un Toast
        	Toast.makeText(this, cdFromBdd.toString(), Toast.LENGTH_LONG).show();
        	Log.d("CONTENT2", intent.getStringExtra("ALBUM"));
        	textchamp = (TextView) findViewById(R.id.display);
        	textchamp.setText(cdFromBdd.getId());
        	textchamp.setText(cdFromBdd.getAlbum());
        	textchamp.setText(cdFromBdd.getArtist());
        	textchamp.setText(cdFromBdd.getYear());
        	textchamp.setText(cdFromBdd.getYear());
        	textchamp.setText(cdFromBdd.getContact());
        	setContentView(textchamp);
        }
        cdBdd.close();
        // récupération de la valeur
        //StringBuffer contenu = new StringBuffer();;
        //artist = intent.getStringExtra("ARTIST");
       // album = intent.getStringExtra("ALBUM");
       // year = intent.getStringExtra("YEAR");
    	//bar = intent.getFloatExtra("RATE", (float) 2.5);
    	//TextView text = new TextView(this);
    	//textchamp = (TextView) findViewById(R.id.display);
    	//textchamp.setText("artist");
        //Log.d("CONTENT", artist);
       // Log.d("CONTENT", album);
        //Log.d("CONTENT", year);
    	//contenu.append(String.valueOf(artist) + album + year + bar);
        //textchamp.setText(artist);
    	//setContentView(textchamp);
    }
}