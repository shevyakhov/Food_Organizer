package com.chelz.foodorganizer.screens.recipes.data.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "RecipeTypeTable")
data class RecipeTypeEntity(
	@PrimaryKey(autoGenerate = true) val id: Int? = null,
	@ColumnInfo(name = "typeName") val typeName: String,
)



