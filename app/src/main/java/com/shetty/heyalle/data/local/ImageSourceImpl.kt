package com.shetty.heyalle.data.local

import android.content.ContentResolver
import android.provider.MediaStore
import android.util.Log
import javax.inject.Inject

class ImageSourceImpl @Inject constructor(
    private val contentResolver: ContentResolver
) : ImageSource {

    private val TAG = "ImageSourceImpl"

    override suspend fun getImages(
    ): List<String> {

        val imageList = mutableListOf<String>()

        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val selection = MediaStore.Images.Media.BUCKET_DISPLAY_NAME + " = ?"
        val selectionArgs = arrayOf("Screenshots")
        val cursor = contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            null
        )

        cursor?.use {
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            while (cursor.moveToNext()) {
                val imagePath = cursor.getString(columnIndex)
                imageList.add(imagePath)
            }
        }
        return imageList
    }

}