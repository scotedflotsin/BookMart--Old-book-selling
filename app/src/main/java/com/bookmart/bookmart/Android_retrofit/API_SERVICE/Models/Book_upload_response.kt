package com.bookmart.bookmart.Android_retrofit.API_SERVICE.Models

data class Book_upload_response(
    val error: Boolean, // Indicates whether an error occurred
    val message: String, // A message providing additional information
    val book_id: String
)
