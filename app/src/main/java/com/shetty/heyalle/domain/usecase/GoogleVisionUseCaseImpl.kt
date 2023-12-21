package com.shetty.heyalle.domain.usecase

import android.graphics.BitmapFactory
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GoogleVisionUseCaseImpl @Inject constructor() : GoogleVisionUsecase {
    override suspend fun fetchCollection(imagePath: String): MutableList<String> =
        suspendCoroutine {
            val labeler = ImageLabeling.getClient(ImageLabelerOptions.DEFAULT_OPTIONS)
            val labelList: MutableList<String> = mutableListOf()
            val bitmap = BitmapFactory.decodeFile(imagePath)
            val imageBit = InputImage.fromBitmap(bitmap, 0)
            labeler.process(imageBit)
                .addOnSuccessListener { labels ->
                    for (label in labels) {
                        val text = label.text
                        labelList.add(text)
                    }
                    it.resumeWith(Result.success(labelList))

                }
                .addOnFailureListener { e ->
                    it.resumeWith(Result.success(mutableListOf()))
                }
        }

    override suspend fun fetchDescription(imagePath: String): String =
        suspendCoroutine { continuation ->
            val bitmap = BitmapFactory.decodeFile(imagePath)
            val imageBit = InputImage.fromBitmap(bitmap, 0)
            val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)
            recognizer.process(imageBit)
                .addOnSuccessListener { visionText ->
                    val resultText = StringBuilder().apply {
                        visionText.textBlocks.take(50).forEach { block ->
                            append(block.text).append(" ")
                        }
                    }.toString()
                    continuation.resume(resultText)
                }
                .addOnFailureListener { e ->
                    continuation.resume("")
                }

        }

}