package com.chelz.foodorganizer.screens.toBuyList.domain

import com.chelz.foodorganizer.screens.foodList.data.LocalFoodDataSource
import com.chelz.foodorganizer.screens.foodList.data.dao.FoodEntity
import com.chelz.foodorganizer.screens.foodList.data.dao.PlacementEntity
import com.chelz.foodorganizer.screens.toBuyList.data.LocalToBuyListDataSource
import com.chelz.foodorganizer.screens.toBuyList.data.dao.ToBuyListEntity

class ToBuyListRepositoryDefault(private val localToBuyListDataSource: LocalToBuyListDataSource, private val localFoodDataSource: LocalFoodDataSource) :
	ToBuyListRepository {

	override suspend fun getAllToBuyLists(): List<ToBuyListEntity> =
		localToBuyListDataSource.getAllToBuyLists()

	override suspend fun insertToBuyList(toBuyList: ToBuyListEntity) {
		localToBuyListDataSource.insertToBuyList(toBuyList)
	}

	override suspend fun updateToBuyList(toBuyList: ToBuyListEntity) {
		localToBuyListDataSource.updateToBuyList(toBuyList)
	}

	override suspend fun deleteToBuyList(toBuyList: ToBuyListEntity) {
		localToBuyListDataSource.deleteToBuyList(toBuyList)
	}

	override suspend fun deleteAllToBuyLists() {
		localToBuyListDataSource.deleteAll()
	}

	override suspend fun getAllFood(): List<FoodEntity> =
		localFoodDataSource.getFood()

	override suspend fun insertFood(foodEntity: FoodEntity) {
		localFoodDataSource.insertFood(foodEntity)
	}

	override suspend fun getAllPlacements(): List<PlacementEntity> =
		localFoodDataSource.getPlacements()

}