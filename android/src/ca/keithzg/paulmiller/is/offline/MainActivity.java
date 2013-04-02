package ca.keithzg.paulmiller.is.offline;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import com.nineoldandroids.animation.ObjectAnimator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.Timer;
import java.util.concurrent.ExecutionException;

@SuppressLint("ValidFragment")
public class MainActivity extends FragmentActivity {

	static String paulTimeLeft;
    static long days, hours, minutes, seconds;

	long milliseconds;

    MyFragmentPagerAdapter mMyFragmentPagerAdapter;
    CountingFragment mCountingFragment;

	public void onCreate(Bundle savedInstanceState) {

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
			Log.d("MainActivity", "Seconds is null, calculating");
			TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
			
			Long now = System.currentTimeMillis()/1000;
			
			Date internetTimeForPaul = new Date("05/01/2013 00:00:00");
			Long online = internetTimeForPaul.getTime()/1000;
					
			remSec = online - now;
		}
		Count c = new Count(remSec);
		
		Timer t = new Timer();
		t.schedule(c, 0, 1000);
		
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewpager);
        MyFragmentPagerAdapter mMyFragmentPagerAdapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
		mViewPager.setAdapter(mMyFragmentPagerAdapter);
		mViewPager.setCurrentItem(0);
		
		
	}

    private class MyFragmentPagerAdapter extends FragmentPagerAdapter {

		private static final int NUMBER_OF_PAGES = 2;

		public MyFragmentPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		public Fragment getItem(int page) {
			switch (page) {
				case 0: return mCountingFragment = new CountingFragment();
				default: return new InfoFragment();
			}
		}

		@Override
		public int getCount() {
			return NUMBER_OF_PAGES;
		}

	}

	/** Using Calendar - THE CORRECT WAY from http://tripoverit.blogspot.ca/2007/07/java-calculate-difference-between-two.html **/
	public static long daysBetween(Calendar startDate, Calendar endDate) {
		Calendar date = (Calendar) startDate.clone();
		long daysBetween = 0;
		while (date.before(endDate)) {
			date.add(Calendar.DAY_OF_MONTH, 1);
			daysBetween++;
		}
		// However, that returns one more day than we want.
		if (daysBetween > 0){
			daysBetween = daysBetween - 1;
		}
		return daysBetween;
	}

	/**
	 * Opens the Github page for the project
	 * @param v
	 */
	public void openGithub(View v) {
		startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse("https://github.com/keithzg/paulmillerisoffline")));
	}


    public void overflow(View v) {
        PopupMenu pm = new PopupMenu(this, v);
        pm.inflate(R.menu.menu_main);
		PreferenceMenuItemHelper.associate(this, pm.getMenu(), R.id.menu_secondsToggle, "showSeconds");
	    pm.getMenu().findItem(R.id.menu_controlledExplosion).setVisible(BuildConfig.DEBUG);
	    pm.getMenu().findItem(R.id.menu_controlledExplosion).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
		    @Override
		    public boolean onMenuItemClick(MenuItem item) {
			    dealWithIt();
			    return false;
		    }
	    });
        pm.show();

    }

	public void dealWithIt() {
		findViewById(R.id.paulsMug).setVisibility(View.VISIBLE);
		findViewById(R.id.paulsSpecs).setVisibility(View.VISIBLE);
		ObjectAnimator.ofFloat(findViewById(R.id.paulsMug), "alpha", 0f, 1f).start();
		ObjectAnimator marginAnimator = ObjectAnimator.ofFloat(findViewById(R.id.paulsSpecs), "translationY",
				-120f * getResources().getDisplayMetrics().density, 0f);
		marginAnimator.setDuration(3000).start();
	}

}
