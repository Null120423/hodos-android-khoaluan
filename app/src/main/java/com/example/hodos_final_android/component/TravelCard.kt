package com.example.hodos_final_android.component
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.example.hodos_final_android.R
import com.example.hodos_final_android.theme.HodosTheme

@Preview()
@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun TravelCard() {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    val context = LocalContext.current
    var isFavorite by remember { mutableStateOf(false) }

   HodosTheme {
       Card(
           shape = RoundedCornerShape(16.dp),
           modifier = Modifier
               .width(screenWidth/2 - 15.dp)
               .padding(5.dp)
               .clickable {
               },
           colors = CardDefaults.cardColors(containerColor = Color.White),
       ) {
           Column {
               Box(modifier = Modifier.fillMaxWidth()) {
                   GlideImage(
                       model = "https://media.istockphoto.com/id/904172104/photo/weve-made-it-all-this-way-i-am-proud.jpg?s=612x612&w=0&k=20&c=MewnsAhbeGRcMBN9_ZKhThmqPK6c8nCT8XYk5ZM_hdg=",
                       contentDescription = "Beach Image",
                       modifier = Modifier
                           .fillMaxWidth()
                           .height(100.dp)
                           .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                       contentScale = ContentScale.Crop
                   )

                   IconButton(
                       onClick = { isFavorite = !isFavorite },
                       modifier = Modifier
                           .align(Alignment.TopEnd)
                           .padding(8.dp)
                   ) {
                       Icon(
                           painter = painterResource(id = R.drawable.save_loca_icon),
                           contentDescription = "Favorite",
                           tint = if (isFavorite) MaterialTheme.colorScheme.primary
                           else MaterialTheme.colorScheme.tertiary
                           ,
                           modifier = Modifier.size(24.dp)
                       )
                   }
               }

               Column(modifier = Modifier.padding(10.dp)) {
                   Row(verticalAlignment = Alignment.CenterVertically) {
                       Icon(
                           painter = painterResource(id = R.drawable.bell_icon), // Icon sao
                           contentDescription = "Rating",
                           tint = Color(0xFFFFD700),
                           modifier = Modifier.size(14.dp)
                       )
                       Spacer(modifier = Modifier.width(4.dp))
                       Text(text = "3.8", fontSize = 12.sp, color = Color.Gray)
                   }
                   Text(
                       text = "Balaikambang Beach",
                       fontSize = 12.sp,
                       fontWeight = FontWeight.Bold,
                   )
                   Text(text = "Quan 1 TP Ho Chi Minh", fontSize = 12.sp, color = Color.Gray)

               }
           }
       }
   }
}
