package com.bookmart.bookmart.volly_Android_networking.USER_API_SERVICES

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class Check_user_email_exist_or_not(private val context: Context) {

    private val requestQueue: RequestQueue = Volley.newRequestQueue(context)
    private val apiUrl = "https://onlinevideodownloader.co/bookstore/api_v1/users/chek_email.php" // Replace with your API URL

    fun Email_check(email: String, listener: EmailSendListener) {
        val stringRequest = object : StringRequest(
            Request.Method.POST, apiUrl,
            { response ->
                listener.onSuccess(response)
            },
            { error ->
                listener.onError(error.toString())
            }
        ) {
            override fun getParams(): Map<String, String> {
                val params = HashMap<String, String>()
                params["email"] = email
                return params
            }
        }

        requestQueue.add(stringRequest)
    }

    interface EmailSendListener {
        fun onSuccess(response: String)
        fun onError(error: String)
    }
}