package com.chelz.foodorganizer.screens.profile.presentation.domain

import com.chelz.foodorganizer.screens.foodList.domain.LocalFoodDataSource
import com.chelz.foodorganizer.screens.foodList.domain.LocalStatisticsDataSource
import com.chelz.foodorganizer.screens.foodList.data.dao.FoodEntity
import com.chelz.foodorganizer.screens.foodList.data.dao.PlacementEntity
import com.chelz.foodorganizer.screens.foodList.data.dao.StatisticsEntity
import com.chelz.foodorganizer.screens.recipes.domain.LocalRecipesDataSource
import com.chelz.foodorganizer.screens.recipes.data.dao.RecipeEntity
import com.chelz.foodorganizer.screens.recipes.data.dao.RecipeTypeEntity
import com.chelz.foodorganizer.screens.toBuyList.domain.LocalToBuyListDataSource
import com.chelz.foodorganizer.screens.toBuyList.data.dao.ToBuyListEntity
import kotlinx.coroutines.flow.Flow

class ProfileRepositoryDefault(
	private val foodDataSource: LocalFoodDataSource,
	private val recipesDataSource: LocalRecipesDataSource,
	private val toBuyListDataSource: LocalToBuyListDataSource,
	private val statisticsDataSource: LocalStatisticsDataSource,
) : ProfileRepository {

	override suspend fun getFood(): List<FoodEntity> = foodDataSource.getFood()

	override suspend fun getFoodById(id: Int): FoodEntity =
		foodDataSource.getFoodById(id)

	override suspend fun updateFood(food: FoodEntity) {
		foodDataSource.editFood(food)
	}

	override suspend fun deleteFood(foodEntity: FoodEntity) {
		foodDataSource.deleteFood(foodEntity)
	}

	override suspend fun insert(statistics: StatisticsEntity) {
		statisticsDataSource.insert(statistics)
	}

	override suspend fun update(statistics: StatisticsEntity) {
		statisticsDataSource.update(statistics)
	}

	override suspend fun getAll(): List<StatisticsEntity> =
		statisticsDataSource.getAll()

	override suspend fun getByDate(date: String): StatisticsEntity? =
		statisticsDataSource.getByDate(date)

	override suspend fun delete(statistics: StatisticsEntity) {
		statisticsDataSource.delete(statistics)
	}

	override suspend fun insertFood(food: FoodEntity) {
		foodDataSource.insertFood(food)
	}

	override suspend fun deleteAllFood() {
		foodDataSource.deleteAllFood()
	}

	override suspend fun getFoodFlow(): Flow<List<FoodEntity>> = foodDataSource.getFoodFlow()

	override suspend fun insertPlacement(placementEntity: PlacementEntity) {
		foodDataSource.insertPlacement(placementEntity)
	}

	override suspend fun getPlacements(): List<PlacementEntity> =
		foodDataSource.getPlacements()

	override suspend fun getPlacement(name: String): PlacementEntity =
		foodDataSource.getPlacement(name)

	override suspend fun getPlacementById(id: Int): PlacementEntity =
		foodDataSource.getPlacementById(id)

	override suspend fun updatePlacement(placementEntity: PlacementEntity) {
		foodDataSource.updatePlacement(placementEntity)
	}

	override suspend fun deletePlacement(placementEntity: PlacementEntity) {
		foodDataSource.deletePlacement(placementEntity)
	}

	override suspend fun getAllRecipes(): List<RecipeEntity> {
		return recipesDataSource.getAllRecipes()
	}

	override suspend fun getRecipeById(id: Int): RecipeEntity? {
		return recipesDataSource.getRecipeById(id)
	}

	override suspend fun insertRecipe(recipe: RecipeEntity) {
		recipesDataSource.insertRecipe(recipe)
	}

	override suspend fun insertRecipes(recipes: List<RecipeEntity>) {
		recipesDataSource.insertRecipes(recipes)
	}

	override suspend fun updateRecipe(recipe: RecipeEntity) {
		recipesDataSource.updateRecipe(recipe)
	}

	override suspend fun deleteRecipe(recipe: RecipeEntity) {
		recipesDataSource.deleteRecipe(recipe)
	}

	override suspend fun deleteAllRecipes() {
		recipesDataSource.deleteAllRecipes()
	}

	override suspend fun getAllRecipeTypes(): List<RecipeTypeEntity> {
		return recipesDataSource.getAllRecipeTypes()
	}

	override suspend fun getRecipeTypeById(id: Int): RecipeTypeEntity? {
		return recipesDataSource.getRecipeTypeById(id)
	}

	override suspend fun insertRecipeType(recipeType: RecipeTypeEntity): Int =
		recipesDataSource.insertRecipeType(recipeType)

	override suspend fun insertRecipeTypes(recipeTypes: List<RecipeTypeEntity>) {
		recipesDataSource.insertRecipeTypes(recipeTypes)
	}

	override suspend fun updateRecipeType(recipeType: RecipeTypeEntity) {
		recipesDataSource.updateRecipeType(recipeType)
	}

	override suspend fun deleteRecipeType(recipeType: RecipeTypeEntity) {
		recipesDataSource.deleteRecipeType(recipeType)
	}

	override suspend fun deleteAllRecipeTypes() {
		recipesDataSource.deleteAllRecipeTypes()
	}

	override suspend fun getAllToBuyLists(): List<ToBuyListEntity> =
		toBuyListDataSource.getAllToBuyLists()

	override suspend fun insertToBuyList(toBuyList: ToBuyListEntity) {
		toBuyListDataSource.insertToBuyList(toBuyList)
	}

	override suspend fun updateToBuyList(toBuyList: ToBuyListEntity) {
		toBuyListDataSource.updateToBuyList(toBuyList)
	}

	override suspend fun deleteToBuyList(toBuyList: ToBuyListEntity) {
		toBuyListDataSource.deleteToBuyList(toBuyList)
	}

	override suspend fun deleteAllToBuyLists() {
		toBuyListDataSource.deleteAll()
	}

}