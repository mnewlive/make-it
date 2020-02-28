package com.mandarine.targetList.features.targets.list

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.mandarine.targetList.interfaces.SelectTargetViewContract
import com.mandarine.targetList.model.Goal

class TargetsPresenter(private val contract: SelectTargetViewContract) {

    var firebaseUser: FirebaseUser? = null
    var targetList: ArrayList<Goal> = ArrayList()
    private var databaseReference: DatabaseReference? = null
    private var targetsRef: DatabaseReference? = null
    private var uid: String? = null

    fun setInitialData() {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        databaseReference = FirebaseDatabase.getInstance().reference
        uid = firebaseUser?.uid
        targetsRef = databaseReference?.child("targets")
            ?.child("users")?.child(uid.toString())
            ?.child("targets")
    }

    fun getTargetsFromDb() {
        targetsRef?.addListenerForSingleValueEvent(MyValueEventListener<String>())
    }

    fun getTargetsByPriority() {
        targetsRef?.addListenerForSingleValueEvent(MyValueEventListener(Goal::priority))
    }

    fun getTargetsByDeadline() {
        targetsRef?.addListenerForSingleValueEvent(MyValueEventListener(Goal::deadline))
    }

    fun collectCompletedGoals() = targetList.filter { it.isComplete }

    fun collectCurrentGoals() = targetList.filter { !it.isComplete }

    private inner class MyValueEventListener<R: Comparable<R>>(
        private val sortCriteria: (Goal) -> R? = { null }
    ): ValueEventListener {
        override fun onDataChange(dataSnapshot: DataSnapshot) {
            targetList.clear()
            dataSnapshot.children
                .mapNotNull { it.getValue(Goal::class.java) }
                .sortedBy(sortCriteria)
                .toCollection(targetList)
            contract.updateViewContent()
        }

        override fun onCancelled(databaseError: DatabaseError) {
            Log.d("some", "Error trying to get targets for ${databaseError.message}")
        }
    }
}
