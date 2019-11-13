package com.mandarine.targetList.features.root

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.navigation.fragment.findNavController
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.mandarine.targetList.R
import kotlinx.android.synthetic.main.fragment_main.*
import java.util.*

class MainFragment : Fragment(), View.OnClickListener {

    private lateinit var auth: FirebaseAuth
    private lateinit var mAuthStateListener: FirebaseAuth.AuthStateListener


    companion object {
        const val TAG = "MainFragment"
        const val SIGN_IN_RESULT_CODE = 1001
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d("some", "onViewCreated")
        signIn()
        setupViews()
    }

    override fun onResume() {
        super.onResume()
        auth.addAuthStateListener(mAuthStateListener)
    }

    override fun onPause() {
        super.onPause()
        auth.removeAuthStateListener(mAuthStateListener)
    }

    private fun signIn() {
        auth = FirebaseAuth.getInstance()
        mAuthStateListener = FirebaseAuth.AuthStateListener { firebaseAuth ->
//            presenter.signIn(activity = this, user = firebaseAuth.currentUser)
            activity?.let { signInApp(it, firebaseAuth.currentUser) }
        }
    }

    fun signInApp(activity: FragmentActivity, user: FirebaseUser?) {
        if (user != null) {
            Log.d("some", "user not null")
            findNavController().navigate(R.id.show_goals)
        } else {
            Log.d("some", "null")
        }
}

    private fun setupViews() {
        auth_button.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (v?.id == R.id.auth_button) launchSignInFlow()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SIGN_IN_RESULT_CODE) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                // User successfully signed in
                Log.i(TAG, "Successfully signed in user ${FirebaseAuth.getInstance().currentUser?.displayName}!")
            } else {
                // Sign in failed. If response is null the user canceled the
                // sign-in flow using the back button. Otherwise check
                // response.getError().getErrorCode() and handle the error.
                Log.i(TAG, "Sign in unsuccessful ${response?.error?.errorCode}")
            }
        }
    }

    private fun launchSignInFlow() {
        // Give users the option to sign in / register with their email
        // If users choose to register with their email,
        // they will need to create a password as well
        val providers = arrayListOf(
            AuthUI.IdpConfig.EmailBuilder().build(), AuthUI.IdpConfig.GoogleBuilder().build()
            //
        )

        // Create and launch sign-in intent.
        // We listen to the response of this activity with the
        // SIGN_IN_RESULT_CODE code
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
             MainFragment.SIGN_IN_RESULT_CODE
        )
    }
}
