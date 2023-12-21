package com.shetty.heyalle.domain.usecase

interface ScreenshotUseCase {

    suspend fun getImagesFromGallery(): List<String>

}