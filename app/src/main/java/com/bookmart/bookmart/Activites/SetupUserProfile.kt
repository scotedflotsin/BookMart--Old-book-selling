package com.bookmart.bookmart.Activites

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Geocoder
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.bookmart.bookmart.databinding.ActivitySetupUserProfileBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import java.io.ByteArrayOutputStream
import android.util.Base64
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bookmart.bookmart.MapsActivity
import com.bookmart.bookmart.R
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.LocationSettingsResponse
import com.google.android.gms.location.LocationSettingsStatusCodes
import com.google.android.gms.tasks.Task
import render.animations.Attention
import render.animations.Render
import java.util.Locale

class SetupUserProfile : AppCompatActivity() {
    val binding by lazy {
        ActivitySetupUserProfileBinding.inflate(layoutInflater)
    }
    private lateinit var locationCallback: LocationCallback
    private val mainHandler = Handler(Looper.getMainLooper())
    public var latitude: Double? = null;
    public var longitude: Double? = null;
    var locatio_STA:Int=1
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    var checkbox: Int = 0
    var check_IMG_status: Int = 10 // checking image updated or not updated by user
    private val locationRequest = LocationRequest.create().apply {
        priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        interval = 10000 // Update interval in milliseconds
        fastestInterval = 5000 // Fastest update interval in milliseconds
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
// Make sure locationCallback is the same instance used when requesting updates.


        // Check if location permissions are granted
        if (isLocationPermissionGranted()) {
          //  getUserLocation()
           promptUserToEnableLocation()


        } else {
            // Request location permissions
            requestLocationPermission()
        }

        binding.checkBoxprivacypolicy.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                checkbox = 2
                //  Toast.makeText(this@SetupUserProfile,"done",Toast.LENGTH_SHORT).show()
            } else {
                //  Toast.makeText(this@SetupUserProfile,"not done",Toast.LENGTH_SHORT).show()
                checkbox = 3
            }
        }
        val receivedData = intent.getStringExtra("userEmail")
        if (receivedData != null) {
            // Do something with the received data
            // For example, display it in a TextView
            binding.veriemail.setText(receivedData.toString().trim())
        } else {
            binding.veriemail.setText("harshvermadr30@gmail.com")
        }
        binding.veriemail.isEnabled = false
        binding.imagepicker.setOnClickListener {
            maicx()
        }
        binding.submitbutton.setOnClickListener {

            if (check_IMG_status == 123) {

            } else {
                val render = Render(this@SetupUserProfile)
                //for positive massage
                render.setAnimation(Attention().Shake(binding.imagepicker))
                render.start()
            }
            if (binding.username.text.toString().equals("")) {
                binding.username.setError("user name requird")
                binding.passwordone.setError("password required")
                binding.conpassword.setError("confirm password")
            } else if (binding.username.text.toString() != null) {
                if (binding.passwordone.text.toString()
                        .equals("") || binding.conpassword.text.toString().equals("")
                ) {
//                    animatio(binding.passwordone)
//                    animatio(binding.conpassword)
                    binding.passwordone.setError("password")
                    binding.conpassword.setError("confirm password")
                } else if (binding.passwordone.text.toString() != null || binding.conpassword.text.toString() != null) {
                    var pass_len: Int = binding.passwordone.length()
                    var con_pass_len: Int = binding.conpassword.length()
                    if (pass_len < 8) {
                        binding.passwordone.setError("length too short")
                    } else if (binding.passwordone.text.toString()
                            .equals(binding.conpassword.text.toString())
                    ) {
                        redirectOnlocation()
                    } else if (binding.passwordone.text.toString() != binding.conpassword.text.toString()) {
                        binding.conpassword.setError("password does not match")
                    }


                }

            }
        }


    }

    fun maicx() {

        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, 123)


    }
//        ImagePicker.with(this)
//            .crop()                    //Crop image(Optional), Check Customization for more option
//            .compress(100)            //Final image size will be less than 1 MB(Optional)
//            .maxResultSize(
//                512,
//                512
//            )    //Final image resolution will be less than 1080 x 1080(Optional)
//            .start()

    fun getRealPathFromUri(context: Context, uri: Uri): String? {
        var realPath: String? = null
        val projection = arrayOf(MediaStore.Images.Media.DATA)

        context.contentResolver.query(uri, projection, null, null, null)?.use { cursor ->
            if (cursor.moveToFirst()) {
                val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                realPath = cursor.getString(column_index)
            }
        }

        return realPath
    }


    private var imagePath: String? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (requestCode == 123 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                // Handle the selected image URI
                val selectedImageUri: Uri = data.data!!

                // Now, you can use the selectedImageUri for further processing
//                imagePath = getRealPathFromUri(this, selectedImageUri)
                imagePath = selectedImageUri.toString()
                // ... (upload the image using Retrofit or perform other actions)
                check_IMG_status = 123
                binding.profileImage.setImageURI(selectedImageUri)


            }

        }
//        if (resultCode == Activity.RESULT_OK) {
//            //Image Uri will not be null for RESULT_OK
//            val uri: Uri = data?.data!!
//            if (uri.toString().trim() != null) {
//                // Use Uri object instead of File to avoid storage permissions
//                //    binding.profileImage.setImageURI(uri)
//                binding.profileImage.setImageURI(uri)
//                check_IMG_status = 123
//            } else {
//                Toast.makeText(this@SetupUserProfile, "something went wrong", Toast.LENGTH_SHORT)
//                    .show()
//            }
//        } else if (resultCode == ImagePicker.RESULT_ERROR) {
//            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
//        } else {
//            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
//        }
    }

//    @OptIn(ExperimentalEncodingApi::class)
//    private fun convertImageToBase64(imageUri: Uri) {
//        val inputStream = contentResolver.openInputStream(imageUri)
//        val bitmap = BitmapFactory.decodeStream(inputStream)
//        inputStream?.close()
//
//        // Convert the Bitmap to Base64
//        val byteArrayOutputStream = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
//        val imageBytes = byteArrayOutputStream.toByteArray()
//        var base64Image = Base64.encodeToString(imageBytes, Base64.DEFAULT)
//        // Now you have the Base64-encoded image
//        Log.d("Base64Image", base64Image)
////Toast.makeText(this@SetupUserProfile,base64Image.toString(),Toast.LENGTH_SHORT).show()
//        setBase64Image(base64Image.toString(), binding.profileImage)
//    }

//    fun setBase64Image(base64Image: String, imageView: ImageView) {
//        try {
//            // Decode the Base64 string into a byte array
//            val decodedBytes = Base64.decode(base64Image, Base64.DEFAULT)
//
//            // Decode the byte array into a Bitmap
//            val decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
//
//            // Set the Bitmap to the ImageView
//            imageView.setImageBitmap(decodedBitmap)
//            binding.imagepicker.setText("Change profile photo")
//            check_IMG_status = 123
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }


    fun redirectOnlocation() {
        if (checkbox == 0 || checkbox == 3) {
            // Toast.makeText(this@SetupUserProfile,"failded",Toast.LENGTH_SHORT).show()
            val render = Render(this@SetupUserProfile)
            //for positive massage
            render.setAnimation(Attention().Shake(binding.checkBoxprivacypolicy))
            render.start()
            Toast.makeText(this@SetupUserProfile, checkbox.toString(), Toast.LENGTH_SHORT)
                .show()
        } else if (checkbox == 2) {



            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra("email", binding.veriemail.text.toString().trim())
            intent.putExtra("confirm_password", binding.conpassword.text.toString().trim())
            intent.putExtra("username", binding.username.text.toString().trim())
            intent.putExtra("imgURI", imagePath)
            intent.putExtra("latitude", latitude.toString())
            intent.putExtra("longitude", longitude.toString())
            if (check_IMG_status == 123) {
                try {

if(locatio_STA==1){
    //location under processing...
    showToast("before location button clicked value 4 it's automatically")
    locatio_STA=4
    binding.progressbar.visibility= View.VISIBLE

}else if(locatio_STA==2){
    //error found location is being null.
    showToast("failed to get location 2 ")
    // location collected move..
    binding.progressbar.visibility=View.VISIBLE
    mainHandler.postDelayed({
        // Code to be run after the delay
        stopLocationUpdates()
        startActivity(intent)
        binding.progressbar.visibility=View.GONE
    }, 2000)


}else if(locatio_STA==3){
   // location collected move..
    showToast("success to get location 3 every thing done")
    binding.progressbar.visibility=View.VISIBLE
                    mainHandler.postDelayed({
                        // Code to be run after the delay
                        binding.progressbar.visibility=View.GONE
                        startActivity(intent)
                    }, 100)


}






                } catch (e: Exception) {
                    Toast.makeText(
                        this@SetupUserProfile,
                        "check - " + e.localizedMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                val render = Render(this@SetupUserProfile)
                //for positive massage
                render.setAnimation(Attention().Shake(binding.imagepicker))
                render.start()
            }


        }

    }
    fun redirectOnlocation_no_location_restriction() {
        if (checkbox == 0 || checkbox == 3) {
            // Toast.makeText(this@SetupUserProfile,"failded",Toast.LENGTH_SHORT).show()
            val render = Render(this@SetupUserProfile)
            //for positive massage
            render.setAnimation(Attention().Shake(binding.checkBoxprivacypolicy))
            render.start()
            Toast.makeText(this@SetupUserProfile, checkbox.toString(), Toast.LENGTH_SHORT)
                .show()
        } else if (checkbox == 2) {



            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra("email", binding.veriemail.text.toString().trim())
            intent.putExtra("confirm_password", binding.conpassword.text.toString().trim())
            intent.putExtra("username", binding.username.text.toString().trim())
            intent.putExtra("imgURI", imagePath)
            intent.putExtra("latitude", latitude.toString())
            intent.putExtra("longitude", longitude.toString())
            if (check_IMG_status == 123) {
                try {

                        // location collected move..


                        mainHandler.postDelayed({
                            // Code to be run after the delay
                            stopLocationUpdates()
                            startActivity(intent)
                            binding.progressbar.visibility=View.GONE

                        }, 100)


                } catch (e: Exception) {
                    Toast.makeText(
                        this@SetupUserProfile,
                        "check - " + e.localizedMessage,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                val render = Render(this@SetupUserProfile)
                //for positive massage
                render.setAnimation(Attention().Shake(binding.imagepicker))
                render.start()
            }


        }

    }
    private fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this@SetupUserProfile,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(
            this@SetupUserProfile, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 23
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
                Toast.makeText(
                    this@SetupUserProfile,
                    "without permission we can not proceed?",
                    Toast.LENGTH_SHORT
                ).show()
                // Request location permissions
                AlertDialog.Builder(this@SetupUserProfile)
                    .setTitle("permission request")
                    .setMessage("location permission required for create account?")
                    .setIcon(R.drawable.baseline_error_24)
                    .setPositiveButton("Allow permission") { dialog, _ ->
                        // Handle OK button click
                     requestLocationPermission()
                        dialog.dismiss()
                    }
                    .setNegativeButton("close app") { dialog, _ ->
                        // Handle Cancel button click
                        finish()
                        dialog.dismiss()
                    }
                    .show()
            }
        }
    }
//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>Deprecated method
//    fun getUserLocation() {
//        if (ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_FINE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
//                this,
//                Manifest.permission.ACCESS_COARSE_LOCATION
//            ) != PackageManager.PERMISSION_GRANTED
//        ) {
//
//            return
//        }
//        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
//            if (location != null) {
//
//                latitude = location.latitude
//                longitude = location.longitude
//                Toast.makeText(
//                    this@SetupUserProfile,
//                    latitude.toString() + "\n" + longitude.toString(),
//                    Toast.LENGTH_SHORT
//                ).show()
//
//            }
//
//
//        }
//
//}


    override fun onResume() {
        super.onResume()


    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun promptUserToEnableLocation() {
        val locationRequestBuilder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val settingsClient = LocationServices.getSettingsClient(this)
        val task: Task<LocationSettingsResponse> =
            settingsClient.checkLocationSettings(locationRequestBuilder.build())

        task.addOnFailureListener { exception ->
            if (exception is ApiException) {
                when (exception.statusCode) {
                    LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                        // Location settings are not satisfied. Show the user a dialog to upgrade location settings
                        try {
                            val resolvable = exception as ResolvableApiException
                            resolvable.startResolutionForResult(
                                this@SetupUserProfile,
                                LOCATION_SETTINGS_REQUEST_CODE
                            )
                        } catch (e: IntentSender.SendIntentException) {
                            // Ignore the error.
                        }
                    }

                    LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                        // Location settings are not satisfied. However, we have no way to fix the settings so we won't show the dialog.
                        // Handle this case as needed.
                    }
                }
            }
        }
        task.addOnSuccessListener {
            requestLocationUpdates()
        }
    }


    companion object {
        private const val LOCATION_SETTINGS_REQUEST_CODE = 1001
    }

    private fun requestLocationUpdates() {
        val locationRequest = LocationRequest.create().apply {
            interval = 10000 // Update interval in milliseconds
            fastestInterval = 5000 // Fastest update interval in milliseconds
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                // Handle location updates
                val location = locationResult.lastLocation
                if (location != null) {
                    if(locatio_STA==4){
//showToast("4 before button clicked")
                        latitude = location.latitude
                        longitude = location.longitude
                        locatio_STA=3
                        redirectOnlocation_no_location_restriction()  //move on next activity without any restriction ALl data OK.......

                    }else{
                        latitude = location.latitude
                        longitude = location.longitude
                        locatio_STA=3
//                        showToast("3 value updated before clicked")
                    }




                }else{
                    locatio_STA=2
                    redirectOnlocation()
                    Toast.makeText(this@SetupUserProfile,"Failed to fetch location",Toast.LENGTH_SHORT).show()
                }
            }
        }

        // Request location updates
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null /* Looper */
        )
    }
    fun showToast(massage:String){
        Toast.makeText(this@SetupUserProfile,massage,Toast.LENGTH_SHORT).show()
    }
    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
}




