package com.hodos.hodos_final_android.view_model

import Resource
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hodos.hodos_final_android.model.Location
import com.hodos.hodos_final_android.repository.LocationRepository
import com.hodos.hodos_final_android.service.api.parseJsonError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class LocationTagViewModel @Inject constructor(
    private val repository: LocationRepository
) : ViewModel() {

    private val _caption = mutableStateOf("")
    val caption: State<String> = _caption

    private val _isTagging = mutableStateOf(false)
    val isTagging: State<Boolean> = _isTagging

    private val _currentTag = mutableStateOf("")
    val currentTag: State<String> = _currentTag

    private val _cursorPosition = mutableStateOf(0)
    val cursorPosition: State<Int> = _cursorPosition

    private val _locationSuggestions = mutableStateOf<List<Location>>(emptyList())
    val locationSuggestions: State<List<Location>> = _locationSuggestions

    private val _allLocationState = MutableStateFlow(ResponseDataState<List<Location>>(isLoading = true))
    val allLocationState: StateFlow<ResponseDataState<List<Location>>> = _allLocationState

    fun allTag() {
        repository.find()
            .onEach { result ->
                _allLocationState.value = when (result) {
                    is Resource.Success -> {
                        ResponseDataState(data = result.data)
                    }

                    is Resource.Error -> {
                        result.message?.let { Log.d("API_ERROR", it) }
                        val error = result.message?.let { parseJsonError(it) }
                        ResponseDataState(error = error)
                    }

                    is Resource.Loading -> {
                        ResponseDataState(isLoading = true)
                    }

                    else -> ResponseDataState()
                }
            }
            .launchIn(viewModelScope)
    }

    fun updateCaption(newText: String, cursorPos: Int, locations: List<Location>) {
        _caption.value = newText
        _cursorPosition.value = cursorPos
        checkForHashtag(newText, cursorPos,locations)
    }

    private fun checkForHashtag(text: String, cursorPos: Int, locations: List<Location>) {

        if (cursorPos <= 0 || text.isEmpty()) {
            resetTaggingState()
            return
        }

        var startPos = cursorPos - 1
        while (startPos >= 0 && !text[startPos].isWhitespace()) {
            startPos--
        }
        startPos++

        if (startPos < cursorPos && startPos < text.length && text[startPos] == '#') {
            val tagText = text.substring(startPos + 1, cursorPos).trim()
            _isTagging.value = true
            _currentTag.value = tagText
            filterLocations(tagText, locations)
        } else {
            resetTaggingState()
        }
    }

    private fun filterLocations(tagText: String, locations: List<Location>) {
        _locationSuggestions.value = if (tagText.isEmpty()) {
            locations.take(5)
        } else {
            locations
                .filter { it.label != null || it.address != null }
                .filter {
                    it.label?.contains(tagText, ignoreCase = true) == true ||
                            it.address?.contains(tagText, ignoreCase = true) == true
                }
                .take(5)

        }
    }


    private fun resetTaggingState() {
        _isTagging.value = false
        _currentTag.value = ""
        _locationSuggestions.value = emptyList()
    }

    fun selectLocation(location: Location) {
        if (!_isTagging.value) return

        val text = _caption.value
        val cursorPos = _cursorPosition.value

        var hashtagStart = cursorPos - 1
        while (hashtagStart >= 0 && text[hashtagStart] != '#') {
            hashtagStart--
        }

        if (hashtagStart >= 0 && text[hashtagStart] == '#') {
            val newText = text.substring(0, hashtagStart) +
                    "#" + location.label?.replace(" ", "") +
                    text.substring(cursorPos)

            _caption.value = newText
            _cursorPosition.value = hashtagStart + (location.label?.length ?: 0) + 1
        }

        resetTaggingState()
    }

    fun cancelTagging() {
        resetTaggingState()
    }
}

class TagHighlightTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val annotated = buildAnnotatedString {
            val regex = "#\\w+".toRegex()
            var lastIndex = 0

            for (match in regex.findAll(text.text)) {
                append(text.text.substring(lastIndex, match.range.first))
                withStyle(SpanStyle(color = Color(0xFF0095F6))) {
                    append(match.value)
                }
                lastIndex = match.range.last + 1
            }

            if (lastIndex < text.text.length) {
                append(text.text.substring(lastIndex))
            }
        }

        return TransformedText(annotated, OffsetMapping.Identity)
    }
}
