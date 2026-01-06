package com.iprism.school.model.plannersandresources

import com.iprism.school.model.classteachermodel.Pagination

data class PlannersAndResourcesApiResponse(

    val message: String,
    val response: PlannersAndResourcesResponse,
    val status: Boolean

)

data class PlannersAndResourcesResponse(

    val categories: List<Category>,
    val pagination: Pagination,
    val pdfs: List<Pdf>,
    val planners: List<Planner>

)

data class Category(

    val id: String,
    val name: String

)

data class Pdf(

    val id: String,
    val image: String

)

data class Planner(

    val date: String,
    val planners: List<PlannerInner>

)

data class PlannerInner(

    val category: String,
    val created_date: String,
    val created_on: String,
    val description: String,
    val id: String,
    val sub_category: String,
    val subject: String

)