package com.shetty.heyalle.domain.usecase

interface ScreenshotUseCase {

    suspend fun getImagesFromGallery(): List<String>

    fun saveNote(note: String, uri: String)
    fun getNote(uri: String): String

}