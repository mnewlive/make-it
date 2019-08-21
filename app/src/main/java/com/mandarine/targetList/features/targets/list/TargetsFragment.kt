package com.mandarine.targetList.features.targets.list

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mandarine.targetList.R
import com.mandarine.targetList.common.addFragment
import com.mandarine.targetList.features.targets.edit.TargetEditFragment
import com.mandarine.targetList.interfaces.BaseDataSetContract
import com.mandarine.targetList.interfaces.ListItemClickListener
import com.mandarine.targetList.interfaces.SelectTargetViewContract

class TargetsFragment : Fragment(), ListItemClickListener, SelectTargetViewContract, BaseDataSetContract {

    private var recyclerView: RecyclerView? = null
    private val presenter = TargetsPresenter(this)
    private var adapter = TargetsAdapter(data = presenter.targetList, clickListener = this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_target_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.setInitialData()
        setupViews()
        updateListData()
    }

    override fun onListItemClick(itemIndex: Int, itemCode: String) {
        presenter.onListItemClick(adapter.getItem(itemIndex))
    }

    override fun showTarget(guid: String) {
        activity?.addFragment(TargetEditFragment.newInstance(guid))
    }

    override fun dataSetChanged() {
        updateListData()
        adapter.notifyDataSetChanged()
    }

    override fun updateViewContent() {
        recyclerView?.adapter = adapter
    }

    private fun updateListData() {
        if (presenter.firebaseUser == null) {
            Log.d("some", "loadLogInView")
        } else {
            presenter.getTargetsFromDb()
        }
    }

    private fun setupViews() {
        recyclerView = view?.findViewById(R.id.recyclerView)
        recyclerView?.layoutManager = LinearLayoutManager(activity)
    }
}
