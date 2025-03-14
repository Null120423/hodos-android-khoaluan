package com.example.hodos_final_android.component


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

/**
 * A composable function that creates a bottom sheet with customizable content.
 *
 * @param isVisible Boolean that controls the visibility of the bottom sheet
 * @param onDismiss Function to call when the bottom sheet is dismissed
 * @param title Optional title to display at the top of the bottom sheet
 * @param showCloseButton Whether to show a close button in the header
 * @param content The content to display in the bottom sheet
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomBottomSheet(
    isVisible: Boolean,
    onDismiss: () -> Unit,
    title: String? = null,
    showCloseButton: Boolean = true,
    content: @Composable ColumnScope.() -> Unit
) {
    if (isVisible) {
        val sheetState = rememberModalBottomSheetState(
            skipPartiallyExpanded = false
        )
        val scope = rememberCoroutineScope()

        // Handle back button press
        LaunchedEffect(isVisible) {
            if (!isVisible && sheetState.isVisible) {
                sheetState.hide()
            }
        }

        ModalBottomSheet(
            onDismissRequest = onDismiss,
            sheetState = sheetState,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            containerColor = MaterialTheme.colorScheme.background,
            tonalElevation = 8.dp,
            dragHandle = { BottomSheetDefaults.DragHandle() }
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .padding(bottom = 32.dp)
            ) {
                // Header with title and close button
                if (title != null || showCloseButton) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (title != null) {
                            Text(
                                text = title,
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        if (showCloseButton) {
                            IconButton(
                                onClick = {
                                    scope.launch {
                                        sheetState.hide()
                                        onDismiss()
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Close"
                                )
                            }
                        }
                    }

                }

                // Content
                content()
            }
        }
    }
}

/**
 * A composable function that provides a controller for the bottom sheet.
 *
 * @return A BottomSheetController that can be used to show and hide the bottom sheet
 */
@Composable
fun rememberBottomSheetController(): BottomSheetController {
    var isBottomSheetVisible by remember { mutableStateOf(false) }

    return remember {
        BottomSheetController(
            isVisible = { isBottomSheetVisible },
            show = { isBottomSheetVisible = true },
            hide = { isBottomSheetVisible = false }
        )
    }
}

/**
 * A controller class for the bottom sheet.
 */
class BottomSheetController(
    val isVisible: () -> Boolean,
    val show: () -> Unit,
    val hide: () -> Unit
)