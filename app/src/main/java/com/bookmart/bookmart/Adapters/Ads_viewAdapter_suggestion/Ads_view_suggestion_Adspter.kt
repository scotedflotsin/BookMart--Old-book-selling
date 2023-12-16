package com.bookmart.bookmart.Adapters.Ads_viewAdapter_suggestion

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bookmart.bookmart.Model.AdsViewModels.AdsView_ads_Model
import com.bookmart.bookmart.databinding.AdsLightSizeRcyBinding
import com.bookmart.bookmart.databinding.AdsLightSizeRcyForHistoryBinding
import com.bookmart.bookmart.databinding.AdsMediumSizeRcyBinding

class Ads_view_suggestion_Adspter(var datalist: ArrayList<AdsView_ads_Model>) :
    RecyclerView.Adapter<Ads_view_suggestion_Adspter.MyViewHolder>() {

    inner class MyViewHolder(var binding: AdsLightSizeRcyBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var binding =
            AdsLightSizeRcyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return datalist.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

    }
}