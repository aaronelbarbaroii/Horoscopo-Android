package com.example.horoscopo_android.utils

import android.content.Context


class SessionManager(context: Context) {
    val sharedPreferences = context.getSharedPreferences("horoscope.session", Context.MODE_PRIVATE)

    fun setFavorite(horoscopeId: String, claveId: String) {
        val editor = sharedPreferences.edit()
//        editor.putString("FAVORITE_HOROSCOPE_ID", horoscopeId)
        editor.putString("$claveId", horoscopeId)
        editor.apply()
    }

    fun getFavorite(claveId: String): String {
        return sharedPreferences.getString("$claveId", "")!!
    }

    fun isFavorite(horoscopeId: String, claveId: String): Boolean {
        return horoscopeId == getFavorite(horoscopeId)
    }
}