package com.bookmart.bookmart.Activites

import AccountFragments
import HistoryFragment
import HomeFragment
import WishListFragment
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.bookmart.bookmart.Global_Object.Temp_location
import com.bookmart.bookmart.LocalDB.HistoryDatabaseHelper
import com.bookmart.bookmart.MapsActivity
import com.bookmart.bookmart.R
import com.bookmart.bookmart.SearchAndFiltersBundle.Book_search_Activity
import com.bookmart.bookmart.SharedPreffrence_Manager.UserAccountManager.SharedPreferenceManager
import com.bookmart.bookmart.databinding.ActivityMainHomeBinding


class MainHome : AppCompatActivity(), HomeFragment.OnFragmentInteractionListener {
    val binding by lazy {
        ActivityMainHomeBinding.inflate(layoutInflater)
    }
    private val locationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "LOCATION_UPDATED_ACTION") {
                // Retrieve location data from the intent
                // Update UI based on the location data
           update_location_on_header()
//update book according to address
                //.......................
            }
        }
    }
    private lateinit var drawerLayout: DrawerLayout
    //  private lateinit var drawerLayout: DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val firstFragment = HomeFragment()
        val secondFragment = WishListFragment()
        val thirdFragment = HistoryFragment()
        val fourthFragment = AccountFragments()

        showToast(Temp_location.city.toString() + "\n" + Temp_location.state.toString())
        // Register the broadcast receiver
        // Register the broadcast receiver
        val filter = IntentFilter("LOCATION_UPDATED_ACTION")
        registerReceiver(locationReceiver, filter)
        update_location_on_header()


        setCurrentFragment(firstFragment)

        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.homemenu ->
                    setCurrentFragment(firstFragment)

                R.id.wishlistmenu -> setCurrentFragment(secondFragment)
                R.id.historymenu -> setCurrentFragment(thirdFragment)
                R.id.accountmenu -> setCurrentFragment(fourthFragment)
                R.id.postadsmenu -> maon()


            }
            true
        }

        drawerLayout = findViewById(R.id.drawerLayout)

        // Set up your custom navigation drawer
        setupCustomNavigationDrawer()

        binding.postads.setOnClickListener {
            val intent = Intent(this@MainHome, Add_ads_screen::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_bottom_to_top, R.anim.fade_out)

        }
//handle click kon temp location
        binding.location.setOnClickListener {
            //redirect on map activity
            val intent = Intent(this@MainHome, MapsActivity::class.java)
            intent.putExtra("MAIN_HOME_temp_search_location_for_ads", "9170599651")
            startActivity(intent)
            // Set the opening animation
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
//handle event on search box >>> ?? Redirect on search activity

        binding.searchbox.setOnClickListener {
            val intent = Intent(this@MainHome, Book_search_Activity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }


    }


    fun showToast(massage: String) {
        Toast.makeText(this@MainHome, massage.toString(), Toast.LENGTH_SHORT).show()
    }

    private fun setCurrentFragment(fragment: Fragment) =
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.frame_layout, fragment)
            commit()
        }


//    private fun setupDrawer() {
//        val navDrawer = findViewById<LinearLayout>(R.id.nav_drawer)
//        navDrawer.setOnClickListener {
//            // Handle navigation item clicks
//            // You can launch different fragments or activities based on the clicked item
//        }
//    }

    fun maon() {
        val intent = Intent(this@MainHome, Add_ads_screen::class.java)
        startActivity(intent)
        // overridePendingTransition(R.anim.slidedown, R.anim.slidedown)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onButtonClicked(data: String) {
        // Handle the button click in the activity
        // Update UI or perform any required action
    }

    fun update_location_on_header() {
        val tep_location_obj = Temp_location //temp location object
        var location_data = tep_location_obj.city + "\n" + tep_location_obj.state
        binding.headLocation.setText(location_data.toString()) //setting data on head textview

    }
    private fun setupCustomNavigationDrawer() {
        val toggleButton = findViewById<ImageView>(R.id.toggleButton)

        toggleButton.setOnClickListener {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            //   drawerLayout.visibility= View.VISIBLE
                drawerLayout.closeDrawer(GravityCompat.START)

            } else {
          //    drawerLayout.visibility= View.GONE
                drawerLayout.openDrawer(GravityCompat.START)

            }
        }
    }

    fun onNavigationItemSelected(view: android.view.View) {
        // Handle navigation item click
        val itemName = (view as Button).text.toString()
        showToast("Selected: $itemName")
        drawerLayout.closeDrawer(GravityCompat.START)
    }


}