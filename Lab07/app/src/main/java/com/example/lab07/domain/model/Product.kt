package com.example.lab07.domain.model

data class Product(
    val id: Long = 0,
    val name: String,
    val price: Double,
    val description: String,
    val imageUri: String,
    val categoryId: Long,
    val stock: Int = 10,
    val isFavorite: Boolean = false
)
