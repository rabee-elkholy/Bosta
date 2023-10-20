package com.androdu.bosta.features.users_fragment

import com.androdu.bosta.data.api.model.User
import com.androdu.bosta.utils.baseviewmodel.UiEvent
import com.androdu.bosta.utils.baseviewmodel.UiState
import kotlin.random.Random

class UsersContract {
    sealed class Event : UiEvent {
        object GetUsers : Event()
    }

    data class State(
        val userState: UsersState
    ) : UiState{
        override fun equals(other: Any?): Boolean {
            return false
        }

        override fun hashCode(): Int {
            return Random.nextInt()
        }
    }

    sealed class UsersState {
        object Idle : UsersState()
        data class GetUsersSuccess(val users: List<User>) : UsersState()
        data class GetUsersFailed(val errMsg: String) : UsersState()
    }
}