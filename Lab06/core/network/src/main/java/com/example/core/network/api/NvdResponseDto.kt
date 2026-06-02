package com.example.core.network.api

import com.google.gson.annotations.SerializedName

data class NvdResponseDto(
    @SerializedName("vulnerabilities") val vulnerabilities: List<NvdVulnerabilityContainerDto>?
)

data class NvdVulnerabilityContainerDto(
    @SerializedName("cve") val cve: NvdCveDto?
)

data class NvdCveDto(
    @SerializedName("id") val id: String?,
    @SerializedName("descriptions") val descriptions: List<NvdDescriptionDto>?,
    @SerializedName("metrics") val metrics: NvdMetricsDto?,
    @SerializedName("published") val published: String?,
    @SerializedName("references") val references: List<NvdReferenceDto>?
)

data class NvdReferenceDto(
    @SerializedName("url") val url: String?,
    @SerializedName("tags") val tags: List<String>?
)


data class NvdDescriptionDto(
    @SerializedName("lang") val lang: String?,
    @SerializedName("value") val value: String?
)

data class NvdMetricsDto(
    @SerializedName("cvssMetricV31") val cvssMetricV31: List<CvssMetricV31Dto>?
)

data class CvssMetricV31Dto(
    @SerializedName("cvssData") val cvssData: CvssDataDto?
)

data class CvssDataDto(
    @SerializedName("baseScore") val baseScore: Double?,
    @SerializedName("baseSeverity") val baseSeverity: String?
)
