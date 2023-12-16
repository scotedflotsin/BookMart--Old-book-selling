package com.bookmart.bookmart.Required_App_operations
import android.content.Context
import android.graphics.Bitmap
import android.util.Log
import java.io.ByteArrayOutputStream

class ImageCompressor(private val context: Context) {

    fun compressImage(bitmap: Bitmap, quality: Int): ByteArray? {
        var compressedImageBytes: ByteArray? = null

        try {
            // Compress image to reduce file size
            val baos = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos)
            compressedImageBytes = baos.toByteArray()

            // Optional: You can also recycle the bitmap to free up memory
            bitmap.recycle()
        } catch (e: Exception) {
            Log.e("ImageCompressor", "Error compressing image: ${e.message}")
        }

        return compressedImageBytes
    }

}


