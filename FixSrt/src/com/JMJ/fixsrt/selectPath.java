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
import android.content.ComponentName;
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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class selectPath extends Activity {
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
	
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    setContentView(R.layout.selectpath);
	    i1 = getIntent();
	    RelativeLayout rl = (RelativeLayout)findViewById(R.id.selectPathLayout);
	    rl.setBackgroundColor(Color.BLACK);
	    lbl_1=(TextView)findViewById(R.id.lbl_1);
	    lbl_1.setTextSize(50);
	    lbl_1.setTextColor(Color.GRAY);
	    lbl_1.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
	    lbl_video=(TextView)findViewById(R.id.lbl_Chooseavideo);
	    lbl_video.setTextColor(Color.LTGRAY);
		pathvideo = (TextView)findViewById(R.id.pathvideo);
		if(i1.getStringExtra("URI")!=null){
			pathvideo.setText(i1.getStringExtra("URI"));
			pathvideo.setTextColor(Color.LTGRAY);
			pathvideo.setTextSize(8);
		}
	    lbl_2=(TextView)findViewById(R.id.lbl_2);
	    lbl_2.setTextSize(50);
	    lbl_2.setTextColor(Color.GRAY);
	    lbl_2.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		lbl_Srt=(TextView)findViewById(R.id.lbl_Choosesrt);
	    lbl_Srt.setTextColor(Color.LTGRAY);
		pathSrt = (TextView)findViewById(R.id.pathsrt);
		if(i1.getStringExtra("URISRT")!=null){
			pathSrt.setText(i1.getStringExtra("URISRT"));
			pathSrt.setTextColor(Color.LTGRAY);
			pathSrt.setTextSize(8);
		}
		setVideo=(Button)findViewById(R.id.btn_video);
		setSrt=(Button)findViewById(R.id.btn_srt);
		Test=(Button)findViewById(R.id.btn_test);
	    lbl_3=(TextView)findViewById(R.id.lbl_3);
	    lbl_3.setTextSize(50);
	    lbl_3.setTextColor(Color.GRAY);
	    lbl_3.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
		lbl_time=(TextView)findViewById(R.id.lbl_setdelay);
		lbl_time.setTextColor(Color.LTGRAY);
		toggle = (Switch)findViewById(R.id.switch1);
		picker=(NumberPicker) findViewById(R.id.timePicker1);
		if(i1.getIntExtra("DELAY", 0)!=0){
			picker.setValue(i1.getIntExtra("DELAY",0));
		}
		picker.setBackgroundColor(Color.DKGRAY);
		picker.setMaxValue(59);
		lbl_sec=(TextView)findViewById(R.id.lbl_second);
		lbl_sec.setTextColor(Color.LTGRAY);
		setVideo.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
			@Override
            public void onClick(View v) {
            	Intent intent = new Intent(getApplicationContext(),affich_video.class);
            	intent.putExtra("URISRT",i1.getStringExtra("URISRT"));
            	intent.putExtra("DELAY",i1.getIntExtra("DELAY",0));
				startActivity(intent);
    			}
            
		});
		setSrt.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
			@Override
            public void onClick(View v) {
            	Intent intent = new Intent(getApplicationContext(),affich_srt.class);
            	intent.putExtra("URI", i1.getStringExtra("URI"));
            	intent.putExtra("DELAY",i1.getIntExtra("DELAY",0));
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
				    			if(String.valueOf(toggle.getText()).equals("+")){
					    			int goodtime1 = Integer.parseInt(badtime1) + delay;
					    			int goodtime2 = Integer.parseInt(badtime2) + delay;					    			
					    			String newline = line.substring(0, 6)+String.valueOf(goodtime1)+line.substring(8, 23)+String.valueOf(goodtime2)+line.substring(25);
					    			bw.write(newline+"\n");	
					    			Log.d("NEWLINE",newline);
				    			}else{
					    			int goodtime1 = Integer.parseInt(badtime1) - delay;
					    			int goodtime2 = Integer.parseInt(badtime2) - delay;
					    			if(goodtime1<0){goodtime1=0;}
					    			if(goodtime2<0){goodtime2=0;}
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
	
}
