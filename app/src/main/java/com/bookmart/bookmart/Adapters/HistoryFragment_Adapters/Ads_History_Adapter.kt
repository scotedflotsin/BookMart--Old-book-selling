package com.bookmart.bookmart.Adapters.HistoryFragment_Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bookmart.bookmart.Model.HistoryFragment_Models.Ads_medium_History_Models
import com.bookmart.bookmart.databinding.AdsLightSizeRcyForHistoryBinding
import com.bookmart.bookmart.databinding.AdsLightSizeRcyForWishlistBinding

class Ads_History_Adapter(var dataList:ArrayList<Ads_medium_History_Models>):RecyclerView.Adapter<Ads_History_Adapter.MyViewHolder>(){

    inner class MyViewHolder(var binding: AdsLightSizeRcyForHistoryBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var binding=AdsLightSizeRcyForHistoryBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
     return dataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

    }
}