package com.mandarine.targetList.features.targets.list

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.mandarine.targetList.interfaces.SelectTargetViewContract
import com.mandarine.targetList.model.Target

class TargetsPresenter(private val contract: SelectTargetViewContract) {

    var firebaseUser: FirebaseUser? = null
    var targetList: ArrayList<Target> = ArrayList()
    private var databaseReference: DatabaseReference? = null

    fun setInitialData() {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        databaseReference = FirebaseDatabase.getInstance().reference
    }

    fun onListItemClick(targetGuid: String) {
        contract.showTarget(targetGuid)
    }

    fun shouldShowContent(): Boolean = targetList.isNotEmpty()

    fun shouldShowEpmtyView(): Boolean = !shouldShowContent()

    fun getTargetsFromDb() {
        val uid = firebaseUser!!.uid
        val targetsRef = databaseReference?.child("targets")?.child("users")?.child(uid)?.child("targets")
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                targetList.clear()
                for (targetSnapshot in dataSnapshot.children) {
                    val target = targetSnapshot.getValue(Target::class.java)
                    target?.let { targetList.add(it) }
                }
                contract.updateViewContent()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("some", "Error trying to get targets for ${databaseError.message}")
            }
        }
        targetsRef?.addListenerForSingleValueEvent(valueEventListener)
    }
}

