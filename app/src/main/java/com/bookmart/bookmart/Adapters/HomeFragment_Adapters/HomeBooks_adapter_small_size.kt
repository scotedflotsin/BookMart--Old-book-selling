package com.bookmart.bookmart.Adapters.HomeFragment_Adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bookmart.bookmart.Model.HomeFragment_Models.AdsSec_Model
import com.bookmart.bookmart.databinding.AdsLightSizeRcyBinding
import com.bookmart.bookmart.volly_Android_networking.USER_API_SERVICES.BookDetailseseIN
import com.squareup.picasso.Picasso

class HomeBooks_adapter_small_size(private val onItemClickListener: onItemClick,var dataList:List<BookDetailseseIN>):RecyclerView.Adapter<HomeBooks_adapter_small_size.MyViewHolder>(){
    inner class MyViewHolder(var binding:AdsLightSizeRcyBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding=AdsLightSizeRcyBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
  return dataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val book = dataList[position]
        holder.binding.booktitle.text = book.name+" "+book.title+".."
        // holder.authorTextView.text = book.author
        holder.binding.price.text = " "+book.price
       holder.binding.standard.text=book.standard
       holder.binding.year.text=book.publication_year
        // ... bind other views
        // Load images using Picasso or Glide
        Picasso.get().load(book.photo1).into(holder.binding.bookimage)
        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClick(position,book.book_id.toString())
        }
    }
    // Update data in the adapter
    interface onItemClick{
        fun onItemClick(position: Int,bookId:String)
    }
}