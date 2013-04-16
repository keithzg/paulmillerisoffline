/*Copyright 2013 Matej Čurilla (sarriel)
* 
* This code is originally from Paul (com.sarriel.paul).
*
* Keith Zubot-Gephart received permission from Matej Čurilla to bump the
* license version.
* 
* Paul is free software: you can redistribute it and/or modify it under
* the terms of the GNU General Public License as published by the
* Free Software Foundation, version 3 or or any later version of the 
* License, or copyleft-next 0.2.1. 
* 
* Paul is distributed in the hope that it will be useful,but
* WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
* or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for
* more details.
* 
* See http://www.gnu.org/licenses/gpl-3.0.html
* Alternatively, you should have received a copy of copyleft-next 0.2.1 with
* this source repository.
*/

package ca.keithzg.paulmiller.is.offline;
import android.appwidget.AppWidgetManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.text.format.Time;
import android.widget.RemoteViews;

	public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {
				
		@Override
		public void onReceive(Context context, Intent intent) {

			//You can do the processing here update the widget/remote views.
			RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
					R.layout.paulwidget);
			
//			SimpleDateFormat formatter = new SimpleDateFormat("hh:mm:ss a");
			
			Time t = new Time();
			Time n = new Time();
			t.switchTimezone(Time.TIMEZONE_UTC);
			n.switchTimezone(Time.TIMEZONE_UTC);
			n.setToNow();
			t.set(0,0,4,1, 4, 2013);

			long diff = (t.toMillis(true) -n.toMillis(true))/1000;
			
			int mins = (int) (diff / 60 % 60);
			int hours = (int) (diff/ 3600 % 24);
			
			int days = (int) (diff / 86400);
			

			remoteViews.setTextViewText(R.id.textView1, Integer.toString(days));
			remoteViews.setTextViewText(R.id.textView2, Integer.toString(hours));
			remoteViews.setTextViewText(R.id.textView3, Integer.toString(mins));
			
			
			ComponentName thiswidget = new ComponentName(context, PaulWidget.class);
			AppWidgetManager manager = AppWidgetManager.getInstance(context);
			manager.updateAppWidget(thiswidget, remoteViews);
		}
	}