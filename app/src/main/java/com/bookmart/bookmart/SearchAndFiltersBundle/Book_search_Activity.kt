package com.bookmart.bookmart.SearchAndFiltersBundle

import ProductService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bookmart.bookmart.Activites.Book_Ads_viewer_detailed
import com.bookmart.bookmart.Adapters.Book_loading_shimmer.Adapter_book_dummy
import com.bookmart.bookmart.Adapters.Book_loading_shimmer.Data_dummy
import com.bookmart.bookmart.Adapters.HomeFragment_Adapters.Ads_Medium_size_Adapter
import com.bookmart.bookmart.Android_retrofit.API_SERVICE.API_interface.ApiService
import com.bookmart.bookmart.Android_retrofit.API_SERVICE.Models.book_details
import com.bookmart.bookmart.BooksShowMet.BookAdaptermetshow
import com.bookmart.bookmart.BooksShowMet.BookDetailsese
import com.bookmart.bookmart.BooksShowMet.BookRepository
import com.bookmart.bookmart.Global_Object.BookFilter
import com.bookmart.bookmart.Global_Object.Temp_location
import com.bookmart.bookmart.Global_Object.UniversalUserID_and_Limit_book
import com.bookmart.bookmart.LocalDB.HistoryDatabaseHelper
import com.bookmart.bookmart.MapsActivity
import com.bookmart.bookmart.Model.HomeFragment_Models.Ads_Medium_Model
import com.bookmart.bookmart.R
import com.bookmart.bookmart.databinding.ActivityBookSearchBinding
import com.bookmart.bookmart.volly_Android_networking.USER_API_SERVICES.BookDetailseseIN

import com.bookmart.bookmart.volly_Android_networking.USER_API_SERVICES.BookRepository_filter
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
Created By Harsh Verma, Gmail scotedflotsin.co.ltd@gmail.com
Book_search_Activity for handling user search request, handle user searched book as a response
#handling three layouts
1. search screen which is search layout XML>>>>>>SAME_NO_CHANGE_Activity
2. filter layout screen which is filter_layout XML>>>>>>SAME_NO_CHANGE_Activity
3. response results screen is response layout XML>>>>>>SAME_NO_CHANGE_Activity
 */


class Book_search_Activity : AppCompatActivity(), SearchAdapter.OnItemClicked,
    Filtersadapter.onItemClick, Adapter_showon_header.onItemClick,Ads_Medium_size_Adapter.OnItemClickListener {
    val binding by lazy {
        ActivityBookSearchBinding.inflate(layoutInflater)
    }
    private val locationReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == "LOCATION_UPDATED_ACTION") {
                // Retrieve location data from the intent
                // Update UI based on the location data
                update_location_on_header()
//update book according to address
                //.......................
            }
        }
    }
var current_filter_open_identifier:Int=0
    //filters
    var page_search_filter: String? = "1"
    var limit_search_filter: String? = "50"
    var search_search_filter: String? = ""
    var min_price_search_filter: String? = ""
    var max_price_search_filter: String? = ""
    var city_search_filter: String? = ""
    var state_search_filter: String? = ""
    var title_search_filter: String? = ""
    var author_search_filter: String? = ""
    var description_search_filter: String? = ""
    var book_condition_search_filter: String? = ""
    var publication_year_search_filter: String? = ""
    var based_on_search_filter: String? = ""
    var standard_search_filter: String? = ""
    var board_preference_search_filter: String? = ""
    var medium_search_filter: String? = ""
    var publisher_search_filter: String? = ""
    var category_id_search_filter: String? = ""

    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>....
    var trace_BACKSTATUS: Int = 0

    //filter data list
    // Declare dataa as a member variable
    val dataa = ArrayList<Final_filter_tx>()
    val adapter = Selected_filter_addOn(dataa)
    lateinit var data: ArrayList<FilterText>
    var searchQuery: String = "book"
    var finalAdapter_filter = Adapter_showon_header(dataa)
    private val database = FirebaseDatabase.getInstance()
    private val keywordsRef = database.getReference("languages")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        // Register the broadcast receiver
        val filter = IntentFilter("LOCATION_UPDATED_ACTION")
        registerReceiver(locationReceiver, filter)

        //checking any request or not for e.g cate, and other
        category_id_search_filter=intent.getStringExtra("cate_ID").toString()
        if(category_id_search_filter.toString()!="null"){
          //  showToast(category_id_search_filter.toString())
            //updating data for call book fetch api
            //e.g city, state, limit, page, search
            //creating object of objects temp_data
            // setting shimmer effect
            var temp_location_obj=Temp_location//>>>>>>>>>>>>>> //transmission sending
            var temp_Book_filters=BookFilter//<<<<<<<<<<<<<<<<< //getting receiving
            temp_Book_filters.page_ob= page_search_filter.toString()
            temp_Book_filters.limit_ob= limit_search_filter.toString()
            temp_Book_filters.city_ob=temp_location_obj.city
            temp_Book_filters.state_ob= temp_location_obj.state
            temp_Book_filters.search_ob= ""
            temp_Book_filters.cate_id_ob= category_id_search_filter.toString()

            Log.d("finalquery",temp_Book_filters.cate_id_ob+""+temp_Book_filters.city_ob+""+temp_Book_filters.state_ob+""+temp_Book_filters.limit_ob+""+temp_Book_filters.page_ob)

            updatedUIWhenQueryExecute()
            //here I am processing final query request to server

            fetchBooks()  //method to request server for books










        }else {
            //testing binding >>>>>>>>>>>>>>>>>>>>>>>>>>>>>> TESTING COMPLETED.........RESULT_OK
            //requesting focus to search view>>>>>>>>>>
            //disabling filter
            binding.searingedittext.requestFocus()  // keyboard will open by default

        }
        book_loading_shimmer()
//now here i am going to implement realtime search view in the app
        // step 1 read text from edittext and than send to real time server and get those keywords which is contained keywords related to search
       // fetchBooks()

        binding.searingedittext.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // This method is called to notify you that characters within `s` are about to be replaced.

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // This method is called to notify you that somewhere within `s` the characters from `start` to `start + before` are about to be replaced with new text with a length of `count`.
            }

            override fun afterTextChanged(s: Editable?) {
                binding.loading.visibility = View.VISIBLE
                checkForKeyword(binding.searingedittext.text.toString().trim())
                // This method is called to notify you that somewhere within `s`, the characters have been replaced with new text, and the `Editable` text now represents the updated content.


            }
        })
        //setting layout type in recycler view
        binding.filtecons.layoutManager = FlexboxLayoutManager(this)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.addedfilters.layoutManager = layoutManager
        // Initialize the 'data' ArrayList
        data = ArrayList()
        val layoutManagerr = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.finalUpperRcy.layoutManager = layoutManagerr

        finalAdapter_filter = Adapter_showon_header(dataa)
        binding.finalUpperRcy.adapter = adapter


        dummyBook()






        binding.medium.setOnClickListener {
            data.clear()

// Add the provided data to the 'data' ArrayList
            data = FiltersTx().mediumfilter()
            current_filter_open_identifier=1
            onButtonClick(binding.medium)


        }

        binding.schoolstandard.setOnClickListener {
            data.clear()

// Add the provided data to the 'data' ArrayList
            data = FiltersTx().standardfilter()
            current_filter_open_identifier=2
            onButtonClick(binding.schoolstandard)

        }
        binding.year.setOnClickListener {
            // HandleFilters().year(binding.year)
            data.clear()

// Add the provided data to the 'data' ArrayList
            data = FiltersTx().yearfilter()
            current_filter_open_identifier=3
            onButtonClick(binding.year)
        }
        binding.prefferdcourse.setOnClickListener {
            data.clear()

// Add the provided data to the 'data' ArrayList
            data = FiltersTx().prefferdfilter()
            current_filter_open_identifier=4
            onButtonClick(binding.prefferdcourse)
        }
        binding.intrest.setOnClickListener {
            data.clear()

// Add the provided data to the 'data' ArrayList
            data = FiltersTx().intrestfilter()
            current_filter_open_identifier=5
            onButtonClick(binding.intrest)

        }
        binding.csprogramming.setOnClickListener {
            data.clear()

// Add the provided data to the 'data' ArrayList
            data = FiltersTx().cscomputer()
            current_filter_open_identifier=6
            onButtonClick(binding.csprogramming)

        }
        binding.buisnessfinace.setOnClickListener {
            data.clear()

// Add the provided data to the 'data' ArrayList
            data = FiltersTx().bsinessfilter()
            current_filter_open_identifier=7
            onButtonClick(binding.buisnessfinace)

        }
        binding.storyandcomics.setOnClickListener {
            data.clear()

// Add the provided data to the 'data' ArrayList
            data = FiltersTx().storycomicfilter()
            current_filter_open_identifier=8
            onButtonClick(binding.storyandcomics)

        }
        binding.author.setOnClickListener {
            data.clear()

// Add the provided data to the 'data' ArrayList
            data = FiltersTx().subject()
            current_filter_open_identifier=9
            onButtonClick(binding.author)

        }
        binding.prise.setOnClickListener {
            data.clear()

// Add the provided data to the 'data' ArrayList
            data = FiltersTx().prise()
            current_filter_open_identifier=10
            onButtonClick(binding.prise)

        }
        binding.astrology.setOnClickListener {
            data.clear()

// Add the provided data to the 'data' ArrayList
            data = FiltersTx().astrologyfilter()
            current_filter_open_identifier=11
            onButtonClick(binding.astrology)

        }
        binding.horror.setOnClickListener {
            data.clear()

// Add the provided data to the 'data' ArrayList
            data = FiltersTx().horrorfilter()
            current_filter_open_identifier=12
            onButtonClick(binding.horror)

        }
        binding.condition.setOnClickListener {
            data.clear()

// Add the provided data to the 'data' ArrayList
            data = FiltersTx().condition()
            current_filter_open_identifier=13
            onButtonClick(binding.condition)

        }



        binding.clearfilter.setOnClickListener {
            Click().clearFilter()
        }


        binding.searingedittext.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                dataa.clear()
                adapter.notifyDataSetChanged()
                binding.searchlayout.visibility = View.VISIBLE
                Click().resetfilterlayout()
                binding.lottieAnimationView.visibility=View.GONE
                binding.button5.visibility=View.GONE
                binding.changelocation.visibility=View.GONE
                binding.textView12.visibility=View.GONE
                binding.rycLoadingShimmerBook.visibility=View.GONE
            }
        }
        binding.button5.setOnClickListener{
            dataa.clear()
            adapter.notifyDataSetChanged()
            binding.searchlayout.visibility = View.VISIBLE
            Click().resetfilterlayout()
            binding.lottieAnimationView.visibility=View.GONE
            binding.button5.visibility=View.GONE
            binding.textView12.visibility=View.GONE
            binding.rycLoadingShimmerBook.visibility=View.GONE
        }

        binding.backbutton.setOnClickListener {
            Click().backButton()
        }
        //click on search fab button
        binding.searchbutton.setOnClickListener {
            if (binding.searingedittext.text.toString().equals("")) {
                // show error because edit text(searching book null)

            } else {
                binding.searchbutton.visibility = View.GONE
                Click().hideKeyboardAndClearFocus(binding.searingedittext)
                Click().searchButton()
            }


        }
        //show filter layout
        binding.filtersShow.setOnClickListener {
            //disabling filter button
            showToast("Still we are working to launch this feature.")



//            trace_BACKSTATUS = 3
//            Click().hideKeyboardAndClearFocus(binding.searingedittext)
//            Click().showFliters_layout()
        }
       binding.searingedittext.setOnTouchListener { v, event ->
            val DRAWABLE_LEFT = 0
            val DRAWABLE_TOP = 1
            val DRAWABLE_RIGHT = 2
            val DRAWABLE_BOTTOM = 3

            if (event.action == MotionEvent.ACTION_UP) {
                if (event.rawX >= (binding.searingedittext.right - binding.searingedittext.compoundDrawables[DRAWABLE_RIGHT].bounds.width())) {
                    // your action here
                    binding.searingedittext.setText("")
                    dataa.clear()
                    adapter.notifyDataSetChanged()
                    binding.searchlayout.visibility = View.VISIBLE
                    Click().resetfilterlayout()
                    binding.lottieAnimationView.visibility=View.GONE
                    binding.button5.visibility=View.GONE
                    binding.changelocation.visibility=View.GONE
                    binding.textView12.visibility=View.GONE
                    binding.rycLoadingShimmerBook.visibility=View.GONE
                    return@setOnTouchListener true
                }
            }
            false
        }

        binding.loca.setOnClickListener{
            //redirect on map activity
            val intent = Intent(this@Book_search_Activity, MapsActivity::class.java)
            intent.putExtra("MAIN_HOME_temp_search_location_for_ads", "9170599651")
            startActivity(intent)
            // Set the opening animation
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        }
        binding.changelocation.setOnClickListener{
            //redirect on map activity
            val intent = Intent(this@Book_search_Activity, MapsActivity::class.java)
            intent.putExtra("MAIN_HOME_temp_search_location_for_ads", "9170599651")
            startActivity(intent)
            // Set the opening animation
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);

        }
        binding.apply.setOnClickListener {
            if (dataa.size < 0) {
                showToast("first apply filter")
            } else {
                Click().searchButton()
            }

        }


    }

    fun showToast(massage: String) {
        Toast.makeText(this@Book_search_Activity, massage.toString(), Toast.LENGTH_SHORT).show()
    }

    inner class Click {
        fun backButton() {
            finish()
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }

        fun searchButton() {
            try {
                //implement feature to search
                //.........
//first save search history in Room DB
                //but before saving history i need to show user query result
                searchQuery = binding.searingedittext.text.toString()  //initialization...
                if (searchQuery.equals("")) {
                    searchQuery = "book"
                } else {

                }
                searchQuery(searchQuery) //handling query and processing for show query related books
                addNewItem(searchQuery)
                //>>>>>>>>>>> after that saving searched history
                SaveHistory().saveHistory(searchQuery) // for saving history in local DB...

            } catch (e: Exception) {
                showToast(e.localizedMessage)
            }


        }

        fun clearFilter() {
            dataa.clear()
            adapter.notifyDataSetChanged()
        }

        fun showFliters_layout() {
            hideKeyboardAndClearFocus(binding.searingedittext)
            binding.searchlayout.visibility = View.GONE
            binding.searchresultLay.visibility = View.GONE
            binding.filterOp.visibility = View.VISIBLE
            binding.filtersLay.visibility = View.VISIBLE
            binding.searchbutton.visibility = View.GONE
            //for showing medium filter open default
            data.clear()
            // Add the provided data to the 'data' ArrayList
            data = FiltersTx().mediumfilter()
            onButtonClick(binding.medium)


        }

        // Function to hide the soft keyboard and clear focus
        fun hideKeyboardAndClearFocus(view: View) {
            val inputMethodManager =
                view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            view.clearFocus()
        }

        fun resetfilterlayout() {
            binding.searchsuggetion.visibility = View.VISIBLE
            binding.filterOp.visibility = View.GONE
            binding.filtersLay.visibility = View.GONE
            binding.searchbutton.visibility = View.VISIBLE
            binding.searchresultLay.visibility = View.GONE
            binding.searchbutton.visibility = View.VISIBLE
            //for showing medium filter open default
            dataa.clear()
            adapter.notifyDataSetChanged()
        }


    }


    data class Keyword(val name: String = "") {
        // Add a no-argument constructor
        constructor() : this("")
    }

    private fun checkForKeyword(searchTerm: String) {
        keywordsRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val matchingKeywords = mutableListOf<Keyword>()

                for (childSnapshot in snapshot.children) {
                    val keyword = childSnapshot.getValue(Keyword::class.java)
                    keyword?.let {
                        if (it.name.contains(searchTerm, ignoreCase = true)) {
                            matchingKeywords.add(it)
                            binding.loading.visibility = View.GONE

                        } else {
                            //handle error
//                            showToast("not found")
                            binding.loading.visibility = View.GONE

                        }
                    }
                }
                //update recycler view
                updateRecycler_searching_result(matchingKeywords)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
                Toast.makeText(this@Book_search_Activity, error.toString(), Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }

    fun updateRecycler_searching_result(list: MutableList<Keyword>) {
        val layoutManager =
            LinearLayoutManager(this@Book_search_Activity, LinearLayoutManager.VERTICAL, false)

        binding.searchsuggetion.layoutManager = layoutManager

//        var data=ArrayList<SearchResult>()
        // Assuming SearchResult is a data class with a single property "name"
        val data = list.map { SearchResult(it.name) }
        val historyDb = HistoryDatabaseHelper(this@Book_search_Activity)
        val allHistory = historyDb.getAllHistory()
        var adapter = SearchAdapter(object : SearchAdapter.OnItemClicked {
            override fun onItemClicked(position: Int, query: String) {
                binding.searingedittext.setText(query.toString().trim())
                // Set the cursor at the end of the text
                binding.searingedittext.setSelection(binding.searingedittext.text.length)
            }

        }, data, allHistory)
        binding.searchsuggetion.adapter = adapter


    }

    override fun onItemClicked(position: Int, query: String) {
//do some thing on item click
    }

    inner class SaveHistory {
        fun saveHistory(history_tx: String) {
            // DB instance
            val historyDb = HistoryDatabaseHelper(this@Book_search_Activity)
            //saving history into Room DB
            historyDb.addHistory(history_tx)
            //now if data saved into DB than display only for testing.....
            val allHistory = historyDb.getAllHistory()
            //   showToast(allHistory.toString()).......................Testing for

        }


    }

    fun onButtonClick(view: View) {
        // Reset all buttons to default color
        resetAllButtons()
        // Change the color of the clicked button
        (view as Button).setBackgroundColor(Color.WHITE)
        (view as Button).setTextColor(Color.BLACK)
        showToast(current_filter_open_identifier.toString())
        val adapter = Filtersadapter(object : Filtersadapter.onItemClick {
            override fun onItemCliked(position: Int, queryfinter: String) {
                //do next step on recycler item view clicked
//                showToast(queryfinter.toString())

// Call this function whenever you want to add a new item
                // Set the adapter for the RecyclerView
                binding.addedfilters.adapter = adapter


                addNewItem(queryfinter)


            }


        }, data)
        binding.filtecons.adapter = adapter
        adapter.notifyDataSetChanged()

    }

    private fun resetAllButtons() {
        val buttonContainer = findViewById<LinearLayout>(R.id.linearLayout16)

        for (i in 0 until buttonContainer.childCount) {
            val button = buttonContainer.getChildAt(i) as Button
            button.setBackgroundColor(getColor(R.color.MainAppColour))// Set your default color here
            button.setTextColor(getColor(R.color.white))// Set your default color here
        }
    }

    // Function to add a new item
    fun addNewItem(queryfinter: String) {
        Log.d("DEBUG", "Adding item to dataa: $queryfinter")

        // Assuming dataa is the new item to be added
        val queryResult = queryfinter
        if (!dataa.contains(Final_filter_tx(queryResult))) {
            dataa.add(Final_filter_tx(queryfinter))
        } else {
            showToast("added")
        }

        Log.d("DEBUG", "dataa size after addition: ${dataa.size}")

        // Notify the adapter about the insertion at the last position
        adapter.notifyItemInserted(dataa.size - 1)
    }


    override fun onItemCliked(position: Int, queryfinter: String) {
        TODO("Not yet implemented")
    }

    override fun OnItemClick(position: Int, filter: String) {

    }

    fun dummyBook() {
//        val layoutmanageradsMedium = GridLayoutManager(this, 2)
//        //  binding.books.layoutManager =FlexboxLayoutManager(this)
//        binding.books.layoutManager = layoutmanageradsMedium
//        //   binding.books.layoutManager =FlexboxLayoutManager(this)
//        var data4 = ArrayList<Ads_Medium_Model>()
//        data4.add(Ads_Medium_Model(R.drawable.bookdemo, "Rs math", "$345", "2023", "10th"))
//        data4.add(Ads_Medium_Model(R.drawable.bookdemo, "Rs math", "$345", "2023", "10th"))
//        data4.add(Ads_Medium_Model(R.drawable.bookdemo, "Rs math", "$345", "2023", "10th"))
//        data4.add(Ads_Medium_Model(R.drawable.bookdemo, "Rs math", "$345", "2023", "10th"))
//        data4.add(Ads_Medium_Model(R.drawable.bookdemo, "Rs math", "$345", "2023", "10th"))
//        data4.add(Ads_Medium_Model(R.drawable.bookdemo, "Rs math", "$345", "2023", "10th"))
//        data4.add(Ads_Medium_Model(R.drawable.bookdemo, "Rs math", "$345", "2023", "10th"))
//        data4.add(Ads_Medium_Model(R.drawable.bookdemo, "Rs math", "$345", "2023", "10th"))
//        data4.add(Ads_Medium_Model(R.drawable.bookdemo, "Rs math", "$345", "2023", "10th"))
//        data4.add(Ads_Medium_Model(R.drawable.bookdemo, "Rs math", "$345", "2023", "10th"))
//        data4.add(Ads_Medium_Model(R.drawable.bookdemo, "Rs math", "$345", "2023", "10th"))
//        data4.add(Ads_Medium_Model(R.drawable.bookdemo, "Rs math", "$345", "2023", "10th"))
//        data4.add(Ads_Medium_Model(R.drawable.bookdemo, "Rs math", "$345", "2023", "10th"))
//        data4.add(Ads_Medium_Model(R.drawable.bookdemo, "Rs math", "$345", "2023", "10th"))
//        data4.add(Ads_Medium_Model(R.drawable.bookdemo, "Rs math", "$345", "2023", "10th"))
//        data4.add(Ads_Medium_Model(R.drawable.bookdemo, "Rs math", "$345", "2023", "10th"))
//        data4.add(Ads_Medium_Model(R.drawable.bookdemo, "Rs math", "$345", "2023", "10th"))
//        data4.add(Ads_Medium_Model(R.drawable.bookdemo, "Rs math", "$345", "2023", "10th"))
//        data4.add(Ads_Medium_Model(R.drawable.bookdemo, "Rs math", "$345", "2023", "10th"))
//        data4.add(Ads_Medium_Model(R.drawable.bookdemo, "Rs math", "$345", "2023", "10th"))
//        data4.add(Ads_Medium_Model(R.drawable.bookdemo, "Rs math", "$345", "2023", "10th"))
//        data4.add(Ads_Medium_Model(R.drawable.bookdemo, "Rs math", "$345", "2023", "10th"))
//        data4.add(Ads_Medium_Model(R.drawable.bookdemo, "Rs math", "$345", "2023", "10th"))
//        data4.add(Ads_Medium_Model(R.drawable.bookdemo, "Rs math", "$345", "2023", "10th"))
//        data4.add(Ads_Medium_Model(R.drawable.bookdemo, "Rs math", "$345", "2023", "10th"))
//        data4.add(Ads_Medium_Model(R.drawable.bookdemo, "Rs math", "$345", "2023", "10th"))
//        data4.add(Ads_Medium_Model(R.drawable.bookdemo, "Rs math", "$345", "2023", "10th"))
//        var adapterrrrrr = Ads_Medium_size_Adapter(data4)
//        //  var adapterrrrrr= Ads_Medium_size_Adapter(data4)
//        // Setting the Adapter with the recyclerview
//        binding.books.adapter = adapterrrrrr


    }

    fun searchQuery(query: String) {
       //updating data for call book fetch api
        //e.g city, state, limit, page, search
        //creating object of objects temp_data
        // setting shimmer effect
        var temp_location_obj=Temp_location//>>>>>>>>>>>>>> //transmission sending
        var temp_Book_filters=BookFilter//<<<<<<<<<<<<<<<<< //getting receiving
        temp_Book_filters.page_ob= page_search_filter.toString()
        temp_Book_filters.limit_ob= limit_search_filter.toString()
        temp_Book_filters.search_ob= query.toString()
        temp_Book_filters.city_ob=temp_location_obj.city
        temp_Book_filters.state_ob= temp_location_obj.state
        temp_Book_filters.cate_id_ob= ""

        Log.d("finalquery",temp_Book_filters.search_ob+""+temp_Book_filters.city_ob+""+temp_Book_filters.state_ob+""+temp_Book_filters.limit_ob+""+temp_Book_filters.page_ob)

        updatedUIWhenQueryExecute()
            //here I am processing final query request to server

        fetchBooks()  //method to request server for books



    }

    fun updatedUIWhenQueryExecute() {
        runOnUiThread {
            binding.linear.visibility=View.VISIBLE
            binding.rycLoadingShimmerBook.visibility=View.VISIBLE
            binding.searchlayout.visibility = View.GONE //hiding current searchinfg layout
            //if filter on so first close with it
            Layouts().filterLAYOUT(false)
            //>>>>>>>>>>>> here should control progress bar
            binding.searchresultLay.visibility = View.VISIBLE //showing books layout if loaded
            trace_BACKSTATUS = 1
        }
    }

    inner class Layouts {
        fun filterLAYOUT(visibility: Boolean) {
            if (visibility) {
                binding.filterOp.visibility = View.VISIBLE
                binding.filtersLay.visibility = View.VISIBLE
            } else if (!visibility) {
                binding.filterOp.visibility = View.GONE
                binding.filtersLay.visibility = View.GONE
            }
        }

        fun searchLAYOUT(visibility: Boolean) {
            if (visibility) {
                binding.searchlayout.visibility = View.VISIBLE
            } else if (!visibility) {
                Click().hideKeyboardAndClearFocus(binding.searingedittext)
                binding.searchlayout.visibility = View.GONE
            }
        }
    }

    fun clear_back_search() {

    }

    override fun onBackPressed() {
        if (trace_BACKSTATUS == 0) {
            super.onBackPressed()
            finish()
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        } else if (trace_BACKSTATUS == 1) {
            binding.searchlayout.visibility = View.VISIBLE
            Click().resetfilterlayout()
            runOnUiThread {
                binding.lottieAnimationView.visibility = View.GONE
                binding.button5.visibility = View.GONE
                binding.textView12.visibility = View.GONE
                binding.changelocation.visibility = View.GONE
                binding.rycLoadingShimmerBook.visibility = View.VISIBLE
                binding.books.visibility = View.VISIBLE
            }
            val inputMethodManager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.showSoftInput(
                binding.searingedittext,
                InputMethodManager.SHOW_IMPLICIT
            )
            binding.searingedittext.requestFocus()
            trace_BACKSTATUS = 0


        } else if (trace_BACKSTATUS == 3) {

        }


    }

    private fun fetchBooks() {
        val bookRepository = BookRepository_filter(this)
        bookRepository.getBooks { books, error ->

            // Define your desired radius in kilometers
            val desiredRadius = Temp_location.search_radius.toDouble()+1.0

            if (books != null) {
                Log.e("YourActivity", books.toString())
                if(books.toString().equals("[]")){
                    runOnUiThread {
                        binding.books.visibility = View.GONE
                        binding.lottieAnimationView.visibility = View.VISIBLE
                        binding.button5.visibility = View.VISIBLE
                        binding.changelocation.visibility = View.VISIBLE
                        binding.textView12.visibility = View.VISIBLE
                        binding.rycLoadingShimmerBook.visibility = View.GONE
                    }
                }else {
                    runOnUiThread {
                        binding.lottieAnimationView.visibility = View.GONE
                        binding.button5.visibility = View.GONE
                        binding.textView12.visibility = View.GONE
                        binding.changelocation.visibility = View.GONE
                        binding.rycLoadingShimmerBook.visibility = View.VISIBLE
                        binding.books.visibility = View.VISIBLE
                    }
                    val nearbyBooks = mutableListOf<BookDetailseseIN>()  // Initialize an empty list

                    binding.books.layoutManager =FlexboxLayoutManager(this)
                    for (book in books) {
                        val distance = calculateDistance(
                            Temp_location.latitude.toDouble(),
                            Temp_location.longitude.toDouble(),
                            book.user_details.Location.Latitude.toDouble(),
                            book.user_details.Location.Longitude.toDouble()
                        )

                        // Check if the book is within the desired radius
                        if (distance <= desiredRadius) {
                            // Access individual book details here
                            Log.d("YourActivity", "Book Title: ${book.title}, Distance: $distance")
                            // Add more fields as needed

                            // Add the book to the list of nearby books
                            nearbyBooks.add(book)


                        }else {
                            runOnUiThread {
                                binding.books.visibility = View.GONE
                                binding.lottieAnimationView.visibility = View.VISIBLE
                                binding.button5.visibility = View.VISIBLE
                                binding.changelocation.visibility = View.VISIBLE
                                binding.textView12.visibility = View.VISIBLE
                                binding.rycLoadingShimmerBook.visibility = View.GONE
                            }
                        }
                    }
                    Log.d("your",nearbyBooks.toString())
                    if(nearbyBooks.toString().equals("[]")){
                        runOnUiThread {
                            binding.books.visibility = View.GONE
                            binding.lottieAnimationView.visibility = View.VISIBLE
                            binding.button5.visibility = View.VISIBLE
                            binding.changelocation.visibility = View.VISIBLE
                            binding.textView12.visibility = View.VISIBLE
                            binding.rycLoadingShimmerBook.visibility = View.GONE
                        }
                    }else{
                        updateRecyclerView(nearbyBooks)

                    }
                    // Now you have a list of nearby books, and you can use it to update your UI or perform other actions
                    // For example, update a RecyclerView
                }

            } else {
                // Handle the case where the list of books is null
                Log.e("YourActivity", "Received null list of books")
                runOnUiThread {
                    binding.books.visibility = View.GONE
                    binding.lottieAnimationView.visibility = View.VISIBLE
                    binding.button5.visibility = View.VISIBLE
                    binding.changelocation.visibility = View.VISIBLE
                    binding.textView12.visibility = View.VISIBLE
                    binding.rycLoadingShimmerBook.visibility = View.GONE
                }
            }



        }
    }
    fun update_location_on_header() {
        val tep_location_obj = Temp_location //temp location object
        var location_data = tep_location_obj.city + "\n" + tep_location_obj.state
        binding.loca.setText(location_data.toString()) //setting data on head textview

    }
    // Haversine formula to calculate distance between two points on the Earth
    private fun calculateDistance(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double
    ): Double {
        val R = 6371 // Radius of the Earth in kilometers
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                Math.sin(dLon / 2) * Math.sin(dLon / 2)
        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))
        return R * c
    }
    private fun updateRecyclerView(books: MutableList<BookDetailseseIN>) {
        // Convert the MutableList to List
        binding.linear.visibility=View.GONE
        binding.books.visibility=View.VISIBLE
       // val booksList: List<BookDetailseseIN> = books.toList()

        // Set up your RecyclerView with a FlexboxLayoutManager
        val layoutManager = FlexboxLayoutManager(this)
        binding.books.layoutManager = layoutManager

        // Set up your RecyclerView adapter with the new list of books including distance
        val adapter = Ads_Medium_size_Adapter(object :Ads_Medium_size_Adapter.OnItemClickListener{
            override fun onItemClick(position: Int, bookId: String) {
                try {
                    val intent = Intent(this@Book_search_Activity, Book_Ads_viewer_detailed::class.java)
                    if(bookId.toString().equals("")){
                        //handle impossible error It will throw when book id will be null
                    }else {
                        intent.putExtra("bookId", bookId.toString())
                        startActivity(intent)
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    }

                } catch (e: Exception) {
                    //handle error unable to start ad viewer activity

                }
            }

        },  books)

        // Set the adapter to your RecyclerView
        binding.books.adapter = adapter

        // Notify the adapter that the dataset has changed
        adapter.notifyDataSetChanged()
        binding.rycLoadingShimmerBook.visibility=View.GONE
    }

    private fun book_loading_shimmer() {
        // Set up your RecyclerView with a FlexboxLayoutManager
        val layoutManager = FlexboxLayoutManager(this)
        binding.rycLoadingShimmerBook.layoutManager = layoutManager
        // Set up your RecyclerView adapter with the new list of books including distance

        var data3 = ArrayList<Data_dummy>()
        data3.add(Data_dummy(1))
        data3.add(Data_dummy(1))
        data3.add(Data_dummy(1))
        data3.add(Data_dummy(1))
        data3.add(Data_dummy(1))
        data3.add(Data_dummy(1))
        data3.add(Data_dummy(1))
        val adapter = Adapter_book_dummy(data3)
        // Set the adapter to your RecyclerView
        binding.rycLoadingShimmerBook.adapter = adapter

        // Notify the adapter that the dataset has changed
        adapter.notifyDataSetChanged()

    }



    private fun onRightDrawableClick() {
        // Handle the click on the right drawable
        // Example: Clear the text in the EditText
        binding.searingedittext.text = null
    }
    private fun isTouchInsideBounds(event: MotionEvent, bounds: Rect): Boolean {
        val touchX = event.x.toInt()
        val touchY = event.y.toInt()
        return bounds.contains(touchX, touchY)
    }

    override fun onItemClick(position: Int, bookId: String) {
        //..............................DO SOME THING
    }

}


