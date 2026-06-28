package com.example.lab09.data.model

import com.google.gson.annotations.SerializedName

data class CoinDetailResponse(
    val id: String,
    val name: String,
    val description: Description?,
    val image: ImageInfo?,
    @SerializedName("market_data")
    val marketData: MarketData?,
    val links: Links?
)

data class Description(val en: String?)

data class ImageInfo(val large: String?)

data class MarketData(
    @SerializedName("current_price")
    val currentPrice: Map<String, Double>?,
    @SerializedName("market_cap")
    val marketCap: Map<String, Double>?,
    @SerializedName("price_change_percentage_24h")
    val priceChangePercentage24h: Double?,
    @SerializedName("ath")
    val ath: Map<String, Double>?,
    @SerializedName("ath_date")
    val athDate: Map<String, String>?,
    @SerializedName("circulating_supply")
    val circulatingSupply: Double?,
    @SerializedName("total_supply")
    val totalSupply: Double?,
    @SerializedName("max_supply")
    val maxSupply: Double?
)

data class Links(val homepage: List<String?>?)
