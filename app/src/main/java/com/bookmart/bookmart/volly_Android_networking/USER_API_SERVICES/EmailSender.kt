package com.bookmart.bookmart.volly_Android_networking.USER_API_SERVICES

import android.content.Context
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class EmailSender(private val context: Context) {

    private val requestQueue: RequestQueue = Volley.newRequestQueue(context)
    private val apiUrl = "https://onlinevideodownloader.co/process_form.php" // Replace with your API URL

    fun sendEmail(name: String, email: String, message: String, listener: EmailSendListener) {
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
                params["name"] = name
                params["email"] = email
                params["message"] = message
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
