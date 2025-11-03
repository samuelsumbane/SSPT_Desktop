package com.samuelsumbane.ssptdesktop.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.samuelsumbane.ssptdesktop.domain.repository.LoginUserRepository
import com.samuelsumbane.ssptdesktop.domain.repository.UserRepository
import com.samuelsumbane.ssptdesktop.kclient.LoginRequest
import com.samuelsumbane.ssptdesktop.kclient.Session
import com.samuelsumbane.ssptdesktop.presentation.viewmodel.viewmodelstates.LoginUserUiState
import com.samuelsumbane.ssptdesktop.ui.utils.FormInputName
import io.ktor.client.call.*
import io.ktor.http.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LoginUserViewModel(
    private val repo: LoginUserRepository,
    private val usersRepo: UserRepository
) : ViewModel() {
    val _uiState = MutableStateFlow(LoginUserUiState())
    val uiState = _uiState.asStateFlow()


    fun fillLoginUserForm(
        userEmail: String? = null,
        userPassword: String? = null,
        attempts: Int? = null,
        errorMessage: String? = null,
    ) {
        userEmail?.let { newValue -> _uiState.update { it.copy(userEmail = newValue) }}
        userPassword?.let { newValue -> _uiState.update { it.copy(userPassword = newValue) }}
        errorMessage?.let { newValue -> _uiState.update { it.copy(errorMessage = newValue) }}
    }

    fun onSubmitLoginUserForm() {
        viewModelScope.launch {
            if (uiState.value.userEmail.isBlank()) {
                setFormError(FormInputName.Email, "O email é obrigatório")
                return@launch
            } else {
                setFormError(FormInputName.Email, "")
            }

            if (uiState.value.userPassword.isBlank()) {
                setFormError(FormInputName.CurrentPassword, "A senha é obrigatória")
                return@launch
            } else {
                setFormError(FormInputName.CurrentPassword, "")
            }

            val response = repo.loginUser(LoginRequest(
                email = uiState.value.userEmail,
                password = uiState.value.userPassword
            ))
            println("response is: $response")
            response?.run { // In this case "run" combine .let + with
                if (status == HttpStatusCode.OK) {
                    fillLoginUserForm(attempts = 0, errorMessage = "")
                    println(response.body<Map<String, String>>())
                    for ((responseKey, responseValue) in body<Map<String, String>>()) {
                        when (responseKey) {
                            "token" -> Session.jwtToken = responseValue
                            "userId" -> {
                                println("userid: $responseValue")
                                usersRepo.getUserById(responseValue.toInt())
                                    ?.let {
                                        println("userItem: $it")
                                        Session.userData = it
                                        _uiState.update { uistate -> uistate.copy(redirectTo = "") }
                                    } ?: run {
                                        fillLoginUserForm(errorMessage = "Houve um desconhecido\n Tente novamente mais tarde.")
                                    }
                            }
                            else -> println("Unknown response key")
                        }
                    }
                } else { // HttpStatusCode.Unauthorized
                    fillLoginUserForm(attempts = 1, errorMessage = body())
                }
            }
        }

    }

    fun setFormError(inputName: FormInputName, error: String) {
        _uiState.update {
            it.copy(formErrors = it.formErrors.toMutableMap().apply { put(inputName, error) })
        }
    }
}