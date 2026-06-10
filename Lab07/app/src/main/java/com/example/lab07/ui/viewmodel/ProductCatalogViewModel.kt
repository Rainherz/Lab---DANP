package com.example.lab07.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.lab07.domain.model.Category
import com.example.lab07.domain.model.Product
import com.example.lab07.domain.repository.ProductRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class SortOption {
    NAME_ASC,
    NAME_DESC,
    PRICE_ASC,
    PRICE_DESC,
    STOCK_ASC,
    STOCK_DESC
}

data class CartItem(
    val product: Product,
    val quantity: Int
) {
    val totalPrice: Double get() = product.price * quantity
}

@OptIn(ExperimentalCoroutinesApi::class)
class ProductCatalogViewModel(private val repository: ProductRepository) : ViewModel() {

    // Categories
    val categories: StateFlow<List<Category>> = repository.getAllCategories()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Selected Category ID for filtering. null means "All"
    private val _selectedCategoryId = MutableStateFlow<Long?>(null)
    val selectedCategoryId = _selectedCategoryId.asStateFlow()

    // Search query
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    // Favorites filter
    private val _showOnlyFavorites = MutableStateFlow(false)
    val showOnlyFavorites = _showOnlyFavorites.asStateFlow()

    // Sort option
    private val _sortOption = MutableStateFlow(SortOption.NAME_ASC)
    val sortOption = _sortOption.asStateFlow()

    // Cart items state: map of productId -> quantity
    private val _cart = MutableStateFlow<Map<Long, Int>>(emptyMap())
    val cart = _cart.asStateFlow()

    // Expose cart items combined with product data
    val cartItems: StateFlow<List<CartItem>> = combine(
        _cart,
        repository.getAllProducts()
    ) { cartMap, allProducts ->
        cartMap.mapNotNull { (productId, qty) ->
            val product = allProducts.find { it.id == productId }
            if (product != null) {
                CartItem(product = product, quantity = qty)
            } else {
                null
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Products flow combining search, category filter, favorites and sorting
    val products: StateFlow<List<Product>> = combine(
        _selectedCategoryId,
        _searchQuery,
        _showOnlyFavorites,
        _sortOption
    ) { categoryId, query, showFavorites, sortOpt ->
        CombinedFilter(categoryId, query, showFavorites, sortOpt)
    }.flatMapLatest { filter ->
        val baseFlow = if (filter.query.isNotBlank()) {
            repository.searchProducts(filter.query).map { list ->
                if (filter.categoryId != null) {
                    list.filter { it.categoryId == filter.categoryId }
                } else {
                    list
                }
            }
        } else if (filter.categoryId != null) {
            repository.getProductsByCategory(filter.categoryId)
        } else {
            repository.getAllProducts()
        }

        baseFlow.map { list ->
            // Filter by favorites if active
            val filtered = if (filter.showFavorites) {
                list.filter { it.isFavorite }
            } else {
                list
            }

            // Apply sorting
            when (filter.sortOpt) {
                SortOption.NAME_ASC -> filtered.sortedBy { it.name.lowercase() }
                SortOption.NAME_DESC -> filtered.sortedByDescending { it.name.lowercase() }
                SortOption.PRICE_ASC -> filtered.sortedBy { it.price }
                SortOption.PRICE_DESC -> filtered.sortedByDescending { it.price }
                SortOption.STOCK_ASC -> filtered.sortedBy { it.stock }
                SortOption.STOCK_DESC -> filtered.sortedByDescending { it.stock }
            }
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private data class CombinedFilter(
        val categoryId: Long?,
        val query: String,
        val showFavorites: Boolean,
        val sortOpt: SortOption
    )

    fun selectCategory(categoryId: Long?) {
        _selectedCategoryId.value = categoryId
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun toggleFavoritesFilter() {
        _showOnlyFavorites.value = !_showOnlyFavorites.value
    }

    fun setSortOption(option: SortOption) {
        _sortOption.value = option
    }

    // Category CRUD operations
    fun addCategory(name: String) {
        viewModelScope.launch {
            repository.insertCategory(Category(name = name))
        }
    }

    fun updateCategory(category: Category) {
        viewModelScope.launch {
            repository.updateCategory(category)
        }
    }

    fun deleteCategory(category: Category) {
        viewModelScope.launch {
            repository.deleteCategory(category)
            if (_selectedCategoryId.value == category.id) {
                _selectedCategoryId.value = null
            }
        }
    }

    // Product CRUD operations
    fun addProduct(name: String, price: Double, description: String, imageUri: String, categoryId: Long, stock: Int = 10) {
        viewModelScope.launch {
            repository.insertProduct(
                Product(
                    name = name,
                    price = price,
                    description = description,
                    imageUri = imageUri,
                    categoryId = categoryId,
                    stock = stock
                )
            )
        }
    }

    fun updateProduct(product: Product) {
        viewModelScope.launch {
            repository.updateProduct(product)
        }
    }

    fun toggleFavorite(product: Product) {
        viewModelScope.launch {
            repository.updateProduct(product.copy(isFavorite = !product.isFavorite))
        }
    }

    fun updateStock(product: Product, newStock: Int) {
        if (newStock < 0) return
        viewModelScope.launch {
            repository.updateProduct(product.copy(stock = newStock))
        }
    }

    fun deleteProduct(product: Product) {
        viewModelScope.launch {
            repository.deleteProduct(product)
            // Remove from cart if deleted
            if (_cart.value.containsKey(product.id)) {
                val updated = _cart.value.toMutableMap()
                updated.remove(product.id)
                _cart.value = updated
            }
        }
    }

    suspend fun getProductById(id: Long): Product? {
        return repository.getProductById(id)
    }

    // Cart operations
    fun addToCart(product: Product) {
        val currentQty = _cart.value[product.id] ?: 0
        if (currentQty >= product.stock) return
        val updated = _cart.value.toMutableMap()
        updated[product.id] = currentQty + 1
        _cart.value = updated
    }

    fun removeFromCart(product: Product) {
        val currentQty = _cart.value[product.id] ?: return
        val updated = _cart.value.toMutableMap()
        if (currentQty <= 1) {
            updated.remove(product.id)
        } else {
            updated[product.id] = currentQty - 1
        }
        _cart.value = updated
    }

    fun clearCart() {
        _cart.value = emptyMap()
    }

    fun checkoutCart() {
        viewModelScope.launch {
            val allProductsList = repository.getAllProducts().first()
            _cart.value.forEach { (productId, quantity) ->
                val product = allProductsList.find { it.id == productId }
                if (product != null) {
                    val newStock = (product.stock - quantity).coerceAtLeast(0)
                    repository.updateProduct(product.copy(stock = newStock))
                }
            }
            _cart.value = emptyMap()
        }
    }

    class Factory(private val repository: ProductRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProductCatalogViewModel::class.java)) {
                return ProductCatalogViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
