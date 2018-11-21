package com.example.hppc.driver;

import android.location.Location;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by hp pc on 16-06-2017.
 */

public class Locations
{

    HashMap<String,Location> loc=new HashMap<String,Location>();
    Location location=new Location("GPS");
   public Locations()
    {

        put("Kashi" ,25.331143 ,83.017830);
        put("Lanka" ,25.2783895,82.9979296);
        put("RAVINDRAPURI",25.289394, 83.001537);
        put("KAMACHHA",25.299936, 83.001382);
        put("RATHYATRA",25.306977, 82.991685);
        put("SIGRA",25.310472, 82.989477);
        put("Vidyapeeth",25.320883, 82.987772);
        put("CANTT",25.325170, 82.987055);
        put("CHAUKAGHAT",25.333242, 82.997956);
        put("Golgadda",25.331188, 83.017816);
        put("Kajjakpura",25.329318, 83.026429);
        put("Bhadau",25.326896, 83.029687);
        put("Rajghat",25.326543, 83.034712);
        put("CV raman",25.265992, 82.986017);

       put("Electrical Engineering Department, IIT BHU",25.262064,82.992392);

        put("Civil Engineering Department, IIT BHU",25.262940,82.992076);

        put("Rampur Lawn, IIT BHU",25.262940,82.992076);


        put("Civil Agriculture Tiraha, IIT BHU",25.263459,82.991933);

        put("ABLT Chauraha, IIT BHU",25.263369,82.989638);

        put("DG Corner, IIT BHU",25.263177,82.986457);

        put("Aryabhatta Entry Gate, IIT BHU",25.263074,82.984339);

        put("Mechanical Engineering Department, IIT BHU",25.261645,82.991721);
        put("Limbdi Chauraha",25.260691, 82.986853);
        put("Limbdi Corner",25.260691, 82.986853);
        put("Limbdi Hostel Entry Gate",25.261372, 82.986746);
        put("Rajputana Hostel Entry Gate",25.262436, 82.986493);
        put("Ramanujan Entry Gate",25.262436, 82.986493);
        put("Hyderabad Gate",25.262940, 82.981908);
      //  put("Aryabhatta Hostel,IIT BHU",25.264674, 82.984133);
        put("Morvi Hostel Entry Gate",25.265546, 82.986588);
        put("Gurtu Hostel",25.267262, 82.987030);

        put("BhagwaanDas Hostel",25.267965, 82.987265);

        put("SR 4 RR 7 chauraha",25.269075, 82.987752);

        put("Sunderlal Chauraha",25.269075, 82.987752);

        put("MMV",25.276932, 83.001732);
        put("IMS BHU",25.275679, 82.999548);

        put("Dalmia Hostel Entry Gate",25.270769, 82.988881);
        put("Brocha Hostel Entry Gate",25.272071, 82.990009);
        put("Birla Chauraha",25.272071, 82.990009);
      //  put("Lanka Gate",25.277683, 83.002186);

       // put("Pehalwaan Lassi ",25.281929, 83.003053);

       // put("Ming Garden",25.290199, 83.001553);
       // put("Crystal Bowl",25.290617, 83.001693);
        //put("Crescent Villa",25.294227, 83.002368);
        //put("Aman Restaurant",25.296820, 83.002482);
        //put("Zaika Restaurant",25.290200, 83.001563);
        put("Vijay Chauraha",25.297019, 83.001277);
        put("IP vijaya",25.297019, 83.001277);
       // put("Kammachha ",25.303087,82.9940943);
       // put("RathYatra Crossing",25.306941,82.9894983);
      //  put("Sigra",25.310449,82.9872283);
        put("Railway Station",25.324581,82.9845843);
        put("DurgaKund Bus Stop",25.289389,82.9996997);



    }
    public void put(String name,Double lat,Double lon)
    {
        Location location_temp=new Location("GPS");
        location_temp.setLatitude(lat);
        location_temp.setLongitude(lon);
        loc.put(name,location_temp);

    }
    public String GetName(Location location)
    {

        int f=0;
       // List<String> list=new ArrayList<>();
        String list="Not Known";
       for(Map.Entry m:loc.entrySet())
       {
          // Log.e("local:",(String)m.getKey()+"  "+" distance is "+location.distanceTo((Location) m.getValue()));
           if(location.distanceTo((Location) m.getValue())<=100)
           {

               list=(String) m.getKey();
               break;
           }

       }
        return list;

    }
}
