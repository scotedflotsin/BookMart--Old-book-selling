import com.bookmart.bookmart.Android_retrofit.API_SERVICE.Models.book_details
import com.bookmart.bookmart.BooksShowMet.BookDetailsese

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ProductService {
    @GET("bookstore/api_v1/app/get_book_filter.php")
    fun getOldProducts(
        @Query("page") page: String?,
        @Query("limit") limit: String?,
        @Query("search") search: String?,
        @Query("min_price") minPrice: String?,
        @Query("max_price") maxPrice: String?,
        @Query("city") city: String?,
        @Query("state") state: String?,
        @Query("title") title: String?,
        @Query("author") author: String?,
        @Query("description") description: String?,
        @Query("book_condition") bookCondition: String?,
        @Query("publication_year") publicationYear: String?,
        @Query("based_on") basedOn: String?,
        @Query("standard") standard: String?,
        @Query("board_preference") boardPreference: String?,
        @Query("medium") medium: String?,
        @Query("publisher") publisher: String?,
        @Query("category_id") categoryId: String?
    ): Call<List<book_details>>
}
