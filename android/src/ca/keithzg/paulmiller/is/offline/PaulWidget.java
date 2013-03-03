package ca.keithzg.paulmiller.is.offline;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.IBinder;
import android.text.format.Time;
import android.util.Log;
import android.widget.RemoteViews;

public class PaulWidget extends AppWidgetProvider {

	@Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // To prevent any ANR timeouts, we perform the update in a service
        context.startService(new Intent(context, UpdateService.class));
    }

	   public static class UpdateService extends Service {
	        @Override
	        public void onStart(Intent intent, int startId) {
	            // Build the widget update for today
	            RemoteViews updateViews = buildUpdate(this);

	            // Push update for this widget to the home screen
	            //ComponentName thisWidget = new ComponentName(this, PaulWidget.class);
	            //AppWidgetManager manager = AppWidgetManager.getInstance(this);
	            //manager.updateAppWidget(thisWidget, updateViews);
	        }

	        @Override
	        public IBinder onBind(Intent intent) {
	            return null;
	        }

	        /**
	         * Regular expression that splits "Word of the day" entry into word
	         * name, word type, and the first description bullet point.
	         */
	        private static final String WOTD_PATTERN =
	            "(?s)\\{\\{wotd\\|(.+?)\\|(.+?)\\|([^#\\|]+).*?\\}\\}";

	        /**
	         * Build a widget update to show the current Wiktionary
	         * "Word of the day." Will block until the online API returns.
	         */
	        public RemoteViews buildUpdate(Context context) {
	            
	            RemoteViews views = null;
				return views;
	        }
	    }
	
}
