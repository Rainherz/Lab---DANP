package com.example.lab07.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.lab07.domain.model.Category

@Entity(tableName = "categories")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String
) {
    fun toDomain(): Category = Category(id = id, name = name)

    companion object {
        fun fromDomain(category: Category): CategoryEntity =
            CategoryEntity(id = category.id, name = category.name)
    }
}
