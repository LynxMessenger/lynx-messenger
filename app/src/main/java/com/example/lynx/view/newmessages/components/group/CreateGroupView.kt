package com.example.lynx.view.newmessages.components.group

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.lynx.R
import com.example.lynx.viewmodel.GroupViewModel

@Composable
fun CreateGroupView(
    navController: NavHostController,
    groupViewModel: GroupViewModel,
    inputName: String,
    onNameChange: (String) -> Unit,
    inputId: String,
    onIdChange: (String) -> Unit,
) {
    val avatarUri = groupViewModel.selectedAvatarUri
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(avatarUri ?: R.drawable.ic_addavatar),
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .clickable {
                        navController.navigate("avatarPickerGroup")
                    }
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(vertical = 8.dp)
            ) {
                BasicTextField(
                    value = inputName,
                    onValueChange = {
                        if (it.length <= 30) onNameChange(it)
                    },
                    textStyle = TextStyle(
                        fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                        fontSize = 20.sp,
                        color = colorResource(R.color.white)
                    ),
                    modifier = Modifier
                        .padding(start = 32.dp)
                        .width(200.dp)
                        .heightIn(min = 24.dp),
                    decorationBox = { innerTextField ->
                        Box(contentAlignment = Alignment.CenterStart) {
                            if (inputName.isEmpty()) {
                                Text(
                                    text = "Group Name",
                                    style = TextStyle(
                                        color = colorResource(R.color.neon_green),
                                        fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                                        fontSize = 20.sp
                                    )
                                )
                            }
                            innerTextField()
                        }
                    }
                )

                Spacer(modifier = Modifier.height(2.dp))

                Divider(
                    color = colorResource(R.color.neon_green),
                    thickness = 1.dp,
                    modifier = Modifier
                        .padding(start = 32.dp)
                        .width(200.dp)
                )

            }
        }
        BasicTextField(
            value = inputId,
            onValueChange = {
                if (it.length <= 20) onIdChange(it)
            },
            textStyle = TextStyle(
                fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                fontSize = 16.sp,
                color = colorResource(R.color.white)
            ),
            modifier = Modifier
                .padding(start = 32.dp)
                .width(120.dp)
                .heightIn(min = 24.dp),
            decorationBox = { innerTextField ->
                Box(contentAlignment = Alignment.CenterStart) {
                    if (inputId.isEmpty()) {
                        Text(
                            text = "Group Id",
                            style = TextStyle(
                                color = colorResource(R.color.neon_green),
                                fontFamily = FontFamily(Font(R.font.robotomonovariable)),
                                fontSize = 16.sp
                            )
                        )
                    }
                    innerTextField()
                }
            }
        )

        Spacer(modifier = Modifier.height(2.dp))

        Divider(
            color = colorResource(R.color.neon_green),
            thickness = 1.dp,
            modifier = Modifier
                .padding(start = 32.dp)
                .width(120.dp)
        )

    }
}
