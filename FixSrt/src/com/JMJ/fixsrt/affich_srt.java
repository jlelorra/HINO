package com.JMJ.fixsrt;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import com.JMJ.commonTools.CommonTools;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("DefaultLocale")
public class affich_srt extends ListActivity{
	
	private static final int PATH = 0;
	private static final int VIDEO = 1;
	private static final int SRT = 2;
	private static final int CONTENTSRT = 3;
	private static final int DELETESRT = 4;
	String name; 
    Intent MainIntent;
    File yourDir;
    String path;
    SimpleAdapter adapter;
    Uri Uri;
    ArrayList <HashMap<String,String>>nameList = new  ArrayList <HashMap<String,String>>();
	
 	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint({ "NewApi", "DefaultLocale" })
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);   
    	MainIntent= getIntent();
    	HashMap<String,String>element = new HashMap<String,String>();
    	yourDir = CommonTools.getExternalSDCardDirectory(); 
    	element.put("Title", "..");
    	element.put("SubTitle", "Parent Directory");
    	nameList.add(element);
		getListeRecursiv(yourDir,String.valueOf(yourDir), element);
    	Collections.sort(nameList, new Comparator<HashMap<String,String>>(){
    	    public int compare(HashMap<String,String> s1, HashMap<String,String> s2) {
    	        return s1.get("Title").compareToIgnoreCase(s2.get("Title"));
    	    }
    	});
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
			                if(Test.isFile()){
				                Uri = MediaStore.Files.getContentUri(name);
				                i.putExtra("NAMESRT", name);
				                i.putExtra("URISRT", String.valueOf(Uri));
				                i.putExtra("URI", MainIntent.getStringExtra("URI"));
				                i.putExtra("PATHSRT",yourDir.getAbsolutePath());
				                i.putExtra("DELAY",MainIntent.getIntExtra("DELAY",0));
				                i.putExtra("SWITCH",MainIntent.getBooleanExtra("SWITCH",false));
				                i.putExtra("VIEW",MainIntent.getStringExtra("VIEW"));
				                i.putExtra("PATHMP4",MainIntent.getStringExtra("PATHMP4"));
				                startActivity(i);
			                }
			                else if(Test.isDirectory() && Test.canRead()){
				             	Intent intent = new Intent(getApplicationContext(),affich_child.class);
				             	intent.putExtra("NAMESRT", name);
				             	intent.putExtra("URISRT", String.valueOf(Uri));
				             	intent.putExtra("URI", MainIntent.getStringExtra("URI"));
				             	intent.putExtra("PATHSRT",Test.getAbsolutePath());
				             	intent.putExtra("DELAY",MainIntent.getIntExtra("DELAY",0));
				             	intent.putExtra("SWITCH",MainIntent.getBooleanExtra("SWITCH",false));
				             	intent.putExtra("VIEW",MainIntent.getStringExtra("VIEW"));
				             	intent.putExtra("PATHMP4",MainIntent.getStringExtra("PATHMP4"));
				                startActivity(intent);
			                }
				           }else{
				             	Intent intent = new Intent(getApplicationContext(),affich_child.class);
				             	intent.putExtra("NAMESRT", name);
				             	intent.putExtra("URISRT", String.valueOf(Uri));
				             	intent.putExtra("URI", MainIntent.getStringExtra("URI"));
				             	intent.putExtra("PATHSRT",yourDir.getParentFile().getAbsolutePath());
				             	intent.putExtra("DELAY",MainIntent.getIntExtra("DELAY",0));
				             	intent.putExtra("SWITCH",MainIntent.getBooleanExtra("SWITCH",false));
				             	intent.putExtra("VIEW",MainIntent.getStringExtra("VIEW"));
				             	intent.putExtra("PATHMP4",MainIntent.getStringExtra("PATHMP4"));
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
					            	intent.putExtra("URISRT", String.valueOf(Uri));
					            	intent.putExtra("URI", MainIntent.getStringExtra("URI"));
					            	intent.putExtra("DELAY",MainIntent.getIntExtra("DELAY",0));
					            	intent.putExtra("SWITCH",MainIntent.getBooleanExtra("SWITCH",false));
					            	intent.putExtra("VIEW",MainIntent.getStringExtra("VIEW"));
					            	intent.putExtra("PATHMP4",MainIntent.getStringExtra("PATHMP4"));
					            	startActivity(intent);
			          	          	return true;
			          	          	
			        case R.id.srt:
					            	Intent intent2 = new Intent(getApplicationContext(),affich_srt.class);
					            	intent2.putExtra("URISRT", String.valueOf(Uri));
					            	intent2.putExtra("URI", MainIntent.getStringExtra("URI"));
					            	intent2.putExtra("DELAY",MainIntent.getIntExtra("DELAY",0));
					            	intent2.putExtra("SWITCH",MainIntent.getBooleanExtra("SWITCH",false));
					            	intent2.putExtra("VIEW",MainIntent.getStringExtra("VIEW"));
					            	intent2.putExtra("PATHMP4",MainIntent.getStringExtra("PATHMP4"));
									startActivity(intent2);
							        return true;
			
			        case R.id.path:
						        	Intent intent3 = new Intent(getApplicationContext(),selectPath.class);
						        	intent3.putExtra("URISRT", String.valueOf(Uri));
						        	intent3.putExtra("URI", MainIntent.getStringExtra("URI"));
						        	intent3.putExtra("DELAY",MainIntent.getIntExtra("DELAY",0));
						        	intent3.putExtra("SWITCH",MainIntent.getBooleanExtra("SWITCH",false));
						        	intent3.putExtra("VIEW",MainIntent.getStringExtra("VIEW"));
						        	intent3.putExtra("PATHMP4",MainIntent.getStringExtra("PATHMP4"));
									startActivity(intent3);
							        return true;
			        case R.id.about:
						        	Intent intent5 = new Intent(getApplicationContext(),about.class);
						        	intent5.putExtra("VIEW",MainIntent.getStringExtra("VIEW"));
						        	intent5.putExtra("NAMESRT",  MainIntent.getStringExtra("NAMESRT"));
						        	intent5.putExtra("URISRT",  MainIntent.getStringExtra("URISRT"));
						        	intent5.putExtra("URI", String.valueOf(Uri));
						        	intent5.putExtra("PATHSRT",MainIntent.getStringExtra("PATHSRT"));
						        	intent5.putExtra("DELAY",MainIntent.getIntExtra("DELAY",0));
						        	intent5.putExtra("SWITCH",MainIntent.getBooleanExtra("SWITCH",false));
						        	intent5.putExtra("PATHMP4",MainIntent.getStringExtra("PATHMP4"));
									startActivity(intent5);
							        return true;

			        }
			        return super.onOptionsItemSelected(item);
			      }
				public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuinfo) {
					
					  super.onCreateContextMenu(menu, v, menuinfo);
					  menu.add(Menu.NONE, PATH, Menu.NONE, "Main");
					  menu.add(Menu.NONE, VIDEO, Menu.NONE, "Video");
					  menu.add(Menu.NONE, SRT, Menu.NONE, "Srt");
					  menu.add(Menu.NONE, CONTENTSRT, Menu.NONE, "Display Srt Content");
					  menu.add(Menu.NONE, DELETESRT, Menu.NONE, "Delete Srt File");

				  }
				  
				
				
				  @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
				@SuppressLint("NewApi")
				@Override
				  public boolean onContextItemSelected(MenuItem item) {
					  
				    
				    switch (item.getItemId()) {
					    		
					    case PATH:
					        	Intent intent3 = new Intent(getApplicationContext(),selectPath.class);
					        	intent3.putExtra("URISRT", String.valueOf(Uri));
					        	intent3.putExtra("URI", MainIntent.getStringExtra("URI"));
					        	intent3.putExtra("DELAY",MainIntent.getIntExtra("DELAY",0));
					        	intent3.putExtra("SWITCH",MainIntent.getBooleanExtra("SWITCH",false));
				            	intent3.putExtra("VIEW",MainIntent.getStringExtra("VIEW"));
				            	intent3.putExtra("PATHMP4",MainIntent.getStringExtra("PATHMP4"));
								startActivity(intent3);
						        return true;
					     	 	
					    case VIDEO:
				            	Intent intent = new Intent(getApplicationContext(),affich_video.class);
				            	intent.putExtra("URISRT", String.valueOf(Uri));
				            	intent.putExtra("URI", MainIntent.getStringExtra("URI"));
				            	intent.putExtra("DELAY",MainIntent.getIntExtra("DELAY",0));
				            	intent.putExtra("SWITCH",MainIntent.getBooleanExtra("SWITCH",false));
				            	intent.putExtra("VIEW",MainIntent.getStringExtra("VIEW"));
				            	intent.putExtra("PATHMP4",MainIntent.getStringExtra("PATHMP4"));
				            	startActivity(intent);
			      	          	return true;
				     	 	
					    case SRT:
				            	Intent intent2 = new Intent(getApplicationContext(),affich_srt.class);
				            	intent2.putExtra("URISRT", String.valueOf(Uri));
				            	intent2.putExtra("URI", MainIntent.getStringExtra("URI"));
				            	intent2.putExtra("DELAY",MainIntent.getIntExtra("DELAY",0));
				            	intent2.putExtra("SWITCH",MainIntent.getBooleanExtra("SWITCH",false));
				            	intent2.putExtra("VIEW",MainIntent.getStringExtra("VIEW"));
				            	intent2.putExtra("PATHMP4",MainIntent.getStringExtra("PATHMP4"));
				            	startActivity(intent2);
						        return true;
						        
					    case CONTENTSRT:
								AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
								int position=item.getItemId();
				                path = yourDir.getAbsolutePath();
				                HashMap<String, String> Hm_name = nameList.get(info.position);
				                name = Hm_name.get("Title") ; //=  adapter.getItem(position);
								File file2 = new File( path +"/"+name);
								if(file2.isFile() && (file2.getName().toLowerCase().endsWith(".srt")||file2.getName().toLowerCase().endsWith(".ass"))){
					            	Intent intent5 = new Intent(getApplicationContext(),affich_srt_txt.class);
					            	intent5.putExtra("PATHSRT", path);
					            	intent5.putExtra("URISRT",   name);
					            	intent5.putExtra("URI", MainIntent.getStringExtra("URI"));
					            	intent5.putExtra("DELAY",MainIntent.getIntExtra("DELAY",0));
					            	intent5.putExtra("SWITCH",MainIntent.getBooleanExtra("SWITCH",false));
					            	intent5.putExtra("VIEW",MainIntent.getStringExtra("VIEW"));
					            	intent5.putExtra("PATHMP4",MainIntent.getStringExtra("PATHMP4"));
					            	startActivity(intent5);
								}
					        return true;
					        
					    case DELETESRT:
								info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
								position=item.getItemId();
								Hm_name = nameList.get(info.position);
				                name = Hm_name.get("Title") ;
				                path = yourDir.getAbsolutePath();
								File file = new File( path +"/"+name);
								if(file.isFile() && (file.getName().toLowerCase().endsWith(".srt")||file.getName().toLowerCase().endsWith(".ass"))){
									boolean deleted = file.delete();
									if(deleted)	onResume();
								}
					        return true;
						    

				    }	
				    return super.onContextItemSelected(item);
				}	
				  
				  
					@Override
					protected void onResume(){
						super.onResume();
						nameList.clear();
						this.onCreate(null);
					}
					
				
					@SuppressLint("DefaultLocale")
					public void getListeRecursiv(File path,String prefix,HashMap<String,String>el ){
						
						if(path !=null && path.exists() && path.isDirectory()){
						    for (File f : path.listFiles()) 
						    {	
						    	el = new HashMap<String,String>();
						       if (f.isFile())
						       {	
						    	   if(f.getName().toLowerCase().endsWith(".srt")||f.getName().toLowerCase().endsWith(".ass")){
						    		   //String str_path=f.getAbsolutePath().replace(prefix+"/", "");
								    	el.put("Title", f.getName());
								    	el.put("SubTitle", "SubTitle File");
						    		   nameList.add(el);
						    	   }
						       }else if(f.isDirectory() && f.canRead()){
						    		   //getListeRecursiv(f,prefix,nameList);
							    	el.put("Title", f.getName());
							    	el.put("SubTitle", "Directory");
					    	   		nameList.add(el);

						       }
						   }
						}
					}
					
}
