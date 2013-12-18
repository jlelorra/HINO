package com.JMJ.fixsrt;

import java.util.List;

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
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
	public String pathh;
	public List<String> path = null;
	Intent i1;
	
	
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
		picker=(NumberPicker) findViewById(R.id.timePicker1);
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
				startActivity(intent);
    			}
            
		});
		setSrt.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
			@Override
            public void onClick(View v) {
            	Intent intent = new Intent(getApplicationContext(),affich_srt.class);
            	intent.putExtra("URI", i1.getStringExtra("URI"));
				startActivity(intent);
    			}
            
		});
		Test.setOnClickListener(new View.OnClickListener(){
			
			public void onClick(View v) {
				if(doesPackageExist("org.videolan.vlc.betav7neon")){
					startApplication("org.videolan.vlc.betav7neon");
				}else{
					startApplication("com.android.sec.gallery3d");
				}
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

	        // No match, so application is not installed
	        showInMarket(packageName);
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
	
}
