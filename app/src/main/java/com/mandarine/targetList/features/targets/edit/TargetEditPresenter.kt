package com.mandarine.targetList.features.targets.edit

import android.text.TextUtils
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.mandarine.targetList.R
import com.mandarine.targetList.model.Target

class TargetEditPresenter(private val contract: TargetEditContract) {

    private var databaseReference: DatabaseReference? = null
    private var firebaseUser: FirebaseUser? = null
    private var uid: String? = null
    private var targetGuid: String? = null
    private var targetsRef: DatabaseReference? = null
    private var query: Query? = null

    fun setInitialData(targetGuid: String) {
        this.targetGuid = targetGuid
        databaseReference = FirebaseDatabase.getInstance().reference
        firebaseUser = FirebaseAuth.getInstance().currentUser
        uid = firebaseUser!!.uid
        targetsRef = databaseReference!!.child("targets")
            .child("users").child(uid.toString()).child("targets")
        query = targetsRef?.orderByChild("guid")?.equalTo(targetGuid)
    }

    fun onViewClick(id: Int, targetGuid: String) {
        when (id) {
            R.id.addNote -> contract.editTarget(targetGuid)
            R.id.deleteButton -> contract.deleteTarget()
        }
    }

    fun fetchTarget() {
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (targetSnapshot in dataSnapshot.children) {
                    val target = targetSnapshot.getValue(Target::class.java)
                    val name = target?.name ?: ""
                    val description = target?.description ?: ""

                    if (name.isEmpty()) Log.d("some", "nameIsEmpty")
                    else contract.updateViewsContent(name = name, description = description)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("some", databaseError.message)
            }
        }
        query?.addListenerForSingleValueEvent(valueEventListener)
    }

    fun addTarget(name: String, description: String) {
        if (!TextUtils.isEmpty(name)) {
            val id: String = databaseReference?.push()?.key.toString()
            val target = Target(guid = id, name = name, description = description)
            targetsRef?.push()?.setValue(target)
        } else Log.d("some", "Enter a name")
    }

    fun updateTarget(name: String, description: String) {
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (targetSnapshot in dataSnapshot.children) {
                    targetSnapshot.child("name").ref.setValue(name)
                    targetSnapshot.child("description").ref.setValue(description)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("some", databaseError.message)
            }
        }
        query?.addListenerForSingleValueEvent(valueEventListener)
    }

    fun deleteTarget() {
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
