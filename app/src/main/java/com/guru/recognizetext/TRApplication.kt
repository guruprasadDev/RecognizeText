package com.guru.recognizetext

import android.app.Application
import com.guru.recognizetext.di.AppComponent
import com.guru.recognizetext.di.AppModule
import com.guru.recognizetext.di.DaggerAppComponent

class TRApplication : Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.builder()
            .appModule(AppModule(this))
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        appComponent // Initialize Dagger
    }
}
