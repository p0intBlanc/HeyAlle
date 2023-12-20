package com.shetty.heyalle.ui

sealed class UIState {
    object Loading : UIState()

    data class Success(
        val articles: List<String>
    ) : UIState()

    data class Error(
        val errorDetails: String
    ) : UIState()

    object PermissionNotGranted : UIState()

}