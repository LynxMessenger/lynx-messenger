package com.example.lynx.view.settings.personal.accountsetting.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lynx.R
import com.example.lynx.session.SessionManager
import com.example.lynx.viewmodel.UserViewModel

@Composable
fun AccountSettingView(
    inputId: String,
    onIdChange:(String)-> Unit,
    inputPassword: String,
    onPasswordChange: (String) -> Unit,
    inputEmail: String,
    onEmailChange: (String) -> Unit,
    userViewModel: UserViewModel,
)

{
    val user = userViewModel.GetUserByUuid(uuid = SessionManager.currentUserId.toString())
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "UserId: ",
                fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = colorResource(R.color.neon_green),
                modifier = Modifier.padding(end = 8.dp)
            )

            Box(modifier = Modifier.weight(1f)) {
                BasicTextField(
                    value = inputId,
                    onValueChange = onIdChange,
                    textStyle = TextStyle(
                        fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = colorResource(R.color.white)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 24.dp),
                    decorationBox = { innerTextField ->
                        Box(contentAlignment = Alignment.CenterStart) {
                            if (inputId.isEmpty()) {
                                Text(
                                    text = user?.id ?: "unknown",
                                    style = TextStyle(
                                        color = colorResource(R.color.white).copy(alpha = 0.7f),
                                        fontSize = 20.sp
                                    )
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }
        }
        Spacer(modifier = Modifier.padding(top = 15.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Email: ",
                fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = colorResource(R.color.neon_green),
                modifier = Modifier.padding(end = 8.dp)
            )

            Box(modifier = Modifier.weight(1f)) {
                BasicTextField(
                    value = inputEmail,
                    onValueChange = onEmailChange,
                    textStyle = TextStyle(
                        fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = colorResource(R.color.white)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 24.dp),
                    decorationBox = { innerTextField ->
                        Box(contentAlignment = Alignment.CenterStart) {
                            if (inputEmail.isEmpty()) {
                                Text(
                                    text = user?.email ?: "unknown",
                                    style = TextStyle(
                                        color = colorResource(R.color.white).copy(alpha = 0.7f),
                                        fontSize = 20.sp
                                    )
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }
        }
        Spacer(modifier = Modifier.padding(top = 15.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Password: ",
                fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = colorResource(R.color.neon_green),
                modifier = Modifier.padding(end = 8.dp)
            )

            Box(modifier = Modifier.weight(1f)) {
                BasicTextField(
                    value = inputPassword,
                    onValueChange = onPasswordChange,
                    visualTransformation = PasswordVisualTransformation(),
                    textStyle = TextStyle(
                        fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp,
                        color = colorResource(R.color.white)
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 24.dp),
                    decorationBox = { innerTextField ->
                        Box(contentAlignment = Alignment.CenterStart) {
                            if (inputPassword.isEmpty()) {
                                val maskedPassword = "•".repeat(user?.password?.length ?: "unknown".length)
                                Text(
                                    text = maskedPassword,
                                    style = TextStyle(
                                        color = colorResource(R.color.white).copy(alpha = 0.7f),
                                        fontSize = 20.sp
                                    )
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }
        }
    }
}