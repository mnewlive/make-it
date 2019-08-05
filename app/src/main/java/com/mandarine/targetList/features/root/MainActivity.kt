package com.mandarine.targetList.features.root

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.mandarine.targetList.R
import com.mandarine.targetList.common.currentFragmentInContainer
import com.mandarine.targetList.common.replaceFragment
import com.mandarine.targetList.features.targets.edit.TargetEditFragment
import com.mandarine.targetList.features.targets.list.TargetsFragment
import com.mandarine.targetList.interfaces.OnBackPressListener
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainActivityViewContract, View.OnClickListener {

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

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        updateUI(currentUser)
    }

    override fun onResume() {
        super.onResume()
        auth.addAuthStateListener(mAuthStateListener)
    }

    override fun onPause() {
        super.onPause()
        auth.removeAuthStateListener(mAuthStateListener)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        presenter.onOptionsItemSelected(item.itemId)
        return super.onOptionsItemSelected(item)
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

    override fun signOut(): Boolean {
        AuthUI.getInstance().signOut(this)
        return true
    }

    private fun setupViews() {
        fab.setOnClickListener(this)
    }

    private fun signIn() {
        auth = FirebaseAuth.getInstance()
        mAuthStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            presenter.signIn(activity = this, user = firebaseAuth.currentUser)
        }
    }

    private fun updateUI(currentUser: FirebaseUser?) {
//        titleView?.text = currentUser?.displayName
    }
}
