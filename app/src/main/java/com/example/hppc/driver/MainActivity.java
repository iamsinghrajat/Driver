package com.example.hppc.driver;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.hppc.driver.SoundReceiver;

import java.util.Calendar;


public class MainActivity extends Activity {
    SharedPreferences sharedPreferences;
    SoundReceiver mReceiver;
    final Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(final Message msg)
        {

            Log.e("Listening Message","lll");
            if (0 < msg.what)
            {
            Log.e("Dff","Dd");
            }
            else
            {
                if (msg.getData() != null)
                {
                    String s = msg.getData().getString("Text");
                    sharedPreferences = getSharedPreferences("busno", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor1 = sharedPreferences.edit();
                    editor1.putString("fuel", s);

                    editor1.apply();
                    Log.e("s",s);

                }
            }
        }
    };

   int lp=1;
    TextView tv;
    Location lastKnownLoc;


    @Override
    protected void onDestroy()
    {
        super.onDestroy();
       // if(mReceiver!=null)
       // mReceiver.destroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {

///////
            Context ctx = getApplicationContext();
/** this gives us the time for the first trigger.  */
            Calendar cal = Calendar.getInstance();
            AlarmManager am = (AlarmManager) ctx.getSystemService(Context.ALARM_SERVICE);
            long interval = 1000*60*60*24;
            Intent serviceIntent = new Intent(ctx,LocationService.class);
// make sure you **don't** use *PendingIntent.getBroadcast*, it wouldn't work
            PendingIntent servicePendingIntent =
                    PendingIntent.getService(ctx,
                            1, // integer constant used to identify the service
                            serviceIntent,
                            PendingIntent.FLAG_CANCEL_CURRENT);  // FLAG to avoid creating a second service if there's already one running
// there are other options like setInexactRepeating, check the docs
            am.setRepeating(
                    AlarmManager.RTC_WAKEUP,//type of alarm. This one will wake up the device when it goes off, but there are others, check the docs
                    cal.getTimeInMillis(),
                    interval,
                    servicePendingIntent
            );

            ////////////
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //mReceiver.start();

      SharedPreferences  sharedPreferences = getSharedPreferences("State", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("State","ON");
        editor.apply();





            BroadcastReceiver mMessageReceiver = new BroadcastReceiver()
            {
                @Override
                public void onReceive(Context context, Intent intent) {
                    // Get extra data included in the Intent
                    String message = intent.getStringExtra("Status");
                    if (message.equals("Please Enable Location")) {
                        if (message != null) {
                            tv = (TextView) findViewById(R.id.tv1);
                            tv.setText(message);
                        }
                    } else {

                        Bundle b = intent.getBundleExtra("Location");
                        lastKnownLoc = b.getParcelable("Location");
                        try {
                            if (lastKnownLoc != null) {
                                if (message != null) {
                                    tv = (TextView) findViewById(R.id.tv1);
                                    tv.setText(message);
                                }

                            }
                        } catch (Exception e) {
                            Log.e("err", e.toString());
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            };
            LocalBroadcastManager.getInstance(this).registerReceiver(
                    mMessageReceiver, new IntentFilter("GPSLocationUpdates"));


            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == 1) {
                Toast.makeText(this, "Location Access already Allowed", Toast.LENGTH_SHORT).show();
                lp = 1;

            } else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

            }
        }
        catch (Exception e)
        {
            Log.e("Error",e.toString());
        }

    }

    @Override
    protected void onResume() {
        try {
            super.onResume();
            SharedPreferences sharedPreferences = getSharedPreferences("State", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("State", "ON");

            Log.e("UI ", "Resumed");

            editor.commit();
        }catch (Exception e)
        {

        }
    }

    @Override
    protected void onStop() {
        try {
            super.onStop();
            SharedPreferences sharedPreferences = getSharedPreferences("State", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("State", "OFF");

            Log.e("UI ", "Stopped");
            editor.commit();
        }
        catch (Exception e)
        {

        }
    }
    public void onClickN()
    {
        Log.e("alarm Set","");



    }

    @Override
        public void onRequestPermissionsResult ( int requestCode,
        String permissions[], int[] grantResults){
            switch (requestCode) {
                case 1: {
                    // If request is cancelled, the result arrays are empty.
                    if (grantResults.length > 0
                            && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    {
                        lp = 1;


                        Toast.makeText(this, "Location Access Allowed", Toast.LENGTH_SHORT).show();

                        // permission was granted, yay! Do the
                        // contacts-related task you need to do.

                    } else
                    {
                        Toast.makeText(this, "Please Allow Location Access", Toast.LENGTH_SHORT).show();

                        lp = 0;
                        // permission denied, boo! Disable the
                        // functionality that depends on this permission.
                    }
                    return;
                }

                // other 'case' lines to check for other
                // permissions this app might request
            }




    }
    public void b1(View view)
    {
        try {

            // mReceiver.start();
            Log.e("ffff", lp + "");
            if (lp == 1) {
                Log.e("", lp + "");
                EditText et = (EditText) findViewById(R.id.et1);
                if (et.getText() != null) {
                    SharedPreferences spf = getSharedPreferences("busno", Context.MODE_PRIVATE);
                    if (!(spf.getString("busno", "10000").equals("10000"))) {
                        stopService(new Intent(this, LocationService.class));
                        Log.e("Service", "Stopped for busno. " + spf.getString("busno", "10000"));

                    }
                    SharedPreferences.Editor editor = spf.edit();
                    editor.putString("busno", et.getText().toString());
                    editor.putFloat("maxspeed", 0.0f);
                    editor.commit();
                    // onClickN();
                    startService(new Intent(this, LocationService.class));
                    Log.e("Service", "Started for busno. " + spf.getString("busno", "10000"));

                    Button button = (Button) findViewById(R.id.b1);
                    TextView tv = (TextView) findViewById(R.id.tv1);
                    tv.setText("Sending Location of Bus to Server.........");
                }
            }
            if (lp == 0) {
                if (ContextCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_FINE_LOCATION) == 1) {
                    Log.e("", lp + "");
                    EditText et = (EditText) findViewById(R.id.et1);
                    SharedPreferences spf = getSharedPreferences("busno", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = spf.edit();
                    if (et.getText() != null) {
                        editor.putString("busno", et.getText().toString());
                        editor.commit();
                        startService(new Intent(this, LocationService.class));
                        Button button = (Button) findViewById(R.id.b1);
                        TextView tv = (TextView) findViewById(R.id.tv1);
                        tv.setText("Sending Location of Bus to Server.........");
                    }
                    lp = 1;

                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);

                }
            }
        }
        catch (Exception e)
        {}

    }

    public void b2(View view) {
        SharedPreferences spf = getSharedPreferences("busno", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = spf.edit();
        editor.putFloat("maxspeed",0.0f);
        editor.putFloat("distance",0.0f);
        editor.apply();
    }



}
