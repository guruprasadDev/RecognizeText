package com.guru.recognizetext.di

import android.content.Context
import com.guru.recognizetext.helperclasses.DialogHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.android.qualifiers.ActivityContext

@Module
@InstallIn(ActivityComponent::class)
object DialogHelperModule {

    @Provides
    fun provideDialogHelper(@ActivityContext context: Context): DialogHelper {
        return DialogHelper(context)
    }
}
