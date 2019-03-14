package com.mandarine.target_list

import android.app.Activity
import android.util.Log
import com.mandarine.target_list.constants.RC_SIGN_IN

//TODO: Test
class MainActivityPresenter(private val contract: MainActivityViewContract?) {

    //https://github.com/firebase/FirebaseUI-Android/blob/master/auth/README.md#response-codes
    fun onActivityResult(requestCode: Int, resultCode: Int) {
        if (requestCode == RC_SIGN_IN) {
            if (resultCode == Activity.RESULT_OK) {
                Log.d("some", "Signed in")
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.d("some", "Sign in canceled")
                contract?.cancelSignIn()
            }
        }
    }

    //https://github.com/firebase/FirebaseUI-Android/blob/master/auth/README.md#sign-out
    fun onOptionsItemSelected(itemId: Int) {
        when (itemId) {
            R.id.action_settings -> Log.d("some", "clickOnActionSettings")
            R.id.sign_out -> contract?.signOut()
        }
    }
}