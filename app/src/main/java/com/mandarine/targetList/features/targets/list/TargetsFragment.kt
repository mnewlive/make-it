package com.mandarine.targetList.features.targets.list

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.navigation.fragment.findNavController
import com.mandarine.targetList.R
import com.mandarine.targetList.interfaces.ListItemClickListener
import com.mandarine.targetList.interfaces.SelectTargetViewContract
import com.mandarine.targetList.widget.BaseFragment
import kotlinx.android.synthetic.main.fragment_target_list.*

class TargetsFragment : BaseFragment(), ListItemClickListener, SelectTargetViewContract {

    private val presenter = TargetsPresenter(this)
    private var targetsAdapter = TargetsAdapter(clickListener = this)
    private var completedTargetsAdapter = TargetsAdapter(clickListener = this)
    private var contentPagerAdapter: ContentPagerAdapter? = null

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

    override fun onListItemClick(itemIndex: Int, itemCode: String) {
        findNavController().navigate(
            TargetsFragmentDirections.nextAction(
                targetsAdapter.getItem(itemIndex)?.guid ?: ""
            )
        )
    }

    override fun updateViewContent() {
        targetsAdapter.data = presenter.targetList
        completedTargetsAdapter.data = presenter.completedTargets
        contentPagerAdapter = activity?.let { ContentPagerAdapter(it, targetsAdapter, completedTargetsAdapter) }
        contentViewPager.adapter = contentPagerAdapter
        tabLayout.setupWithViewPager(contentViewPager)
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
    }
}
