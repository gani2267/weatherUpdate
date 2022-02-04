package com.ganilabs.weatherupdate;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class location_work {

    public void work(Context c , Activity a){

        // location access permission
        try{
            int perm = ContextCompat.checkSelfPermission(c, Manifest.permission.ACCESS_FINE_LOCATION);
            if (perm == PackageManager.PERMISSION_GRANTED) {

            } else {
                ActivityCompat.requestPermissions(
                        a,
                        new String[] {
                                Manifest.permission.ACCESS_FINE_LOCATION
                        },
                        121
                );
            }
        }catch (Exception e){

        }

        // if android location is off

    }
}
