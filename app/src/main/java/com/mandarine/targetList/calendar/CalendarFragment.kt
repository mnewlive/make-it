package com.mandarine.targetList.calendar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.mandarine.targetList.R
import kotlinx.android.synthetic.main.fragment_calendar.*

class CalendarFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        var name: String? = null
        val uid = FirebaseAuth.getInstance().currentUser!!.uid
        val rootRef = FirebaseDatabase.getInstance().reference
        val targetsRef = rootRef.child("targets").child("users").child(uid).child("targets")
        val query = targetsRef.orderByChild("date")
        val valueEventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (ds in dataSnapshot.children) {
                    name = ds.child("name").getValue(String::class.java)
                    Log.d("some", name)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("some", databaseError.getMessage()) //Don't ignore errors!
            }
        }
        query.addListenerForSingleValueEvent(valueEventListener)

        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
//            dateView.text = "Selected date is " + dayOfMonth + "/" + (month + 1) + "/" + year
            dateView.text = name
            Log.d("some", name)
        }
    }
}
