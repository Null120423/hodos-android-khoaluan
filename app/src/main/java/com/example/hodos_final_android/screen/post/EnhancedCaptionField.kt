package com.example.hodos_final_android.screen.post

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.hodos_final_android.model.Location
import com.example.hodos_final_android.view_model.LocationTagViewModel
import com.example.hodos_final_android.view_model.TagHighlightTransformation

@Composable
fun EnhancedCaptionField(
    viewModel: LocationTagViewModel = hiltViewModel(),
    placeholder: String = "Add caption...",
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit
) {
    val caption = viewModel.caption.value
    val isTagging = viewModel.isTagging.value
    val locationSuggestions = viewModel.locationSuggestions.value
    val allLocationState by viewModel.allLocationState.collectAsState()
    // Use TextFieldValue to track cursor position
    var textFieldValue by remember {
        mutableStateOf(TextFieldValue(text = caption, selection = TextRange(caption.length)))
    }

    LaunchedEffect(Unit) {
        viewModel.allTag()
    }
    // Update TextFieldValue when caption changes from ViewModel
    LaunchedEffect(caption) {
        if (caption != textFieldValue.text) {
            textFieldValue = TextFieldValue(
                text = caption,
                selection = TextRange(viewModel.cursorPosition.value)
            )
        }
    }

    val focusRequester = remember { FocusRequester() }
    val focusManager = LocalFocusManager.current

    Column(modifier = modifier) {
        OutlinedTextField(
            visualTransformation = TagHighlightTransformation(),
            value = textFieldValue,
            onValueChange = { newValue ->
                textFieldValue = newValue
                allLocationState.data?.let {
                    viewModel.updateCaption(newValue.text, newValue.selection.end,
                        it
                    )
                }
                onValueChange(newValue.text)
            },
            placeholder = {
                Text(
                    text = placeholder,
                    color = Color.Gray
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = Color(0xFF0095F6),
                focusedContainerColor = Color(0xFF121212),
                unfocusedContainerColor = Color(0xFF121212)
            ),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Sentences
            ),
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .focusRequester(focusRequester)
        )

        // Location suggestions dropdown
        if (isTagging && locationSuggestions.isNotEmpty()) {
            LocationSuggestionsDropdown(
                isLoading = allLocationState.isLoading,
                suggestions = locationSuggestions,
                onLocationSelected = { location ->
                    viewModel.selectLocation(location)
                    onValueChange(viewModel.caption.value) // Update parent with new caption after location selection
                }
            )
        }
    }
}

@Composable
fun LocationSuggestionsDropdown(
    suggestions: List<Location>,
    onLocationSelected: (Location) -> Unit,
    isLoading: Boolean
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color(0xFF1E1E1E))
            .heightIn(max = 150.dp)
    ) {
        if(isLoading) {
            item {
                CircularProgressIndicator(
                    color = MaterialTheme.colorScheme.primary,
                    strokeWidth = 5.dp,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        items(suggestions) { location ->
            LocationSuggestionItem(
                location = location,
                onClick = { onLocationSelected(location) }
            )
        }
    }
}

@Composable
fun LocationSuggestionItem(
    location: Location,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = "Location",
            tint = Color.White
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = location.name,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(2.dp))

            location.address?.let {
                Text(
                    text = it,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }
    }
}
