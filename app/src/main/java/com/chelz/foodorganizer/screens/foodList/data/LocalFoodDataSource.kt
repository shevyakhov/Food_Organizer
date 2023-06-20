package com.chelz.foodorganizer.screens.foodList.data

import com.chelz.foodorganizer.screens.foodList.data.dao.FoodEntity
import com.chelz.foodorganizer.screens.foodList.data.dao.PlacementEntity
import com.chelz.foodorganizer.screens.foodList.data.dao.PlacementWithFood
import kotlinx.coroutines.flow.Flow

interface LocalFoodDataSource {

	suspend fun getFood(): List<FoodEntity>

	suspend fun getPlacementWithFood(): List<PlacementWithFood>

	suspend fun getFoodById(id: Int): FoodEntity

	suspend fun insertFood(foodEntity: FoodEntity):Long

	suspend fun editFood(foodEntity: FoodEntity)

	suspend fun deleteAllFood()

	suspend fun deleteFood(foodEntity: FoodEntity)

	suspend fun getFoodFlow(): Flow<List<FoodEntity>>

	suspend fun insertPlacement(placementEntity: PlacementEntity)

	suspend fun getPlacements(): List<PlacementEntity>

	suspend fun updatePlacement(placementEntity: PlacementEntity)

	suspend fun deletePlacement(placementEntity: PlacementEntity)

	suspend fun getPlacement(name: String): PlacementEntity

	suspend fun getPlacementById(id: Int): PlacementEntity

}