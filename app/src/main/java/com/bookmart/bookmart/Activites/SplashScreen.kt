package com.bookmart.bookmart.Activites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.bookmart.bookmart.Global_Object.Temp_location
import com.bookmart.bookmart.R
import com.bookmart.bookmart.SharedPreffrence_Manager.UserAccountManager.SharedPreferenceManager

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        // we used the postDelayed(Runnable, time) method
        // to send a message with a delayed time.
        //Normal Handler is deprecated , so we have to change the code little bit
        // Handler().postDelayed({
        Handler(Looper.getMainLooper()).postDelayed({
            //check user already logged or not
// Create an instance of the SharedPreferenceManager
            val sharedPreferenceManager = SharedPreferenceManager.getInstance(this@SplashScreen)
            val userDetails = sharedPreferenceManager.getUserDetails()
            if (userDetails.userID.toString() != "") {
                val intent = Intent(this, MainHome::class.java)
                update_temp_location()  //updating location
                startActivity(intent)
                finish()
            } else {
                val intent = Intent(this, WelcomeScreen::class.java)
                startActivity(intent)
                finish()
            }
        }, 3000) // 3000 is the delayed time in milliseconds.

    }

    fun update_temp_location() {
        try {
            //instance of sha_pre....enc
            val sharedPreferenceManager = SharedPreferenceManager.getInstance(this@SplashScreen)
            val userDetails =
                sharedPreferenceManager.getUserDetails()   //access your database basic details  email, userID, password, username, latitude, longitude, address, city, state, country, postalCode, profileURL
            // setting these retrieved data in temp_location_object
            var temp_Loction_Obj = Temp_location //object of temp_location_object
            //setting location and other data
            temp_Loction_Obj.city = userDetails.city
            temp_Loction_Obj.state = userDetails.state

        } catch (e: Exception) {
            //handle error

        }

    }
}