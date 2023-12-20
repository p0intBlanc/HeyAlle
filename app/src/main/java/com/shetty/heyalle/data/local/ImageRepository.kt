package com.shetty.heyalle.data.local


interface ImageRepository {
    suspend fun getImages(): List<String>
}