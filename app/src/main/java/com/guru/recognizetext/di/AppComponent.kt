package com.guru.recognizetext.di

import com.guru.recognizetext.ImagePickerBottomSheet
import com.guru.recognizetext.MainActivity
import dagger.Component

@Component(modules = [AppModule::class])
interface AppComponent {
    fun initDi(mainActivity: MainActivity)
    fun initDi(imagePickerBottomSheet: ImagePickerBottomSheet)
}
