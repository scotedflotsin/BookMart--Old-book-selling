package com.bookmart.bookmart.BooksShowMet


import android.content.Context
import android.util.Log
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.bookmart.bookmart.Global_Object.UniversalUserID_and_Limit_book
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

data class BookDetailsese(
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

class BookRepository(private val context: Context) {
    private val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context)
    }

    fun getBooks(callback: (List<BookDetailsese>?,String?) -> Unit) {
        val obj= UniversalUserID_and_Limit_book
    val userId=obj.userId
    val limit=obj.limit
            val url =
                "https://www.onlinevideodownloader.co/bookstore/api_v1/app/get_user_books.php?user_id=$userId&limit=$limit"

            val request = JsonObjectRequest(Request.Method.GET, url, null,
                Response.Listener { response ->
                    val books: List<BookDetailsese> = parseBooks(response)
                    callback(books, null)
                },
                Response.ErrorListener { error ->
                    callback(null, error.message)
                })

            requestQueue.add(request)

    }

    private fun parseBooks(response: JSONObject): List<BookDetailsese> {
        val gson = Gson()

        if (response.has("book_details")) {
            val bookDetailsArray = response.getJSONArray("book_details")
            return gson.fromJson(bookDetailsArray.toString(), object : TypeToken<List<BookDetailsese>>() {}.type)
        } else {
            Log.e("BookRepository", "Missing 'book_details' field in the response")
            return emptyList()
        }
    }

}


//"book_details": [
//{
//    "book_id": "18",
//    "title": "maths king for all increase maths calculation speedimprove maths",
//    "author": "Harsh Verma, Sachin kumar",
//    "description": "very good for whom lazy in maths??",
//    "price": "206.00",
//    "book_condition": "good",
//    "user_id": "8",
//    "photo1": "https://www.onlinevideodownloader.co/bookstore/api_v1/app/data/photo1_1700552100188.jpeg",
//    "photo2": "https://www.onlinevideodownloader.co/bookstore/api_v1/app/data/photo2_1700552100189.jpeg",
//    "photo3": "https://www.onlinevideodownloader.co/bookstore/api_v1/app/data/photo3_1700552100189.jpeg",
//    "name": "math king",
//    "publication_year": "2023",
//    "publisher": "HPL pvt ltd",
//    "based_on": "free",
//    "standard": "free",
//    "board_preference": "no",
//    "medium": "english& hindi",
//    "local_address": "near sami mobile shop",
//    "whatsapp_number": "916392328912",
//    "whatsapp_enabled": "1",
//    "phone_number": "916392328912",
//    "phone_enabled": "1",
//    "created_at": "2023-11-01 08:35:00",
//    "category_id": "2",
//    "user_details": {
//    "ID": "8",
//    "Email": "scotedflotsin.co.ltd@gmail.com",
//    "Username": "Jai Mata Di",
//    "ProfilePicture": "https://www.onlinevideodownloader.co/bookstore/api_v1/users/data/image1700551827.jpg",
//    "Location": {
//    "Latitude": "26.43890920",
//    "Longitude": "80.54656870",
//    "Address": "CGQW+HJG, Achalganj, Bithar, Uttar Pradesh 209801, India",
//    "City": "Achalganj",
//    "State": "Uttar Pradesh",
//    "Country": "India",
//    "PostalCode": "209801"
//}
//}
//}
