package com.chelz.foodorganizer.screens.recipes.domain

import com.chelz.foodorganizer.screens.foodList.data.dao.FoodRecipeJunctionEntity
import com.chelz.foodorganizer.screens.foodList.data.dao.RecipeWithFood
import com.chelz.foodorganizer.screens.recipes.data.dao.RecipeEntity
import com.chelz.foodorganizer.screens.recipes.data.dao.RecipeTypeEntity

interface LocalRecipesDataSource {

	suspend fun getAllRecipes(): List<RecipeEntity>
	suspend fun getRecipeById(id: Int): RecipeEntity?
	suspend fun insertRecipe(recipe: RecipeEntity)
	suspend fun insertRecipes(recipes: List<RecipeEntity>)
	suspend fun updateRecipe(recipe: RecipeEntity)
	suspend fun deleteRecipe(recipe: RecipeEntity)

	suspend fun deleteAllRecipes()
	suspend fun getAllRecipeTypes(): List<RecipeTypeEntity>
	suspend fun getRecipeTypeById(id: Int): RecipeTypeEntity?
	suspend fun insertRecipeType(recipeType: RecipeTypeEntity): Int
	suspend fun insertRecipeTypes(recipeTypes: List<RecipeTypeEntity>)
	suspend fun updateRecipeType(recipeType: RecipeTypeEntity)
	suspend fun deleteRecipeType(recipeType: RecipeTypeEntity)
	suspend fun deleteAllRecipeTypes()

	suspend fun addFoodWithRecipe(foodRecipeJunctionEntity: FoodRecipeJunctionEntity)
	suspend fun getRecipeWithFood(id: Int): List<RecipeWithFood>
	suspend fun deleteFoodRecipeJunctionEntity(recipeId: Int, foodId: Int)
	suspend fun getFoodRecipeJunctionEntity(id: Int): List<FoodRecipeJunctionEntity>
}