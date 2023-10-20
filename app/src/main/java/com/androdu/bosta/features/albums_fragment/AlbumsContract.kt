package com.androdu.bosta.features.albums_fragment

import com.androdu.bosta.data.api.model.Album
import com.androdu.bosta.data.api.model.User
import com.androdu.bosta.utils.baseviewmodel.UiEvent
import com.androdu.bosta.utils.baseviewmodel.UiState
import kotlin.random.Random

class AlbumsContract {
    sealed class Event : UiEvent {
        data class GetAlbums(val userId:Int) : Event()
    }

    data class State(
        val userState: AlbumsState
    ) : UiState{
        override fun equals(other: Any?): Boolean {
            return false
        }

        override fun hashCode(): Int {
            return Random.nextInt()
        }
    }

    sealed class AlbumsState {
        object Idle : AlbumsState()
        data class GetAlbumsSuccess(val albums: List<Album>) : AlbumsState()
        data class GetAlbumsFailed(val errMsg: String) : AlbumsState()
    }
}