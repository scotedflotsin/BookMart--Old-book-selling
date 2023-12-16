package com.bookmart.bookmart.SearchAndFiltersBundle


import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bookmart.bookmart.databinding.AddFilterRcyBinding
import com.bookmart.bookmart.databinding.FilterSuggestionRycBinding
import com.bookmart.bookmart.databinding.FinalSuggestionRcyHeaderBinding

class Adapter_showon_header(val filterdatalist:ArrayList<Final_filter_tx>): RecyclerView.Adapter<Adapter_showon_header.MyViewHolder>(){




    inner class MyViewHolder(val binding:FinalSuggestionRcyHeaderBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding=FinalSuggestionRcyHeaderBinding.inflate((LayoutInflater.from(parent.context)),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return filterdatalist.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val datalist = filterdatalist[position]
        holder.binding.slectedfilt.text = datalist.filters
        holder.binding.fildeletbutton.setOnClickListener {
            // Remove the item from the data list
            filterdatalist.removeAt(position)

            // Notify the adapter about the item removal
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemCount - position)
        }
    }


    override fun getItemId(position: Int): Long {
        // Return the position as a unique identifier
        return position.toLong()
    }



    interface onItemClick{
        fun OnItemClick(position: Int,filter:String)
    }
}