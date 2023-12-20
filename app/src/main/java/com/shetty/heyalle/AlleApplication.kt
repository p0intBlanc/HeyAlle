package com.shetty.heyalle

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AlleApplication:Application() {
    override fun onCreate() {
        super.onCreate()
    }
}