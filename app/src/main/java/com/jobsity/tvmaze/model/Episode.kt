package com.jobsity.tvmaze.model

data class Episode(
    val id: Long,
    val name: String,
    val season: Int,
    val number: Int,
    val summary: String,
    val image: Image?,
)

