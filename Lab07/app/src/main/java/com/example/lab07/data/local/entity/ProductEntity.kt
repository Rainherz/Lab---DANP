package com.example.lab07.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.example.lab07.domain.model.Product

@Entity(
    tableName = "products",
    foreignKeys = [
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["id"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["categoryId"])]
)
data class ProductEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val price: Double,
    val description: String,
    val imageUri: String,
    val categoryId: Long,
    val stock: Int = 10,
    val isFavorite: Boolean = false
) {
    fun toDomain(): Product = Product(
        id = id,
        name = name,
        price = price,
        description = description,
        imageUri = imageUri,
        categoryId = categoryId,
        stock = stock,
        isFavorite = isFavorite
    )

    companion object {
        fun fromDomain(product: Product): ProductEntity = ProductEntity(
            id = product.id,
            name = product.name,
            price = product.price,
            description = product.description,
            imageUri = product.imageUri,
            categoryId = product.categoryId,
            stock = product.stock,
            isFavorite = product.isFavorite
        )
    }
}
