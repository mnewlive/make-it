package com.mandarine.targetList.features.root

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.mandarine.targetList.R
import com.mandarine.targetList.calendar.CalendarFragment
import com.mandarine.targetList.common.currentFragmentInContainer
import com.mandarine.targetList.common.replaceFragment
import com.mandarine.targetList.features.settings.SettingsListFragment
import com.mandarine.targetList.features.targets.edit.TargetEditFragment
import com.mandarine.targetList.features.targets.list.TargetsFragment
import com.mandarine.targetList.interfaces.OnBackPressListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainActivityViewContract, View.OnClickListener,
    BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var mAuthStateListener: FirebaseAuth.AuthStateListener
    private val presenter = MainActivityPresenter(contract = this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        signIn()
        setupViews()
    }

    override fun onBackPressed() {
        val onBackPressListener = currentFragmentInContainer() as? OnBackPressListener
        if (onBackPressListener?.onBackPress() != true) {
            super.onBackPressed()
        }
    }

    override fun onResume() {
        super.onResume()
        auth.addAuthStateListener(mAuthStateListener)
    }

    override fun onPause() {
        super.onPause()
        auth.removeAuthStateListener(mAuthStateListener)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        presenter.onActivityResult(requestCode, resultCode)
    }

    override fun cancelSignIn() {
        finish()
    }

    override fun onClick(v: View?) {
        presenter.onViewClick(v?.id ?: return)
    }

    override fun addTarget() {
        replaceFragment(TargetEditFragment())
    }

    override fun onPostResume() {
        super.onPostResume()
        replaceFragment(TargetsFragment())
    }

    override fun showListOfTarget() {
        replaceFragment(TargetsFragment())
    }

    override fun showSettingsList() {
        replaceFragment(SettingsListFragment())
    }

    override fun showCalendar() {
        replaceFragment(CalendarFragment())
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean =
        presenter.onNavigationItemSelected(item.itemId)

    private fun setupViews() {
        fab.setOnClickListener(this)
        bottomNavigationView?.setOnNavigationItemSelectedListener(this)
    }

    private fun signIn() {
        auth = FirebaseAuth.getInstance()
        mAuthStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            presenter.signIn(activity = this, user = firebaseAuth.currentUser)
        }
    }
}
