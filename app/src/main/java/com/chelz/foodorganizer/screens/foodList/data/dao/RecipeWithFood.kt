package com.chelz.foodorganizer.screens.foodList.data.dao

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.chelz.foodorganizer.screens.recipes.data.dao.RecipeEntity

data class RecipeWithFood(
	@Embedded val recipe: RecipeEntity,
	@Relation(
		parentColumn = "recipeId",
		entityColumn = "foodId",
		associateBy = Junction(FoodRecipeJunctionEntity::class)
	)
	val food: List<FoodEntity>
)