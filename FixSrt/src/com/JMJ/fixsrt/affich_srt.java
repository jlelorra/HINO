package com.JMJ.fixsrt;

import java.io.File;
import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
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
	
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@SuppressLint({ "NewApi", "DefaultLocale" })
	@SuppressWarnings("unchecked")
    public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);   
    	MainIntent= getIntent();
	    ArrayList<String>nameList = new ArrayList<String>();
	    File yourDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), "");
 	  Log.d("DEVICE",android.os.Build.BRAND);
	    if(android.os.Build.DEVICE.toLowerCase().contains("samsung") 
	    || android.os.Build.MANUFACTURER.toLowerCase().contains("samsung") 
	    || android.os.Build.PRODUCT.toLowerCase().contains("samsung") 
	    ||android.os.Build.BRAND.toLowerCase().contains("samsung")) {
            yourDir = new File("/storage/extSdCard/", "");
	    }
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
	   /* try{
	    	arr = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,nameList);
			setListAdapter(arr);
	    }catch(Exception e) 
	    {
			if(nameList.isEmpty()){
				ContentResolver cr = getContentResolver();
				String selectionMimeType = MediaStore.Files.FileColumns.MIME_TYPE+"=?";
				String mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension("srt");
				String mimeTypeAss = MimeTypeMap.getSingleton().getMimeTypeFromExtension("ass");
				String[] selectionArgsPdf = new String[]{mimeType,mimeTypeAss};
				cursor = cr.query(MediaStore.Files.getContentUri("external"), null, null, null , null);
				nameIdx = cursor.getColumnIndexOrThrow(MediaStore.Files.FileColumns.DISPLAY_NAME);
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
			}
	    }*/
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
