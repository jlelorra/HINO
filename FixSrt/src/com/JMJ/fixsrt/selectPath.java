package com.JMJ.fixsrt;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class selectPath extends Activity {
	String UriVideo;
	String UriSrt;
	EditText pathvideo;
	EditText pathSrt;
	Button setVideo;
	Button setSrt;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		pathvideo = (EditText)findViewById(R.id.pathvideo);
		pathSrt = (EditText)findViewById(R.id.pathsrt);
		setVideo=(Button)findViewById(R.id.btn_video);
		setSrt=(Button)findViewById(R.id.btn_srt);
		
		
		
	}

}
