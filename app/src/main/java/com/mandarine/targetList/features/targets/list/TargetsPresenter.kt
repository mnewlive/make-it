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
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                targetList.clear()
                dataSnapshot.children
                    .mapNotNull { it.getValue(Goal::class.java) }
                    .toCollection(targetList)
                contract.updateViewContent()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("some", "Error trying to get targets for ${databaseError.message}")
            }
        }
        targetsRef?.addListenerForSingleValueEvent(valueEventListener)
    }

    fun getTargetsByPriority() {
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                targetList.clear()
                dataSnapshot.children
                    .mapNotNull { it.getValue(Goal::class.java) }
                    .sortedBy { it.priority }
                    .toCollection(targetList)
                contract.updateViewContent()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("some", "Error trying to get targets for ${databaseError.message}")
            }
        }
        targetsRef?.addListenerForSingleValueEvent(valueEventListener)
    }

    fun getTargetsByDeadline() {
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                targetList.clear()
                dataSnapshot.children
                    .mapNotNull { it.getValue(Goal::class.java) }
                    .sortedBy { it.deadline }
                    .toCollection(targetList)
                contract.updateViewContent()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("some", "Error trying to get targets for ${databaseError.message}")
            }
        }
        targetsRef?.addListenerForSingleValueEvent(valueEventListener)
    }

    fun collectCompletedGoals() = targetList.filter { it.isComplete }

    fun collectCurrentGoals() = targetList.filter { !it.isComplete }
}
