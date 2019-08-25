package com.mandarine.targetList.unitTests

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mandarine.targetList.R
import com.mandarine.targetList.features.root.MainActivityPresenter
import com.mandarine.targetList.features.root.MainActivityViewContract
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
class MainActivityPresenterTest {

    private val mockView = Mockito.mock(MainActivityViewContract::class.java)

    @Test
    @Throws(Exception::class)
    fun onNavigationItemSelectedTest() {
        val presenter = MainActivityPresenter(contract = mockView)

        assertTrue(presenter.onNavigationItemSelected(R.id.menu_targets))

        Mockito.verify(mockView).showListOfTarget()
        Mockito.clearInvocations(mockView)

        assertTrue(presenter.onNavigationItemSelected(R.id.menu_calendar))

        Mockito.verify(mockView).showCalendar()
        Mockito.clearInvocations(mockView)

        assertTrue(presenter.onNavigationItemSelected(R.id.menu_settings))

        Mockito.verify(mockView).showSettingsList()
        Mockito.clearInvocations(mockView)

        assertFalse(presenter.onNavigationItemSelected(-1))

        Mockito.verifyNoMoreInteractions(mockView)
    }
}
