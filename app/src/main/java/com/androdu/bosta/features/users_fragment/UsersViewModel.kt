package com.androdu.bosta.features.users_fragment

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.androdu.bosta.data.BostaRepository
import com.androdu.bosta.features.users_fragment.UsersContract.UsersState.GetUsersFailed
import com.androdu.bosta.features.users_fragment.UsersContract.UsersState.GetUsersSuccess
import com.androdu.bosta.utils.ApiResult
import com.androdu.bosta.utils.Helper.handleResponse
import com.androdu.bosta.utils.baseviewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UsersViewModel @Inject constructor(
    private val bostaRepository: BostaRepository,
    private val application: Application
) : BaseViewModel<UsersContract.Event, UsersContract.State>() {

    init {
        getUsers()
    }

    override fun createInitialState(): UsersContract.State {
        return UsersContract.State(userState = UsersContract.UsersState.Idle)
    }

    override fun handleEvent(event: UsersContract.Event) {
        when (event) {
            UsersContract.Event.GetUsers -> {
                getUsers()
            }
        }
    }

    private fun getUsers() {
        viewModelScope.launch {
            try {
                val result = bostaRepository.getUsers()
                when (val response = handleResponse(application, result)) {
                    is ApiResult.Success -> {
                        delay(1000)
                        setState { copy(userState = GetUsersSuccess(response.data!!)) }
                    }

                    is ApiResult.Error -> {
                        setState { copy(userState = GetUsersFailed(response.message)) }
                    }
                }
            } catch (e: Exception) {
                Log.d("Api", "safeCall: " + e.message)
                setState { copy(userState = GetUsersFailed(e.message.toString())) }
            }
        }
    }
}