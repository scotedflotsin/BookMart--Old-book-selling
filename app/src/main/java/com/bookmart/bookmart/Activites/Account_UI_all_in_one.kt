package com.bookmart.bookmart.Activites

import TabPagerAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import android.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.bookmart.bookmart.AboutTab_lay.About_fragment
import com.bookmart.bookmart.AboutTab_lay.Privacy_policy
import com.bookmart.bookmart.AboutTab_lay.Term_condition

import com.bookmart.bookmart.R
import com.bookmart.bookmart.databinding.ActivityAccountUiAllInOneBinding
import com.google.android.material.tabs.TabLayout



class Account_UI_all_in_one : AppCompatActivity() {
lateinit var binding:ActivityAccountUiAllInOneBinding
    var identifies_for_UI:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAccountUiAllInOneBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        //getting valuse from intent which coming from account fragment
        identifies_for_UI= intent.getStringExtra("helpandsupportIDEN").toString()
//        Toast.makeText(this@Account_UI_all_in_one,identifies_for_UI,Toast.LENGTH_SHORT).show()

        if(identifies_for_UI.equals("feedback")){
            // you visible feedback layout
            binding.aboutus.visibility=View.VISIBLE
            // Customize the Toolbar as needed
            supportActionBar?.setDisplayHomeAsUpEnabled(true) // Show the back arrow
            supportActionBar?.title = "Help & feedback" // Set the title
        }else if(identifies_for_UI.equals("userdataandprivacy")){
            // you visible feedback layout
            binding.yourdataandprivacy.visibility=View.VISIBLE
            // Customize the Toolbar as needed
            supportActionBar?.setDisplayHomeAsUpEnabled(true) // Show the back arrow
            supportActionBar?.title = "Data & Privacy" // Set the title
        }
        else if(identifies_for_UI.equals("aboutus")){
            // you visible feedback layout
            binding.aaaa.visibility=View.VISIBLE
            // Customize the Toolbar as needed
            supportActionBar?.setDisplayHomeAsUpEnabled(true) // Show the back arrow
            supportActionBar?.title = "About us & privacy policy" // Set the title
        }












        val tabLayout: TabLayout = findViewById(R.id.tabLayout)
        val viewPager: ViewPager = findViewById(R.id.viewPager)
        val adapter = TabPagerAdapter(supportFragmentManager)
        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)


        binding.toolbar.setNavigationOnClickListener{

           finish()

        }


    }
    override fun onBackPressed() {
        super.onBackPressed()
        // Set the exit animation
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
// Finish the current activity
        finish();


    }

}