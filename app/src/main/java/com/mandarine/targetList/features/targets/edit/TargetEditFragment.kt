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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.DatabaseError
import com.mandarine.targetList.R
import com.mandarine.targetList.constants.KEY_TARGET_GUID
import com.mandarine.targetList.model.Target

class TargetEditFragment : Fragment(), View.OnClickListener, TargetEditContract {

    private var nameEditText: TextInputEditText? = null
    private var descriptionEditText: TextInputEditText? = null
    private var button: Button? = null
    private var deleteButton: Button? = null
    private var databaseReference: DatabaseReference? = null
    private val presenter = TargetEditPresenter(contract = this)
    private val targetGuid: String
        get() = arguments?.getString(KEY_TARGET_GUID, "") ?: ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_target_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        databaseReference = FirebaseDatabase.getInstance().getReference("targets")
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
        databaseReference?.child(targetGuid)?.removeValue()
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
            databaseReference?.child(id)?.setValue(target)
        } else Log.d("some", "Enter a name")
    }

    private fun updateTarget(name: String, description: String) {
        val map = mapOf("name" to name, "description" to description)
        databaseReference?.child(targetGuid)?.updateChildren(map)
    }

    private fun fetchTarget(guid: String) {
        // Attach a listener to read the data at the target id
        databaseReference?.child(guid)?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.value as? HashMap<String, String>?
                val name = data?.get("name") ?: ""
                val description = data?.get("description") ?: ""

                if (name.isEmpty()) Log.d("some", "nameIsEmpty")
                else updateViewsContent(name = name, description = description)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("some", databaseError.message)
            }
        })
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