
import android.content.Context
import android.content.Intent
import android.nfc.tech.MifareUltralight.PAGE_SIZE
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.bookmart.bookmart.Activites.Book_Ads_viewer_detailed
import com.bookmart.bookmart.Adapters.HomeFragment_Adapters.HomeBooks_adapter_small_size
import com.bookmart.bookmart.Adapters.HomeFragment_Adapters.Ads_Medium_size_Adapter
import com.bookmart.bookmart.Adapters.HomeFragment_Adapters.Categories_Adapters
import com.bookmart.bookmart.Adapters.HomeFragment_Adapters.TopAds_Adapter
import com.bookmart.bookmart.Adapters.MySliderAdapter
import com.bookmart.bookmart.Adapters.Poast_ads_Adapter.Post_ads_adapter
import com.bookmart.bookmart.AsyncOperation.YourViewModel
import com.bookmart.bookmart.Global_Object.BookFilter
import com.bookmart.bookmart.Global_Object.Temp_location

import com.bookmart.bookmart.Model.HomeFragment_Models.AdsSec_Model
import com.bookmart.bookmart.Model.HomeFragment_Models.Ads_Medium_Model
import com.bookmart.bookmart.Model.HomeFragment_Models.Categorie_Model
import com.bookmart.bookmart.Model.HomeFragment_Models.TopAds_Model
import com.bookmart.bookmart.Model.Post_Ads_model.ApiResponse
import com.bookmart.bookmart.R
import com.bookmart.bookmart.SearchAndFiltersBundle.Book_search_Activity
import com.bookmart.bookmart.databinding.FragmentHomeBinding
import com.bookmart.bookmart.volly_Android_networking.USER_API_SERVICES.BookDetailseseIN
import com.bookmart.bookmart.volly_Android_networking.USER_API_SERVICES.BookRepository_filter
import com.bookmart.bookmart.volly_Android_networking.USER_API_SERVICES.Required_FILES.VolleySingleton
import com.google.android.flexbox.FlexboxLayoutManager
import com.smarteist.autoimageslider.SliderView
import org.json.JSONException
import org.json.JSONObject


class HomeFragment:Fragment(R.layout.fragment_home) {
    // assign the _binding variable initially to null and
    // also when the view is destroyed again it has to be
    // set to null
    val binding by lazy {
        FragmentHomeBinding.inflate(layoutInflater)
    }
    private lateinit var viewModel: YourViewModel
    // on below line we are creating a variable
    // for our array list for storing our images.
    lateinit var imageUrl: ArrayList<String>

    // on below line we are creating
    // a variable for our slider view.
    // Move the initialization of activityContext inside onAttach
    // Move the initialization of activityContext inside onAttach



    lateinit var sliderView: SliderView
    private var isLoading = false
    private var isLastPage = false
    private var currentPage = 1
    // on below line we are creating
    // a variable for our slider adapter.
    private var listener: OnFragmentInteractionListener? = null
var rcy_status:Int=1
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Button click listener


        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sliderView = view.findViewById<SliderView>(R.id.slider)
        viewModel = ViewModelProvider(this).get(YourViewModel::class.java)

        // Call the function in the ViewModel to perform async operation
        viewModel.performAsyncOperation()
        imageSlidder()
cate_Adapter()
loadBooks_near_you()
//        binding.scrollView2.setOnTouchListener { _, _ -> true }

//handle event on search box >>> ?? Redirect on search activity

        binding.findmore.setOnClickListener {
            val intent = Intent(activity, Book_search_Activity::class.java)
            startActivity(intent)
        }

        binding.searchmore.setOnClickListener {
            val intent = Intent(activity, Book_search_Activity::class.java)
            startActivity(intent)
        }


        binding.commanryc.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val layoutManager = recyclerView.layoutManager as FlexboxLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                // Check if the user has scrolled to the last item
                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount
                    && firstVisibleItemPosition >= 0
                    && totalItemCount >= PAGE_SIZE
                ) {
                    // Check if the scroll state is IDLE
                    if (!isLoading && !isLastPage && layoutManager.findLastCompletelyVisibleItemPosition() == totalItemCount - 1) {
                        // Load more data only when the user scrolls to the last item
                        currentPage++
                        // Add your logic for loading more data here...
                        // For example, fetch the next page of data
                      //  fetchNextPage()
                    Toast.makeText(activity, "hello", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })





        // Add the scroll listener for endless scrolling
        // Adding on scroll change listener method for our nested scroll view.
        // Adding on scroll change listener method for our nested scroll view.



    }

    interface OnFragmentInteractionListener {
        fun onButtonClicked(data: String)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException("$context must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    // Inside your fragment, call this method when needed
    private fun someMethod() {
        // Call the interface method
        listener?.onButtonClicked("Data to pass to activity")
    }

    fun imageSlidder() {
        try {
            // Your image URLs
            val imageUrls = listOf(
                "https://practice.geeksforgeeks.org/_next/image?url=https%3A%2F%2Fmedia.geeksforgeeks.org%2Fimg-practice%2Fbanner%2Fdsa-self-paced-thumbnail.png&w=1920&q=75",
                "https://practice.geeksforgeeks.org/_next/image?url=https%3A%2F%2Fmedia.geeksforgeeks.org%2Fimg-practice%2Fbanner%2Fdata-science-live-thumbnail.png&w=1920&q=75",
                "https://practice.geeksforgeeks.org/_next/image?url=https%3A%2F%2Fmedia.geeksforgeeks.org%2Fimg-practice%2Fbanner%2Ffull-stack-node-thumbnail.png&w=1920&q=75"
            )

            // Launch a coroutine in the global scope


            val sliderAdapter = MySliderAdapter(imageUrls)
            binding.slider.setSliderAdapter(sliderAdapter)

            // Set auto cycle direction, adapter, scroll time, auto cycle, and start auto cycle
            binding.slider.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR
            binding.slider.scrollTimeInSec = 3
            binding.slider.isAutoCycle = true
            binding.slider.startAutoCycle()
        }catch (e:Exception){
            //handle error
        }



}

    fun cate_Adapter() {
        binding.shimmerViewContainer.visibility = View.VISIBLE
        binding.shimmerViewContainer.startShimmer()
        // Initialize RecyclerView
        binding.recycForCateHome.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)

        val categoriesList = mutableListOf<ApiResponse>()
        val url = "https://www.onlinevideodownloader.co/bookstore/api_v1/app/get_all_categories.php"

        val request = StringRequest(
            Request.Method.GET, url,
            { response ->
                // Handle the successful response
                try {
                    // Parse the JSON response
                    val jsonResponse = JSONObject(response)
                    val categoriesArray = jsonResponse.getJSONArray("categories")

                    // Process the list of categories
                    for (i in 0 until categoriesArray.length()) {
                        val categoryObject = categoriesArray.getJSONObject(i)
                        val categoryId = categoryObject.getString("category_id")
                        val categoryName = categoryObject.getString("category_name")
                        val categoryImage = categoryObject.getString("category_image")

                        // Do something with the category data
                        // Create a Category object and add it to the list
                        val category =
                            ApiResponse(categoryId.toString(), categoryImage, categoryName)
                        categoriesList.add(category)
                    }

                    // Create a Post_ads_adapter and set it to the RecyclerView
                    // Assuming this code is inside YourActivity

// Create a Post_ads_adapter and set it to the RecyclerView
                    val adapter = Post_ads_adapter(
                        object : Post_ads_adapter.ImageLoadCallback {
                            override fun onImageLoadSuccess() {
                                // Handle image load success if needed
                                // For example, you can perform additional UI updates here
                                binding.shimmerViewContainer.hideShimmer()
                            }

                            override fun onImageLoadError() {
                                // Handle image load error if needed
                                // For example, you can perform additional error handling actions
                                binding.shimmerViewContainer.hideShimmer()
                            }
                        },
                        categoriesList,
                        object : Post_ads_adapter.OnItemClickListener {
                            override fun onItemClicked(position: Int) {
                                // Handle item click if needed
                         //    Toast.makeText(activity, position.toString(), Toast.LENGTH_SHORT).show()
                                val intent = Intent(activity,Book_search_Activity::class.java)
                                intent.putExtra("cate_ID",position.toString().toString())
                                startActivity(intent)


                            }
                        },
                        object : Post_ads_adapter.OnImageClickListener {
                            override fun onImageClicked(position: Int, title: String, id: String) {
                                    // Updating UI and setting category on TextView
                                Toast.makeText(activity, "Your book category $title", Toast.LENGTH_SHORT).show()

                            }
                        }
                    )

// Set the adapter to the RecyclerView

                    binding.recycForCateHome.adapter = adapter


                } catch (e: JSONException) {
                    e.printStackTrace()
                    binding.shimmerViewContainer.stopShimmer()
                }
            },
            { error ->
                // Handle errors
                error.printStackTrace()
            })

        // Add the request to the RequestQueue
        VolleySingleton.getInstance(requireContext()).requestQueue.add(request)
    }
    private fun updateRecyclerView(books: MutableList<BookDetailseseIN>) {
        // Convert the MutableList to List
        val booksList: List<BookDetailseseIN> = books.toList()
        // Set up your RecyclerView with a FlexboxLayoutManagerj
     //  val layoutManager = FlexboxLayoutManager(activity)
        binding.nearyou.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        binding.rcylersearchmost.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)



        if(rcy_status==1){
            // Set up your RecyclerView adapter with the new list of books including distance
            val adapter = HomeBooks_adapter_small_size(object :HomeBooks_adapter_small_size.onItemClick{
                override fun onItemClick(position: Int, bookId: String) {
                    try {
                        val intent = Intent(activity, Book_Ads_viewer_detailed::class.java)
                        if(bookId.toString().equals("")){
                            //handle impossible error It will throw when book id will be null
                        }else {
                            intent.putExtra("bookId", bookId.toString())
                            startActivity(intent)
                        }

                    } catch (e: Exception) {
                        //handle error unable to start ad viewer activity

                    }
                }

            },  booksList)

            // Set the adapter to your RecyclerView
            binding.nearyou.adapter = adapter
            // Notify the adapter that the dataset has changed
            rcy_status=2

            adapter.notifyDataSetChanged()
        }else {
            // Set up your RecyclerView adapter with the new list of books including distance
            val adapter = HomeBooks_adapter_small_size(object :HomeBooks_adapter_small_size.onItemClick{
                override fun onItemClick(position: Int, bookId: String) {
                    try {
                        val intent = Intent(activity, Book_Ads_viewer_detailed::class.java)
                        if(bookId.toString().equals("")){
                            //handle impossible error It will throw when book id will be null
                        }else {
                            intent.putExtra("bookId", bookId.toString())
                            startActivity(intent)
                        }

                    } catch (e: Exception) {
                        //handle error unable to start ad viewer activity

                    }
                }

            },  booksList)

            // Set the adapter to your RecyclerView
            binding.rcylersearchmost.adapter = adapter
            // Notify the adapter that the dataset has changed
            adapter.notifyDataSetChanged()
        }






    }
    private fun fetchBooks() {
        val bookRepository = BookRepository_filter(requireContext())
        bookRepository.getBooks { books, error ->

            // Define your desired radius in kilometers
            val desiredRadius = 50000.0

            if (books != null) {
                Log.e("YourActivity", books.toString())
                if(books.toString().equals("[]")){

                }else {
                    val nearbyBooks = mutableListOf<BookDetailseseIN>()  // Initialize an empty list
                    binding.nearyou.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
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

                        }
                    }
                    Log.d("your",nearbyBooks.toString())
                    if(nearbyBooks.toString().equals("[]")){

                    }else{
                        updateRecyclerView(nearbyBooks)
                        loadBooks_near_most_search()
                        binding.mainshimmer.stopShimmer()
                        binding.mainshimmer.hideShimmer()

                    }
                    // Now you have a list of nearby books, and you can use it to update your UI or perform other actions
                    // For example, update a RecyclerView
                }

            } else {
                // Handle the case where the list of books is null
                Log.e("YourActivity", "Received null list of books")

            }



        }
    }
    private fun fetchBooks2() {
        val bookRepository = BookRepository_filter(requireActivity())
        bookRepository.getBooks { books, error ->

            // Define your desired radius in kilometers
            val desiredRadius = 50000.0

            if (books != null) {
                Log.e("YourActivity", books.toString())
                if(books.toString().equals("[]")){

                }else {
                    val nearbyBooks2 = mutableListOf<BookDetailseseIN>()  // Initialize an empty list
                    binding.nearyou.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
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
                            nearbyBooks2.add(book)


                        }else {

                        }
                    }
                    Log.d("your",nearbyBooks2.toString())
                    if(nearbyBooks2.toString().equals("[]")){

                    }else{
                        updateRecyclerView(nearbyBooks2)
                        loadBooks_third_last_view()


                    }
                    // Now you have a list of nearby books, and you can use it to update your UI or perform other actions
                    // For example, update a RecyclerView
                }

            } else {
                // Handle the case where the list of books is null
                Log.e("YourActivity", "Received null list of books")


            }



        }
    }
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
    fun loadBooks_near_you(){
        var temp_location_obj=Temp_location//>>>>>>>>>>>>>> //transmission sending
        var temp_Book_filters= BookFilter//<<<<<<<<<<<<<<<<< //getting receiving
        temp_Book_filters.page_ob= "1"
        temp_Book_filters.limit_ob= "5"
        temp_Book_filters.search_ob= ""
        temp_Book_filters.city_ob=temp_location_obj.city
        temp_Book_filters.state_ob= temp_location_obj.state
        temp_Book_filters.cate_id_ob= ""

        Log.d("finalquery",temp_Book_filters.search_ob+""+temp_Book_filters.city_ob+""+temp_Book_filters.state_ob+""+temp_Book_filters.limit_ob+""+temp_Book_filters.page_ob)
fetchBooks()
    }
    fun loadBooks_near_most_search(){
        var temp_location_obj=Temp_location//>>>>>>>>>>>>>> //transmission sending
        var temp_Book_filters= BookFilter//<<<<<<<<<<<<<<<<< //getting receiving
        temp_Book_filters.page_ob= "2"
        temp_Book_filters.limit_ob= "5"
        temp_Book_filters.search_ob= ""
        temp_Book_filters.city_ob=temp_location_obj.city
        temp_Book_filters.state_ob= temp_location_obj.state
        temp_Book_filters.cate_id_ob= ""

        Log.d("finalquery",temp_Book_filters.search_ob+""+temp_Book_filters.city_ob+""+temp_Book_filters.state_ob+""+temp_Book_filters.limit_ob+""+temp_Book_filters.page_ob)
        fetchBooks2()
    }
    fun loadBooks_third_last_view(){
        var temp_location_obj=Temp_location//>>>>>>>>>>>>>> //transmission sending
        var temp_Book_filters= BookFilter//<<<<<<<<<<<<<<<<< //getting receiving
        temp_Book_filters.page_ob= currentPage.toString()
        temp_Book_filters.limit_ob= "5"
        temp_Book_filters.search_ob= ""
        temp_Book_filters.city_ob=temp_location_obj.city
        temp_Book_filters.state_ob= temp_location_obj.state
        temp_Book_filters.cate_id_ob= ""

        Log.d("finalquery",temp_Book_filters.search_ob+""+temp_Book_filters.city_ob+""+temp_Book_filters.state_ob+""+temp_Book_filters.limit_ob+""+temp_Book_filters.page_ob)
        fetchBooks3()
    }
    private fun fetchBooks3() {
        val bookRepository = BookRepository_filter(requireActivity())
        bookRepository.getBooks { books, error ->

            // Define your desired radius in kilometers
            val desiredRadius = 50000.0

            if (books != null) {
                Log.e("YourActivity", books.toString())
                if(books.toString().equals("[]")){

                }else {
                    val nearbyBooks2 = mutableListOf<BookDetailseseIN>()  // Initialize an empty list
                    binding.nearyou.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
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
                            Log.d("YourActivity3", "Book Title: ${book.title}, Distance: $distance")
                            // Add more fields as needed

                            // Add the book to the list of nearby books
                            nearbyBooks2.add(book)


                        }else {

                        }
                    }
                    Log.d("your",nearbyBooks2.toString())
                    if(nearbyBooks2.toString().equals("[]")){

                    }else{

                        updateRecyclerView_last(nearbyBooks2)



                    }
                    // Now you have a list of nearby books, and you can use it to update your UI or perform other actions
                    // For example, update a RecyclerView
                }

            } else {
                // Handle the case where the list of books is null
                Log.e("YourActivity", "Received null list of books")


            }



        }
    }
    private fun updateRecyclerView_last(books: MutableList<BookDetailseseIN>) {
        // Convert the MutableList to List

//        val booksList: List<BookDetailseseIN> = books.toList()

        // Set up your RecyclerView with a FlexboxLayoutManager
        val layoutManager = FlexboxLayoutManager(requireActivity())
        binding.commanryc.layoutManager = layoutManager

        // Set up your RecyclerView adapter with the new list of books including distance
        val adapter = Ads_Medium_size_Adapter(object :Ads_Medium_size_Adapter.OnItemClickListener{
            override fun onItemClick(position: Int, bookId: String) {
                try {
                    val intent = Intent(requireActivity(), Book_Ads_viewer_detailed::class.java)
                    if(bookId.toString().equals("")){
                        //handle impossible error It will throw when book id will be null
                    }else {
                        intent.putExtra("bookId", bookId.toString())
                        startActivity(intent)
                    }

                } catch (e: Exception) {
                    //handle error unable to start ad viewer activity

                }
            }

        },  books)
        adapter.addBooks(books)
        // Set the adapter to your RecyclerView
        binding.commanryc.adapter = adapter
        // Notify the adapter that the dataset has changed
        adapter.notifyDataSetChanged()
    }

}