package com.example.lab07.domain.repository

import com.example.lab07.domain.model.Category
import com.example.lab07.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    // Products
    fun getAllProducts(): Flow<List<Product>>
    fun getProductsByCategory(categoryId: Long): Flow<List<Product>>
    fun searchProducts(query: String): Flow<List<Product>>
    suspend fun getProductById(id: Long): Product?
    suspend fun insertProduct(product: Product): Long
    suspend fun updateProduct(product: Product)
    suspend fun deleteProduct(product: Product)

    // Categories
    fun getAllCategories(): Flow<List<Category>>
    suspend fun getCategoryById(id: Long): Category?
    suspend fun insertCategory(category: Category): Long
    suspend fun updateCategory(category: Category)
    suspend fun deleteCategory(category: Category)
}
