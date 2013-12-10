package com.example.cdtheque;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.ListActivity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class affich_contact2 extends ListActivity {
	ListView liste = null;
	Cursor cursor;
	int nameIdx;
    String name; 
    ArrayAdapter<String> arr;
    ArrayList<Object> listActivities;
    ListAdapter adapter;



@TargetApi(Build.VERSION_CODES.HONEYCOMB)
@SuppressLint("NewApi")
@SuppressWarnings("unchecked")
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    ContentResolver cr = getContentResolver();
    cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
    nameIdx = cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME);
    Integer idx = cursor.getCount();
    String str[]= new String[idx+1];
    str[0]="FREE (disponible)";
    if (cursor.moveToFirst()){
        int x = 1;
	    do{
		   	 name = cursor.getString(nameIdx);
		     Log.d("CONTENT",name);
		     str[x]= name;
		     x++;
		 } while(cursor.moveToNext());
	 //str[idx+1]="FREE (disponible)";   
	 arr = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,str);
	 setListAdapter(arr);
     ActionBar actionBar = getActionBar();
     actionBar.setDisplayHomeAsUpEnabled(true);
	 ListView list = this.getListView();   
	 list.setOnItemClickListener(new OnItemClickListener() {
			
           @Override
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {

                // TODO Auto-generated method stub
                Intent i = new Intent(getApplicationContext(),UpdateCD.class);
                Intent mainIntent = getIntent();
                name =  arr.getItem(position);
                i.putExtra("ID", mainIntent.getIntExtra("ID", 0));
                i.putExtra("CONTACT", name);
                i.putExtra("ALBUM",mainIntent.getStringExtra("ALBUM"));
                i.putExtra("YEAR",mainIntent.getStringExtra("YEAR"));
                i.putExtra("RATE",mainIntent.getFloatExtra("RATE",2));
                i.putExtra("ARTIST",mainIntent.getStringExtra("ARTIST"));
                Log.d("CONTENT",name);
                startActivity(i);
            }
        });
    } 

}
}