package com.example.lynx.view.settings.personal.helpsetting.components

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.widget.Toast

fun sendEmailView(context: Context, email: String, subject: String, body: String) {
    val intent = Intent(Intent.ACTION_SEND).apply {
        setType("message/rfc822")
        putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        putExtra(Intent.EXTRA_SUBJECT, subject)
        putExtra(Intent.EXTRA_TEXT, body)
    }

    try {
        context.startActivity(Intent.createChooser(intent, "Send email using..."))
    } catch (e: ActivityNotFoundException) {
        Toast.makeText(
            context,
            "No email clients installed",
            Toast.LENGTH_SHORT
        ).show()
    }
}