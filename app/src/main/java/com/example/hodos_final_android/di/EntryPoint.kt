package com.example.hodos_final_android.di
import com.example.hodos_final_android.view_model.AppViewViewModel
import com.example.hodos_final_android.view_model.ChatViewModel
import com.example.hodos_final_android.view_model.HomeViewModel
import com.example.hodos_final_android.view_model.LocationViewModel
import com.example.hodos_final_android.view_model.PlanTripViewModel
import com.example.hodos_final_android.view_model.PostViewModel
import com.example.hodos_final_android.view_model.UserSubscriptionModel
import com.example.hodos_final_android.view_model.UserViewModel
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface UserViewModelEntryPoint {
    fun userViewModel(): UserViewModel
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface ChatViewModelEntryPoint {
    fun chatViewModel(): ChatViewModel
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface PlanTripModelEntryPoint {
    fun planTripModel(): PlanTripViewModel
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface HomeViewModelEntryPoint {
    fun homeViewModel(): HomeViewModel
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface AppStateViewEntryPoint {
    fun appStateViewModel(): AppViewViewModel
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface PostViewModelEntryPoint {
    fun postViewModel(): PostViewModel
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface UserSubscriptionEntryPoint {
    fun userSubscriptionModel(): UserSubscriptionModel
}




@EntryPoint
@InstallIn(SingletonComponent::class)
interface LocationViewEntryPoint {
    fun locationViewModel(): LocationViewModel
}

