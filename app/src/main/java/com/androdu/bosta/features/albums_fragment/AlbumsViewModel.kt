package com.androdu.bosta.features.albums_fragment

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.androdu.bosta.data.BostaRepository
import com.androdu.bosta.features.albums_fragment.AlbumsContract.AlbumsState.GetAlbumsFailed
import com.androdu.bosta.features.albums_fragment.AlbumsContract.AlbumsState.GetAlbumsSuccess
import com.androdu.bosta.features.albums_fragment.AlbumsContract.AlbumsState.Idle
import com.androdu.bosta.utils.ApiResult
import com.androdu.bosta.utils.Helper.handleResponse
import com.androdu.bosta.utils.baseviewmodel.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlbumsViewModel @Inject constructor(
    private val bostaRepository: BostaRepository,
    private val application: Application
) : BaseViewModel<AlbumsContract.Event, AlbumsContract.State>() {

    override fun createInitialState(): AlbumsContract.State {
        return AlbumsContract.State(userState = Idle)
    }

    override fun handleEvent(event: AlbumsContract.Event) {
        when (event) {
            is AlbumsContract.Event.GetAlbums -> {
                getUsers(event.userId)
            }
        }
    }

    private fun getUsers(userId: Int) {
        viewModelScope.launch {
            try {
                val result = bostaRepository.getAlbums(userId)
                when (val response = handleResponse(application, result)) {
                    is ApiResult.Success -> {
                        delay(1000)
                        setState { copy(userState = GetAlbumsSuccess(response.data!!)) }
                    }

                    is ApiResult.Error -> {
                        setState { copy(userState = GetAlbumsFailed(response.message)) }
                    }
                }
            } catch (e: Exception) {
                Log.d("Api", "safeCall: " + e.message)
                setState { copy(userState = GetAlbumsFailed(e.message.toString())) }
            }
        }
    }
}