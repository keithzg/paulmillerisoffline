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
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import com.nineoldandroids.animation.ObjectAnimator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@SuppressLint("ValidFragment")
public class MainActivity extends FragmentActivity {

	static String paulTimeLeft;
    static long days, hours, minutes, seconds;

	long milliseconds;

    MyFragmentPagerAdapter mMyFragmentPagerAdapter;
    CountingFragment mCountingFragment;

	public void onCreate(Bundle savedInstanceState) {

		Date internetTimeForPaul = new Date("05/01/2013 00:00:00");
		milliseconds = internetTimeForPaul.getTime();

		MyCount counter = new MyCount(milliseconds,1000);
		counter.start();
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

	// countdowntimer is an abstract class, so extend it and fill in methods
	public class MyCount extends CountDownTimer {

		public MyCount(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {
		}

		@Override
		public void onTick(long millisUntilFinished) {

			TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
			DateFormat dateFormat = new SimpleDateFormat();
			java.util.Date date = new java.util.Date();
			Calendar nowCal = Calendar.getInstance();nowCal.setTime(date);

			Date internetTimeForPaul = new Date("05/01/2013 00:00:00");
			Calendar paulCal = Calendar.getInstance();paulCal.setTime(internetTimeForPaul);

			long days = daysBetween(nowCal, paulCal);

			dateFormat = new SimpleDateFormat("HH");
			date = new java.util.Date();
			int hours = Math.abs(Integer.parseInt(dateFormat.format(date)) - 24);
			if (hours == 24) {
				hours = 0;
				days++;
			}
			if (hours != 0 ) {hours = hours - 1;}

			dateFormat = new SimpleDateFormat("mm");
			date = new java.util.Date();
			int minutes = Math.abs(Integer.parseInt(dateFormat.format(date)) - 60);
			if (minutes == 60) {
				minutes = 0;
			}

			dateFormat = new SimpleDateFormat("ss");
			date = new java.util.Date();
			int seconds = Math.abs(Integer.parseInt(dateFormat.format(date)) - 60);
			if (seconds == 60) {
				seconds = 0;
			}
			if (minutes != 0 ) {minutes = minutes - 1;}

			paulTimeLeft = String.format(" %d days\n %d hours\n %d minutes\n %d seconds\n",
					MainActivity.days = days,
					MainActivity.hours = hours,
					MainActivity.minutes = minutes,
					MainActivity.seconds = seconds
			);

		}
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
