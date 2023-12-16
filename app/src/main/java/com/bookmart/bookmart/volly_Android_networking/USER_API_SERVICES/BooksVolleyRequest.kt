package com.bookmart.bookmart.volly_Android_networking.USER_API_SERVICES

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class BooksVolleyRequest private constructor(private val context: Context) {
    private var requestQueue: RequestQueue = getRequestQueue(context)

    companion object {
        private var INSTANCE: BooksVolleyRequest? = null

        @Synchronized
        fun getInstance(context: Context): BooksVolleyRequest {
            if (INSTANCE == null) {
                INSTANCE = BooksVolleyRequest(context)
            }
            return INSTANCE!!
        }
    }

    private fun getRequestQueue(context: Context): RequestQueue {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.applicationContext)
        }
        return requestQueue
    }

    fun <T> addToRequestQueue(context: Context, req: Request<T>) {
        getRequestQueue(context).add(req)
    }

// ...

    fun getBookDetails(context: Context, url: String, callback: VolleyCallback) {
        val jsonObjectRequest = JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            { response ->
                // Handle the response here
                callback.onSuccess(response)
            },
            { error ->
                // Handle errors here
                callback.onError(error.toString())
            }
        )

        // Add the request to the RequestQueue
        addToRequestQueue(context, jsonObjectRequest)
    }


    interface VolleyCallback {
        fun onSuccess(result: JSONObject)
        fun onError(error: String)
    }


}
