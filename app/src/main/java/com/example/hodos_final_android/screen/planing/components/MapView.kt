package com.example.hodos_final_android.screen.planing.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun MapView(
    modifier: Modifier = Modifier
) {
    // In a real app, this would be implemented with Google Maps or another map provider
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Map View\nWould display locations from the itinerary",
            textAlign = TextAlign.Center
        )
    }
}
