package com.mandarine.targetList.app

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen

open class MakeItApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
    }
}
