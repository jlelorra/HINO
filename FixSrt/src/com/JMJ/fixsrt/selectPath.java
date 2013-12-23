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
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.DisplayMetrics;
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
	TextView lbl_time;
	TextView lbl_1;
	TextView lbl_2;
	TextView lbl_3;
	Button setVideo;
	Button setSrt;
	Button Test;
	Cursor cursor;
	NumberPicker picker;
	Switch toggle;
	public String pathh;
	public List<String> path = null;
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
		if(i1.getStringExtra("VIEW")!=null)View=i1.getStringExtra("VIEW");
		if(View.equals("selectpath")){
			setContentView(R.layout.selectpath);
		}else{
		    setContentView(R.layout.selectpath_fr);
		}
	    RelativeLayout rl = (RelativeLayout)findViewById(R.id.selectPathLayout);
	    rl.setBackgroundColor(Color.BLACK);
	    DisplayMetrics displaymetrics = new DisplayMetrics();
	    getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
	    screenHeight = (int) displaymetrics.xdpi;
	    screenWidth = (int) displaymetrics.ydpi;
	    /*screendensity = displaymetrics.densityDpi;
	    switch(displaymetrics.densityDpi){
	
		    case DisplayMetrics.DENSITY_XXXHIGH:
		    	textsize1=35;
		        break;
		        
		    case DisplayMetrics.DENSITY_XXHIGH:
		    	textsize1=30;
		        break;
		        
		    case DisplayMetrics.DENSITY_XHIGH:
		    	textsize1=25;
		        break;
		        
		    case DisplayMetrics.DENSITY_HIGH:
		    	textsize1=20;
		        break;
	
		    case DisplayMetrics.DENSITY_MEDIUM:
		         textsize1=10;
		         break;
	
		    case DisplayMetrics.DENSITY_LOW:
		         textsize1=5;
			     break;
	    }*/
        textsize1=(screenWidth/screenHeight)*15;
	    Log.d("SIZE",String.valueOf(textsize1));
	    lbl_1=(TextView)findViewById(R.id.lbl_1);
	    lbl_1.setTextSize(50+textsize1);
	    lbl_1.setTextColor(Color.GRAY);
	    lbl_1.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
	    lbl_video=(TextView)findViewById(R.id.lbl_Chooseavideo);
	    lbl_video.setTextColor(Color.LTGRAY);
	    lbl_video.setTextSize(1*textsize1);
		pathvideo = (TextView)findViewById(R.id.pathvideo);
		if(i1.getStringExtra("URI")!=null){
			pathvideo.setText(i1.getStringExtra("URI"));
			pathvideo.setTextColor(Color.LTGRAY);
			pathvideo.setTextSize(textsize1/2);
		}
	    lbl_2=(TextView)findViewById(R.id.lbl_2);
	    lbl_2.setTextSize(50+textsize1);
	    lbl_2.setTextColor(Color.GRAY);
	    lbl_2.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		lbl_Srt=(TextView)findViewById(R.id.lbl_Choosesrt);
	    lbl_Srt.setTextColor(Color.LTGRAY);
	    lbl_Srt.setTextSize(1*textsize1);
		pathSrt = (TextView)findViewById(R.id.pathsrt);
		if(i1.getStringExtra("URISRT")!=null){
			pathSrt.setText(i1.getStringExtra("URISRT"));
			pathSrt.setTextColor(Color.LTGRAY);
			pathSrt.setTextSize(textsize1/2);
		}
		setVideo=(Button)findViewById(R.id.btn_video);
		setSrt=(Button)findViewById(R.id.btn_srt);
		Test=(Button)findViewById(R.id.btn_test);
	    lbl_3=(TextView)findViewById(R.id.lbl_3);
	    lbl_3.setTextSize(50+textsize1);
	    lbl_3.setTextColor(Color.GRAY);
	    lbl_3.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		lbl_time=(TextView)findViewById(R.id.lbl_setdelay);
		lbl_time.setTextColor(Color.LTGRAY);
		lbl_time.setTextSize(1*textsize1);
		toggle = (Switch)findViewById(R.id.switch1);
		toggle.setWidth(screenWidth*toggle.getWidth());
		toggle.setHeight(screenHeight*toggle.getHeight());
		if(i1.getBooleanExtra("SWITCH",false)){
			toggle.setChecked(i1.getBooleanExtra("SWITCH", false));
		}
		toggle.setOnCheckedChangeListener(new OnCheckedChangeListener() {
		    @SuppressLint("NewApi")
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		        if(toggle.isChecked()){
		        	if(!View.equals("selectpath"))Toast.makeText(getApplicationContext(), "Les sous titres seront avancé par rapport à la video", Toast.LENGTH_LONG).show();
		        	else Toast.makeText(getApplicationContext(), "Subtitles will be advanced compared to video", Toast.LENGTH_LONG).show();
		        }else{
		        	if(!View.equals("selectpath"))Toast.makeText(getApplicationContext(), "Les sous titres seront reculé par rapport à la video", Toast.LENGTH_LONG).show();
		        	else Toast.makeText(getApplicationContext(), "Subtitles will be decreased compared to video", Toast.LENGTH_LONG).show();
		        }
		    }
		});
		picker=(NumberPicker) findViewById(R.id.timePicker1);
		if(i1.getIntExtra("DELAY", 0)!=0){
			picker.setValue(i1.getIntExtra("DELAY",0));
		}
		picker.setBackgroundColor(Color.DKGRAY);
		picker.setMaxValue(59);
		lbl_sec=(TextView)findViewById(R.id.lbl_second);
		lbl_sec.setTextColor(Color.LTGRAY);
		setVideo.setOnClickListener(new View.OnClickListener() {
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
					modif_srt(String.valueOf(pathvideo.getText()),String.valueOf(pathSrt.getText()),i1.getStringExtra("PATHSRT"),picker.getValue());
					if(doesPackageExist("org.videolan.vlc.betav7neon")){
						Intent LaunchIntent = new Intent("android.intent.action.MAIN");
						/*LaunchIntent.addCategory("android.intent.category.LAUNCHER");
						LaunchIntent = getPackageManager().getLaunchIntentForPackage("org.videolan.vlc.betav7neon");
						LaunchIntent.setDataAndType(Uri.parse(i1.getStringExtra("URI")), "video/*");
						LaunchIntent.setDataAndType(Uri.parse(i1.getStringExtra("URISRT")), "file/*");
						startActivity(LaunchIntent);*/
						startApplication("org.videolan.vlc.betav7neon");
					}else{
						Toast.makeText(getApplicationContext(), "Please install VLCBeta For android", Toast.LENGTH_LONG).show();
						//startApplication("org.videolan.vlc.betav7neon");
						showInMarket("org.videolan.vlc.betav7neon");
					}
				}else{Toast.makeText(getApplicationContext(), "No Video or Subtitle selected", Toast.LENGTH_LONG).show();}
			}

		});
        registerForContextMenu(lbl_1);
        registerForContextMenu(lbl_video);
        registerForContextMenu(pathvideo);
        registerForContextMenu(lbl_2);
        registerForContextMenu(lbl_Srt);
        registerForContextMenu(pathSrt);
        registerForContextMenu(lbl_3);
        registerForContextMenu(lbl_time);
        registerForContextMenu(lbl_sec);
		
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
	    intent.setDataAndType(Uri.parse(i1.getStringExtra("URI")), "video/*");
	    intent.setDataAndType(Uri.parse(i1.getStringExtra("URISRT")), "file/*");
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
	public void modif_srt(String Uri, String uriSrt,String path,int delay) {
		String line;
		int value;
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
		    try {
		    	if(Uri.endsWith("/media")){
		    		Uri=Uri.replace(".mp4/video/media", "");
		    		Uri=Uri.replace("content://media", "");
		    		Uri=Uri.replace(".mp4", "");
		    		Log.d("URI",path+Uri+".2.srt");
		    	}
		    	if(uriSrt.endsWith("/file"))
		    	{
		    		uriSrt=uriSrt.replace("/file", "");
		    		uriSrt=uriSrt.replace("content://media", path);
	    			Log.d("URISRT",uriSrt);
		    	}
		    	FileInputStream input = new FileInputStream(uriSrt);
		    	FileOutputStream output = new FileOutputStream(path+Uri+".2.srt");
		    	DataInputStream in = new DataInputStream(input);
		    	DataOutputStream out = new DataOutputStream(output);
		    	BufferedReader br = new BufferedReader(new InputStreamReader(in));
		    	BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
		    	String regexp="[0-9]{2}:[0-9]{2}:[0-9]{2},[0-9]{3} --> [0-9]{2}:[0-9]{2}:[0-9]{2},[0-9]{3}";
		    	String Secregexp="[0-9]{2}";
		    	//String testregexp="[0-9]{1}";
		    	int jazz=0;
		    	while((line = br.readLine()) != null) {

				    if (line.matches(regexp)){
				    	if(delay>0){
				    		Log.d("VALUE",line);
				    		jazz++;
				    		String badtime1 = line.substring(6, 8);
				    		String badtime2 = line.substring(23, 25);
				    		if(badtime1.matches(Secregexp)&&badtime2.matches(Secregexp)){
				    			if(!toggle.isChecked()){
					    			int goodtime1 = Integer.parseInt(badtime1) + delay;
					    			int goodtime2 = Integer.parseInt(badtime2) + delay;					    			
					    			String newline = line.substring(0, 6)+String.valueOf(goodtime1)+line.substring(8, 23)+String.valueOf(goodtime2)+line.substring(25);
					    			bw.write(newline+"\n");	
					    			Log.d("NEWLINE",newline);
				    			}else{
					    			int goodtime1 = Integer.parseInt(badtime1) - delay;
					    			int goodtime2 = Integer.parseInt(badtime2) - delay;
					    			if(goodtime1<0){
							    		String mintime1 = line.substring(3, 5);
							    		int newmintime1 = Integer.parseInt(mintime1)-1;
							    		//goodtime1=goodtime1+60;
					    			}
					    			if(goodtime2<0){
							    		String mintime2 = line.substring(20, 22);
							    		int newmintime2 = Integer.parseInt(mintime2)-1;
					    				//goodtime2=goodtime2+60;
					    			}
					    			String newline = line.substring(0, 6)+String.valueOf(goodtime1)+line.substring(8, 23)+String.valueOf(goodtime2)+line.substring(25);
					    			bw.write(newline+"\n");	
					    			Log.d("NEWLINE",newline);
				    			}
				    		}
				    		
				    	}else{bw.write(line+"\n");}
			    	}else{
			    		bw.write(line+"\n");
			    		Log.d("VALUE2",line);
			    	}
			    }
    			Log.d("NB MODIF",String.valueOf(jazz));
		    	if(input != null)input.close();
		    	if(output != null)output.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
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
				        	            	  onResume();
				        	            	  
				        	              }else{
				        	            	  setContentView(R.layout.selectpath);
				        	            	  View="selectpath";
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
			        	            	  onResume();
			        	              }else{
			        	            	  setContentView(R.layout.selectpath);
			        	            	  View="selectpath";
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
	
}
