package com.shetty.heyalle.data.local

import javax.inject.Inject

class ImageRepositoryImpl @Inject constructor(
    private val imageSource: ImageSource
) : ImageRepository {
    override suspend fun getImages(
    ): List<String> {
        return imageSource.getImages(
        )
    }
}