package com.example.lynx.view.media

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lynx.R


@Composable
fun ImageViewerScreen(imageUri: Uri, onBack: () -> Unit) {
    val topPadding = WindowInsets.statusBars.asPaddingValues().calculateTopPadding()
    val context = LocalContext.current
    val imageBitmap by remember(imageUri) {
        mutableStateOf(
            try {
                val stream = context.contentResolver.openInputStream(imageUri)
                val bitmap = BitmapFactory.decodeStream(stream)
                stream?.close()
                bitmap?.asImageBitmap()
            } catch (e: Exception) {
                null
            }
        )
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.black))
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                Spacer(modifier = Modifier.statusBarsPadding())

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(35.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_backarrow),
                        contentDescription = null,
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .size(20.dp)
                            .clickable { onBack() },
                        contentScale = ContentScale.Fit
                    )

                    Text(
                        text = "Image",
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 36.dp),
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                        fontWeight = FontWeight.W600,
                        fontSize = 20.sp,
                        color = colorResource(id = R.color.neon_green),
                    )
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = topPadding, bottom = 35.dp),
                    contentAlignment = Alignment.Center
                )
                {
                    imageBitmap?.let { bitmap ->
                        Image(
                            bitmap = bitmap,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth()
                                .fillMaxHeight()
                                .padding(horizontal = 16.dp)
                                .align(Alignment.Center),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }
        }
    }
}