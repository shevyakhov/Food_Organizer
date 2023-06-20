package com.chelz.foodorganizer.screens.recipes.domain

import com.chelz.foodorganizer.screens.foodList.data.dao.FoodRecipeJunctionEntity
import com.chelz.foodorganizer.screens.foodList.data.dao.RecipeWithFood
import com.chelz.foodorganizer.screens.recipes.data.LocalRecipesDataSource
import com.chelz.foodorganizer.screens.recipes.data.dao.RecipeEntity
import com.chelz.foodorganizer.screens.recipes.data.dao.RecipeTypeEntity

class RecipesRepositoryDefault(private val localDataSource: LocalRecipesDataSource) : RecipesRepository {

	override suspend fun getAllRecipes(): List<RecipeEntity> {
		return localDataSource.getAllRecipes()
	}

	override suspend fun getRecipeById(id: Int): RecipeEntity? {
		return localDataSource.getRecipeById(id)
	}

	override suspend fun insertRecipe(recipe: RecipeEntity) {
		localDataSource.insertRecipe(recipe)
	}

	override suspend fun insertRecipes(recipes: List<RecipeEntity>) {
		localDataSource.insertRecipes(recipes)
	}

	override suspend fun updateRecipe(recipe: RecipeEntity) {
		localDataSource.updateRecipe(recipe)
	}

	override suspend fun deleteRecipe(recipe: RecipeEntity) {
		localDataSource.deleteRecipe(recipe)
	}

	override suspend fun deleteAllRecipes() {
		localDataSource.deleteAllRecipes()
	}

	override suspend fun getAllRecipeTypes(): List<RecipeTypeEntity> {
		return localDataSource.getAllRecipeTypes()
	}

	override suspend fun getRecipeTypeById(id: Int): RecipeTypeEntity? {
		return localDataSource.getRecipeTypeById(id)
	}

	override suspend fun insertRecipeType(recipeType: RecipeTypeEntity): Int =
		localDataSource.insertRecipeType(recipeType)

	override suspend fun insertRecipeTypes(recipeTypes: List<RecipeTypeEntity>) {
		localDataSource.insertRecipeTypes(recipeTypes)
	}

	override suspend fun updateRecipeType(recipeType: RecipeTypeEntity) {
		localDataSource.updateRecipeType(recipeType)
	}

	override suspend fun deleteRecipeType(recipeType: RecipeTypeEntity) {
		localDataSource.deleteRecipeType(recipeType)
	}

	override suspend fun deleteAllRecipeTypes() {
		localDataSource.deleteAllRecipeTypes()
	}

	override suspend fun addFoodWithRecipe(foodRecipeJunctionEntity: FoodRecipeJunctionEntity) {
		localDataSource.addFoodWithRecipe(foodRecipeJunctionEntity)
	}

	override suspend fun getRecipeWithFood(id: Int): List<RecipeWithFood> =
		localDataSource.getRecipeWithFood(id)

	override suspend fun deleteFoodRecipeJunctionEntity(recipeId: Int, foodId: Int) {
		localDataSource.deleteFoodRecipeJunctionEntity(recipeId, foodId)
	}

	override suspend fun getFoodRecipeJunctionEntity(recipeId: Int): List<FoodRecipeJunctionEntity> =
		localDataSource.getFoodRecipeJunctionEntity(recipeId)
}