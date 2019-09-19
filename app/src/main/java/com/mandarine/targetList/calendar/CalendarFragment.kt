package com.mandarine.targetList.calendar

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mandarine.targetList.R
import kotlinx.android.synthetic.main.fragment_calendar.*

class CalendarFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setupViews() {
        calendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            dateView.text = "Selected date is " + dayOfMonth + "/" + (month + 1) + "/" + year
        }
    }
}
