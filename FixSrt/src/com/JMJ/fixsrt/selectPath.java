package com.JMJ.fixsrt;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.List;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class selectPath extends Activity {
	private static final int LANGUAGE = 0;
	private static final int VIDEO = 1;
	private static final int SRT = 2;
	String UriVideo;
	String UriSrt;
	TextView pathvideo;
	TextView pathSrt;
	TextView lbl_video;
	TextView lbl_Srt;
	TextView lbl_sec;
	TextView lbl_millisec;
	TextView lbl_time;
	TextView lbl_1;
	TextView lbl_2;
	TextView lbl_3;
	Button setVideo;
	Button setSrt;
	Button Test;
	Cursor cursor;
	NumberPicker picker;
	NumberPicker picker1;
	NumberPicker picker2;
	NumberPicker picker3;
	Switch toggle;
	Intent i1;
	CharSequence[] items = {"FR","EN"};
	String View ="selectpath";
	int screenHeight;
	int screenWidth;
	int screendensity;
	int textsize1;
	
	
	@SuppressLint("NewApi")
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    i1 = getIntent();
		if(i1.getStringExtra("VIEW")!=null){
			View=i1.getStringExtra("VIEW");
		}
		Configuration newConfig = getResources().getConfiguration();
    	if(newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE){
    		if(View.equals("selectpath")||View.equals("selectpath_ms")){
	            setContentView(R.layout.selectpath_ms); 
	            View="selectpath_ms";
	            populate();
	            return;
    		}else{
    		    setContentView(R.layout.selectpath_ms_fr);
	            View="selectpath_ms_fr";
	            populate();
	            return;
    		}
    	}else{
    		if(View.equals("selectpath")||View.equals("selectpath_ms")){
    			setContentView(R.layout.selectpath);
	            View="selectpath";
	            populate2();
	            return;
    		}else{
    		    setContentView(R.layout.selectpath_fr);
	            View="selectpath_fr";
	            populate2();
	            return;
    		}
    	}
		
	}
	
	public void startApplication(String packageName) //pour VLC "org.videolan.vlc.betav7neon"
	{
	    try
	    {
	        Intent intent = new Intent("android.intent.action.MAIN");
	        intent.addCategory("android.intent.category.LAUNCHER");

	        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
	        List<ResolveInfo> resolveInfoList = getPackageManager().queryIntentActivities(intent, 0);

	        for(ResolveInfo info : resolveInfoList)
	            if(info.activityInfo.packageName.equalsIgnoreCase(packageName))
	            {
	                launchComponent(info.activityInfo.packageName, info.activityInfo.name);
	                return;
	            }
	    }
	    catch (Exception e) 
	    {
	        showInMarket(packageName);
	    }
	}

	private void launchComponent(String packageName, String name)
	{
	    Intent intent = new Intent("android.intent.action.MAIN");
	    intent.addCategory("android.intent.category.LAUNCHER");
	    intent.setComponent(new ComponentName(packageName, name));
	    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    intent.setDataAndType(Uri.parse(i1.getStringExtra("URI")), "video/*");
	    intent.setDataAndType(Uri.parse(i1.getStringExtra("URISRT")), "file/*");
	    startActivity(intent);
	}

	private void showInMarket(String packageName)
	{
	    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName));
	    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	    //intent.setDataAndType(Uri.parse(i1.getStringExtra("URI")), "video/*");
	    //intent.setDataAndType(Uri.parse(i1.getStringExtra("URISRT")), "file/*");
	    startActivity(intent);
	}
	
	public boolean doesPackageExist(String targetPackage) {

	    PackageManager pm = getPackageManager();
	    try {
	        PackageInfo info = pm.getPackageInfo(targetPackage, PackageManager.GET_META_DATA);
	    } catch (NameNotFoundException e) {
	        return false;
	    }
	    return true;    
	}
	
	@SuppressLint("NewApi")
	public void modif_srt(String Uri, String uriSrt,String path,int delay, int msdelay) {
		String line;
		int value;
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
		    try {
		    	if(Uri.endsWith("/media")){
		    		Uri=Uri.replace("/video/media", "");
		    		Uri=Uri.replace("content://media", "");
		    		Uri=Uri.replace(".mp4", "");
		    		Uri=Uri.replace(".wmv", "");
		    		Uri=Uri.replace(".m4a", "");
		    		Uri=Uri.replace(".mkv", "");
		    	}
		    	if(uriSrt.endsWith("/file")){
		    		uriSrt=uriSrt.replace("/file", "");
		    		uriSrt=uriSrt.replace("content://media", path);
		    	}
		    	if(uriSrt.endsWith(".ass")){
		    		uriSrt=convertAssToSrt(uriSrt);
		    	}
		    	FileInputStream input = new FileInputStream(uriSrt);
		    	FileOutputStream output = new FileOutputStream(path+Uri+".2.srt");
		    	DataInputStream in = new DataInputStream(input);
		    	DataOutputStream out = new DataOutputStream(output);
		    	BufferedReader br = new BufferedReader(new InputStreamReader(in));
		    	BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
		    	String regexp="[0-9]{2}:[0-9]{2}:[0-9]{2},[0-9]{3} --> [0-9]{2}:[0-9]{2}:[0-9]{2},[0-9]{3}";
		    	String AssRegexp="[0-9]{2}:[0-9]{2}:[0-9]{2},[0-9]{2} --> [0-9]{1}:[0-9]{2}:[0-9]{2},[0-9]{2}";
		    	String Secregexp="[0-9]{2}";
		    	String MilliSecregexp="[0-9]{3}";
		    	int jazz=0;
		    	while((line = br.readLine()) != null) {
		    		String newline=null;
				    if (line.matches(regexp)||line.matches(AssRegexp)){
				    	if(delay!=0 && msdelay !=0){
				    		jazz++;
				    		String badtime1 = line.substring(6, 8);
				    		String badtime2 = line.substring(23, 25);
				    		String badtime3 = line.substring(9, 12);
				    		String badtime4 = line.substring(26, 29);		
				    		if(badtime3.matches(MilliSecregexp) && badtime4.matches(MilliSecregexp) && badtime1.matches(Secregexp)&&badtime2.matches(Secregexp)){
				    			if(!toggle.isChecked()){
					    			int goodtime1 = Integer.parseInt(badtime1) + delay;
					    			int goodtime2 = Integer.parseInt(badtime2) + delay;	
					    			int goodtime3 = Integer.parseInt(badtime3) + msdelay;
					    			int goodtime4 = Integer.parseInt(badtime4) + msdelay;
					    			int newmintime3=Integer.parseInt(line.substring(3, 5));
					    			int newmintime4=Integer.parseInt(line.substring(20, 22));
					    			int newmintime1=goodtime1;
					    			int newmintime2=goodtime2;
					    			if(goodtime3>999){
							    		newmintime1++;
							    		goodtime3=goodtime3-1000;
					    			}
					    			if(goodtime4>999){
							    		newmintime2++;
					    				goodtime4=goodtime4-1000;
					    			}
					    			if(newmintime1>59){
					    				newmintime3++;
							    		goodtime1=newmintime1-60;
							    		newmintime1=goodtime1;
					    			}
					    			if(newmintime2>59){
					    				newmintime4++;
					    				goodtime2=newmintime2-60;
						    			newmintime2=goodtime2;
					    			}
					    			newline = line.substring(0, 3)+newmintime3+":"+newmintime1+","+String.valueOf(goodtime3)+line.substring(12, 20)+newmintime4+":"+newmintime2+","+String.valueOf(goodtime4);
					    			//newline = line.substring(0, 6)+String.valueOf(goodtime1)+","+String.valueOf(goodtime3)+line.substring(12, 23)+String.valueOf(goodtime2)+","+String.valueOf(goodtime4);
				    			}else{
					    			int goodtime1 = Integer.parseInt(badtime1) - delay;
					    			int goodtime2 = Integer.parseInt(badtime2) - delay;	
					    			int goodtime3 = Integer.parseInt(badtime3) - msdelay;
					    			int goodtime4 = Integer.parseInt(badtime4) - msdelay;
					    			int newmintime3=Integer.parseInt(line.substring(3, 5));
					    			int newmintime4=Integer.parseInt(line.substring(20, 22));
					    			int newmintime1=goodtime1;
					    			int newmintime2=goodtime2;
					    			if(goodtime3<0){
							    		newmintime1--;
							    		goodtime3=goodtime3+1000;
					    			}
					    			if(goodtime4<0){
							    		newmintime2--;
					    				goodtime4=goodtime4+1000;
					    			}
					    			if(newmintime1<0){
					    				newmintime3--;
							    		goodtime1=newmintime1+60;
							    		newmintime1=goodtime1;
					    			}
					    			if(newmintime2<0){
					    				newmintime4--;
					    				goodtime2=newmintime2+60;
						    			newmintime2=goodtime2;
					    			}
					    			newline = line.substring(0, 3)+newmintime3+":"+newmintime1+","+String.valueOf(goodtime3)+line.substring(12, 20)+newmintime4+":"+newmintime2+","+String.valueOf(goodtime4);
				    			}
				    		}
				    	}else if(delay!=0){
				    		jazz++;
				    		String badtime1 = line.substring(6, 8);
				    		String badtime2 = line.substring(23, 25);
				    		if(badtime1.matches(Secregexp)&&badtime2.matches(Secregexp)){
				    			if(!toggle.isChecked()){
					    			int goodtime1 = Integer.parseInt(badtime1) + delay;
					    			int goodtime2 = Integer.parseInt(badtime2) + delay;	
					    			
					    			int newmintime3=Integer.parseInt(line.substring(3, 5));
					    			int newmintime4=Integer.parseInt(line.substring(20, 22));
					    			int newmintime1=goodtime1;
					    			int newmintime2=goodtime2;
					    			
					    			if(newmintime1>59){
					    				newmintime3++;
							    		goodtime1=newmintime1-60;
							    		newmintime1=goodtime1;
					    			}
					    			if(newmintime2>59){
					    				newmintime4++;
					    				goodtime2=newmintime2-60;
						    			newmintime2=goodtime2;
					    			}
					    			newline = line.substring(0, 3)+newmintime3+":"+newmintime1+line.substring(8, 20)+newmintime4+":"+newmintime2+line.substring(25);
					    			//newline = line.substring(0, 6)+String.valueOf(goodtime1)+line.substring(8, 23)+String.valueOf(goodtime2)+line.substring(25);
				    			}else{

					    			int goodtime1 = Integer.parseInt(badtime1) - delay;
					    			int goodtime2 = Integer.parseInt(badtime2) - delay;
					    			if(goodtime1<0 && goodtime2<0){
							    		String mintime1 = line.substring(3, 5);
							    		int newmintime1 = Integer.parseInt(mintime1)-1;
							    		goodtime1=goodtime1+60;
							    		String mintime2 = line.substring(20, 22);
							    		int newmintime2 = Integer.parseInt(mintime2)-1;
					    				goodtime2=goodtime2+60;
					    				newline = line.substring(0, 3)+newmintime1+":"+String.valueOf(goodtime1)+line.substring(8, 20)+newmintime2+":"+String.valueOf(goodtime2)+line.substring(25);
					    			}else if(goodtime1<0){
							    		String mintime1 = line.substring(3, 5);
							    		int newmintime1 = Integer.parseInt(mintime1)-1;
							    		goodtime1=goodtime1+60;
						    			newline = line.substring(0, 3)+newmintime1+":"+String.valueOf(goodtime1)+line.substring(8, 23)+String.valueOf(goodtime2)+line.substring(25);
						    			
					    			}else if(goodtime2<0){
							    		String mintime2 = line.substring(20, 22);
							    		int newmintime2 = Integer.parseInt(mintime2)-1;
					    				goodtime2=goodtime2+60;
						    			newline = line.substring(0, 6)+String.valueOf(goodtime1)+line.substring(8, 20)+newmintime2+":"+String.valueOf(goodtime2)+line.substring(25);

					    			}else{
						    			newline = line.substring(0, 6)+String.valueOf(goodtime1)+line.substring(8, 23)+String.valueOf(goodtime2)+line.substring(25);

					    			}

				    			}
				    		}
				    	}else if(msdelay!=0){

				    		jazz++;
				    		String badtime1 = line.substring(9, 12);
				    		String badtime2 = line.substring(26, 29);
				    		if(badtime1.matches(MilliSecregexp)&&badtime2.matches(MilliSecregexp)){
				    			if(!toggle.isChecked()){
						    		int mintime1 = Integer.parseInt(line.substring(6,8));
						    		int mintime2 = Integer.parseInt(line.substring(23, 25));
					    			int goodtime1 = Integer.parseInt(badtime1) + msdelay;
					    			int goodtime2 = Integer.parseInt(badtime2) + msdelay;	
					    			if(goodtime1>999){
					    				mintime1++;
					    				goodtime1=goodtime1-1000;
					    			}
					    			if(goodtime2>999){
					    				mintime2++;
					    				goodtime2=goodtime2-1000;
					    			}
					    			newline = line.substring(0, 6)+mintime1+","+String.valueOf(goodtime1)+line.substring(12, 23)+mintime2+","+String.valueOf(goodtime2);
					    			//newline = line.substring(0, 9)+String.valueOf(goodtime1)+line.substring(12, 26)+String.valueOf(goodtime2);

				    			}else{

					    			int goodtime1 = Integer.parseInt(badtime1) - msdelay;
					    			int goodtime2 = Integer.parseInt(badtime2) - msdelay;
					    			if(goodtime1<0 && goodtime2<0){
							    		String mintime1 = line.substring(6,8);
							    		int newmintime1 = Integer.parseInt(mintime1)-1;
							    		goodtime1=goodtime1+1000;
							    		String mintime2 = line.substring(23, 25);
							    		int newmintime2 = Integer.parseInt(mintime2)-1;
					    				goodtime2=goodtime2+1000;
					    				newline = line.substring(0, 6)+newmintime1+","+String.valueOf(goodtime1)+line.substring(12, 23)+newmintime2+","+String.valueOf(goodtime2);

					    			}else if(goodtime1<0){
							    		String mintime1 = line.substring(6, 8);
							    		int newmintime1 = Integer.parseInt(mintime1)-1;
							    		goodtime1=goodtime1+1000;
						    			newline = line.substring(0, 6)+newmintime1+","+String.valueOf(goodtime1)+line.substring(12, 26)+String.valueOf(goodtime2);

					    			}else if(goodtime2<0){
							    		String mintime2 = line.substring(23, 25);
							    		int newmintime2 = Integer.parseInt(mintime2)-1;
					    				goodtime2=goodtime2+1000;
						    			newline = line.substring(0, 9)+String.valueOf(goodtime1)+line.substring(12, 23)+newmintime2+","+String.valueOf(goodtime2);

					    			}else{
						    			newline = line.substring(0, 9)+String.valueOf(goodtime1)+line.substring(12, 26)+String.valueOf(goodtime2);

					    			}
				    			}
				    		}
				    	}
				    	bw.write(newline+"\n");	
			    	}else{
			    		bw.write(line+"\n");

			    	}
			    }
		    	if(input != null)input.close();
		    	if(output != null)output.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{Toast.makeText(getApplicationContext(), "No external card mount", Toast.LENGTH_LONG).show();}
	}
	
	
	 @Override
	    public boolean onCreateOptionsMenu(Menu menu) {
	    	
	    	MenuInflater inflater = getMenuInflater();
	        // Inflate the menu; this adds items to the action bar if it is present.
	    	inflater.inflate(R.menu.main, menu);
	        return super.onCreateOptionsMenu(menu);
	        }
	            
	    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
		@SuppressLint("NewApi")
		@Override
	    public boolean onOptionsItemSelected (MenuItem item)
	    {
	        switch(item.getItemId())
	        {
	          case R.id.action_settings:
			
				            //Dans le Menu "m", on active tous les items dans le groupe d'identifiant "R.id.group2"
				        	AlertDialog.Builder box = new AlertDialog.Builder(this);  
				        	int checkedView;
				        	if(View.equals("selectpath")||View.equals("selectpath_ms"))
				        	{
				        		checkedView=1;
				        	}else{
				        		checkedView=0;
				        	}
				        	box.setTitle("LANGUAGE").setSingleChoiceItems(items,checkedView,new DialogInterface.OnClickListener(){
				        	            public void onClick(DialogInterface dialog, int whichButton) {
				        	              if(items[whichButton]=="FR"){
				        	            	  setContentView(R.layout.selectpath_fr);		
				        	            	  View="selectpath_fr";
				        	            	  i1.putExtra("VIEW",View);
				        	            	  onResume();
				        	            	  
				        	              }else{
				        	            	  setContentView(R.layout.selectpath);
				        	            	  View="selectpath";
				        	            	  i1.putExtra("VIEW",View);
				        	            	  onResume();
				        	              }
				        	            }
				        	        });
				        	box.show();
				        	return true;
			        	
	         case R.id.video:
			            	Intent intent = new Intent(getApplicationContext(),affich_video.class);
			            	intent.putExtra("URISRT",i1.getStringExtra("URISRT"));
			            	intent.putExtra("DELAY",picker.getValue());
			            	intent.putExtra("SWITCH",toggle.isChecked());
			            	intent.putExtra("VIEW",View);
			            	startActivity(intent);
	          	          	return true;
	          	          	
	        case R.id.srt:
			            	Intent intent2 = new Intent(getApplicationContext(),affich_srt.class);
			            	intent2.putExtra("URI", i1.getStringExtra("URI"));
			            	intent2.putExtra("DELAY",picker.getValue());
			            	intent2.putExtra("SWITCH",toggle.isChecked());
			            	intent2.putExtra("VIEW",View);
							startActivity(intent2);
					        return true;
	
	        case R.id.path:
				        	Intent intent3 = new Intent(getApplicationContext(),selectPath.class);
				        	intent3.putExtra("URI", i1.getStringExtra("URI"));
				        	intent3.putExtra("DELAY",picker.getValue());
				        	intent3.putExtra("SWITCH",toggle.isChecked());
				        	intent3.putExtra("VIEW",View);
							startActivity(intent3);
					        return true;

	        }
	        return super.onOptionsItemSelected(item);
	      }
	    
		@Override
		protected void onResume(){
			super.onResume();
			this.onCreate(null);
		}
		
		public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuinfo) {
			
			  super.onCreateContextMenu(menu, v, menuinfo);
			  menu.add(Menu.NONE, LANGUAGE, Menu.NONE, "Language");
			  menu.add(Menu.NONE, VIDEO, Menu.NONE, "Video");
			  menu.add(Menu.NONE, SRT, Menu.NONE, "Srt");

		  }
		  
		
		
		  @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
		@SuppressLint("NewApi")
		@Override
		  public boolean onContextItemSelected(MenuItem item) {
			  
		    
		    switch (item.getItemId()) {
			    		
			    case LANGUAGE:
		            //Dans le Menu "m", on active tous les items dans le groupe d'identifiant "R.id.group2"
			        	AlertDialog.Builder box = new AlertDialog.Builder(this);  
			        	int checkedView;
			        	if(View.equals("selectpath"))
			        	{
			        		checkedView=1;
			        	}else{
			        		checkedView=0;
			        	}
			        	box.setTitle("LANGUAGE").setSingleChoiceItems(items,checkedView,new DialogInterface.OnClickListener(){
			        		 public void onClick(DialogInterface dialog, int whichButton) {
		        	              if(items[whichButton]=="FR"){
		        	            	  setContentView(R.layout.selectpath_fr);		
		        	            	  View="selectpath_fr";
		        	            	  i1.putExtra("VIEW",View);
		        	            	  onResume();
		        	            	  
		        	              }else{
		        	            	  setContentView(R.layout.selectpath);
		        	            	  View="selectpath";
		        	            	  i1.putExtra("VIEW",View);
		        	            	  onResume();
		        	              }
		        	            }
		        	        });
			        	box.show();
			        	return true;
			     	 	
			    case VIDEO:
		            	Intent intent = new Intent(getApplicationContext(),affich_video.class);
		            	intent.putExtra("URISRT",i1.getStringExtra("URISRT"));
		            	intent.putExtra("DELAY",picker.getValue());
		            	intent.putExtra("SWITCH",toggle.isChecked());
		            	intent.putExtra("VIEW",View);
		            	startActivity(intent);
	      	          	return true;
		     	 	
			    case SRT:
		            	Intent intent2 = new Intent(getApplicationContext(),affich_srt.class);
		            	intent2.putExtra("URI", i1.getStringExtra("URI"));
		            	intent2.putExtra("DELAY",picker.getValue());
		            	intent2.putExtra("SWITCH",toggle.isChecked());
		            	intent2.putExtra("VIEW",View);
						startActivity(intent2);
				        return true;

		    }	
		    return super.onContextItemSelected(item);
		  }
		  
		    public void onConfigurationChanged(Configuration newConfig){
		    	super.onConfigurationChanged(newConfig);
		            onResume();
			}
		    
		    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
			@SuppressLint("NewApi")
			public void populate2(){
		    	populate();
		    	
		 	    lbl_1=(TextView)findViewById(R.id.lbl_1);
	    		if((View.equals("selectpath")||View.equals("selectpath_fr")) && getResources().getConfiguration().screenLayout == Configuration.SCREENLAYOUT_SIZE_UNDEFINED )lbl_1.setTextSize(50+textsize1);
		 	    lbl_1.setTextColor(Color.GRAY);
		 	    lbl_1.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		 	    
		 		lbl_2=(TextView)findViewById(R.id.lbl_2);
	    		if((View.equals("selectpath")||View.equals("selectpath_fr")) && getResources().getConfiguration().screenLayout == Configuration.SCREENLAYOUT_SIZE_UNDEFINED )lbl_2.setTextSize(50+textsize1);
		 	    lbl_2.setTextColor(Color.GRAY);
		 	    lbl_2.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		 	    
		 		lbl_3=(TextView)findViewById(R.id.lbl_3);
	    		if((View.equals("selectpath")||View.equals("selectpath_fr")) && getResources().getConfiguration().screenLayout == Configuration.SCREENLAYOUT_SIZE_UNDEFINED )lbl_3.setTextSize(50+textsize1);
		 	    lbl_3.setTextColor(Color.GRAY);
		 	    lbl_3.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		 	    
		        registerForContextMenu(lbl_1);
		        registerForContextMenu(lbl_2);
		        registerForContextMenu(lbl_3);
		    }
		    
		    
		    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
			@SuppressLint("NewApi")
			public void populate(){
		    	 RelativeLayout rl = (RelativeLayout)findViewById(R.id.selectPathLayout);
		 	    rl.setBackgroundColor(Color.BLACK);
		 	    textsize1=15;
		 	    lbl_video=(TextView)findViewById(R.id.lbl_Chooseavideo);
		 	    lbl_video.setTextColor(Color.LTGRAY);
		 		pathvideo = (TextView)findViewById(R.id.pathvideo);
		 		if(i1.getStringExtra("URI")!=null){
		 			pathvideo.setText(i1.getStringExtra("URI"));
		 			pathvideo.setTextColor(Color.LTGRAY);
		 		}
		 		lbl_Srt=(TextView)findViewById(R.id.lbl_Choosesrt);
		 	    lbl_Srt.setTextColor(Color.LTGRAY);
		 		pathSrt = (TextView)findViewById(R.id.pathsrt);
		 		if(i1.getStringExtra("URISRT")!=null){
		 			pathSrt.setText(i1.getStringExtra("URISRT"));
		 			pathSrt.setTextColor(Color.LTGRAY);
		 		}
		 		setVideo=(Button)findViewById(R.id.btn_video);
		 		setSrt=(Button)findViewById(R.id.btn_srt);
		 		Test=(Button)findViewById(R.id.btn_test);
		 		lbl_time=(TextView)findViewById(R.id.lbl_setdelay);
		 		lbl_time.setTextColor(Color.LTGRAY);
		 		toggle = (Switch)findViewById(R.id.switch1);
		 		if(i1.getBooleanExtra("SWITCH",false)){
		 			toggle.setChecked(i1.getBooleanExtra("SWITCH", false));
		 		}
		 		toggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		 		    @SuppressLint("NewApi")
		 			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		 		        if(toggle.isChecked()){
		 		        	if(!View.equals("selectpath")&&!View.equals("selectpath_ms"))Toast.makeText(getApplicationContext(), "Les sous titres seront avancé par rapport à la video", Toast.LENGTH_LONG).show();
		 		        	else Toast.makeText(getApplicationContext(), "Subtitles will be advanced compared to video", Toast.LENGTH_LONG).show();
		 		        }else{
		 		        	if(!View.equals("selectpath")&&!View.equals("selectpath_ms"))Toast.makeText(getApplicationContext(), "Les sous titres seront reculé par rapport à la video", Toast.LENGTH_LONG).show();
		 		        	else Toast.makeText(getApplicationContext(), "Subtitles will be decreased compared to video", Toast.LENGTH_LONG).show();
		 		        }
		 		        i1.putExtra("SWITCH",toggle.isChecked());
		 		    }
		 		});
		 		picker=(NumberPicker) findViewById(R.id.timePicker1);
		 		if(i1.getIntExtra("DELAY", 0)!=0){
		 			picker.setValue(i1.getIntExtra("DELAY",0));
		 		}
		 		picker.setBackgroundColor(Color.DKGRAY);
		 		picker.setMaxValue(59);
		 		if(!View.equals("selectpath")&&!View.equals("selectpath_fr")){
					picker1=(NumberPicker) findViewById(R.id.timePicker2);
			 		picker1.setBackgroundColor(Color.DKGRAY);
			 		picker1.setMaxValue(9);		 		
					picker2=(NumberPicker) findViewById(R.id.timePicker3);
			 		picker2.setBackgroundColor(Color.DKGRAY);
					picker3=(NumberPicker) findViewById(R.id.timePicker4);
			 		picker3.setBackgroundColor(Color.DKGRAY);
					picker2.setMaxValue(9);
					picker3.setMaxValue(9);
					lbl_millisec=(TextView)findViewById(R.id.lbl_millisecond);
					lbl_millisec.setTextColor(Color.LTGRAY);
		 		}
		 		lbl_sec=(TextView)findViewById(R.id.lbl_second);
		 		lbl_sec.setTextColor(Color.LTGRAY);

		 		setVideo.setOnClickListener(new View.OnClickListener() {
		             @SuppressLint("NewApi")
					@Override
		             public void onClick(View v) {
		             	Intent intent = new Intent(getApplicationContext(),affich_video.class);
		             	intent.putExtra("URISRT",i1.getStringExtra("URISRT"));
		             	intent.putExtra("DELAY",picker.getValue());
		             	intent.putExtra("SWITCH",toggle.isChecked());
		             	intent.putExtra("VIEW",View);

		 				startActivity(intent);
		     		}
		             
		 		});
		 		setSrt.setOnClickListener(new View.OnClickListener() {
		             @SuppressLint("NewApi")
					@Override
		             public void onClick(View v) {
		             	Intent intent = new Intent(getApplicationContext(),affich_srt.class);
		             	intent.putExtra("URI", i1.getStringExtra("URI"));
		             	intent.putExtra("DELAY",picker.getValue());
		             	intent.putExtra("SWITCH",toggle.isChecked());
		             	intent.putExtra("VIEW",View);
		 				startActivity(intent);
		     			}
		             
		 		});
		 		Test.setOnClickListener(new View.OnClickListener(){
		 			
		 			public void onClick(View v) {
		 				if(!String.valueOf(pathvideo.getText()).trim().equals("")&&!String.valueOf(pathSrt.getText()).trim().equals("")){
		 					if(View.equals("selectpath_ms")||View.equals("selectpath_ms_fr")){
			 					modif_srt(String.valueOf(pathvideo.getText()),
			 							String.valueOf(pathSrt.getText()),
			 							i1.getStringExtra("PATHSRT"),
			 							picker.getValue(),
			 							Integer.parseInt(String.valueOf(picker1.getValue()).concat(String.valueOf(picker2.getValue())).concat(String.valueOf(picker3.getValue()))));
			 				}else{
			 					modif_srt(String.valueOf(pathvideo.getText()),
			 							String.valueOf(pathSrt.getText()),
			 							i1.getStringExtra("PATHSRT"),
			 							picker.getValue(),
			 							0);
			 				}
		 					if(doesPackageExist("org.videolan.vlc.betav7neon")){
		 						Intent LaunchIntent = new Intent("android.intent.action.MAIN");
		 						startApplication("org.videolan.vlc.betav7neon");
		 					}else{
		 						if(!View.equals("selectpath")&&!View.equals("selectpath_ms"))Toast.makeText(getApplicationContext(), "Veuillez Installer VLC", Toast.LENGTH_LONG).show();
		 						else Toast.makeText(getApplicationContext(), "Please install VLCBeta For android", Toast.LENGTH_LONG).show();
		 						showInMarket("org.videolan.vlc.betav7neon");
		 					}
		 				}else{
		 					if(!View.equals("selectpath")&&!View.equals("selectpath_ms"))Toast.makeText(getApplicationContext(), "Pas de video ou de sous titres selectionner", Toast.LENGTH_LONG).show();
		 					else Toast.makeText(getApplicationContext(), "No Video or Subtitle selected", Toast.LENGTH_LONG).show();}
		 			}

		 		});
		        registerForContextMenu(lbl_video);
		        registerForContextMenu(pathvideo);
		        registerForContextMenu(lbl_Srt);
		        registerForContextMenu(pathSrt);
		        registerForContextMenu(lbl_time);
		        registerForContextMenu(lbl_sec);
		    }
		    
		    
		    public String convertAssToSrt(String srt){
		    	
		    	String line;
		    	String newSrt = srt.replace(".ass", ".srt");
		    	try {
			    	FileInputStream input = new FileInputStream(srt);
			    	FileOutputStream output = new FileOutputStream(newSrt);
			    	DataInputStream in = new DataInputStream(input);
			    	DataOutputStream out = new DataOutputStream(output);
			    	BufferedReader br = new BufferedReader(new InputStreamReader(in));
			    	BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
			    	int jazz=0;
					while((line = br.readLine()) != null) {
						if(line.startsWith("Dialogue:")){
							jazz++;
							String[] tab = line.split(",");
							if(tab.length>9){
								for(int i=10;i<tab.length;i++){
									tab[9]+=","+tab[i];
								}
							}
							line=String.valueOf(jazz)+"\n";
							bw.write(line);
							tab[1]=tab[1].replace(".", ",");
							tab[2]=tab[2].replace(".", ",");
							line="0"+tab[1]+"0 --> 0"+tab[2]+"0\n";
							bw.write(line);
							line=tab[9]+"\n";
							bw.write(line+"\n");
						}
					}
	        		//br.close();
			    	if(input != null)input.close();
			    	if(output != null)output.close();
					//Toast.makeText(getApplicationContext(), srt+" CONVERT into : "+newSrt, Toast.LENGTH_LONG).show();
			    	//i1.putExtra("URISRT", newSrt);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

		    	return newSrt;

		    }
}
