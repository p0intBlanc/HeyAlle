package com.shetty.heyalle.domain.usecase

interface ScreenshotUseCase {

    suspend fun getImagesFromGallery(): List<String>
    suspend fun fetchCollection(imagePath: String): MutableList<String>
    suspend fun fetchDescription(imagePath: String): String

}