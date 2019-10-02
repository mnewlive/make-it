package com.mandarine.targetList.features.root

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.mandarine.targetList.R

import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainActivityViewContract {

    private lateinit var auth: FirebaseAuth
    private lateinit var mAuthStateListener: FirebaseAuth.AuthStateListener
    private val presenter = MainActivityPresenter(contract = this)

    private lateinit var appBarConfiguration : AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        signIn()

        val host: NavHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment? ?: return

        // Set up Action Bar
        val navController = host.navController

        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupBottomNavMenu(navController)
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

    private fun setupBottomNavMenu(navController: NavController) {
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation_view)
        bottomNavigationView?.setupWithNavController(navController)
    }

    private fun signIn() {
        auth = FirebaseAuth.getInstance()
        mAuthStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
            presenter.signIn(activity = this, user = firebaseAuth.currentUser)
        }
    }
}
