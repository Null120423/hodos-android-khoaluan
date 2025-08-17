package com.hodos.hodos_final_android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class DateItem(
    val date: Date,
    val dayOfMonth: Int,
    val dayOfWeek: String,
    val month: String,
    val year: Int
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerComponent(
    label: String,
    selectedDate: String,
    onDateSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var showDatePicker by remember { mutableStateOf(false) }



    Column(modifier = modifier) {
        // Input field
        OutlinedTextField(
            value = if (selectedDate.isBlank()) "Select date" else selectedDate,
            onValueChange = { },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { showDatePicker = true }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select Date",
                        tint = Color(0xFF007AFF)
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDatePicker = true },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF007AFF),
                unfocusedBorderColor = Color(0xFFE5E5E5),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                disabledTextColor = if (selectedDate.isBlank()) Color(0xFF999999) else Color.Black
            ),
            shape = RoundedCornerShape(12.dp)
        )

        if (showDatePicker) {
            DatePickerDialog(
                onDateSelected = { dateMillis ->
                    dateMillis?.let {
                        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                        onDateSelected(formatter.format(Date(it)))
                    }
                    showDatePicker = false
                },
                onDismiss = {
                    showDatePicker = false
                }
            )
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerDialog(
    onDateSelected: (Long?) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateSelected(datePickerState.selectedDateMillis)
                },
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color(0xFF007AFF)
                )
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                colors = ButtonDefaults.textButtonColors(
                    contentColor = Color(0xFF999999)
                )
            ) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            colors = DatePickerDefaults.colors(
                selectedDayContainerColor = Color(0xFF007AFF)
            )
        )
    }
}
