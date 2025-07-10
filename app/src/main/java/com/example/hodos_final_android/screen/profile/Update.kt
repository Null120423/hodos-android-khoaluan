package com.example.hodos_final_android.screen.profile

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hodos_final_android.LocalNavController
import com.example.hodos_final_android.di.UserViewModelEntryPoint
import com.example.hodos_final_android.ui.components.AvatarUploadComponent
import com.example.hodos_final_android.ui.components.DatePickerComponent
import dagger.hilt.android.EntryPointAccessors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileUpdateScreen(
) {
    val navController = LocalNavController.current
    val onNavigateBack = {
        navController.popBackStack()
    }
    val context = LocalContext.current
    val userViewModel = remember {
        EntryPointAccessors
            .fromApplication(context, UserViewModelEntryPoint::class.java)
            .userViewModel()
    }

    val uiState by userViewModel.uiState.collectAsState()
    val isLoading by userViewModel.isLoading.collectAsState()
    val validationErrors by userViewModel.validationErrors.collectAsState()

    // Image picker launcher
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { userViewModel.updateField("avatarUri", it) }
    }

    // Calculate completion percentage
    val completionPercentage = remember(uiState) {
        val totalFields = 8f
        var completedFields = 0f

        if (uiState.fullName.isNotBlank()) completedFields++
        if (uiState.email.isNotBlank()) completedFields++
        if (uiState.phoneNumber.isNotBlank()) completedFields++
        if (uiState.birthDate.isNotBlank()) completedFields++
        if (uiState.gender.isNotBlank()) completedFields++
        if (uiState.avatarUri != null || !uiState.avatar.isNullOrBlank()) completedFields++

        (completedFields / totalFields * 100).toInt()
    }


    LaunchedEffect(Unit) {
        userViewModel.initUiState()
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Edit Profile",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        onNavigateBack()
                    }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                // Progress Section
                ProgressSection(
                    completionPercentage = completionPercentage
                )

                // Avatar Section
                ModernAvatarSection(
                    avatarUri = uiState.avatarUri,
                    avatarUrl = uiState.avatar,
                    onAvatarClick = { imagePickerLauncher.launch("image/*") }
                )

                // Form Fields
                Column(
                    verticalArrangement = Arrangement.spacedBy(20.dp)
                ) {
                    ModernTextField(
                        value = uiState.fullName,
                        onValueChange = { userViewModel.updateField("fullName", it) },
                        label = "Name",
                        isError = validationErrors.containsKey("fullName"),
                        errorMessage = validationErrors["fullName"]
                    )

                    ModernTextField(
                        isEdit = false,
                        value = uiState.email,
                        onValueChange = { userViewModel.updateField("email", it) },
                        label = "Email Address",
                        isError = validationErrors.containsKey("email"),
                        errorMessage = validationErrors["email"],
                        isVerified = uiState.email.isNotBlank() && !validationErrors.containsKey("email")
                    )

                    ModernTextField(
                        value = uiState.phoneNumber,
                        onValueChange = { userViewModel.updateField("phoneNumber", it) },
                        label = "Phone Number"
                    )

                    ModernDateField(
                        value = uiState.birthDate,
                        onValueChange = { userViewModel.updateField("birthDate", it) },
                        label = "Date of Birth"
                    )

                    ModernDropdownField(
                        value = uiState.gender,
                        onValueChange = { userViewModel.updateField("gender", it) },
                        label = "Gender",
                        options = listOf("Male", "Female", "Other")
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Save Button with Data Printing
                Button(
                    onClick = {
                        // Print all data before sending to API
                        printProfileData(uiState)
                        userViewModel.saveProfile(context = context, onSuccess = {
                            navController.popBackStack()
                        })
                    },
                    enabled = !isLoading && validationErrors.isEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.background
                        )
                    } else {
                        Text(
                            "Saved Change",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.background
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))
            }

            // Loading overlay
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier.padding(32.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                "Updating profile...",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }
    }
}

// Function to print profile data
private fun printProfileData(uiState: com.example.hodos_final_android.view_model.ProfileUpdateUiState) {
    println("=== PROFILE DATA TO SEND TO API ===")
    println("Full Name: ${uiState.fullName}")
    println("Email: ${uiState.email}")
    println("Phone Number: ${uiState.phoneNumber}")
    println("Birth Date: ${uiState.birthDate}")
    println("Gender: ${uiState.gender}")
    println("Avatar URI: ${uiState.avatarUri}")
    println("Avatar URL: ${uiState.avatar}")

    // Create API payload structure
    val apiPayload = mapOf(
        "user" to mapOf(
            "email" to uiState.email,
            "avatar" to (uiState.avatarUri?.toString() ?: uiState.avatar)
        ),
        "userDetails" to mapOf(
            "fullName" to uiState.fullName,
            "phoneNumber" to uiState.phoneNumber,
            "birthDate" to uiState.birthDate,
            "gender" to uiState.gender
        )
    )

    println("=== API PAYLOAD STRUCTURE ===")
    println(apiPayload)
    println("=== END OF PROFILE DATA ===")
}

@Composable
private fun ProgressSection(
    completionPercentage: Int
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "You only need ${100 - completionPercentage}% more!",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black
        )

        Text(
            text = "Complete your data ",
            fontSize = 14.sp,
            color = Color.Gray,
            lineHeight = 20.sp
        )

        // Progress Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .background(
                    Color(0xFFE5E5E5),
                    RoundedCornerShape(3.dp)
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(completionPercentage / 100f)
                    .height(6.dp)
                    .background(
                        MaterialTheme.colorScheme.primary,
                        RoundedCornerShape(3.dp)
                    )
            )
        }
    }
}

@Composable
private fun ModernAvatarSection(
    avatarUri: android.net.Uri?,
    avatarUrl: String?,
    onAvatarClick: () -> Unit
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        AvatarUploadComponent(
            avatarUri = avatarUri,
            avatarUrl = avatarUrl,
            onAvatarClick = onAvatarClick,
            modifier = Modifier.size(120.dp)
        )
    }
}

@Composable
private fun ModernTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean = false,
    errorMessage: String? = null,
    isVerified: Boolean = false,
    maxLines: Int = 1,
    isEdit: Boolean = true
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = label,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.tertiary
            )

            if (isVerified) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Verified",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = "VERIFIED",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }

        OutlinedTextField(
            enabled = isEdit,
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            maxLines = maxLines,
            isError = isError,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MaterialTheme.colorScheme.primary,
                unfocusedBorderColor = Color(0xFFE5E5E5),
                errorBorderColor = Color.Red,
                focusedContainerColor = MaterialTheme.colorScheme.background,
                unfocusedContainerColor = MaterialTheme.colorScheme.background,
                disabledContainerColor = Color.Gray.copy(0.4f)
            ),
            shape = RoundedCornerShape(12.dp)
        )

        if (isError && errorMessage != null) {
            Text(
                text = errorMessage,
                fontSize = 12.sp,
                color = Color.Red
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModernDateField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )

        DatePickerComponent(
            label = "",
            selectedDate = value,
            onDateSelected = onValueChange,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModernDropdownField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    options: List<String>
) {
    var expanded by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = value,
                onValueChange = { },
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = Color(0xFFE5E5E5),
                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    unfocusedContainerColor = MaterialTheme.colorScheme.background
                ),
                shape = RoundedCornerShape(12.dp)
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                options.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(option) },
                        onClick = {
                            onValueChange(option)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
