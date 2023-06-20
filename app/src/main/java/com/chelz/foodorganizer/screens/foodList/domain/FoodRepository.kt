package com.chelz.foodorganizer.screens.foodList.domain

import com.chelz.foodorganizer.screens.foodList.data.dao.FoodEntity
import com.chelz.foodorganizer.screens.foodList.data.dao.PlacementEntity
import com.chelz.foodorganizer.screens.foodList.data.dao.PlacementWithFood
import com.chelz.foodorganizer.screens.foodList.data.dao.StatisticsEntity
import kotlinx.coroutines.flow.Flow

interface FoodRepository {

	suspend fun insertFood(food: FoodEntity): Long
	suspend fun getFood(): List<FoodEntity>
	suspend fun getPlacementWithFood(): List<PlacementWithFood>
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

	suspend fun insertStatistics(statistics: StatisticsEntity)
	suspend fun updateStatistics(statistics: StatisticsEntity)
	suspend fun getByDateStatistics(date: String): StatisticsEntity?

}