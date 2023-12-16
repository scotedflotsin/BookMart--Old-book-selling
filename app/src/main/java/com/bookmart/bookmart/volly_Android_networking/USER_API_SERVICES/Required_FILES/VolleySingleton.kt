package com.bookmart.bookmart.volly_Android_networking.USER_API_SERVICES.Required_FILES

import android.content.Context
import com.android.volley.toolbox.Volley
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.RequestQueue

class VolleySingleton private constructor(context: Context) {
    companion object {
        @Volatile
        private var INSTANCE: VolleySingleton? = null

        fun getInstance(context: Context): VolleySingleton {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: VolleySingleton(context).also {
                    INSTANCE = it
                }
            }
        }
    }

    val requestQueue: RequestQueue by lazy {
        Volley.newRequestQueue(context.applicationContext)
    }
}



