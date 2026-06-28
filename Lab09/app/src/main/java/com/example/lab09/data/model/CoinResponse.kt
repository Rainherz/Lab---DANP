package com.example.lab09.data.model

import com.google.gson.annotations.SerializedName

data class CoinResponse(
    val id: String,
    val symbol: String,
    val name: String,
    val image: String,
    @SerializedName("current_price")
    val currentPrice: Double?,
    @SerializedName("market_cap")
    val marketCap: Double?,
    @SerializedName("market_cap_rank")
    val marketCapRank: Int?,
    @SerializedName("total_volume")
    val totalVolume: Double?,
    @SerializedName("price_change_percentage_24h")
    val priceChangePercentage24h: Double?,
    @SerializedName("circulating_supply")
    val circulatingSupply: Double?
)
