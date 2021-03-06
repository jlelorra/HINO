package com.example.cdtheque;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class affich_CD extends Activity {
	private static final int UPDATE_CD = 2;
	private static final int ADD_ALBUM = 0;
	private static final int DELETE_ALBUM = 1;
	LinearLayout layout;
	private TextView textchamp;
	String artist;
	String album;
	String year;
	String contact;
	float bar;
	CDBDD cdBdd;
	 private Menu Contact = null;
	 private Menu ListOfAlbum = null;
	 int idCD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.affich_text);
        Intent intent = getIntent();
        cdBdd = new CDBDD(this);
        cdBdd.open();
        CD cdFromBdd = cdBdd.getCDWithAlbum(intent.getStringExtra("ALBUM"));
        cdBdd.close();
        Log.d("CONTENT", intent.getStringExtra("ALBUM"));
        Log.d("ID CD",String.valueOf(cdFromBdd.getId()));
        StringBuffer contenu = new StringBuffer();
        album = intent.getStringExtra("ALBUM");
       	textchamp = (TextView) findViewById(R.id.display);
       	contenu.append("Album : "+cdFromBdd.getAlbum()+" \nArtist : "+cdFromBdd.getArtist()+" \nYear : "+cdFromBdd.getYear()+" \nRate : "+String.valueOf(cdFromBdd.getRate())+" \nContact : "+cdFromBdd.getContact());
       	textchamp.setText(contenu);
       	textchamp.setTextColor(Color.DKGRAY);
       	textchamp.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
       	textchamp.setBackgroundColor(Color.WHITE);
       	DisplayMetrics metrics;
       	metrics = getApplicationContext().getResources().getDisplayMetrics();
       	float Textsize =textchamp.getTextSize()/metrics.density;
       	textchamp.setTextSize(Textsize+25);
       	registerForContextMenu(textchamp);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        Contact = menu;
        ListOfAlbum = menu;
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
		  menu.add(Menu.NONE, UPDATE_CD, Menu.NONE, "Mettre à jour les infos");
		  menu.add(Menu.NONE, ADD_ALBUM, Menu.NONE, "Ajouter");
		  menu.add(Menu.NONE, DELETE_ALBUM, Menu.NONE, "Supprimer");
	  }
	  
	
	
	  @Override
	  public boolean onContextItemSelected(MenuItem item) {
		  
   	    switch (item.getItemId()) {
		    case UPDATE_CD:
		    	  	cdBdd = new CDBDD(this);
		  	    	cdBdd.open();
		            CD cdFromBdd = cdBdd.getCDWithAlbum(album);
		  	    	Log.d("ALBUM NAME",cdFromBdd.getAlbum());
			    	Intent intent = new Intent(getApplicationContext(),UpdateCD.class);
			    	intent.putExtra("ID", cdFromBdd.getId());
			    	intent.putExtra("ALBUM", cdFromBdd.getAlbum());
			    	intent.putExtra("ARTIST", cdFromBdd.getArtist());
			    	intent.putExtra("YEAR", cdFromBdd.getYear());
			    	intent.putExtra("RATE", cdFromBdd.getRate());
			    	intent.putExtra("CONTACT", cdFromBdd.getContact());
					startActivity(intent);
		     	 	cdBdd.close();
		     	 	return true;
		     	 	
		    case ADD_ALBUM:
			    	Intent intent2 = new Intent(getApplicationContext(),MainActivity.class);
					startActivity(intent2);
		    		return true;
		    		
		    case DELETE_ALBUM:
	    	  	cdBdd = new CDBDD(this);
	  	    	cdBdd.open();
	  	    	CD cdFBdd = cdBdd.getCDWithAlbum(album);
	            cdBdd.removeCDWithID(cdFBdd.getId());
	     	 	cdBdd.close();
	     	 	onResume();
	     	 	return true;
	     	 	

	    }	
	    return super.onContextItemSelected(item);
	  }
}
