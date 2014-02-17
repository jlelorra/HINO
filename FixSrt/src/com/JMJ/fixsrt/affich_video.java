package com.JMJ.fixsrt;


import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import com.JMJ.commonTools.CommonTools;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ListActivity;
import android.content.ContentResolver;
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
import android.widget.SimpleAdapter;
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
    SimpleAdapter adapter;
    Uri Uri;
    File yourDir;
    Intent MainIntent;
    ArrayList <HashMap<String,String>>nameList = new ArrayList <HashMap<String,String>>();

	
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	public void onCreate(Bundle savedInstanceState) {
	
		 super.onCreate(savedInstanceState);   
	    	MainIntent= getIntent();
			HashMap<String,String>element = new HashMap<String,String>();
		    yourDir = CommonTools.getExternalSDCardDirectory();
		    	element.put("Title", "..");
		    	element.put("SubTitle", "Parent Directory");
		    	nameList.add(element);
	    		getListeRecursiv(yourDir,String.valueOf(yourDir), element);
		    	//Collections.sort((List<String>) element.values());
		    	//arr = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_2, nameList);
		    	adapter= new SimpleAdapter(this,nameList,android.R.layout.simple_list_item_2,new String[]{ "Title", "SubTitle" },new int[] { android.R.id.text1, android.R.id.text2 });
				setListAdapter(adapter);
			    ActionBar actionBar = getActionBar();
			    actionBar.setDisplayHomeAsUpEnabled(true);
			    actionBar.setSubtitle(yourDir.getAbsolutePath());
				ListView list = this.getListView();   
			    registerForContextMenu(list);
				list.setOnItemClickListener(new OnItemClickListener() {
						
			           @Override
			            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
			
			                // TODO Auto-generated method stub
			                Intent i = new Intent(getApplicationContext(),selectPath.class);
			                HashMap<String, String> Hm_name = nameList.get(position);
			                name = Hm_name.get("Title") ; //=  adapter.getItem(position);
			                if(!name.equals("..")){
				                File Test = new File(yourDir+"/"+name);
			                	Log.d("TEST",Test.getAbsolutePath());
				                if(Test.isFile()){
				                	Log.d("IS FILE","OK");
					                Uri = MediaStore.Video.Media.getContentUri(name);
					                i.putExtra("NAME", name);
					                i.putExtra("URI", String.valueOf(Uri));
					                i.putExtra("PATHMP4", Test.getAbsolutePath().replace(name, ""));
					                i.putExtra("URISRT", MainIntent.getStringExtra("URISRT"));
					                i.putExtra("PATHSRT", MainIntent.getStringExtra("PATHSRT"));
					                i.putExtra("DELAY",MainIntent.getIntExtra("DELAY",0));
					                i.putExtra("SWITCH",MainIntent.getBooleanExtra("SWITCH",false));
						        	i.putExtra("VIEW",MainIntent.getStringExtra("VIEW"));
					                startActivity(i);
				                }
				                else if(Test.isDirectory()){
				                	Log.d("IS DIRECTORY","OK");
					             	Intent intent = new Intent(getApplicationContext(),affich_child_video.class);
					             	intent.putExtra("NAMESRT", name);
					             	intent.putExtra("URISRT", String.valueOf(Uri));
					             	intent.putExtra("URI", MainIntent.getStringExtra("URI"));
					             	intent.putExtra("PATHSRT",Test.getAbsolutePath());
					             	intent.putExtra("DELAY",MainIntent.getIntExtra("DELAY",0));
					             	intent.putExtra("SWITCH",MainIntent.getBooleanExtra("SWITCH",false));
					             	intent.putExtra("VIEW",MainIntent.getStringExtra("VIEW"));
					                startActivity(intent);
				                }
					           }else{
				                	Log.d("IS PARENT","OK");
					             	Intent intent = new Intent(getApplicationContext(),affich_child_video.class);
					             	intent.putExtra("NAMESRT", name);
					             	intent.putExtra("URISRT", String.valueOf(Uri));
					             	intent.putExtra("URI", MainIntent.getStringExtra("URI"));
					             	intent.putExtra("PATHSRT",yourDir.getParentFile().getAbsolutePath());
					             	intent.putExtra("DELAY",MainIntent.getIntExtra("DELAY",0));
					             	intent.putExtra("SWITCH",MainIntent.getBooleanExtra("SWITCH",false));
					             	intent.putExtra("VIEW",MainIntent.getStringExtra("VIEW"));
					                startActivity(intent);
					           }
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
					        
	        case R.id.about:
	        	Intent intent5 = new Intent(getApplicationContext(),about.class);
	        	intent5.putExtra("VIEW",MainIntent.getStringExtra("VIEW"));
				startActivity(intent5);
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
		  
		  
			@SuppressLint("DefaultLocale")
			public void getListeRecursiv(File path,String prefix,HashMap<String,String>el ){
				
				if(path !=null && path.exists() && path.isDirectory()){
				    for (File f : path.listFiles()) 
				    {	
				    	el = new HashMap<String,String>();
				       if (f.isFile())
				       {	
				    	   if(f.getName().toLowerCase().endsWith(".mp4")||f.getName().toLowerCase().endsWith(".mkv") || f.getName().toLowerCase().endsWith(".wmv")|| f.getName().toLowerCase().endsWith(".m4a")){
				    		   //String str_path=f.getAbsolutePath().replace(prefix+"/", "");
						    	el.put("Title", f.getName());
						    	el.put("SubTitle", "Video File");
				    		   nameList.add(el);
				    	   }
				       }else if(f.isDirectory()){
				    		   //getListeRecursiv(f,prefix,nameList);
					    	el.put("Title", f.getName());
					    	el.put("SubTitle", "Directory");
			    	   		nameList.add(el);

				       }
				   }
				}
			}
}