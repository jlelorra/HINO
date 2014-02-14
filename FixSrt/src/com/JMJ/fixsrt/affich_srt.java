package com.JMJ.fixsrt;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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

import com.JMJ.commonTools.*;

@SuppressLint("DefaultLocale")
public class affich_srt extends ListActivity{
	
	private static final int PATH = 0;
	private static final int VIDEO = 1;
	private static final int SRT = 2;
	private static final int CONTENTSRT = 3;
	private static final int DELETESRT = 4;
	ListView liste = null;
	Cursor cursor;
	int nameIdx;
	String name; 
	private static ArrayAdapter<String> arr;
    Intent MainIntent;
    File yourDir;
    String path;
    File DownloadDir;
    File BlueToothDir;
    Uri Uri;
	
 	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint({ "NewApi", "DefaultLocale" })
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);   
    	MainIntent= getIntent();
	    ArrayList<String>nameList = new ArrayList<String>();
	    /*if(!MainIntent.getStringExtra("PATHSRT").trim().equalsIgnoreCase("")){ 
	    	yourDir = new File(MainIntent.getStringExtra("PATHSRT"));
		    File[] files = yourDir.listFiles();
		    for (File file : files) {
		    	Log.d("FILE TO STRING",file.toString());
		    	nameList.add(file.toString());
		    }
	    }else*/ yourDir = CommonTools.getExternalSDCardDirectory(); //new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "");
	    /*if(android.os.Build.DEVICE.toLowerCase().contains("samsung") 
		    || android.os.Build.MANUFACTURER.toLowerCase().contains("samsung") 
		    || android.os.Build.PRODUCT.toLowerCase().contains("samsung") 
		    ||android.os.Build.BRAND.toLowerCase().contains("samsung")
		    ||android.os.Build.DEVICE.toLowerCase().contains("galaxy") 
		    || android.os.Build.MANUFACTURER.toLowerCase().contains("galaxy") 
		    || android.os.Build.PRODUCT.toLowerCase().contains("galaxy") 
		    ||android.os.Build.BRAND.toLowerCase().contains("galaxy")) 
	    {
	            yourDir = new File("/storage/extSdCard/", "");
	    }
	    if(android.os.Build.DEVICE.toLowerCase().contains("tablet s") 
			|| android.os.Build.MANUFACTURER.toLowerCase().contains("tablet s") 
			|| android.os.Build.PRODUCT.toLowerCase().contains("tablet s") 
			||android.os.Build.BRAND.toLowerCase().contains("tablet s")
			||android.os.Build.DEVICE.toLowerCase().contains("sony") 
			|| android.os.Build.MANUFACTURER.toLowerCase().contains("sony") 
			|| android.os.Build.PRODUCT.toLowerCase().contains("sony") 
			||android.os.Build.BRAND.toLowerCase().contains("sony")) 
		    {
		            yourDir = new File("/mnt/sdcard2/", "");
		    }
	    if(android.os.Build.DEVICE.toLowerCase().contains("wiko") 
			|| android.os.Build.MANUFACTURER.toLowerCase().contains("wiko") 
			|| android.os.Build.PRODUCT.toLowerCase().contains("wiko") 
			||android.os.Build.BRAND.toLowerCase().contains("wiko")
			|| android.os.Build.DEVICE.toLowerCase().contains("htc") 
			|| android.os.Build.MANUFACTURER.toLowerCase().contains("htc") 
			|| android.os.Build.PRODUCT.toLowerCase().contains("htc") 
			||android.os.Build.BRAND.toLowerCase().contains("htc")) 
		    {
		            yourDir = new File("/mnt/sdcard/", "");
		    }
	    if(android.os.Build.DEVICE.toLowerCase().contains("lg") 
			|| android.os.Build.MANUFACTURER.toLowerCase().contains("lg") 
			|| android.os.Build.PRODUCT.toLowerCase().contains("lg") 
			||android.os.Build.BRAND.toLowerCase().contains("lg")) 
		    {
		            yourDir = new File("/mnt/sdcard/external_sd", "");
		    }
	    if(android.os.Build.DEVICE.toLowerCase().contains("clust") 
			|| android.os.Build.MANUFACTURER.toLowerCase().contains("clust") 
			|| android.os.Build.PRODUCT.toLowerCase().contains("clust") 
			||android.os.Build.BRAND.toLowerCase().contains("clust")) 
		    {
		            yourDir = new File("/mnt/ext-sd", "");
		    }
		    /*DownloadDir = CommonTools.getDownloadSDCardDirectory(); 
	    	getListeRecursiv(DownloadDir,String.valueOf(DownloadDir), nameList);
		   BlueToothDir = CommonTools.getBlueToothSDCardDirectory(); 
	    	getListeRecursiv(BlueToothDir,String.valueOf(BlueToothDir), nameList);*/
	    	nameList.add("..");
    		getListeRecursiv(yourDir,String.valueOf(yourDir), nameList);
	    	Collections.sort((List<String>) nameList);
	    	arr = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, nameList);
			setListAdapter(arr);
		    ActionBar actionBar = getActionBar();
		    actionBar.setDisplayHomeAsUpEnabled(true);
			ListView list = this.getListView();   
		    registerForContextMenu(list);
			list.setOnItemClickListener(new OnItemClickListener() {
					
		           @Override
		            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
		
		                // TODO Auto-generated method stub
		                Intent i = new Intent(getApplicationContext(),selectPath.class);
		                name =  arr.getItem(position);
		                if(!name.equals("..")){
			                File Test = new File(yourDir+"/"+name);
		                	Log.d("TEST",Test.getAbsolutePath());
			                if(Test.isFile()){
			                	Log.d("IS FILE","OK");
				                Uri = MediaStore.Files.getContentUri(arr.getItem(position));
				                path = getSrtPath(arr.getItem(position));
				                i.putExtra("NAMESRT", name);
				                i.putExtra("URISRT", String.valueOf(Uri));
				                i.putExtra("URI", MainIntent.getStringExtra("URI"));
				                i.putExtra("PATHSRT",path);
				                i.putExtra("DELAY",MainIntent.getIntExtra("DELAY",0));
				                i.putExtra("SWITCH",MainIntent.getBooleanExtra("SWITCH",false));
				                i.putExtra("VIEW",MainIntent.getStringExtra("VIEW"));
				                i.putExtra("PATHMP4",MainIntent.getStringExtra("PATHMP4"));
				                startActivity(i);
			                }
			                else if(Test.isDirectory()){
			                	Log.d("IS DIRECTORY","OK");
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
			                	Log.d("IS PARENT","OK");
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
				                path = getSrtPath(arr.getItem(info.position));
								File file2 = new File( path +"/"+arr.getItem(info.position));
								if(file2.isFile() && (file2.getName().endsWith(".srt")||file2.getName().endsWith(".ass"))){
					            	Intent intent5 = new Intent(getApplicationContext(),affich_srt_txt.class);
					            	intent5.putExtra("PATHSRT", path);
					            	intent5.putExtra("URISRT",   arr.getItem(info.position));
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
								path = getSrtPath(arr.getItem(info.position));
								File file = new File( path +"/"+arr.getItem(info.position));
								if(file.isFile() && (file.getName().endsWith(".srt")||file.getName().endsWith(".ass"))){
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
						this.onCreate(null);
					}
					
					
					
					public String getSrtPath(String pos){
						
						String Dir = yourDir.getAbsolutePath()+"/"+pos;
		                File f = new File(Dir);
		                if(!f.exists()){
		                	Dir = DownloadDir.getAbsolutePath()+"/"+pos;
		                	f = new File(Dir);
		                	if(!f.exists()){
		                		Dir = BlueToothDir.getAbsolutePath()+"/"+pos;
			                	f = new File(Dir);
		                	}
		                	
		                }
		                Dir=Dir.replace("/"+pos, "");
		                return Dir;
					}
					
					public void getListeRecursiv(File path,String prefix,ArrayList<String>nameList ){
						
						if(path !=null && path.exists() && path.isDirectory()){
						    for (File f : path.listFiles()) 
						    {
						       if (f.isFile())
						       {	
						    	   if(f.getName().endsWith(".srt")||f.getName().endsWith(".ass")){
						    		   //String str_path=f.getAbsolutePath().replace(prefix+"/", "");
						    		   nameList.add(f.getName());
						    	   }
						       }else if(f.isDirectory()){
						    		   //getListeRecursiv(f,prefix,nameList);
						    	   		nameList.add(f.getName());

						       }
						   }
						}
					}
					
}
