package com.iprism.school.repositories

import android.content.Context
import com.iprism.school.model.albums.AlbumCoverImagesApiRequest
import com.iprism.school.model.albums.AlbumCoverImagesApiResponse
import com.iprism.school.model.classteachermodel.ClassTeacherApiRequest
import com.iprism.school.model.classteachermodel.ClassTeacherApiResponse
import com.iprism.school.network.SchoolApi

class AlbumsRepository(private var context: Context) {

    private val apiService = SchoolApi.create(context)
    private val authRepository = AuthRepository(context)

    suspend fun fetchAndInsertAlbumCovers(request: AlbumCoverImagesApiRequest): AlbumCoverImagesApiResponse {
        var response = apiService.fetchAndInsertAlbumCovers(request)

        if (response.message.equals("Invalid or expired token", true)) {
            val refreshed = authRepository.refreshToken()
            if (refreshed) {
                response = apiService.fetchAndInsertAlbumCovers(request)
            }
        }

        return response
    }

}