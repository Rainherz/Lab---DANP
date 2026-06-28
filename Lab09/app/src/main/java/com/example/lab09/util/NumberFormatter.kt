package com.example.lab09.util

import java.util.Locale

object NumberFormatter {

    fun formatPrice(price: Double): String {
        return when {
            price >= 1 -> String.format(Locale.US, "$%,.2f", price)
            price >= 0.01 -> String.format(Locale.US, "$%,.4f", price)
            else -> String.format(Locale.US, "$%,.6f", price)
        }
    }

    fun formatPercentage(change: Double): String {
        return String.format(Locale.US, "%+.2f%%", change)
    }

    fun formatMarketCap(marketCap: Double): String {
        return when {
            marketCap >= 1_000_000_000_000 -> String.format(Locale.US, "$%.2fT", marketCap / 1_000_000_000_000)
            marketCap >= 1_000_000_000 -> String.format(Locale.US, "$%.2fB", marketCap / 1_000_000_000)
            marketCap >= 1_000_000 -> String.format(Locale.US, "$%.2fM", marketCap / 1_000_000)
            marketCap >= 1_000 -> String.format(Locale.US, "$%.2fK", marketCap / 1_000)
            else -> String.format(Locale.US, "$%.2f", marketCap)
        }
    }

    fun formatVolume(volume: Double): String {
        return formatMarketCap(volume)
    }
}
