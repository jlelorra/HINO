package com.JMJ.fixsrt;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

public class affich_srt_txt extends Activity {
	
	Intent MainIntent;
	TextView edittext;
	String line;
	StringBuilder Buffer= new StringBuilder();
	
    public void onCreate(Bundle savedInstanceState) {
    	MainIntent= getIntent();
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.affich_srt_text); 
	    edittext = (TextView) findViewById(R.id.editText1);
	    String uriSrt=MainIntent.getStringExtra("PATHSRT")+"/"+MainIntent.getStringExtra("URISRT");
        try{ 
        		FileInputStream input = new FileInputStream(uriSrt);
        		DataInputStream in = new DataInputStream(input);
        		BufferedReader br = new BufferedReader(new InputStreamReader(in));
	           //affiche le contenu de mon fichier dans un popup surgissant
        		while((line = br.readLine()) != null) {
        			Buffer.append(line+"\n");
        		}
        		br.close();
        		edittext.setText(Buffer.toString());
        	    ActionBar actionBar = getActionBar();
        	    actionBar.setDisplayHomeAsUpEnabled(true);
        	    actionBar.setSubtitle(MainIntent.getStringExtra("PATHSRT")+"/"+MainIntent.getStringExtra("URISRT"));
        	    edittext.setClickable(true);
        	    edittext.setOnClickListener(new OnClickListener(){
        			   @Override
        			    public void onClick(View v) {
			             	Intent intent = new Intent(getApplicationContext(),affich_child.class);
			             	intent.putExtra("NAMESRT",  MainIntent.getStringExtra("NAMESRT"));
			             	intent.putExtra("URISRT",  MainIntent.getStringExtra("URISRT"));
			             	intent.putExtra("URI", MainIntent.getStringExtra("URI"));
			             	intent.putExtra("PATHSRT",MainIntent.getStringExtra("PATHSRT"));
			             	intent.putExtra("DELAY",MainIntent.getIntExtra("DELAY",0));
			             	intent.putExtra("SWITCH",MainIntent.getBooleanExtra("SWITCH",false));
			             	intent.putExtra("VIEW",MainIntent.getStringExtra("VIEW"));
			            	intent.putExtra("PATHMP4",MainIntent.getStringExtra("PATHMP4"));
			                startActivity(intent);
			                return;
        			    }
        		});
        		
            }catch (Exception e) {       
                      Toast.makeText(this, "Settings not read",Toast.LENGTH_SHORT).show(); 
                      e.printStackTrace();
            } 

    }
}
