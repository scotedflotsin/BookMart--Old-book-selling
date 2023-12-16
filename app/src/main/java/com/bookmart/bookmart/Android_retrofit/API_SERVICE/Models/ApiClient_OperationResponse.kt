package com.bookmart.bookmart.Android_retrofit.API_SERVICE.Models

data class ApiClient_OperationResponse(
    val error: Boolean, // Indicates whether an error occurred
    val message: String, // A message providing additional information
)
data class User_passsword_update(val email:String,val new_password:String)