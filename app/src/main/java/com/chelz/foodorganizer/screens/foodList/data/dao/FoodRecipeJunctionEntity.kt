package com.chelz.foodorganizer.screens.foodList.data.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import com.chelz.foodorganizer.screens.recipes.data.dao.RecipeEntity

@Entity(tableName = "FoodRecipeJunctionTable", primaryKeys = ["foodId", "recipeId"])
data class FoodRecipeJunctionEntity(
	@ColumnInfo(name = "foodId") val foodId: Int,
	@ColumnInfo(name = "recipeId") val recipeId: Int,
)