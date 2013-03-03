package ca.keithzg.paulmiller.is.offline;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import android.net.ParseException;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.app.Activity;
import android.util.TypedValue;
import android.widget.TextView;

public class MainActivity extends Activity {
    /** Called when the activity is first created. */
    TextView tv;
    long diff;
    long milliseconds;
    long endTime;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        tv = new TextView(this);
        this.setContentView(tv);
        //TimeZone.setDefault(TimeZone.getTimeZone("America/New_York"));
        
        java.util.Date date = new java.util.Date();
        Calendar nowCal = Calendar.getInstance();nowCal.setTime(date);
        
        Date internetTimeForPaul = new Date("03/04/2013 00:00:00");
        Calendar paulCal = Calendar.getInstance();paulCal.setTime(internetTimeForPaul);
       
        try {
             milliseconds = internetTimeForPaul.getTime();
            
             diff = endTime-milliseconds;       

             DateFormat dateFormat = new SimpleDateFormat();
             
             dateFormat = new SimpleDateFormat("HH");
             date = new java.util.Date();
             int hours = Math.abs(24 - Integer.parseInt(dateFormat.format(date)));
             if (hours == 24) {
            	 hours = 0;
             }

             dateFormat = new SimpleDateFormat("mm");
             date = new java.util.Date();
             int minutes = Math.abs(60 - Integer.parseInt(dateFormat.format(date)));

             dateFormat = new SimpleDateFormat("ss");
             date = new java.util.Date();
             int seconds = Math.abs(60 - Integer.parseInt(dateFormat.format(date)));  


        } catch (ParseException e) {
            // Auto-generated catch block
            e.printStackTrace();
        } 
        
        MyCount counter = new MyCount(milliseconds,1000);
        counter.start();


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
            tv.setText("done!");
        }

        @Override
        public void onTick(long millisUntilFinished) {
            

            long diff = endTime - millisUntilFinished; 
            
            DateFormat dateFormat = new SimpleDateFormat();      
            java.util.Date date = new java.util.Date();
            Calendar nowCal = Calendar.getInstance();nowCal.setTime(date);
            
            Date internetTimeForPaul = new Date("03/04/2013 00:00:00");
            Calendar paulCal = Calendar.getInstance();paulCal.setTime(internetTimeForPaul);
            
            long days = daysBetween(nowCal, paulCal);

            dateFormat = new SimpleDateFormat("HH");
            date = new java.util.Date();
            int hours = Math.abs(Integer.parseInt(dateFormat.format(date)) - 24);
            if (hours == 24) {
           	 hours = 0;
            }

            dateFormat = new SimpleDateFormat("mm");
            date = new java.util.Date();
            int minutes = Math.abs(Integer.parseInt(dateFormat.format(date)) - 60);

            dateFormat = new SimpleDateFormat("ss");
            date = new java.util.Date();
            int seconds = Math.abs(Integer.parseInt(dateFormat.format(date)) - 60);  



                String paulTimeLeft = String.format(" %d days\n %d hours\n %d minutes\n %d seconds\n",
                days,
                hours,
                minutes,
                seconds
                );  

                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 48);
                tv.setText(paulTimeLeft);
        }
    }
}
