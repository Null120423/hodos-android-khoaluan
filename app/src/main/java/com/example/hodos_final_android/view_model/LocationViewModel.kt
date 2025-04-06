package com.example.hodos_final_android.view_model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hodos_final_android.R
import com.example.hodos_final_android.model.LocationDetail
import com.example.hodos_final_android.repository.LocationRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationDetailViewModel @Inject constructor(
) : ViewModel() {
    private val _locationDetail = MutableStateFlow<LocationDetail?>(null)
    val locationDetail: StateFlow<LocationDetail?> = _locationDetail

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> = _isFavorite

    fun loadLocationDetail(locationId: String) {
        viewModelScope.launch {
            // In a real app, you would fetch this from a repository
            // For this example, we're using mock data
            _locationDetail.value = getMockLocationDetail(locationId)
        }
    }

    fun toggleFavorite() {
        _isFavorite.value = !_isFavorite.value
        // In a real app, you would update this in a repository
    }

    private fun getMockLocationDetail(locationId: String): LocationDetail {
        // Mock data for demonstration
        return LocationDetail(
            id = locationId,
            name = "Donau Elegance",
            hotelName = "Lenas Donau Hotel",
            rating = 4,
            reviews = 72000,
            region = "TYROLEAN ALPS",
            distanceToCenter = 1,
            guests = 2,
            stayDuration = 10,
            amenities = listOf("1 king bed", "Free wi-fi", "TV"),
            pricePerNight = 80,
            imageResId = R.drawable.email_icon // You would need to add this image
        )
    }
}

