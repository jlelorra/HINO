package com.example.cdtheque;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateCD extends Activity{
	 private EditText artist = null;
	 private EditText album = null;
    private EditText year = null;
	 private RatingBar rate = null;
	 private Button OK = null;
	 private Menu Contact = null;
	 private Menu ListOfAlbum = null;
	 private Toast Toast =null;
	 private TextView borrow;
	 String str_artist;
	 String str_album;
	 String str_year;
	 String str_borrow;
	 float int_rate;
	 CDBDD cdBdd;
	 int idCd;


	
   @Override
   protected void onCreate(Bundle savedInstanceState) {
  	 	super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main); 
       
       //Création d'une instance de ma classe CDBDD
       cdBdd = new CDBDD(this);
       
       Intent intent = getIntent();
       
       idCd= intent.getIntExtra("ID", 0);
       Log.d("ID UPDATE",String.valueOf(idCd));
       artist = (EditText)this.findViewById(R.id.artist);
       if(intent.getStringExtra("ARTIST")!= null){
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
	                    	
	                   	 	str_artist = artist.getText().toString();
	                   	 	str_album = album.getText().toString();
	                   	 	str_year = year.getText().toString();
	                	 	int_rate = (float) rate.getRating();
	                	 	str_borrow= borrow.getText().toString();
	                	 	

	                        //Création d'un CD
	                        CD cd = new CD(str_artist, str_album , str_year, str_borrow , int_rate);
	                        
	                        //On ouvre la base de données pour écrire dedans
	                        cdBdd.open();
	                        //On insère le livre que l'on vient de créer
	                        //cdBdd.insertCD(cd);
	                        cdBdd.updateCD(idCd, cd);
	                        //cdBdd.removeCDWithID(idCd);
	                        cdBdd.close();
	                        
	                        // TODO Auto-generated method stub
	                        Intent i = new Intent(getApplicationContext(),affich_text.class);
	                        i.putExtra("ID",idCd);
	                        i.putExtra("ALBUM",str_album);
	                        i.putExtra("YEAR",str_year);
	                        i.putExtra("RATE",int_rate);
 	                        i.putExtra("ARTIST",str_artist);
	                        i.putExtra("BORROW",str_borrow);
	                        Log.d("ID",String.valueOf(idCd));
	                        Log.d("artist_name",str_artist);
	                        Log.d("album_name", str_album);
	                        Log.d("year", str_year);
	                        Log.d("borrow", str_borrow);
	                        Log.d("rate", String.valueOf(int_rate));
	                        startActivity(i);
	                    }
	                }); 
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
        	 				Intent intent3 = getIntent();
        	 				idCd= intent3.getIntExtra("ID", 0);
				         	str_artist = artist.getEditableText().toString();
				         	str_album = album.getEditableText().toString();
				         	str_year = year.getEditableText().toString();
				      	 	int_rate = (float) rate.getRating();
 	
 	                        // TODO Auto-generated method stub
 	                        Intent i = new Intent(getApplicationContext(),affich_contact2.class);
 	                        i.putExtra("ID",idCd);
 	                        i.putExtra("ALBUM",str_album);
	                        i.putExtra("YEAR",str_year);
	                        i.putExtra("RATE",int_rate);
	                        i.putExtra("ARTIST",str_artist);
	                        Log.d("artist_name",str_artist);
	                        Log.d("album_name", str_album);
	                        Log.d("year", str_year);
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
          	case R.id.ListOfArtist:
			          		Intent intent4 = new Intent(getApplicationContext(),affich_artist.class);
			          		startActivity(intent4);
			          		return true;
       }
       return super.onOptionsItemSelected(item);
     }
}
