package com.JMJ.fixsrt;

import java.io.File;
import java.util.ArrayList;

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
	    yourDir = CommonTools.getExternalSDCardDirectory(); //new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "");
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
		    }*/
		    /*for (File f : yourDir.listFiles()) 
		    {
		       if (f.isFile())
		       {	
		    	   if(f.getName().endsWith(".srt")||f.getName().endsWith(".ass")){
		    		   nameList.add(f.getName());
		    	   }
		       }
	
		   }*/
	    	getListeRecursiv(yourDir,String.valueOf(yourDir), nameList);
		    DownloadDir = CommonTools.getDownloadSDCardDirectory(); 
			/*if(DownloadDir!=null){
				    for (File f : DownloadDir.listFiles()) 
				    {
				       if (f.isFile())
				       {	
				    	   if(f.getName().endsWith(".srt")||f.getName().endsWith(".ass")){
				    		   nameList.add(f.getName());
				    	   }
				       }
			
				   }
			}*/
	    	getListeRecursiv(DownloadDir,String.valueOf(DownloadDir), nameList);
		   BlueToothDir = CommonTools.getBlueToothSDCardDirectory(); 
		   /*if(BlueToothDir!=null){
			    for (File f : BlueToothDir.listFiles()) 
			    {
			       if (f.isFile())
			       {	
			    	   if(f.getName().endsWith(".srt")||f.getName().endsWith(".ass")){
			    		   nameList.add(f.getName());
			    	   }
			       }
		
			   }
		   }*/
	    	getListeRecursiv(BlueToothDir,String.valueOf(BlueToothDir), nameList);
	    	arr = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,nameList);
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
		                Uri = MediaStore.Files.getContentUri(arr.getItem(position));
		                path = getSrtPath(arr.getItem(position));
		                i.putExtra("NAMESRT", name);
		                i.putExtra("URISRT", String.valueOf(Uri));
		                i.putExtra("URI", MainIntent.getStringExtra("URI"));
		                i.putExtra("PATHSRT",path);
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
					            	intent.putExtra("URISRT", String.valueOf(Uri));
					            	intent.putExtra("URI", MainIntent.getStringExtra("URI"));
					            	intent.putExtra("DELAY",MainIntent.getIntExtra("DELAY",0));
					            	intent.putExtra("SWITCH",MainIntent.getBooleanExtra("SWITCH",false));
					            	intent.putExtra("VIEW",MainIntent.getStringExtra("VIEW"));
					            	startActivity(intent);
			          	          	return true;
			          	          	
			        case R.id.srt:
					            	Intent intent2 = new Intent(getApplicationContext(),affich_srt.class);
					            	intent2.putExtra("URISRT", String.valueOf(Uri));
					            	intent2.putExtra("URI", MainIntent.getStringExtra("URI"));
					            	intent2.putExtra("DELAY",MainIntent.getIntExtra("DELAY",0));
					            	intent2.putExtra("SWITCH",MainIntent.getBooleanExtra("SWITCH",false));
					            	intent2.putExtra("VIEW",MainIntent.getStringExtra("VIEW"));
									startActivity(intent2);
							        return true;
			
			        case R.id.path:
						        	Intent intent3 = new Intent(getApplicationContext(),selectPath.class);
						        	intent3.putExtra("URISRT", String.valueOf(Uri));
						        	intent3.putExtra("URI", MainIntent.getStringExtra("URI"));
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
								startActivity(intent3);
						        return true;
					     	 	
					    case VIDEO:
				            	Intent intent = new Intent(getApplicationContext(),affich_video.class);
				            	intent.putExtra("URISRT", String.valueOf(Uri));
				            	intent.putExtra("URI", MainIntent.getStringExtra("URI"));
				            	intent.putExtra("DELAY",MainIntent.getIntExtra("DELAY",0));
				            	intent.putExtra("SWITCH",MainIntent.getBooleanExtra("SWITCH",false));
				            	intent.putExtra("VIEW",MainIntent.getStringExtra("VIEW"));
				            	startActivity(intent);
			      	          	return true;
				     	 	
					    case SRT:
				            	Intent intent2 = new Intent(getApplicationContext(),affich_srt.class);
				            	intent2.putExtra("URISRT", String.valueOf(Uri));
				            	intent2.putExtra("URI", MainIntent.getStringExtra("URI"));
				            	intent2.putExtra("DELAY",MainIntent.getIntExtra("DELAY",0));
				            	intent2.putExtra("SWITCH",MainIntent.getBooleanExtra("SWITCH",false));
				            	intent2.putExtra("VIEW",MainIntent.getStringExtra("VIEW"));
				            	startActivity(intent2);
						        return true;
						        
					    case CONTENTSRT:
								AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
								int position=item.getItemId();
				                path = getSrtPath(arr.getItem(info.position));
				            	Intent intent5 = new Intent(getApplicationContext(),affich_srt_txt.class);
				            	intent5.putExtra("PATHSRT", path);
				            	intent5.putExtra("URISRT",   arr.getItem(info.position));
				            	intent5.putExtra("DELAY",MainIntent.getIntExtra("DELAY",0));
				            	intent5.putExtra("SWITCH",MainIntent.getBooleanExtra("SWITCH",false));
				            	intent5.putExtra("VIEW",MainIntent.getStringExtra("VIEW"));
				            	startActivity(intent5);
					        return true;
					        
					    case DELETESRT:
								info = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
								position=item.getItemId();
								path = getSrtPath(arr.getItem(info.position));
								File file = new File( path +"/"+arr.getItem(info.position));
								boolean deleted = file.delete();
								if(deleted)	onResume();
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
						
						Log.d("PATH",String.valueOf(path));
						Log.d("PREFIX",prefix);
						if(path !=null && path.exists() && path.isDirectory()){
						    for (File f : path.listFiles()) 
						    {
						       if (f.isFile())
						       {	
						    	   if(f.getName().endsWith(".srt")||f.getName().endsWith(".ass")){
						    		   prefix=path.getAbsolutePath().replace(prefix, "");
						    		   nameList.add(prefix+"/"+f.getName());
						    		   Log.d("ADD",String.valueOf(prefix+"/"+f.getName()));
						    	   }
						       }else if(f.isDirectory()){
						    		   Log.d("CONTENU",f.getName());
						    		   prefix=path.getAbsolutePath().replace(prefix, "");
						    		   Log.d("PREFIXXXXX",prefix);
						    		   getListeRecursiv(f,prefix,nameList);

						       }
						   }
						}
					}
					
}
