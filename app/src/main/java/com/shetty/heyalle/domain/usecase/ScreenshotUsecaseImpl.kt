package com.shetty.heyalle.domain.usecase

import android.graphics.BitmapFactory
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import com.shetty.heyalle.data.local.ImageRepository
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine



class ScreenshotUseCaseImpl @Inject constructor(
    private val repo: ImageRepository
) : ScreenshotUseCase {


    override suspend fun getImagesFromGallery(): List<String> {
        return repo.getImages()
    }



}