package com.bookmart.bookmart.Adapters


import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.bookmart.bookmart.R
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.SliderViewAdapter
import com.squareup.picasso.Picasso

// on below line we are creating a class for slider
// adapter and passing our array list to it.



class MySliderAdapter(private val imageUrls: List<String>) :
    SliderViewAdapter<MySliderAdapter.SliderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup): SliderViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.imageslider_banner_home, parent, false)
        return SliderViewHolder(view)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        // Load images into the ImageView using an image loading library (e.g., Glide, Picasso)
        // Example with Glide:
        // Example with Glide: placeholder and error handling
        Glide.with(holder.itemView)
            .load(imageUrls[position])
            .placeholder(com.denzcoskun.imageslider.R.drawable.default_loading)
            .error(com.denzcoskun.imageslider.R.drawable.default_error)
            .into(holder.imageView)
        Log.d("SliderAdapter", "onBindViewHolder: $position, Image URL: ${imageUrls[position]}")

    }

    override fun getCount(): Int {
        return imageUrls.size
    }

    class SliderViewHolder(view: View) : SliderViewAdapter.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.myimage)
    }
}