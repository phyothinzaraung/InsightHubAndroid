package edu.miu.cs489.insightHub.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.miu.cs489.insightHub.data.LoginResponse
import edu.miu.cs489.insightHub.data.User
import edu.miu.cs489.insightHub.data.local.preferences.PreferencesManager
import edu.miu.cs489.insightHub.repository.UserRepository
import edu.miu.cs489.insightHub.view.util.loginToken
import edu.miu.cs489.insightHub.view.util.userId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val userRepository: UserRepository
): ViewModel(){

    @Inject lateinit var preferencesManager: PreferencesManager

    private val _user = MutableLiveData(User())
    val user: LiveData<User>
        get() = _user

    private val _userState = MutableLiveData<UserState>(UserState.Initial)
    val userState: LiveData<UserState>
        get() = _userState

    fun getUserById(){
        _userState.value = UserState.Loading
        viewModelScope.launch {
            val userResult = userRepository.getUserById(userId)
            userResult.onSuccess {
                _userState.value = UserState.Success(it)
            }
            userResult.onFailure {
                _userState.value = UserState.Error(it.message.toString())
            }

        }
    }

    fun clearPreference(){
        loginToken = ""
        userId = 0
        preferencesManager.clear()
    }

    sealed class UserState {
        object Initial : UserState()
        object Loading : UserState()
        data class Success(val user: User) : UserState()
        data class Error(val message: String) : UserState()
    }
}