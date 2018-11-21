package com.example.hppc.driver;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;

/**
 * Created by hp pc on 09/01/2017.
 */

public class YourService extends Service {


    public int flag = 1;
    private LocationManager lm;
    private LocationListener ll;
    String Url1 = "http://innovtracks.pe.hu/tc.php?i=";
    String serverresponse = " E";

    int len = 20;
    Locations locations=new Locations();
    List<String> list=new ArrayList<>();
    List<String> urlList=new ArrayList<>();
    boolean thread=false;


    @Nullable
    @Override
    public IBinder onBind(Intent intent)
    {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        try{
            List<String> ll=new ArrayList<>();
            ll.add("hh");
            ll.add("kk");
            ll.remove(0);
            Log.e("rr",ll.get(0));
        }catch (Exception e){}

       // urlList.add("http://www.innovtracks.pe.hu/tc.php?i=10000&a=0&o=&s=0&r=0");

        SharedPreferences sharedPreferences = getSharedPreferences("busno", Context.MODE_PRIVATE);
        String temp = sharedPreferences.getString("busno", "10000");
        if (!(temp.equals("10000")))
            Url1 = Url1 + temp;
        else
            Url1 = Url1 + "10000";
        try {


            lm = (LocationManager) getSystemService(LOCATION_SERVICE);
            Location lkn=lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            sendMessageToActivity(lkn,"***"+ "\n" + lkn.getProvider() + "\nCoordinate:" + lkn.getLatitude() + "," + lkn.getLongitude() +" with accuracy of "+lkn.getAccuracy()+ "\nSpeed:" + (lkn.getSpeed() * 3.6) + "\nMax Speed:" +"\n Location:"+locations.GetName(lkn));

            ll = new LocationListener() {
                @Override
                public void onLocationChanged(Location location)

                {

                    if(location.getAccuracy()<300)
                    {
                        String knownLoc="";
                    SharedPreferences sharedPreferences1 = getSharedPreferences("busno", Context.MODE_PRIVATE);
                    String temp1 = sharedPreferences1.getString("busno", "10000");
                    Float ms = sharedPreferences1.getFloat("maxspeed", 0.0f);

                        //list=locations.GetName(location);
                        for(String a:list)
                        {
                            knownLoc=knownLoc+"\n"+a;
                        }


                    if ((location.getSpeed() * 3.6) > ms) {
                        SharedPreferences.Editor editor = sharedPreferences1.edit();
                        float sp = location.getSpeed();
                        Log.e("speed ", (sp * 3.6) + "");
                        editor.putFloat("maxspeed", sp * 3.6f);
                        editor.commit();
                        ms = sharedPreferences1.getFloat("maxspeed", 0.0f);
                        Log.e("ms", ms + "");
                    }
                    sendMessageToActivity(location, temp1 + "\n" + location.getProvider() + "\nCoordinate:" + location.getLatitude() + "," + location.getLongitude() +" with accuracy of "+location.getAccuracy()+ "\nSpeed:" + (location.getSpeed() * 3.6) + "\nMax Speed:" + sharedPreferences1.getFloat("maxspeed", 0.0f)+"\n Location:"+knownLoc);

                    int rs = 0;
                    if (location.getSpeed() > 0.0) {
                        rs = 1;
                    }

                    String urls = Url1 + "&a=" + location.getLatitude() + "&o=" + location.getLongitude() + "&s=" + location.getSpeed() + "&r=" + rs;

                   // Log.e(urls, "url");
                        urlList.add(urls);
                        Log.e("Added url size update",urlList.size()+"");
                        if(!thread) {

                            Log.e("entry through main","");
                            volleyRequest();
                        }


                  /*  if (lh.ac == 1)
                    {
                        Log.e("Array size doubled","");
                        lh = new LocHandling(lh.arraySize() * 2, lh.arrayCopy(), 0);
                    }
                    else if (lh.ac == -1)
                    {
                        Log.e("Array size reduced","");
                        int aasz=lh.arraySize();
                        if(aasz<len)
                        lh = new LocHandling(len, lh.arrayCopy(), 0);
                        else
                            lh = new LocHandling(aasz*2, lh.arrayCopy(), 0);
                    }
                    else
                    {
                        int asz=lh.arraySize();
                        Log.e("Actual array size",asz+"");
                        String urls = Url1 + "&a=" + location.getLatitude() + "&o=" + location.getLongitude() + "&s=" + location.getSpeed()+"&r=1";
                        lh.urlAdd(urls);
                        Log.e("URl ADDED","");
                      asz=lh.arraySize();
                        Log.e("Actual array size",asz+"");
                        if (asz == 1)
                        {
                            Log.e("URl Execution Started","url :"+lh.locarray[0]);

                        }

                    }
                    */














                /*

                HttpURLConnection urlConnection = null;
                try
                {
                    SharedPreferences sharedPreferences1=getSharedPreferences("busno", Context.MODE_PRIVATE);
                    String temp1= sharedPreferences1.getString("busno","10000");
                    String urls = Url1 + "&a=" + location.getLatitude() + "&o=" + location.getLongitude()+"&s="+location.getSpeed();


                    sendMessageToActivity(location,temp1+"\n"+location.getProvider()+"\nCoordinate:"+location.getLatitude()+","+location.getLongitude()+"\nHeight:"+location.getAltitude()+"\nSpeed:"+location.getSpeed()+"\n");

                   Log.e("flag",flag+"");
                    if(flag==1)
                   {
                       new GetMethodDemo().execute(urls);
                       flag=0;





                       Log.e("Status", "Added to Server Location " + location.getLatitude() + "," + location.getLongitude());
                   }
                    else
                   {
                       Log.e("Status", " Not Added to Server Location " + location.getLatitude() + "," + location.getLongitude());

                   }
                }
                catch (Exception e)
                {

                    Toast.makeText(getApplicationContext(), e.toString(), Toast.LENGTH_SHORT).show();
                } finally 
{
                    if (urlConnection != null)
                    {
                        urlConnection.disconnect();
                    }
                }

*/}

                }

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {

                }

                @Override
                public void onProviderEnabled(String provider) {

                }

                @Override
                public void onProviderDisabled(String provider) {
                    Location lc = null;
                    sendMessageToActivity(lc, "Please Enable Location");


                }
            };
            if (ContextCompat.checkSelfPermission(getApplicationContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION) == 1) {


            }
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 0, ll);
            return super.onStartCommand(intent, flags, startId);

        } catch (Exception e) {
            Log.e("error0", e.toString());
            return super.onStartCommand(intent, flags, startId);
        }

    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    public void volleyRequest()
    {
        thread=true;

        String url="";



        if(urlList.size()>=1)
        {try {

            Log.e(".......","........");

            Log.e("size at request update",urlList.size()+"");
            url = urlList.get(0);




            Log.e("Request for ", url);
            // Tag used to cancel the request

                String tag_string_req = "string_req";
                StringRequest strReq = new StringRequest(Request.Method.GET,
                        url, new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response)
                    {




                        Log.d(TAG, response.toString());
                        removeURL(response);





                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error)
                    {

                        VolleyLog.d(TAG, "Error: " + error.getMessage());
                        thread=false;

                    }
                });

// Adding request to request queue

                RequestQueue requestQueue = Volley.newRequestQueue(this);

                requestQueue.add(strReq);
            }
            catch (Exception e)
            {

                Log.e("error", e.toString());
                thread=false;
            }
        }
        else
        {
            Log.e("ERror","URL list  null");
            thread=false;
        }
    }

    private void removeURL(String response)
    {
        if(response.equals("1")) {
            urlList.remove(0);
            Log.e("size reduced", urlList.size() + "");
        }

        Log.e("entry through Rurl","");
        volleyRequest();

    }

    public class GetMethodDemo extends AsyncTask<String, Void, String> {
        String server_response;

        @Override
        protected String doInBackground(String... strings) {

            URL url;
            HttpURLConnection urlConnection = null;

            try {


                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                int responseCode = urlConnection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    server_response = readStream(urlConnection.getInputStream());
                    serverresponse = server_response;
                    Log.v("CatalogClient", server_response + " for url" + strings[0]);
                }


            } catch (Exception e) {
                Log.e("Error ", e.toString());
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            flag = 1;

            Log.e("Response", "" + server_response + " on url " + s);


        }
    }


// Converting InputStream to String

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e("Error", e.toString());
                }
            }
        }
        return response.toString();
    }

    private void sendMessageToActivity(Location l, String msg) {
        Intent intent = new Intent("GPSLocationUpdates");
        // You can also include some extra data.
        intent.putExtra("Status", msg);
        Bundle b = new Bundle();
        b.putParcelable("Location", l);
        intent.putExtra("Location", b);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }


}





