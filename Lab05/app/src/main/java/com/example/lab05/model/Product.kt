package com.example.lab05.model

data class Product(
    val id: Int,
    val name: String,
    val description: String,
    val price: Double,
    val category: String,
    val rating: Float = 4.5f,
    val reviewsCount: Int = 24,
    val stock: Int = 10,
    val features: List<String> = emptyList(),
    val imageType: String = "laptop"
)

data class CartItem(
    val product: Product,
    val quantity: Int
)