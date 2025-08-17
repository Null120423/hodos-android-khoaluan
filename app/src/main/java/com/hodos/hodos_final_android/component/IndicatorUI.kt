package com.hodos.hodos_final_android.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hodos.hodos_final_android.theme.HodosTheme

@Composable
fun IndicatorUI(
    pageSize: Int,
    currentPage: Int,
) {


    HodosTheme { Row (horizontalArrangement = Arrangement.SpaceBetween) {
        repeat(pageSize){
            Spacer(modifier = Modifier.size(2.5.dp))

            Box(modifier = Modifier
                .height(14.dp)
                .width(width = if (it == currentPage) 32.dp else 14.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(color = if(it == currentPage) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background)

            )
            Spacer(modifier = Modifier.size(2.5.dp))

        }

    } }


}


@Preview(showBackground = true)
@Composable
fun IndicatorUIPreview1() {

    IndicatorUI(pageSize = 3, currentPage = 0)

}

@Preview(showBackground = true)
@Composable
fun IndicatorUIPreview2() {

    IndicatorUI(pageSize = 3, currentPage = 1)

}

@Preview(showBackground = true)
@Composable
fun IndicatorUIPreview3() {

    IndicatorUI(pageSize = 3, currentPage = 2)

}