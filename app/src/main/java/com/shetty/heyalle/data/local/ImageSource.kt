package com.shetty.heyalle.data.local


interface ImageSource {
    suspend fun getImages(): List<String>
}