package com.bookmart.bookmart.Activites

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.os.postDelayed
import androidx.dynamicanimation.animation.FlingAnimation
import com.bookmart.bookmart.R
import com.bookmart.bookmart.databinding.ActivityWelcomeScreenBinding
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import render.animations.Attention
import render.animations.Bounce
import render.animations.Fade
import render.animations.Flip
import render.animations.Render
import render.animations.Slide

class WelcomeScreen : AppCompatActivity() {
    val binding by lazy {
        ActivityWelcomeScreenBinding.inflate(layoutInflater)
    }
    val handler = Handler()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.button.setOnClickListener {
            val intent = Intent(this@WelcomeScreen, NewAccountCreation::class.java)
            intent.putExtra("newAC_creation", "no")
            startActivity(intent)


        }
        binding.login.setOnClickListener {
            val intent = Intent(this@WelcomeScreen, LoginScreen::class.java)
            startActivity(intent)

        }
        binding.skipped.setOnClickListener {

            openWebsiteInChrome(  "https://www.onlinevideodownloader.co/")
        }

        handler.removeCallbacksAndMessages(null) // Remove all pending tasks when the activity is destroyed
        try {
            animation()
        } catch (e: Exception) {
            showToastMassage(e.localizedMessage.toString(), "negative")
        }


    }

    fun animation() {
        // Create Render Class
        try {
            val render = Render(this)
            var titleList = (arrayOf<String>(
                "A warm and heartfelt \"Welcome to Our Diverse Collection of Books – Where Enthusiasts Unite to Turn Dreams into Reality Through Happy Reading and Trading!\"",
                "Welcome to our one-stop marketplace for buying and selling preloved items! Discover hidden treasures today.",
                "\"Welcome to our app! Buy and sell old products with ease. Your treasure, someone else's treasure. Let's trade and save!\"\n",
                "\"Discover, trade, and find treasures on our Buy & Sell app for pre-loved items. Join us today and turn clutter into cash!\"",
                "Welcome to our buy and sell app! Easily find and sell pre-loved items, making transactions seamless and convenient. Happy exploring!",
                "Discover a new world of possibilities! Buy and sell your old treasures with ease on our app. Welcome to the future of recycling!"
            ))


            var currentIndex = 0

            val updateTextRunnable = object : Runnable {
                // Update the text view with the current text
                override fun run() {
                    binding.welocmeText.text = titleList[currentIndex]
                    render.setAnimation(Slide().InLeft(binding.welocmeText))
                    render.start()
                    // Increment the index for the next text (loop back to 0 if needed)

                    if (currentIndex == 5) {
                        currentIndex = 0
                    }
                    if (currentIndex == 0) {
                        // Create a drawable, for example, a solid color drawable
                        binding.t1.setBackgroundResource(R.drawable.activeslidingelement_bg)
                        binding.t2.setBackgroundResource(R.drawable.slidingelement_bg)
                        binding.t3.setBackgroundResource(R.drawable.slidingelement_bg)
                        binding.t4.setBackgroundResource(R.drawable.slidingelement_bg)
                        binding.t5.setBackgroundResource(R.drawable.slidingelement_bg)
                        render.setAnimation(Slide().InLeft(binding.t1))
                        render.start()

                    }
                    if (currentIndex == 1) {
                        // Create a drawable, for example, a solid color drawable
                        render.setAnimation(Slide().InLeft(binding.t2))
                        render.start()
                        binding.t1.setBackgroundResource(R.drawable.slidingelement_bg)
                        binding.t2.setBackgroundResource(R.drawable.activeslidingelement_bg)
                        binding.t3.setBackgroundResource(R.drawable.slidingelement_bg)
                        binding.t4.setBackgroundResource(R.drawable.slidingelement_bg)
                        binding.t5.setBackgroundResource(R.drawable.slidingelement_bg)

                    }
                    if (currentIndex == 2) {
                        render.setAnimation(Slide().InLeft(binding.t3))
                        render.start()
                        // Create a drawable, for example, a solid color drawable
                        binding.t1.setBackgroundResource(R.drawable.slidingelement_bg)
                        binding.t2.setBackgroundResource(R.drawable.slidingelement_bg)
                        binding.t3.setBackgroundResource(R.drawable.activeslidingelement_bg)
                        binding.t4.setBackgroundResource(R.drawable.slidingelement_bg)
                        binding.t5.setBackgroundResource(R.drawable.slidingelement_bg)

                    }
                    if (currentIndex == 3) {
                        render.setAnimation(Slide().InLeft(binding.t4))
                        render.start()
                        // Create a drawable, for example, a solid color drawable
                        binding.t1.setBackgroundResource(R.drawable.slidingelement_bg)
                        binding.t2.setBackgroundResource(R.drawable.slidingelement_bg)
                        binding.t3.setBackgroundResource(R.drawable.slidingelement_bg)
                        binding.t4.setBackgroundResource(R.drawable.activeslidingelement_bg)
                        binding.t5.setBackgroundResource(R.drawable.slidingelement_bg)

                    }
                    if (currentIndex == 4) {
                        render.setAnimation(Slide().InLeft(binding.t5))
                        render.start()
                        // Create a drawable, for example, a solid color drawable
                        binding.t1.setBackgroundResource(R.drawable.slidingelement_bg)
                        binding.t2.setBackgroundResource(R.drawable.slidingelement_bg)
                        binding.t3.setBackgroundResource(R.drawable.slidingelement_bg)
                        binding.t4.setBackgroundResource(R.drawable.slidingelement_bg)
                        binding.t5.setBackgroundResource(R.drawable.activeslidingelement_bg)

                    }
                    currentIndex = (currentIndex + 1) % binding.welocmeText.length()
                    Handler(Looper.getMainLooper()).postDelayed({
                        render.setAnimation(Slide().OutRight(binding.welocmeText))
                        render.start()
                    }, 3000) // 3000 is the delayed time in milliseconds.
                    // Post the same task again with a delay (e.g., 2000 milliseconds or 2 seconds)
                    handler.postDelayed(this, 4000)
                }

            }


// Start the text cycling by posting the initial task
            handler.post(updateTextRunnable)


        } catch (e: Exception) {

            showToastMassage(e.localizedMessage.toString(), "negative")
        }
    }

    //for costume toast for any type massage
    fun showToastMassage(toastTitle: String, massageType: String) {
        val render = Render(this@WelcomeScreen)


        //for positive massage
        if (massageType.equals("positive")) {
            binding.toastbody.visibility = View.VISIBLE
            render.setAnimation(Attention().Shake(binding.toastmassage))
            binding.toastmassage.setBackgroundColor(getColor(R.color.toastbodycolourpositive))
            val drawable: Drawable? = resources.getDrawable(
                R.drawable.baseline_done_24,
                null
            )  // Replace with your drawable resource
            // Set the left drawable icon in the TextView
            binding.toastmassage.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
            binding.toastmassage.text = toastTitle
            render.start()

            render.setAnimation(Fade().Out(binding.toastmassage))
            Handler(Looper.getMainLooper()).postDelayed({
                render.start()
            }, 500) // 3000 is the delayed time in milliseconds.     render.start()

        }
        //for negative massage
        else {
            binding.toastbody.visibility = View.VISIBLE
            render.setAnimation(Attention().Shake(binding.toastmassage))
            binding.toastmassage.setBackgroundColor(getColor(R.color.toastbodycolournegative))
            val drawable: Drawable? = resources.getDrawable(
                R.drawable.outline_error_24,
                null
            )  // Replace with your drawable resource
            // Set the left drawable icon in the TextView
            binding.toastmassage.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
            binding.toastmassage.text = toastTitle
            render.start()

            render.setAnimation(Fade().Out(binding.toastmassage))
            Handler(Looper.getMainLooper()).postDelayed({
                render.start()
            }, 500) // 3000 is the delayed time in milliseconds.     render.start()


        }


    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacksAndMessages(null) // Remove all pending tasks when the activity is destroyed
    }

    fun dailogbox() {


    }

    override fun onBackPressed() {
        super.onBackPressed()


    }

    fun showMessageBox(text: String) {

        //Inflate the dialog as custom view
        val messageBoxView = LayoutInflater.from(this@WelcomeScreen)
            .inflate(R.layout.custom_dailog_box, binding.root)

        //AlertDialogBuilder
        val messageBoxBuilder = AlertDialog.Builder(this@WelcomeScreen).setView(messageBoxView)

//        //setting text values
//        messageBoxView.message_box_header.text = "This is message header"
//        messageBoxView.message_box_content.text = "This is message content"

        //show dialog
        val messageBoxInstance = messageBoxBuilder.show()

        //set Listener
        messageBoxView.setOnClickListener() {
            //close dialog
            messageBoxInstance.dismiss()
        }
    }
    private fun openWebsiteInChrome(url: String) {
        val packageName = "com.android.chrome" // Package name of Chrome browser

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        intent.`package` = packageName

        // Check if Chrome is installed
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            // If Chrome is not installed, open the link using a default browser
            openWebsite(url)
        }
    }

    private fun openWebsite(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }

}