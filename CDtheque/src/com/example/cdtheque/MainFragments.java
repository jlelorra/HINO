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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class MainFragments extends Activity {

    private ArrayList<CD> cd;
    private ListView liste;
    CDBDD cdBdd;
	
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActionBar();
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
        	}else{
        		setContentView(R.layout.affich_artist); 
        		Log.d("LAYOUT","ARTIST");
        	}
        }

        @Override
        public void onTabReselected(Tab tab, FragmentTransaction ft) {

        }
    };

    @SuppressLint("ValidFragment")
	public class FirstFragment extends ListFragment {
    		String str[];
    	    CDBDD cdBdd;
    	    ArrayAdapter<String> arr;

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
    		    setListAdapter(arr);
    			ListView list = this.getListView();   
    		    registerForContextMenu(list);
    			list.setOnItemClickListener(new OnItemClickListener() {
    					
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
    			}
          }

          @Override
          public void onListItemClick(ListView l, View v, int position, long id) {
              
          }
        } 

}
