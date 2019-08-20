package com.mandarine.targetList.features.targets.list

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.mandarine.targetList.R
import com.mandarine.targetList.common.addFragment
import com.mandarine.targetList.features.targets.edit.TargetEditFragment
import com.mandarine.targetList.interfaces.BaseDataSetContract
import com.mandarine.targetList.interfaces.ListItemClickListener
import com.mandarine.targetList.interfaces.SelectTargetViewContract
import com.mandarine.targetList.model.Target

class TargetsFragment : Fragment(), ListItemClickListener, SelectTargetViewContract, BaseDataSetContract {

    private var recyclerView: RecyclerView? = null
    private var targetList: ArrayList<Target> = ArrayList()
    private var adapter = TargetsAdapter(data = targetList, clickListener = this)
    private val presenter = TargetsPresenter(this)
    private var firebaseUser: FirebaseUser? = null
    private var databaseReference: DatabaseReference? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_target_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firebaseUser = FirebaseAuth.getInstance().currentUser
        databaseReference = FirebaseDatabase.getInstance().reference
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
        if (firebaseUser == null) {
            Log.d("some", "loadLogInView")
        } else {
            getTargetsFromDb()
        }
    }

    private fun setupViews() {
        recyclerView = view?.findViewById(R.id.recyclerView)
        recyclerView?.layoutManager = LinearLayoutManager(activity)
    }

    private fun getTargetsFromDb() {
        val uid = firebaseUser!!.uid
        val targetsRef = databaseReference?.child("targets")?.child("users")?.child(uid)?.child("targets")
        val valueEventListener = object : ValueEventListener {
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
        }
        targetsRef?.addListenerForSingleValueEvent(valueEventListener)
    }
}
