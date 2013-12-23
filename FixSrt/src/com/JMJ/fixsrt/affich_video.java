package com.JMJ.fixsrt;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.net.Uri;

public class affich_video extends ListActivity{
	
	private static final int PATH = 0;
	private static final int VIDEO = 1;
	private static final int SRT = 2;
	ListView liste = null;
	Cursor cursor;
	int nameIdx;
	String name; 
    ArrayAdapter<String> arr;
    Uri Uri;
    Intent MainIntent;
	
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState) {
    	MainIntent= getIntent();
	    super.onCreate(savedInstanceState);
	    ContentResolver cr = getContentResolver();
	    cursor = cr.query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
	    nameIdx = cursor.getColumnIndexOrThrow(MediaStore.Video.Media.DISPLAY_NAME);
	    Integer idx = cursor.getCount();
	    String str[]= new String[idx];
	    if (cursor.moveToFirst()){
	        int x = 0;
		    do{
			   	 name = cursor.getString(nameIdx);
			     str[x]= name;
			     x++;
			 } while(cursor.moveToNext());
	    }
		arr = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,str);
		setListAdapter(arr);
	    ActionBar actionBar = getActionBar();
	    actionBar.setDisplayHomeAsUpEnabled(true);
		ListView list = this.getListView();   
	    registerForContextMenu(list);
		list.setOnItemClickListener(new OnItemClickListener() {
				
	           @Override
	            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

	                Intent i = new Intent(getApplicationContext(),selectPath.class);
	                name =  arr.getItem(position);
	                //ContentResolver cr = getContentResolver();
	                Uri = MediaStore.Video.Media.getContentUri(arr.getItem(position));
	                Log.d("NAME",name);
	                i.putExtra("NAME", name);
	                Log.d("URI",String.valueOf(Uri));
	                i.putExtra("URI", String.valueOf(Uri));
	                i.putExtra("URISRT", MainIntent.getStringExtra("URISRT"));
	                i.putExtra("DELAY",MainIntent.getIntExtra("DELAY",0));
	                i.putExtra("SWITCH",MainIntent.getBooleanExtra("SWITCH",false));
		        	i.putExtra("VIEW",MainIntent.getStringExtra("VIEW"));
	                startActivity(i);
	            }
	        });
	}
    
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	    	
	    	MenuInflater inflater = getMenuInflater();
	        // Inflate the menu; this adds items to the action bar if it is present.
	    	inflater.inflate(R.menu.main2, menu);
	        return super.onCreateOptionsMenu(menu);
	        }
	            
	    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
		@SuppressLint("NewApi")
		@Override
	    public boolean onOptionsItemSelected (MenuItem item)
	    {
	        switch(item.getItemId())
	        {
       	
	         case R.id.video:
			            	Intent intent = new Intent(getApplicationContext(),affich_video.class);
			            	intent.putExtra("URI", String.valueOf(Uri));
			            	intent.putExtra("URISRT", MainIntent.getStringExtra("URISRT"));
			            	intent.putExtra("DELAY",MainIntent.getIntExtra("DELAY",0));
			            	intent.putExtra("SWITCH",MainIntent.getBooleanExtra("SWITCH",false));
			            	intent.putExtra("VIEW",MainIntent.getStringExtra("VIEW"));
			            	startActivity(intent);
	          	          	return true;
	          	          	
	        case R.id.srt:
			            	Intent intent2 = new Intent(getApplicationContext(),affich_srt.class);
			            	intent2.putExtra("URI", String.valueOf(Uri));
			            	intent2.putExtra("URISRT", MainIntent.getStringExtra("URISRT"));
			            	intent2.putExtra("DELAY",MainIntent.getIntExtra("DELAY",0));
			            	intent2.putExtra("SWITCH",MainIntent.getBooleanExtra("SWITCH",false));
			            	intent2.putExtra("VIEW",MainIntent.getStringExtra("VIEW"));
							startActivity(intent2);
					        return true;
	
	        case R.id.path:
				        	Intent intent3 = new Intent(getApplicationContext(),selectPath.class);
				        	intent3.putExtra("URI", String.valueOf(Uri));
				        	intent3.putExtra("URISRT", MainIntent.getStringExtra("URISRT"));
				        	intent3.putExtra("DELAY",MainIntent.getIntExtra("DELAY",0));
				        	intent3.putExtra("SWITCH",MainIntent.getBooleanExtra("SWITCH",false));
			            	intent3.putExtra("VIEW",MainIntent.getStringExtra("VIEW"));
							startActivity(intent3);
					        return true;

	        }
	        return super.onOptionsItemSelected(item);
	      }
	    
		public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuinfo) {
			
			  super.onCreateContextMenu(menu, v, menuinfo);
			  menu.add(Menu.NONE, PATH, Menu.NONE, "Language");
			  menu.add(Menu.NONE, VIDEO, Menu.NONE, "Video");
			  menu.add(Menu.NONE, SRT, Menu.NONE, "Srt");

		  }
		  
		
		
		  @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
		@SuppressLint("NewApi")
		@Override
		  public boolean onContextItemSelected(MenuItem item) {
			  
		    
		    switch (item.getItemId()) {
			    		
			    case PATH:
			        	Intent intent3 = new Intent(getApplicationContext(),selectPath.class);
			        	intent3.putExtra("URI", String.valueOf(Uri));
			        	intent3.putExtra("URISRT", MainIntent.getStringExtra("URISRT"));
			        	intent3.putExtra("DELAY",MainIntent.getIntExtra("DELAY",0));
			        	intent3.putExtra("SWITCH",MainIntent.getBooleanExtra("SWITCH",false));
		            	intent3.putExtra("VIEW",MainIntent.getStringExtra("VIEW"));
						startActivity(intent3);
				        return true;
			     	 	
			    case VIDEO:
		            	Intent intent = new Intent(getApplicationContext(),affich_video.class);
		            	intent.putExtra("URI", String.valueOf(Uri));
		            	intent.putExtra("URISRT", MainIntent.getStringExtra("URISRT"));
		            	intent.putExtra("DELAY",MainIntent.getIntExtra("DELAY",0));
		            	intent.putExtra("SWITCH",MainIntent.getBooleanExtra("SWITCH",false));
		            	intent.putExtra("VIEW",MainIntent.getStringExtra("VIEW"));
		            	startActivity(intent);
	      	          	return true;
		     	 	
			    case SRT:
		            	Intent intent2 = new Intent(getApplicationContext(),affich_srt.class);
		            	intent2.putExtra("URI", String.valueOf(Uri));
		            	intent2.putExtra("URISRT", MainIntent.getStringExtra("URISRT"));
		            	intent2.putExtra("DELAY",MainIntent.getIntExtra("DELAY",0));
		            	intent2.putExtra("SWITCH",MainIntent.getBooleanExtra("SWITCH",false));
		            	intent2.putExtra("VIEW",MainIntent.getStringExtra("VIEW"));
				        return true;

		    }	
		    return super.onContextItemSelected(item);
		  }
}