package com.example.lynx.view.settings.personal.notificationsetting

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.lynx.R

@Composable
fun NotificationsSettingScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.black)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "Notifications Setting Screen",
                fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                color = colorResource(R.color.white),
                modifier = Modifier.padding(16.dp)
            )
            Button(onClick = {navController.navigate("SettingScreen")},
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.neon_green))){
                Text(
                    text = "Go to setting",
                    fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                    color = colorResource(R.color.white)
                )
            }
        }
    }
}