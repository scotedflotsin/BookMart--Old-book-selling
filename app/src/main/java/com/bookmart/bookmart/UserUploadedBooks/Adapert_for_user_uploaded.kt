package com.bookmart.bookmart.UserUploadedBooks

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bookmart.bookmart.Model.HistoryFragment_Models.Ads_medium_History_Models
import com.bookmart.bookmart.databinding.UserUploadedBooksListRcyBinding

class Adapert_for_user_uploaded(
    var onEditClick_var: onEditClick,
    var onSoldClick_var: onSoldClick,
    var books: ArrayList<Ads_medium_History_Models>
) : RecyclerView.Adapter<Adapert_for_user_uploaded.MyViewHolder>() {


    inner class MyViewHolder(val binding: UserUploadedBooksListRcyBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        var binding = UserUploadedBooksListRcyBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return books.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var dataList = books[position]
holder.binding.booktitleupli.setText(dataList.adTitle)
holder.binding.booknameupli.setText(dataList.adTitle)



    }

    interface onEditClick {
        fun onEditClick(position: Int, BookId: String)
    }

    interface onSoldClick {
        fun onSolidClick(position: Int, BookId: String)
    }


}