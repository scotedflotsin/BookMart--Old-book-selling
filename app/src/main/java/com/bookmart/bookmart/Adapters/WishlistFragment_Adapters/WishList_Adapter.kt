package com.bookmart.bookmart.Adapters.WishlistFragment_Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bookmart.bookmart.Model.WishListFragment_Models.Wishlist_Model
import com.bookmart.bookmart.databinding.AdsLightSizeRcyBinding
import com.bookmart.bookmart.databinding.AdsLightSizeRcyForWishlistBinding
import com.bookmart.bookmart.databinding.AdsMediumSizeRcyBinding

class WishList_Adapter(var dataList:ArrayList<Wishlist_Model>):RecyclerView.Adapter<WishList_Adapter.MyViewHolder>() {
    inner class MyViewHolder(var binding:AdsLightSizeRcyForWishlistBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
  var binding=AdsLightSizeRcyForWishlistBinding.inflate(LayoutInflater.from(parent.context),parent,false)

        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
    return dataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


    }
}