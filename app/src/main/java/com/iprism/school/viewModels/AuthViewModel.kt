package com.iprism.school.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.iprism.school.model.authmodel.LoginApiRequest
import com.iprism.school.model.authmodel.LoginResponse
import com.iprism.school.repositories.AuthenticationRepository
import com.iprism.school.utils.UiState
import kotlinx.coroutines.launch

class AuthViewModel(private var repository: AuthenticationRepository) : ViewModel(){

    private val _otpResponse = MutableLiveData<UiState<LoginResponse>>()
    val otpResponse: LiveData<UiState<LoginResponse>> = _otpResponse

    private val _loginResponse = MutableLiveData<UiState<LoginResponse>>()
    val loginResponse: LiveData<UiState<LoginResponse>> = _loginResponse

    fun generateOtp(request : LoginApiRequest) {
        viewModelScope.launch {
            _otpResponse.value = UiState.Loading
            try {
                val response = repository.userLogin(request)
                if (response.status) {
                    _otpResponse.value = UiState.Success(response.response)
                } else {
                    _otpResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _otpResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun loginUser(request : LoginApiRequest) {
        viewModelScope.launch {
            _loginResponse.value = UiState.Loading
            try {
                val response = repository.userLogin(request)
                if (response.status) {
                    _loginResponse.value = UiState.Success(response.response)
                } else {
                    _loginResponse.value = UiState.Error(response.message ?: "Something went wrong")
                }
            } catch (e: Exception) {
                _loginResponse.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

}