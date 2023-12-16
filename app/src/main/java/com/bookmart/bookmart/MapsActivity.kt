package com.bookmart.bookmart

import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import android.Manifest
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.location.Address
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.Uri
import android.net.http.UrlRequest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.toolbox.Volley
import java.util.Locale
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.bookmart.bookmart.databinding.ActivityMapsBinding
import com.github.razir.progressbutton.attachTextChangeAnimator
import com.github.razir.progressbutton.bindProgressButton
import com.github.razir.progressbutton.hideProgress
import com.github.razir.progressbutton.showProgress
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.Marker
import java.io.ByteArrayOutputStream
import kotlin.io.encoding.ExperimentalEncodingApi
import android.util.Base64
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import com.bookmart.bookmart.Activites.MainHome
import com.bookmart.bookmart.Android_retrofit.API_SERVICE.API_interface.ApiService
import com.bookmart.bookmart.Android_retrofit.API_SERVICE.Models.UserResponse
import com.bookmart.bookmart.Global_Object.Temp_location
import com.bookmart.bookmart.SharedPreffrence_Manager.UserAccountManager.SharedPreferenceManager
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.FileOutputStream

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    var baseurl: String? = "https://www.onlinevideodownloader.co/"
    var email: String = ""
    var username: String? = ""
    var confirm_password: String? = ""
    var ImageURI: String = ""
    var latitude: String? = "0.0"
    var longitude: String? = "0.0"
    var latitudee: Double = 0.0
    var longitudee: Double = 0.0
    var lati: Double = 0.0;
    var longl: Double = 0.0;
    var myaddress: String? = ""
    var country: String = "null"
    var postalcode: String = "null"
    var city: String = "null"
    var adminarea: String = "null"
    var latitudefial: String = "0.00"
    var longitudefinal: String = "0.00"
    var subadminarea: String = "null"
    var sublocality: String = "null"
    var base64Image: String = ""
    private var progressDialog: ProgressDialog? = null  //progress Dialog
    private var currentMapType = GoogleMap.MAP_TYPE_NORMAL
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var geocoder: Geocoder
    private var address: List<Address>? = null
    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var marker: Marker? = null
    lateinit var mapView: MapView
    var layer_handler: Int = 0;
    var temp_location_request_id: String = "9999999"
    private lateinit var placesClient: PlacesClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        email = intent.getStringExtra("email").toString()
        username = intent.getStringExtra("confirm_password")
        confirm_password = intent.getStringExtra("username")
        ImageURI = intent.getStringExtra("imgURI").toString()
        latitude = intent.getStringExtra("latitude")
        longitude = intent.getStringExtra("longitude")
        temp_location_request_id = intent.getStringExtra("MAIN_HOME_temp_search_location_for_ads")
            .toString()   //tem location call only for ads view it will call from main home activity gor setting searching location

        if (email != null && username != null && confirm_password != null && ImageURI != null) {

            try {
                if (latitude.toString().equals("0.0")) {
                    //    Toast.makeText(this@MapsActivity, "yes", Toast.LENGTH_SHORT).show()
                } else {

                    // Toast.makeText(this@MapsActivity, "no", Toast.LENGTH_SHORT).show()

                    lati = java.lang.Double.parseDouble(latitude)
                    longl = java.lang.Double.parseDouble(longitude)
                    getFullAddress(lati, longl)

                }
            } catch (e: Exception) {
                Toast.makeText(
                    this@MapsActivity,
                    e.localizedMessage.toString(),
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else if (temp_location_request_id.equals("9170599651")) {
            binding.headtitle.setText("Choose your Ads viewing location")
            binding.radius.visibility = View.VISIBLE
            binding.radiusShow.setText(Temp_location.search_radius.toString())
            lati = java.lang.Double.parseDouble(Temp_location.latitude)
            longl = java.lang.Double.parseDouble(Temp_location.longitude)
            getFullAddress(lati, longl)


        } else {
            Toast.makeText(this@MapsActivity, "error@INTENT_VALUE not found.", Toast.LENGTH_SHORT)
                .show()
            if (latitude == null && longitude == null) {
                lati = 26.4406932
                longl = 80.5393553
            } else {
                //      Toast.makeText(this@MapsActivity, "no", Toast.LENGTH_SHORT).show()

            }
        }


//        binding.mylocation.setOnClickListener{
//            getCurrentLocation()
//        }

// Initialize the Places API client first
        // Initialize the Places API
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        // Setup a button to get location data
        val getLocationButton = findViewById<Button>(R.id.button9)
        getLocationButton.setOnClickListener {
            val markerPosition = marker?.position
            if (markerPosition != null) {
                // Handle location data here
                latitudee = markerPosition.latitude
                longitudee = markerPosition.longitude
                //val address = getAddressFromLatLng(markerPosition)
                // You can use latitude, longitude, and address as needed.
                val connectivityManager =
                    getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                val networkInfo = connectivityManager.activeNetworkInfo
                if (networkInfo != null && networkInfo.isConnected) {
                    // Make the geocoding request
                    bindProgressButton(binding.button9)
                    // (Optional) Enable fade In / Fade out animations
                    binding.button9.attachTextChangeAnimator()
                    // Show progress with "Loading" text
                    binding.button9.showProgress {
                        progressColor = Color.WHITE
                    }
                    getFullAddress(latitudee, longitudee)
                } else {
                    Toast.makeText(
                        this@MapsActivity,
                        "check internet connection?",
                        Toast.LENGTH_SHORT
                    ).show()
                }


            }

        }
        binding.layerbutton.setOnClickListener {
            if (layer_handler == 0) {
                binding.hybird.visibility = View.VISIBLE
                binding.setelite.visibility = View.VISIBLE
                binding.terrain.visibility = View.VISIBLE
                binding.normal.visibility = View.VISIBLE
                animation_For_Layers_In_left(binding.hybird)
                animation_For_Layers_In_left(binding.setelite)
                animation_For_Layers_In_left(binding.terrain)
                animation_For_Layers_In_left(binding.normal)
                layer_handler = 1
            } else if (layer_handler == 1) {
                animation_For_Layers_Out_left(binding.hybird)
                animation_For_Layers_Out_left(binding.setelite)
                animation_For_Layers_Out_left(binding.terrain)
                animation_For_Layers_Out_left(binding.normal)
//        Handler(Looper.getMainLooper()).postDelayed({
                binding.hybird.visibility = View.GONE
                binding.setelite.visibility = View.GONE
                binding.terrain.visibility = View.GONE
                binding.normal.visibility = View.GONE
//        },5000)
                layer_handler = 0
            } else {
                Toast.makeText(this@MapsActivity, "undefined command", Toast.LENGTH_SHORT).show()

            }


        }
        //radius dialog box for setting searching radius
        binding.radius.setOnClickListener {
            radiusDialogBox() // custom dialog box
        }


        binding.hybird.setOnClickListener {
            currentMapType = GoogleMap.MAP_TYPE_HYBRID
            mMap.mapType = currentMapType
            binding.maptitel.setText("Hybird map view")
        }
        binding.setelite.setOnClickListener {
            currentMapType = GoogleMap.MAP_TYPE_SATELLITE
            mMap.mapType = currentMapType
            binding.maptitel.setText("Satellite map view")
        }
        binding.terrain.setOnClickListener {
            currentMapType = GoogleMap.MAP_TYPE_TERRAIN
            mMap.mapType = currentMapType
            binding.maptitel.setText("Terrain map view")
        }
        binding.normal.setOnClickListener {
            currentMapType = GoogleMap.MAP_TYPE_NORMAL
            mMap.mapType = currentMapType
            binding.maptitel.setText("Normal map view")
        }

    }

    override fun onMapReady(googleMap: GoogleMap) {

        mMap = googleMap
        currentMapType = GoogleMap.MAP_TYPE_HYBRID
        binding.maptitel.setText("Hybird map view")
        mMap.mapType = currentMapType // Set the initial map type

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            requestLocationPermission()
            return
        }
        // mMap.isMyLocationEnabled = true
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
        mMap.isBuildingsEnabled = true
        mMap.isTrafficEnabled = true
        mMap.isMyLocationEnabled = true
        mMap.uiSettings.isMapToolbarEnabled = true
        mMap.uiSettings.isIndoorLevelPickerEnabled = true
        mMap.uiSettings.setAllGesturesEnabled(true)
        mMap.uiSettings.isScrollGesturesEnabledDuringRotateOrZoom = true
        mMap.uiSettings.isRotateGesturesEnabled = true
        mMap.uiSettings.isScrollGesturesEnabled = true
        // Set an initial marker
        val initialPosition = LatLng(lati, longl)
        val markerOptions = MarkerOptions()
            .position(initialPosition)
            .title("your pinned location")
            .draggable(true)

        marker = mMap.addMarker(markerOptions)
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialPosition, 14f))

        mMap.setOnMarkerDragListener(object : GoogleMap.OnMarkerDragListener {
            override fun onMarkerDragStart(p0: Marker) {

            }

            override fun onMarkerDrag(p0: Marker) {

            }

            override fun onMarkerDragEnd(p0: Marker) {
                //  getFullAddress(lati,longl)
            }

        })

    }

    private fun getAddressFromLatLng(latLng: LatLng): String {
        // Implement a method to convert the LatLng into a readable address
        // You can use Geocoding APIs for this purpose.
        return "Location Address"
    }

    private fun getFullAddress(latitude: Double, longitude: Double) {
        try {
            geocoder = Geocoder(this@MapsActivity, Locale.getDefault())
            address = geocoder.getFromLocation(latitude, longitude, 1)
            if (address != null) {
                myaddress = address!![0].getAddressLine(0)
                //for country
                try {
                    country = address!![0].countryName.toString()

                } catch (e: Exception) {
                    // Toast.makeText(this@MapsActivity,e.localizedMessage.toString(),Toast.LENGTH_SHORT).show()
                    country = "null"
                }
                //for postal code
                try {
                    postalcode = address!![0].postalCode.toString()
                } catch (e: Exception) {
                    //  Toast.makeText(this@MapsActivity,e.localizedMessage.toString(),Toast.LENGTH_SHORT).show()
                    postalcode = "null"
                }
                //for locality
                try {
                    city = address!![0].locality.toString()
                } catch (e: Exception) {
                    //  Toast.makeText(this@MapsActivity,e.localizedMessage.toString(),Toast.LENGTH_SHORT).show()
                    city = "null"
                }
                //for state
                try {
                    adminarea = address!![0].adminArea.toString()
                } catch (e: Exception) {
                    //   Toast.makeText(this@MapsActivity,e.localizedMessage.toString(),Toast.LENGTH_SHORT).show()
                    adminarea = "null"
                }
                //for latitude
                try {
                    latitudefial = address!![0].latitude.toString()
                } catch (e: Exception) {
                    //  Toast.makeText(this@MapsActivity,e.localizedMessage.toString(),Toast.LENGTH_SHORT).show()
                }
                //for longitude
                try {
                    longitudefinal = address!![0].longitude.toString()
                } catch (e: Exception) {
                    //  Toast.makeText(this@MapsActivity,e.localizedMessage.toString(),Toast.LENGTH_SHORT).show()
                }


                try {
                    // Create an AlertDialog
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding.button9.hideProgress("confirm location")
                    }, 1000)
                    AlertDialog.Builder(this)
                        .setTitle("Your Location")
                        .setMessage(address!![0].getAddressLine(0))
                        .setPositiveButton("confirm") { dialog, _ ->
                            // Handle OK button click
                            if (temp_location_request_id.equals("9170599651")) {
                                showProgressDialog("updating location", "") //show progress dialog
                                update_temp_search_location()

                            } else {
                                showProgressDialog(
                                    "creating your account...",
                                    "Authenticating to server."
                                ) //show progress dialog
                                prapareUserDataToSentServer()

                            }

                            dialog.dismiss()

                        }
                        .setNegativeButton("Change") { dialog, _ ->
                            // Handle Cancel button click
                            dialog.dismiss()
                        }
                        .show()


                } catch (e: Exception) {
                    Toast.makeText(this@MapsActivity, e.localizedMessage, Toast.LENGTH_SHORT).show()
                    dismissProgressDialog()
                    binding.button9.hideProgress("confirm location")

                }


            }

        } catch (e: Exception) {
            Toast.makeText(this@MapsActivity, e.localizedMessage.toString(), Toast.LENGTH_SHORT)
                .show()


        }


    }


    /**
     * //breaking the code it two parts of on code
     * just simplify
     *
     * */

    private fun animation_For_Layers_In_left(linearLayout: LinearLayout) {
        val slideAnimation =
            AnimationUtils.loadAnimation(this@MapsActivity, R.anim.slide_in_from_left)
        // Set the animation listener to handle animation events
        slideAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                // Animation has ended, perform any actions here

            }

            override fun onAnimationRepeat(animation: Animation) {}
        })

        // Apply the animation to the target view
        linearLayout.startAnimation(slideAnimation)

    }

    private fun animation_For_Layers_Out_left(linearLayout: LinearLayout) {
        val slideAnimation =
            AnimationUtils.loadAnimation(this@MapsActivity, R.anim.right_to_in_left)
        // Set the animation listener to handle animation events
        slideAnimation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}

            override fun onAnimationEnd(animation: Animation) {
                // Animation has ended, perform any actions here

            }

            override fun onAnimationRepeat(animation: Animation) {}
        })

        // Apply the animation to the target view
        linearLayout.startAnimation(slideAnimation)

    }

    private fun prapareUserDataToSentServer() {
        val requestQueue = Volley.newRequestQueue(applicationContext)
        val EMAIL: String = email.toString().trim()
        val USER_PASSWORD: String =
            username.toString()// this a i am replacing by it's own variable because it's getting user name some wne t wrong in upper program so i am using for simple solution ok
        val USER_NAME: String =
            confirm_password.toString()// this b i am replacing by it's own variable because it's getting user name some wne t wrong in upper program so i am using for simple solution ok
        val LATITUDE: String = latitudefial.toString()
        val LONGITUDE: String = longitudefinal.toString()
        val ADDRESS: String = myaddress.toString()
        val CITY: String = city.toString()
        val STATE: String = adminarea.toString()
        val COUNTRY: String = country.toString()
        val POSTAL_CODE: String = postalcode.toString()
        val PROFILE_PICTURE: String = base64Image.toString().trim()


//        Toast.makeText(
//            this@MapsActivity, USER_PASSWORD + "" +
//                    USER_NAME + "\n" +
//                    LATITUDE + "\n" +
//                    LONGITUDE + "\n" +
//                    ADDRESS + "\n" +
//                    CITY + "\n" +
//                    STATE + "\n" +
//                    COUNTRY + "\n" +
//                    POSTAL_CODE + "\n" +
//                    PROFILE_PICTURE, Toast.LENGTH_LONG
//        ).show()

        if (USER_PASSWORD != null && USER_NAME != "" && LATITUDE != null && LONGITUDE != null && ADDRESS != null && CITY != null && STATE != null && COUNTRY != null && POSTAL_CODE != null && PROFILE_PICTURE != null && EMAIL != null) {
//            Toast.makeText(
//                this@MapsActivity, "done no problem", Toast.LENGTH_SHORT
//            ).show()
            //convertImageToBase64(Uri.parse(ImageURI))

            createUser()


        } else {
            Toast.makeText(this@MapsActivity, "ERROR@null value", Toast.LENGTH_SHORT).show()
            dismissProgressDialog()
        }


    }

    override fun onResume() {
        super.onResume()
        // Check if location permissions are granted
        if (isLocationPermissionGranted()) {

        } else {
            // Request location permissions
            requestLocationPermission()
        }
    }

    private fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this@MapsActivity,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this@MapsActivity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 23
        )
    }

    // Handle permission request results in onRequestPermissionsResult
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 23) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, proceed with location access
                // getUserLocation()
            } else {
                // Permission denied, handle accordingly (e.g., show a message or disable location features)
            }
        }
    }

    @OptIn(ExperimentalEncodingApi::class)
    private fun convertImageToBase64(imageUri: Uri) {
        try {
            val inputStream = contentResolver.openInputStream(imageUri)
            if (inputStream != null) {
                val bitmap = BitmapFactory.decodeStream(inputStream)
                inputStream.close()

                // Convert the Bitmap to Base64
                val byteArrayOutputStream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
                val imageBytes = byteArrayOutputStream.toByteArray()
                base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT)

                // Now you have the Base64-encoded image
                Log.d("Base64Image", base64Image)
                // setBase64Imagee(base64Image)
                // Handle the base64Image string as needed, e.g., send it to a server or use it in your app
            } else {
                // Handle the case where the inputStream is null
                Log.e("ImageConversion", "Input stream is null")
            }
        } catch (e: Exception) {
            // Handle exceptions, such as file not found, decoding errors, or other issues
            Log.e("ImageConversion", "Error converting image to Base64: ${e.message}")
        }

    }

    fun setBase64Imagee(base64Imagee: String) {
        try {
            // Decode the Base64 string into a byte array
            val decodedBytes = Base64.decode(base64Imagee, Base64.DEFAULT)

            // Decode the byte array into a Bitmap
            val decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

            // Set the Bitmap to the ImageView

            // base64Image = decodedBitmap.toString()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun uriToFile(uri: Uri): File {
        val context = applicationContext // Replace with your context
        val inputStream = context.contentResolver.openInputStream(uri)
        val file = File(context.cacheDir, "image.jpg")
        val outputStream = FileOutputStream(file)
        inputStream.use { input ->
            outputStream.use { output ->
                input!!.copyTo(output)
            }
        }
        return file
    }

    // Function to create a user
    private fun createUser() {
        val email = email
        val password = username.toString()
            .trim() // this a i am replacing by it's own variable because it's getting user name some wne t wrong in upper program so i am using for simple solution ok
        val username = confirm_password.toString()
            .trim() // this a i am replacing by it's own variable because it's getting user name some wne t wrong in upper program so i am using for simple solution ok
        val latitude = latitudefial.toString()
        val longitude = longitudefinal.toString()
        val address = myaddress.toString()
        val city = city.toString()
        val state = adminarea.toString()
        val country = country.toString()
        val postalCode = postalcode.toString()
// Usage:
        // var imagePath: String? = null
//        Toast.makeText(this@MapsActivity, "PATH - " + ImageURI, Toast.LENGTH_SHORT).show()
        Log.d("TESTINGURI", "createUser: " + email)
        Log.d("TESTINGURI", "createUser: " + password)
        Log.d("TESTINGURI", "createUser: " + username)
        Log.d("TESTINGURI", "createUser: " + email)

        val uri = Uri.parse(ImageURI)


        val file = uriToFile(uri)
        if (file.exists()) {

            //   Toast.makeText(this@MapsActivity, "Creating..", Toast.LENGTH_SHORT).show()

// Create a Retrofit instance
            val retrofit = Retrofit.Builder()
                .baseUrl(baseurl) // Replace with the actual API base URL
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            // Create a RequestBody for the image
            val userService = retrofit.create(ApiService::class.java)


            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), file)
            val imagePart =
                MultipartBody.Part.createFormData("profile_picture", file.name, requestFile)

            val emailBody = RequestBody.create("text/plain".toMediaTypeOrNull(), email)
            val passwordBody = RequestBody.create("text/plain".toMediaTypeOrNull(), password)
            val usernameBody = RequestBody.create("text/plain".toMediaTypeOrNull(), username)
            val latBody = RequestBody.create("text/plain".toMediaTypeOrNull(), latitude.toString())
            val longBody =
                RequestBody.create("text/plain".toMediaTypeOrNull(), longitude.toString())
            val addressBody = RequestBody.create("text/plain".toMediaTypeOrNull(), address)
            val cityBody = RequestBody.create("text/plain".toMediaTypeOrNull(), city)
            val stateBody = RequestBody.create("text/plain".toMediaTypeOrNull(), state)
            val countryBody = RequestBody.create("text/plain".toMediaTypeOrNull(), country)
            val postalBody = RequestBody.create("text/plain".toMediaTypeOrNull(), postalCode)


            // Make the API request
            val call: Call<UserResponse> = userService.createUser(
                emailBody, passwordBody, usernameBody,
                latBody,
                longBody, addressBody, cityBody, stateBody, countryBody, postalBody, imagePart
            )




            call.enqueue(object : retrofit2.Callback<UserResponse> {
                override fun onResponse(
                    call: Call<UserResponse>,
                    response: retrofit2.Response<UserResponse>
                ) {
//                    Toast.makeText(this@MapsActivity, response.toString(), Toast.LENGTH_SHORT)
//                        .show()
                    if (response.isSuccessful) {
                        val userResponse: UserResponse? = response.body()

                        if (userResponse != null) {


                            if (userResponse.error) {
                                Toast.makeText(
                                    this@MapsActivity,
                                    userResponse.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                                dismissProgressDialog()
                            } else {
                                //we got response without any error
                                // Create an instance of the SharedPreferenceManager
                                val sharedPreferenceManager =
                                    SharedPreferenceManager.getInstance(this@MapsActivity)
// Save user details
                                val email: String = userResponse.user_details.Email
                                val userID: String = userResponse.user_details.ID
                                val username: String = userResponse.user_details.Username
                                val latitude: String = userResponse.user_details.Location.Latitude
                                val longitude: String = userResponse.user_details.Location.Longitude
                                val address: String = userResponse.user_details.Location.Address
                                val city: String = userResponse.user_details.Location.City
                                val state: String = userResponse.user_details.Location.State
                                val country: String = userResponse.user_details.Location.Country
                                val postalCode: String =
                                    userResponse.user_details.Location.PostalCode
                                val profileURL: String = userResponse.user_details.ProfilePicture
//                            showToast(email)
//                            showToast(userID)
//                            showToast(username)
////                            showToast(latitude)
////                            showToast(longitude)
////                            showToast(address)
////                            showToast(city)
////                            showToast(state)
////                            showToast(country)
////                            showToast(postalCode)
////                            showToast(profileURL)
                                //save user data to sharedPreference
                                if (email.toString().equals("") || userID.toString()
                                        .equals("") || username.toString()
                                        .equals("") || latitude.toString()
                                        .equals("") || longitude.toString().equals("") ||
                                    address.toString().equals("") || city.toString()
                                        .equals("") || state.toString()
                                        .equals("") || state.toString()
                                        .equals("") || country.toString()
                                        .equals("") || postalCode.toString()
                                        .equals("") || profileURL.toString().equals("")
                                ) {
//please handle here if response values null
                                    dismissProgressDialog()
                                } else {
                                    //save user data to sharedPreference
                                    try {
                                        sharedPreferenceManager.saveUserDetails(
                                            email,
                                            userID,
                                            username,
                                            latitude,
                                            longitude,
                                            address,
                                            city,
                                            state,
                                            country,
                                            postalCode,
                                            profileURL
                                        )
                                        update_temp_location()
                                        redirectOnMain()  //here we done our login operations and successfully get full userdata so now we are redirecting on MAIN HOME activty


                                    } catch (e: Exception) {
                                        showToast("@ERROR: failed to save data in SharedPreference!")
                                        dismissProgressDialog()
                                    }
                                    // now you can Retrieve user details
                                    //                       val userDetails = sharedPreferenceManager.getUserDetails()


                                }
                            }


                        } else {
                            // The response body is null, handle this as an error
                            Toast.makeText(
                                this@MapsActivity,
                                "user response null",
                                Toast.LENGTH_SHORT
                            ).show()
                            dismissProgressDialog()
                        }


                    } else {
                        // Handle the case where the response is not successful (e.g., HTTP error)


                        Toast.makeText(
                            this@MapsActivity,
                            "user response null",
                            Toast.LENGTH_SHORT
                        ).show()
                        dismissProgressDialog()

                    }

                }//https://pornkai.com/view?key=xv73903081

                override fun onFailure(call: Call<UserResponse>, t: Throwable) {
                    // Handle network or other errors
                    dismissProgressDialog()
                    showToast("APi err " + t.message)
                    Log.d("TESTINGURIFFFFF", "onFailure: " + t.message)

                }
            })
        }

    }

    private fun showProgressDialog(title: String, massage: String) {
        progressDialog = ProgressDialog(this@MapsActivity)
        progressDialog?.setTitle(title)
        progressDialog?.setMessage(massage) // Set a message
        progressDialog?.setCancelable(false) // Make it non-cancelable
        progressDialog?.show()
    }

    private fun dismissProgressDialog() {
        progressDialog?.dismiss()
    }

    fun redirectOnMain() {
        showToast("Signup successfully")
        val intent = Intent(this@MapsActivity, MainHome::class.java)
        startActivity(intent)
        finish()
    }

    fun update_temp_search_location() {  //method for updating temp searching location in temp location object
        try {
            val LATITUDE: String = latitudefial.toString()
            val LONGITUDE: String = longitudefinal.toString()
            val ADDRESS: String = myaddress.toString()
            val CITY: String = city.toString()
            val STATE: String = adminarea.toString()
            val COUNTRY: String = country.toString()
            val POSTAL_CODE: String = postalcode.toString()

            var temp_obj = Temp_location
            temp_obj.latitude = LATITUDE
            temp_obj.longitude = LONGITUDE
            temp_obj.address = ADDRESS
            temp_obj.city = CITY
            temp_obj.state = STATE
            temp_obj.country = COUNTRY
            temp_obj.postalCode = POSTAL_CODE
            //now location is updated .....Write code to update ads according to address.,,,,,,,,
            // Create an Intent with a custom action
            val intent = Intent("LOCATION_UPDATED_ACTION")
            // Pass location data as extras
            // Send the broadcast
            sendBroadcast(intent)
            Toast.makeText(this@MapsActivity, "location updated for searching", Toast.LENGTH_SHORT)
                .show()
finish()
            Log.d(
                "address_temp", temp_obj.latitude + "" + temp_obj.longitude + "" +
                        temp_obj.address + "" +
                        temp_obj.city + "" +
                        temp_obj.state + "" +
                        temp_obj.country + "" +
                        temp_obj.postalCode
            )
            dismissProgressDialog()
        } catch (e: Exception) {
            Log.d("address_temp_error", e.localizedMessage)
        }


    }

    fun radiusDialogBox() {
        val minus: Button
        val plus: Button
        val meter: TextView
        val apply: TextView

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.set_search_radius_layout)
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.setCancelable(false)
        dialog.window?.attributes?.windowAnimations = R.anim.fade_out

        minus = dialog.findViewById(R.id.minus)
        plus = dialog.findViewById(R.id.plus)
        apply = dialog.findViewById(R.id.apply)
        meter = dialog.findViewById(R.id.meter)
        meter.setText(binding.radiusShow.text.toString())
        minus.setOnClickListener {
            // add on increment
            var int_value = convertStringToInt(meter.text.toString())
            if (int_value != null) {
                var final_value = subtractOne(int_value)
                meter.setText(final_value.toString())

            }
        }

        plus.setOnClickListener {
            // add on increment
            var int_value = convertStringToInt(meter.text.toString())
            if (int_value != null) {
                var final_value = addOne(int_value)
                meter.setText(final_value.toString())

            }


        }
        apply.setOnClickListener {
            //here i am saving value in a temp_location object
            var temp_locatio_obj = Temp_location
            temp_locatio_obj.search_radius = meter.text.toString()
            binding.radiusShow.setText(temp_locatio_obj.search_radius)   //updating UI on map activity Search Radius 1km
            dialog.dismiss()

        }
        meter.setOnClickListener {
            Toast.makeText(this@MapsActivity, "Cancel clicked", Toast.LENGTH_SHORT).show()
            //minus on decrement
        }

        dialog.show()

    }

    fun addOne(input: Int): Int {
        return input + 1

    }

    fun subtractOne(input: Int): Int {
        return if (input > 1) {
            input - 1
        } else {
            // Handle the case when input is 1 (or less) differently, e.g., return the input itself
            input
        }
    }

    fun convertStringToInt(input: String): Int? {
        return try {
            input.toInt()
        } catch (e: NumberFormatException) {
            // Handle the case where the input is not a valid integer
            null
        }
    }

    fun update_temp_location() {
        try {
            //instance of sha_pre....enc
            val sharedPreferenceManager = SharedPreferenceManager.getInstance(this@MapsActivity)
            val userDetails =
                sharedPreferenceManager.getUserDetails()   //access your database basic details  email, userID, password, username, latitude, longitude, address, city, state, country, postalCode, profileURL
            // setting these retrieved data in temp_location_object
            var temp_Loction_Obj = Temp_location //object of temp_location_object
            //setting location and other data
            temp_Loction_Obj.city = userDetails.city
            temp_Loction_Obj.state = userDetails.state

        } catch (e: Exception) {
            //handle error

        }

    }

}
