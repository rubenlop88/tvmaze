package com.jobsity.tvmaze.api

import com.google.gson.annotations.SerializedName

data class Show(
    @field:SerializedName("id") val id: Long,
    @field:SerializedName("url") val url: String,
    @field:SerializedName("name") val name: String,
    @field:SerializedName("image") val image: Image
)

data class Image(
    @field:SerializedName("medium") val medium: String,
    @field:SerializedName("original") val original: String,
)