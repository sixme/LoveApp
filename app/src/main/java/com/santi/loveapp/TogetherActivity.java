package com.santi.loveapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import android.widget.TextView;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.PeriodType;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;


import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class TogetherActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_together);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Berlin"));
        Locale locale = new Locale("Germany");
        Calendar startCal = Calendar.getInstance(locale);
        startCal.set(2015, Calendar.FEBRUARY, 17);
        Calendar today = Calendar.getInstance(locale);
        Interval interval = new Interval(startCal.getTimeInMillis(), today.getTimeInMillis());
        Period period = interval.toPeriod().normalizedStandard(PeriodType.yearMonthDay());

        PeriodFormatter formatter = new PeriodFormatterBuilder().appendYears().appendSuffix(" year ", " years ").appendSeparator("and ").appendMonths().appendSuffix(" month ", " months ").appendSeparator("and ").appendDays().appendSuffix(" day ", " days ").toFormatter();
        String time = formatter.print(period);

        TextView newtext = (TextView) findViewById(R.id.time);
        newtext.setText(time);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_together, menu);
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
