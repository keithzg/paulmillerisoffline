package ca.keithzg.paulmillerisoffline;

import java.text.SimpleDateFormat;
import java.util.Date;

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
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy, HH:mm");
        formatter.setLenient(false);


        String oldTime = "1.5.2013, 04:00";
        Date oldDate;
        try {
            oldDate = formatter.parse(oldTime);
             milliseconds = oldDate.getTime();

            //long startTime = System.currentTimeMillis();
            // do your work...
            long endTime=System.currentTimeMillis();

             diff = endTime-milliseconds;       

          
            long seconds = (long) (diff / 1000) % 60 ;

            long minutes = (long) ((diff / (1000*60)) % 60);

            long hours   = (long) ((diff / (1000*60*60)) % 24);

            long days = (int)((diff / (1000*60*60*24)) % 365);

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


        Long serverUptimeSeconds = (System.currentTimeMillis() - milliseconds) / 1000;


            String serverUptimeText = String.format("%d days %d hours %d minutes %d seconds",
            serverUptimeSeconds / 86400,
            ( serverUptimeSeconds % 86400) / 3600 ,
            ((serverUptimeSeconds % 86400) % 3600 ) / 60,
            ((serverUptimeSeconds % 86400) % 3600 ) % 60
            );



        MyCount counter = new MyCount(milliseconds,1000);
        counter.start();


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
            //tv.setText("Left: " + millisUntilFinished / 1000);

            long diff = endTime - millisUntilFinished; 

            long seconds = (long) (diff / 1000) % 60 ;

            long minutes = (long) ((diff / (1000*60)) % 60);

            long hours   = (long) ((diff / (1000*60*60)) % 24);

            int days = (int)((diff / (1000*60*60*24)) % 365);



            Long serverUptimeSeconds = 
                    (System.currentTimeMillis() - millisUntilFinished) / 1000;


                String serverUptimeText = 
                String.format(" %d days\n %d hours\n %d minutes\n %d seconds\n",
                serverUptimeSeconds / 86400,
                ( serverUptimeSeconds % 86400) / 3600 ,
                ((serverUptimeSeconds % 86400) % 3600 ) / 60,
                ((serverUptimeSeconds % 86400) % 3600 ) % 60
                );  



             // tv.setText(days +":"+hours+":"+minutes + ":" + seconds);
                tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 48);
                tv.setText(serverUptimeText);
        }
    }
}
