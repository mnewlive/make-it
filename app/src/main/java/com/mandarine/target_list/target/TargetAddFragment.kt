package com.mandarine.target_list.target

import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.mandarine.target_list.R

class TargetAddFragment : Fragment() {

    private var nameEditText: TextInputEditText? = null
    private var descriptionEditText: TextInputEditText? = null
    private var button: Button? = null
    private var databaseReference: DatabaseReference? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_target_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        databaseReference = FirebaseDatabase.getInstance().getReference("targets")
        setupViews()
    }

    private fun setupViews() {
        nameEditText = view?.findViewById<TextInputEditText>(R.id.nameEditText)
        descriptionEditText = view?.findViewById<TextInputEditText>(R.id.descriptionEditText)

        button = view?.findViewById<Button>(R.id.addNote)
        button?.setOnClickListener { checkClick() }
    }

    private fun checkClick() {
        val name = nameEditText?.text.toString().trim()
        val description = descriptionEditText?.text.toString().trim()

        if (!TextUtils.isEmpty(name)) {
            val id: String = databaseReference?.push()?.key.toString()
            val target = Target(guid = id, name = name, description = description)
            databaseReference?.child(id)?.setValue(target)
        } else Log.d("some", "Enter a name")
    }


}