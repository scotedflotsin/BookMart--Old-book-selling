package com.bookmart.bookmart.Android_retrofit.API_SERVICE.Models


data class book_details(
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
    val user_details: UserDetailsl
)

data class UserDetailsl(
    val ID: String,
    val Email: String,
    val Username: String,
    val ProfilePicture: String,
    val Location: Locationl
)

data class Locationl(
    val Latitude: String,
    val Longitude: String,
    val Address: String,
    val City: String,
    val State: String,
    val Country: String,
    val PostalCode: String
)
