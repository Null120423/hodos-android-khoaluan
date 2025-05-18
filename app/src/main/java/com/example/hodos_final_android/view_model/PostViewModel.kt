package com.example.hodos_final_android.view_model

import Resource
import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.hodos_final_android.model.Pagination
import com.example.hodos_final_android.model.PaginationResponse
import com.example.hodos_final_android.model.Post
import com.example.hodos_final_android.model.Response
import com.example.hodos_final_android.repository.PostRepository
import com.example.hodos_final_android.service.api.parseJsonError
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class PostViewModel @Inject constructor(
    private val repository: PostRepository,
    private val app: Application
) : ViewModel() {
    // Step tracking
    private val _currentStep = mutableStateOf(PostCreationStep.SELECT_MEDIA)
    val currentStep = _currentStep

    // Selected media
    private val _selectedImages = mutableStateListOf<Uri>()
    val selectedImages = _selectedImages

    // Caption
    private val _caption = mutableStateOf("")
    val caption = _caption

    private val _postCreateState = MutableStateFlow(ResponseDataState<Response>(isLoading = false))
    val postCreateState: StateFlow<ResponseDataState<Response>> = _postCreateState

    private val _paginationState = MutableStateFlow(ResponseDataState<PaginationResponse<Post>>(isLoading = true))
    val paginationState: StateFlow<ResponseDataState<PaginationResponse<Post>>> = _paginationState


    // Add a single image to selection
    fun addImage(uri: Uri) {
        if (!_selectedImages.contains(uri)) {
            _selectedImages.add(uri)
        }
    }

    // Remove an image from selection
    fun removeImage(uri: Uri) {
        _selectedImages.remove(uri)
    }

    // Clear all selected images
    fun clearImages() {
        _selectedImages.clear()
    }

    // Update caption
    fun updateCaption(newCaption: String) {
        _caption.value = newCaption
    }

    // Move to next step
    fun moveToNextStep() {
        when (_currentStep.value) {
            PostCreationStep.SELECT_MEDIA -> _currentStep.value = PostCreationStep.ADD_CAPTION
            PostCreationStep.ADD_CAPTION -> {}
        }
    }

    // Move to previous step
    fun moveToPreviousStep() {
        when (_currentStep.value) {
            PostCreationStep.SELECT_MEDIA -> {}
            PostCreationStep.ADD_CAPTION -> _currentStep.value = PostCreationStep.SELECT_MEDIA
        }
    }

    fun createPost() {
        val captionRequest = caption.value.toRequestBody("text/plain".toMediaTypeOrNull())
        val imageParts = selectedImages.mapNotNull { uri ->
            uriToPart(uri)
        }

        repository.createPost(captionRequest, imageParts)
            .onEach { result ->
                _postCreateState.value = when (result) {
                    is Resource.Success -> {
                        delay(1000)
                        ResponseDataState(data = result.data)
                    }
                    is Resource.Error -> {
                        result.message?.let { Log.i("API", it) }
                        val error = result.message?.let { parseJsonError(it) }
                        ResponseDataState(error = error)
                    }
                    is Resource.Loading -> ResponseDataState(isLoading = true)
                    else -> ResponseDataState()
                }
            }
            .launchIn(viewModelScope)
    }


    private fun uriToPart(uri: Uri): MultipartBody.Part? {
        val context = app.applicationContext
        val file = uriToFile(context, uri) ?: return null
        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("imgs", file.name, requestFile)
    }

    private fun uriToFile(context: android.content.Context, uri: Uri): File? {
        return try {
            val inputStream = context.contentResolver.openInputStream(uri) ?: return null
            val tempFile = File.createTempFile("upload", ".jpg", context.cacheDir)
            tempFile.outputStream().use { outputStream ->
                inputStream.copyTo(outputStream)
            }
            tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun pagination(body: Pagination<Any>) {
        repository.pagination(body)
            .onEach { result ->
                _paginationState.value = when (result) {
                    is Resource.Success -> {
                        val currentData = _paginationState.value.data?.data ?: emptyList()
                        val newData = if (body.skip > 0) currentData + (result.data?.data ?: emptyList())
                        else result.data?.data ?: emptyList()
                        ResponseDataState(data = result.data?.copy(data = newData))
                    }

                    is Resource.Error -> {
                        val error = result.message?.let { parseJsonError(it) }
                        ResponseDataState(error = error)
                    }

                    is Resource.Loading -> {
                        ResponseDataState(isLoading = true, data = _paginationState.value.data)
                    }

                    else -> ResponseDataState()
                }
            }
            .launchIn(viewModelScope)
    }



    fun resetFlow() {
        _currentStep.value = PostCreationStep.SELECT_MEDIA
        _selectedImages.clear()
        _caption.value = ""
        _postCreateState.value = ResponseDataState()
    }

    fun resetCreatePost() {
        _postCreateState.value = ResponseDataState()
    }
}

/**
 * Enum to track the current step in the post creation flow
 */
enum class PostCreationStep {
    SELECT_MEDIA,
    ADD_CAPTION
}
