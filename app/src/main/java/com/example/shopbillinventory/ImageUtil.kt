package com.example.shopbillinventory

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.Activity
import android.content.ContentResolver
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

object ImageUtil {
    private const val WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 100
    fun getandDownload(context: Context, layoutContainer: View): Boolean {
        val bitmap = captureView(layoutContainer)
        bitmap?.let {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                saveBitmap(context, it)
            } else {
                saveBitmapBelowVersion(context, it)
            }
        }
        return false
    }

    private fun captureView(view: View): Bitmap? {
        return try {
            view.isDrawingCacheEnabled = true
            val bitmap = Bitmap.createBitmap(view.drawingCache)
            view.isDrawingCacheEnabled = false
            bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun saveBitmap(context: Context, bitmap: Bitmap): Boolean {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "Unique_Posters_$timeStamp.png"

        val contentResolver: ContentResolver = context.contentResolver

        val values = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Images.Media.MIME_TYPE, "image/png")
            put(
                MediaStore.Images.Media.RELATIVE_PATH,
                "${Environment.DIRECTORY_DCIM}/Unique_Posters"
            )
        }

        val imageUri: Uri? =
            contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        imageUri?.let {
            try {
                contentResolver.openOutputStream(it)?.use { outputStream ->
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                }
                Log.d("saveBitmap", "success $it")
                return true
            } catch (e: IOException) {
                e.printStackTrace()
                Log.e("saveBitmap", "Failed ${e.message}")
                return false
            }
        } ?: run {
            Log.e("saveBitmap", "Failed to store image")
            return false
        }
    }

    private fun saveBitmapBelowVersion(context: Context, bitmap: Bitmap): Boolean {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val fileName = "Bussiness_Posters_$timeStamp.png"

        if (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            Log.e("saveBitmap", "Write external storage permission not granted")
            ActivityCompat.requestPermissions(
                context as Activity,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                WRITE_EXTERNAL_STORAGE_REQUEST_CODE
            )
            return false
        }

        // Get the directory where you want to save the image
        val directory = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),
            "Unique_Posters"
        )

        // Make sure the directory exists, if not, create it
        if (!directory.exists()) {
            if (!directory.mkdirs()) {
                Log.e("saveBitmap", "Failed to create directory")
                return false
            }
        }

        // Create the file where the bitmap will be saved
        val file = File(directory, fileName)

        return try {
            FileOutputStream(file).use { outputStream ->
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
                outputStream.flush()
            }
            Log.d("saveBitmap", "Image saved successfully: ${file.absolutePath}")
            true
        } catch (e: IOException) {
            e.printStackTrace()
            Log.e("saveBitmap", "Error saving bitmap: ${e.message}")
            false
        }
    }


}