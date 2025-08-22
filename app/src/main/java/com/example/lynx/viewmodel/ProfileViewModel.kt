package com.example.lynx.viewmodel

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.ViewModel
import coil.compose.rememberAsyncImagePainter
import com.example.lynx.R
import com.example.lynx.model.Channel
import com.example.lynx.model.Group
import com.example.lynx.model.User

class ProfileViewModel : ViewModel() {
    val selectedAvatarUri = mutableStateOf<Uri?>(null)

    @Composable
    fun avatarCheck(obj: Any?): Painter {
        val overrideUri = selectedAvatarUri.value

        return when (obj) {
            is User -> {
                if (!obj.avatarUri.isNullOrBlank()) {
                    rememberAsyncImagePainter(obj.avatarUri)
                } else {
                    painterResource(id = R.drawable.avatar)
                }
            }

            is Group -> {
                when {
                    overrideUri != null -> rememberAsyncImagePainter(overrideUri)
                    !obj.avatarUri.isNullOrBlank() -> rememberAsyncImagePainter(obj.avatarUri)
                    else -> painterResource(id = R.drawable.avatar)
                }
            }

            is Channel -> {
                if (!obj.avatarUri.isNullOrBlank()) {
                    rememberAsyncImagePainter(obj.avatarUri)
                } else {
                    painterResource(id = R.drawable.avatar)
                }
            }

            else -> painterResource(id = R.drawable.avatar)
        }
    }
}