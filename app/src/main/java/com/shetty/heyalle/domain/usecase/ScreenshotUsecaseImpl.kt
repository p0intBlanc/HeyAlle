package com.shetty.heyalle.domain.usecase

import android.content.SharedPreferences
import com.shetty.heyalle.data.local.ImageRepository
import javax.inject.Inject


class ScreenshotUseCaseImpl @Inject constructor(
    private val repo: ImageRepository,
    private val pref: SharedPreferences
) : ScreenshotUseCase {


    override suspend fun getImagesFromGallery(): List<String> {
        return repo.getImages()
    }

    override fun saveNote(note: String, uri: String) {
        pref.edit().putString(uri, note).apply()
    }

    override fun getNote(uri: String): String {
        return pref.getString(uri, "") ?: ""
    }


}