package com.bookmart.bookmart.Activites

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.Manifest
import com.android.volley.Request
import android.content.Context
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.android.volley.NoConnectionError
import com.android.volley.RequestQueue
import com.android.volley.TimeoutError
import com.android.volley.VolleyError
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bookmart.bookmart.BooksShowMet.BookDetailsese
import com.bookmart.bookmart.BooksShowMet.BookAdaptermetshow
import com.bookmart.bookmart.BooksShowMet.BookRepository
import com.bookmart.bookmart.R
import com.bookmart.bookmart.Required_App_operations.NetworkChangeReceiver
import com.bookmart.bookmart.databinding.ActivityBookAdsViewerDetailedBinding

import com.bookmart.bookmart.Global_Object.UniversalUserID_and_Limit_book
import com.bookmart.bookmart.SearchAndFiltersBundle.Book_search_Activity
import com.denzcoskun.imageslider.ImageSlider
import com.denzcoskun.imageslider.models.SlideModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.squareup.picasso.MemoryPolicy
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.Date
import java.util.concurrent.TimeUnit

class Book_Ads_viewer_detailed : AppCompatActivity(), OnMapReadyCallback,BookAdaptermetshow.OnItemClickListener{
    val binding by lazy {
       ActivityBookAdsViewerDetailedBinding.inflate(layoutInflater)
    }
    var limitBook:Int=10
    var user_id2nd:String=""
    //images url of posted book
    var photo111:String=""
    var photo222:String=""
    var photo333:String=""
    var pagignation:String=""
    private var isFullscreen = false
//    private lateinit var recyclerView: RecyclerView
//    private lateinit var bookAdapter: BookAdapter
    private lateinit var bookList: MutableList<BookDetailsese>
    private lateinit var networkReceiver: NetworkChangeReceiver
    private var maxRetries = 5
    private var retryCount = 0
    var latitude: Double = 0.00000
    var longitude: Double = 0.00000
    private var marker: Marker? = null
    var Massage_STA: String = ""
    var Call_STA: String = ""
    var major_back:Int=1

    // Instantiate the RequestQueue.
    private lateinit var requestQueue: RequestQueue
    var url: String = ""
    var bookId: String = "null"
    private lateinit var mapView: MapView
    private lateinit var mMap: GoogleMap
    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                val ph = binding.callnow.hint.toString().trim()
                dialPhoneNumber(ph)
            } else {
                // Permission denied, handle accordingly (e.g., inform the user)
                showToast("cannot make call accept permission.")

            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        // Initialize the network receiver
        // getting the data from our
        // intent in our uri.
        val uri: Uri? = intent.data

        // checking if the uri is null or not.
        if (uri != null) {
            // if the uri is not null then we are getting the
            // path segments and storing it in list.
            val parameters: List<String> = uri.getPathSegments()

            // after that we are extracting string from that parameters.
            val param = parameters[parameters.size - 1]
//showToast(param)
            //there is bookId is ready, I and you can proceed this for further execution || Load book
            // Toast.makeText(this@Book_Ads_viewer_detailed, bookId, Toast.LENGTH_SHORT).show()
            // Instantiate the RequestQueue.
            val extractedId = extractIdFromUrl(param.toString())
            requestQueue = Volley.newRequestQueue(this)
            // Replace "123" with the actual book ID
            url =
                "https://www.onlinevideodownloader.co/bookstore/api_v1/app/get_book_details.php?book_id=$extractedId"
            // Make a GET request with a specific book ID
            major_back=2
            makeGetRequest_FOR_BOOK_DETAIL(url)
        }else {
            try {
                bookId = intent.getStringExtra("bookId").toString()

                if (bookId.toString() != "20") {
                    //there is bookId is ready, I and you can proceed this for further execution || Load book
                    // Toast.makeText(this@Book_Ads_viewer_detailed, bookId, Toast.LENGTH_SHORT).show()
                    // Instantiate the RequestQueue.
                    requestQueue = Volley.newRequestQueue(this)
                    // Replace "123" with the actual book ID
                    url =
                        "https://www.onlinevideodownloader.co/bookstore/api_v1/app/get_book_details.php?book_id=$bookId"
                    // Make a GET request with a specific book ID
                    makeGetRequest_FOR_BOOK_DETAIL(url)

                } else if (bookId.toString().equals("null")) {
                    Toast.makeText(this@Book_Ads_viewer_detailed, "null value", Toast.LENGTH_SHORT)
                        .show()

                    //book id found null, I am again happy to say, It's impossible
                    //handle error here
                } else {
                    //handle impossible error
                }
            } catch (e: Exception) {
                //handle error here

            }
        }
//        recyclerView = findViewById(R.id.rec)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//
//        bookList = mutableListOf()
//        bookAdapter = BookAdapter(bookList)
//        recyclerView.adapter = bookAdapter

        //MAP
        mapView = findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)


        //loading map
// maploader()
        //images loader
        // book da[ter loader
// handling click operations massage
        binding.massagenow.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            if (Massage_STA.equals("0")) {
                //disabled
                showToast("Disabled massaging")
                binding.progressBar.visibility = View.GONE

            } else if (Massage_STA.equals("1")) {
                //enabled
                // Replace "1234567890" with the actual phone number you want to open in WhatsApp
                // Replace "Hello, this is a test message!" with your desired pre-filled message
                AlertDialog.Builder(this@Book_Ads_viewer_detailed)
                    .setTitle("send message to Harsh Verma")
                    .setMessage("massage regarding inquiry of posted book")
                    .setIcon(R.drawable.baseline_error_24)
                    .setPositiveButton("massage") { dialog, _ ->
                        // Handle OK button click
                        openWhatsAppWithNumberAndMessage(  binding.massagenow.hint.toString() , "demo massage")
                        dialog.dismiss()
                    }
                    .setNegativeButton("cancel") { dialog, _ ->
                        // Handle Cancel button click
                        binding.progressBar.visibility = View.GONE
                        dialog.dismiss()
                    }
                    .show()
                binding.progressBar.visibility = View.GONE

            } else if (Massage_STA.equals("")) {
                //null
                showToast("Disabled messaging")
                binding.progressBar.visibility = View.GONE

            }
        }
        //handling call button click
        binding.callnow.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            if (Call_STA.equals("0")) {
                //disabled
                showToast("Disabled Calling")
                binding.progressBar.visibility = View.GONE

            } else if (Call_STA.equals("1")) {
                //enabled
                // Replace "1234567890" with the actual phone number you want to open in WhatsApp
                // Replace "Hello, this is a test message!" with your desired pre-filled message
                //enabled
                // Replace "1234567890" with the actual phone number you want to open in WhatsApp
                // Replace "Hello, this is a test message!" with your desired pre-filled message
                AlertDialog.Builder(this@Book_Ads_viewer_detailed)
                    .setTitle("Make call to Harsh Verma")
                    .setMessage("call regarding inquiry of posted book")
                    .setIcon(R.drawable.baseline_error_24)
                    .setPositiveButton("call") { dialog, _ ->
                        // Handle OK button click
                        // Permission request launcher
                        checkAndRequestCallPermission()
//                        val ph=binding.callnow.hint.toString().trim()
//                        showToast(ph)
//                      dialPhoneNumber(ph)
                        dialog.dismiss()
                    }
                    .setNegativeButton("cancel") { dialog, _ ->
                        // Handle Cancel button click
                        binding.progressBar.visibility = View.GONE
                        dialog.dismiss()
                    }
                    .show()
                binding.progressBar.visibility = View.GONE


            } else if (Call_STA.equals("")) {
                //null
                showToast("Disabled calling")
                binding.progressBar.visibility = View.GONE

            }
        }
        //sharebook
        binding.sharebook.setOnClickListener {
            binding.progressBar.visibility = View.VISIBLE
            shareProduct("JDDJF", "IHDFIU")
        }
        //back button
        binding.backbutton.setOnClickListener {
            if(major_back==2){
                // Set the exit animation
                val intent=Intent(this@Book_Ads_viewer_detailed,MainHome::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
// Finish the current activity
                finish();
            }else {
                // Set the exit animation
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
// Finish the current activity
                finish();
            }




        }
//full screen image slider
        binding.fullscreen.setOnClickListener{
            toggleFullscreen()
        }
        binding.exitfullscreen.setOnClickListener{
            toggleFullscreen()
        }
binding.backbuttonn.setOnClickListener{
    toggleFullscreen()
}
        //search act
        binding.fullbackexit.setOnClickListener{
            var intent=Intent(this@Book_Ads_viewer_detailed, Book_search_Activity::class.java)
            startActivity(intent)

        }
//load more button backend
        binding.loadmore.setOnClickListener{
            //updating book limit
            binding.loadmore.setOnClickListener {
                // Updating book limit
                limitBook += 10
                showToast(limitBook.toString())
                makeVolleyRequest(limitBook.toString())
            }
        }
    }
    private fun toggleFullscreen() {
        val params = binding.imageslide.layoutParams as ViewGroup.MarginLayoutParams

        if (isFullscreen) {
            // If in fullscreen, set it to the initial position
//            params.height = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._300sdp)
//            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            binding.imageslider.visibility=View.GONE
            binding.linearLayout8.visibility=View.VISIBLE
           // showToast("Exit full screen")
            isFullscreen = false
        } else {
            // If not in fullscreen, set it to fullscreen
//            params.height = ViewGroup.LayoutParams.MATCH_PARENT
//            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            binding.imageslider.visibility=View.VISIBLE
            imaeSliderloader(photo111, photo222, photo333,binding.imageslider)
            binding.linearLayout8.visibility=View.GONE
         //   showToast("full screen")
            isFullscreen = true
        }

        // Apply the layout changes
        binding.imageslide.layoutParams = params
    }

    private fun dialPhoneNumber(phoneNumber: String) {
        if (phoneNumber.isNotEmpty()) {
            val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber"))
            startActivity(dialIntent)
            binding.progressBar.visibility = View.GONE
        } else {
            // Handle the case where the phone number is empty
            Toast.makeText(this, "Phone number is empty", Toast.LENGTH_SHORT).show()
        }
    }

    private fun shareProduct(productName: String, productDescription: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"

        // Add text to the intent
        intent.putExtra(
            Intent.EXTRA_TEXT,
            "Check out this product: $productName\n\nDescription: $productDescription"
        )

        // Create a chooser to let the user pick the app
        val chooserIntent = Intent.createChooser(intent, "Share Product via")

        // Verify that there is an app to handle the intent
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(chooserIntent)
            binding.progressBar.visibility = View.GONE
        } else {
            // No app can handle the intent. Handle accordingly.
            // You might want to inform the user that no app is available.
        }
    }

    private fun openWhatsAppWithNumberAndMessage(phoneNumber: String, message: String) {
        // Encode the message for the URI
        val encodedMessage = Uri.encode(message)

        // Create a URI with the phone number and the pre-filled message
        val uri = Uri.parse("smsto:$phoneNumber?body=$encodedMessage")

        // Create an intent with the ACTION_SENDTO action and the WhatsApp package
        val intent = Intent(Intent.ACTION_SENDTO, uri)
        intent.setPackage("com.whatsapp")

        // Check if WhatsApp is installed on the device
        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
            binding.progressBar.visibility = View.GONE
        } else {
            // WhatsApp is not installed. Handle accordingly.
            // You might want to redirect the user to the Play Store to install WhatsApp.
        }
    }


    override fun onMapReady(googleMap: GoogleMap) {
        // Handle map initialization here
        //.................................
        // Set the initial map type (you can change this based on your requirements)
        googleMap.mapType = GoogleMap.MAP_TYPE_HYBRID
        googleMap.isTrafficEnabled = true
        googleMap.uiSettings.isRotateGesturesEnabled = true
        googleMap.uiSettings.isScrollGesturesEnabledDuringRotateOrZoom = true
        googleMap.uiSettings.isZoomControlsEnabled = true
        googleMap.uiSettings.isMapToolbarEnabled = true
        val location = LatLng(latitude, longitude) // San Francisco coordinates
        googleMap.addMarker(MarkerOptions().position(location).title("Book location."))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 17f))
        // Set the listener for marker click events
// Set the listener for marker click events
        googleMap.setOnMarkerClickListener { clickedMarker ->
            // When a marker is clicked, open Google Maps with the marker's location
            openGoogleMaps(clickedMarker.position, clickedMarker.title)
            true // Return true to consume the event and prevent the default behavior
        }

    }

    private fun openGoogleMaps(location: LatLng, label: String?) {
        val uri =
            Uri.parse("geo:${location.latitude},${location.longitude}?q=${location.latitude},${location.longitude}($label)")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.google.android.apps.maps")
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }



    fun imaeSliderloader(ph1: String, ph2: String, ph3: String,view:ImageSlider) {
        val imageList = ArrayList<SlideModel>() // Create image list
// imageList.add(SlideModel("String Url" or R.drawable)
// imageList.add(SlideModel("String Url" or R.drawable, "title") You can add title
        imageList.add(SlideModel(ph1, "Book front Image"))
        imageList.add(SlideModel(ph2, "Book back side image"))
        imageList.add(SlideModel(ph3, "lookup book"))

        view.setImageList(imageList)

// Set the image slide load listener


    }

    fun maploader() {
        mapView.getMapAsync(this)
    }

    private fun makeGetRequest_FOR_BOOK_DETAIL(url: String) {
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                handlebookResponse(response)
                // Toast.makeText(this@Book_Ads_viewer_detailed,response.toString(),Toast.LENGTH_SHORT).show()
            },
            { error ->
                showToast("caled" + error.message.toString())
                if (shouldRetry(error) && retryCount < maxRetries) {
                    // Retry the request
                    retryCount++
                    showToast("caled$retryCount")
                    networkReceiver = NetworkChangeReceiver { onNetworkChanged() }
                    val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
                    registerReceiver(networkReceiver, filter)
                    //  makeGetRequest_FOR_BOOK_DETAIL(url)
                } else {
                    handleError(error)
                }
            })

        requestQueue.add(jsonObjectRequest)
    }

    private fun makeGetRequest_FOR_FETC_USER_DEtAIL(url: String) {
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                handleApiResponse(response)
                //   Toast.makeText(this@Book_Ads_viewer_detailed,response.toString(),Toast.LENGTH_SHORT).show()
            },
            { error ->
                if (shouldRetry(error) && retryCount < maxRetries) {
                    // Retry the requestggi
                    retryCount++
                    makeGetRequest_FOR_FETC_USER_DEtAIL(url)
                } else {
                    handleError(error)
                }
            })

        requestQueue.add(jsonObjectRequest)
    }

    private fun handlebookResponse(response: JSONObject) {
        try {


            val error = response.getBoolean("error")
            val message = response.getString("message")

            if (!error) {
                // Parse the JSON response and set values to variables
                val bookDetails = response.getJSONObject("book_details")

                val bookId = bookDetails.getInt("book_id")
                val title = bookDetails.getString("title")
                val author = bookDetails.getString("author")
                val description = bookDetails.getString("description")
                val price = bookDetails.getString("price")
                val book_condition = bookDetails.getString("book_condition")
                var user_id = bookDetails.getString("user_id")
                val photo1 = bookDetails.getString("photo1")
                val photo2 = bookDetails.getString("photo2")
                val photo3 = bookDetails.getString("photo3")
                val bookname = bookDetails.getString("name")
                val publication_year = bookDetails.getString("publication_year")
                val publisher = bookDetails.getString("publisher")
                val basedon = bookDetails.getString("based_on")
                val standard = bookDetails.getString("standard")
                val boardprefrence = bookDetails.getString("board_preference")
                val medium_languge = bookDetails.getString("medium")
                val localAddress = bookDetails.getString("local_address")
                val whatsappnumber = bookDetails.getString("whatsapp_number")
                val whatsapp_status = bookDetails.getString("whatsapp_enabled")
                val phonenumber = bookDetails.getString("phone_number")
                val phoneenabled = bookDetails.getString("phone_enabled")
                val created_at = bookDetails.getString("created_at")
                val category_id = bookDetails.getString("category_id")
                //setting
               user_id2nd=user_id
                // Now, you can use these variables as needed (e.g., update UI or perform other operations)
                // For example:
                //lod book images on slide imageview
                //setting photo url on image url verile
                photo111=photo1
                photo222=photo2
                photo333=photo3
                imaeSliderloader(photo1, photo2, photo3,binding.imageslide)
                //set book data on table
                setTbleData(
                    bookname,
                    title,
                    author,
                    publication_year,
                    publisher,
                    basedon,
                    standard,
                    boardprefrence,
                    medium_languge,
                    book_condition,
                    price
                )
                binding.shimmerrahuViewhero.stopShimmer()
                binding.shimmerrahuViewhero.hideShimmer()
                //set book posted data
                setBookPosteData(created_at)
                maxRetries = 5
//set description and address And user ID for user location loading on map and profile url and user name
                setAddressAndLocation(description, localAddress, user_id)
                //set book main title
                setbookainTitle(bookname, title)
                // make usable contact and massage buttons, WhatsApp massager(for chat), contact number(for call)
                operationcontactMethods(whatsappnumber, whatsapp_status, phonenumber, phoneenabled)


            } else {
                Toast.makeText(applicationContext, "Error: $message", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(applicationContext, "Error parsing response", Toast.LENGTH_SHORT).show()
        }


    }

    private fun shouldRetry(error: VolleyError): Boolean {
        // Add conditions based on the type of error to decide whether to retry or not
        return error is TimeoutError || error is NoConnectionError
    }

    private fun handleApiResponse(response: JSONObject) {
        try {
            val error = response.getBoolean("error")
            val message = response.getString("message")

            if (!error) {
                val userDetails = response.getJSONObject("user_details")
                val userId = userDetails.getInt("ID")
                val username = userDetails.getString("Username")
                val profilePicture = userDetails.getString("ProfilePicture")

                val location = userDetails.getJSONObject("Location")
                val latitude = location.getString("Latitude")
                val longitude = location.getString("Longitude")
                val address = location.getString("Address")
                val city = location.getString("City")
                val state = location.getString("State")
                val country = location.getString("Country")
                val postalCode = location.getString("PostalCode")

//                val toastMessage =
//                    "User ID: $userId\nUsername: $username\nProfile Picture: $profilePicture\n" +
//                            "Latitude: $latitude\nLongitude: $longitude\nAddress: $address\nCity: $city\n" +
//                            "State: $state\nCountry: $country\nPostal Code: $postalCode"
//
//                Toast.makeText(applicationContext, toastMessage, Toast.LENGTH_LONG).show()
                //setting location system on mapp and textview
                setlocationdata(address, latitude.toString(), longitude.toString())
                //setting data on profile view
                setprofiledata(profilePicture, username)
//                showToast(address)
//                showToast(latitude)
//                showToast(longitude)
            } else {
                Toast.makeText(applicationContext, "Error: $message", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(applicationContext, "Error parsing response user", Toast.LENGTH_SHORT)
                .show()
        }
    }


    private fun handleError(error: Exception) {
        // Handle errors here
        // Display an error message or log the error
        Toast.makeText(this@Book_Ads_viewer_detailed, "error", Toast.LENGTH_SHORT).show()
    }

     fun showToast(message: String) {
        Toast.makeText(this@Book_Ads_viewer_detailed, message, Toast.LENGTH_SHORT).show()
    }

    fun setTbleData(
        bookName: String,
        BookTitle: String,
        authorName: String,
        publicationYear: String,
        publisher: String,
        basedOn: String,
        standard: String,
        boardPreffrence: String,
        languageMedium: String,
        condition: String,
        bookPrise: String
    ) {
        binding.bookname.setText(bookName)
        binding.booktitle.setText(BookTitle)
        binding.authorName.setText(authorName)
        binding.publicationyear.setText(publicationYear)
        binding.publisher.setText(publisher)
        binding.basedon.setText(basedOn)
        binding.standard.setText(standard)
        binding.boardpreffernce.setText(boardPreffrence)
        binding.medium.setText(languageMedium)
        binding.condition.setText(condition)
        binding.bookprise.setText(bookPrise)


    }

    private fun setBookPosteData(createdAt: String) {
        // Assuming you have a timestamp as a string
        val timestampString = createdAt

        // Parse the timestamp string into a Date object
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date: Date = dateFormat.parse(timestampString)

        // Get the current date
        val currentDate = Date()

        // Format the Date object to display only the date in reverse form
//        val formattedDate = SimpleDateFormat("yyyy-MM-dd").format(date)
        val formattedDate = SimpleDateFormat("dd-MM-yyyy").format(date)


        // Display the results
        //formatted date
        //for dynamic response of time period
        dynamicBookage(createdAt, formattedDate)
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo?.isConnected == true
    }

    private fun onNetworkChanged() {
        // Check for network availability and retry if needed
        if (isNetworkAvailable()) {
            // Reset retry count and retry the request
            retryCount = 0
            makeGetRequest_FOR_BOOK_DETAIL(url)
        } else {
            showToast("Check your internet connection?")
        }
    }

    fun dynamicBookage(timeStamp: String, formatData: String) {
        // Assuming you have a timestamp as a string
        val timestampString = timeStamp

        // Parse the timestamp string into a Date object
        val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val date: Date = dateFormat.parse(timestampString)

        // Get the current date
        val currentDate = Date()

        // Calculate the difference in milliseconds between the two dates
        val differenceInMillis = currentDate.time - date.time

        // Convert the difference to days, weeks, months, and years
        val daysDifference = TimeUnit.MILLISECONDS.toDays(differenceInMillis)
        val weeksDifference = TimeUnit.MILLISECONDS.toDays(differenceInMillis) / 7
        val monthsDifference = calculateMonthsDifference(date, currentDate)
        val yearsDifference = calculateYearsDifference(date, currentDate)

        // Display the results dynamically
        var result = when {
            yearsDifference > 0 -> "$yearsDifference ${if (yearsDifference == 1) "year" else "years"}"
            monthsDifference > 0 -> "$monthsDifference ${if (monthsDifference == 1) "month" else "months"}"
            weeksDifference > 0 -> "$weeksDifference ${if (weeksDifference.toInt() == 1) "week" else "weeks"}"
            else -> "$daysDifference ${if (daysDifference.toInt() == 1) "day" else "days"}"
        }

// You can now use the 'result' variable as needed
        binding.postedon.setText("$result Ago posted On $formatData ")
    }

    fun calculateMonthsDifference(startDate: Date, endDate: Date): Int {
        val startCalendar = java.util.Calendar.getInstance()
        startCalendar.time = startDate
        val endCalendar = java.util.Calendar.getInstance()
        endCalendar.time = endDate

        val diffYear =
            endCalendar.get(java.util.Calendar.YEAR) - startCalendar.get(java.util.Calendar.YEAR)
        return diffYear * 12 + endCalendar.get(java.util.Calendar.MONTH) - startCalendar.get(java.util.Calendar.MONTH)
    }

    fun calculateYearsDifference(startDate: Date, endDate: Date): Int {
        val startCalendar = java.util.Calendar.getInstance()
        startCalendar.time = startDate
        val endCalendar = java.util.Calendar.getInstance()
        endCalendar.time = endDate

        return endCalendar.get(java.util.Calendar.YEAR) - startCalendar.get(java.util.Calendar.YEAR)
    }

    fun setAddressAndLocation(description: String, Address: String, user_id: String) {
//first setting description and address on textview
        binding.description.setText(description)
        binding.localaddresh.setText(Address)
        // Instantiate the RequestQueue.
        requestQueue = Volley.newRequestQueue(this)
        // Replace "123" with the actual book ID
        val url =
            "https://www.onlinevideodownloader.co/bookstore/api_v1/users/MY_FILEuser_detail_for_ads.php?user_id=$user_id"
        // Make a GET request with a specific book ID
        makeGetRequest_FOR_FETC_USER_DEtAIL(url)


    }

    fun setlocationdata(address: String, latitude: String, longitude: String) {
        if (binding.localaddresh.text.toString().equals(address)) {

        } else {

            if (address.equals(binding.localaddresh.text.toString())) {
                binding.localaddresh.setText(address)
            } else {
                binding.localaddresh.setText("$address, " + binding.localaddresh.text.toString())
            }




        }
        // here I am pointing on book posted user location on google map (Marker)
        updateMarkerPosition(latitude.toDouble(), longitude.toDouble())


    }

    private fun updateMarkerPosition(lantitudeMN: Double, longitudeMN: Double) {
        // Check if the marker is properly initialized
        try {
            //it's ready for loading map
            latitude = lantitudeMN
            longitude = longitudeMN
            maploader()
        } catch (e: Exception) {
            //handle error here
        }

    }

    fun setbookainTitle(bookname: String, title: String) {
        //setting data on main book heading on ads detail view below image
        binding.maintitle.setText("$bookname, $title")

    }

    fun operationcontactMethods(
        whatsappnumber: String,
        whatsapp_status: String,
        phonenumber: String,
        phoneenabled: String
    ) {
        //check which method allowed for execute MEANS which method allowed by user
        if (whatsapp_status.toString() != "" && phonenumber.toString() != "" && whatsapp_status.toString() != "" && phoneenabled != "") {
            binding.massagenow.setHint(whatsappnumber)
            binding.massagenow.isEnabled = true
            binding.callnow.setHint(phonenumber)
            binding.callnow.isEnabled = true
            Massage_STA = whatsapp_status
            Call_STA = phoneenabled


        }


    }

    private fun checkAndRequestCallPermission() {
        val permission = Manifest.permission.CALL_PHONE


        if (ContextCompat.checkSelfPermission(
                this,
                permission
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // Permission already granted, proceed with making the call
            // makePhoneCall("tel:+1234567890") // Replace with the actual phone number
            val ph = binding.callnow.hint.toString().trim()
            dialPhoneNumber(ph)
        } else {
            // Permission not granted, request it
            requestPermissionLauncher.launch(permission)
        }
    }

    fun setprofiledata(profileUr: String, name: String) {
        Picasso.get()
            .load(profileUr)
            .memoryPolicy(MemoryPolicy.NO_CACHE)
            .networkPolicy(NetworkPolicy.NO_CACHE)
            .into(binding.userprofilepic)
        binding.username.setText(name)
        makeVolleyRequest(limitBook.toString())
        // user_uploaded_books_Adapter()
    }

    private fun makeVolleyRequest(limit:String) {
        //before making request set user id and limit in object value,>>>>> there is must to update user id and also limit but it's optional , i am using for load more
        val obj= UniversalUserID_and_Limit_book
        obj.userId=user_id2nd
        obj.limit=limit
        if(user_id2nd.equals("")&&limitBook.equals("")){
            //handle impossible error
        }else {
        // Initialize RecyclerView
        binding.rec.layoutManager =
            LinearLayoutManager(this@Book_Ads_viewer_detailed, LinearLayoutManager.HORIZONTAL, false)
val bookRepository = BookRepository(this)
    bookRepository.getBooks { BookDetailsese, error ->
        if (error != null) {
            // Handle error
            Log.e("BookRepository", "Error fetching books: $error")
        } else {
            // Handle successful response
            BookDetailsese?.let { bookList ->
                val adapter = BookAdaptermetshow(object : BookAdaptermetshow.OnItemClickListener {
                    override fun onItemClicked(position: String) {
                        //set book it for loading book
                        startShimmer()
                        bookId = position
                        url =
                            "https://www.onlinevideodownloader.co/bookstore/api_v1/app/get_book_details.php?book_id=$bookId"
                        // Make a GET request with a specific book ID
                        makeSaprateRequest(url)


                        //     showToast(position.toString())
                    }
                }, bookList)
                binding.rec.adapter = adapter
                // If you need to update the data later, you can use setData
                // adapter.setData(newListOfBooks)
            }

        }
    }
    }

}

    override fun onBackPressed() {
        val params = binding.imageslide.layoutParams as ViewGroup.MarginLayoutParams

        if (isFullscreen) {
//            // If in fullscreen, set it to the initial position
//            params.height = resources.getDimensionPixelSize(com.intuit.sdp.R.dimen._300sdp)
//            params.width = ViewGroup.LayoutParams.MATCH_PARENT
            binding.imageslider.visibility=View.GONE
            binding.linearLayout8.visibility=View.VISIBLE
           // showToast("Exit full screen")
            isFullscreen = false
        }else if(major_back==2){
            // Set the exit animation
            val intent=Intent(this@Book_Ads_viewer_detailed,MainHome::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
// Finish the current activity
            finish();
        }else {
            // If not in fullscreen, set it to fullscreen

            super.onBackPressed()
        }



    }

    override fun onItemClicked(position: String) {
//do something on adapter item click
    }
//now creating a function for loading book images, title , book Age, data table, prise.........!
    fun makeSaprateRequest(addressUrl:String){ //make update book only>>>>>>>>> REQUEST
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                handlebookResponseSaprate(response)
                // Toast.makeText(this@Book_Ads_viewer_detailed,response.toString(),Toast.LENGTH_SHORT).show()
            },
            { error ->
                showToast("caled" + error.message.toString())
                if (shouldRetry(error) && retryCount < maxRetries) {
                    // Retry the request
                    retryCount++
                    showToast("caled$retryCount")
                    networkReceiver = NetworkChangeReceiver { onNetworkChanged() }
                    val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
                    registerReceiver(networkReceiver, filter)
                    //  makeGetRequest_FOR_BOOK_DETAIL(url)
                } else {
                    handleError(error)
                }
            })

        requestQueue.add(jsonObjectRequest)
    }
    private fun handlebookResponseSaprate(response: JSONObject) {
        try {


            val error = response.getBoolean("error")
            val message = response.getString("message")

            if (!error) {
                // Parse the JSON response and set values to variables
                val bookDetails = response.getJSONObject("book_details")

                val bookId = bookDetails.getInt("book_id")
                val title = bookDetails.getString("title")
                val author = bookDetails.getString("author")
                val description = bookDetails.getString("description")
                val price = bookDetails.getString("price")
                val book_condition = bookDetails.getString("book_condition")
                val user_id = bookDetails.getString("user_id")
                val photo1 = bookDetails.getString("photo1")
                val photo2 = bookDetails.getString("photo2")
                val photo3 = bookDetails.getString("photo3")
                val bookname = bookDetails.getString("name")
                val publication_year = bookDetails.getString("publication_year")
                val publisher = bookDetails.getString("publisher")
                val basedon = bookDetails.getString("based_on")
                val standard = bookDetails.getString("standard")
                val boardprefrence = bookDetails.getString("board_preference")
                val medium_languge = bookDetails.getString("medium")
                val localAddress = bookDetails.getString("local_address")
                val whatsappnumber = bookDetails.getString("whatsapp_number")
                val whatsapp_status = bookDetails.getString("whatsapp_enabled")
                val phonenumber = bookDetails.getString("phone_number")
                val phoneenabled = bookDetails.getString("phone_enabled")
                val created_at = bookDetails.getString("created_at")
                val category_id = bookDetails.getString("category_id")
                //setting
                user_id2nd=user_id
                // Now, you can use these variables as needed (e.g., update UI or perform other operations)
                // For example:
                //lod book images on slide imageview
                //setting photo url on image url verile
                photo111=photo1
                photo222=photo2
                photo333=photo3
                imaeSliderloader(photo1, photo2, photo3,binding.imageslide)
                //set book data on table
                setTbleData(
                    bookname,
                    title,
                    author,
                    publication_year,
                    publisher,
                    basedon,
                    standard,
                    boardprefrence,
                    medium_languge,
                    book_condition,
                    price
                )
             dismissShimmer()
                //set book posted data
                setBookPosteData(created_at)
                maxRetries = 5
                //set book main title
                setbookainTitle(bookname, title)
                // make usable contact and massage buttons, WhatsApp massager(for chat), contact number(for call)
                operationcontactMethods(whatsappnumber, whatsapp_status, phonenumber, phoneenabled)
runOnUiThread{
    // Scroll to the top when the layout is ready
    binding.scroolview.viewTreeObserver.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
        override fun onPreDraw(): Boolean {
            binding.scroolview.fullScroll(View.FOCUS_UP)
            binding.scroolview.viewTreeObserver.removeOnPreDrawListener(this)
            return true
        }
    })
}

            } else {
                Toast.makeText(applicationContext, "Error: $message", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(applicationContext, "Error parsing response", Toast.LENGTH_SHORT).show()
        }


    }
    fun startShimmer(){
        binding.shimmerrahuViewhero.startShimmer()
        binding.shimmerrahuViewhero.showShimmer(true)
    }
    fun dismissShimmer(){
        binding.shimmerrahuViewhero.stopShimmer()
        binding.shimmerrahuViewhero.hideShimmer()
    }
    fun extractIdFromUrl(url: String): String? {


        // Ignore the first 4 characters and start reading from index 4
        val substring = url.substring(3)
      //  println("Substring from index 4 onwards: $substring")
        return substring
    }
}




