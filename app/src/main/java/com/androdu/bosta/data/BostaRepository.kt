package com.androdu.bosta.data

import com.androdu.bosta.data.api.BostaApi
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject


@ActivityRetainedScoped
class BostaRepository @Inject constructor(
    private val bostaApi: BostaApi
) {
    suspend fun getUsers() = bostaApi.getUsers()
    suspend fun getAlbums(userId:Int) = bostaApi.getAlbums(userId)
    suspend fun getPhotos(albumId:Int) = bostaApi.getPhotos(albumId)
}