package com.mandarine.targetList.common

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.fragment.app.FragmentActivity

const val SUPPORT_EMAIL_LINK = "morozovvadim91@gmail.com"

fun FragmentActivity.runEmailApp() {
    try {
        this.startActivityForResult(
            Intent(Intent.ACTION_SENDTO)
                .apply { data = Uri.parse("mailto:${SUPPORT_EMAIL_LINK}") }, 0
        )
    } catch (ignored: IllegalStateException) {
    } catch (ignored: ActivityNotFoundException) {
    } catch (e: Exception) {
    }
}
