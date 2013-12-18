package com.JMJ.fixsrt;


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
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.net.Uri;

public class affich_video extends ListActivity{
	
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
	                startActivity(i);
	            }
	        });
	}
}