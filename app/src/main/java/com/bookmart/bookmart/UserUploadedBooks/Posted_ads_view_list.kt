package com.bookmart.bookmart.UserUploadedBooks

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bookmart.bookmart.Model.HistoryFragment_Models.Ads_medium_History_Models
import com.bookmart.bookmart.R
import com.bookmart.bookmart.databinding.ActivityPostedAdsViewListBinding

class Posted_ads_view_list : AppCompatActivity(), Adapert_for_user_uploaded.onSoldClick,
    Adapert_for_user_uploaded.onEditClick {

    val binding by lazy {
        ActivityPostedAdsViewListBinding.inflate(layoutInflater)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Show the back arrow
        supportActionBar?.title = "Your uploaded books" // Set the title
        supportActionBar?.subtitle= "you can view your books and edit or sold"
        binding.toolbar.setNavigationOnClickListener{

            finish()

        }
        //adapter recyler view
        val layoutManager= LinearLayoutManager(this@Posted_ads_view_list, LinearLayoutManager.VERTICAL,false)
        binding.bookrecylerview.layoutManager=layoutManager

        var data=ArrayList<Ads_medium_History_Models>()

        data.add(Ads_medium_History_Models(R.drawable.bookdemo,"Rs math", "$345","2023","10th"))
        data.add(Ads_medium_History_Models(R.drawable.bookdemo,"Rs math", "$345","2023","10th"))
        data.add(Ads_medium_History_Models(R.drawable.bookdemo,"Rs math", "$345","2023","10th"))
        data.add(Ads_medium_History_Models(R.drawable.bookdemo,"Rs math", "$345","2023","10th"))
        data.add(Ads_medium_History_Models(R.drawable.bookdemo,"Rs math", "$345","2023","10th"))
        data.add(Ads_medium_History_Models(R.drawable.bookdemo,"Rs math", "$345","2023","10th"))
        data.add(Ads_medium_History_Models(R.drawable.bookdemo,"Rs math", "$345","2023","10th"))
        data.add(Ads_medium_History_Models(R.drawable.bookdemo,"Rs math", "$345","2023","10th"))
        data.add(Ads_medium_History_Models(R.drawable.bookdemo,"Rs math", "$345","2023","10th"))
        data.add(Ads_medium_History_Models(R.drawable.bookdemo,"Rs math", "$345","2023","10th"))
        data.add(Ads_medium_History_Models(R.drawable.bookdemo,"Rs math", "$345","2023","10th"))
        data.add(Ads_medium_History_Models(R.drawable.bookdemo,"Rs math", "$345","2023","10th"))
        data.add(Ads_medium_History_Models(R.drawable.bookdemo,"Rs math", "$345","2023","10th"))
        data.add(Ads_medium_History_Models(R.drawable.bookdemo,"Rs math", "$345","2023","10th"))
        data.add(Ads_medium_History_Models(R.drawable.bookdemo,"Rs math", "$345","2023","10th"))
        data.add(Ads_medium_History_Models(R.drawable.bookdemo,"Rs math", "$345","2023","10th"))
        data.add(Ads_medium_History_Models(R.drawable.bookdemo,"Rs math", "$345","2023","10th"))
        data.add(Ads_medium_History_Models(R.drawable.bookdemo,"Rs math", "$345","2023","10th"))

        var adapter= Adapert_for_user_uploaded(object : Adapert_for_user_uploaded.onEditClick{
            override fun onEditClick(position: Int, BookId: String) {



            }

        },object: Adapert_for_user_uploaded.onSoldClick{
            override fun onSolidClick(position: Int, BookId: String) {
            }

        },data)
        binding.bookrecylerview.adapter=adapter
adapter.notifyDataSetChanged()







    }

    override fun onEditClick(position: Int, BookId: String) {
    }

    override fun onSolidClick(position: Int, BookId: String) {
    }


}