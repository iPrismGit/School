package com.iprism.school.model.albums

import com.iprism.school.model.classteachermodel.Pagination

data class AlbumsGalleryApiResponse(

    val message: String,
    val response: AlbumsGalleryResponse,
    val status: Boolean

)

data class AlbumsGalleryResponse(

    val albums_gallery: List<AlbumsGallery>,
    val pagination: Pagination

)

data class AlbumsGallery(

    val id: String,
    val image: String,
    val type: String

)