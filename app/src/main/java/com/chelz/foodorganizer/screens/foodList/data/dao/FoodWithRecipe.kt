package com.chelz.foodorganizer.screens.foodList.data.dao

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.chelz.foodorganizer.screens.recipes.data.dao.RecipeEntity

data class FoodWithRecipe(
    @Embedded val food: FoodEntity,
    @Relation(
        parentColumn = "foodId",
        entityColumn = "recipeId",
        associateBy = Junction(FoodRecipeJunctionEntity::class)
    )
    val recipes: List<RecipeEntity>,
)
