package edu.bluejack21_1.SunibTinder

import android.content.Context

class SharedPrefConfig (context: Context){
    private val sharedPreferences = context.getSharedPreferences("myPreferences", 0)

//    sharedPref.putInt("MinAge", 0)
//    sharedPref.putInt("MaxAge", 100)
//    sharedPref.putString("FullName", personName)
//    sharedPref.putString("Email", personEmail)
//    sharedPref.putBoolean("IsGoogle",true)
//    sharedPref.putString("Preferences", "Same Campus")


    fun putString(key: String, value : String){
        sharedPreferences.edit().putString(key, value).apply()
    }

    fun putInt(key: String, value : Int){
        sharedPreferences.edit().putInt(key, value).apply()
    }

    fun putBoolean(key: String, value : Boolean){
        sharedPreferences.edit().putBoolean(key, value).apply()
    }

    fun getString(key: String) : String? {
        return sharedPreferences.getString(key, "")
    }

    fun getInt(key: String) : Int? {
        return sharedPreferences.getInt(key, -1)
    }

    fun getBoolean(key: String) : Boolean? {
        return sharedPreferences.getBoolean(key, false)
    }

    fun getNotification() : String? {
        return sharedPreferences.getString("Notification", "false")
    }

    fun clearSharedPreference() : Boolean{
        return sharedPreferences.edit().clear().commit()
    }

    fun clearOneSharedPreference(key : String) : Boolean {
        return sharedPreferences.edit().remove(key).commit()
    }
}