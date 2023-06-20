package com.chelz.foodorganizer.screens.foodList.data

import com.chelz.foodorganizer.screens.foodList.data.dao.FoodDao
import com.chelz.foodorganizer.screens.foodList.data.dao.FoodEntity
import com.chelz.foodorganizer.screens.foodList.data.dao.PlacementEntity
import com.chelz.foodorganizer.screens.foodList.data.dao.PlacementWithFood
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class LocalFoodDataSourceDefault(private val dao: FoodDao) : LocalFoodDataSource {

	override suspend fun getFood(): List<FoodEntity> =
		withContext(Dispatchers.IO) {
			dao.getFood()
		}
	override suspend fun getPlacementWithFood(): List<PlacementWithFood> =
		withContext(Dispatchers.IO) {
			dao.getPlacementWithFood()
		}

	override suspend fun getFoodById(id: Int): FoodEntity =
		withContext(Dispatchers.IO) {
			dao.getFoodById(id)
		}

	override suspend fun insertFood(foodEntity: FoodEntity) =
		withContext(Dispatchers.IO) {
			dao.insertFood(foodEntity)
		}


	override suspend fun editFood(foodEntity: FoodEntity) {
		withContext(Dispatchers.IO) {
			dao.updateFood(foodEntity)
		}
	}

	override suspend fun deleteAllFood() {
		withContext(Dispatchers.IO) {
			dao.deleteAllFood()
		}
	}

	override suspend fun deleteFood(foodEntity: FoodEntity) {
		withContext(Dispatchers.IO) {
			dao.deleteFood(foodEntity)
		}
	}

	override suspend fun getFoodFlow(): Flow<List<FoodEntity>> =
		withContext(Dispatchers.IO) {
			dao.getFoodFlow()
		}

	override suspend fun insertPlacement(placementEntity: PlacementEntity) {
		withContext(Dispatchers.IO) {
			dao.insertPlacement(placementEntity)
		}
	}

	override suspend fun getPlacements() =
		withContext(Dispatchers.IO) {
			dao.getPlacements()
		}

	override suspend fun updatePlacement(placementEntity: PlacementEntity) {
		withContext(Dispatchers.IO) {
			dao.updatePlacement(placementEntity)
		}
	}

	override suspend fun deletePlacement(placementEntity: PlacementEntity) {
		withContext(Dispatchers.IO) {
			dao.deletePlacement(placementEntity)
		}
	}

	override suspend fun getPlacement(name: String): PlacementEntity =
		withContext(Dispatchers.IO) {
			dao.getPlacement(name)
		}

	override suspend fun getPlacementById(id: Int): PlacementEntity =
		withContext(Dispatchers.IO) {
			dao.getPlacementById(id)
		}
}