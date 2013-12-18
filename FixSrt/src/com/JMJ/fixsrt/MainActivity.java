package com.JMJ.fixsrt;

import java.util.Date;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

public class MainActivity extends Activity {
	VideoView player;
	Thread myThread;
	 int mHours;
	 int mMinutes;
	 int mSeconds;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	    requestWindowFeature(Window.FEATURE_NO_TITLE);
	    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
	    setContentView(R.layout.activity_main);

	    player = (VideoView)findViewById(R.id.videoView1);
	    Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath() +"/American.Horror.Story.S03E09.HDTV.x264-ASAP.mp4");
	    Log.d("URI CONTENT",Environment.getExternalStorageDirectory().getPath() +"/American.Horror.Story.S03E09.HDTV.x264-ASAP.mp4");

	    @SuppressWarnings("deprecation")
		Date dt = new Date();
	    mHours = dt.getHours();
	    mMinutes = dt.getMinutes();
	    mSeconds = dt.getSeconds();
	    String curTime = mHours + ":"+ mMinutes + ":" + mSeconds;

	    player.setVideoPath(Environment.getExternalStorageDirectory().getPath() +"/American.Horror.Story.S03E09.HDTV.x264-ASAP");
	    //player.setMediaController(new MediaController(this));
	    player.start();

	    Runnable runnable = new CountDownRunner();
	    myThread= new Thread(runnable);   
	    myThread.start();

	    player.setOnPreparedListener(new OnPreparedListener() {
	        public void onPrepared(MediaPlayer mp) {
	            Log.i("TAG", "On Prepared");
	        }
	    });

	    player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
	        @SuppressWarnings("deprecation")
			@Override
	        public void onCompletion(MediaPlayer mp) {
	        Log.v("TAG", "On Completion");
	        myThread.interrupt();
	        Intent i = new Intent(getApplicationContext(), MainActivity.class);
	        startActivity(i);
	        finish();
	        }
	    });
	}

	class CountDownRunner implements Runnable {
	    public void run() {
	        while(!Thread.currentThread().isInterrupted()) {
	            try {
	                doWork();
	                Thread.sleep(1000);
	            } catch (InterruptedException e) {
	                Thread.currentThread().interrupt();
	                e.printStackTrace();
	            } catch(Exception e) {
	                e.printStackTrace();
	            }
	        }
	    }
	}

	public void doWork() {
	    runOnUiThread(new Runnable() {
	        public void run()  {
	            try {
	                TextView mText = (TextView)findViewById(R.id.textview);
	                Date dt = new Date();
	                int hours = dt.getHours();
	                int minutes = dt.getMinutes();
	                int seconds = dt.getSeconds();
	                String curTime = hours + ":"+ minutes + ":" + seconds;
	                /*if(minutes == mMinutes && seconds == mSeconds) {
	                    mText.setText(getString(R.string.one));
	                } else if(minutes == mMinutes && seconds == mSeconds+20) {
	                    mText.setText(getString(R.string.two));
	                } else if(minutes == mMinutes && seconds == mSeconds+38) {
	                    mText.setText(getString(R.string.three));
	                } else if(minutes == mMinutes && seconds == mSeconds+47) {
	                    mText.setText(getString(R.string.four));
	                } else if(minutes == mMinutes+1 && seconds == mSeconds2+2) {
	                    mText.setText(getString(R.string.five));
	                } else if(minutes == mMinutes+1 && seconds == mSeconds2+22) {
	                    mText.setText(getString(R.string.six));
	                } else if(minutes == mMinutes+2) {
	                    mText.setText(getString(R.string.seven));
	                } else if(minutes == mMinutes+2 && seconds == mSeconds2+2) {
	                    mText.setText("");
	                }*/
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	    });
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if ((!(android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.DONUT) 
	            &&keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0))
	    {
	        onBackPressed();
	    }
	    return super.onKeyDown(keyCode, event);
	}

	public void onBackPressed() {
	    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
	    intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
	    startActivity(intent);
	    finish();
	    return;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
