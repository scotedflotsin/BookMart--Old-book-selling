package com.bookmart.bookmart.Adapters.HomeFragment_Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bookmart.bookmart.Model.HomeFragment_Models.Categorie_Model
import com.bookmart.bookmart.R
import com.bookmart.bookmart.databinding.ActivityMainBinding
import com.bookmart.bookmart.databinding.CategoriesLayoutRcyBinding

class Categories_Adapters(var dataList:List<Categorie_Model>):RecyclerView.Adapter<Categories_Adapters.MyViewHolder>() {

    inner class MyViewHolder(var binding:CategoriesLayoutRcyBinding)
        :RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = CategoriesLayoutRcyBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
      return dataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.binding.imageView10.setImageResource(dataList.get(position).image)
        holder.binding.textView14.text=dataList.get(position).title


    }

}

