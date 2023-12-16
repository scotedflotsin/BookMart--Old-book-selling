package com.bookmart.bookmart.Activites

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.bookmart.bookmart.Android_retrofit.API_SERVICE.API_interface.ApiService
import com.bookmart.bookmart.Android_retrofit.API_SERVICE.Models.ApiClient_OperationResponse

import com.bookmart.bookmart.databinding.ActivityChangeProfilePasswordBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import render.animations.Attention
import render.animations.Render
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ChangeProfilePassword : AppCompatActivity() {
    val binding by lazy {
        ActivityChangeProfilePasswordBinding.inflate(layoutInflater)
    }
    var baseurl: String? = "https://www.onlinevideodownloader.co/"
    var email: String? = null  //setting by default null
    var checkbox: Int = 0
    private var progressDialog: ProgressDialog? = null  //progress Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
//setting verified email on emailEditText
        email = intent.getStringExtra("userEmail")
        if (email.toString().equals("")) {

        } else {
            binding.emailfieldveri.isEnabled = false
            binding.emailfieldveri.setText(email)
        }
        //handling click on change password
        binding.changepassword.setOnClickListener {
            /**
             * now here we are going to handle edittext error
             *  STEP 1: both password length should be same
             *  STEP 2: password length can not be less than 8
             *  STEP 3: both password text should be match
             *   STEP 4: finally checking all operation done before executing main function changepassword()
             * */
            if (binding.firstpasssword.text.toString().equals("")) {
                binding.firstpasssword.setError("New password?")
            }
            if (binding.secpassword.text.toString().equals("")) {
                binding.secpassword.setError("Confirm password??")
            }
            if (binding.firstpasssword.text.toString().length < 8) {
                binding.firstpasssword.setError("password length too short")
            }
            if (binding.firstpasssword.text.toString() != "" && binding.secpassword.text.toString() != "") {
                //check that password both are equal or not
                if (binding.firstpasssword.text.toString().length < 8) {
                    binding.firstpasssword.setError("password length too short")
                } else if (binding.firstpasssword.text.toString().length > 8) {
                    if (binding.firstpasssword.text.toString().trim()
                            .equals(binding.secpassword.text.toString().trim())
                    ) {
                        //check here if password matched
                        try {
                            if (checkbox == 0 || checkbox == 3) {
                                // Toast.makeText(this@SetupUserProfile,"failded",Toast.LENGTH_SHORT).show()
                                // Toast.makeText(this@SetupUserProfile,"failded",Toast.LENGTH_SHORT).show()
                                val render = Render(this@ChangeProfilePassword)
                                //for positive massage
                                render.setAnimation(Attention().Shake(binding.checkBoxprivacypolicy))
                                render.start()
                                Toast.makeText(
                                    this@ChangeProfilePassword,
                                    "check to privacy policy",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()

                            } else if (checkbox == 2) {
                                try {
                                    if (binding.emailfieldveri.text.toString().toString().equals("")) {
//if email field null
                                        Toast.makeText(
                                            this@ChangeProfilePassword,
                                            "@ERROR: Email not found",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else if (binding.emailfieldveri.text.toString() != "") {
                                        //Now we are enabling=false because we are executing for final program(change password) we don't want
                                        binding.emailfieldveri.isEnabled = false
                                        binding.firstpasssword.isEnabled = false
                                        binding.secpassword.isEnabled = false
                                        //there we are taking final step to executing our change final program
                                        changePassword()  //method for checking checkbox checked or not
                                    }
                                } catch (e: Exception) {
                                    //handle error here is failed to run change password function
                                    Toast.makeText(
                                        this@ChangeProfilePassword,
                                        "@ERROR: Exception in running change password method!",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                }


                            }
                  //      Toast.makeText(this@ChangeProfilePassword,"done",Toast.LENGTH_SHORT).show()
                        } catch (e: Exception) {
                            //handle error here is failed to run change password function
                            Toast.makeText(
                                this@ChangeProfilePassword,
                                "@ERROR: Exception in running change password method!",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    } else if (binding.firstpasssword.text.toString()
                            .trim() != binding.secpassword.text.toString().trim()
                    ) {
                        //if password does not matched
                        binding.secpassword.setError("password mismatch")

                    } else {
                        /**
                        handle problem if both condition will be false!!!!!!!! But it's impossible because
                        there in only two possible conditions here edit text can be null or not null
                         */
                        Toast.makeText(
                            this@ChangeProfilePassword,
                            "@ERROR: impossible error",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

            } else if (binding.firstpasssword.text.toString()
                    .equals("") && binding.secpassword.text.toString().equals("")
            ) {
                binding.firstpasssword.setError("New password?")
                binding.secpassword.setError("Confirm password?")

            }


        }
//handling check box status checked ot not
        binding.checkBoxprivacypolicy.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkbox = 2
                //  Toast.makeText(this@SetupUserProfile,"done",Toast.LENGTH_SHORT).show()
            } else {
                //  Toast.makeText(this@SetupUserProfile,"not done",Toast.LENGTH_SHORT).show()
                checkbox = 3
            }
        }
    }

    fun changePassword() {
        showProgressDialog()
        val email: String = binding.emailfieldveri.text.toString()
        val con_password: String = binding.secpassword.text.toString()
// Create a Retrofit instance
        val retrofit = Retrofit.Builder()
            .baseUrl(baseurl) // Replace with the actual API base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        // Create a RequestBody for the image
        val userService = retrofit.create(ApiService::class.java)

        // Make the API request
        //but first i need to

        val emailBody = RequestBody.create("text/plain".toMediaTypeOrNull(), email)
        val passBody = RequestBody.create("text/plain".toMediaTypeOrNull(), con_password)

        val call: Call<ApiClient_OperationResponse> =
            userService.update_password(emailBody, passBody)

        call.enqueue(object : retrofit2.Callback<ApiClient_OperationResponse> {
            override fun onResponse(
                call: Call<ApiClient_OperationResponse>,
                response: Response<ApiClient_OperationResponse>
            ) {

                if (response.isSuccessful) {
                    val userResponse: ApiClient_OperationResponse? = response.body()

                    if (userResponse != null) {

Toast.makeText(this@ChangeProfilePassword,userResponse.message.toString(),Toast.LENGTH_SHORT).show()
                   dismissProgressDialog()
if(userResponse.error){
  //  Toast.makeText(this@ChangeProfilePassword,"true",Toast.LENGTH_SHORT).show()
    binding.emailfieldveri.isEnabled = true
    binding.firstpasssword.isEnabled = true
    binding.secpassword.isEnabled = true
    dismissProgressDialog()
}else{
   // Toast.makeText(this@ChangeProfilePassword,"false",Toast.LENGTH_SHORT).show()
    val intent=Intent(this@ChangeProfilePassword,LoginScreen::class.java)
    intent.putExtra("forget_pass_email",binding.emailfieldveri.text.toString())
    intent.putExtra("forget_password",binding.secpassword.text.toString())
    startActivity(intent)
    dismissProgressDialog()
    finish()

}

                    } else {
                        // The response body is null, handle this as an error
                        Toast.makeText(
                            this@ChangeProfilePassword,
                            "user response null",
                            Toast.LENGTH_SHORT
                        ).show()
                        dismissProgressDialog()
                    }


                } else {
                    // Handle the case where the response is not successful (e.g., HTTP error)


                    Toast.makeText(
                        this@ChangeProfilePassword,
                        "user response null",
                        Toast.LENGTH_SHORT
                    ).show()
                    dismissProgressDialog()

                }

            }

            override fun onFailure(call: Call<ApiClient_OperationResponse>, t: Throwable) {
                // Handle network or other errors

                //   showToast("APi err " + t.message)
                Log.d("TESTINGURIFFFFF", "onFailure: " + t.message)
                binding.emailfieldveri.isEnabled = true
                binding.firstpasssword.isEnabled = true
                binding.secpassword.isEnabled = true
                dismissProgressDialog()
            }


        })


    }
    private fun showProgressDialog() {
        progressDialog = ProgressDialog(this@ChangeProfilePassword)
        progressDialog?.setTitle("changing password...")
        progressDialog?.setMessage("Authenticating to server") // Set a message
        progressDialog?.setCancelable(false) // Make it non-cancelable
        progressDialog?.show()
    }

    private fun dismissProgressDialog() {
        progressDialog?.dismiss()
    }



}