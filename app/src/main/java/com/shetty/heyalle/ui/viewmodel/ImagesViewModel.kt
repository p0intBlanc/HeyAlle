package com.shetty.heyalle.ui.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.shetty.heyalle.domain.usecase.GoogleVisionUsecase
import com.shetty.heyalle.domain.usecase.ScreenshotUseCase
import com.shetty.heyalle.ui.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class ImagesViewModel @Inject constructor(
    private val imagesUseCase: ScreenshotUseCase,
    private val mlUsecase: GoogleVisionUsecase
) : ViewModel() {
    private val TAG = "ImagesViewModel"

    private var _selectedImageUri = MutableStateFlow("")
    var selectedImageUri: StateFlow<String> = _selectedImageUri

    private val _state = MutableStateFlow<UIState>(UIState.Loading)
    val state: StateFlow<UIState> get() = _state.asStateFlow()

    private var _collectionLabels = MutableStateFlow<List<String>>(emptyList())
    var collectionLabels: StateFlow<List<String>> = _collectionLabels

    private var _imageDescription = MutableStateFlow<String>("")
    var imageDescription: StateFlow<String> = _imageDescription

    fun getImages() {
        viewModelScope.launch {
            val data = imagesUseCase.getImagesFromGallery()
            Log.i(TAG, "getImages: $data")
            _state.value = UIState.Success(data)
            if (data.isEmpty().not())
                _selectedImageUri.value = data[0]
        }
    }

    fun selectImage(uri: String) {
        _selectedImageUri.value = uri
    }

    fun fetchCollectionsForImage(imageUri: String) {
        viewModelScope.launch {
            mlUsecase.fetchCollection(imageUri).let {
                _collectionLabels.value = it
            }
        }
    }

    fun fetchDescriptionForImage(imageUri: String) {
        viewModelScope.launch {
            mlUsecase.fetchDescription(imageUri).let {
                _imageDescription.value = it.ifBlank {
                    "No Description found for image"
                }
            }
        }
    }

    fun getNotesForImage(uri: String): String {
        return imagesUseCase.getNote(uri)
    }

    fun saveNotesForImage(uri: String, note: String) {
        return imagesUseCase.saveNote(note, uri)
    }

    fun permissionsGranted(isGranted: Boolean) {
        if (isGranted) {
            getImages()
        } else {
            _state.value = UIState.PermissionNotGranted
        }
    }

    fun deleteImage(uri: String) {
        val image = File(uri)
        if (image.exists()) {
            if (image.delete()) {
                System.out.println("file Deleted :$uri")
            } else {
                System.out.println("file not Deleted :$uri")
            }
        }
    }

}