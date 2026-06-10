package com.example.lab05.data.repository

import android.content.Context
import android.content.SharedPreferences

class CartRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("store_cart_prefs", Context.MODE_PRIVATE)

    fun getCartItems(): Map<Int, Int> {
        val serialized = prefs.getString("cart_items", "") ?: ""
        if (serialized.isEmpty()) return emptyMap()
        val map = mutableMapOf<Int, Int>()
        serialized.split(",").forEach { pairStr ->
            val parts = pairStr.split(":")
            if (parts.size == 2) {
                val id = parts[0].toIntOrNull()
                val qty = parts[1].toIntOrNull()
                if (id != null && qty != null) {
                    map[id] = qty
                }
            }
        }
        return map
    }

    fun saveCartItems(cart: Map<Int, Int>) {
        val serialized = cart.entries.joinToString(",") { "${it.key}:${it.value}" }
        prefs.edit().putString("cart_items", serialized).apply()
    }

    fun addToCart(productId: Int, quantity: Int = 1) {
        val cart = getCartItems().toMutableMap()
        val currentQty = cart[productId] ?: 0
        cart[productId] = currentQty + quantity
        saveCartItems(cart)
    }

    fun updateQuantity(productId: Int, quantity: Int) {
        val cart = getCartItems().toMutableMap()
        if (quantity <= 0) {
            cart.remove(productId)
        } else {
            cart[productId] = quantity
        }
        saveCartItems(cart)
    }

    fun removeFromCart(productId: Int) {
        val cart = getCartItems().toMutableMap()
        cart.remove(productId)
        saveCartItems(cart)
    }

    fun clearCart() {
        prefs.edit().remove("cart_items").apply()
    }
}
