package com.bookmart.bookmart.SearchAndFiltersBundle

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bookmart.bookmart.databinding.FilterSuggestionRycBinding

class Filtersadapter(private val valonItemClick: onItemClick, val filtersDataList:ArrayList<FilterText>):RecyclerView.Adapter<Filtersadapter.MyViewHolder>(){
    inner class MyViewHolder(val binding:FilterSuggestionRycBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var binding=FilterSuggestionRycBinding.inflate((LayoutInflater.from(parent.context)),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
       return filtersDataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var data=filtersDataList[position]
        holder.binding.filtextcontainer.setText(data.filter_tx)
        holder.itemView.setOnClickListener{

            valonItemClick.onItemCliked(position,data.filter_tx)
        }


    }
    interface onItemClick{
        fun onItemCliked(position:Int,queryfinter:String)
    }
}
