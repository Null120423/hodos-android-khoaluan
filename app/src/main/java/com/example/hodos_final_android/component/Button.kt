package com.example.hodos_final_android.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hodos_final_android.helper.sdp
import com.example.hodos_final_android.helper.toSsp

@Composable
fun ButtonUi(
    text: String = "Next",
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = MaterialTheme.colorScheme.onPrimary,
    textStyle: TextStyle = MaterialTheme.typography.titleMedium,
    fontSize: Int = 14,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick, colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor, contentColor = textColor
        ), shape = RoundedCornerShape(10.dp)
    ) {
        Text(
            text = text, fontSize = fontSize.sp, style = textStyle
        )
    }

}


@Preview
@Composable
fun NextButton() {

    ButtonUi (text = "Next") {

    }

}

@Preview
@Composable
fun BackButton() {

    ButtonUi(text = "Back",
        backgroundColor = Color.Transparent,
        textColor = Color.Gray,
        textStyle = MaterialTheme.typography.bodySmall,
        fontSize = 13) {
    }


}

@Composable()
fun TextBtn(
    onClick: () -> Unit = {},
    title : String = "",
    color: Color = Color.Black,
    size: Int = 12,
    fontWeight: FontWeight = FontWeight.Normal,
    disabled: Boolean = false
) {
    TextButton(onClick = onClick) {
        Text(
            text = title,
            color = color,
            fontSize = size.sp,
            fontWeight = fontWeight
        )
    }
}


@Preview()
@Composable
fun BtnPrimary(
    title: String = "Default",
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = Color.White,
    rounded : Int = 10000,
    height : Int = 50,
    size : Int = 16,
    fontWeight: FontWeight = FontWeight.Medium,
    minWidth : Int =  100,
    disabled : Boolean = false,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .width(minWidth.dp)
            .height(height.sdp)
        ,
        shape = RoundedCornerShape(rounded.sdp),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor
        ),
        enabled = !disabled
    ) {
        Text(
            text = title,
            fontSize = size.toSsp(),
            fontWeight = fontWeight,
            color = textColor
        )
    }
}


@Composable
fun IconBtn(
    imgVector: ImageVector? = null,
    icon: Int? = null,
    contentDescription: String? = "",
    onClick: () -> Unit = {},
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    modifier: Modifier = Modifier,
    size: Int = 20
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
            .size(size.dp)
            .clip(CircleShape)
            .background(backgroundColor)
    ) {
        when {
            imgVector != null -> {
                Icon(
                    imageVector = imgVector,
                    contentDescription = contentDescription,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(24.dp)
                )
            }
            icon != null -> {
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = contentDescription,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(24.dp)
                )
            }
            else -> {
                Spacer(modifier = Modifier.size(24.dp))
            }
        }
    }
}


@Composable
fun CircularButtonWithTitle(
    value: String ="",
    onClick: () -> Unit
) {
    Surface(
        onClick = { onClick()},
        color = MaterialTheme.colorScheme.primary,
        shape = RoundedCornerShape(80.sdp), // Very rounded corners for pill shape
        modifier = Modifier
            .shadow(
                elevation = 8.sdp,
                shape = RoundedCornerShape(80.sdp),
                spotColor = Color(0xFF000000),
                ambientColor = Color(0xFF000000)
            )
            .border(2.sdp, Color.White, RoundedCornerShape(80.sdp))
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.sdp),
            modifier = Modifier.padding(start = 24.sdp, end = 8.sdp, top = 8.sdp, bottom = 8.sdp)
        ) {
            // Title text
            Txt(
                value = value,
                color = MaterialTheme.colorScheme.background,
                fontWeight = FontWeight.ExtraBold,
                size = 18
            )

            // Circular arrow icon
            Box(
                modifier = Modifier
                    .size(40.sdp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(
                        width = 1.5.dp,
                        color = Color.LightGray.copy(alpha = 0.5f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.ArrowForward,
                    contentDescription = "Forward",
                    tint = Color.DarkGray,
                    modifier = Modifier.size(20.sdp)
                )
            }
        }
    }
}



@Preview()
@Composable
fun BtnOutline(
    title: String = "Default",
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.primary,
    textColor: Color = MaterialTheme.colorScheme.primary,
    rounded : Int = 1000,
    height : Int = 50,
    size : Int = 16,
    fontWeight: FontWeight = FontWeight.Medium,
    minWidth : Int =  100
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .width(minWidth.dp)
            .height(height.sdp)
        ,
        shape = RoundedCornerShape(rounded.sdp),
        colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.primary),
        border = BorderStroke(2.sdp, MaterialTheme.colorScheme.primary)
    ) {
        Text(
            text = title,
            fontSize = size.sp,
            fontWeight = fontWeight,
            color = textColor
        )
    }
}
