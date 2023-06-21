package com.chelz.foodorganizer.screens.foodList.domain

import com.chelz.foodorganizer.screens.foodList.data.dao.FoodEntity
import com.chelz.foodorganizer.screens.foodList.data.dao.PlacementEntity
import com.chelz.foodorganizer.screens.foodList.data.dao.StatisticsEntity
import kotlinx.coroutines.flow.Flow

class FoodRepositoryDefault(
	private val localFoodDataSource: LocalFoodDataSource,
	private val localStatisticsDataSource: LocalStatisticsDataSource,
) : FoodRepository {

	override suspend fun getFood(): List<FoodEntity> = localFoodDataSource.getFood()

	override suspend fun getPlacementWithFood() =localFoodDataSource.getPlacementWithFood()
	override suspend fun getFoodById(id: Int): FoodEntity =
		localFoodDataSource.getFoodById(id)

	override suspend fun updateFood(food: FoodEntity) {
		localFoodDataSource.editFood(food)
	}

	override suspend fun deleteFood(foodEntity: FoodEntity) {
		localFoodDataSource.deleteFood(foodEntity)
	}

	override suspend fun insertFood(food: FoodEntity) =
		localFoodDataSource.insertFood(food)


	override suspend fun deleteAllFood() {
		localFoodDataSource.deleteAllFood()
	}

	override suspend fun getFoodFlow(): Flow<List<FoodEntity>> = localFoodDataSource.getFoodFlow()

	override suspend fun insertPlacement(placementEntity: PlacementEntity) {
		localFoodDataSource.insertPlacement(placementEntity)
	}

	override suspend fun getPlacements(): List<PlacementEntity> =
		localFoodDataSource.getPlacements()

	override suspend fun getPlacement(name: String): PlacementEntity =
		localFoodDataSource.getPlacement(name)

	override suspend fun getPlacementById(id: Int): PlacementEntity =
		localFoodDataSource.getPlacementById(id)

	override suspend fun updatePlacement(placementEntity: PlacementEntity) {
		localFoodDataSource.updatePlacement(placementEntity)
	}

	override suspend fun deletePlacement(placementEntity: PlacementEntity) {
		localFoodDataSource.insertPlacement(placementEntity)
	}

	override suspend fun insertStatistics(statistics: StatisticsEntity) {
		localStatisticsDataSource.insert(statistics)
	}

	override suspend fun updateStatistics(statistics: StatisticsEntity) {
		localStatisticsDataSource.update(statistics)
	}

	override suspend fun getByDateStatistics(date: String): StatisticsEntity? =
		localStatisticsDataSource.getByDate(date)
}