package com.example.adanapp

import android.content.Context


class LocationPrefrence (context : Context) {

        private val NAME = "Location SharedPreference"
        private val Latitude = "latitude"
        private val Longitude= "longitude"

        val preference = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)

        fun getLat(): String {
            return preference.getString(Latitude, "36.9142")
        }

        fun getLon(): String {
            return preference.getString(Longitude, "7.7427")
        }

    fun getImsak(): String {
        return preference.getString("imsak", "3:16")
    }

    fun getFadjr(): String {
        return preference.getString("fadjr", "3:27")
    }

    fun getShuruq(): String {
        return preference.getString("shuruq", "5:22")
    }

    fun getThuhr(): String {
        return preference.getString("thuhr", "12:25")
    }

    fun getAssr(): String {
        return preference.getString("assr", "16:15")
    }

    fun getMaghrib(): String {
        return preference.getString("maghrib", "19:29")
    }

    fun getIshaa(): String {
        return preference.getString("ishaa", "21:10")
    }

        fun setLocation(latitude:String , longitude:String){
            val editor = preference.edit()
            editor.putString(Latitude,latitude)
            editor.putString(Longitude,longitude)
            editor.apply()
        }

    fun setprayers (imsak : String, fadjr : String, shuruq : String,
                    thuhr : String, assr : String, maghrib : String,
                    ishaa : String) {

        val editor = preference.edit()
        editor.putString("imsak",imsak)
        editor.putString("fadjr",fadjr)
        editor.putString("shuruq",shuruq)
        editor.putString("thuhr",thuhr)
        editor.putString("assr",assr)
        editor.putString("maghrib",maghrib)
        editor.putString("ishaa",ishaa)
        editor.apply()
    }


}
