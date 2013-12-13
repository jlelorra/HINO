package com.example.cdtheque;

import java.util.ArrayList;
import java.util.Arrays;


import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class affich_artist extends ListActivity{
	private static final int DELETE_ALBUM = 1;
	private static final int ADD_ALBUM = 0;
	private static final int UPDATE_CD = 2;
	ListView liste = null;
    ArrayList<Object> listActivities;
    ListAdapter adapter;
    CDBDD cdBdd;
    ArrayAdapter<String> arr;
	private Menu Contact = null;
	private Menu ListOfArtist = null;
	private Menu ListOfAlbum = null;
	private Menu Supprimer = null;
	String str[];




	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    liste = (ListView) findViewById(R.id.ListOfArtist);
	    cdBdd = new CDBDD(this);
	    cdBdd.open();
	    Cursor c = cdBdd.getListOfArtist();
		str= new String[c.getCount()];
		if (c.getCount() != 0){
	
			//Sinon on se place sur le premier élément
			c.moveToFirst();
			int i = 0;
			while(!c.isAfterLast()){
		    	Log.d("CONTENT",c.getString(1));
				str[i] = c.getString(1);
				c.moveToNext();
				i++;
			}
			Arrays.sort(str);
		    arr = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,str);
		    setListAdapter(arr);
			ListView list = this.getListView();   
		    registerForContextMenu(list);
			list.setOnItemClickListener(new OnItemClickListener() {
					
		         @Override
		          public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
		        	Log.d("ALBUM NAME",arr.getItem(position));
		     	    cdBdd.open();
		            CD cdFromBdd = cdBdd.getCDWithArtist(arr.getItem(position));
                    Intent i = new Intent(getApplicationContext(),affich_CD.class);
                    i.putExtra("ID",cdFromBdd.getId());
                    i.putExtra("ALBUM",cdFromBdd.getAlbum());
                    i.putExtra("YEAR",cdFromBdd.getYear());
                    i.putExtra("RATE",cdFromBdd.getRate());
                    i.putExtra("ARTIST",cdFromBdd.getArtist());
                    if(cdFromBdd.getContact()==null){
                    	i.putExtra("CONTACT","diponible");
                    }else{
                    	 i.putExtra("CONTACT",cdFromBdd.getContact());
                    }
                    startActivity(i);
		     	 	cdBdd.close();
		     	 	
		          }	
		         
		      });
			}
	
	    } 
	
	@Override
	protected void onResume(){
		super.onResume();
		this.onCreate(null);
	}
	
	  @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu; this adds items to the action bar if it is present.
	        getMenuInflater().inflate(R.menu.main, menu);
	        Contact = menu;
	        ListOfArtist = menu;
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
			  menu.add(Menu.NONE, ADD_ALBUM, Menu.NONE, "Ajouter");
			  menu.add(Menu.NONE, DELETE_ALBUM, Menu.NONE, "Supprimer");
			  menu.add(Menu.NONE, UPDATE_CD, Menu.NONE, "Mettre à jour les infos");
		  }
		  
		
		
		  @Override
		  public boolean onContextItemSelected(MenuItem item) {
		    
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
			int position=item.getItemId();
  	    	Log.d("position",String.valueOf(info.position));
		    switch (item.getItemId()) {
			    case DELETE_ALBUM:
			    	  	cdBdd = new CDBDD(this);
			  	    	cdBdd.open();
			  	    	Log.d("ARTIST NAME",arr.getItem(info.position));
			            CD cdFromBdd = cdBdd.getCDWithArtist(arr.getItem(info.position));
			            cdBdd.removeCDWithID(cdFromBdd.getId());
			            Toast.makeText(this, String.valueOf(info.id), Toast.LENGTH_SHORT).show();
			     	 	cdBdd.close();
			     	 	onResume();
			     	 	return true;
			     	 	
			    case ADD_ALBUM:
				    	Intent intent = new Intent(getApplicationContext(),MainActivity.class);
	  					startActivity(intent);
			    		return true;
			    		
			    case UPDATE_CD:
			    	  	cdBdd = new CDBDD(this);
			  	    	cdBdd.open();
			  	    	Log.d("ALBUM NAME",arr.getItem(position));
			            cdFromBdd = cdBdd.getCDWithAlbum(arr.getItem(info.position));
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

		    }	
		    return super.onContextItemSelected(item);
		  }
}