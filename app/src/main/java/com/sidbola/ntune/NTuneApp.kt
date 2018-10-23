package com.sidbola.ntune

import android.app.Application
import android.content.Context

class NTuneApp: Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: NTuneApp? = null

        fun getApplicationContext(): Context {
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()

    }
}