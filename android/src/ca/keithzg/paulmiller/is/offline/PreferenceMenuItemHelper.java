/* Copyright 2013 Alex Curran

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.  */

package ca.keithzg.paulmiller.is.offline;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Helper class which can associate a checkable {@link MenuItem} with a boolean preference
 */
public class PreferenceMenuItemHelper {

	/**
	 * Associate a preference with a {@link MenuItem}. Best to call this in {@link Activity.onPrepareOptionsMenu()}
	 * @param context Context with which to retrieve the {@link SharedPreferences}
	 * @param menu The menu to search for the menu item in
	 * @param itemId Id of the menu item to associate
	 * @param preference Key of the preference to associate
	 * @return Associated MenuItem
	 */
	public static MenuItem associate(Context context, Menu menu, int itemId, final String preference) {
		MenuItem internal = menu.findItem(itemId);
		if (internal == null) return null;
		return associate(PreferenceManager.getDefaultSharedPreferences(context), internal, preference);
	}

	/**
	 * Associate a preference with a {@link MenuItem}. Best to call this in {@link Activity.onPrepareOptionsMenu()}.
	 * Use this method if you wish to supply a {@link SharedPreferences} which isn't the {@link Context} default.
	 * @param preferences SharedPreferences to search within for preference
	 * @param item MenuItem to associate
	 * @param preference SharedPreference to associate
	 * @return Associated MenuItem
	 */
	public static MenuItem associate(final SharedPreferences preferences, MenuItem item, final String preference) {

		if (!preferences.contains(preference)) {
			Log.w("PreferenceMenuItemHelper", "Default SharedPreference doesn't contain key: " + preference);
		}

		item.setCheckable(true);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) item.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		// If the boolean doesn't exist, we default here to false
		item.setChecked(preferences.getBoolean(preference, false));
		item.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
			@Override
			public boolean onMenuItemClick(MenuItem item) {
				boolean result;
				if (item.isChecked()) item.setChecked(result = false);
				else item.setChecked(result = true);
				applyOrCommit(preferences.edit().putBoolean(preference, result));
				return true;
			}
		});

		return item;
	}

	public static void applyOrCommit(SharedPreferences.Editor editor) {
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
			editor.apply();
		} else {
			editor.commit();
		}
	}

}
