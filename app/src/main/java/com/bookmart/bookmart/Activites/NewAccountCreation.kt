package com.bookmart.bookmart.Activites

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import com.bookmart.bookmart.BasicTools.OTP_genrater_six_digit
import com.bookmart.bookmart.R
import com.bookmart.bookmart.databinding.ActivityNewAccountCreationBinding
import com.bookmart.bookmart.volly_Android_networking.USER_API_SERVICES.Check_user_email_exist_or_not
import com.bookmart.bookmart.volly_Android_networking.USER_API_SERVICES.EmailSender

import com.facebook.CallbackManager
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import render.animations.Attention
import render.animations.Render

class NewAccountCreation : AppCompatActivity() {
    val binding by lazy {
        ActivityNewAccountCreationBinding.inflate(layoutInflater)
    }
    var email_lo:String?=null  //login otp email
    var newAC_creation:String?="no"  //login otp email
    private lateinit var callbackManager: CallbackManager
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    val totalTimeInMillis: Long = 120000 // Total time for the timer (60 seconds)
    val intervalInMillis: Long = 1000  // Interval to update the UI (1 second)
    var random6DigitNumber: Int? = null//otp
    var status_Identifier: Int = 10;
    private lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

    email_lo = intent.getStringExtra("userEmail")
        newAC_creation = intent.getStringExtra("newAC_creation")
        if (newAC_creation.toString().equals("no")){
email_lo=""
        } else if(email_lo.toString()!="") {
            binding.editTextTextEmailAddress.isEnabled = false
            binding.sendotp.isEnabled = false
            binding.editTextTextEmailAddress.setText(email_lo.toString())
            binding.googleSignIn.visibility = View.GONE
            binding.facebboklogin.visibility = View.GONE
            binding.textView3.visibility = View.GONE
            binding.textView10.visibility = View.VISIBLE
            prapareOtp()
        }


        //basic required settings
        binding.editTextTextEmailAddress.requestFocus()
        auth = FirebaseAuth.getInstance()
        configureGoogleSignIn()
        //handling click on already have account
        binding.loginback.setOnClickListener {
            redirect_onLogin_page()
        }
        binding.newloginbutton.setOnClickListener {
            redirect_onLogin_page()
        }
        binding.signupwithgoogle.setOnClickListener {
            binding.emaillayout.visibility = View.VISIBLE
            binding.otplayout.visibility = View.GONE
            binding.othersignupoption.visibility = View.VISIBLE
            binding.editTextTextEmailAddress.setText("")
            binding.sendotp.setText("send OTP")
            status_Identifier = 10
        }
        // Initialize the timer with a total time in milliseconds and a time interval
        // Hide progress and show "Submit" text instead
        //   binding.sendotp.hideProgress(R.string.loading)
        binding.status.setOnClickListener {
            if (email_lo.toString().equals("")){
                if (status_Identifier == 10) {
                    binding.emaillayout.visibility = View.VISIBLE
                    binding.otplayout.visibility = View.GONE
                    binding.othersignupoption.visibility = View.VISIBLE
                    binding.editTextTextEmailAddress.setText("")
                    binding.editTextTextEmailAddress.requestFocus()
                    binding.sendotp.setText("send OTP")
                    status_Identifier = 10
                } else if (status_Identifier == 123) {
                    prapareOtp()

                }
            } else if(email_lo.toString()!=""){
                if (status_Identifier == 10) {
                    Toast.makeText(this@NewAccountCreation,"this is run",Toast.LENGTH_LONG).show()
                   finish()
                } else if (status_Identifier == 123) {
                    prapareOtp()

                }
            }




        }
        binding.verify.setOnClickListener {
            verifyOtp()
        }
        binding.sendotp.setOnClickListener {
            try{

            }catch (e:Exception){
                Toast.makeText(this@NewAccountCreation,e.localizedMessage,Toast.LENGTH_SHORT).show()
            }
            checkEmailEXIT_OR_NOT(binding.editTextTextEmailAddress.text.toString().trim(),true)
        }
        binding.googleSignIn.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, RC_SIGN_IN)
            // Initialize the handler to update the progress bar on the main thread
            binding.progressBar2.visibility = View.VISIBLE

        }

        binding.facebboklogin.setOnClickListener {
            Toast.makeText(this@NewAccountCreation, "temperery disabled by us", Toast.LENGTH_SHORT)
                .show()
        }


    }

    private fun redirect_onLogin_page() {
        try {
            val intent = Intent(this@NewAccountCreation, LoginScreen::class.java)
            startActivity(intent)
            finish()
        }catch (e:Exception){
            showToastMassage(e.localizedMessage)
        }
    }

    private fun configureGoogleSignIn() {
      try {
          val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
              .requestIdToken(getString(R.string.default_web_client_id))
              .requestEmail()
              .build()
          googleSignInClient = GoogleSignIn.getClient(this, gso)
      }catch (e:Exception){
          binding.progressBar2.visibility=View.GONE
      }

    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Handle sign-in failure
                binding.progressBar2.visibility=View.GONE
            }
        } else {
            callbackManager.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this@NewAccountCreation,
                        "Email verification successful",
                        Toast.LENGTH_SHORT
                    ).show()
                    val userdat = Firebase.auth.currentUser
                    val useremail = userdat?.email.toString()
                    val userimageUrl = userdat?.photoUrl.toString()
                    val displayname = userdat?.displayName.toString()

                    if (useremail != null) {
                   //     afterEMailVerification(useremail.toString().trim())
                        checkEmailEXIT_OR_NOT(useremail.toString().trim(),false)
                    } else {
                        Toast.makeText(
                            this@NewAccountCreation,
                            "some went wrong please try email method.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


                    binding.progressBar2.visibility = View.GONE
                    Log.d("demodetail?", userimageUrl + "" + useremail + "" + displayname)
                } else {
binding.progressBar2.visibility=View.GONE
                }
            }
    }

    companion object {
        private const val RC_SIGN_IN = 9001
    }

    public fun sendOTP(otpCode: String) {

        //Toast.makeText(this@NewAccountCreation, otpCode.toString(), Toast.LENGTH_SHORT).show()
        //preparing for send otp
        var userEmail: String = binding.editTextTextEmailAddress.text.toString().trim()
        val emailSender = EmailSender(this@NewAccountCreation)
        emailSender.sendEmail(
            "Old book mart",
            userEmail.toString().trim(),
            otpCode.toString(),
            object : EmailSender.EmailSendListener {
                override fun onSuccess(response: String) {
                    //    Toast.makeText(this@NewAccountCreation,response.toString(),Toast.LENGTH_LONG).show()
                    if (response.toString().contains("Message has been sent")) {
                        binding.sendotp.hideProgress(R.string.finishotpsend)
                        showToastMassage(response.toString())
                        binding.emaillayout.visibility = View.GONE
                        binding.othersignupoption.visibility = View.GONE
// Create Render Class
                       // val render = Render(this@NewAccountCreation)
// Set Animation
                    //    render.setAnimation(Rotate().In(binding.otplayout))
                        binding.otplayout.visibility = View.VISIBLE
                        binding.otp1.requestFocus()
                    //    render.start()
                        optLayout()
                        binding.status.text = "change email ID"
                        binding.previewemail.setText(
                            "send to " + binding.editTextTextEmailAddress.text.toString()
                                .trim() + "..."
                        )
                        binding.otp1.requestFocus()
                        otpTimer.start()


                    } else {
                        negativeToast(response.toString())
                        binding.sendotp.hideProgress(R.string.failedtosendotp)
                    }
                }

                override fun onError(error: String) {
                    // Handle the error here
                    negativeToast(error.toString().trim())
                    binding.sendotp.hideProgress(R.string.failedtosendotp)
                }
            }
        )
    }

    // should be pass "positive" if massage positive, if negation than should be pass negative
    fun showToastMassage(toastTitle: String) {
        val render = Render(this@NewAccountCreation)
        //for positive massage
        binding.toastbody.visibility = View.VISIBLE
        render.setAnimation(Attention().Shake(binding.toastmassage))
        binding.toastmassage.text = toastTitle
        render.start()
        Handler(Looper.getMainLooper()).postDelayed({
            binding.toastmassage.visibility = View.GONE
        }, 1000) // 3000 is the delayed time in milliseconds.     render.start()

    }

    fun optLayout() {

        binding.otp1.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.otp2.requestFocus()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        binding.otp2.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.otp3.requestFocus()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        binding.otp3.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.otp4.requestFocus()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        binding.otp4.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                binding.otp5.requestFocus()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        binding.otp5.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                //  verifyOtp()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    val otpTimer = object : CountDownTimer(totalTimeInMillis, intervalInMillis) {
        override fun onTick(millisUntilFinished: Long) {
            // Update UI with the remaining time (e.g., show the countdown on a TextView)
            val secondsRemaining = millisUntilFinished / 1000
            binding.timer.text = "$secondsRemaining seconds"
        }

        override fun onFinish() {
            // Perform actions when the timer finishes (e.g., show a message or enable resend)
            binding.status.text = "resend it"
            status_Identifier = 123
            prapareOtp()
showToastMassage("otp resend successfully")
            // Enable the "Resend OTP" button or perform any other action.
        }
    }

    private fun verifyOtp() {
        val op1 = binding.otp1.text.toString().trim()
        val op2 = binding.otp2.text.toString().trim()
        val op3 = binding.otp3.text.toString().trim()
        val op4 = binding.otp4.text.toString().trim()
        val op5 = binding.otp5.text.toString().trim()
        val finalotp = op1 + op2 + op3 + op4 + op5
        // Toast.makeText(this@NewAccountCreation, finalotp.toString().trim(), Toast.LENGTH_SHORT).show()
        // bind your button to activity lifecycle
        bindProgressButton(binding.verify)
        // (Optional) Enable fade In / Fade out animations
        binding.verify.attachTextChangeAnimator()
        // Show progress with "Loading" text
        binding.verify.showProgress {
            buttonTextRes = R.string.verifying
            progressColor = Color.WHITE
        }

        if (random6DigitNumber.toString().equals(finalotp)) {
            // binding.verify.hideProgress("done")
            Toast.makeText(
                this@NewAccountCreation,
                "Email verification successful",
                Toast.LENGTH_SHORT
            ).show()
            //  showToastMassage("Email verification success")
                binding.timer.visibility = View.GONE
                afterEMailVerification(binding.editTextTextEmailAddress.text.toString().trim())






        } else {
            binding.verify.hideProgress("Try again")
            val render = Render(this@NewAccountCreation)
            render.setAnimation(Attention().Shake(binding.otpcontsiner))
//            binding.otp1.requestFocus()
            resetTextViewData()
            render.start()
            negativeToast("Otp mismatch try again or choose other method to signup")

        }

    }

    fun negativeToast(toastTitle: String) {
        binding.toastmassagene.visibility = View.VISIBLE
        val render = Render(this@NewAccountCreation)
        render.setAnimation(Attention().Shake(binding.toastmassagene))
        binding.toastmassagene.text = toastTitle
        binding.toastbodyne.visibility = View.VISIBLE
        render.start()
        Handler(Looper.getMainLooper()).postDelayed({
            binding.toastmassagene.visibility = View.GONE
        }, 2000)


    }

    private fun resetTextViewData() {
        binding.otp1.setText("")
        binding.otp2.setText("")
        binding.otp3.setText("")
        binding.otp4.setText("")
        binding.otp5.setText("")
        binding.otp1.requestFocus()

    }

    private fun prapareOtp() {
        // bind your button to activity lifecycle
        bindProgressButton(binding.sendotp)
        // (Optional) Enable fade In / Fade out animations
        binding.sendotp.attachTextChangeAnimator()
        // Show progress with "Loading" text
        binding.sendotp.showProgress {
            buttonTextRes = R.string.loading
            progressColor = Color.WHITE
        }
        val randomNumberGenerator = OTP_genrater_six_digit()
        // Generate and print a random 6-digit number
        random6DigitNumber = randomNumberGenerator.generateRandomNumber()
        sendOTP(random6DigitNumber.toString())
    }

    private fun afterEMailVerification(email: String) {
//Toast.makeText(this@NewAccountCreation,newAC_creation,Toast.LENGTH_SHORT).show()
        if(newAC_creation.toString().equals("no")){
            val intent = Intent(this@NewAccountCreation, SetupUserProfile::class.java)
            intent.putExtra("userEmail", email)
            startActivity(intent)
            finish()
        }else if(email_lo.toString()!=""){
            val intent = Intent(this@NewAccountCreation, ChangeProfilePassword::class.java)
            intent.putExtra("userEmail", email)
            startActivity(intent)
            finish()
        }


    }
    fun checkEmailEXIT_OR_NOT(email:String,opretion_IDENTI:Boolean){
        /**
         * if opretion_IDENTI = true // means request coming from email edit text aimple
         *    else
         *    operation_IDENTI = false //means email coming from google verification
        * */



      //  var userEmail: String = binding.editTextTextEmailAddress.text.toString().trim()
        val checkUserEmailExistOrNot = Check_user_email_exist_or_not(this@NewAccountCreation)
        checkUserEmailExistOrNot.Email_check(
            email,
            object : Check_user_email_exist_or_not.EmailSendListener {
                override fun onSuccess(response: String) {
                    //    Toast.makeText(this@NewAccountCreation,response.toString(),Toast.LENGTH_LONG).show()
                  if (response.toString().contains("true")) {
                      //h=now finally we are sending otp
                      if(opretion_IDENTI){
                          // send otp i am sure otp coming from email edit text
                          try {
                              prapareOtp()
                          }catch (e:Exception){
                              showToastMassage(e.localizedMessage)

                          }
                      }else{
                          try {
                              afterEMailVerification(email.toString().trim())
                          }catch (e:Exception){
                              showToastMassage(e.localizedMessage)
                              binding.progressBar2.visibility=View.GONE
                          }
                      }



          } else if(response.toString().contains("false")){
              //redirecting on login if email already exist.
             Toast.makeText(this@NewAccountCreation,"EMail already exist please login.",Toast.LENGTH_SHORT).show()
                      val intent=Intent(this@NewAccountCreation,LoginScreen::class.java)
                      intent.putExtra("emailKey",binding.editTextTextEmailAddress.text.toString().trim())
                      startActivity(intent)
                      finish()
            }
                else{

                  }
                }

                override fun onError(error: String) {
                    // Handle the error here
                    negativeToast(error.toString().trim())
                    binding.sendotp.hideProgress(R.string.failedtosendotp)
                }
            }
        )
    }

}

