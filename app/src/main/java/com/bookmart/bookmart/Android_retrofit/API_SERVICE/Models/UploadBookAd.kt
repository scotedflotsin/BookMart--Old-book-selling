package com.bookmart.bookmart.Android_retrofit.API_SERVICE.Models

data class UploadBookAd(
    val user_id: String,
    val title: String,
    val name: String,
    val author: String,
    val description: String,
    val price: String,
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
    val photo1: ByteArray,
    val photo2: ByteArray,
    val photo3: ByteArray,
    val book_condition: String,
    val category_id: String,val created_at:String
)

//created by harsh Verma
/**>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>*/
/** It data class will never used only for testing, Deprecated
 * again it never be used and doesn't have any relation with real word App program*/
data class UploadBookAdDepricated(  //Ignore it
    val userId: String,
    val title: String,
    val name: String,
    val author: String,
    val description: String,
    val price: Double,
    val publicationYear: Int,
    val publisher: String,
    val basedOn: String,
    val standard: String,
    val boardPreference: String,
    val medium: String,
    val localAddress: String,
    val whatsappNumber: String,
    val whatsappEnabled: Boolean,
    val phoneNumber: String,
    val phoneNumberEnabled: Boolean,
    val photo1: ByteArray,
    val photo2: ByteArray,
    val photo3: ByteArray,
    val bookCondition: String,
    val category: String
)
