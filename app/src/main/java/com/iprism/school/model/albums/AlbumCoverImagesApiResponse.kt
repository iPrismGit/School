package com.iprism.school.model.albums

import com.iprism.school.model.classteachermodel.Pagination

data class AlbumCoverImagesApiResponse(

    val message: String,
    val response: AlbumCoverImagesResponse,
    val status: Boolean

)

data class AlbumCoverImagesResponse(

    val album_covers: List<AlbumCover>,
    val pagination: Pagination,
    val id: String,
    val title: String

)

data class AlbumCover(

    val date: String,
    val description: String,
    val id: String,
    val image: String,
    val title: String

)