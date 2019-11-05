package com.mandarine.targetList.app

import android.app.Application
import com.google.firebase.database.FirebaseDatabase
import com.jakewharton.threetenabp.AndroidThreeTen

open class MakeItApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        AndroidThreeTen.init(this)
    }
}
