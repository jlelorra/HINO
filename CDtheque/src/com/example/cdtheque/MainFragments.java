package com.example.cdtheque;

import java.util.ArrayList;
import java.util.Arrays;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ListFragment;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainFragments extends Activity {

    private static final int ADD_ALBUM = 0;
	private static final int DELETE_ALBUM = 1;
	private static final int UPDATE_CD = 2;
	private ArrayList<CD> cd;
    private ListView liste;
    CDBDD cdBdd;
    private static String str[]=null;
    private static ArrayAdapter<String> arr=null;
	
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        ActionBar.Tab tabOne = actionBar.newTab().setText("Album");
        ActionBar.Tab tabTwo = actionBar.newTab().setText("Artist");
          
        tabOne.setTabListener(new tabListener());
        tabTwo.setTabListener(new tabListener());

        actionBar.addTab(tabOne);
        actionBar.addTab(tabTwo);  
 
    } 


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	protected class tabListener implements ActionBar.TabListener {

        @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
		@SuppressLint("NewApi")
		@Override
        public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        }

        @SuppressLint("NewApi")
		@Override
        public void onTabSelected(Tab tab, FragmentTransaction ft) {
        	
        	if(tab.getText()=="Album"){
        		FirstFragment f = new FirstFragment();	
	            setContentView(R.layout.affich_album);
	            Log.d("LAYOUT","ALBUM");
	            ft.add(f,"ALBUM");
                
        	}else{
        		SecFragment	 f = new SecFragment();
        		setContentView(R.layout.affich_album); 
        		Log.d("LAYOUT","ARTIST");
                ft.add(f,"ALBUM");
        	}
        }

        @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
		@SuppressLint("NewApi")
		@Override
        public void onTabReselected(Tab tab, FragmentTransaction ft) {

        }
    };

    @SuppressLint("ValidFragment")
	public class FirstFragment extends ListFragment {
    	    CDBDD cdBdd;


          @Override
          public void onActivityCreated(Bundle savedInstanceState) {
            super.onActivityCreated(savedInstanceState);
            liste = (ListView) findViewById(R.id.ListOfAlbum);
    	    cdBdd = new CDBDD(this.getActivity());
    	    cdBdd.open();
    	    Cursor c = cdBdd.getListOfAlbum();
    		str= new String[c.getCount()];
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
    			Arrays.sort(str);	
    		    arr = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_list_item_1,str);
    		    liste.setAdapter(arr);
    		    registerForContextMenu(liste);
    		    liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    					
    		         @Override
    		          public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
    		        	Log.d("ALBUM NAME",arr.getItem(position));
    		     	    cdBdd.open();
    		            CD cdFromBdd = cdBdd.getCDWithAlbum(arr.getItem(position));
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
    			/*SwipeListViewTouchListener touchListener =
		    		    new SwipeListViewTouchListener(
		    		            liste,
		    		            new SwipeListViewTouchListener.OnSwipeCallback() {
		    		                @Override
		    		                public void onSwipeLeft(ListView listView, int [] reverseSortedPositions) {;
		    		                    //Log.i(this.getClass().getName(), "swipe left : pos="+reverseSortedPositions[0]);
		    		                	ActionBar action = getActionBar();
		    		                	//FragmentTransaction ft = this.getSupportFragmentManager().beginTransaction();
		    		                	if(action.getTitle()=="ARTIST"){
		    		                		FirstFragment f = new FirstFragment();	
		    		                        setContentView(R.layout.affich_album);
		    		                        Log.d("LAYOUT","ALBUM");
		    		                        //ft.add(f,"ALBUM");
		    		                	}
		    		                }

		    		                @Override
		    		                public void onSwipeRight(ListView listView, int [] reverseSortedPositions) {
		    		                    //  Log.i(ProfileMenuActivity.class.getClass().getName(), "swipe right : pos="+reverseSortedPositions[0]);
		    		                    // TODO : YOUR CODE HERE FOR RIGHT ACTION
		    		                }
		    		            },
		    		            true, // example : left action = dismiss
		    		            false); // example : right action without dismiss animation
						liste.setOnTouchListener(touchListener);
		    		    // Setting this scroll listener is required to ensure that during ListView scrolling,
		    		    // we don't look for swipes.
						liste.setOnScrollListener(touchListener.makeScrollListener());*/
    			}
          }

          @Override
          public void onListItemClick(ListView l, View v, int position, long id) {
              
          }
        } 
    
    @SuppressLint("ValidFragment")
	public class SecFragment extends ListFragment {
    	    CDBDD cdBdd;

          @Override
          public void onActivityCreated(Bundle savedInstanceState) {
      	    super.onCreate(savedInstanceState);
    	    liste = (ListView) findViewById(R.id.ListOfAlbum);
    	    cdBdd = new CDBDD(this.getActivity());
    	    cdBdd.open();
    	    Cursor c = cdBdd.getListOfArtist();
    		str= new String[c.getCount()];
    		if (c.getCount() != 0){
    	
    			//Sinon on se place sur le premier élément
    			c.moveToFirst();
    			int i = 0;
    			while(!c.isAfterLast()){
    		    	Log.d("CONTENT2",c.getString(1));
    				str[i] = c.getString(1);
    				c.moveToNext();
    				i++;
    			}
    			Arrays.sort(str);
    		    arr = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_list_item_1,str);
    		    liste.setAdapter(arr);
    		    registerForContextMenu(liste);
    			liste.setOnItemClickListener(new AdapterView.OnItemClickListener() {
    					
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
    			SwipeListViewTouchListener touchListener =
    		    		    new SwipeListViewTouchListener(
    		    		            liste,
    		    		            new SwipeListViewTouchListener.OnSwipeCallback() {
    		    		                @Override
    		    		                public void onSwipeLeft(ListView listView, int [] reverseSortedPositions) {
 
    		    		                }

    		    		                @Override
    		    		                public void onSwipeRight(ListView listView, int [] reverseSortedPositions) {
    		    		                    //  Log.i(ProfileMenuActivity.class.getClass().getName(), "swipe right : pos="+reverseSortedPositions[0]);
    		    		              		Intent intent = new Intent(getApplicationContext(),MainFragments.class);
    		    		              		startActivity(intent);
    		    		                }
    		    		            },
    		    		            true, // example : left action = dismiss
    		    		            false); // example : right action without dismiss animation
    						liste.setOnTouchListener(touchListener);
    		    		    // Setting this scroll listener is required to ensure that during ListView scrolling,
    		    		    // we don't look for swipes.
    						liste.setOnScrollListener(touchListener.makeScrollListener());
    			}
          }

          @Override
          public void onListItemClick(ListView l, View v, int position, long id) {

          }
        } 
    
	  @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	        // Inflate the menu; this adds items to the action bar if it is present.
	        getMenuInflater().inflate(R.menu.main, menu);
	        return true;
	    }
    
    @Override
    public boolean onOptionsItemSelected (MenuItem item) {
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
    
    @SuppressLint("NewApi")
	@Override
	protected void onResume(){
		super.onResume();
        ActionBar actionBar = getActionBar();
        actionBar.removeAllTabs();
		this.onCreate(null);
	}

    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuinfo) {
		
		  super.onCreateContextMenu(menu, v, menuinfo);
		  //getMenuInflater().inflate(R.menu.contect, menu);
		  //Supprimer = menu;
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
		            CD cdFromBdd = cdBdd.getCDWithAlbum(arr.getItem(info.position));
		            if(cdFromBdd == null){
		            	cdFromBdd= cdBdd.getCDWithArtist(arr.getItem(info.position));
		            }
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
		  	    	Log.d("ALBUM NAME",arr.getItem(info.position));
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
