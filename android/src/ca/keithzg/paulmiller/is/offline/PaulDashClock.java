package ca.keithzg.paulmiller.is.offline;

import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

import android.util.Log;

import com.google.android.apps.dashclock.api.DashClockExtension;
import com.google.android.apps.dashclock.api.ExtensionData;

public class PaulDashClock extends DashClockExtension {
    private static final String TAG = "ExampleExtension";
    public static final String PREF_NAME = "pref_name";
	@Override
	protected void onUpdateData(int reason) {
        // Get the time left.
		ApiConnector ac = new ApiConnector();
		Long remSec = null;
		try {
			remSec = ac.execute("http://pauldown.genicus.be/seconds.php").get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		if(remSec == null)
		{
			TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
			
			Long now = System.currentTimeMillis()/1000;
			
			Date internetTimeForPaul = new Date("05/01/2013 00:00:00");
			Long online = internetTimeForPaul.getTime()/1000;
					
			remSec = online - now;
		}
		
		// Convert to days minutes hours
		Long minutes = ((remSec / 60) % 60);
		Long hours   = ((remSec / (60*60)) % 24);
		Long days = ((remSec / (60*60*24)));
		
		// Publish the extension data update.
		publishUpdate(new ExtensionData()
	        .visible(true)
	        .icon(R.drawable.dc_extension)
	        .status(remSec<=0?"Online":"Offline")
	        .expandedTitle(String.format("Paul Miller is %s",remSec<=0?"Online":"Offline"))
	        .expandedBody(String.format("%d Days %d Hours %d Minutes",
	        		MainActivity.days, MainActivity.hours, MainActivity.minutes)));
	}
	
	protected void onInitialize(boolean isReconnect) {
		setUpdateWhenScreenOn(true);
	}
}
