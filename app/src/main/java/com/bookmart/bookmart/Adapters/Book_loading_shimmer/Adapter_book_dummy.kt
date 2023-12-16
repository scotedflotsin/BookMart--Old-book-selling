package com.bookmart.bookmart.Adapters.Book_loading_shimmer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.animation.content.Content
import com.bookmart.bookmart.Model.AdsViewModels.AdsView_ads_Model
import com.bookmart.bookmart.databinding.ActivityAccountUiAllInOneBinding
import com.bookmart.bookmart.databinding.BookLoadingShimmerRcyBinding

class Adapter_book_dummy(var datalist: ArrayList<Data_dummy>):RecyclerView.Adapter<Adapter_book_dummy.MyViewHolder>(){
    inner class MyViewHolder(val binding: BookLoadingShimmerRcyBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       var binding=BookLoadingShimmerRcyBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
      return datalist.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

    }
}


// only dummy
data class Data_dummy(var position:Int)  //not any real time use only for loading view totally dummy