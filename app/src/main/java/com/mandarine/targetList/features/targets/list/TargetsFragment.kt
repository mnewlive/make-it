package com.mandarine.targetList.features.targets.list

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
import com.mandarine.targetList.R
import com.mandarine.targetList.common.addFragment
import com.mandarine.targetList.features.targets.edit.TargetEditFragment
import com.mandarine.targetList.interfaces.BaseDataSetContract
import com.mandarine.targetList.interfaces.ListItemClickListener
import com.mandarine.targetList.interfaces.SelectTargetViewContract
import com.mandarine.targetList.model.Target

class TargetsFragment : Fragment(), ListItemClickListener, SelectTargetViewContract, BaseDataSetContract {

    private var databaseReference: DatabaseReference? = null
    private var recyclerView: RecyclerView? = null
    private var targetList: ArrayList<Target> = ArrayList()
    private var adapter = TargetsAdapter(data = targetList, clickListener = this)
    private val presenter = TargetsPresenter(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_target_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        updateListData()
    }

    override fun onListItemClick(itemIndex: Int, itemCode: String) {
        presenter.onListItemClick(adapter.getItem(itemIndex))
    }

    override fun showTarget(guid: String) {
        activity?.addFragment(TargetEditFragment.newInstance(guid))
    }

    // TODO: https://bitbucket.org/morozovvadim91/target-list/issues/1/show-the-current-list-of-targets
    override fun dataSetChanged() {
        updateListData()
        adapter.notifyDataSetChanged()
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
                recyclerView?.adapter = adapter
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("some", "Error trying to get targets for ${databaseError.message}")
            }
        })
    }
}