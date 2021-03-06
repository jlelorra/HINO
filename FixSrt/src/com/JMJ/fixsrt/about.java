package com.JMJ.fixsrt;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class about extends Activity {
	
	private TextView about = null;
	Intent MainIntent;

	@SuppressLint("DefaultLocale")
	protected void onCreate(Bundle savedInstanceState) {
		
		//setContentView(R.layout.affich_srt_text);
		   super.onCreate(savedInstanceState);     
		   about = new TextView(this);
		   MainIntent= getIntent();
		   PackageInfo pInfo=null;
			try {
				pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			} catch (NameNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		   String version = pInfo.versionName;
		   if(MainIntent.getStringExtra("VIEW").toLowerCase().contains("_fr"))about.setText("\n\nFixSrt "+version+"\n\n\n\nDeveloppe par Jason LELORRAIN.\n\n Cette application :\n\t - permet d'adapter les sous titres selectionnes par rapport à la video.\n\t - liste l'ensemble des sous titres (.srt et .ass) contenu a la racine de la carte Sd externe, du dossier de telechargement ainsi que du dossier bluetooth.");
		   else about.setText("\n\nFixSrt "+version+"\n\n\n\nDevelopped By Jason LELORRAIN.\n\n This application :\n\t- adapt selected subtitles with the delay provided.\n\t- list all subtitles (.srt et .ass) contained in External SdCard root Directory, Download root directory and BlueTooth root directory.");		   
		   setContentView(about);
		   about.setClickable(true);
		   about.setOnClickListener(new OnClickListener(){
   			   @Override
   			    public void onClick(View v) {
		             	Intent intent = new Intent(getApplicationContext(),selectPath.class);
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
			            	startActivity(intent);
	          	          	return true;
	          	          	
	        case R.id.srt:
			            	Intent intent2 = new Intent(getApplicationContext(),affich_srt.class);
							startActivity(intent2);
					        return true;
	
	        case R.id.path:
				        	Intent intent3 = new Intent(getApplicationContext(),selectPath.class);
							startActivity(intent3);
					        return true;
					        
	        case R.id.about:
	        	Intent intent5 = new Intent(getApplicationContext(),about.class);
	        	intent5.putExtra("VIEW",MainIntent.getStringExtra("VIEW"));
	        	intent5.putExtra("NAMESRT",  MainIntent.getStringExtra("NAMESRT"));
	        	intent5.putExtra("URISRT",  MainIntent.getStringExtra("URISRT"));
	        	intent5.putExtra("URI", MainIntent.getStringExtra("URI"));
	        	intent5.putExtra("PATHSRT",MainIntent.getStringExtra("PATHSRT"));
	        	intent5.putExtra("DELAY",MainIntent.getIntExtra("DELAY",0));
	        	intent5.putExtra("SWITCH",MainIntent.getBooleanExtra("SWITCH",false));
	        	intent5.putExtra("PATHMP4",MainIntent.getStringExtra("PATHMP4"));
				startActivity(intent5);
		        return true;

	        }
	        return super.onOptionsItemSelected(item);
	      }
}
