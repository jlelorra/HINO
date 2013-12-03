package com.example.cdtheque;

import java.util.ArrayList;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class affich_album extends ListActivity{
	ListView liste = null;
    ArrayList<Object> listActivities;
    ListAdapter adapter;
    CDBDD cdBdd;
    ArrayAdapter<String> arr;
	private Menu Contact = null;
	private Menu ListOfAlbum = null;



	@SuppressWarnings("unchecked")
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    liste = (ListView) findViewById(R.id.ListOfAlbum);
	    cdBdd = new CDBDD(this);
	    cdBdd.open();
	    Cursor c = cdBdd.getListOfAlbum();
		String str[]= new String[c.getCount()];
		if (c.getCount() != 0){
	
			//Sinon on se place sur le premier élément
			c.moveToFirst();
			int i = 0;
			while(!c.isAfterLast()){
		    	Log.d("CONTENT",c.getString(2));
				str[i] = c.getString(2);
				c.moveToNext();
				i++;
			}
			 //arr = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,str);
		     arr = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,str);
		     setListAdapter(arr);
			 ListView list = this.getListView();   
			 list.setOnItemClickListener(new OnItemClickListener() {
					
		         @Override
		          public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
		        	Log.d("ALBUM NAME",arr.getItem(position));
		     	    cdBdd.open();
		            CD cdFromBdd = cdBdd.getCDWithAlbum(arr.getItem(position));
		            //arr = new ArrayAdapter<String>(this,android.R.id.listTextView,arr.getItem(position));
		            cdBdd.removeCDWithID(cdFromBdd.getId());
		     	 	cdBdd.close();
		     	 	onResume();
		         	//Toast.makeText(this, cdFromBdd.toString(), Toast.LENGTH_LONG).show();
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
	        }
	        return super.onOptionsItemSelected(item);
	      }

}

