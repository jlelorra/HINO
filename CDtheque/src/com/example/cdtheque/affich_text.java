package com.example.cdtheque;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class affich_text extends Activity {
	private static final int ADD_ALBUM = 0;
	private static final int UPDATE_CD = 1;
	private static final int LISTOFALBUM = 2;
	private static final int LISTOFARTIST = 3;
	LinearLayout layout;
	private TextView textchamp;
	String artist;
	String album;
	String year;
	float bar;
	CDBDD cdBdd;
	 private Menu Contact = null;
	 private Menu ListOfAlbum = null;

	 
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.affich_text);
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
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
        	contenu.append("Vous venez de créer l'entrée suivante avec succés !\nAlbum : "+cdFromBdd.getAlbum()+" \nArtist : "+cdFromBdd.getArtist()+" \nYear : "+cdFromBdd.getYear()+" \nRate : "+String.valueOf(cdFromBdd.getRate())+" \nContact : "+cdFromBdd.getContact());
        	textchamp.setText(contenu);
        }
        registerForContextMenu(textchamp);
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
          					Intent intent = new Intent(getApplicationContext(),MainFragments.class);
          					startActivity(intent);
  	                        return true;
          	case R.id.addCD:
			          		Intent intent2 = new Intent(getApplicationContext(),MainActivity.class);
			          		startActivity(intent2);
			          		return true;
          	case R.id.ListOfArtist:
			          		Intent intent3 = new Intent(getApplicationContext(),MainFragments.class);
			          		startActivity(intent3);
			          		return true;
        }
        return super.onOptionsItemSelected(item);
     }
    
    
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuinfo) {
		
		  super.onCreateContextMenu(menu, v, menuinfo);
		  //getMenuInflater().inflate(R.menu.contect, menu);
		  //Supprimer = menu;
		  menu.add(Menu.NONE, ADD_ALBUM, Menu.NONE, "Ajouter");
		  //menu.add(Menu.NONE, DELETE_ALBUM, Menu.NONE, "Supprimer");
		  menu.add(Menu.NONE, UPDATE_CD, Menu.NONE, "Mettre à jour les infos");
		  menu.add(Menu.NONE, LISTOFALBUM, Menu.NONE, "Liste des Albums");
		  menu.add(Menu.NONE, LISTOFARTIST, Menu.NONE, "Liste des Artistes");
	  }
	  
	
	
	  @Override
	  public boolean onContextItemSelected(MenuItem item) {
		  
		Intent intent;
 	    switch (item.getItemId()) {
		     	 	
		    case ADD_ALBUM:
			    	intent = new Intent(getApplicationContext(),MainActivity.class);
					startActivity(intent);
		    		return true;
		    		
		    case UPDATE_CD:
		    		cdBdd = new CDBDD(this);
		    	  	intent = new Intent(getApplicationContext(),UpdateCD.class);
		  	    	cdBdd.open();
		  	    	Log.d("ALBUM NAME",intent.getStringExtra("ALBUM"));
		  	    	CD cdFromBdd = cdBdd.getCDWithAlbum(intent.getStringExtra("ALBUM"));
			    	intent = new Intent(getApplicationContext(),UpdateCD.class);
			    	intent.putExtra("ID", cdFromBdd.getId());
			    	intent.putExtra("ALBUM", cdFromBdd.getAlbum());
			    	intent.putExtra("ARTIST", cdFromBdd.getArtist());
			    	intent.putExtra("YEAR", cdFromBdd.getYear());
			    	intent.putExtra("RATE", cdFromBdd.getRate());
			    	intent.putExtra("CONTACT", cdFromBdd.getContact());
					startActivity(intent);
		     	 	cdBdd.close();
		     	 	return true;
		     	 	
		    case LISTOFALBUM:
		    	  	intent = new Intent(getApplicationContext(),MainFragments.class);
					startActivity(intent);
		     	 	return true;
	     	 	
		    case LISTOFARTIST:
		    	  	intent = new Intent(getApplicationContext(),MainFragments.class);
					startActivity(intent);
		     	 	return true;

	    }	
	    return super.onContextItemSelected(item);
	  }
}