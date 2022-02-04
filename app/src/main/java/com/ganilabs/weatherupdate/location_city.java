package com.ganilabs.weatherupdate;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Locale;

public class location_city {
    public Double latitude;
    public Double longitude;

    @NotNull
    public String getCityName(Double longitude, Double latitude, Context baseContext) {
        String cityName = "Not Found";
        String TAG = "tag";
        Log.d(TAG, "getCityName: check 1");
        Geocoder gcd = new Geocoder(baseContext, Locale.getDefault());
        try{
            Log.d(TAG, "getCityName: check 2");
            List<Address> addresses = gcd.getFromLocation(latitude,longitude,10);
            for(Address adr : addresses){
                if(adr != null){
                    String city = adr.getLocality();
                    Log.d(TAG, "getCityName: check 3");
                    if(city!= null && !city.equals("")){
                        cityName = city;
                    }
                }else {
                    Toast.makeText(baseContext, "User City Not Found", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "getCityName: check 4");
                }
            }
        }catch (Exception e){
            Log.d(TAG, "getCityName: check 5");
        }

        return cityName;
    }
}
