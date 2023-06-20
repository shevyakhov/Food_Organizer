package com.chelz.foodorganizer.screens.recipes.data

import com.chelz.foodorganizer.screens.foodList.data.dao.FoodRecipeJunctionEntity
import com.chelz.foodorganizer.screens.foodList.data.dao.RecipeWithFood
import com.chelz.foodorganizer.screens.recipes.data.dao.RecipeEntity
import com.chelz.foodorganizer.screens.recipes.data.dao.RecipeTypeEntity
import com.chelz.foodorganizer.screens.recipes.data.dao.RecipesDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class LocalRecipesDataSourceDefault(private val recipeDao: RecipesDao) : LocalRecipesDataSource {

	override suspend fun getAllRecipes(): List<RecipeEntity> =
		withContext(Dispatchers.IO) {
			recipeDao.getAllRecipes()
		}

	override suspend fun getRecipeById(id: Int): RecipeEntity? =
		withContext(Dispatchers.IO) {
			recipeDao.getRecipeById(id)
		}

	override suspend fun insertRecipe(recipe: RecipeEntity) =
		withContext(Dispatchers.IO) {
			recipeDao.insertRecipe(recipe)
		}

	override suspend fun insertRecipes(recipes: List<RecipeEntity>) =
		withContext(Dispatchers.IO) {
			recipeDao.insertRecipes(recipes)
		}

	override suspend fun updateRecipe(recipe: RecipeEntity) =
		withContext(Dispatchers.IO) {
			recipeDao.updateRecipe(recipe)
		}

	override suspend fun deleteRecipe(recipe: RecipeEntity) =
		withContext(Dispatchers.IO) {
			recipeDao.deleteRecipe(recipe)
		}

	override suspend fun deleteAllRecipes() =
		withContext(Dispatchers.IO) {
			recipeDao.deleteAllRecipes()
		}

	override suspend fun getAllRecipeTypes(): List<RecipeTypeEntity> =
		withContext(Dispatchers.IO) {
			recipeDao.getAllRecipeTypes()
		}

	override suspend fun getRecipeTypeById(id: Int): RecipeTypeEntity? =
		withContext(Dispatchers.IO) {
			recipeDao.getRecipeTypeById(id)
		}

	override suspend fun insertRecipeType(recipeType: RecipeTypeEntity): Int =
		withContext(Dispatchers.IO) {
			recipeDao.insertRecipeType(recipeType).toInt()
		}

	override suspend fun insertRecipeTypes(recipeTypes: List<RecipeTypeEntity>) =
		withContext(Dispatchers.IO) {
			recipeDao.insertRecipeTypes(recipeTypes)
		}

	override suspend fun updateRecipeType(recipeType: RecipeTypeEntity) =
		withContext(Dispatchers.IO) {
			recipeDao.updateRecipeType(recipeType)
		}

	override suspend fun deleteRecipeType(recipeType: RecipeTypeEntity) =
		withContext(Dispatchers.IO) {
			recipeDao.deleteRecipeType(recipeType)
		}

	override suspend fun deleteAllRecipeTypes() =
		withContext(Dispatchers.IO) {
			recipeDao.deleteAllRecipeTypes()
		}

	override suspend fun addFoodWithRecipe(foodRecipeJunctionEntity: FoodRecipeJunctionEntity) {
		withContext(Dispatchers.IO) {
			recipeDao.addFoodWithRecipe(foodRecipeJunctionEntity)
		}
	}

	override suspend fun getRecipeWithFood(id: Int): List<RecipeWithFood> =
		withContext(Dispatchers.IO) {
			recipeDao.getRecipeWithFood(id)
		}

	override suspend fun deleteFoodRecipeJunctionEntity(recipeId: Int, foodId: Int) {
		withContext(Dispatchers.IO) {
			recipeDao.deleteFoodRecipeJunctionEntity(recipeId, foodId)
		}
	}

	override suspend fun getFoodRecipeJunctionEntity(id: Int): List<FoodRecipeJunctionEntity> =
		withContext(Dispatchers.IO) {
			recipeDao.getFoodRecipeJunctionEntity(id)
		}
}