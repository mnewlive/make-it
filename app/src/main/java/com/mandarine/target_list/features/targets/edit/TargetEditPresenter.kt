package com.mandarine.target_list.features.targets.edit

import android.util.Log
import com.google.firebase.database.*
import com.mandarine.target_list.model.Target

class TargetEditPresenter {

    private var target: Target? = null
    private var databaseReference: DatabaseReference? = null

    fun setInitialData(guid: String) {
        databaseReference = FirebaseDatabase.getInstance().getReference("targets").child(guid)
        Log.d("some", "databaseReference: $databaseReference")
    }

    fun getTargetName(): String = target?.name ?: "some"
}