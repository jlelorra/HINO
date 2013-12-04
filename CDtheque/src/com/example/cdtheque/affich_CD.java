package com.example.cdtheque;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnDragListener;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class affich_CD extends FragmentActivity {
	private static final int UPDATE_CD = 0;
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
        Log.d("CONTENT", intent.getStringExtra("ALBUM"));
        //idCD=intent.getIntExtra("ID", 0);
        StringBuffer contenu = new StringBuffer();
        album = intent.getStringExtra("ALBUM");
       	textchamp = (TextView) findViewById(R.id.display);
       	contenu.append("Album : "+intent.getStringExtra("ALBUM")+" \nArtist : "+intent.getStringExtra("ARTIST")+" \nYear : "+intent.getStringExtra("YEAR")+" \nRate : "+intent.getStringExtra("RATE")+" \nContact : "+intent.getStringExtra("CONTACT"));
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
  	                        return true;
          	case R.id.addCD:
          		Intent intent2 = new Intent(getApplicationContext(),MainActivity.class);
          		startActivity(intent2);
          		return true;
        }
        return super.onOptionsItemSelected(item);
      }
    
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuinfo) {
		
		  super.onCreateContextMenu(menu, v, menuinfo);
		  //getMenuInflater().inflate(R.menu.contect, menu);
		  //Supprimer = menu;
		  menu.add(Menu.NONE, UPDATE_CD, Menu.NONE, "Mettre à jour les infos");
		  //menu.add(Menu.NONE, DELETE_ALBUM, Menu.NONE, "Supprimer");
	  }
	  
	
	
	  @Override
	  public boolean onContextItemSelected(MenuItem item) {
		  
	    
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
		//int position=item.getItemId();
    	//Log.d("position",String.valueOf(info.position));
	    switch (item.getItemId()) {
		    case UPDATE_CD:

		    	  	cdBdd = new CDBDD(this);
		  	    	cdBdd.open();
		  	    	Log.d("ALBUM NAME",album);
		            CD cdFromBdd = cdBdd.getCDWithAlbum(album);
		            //Toast.makeText(this, String.valueOf(info.id), Toast.LENGTH_SHORT).show();
			    	//Intent intent = new Intent(getApplicationContext(),UpdateCD.class);
		     	 	//onResume();
					//startActivity(intent);
		     	 	cdBdd.close();
		     	 	return true;
		     	 	
		    /*case ADD_ALBUM:
			    	Intent intent = new Intent(getApplicationContext(),MainActivity.class);
					startActivity(intent);
		    		return true;*/

	    }	
	    return super.onContextItemSelected(item);
	  }
}
