package com.chelz.foodorganizer.screens.toBuyList.domain

import com.chelz.foodorganizer.screens.foodList.data.dao.FoodEntity
import com.chelz.foodorganizer.screens.foodList.data.dao.PlacementEntity
import com.chelz.foodorganizer.screens.toBuyList.data.dao.ToBuyListEntity

interface ToBuyListRepository {

	suspend fun getAllToBuyLists(): List<ToBuyListEntity>
	suspend fun insertToBuyList(toBuyList: ToBuyListEntity)
	suspend fun updateToBuyList(toBuyList: ToBuyListEntity)
	suspend fun deleteToBuyList(toBuyList: ToBuyListEntity)
	suspend fun deleteAllToBuyLists()

	suspend fun getAllFood(): List<FoodEntity>
	suspend fun insertFood(foodEntity: FoodEntity)

	suspend fun getAllPlacements(): List<PlacementEntity>
}