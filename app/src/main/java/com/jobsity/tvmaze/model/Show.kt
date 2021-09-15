package com.jobsity.tvmaze.model

data class Show(
    val id: Long,
    val name: String,
    val summary: String,
    val image: Image,
    val schedule: Schedule,
    val genres: List<String>
)

