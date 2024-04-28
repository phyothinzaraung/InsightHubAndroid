package edu.miu.cs489.insightHub.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.miu.cs489.insightHub.data.LoginRequest
import edu.miu.cs489.insightHub.data.LoginResponse
import edu.miu.cs489.insightHub.data.local.preferences.PreferencesManager
import edu.miu.cs489.insightHub.repository.UserRepository
import edu.miu.cs489.insightHub.view.util.loginToken
import edu.miu.cs489.insightHub.view.util.userId
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserSignInViewModel @Inject constructor(
    private val userRepository: UserRepository
) : ViewModel() {

    @Inject lateinit var preferencesManager: PreferencesManager

    private val _signInState = MutableStateFlow<SignInState>(SignInState.Initial)

    private val _userExist = MutableStateFlow(false)
    val userExit : StateFlow<Boolean> get() = _userExist

    fun setSignInStateEmpty(){
        _signInState.value = SignInState.Initial
    }

    val signInState: StateFlow<SignInState>
        get() = _signInState

    fun resetSignInState() {
        _signInState.value = SignInState.Initial
        _userExist.value = false
    }

    fun getSaveLoginData(){
        preferencesManager.getAccessToken()?.let {
            loginToken = it
        }
        preferencesManager.getUserId()?.let {
            userId = it
        }
        if (loginToken.isNotBlank() && userId != 0){
            _userExist.value = true
        }
    }

    fun signIn(loginRequest: LoginRequest) {
        _signInState.value = SignInState.Loading
        viewModelScope.launch {
            val userResult = userRepository.loginUser(loginRequest)

            userResult.onSuccess {
                preferencesManager.saveAccessToken(token = it.token)
                Log.d("token", it.token)
                preferencesManager.saveUserId(userId = it.userId)
                Log.d("userId", it.userId.toString())
                _signInState.value = SignInState.Success(it)
            }
            userResult.onFailure {
                _signInState.value = SignInState.Error(it.message ?: "Unknown Error")
            }
        }
    }

    sealed class SignInState {
        object Initial : SignInState()
        object Loading : SignInState()
        data class Success(val user: LoginResponse) : SignInState()
        data class Error(val message: String) : SignInState()
    }
}