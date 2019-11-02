package com.mandarine.targetList.features.targets.list

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.mandarine.targetList.common.indexIsInBounds
import com.mandarine.targetList.interfaces.SelectTargetViewContract
import com.mandarine.targetList.model.Target

class TargetsPresenter(private val contract: SelectTargetViewContract) {

    var firebaseUser: FirebaseUser? = null
    var targetList: ArrayList<Target> = ArrayList()
    private var databaseReference: DatabaseReference? = null
    private var query: Query? = null
    private var targetsRef: DatabaseReference? = null
    private var uid: String? = null

    fun setInitialData() {
        firebaseUser = FirebaseAuth.getInstance().currentUser
        databaseReference = FirebaseDatabase.getInstance().reference
        uid = firebaseUser?.uid
        targetsRef = databaseReference?.child("targets")?.child("users")?.child(uid.toString())
            ?.child("targets")
    }

    fun shouldShowContent(): Boolean = targetList.isNotEmpty()

    fun shouldShowEmptyView(): Boolean = !shouldShowContent()

    fun getTargetsFromDb() {
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                targetList.clear()
                for (targetSnapshot in dataSnapshot.children) {
                    val target = targetSnapshot.getValue(Target::class.java)
                    if(target != null) {
                        targetList.add(target)
                    }
                }
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
                for (targetSnapshot in dataSnapshot.children) {
                    val target = targetSnapshot.getValue(Target::class.java)
                    target?.let { targetList.add(it) }
                }
                targetList.sortByDescending { it.priority }
                contract.updateViewContent()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("some", "Error trying to get targets for ${databaseError.message}")
            }
        }
        targetsRef?.addListenerForSingleValueEvent(valueEventListener)
    }

    fun removeListItem(position: Int) {
        if (targetList.indexIsInBounds(position)) {
            deleteTarget(targetList[position].guid)
            targetList.removeAt(position)
            contract.updateViewContent()
        }
    }

    private fun deleteTarget(guid: String) {
        query = targetsRef?.orderByChild("guid")?.equalTo(guid)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (targetSnapshot in dataSnapshot.children) {
                    targetSnapshot.ref.removeValue()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("some", databaseError.message)
            }
        }
        query?.addListenerForSingleValueEvent(valueEventListener)
    }
}
