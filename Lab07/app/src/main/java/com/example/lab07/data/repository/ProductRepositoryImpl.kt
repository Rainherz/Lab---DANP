package com.example.lab07.data.repository

import com.example.lab07.data.local.dao.CategoryDao
import com.example.lab07.data.local.dao.ProductDao
import com.example.lab07.data.local.entity.CategoryEntity
import com.example.lab07.data.local.entity.ProductEntity
import com.example.lab07.domain.model.Category
import com.example.lab07.domain.model.Product
import com.example.lab07.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ProductRepositoryImpl(
    private val productDao: ProductDao,
    private val categoryDao: CategoryDao
) : ProductRepository {

    // Products
    override fun getAllProducts(): Flow<List<Product>> {
        return productDao.getAllProducts().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getProductsByCategory(categoryId: Long): Flow<List<Product>> {
        return productDao.getProductsByCategory(categoryId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun searchProducts(query: String): Flow<List<Product>> {
        return productDao.searchProducts(query).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getProductById(id: Long): Product? {
        return productDao.getProductById(id)?.toDomain()
    }

    override suspend fun insertProduct(product: Product): Long {
        return productDao.insertProduct(ProductEntity.fromDomain(product))
    }

    override suspend fun updateProduct(product: Product) {
        productDao.updateProduct(ProductEntity.fromDomain(product))
    }

    override suspend fun deleteProduct(product: Product) {
        productDao.deleteProduct(ProductEntity.fromDomain(product))
    }

    // Categories
    override fun getAllCategories(): Flow<List<Category>> {
        return categoryDao.getAllCategories().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getCategoryById(id: Long): Category? {
        return categoryDao.getCategoryById(id)?.toDomain()
    }

    override suspend fun insertCategory(category: Category): Long {
        return categoryDao.insertCategory(CategoryEntity.fromDomain(category))
    }

    override suspend fun updateCategory(category: Category) {
        categoryDao.updateCategory(CategoryEntity.fromDomain(category))
    }

    override suspend fun deleteCategory(category: Category) {
        categoryDao.deleteCategory(CategoryEntity.fromDomain(category))
    }
}
