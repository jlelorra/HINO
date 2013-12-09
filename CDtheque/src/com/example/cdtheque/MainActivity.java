package com.example.cdtheque;

import android.os.Build;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Activity;
import android.widget.Toast;
import android.content.Intent;
import android.content.res.Configuration;
import android.text.Layout;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

public class MainActivity extends Activity{
	
	private static final int LISTOFALBUM = 0;
	private static final int LISTOFARTIST = 1;
	 private EditText artist = null;
	 private EditText album = null;
     private EditText year = null;
	 private RatingBar rate = null;
	 private Button OK = null;
	 private TextView borrow;
	 String str_artist;
	 String str_album;
	 String str_year;
	 String str_borrow;
	 float int_rate;
	 CDBDD cdBdd;
	 


	
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
   	 	super.onCreate(savedInstanceState);
   	 	
   	 	Configuration config = new Configuration();
    	if(config.orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_main2); 
    	}else{
            setContentView(R.layout.activity_main);
    	}
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        //Création d'une instance de ma classe CDBDD
        cdBdd = new CDBDD(this);
        Intent intent = getIntent();
        
        artist = (EditText)this.findViewById(R.id.artist);
        if(intent.getStringExtra("ARTIST")!= null){
        	Log.d("ARTIST", "different de null");
        	str_artist = intent.getStringExtra("ARTIST");
        	artist.setText(intent.getStringExtra("ARTIST"));
        }
        
   	 	album = (EditText)this.findViewById(R.id.album);
        if(intent.getStringExtra("ALBUM")!= null){
        	str_album = intent.getStringExtra("ALBUM");
        	album.setText(intent.getStringExtra("ALBUM"));
        }
        
	 	year = (EditText)this.findViewById(R.id.year);
        if(intent.getStringExtra("YEAR")!= null){
        	str_year = intent.getStringExtra("YEAR");
        	year.setText(intent.getStringExtra("YEAR"));
        }
        
	 	rate = (RatingBar)this.findViewById(R.id.ratingBar1);
        if(intent.getFloatExtra("RATE",0)>= 0){
        	int_rate = intent.getFloatExtra("RATE",0);
        	rate.setRating(intent.getFloatExtra("RATE",0));
        }
        OK=(Button)this.findViewById(R.id.OK);
        
    	borrow = (TextView)  findViewById(R.id.borrow);
        if(intent.getStringExtra("CONTACT")!= null){
        	str_borrow = intent.getStringExtra("CONTACT");
        	borrow.setText(intent.getStringExtra("CONTACT"));
        }
	        OK.setOnClickListener(new View.OnClickListener() {

	                    @Override
	                    public void onClick(View v) {
	                    	
	                   	 	str_artist = artist.getEditableText().toString();
	                   	 	str_album = album.getEditableText().toString();
	                   	 	str_year = year.getEditableText().toString();
	                	 	int_rate = (float) rate.getRating();
	                	 	str_borrow= borrow.getText().toString();
	                	 	
	                    	
	                        //Création d'un CD
	                        CD cd = new CD(str_artist, str_album , str_year, str_borrow , int_rate);
	                        
	                        //On ouvre la base de données pour écrire dedans
	                        cdBdd.open();
	                        //On insère le livre que l'on vient de créer
	                        cdBdd.insertCD(cd);
	
	                        // TODO Auto-generated method stub
	                        Intent i = new Intent(getApplicationContext(),affich_text.class);
	                        //i.putExtra("ID",idCd);
	                        i.putExtra("ALBUM",str_album);
	                        i.putExtra("YEAR",str_year);
	                        i.putExtra("RATE",int_rate);
 	                        i.putExtra("ARTIST",str_artist);
	                        i.putExtra("BORROW",str_borrow);
	                        //Log.d("ID",String.valueOf(idCd));
	                        Log.d("artist_name",str_artist);
	                        Log.d("album_name", str_album);
	                        Log.d("year", str_year);
	                        Log.d("borrowMain", str_borrow);
	                        Log.d("rate", String.valueOf(int_rate));
	                        startActivity(i);
	                        cdBdd.close();
	                    }
	                }); 
	        registerForContextMenu(artist);
	        registerForContextMenu(album);
	        registerForContextMenu(year);
	        registerForContextMenu(rate);
	        registerForContextMenu(borrow);
    }
    
       
        
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    	
    	MenuInflater inflater = getMenuInflater();
        // Inflate the menu; this adds items to the action bar if it is present.
    	inflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
        }
            
    @Override
    public boolean onOptionsItemSelected (MenuItem item)
    {
        switch(item.getItemId())
        {
          case R.id.ChooseAContact:
            //Dans le Menu "m", on active tous les items dans le groupe d'identifiant "R.id.group2"
        	//Contact=(Button)this.findViewById(R.id.btn_borrow);
  	        //Contact.setOnClickListener(new View.OnClickListener() {
  	
  	                   // @Override
  	                   // public void onClick(View v) {
				         	str_artist = artist.getEditableText().toString();
				         	str_album = album.getEditableText().toString();
				         	str_year = year.getEditableText().toString();
				      	 	int_rate = (float) rate.getRating();
  	
  	                        // TODO Auto-generated method stub
  	                        Intent i = new Intent(getApplicationContext(),affich_contact.class);
  	                        i.putExtra("ALBUM",str_album);
	                        i.putExtra("YEAR",str_year);
	                        i.putExtra("RATE",int_rate);
	                        i.putExtra("ARTIST",str_artist);
	                        Log.d("artist_name",str_artist);
	                        Log.d("album_name", str_album);
	                        Log.d("year", str_year);
  	                        startActivity(i);
  	                    //}
  	                //});
  	             return true;
          	case R.id.ListOfAlbum:
          					Intent intent = new Intent(getApplicationContext(),affich_album.class);
          					startActivity(intent);
          	          		return true;
          	case R.id.addCD:
			          		Intent intent2 = new Intent(getApplicationContext(),MainActivity.class);
			          		startActivity(intent2);
			          		return true;
          	case R.id.ListOfArtist:
			          		Intent intent3 = new Intent(getApplicationContext(),affich_artist.class);
			          		startActivity(intent3);
			          		return true;
        }
        return super.onOptionsItemSelected(item);
      }
    
    public void onConfigurationChanged(Configuration newConfig){
    	super.onConfigurationChanged(newConfig);
    	if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
       	 	str_artist = artist.getEditableText().toString();
       	 	str_album = album.getEditableText().toString();
       	 	str_year = year.getEditableText().toString();
    	 	int_rate = (float) rate.getRating();
    	 	str_borrow= borrow.getText().toString();
            setContentView(R.layout.activity_main2); 
            populate();
    	}else{
       	 	str_artist = artist.getEditableText().toString();
       	 	str_album = album.getEditableText().toString();
       	 	str_year = year.getEditableText().toString();
    	 	int_rate = (float) rate.getRating();
    	 	str_borrow= borrow.getText().toString();
            setContentView(R.layout.activity_main);
            populate();
    	}
        registerForContextMenu(artist);
        registerForContextMenu(album);
        registerForContextMenu(year);
        registerForContextMenu(rate);
        registerForContextMenu(borrow);
	}
    
    private void populate(){
    	
        artist = (EditText)this.findViewById(R.id.artist);
        artist.setText(str_artist);
        album = (EditText)this.findViewById(R.id.album);
   	 	album.setText(str_album);
   	 	year = (EditText)this.findViewById(R.id.year);
	 	year.setText(str_year);
	 	rate = (RatingBar)this.findViewById(R.id.ratingBar1);
	 	rate.setRating(int_rate);
	 	borrow = (TextView)this.findViewById(R.id.borrow);
    	borrow.setText(str_borrow);
    	OK=(Button)this.findViewById(R.id.OK);
    	OK.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View v) {
             	
            	str_artist = artist.getEditableText().toString();
            	str_album = album.getEditableText().toString();
            	str_year = year.getEditableText().toString();
         	 	int_rate = (float) rate.getRating();
         	 	str_borrow= borrow.getText().toString();
         	 	
             	
                 //Création d'un CD
                 CD cd = new CD(str_artist, str_album , str_year, str_borrow , int_rate);
                 
                 //On ouvre la base de données pour écrire dedans
                 cdBdd.open();
                 //On insère le livre que l'on vient de créer
                 cdBdd.insertCD(cd);

                 // TODO Auto-generated method stub
                 Intent i = new Intent(getApplicationContext(),affich_text.class);
                 //i.putExtra("ID",idCd);
                 i.putExtra("ALBUM",str_album);
                 i.putExtra("YEAR",str_year);
                 i.putExtra("RATE",int_rate);
                  i.putExtra("ARTIST",str_artist);
                 i.putExtra("BORROW",str_borrow);
                 //Log.d("ID",String.valueOf(idCd));
                 Log.d("artist_name",str_artist);
                 Log.d("album_name", str_album);
                 Log.d("year", str_year);
                 Log.d("borrowMain", str_borrow);
                 Log.d("rate", String.valueOf(int_rate));
                 startActivity(i);
                 cdBdd.close();
             }
         }); 
    	
    }
    
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuinfo) {
		
		  super.onCreateContextMenu(menu, v, menuinfo);
		  //getMenuInflater().inflate(R.menu.contect, menu);
		  //Supprimer = menu;
		  //menu.add(Menu.NONE, ADD_ALBUM, Menu.NONE, "Ajouter");
		  //menu.add(Menu.NONE, DELETE_ALBUM, Menu.NONE, "Supprimer");
		  //menu.add(Menu.NONE, UPDATE_CD, Menu.NONE, "Mettre à jour les infos");
		  menu.add(Menu.NONE, LISTOFALBUM, Menu.NONE, "Liste des Albums");
		  menu.add(Menu.NONE, LISTOFARTIST, Menu.NONE, "Liste des Artistes");
	  }
	  
	
	
	  @Override
	  public boolean onContextItemSelected(MenuItem item) {
		  
	    
	    switch (item.getItemId()) {
		     	 	
		    /*case ADD_ALBUM:
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
		     	 	return true;*/
		     	 	
		    case LISTOFALBUM:
		    	  	Intent intent = new Intent(getApplicationContext(),affich_album.class);
					startActivity(intent);
		     	 	return true;
	     	 	
		    case LISTOFARTIST:
		    		Intent i = new Intent(getApplicationContext(),affich_artist.class);
					startActivity(i);
		     	 	return true;

	    }	
	    return super.onContextItemSelected(item);
	  }

}

