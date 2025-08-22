package com.example.lynx.view.settings.personal.helpsetting

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import androidx.navigation.NavHostController
import com.example.lynx.R
import com.example.lynx.view.settings.personal.helpsetting.components.sendEmailView

@Composable
fun HelpScreen(navController: NavHostController) {
    val context = LocalContext.current
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
                            .clickable { navController.navigate("SettingScreen") },
                        contentScale = ContentScale.Fit
                    )

                    Text(
                        text = "feedback",
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
                Column(modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Contact us!",
                        modifier = Modifier.padding(top = 150.dp),
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                        fontWeight = FontWeight.W900,
                        fontSize = 20.sp,
                        color = colorResource(id = R.color.neon_green),
                    )
                    Spacer(modifier = Modifier.padding(top = 20.dp))
                    Button(
                        onClick = {
                            sendEmailView(
                                context = context,
                                email = "lynxfeedback@gmail.com",
                                subject = "App Support Request",
                                body = "Dear Support Team,\n\nI need help with..."
                            )
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.neon_green)
                        ),
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Email Us",
                            fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                            color = colorResource(R.color.black),
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}