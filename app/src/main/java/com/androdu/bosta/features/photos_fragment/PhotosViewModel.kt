package com.androdu.bosta.features.photos_fragment

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.androdu.bosta.data.BostaRepository
import com.androdu.bosta.features.photos_fragment.PhotosContract.PhotosState.GetPhotosFailed
import com.androdu.bosta.features.photos_fragment.PhotosContract.PhotosState.GetPhotosSuccess
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
class PhotosViewModel @Inject constructor(
    private val bostaRepository: BostaRepository,
    private val application: Application
) : BaseViewModel<PhotosContract.Event, PhotosContract.State>() {

    init {
        Log.d("TAG", "PhotosViewModel: init")
    }

    override fun createInitialState(): PhotosContract.State {
        return PhotosContract.State(photosState = PhotosContract.PhotosState.Idle)
    }

    override fun handleEvent(event: PhotosContract.Event) {
        when (event) {
            is PhotosContract.Event.GetPhotos -> {
                getPhotos(event.albumId)
            }
        }
    }

    private fun getPhotos(albumId: Int) {
        viewModelScope.launch {
            try {
                val result = bostaRepository.getPhotos(albumId)
                when (val response = handleResponse(application, result)) {
                    is ApiResult.Success -> {
                        delay(1000)
                        setState { copy(photosState = GetPhotosSuccess(response.data!!)) }
                    }

                    is ApiResult.Error -> {
                        setState { copy(photosState = GetPhotosFailed(response.message)) }
                    }
                }
            } catch (e: Exception) {
                Log.d("Api", "safeCall: " + e.message)
                setState { copy(photosState = GetPhotosFailed(e.message.toString())) }
            }
        }
    }
}