package com.shetty.heyalle.domain.usecase

interface GoogleVisionUsecase {
    suspend fun fetchCollection(imagePath: String): MutableList<String>
    suspend fun fetchDescription(imagePath: String): String
}