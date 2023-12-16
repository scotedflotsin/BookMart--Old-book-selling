package com.bookmart.bookmart.Android_retrofit.API_SERVICE.Models

data class UserResponse(
    val error: Boolean, // Indicates whether an error occurred
    val message: String, // A message providing additional information
    val user_details: UserDetails // An object containing user details
)

data class UserDetails(
    val ID:String,
    val Email: String, // User's email
    val Username: String, // User's username
    val ProfilePicture: String, // URL to the user's profile picture
    val Location: LocationDetails // An object containing location details
)

data class LocationDetails(
    val Latitude: String,
    val Longitude: String,
    val Address: String,
    val City: String,
    val State: String,
    val Country: String,
    val PostalCode: String
)
// User.kt
data class Category(
    val category_id: String,
    val category_name: String,
    val category_image: String
)

// ApiResponse.kt
data class ApiResponse(
    val message: String,
    val error: Boolean,
    val categories: List<Category>
)

