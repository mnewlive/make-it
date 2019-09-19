package com.mandarine.targetList.features.targets.edit

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.mandarine.targetList.R
import com.mandarine.targetList.constants.KEY_TARGET_GUID
import kotlinx.android.synthetic.main.fragment_target_add.*
import java.text.SimpleDateFormat
import java.util.*

class TargetEditFragment : Fragment(), View.OnClickListener, TargetEditContract {

    private val presenter = TargetEditPresenter(contract = this)
    private val targetGuid: String
        get() = arguments?.getString(KEY_TARGET_GUID, "") ?: ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.setInitialData(targetGuid)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_target_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.fetchTarget()
        setupViews()
    }

    override fun updateViewsContent(name: String?, description: String?, date: String?) {
        nameEditText?.text = Editable.Factory.getInstance().newEditable(name)
        descriptionEditText?.text = Editable.Factory.getInstance().newEditable(description)
        dateView?.text = Editable.Factory.getInstance().newEditable(date)
    }

    override fun onClick(v: View?) {
        presenter.onViewClick(v?.id ?: return, targetGuid)
    }

    override fun editTarget(targetGuid: String) {
        val name = nameEditText?.text.toString().trim()
        val description = descriptionEditText?.text.toString().trim()
        val date = dateView?.text.toString().trim()
        if (targetGuid.isEmpty()) presenter.addTarget(name, description, date)
        else presenter.updateTarget(name, description, date)
    }

    override fun deleteTarget() {
        presenter.deleteTarget()
    }

    private fun setupViews() {
        showDatePickerDialog()
        addActionView?.setOnClickListener(this)
        deleteActionView?.setOnClickListener(this)
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val currentYear = calendar.get(Calendar.YEAR)
        val currentMonth = calendar.get(Calendar.MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)

        pickDate.setOnClickListener {
            val datePickDialog = DatePickerDialog(
                activity,
                R.style.DatePickerDialogTheme,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    val dateFormat = SimpleDateFormat("d MMMM yyyy", Locale.US)
                    calendar.set(year, month, dayOfMonth)
                    val dateString = dateFormat.format(calendar.time)
                    dateView.text = dateString
                },
                currentYear,
                currentMonth,
                currentDay
            )
            datePickDialog.show()

            datePickDialog.setOnCancelListener { dialog -> dialog.dismiss() }
        }
    }

    companion object {
        fun newInstance(guid: String): TargetEditFragment =
            TargetEditFragment().apply {
                arguments = Bundle().apply { putString(KEY_TARGET_GUID, guid) }
            }
    }
}
