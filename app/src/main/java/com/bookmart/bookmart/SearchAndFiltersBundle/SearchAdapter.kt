package com.bookmart.bookmart.SearchAndFiltersBundle

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bookmart.bookmart.R
import com.bookmart.bookmart.databinding.SearchSuggestionRcyBinding

class SearchAdapter(private val onImageClickListener: OnItemClicked,var datalist: List<SearchResult>, private val historyList: List<String>) : RecyclerView.Adapter<SearchAdapter.MyViewHolder>() {

   inner class MyViewHolder(var binding: SearchSuggestionRcyBinding) : RecyclerView.ViewHolder(binding.root)

   // Map to store drawable resource ID for each item
   private val leftDrawableMap = mutableMapOf<Int, Int>()

   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
      val binding = SearchSuggestionRcyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
      return MyViewHolder(binding)
   }

   override fun getItemCount(): Int {
      return datalist.size
   }

   override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
      val book = datalist[position]
      holder.binding.suggetiontv.text = book.searchTitle
      //animation
      val animation = AnimationUtils.loadAnimation(holder.itemView.context, android.R.anim.fade_out)
      holder.itemView.startAnimation(animation)
      // Check if the searchTitle is in your history list
      val isHistoryItem = isHistoryItem(book.searchTitle)

      if(isHistoryItem){
               holder.binding.suggetiontv.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(holder.itemView.context, R.drawable.group_28search_suggestion_icon), null, ContextCompat.getDrawable(holder.itemView.context, R.drawable.baseline_history_24), null)
      }else{

      }
   holder.itemView.setOnClickListener{
      onImageClickListener.onItemClicked(position,book.searchTitle)
   }






   }
   override fun onViewDetachedFromWindow(holder: MyViewHolder) {
      super.onViewDetachedFromWindow(holder)
      holder.itemView.clearAnimation()
   }


   // Update left drawable for a specific item
   fun updateLeftDrawableForItem(position: Int, drawableResId: Int) {
      leftDrawableMap[position] = drawableResId
      notifyItemChanged(position) // Refresh only the specific item
   }
   private fun isHistoryItem(searchTitle: String): Boolean {
      return historyList.contains(searchTitle)
   }

   //click on item
   interface OnItemClicked{
      fun onItemClicked(position:Int,query:String)
   }


}

/**
// Inside your activity

// Assuming you have an instance of SearchAdapter named searchAdapter
searchAdapter.updateLeftDrawableForItem(itemPosition, R.drawable.new_drawable)
*/



