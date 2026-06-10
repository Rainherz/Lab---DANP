package com.example.lab07.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.lab07.domain.model.Category
import com.example.lab07.domain.model.Product
import com.example.lab07.ui.viewmodel.ProductCatalogViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductAddEditScreen(
    viewModel: ProductCatalogViewModel,
    productId: Long,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val categories by viewModel.categories.collectAsState()

    var name by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var stock by remember { mutableStateOf("10") }
    var description by remember { mutableStateOf("") }
    var selectedImageUri by remember { mutableStateOf("📦") }
    var selectedCategoryId by remember { mutableStateOf<Long?>(null) }

    var isEditMode by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }

    // Errors
    var nameError by remember { mutableStateOf(false) }
    var priceError by remember { mutableStateOf(false) }
    var stockError by remember { mutableStateOf(false) }
    var categoryError by remember { mutableStateOf(false) }

    val emojis = listOf(
        "📦", "📱", "💻", "⌚", "🎧", "📷", "🎮", "👕",
        "👟", "🎒", "📚", "🎨", "⚽", "🚗", "🍕", "☕"
    )

    var expandedDropdown by remember { mutableStateOf(false) }

    LaunchedEffect(productId) {
        if (productId > 0) {
            isEditMode = true
            isLoading = true
            val product = viewModel.getProductById(productId)
            if (product != null) {
                name = product.name
                price = product.price.toString()
                stock = product.stock.toString()
                description = product.description
                selectedImageUri = product.imageUri
                selectedCategoryId = product.categoryId
            }
            isLoading = false
        } else {
            if (categories.isNotEmpty() && selectedCategoryId == null) {
                selectedCategoryId = categories.first().id
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEditMode) "Editar Producto" else "Nuevo Producto") },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atrás")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            // Validations
                            nameError = name.trim().isEmpty()
                            
                            val parsedPrice = price.toDoubleOrNull()
                            priceError = parsedPrice == null || parsedPrice <= 0
                            
                            val parsedStock = stock.toIntOrNull()
                            stockError = parsedStock == null || parsedStock < 0
                            
                            categoryError = selectedCategoryId == null

                            if (!nameError && !priceError && !stockError && !categoryError) {
                                val catId = selectedCategoryId!!
                                val prc = parsedPrice!!
                                val stk = parsedStock!!
                                if (isEditMode) {
                                    viewModel.updateProduct(
                                        Product(
                                            id = productId,
                                            name = name.trim(),
                                            price = prc,
                                            description = description.trim(),
                                            imageUri = selectedImageUri,
                                            categoryId = catId,
                                            stock = stk
                                        )
                                    )
                                } else {
                                    viewModel.addProduct(
                                        name = name.trim(),
                                        price = prc,
                                        description = description.trim(),
                                        imageUri = selectedImageUri,
                                        categoryId = catId,
                                        stock = stk
                                    )
                                }
                                onNavigateBack()
                            }
                        }
                    ) {
                        Icon(Icons.Default.Check, contentDescription = "Guardar")
                    }
                }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Icon selector
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(selectedImageUri, fontSize = 48.sp)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Selecciona un Icono",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        emojis.take(8).forEach { emoji ->
                            EmojiButton(
                                emoji = emoji,
                                isSelected = selectedImageUri == emoji,
                                onClick = { selectedImageUri = emoji }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        emojis.drop(8).forEach { emoji ->
                            EmojiButton(
                                emoji = emoji,
                                isSelected = selectedImageUri == emoji,
                                onClick = { selectedImageUri = emoji }
                            )
                        }
                    }
                }

                HorizontalDivider()

                // Name Input
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        nameError = false
                    },
                    label = { Text("Nombre del producto") },
                    singleLine = true,
                    isError = nameError,
                    supportingText = {
                        if (nameError) Text("El nombre no puede estar vacío")
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                // Row for Price and Stock
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Price Input
                    OutlinedTextField(
                        value = price,
                        onValueChange = {
                            price = it
                            priceError = false
                        },
                        label = { Text("Precio ($)") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = priceError,
                        supportingText = {
                            if (priceError) Text("Precio > 0")
                        },
                        modifier = Modifier.weight(1f)
                    )

                    // Stock Input
                    OutlinedTextField(
                        value = stock,
                        onValueChange = {
                            stock = it
                            stockError = false
                        },
                        label = { Text("Stock / Cantidad") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = stockError,
                        supportingText = {
                            if (stockError) Text("Stock >= 0")
                        },
                        modifier = Modifier.weight(1f)
                    )
                }

                // Category Dropdown
                Box(modifier = Modifier.fillMaxWidth()) {
                    val selectedCategoryName = categories.find { it.id == selectedCategoryId }?.name ?: "Seleccionar Categoría"
                    OutlinedTextField(
                        value = selectedCategoryName,
                        onValueChange = {},
                        label = { Text("Categoría") },
                        readOnly = true,
                        isError = categoryError,
                        supportingText = {
                            if (categoryError) Text("Debe seleccionar una categoría")
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDropdown)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { expandedDropdown = !expandedDropdown }
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .clickable { expandedDropdown = !expandedDropdown }
                    )

                    DropdownMenu(
                        expanded = expandedDropdown,
                        onDismissRequest = { expandedDropdown = false },
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        categories.forEach { category ->
                            DropdownMenuItem(
                                text = { Text(category.name) },
                                onClick = {
                                    selectedCategoryId = category.id
                                    expandedDropdown = false
                                    categoryError = false
                                }
                            )
                        }
                    }
                }

                // Description Input
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Descripción") },
                    minLines = 3,
                    maxLines = 5,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = {
                        nameError = name.trim().isEmpty()
                        val parsedPrice = price.toDoubleOrNull()
                        priceError = parsedPrice == null || parsedPrice <= 0
                        val parsedStock = stock.toIntOrNull()
                        stockError = parsedStock == null || parsedStock < 0
                        categoryError = selectedCategoryId == null

                        if (!nameError && !priceError && !stockError && !categoryError) {
                            val catId = selectedCategoryId!!
                            val prc = parsedPrice!!
                            val stk = parsedStock!!
                            if (isEditMode) {
                                viewModel.updateProduct(
                                    Product(
                                        id = productId,
                                        name = name.trim(),
                                        price = prc,
                                        description = description.trim(),
                                        imageUri = selectedImageUri,
                                        categoryId = catId,
                                        stock = stk
                                    )
                                )
                            } else {
                                viewModel.addProduct(
                                    name = name.trim(),
                                    price = prc,
                                    description = description.trim(),
                                    imageUri = selectedImageUri,
                                    categoryId = catId,
                                    stock = stk
                                )
                            }
                            onNavigateBack()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(if (isEditMode) "Actualizar Producto" else "Guardar Producto")
                }
            }
        }
    }
}

@Composable
fun EmojiButton(
    emoji: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(36.dp)
            .clip(CircleShape)
            .background(
                if (isSelected) MaterialTheme.colorScheme.primary
                else Color.Transparent
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(emoji, fontSize = 20.sp)
    }
}

