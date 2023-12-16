package com.bookmart.bookmart.Adapters.HomeFragment_Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bookmart.bookmart.Model.HomeFragment_Models.TopAds_Model
import com.bookmart.bookmart.databinding.AdsLightSizeRcyBinding
import com.bookmart.bookmart.databinding.QuickLayoutRcyBinding

class TopAds_Adapter(var datalist:ArrayList<TopAds_Model>):RecyclerView.Adapter<TopAds_Adapter.MyViewHolder>(){
    inner class MyViewHolder(var binding: AdsLightSizeRcyBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       var binding=AdsLightSizeRcyBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
     return datalist.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

    }


}