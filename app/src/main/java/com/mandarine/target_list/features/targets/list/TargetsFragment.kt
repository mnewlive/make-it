package com.mandarine.target_list.features.targets.list

import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.google.firebase.database.DatabaseReference
import com.mandarine.target_list.R
import com.mandarine.target_list.model.Target

class TargetsFragment : Fragment() {

    private var databaseReference: DatabaseReference? = null
    private var recyclerView: RecyclerView? = null
    private var targetList: ArrayList<Target> = ArrayList()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_target_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        updateListData()
    }

    private fun updateListData() {
        databaseReference = FirebaseDatabase.getInstance().getReference("targets")
        getTargetsFromDb()
    }

    private fun setupViews() {
        recyclerView = view?.findViewById(R.id.recyclerView)
        recyclerView?.layoutManager = LinearLayoutManager(activity)
    }

    private fun getTargetsFromDb() {
        databaseReference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (targetSnapshot in dataSnapshot.children) {
                    val target = targetSnapshot.getValue(Target::class.java)
                    target?.let { targetList.add(it) }
                }
                val recyclerViewAdapter = TargetsItemViewHolder(targetList)
                recyclerView?.adapter = recyclerViewAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("some", "Error trying to get targets for $databaseError")
            }
        })
    }
}