package com.androdu.bosta.data.api

import com.androdu.bosta.data.api.model.Album
import com.androdu.bosta.data.api.model.Photo
import com.androdu.bosta.data.api.model.User
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface BostaApi {
    @GET("users")
    suspend fun getUsers(
    ): Response<List<User>>

    @GET("albums")
    suspend fun getAlbums(
        @Query("userId") userId: Int,
    ): Response<List<Album>>

    @GET("photos")
    suspend fun getPhotos(
        @Query("albumId") userId: Int,
    ): Response<List<Photo>>
}