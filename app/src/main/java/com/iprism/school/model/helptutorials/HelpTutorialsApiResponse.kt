package com.iprism.school.model.helptutorials

import com.iprism.school.model.classteachermodel.Pagination

data class HelpTutorialsApiResponse(

    val message: String,
    val response: HelpTutorialsResponse,
    val status: Boolean

)

data class HelpTutorialsResponse(

    val help_tutorials: List<HelpTutorial>,
    val pagination: Pagination

)

data class HelpTutorial(

    val description: String,
    val id: String,
    val image: String,
    val link: String,
    val title: String

)
