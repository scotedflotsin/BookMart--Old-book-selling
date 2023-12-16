package com.bookmart.bookmart.Adapters.HomeFragment_Adapters

//import android.view.LayoutInflater
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.bookmart.bookmart.Android_retrofit.API_SERVICE.Models.BookDetails
//import com.bookmart.bookmart.BooksShowMet.BookDetailsese
//import com.bookmart.bookmart.Model.HomeFragment_Models.Ads_Medium_Model
//import com.bookmart.bookmart.databinding.AdsMediumSizeRcyBinding
//import com.bookmart.bookmart.volly_Android_networking.USER_API_SERVICES.BookDetailseseIN
//import com.squareup.picasso.Picasso
//
//class Ads_Medium_size_Adapter(private val onItemClickListener: onItemClick,var datalist: MutableList<BookDetailseseIN>) :
//    RecyclerView.Adapter<Ads_Medium_size_Adapter.MyViewHolder>() {
//    inner class MyViewHolder(var binding: AdsMediumSizeRcyBinding) :
//        RecyclerView.ViewHolder(binding.root)
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//        var binding =
//            AdsMediumSizeRcyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return MyViewHolder(binding)
//    }
//
//    override fun getItemCount(): Int {
//        return datalist.size
//    }
//
//    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        val book = datalist[position]
//        holder.binding.textView16.text = book.name+" "+book.title+".."
//       // holder.authorTextView.text = book.author
//        holder.binding.textView17.text = " "+book.price
//        holder.binding.standardbookab.text = book.standard+" "+book.publication_year
//        holder.binding.textew17.text = book.local_address
//        // ... bind other views
//        // Load images using Picasso or Glide
//        Picasso.get().load(book.photo1).into(holder.binding.imageView13)
//        holder.itemView.setOnClickListener {
//            onItemClickListener.onItemClick(position,book.book_id.toString())
//        }
//
//    }
//    // Update data in the adapter
//interface onItemClick{
//    fun onItemClick(position: Int,bookId:String)
//}
//    fun addBooks(newBooks: List<BookDetailseseIN>) {
//        datalist.addAll(newBooks)
//        notifyDataSetChanged()
//    }
//}

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bookmart.bookmart.databinding.AdsMediumSizeRcyBinding
import com.bookmart.bookmart.volly_Android_networking.USER_API_SERVICES.BookDetailseseIN
import com.squareup.picasso.Picasso

class Ads_Medium_size_Adapter(
    private val onItemClickListener: OnItemClickListener,
    private var dataList: MutableList<BookDetailseseIN>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1

    inner class ItemViewHolder(private val binding: AdsMediumSizeRcyBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(book: BookDetailseseIN) {
            binding.textView16.text = "${book.name} ${book.title}.."
            binding.textView17.text = " ${book.price}"
            binding.standardbookab.text = "${book.standard} ${book.publication_year}"
            binding.textew17.text = book.local_address
            Picasso.get().load(book.photo1).into(binding.imageView13)
            binding.root.setOnClickListener {
                onItemClickListener.onItemClick(adapterPosition, book.book_id.toString())
            }
        }
    }

    inner class LoadingViewHolder(binding: AdsMediumSizeRcyBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            VIEW_TYPE_ITEM -> {
                val binding = AdsMediumSizeRcyBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                ItemViewHolder(binding)
            }
            VIEW_TYPE_LOADING -> {
                val binding = AdsMediumSizeRcyBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                LoadingViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun getItemCount(): Int {
        return dataList.size + if (isLoading()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == dataList.size && isLoading()) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ItemViewHolder -> {
                val book = dataList[position]
                holder.bind(book)
            }
            is LoadingViewHolder -> {
                // Handle the "Load More" indicator if needed
            }
        }
    }

    // Update data in the adapter
    fun addBooks(newBooks: List<BookDetailseseIN>) {
        dataList.addAll(newBooks)
        notifyDataSetChanged()
    }

    // Check if the adapter is in a loading state
    private fun isLoading(): Boolean {
        // Implement your loading state logic here
        return false
    }

    // Interface for item click
    interface OnItemClickListener {
        fun onItemClick(position: Int, bookId: String)
    }
}