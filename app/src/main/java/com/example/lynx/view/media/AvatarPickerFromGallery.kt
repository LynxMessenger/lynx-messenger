package com.example.lynx.view.media


import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.example.lynx.viewmodel.ChannelViewModel
import com.example.lynx.viewmodel.GroupViewModel
import com.example.lynx.viewmodel.UserViewModel


@Composable
fun AvatarPickerFromGallery(
    navController: NavHostController,
    userViewModel: UserViewModel,
    groupViewModel: GroupViewModel,
    channelViewModel: ChannelViewModel,
    isForGroup: Boolean = false,
    isForChannel: Boolean = false
) {
    val context = LocalContext.current
    val imageUri = remember { mutableStateOf<Uri?>(null) }
    val hasPoppedBack = remember { mutableStateOf(false) }

    val cropImageLauncher = rememberLauncherForActivityResult(
        contract = CropImageContract()
    ) { result ->
        if (result.isSuccessful) {
            result.uriContent?.let { croppedUri ->
                imageUri.value = croppedUri

                when {
                    isForGroup -> groupViewModel.selectedAvatarUri = croppedUri
                    isForChannel -> channelViewModel.selectedAvatarUri = croppedUri
                    else -> {
                        userViewModel.selectedAvatarUri = croppedUri
                    }
                }
            }
        } else {
            navController.popBackStack()
        }
    }


    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val cropOptions = CropImageContractOptions(
                uri = it,
                cropImageOptions = CropImageOptions().apply {
                    cropShape = CropImageView.CropShape.OVAL
                    fixAspectRatio = true
                    aspectRatioX = 1
                    aspectRatioY = 1
                    guidelines = CropImageView.Guidelines.ON
                    activityBackgroundColor = Color.Black.toArgb()
                }
            )
            cropImageLauncher.launch(cropOptions)
        }
    }

    LaunchedEffect(Unit) {
        galleryLauncher.launch("image/*")
    }

    LaunchedEffect(imageUri.value) {
        if (imageUri.value != null && !hasPoppedBack.value) {
            hasPoppedBack.value = true
            navController.popBackStack()
        }
    }
}
