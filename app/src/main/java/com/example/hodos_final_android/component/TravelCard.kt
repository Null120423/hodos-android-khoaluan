package com.example.hodos_final_android.component
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.example.hodos_final_android.LocalNavController
import com.example.hodos_final_android.Screen
import com.example.hodos_final_android.model.Location
import com.example.hodos_final_android.navigateWithAnimation
import com.example.hodos_final_android.theme.HodosTheme

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun TravelCard(
    data : Location
) {
    val navController = LocalNavController.current
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

   HodosTheme {
       Card(
           onClick = {
               navController.navigateWithAnimation(Screen.LocationDetailScreen.route)
           },
           shape = RoundedCornerShape(16.dp),
           modifier = Modifier
               .width(screenWidth/2 - 20.dp)
               .padding(0.dp)
               .clickable {
               },
           colors = CardDefaults.cardColors(containerColor = Color.White),
       ) {
               Box(modifier = Modifier.fillMaxWidth()) {
                   ImgWithUrl(
                       url = data.img,
                       modifier = Modifier
                           .fillMaxWidth()
                           .height(200.dp)
                           .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                   )

                   Box(
                       modifier = Modifier
                           .align(Alignment.BottomStart)
                           .fillMaxWidth()
                           .background(MaterialTheme.colorScheme.scrim
                           )
                   ){
                       Column(modifier = Modifier.padding(10.dp)) {
                           Txt(
                               value = data.name,
                               size = 16,
                               fontWeight = FontWeight.Bold,
                               color = MaterialTheme.colorScheme.background
                           )
                           Txt(value = data.address.take(12), size = 12, color =  MaterialTheme.colorScheme.background)
                       }
                   }

           }
       }
   }
}
