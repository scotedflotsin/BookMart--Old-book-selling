package com.bookmart.bookmart.BooksShowMet

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bookmart.bookmart.Adapters.Poast_ads_Adapter.Post_ads_adapter
import com.bookmart.bookmart.R
import com.squareup.picasso.Picasso

// BookAdapter.kt
class BookAdaptermetshow(private val onItemClickListener: BookAdaptermetshow.OnItemClickListener, private val bookList: List<BookDetailsese>) :
    RecyclerView.Adapter<BookAdaptermetshow.ViewHolder>() {


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTextView: TextView = itemView.findViewById(R.id.booktitleadsab)
        val authorTextView: TextView = itemView.findViewById(R.id.authorab)
        val imageView: ImageView = itemView.findViewById(R.id.bookmage)
        val prise: TextView = itemView.findViewById(R.id.priseab)
        val standard: TextView = itemView.findViewById(R.id.standardbookab)
        // ... other views

        // Initialize other views here
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.basicbookview, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val book = bookList[position]
        holder.titleTextView.text = book.title
        holder.authorTextView.text = book.author
        holder.prise.text = book.price
        holder.standard.text = book.standard
        // ... bind other views
        // Load images using Picasso or Glide
        Picasso.get().load(book.photo1).into(holder.imageView)
        holder.titleTextView.setOnClickListener {
            onItemClickListener.onItemClicked(book.book_id.toString())
        }
        holder.authorTextView.setOnClickListener {
            onItemClickListener.onItemClicked(book.book_id.toString())
        }
        holder.prise.setOnClickListener {
            onItemClickListener.onItemClicked(book.book_id.toString())
        }
        holder.standard.setOnClickListener {
            onItemClickListener.onItemClicked(book.book_id.toString())
        }
        holder.imageView.setOnClickListener {
            onItemClickListener.onItemClicked(book.book_id.toString())
        }

    }

    override fun getItemCount(): Int {
        return bookList.size
    }
    interface OnItemClickListener {
        fun onItemClicked(position: String)
    }
}
