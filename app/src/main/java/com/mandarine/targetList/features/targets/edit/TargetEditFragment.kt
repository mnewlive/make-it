package com.mandarine.targetList.features.targets.edit

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.mandarine.targetList.R
import com.mandarine.targetList.common.tools.setVisible
import com.mandarine.targetList.common.dialogs.showWarningDialog
import com.mandarine.targetList.interfaces.ActivityComponentsContract
import com.mandarine.targetList.widget.BaseFragment
import kotlinx.android.synthetic.main.fragment_target_add.*
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

class TargetEditFragment : BaseFragment(), View.OnClickListener, TargetEditContract{

    private val presenter = TargetEditPresenter(contract = this)
    private val safeArgs: TargetEditFragmentArgs by navArgs()
    private val targetGuid: String
        get() = safeArgs.guid
    private var parsedDate: LocalDate? = null
    private var spinnerPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        presenter.setInitialData(targetGuid)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activityComponents?.updateAppbarTitle(getString(presenter.getTitleResId()))
        return inflater.inflate(R.layout.fragment_target_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.fetchTarget()
        setupViews()
        updateActionViews()
    }

    override fun updateViewsContent(
        name: String?,
        description: String?,
        deadline: String?,
        priorityPosition: Int,
        isComplete: Boolean
    ) {
        nameEditText?.text = Editable.Factory.getInstance().newEditable(name)
        descriptionEditText?.text = Editable.Factory.getInstance().newEditable(description)
        dateView?.text = Editable.Factory.getInstance().newEditable(deadline)
        spinner.setSelection(priorityPosition)
        checkBoxView.isChecked = isComplete
    }

    override fun onClick(v: View?) {
        presenter.onViewClick(v?.id ?: return, targetGuid)
    }

    override fun editTarget(targetGuid: String) {
        val name = nameEditText?.text.toString().trim()
        val description = descriptionEditText?.text.toString().trim()
        val deadline = parsedDate?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli()
            ?: presenter.savedDeadline
        val priority = spinnerPosition
        val isComplete = checkBoxView.isChecked
        if (targetGuid.isEmpty()) presenter.addTarget(
            name = name,
            description = description,
            date = deadline,
            priority = priority,
            isComplete = isComplete
        )
        else presenter.updateTarget(
            name = name,
            description = description,
            date = deadline,
            priority = priority,
            isComplete = isComplete
        )
    }

    override fun deleteTarget() {
        presenter.deleteTarget()
    }

    override fun closeView() {
        findNavController().navigate(R.id.show_list)
    }

    override fun showWarningDialog() {
        activity?.showWarningDialog(description = getString(R.string.warning_description))
    }

    private fun setupViews() {
        showDatePickerDialog()
        setupPriority()
        addActionView?.setOnClickListener(this)
        deleteActionView?.setOnClickListener(this)
    }

    private fun updateActionViews() {
        if (targetGuid.isEmpty()) {
            titleView?.text = getString(R.string.add_goal)
            addActionView?.text = getString(R.string.add_goal)
            deleteActionView?.setVisible(show = false)
        } else {
            titleView?.text = getString(R.string.edit_goal)
            addActionView?.text = getString(R.string.edit_goal)
            deleteActionView?.setVisible(show = true)
        }
    }

    private fun setupPriority() {
        val priorityList = arrayOf(
            getString(R.string.priority_high),
            getString(R.string.priority_medium),
            getString(R.string.priority_low)
        )
        val adapter = ArrayAdapter(
            activity,
            android.R.layout.simple_spinner_item,
            priorityList
        )
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
                spinnerPosition = position
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun showDatePickerDialog() {
        val date = LocalDate.now(ZoneId.systemDefault())
        val currentYear = date.year
        val currentMonth = date.monthValue
        val currentDay = date.dayOfMonth

        val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)

        pickDate.setOnClickListener {
            val datePickDialog = DatePickerDialog(
                activity,
                R.style.DatePickerDialogTheme,
                DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
                    val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
                    val dateString = selectedDate.format(dateFormatter)
                    dateView.text = dateString
                    parsedDate = LocalDate.parse(dateString, dateFormatter)
                },
                currentYear,
                currentMonth - 1,
                currentDay
            )
            datePickDialog.show()

            datePickDialog.setOnCancelListener { dialog -> dialog.dismiss() }
        }
    }
}
