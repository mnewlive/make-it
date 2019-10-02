package com.mandarine.targetList.features.targets.edit

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.mandarine.targetList.R
import com.mandarine.targetList.common.setVisible
import com.mandarine.targetList.constants.KEY_TARGET_GUID
import kotlinx.android.synthetic.main.fragment_target_add.*
import org.threeten.bp.LocalDate
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset
import org.threeten.bp.format.DateTimeFormatter
import org.threeten.bp.format.FormatStyle

class TargetEditFragment : Fragment(), View.OnClickListener, TargetEditContract {

    private val presenter = TargetEditPresenter(contract = this)
    private val targetGuid: String
        get() = arguments?.getString(KEY_TARGET_GUID, "") ?: ""
    private var parsedDate: LocalDate? = null

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

    private fun updateActionViews() {
        if (targetGuid.isEmpty()) {
            addActionView?.text = getString(R.string.add_goal)
            deleteActionView?.setVisible(show = false)
        } else {
            addActionView?.text = getString(R.string.edit_goal)
            deleteActionView?.setVisible(show = true)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.fetchTarget()
        setupViews()
        updateActionViews()
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
        val date = parsedDate?.atStartOfDay()?.toInstant(ZoneOffset.UTC)?.toEpochMilli() ?: 0L
        if (targetGuid.isEmpty()) presenter.addTarget(name, description, date)
        else presenter.updateTarget(name, description, date)
    }

    override fun deleteTarget() {
        presenter.deleteTarget()
    }

//  TODO: https://github.com/mnewlive/make-it/issues/8 Update TargetsFragment when u add/updated goal
    override fun closeView() {
        findNavController().navigate(R.id.show_list)
    }

    private fun setupViews() {
        showDatePickerDialog()
        addActionView?.setOnClickListener(this)
        deleteActionView?.setOnClickListener(this)
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
