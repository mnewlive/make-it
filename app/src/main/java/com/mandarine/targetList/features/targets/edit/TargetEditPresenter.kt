package com.mandarine.targetList.features.targets.edit

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.mandarine.targetList.R
import com.mandarine.targetList.model.Goal
import java.text.SimpleDateFormat
import java.util.*

class TargetEditPresenter(private val contract: TargetEditContract) {

    private var databaseReference: DatabaseReference? = null
    private var firebaseUser: FirebaseUser? = null
    private var uid: String? = null
    private var targetGuid: String? = null
    private var targetsRef: DatabaseReference? = null
    private var query: Query? = null
    var savedDeadline: Long = 0L

    fun setInitialData(targetGuid: String) {
        this.targetGuid = targetGuid
        databaseReference = FirebaseDatabase.getInstance().reference
        firebaseUser = FirebaseAuth.getInstance().currentUser
        uid = firebaseUser?.uid
        targetsRef = databaseReference?.child("targets")?.child("users")
            ?.child(uid.toString())
            ?.child("targets")
        query = targetsRef?.orderByChild("guid")?.equalTo(targetGuid)
    }

    fun onViewClick(id: Int, targetGuid: String) {
        when (id) {
            R.id.addActionView -> contract.editTarget(targetGuid)
            R.id.deleteActionView -> contract.deleteTarget()
        }
    }

    fun fetchTarget() {
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (targetSnapshot in dataSnapshot.children) {
                    val target = targetSnapshot.getValue(Goal::class.java)
                    val name = target?.name ?: ""
                    val description = target?.description ?: ""
                    val deadline = target?.deadline ?: 0L
                    val priority = target?.priority ?: 0
                    val isComplete = target?.isComplete ?: false

                    savedDeadline = deadline

                    val date = Date(deadline)
                    val format = SimpleDateFormat("d MMMM, yyyy")

                    if (name.isEmpty()) Log.d("some", "nameIsEmpty")
                    else contract.updateViewsContent(
                        name = name,
                        description = description,
                        deadline = format.format(date),
                        priorityPosition = priority,
                        isComplete = isComplete
                    )
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("some", databaseError.message)
            }
        }
        query?.addListenerForSingleValueEvent(valueEventListener)
    }

    fun addTarget(name: String, description: String, date: Long, priority: Int, isComplete: Boolean) {
        when {
            name.isEmpty() -> contract.showWarningDialog()
            date == 0L -> contract.showWarningDialog()
            else -> {
                val id: String = databaseReference?.push()?.key.toString()
                val target = Goal(
                    guid = id,
                    name = name,
                    description = description,
                    deadline = date,
                    priority = priority,
                    isComplete = isComplete
                )
                targetsRef?.push()?.setValue(target)
                contract.closeView()
            }
        }
    }

    fun updateTarget(name: String, description: String, date: Long, priority: Int, isComplete: Boolean) {
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (targetSnapshot in dataSnapshot.children) {
                    targetSnapshot.child("name").ref.setValue(name)
                    targetSnapshot.child("description").ref.setValue(description)
                    targetSnapshot.child("deadline").ref.setValue(date)
                    targetSnapshot.child("priority").ref.setValue(priority)
                    targetSnapshot.child("complete").ref.setValue(isComplete)
                    contract.closeView()
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
                    contract.closeView()
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("some", databaseError.message)
            }
        }
        query?.addListenerForSingleValueEvent(valueEventListener)
    }
}
