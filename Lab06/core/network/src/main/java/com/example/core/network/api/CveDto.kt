package com.example.core.network.api

import com.google.gson.annotations.SerializedName

data class CveDto(
    @SerializedName("id") val id: String,
    @SerializedName("cveId") val cveId: String,
    @SerializedName("severity") val severity: String,
    @SerializedName("description") val description: String,
    @SerializedName("date") val date: String,
    @SerializedName("affectedTechnology") val affectedTechnology: String
)
