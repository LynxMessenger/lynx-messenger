package com.example.lynx.session

import android.content.Context
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.edit

object SessionManager {
    private const val PREFS_NAME = "session_prefs"
    private const val KEY_USER_ID = "current_user_id"

    private val _isLoggedIn = mutableStateOf(false)
    val isLoggedIn: State<Boolean> = _isLoggedIn

    var currentUserId: String? = null
        private set

    fun init(context: Context) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        currentUserId = prefs.getString(KEY_USER_ID, null)
        _isLoggedIn.value = currentUserId != null
    }

    fun login(context: Context, uuid: String) {
        currentUserId = uuid
        _isLoggedIn.value = true

        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit { putString(KEY_USER_ID, uuid) }
    }

    fun logout(context: Context) {
        currentUserId = null
        _isLoggedIn.value = false

        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit { remove(KEY_USER_ID) }
    }
    fun setCurrentUserId(UUID: String){
        currentUserId = UUID
    }
}