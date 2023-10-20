package com.androdu.bosta.features.photos_fragment

import com.androdu.bosta.data.api.model.Album
import com.androdu.bosta.data.api.model.Photo
import com.androdu.bosta.data.api.model.User
import com.androdu.bosta.utils.baseviewmodel.UiEvent
import com.androdu.bosta.utils.baseviewmodel.UiState
import kotlin.random.Random

class PhotosContract {
    sealed class Event : UiEvent {
        data class GetPhotos(val albumId: Int) : Event()
    }

    data class State(
        val photosState: PhotosState
    ) : UiState {
        override fun equals(other: Any?): Boolean {
            return false
        }

        override fun hashCode(): Int {
            return Random.nextInt()
        }
    }

    sealed class PhotosState {
        object Idle : PhotosState()
        data class GetPhotosSuccess(val photos: List<Photo>) : PhotosState()
        data class GetPhotosFailed(val errMsg: String) : PhotosState()
    }
}