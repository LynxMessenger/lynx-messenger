package com.example.lynx.view.authentication.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.lynx.R
import com.example.lynx.session.SessionManager
import com.example.lynx.viewmodel.AuthorizationViewModel

@Composable
fun RegisterView(
    navController: NavHostController,
    authorizationViewModel: AuthorizationViewModel
){
    val context = LocalContext.current

    val userId = remember { mutableStateOf("") }
    val nickname = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }

    val idIsBusy = remember { mutableStateOf(false) }
    val loginError = remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .border(1.dp, colorResource(R.color.neon_green), shape = RoundedCornerShape(8.dp))
                .padding(horizontal = 5.dp, vertical = 5.dp)
                .widthIn(max = 350.dp)
        ) {
            TextField(
                value = userId.value,
                onValueChange = { userId.value = it },
                label = { Text(
                    text = "UserId",
                    color = colorResource(R.color.neon_green),
                    fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                ) },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    focusedTextColor = colorResource(R.color.white),
                    unfocusedTextColor = colorResource(R.color.white),
                    cursorColor = colorResource(R.color.white)
                )
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .border(1.dp, colorResource(R.color.neon_green), shape = RoundedCornerShape(8.dp))
                .padding(horizontal = 5.dp, vertical = 5.dp)
                .widthIn(max = 350.dp)
        ) {
            TextField(
                value = nickname.value,
                onValueChange = { nickname.value = it },
                label = { Text(
                    text = "Nickname",
                    color = colorResource(R.color.neon_green),
                    fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                ) },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    focusedTextColor = colorResource(R.color.white),
                    unfocusedTextColor = colorResource(R.color.white),
                    cursorColor = colorResource(R.color.white)
                )
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .border(1.dp, colorResource(R.color.neon_green), shape = RoundedCornerShape(8.dp))
                .padding(horizontal = 5.dp, vertical = 5.dp)
                .widthIn(max = 350.dp)
        ) {
            TextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = { Text(
                    text = "Email",
                    color = colorResource(R.color.neon_green),
                    fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                ) },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    focusedTextColor = colorResource(R.color.white),
                    unfocusedTextColor = colorResource(R.color.white),
                    cursorColor = colorResource(R.color.white)
                )
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .border(1.dp, colorResource(R.color.neon_green), shape = RoundedCornerShape(8.dp))
                .padding(horizontal = 5.dp, vertical = 5.dp)
                .widthIn(max = 350.dp)
        ) {
            TextField(
                value = password.value,
                onValueChange = { password.value = it },
                label = { Text(
                    text = "Password",
                    color = colorResource(R.color.neon_green),
                    fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                ) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.Transparent,
                    focusedContainerColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    focusedTextColor = colorResource(R.color.white),
                    unfocusedTextColor = colorResource(R.color.white),
                    cursorColor = colorResource(R.color.white)
                )
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if ((userId.value.isNotBlank() && nickname.value.isNotBlank() && password.value.isNotBlank() && email.value.isNotBlank())) {

                    val user = authorizationViewModel.validateRegistration(
                        id = userId.value,
                        nickname =  nickname.value,
                        email =  email.value,
                        password =  password.value
                    )

                    if(user != null) {
                        SessionManager.login(context, user.uuid)
                        navController.navigate("MainScreen") {
                            popUpTo("RegisterScreen")
                        }
                    }
                    else {
                        idIsBusy.value = true
                        loginError.value = false
                    }
                }
                else{
                    loginError.value = true
                    idIsBusy.value = false
                }
            },
            modifier = Modifier.fillMaxWidth().padding(start = 50.dp, end = 50.dp),
            colors = ButtonDefaults.buttonColors(colorResource(R.color.neon_green))
        ) {
            Text(
                "Register",
                color = Color.Black,
                fontFamily = FontFamily(Font(R.font.robotomonovariable)),
            )
        }
        if (idIsBusy.value) {
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = "UserId is busy",
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                fontWeight = FontWeight.W600,
                fontSize = 20.sp,
                color = colorResource(id = R.color.neon_green),
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (loginError.value) {
            Spacer(modifier = Modifier.height(18.dp))
            Text(
                text = "Invalid parameters",
                textAlign = TextAlign.Center,
                fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                fontWeight = FontWeight.W600,
                fontSize = 20.sp,
                color = colorResource(id = R.color.neon_green),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}