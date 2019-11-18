package com.mandarine.targetList.features.targets.list

import android.content.Context
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.*
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.android.material.snackbar.Snackbar
import com.mandarine.targetList.R
import com.mandarine.targetList.common.SwipeToDeleteCallback
import com.mandarine.targetList.common.buildSnackbar
import com.mandarine.targetList.common.tools.setVisible
import com.mandarine.targetList.interfaces.ListItemClickListener
import com.mandarine.targetList.interfaces.SelectTargetViewContract
import com.mandarine.targetList.widget.BaseFragment
import kotlinx.android.synthetic.main.fragment_target_list.*

class TargetsFragment : BaseFragment(), ListItemClickListener, SelectTargetViewContract {

    private var recyclerView: RecyclerView? = null
    private val presenter = TargetsPresenter(this)
    private var adapter = TargetsAdapter(clickListener = this)
    private lateinit var swipeHandler: ItemTouchHelper.Callback
    private var snackbar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_target_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.setInitialData()
        setupViews()
        updateListData()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        swipeHandler = object : SwipeToDeleteCallback(context) {

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                presenter.removeListItem(viewHolder.adapterPosition)
            }
        }
    }

    override fun onListItemClick(itemIndex: Int, itemCode: String) {
        findNavController().navigate(
            TargetsFragmentDirections.nextAction(
                adapter.getItem(itemIndex)?.guid ?: ""
            )
        )
    }

    override fun updateViewContent() {
        adapter.data = presenter.targetList
        recyclerView?.adapter = adapter
        recyclerView?.setVisible(presenter.shouldShowContent())
        emptyView?.setVisible(presenter.shouldShowEmptyView())
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.filter, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.sort_by_priority -> presenter.getTargetsByPriority()
            R.id.sort_by_deadline -> presenter.getTargetsByDeadline()
        }
        return true
    }

    override fun onNetworkConnectionChanged(isConnected: Boolean) {
        if (isConnected) {
            snackbar?.dismiss()
        } else {
            snackbar = activity?.snackBarCoordinator?.buildSnackbar(
                message = getString(R.string.warning_no_internet_connections),
                backgroundResId = android.R.color.holo_orange_dark
            )
            snackbar?.show()
        }
    }

    private fun updateListData() {
        if (presenter.firebaseUser == null) {
            Log.d("some", "loadLogInView")
        } else {
            presenter.getTargetsFromDb()
        }
    }

    private fun setupViews() {
        fab?.setOnClickListener {
            findNavController().navigate(TargetsFragmentDirections.nextAction(""))
        }
        recyclerView = view?.findViewById(R.id.recyclerView)
        recyclerView?.layoutManager = LinearLayoutManager(activity)
        ItemTouchHelper(swipeHandler).attachToRecyclerView(recyclerView)
    }
}
