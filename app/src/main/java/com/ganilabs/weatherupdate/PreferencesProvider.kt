package com.ganilabs.weatherupdate

import android.content.Context

class PreferencesProvider (context: Context) {

    private val sharedPreferences = context.getSharedPreferences("myPreferences",0)

    fun putStringSet( key : String , value : Set<String>){
        sharedPreferences.edit().putStringSet(key,value).apply()
    }

    fun getStringSet(key: String): MutableSet<String>? {
        return sharedPreferences.getStringSet(key,null)
    }
}