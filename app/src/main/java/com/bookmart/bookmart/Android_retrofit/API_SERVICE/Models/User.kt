data class User(
    val email: String,
    val password: String,
    val username: String,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val city: String,
    val state: String,
    val country: String,
    val postal_code:String
)
data class UserDetails(
    val id:String,
    val Email: String,
    val Username: String,
    val ProfilePicture: String,
    val Location: LocationDetails
)

data class LocationDetails(
    val Latitude: String,
    val Longitude: String,
    val Address: String,
    val City: String,
    val State: String,
    val Country: String,
    val PostalCode:String
)
//
//your main activity code will be like import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//import okhttp3.MediaType
//import okhttp3.RequestBody
//import okhttp3.RequestBody.Companion.asRequestBody
//import java.io.File
//
//class MainActivity : AppCompatActivity() {
//    private val apiService: ApiService
//
//    init {
//        val retrofit = Retrofit.Builder()
//            .baseUrl("http://your-base-url.com/") // Replace with your API base URL
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//
//        apiService = retrofit.create(ApiService::class.java)
//    }
//
//    // Function to create a user
//    private fun createUser() {
//        val email = "user@example.com"
//        val password = "password"
//        val username = "Username"
//        val latitude = 0.0
//        val longitude = 0.0
//        val address = "123 Street"
//        val city = "City"
//        val state = "State"
//        val country = "Country"
//        val postalCode = "12345"
//
//        // Create a User object
//        val user = User(email, password, username, latitude, longitude, address, city, state, country, postalCode)
//
//        // Create a File object for the image (change the path to your image)
//        val imageFile = File("path_to_your_image.jpg")
//
//        // Create a RequestBody for the image
//        val imageRequestBody = RequestBody.create(MediaType.parse("image/*"), imageFile)
//        val imagePart = MultipartBody.Part.createFormData("profile_picture", imageFile.name, imageRequestBody)
//
//        val call = apiService.createUser(
//            user.email, user.password, user.username,
//            user.latitude, user.longitude, user.address, user.city, user.state, user.country, user.postal_code,
//            imagePart
//        )
//
//        call.enqueue(object : Callback<User> {
//            override fun onResponse(call: Call<User>, response: Response<User>) {
//                if (response.isSuccessful) {
//                    val user = response.body()
//                    // User created successfully, handle the response here
//                } else {
//                    // Handle API error
//                }
//            }
//
//            override fun onFailure(call: Call<User>, t: Throwable) {
//                // Handle network error
//            }
//        })
//    }
//}