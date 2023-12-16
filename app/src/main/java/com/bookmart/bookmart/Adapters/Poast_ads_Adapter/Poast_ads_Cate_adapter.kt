//package com.bookmart.bookmart.Adapters.Poast_ads_Adapter
//
//import android.view.LayoutInflater
//import android.view.ViewGroup
//import android.widget.Toast
//import androidx.recyclerview.widget.RecyclerView
//import com.bookmart.bookmart.Model.Post_Ads_model.ApiResponse
//import com.bookmart.bookmart.OnCategoryClickListener
//import com.bookmart.bookmart.databinding.CategoriesSelectionRcyBinding
//import com.squareup.picasso.Picasso
//
//class Post_ads_adapter(private val dataList: List<ApiResponse>) :
//    RecyclerView.Adapter<Post_ads_adapter.MyViewHolder>() {
//    inner class MyViewHolder(var binding: CategoriesSelectionRcyBinding) :
//        RecyclerView.ViewHolder(binding.root)
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
//        var binding =
//            CategoriesSelectionRcyBinding.inflate(
//                LayoutInflater.from(parent.context),
//                parent,
//                false
//            )
//        return MyViewHolder(binding)
//    }
//
//    override fun getItemCount(): Int {
//        return dataList.size
//    }
//
//    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        val data = dataList[position]
//
//        holder.binding.cateName.setText(data.name)
//        // Load image using Picasso
//        Picasso.get().load(data.image.toString()).into(holder.binding.cateImage)
//        holder.binding.cateName.setOnClickListener {
//            // Access the value of identifier
//            Object.indentifier=position
//            val currentValue = Object.indentifier
//            Toast.makeText(it.context, currentValue.toString()+"hello", Toast.LENGTH_SHORT).show()
//        }
//
//        // Set a click listener for the item
////        holder.itemView.setOnClickListener {
////            // Invoke the interface method when the item is clicked
////            OnCategoryClickListener.onCategoryClick(data.id)
////        }
//
//    }
//    interface OnItemClickListener {
//        fun onItemClicked(position: Int)
//    }
//
//}


//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

// Post_ads_adapter.kt

package com.bookmart.bookmart.Adapters.Poast_ads_Adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bookmart.bookmart.Model.Post_Ads_model.ApiResponse
import com.bookmart.bookmart.databinding.CategoriesLayoutRcyBinding
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class Post_ads_adapter(
    private val imageLoadCallback: ImageLoadCallback,
    private val dataList: List<ApiResponse>,
    private val onItemClickListener: OnItemClickListener,
    private val onImageClickListener: OnImageClickListener
) : RecyclerView.Adapter<Post_ads_adapter.MyViewHolder>() {

    inner class MyViewHolder(var binding: CategoriesLayoutRcyBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            CategoriesLayoutRcyBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return MyViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = dataList[position]

        holder.binding.textView14.text= data.name
        // Load image using Picasso
        Picasso.get()
            .load(data.image.toString())
            .into(holder.binding.imageView10, object : Callback {
                override fun onSuccess() {
                    // Image loaded successfully
                    // You can perform any additional actions here if needed
                    // Image loaded successfully
                    imageLoadCallback.onImageLoadSuccess()
                }
                override fun onError(e: Exception?) {
                    // Handle error loading image
                    if (e != null) {
                        // Log the error or perform any other error handling actions
                        // Image loaded successfully
                        imageLoadCallback.onImageLoadError()
                    }
                }
            })
        holder.itemView.setOnClickListener {
            onItemClickListener.onItemClicked(data.id.toInt())
        }
        holder.binding.imageView10.setOnClickListener {
            onImageClickListener.onImageClicked(position,data.name.toString(),data.id.toString())
        }
//        // Set click listener for the category name
//        holder.binding.cateName.setOnClickListener {
//            // You can perform specific actions when the category name is clicked
//            Toast.makeText(holder.binding.root.context, data.name.toString(), Toast.LENGTH_SHORT).show()
//        }
        // Set click listener for the category name
//        holder.binding.imageView10.setOnClickListener {
//            // You can perform specific actions when the category name is clicked
//         //   Toast.makeText(holder.binding.root.context, data.name.toString(), Toast.LENGTH_SHORT).show()
//            // Pass the title to the onItemClickListener
//            onImageClickListener.onImageClicked(position, data.name, data.id)
//        }
    }

    interface OnItemClickListener {
        fun onItemClicked(position: Int)
    }
    interface OnImageClickListener {
        fun onImageClicked(position: Int, title: String, id:String)
    }
    interface ImageLoadCallback {
        fun onImageLoadSuccess()
        fun onImageLoadError()
    }


}