package com.example.hodos_final_android.di
import com.example.hodos_final_android.view_model.UserViewModel
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface UserViewModelEntryPoint {
    fun userViewModel(): UserViewModel
}