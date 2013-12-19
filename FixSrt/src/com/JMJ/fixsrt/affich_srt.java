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
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

@SuppressLint("DefaultLocale")
public class affich_srt extends ListActivity{
	
	ListView liste = null;
	Cursor cursor;
	int nameIdx;
	String name; 
    ArrayAdapter<String> arr;
    Intent MainIntent;
    File yourDir;
	
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint({ "NewApi", "DefaultLocale" })
	@SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);   
    	MainIntent= getIntent();
	    ArrayList<String>nameList = new ArrayList<String>();
	    yourDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "");
	    Log.d("PATH",Environment.getExternalStorageDirectory().getAbsolutePath());
	    if(android.os.Build.DEVICE.toLowerCase().contains("samsung") 
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
	    //if(yourDir.isFile())
	    //{
		    for (File f : yourDir.listFiles()) 
		    {
		       if (f.isFile())
		       {	
		    	   Log.d("FILE",f.getName());
		    	   if(f.getName().endsWith(".srt")){
		    		   nameList.add(f.getName());
		    	   }
		       }
	
		   }
	    	arr = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,nameList);
			setListAdapter(arr);
		    ActionBar actionBar = getActionBar();
		    actionBar.setDisplayHomeAsUpEnabled(true);
			ListView list = this.getListView();   
			list.setOnItemClickListener(new OnItemClickListener() {
					
		           @Override
		            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
		
		                // TODO Auto-generated method stub
		                Intent i = new Intent(getApplicationContext(),selectPath.class);
		                name =  arr.getItem(position);
		                Uri Uri = MediaStore.Files.getContentUri(arr.getItem(position));
		                i.putExtra("NAMESRT", name);
		                Log.d("URI",String.valueOf(Uri));
		                i.putExtra("URISRT", String.valueOf(Uri));
		                i.putExtra("URI", MainIntent.getStringExtra("URI"));
		                i.putExtra("PATHSRT",yourDir.getAbsolutePath());
		                startActivity(i);
		            }
		        });
	    //}else{Toast.makeText(getApplicationContext(), "No Sutitle file detected", Toast.LENGTH_LONG).show();}
	}	
}
