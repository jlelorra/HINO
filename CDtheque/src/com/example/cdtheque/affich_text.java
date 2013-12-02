package com.example.cdtheque;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
	 private Menu Contact = null;
	 private Menu ListOfAlbum = null;

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
        cdBdd.close();
        Log.d("CONTENT2", cdFromBdd.getAlbum());
        //Si un livre est retourné (donc si le livre à bien été ajouté à la BDD)
        if(cdFromBdd != null){
        	StringBuffer contenu = new StringBuffer();
        	//On affiche les infos du livre dans un Toast
        	Toast.makeText(this, cdFromBdd.toString(), Toast.LENGTH_LONG).show();
        	textchamp = (TextView) findViewById(R.id.display);
        	contenu.append("Vous venez de créer l'entrée suivante avec succés !\nAlbum : "+cdFromBdd.getAlbum()+" \nArtist : "+cdFromBdd.getArtist()+" \nYear : "+cdFromBdd.getYear()+" \nContact : "+cdFromBdd.getContact());
        	textchamp.setText(contenu);
        }
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        Contact = menu;
        ListOfAlbum = menu;
        return true;
        }
    
    @Override
    public boolean onOptionsItemSelected (MenuItem item)
    {
        switch(item.getItemId())
        {
          case R.id.ChooseAContact:
  	                        // TODO Auto-generated method stub
  	                        Intent i = new Intent(getApplicationContext(),affich_contact.class);
  	                        startActivity(i);
  	                        return true;
          	case R.id.ListOfAlbum:
          					Intent intent = new Intent(getApplicationContext(),affich_album.class);
          					startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
      }
}