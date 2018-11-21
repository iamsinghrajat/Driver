package com.example.hppc.driver;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;

import com.android.volley.AuthFailureError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.location.LocationListener;

import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by hp pc on 17-06-2017.
 */
public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {
    public static Locations locations;
    RequestQueue requestQueue;
    String list = "";
    public static List<HashMap<String, String>> urlList = new ArrayList<>();
    public static boolean thread = false;

    public static Location mLocation;

    public static GoogleApiClient mGoogleApiClient;
    public static SharedPreferences sharedPreferences1;
    public static SharedPreferences sharedPreferences;
    public static SharedPreferences state;
    public static String temp;
    public static String temp1;
    public static float ms;
    public static float sp;
    int isFirstLoc;
    public static String Urlf;

    public String busnof;
    int stopStatus = 1;
    public static String knownLoc;
    Intent notificationIntent;
    PendingIntent pendingIntent;

    public static int locstat;
    private static LocationRequest mLocationRequest;
    Notification notification;
    Location loc = new Location("fused");


    long strtime;

    /* final Handler mHandler = new Handler()
     {
         @Override
         public void handleMessage(final Message msg)
         {

             Log.e("Listening Message","lll");
             if (0 < msg.what)
             {

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


     @Override
     public void onCreate()
     {
         super.onCreate();


     }

 */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try {
            Log.e("SS", "SS");

            // mReceiver = new SoundReceiver(mHandler);
            //  mReceiver.start();
            locstat = 0;
            strtime = 0;
            stopStatus = 1;
            Log.e("Service Started", "oo");
            Urlf = "http://13.58.183.35/api/update_location_and_parameters/";

            notificationIntent = new Intent(this, MainActivity.class);
            pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
            isFirstLoc = 1;

            notification = new Notification.Builder(this)
                    .setContentTitle("Tracking Location")
                    .setContentText("Powered by Inotracks")
                    .setSmallIcon(R.drawable.googleg_standard_color_18)
                    .setContentIntent(pendingIntent)
                    .setTicker(".....")
                    .build();
            startForeground(3, notification);

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();

            locations = new Locations();
            sharedPreferences1 = getSharedPreferences("busno", Context.MODE_PRIVATE);
            temp1 = sharedPreferences1.getString("busno", "10000");
            ms = sharedPreferences1.getFloat("maxspeed", 0.0f);
            state = getSharedPreferences("State", Context.MODE_PRIVATE);
            sharedPreferences = getSharedPreferences("busno", Context.MODE_PRIVATE);
            temp = sharedPreferences.getString("busno", "10000");
            requestQueue = Volley.newRequestQueue(this);


            busnof = temp;
            Log.e("busno", busnof);
            Toast.makeText(getApplicationContext(), busnof, Toast.LENGTH_SHORT).show();
            if (mGoogleApiClient != null) {
                Log.e("API connecting", "");
                mGoogleApiClient.connect();

            } else {
                Log.e("API empty", "");
            }
        } catch (Exception e) {
            return START_REDELIVER_INTENT;
        }
        return START_REDELIVER_INTENT;


    }

    @Override
    public void onDestroy() {

        try {
            super.onDestroy();

            if (mGoogleApiClient.isConnected()) {
                LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
                mGoogleApiClient.disconnect();
            }
            Log.e("Service Stopped", "..");
            Intent intent = new Intent("driver.hppc.example.com.ActivityRecognition.RestartSensor");
            sendBroadcast(intent);
        } catch (Exception e) {
            Log.e("error", "class exception");
        }
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onLocationChanged(Location location) {

        HashMap<String, String> urlf = new HashMap<String, String>();
        try {
            if (location.getAccuracy() <= 45) {
                locstat = 1;
                knownLoc = "";
                try {
/////////////////////////////
                    if ((location.getAccuracy() < 45) && (loc != null) && (locstat == 1)) {

                        float dis = loc.distanceTo(location);


                        if ((dis > 20) && (dis > 2 * (location.getAccuracy())) && (dis < 2000.0)) {
                            Log.e("location travleed----", dis + "");
                            SharedPreferences.Editor editor1 = sharedPreferences.edit();
                            editor1.putFloat("distance", +(float) (sharedPreferences.getFloat("distance", 0.0f) + dis / 1000.0));
                            loc = location;
                            editor1.apply();
                        }
                    } else if (loc == null) {
                        loc = location;
                    }

//////////////////////////////

                    ///////////////////////////////

                } catch (Exception e) {
                    Log.e("erroe56", e.toString());
                }

/////////////////////////////////
                ////////////////////////
                if ((location.getSpeed() * 3.6) > ms) {
                    SharedPreferences.Editor editor = sharedPreferences1.edit();
                    sp = location.getSpeed();
                    Log.e("speed ", (sp * 3.6) + "");
                    editor.putFloat("maxspeed", sp * 3.6f);
                    editor.apply();
                    ms = sharedPreferences1.getFloat("maxspeed", 0.0f);
                    Log.e("ms", ms + "");
                }

                if (state.getString("State", "OFF").equals("ON"))
                    sendMessageToActivity(location, temp1 + "\n" + location.getProvider() + "\nCoordinate:" + location.getLatitude() + "," + location.getLongitude() + " with accuracy of " + location.getAccuracy() + "\nSpeed:" + (location.getSpeed() * 3.6) + "\nMax Speed:" + sharedPreferences1.getFloat("maxspeed", 0.0f) + "\n Location:" + knownLoc + "\nDistance travelled : " + sharedPreferences.getFloat("distance", 0.0f) + " km" + "\nFuel Data:" + sharedPreferences.getString("fuel", "NILL"));

                int rs = 0;
                if (location.getSpeed() > 0.0) {
                    rs = 1;
                }
////////////
                knownLoc = locations.GetName(location);

                if (!knownLoc.equals("Not Known")) {

                    String temp = "";
                    temp = sharedPreferences.getString("knownLoc", "Not Known");
                    if (temp.equals("Not Known")) {


                        urlf.put("known_location", "true");
                        urlf.put("place_name", knownLoc);
                        //addLocation(knownLoc);
                    } else {
                        urlf.put("known_location", "false");
                        urlf.put("place_name", "");
                    }

                } else {
                    urlf.put("known_location", "false");
                    urlf.put("place_name", "");
                }
                SharedPreferences.Editor editor1 = sharedPreferences.edit();
                editor1.putString("knownLoc", knownLoc);
                editor1.apply();


/////////////////////////////
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                String d = df.format(c.getTime());
                d.replace("  ", "");
                urlf.put("key", "70b66a89929e93416d2ef535893ea14da331da8991cc7c74010b4f3d7fabfd62");
                urlf.put("bus_number", busnof);
                urlf.put("time_recorded", d);
                urlf.put("latitude", location.getLatitude() + "");
                urlf.put("longitude", location.getLongitude() + "");
                urlf.put("speed", (location.getSpeed() * 3.6) + "");
                urlf.put("fuel", urlList.size() + ".0");
                urlf.put("distance", sharedPreferences.getFloat("distance", 0.0f) + "");
                // String urls = Url1 + "&a=" + location.getLatitude() + "&o=" + location.getLongitude() + "&s=" + location.getSpeed() + "&r=" + rs+"&dis="+sharedPreferences.getFloat("distance", 0.0f)+"&y="+d.substring(0,4)+"&m="+d.substring(5,7)+"&d="+d.substring(8,10)+"&hh="+d.substring(11,13)+"&mm="+d.substring(14,16)+"&ss="+d.substring(17,19);

                if (urlList.size() > 10) {
                    removeUWURL();
                }


                urlList.add(urlf);
                Log.e("Added url size update", urlList.size() + "");
                if (!thread) {
                    Log.e("entry through main", "");
                    volleyRequest();
                }
            }
        } catch (Exception e) {
            Log.e("errro", e.toString());
        }

    }

    private void removeUWURL() {
        int c = 0;
        try {

            while (c < urlList.size()) {

                if (urlList.get(c).get("known_location").equals("false")) {
                    urlList.remove(c);
                } else {
                    c++;
                }
            }
        } catch (Exception e) {
            Log.e("e", e.toString());
        }
    }


    private void stopDuration(Location location) {

        try {
            long dur = System.currentTimeMillis() / (1000 * 60) - strtime;
            Calendar c = Calendar.getInstance();
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String d = df.format(c.getTime());
            //  urlList.add(urls);
            strtime = 0;
            stopStatus = 1;
        } catch (Exception e) {
        }

    }

    private void startDuration(Location location) {
        strtime = System.currentTimeMillis() / (1000 * 60);

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        try {
            Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                return;
            }
            mLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if (mLocation != null && mLocation.getAccuracy() < 80) {
                loc = mLocation;
                Location lkn = mLocation;


                if (mLocation != null) {
                    sendMessageToActivity(lkn, "***" + "\n" + lkn.getProvider() + "\nCoordinate:" + lkn.getLatitude() + "," + lkn.getLongitude() + " with accuracy of " + lkn.getAccuracy() + "\nSpeed:" + (lkn.getSpeed() * 3.6) + "\nMax Speed:" + "\n Location:" + locations.GetName(lkn));


                } else
                    sendMessageToActivity(null, "NO Last Location Available");
            }

            startLocationUpdates();
        } catch (Exception e) {
            Log.e("error", e.toString());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection Suspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("Connection failed", "");

    }


    public void addLocation(String knownLoc) {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String d = df.format(c.getTime());
        Log.e("New Stopp Added", knownLoc);
        // String urls=Url2+"&la=0.0&lo=0.0&loc=\""+knownLoc+"\""+"&t=\""+d+"\"";
        //urlList.add(urls);

    }

    protected void startLocationUpdates() {
        Log.e("Location Update Started", "d");
        try {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            mLocationRequest.setInterval(5000);
            mLocationRequest.setFastestInterval(4000);
            mLocationRequest.setSmallestDisplacement(15);

            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "Enable Permissions", Toast.LENGTH_LONG).show();
            }


            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        } catch (Exception e) {
            Log.e("hh", "hhhh");
        }


    }


    private void sendMessageToActivity(Location l, String msg) {
        try {
            Intent intent = new Intent("GPSLocationUpdates");
            // You can also include some extra data.
            intent.putExtra("Status", msg);
            Bundle b = new Bundle();
            b.putParcelable("Location", l);
            intent.putExtra("Location", b);
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        } catch (Exception e) {
            Log.e("ss", "ssss");
        }
    }

    public void volleyRequest() {
        try {
            thread = true;


            if (urlList.size() >= 1) {

                Log.e(".......", "........");

                Log.e("size at request update", urlList.size() + "");


                // Tag used to cancel the request
                //////
                JSONObject urlf = new JSONObject(urlList.get(0));


                Log.e("url", urlf.toString());
                String tag_string_req = "string_req";
                final JsonObjectRequest putRequest = new JsonObjectRequest(Request.Method.POST, Urlf, urlf,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // response

                                Log.d("Response", response.toString());

                                removeURL("Success");

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // error
                                Log.d("Error.Response", error.toString());
                                thread = false;
                            }
                        }
                ) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("Content-Type", "application/json");
                        return params;
                    }


                };

                requestQueue.add(putRequest);
                //  Log.e("ggg",putRequest.toString());

            } else {
                Log.e("ERror", "URL list  null");
                thread = false;

            }

        } catch (Exception e) {
            Log.e("volley ereor", "ss");
        }
    }

    private void removeURL(String response) {

        DateFormat df = new SimpleDateFormat("HH:mm:ss");
        String date = df.format(Calendar.getInstance().getTime());
        if (response != null && response.equals("Success")) {

            try {
                notification = new Notification.Builder(this)
                        .setContentTitle("Tracking Location")
                        .setContentText("Powered by Inotracks")
                        .setSubText("Last Update:" + date + "   " + sharedPreferences.getFloat("distance", 0.0f) + " km")
                        .setSmallIcon(R.drawable.googleg_standard_color_18)
                        .setContentIntent(pendingIntent)
                        .setTicker(".....")
                        .build();
                startForeground(3, notification);

            } catch (Exception e) {
                Log.e("volley error", "kk");
            }
            urlList.remove(0);
            Log.e("size reduced", urlList.size() + "");
        }

        Log.e("entry through Rurl", "");
        volleyRequest();

    }

}
