package com.iprism.school.repositories

import android.content.Context
import com.iprism.school.model.albums.AlbumCoverImagesApiRequest
import com.iprism.school.model.albums.AlbumCoverImagesApiResponse
import com.iprism.school.model.albums.AlbumsGalleryApiResponse
import com.iprism.school.model.classteachermodel.ClassTeacherApiRequest
import com.iprism.school.model.classteachermodel.ClassTeacherApiResponse
import com.iprism.school.network.SchoolApi
import okhttp3.MultipartBody
import okhttp3.RequestBody

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

    suspend fun uploadAlbumMedia(
        userId: RequestBody,
        albumId: RequestBody,
        viewType: RequestBody,
        page: RequestBody,
        type: RequestBody,
        media: List<MultipartBody.Part>
    ): AlbumsGalleryApiResponse {

        var response = apiService.uploadAlbumMedia(
            userId, albumId, viewType, page, type, media
        )

        if (response.message.equals("Invalid or expired token", true)) {
            val refreshed = authRepository.refreshToken()
            if (refreshed) {
                response = apiService.uploadAlbumMedia(
                    userId, albumId, viewType, page, type, media
                )
            }
        }

        return response
    }


}