package com.mandarine.targetList.features.targets.edit

import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.app.Fragment
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.mandarine.targetList.R
import com.mandarine.targetList.constants.KEY_TARGET_GUID

class TargetEditFragment : Fragment(), View.OnClickListener, TargetEditContract {

    private var nameEditText: TextInputEditText? = null
    private var descriptionEditText: TextInputEditText? = null
    private var button: Button? = null
    private var deleteButton: Button? = null

    private val presenter = TargetEditPresenter(contract = this)
    private val targetGuid: String
        get() = arguments?.getString(KEY_TARGET_GUID, "") ?: ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.setInitialData(targetGuid)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_target_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.fetchTarget()
        setupViews()
    }

    override fun updateViewsContent(name: String?, description: String?) {
        nameEditText?.text = Editable.Factory.getInstance().newEditable(name)
        descriptionEditText?.text = Editable.Factory.getInstance().newEditable(description)
    }

    override fun onClick(v: View?) {
        presenter.onViewClick(v?.id ?: return, targetGuid)
    }

    override fun editTarget(targetGuid: String) {
        val name = nameEditText?.text.toString().trim()
        val description = descriptionEditText?.text.toString().trim()
        if (targetGuid.isEmpty()) presenter.addTarget(name, description)
        else presenter.updateTarget(name, description)
    }

    override fun deleteTarget() {
        presenter.deleteTarget()
    }

    private fun setupViews() {
        nameEditText = view?.findViewById(R.id.nameEditText)
        descriptionEditText = view?.findViewById(R.id.descriptionEditText)
        button = view?.findViewById(R.id.addNote)
        deleteButton = view?.findViewById(R.id.deleteButton)

        button?.setOnClickListener(this)
        deleteButton?.setOnClickListener(this)
    }

    companion object {
        fun newInstance(guid: String): TargetEditFragment =
            TargetEditFragment().apply {
                arguments = Bundle().apply { putString(KEY_TARGET_GUID, guid) }
            }
    }
}
