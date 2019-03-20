package com.mandarine.target_list.features.targets.edit

import android.app.Fragment
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.firebase.database.*
import com.mandarine.target_list.R
import com.mandarine.target_list.model.Target

class TargetEditFragment : Fragment() {

    private var nameEditText: TextInputEditText? = null
    private var descriptionEditText: TextInputEditText? = null
    private var button: Button? = null
    private var databaseReference: DatabaseReference? = null
    private var presenter = TargetEditPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getString("guid", "")
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_target_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        databaseReference = FirebaseDatabase.getInstance().getReference("targets")
        setupViews()
        fetchData(arguments?.getString("guid", "") ?: "")
    }

    private fun setupViews() {
        nameEditText = view?.findViewById(R.id.nameEditText)
        descriptionEditText = view?.findViewById(R.id.descriptionEditText)

        button = view?.findViewById(R.id.addNote)
        button?.setOnClickListener { addTarget() }
    }

    private fun addTarget() {
        val name = nameEditText?.text.toString().trim()
        val description = descriptionEditText?.text.toString().trim()

        if (!TextUtils.isEmpty(name)) {
            val id: String = databaseReference?.push()?.key.toString()
            val target = Target(guid = id, name = name, description = description)
            databaseReference?.child(id)?.setValue(target)
        } else Log.d("some", "Enter a name")
    }

    private fun fetchData(guid: String) {
        // Attach a listener to read the data at the target id
        databaseReference?.child(guid)?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val data = dataSnapshot.value as HashMap<String, String>
                val name = data["name"]
                val description = data["description"]

                updateViewsContent(name, description)
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.d("some", "onCancelled")
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
                arguments = Bundle().apply { putString("guid", guid) }
                Log.d("some", "argumeeents $arguments")
            }
    }
}