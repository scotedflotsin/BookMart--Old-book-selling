package com.bookmart.bookmart.Activites

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bookmart.bookmart.Android_retrofit.API_SERVICE.API_interface.ApiService
import com.bookmart.bookmart.Android_retrofit.API_SERVICE.Models.UserResponse
import com.bookmart.bookmart.Global_Object.Temp_location
import com.bookmart.bookmart.SharedPreffrence_Manager.UserAccountManager.SharedPreferenceManager
import com.bookmart.bookmart.databinding.ActivityLoginScreenBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginScreen : AppCompatActivity() {

    val binding by lazy {
        ActivityLoginScreenBinding.inflate(layoutInflater)
    }
    var email: String? = "null"
    var pasword: String? = "null"
    var emailIFExist: String? =
        "null" //if email exist which is coming from create acccount if email already exist on ther server database
    var baseurl: String? = "https://www.onlinevideodownloader.co/"
    private var progressDialog: ProgressDialog? = null  //progress Dialog
    var action_request_identifier: Boolean =
        false  //check for user press button for forget password or logging if true forget password else logging

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //checking status for that you are coming from forget password.kt
        email = intent.getStringExtra("forget_pass_email")
        pasword = intent.getStringExtra("forget_password")
        emailIFExist = intent.getStringExtra("emailKey")

        if (email.toString().trim().equals("null") && pasword.toString().trim().equals("null")) {
            if (emailIFExist.toString().trim() != "null") {
                binding.email.setText(emailIFExist.toString().trim())

            }
        } else if (email.toString().trim() != "null" && pasword.toString().trim() != "null") {
            binding.email.setText(email.toString().trim())
            binding.password.setText(pasword.toString().trim())
        }


        // handling other click for redirect on other new account creations ACTIVITY
        binding.createaccount.setOnClickListener {
            val intent = Intent(this@LoginScreen, NewAccountCreation::class.java)
            intent.putExtra("newAC_creation", "no")
            startActivity(intent)
            finish()
        }
        //working with forget password button
        binding.forgetpassword.setOnClickListener {
            if (action_request_identifier) {
                //visible passsword and password text
                binding.password.visibility = View.VISIBLE
                binding.passwordtext.visibility = View.VISIBLE
                action_request_identifier =
                    false   //setting false status that user can login by email and password
                binding.forgetpassword.setText("forget password?")
            } else {
                //hide password field and password title textview
                binding.passwordtext.visibility = View.GONE
                binding.password.visibility = View.GONE
                binding.login.setText("send OTP")
                action_request_identifier =
                    true //setting true status that user can enter email and forget password
                binding.forgetpassword.setText("Login via password?")
            }
        }

        //code for login user in App
        binding.login.setOnClickListener {
            if (action_request_identifier) {
                //now we can call forget password function
                if (binding.email.text.toString().equals("")) {
                    binding.email.setError("email required?")
                } else {
                    try {
                        val email: String = binding.email.text.toString()
                        forgetpassword(email)
                    } catch (e: Exception) {
                        showToast(e.localizedMessage.toString())
                    }
                }
            } else {
                showProgressDialog()
                if (binding.email.text.toString().equals("")) {
                    binding.email.setError("Enter eMail")
                }
                if (binding.password.text.toString().equals("")) {
                    binding.password.setError("Enter password")

                }

                if (binding.email.text.toString() != null && binding.password.text.toString() != null) {
                    //finally we found user entered email & password email & password not null we can start next step
                    // sending user email and password for call user login API
                    try {
                        //   showToast("creating your account")
                        loginUser(
                            binding.email.text.toString(),
                            binding.password.text.toString()
                        )
                        binding.email.isEnabled = false
                        binding.password.isEnabled = false
                    } catch (e: Exception) {
                        showToast("@ERROR:" + e.localizedMessage)
                        dismissProgressDialog()
                    }

                } else if (binding.email.text.toString()
                        .equals("") && binding.password.text.toString()
                        .equals("")
                ) {


                }

            }
        }
    }

    private fun loginUser(email: String, password: String) {
//        var email: String = email.toString()
//        var password: String = password.toString()


//        val userService = LoginApiClient.instance

        val emailBody = RequestBody.create("text/plain".toMediaTypeOrNull(), email)
        val passBody = RequestBody.create("text/plain".toMediaTypeOrNull(), password)


        val retrofit = Retrofit.Builder()
            .baseUrl(baseurl) // Replace with the actual API base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        // Create a RequestBody for the image
        val userService = retrofit.create(ApiService::class.java)

        // Make the API request
        val call: Call<UserResponse> = userService.login_user(
            emailBody, passBody
        )

        call.enqueue(object : retrofit2.Callback<UserResponse> {
            override fun onResponse(
                call: Call<UserResponse>,
                response: retrofit2.Response<UserResponse>
            ) {
                if (response.isSuccessful) {
                    val userResponse: UserResponse? = response.body()

                    if (userResponse != null) {


                        if (userResponse.error) {
                            showToast("@ERROR: SERVER RESPONSE ERROR!")
                            binding.email.isEnabled =
                                true //enabling email edit text because during progress it's was disabled
                            binding.password.isEnabled =
                                true //enabling password edit text because during progress it's was disabled
                            dismissProgressDialog()
                        } else {
                            //we got response without any error
                            // Create an instance of the SharedPreferenceManager
                            val sharedPreferenceManager =
                                SharedPreferenceManager.getInstance(this@LoginScreen)
// Save user details
                            val email: String = userResponse.user_details.Email
                            val userID: String = userResponse.user_details.ID
                            val username: String = userResponse.user_details.Username
                            val latitude: String = userResponse.user_details.Location.Latitude
                            val longitude: String = userResponse.user_details.Location.Longitude
                            val address: String = userResponse.user_details.Location.Address
                            val city: String = userResponse.user_details.Location.City
                            val state: String = userResponse.user_details.Location.State
                            val country: String = userResponse.user_details.Location.Country
                            val postalCode: String = userResponse.user_details.Location.PostalCode
                            val profileURL: String = userResponse.user_details.ProfilePicture
//                            showToast(email)
//                            showToast(userID)
//                            showToast(username)
//                            showToast(latitude)
//                            showToast(longitude)
//                            showToast(address)
//                            showToast(city)
//                            showToast(state)
//                            showToast(country)
//                            showToast(postalCode)
//                            showToast(profileURL)
                            //save user data to sharedPreference
                            if (email.toString().equals("") || userID.toString()
                                    .equals("") || username.toString()
                                    .equals("") || latitude.toString()
                                    .equals("") || longitude.toString().equals("") ||
                                address.toString().equals("") || city.toString()
                                    .equals("") || state.toString().equals("") || state.toString()
                                    .equals("") || country.toString()
                                    .equals("") || postalCode.toString()
                                    .equals("") || profileURL.toString().equals("")
                            ) {
//please handle here if response values null
                                dismissProgressDialog()
                            } else {
                                //save user data to sharedPreference
                                try {
                                    sharedPreferenceManager.saveUserDetails(
                                        email,
                                        userID,
                                        username,
                                        latitude,
                                        longitude,
                                        address,
                                        city,
                                        state,
                                        country,
                                        postalCode,
                                        profileURL
                                    )
                                    update_temp_location()
                                    redirectOnMainHome()     //here we done our login operations and successfully get full userdata so now we are redirecting on MAIN HOME activty


                                } catch (e: Exception) {
                                    showToast("@ERROR: failed to save data in SharedPreference!")
                                    binding.email.isEnabled =
                                        true //enabling email edit text because during progress it's was disabled
                                    binding.password.isEnabled =
                                        true //enabling password edit text because during progress it's was disabled
                                    dismissProgressDialog()
                                }
                                // now you can Retrieve user details
                                //                       val userDetails = sharedPreferenceManager.getUserDetails()


                            }


                        }


                    } else {
                        // The response body is null, handle this as an error
                        Toast.makeText(
                            this@LoginScreen,
                            "user response null",
                            Toast.LENGTH_SHORT
                        ).show()
                        binding.email.isEnabled =
                            true //enabling email edit text because during progress it's was disabled
                        binding.password.isEnabled =
                            true //enabling password edit text because during progress it's was disabled
                        dismissProgressDialog()
                    }


                } else {
                    // Handle the case where the response is not successful (e.g., HTTP error)
                    Toast.makeText(
                        this@LoginScreen,
                        "@ERROR: HTTP ERROR",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.email.isEnabled =
                        true //enabling email edit text because during progress it's was disabled
                    binding.password.isEnabled =
                        true //enabling password edit text because during progress it's was disabled
                    dismissProgressDialog()

                }

            }

            override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                // Handle network or other errors
                showToast("APi err " + t.message)
                Log.d("TESTINGURIFFFFF", "onFailure: " + t.localizedMessage)
                binding.email.isEnabled =
                    true //enabling email edit text because during progress it's was disabled
                binding.password.isEnabled =
                    true //enabling password edit text because during progress it's was disabled
                dismissProgressDialog()

            }
        })


    }

    fun showToast(message: String) { //function for show toast massage
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun redirectOnMainHome() {    //if user logging successfully
        val intent = Intent(this@LoginScreen, MainHome::class.java)
        startActivity(intent)
        dismissProgressDialog()
        finish()
    }

    private fun showProgressDialog() {
        progressDialog = ProgressDialog(this@LoginScreen)
        progressDialog?.setTitle("Logging...")
        progressDialog?.setMessage("Authenticating to server") // Set a message
        progressDialog?.setCancelable(false) // Make it non-cancelable
        progressDialog?.show()
    }

    private fun dismissProgressDialog() {
        progressDialog?.dismiss()
    }

    fun forgetpassword(email: String) {
        val intent = Intent(this@LoginScreen, NewAccountCreation::class.java)
        intent.putExtra("userEmail", email)
        startActivity(intent)


    }
    fun update_temp_location() {
        try {
            //instance of sha_pre....enc
            val sharedPreferenceManager = SharedPreferenceManager.getInstance(this@LoginScreen)
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