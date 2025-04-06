package com.example.hodos_final_android.component


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.example.hodos_final_android.R
import com.example.hodos_final_android.helper.getScreenHeight
import com.example.hodos_final_android.helper.getScreenWidth


@Composable
fun NoResultDialog(
    onDismiss: () -> Unit,
    title : String = "Can not find data!z"
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {

        ColumnCenter(
            modifier = Modifier
                .fillMaxSize()
        ) {

            AnimateImg(
                source = R.raw.nores_ani,
                modifier = Modifier.height(
                    (getScreenHeight() / 2).dp
                )
            )
            Seprate(height = 10)
            Box(modifier = Modifier.padding(20.dp)) {
                Column(
                    modifier = Modifier
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(10.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    Title(
                        value = title,
                        size = 24,
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Seprate(height = 10)
                    BtnPrimary(
                        minWidth = getScreenWidth(),
                        title = "Close",
                        onClick = onDismiss,
                    )

                }
            }

        }
    }
}




