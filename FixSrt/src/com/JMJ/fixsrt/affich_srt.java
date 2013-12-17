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

public class affich_srt extends ListActivity{
	
	ListView liste = null;
	Cursor cursor;
	int nameIdx;
	String name; 
    ArrayAdapter<String> arr;
    Intent MainIntent;
	
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint("NewApi")
	@SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);   
    	MainIntent= getIntent();
	    ArrayList<String>nameList = new ArrayList<String>();
	    File yourDir = new File(Environment.getExternalStorageDirectory(), "");
	    for (File f : yourDir.listFiles()) 
	    {
	       if (f.isFile())
	       {	
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
	                startActivity(i);
	            }
	        });
	}	
}
