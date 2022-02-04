package com.ganilabs.weatherupdate

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_location_add.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.city_list_item.view.*

class location_add : AppCompatActivity() {

    var arrCity : ArrayList<String> = ArrayList()
//    var mySet = mutableSetOf("Mumbai","Delhi")

//    private lateinit var preferencesProvider: PreferencesProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_add)

//        preferencesProvider = PreferencesProvider(applicationContext)

//        mySet = preferencesProvider.getStringSet("keyforSet")!!

//        if(arrCity.size == 0){
//            for (item in mySet) {
//                arrCity.add(item)
//            }
//        }

        val is_da = intent.getStringExtra("is_day").toString()

        val is_day = is_da?.toInt()

        Log.d("tag3", "onCreate: check $is_da f $is_day")

        if(is_day == 1){
            //morning
            add_location_page.setBackgroundResource(R.drawable.bg_day)
            window.statusBarColor = ContextCompat.getColor(this, R.color.white)

            enter_city.setTextColor(Color.parseColor("#000000"))
            edt_loc.setTextColor(Color.parseColor("#000000"))
            edt_loc.setHintTextColor(Color.parseColor("#000000"))
            btn_add_loc.setTextColor(Color.parseColor("#000000"))
        }else{
            //night
            add_location_page.setBackgroundResource(R.drawable.bg)
            window.statusBarColor = ContextCompat.getColor(this, R.color.black)

            enter_city.setTextColor(Color.parseColor("#D7D5D5"))
            edt_loc.setTextColor(Color.parseColor("#D7D5D5"))
            edt_loc.setHintTextColor(Color.parseColor("#D7D5D5"))
            btn_add_loc.setTextColor(Color.parseColor("#D7D5D5"))
        }

        Log.d("tag2", "onCreate: c1 ")

        val ad = ArrayAdapter<String>(
            this,
            R.layout.city_list_item,
            R.id.city_txt,
            arrCity
        )
        city_list.adapter = ad

        Log.d("tag2", "onCreate: c2 ")

        btn_add_loc.setOnClickListener{
            var s : String = edt_loc.text.toString()
            if(s.equals("") || s.equals(null)){
                Toast.makeText(this,"Please write ciy name in searchbar",Toast.LENGTH_SHORT).show()
                Log.d("tag2", "onCreate: c4 ")
            }else{
                arrCity.add(s)
//                mySet.add(s)
                city_list.adapter = ad
//                preferencesProvider.putStringSet("keyforSet",mySet)
                Log.d("tag2", "onCreate: c3 ")
            }
        }

        city_list.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->

                val selectedItemText = parent.getItemAtPosition(position)
                Toast.makeText(this,selectedItemText.toString(),Toast.LENGTH_SHORT).show()

            val intent = Intent(this,MainActivity::class.java)
            intent.putExtra("samplename", selectedItemText.toString())
            startActivity(intent)
            }

    }
}