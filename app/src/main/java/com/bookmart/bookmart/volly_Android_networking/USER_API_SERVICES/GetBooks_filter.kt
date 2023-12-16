package com.bookmart.bookmart.volly_Android_networking.USER_API_SERVICES


import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bookmart.bookmart.Global_Object.BookFilter
import com.bookmart.bookmart.Global_Object.UniversalUserID_and_Limit_book
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

data class BookDetailseseIN(
    val book_id: String,
    val title: String,
    val author: String,
    val description: String,
    val price: String,
    val book_condition: String,
    val user_id: String,
    val photo1: String,
    val photo2: String,
    val photo3: String,
    val name: String,
    val publication_year: String,
    val publisher: String,
    val based_on: String,
    val standard: String,
    val board_preference: String,
    val medium: String,
    val local_address: String,
    val whatsapp_number: String,
    val whatsapp_enabled: String,
    val phone_number: String,
    val phone_enabled: String,
    val created_at: String,
    val category_id: String,
    val user_details: UserDetails
)

data class UserDetails(
    val ID: String,
    val Email: String,
    val Username: String,
    val ProfilePicture: String,
    val Location: Location
)

data class Location(
    val Latitude: String,
    val Longitude: String,
    val Address: String,
    val City: String,
    val State: String,
    val Country: String,
    val PostalCode: String
)
//
//class BookRepository_filter(private val context: Context) {
//    private val requestQueue: RequestQueue by lazy {
//        Volley.newRequestQueue(context)
//    }
//
//    fun getBooks(callback: (List<BookDetailseseIN>?,String?) -> Unit) {
//        val obj= BookFilter
//        var page_ob=obj.page_ob
//        var limit_ob=obj.limit_ob
//        var city_ob=obj.city_ob
//        var state_ob=obj.state_ob
//        var cate_id_ob=obj.cate_id_ob
//        var search_ob=obj.search_ob
//        var min_prise_ob=obj.min_prise_ob
//        var max_prise_ob=obj.max_prise_ob
//        var title_ob=obj.title_ob
//        var author_ob=obj.author_ob
//        var description_ob=obj.description_ob
//        var book_condition_ob=obj.book_condition_ob
//        var publication_year_ob=obj.publication_year_ob
//        var based_on_ob=obj.based_on_ob
//        var standard_ob=obj.standard_ob
//        var board_preffrence_ob=obj.board_preffrence_ob
//        var medium_ob=obj.medium_ob
//        var publisher_ob=obj.publisher_ob
//        val url =
//            "https://www.onlinevideodownloader.co/bookstore/api_v1/app/get_book_filter.php?page=$page_ob&limit=$limit_ob&city=$city_ob&state=$state_ob&category_id=$cate_id_ob&search=$search_ob&min_price=$min_prise_ob&max_price=$max_prise_ob&title=$title_ob&author=$author_ob&description=$description_ob&book_condition=$book_condition_ob&publication_year=$publication_year_ob&based_on=$based_on_ob&standard=$standard_ob&board_preference=$board_preffrence_ob&medium=$medium_ob&publisher=$publisher_ob"
//        Log.d("YourActivity", "Book Title:$url")
//
//        val request = JsonObjectRequest(Request.Method.GET, url, null,
//            Response.Listener { response ->
//                val books: List<BookDetailseseIN> = parseBooks(response)
//                callback(books, null)
//            },
//            Response.ErrorListener { error ->
//                callback(null, error.message)
//            })
//
//        requestQueue.add(request)
//
//    }
//
//    private fun parseBooks(response: JSONObject): List<BookDetailseseIN> {
//        val gson = Gson()
//        if (response.has("book_details")) {
//            val bookDetailsArray = response.getJSONArray("book_details")
//            return gson.fromJson(bookDetailsArray.toString(), object : TypeToken<List<BookDetailseseIN>>() {}.type)
//        } else {
//            Log.e("BookRepository", "Missing 'book_details' field in the response")
//            return emptyList()
//        }
//    }
//
//}
class BookRepository_filter(private val context: Context) {
    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context)
    }

    // Maximum number of retries
    private val maxRetries = 5
    private var retryCount = 0

    fun getBooks(callback: (List<BookDetailseseIN>?, String?) -> Unit) {
        val obj = BookFilter
        // ... (your existing code to construct URL)
        var page_ob=obj.page_ob
        var limit_ob=obj.limit_ob
        var city_ob=obj.city_ob
        var state_ob=obj.state_ob
        var cate_id_ob=obj.cate_id_ob
        var search_ob=obj.search_ob
        var min_prise_ob=obj.min_prise_ob
        var max_prise_ob=obj.max_prise_ob
        var title_ob=obj.title_ob
        var author_ob=obj.author_ob
        var description_ob=obj.description_ob
        var book_condition_ob=obj.book_condition_ob
        var publication_year_ob=obj.publication_year_ob
        var based_on_ob=obj.based_on_ob
        var standard_ob=obj.standard_ob
        var board_preffrence_ob=obj.board_preffrence_ob
        var medium_ob=obj.medium_ob
        var publisher_ob=obj.publisher_ob
        val url =
            "https://www.onlinevideodownloader.co/bookstore/api_v1/app/get_book_filter.php?page=$page_ob&limit=$limit_ob&city=$city_ob&state=$state_ob&category_id=$cate_id_ob&search=$search_ob&min_price=$min_prise_ob&max_price=$max_prise_ob&title=$title_ob&author=$author_ob&description=$description_ob&book_condition=$book_condition_ob&publication_year=$publication_year_ob&based_on=$based_on_ob&standard=$standard_ob&board_preference=$board_preffrence_ob&medium=$medium_ob&publisher=$publisher_ob"
        Log.d("YourActivity", "Book Title:$url")
        val request = JsonObjectRequest(Request.Method.GET, url, null,
            Response.Listener { response ->
                val books: List<BookDetailseseIN> = parseBooks(response)
                callback(books, null)
                // Reset retry count upon successful response
                retryCount = 0
            },
            Response.ErrorListener { error ->
                if (retryCount < maxRetries) {
                    // Retry the request
                    Log.d("YourActivity", "Retrying request. Retry Count: $retryCount")
                    retryCount++
                    getBooks(callback)
                } else {
                    // Maximum retries reached, report error
                    retryCount = 0 // Reset retry count for future requests
                    callback(null, error.message)
                }
            })

        requestQueue.add(request)
    }

    private fun parseBooks(response: JSONObject): List<BookDetailseseIN> {
        val gson = Gson()
        if (response.has("book_details")) {
            val bookDetailsArray = response.getJSONArray("book_details")
            return gson.fromJson(
                bookDetailsArray.toString(),
                object : TypeToken<List<BookDetailseseIN>>() {}.type
            )
        } else {
            Log.e("BookRepository", "Missing 'book_details' field in the response")
            return emptyList()
        }
    }
}