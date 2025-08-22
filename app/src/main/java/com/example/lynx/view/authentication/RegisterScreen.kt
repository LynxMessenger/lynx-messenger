package com.example.lynx.view.authentication

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.lynx.R
import com.example.lynx.view.authentication.components.RegisterView
import com.example.lynx.viewmodel.AuthorizationViewModel

@Composable
fun RegisterScreen(
    navController: NavHostController,
    authorizationViewModel: AuthorizationViewModel
){
    Box(modifier = Modifier.fillMaxSize().background(colorResource(R.color.black))) {

        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.statusBarsPadding())

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(35.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_backarrow),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 16.dp)
                        .size(20.dp)
                        .clickable { navController.navigate("LoginScreen") },
                    contentScale = ContentScale.Fit
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(110.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Register",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                    color = colorResource(R.color.neon_green),
                    modifier = Modifier.padding(bottom = 32.dp)
                )
            }
            RegisterView(
                navController,
                authorizationViewModel
            )
        }
    }
}