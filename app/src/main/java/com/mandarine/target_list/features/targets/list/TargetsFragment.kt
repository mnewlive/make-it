package com.mandarine.target_list.features.targets.list

import android.app.Fragment
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.ValueEventListener
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.google.firebase.database.DatabaseReference
import com.mandarine.target_list.R
import com.mandarine.target_list.interfaces.ListItemClickListener
import com.mandarine.target_list.model.Target

class TargetsFragment : Fragment(), ListItemClickListener {

    private var databaseReference: DatabaseReference? = null
    private var recyclerView: RecyclerView? = null
    private var targetList: ArrayList<Target> = ArrayList()
    private var adapter = TargetsAdapter(data = targetList, clickListener = this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_target_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        updateListData()
    }

    override fun onListItemClick(itemIndex: Int, itemCode: String) {
        Log.d("some", "onListItemClick")
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
                val recyclerViewAdapter = adapter
                recyclerView?.adapter = recyclerViewAdapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("some", "Error trying to get targets for $databaseError")
            }
        })
    }
}