package com.santi.loveapp;

import android.app.Activity;
import android.app.DialogFragment;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;



public class NextVisit extends Activity implements DatePickerFragment.TheListener {
    protected Vibrator vibrate;
    protected String filePath = "";
    protected SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    final long[] pattern = {0, 1000, 500, 1000, 500, 1000, 500, 1000};
    CountDownTimer timer = null;
    String dateString = "";
    boolean done = false; //TO FIX, this is to avoid duplicities on saving the date, dunno why it goes twice...
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_visit);
        final TextView label = (TextView) findViewById(R.id.label);
        final TextView countdown = (TextView) findViewById(R.id.time_visit);
        final Button bDate = (Button) findViewById(R.id.date_button);
        final Button bDelete = (Button) findViewById(R.id.delete_button);
        File dir = new File(getFilesDir(), "loveApp");
        if (!dir.exists())
            dir.mkdirs();
        final File f = new File(getFilesDir() + "/loveApp" + "/love_date.txt");
        filePath = f.getAbsolutePath();
        vibrate = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        bDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (f.exists()) {
                    f.delete();
                    System.out.println("File deleted successfully");
                }
                bDate.setVisibility(View.VISIBLE);
                label.setText(R.string.next_visit1);
                if(timer != null){
                    System.out.println("I cancelled the timer");
                    timer.cancel();
                    countdown.setText("");
                    System.out.println("I put the text off");
                    countdown.setText("");
                    System.out.println("again, sometimes the timer keeps ticking");
                }
                bDelete.setVisibility(View.INVISIBLE);
                bDate.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        DialogFragment picker = new DatePickerFragment();
                        bDelete.setVisibility(View.VISIBLE);
                        bDate.setVisibility(View.INVISIBLE);
                        picker.show(getFragmentManager(), "datePicker");
                    }
                });


            }
        });

        if (f.exists()) {
            System.out.println("File exists");
            bDate.setVisibility(View.INVISIBLE);
            label.setText(getString(R.string.label));
            try {
                BufferedReader br = new BufferedReader(new FileReader(f));
                dateString = br.readLine();
                br.close();
                System.out.println("Date: " + dateString);
                Date finalDate = formatter.parse(dateString);
                setCountdown(finalDate);
            } catch (Exception e) {
                e.printStackTrace();
                label.setText("ERROR");
            }

        } else {
            System.out.println("File DOES NOT EXIST");
            bDelete.setVisibility(View.INVISIBLE); //Removing the delete date button
            bDate.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    DialogFragment picker = new DatePickerFragment();
                    bDelete.setVisibility(View.VISIBLE);
                    bDate.setVisibility(View.INVISIBLE);
                    picker.show(getFragmentManager(), "datePicker");
                }
            });
        }
    }

    public void setCountdown(Date finishDate) {
        long end = finishDate.getTime();
        final TextView countdown = (TextView) findViewById(R.id.time_visit);
        timer = new CountDownTimer(end - System.currentTimeMillis(), 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long millis = millisUntilFinished / 1000;

                String timeText =
                        String.format("%d days, %d hours, %d minutes, %d seconds!", millis / 86400,
                                 (millis % 86400) / 3600,
                                ((millis % 86400) % 3600) / 60,
                                ((millis % 86400) % 3600) % 60
                        );

                countdown.setText(timeText);

            }

            @Override
            public void onFinish() {
                countdown.setText("Today is the day!");
                vibrate.vibrate(pattern, -1);
                File f = new File(getFilesDir() + "/loveApp" + "/love_date.txt");
                if(f.exists())
                    f.delete();
            }
        }.start();


    }


    @Override
    public void returnDate(String date) {
        //Easy way to avoid repetition... needs fixing.
        if(done == false){
            done = true;
        }else{
            done = false;
            return;
        }
        Date parsedDate = null;
        try{
            parsedDate = formatter.parse(date);
        }catch (ParseException pe){
            pe.printStackTrace();
            return;
        }
        if(parsedDate != null  && parsedDate.before(new Date())){
           Toast toast = Toast.makeText(getApplicationContext(), "Please select a future date, don't cheat :P",
                    Toast.LENGTH_LONG);
            ViewGroup group = (ViewGroup) toast.getView();
            TextView messageTextView = (TextView) group.getChildAt(0);
            messageTextView.setTextSize(25);
            toast.show();
            Button bDelete = (Button) findViewById(R.id.delete_button);
            bDelete.setVisibility(Button.INVISIBLE);
            Button bDate = (Button) findViewById(R.id.date_button);
            bDate.setVisibility(Button.VISIBLE);
            return;
        }
        dateString = date;
        TextView label = (TextView) findViewById(R.id.label);
        label.setText(R.string.label);
        BufferedWriter bw;
        try {
            bw = new BufferedWriter(new FileWriter(new File(filePath)));
            bw.write(dateString);
            bw.close();
            System.out.println("Saved the file, date is: " + dateString);
            Date finishDate = formatter.parse(date);
            setCountdown(finishDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
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



