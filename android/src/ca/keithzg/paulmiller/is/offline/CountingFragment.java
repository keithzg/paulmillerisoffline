package ca.keithzg.paulmiller.is.offline;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class CountingFragment extends Fragment implements SharedPreferences.OnSharedPreferenceChangeListener {

	public TextView mDaysView, mHoursView, mMinsView, mSecsView, mSecsCaption;
	private SharedPreferences mPreferences;

	public CountingFragment() { }

	public void secondsShowAltered(boolean showSeconds, boolean animate) {
		if (showSeconds) {

			// Set the items as visible
			mSecsView.setVisibility(View.VISIBLE);
			mSecsCaption.setVisibility(View.VISIBLE);

			if (animate) {
				// Animate them together
				AnimatorSet as = new AnimatorSet();
				ObjectAnimator mViewAlpha = ObjectAnimator.ofFloat(mSecsView, "alpha", 1f);
				ObjectAnimator mCaptionAlpha = ObjectAnimator.ofFloat(mSecsCaption, "alpha", 1f);
				as.setDuration(300);
				as.playTogether(mViewAlpha, mCaptionAlpha);
				as.start();
			} else {
				ViewHelper.setAlpha(mSecsView, 1f);
				ViewHelper.setAlpha(mSecsCaption, 1f);
			}

		} else {

			if (animate) {
				// Animate them together
				AnimatorSet as = new AnimatorSet();
				ObjectAnimator mViewAlpha = ObjectAnimator.ofFloat(mSecsView, "alpha", 1f, 0f);
				ObjectAnimator mCaptionAlpha = ObjectAnimator.ofFloat(mSecsCaption, "alpha", 1f, 0f);
				as.setDuration(300);
				as.addListener(new Animator.AnimatorListener() {
					@Override
					public void onAnimationStart(Animator animator) {
					}

					@Override
					public void onAnimationEnd(Animator animator) {
						// Make the views disappear completely once the animation is done
						mSecsView.setVisibility(View.GONE);
						mSecsCaption.setVisibility(View.GONE);
					}

					@Override
					public void onAnimationCancel(Animator animator) {
					}

					@Override
					public void onAnimationRepeat(Animator animator) {
					}
				});
				as.playTogether(mViewAlpha, mCaptionAlpha);
				as.start();
			} else {
				ViewHelper.setAlpha(mSecsView, 0f);
				ViewHelper.setAlpha(mSecsCaption, 0f);
				mSecsCaption.setVisibility(View.GONE);
				mSecsView.setVisibility(View.GONE);
			}

		}
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
		if (key.equals("showSeconds")) {
			// User has explicitly changed the value, so we animate
			secondsShowAltered(sharedPreferences.getBoolean("showSeconds", true), true);
		}
	}

	public class MyCount1 extends CountDownTimer {

		public MyCount1(long millisInFuture, long countDownInterval) {
			super(millisInFuture, countDownInterval);
		}

		@Override
		public void onFinish() {

		}

		@Override
		public void onTick(long millisUntilFinished) {
			// TODO: static reference is not a good idea...
			mHoursView.setText(String.valueOf(MainActivity.hours));
			mDaysView.setText(String.valueOf(MainActivity.days));
			mMinsView.setText(String.valueOf(MainActivity.minutes));
			mSecsView.setText(String.valueOf(MainActivity.seconds));
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		mPreferences.unregisterOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onResume() {
		super.onResume();
		mPreferences.registerOnSharedPreferenceChangeListener(this);
		// Don't animate - this will be called on "setting up" the Fragment
		secondsShowAltered(mPreferences.getBoolean("showSeconds", true), false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));

		java.util.Date date = new java.util.Date();
		Calendar nowCal = Calendar.getInstance();
		nowCal.setTime(date);
		Date internetTimeForPaul = new Date("05/01/2013 00:00:00");
		long onTickSeconds = internetTimeForPaul.getTime();

		View v = inflater.inflate(R.layout.frag_counting, null);
		mHoursView = (TextView) v.findViewById(R.id.numHours);
		mMinsView = (TextView) v.findViewById(R.id.numMins);
		mSecsView = (TextView) v.findViewById(R.id.numSeconds);
		mDaysView = (TextView) v.findViewById(R.id.numDays);
		mSecsCaption = (TextView) v.findViewById(R.id.secondsCaption);

		MyCount1 counter1 = new MyCount1(onTickSeconds,1000);
		counter1.start();

		return v;
	}

}
