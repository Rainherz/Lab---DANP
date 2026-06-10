package com.tuapp.cineapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class MovieVideosResponseDto(
    val id: Int,
    val results: List<VideoDto>
)

data class VideoDto(
    val id: String,
    val key: String,
    val name: String,
    val site: String,
    val type: String,
    val official: Boolean
)
