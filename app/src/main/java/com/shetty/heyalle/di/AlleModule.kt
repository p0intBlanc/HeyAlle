package com.shetty.heyalle.di

import com.shetty.heyalle.data.local.ImageRepository
import com.shetty.heyalle.data.local.ImageRepositoryImpl
import com.shetty.heyalle.data.local.ImageSource
import com.shetty.heyalle.data.local.ImageSourceImpl
import com.shetty.heyalle.domain.usecase.GoogleVisionUseCaseImpl
import com.shetty.heyalle.domain.usecase.GoogleVisionUsecase
import com.shetty.heyalle.domain.usecase.ScreenshotUseCase
import com.shetty.heyalle.domain.usecase.ScreenshotUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface AlleModule {

    @Singleton
    @Binds
    fun bindImageUsecase(usecase: ScreenshotUseCaseImpl): ScreenshotUseCase

    @Singleton
    @Binds
    fun bindMlUsecase(usecase: GoogleVisionUseCaseImpl): GoogleVisionUsecase

    @Singleton
    @Binds
    fun bindImageRepo(repo: ImageRepositoryImpl): ImageRepository

    @Singleton
    @Binds
    fun bindImageSource(repo: ImageSourceImpl): ImageSource

}