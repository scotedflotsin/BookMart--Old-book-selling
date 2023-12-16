package com.bookmart.bookmart.Android_retrofit.API_SERVICE.API_interface


import com.bookmart.bookmart.Android_retrofit.API_SERVICE.Models.ApiClient_OperationResponse
import com.bookmart.bookmart.Android_retrofit.API_SERVICE.Models.Book_upload_response
import com.bookmart.bookmart.Android_retrofit.API_SERVICE.Models.UploadBookAd
import com.bookmart.bookmart.Android_retrofit.API_SERVICE.Models.UserResponse
import com.bookmart.bookmart.BooksShowMet.BookDetailsese
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query


interface ApiService {

//.. for creating user

    @Multipart
    @POST("bookstore/api_v1/users/create_user.php/")
    fun createUser(
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
        @Part("username") username: RequestBody,
        @Part("latitude") latitude: RequestBody,
        @Part("longitude") longitude: RequestBody,
        @Part("address") address: RequestBody,
        @Part("city") city: RequestBody,
        @Part("state") state: RequestBody,
        @Part("country") country: RequestBody,
        @Part("postal_code") postalCode: RequestBody,
        @Part profilePicture: MultipartBody.Part? // Use nullable to handle optional profile picture
    ): Call<UserResponse>

//for login

    @Multipart
    @POST("bookstore/api_v1/users/login_user.php")
    fun login_user(
        @Part("email") email: RequestBody,
        @Part("password") password: RequestBody,
    ): Call<UserResponse>

    // for updating password
    @Multipart
    @POST("bookstore/api_v1/users/update_pass.php/")
    fun update_password(
        @Part("email") email: RequestBody,
        @Part("new_password") new_password: RequestBody,
    ): Call<ApiClient_OperationResponse>

    // for updating password
    @Multipart
    @POST("bookstore/api_v1/users/edit_user.php/")
    fun update_user_data(
        @Part("user_id") userId: RequestBody,
        @Part("field_to_edit") fieldToEdit: RequestBody,
        @Part("new_value") new_updated_valuse: RequestBody,
    ): Call<ApiClient_OperationResponse>

    // for updating password
    @Multipart
    @POST("bookstore/api_v1/users/edit_user.php/")
    fun update_profile_image(
        @Part("user_id") userId: RequestBody,
        @Part("field_to_edit") fieldToEdit: RequestBody,
        @Part profilePicture: MultipartBody.Part?,// Use nullable to handle optional profile picture
        @Part("old_profile_url") old_profile_url: RequestBody,
    ): Call<ApiClient_OperationResponse>

    //for uploading book ads on server APi client
    @Multipart
    @POST("bookstore/api_v1/app/save_new_book.php")
    fun uploadBook_ApiClient(
        @Part("title") bookTitle: RequestBody,
        @Part("author") bookAuthor: RequestBody,
        @Part("description") bookDescription: RequestBody,
        @Part("price") bookPrice: RequestBody,
        @Part("book_condition") bookCondition: RequestBody,
        @Part("user_id") userID: RequestBody,
        @Part("name") bookName: RequestBody,
        @Part("publication_year") bookPublicationYear: RequestBody,
        @Part("publisher") bookPublisher: RequestBody,
        @Part("based_on") bookBasedOn: RequestBody,
        @Part("standard") bookStandard: RequestBody,
        @Part("board_preference") bookBoardPreference: RequestBody,
        @Part("medium") bookMedium: RequestBody,
        @Part("local_address") bookLocalAddress: RequestBody,
        @Part("whatsapp_number") whatsNumber: RequestBody,
        @Part("whatsapp_enabled") whatsEnabled: RequestBody,
        @Part("phone_number") phoneNumber: RequestBody,
        @Part("phone_enabled") phoneEnabled: RequestBody,
        @Part("category") category: RequestBody,
//    @Part("photo1") bookPhotoFront: RequestBody,
//    @Part("photo2") bookPhotBack: RequestBody,
//    @Part("photo3") bookPhotoInside: RequestBody,
        @Part bookPhotoFront: MultipartBody.Part?,
        @Part bookPhotoBack: MultipartBody.Part?,
        @Part bookPhotoInside: MultipartBody.Part?
    ): Call<Book_upload_response>


    @GET("bookstore/api_v1/app/get_book_filter.php")
    fun getBooksAp(
        @Query("page") page: String,
        @Query("limit") limit: String,
        @Query("search") search: RequestBody,
        @Query("min_price") min_price: RequestBody,
        @Query("max_price") max_price: RequestBody,
        @Query("city") city: RequestBody,
        @Query("state") state: RequestBody,
        @Query("title") title: RequestBody,
        @Query("author") author: RequestBody,
        @Query("description") description: RequestBody,
        @Query("book_condition") book_condition: RequestBody,
        @Query("publication_year") publication_year: RequestBody,
        @Query("based_on") based_on: RequestBody,
        @Query("standard") standard: RequestBody,
        @Query("board_preference") board_preference: RequestBody,
        @Query("medium") medium: RequestBody,
        @Query("publisher") publisher: RequestBody,
        @Query("category_id") category_id: RequestBody
    ): Call<BookDetailsese>
}




