/*Copyright 2013 Matej Čurilla (sarriel)
* 
* This file is part of Paul (com.sarriel.paul).
* 
* Paul is free software: you can redistribute it and/or modify it under
* the terms of the GNU General Public License as published by the
* Free Software Foundation, version 2 of the License.
* 
* Paul is distributed in the hope that it will be useful,but
* WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
* or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
* more details.
* 
* See http://www.gnu.org/licenses/gpl-2.0.html .
*/

package ca.keithzg.paulmiller.is.offline;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.text.format.Time;
import android.widget.RemoteViews;

public class PaulWidget extends AppWidgetProvider {
	
	public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
		final int N = appWidgetIds.length;

		// Perform this loop procedure for each App Widget that belongs to this provider
		for (int i=0; i<N; i++) {
			int appWidgetId = appWidgetIds[i];

			// Create an Intent to launch ExampleActivity
			Intent intent = new Intent(context, MainActivity.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);

			// Get the layout for the App Widget and attach an on-click listener
			// to the button
			RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.paulwidget);
			views.setOnClickPendingIntent(R.id.widget_wrapper, pendingIntent);
			
			Time t = new Time();
			Time n = new Time();
			t.switchTimezone(Time.TIMEZONE_UTC);
			n.switchTimezone(Time.TIMEZONE_UTC);
			n.setToNow();
			t.set(0,0,4,1, 4, 2013);
			
			long diff = (t.toMillis(true) - n.toMillis(true))/1000;
			
			int mins = (int) (diff / 60 % 60);
			int hours = (int) (diff/ 3600 % 24);
			
			int days = (int) (diff / 86400);
			
			views.setTextViewText(R.id.textView1, Integer.toString(days));
			views.setTextViewText(R.id.textView2, Integer.toString(hours));
			views.setTextViewText(R.id.textView3, Integer.toString(mins));
			
			// Tell the AppWidgetManager to perform an update on the current app widget
			appWidgetManager.updateAppWidget(appWidgetId, views);
		}
	}

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
		Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 0);

		Time n = new Time();
		n.setToNow();
		
		n.second = 0;
		n.minute +=1;
		
		am.setRepeating(AlarmManager.RTC, n.toMillis(false) + 1000 , 60000 , pi);
	}
	@Override
	public void onDisabled(Context context) {
//		Toast.makeText(context, "onDisabled():last widget instance removed", Toast.LENGTH_SHORT).show();
		Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
		PendingIntent sender = PendingIntent.getBroadcast(context, 0, intent, 0);
		AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
		alarmManager.cancel(sender);
		super.onDisabled(context);
	}

	@Override
	 public void onDeleted(Context context, int[] appWidgetIds) {
//	  Toast.makeText(context, "TimeWidgetRemoved id(s):"+appWidgetIds, Toast.LENGTH_SHORT).show();
	  super.onDeleted(context, appWidgetIds);
	 }
	


}


