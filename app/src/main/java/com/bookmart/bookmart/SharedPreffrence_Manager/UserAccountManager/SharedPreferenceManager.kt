package com.bookmart.bookmart.SharedPreffrence_Manager.UserAccountManager

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceManager private constructor(context: Context) {
    private val sharedPreferences: SharedPreferences

    init {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun saveUserDetails(email: String, userID: String, username: String, latitude: String, longitude: String, address: String, city: String, state: String, country: String, postalCode: String, profileURL: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_EMAIL, email)
        editor.putString(KEY_USER_ID, userID)
        editor.putString(KEY_USERNAME, username)
        editor.putString(KEY_LATITUDE, latitude.toString())
        editor.putString(KEY_LONGITUDE, longitude.toString())
        editor.putString(KEY_ADDRESS, address)
        editor.putString(KEY_CITY, city)
        editor.putString(KEY_STATE, state)
        editor.putString(KEY_COUNTRY, country)
        editor.putString(KEY_POSTAL_CODE, postalCode)
        editor.putString(KEY_PROFILE_URL, profileURL)
        editor.apply()
    }

    fun getUserDetails(): UserModel {
        val email = sharedPreferences.getString(KEY_EMAIL, "") ?: ""
        val userID = sharedPreferences.getString(KEY_USER_ID, "") ?: ""
        val username = sharedPreferences.getString(KEY_USERNAME, "") ?: ""
        val latitude = sharedPreferences.getString(KEY_LATITUDE, "0.0")?:""
        val longitude = sharedPreferences.getString(KEY_LONGITUDE, "0.0")?:""
        val address = sharedPreferences.getString(KEY_ADDRESS, "") ?: ""
        val city = sharedPreferences.getString(KEY_CITY, "") ?: ""
        val state = sharedPreferences.getString(KEY_STATE, "") ?: ""
        val country = sharedPreferences.getString(KEY_COUNTRY, "") ?: ""
        val postalCode = sharedPreferences.getString(KEY_POSTAL_CODE, "") ?: ""
        val profileURL = sharedPreferences.getString(KEY_PROFILE_URL, "") ?: ""

        return UserModel(email, userID, username, latitude, longitude, address, city, state, country, postalCode, profileURL)
    }

    companion object {
        private const val PREF_NAME = "MyAppPreferences"
        private const val KEY_EMAIL = "email"
        private const val KEY_USER_ID = "userID"
        private const val KEY_USERNAME = "username"
        private const val KEY_LATITUDE = "latitude"
        private const val KEY_LONGITUDE = "longitude"
        private const val KEY_ADDRESS = "address"
        private const val KEY_CITY = "city"
        private const val KEY_STATE = "state"
        private const val KEY_COUNTRY = "country"
        private const val KEY_POSTAL_CODE = "postal_code"
        private const val KEY_PROFILE_URL = "profileURL"

        private var instance: SharedPreferenceManager? = null

        fun getInstance(context: Context): SharedPreferenceManager {
            if (instance == null) {
                instance = SharedPreferenceManager(context)
            }
            return instance!!
        }
    }
}
