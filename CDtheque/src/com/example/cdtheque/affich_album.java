package com.example.cdtheque;

import java.util.ArrayList;

import android.app.ListActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

public class affich_album extends ListActivity{
	ListView liste = null;
    ArrayAdapter<String> arr;
    ArrayList<Object> listActivities;
    ListAdapter adapter;
    CDBDD cdBdd;



@SuppressWarnings("unchecked")
public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    cdBdd = new CDBDD(this);
    cdBdd.open();
    CD[] CDs = cdBdd.getAllAlbum();
    String str[]= new String[CDs.length];
    for(int i = 0;i>=CDs.length;i++){
    	Log.d("CONTENT",CDs[i].getAlbum());
	     str[i]= CDs[i].getAlbum();
    	
    }
	 arr = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,str);
	 setListAdapter(arr);
	 //ListView list = this.getListView();   
	 cdBdd.close();
    } 

}

