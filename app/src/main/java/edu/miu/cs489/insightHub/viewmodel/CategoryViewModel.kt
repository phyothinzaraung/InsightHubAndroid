package edu.miu.cs489.insightHub.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.miu.cs489.insightHub.data.Category
import edu.miu.cs489.insightHub.data.User
import edu.miu.cs489.insightHub.repository.CategoryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
): ViewModel() {
    private val _categories = MutableLiveData<List<Category>>(emptyList())
    val categories: LiveData<List<Category>>
        get() = _categories

    private val _categoryState = MutableLiveData<CategoryState>(CategoryState.Initial)
    val categoryState: LiveData<CategoryState>
        get() = _categoryState

    fun getCategories(){
        _categoryState.value = CategoryState.Loading
        viewModelScope.launch {
            val categoryResult  = categoryRepository.getCategories()
            categoryResult.onSuccess {
                _categoryState.value = CategoryState.Success(it)
            }
            categoryResult.onFailure {
                _categoryState.value = CategoryState.Error(it.message.toString())
            }
        }
    }

    sealed class CategoryState {
        object Initial : CategoryState()
        object Loading : CategoryState()
        data class Success(val categoryList: List<Category>) : CategoryState()
        data class Error(val message: String) : CategoryState()
    }
}