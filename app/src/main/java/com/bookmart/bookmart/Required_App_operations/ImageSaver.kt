import android.content.ContentValues
import android.content.Context
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class ImageSaver(private val context: Context) {

    fun saveImage(compressedImageBytes: ByteArray): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Use MediaStore API for Android 10 and above
            saveToMediaStore(compressedImageBytes)
        } else {
            // Use traditional external storage access
            saveToExternalStorage(compressedImageBytes)
        }
    }

    private fun saveToMediaStore(compressedImageBytes: ByteArray): Boolean {
        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "compressed_image.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpg")
            put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }

        val contentResolver = context.contentResolver
        val uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

        return uri?.let { imageUri ->
            try {
                val outputStream: OutputStream? = contentResolver.openOutputStream(imageUri)
                outputStream?.write(compressedImageBytes)
                outputStream?.close()
                true
            } catch (e: Exception) {
                Log.e("ImageSaver", "Error saving image to MediaStore: ${e.message}")
                false
            }
        } ?: false
    }

    private fun saveToExternalStorage(compressedImageBytes: ByteArray): Boolean {
        val directory = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "YourAppName")
        if (!directory.exists()) {
            directory.mkdirs()
        }

        val file = File(directory, "compressed_image.jpg")

        return try {
            val outputStream = FileOutputStream(file)
            outputStream.write(compressedImageBytes)
            outputStream.close()
            true
        } catch (e: Exception) {
            Log.e("ImageSaver", "Error saving image to external storage: ${e.message}")
            false
        }
    }
}
