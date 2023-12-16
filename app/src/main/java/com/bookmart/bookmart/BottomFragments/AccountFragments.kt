import android.app.Activity
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.bookmart.bookmart.Activites.Account_UI_all_in_one
import com.bookmart.bookmart.Activites.LoginScreen
import com.bookmart.bookmart.UserUploadedBooks.Posted_ads_view_list
import com.bookmart.bookmart.Android_retrofit.API_SERVICE.API_interface.ApiService
import com.bookmart.bookmart.Android_retrofit.API_SERVICE.Models.ApiClient_OperationResponse
import com.bookmart.bookmart.R
import com.bookmart.bookmart.SharedPreffrence_Manager.UserAccountManager.SharedPreferenceManager
import com.bookmart.bookmart.SharedPreffrence_Manager.UserAccountManager.SharedPreferencesHelper_for_UP_u_name
import com.bookmart.bookmart.databinding.FragmentAccountFragmentsBinding
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class AccountFragments : Fragment(R.layout.fragment_account_fragments) {

    val binding by lazy {
        FragmentAccountFragmentsBinding.inflate(layoutInflater)

    }
    var profile_arrow_status: Boolean = false
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return binding.root

    }
    var proUrl:String=""
    private var progressDialog: ProgressDialog? = null  //progress Dialog
    var check_IMG_status:Int=0
    var imagePath:String=""
    val baseurl:String="https://www.onlinevideodownloader.co/"   //setting API baseURl of server address
var userId:String=""  //use// rID
    var userOld_url_profile:String=""
    var usernamesta:String=""
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.runOnUiThread {
           // UI operations go here

            // Perform other UI operations
        }

        //loading user profile
        loadDataforProfile(binding.profileimage,binding.username)
        binding.dropdownprofile.setOnClickListener {

            if (profile_arrow_status) {
                binding.proflily.visibility = View.GONE
                binding.savechanges.visibility = View.GONE
                binding.verimail.visibility = View.GONE
                binding.dropdownprofile.setImageResource(R.drawable.baseline_keyboard_arrow_right_24)
                profile_arrow_status = false
            } else {


// To replace the drawable icon on the left:
                binding.dropdownprofile.setImageResource(R.drawable.baseline_keyboard_arrow_down_24)
                //loading user data on edit layout
                loadDataforProfile(binding.editimageprofile,binding.editusername)
                //view=visible
                binding.proflily.visibility = View.VISIBLE
                binding.savechanges.visibility = View.VISIBLE
                binding.verimail.visibility = View.VISIBLE
                profile_arrow_status = true
            }


        }
        binding.helpandsupport.setOnClickListener {
            val intent = Intent(activity, Account_UI_all_in_one::class.java)
            intent.putExtra("helpandsupportIDEN", "feedback")
            startActivity(intent)

        }
        binding.userdataandprivacy.setOnClickListener {
            val intent = Intent(activity, Account_UI_all_in_one::class.java)
            intent.putExtra("helpandsupportIDEN", "userdataandprivacy")
            startActivity(intent)

        }
        binding.aboutus.setOnClickListener {
            val intent = Intent(activity, Account_UI_all_in_one::class.java)
            intent.putExtra("helpandsupportIDEN", "aboutus")
            startActivity(intent)

        }
        binding.postedbook.setOnClickListener {
            val intent = Intent(activity, Posted_ads_view_list::class.java)
            startActivity(intent)
        }
        binding.editimageprofile.setOnClickListener{
            ImagePicker()
        }
        binding.logout.setOnClickListener{
            try {
                // Create an AlertDialog
                Handler(Looper.getMainLooper()).postDelayed({
                }, 1000)
                AlertDialog.Builder(requireContext())
                    .setTitle("Sure want to logout!")
                    .setMessage("we will meet again soon").setIcon(R.drawable.baseline_error_24)
                    .setPositiveButton("Logout") { dialog, _ ->
                        // Handle OK button click
                        deleteSharedPreferences(requireContext())
                        dialog.dismiss()
                    }
                    .setNegativeButton("cancel") { dialog, _ ->
                        // Handle Cancel button click
                        dialog.dismiss()
                    }
                    .show()


            } catch (e: Exception) {
                Toast.makeText(activity, e.localizedMessage, Toast.LENGTH_SHORT).show()
            }
        }

        binding.savechanges.setOnClickListener{

            if(check_IMG_status==123){
                showProgressDialog()
                updateProfile()
            }
            if(usernamesta.toString()!=binding.editusername.text.toString()){
                showProgressDialog()
                updateUserName()
               // Toast.makeText(activity, "username", Toast.LENGTH_SHORT).show()

            }
            if(check_IMG_status!=123&&usernamesta.toString()==binding.editusername.text.toString()){
                Toast.makeText(activity,"No changes found!",Toast.LENGTH_SHORT).show()
        }
        }
    }

    fun loadDataforProfile(profile: ImageView,username: TextView) {
        try {
            binding.shimmerViewForUserProfile.startShimmer()
            binding.shimmerViewForUsername.startShimmer()
            val sharedPreferenceManager = SharedPreferenceManager.getInstance(requireContext())
            val userDetails = sharedPreferenceManager.getUserDetails()
            //setting userId
            userId=userDetails.userID.toString()
            //getting user profile url
           usernamesta =userDetails.username.toString()
            userOld_url_profile=userDetails.profileURL.toString()
            Log.d("profile url xmxm", userDetails.profileURL.toString())
            Log.d("profile url xmxm", userDetails.profileURL.toString())
            //loading image on profile pick
            // Load and display the image using Picasso
          proUrl=userDetails.profileURL
            LoadImageTask(profile).execute(userDetails.profileURL)
            //loading name for name view
            username.setText(userDetails.username.toString())
            //setting email on text view
            binding.verimail.setText(userDetails.email.toString())

        } catch (e: Exception) {
            Toast.makeText(activity, e.localizedMessage, Toast.LENGTH_SHORT).show()

        }
        binding.shimmerViewForUserProfile.stopShimmer()
        binding.shimmerViewForUsername.stopShimmer()
    }

    fun ImagePicker() {
try{
    val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    startActivityForResult(intent, 123)
}catch (e:Exception){
    Toast.makeText(activity,e.localizedMessage,Toast.LENGTH_SHORT).show()
}



    }
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
                binding.editimageprofile.setImageURI(selectedImageUri)
                Log.d("profile url xmxm",imagePath)

            }

        }

    }
    fun updateUserName(){
        binding.shimmerViewForUsername.startShimmer()
        val UseID=userId
        val useranem_up=binding.editusername.text.toString().trim()
        val retrofit = Retrofit.Builder()
            .baseUrl(baseurl) // Replace with the actual API base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        // Create a RequestBody for the image
        val userService = retrofit.create(ApiService::class.java)



        val userId = RequestBody.create("text/plain".toMediaTypeOrNull(), UseID)
        val user_field = RequestBody.create("text/plain".toMediaTypeOrNull(),"Username" )
        val New_valuse = RequestBody.create("text/plain".toMediaTypeOrNull(), useranem_up)

        // Make the API request
        val call_for_username: Call<ApiClient_OperationResponse> = userService.update_user_data(userId,user_field,New_valuse)

        call_for_username.enqueue(object : retrofit2.Callback<ApiClient_OperationResponse> {
            override fun onResponse(
                call: Call<ApiClient_OperationResponse>,
                response: retrofit2.Response<ApiClient_OperationResponse>
            ) {
                if(response.isSuccessful){
                    val userResponse: ApiClient_OperationResponse? = response.body()
                    if (userResponse != null) {
if(userResponse.error){
    Toast.makeText(activity,"saved",Toast.LENGTH_SHORT).show()

}else{
  //task is successfully done and completed
    // Load the current username from SharedPreferences
        // Update the username in SharedPreferences
    try{
        SharedPreferencesHelper_for_UP_u_name.saveUsername(requireContext(), useranem_up)
        val sharedPreferenceManager = SharedPreferenceManager.getInstance(requireContext())
        val currentUsername = SharedPreferencesHelper_for_UP_u_name.getUsername(requireContext())
        usernamesta=currentUsername.toString()
        dismissProgressDialog()
        binding.username.setText(usernamesta)
        binding.proflily.visibility = View.GONE
        binding.savechanges.visibility = View.GONE
        binding.verimail.visibility = View.GONE
        binding.dropdownprofile.setImageResource(R.drawable.baseline_keyboard_arrow_right_24)
        Toast.makeText(activity,"Task successfully",Toast.LENGTH_SHORT).show()
        profile_arrow_status = false
        binding.shimmerViewForUsername.stopShimmer()
    }catch (e:Exception){
        Toast.makeText(activity,"error",Toast.LENGTH_SHORT).show()
        binding.shimmerViewForUsername.stopShimmer()
        dismissProgressDialog()
    }


}


}

                }else{
                    Toast.makeText(activity,"wrong",Toast.LENGTH_SHORT).show()
                    binding.shimmerViewForUsername.stopShimmer()
                    dismissProgressDialog()

                }

            }

            override fun onFailure(call: Call<ApiClient_OperationResponse>, t: Throwable) {
                Toast.makeText(activity,t.message.toString(),Toast.LENGTH_SHORT).show()
                dismissProgressDialog()
                binding.shimmerViewForUsername.stopShimmer()

            }
        })
    }
    fun updateProfile(){
        binding.shimmerViewForUserProfile.stopShimmer()
        val UseID=userId
        val useranem_up=binding.editusername.text.toString().trim()
        val userOld_image_url=userOld_url_profile
        val user_profileImage=imagePath //initialise image uri to this variable
        val uri = Uri.parse(user_profileImage)

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
                MultipartBody.Part.createFormData("new_profile_picture", file.name, requestFile)
            val userId = RequestBody.create("text/plain".toMediaTypeOrNull(), UseID)
            val profile_field = RequestBody.create("text/plain".toMediaTypeOrNull(), "ProfilePicture")
            val old_image_url = RequestBody.create("text/plain".toMediaTypeOrNull(), userOld_image_url)

            // Make the API request
            val call_for_profile: Call<ApiClient_OperationResponse> = userService.update_profile_image(userId,profile_field,imagePart,old_image_url)
            call_for_profile.enqueue(object : retrofit2.Callback<ApiClient_OperationResponse> {
                override fun onResponse(
                    call: Call<ApiClient_OperationResponse>,
                    response: retrofit2.Response<ApiClient_OperationResponse>
                ) {
                    if(response.isSuccessful){
                        val userResponse: ApiClient_OperationResponse? = response.body()
                        if (userResponse != null) {

                            if(userResponse.error){
                                dismissProgressDialog()
                                binding.shimmerViewForUserProfile.stopShimmer()
                            }else{
                                Toast.makeText(activity,"Task successfully",Toast.LENGTH_SHORT).show()
                                check_IMG_status=0
                                LoadImageTask(binding.profileimage).execute(proUrl.toString())
                                binding.shimmerViewForUserProfile.stopShimmer()
                                Toast.makeText(activity,proUrl.toString(),Toast.LENGTH_SHORT).show()
                                if(usernamesta.toString()!=binding.editusername.text.toString()){
                                    updateUserName()
                            }
                                dismissProgressDialog()



                            }
                        }





                    }else{
                        Toast.makeText(activity,"wrong",Toast.LENGTH_SHORT).show()
                        binding.shimmerViewForUserProfile.stopShimmer()
                        dismissProgressDialog()

                    }




                }

                override fun onFailure(call: Call<ApiClient_OperationResponse>, t: Throwable) {
                    Toast.makeText(activity,t.message.toString(),Toast.LENGTH_SHORT).show()
                    dismissProgressDialog()
                    binding.shimmerViewForUserProfile.stopShimmer()

                }
            })
        }
    }
    fun uriToFile(uri: Uri): File {
        val context = requireContext() // Replace with your context
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
    private inner class LoadImageTask(private val imageView: ImageView) :
        AsyncTask<String, Void, Bitmap>() {

        override fun doInBackground(vararg params: String): Bitmap? {
            val imageUrl = params[0]

            try {
                val url = URL(imageUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.connect()

                val input: InputStream = connection.inputStream
                return BitmapFactory.decodeStream(input)
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return null
        }

        override fun onPostExecute(result: Bitmap?) {
            if (result != null) {
                imageView.setImageBitmap(result)
            } else {
                // Handle the case where the image couldn't be loaded
            }
        }
    }

    private fun showProgressDialog() {
        progressDialog = ProgressDialog(activity)
        progressDialog?.setTitle("Updating profile...")
        progressDialog?.setMessage("Authenticating to server") // Set a message
        progressDialog?.setCancelable(false) // Make it non-cancelable
        progressDialog?.show()
    }
    fun deleteSharedPreferences(context: Context) {
        val sharedPreferences = context.getSharedPreferences("MyAppPreferences", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
        val intent=Intent(activity,LoginScreen::class.java)
        startActivity(intent)
  requireActivity().finish()
    }

    private fun dismissProgressDialog() {
        progressDialog?.dismiss()
    }
    }

