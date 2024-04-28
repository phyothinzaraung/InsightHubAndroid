package edu.miu.cs489.insightHub.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.miu.cs489.insightHub.data.Content
import edu.miu.cs489.insightHub.data.ContentDeleteResponse
import edu.miu.cs489.insightHub.data.ContentUpdate
import edu.miu.cs489.insightHub.repository.ContentRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContentViewModel @Inject constructor(
    private val contentRepository: ContentRepository
) : ViewModel() {

    private val _contents = MutableLiveData<List<Content>>(emptyList())
    val contents: LiveData<List<Content>>
        get() = _contents

    fun initializeContentList() {
        _contentState.value = ContentState.Loading
        viewModelScope.launch {
            val contentsResult = contentRepository.getContents()
            contentsResult.onSuccess {
                _contentState.value = ContentState.ListSuccess(it)
            }
            contentsResult.onFailure {
                _contentState.value = ContentState.Error(it.message.toString())
            }
        }
    }

    private val _contentState = MutableLiveData<ContentState>(ContentState.Initial)
    val contentState: LiveData<ContentState>
        get() = _contentState

    fun clearContentState() {
        _contentState.value = ContentState.Initial
    }

    fun addContent(content: Content) {
        _contentState.value = ContentState.Loading
        viewModelScope.launch {
            val addContentResult = contentRepository.addContent(content = content)
            addContentResult.onSuccess {
                _contents.value = _contents.value?.plus(it)
                _contentState.value = ContentState.Success(content = it)
            }
            addContentResult.onFailure {
                _contentState.value = ContentState.Error(it.message ?: "Unknown Error")
            }
        }
    }

    fun updateContent(content: ContentUpdate) {
        _contentState.value = ContentState.Loading
        viewModelScope.launch {
            val updateContentResult = contentRepository.updateContent(content = content)
            updateContentResult.onSuccess {
                _contents.value = _contents.value?.plus(it)
                _contentState.value = ContentState.Success(it)
            }
            updateContentResult.onFailure {
                _contentState.value = ContentState.Error(it.message ?: "Unknown Error")
            }
        }
    }

    fun getContentById(contentId: Int) {
        _contentState.value = ContentState.Loading
        viewModelScope.launch {
            val contentResult = contentRepository.getContentById(contentId)
            contentResult.onSuccess {
                _contentState.value = ContentState.Success(it)
            }
            contentResult.onFailure {
                _contentState.value = ContentState.Error(it.message ?: "Unknown Error")
            }

        }
    }

    fun deleteContent(contentId: Int) {
        _contentState.value = ContentState.Loading
        viewModelScope.launch {
            val deleteContentResult = contentRepository.deleteContent(contentId = contentId)
            deleteContentResult.onSuccess {
                _contents.value = _contents.value?.filter { it.contentId != contentId }
                _contentState.value = ContentState.DeleteSuccess(it)
            }
            deleteContentResult.onFailure {
                _contentState.value = ContentState.Error(it.message ?: "Unknown Error")
            }
        }
    }

    private val _content = MutableStateFlow(Content())
    val content get() = _content

    fun setContent(content: Content) {
        _content.value = content
    }


    sealed class ContentState {
        object Initial : ContentState()
        object Loading : ContentState()
        data class DeleteSuccess(val contentDeleteResponse: ContentDeleteResponse) : ContentState()
        data class Success(val content: Content) : ContentState()
        data class ListSuccess(val contents: List<Content>): ContentState()
        data class Error(val message: String) : ContentState()
    }

}

