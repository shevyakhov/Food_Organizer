package com.chelz.foodorganizer.screens.profile.presentation.domain

import com.chelz.foodorganizer.screens.foodList.data.dao.FoodEntity
import com.chelz.foodorganizer.screens.foodList.data.dao.PlacementEntity
import com.chelz.foodorganizer.screens.foodList.data.dao.StatisticsEntity
import com.chelz.foodorganizer.screens.recipes.data.dao.RecipeEntity
import com.chelz.foodorganizer.screens.recipes.data.dao.RecipeTypeEntity
import com.chelz.foodorganizer.screens.toBuyList.data.dao.ToBuyListEntity
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {

	suspend fun insert(statistics: StatisticsEntity)
	suspend fun update(statistics: StatisticsEntity)
	suspend fun getAll(): List<StatisticsEntity>
	suspend fun getByDate(date: String): StatisticsEntity?
	suspend fun delete(statistics: StatisticsEntity)

	suspend fun insertFood(food: FoodEntity)
	suspend fun getFood(): List<FoodEntity>
	suspend fun getFoodById(id: Int): FoodEntity
	suspend fun updateFood(food: FoodEntity)
	suspend fun deleteFood(foodEntity: FoodEntity)
	suspend fun deleteAllFood()
	suspend fun getFoodFlow(): Flow<List<FoodEntity>>

	suspend fun insertPlacement(placementEntity: PlacementEntity)
	suspend fun getPlacements(): List<PlacementEntity>
	suspend fun getPlacement(name: String): PlacementEntity
	suspend fun getPlacementById(id: Int): PlacementEntity
	suspend fun updatePlacement(placementEntity: PlacementEntity)
	suspend fun deletePlacement(placementEntity: PlacementEntity)

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

	suspend fun getAllToBuyLists(): List<ToBuyListEntity>
	suspend fun insertToBuyList(toBuyList: ToBuyListEntity)
	suspend fun updateToBuyList(toBuyList: ToBuyListEntity)
	suspend fun deleteToBuyList(toBuyList: ToBuyListEntity)
	suspend fun deleteAllToBuyLists()
}