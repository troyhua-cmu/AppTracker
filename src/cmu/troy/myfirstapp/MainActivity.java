package cmu.troy.myfirstapp;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RecentTaskInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends Activity {

	public final static String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Intent mServiceIntent = new Intent(this, BackgroundOpt.class);
		startService(mServiceIntent);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public void seeDebug(View view){
		TextView mainView = (TextView) findViewById(R.id.main_view);
		
		boolean mExternalStorageAvailable = false;
		boolean mExternalStorageWriteable = false;
		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state)) {
		    // We can read and write the media
		    mExternalStorageAvailable = mExternalStorageWriteable = true;
		} else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
		    // We can only read the media
		    mExternalStorageAvailable = true;
		    mExternalStorageWriteable = false;
		} else {
		    // Something else is wrong. It may be one of many other states, but all we need
		    //  to know is we can neither read nor write
		    mExternalStorageAvailable = mExternalStorageWriteable = false;
		}
		
		
//		Context context = this.getApplicationContext();
//	    ActivityManager mgr = (ActivityManager)context.getSystemService(ACTIVITY_SERVICE);
//
//	    List<RunningTaskInfo> tasks = mgr.getRunningTasks(100);
//
//	    StringBuilder sb = new StringBuilder();
//	    sb.append(this.getFilesDir().toString() + "\n");
//	    sb.append("Running tasks: " + tasks.size() + "\n");
//	    for(Iterator<RunningTaskInfo> i = tasks.iterator(); i.hasNext(); )
//	    {
//	        RunningTaskInfo p = (RunningTaskInfo)i.next();
//	        sb.append("Short Name: " + p.baseActivity.flattenToShortString() + "\n");
//	        sb.append("Full Name: " + p.baseActivity.flattenToString() + "\n----------\n");
//	    }
//	    
		StringBuilder sb = new StringBuilder();
		File file = new File(getExternalFilesDir(null), getString(R.string.log_file));
		sb.append(file.getAbsolutePath() + "\n");
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		file = new File(Environment.getDataDirectory(), getString(R.string.log_file));
		sb.append(file.getAbsolutePath() + "\n");
		try {
			file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	    mainView.setMovementMethod(new ScrollingMovementMethod());
	    mainView.setText(sb.toString());
	}

	public void sendMessage(View view) {
		Intent intent = new Intent(this, DisplayMessageActivity.class);
		startActivity(intent);
	}
	
	public void onDestroy() {
	    super.onDestroy();  // Always call the superclass
	    
	    // Stop method tracing that the activity started during onCreate()
	    android.os.Debug.stopMethodTracing();
	}

}
