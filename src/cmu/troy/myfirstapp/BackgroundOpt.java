package cmu.troy.myfirstapp;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.text.format.Time;

public class BackgroundOpt extends IntentService {
	public BackgroundOpt(){
		super("troy.background");
	}
	public BackgroundOpt(String name) {
		super(name);
	}

	private List<String> getLastApps() throws IOException{
		File file = new File(this.getFilesDir(), getString(R.string.last_apps));
//		File file = new File(getString(R.string.last_apps));
		ArrayList<String> res = new ArrayList<String>();
		if (!file.exists()){
			file.createNewFile();
			return res;
		}
		
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String ts = reader.readLine();
		while (ts != null){
			res.add(ts);
			ts = reader.readLine();
		}
		reader.close();
		return res;
	}
	
	private List<String> getCurrentApps(){
		Context context = this.getApplicationContext();
	    ActivityManager mgr = (ActivityManager)context.getSystemService(ACTIVITY_SERVICE);

	    List<RunningTaskInfo> tasks = mgr.getRunningTasks(100);
	    
	    ArrayList<String> res = new ArrayList<String>();
	    
	    for(Iterator<RunningTaskInfo> i = tasks.iterator(); i.hasNext(); )
	    {
	        RunningTaskInfo p = (RunningTaskInfo)i.next();
	        res.add(p.baseActivity.flattenToString());
	    }
	    return res;
	}
	
	//TODO
	private List<String> getNewApps(List<String> oldApps, List<String> currentApps){
		return currentApps;
	}
	
	private void logApps(List<String> newApps) throws IOException{

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
		
		File file = null;
		if (mExternalStorageAvailable && mExternalStorageWriteable)
			file = new File(getExternalFilesDir(null), getString(R.string.log_file));
		else
			file = new File(this.getFilesDir(), getString(R.string.log_file));
		PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(file, true)));
		Time now = new Time();
		now.setToNow();
		writer.println("****************");
		writer.println(now.format2445());
		for (String s : newApps){
			writer.println(s);
		}
		writer.close();
	}
	
	@Override
    protected void onHandleIntent(Intent workIntent) {
		while (true){
			try{
			List<String> lastApps = getLastApps();
			List<String> currentApps = getCurrentApps();
			List<String> newApps = getNewApps(lastApps, currentApps);
			logApps(newApps);
			}catch(Exception e){
				e.printStackTrace();
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
    }
}
