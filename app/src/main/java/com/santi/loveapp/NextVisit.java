package com.santi.loveapp;

import android.app.Activity;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;


public class NextVisit extends Activity {
    protected Vibrator vibrate;
    protected int SECONDS_IN_A_DAY = 24 * 60 * 60;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_visit);
        final TextView countdown = (TextView) findViewById(R.id.time_visit);
        Calendar thatDay = Calendar.getInstance();
        thatDay.setTime(new Date(0)); /* reset */
        thatDay.set(Calendar.DAY_OF_MONTH,3);
        thatDay.set(Calendar.MONTH,6); // 0-11 so 1 less
        thatDay.set(Calendar.YEAR, 2015);
        thatDay.set(Calendar.HOUR_OF_DAY, 17);
        thatDay.set(Calendar.MINUTE,45);

        vibrate = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        final long[] pattern = {0, 1000, 500, 1000, 500, 1000, 500, 1000 };

        new CountDownTimer(thatDay.getTimeInMillis(), 1000) {

            public void onTick(long millisUntilFinished) {
                Calendar today = Calendar.getInstance();
                long diff =  millisUntilFinished - today.getTimeInMillis();
                long diffSec = diff / 1000;

                long days = diffSec / SECONDS_IN_A_DAY;
                long secondsDay = diffSec % SECONDS_IN_A_DAY;
                long seconds = secondsDay % 60;
                long minutes = (secondsDay / 60) % 60;
                long hours = (secondsDay / 3600);
                countdown.setText(days+ " days, "+ hours + "hours, "+minutes + "m, "+seconds + "s remaining!");
            }

            public void onFinish() {
                countdown.setText("done!");
                vibrate.vibrate(pattern,-1);
            }
        }.start();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_next_visit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
