package com.iprism.school.model.homepagemodel

data class HomePageApiResponse(

    val message: String,
    val response: HomePageResponse,
    val status: Boolean

)

data class HomePageResponse (

    val album_covers: List<AlbumCoverHome>,
    val day_care_album_covers: List<DayCareAlbumCoverHome>

)

data class AlbumCoverHome(

    val branch_id: String,
    val date: String,
    val description: String,
    val id: String,
    val image: String,
    val title: String

)

data class DayCareAlbumCoverHome(

    val branch_id: String,
    val date: String,
    val description: String,
    val id: String,
    val image: String,
    val title: String

)