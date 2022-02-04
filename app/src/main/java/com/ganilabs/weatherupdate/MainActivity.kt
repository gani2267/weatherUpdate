package com.ganilabs.weatherupdate

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.ColorStateList
import android.graphics.Color
import android.location.LocationListener
import android.location.LocationManager
import android.net.ConnectivityManager
import android.os.AsyncTask
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_location_add.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.daily_update_item.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.net.URL
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.log
import kotlin.math.roundToInt


class MainActivity : AppCompatActivity() {

    val six_item_api = "1160bde705015a37152aeb07a6796a59"
    public var city = "kolhapur"
    val main_api = "b3cf8b5bb70847718a2145028223101"
    var time_hrrrr : Int = 0

    var page_default = true
    var arr : ArrayList<hourly_update> = ArrayList()
    var brr : ArrayList<daily_update> = ArrayList()
    var is_day : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(page_default == true) {
        val w1 : location_work = location_work()
        w1.work(this,this)

        internet_work()
        location_work2()
            getting_location()
            city = locationCity.getCityName(locationCity.longitude, locationCity.latitude, baseContext)
        }

        val ss: String = intent.getStringExtra("samplename").toString()
        if(!ss.equals("null")) {
            city = ss
        }

        current_location_text.text = city

        update_current_values()
        update_forecast_values()

        start_rec_view()

        getExtraDetails()
    }

    private fun getExtraDetails() {

        refreshLayout.setOnRefreshListener {

            if(page_default == true) {
                val w1 : location_work = location_work()
                w1.work(this,this)

                internet_work()
                location_work2()
                getting_location()
                city = locationCity.getCityName(locationCity.longitude, locationCity.latitude, baseContext)
            }

            val ss: String = intent.getStringExtra("samplename").toString()
            if(!ss.equals("null")) {
                city = ss
            }

            current_location_text.text = city

            update_current_values()
            update_forecast_values()

            start_rec_view()

            getExtraDetails()

            refreshLayout.isRefreshing = false
        }

        add_location.setOnClickListener {
            val intent = Intent(this,location_add::class.java)
            intent.putExtra("is_day",is_day.toString())
            startActivity(intent)
        }

        current_location.setOnClickListener {
            Log.d("ragew", "getExtraDetails: check1")
            val w1 : location_work = location_work()
            w1.work(this,this)

            internet_work()
            location_work2()
            getting_location()
            city = locationCity.getCityName(locationCity.longitude, locationCity.latitude, baseContext)

            current_location_text.text = city

            update_current_values()
            update_forecast_values()

            start_rec_view()

            getExtraDetails()
        }
    }

    private fun start_rec_view() {
        var hra : hourlyUpdateAdapter = hourlyUpdateAdapter(arr)
        hourly.adapter = hra

        var dl : dailyUpdateAdapter = dailyUpdateAdapter(brr)
        daily.adapter = dl
    }

    private fun update_forecast_values() {
        val url : String = "https://api.weatherapi.com/v1/forecast.json?key=b3cf8b5bb70847718a2145028223101&q=$city&days=6&aqi=no&alerts=no"

        Log.d("tag", "update_forecast_values: dd1")
        val requestQueue : RequestQueue = Volley.newRequestQueue(this)

        val jsonObjectRequest : JsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url,
            null, // json request
            Response.Listener
            { response -> // response listener
                try {
                    val obj: JSONObject = response

                    val forecastDay : JSONArray = obj.getJSONObject("forecast").getJSONArray("forecastday")

                    val min_temp = forecastDay.getJSONObject(0).getJSONObject("day").getString("mintemp_c")
                    val max_temp : String = forecastDay.getJSONObject(0).getJSONObject("day").getString("maxtemp_c")
                    val min : Int = min_temp.toDouble().roundToInt()
                    val max : Int = max_temp.toDouble().roundToInt()
                    temp_min_max.text = min.toString() + "°C/" + max.toString() + "°C"

                    val hr_array : JSONArray = forecastDay.getJSONObject(0).getJSONArray("hour")
                    var i : Int = time_hrrrr
                    while (arr.size < 25 && i<hr_array.length()){
                        val hr_obj : JSONObject = hr_array.getJSONObject(i)
                        var time : String = hr_obj.getString("time")
                        val icon : String = hr_obj.getJSONObject("condition").getString("icon")
                        var tempString : String = hr_obj.getString("temp_c")

                        time = time.substring(time.length-5)

                        var tempInt : Int = tempString.toDouble().roundToInt()
                        val hu : hourly_update = hourly_update(time,icon,tempInt,is_day)
                        arr.add(hu)

                        i += 1
                    }

                    i=0;
                    while (arr.size < 25){
                        val hr_array2 :  JSONArray = forecastDay.getJSONObject(1).getJSONArray("hour")
                        val hr_obj : JSONObject = hr_array2.getJSONObject(i)
                        var time : String = hr_obj.getString("time")
                        val icon : String = hr_obj.getJSONObject("condition").getString("icon")
                        var tempString : String = hr_obj.getString("temp_c")

                        time = time.substring(time.length-5)

                        var tempInt : Int = tempString.toDouble().roundToInt()
                        val hu : hourly_update = hourly_update(time,icon,tempInt,is_day)
                        arr.add(hu)

                        i += 1
                    }

                    val dl_arrray : JSONArray = obj.getJSONObject("forecast").getJSONArray("forecastday");
                    var k = 0
                    while (brr.size < 3 ){
                        val dl_obj : JSONObject = dl_arrray.getJSONObject(k)
                        var dl_status : String = dl_obj.getJSONObject("day").getJSONObject("condition").getString("text")
                        var dl_icon : String = dl_obj.getJSONObject("day").getJSONObject("condition").getString("icon")

                        Log.d("tag", "update_forecast_values: check $i")
                        var dl_min : String = dl_obj.getJSONObject("day").getString("mintemp_c")
                        var dl_max : String = dl_obj.getJSONObject("day").getString("maxtemp_c")
                        var dl_min_max : String = dl_min.toDouble().roundToInt().toString() + "/" + dl_max.toDouble().roundToInt().toString() + "°C"

                        var dates : String = dl_obj.getString("date")
                        if(k==0){
                            dates = "Today"
                        }else if(k==1){
                            dates = "Tomorrow"
                        }else if(k==2){
                            dates = "Day Later"
                        }

                        var du : daily_update = daily_update(dates,dl_icon,dl_status,dl_min_max,is_day)
                        brr.add(du)

                        k += 1
                    }

                }catch (e: JSONException){

                }
            },
            Response.ErrorListener { error -> // error listener
                error.printStackTrace()

            }
        )

        requestQueue.add(jsonObjectRequest)
        requestQueue.start()


    }

    private fun update_current_values() {
        val task1 = six_item_task()
        task1.execute()

        current_task()
    }

    private fun current_task() {
        val url : String = "https://api.weatherapi.com/v1/current.json?key=b3cf8b5bb70847718a2145028223101&q=$city&aqi=no"

        val requestQueue : RequestQueue = Volley.newRequestQueue(this)

        val jsonObjectRequest : JsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url,
            null, // json request
            Response.Listener
            { response -> // response listener
                try {
                    val obj: JSONObject = response

                    var timeval : String = obj.getJSONObject("location").getString("localtime")
                    if(timeval.substring(12,13).equals(":")){
                        time_hrrrr = timeval.substring(11,12).toInt()
                        timeval = "Updated : " + timeval.substring(0,11) + "   at : 0" + timeval.substring(11)
                    }else {
                        time_hrrrr = timeval.substring(11, 13).toInt()
                        timeval = "Updated : " + timeval.substring(0,11) + "   at : " + timeval.substring(11)
                    }
                    updated_at.text = timeval

                    val icon : String = obj.getJSONObject("current").getJSONObject("condition").getString("icon")
                    Picasso.get().load("https:"+icon).into(curr_img)

                    val tempval : String = obj.getJSONObject("current").getString("temp_c")
                    temp.text = tempval.toDouble().roundToInt().toString() + "°C"

                    val statusval : String = obj.getJSONObject("current").getJSONObject("condition").getString("text")
                    status.text = statusval

                    is_day = obj.getJSONObject("current").getInt("is_day")

                    if(is_day == 1){
                        //morning
                        refreshLayout.setBackgroundResource(R.drawable.bg_day)
                        window.statusBarColor = ContextCompat.getColor(this, R.color.white)
                        set_day_text_color()
                    }else{
                        //night
                        refreshLayout.setBackgroundResource(R.drawable.bg)
                        window.statusBarColor = ContextCompat.getColor(this, R.color.black)
                        set_night_text_color()
                    }

                }catch (e: JSONException){

                }
            },
            Response.ErrorListener { error -> // error listener
                error.printStackTrace()
            }
        )

        requestQueue.add(jsonObjectRequest)
        requestQueue.start()


    }

    private fun set_day_text_color() {
        current_location_text.setTextColor(Color.parseColor("#000000"))
        updated_at.setTextColor(Color.parseColor("#000000"))
        status.setTextColor(Color.parseColor("#000000"))
        temp.setTextColor(Color.parseColor("#000000"))
        temp_min_max.setTextColor(Color.parseColor("#000000"))

        current_location.setColorFilter(Color.parseColor("#000000"))
        add_location.setColorFilter(Color.parseColor("#000000"))

        sunrise.setTextColor(Color.parseColor("#000000"))
        sunrise_text.setTextColor(Color.parseColor("#000000"))
        sunset.setTextColor(Color.parseColor("#000000"))
        sunset_text.setTextColor(Color.parseColor("#000000"))
        wind.setTextColor(Color.parseColor("#000000"))
        wind_text.setTextColor(Color.parseColor("#000000"))
        pressure.setTextColor(Color.parseColor("#000000"))
        pressure_text.setTextColor(Color.parseColor("#000000"))
        humidity.setTextColor(Color.parseColor("#000000"))
        humidity_text.setTextColor(Color.parseColor("#000000"))
    }

    private fun set_night_text_color() {
        current_location_text.setTextColor(Color.parseColor("#D7D5D5"))
        updated_at.setTextColor(Color.parseColor("#D7D5D5"))
        status.setTextColor(Color.parseColor("#D7D5D5"))
        temp.setTextColor(Color.parseColor("#D7D5D5"))
        temp_min_max.setTextColor(Color.parseColor("#D7D5D5"))

        current_location.setColorFilter(Color.parseColor("#D7D5D5"))
        add_location.setColorFilter(Color.parseColor("#D7D5D5"))

        sunrise.setTextColor(Color.parseColor("#D7D5D5"))
        sunrise_text.setTextColor(Color.parseColor("#D7D5D5"))
        sunset.setTextColor(Color.parseColor("#D7D5D5"))
        sunset_text.setTextColor(Color.parseColor("#D7D5D5"))
        wind.setTextColor(Color.parseColor("#D7D5D5"))
        wind_text.setTextColor(Color.parseColor("#D7D5D5"))
        pressure.setTextColor(Color.parseColor("#D7D5D5"))
        pressure_text.setTextColor(Color.parseColor("#D7D5D5"))
        humidity.setTextColor(Color.parseColor("#D7D5D5"))
        humidity_text.setTextColor(Color.parseColor("#D7D5D5"))

    }

    inner class six_item_task() : AsyncTask<String, Void, String>(){

        override fun doInBackground(vararg p0: String?): String {
            var response:String?
            try{
                response = URL("https://api.openweathermap.org/data/2.5/weather?q=$city&units=metric&appid=$six_item_api").readText(
                    Charsets.UTF_8
                )
            }catch (e: Exception){
                response = null
            }
            return response.toString()
        }

        override fun onPostExecute(result: String?) {
            super.onPostExecute(result)

            try{
                val j = JSONObject(result)

                val sys = j.getJSONObject("sys")
                val su:Long = sys.getLong("sunrise")
                val sn:Long = sys.getLong("sunset")
                sunrise.text = SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(Date(su*1000))
                sunset.text = SimpleDateFormat("hh:mm a",Locale.ENGLISH).format(Date(sn*1000))

                val wd = j.getJSONObject("wind")
                val wdspeed = wd.getString("speed")
                wind.text = wdspeed + "m/s"

                val prs = j.getJSONObject("main").getString("pressure")
                pressure.text = prs + "hPa"

                val hmd = j.getJSONObject("main").getString("humidity")
                humidity.text = hmd + "%"

            }catch (e : java.lang.Exception){

            }
        }

    }

    var isNetworkOn :Boolean ?= false
    val locationCity : location_city = location_city()

    private fun getting_location() {
        val locationManager : LocationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (isNetworkOn == true) {
            //check the network permission
            val MIN_TIME_BW_UPDATES : Long = 5000
            val MIN_DISTANCE_CHANGE_FOR_UPDATES = 10F

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER,
                MIN_TIME_BW_UPDATES,
                MIN_DISTANCE_CHANGE_FOR_UPDATES, LocationListener {  }
            )
            Log.d("Network", "Network")
            if (locationManager != null) {
                val location = locationManager
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
                if (location != null) {
                    locationCity.latitude = location.getLatitude()
                    locationCity.longitude = location.getLongitude()
                }
            }
        }
    }


    private fun location_work2() {
        val loc :LocationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        var gps_enabled : Boolean ?= false
        try {
            gps_enabled = loc.isProviderEnabled(LocationManager.GPS_PROVIDER)
        }catch (e: Exception){

        }

        // if gps not enabled
        if(gps_enabled == false){
            AlertDialog.Builder(this@MainActivity)
                .setMessage("GPS Enable")
                .setPositiveButton("Settings",
                    DialogInterface.OnClickListener { paramDialogInterface, paramInt ->
                        startActivity(
                            Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                        )
                    })
                .setNegativeButton("Cancel", null)
                .show()
        }
    }

    private fun internet_work() {
        val cm = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = cm.activeNetworkInfo

        val isConnected = networkInfo != null && networkInfo.isConnected
        isNetworkOn = isConnected
        if(isConnected == false) {
            Toast.makeText(this, "Please Turn on Network", Toast.LENGTH_SHORT).show()
        }
    }
}