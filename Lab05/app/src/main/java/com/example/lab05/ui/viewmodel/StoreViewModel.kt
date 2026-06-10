package com.example.lab05.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.lab05.data.repository.CartRepository
import com.example.lab05.data.repository.FavoriteRepository
import com.example.lab05.data.repository.StoreRepository
import com.example.lab05.model.CartItem
import com.example.lab05.model.Product
import com.example.lab05.ui.theme.AppThemeMode
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class StoreViewModel(application: Application) : AndroidViewModel(application) {
    private val storeRepository = StoreRepository()
    private val favoriteRepository = FavoriteRepository(application)
    private val cartRepository = CartRepository(application)

    // Theme state
    private val _themeMode = MutableStateFlow(AppThemeMode.BLUE)
    val themeMode: StateFlow<AppThemeMode> = _themeMode.asStateFlow()

    private val _isDarkMode = MutableStateFlow(false)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    // Search and Category State
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    // Categories with counts list
    val categories: List<Pair<String, Int>> = storeRepository.getCategories().map { category ->
        category to storeRepository.getProductCountForCategory(category)
    }

    // Filtered products list with Debounce
    @OptIn(FlowPreview::class)
    val products: StateFlow<List<Product>> = combine(
        _searchQuery.debounce(200).onStart { emit("") },
        _selectedCategory
    ) { query, category ->
        storeRepository.getProducts().filter { product ->
            val matchesCategory = category == null || product.category == category
            val matchesQuery = query.isEmpty() ||
                    product.name.contains(query, ignoreCase = true) ||
                    product.description.contains(query, ignoreCase = true)
            matchesCategory && matchesQuery
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = storeRepository.getProducts()
    )

    // Favorites State
    private val _favoriteIds = MutableStateFlow<Set<Int>>(favoriteRepository.getFavoriteIds())
    val favorites: StateFlow<List<Product>> = _favoriteIds.map { ids ->
        storeRepository.getProducts().filter { ids.contains(it.id) }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Cart State
    private val _cartItemsMap = MutableStateFlow<Map<Int, Int>>(cartRepository.getCartItems())
    val cartItems: StateFlow<List<CartItem>> = _cartItemsMap.map { map ->
        map.mapNotNull { entry ->
            val product = storeRepository.getProductById(entry.key)
            if (product != null) CartItem(product, entry.value) else null
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    // Cart calculations
    val cartSubtotal: StateFlow<Double> = cartItems.map { items ->
        items.sumOf { it.product.price * it.quantity }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val cartTotal: StateFlow<Double> = cartSubtotal.map { subtotal ->
        if (subtotal > 0) subtotal + 15.0 else 0.0 // Flat rate shipping of $15.0
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0.0)

    val cartCount: StateFlow<Int> = cartItems.map { items ->
        items.sumOf { it.quantity }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Theme Management
    fun setThemeMode(theme: AppThemeMode) {
        _themeMode.value = theme
    }

    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
    }

    // Search/Filters Management
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectCategory(category: String?) {
        _selectedCategory.value = category
    }

    // Favorites Management
    fun toggleFavorite(productId: Int) {
        val currentIds = favoriteRepository.getFavoriteIds().toMutableSet()
        if (currentIds.contains(productId)) {
            favoriteRepository.removeFavorite(productId)
        } else {
            favoriteRepository.addFavorite(productId)
        }
        _favoriteIds.value = favoriteRepository.getFavoriteIds()
    }

    fun isFavorite(productId: Int): Boolean {
        return _favoriteIds.value.contains(productId)
    }

    // Cart Management
    fun addToCart(productId: Int, quantity: Int = 1) {
        cartRepository.addToCart(productId, quantity)
        _cartItemsMap.value = cartRepository.getCartItems()
    }

    fun updateCartQuantity(productId: Int, quantity: Int) {
        cartRepository.updateQuantity(productId, quantity)
        _cartItemsMap.value = cartRepository.getCartItems()
    }

    fun removeFromCart(productId: Int) {
        cartRepository.removeFromCart(productId)
        _cartItemsMap.value = cartRepository.getCartItems()
    }

    fun clearCart() {
        cartRepository.clearCart()
        _cartItemsMap.value = emptyMap()
    }
}
