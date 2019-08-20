package com.mandarine.targetList.features.targets.edit

import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mandarine.targetList.R
import com.mandarine.targetList.constants.KEY_TARGET_GUID
import com.mandarine.targetList.model.Target

class TargetEditFragment : Fragment(), View.OnClickListener, TargetEditContract {

    private var nameEditText: TextInputEditText? = null
    private var descriptionEditText: TextInputEditText? = null
    private var button: Button? = null
    private var deleteButton: Button? = null
    private var databaseReference: DatabaseReference? = null
    private var firebaseUser: FirebaseUser? = null
    private val presenter = TargetEditPresenter(contract = this)
    private val targetGuid: String
        get() = arguments?.getString(KEY_TARGET_GUID, "") ?: ""
    private var uid: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_target_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        databaseReference = FirebaseDatabase.getInstance().reference
        firebaseUser = FirebaseAuth.getInstance().currentUser
        uid = firebaseUser!!.uid
        setupViews()
        fetchTarget(guid = targetGuid)
    }

    override fun onClick(v: View?) {
        presenter.onViewClick(v?.id ?: return, targetGuid)
    }

    override fun editTarget(targetGuid: String) {
        val name = nameEditText?.text.toString().trim()
        val description = descriptionEditText?.text.toString().trim()
        if (targetGuid.isEmpty()) addTarget(name, description) else updateTarget(name, description)
    }

    override fun deleteTarget(targetGuid: String) {
        val targetsRef = databaseReference?.child("targets")?.child("users")?.child(uid.toString())?.child("targets")
        val query = targetsRef?.orderByChild("guid")?.equalTo(targetGuid)
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

    private fun setupViews() {
        nameEditText = view?.findViewById(R.id.nameEditText)
        descriptionEditText = view?.findViewById(R.id.descriptionEditText)
        button = view?.findViewById(R.id.addNote)
        deleteButton = view?.findViewById(R.id.deleteButton)

        button?.setOnClickListener(this)
        deleteButton?.setOnClickListener(this)
    }

    private fun addTarget(name: String, description: String) {
        if (!TextUtils.isEmpty(name)) {
            val id: String = databaseReference?.push()?.key.toString()
            val target = Target(guid = id, name = name, description = description)
            databaseReference?.child("targets")?.child("users")
                ?.child(uid.toString())?.child("targets")?.push()?.setValue(target)
        } else Log.d("some", "Enter a name")
    }

    private fun updateTarget(name: String, description: String) {
        val targetsRef = databaseReference?.child("targets")?.child("users")?.child(uid.toString())?.child("targets")
        val query = targetsRef?.orderByChild("guid")?.equalTo(targetGuid)
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

    private fun fetchTarget(guid: String) {
        val targetsRef = databaseReference!!.child("targets").child("users").child(uid.toString()).child("targets")
        val query = targetsRef.orderByChild("guid").equalTo(guid)
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (targetSnapshot in dataSnapshot.children) {
                    val target = targetSnapshot.getValue(Target::class.java)

                    val name = target?.name ?: ""
                    val description = target?.description ?: ""

                    if (name.isEmpty()) Log.d("some", "nameIsEmpty")
                    else updateViewsContent(name = name, description = description)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("some", databaseError.message)
            }
        }
        query.addListenerForSingleValueEvent(valueEventListener)
    }

    private fun updateViewsContent(name: String?, description: String?) {
        nameEditText?.text = Editable.Factory.getInstance().newEditable(name)
        descriptionEditText?.text = Editable.Factory.getInstance().newEditable(description)
    }

    companion object {
        fun newInstance(guid: String): TargetEditFragment =
            TargetEditFragment().apply {
                arguments = Bundle().apply { putString(KEY_TARGET_GUID, guid) }
            }
    }
}
