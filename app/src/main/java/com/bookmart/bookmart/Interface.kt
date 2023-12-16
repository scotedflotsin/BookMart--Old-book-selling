package com.bookmart.bookmart

interface Interface{
}
interface OnCategoryClickListener {
    fun onCategoryClick(categoryId: String)


}





//val imageByteArray: ByteArray = // your image byte array

//// Create RequestBody
//val requestBody: RequestBody = imageByteArray.toRequestBody("image/*".toMediaTypeOrNull())
//
//// Create MultipartBody.Part
//val imagePart: MultipartBody.Part =
//    MultipartBody.Part.createFormData("image", "image.jpg", requestBody)