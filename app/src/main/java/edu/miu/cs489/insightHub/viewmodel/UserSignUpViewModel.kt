package edu.miu.cs489.insightHub.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import edu.miu.cs489.insightHub.data.User
import edu.miu.cs489.insightHub.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserSignUpViewModel @Inject constructor(private val userRepository: UserRepository) :
    ViewModel() {

    private val _signUpState = MutableStateFlow<SignUpState>(SignUpState.Initial)
    val signUpState: StateFlow<SignUpState>
        get() = _signUpState

    fun setSignUpStateEmpty(){
        _signUpState.value = SignUpState.Initial
    }

    fun signUp(user: User) {
        _signUpState.value = SignUpState.Loading
        viewModelScope.launch {
            val userResult = userRepository.registerUser(user)
            userResult.onSuccess {
                _signUpState.value = SignUpState.Success(it)
            }
            userResult.onFailure {
                _signUpState.value = SignUpState.Error(it.message ?: "Unknown Error")
            }

        }

    }

    sealed class SignUpState {
        object Initial : SignUpState()
        object Loading : SignUpState()
        data class Success(val user: User) : SignUpState()
        data class Error(val message: String) : SignUpState()
    }
}